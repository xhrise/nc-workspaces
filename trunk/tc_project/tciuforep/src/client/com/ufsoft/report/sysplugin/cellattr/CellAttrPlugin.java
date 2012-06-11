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
 * ��Ԫ�������ò����������Ԫ���������ù��ܺ�������ʽ���ù��ܵ�ʵ��
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
    	//add by guogang 2007-7-4 ע��������ʽ��Ӧ��render��editor
//    	getReport().getTable().getCells().getReanderAndEditor().registEditor("ConditionFormat",new DoubleEditor());
		//add end
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
     */
    public void shutdown() {
        // TODO �Զ����ɷ������

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#store()
     */
    public void store() {
        // TODO �Զ����ɷ������

    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
     */
    public boolean isDirty() {
        // TODO �Զ����ɷ������
        return false;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
     */
    public String[] getSupportData() {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
     */
    public SheetCellRenderer getDataRender(String extFmtName) {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
     */
    public SheetCellEditor getDataEditor(String extFmtName) {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
     */
    public void actionPerform(UserUIEvent e) {
        // TODO �Զ����ɷ������

    }

    /*
     * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
     */
    public String isSupport(int source, EventObject e)
            throws ForbidedOprException {
        // TODO �Զ����ɷ������
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.AbstractPlugIn#createDescriptor()
     */
    protected IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){
            protected IExtension[] createExtensions() {
        		//add by guogang 2007-7-2 ����������ʽ����
        		ICommandExt extConCellAttr = new SetConditionAttrExt(getReport());
        		ICommandExt extSetCellAttr = new SetCellAttrExt(getReport());   
        		return new IExtension[]{extConCellAttr,extSetCellAttr};
            }
            
        };
    }
}
