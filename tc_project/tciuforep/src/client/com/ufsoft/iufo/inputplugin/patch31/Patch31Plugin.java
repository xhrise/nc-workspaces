package com.ufsoft.iufo.inputplugin.patch31;

import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
/**
 * �����ֻ��������һЩ���˵��İ�ťת����������.
 * @deprecated by 2008-4-28 ����� ���ƣ����У�ճ���ȱ༭���ܷŵ�ͳһ�����EditPlugin  
 * @author zzl 2005-8-11
 */
public class Patch31Plugin extends AbstractPlugIn {

    protected IPluginDescriptor createDescriptor() {
        return new Patch31PlugDes(this);
    }

    public void startup() {
    }

    public void shutdown() {
    }


    public String[] getSupportData() {
        return null;
    }

    public SheetCellRenderer getDataRender(String extFmtName) {
        return null;
    }

    public SheetCellEditor getDataEditor(String extFmtName) {
        return null;
    }

    public void actionPerform(UserUIEvent e) {
    }

    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        return null;
    }

}
