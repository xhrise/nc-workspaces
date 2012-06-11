package com.ufsoft.report.sysplugin.headerlock;

import java.util.EventObject;

import javax.swing.SwingUtilities;

import com.ufida.zior.event.EventManager;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * 头（行列）锁定插件。
 * 状态描述:
 * Freezing:
 *     FrozenNoSplit
 *     ForzenSplit
 * NoFreezing:
 *     NoSplit
 *     Split
 * @author zzl 2005-5-23
 * @deprecated
 * see
 */
public class HeaderLockPlugin extends AbstractPlugIn implements UserActionListner {
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
    	EventManager eventManager = getReport().getEventManager();
    	if(eventManager != null){
    		eventManager.addListener(this);
    	}   	
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
    }



    /*
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        return false;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        return null;
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new HeaderLockDes(this);
    }
	public void userActionPerformed(UserUIEvent e) {
		if(e.getEventType() == UserUIEvent.MODEL_CHANGED){
			modelChanged();
		}
	}
	private void modelChanged(){
		SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				getReport().resetGlobalPopMenuSupport();
			}				
		});
	}
}
