package com.ufsoft.report.plugin;

import javax.swing.JPanel;

/**
 * ��������չ
 * 
 * @author zzl
 */
public interface INavigationExt extends IExtension {
	public String getName();

	/**
	 * ���ص�������panel��
	 * 
	 * @return JPanel
	 */
	public JPanel getPanel();

	/**
	 * �õ�λ�á�
	 * 
	 * @return int
	 */
	public int getNavPanelPos();
	/**
	 * �Ƿ�������ͼ����
	 * @return
	 */
	public boolean isShow();
	public void setShow(boolean show);
	
}
