package com.ufsoft.report.sysplugin.repname;

import java.awt.Component;
import java.util.EventListener;
import java.util.EventObject;

import javax.swing.JLabel;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;

public class RepNameExt extends AbsActionExt implements IStatusBarExt{
	
	private UfoReport _report;

	public RepNameExt(UfoReport report){
		_report = report;
	}
	public EventListener getListener(Component stateChangeComp) {
		return null;
	}

	public ActionUIDes[] getUIDesArr() {
		return null;
	}

	public void initListenerByComp(final Component stateChangeComp) {
		initDisplay(stateChangeComp);
		_report.getTable().addUserActionListener(new UserActionListner(){
			public void userActionPerformed(UserUIEvent e) {
				if(e.getEventType() == UserUIEvent.MODEL_CHANGED){
					initDisplay(stateChangeComp);
				}
			}
			public String isSupport(int source, EventObject e) throws ForbidedOprException {
				return null;
			}			
		});
	}
	
	private void initDisplay(Component stateChangeComp){
		String code = _report.getContextVo().getReportcode();
		String name = _report.getContextVo().getName();
		JLabel label = (JLabel) stateChangeComp;
		String text = MultiLang.getString("report");
		if(code == null) code = "";
		if(name == null) name = "";
		if(code.length() != 0){
			text = text + ":(" + code + ")" + name;
		}else{
			text = text + ":" + name;
		}
		label.setText(text);
	}
	
	public boolean isEnabled(Component focusComp) {
		return false;
	}

	public UfoCommand getCommand() {
		return null;
	}

	public Object[] getParams(UfoReport container) {
		return null;
	}

}
