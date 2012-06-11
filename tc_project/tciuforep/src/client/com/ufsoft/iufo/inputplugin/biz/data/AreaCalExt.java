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

public class AreaCalExt extends AbsIufoOpenedRepBizMenuExt{
    private boolean m_bCanAreaCal = true;

    public AreaCalExt(UfoReport ufoReport) {
        super(ufoReport);
    }
    private boolean isCanAreaCal() {
        return m_bCanAreaCal;
    }
    public void setCanAreaCal(boolean bCanAreaCal) {
        this.m_bCanAreaCal = bCanAreaCal;
    }
    
    @Override
	protected String getGroup() {
		return "calGroup";
	}
    
	protected String[] getPaths() {
        return doGetDataMenuPaths();
    }

    protected String getMenuName() {
        return MultiLangInput.getString("uiufotableinput0003");//"区域计算";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new AreaCalCmd(ufoReport);
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return super.isEnabled(focusComp) && isCanAreaCal();
    }
}
