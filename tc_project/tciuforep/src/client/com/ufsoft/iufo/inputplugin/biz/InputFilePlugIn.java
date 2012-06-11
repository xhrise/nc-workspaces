package com.ufsoft.iufo.inputplugin.biz;

import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
/**
 * 报表录入的文件菜单组插件
 * 
 * @author liulp
 *
 */
public class InputFilePlugIn extends AbstractPlugIn {
	protected IPluginDescriptor createDescriptor() {
        return new InputFilePlugDes(this);
    }

    public void startup() {
    }

    public void shutdown() {
    }

    public void store() {
    }

    public boolean isDirty() {
        return false;
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
