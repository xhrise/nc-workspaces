package com.ufsoft.report.sysplugin.edit;

import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
/**
 * <pre>
 * </pre>
 * 
 * ѡ����ճ����ճ����ʽ
 * 
 * @author �����
 * @version Create on 2008-4-14
 */
public class EditPasteFormat extends AbsChoosePaste {

	public EditPasteFormat(UfoReport m_rep,boolean b_isTransfer) {
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
		return EditPasteExt.FORMAT;
	}

	/**
	 * add by ����� 2008-3-23 �����ճ������
	 * 
	 * @param Cell cell,int row,int column
	 * @return 
	 */
	@Override
	protected void paste(Cell cell,int row,int column){
		pasteFormat(cell,row,column);
			
	}
	
	/**
	 * add by ����� 2008-3-23 �û��¼���ת��������
	 * 
	 * @param Object content,String pasteType,Cell[][] c
	 * @return 
	 */
	@Override
	protected boolean userEventAction(Object content,String pasteType,Cell[][] c){	
		return true;
	}

}
