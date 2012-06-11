package com.ufsoft.report.sysplugin.edit;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class CutExt extends EditExt{

	 public CutExt(UfoReport report, int editType, int clipType) {
		 super(report,editType,clipType);
	 }
	 
	 public ActionUIDes[] getUIDesArr() {
	        ActionUIDes uiDes = new ActionUIDes();
	        uiDes.setName(MultiLang.getString("miufo1000654"));
	        uiDes.setImageFile("reportcore/cut.gif");
	        uiDes.setToolBar(true);
	        uiDes.setGroup(MultiLang.getString("edit"));
	        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					KeyEvent.CTRL_MASK));
	        return new ActionUIDes[]{uiDes};
}

}
