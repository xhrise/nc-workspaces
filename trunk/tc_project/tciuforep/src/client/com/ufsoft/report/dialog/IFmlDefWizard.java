package com.ufsoft.report.dialog;

public interface IFmlDefWizard {
	
	/**
	 * ��ȡ���幫ʽ���ʽ
	 * @return
	 */
	public String getCellFunc();
	
	/**
	 * ��ʾ����Ի���
	 * @return UfoDialog.ID_OK �� UfoDialog.ID_CANCEL
	 */
	public int showModal();
	
	
}
