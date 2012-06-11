package com.ufsoft.iufo.inputplugin.patch31;

import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
/**
 * 本插件只是用来将一些主菜单的按钮转换到工具栏.
 * @deprecated by 2008-4-28 王宇光 复制，剪切，粘贴等编辑功能放到统一插件：EditPlugin  
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
