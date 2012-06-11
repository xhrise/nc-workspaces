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
 * ϵͳԤ�Ʋ�����ܵ㣺�����п�
 * @author caijie
 * @since 3.1
 */
public class SetColWidthExt extends AbsActionExt{// implements IMainMenuExt{
    private UfoReport m_report;//������
    
    /**
	 * ���캯��
	 * @param rep - ����
	 */
	public SetColWidthExt(UfoReport rep){
		m_report = rep;
	}
    /* 
     * @see com.ufsoft.report.plugin.ICommandExt#getName()
     */
    public String getName() {
        
        return MultiLang.getString("col_width");//�п�
    }

    /* 
     * @see com.ufsoft.report.plugin.ICommandExt#getHint()
     */
    public String getHint() {
        
        return MultiLang.getString("uiuforep0000869");//�����п�
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
        return "colwidth.gif";
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
        
        return new SetColWidthCmd(this.getReport());
    }

    /** 
     * �õ���������
     * params[0]: a / p  a��ʾҪ����������(����������������) o��ʾֻ���ò�����
     * params[1]: Integer / int[] �������ȫ����,��Integer��Ϊ����,������int[]��ʾҪ���õ���
     * params[3]: Integer Ҫ���õ��п�
     */
    public Object[] getParams(UfoReport container) {      
        Object[] result = new Object[3];
        
        int initialColWidth = 0;  
        int[] selectedCol = null;        
        CellsModel cellModel = this.getReport().getCellsModel();
        
        //��SelectedModel��ȡ��ǰѡ�е��У���ͨ������ͷ��ѡ�е��У�,
        //�����ѡ�еĻ�,������ȫ����п�        
        int[] selectedRow = cellModel.getSelectModel().getSelectedRow();        
        if ((selectedRow != null) && selectedRow.length > 0 ) {  
            int realcount = cellModel.getColumnHeaderModel().getTempCount();
            for(int i = 0; i < realcount; i++){
                if(initialColWidth <= 0){
                    initialColWidth = this.getReport().getTable().getCells().getColumnWidth(i);
                    break;
                }                    
            }                   
            result[0] = new Character('a');
            result[1] = new Integer(selectedRow[0]);  
            
        }else{
            // ��SelectedModel��ȡ��ǰѡ�е��У���ͨ������ͷ��ѡ�е��У� 
            selectedCol = cellModel.getSelectModel().getSelectedCol();
            if ((selectedCol != null) && selectedCol.length > 0 ) {
                
                for(int i = 0; i < selectedCol.length; i++){
                    if(initialColWidth <= 0){
                        initialColWidth = this.getReport().getTable().getCells().getColumnWidth(selectedCol[i]);
                        break;
                    }
                        
                }
            }    
            // ��CellsModel��ȡ��ǰѡ�е�����ͨ������ͷ��ѡ�е����г��⣩
            AreaPosition[] selectedArea = cellModel.getSelectModel().getSelectedAreas();       
            if ((selectedArea != null) && selectedArea.length > 0 ) {
                HashSet areaSet = AreaCommonOpr.intArrayToHashSet(AreaCommonOpr.getColumns(selectedArea));
                HashSet selectedColSet = AreaCommonOpr.intArrayToHashSet(selectedCol);
                if (selectedColSet == null ) selectedColSet = new HashSet();
                selectedColSet.addAll(areaSet);
                selectedCol = AreaCommonOpr.hashSetToIntArray(selectedColSet);            
                for(int i = 0; i < selectedArea.length; i++){
                    if(initialColWidth <= 0){
                        initialColWidth = this.getReport().getTable().getCells().getColumnWidth(selectedArea[i].getStart().getColumn()); 
                        break;
                    }
                                             
                }
            }  
            if ((selectedCol == null) || (selectedCol.length < 1))
                return null; //û��ѡ���κ����л�����
            result[0] = new Character('p');
            result[1] = selectedCol;
        }      
        

        //�û��趨�п�ֵ        
        SetColWidthDlg dlg = new SetColWidthDlg(initialColWidth,this.getReport());
        dlg.setModal(true);
        dlg.show();

		if( dlg.getResult() == UfoDialog.ID_OK )
		{		    
		    if(dlg.getColWidth() != initialColWidth){
		        result[2] = new Integer(dlg.getColWidth());
		        return result;
		    }		       
		}
		
		return null;
    }
    /**
     * @return ����UfoReport��
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
	 * @i18n format=��ʽ
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("col_width"));
        uiDes.setPaths(new String[]{MultiLang.getString("format"),MultiLang.getString("Column")});
        uiDes.setGroup("headerSizeExt");
        return new ActionUIDes[]{uiDes};
    }
}
  