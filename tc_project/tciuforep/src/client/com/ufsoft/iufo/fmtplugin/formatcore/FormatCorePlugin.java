package com.ufsoft.iufo.fmtplugin.formatcore;

import java.util.EventObject;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;
import com.ufsoft.table.re.StringRender;

/**
 * �����߸�ʽ��ƺ��Ĳ��.
 * @author chxiaowei 2007-3-26
 */
public class FormatCorePlugin extends AbstractPlugIn {
	protected IPluginDescriptor createDescriptor() {
		return null;
	}

	public void startup() {
		UFOTable ufoTable = getReport().getTable();
		ufoTable.getReanderAndEditor().registRender(String.class, new StringRender());	
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
     * @return boolean
     */
    public boolean verifyBeforeSave(){
        //��ȷ�����༭
        if(!getReport().stopCellEditing()){
            return false;
        }
        getReport().getCellsModel().getSelectModel().clear();
        return true;
    }
}
