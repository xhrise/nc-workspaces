package com.ufsoft.iufo.inputplugin.ufodynarea;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.key.KeyFmt;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IAreaAtt;
import com.ufsoft.table.format.TableConstant;

public abstract class AbsUfoDynAreaActionExt extends AbstractPluginAction {
	
	public AbsUfoDynAreaActionExt(){
	}
	
	public void execute(ActionEvent e) {
		getCommand().execute(getParams());
	}

	public IPluginActionDescriptor getPluginActionDescriptor() {        
		PluginActionDescriptor pad = new PluginActionDescriptor(getMenuName());
		pad.setGroupPaths(new String[]{MultiLangInput.getString("miufotableinput0014")});
		pad.setAccelerator(getActionKeyStroke());
		pad.setMemonic(getMemonic());
		
		String strIcon=getIconName();
		if (strIcon==null)
			pad.setExtensionPoints(XPOINT.POPUPMENU);
		else{
			pad.setIcon(strIcon);
			pad.setExtensionPoints(XPOINT.POPUPMENU,XPOINT.TOOLBAR);
			
		}
		pad.setShowDialog(isShowDialog());
        
		return pad;
	}

	protected boolean isShowDialog(){
		return false;
	}

	public boolean isEnabled() {
    	Viewer curView=getCurrentView();
    	if (curView==null || curView instanceof ReportDesigner==false)
    		return false;
    	
    	ReportDesigner editor=(ReportDesigner)curView;
    	// @edit by wangyga at 2009-6-24,下午04:43:19
    	CellsModel cellsModel = editor.getCellsModel();
    	if(cellsModel == null){
    		return false;
    	}
    	if (editor instanceof RepDataEditor){
    		RepDataEditor repEditor=(RepDataEditor)editor;
    		if (repEditor.getMenuState()!=null && repEditor.getMenuState().isCommited())
    			return false;
    	}
        CellPosition anchorPos = cellsModel.getSelectModel().getAnchorCell();
        return isInDynArea(editor,anchorPos.getRow(),anchorPos.getColumn());
    }
	
	protected String getIconName(){
		return null;
	}

    protected int getInputCount(boolean bAddRow){
    	SelInsRowNumDlg dlg=new SelInsRowNumDlg(this.getMainboard(),bAddRow);
    	dlg.setVisible(true);
    	if (dlg.getResult()==UfoDialog.ID_OK){
    		return dlg.getInputNum();
    	}
    	return 0;
    }
    
    protected ReportDesigner getReportDesigner(){ 
    	return (ReportDesigner)getCurrentView();
    }
    
    /**
     * 得到所有动态区域。
     * @return DynAreaCell[]
     */
    private static DynAreaCell[] getDynAreaCells(ReportDesigner editor){ 
        //暂时不做缓存，效率优化时再考虑。
        ArrayList<DynAreaCell> list = new ArrayList<DynAreaCell>();
        Iterator<IAreaAtt> iter = editor.getCellsModel().getAreaDatas().iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if(element instanceof DynAreaCell){
                list.add((DynAreaCell)element);
            }
        }
        return (DynAreaCell[]) list.toArray(new DynAreaCell[0]);
    }
    
    protected DynAreaCell getAnchorDynAreaCell(ReportDesigner editor){
        CellPosition anchorPos = getReportDesigner().getCellsModel().getSelectModel().getAnchorCell();
        return getDynAreaCell(editor,anchorPos);
    }
    
    private boolean isInDynArea(ReportDesigner editor,int row,int col){
        return !(getDynAreaCell(editor,CellPosition.getInstance(row,col)) == null);
    }
    
    private static DynAreaCell getDynAreaCell( ReportDesigner editor,CellPosition pos){
        DynAreaCell[] dynCells = getDynAreaCells(editor);
        for (int i = 0; i < dynCells.length; i++) {
            AreaPosition area = dynCells[i].getArea();
            if(area.contain(pos)){
                return dynCells[i];
            }
        }
        return null;
    }

    /**
     * 关键字不完整验证
     * 关键字组合重复验证
     * 动态区时间关键字范围验证，只验证月和日的关系。
     * @return boolean
     */
    public static boolean verifyBeforeSave(ReportDesigner editor) {
        DynAreaCell[] dynCells = getDynAreaCells(editor);
        CellsModel cellsModel = editor.getCellsModel(); 
        DynAreaModel dynModel=DynAreaModel.getInstance(cellsModel);
        for(int i=0;i<dynCells.length;i++){//针对每一个动态区域
            DynAreaCell dynCell = dynCells[i];
            if (dynModel.isProcessedAndNotSortFilter(dynCells[i].getDynAreaPK()))
            	continue;
            
            AreaPosition[] unitAreas = dynCell.getUnitAreas();
            Vector<String> vecKeyComb = new Vector<String>();
            for (int j = 0; j < unitAreas.length; j++) {//针对每一组数据
                if(isAllNull(editor,unitAreas[j])){
                    continue;
                }
                String keyComb = "";
                ArrayList<CellPosition> list = cellsModel.getSeperateCellPos(unitAreas[j]);
                for (Iterator<CellPosition> iter = list.iterator(); iter.hasNext();) {//针对每一个单元格
                    CellPosition cellPos = (CellPosition) iter.next();
                    Cell cell = cellsModel.getCell(cellPos);
                    if(cell != null){
                        KeyFmt keyFmt = (KeyFmt) cell.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT);
                        if(keyFmt != null){
                            Object value = cell.getValue();
                            if(value == null || "".equals(value.toString())){
                                JOptionPane.showMessageDialog(editor.getMainboard(),MultiLangInput.getString("verify_dynarea_keyvalue_notnull"));
                                return false;
                            }else{
                                keyComb += value.toString();
                            }
                            //时间范围在结束编辑时控制，自动更新为范围内的值。这里不再控制。
//                            if(keyFmt.getType() == KeyFmt.TYPE_TIME){
//                                if(!isInTimeExtent(value.toString())){
//                                    return false;
//                                }
//                            }
                        }
                    }
                }
                if(!keyComb.equals("")){//本行无关键字（例如分组加出来的行），则不加入比较
                	if(vecKeyComb.contains(keyComb)){
                		JOptionPane.showMessageDialog(editor.getMainboard(),MultiLangInput.getString("verify_dynarea_keycomb_notduplicate"));
                    	return false;
                	}else{
                		vecKeyComb.add(keyComb);
                	}
                }
            }
        }
        return true;
    }
    
    protected KeyStroke getActionKeyStroke(){
    	return null;
    }

    private static boolean isAllNull(ReportDesigner editor,AreaPosition areaPos) {
        ArrayList<CellPosition> list = editor.getCellsModel().getSeperateCellPos(areaPos);
        for (Iterator<CellPosition> iter = list.iterator(); iter.hasNext();) {
            CellPosition cellPos = (CellPosition) iter.next();
            Cell cell = editor.getCellsModel().getCell(cellPos);
            if (cell==null)
            	continue;
            
            if(cell.getFormat()!=null && cell.getFormat().getCellType()==TableConstant.CELLTYPE_SAMPLE)
            	continue;
            
            if(cell != null && cell.getValue() != null && cell.getValue() instanceof Double 
                    && ((Double)cell.getValue()).doubleValue() == 0.0){//double值0.0也认为是空值
                continue;
            }else if(cell != null && cell.getValue() != null && !"".equals(cell.getValue().toString())){
                return false;
            }
        }
        return true;
    }
    
    abstract public UfoCommand getCommand();

    abstract public Object[] getParams();
    
    abstract protected String getMenuName();
    
    abstract protected int getMemonic();
}
