package com.ufsoft.iufo.inputplugin.ufobiz;

import com.ufsoft.iufo.fmtplugin.formula.ToolBarFormulaComp;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class TmpInputPlugIn extends AbstractPlugIn{
    protected IPluginDescriptor createDescriptor() {
        return null;          
    }

    public void startup() {
    	//��ӵ�Ԫ��λ��������ʾ�ؼ���������
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
