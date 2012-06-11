package com.ufsoft.report.sysplugin.help;

import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 
 * @author zzl 2005-5-25
 */
public class HelpPlugin extends AbstractPlugIn {

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
        // TODO 自动生成方法存根

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
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        // TODO 自动生成方法存根
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {        		
        		//帮助
        		HelpAboutExt aboutExt = new HelpAboutExt();
        		HelpExt helpExt = new HelpExt();
                return new IExtension[]{helpExt,aboutExt};
            }
            
        };
    }
}
