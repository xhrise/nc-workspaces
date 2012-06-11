package com.ufsoft.iufo.inputplugin.ufobiz;

import com.ufsoft.iufo.fmtplugin.formula.ToolBarFormulaComp;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class TmpInputPlugIn extends AbstractPlugIn{
    protected IPluginDescriptor createDescriptor() {
        return null;          
    }

    public void startup() {
    	//添加单元定位和内容显示控件到工具栏
    	getReport().getToolBarPane().add(new ToolBarFormulaComp(getReport()));
    }

    public void shutdown() {
    }

    public void store() {
    }

    public boolean isDirty() {
        return false;
    }
}
