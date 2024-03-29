package com.ufsoft.report.sysplugin.edit;


import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>
 * 
 * 选择性粘贴：粘贴所有
 * 
 * @author 王宇光
 * @version Create on 2008-4-14
 */
public class EditPasteAll extends AbsChoosePaste{

	public EditPasteAll(UfoReport m_rep,boolean b_isTransfer){
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
		return EditPasteExt.ALL;
	}

	/**
	 * add by 王宇光 2008-3-23 具体的粘贴方法
	 * 
	 * @param Cell cell,int row,int column
	 * @return 
	 */
	@Override
	protected void paste(Cell cell,int row,int column){
		if(cell == null){
			return;
		}
		
//		CellPosition cellPosition = CellPosition.getInstance(cell.getRow(), cell.getCol());
//		CellsModel cellsModel = getDataModel();
//		pasteValue(cellsModel, getTable(), cell, row, column);//值
//		pasteFormat(cell, row, column);//格式
//		pastePostil(cellsModel, row, column,
//				cellPosition);//批注
		pasteAll(cell,row,column);

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
