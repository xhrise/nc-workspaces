/*
 * 创建日期 2006-4-18
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import java.awt.Component;

import com.ufsoft.report.UfoReport;

public abstract class AbsIufoOpenedRepBizMenuExt extends AbsIufoBizMenuExt{
    private boolean m_bIsRepOpened = false;
    public AbsIufoOpenedRepBizMenuExt(UfoReport ufoReport) {
        super(ufoReport);
    }
    protected boolean isRepOpened(){
        return m_bIsRepOpened;
    }
    public void setIsRepOpened(boolean isRepOpened){
        this.m_bIsRepOpened = isRepOpened;
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return isRepOpened();
    }
	@Override
	protected String getGroup() {
		return null;
	}
    
}
