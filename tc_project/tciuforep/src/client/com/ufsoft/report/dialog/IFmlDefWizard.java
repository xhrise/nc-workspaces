package com.ufsoft.report.dialog;

public interface IFmlDefWizard {
	
	/**
	 * 获取定义公式表达式
	 * @return
	 */
	public String getCellFunc();
	
	/**
	 * 显示定义对话框
	 * @return UfoDialog.ID_OK 或 UfoDialog.ID_CANCEL
	 */
	public int showModal();
	
	
}
