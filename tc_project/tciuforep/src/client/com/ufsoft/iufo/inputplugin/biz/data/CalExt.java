/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import java.awt.Component;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoOpenedRepBizMenuExt;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class CalExt extends AbsIufoOpenedRepBizMenuExt{
    public CalExt(UfoReport ufoReport) {
        super(ufoReport);
    }   
    
    private boolean m_bCanCal = true;
    
    private boolean isCanCal() {
        return m_bCanCal;
    }
    public void setCanCal(boolean bCanCal) {
        this.m_bCanCal = bCanCal;
    }
    
    @Override
	protected String getGroup() {
		return "calGroup";
	}
    
    protected String[] getPaths() {
        return doGetDataMenuPaths();
    }

    protected String getMenuName() {
        return MultiLangInput.getString("uiufotableinput0004");//"计算";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new CalCmd(ufoReport);
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return super.isEnabled(focusComp) && isCanCal();
    }
}
