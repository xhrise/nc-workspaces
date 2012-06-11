package com.ufsoft.report.sysplugin.findreplace;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class ReplaceExt extends AbsActionExt {
    
	private UfoReport m_report=null;
	
	public ReplaceExt(UfoReport container){
		super();
		this.m_report=container;
	}
	@Override
	public UfoCommand getCommand() {
		return new FindReplaceCmd(false);
	}

	@Override
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes2 = new ActionUIDes();
		uiDes2.setName(MultiLang.getString("replace"));
		uiDes2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK));
		uiDes2.setMnemonic(KeyEvent.VK_H);
		uiDes2.setPaths(new String[]{MultiLang.getString("edit")});
		uiDes2.setGroup(MultiLang.getString("edit"));
		uiDes2.setShowDialog(true);
		return new ActionUIDes[]{uiDes2};
	}
	@Override
	public boolean isEnabled(Component focusComp) {
		if(m_report!=null&&m_report.getOperationState()==UfoReport.OPERATION_INPUT){
			return false;
		}
		return super.isEnabled(focusComp);
	}
    
	
}
