package com.ufsoft.report.sysplugin.findreplace;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class FindExt extends AbsActionExt {

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("find"));
		uiDes.setImageFile("reportcore/query.gif");
		uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,InputEvent.CTRL_MASK));
		uiDes.setMnemonic(KeyEvent.VK_F);
		uiDes.setPaths(new String[]{MultiLang.getString("edit")});
		uiDes.setShowDialog(true);
		uiDes.setGroup(MultiLang.getString("edit"));
		ActionUIDes uiDes1=(ActionUIDes)uiDes.clone();
		uiDes1.setToolBar(true);
		uiDes1.setPaths(new String[]{});
		uiDes1.setTooltip(MultiLang.getString("findAndReplace"));
		return new ActionUIDes[]{uiDes,uiDes1};
	}

	public UfoCommand getCommand() {
		return new FindReplaceCmd(true);
	}

	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

}
