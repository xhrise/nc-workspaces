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
     * 报表保存前需要验证的问题。
     * 1.正确结束编辑状态
     * 2.动态区内关键字组合不能重复
     * 3.动态区内时间关键字范围不能超出主表关键字。
     * 4.关键字不完整，需要提示。
     * @return boolean
     */
    public boolean verifyBeforeSave(){
        //正确结束编辑
        if(!getReport().stopCellEditing()){
            return false;
        }
        //关键字不完整验证        
        //关键字组合重复验证
        //动态区时间关键字范围验证，只验证月和日的关系。
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
