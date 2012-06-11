package com.ufsoft.report.sysplugin.cellattr;

import java.util.ArrayList;

import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.format.ConditionFormat;
import com.ufsoft.table.format.Format;
/**
 * ���õ�Ԫ��������ʽ
 * @author guogang
 *
 */
public class SetCellConditionCmd extends UfoCommand {
    //  ������
    private UfoReport m_RepTool = null;
    //ģ��
    private CellsModel m_cellsModel;
    /**
     * ���췽��
     * @param rep
     */
	public SetCellConditionCmd(UfoReport rep){
		 super();
	        if (rep == null) {
	            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000824"));//�Ƿ��ı����߲��� 
	        }
	        this.m_RepTool = rep;
	        m_cellsModel = m_RepTool.getCellsModel();
	}

    /**
     * @see UfoCommand.execute()
     */
	@SuppressWarnings("unchecked")
	public void execute(Object[] params) {
		if ((params == null ) || (params.length == 0))
            return; 
		if(m_cellsModel == null)
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000825"), getReport(), null);//ģ�Ͳ���Ϊ��
		ArrayList<ConditionFormat> result=null;
		if(params[1]!=null){
			if(params[8]!=null){
				result=(ArrayList<ConditionFormat>)params[8];
			}
			char scope = ((Character)params[1]).charValue();
			switch (scope) {
            case 't': //ȫ��  
            	setTableCondition((String)params[0],result);
                break;
            case 'r'://��
                int[] rows = (int[])params[2];
                if((rows == null) || (rows.length == 0)) return;                    
                setRowsCondition(rows,(String)params[0],result);
                break;
            case 'c'://��
                int[] cols = (int[])params[2];
                if((cols == null) || (cols.length == 0)) return;                   
                setColsCondition(cols, (String)params[0],result);
                break;
            case 'a'://����
                CellPosition[] cells = (CellPosition[])params[2];
                if((cells == null) || (cells.length == 0)) return;                  
                setCellsCondition(cells, (String)params[0],result);
                break;
            default:
                UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000826"), getReport(), null);//�������ԵĲ�������
            }   
		}
	}
	  /**
     * @return ����UfoReport��
     */
    public UfoReport getReport() {
        return m_RepTool;
    }
    /**
     * ����ȫ���������ʽ
     * @param name ������ʽ�ؼ���
     * @param condition ��������
     */
    public void setTableCondition(String name,ArrayList<ConditionFormat> condition){
    	CellsModel m_cellsModel = getCellsModel();
    	Format format = m_cellsModel.getTableFormat();
		if (format == null) {
			format = new IufoFormat();
		}
		format.setCondition(true);
		m_cellsModel.setTableFormat((IufoFormat) format);
		for (int i = 0; i < m_cellsModel.getRowNum(); i++) 
			setRowCondition(i,name,condition); 
		for (int j = 0; j < m_cellsModel.getColNum(); j++) 
			setColCondition(j,name,condition); 
		
    }
    /**
     * ���������������
     * @param targetRows Ҫ����������ʽ��������
     * @param name ������ʽ�ؼ���
     * @param condition ��������
     */
    public void setRowsCondition(int[] targetRows,String name,ArrayList<ConditionFormat> condition){
    	if(targetRows == null) return;
        int cellIndex = 0;   
       
        getCellsModel().putExtProp(name, condition);
        while (cellIndex < targetRows.length) {            
        	setRowCondition(targetRows[cellIndex],name,condition);
            cellIndex++;
        }
    }
    /**
     * ���õ��е�����
     * @param num �к�
     * @param name ������ʽ�ؼ���
     * @param condition ��������
     */
    public void setRowCondition(int num,String name,ArrayList<ConditionFormat> condition){
    	Format format=getCellsModel().getRowFormat(num);  
    	if (format == null) {
			format = new IufoFormat();
		}
    	format.setCondition(true);
    	getCellsModel().setRowFormat(num, format);
    }
    /**
     * ���������������
     * @param targetCols Ҫ����������ʽ���õ�������
     * @param name ������ʽ�ؼ���
     * @param condition ��������
     */
   public void setColsCondition(int[] targetCols,String name,ArrayList<ConditionFormat> condition){
	   if(targetCols == null) return;
       int cellIndex = 0;   
       
       getCellsModel().putExtProp(name, condition);
       while (cellIndex < targetCols.length) {            
    	   setColCondition(targetCols[cellIndex],name,condition);  
           cellIndex++;
       }
    }
   /**
    * ���õ��е�����
    * @param num �к�
    * @param name ������ʽ�ؼ���
    * @param condition ��������
    */
   public void setColCondition(int num,String name,ArrayList<ConditionFormat> condition){
   	Format format=getCellsModel().getColFormat(num);  
   	if (format == null) {
			format = new IufoFormat();
		}
   	format.setCondition(true);
   	getCellsModel().setColFormat(num, format);
   }
   /**
    * ���õ�Ԫ���������
    * @param cells ��Ԫλ������
    * @param name ������ʽ�ؼ���
    * @param condition ��������
    */
   public void setCellsCondition(CellPosition[] cells,String name,ArrayList<ConditionFormat> condition){
	   if(cells == null) return;
       int cellIndex = 0;  
       Format format = null;
       while (cellIndex < cells.length) {
			format = getCellsModel().getFormat(cells[cellIndex]);
			if (format == null) {
				format = new IufoFormat();
			}
			
			Cell cell = getCellsModel().getCell(cells[cellIndex]);
			if (cell == null) {
				cell = new Cell();
				cell.setRow(cells[cellIndex].getRow());
				cell.setCol(cells[cellIndex].getColumn());
				getCellsModel().setCell(cells[cellIndex].getRow(),
						cells[cellIndex].getColumn(), cell);
			}
			if (cell.getExtFmt(name) != null)
				cell.removeExtFmt(name);
			if(condition!=null&&!condition.isEmpty()){
			cell.addExtFmt(name, condition);
			format.setCondition(true);
			}else{
				format.setCondition(false);	
			}
			getCellsModel().setCellFormat(cell.getRow(), cell.getCol(), format);
			cellIndex++;
		}
   }

	protected CellsModel getCellsModel() {
		return m_cellsModel;
	}
}
