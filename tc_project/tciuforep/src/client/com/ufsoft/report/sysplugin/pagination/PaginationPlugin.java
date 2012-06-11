package com.ufsoft.report.sysplugin.pagination;

import java.util.EventObject;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * ǿ�Ʒ�ҳ�Ĳ��
 * @author zzl
 */
public class PaginationPlugin extends AbstractPlugIn {


    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#startup()
     */
    public void startup() {
    }

    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
    }

    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#store()
     */
    public void store() {
    }

    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new PaginationDescriptor(this);
    }


    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        return false;
    }

    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        return null;
    }

    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        return null;
    }

    /* ���� Javadoc��
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        return null;
    }

    /* ���� Javadoc��
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
    }

    /* ���� Javadoc��
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        return null;
    }
}
