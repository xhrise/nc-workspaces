/*
 * 创建日期 2005-1-6
 */
package com.ufsoft.report;

import com.ufida.dataset.IContext;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsAuthorization;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.IVerify.VerifyType;


/**
 * 报表单元的数据权限管理。
 * @author wupeng
 * @version 3.1
 */
public class ReportAuth implements CellsAuthorization{

	private CellsPane cellsPane;
	
	private IContext context;
	/**
	 * @param report
	 */
	public ReportAuth(CellsPane cellsPane,IContext context) {
		super();
		this.cellsPane = cellsPane;
		this.context = context;
	}
	/**
	 * @see com.ufsoft.table.CellsAuthorization#isReadable(int, int)
	 */
	public boolean isReadable(int row, int col) {
		return true;
	}
	/**
	 * @see com.ufsoft.table.CellsAuthorization#isWritable(int, int)
	 */
	public boolean isWritable(int row, int col) {
		if(isFormatOperationState()){
			return true;
		}else if(isInputOperationState()){//数据状态
			Cell cell = cellsPane.getCell(row,col);
			if(cell!=null&& cellsPane.getDataModel().isVerify(
				CellPosition.getInstance(row,col), VerifyType.WRITABLE_AT_INPUTSTAT,false)){
				return true;
			}else{
			    return false;
			}
		}else{
			return false;
		   // throw new IllegalArgumentException();
		}
	}
	
	protected boolean isFormatOperationState(){
		return (Integer)context.getAttribute(ReportContextKey.OPERATION_STATE)==UfoReport.OPERATION_FORMAT;
	}
	
	protected boolean isInputOperationState(){
		return (Integer)context.getAttribute(ReportContextKey.OPERATION_STATE)==UfoReport.OPERATION_INPUT;
	}
	
	protected CellsPane getCellsPane(){
		return this.cellsPane;
	}

	/**
	 * @param row
	 * @param col
	 * @param type
	 * @see com.ufsoft.table.CellsAuthorization#authorize(int, int, int)
	 */
	public void authorize(int row, int col, int type) {		
	}
	protected IContext getContext() {
		return context;
	}
	
}