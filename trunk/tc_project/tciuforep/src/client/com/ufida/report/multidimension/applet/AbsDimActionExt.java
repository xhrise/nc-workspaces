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
 * ��ά��չ��Ļ��࣬��Ҫ������չ����δѡ��ά�ȳ�Աʱ�Ŀ�����
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
		//ֻ�в���������¼��״̬�²ſ���
		return (m_dimPlugin.getModel().getSelDimModel().isHasDimInfo() &&
				m_dimPlugin.getModel().isDataEditable() == false);
	}

	protected DimensionPlugin getPlugIn(){
		return m_dimPlugin;
	}
}