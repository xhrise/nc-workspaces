package com.ufsoft.report.sysplugin.headersize;

import java.awt.Component;
import java.util.HashSet;

import javax.swing.KeyStroke;

import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.AreaCommonOpr;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;

/**
 * 系统预制插件功能点：设置行高
 * @author caijie
 */
public class SetRowHeightExt extends AbsActionExt{// implements IMainMenuExt{
    private UfoReport m_report;//报表工具
    

    /**
	 * 构造函数
	 * @param rep - 报表
	 */
	public SetRowHeightExt(UfoReport rep){
		m_report = rep;
	}

    /* 
     * @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {
        
        return MultiLang.getString("row_height");//行高
    }

    /* 
     * @see com.ufsoft.report.plugin.ICommandExt#getHint()
     */
    public String getHint() {
        
        return MultiLang.getString("uiuforep0000876");//设置行高
    }

//    /* 
//     * @see com.ufsoft.report.plugin.ICommandExt#getMenuSlot()
//     */
//    public int getMenuSlot() {
//        
//        return ReportMenuBar.FORMAT_END;
//    }

    /* 
     * @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
     */
    public String getImageFile() {
        
        return "Rowheight.gif";
    }

    /* 
     * @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
     */
    public KeyStroke getAccelerator() {
    	return null;
    }

    /* 
     * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
     */
    public UfoCommand getCommand() {
        
        return new SetRowHeightCmd(this.getReport());
    }

    /** 
     * 得到三个参数
     * params[0]: a / p  a表示要设置所有行(包括将来产生的行) o表示只设置部分行
     * params[1]: Integer / int[] 如果设置全部行,以Integer列为代表,否则用int[]表示要设置的行
     * params[3]: Integer 要设置的行高
     */
    public Object[] getParams(UfoReport container) {      
        Object[] result = new Object[3];
        
        int initialColWidth = 0;  
        int[] selectedRow = null;        
        CellsModel cellModel = this.getReport().getCellsModel();
        
        //从SelectedModel获取当前选中的列（限通过行列头部选中的列）,
        //如果有选中的话,则将设置全表的行高        
        int[] selectedCol = cellModel.getSelectModel().getSelectedCol();        
        if ((selectedCol != null) && selectedCol.length > 0 ) {  
            int realcount = cellModel.getRowHeaderModel().getTempCount();
            for(int i = 0; i < realcount; i++){
                if(initialColWidth <= 0){
                    initialColWidth = this.getReport().getTable().getCells().getRowHeight(i);
                    break;
                }                    
            }                   
            result[0] = new Character('a');
            result[1] = new Integer(selectedCol[0]);             
        }else{
            // 从SelectedModel获取当前选中的行（限通过行列头部选中的行） 
            selectedRow = cellModel.getSelectModel().getSelectedRow();
            if ((selectedRow != null) && selectedRow.length > 0 ) {                
                for(int i = 0; i < selectedRow.length; i++){
                    if(initialColWidth <= 0){
                        initialColWidth = this.getReport().getTable().getCells().getRowHeight(selectedRow[i]);
                        break;
                    }
                        
                }
            }    
            // 从CellsModel获取当前选中的区域对应的行（通过行列头部选中的行列除外）
            AreaPosition[] selectedArea = cellModel.getSelectModel().getSelectedAreas();       
            if ((selectedArea != null) && selectedArea.length > 0 ) {
                HashSet areaSet = AreaCommonOpr.intArrayToHashSet(AreaCommonOpr.getRows(selectedArea));
                HashSet selectedRowSet = AreaCommonOpr.intArrayToHashSet(selectedRow);
                if (selectedRowSet == null ) selectedRowSet = new HashSet();
                selectedRowSet.addAll(areaSet);
                selectedRow = AreaCommonOpr.hashSetToIntArray(selectedRowSet);            
                for(int i = 0; i < selectedArea.length; i++){
                    if(initialColWidth <= 0){
                        initialColWidth = this.getReport().getTable().getCells().getRowHeight(selectedArea[i].getStart().getRow()); 
                        break;
                    }
                                             
                }
            }  
            if ((selectedRow == null) || (selectedRow.length < 1))
                return null; //没有选中任何行列或区域
            result[0] = new Character('p');
            result[1] = selectedRow;
        }      
        

        //用户设定行高值        
        SetRowHeightDlg dlg = new SetRowHeightDlg(initialColWidth,this.getReport().getFrame());
        dlg.setModal(true);
        dlg.show();

		if( dlg.getResult() == UfoDialog.ID_OK )
		{		    
		    if(dlg.getRowHeight() != initialColWidth){
		        result[2] = new Integer(dlg.getRowHeight());
		        return result;
		    }		       
		}
		
		return null;
    }
    /**
     * @return 返回UfoReport。
     */
    public UfoReport getReport() {
        return m_report;
    }

    /*
     * @see com.ufsoft.report.plugin.IMainMenuExt#getPath()
     */
    public String[] getPath() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return StateUtil.isCPane1THeader(m_report,focusComp);
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    /**
	 * @i18n format=格式
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("row_height"));
        uiDes.setPaths(new String[]{MultiLang.getString("format"),MultiLang.getString("Row")});
        uiDes.setGroup("headerSizeExt");
        return new ActionUIDes[]{uiDes};
    }
}
  