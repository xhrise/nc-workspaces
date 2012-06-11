package com.ufsoft.iufo.inputplugin.inputcore;

import java.util.EventObject;
import java.util.Hashtable;
import java.util.Vector;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaInputPlugin;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.CellRenderAndEditor;
import com.ufsoft.table.re.DoubleRender;
import com.ufsoft.table.re.IDName;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

public class InputCorePlugin extends AbstractPlugIn {
	private Hashtable<String,Vector<String>> m_hashDynAloneID=null;
	
	static{
		CellRenderAndEditor.registRender(IDName.class, new IDNameRender());
		CellRenderAndEditor.registRender(String.class, new StringRender());
	}
	
	protected IPluginDescriptor createDescriptor() {
        return null;
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
    /**
     * ������ǰ��Ҫ��֤�����⡣
     * 1.��ȷ�����༭״̬
     * 2.��̬���ڹؼ�����ϲ����ظ�
     * 3.��̬����ʱ��ؼ��ַ�Χ���ܳ�������ؼ��֡�
     * 4.�ؼ��ֲ���������Ҫ��ʾ��
     * @return boolean
     */
    public boolean verifyBeforeSave(){
        //��ȷ�����༭
        if(!getReport().stopCellEditing()){
            return false;
        }
        //�ؼ��ֲ�������֤        
        //�ؼ�������ظ���֤
        //��̬��ʱ��ؼ��ַ�Χ��֤��ֻ��֤�º��յĹ�ϵ��
        DynAreaInputPlugin dynAreaPI = (DynAreaInputPlugin) getReport().getPluginManager().getPlugin(DynAreaInputPlugin.class.getName());
        if(dynAreaPI.verifyBeforeSave()){
            return true;
        }
        return false;
    }
    
    public Hashtable<String, Vector<String>> getHashDynAloneID() {
		return m_hashDynAloneID;
	}

	public void setHashDynAloneID(Hashtable<String, Vector<String>> dynAloneID) {
		m_hashDynAloneID = dynAloneID;
	}
	
}
