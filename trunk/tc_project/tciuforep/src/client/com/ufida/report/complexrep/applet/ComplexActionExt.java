/*
 * 创建日期 2006-7-13
 */
package com.ufida.report.complexrep.applet;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbsActionExt;

/**
 * @author ljhua
 */
public abstract class ComplexActionExt extends AbsActionExt{

	 private ComplexRepPlugin m_plugin;
	 
	/**
	 * 
	 */
	public ComplexActionExt(ComplexRepPlugin plugin) {
		super();
		m_plugin = plugin;
	}
	public boolean isEnabled(Component focusComp) {
        if(m_plugin.getModel().getSubRepModel() == null || 
        		m_plugin.getModel().getOperationState()==UfoReport.OPERATION_INPUT){
            return false;
        }
        return true;
    }
	protected ComplexRepPlugin getPlugIn(){
		return m_plugin;
	}

}
