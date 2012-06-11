package com.ufsoft.report.sysplugin.cellattr;

import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.DoubleEditor;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 单元属性设置插件，包括单元格属性设置功能和条件格式设置功能的实现
 * @author zzl 2005-5-25
 */
public class CellAttrPlugin extends AbstractPlugIn {

	static{
		CellRenderAndEditor.getInstance().registEditor("ConditionFormat",new DoubleEditor());
	}
	
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
    	//add by guogang 2007-7-4 注册条件格式对应的render和editor
//    	getReport().getTable().getCells().getReanderAndEditor().registEditor("ConditionFormat",new DoubleEditor());
		//add end
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
        // TODO 自动生成方法存根

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#store()
     */
    public void store() {
        // TODO 自动生成方法存根

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        // TODO 自动生成方法存根
        return false;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
        // TODO 自动生成方法存根

    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        // TODO 自动生成方法存根
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
     */
    protected IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){
            protected IExtension[] createExtensions() {
        		//add by guogang 2007-7-2 增加条件格式功能
        		ICommandExt extConCellAttr = new SetConditionAttrExt(getReport());
        		ICommandExt extSetCellAttr = new SetCellAttrExt(getReport());   
        		return new IExtension[]{extConCellAttr,extSetCellAttr};
            }
            
        };
    }
}
