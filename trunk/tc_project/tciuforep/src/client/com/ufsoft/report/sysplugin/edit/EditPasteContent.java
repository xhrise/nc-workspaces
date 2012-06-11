package com.ufsoft.report.sysplugin.edit;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>
 * 
 * 选择性粘贴：粘贴内容
 * 
 * @author 王宇光
 * @version Create on 2008-4-14
 */
public class EditPasteContent extends AbsChoosePaste{

	public EditPasteContent(UfoReport m_rep,boolean b_isTransfer){
		super(m_rep,b_isTransfer);
	}
	
	/**
	 * add by 王宇光 2008-3-23 获得粘贴类型
	 * 
	 * @param 
	 * @return 
	 */
	@Override
	protected String getPasteType() {
		if(isTransfer()){
        	return EditPasteExt.TRANSFER;
        }
		return EditPasteExt.CONTENT;
	}
	
	/**
	 * add by 王宇光 2008-3-23 具体的粘贴方法
	 * 
	 * @param Cell cell,int row,int column
	 * @return 
	 */
	@Override
	protected void paste(Cell cell,int row,int column){
		CellsModel cellsModel = getDataModel();	
		pasteValue(cellsModel, getTable(), cell, row, column);//值	
	}
	
	/**
	 * add by 王宇光 2008-3-23 用户事件，转交插件完成
	 * 
	 * @param Object content,String pasteType,Cell[][] c
	 * @return 
	 */
	@Override
	protected boolean userEventAction(Object content,String pasteType,Cell[][] c){
		boolean bContinue = userEventAction(content,getPasteType(),c,getTable());//用户事件
		return bContinue;
	}
}
