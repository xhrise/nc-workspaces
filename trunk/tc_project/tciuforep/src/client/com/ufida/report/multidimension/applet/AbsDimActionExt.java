/*
 * Created on 2005-7-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufida.report.multidimension.applet;

import java.awt.Component;

/**
 * @author ll
 * 
 * 多维扩展项的基类，主要处理扩展项在未选择维度成员时的可用性
 * 
 */
public abstract class AbsDimActionExt extends
		com.ufsoft.report.plugin.AbsActionExt {
	private DimensionPlugin m_dimPlugin = null;

	public AbsDimActionExt(DimensionPlugin plugin) {
		super();
		m_dimPlugin = plugin;
	}

	public boolean isEnabled(Component focusComp) {
		//只有不是在数据录入状态下才可以
		return (m_dimPlugin.getModel().getSelDimModel().isHasDimInfo() &&
				m_dimPlugin.getModel().isDataEditable() == false);
	}

	protected DimensionPlugin getPlugIn(){
		return m_dimPlugin;
	}
}