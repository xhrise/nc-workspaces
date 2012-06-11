package com.ufsoft.report.plugin;

import javax.swing.JPanel;

/**
 * 导航栏扩展
 * 
 * @author zzl
 */
public interface INavigationExt extends IExtension {
	public String getName();

	/**
	 * 返回导航栏的panel。
	 * 
	 * @return JPanel
	 */
	public JPanel getPanel();

	/**
	 * 得到位置。
	 * 
	 * @return int
	 */
	public int getNavPanelPos();
	/**
	 * 是否置于视图管理
	 * @return
	 */
	public boolean isShow();
	public void setShow(boolean show);
	
}
