package com.ufsoft.report.sysplugin.edit;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellsModel;
/**
 * <pre>
 * </pre>
 * 
 * ѡ����ճ����ֵ�����ָ�ʽ
 * 
 * @author �����
 * @version Create on 2008-4-14
 */
public class EditPasteValueNumFormat extends AbsChoosePaste{

	public EditPasteValueNumFormat(UfoReport m_rep,boolean b_isTransfer){
		super(m_rep,b_isTransfer);
	}
	
	/**
	 * add by ����� 2008-3-23 ���ճ������
	 * 
	 * @param 
	 * @return 
	 */
	@Override
	protected String getPasteType() {
		if(isTransfer()){
        	return EditPasteExt.TRANSFER;
        }
		return EditPasteExt.VALUE_NUM_FORMAT;
	}


	/**
	 * add by ����� 2008-3-23 �����ճ������
	 * 
	 * @param Cell cell,int row,int column
	 * @return 
	 */
	@Override
	protected void paste(Cell cell,int row,int column){
		if(cell == null){
			return;
		}
		CellsModel cellsModel = getDataModel();
		pasteValue(cellsModel, getTable(), cell, row, column);//ֵ
		pasteNumberFormat(cellsModel, row,
				column, cell.getFormat(),getPasteType());//���ָ�ʽ
	}
	
	/**
	 * add by ����� 2008-3-23 �û��¼���ת��������
	 * 
	 * @param Object content,String pasteType,Cell[][] c
	 * @return 
	 */
	@Override
	protected boolean userEventAction(Object content,String pasteType,Cell[][] c){
		boolean bContinue = userEventAction(content,getPasteType(),c,getTable());//�û��¼�
		return bContinue;
	}

}
