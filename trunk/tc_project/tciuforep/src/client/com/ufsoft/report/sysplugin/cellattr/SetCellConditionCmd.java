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
 * 设置单元的条件格式
 * @author guogang
 *
 */
public class SetCellConditionCmd extends UfoCommand {
    //  报表工具
    private UfoReport m_RepTool = null;
    //模型
    private CellsModel m_cellsModel;
    /**
     * 构造方法
     * @param rep
     */
	public SetCellConditionCmd(UfoReport rep){
		 super();
	        if (rep == null) {
	            throw new IllegalArgumentException(MultiLang.getString("uiuforep0000824"));//非法的报表工具参数 
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
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000825"), getReport(), null);//模型不能为空
		ArrayList<ConditionFormat> result=null;
		if(params[1]!=null){
			if(params[8]!=null){
				result=(ArrayList<ConditionFormat>)params[8];
			}
			char scope = ((Character)params[1]).charValue();
			switch (scope) {
            case 't': //全表  
            	setTableCondition((String)params[0],result);
                break;
            case 'r'://行
                int[] rows = (int[])params[2];
                if((rows == null) || (rows.length == 0)) return;                    
                setRowsCondition(rows,(String)params[0],result);
                break;
            case 'c'://列
                int[] cols = (int[])params[2];
                if((cols == null) || (cols.length == 0)) return;                   
                setColsCondition(cols, (String)params[0],result);
                break;
            case 'a'://区域
                CellPosition[] cells = (CellPosition[])params[2];
                if((cells == null) || (cells.length == 0)) return;                  
                setCellsCondition(cells, (String)params[0],result);
                break;
            default:
                UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000826"), getReport(), null);//设置属性的参数错误
            }   
		}
	}
	  /**
     * @return 返回UfoReport。
     */
    public UfoReport getReport() {
        return m_RepTool;
    }
    /**
     * 设置全表的条件格式
     * @param name 条件格式关键字
     * @param condition 条件内容
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
     * 设置行数组的条件
     * @param targetRows 要设置条件格式的行数组
     * @param name 条件格式关键字
     * @param condition 条件内容
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
     * 设置单行的条件
     * @param num 行号
     * @param name 条件格式关键字
     * @param condition 条件内容
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
     * 设置列数组的条件
     * @param targetCols 要进行条件格式设置的列数组
     * @param name 条件格式关键字
     * @param condition 条件内容
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
    * 设置单列的条件
    * @param num 列号
    * @param name 条件格式关键字
    * @param condition 条件内容
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
    * 设置单元数组的条件
    * @param cells 单元位置数组
    * @param name 条件格式关键字
    * @param condition 条件内容
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
