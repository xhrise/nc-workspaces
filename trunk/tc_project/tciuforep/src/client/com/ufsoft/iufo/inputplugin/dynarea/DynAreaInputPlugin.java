package com.ufsoft.iufo.inputplugin.dynarea;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.inputplugin.key.KeyFmt;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 
 * @author zzl 2005-6-20
 */
public class DynAreaInputPlugin extends AbstractPlugIn{

    /*
     * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
     */
    protected IPluginDescriptor createDescriptor() {
        return new DynAreaDes(this);
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
    	if (getReport().getTable()!=null)
    		getReport().getTable().getReanderAndEditor().registRender(RowNumber.class,new RowNumberRender());
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
    }


    /*
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        return false;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        return new SheetCellRenderer(){
			public Component getCellRendererComponent(CellsPane cellsPane,Object value, boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
				return null;
			}        	
        };
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        return null;
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        return null;
    }
    /**
     * 得到所有动态区域。
     * @return DynAreaCell[]
     */
    public DynAreaCell[] getDynAreaCells(){
        //暂时不做缓存，效率优化时再考虑。
        ArrayList list = new ArrayList();
        Iterator iter = getReport().getCellsModel().getAreaDatas().iterator();
        while (iter.hasNext()) {
            Object element = iter.next();
            if(element instanceof DynAreaCell){
                list.add(element);
            }
        }
        return (DynAreaCell[]) list.toArray(new DynAreaCell[0]);
    }
    
    public DynAreaCell getAnchorDynAreaCell(){
        CellPosition anchorPos = getReport().getCellsModel().getSelectModel().getAnchorCell();
        return getDynAreaCell(anchorPos);
    }
    
    public boolean isInDynArea(int row,int col){
        return !(getDynAreaCell(CellPosition.getInstance(row,col)) == null);
    }
    public DynAreaCell getDynAreaCell(CellPosition pos){
        DynAreaCell[] dynCells = getDynAreaCells();
        for (int i = 0; i < dynCells.length; i++) {
            AreaPosition area = dynCells[i].getArea();
            if(area.contain(pos)){
                return dynCells[i];
            }
        }
        return null;
    }
    public String getDynAreaPK(CellPosition pos){
        DynAreaCell cell = getDynAreaCell(pos);
        return cell == null? null : cell.getDynAreaVO().getDynamicAreaPK();
    }

	/**
	 * 得到初始位置
	 * @param selCellPos
	 * @return
	 */
	public CellPosition getOrigCellPos(CellPosition pos) {
        if(!isInDynArea(pos.getRow(),pos.getColumn())){
            return pos;
        }else{
            MeasureFmt measureFmt = (MeasureFmt) getCellsModel().getBsFormat(pos,MeasureFmt.EXT_FMT_MEASUREINPUT);
            KeyFmt keyFmt = (KeyFmt) getCellsModel().getBsFormat(pos,KeyFmt.EXT_FMT_KEYINPUT);
            if(measureFmt != null){
                return CellPosition.getInstance(measureFmt.getOriginRow(),measureFmt.getOriginCol());
            }else if(keyFmt != null){
                return CellPosition.getInstance(keyFmt.getOriginRow(),keyFmt.getOriginCol());
            }else{
                return pos;
            }
        }
	}
    /**
     * 得到动态区域中单元格所在组的动态区关键字信息.
     * @param pos
     * @return Hashtable
     */
    public Hashtable getUnitKeys(CellPosition pos){
        if(!isInDynArea(pos.getRow(),pos.getColumn())){
            return null;
        }else{
            AreaPosition area = getUnitArea(pos);
            ArrayList list = getCellsModel().seperateArea(area);
            Hashtable ht = new Hashtable();
            for(Iterator iter=list.iterator();iter.hasNext();){
                CellPosition cellPos = (CellPosition) iter.next();
                KeyFmt keyFmt = (KeyFmt) getCellsModel().getBsFormat(cellPos,KeyFmt.EXT_FMT_KEYINPUT);
                if(keyFmt != null){
                    ht.put(keyFmt,getCellsModel().getCell(cellPos).getValue());
                }
            }
            return ht;
        }
    }
    private AreaPosition getUnitArea(CellPosition pos) {
        DynAreaCell dynCell = getDynAreaCell(pos);
        if(dynCell == null){
            return null;
        }        
        AreaPosition[] unitAreas = dynCell.getUnitAreas();
        for(int i=0;i<unitAreas.length;i++){
            if(unitAreas[i].contain(pos)){
                return unitAreas[i];
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
    public boolean verifyBeforeSave() {
        DynAreaCell[] dynCells = getDynAreaCells();
        CellsModel cellsModel = getReport().getCellsModel(); 
        for(int i=0;i<dynCells.length;i++){//针对每一个动态区域
            DynAreaCell dynCell = dynCells[i];
            AreaPosition[] unitAreas = dynCell.getUnitAreas();
            Vector vecKeyComb = new Vector();
            for (int j = 0; j < unitAreas.length; j++) {//针对每一组数据
                if(isAllNull(unitAreas[j])){
                    continue;
                }
                String keyComb = "";
                ArrayList list = cellsModel.getSeperateCellPos(unitAreas[j]);
                for (Iterator iter = list.iterator(); iter.hasNext();) {//针对每一个单元格
                    CellPosition cellPos = (CellPosition) iter.next();
                    Cell cell = cellsModel.getCell(cellPos);
                    if(cell != null){
                        KeyFmt keyFmt = (KeyFmt) cell.getExtFmt(KeyFmt.EXT_FMT_KEYINPUT);
                        if(keyFmt != null){
                            Object value = cell.getValue();
                            if(value == null || "".equals(value.toString())){
                                JOptionPane.showMessageDialog(getReport(),
                                		MultiLangInput.getString("verify_dynarea_keyvalue_notnull"));
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
                		JOptionPane.showMessageDialog(getReport(),
                    		MultiLangInput.getString("verify_dynarea_keycomb_notduplicate"));
                    	return false;
                	}else{
                		vecKeyComb.add(keyComb);
                	}
                }
            }
        }
        return true;
    }
    private boolean isAllNull(AreaPosition areaPos) {
        ArrayList list = getCellsModel().getSeperateCellPos(areaPos);
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            CellPosition cellPos = (CellPosition) iter.next();
            Cell cell = getCellsModel().getCell(cellPos);
            if(cell != null && cell.getValue() != null && cell.getValue() instanceof Double 
                    && ((Double)cell.getValue()).doubleValue() == 0.0){//double值0.0也认为是空值
                continue;
            }else if(cell != null && cell.getValue() != null && !"".equals(cell.getValue().toString())){
                return false;
            }
        }
        return true;
    }
//    private boolean isInTimeExtent(String dynTime){
//        Hashtable htKeyFmt = getReport().getCellsModel().getBsFormats(KeyInputPlugin.EXT_FMT_KEYINPUT);
//        String mainTableTime = null;
//        Enumeration enumeration = htKeyFmt.keys();
//        //找主表时间关键字
//        while (enumeration.hasMoreElements()) {
//            CellPosition cellPos = (CellPosition) enumeration.nextElement();
//            if(!isInDynArea(cellPos.getRow(),cellPos.getColumn())
//               && ((KeyFmt)htKeyFmt.get(cellPos)).getType() == KeyFmt.TYPE_TIME){
//                Object value = getReport().getCellsModel().getCellValue(cellPos);
//                mainTableTime = value==null?null:value.toString();
//                break;
//            }            
//        }
//        if(mainTableTime == null){
//            return true;
//        }else{
//            int index = mainTableTime.lastIndexOf('-');
//            mainTableTime = mainTableTime.substring(0,index);
//            if(dynTime.startsWith(mainTableTime)){
//                return true;
//            }else{
//                JOptionPane.showMessageDialog(getReport(),
//                        MultiLang.getString(this,"verify_dynarea_timekeyvalue_inextent_maintable"));
//                return false;
//            }
//        }
//    }
    /**
     * 得到主表时间关键字信息。如果没有返回null。
     * @return Object[] 第一个元素为KeyFmt，第二个元素为value（String）。
     */
    public Object[] getMainTableTimeKey(){
        Hashtable htKeyFmt = getReport().getCellsModel().getBsFormats(KeyFmt.EXT_FMT_KEYINPUT);
        String mainTableTimeValue = null;
        KeyFmt mainTableTimeKey = null;
        Enumeration enumeration = htKeyFmt.keys();
        //找主表时间关键字
        while (enumeration.hasMoreElements()) {
            CellPosition cellPos = (CellPosition) enumeration.nextElement();
            if(!isInDynArea(cellPos.getRow(),cellPos.getColumn())
               && ((KeyFmt)htKeyFmt.get(cellPos)).getType() == KeyFmt.TYPE_TIME){
                Object value = getReport().getCellsModel().getCellValue(cellPos);
                mainTableTimeValue = value==null?null:value.toString();
                mainTableTimeKey = (KeyFmt)htKeyFmt.get(cellPos);
                break;
            }            
        }
        return new Object[]{mainTableTimeKey,mainTableTimeValue};
    }
}
