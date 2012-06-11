package com.ufsoft.iufo.inputplugin.biz;

import java.util.EventObject;

import com.ufsoft.iufo.fmtplugin.formula.ToolBarFormulaComp;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
/**
 * 报表录入的数据菜单组插件
 * 
 * @author liulp
 *
 */
public class InputDataPlugIn extends AbstractPlugIn {

    protected IPluginDescriptor createDescriptor() {
        return new InputDataPlugDes(this);           
           
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
