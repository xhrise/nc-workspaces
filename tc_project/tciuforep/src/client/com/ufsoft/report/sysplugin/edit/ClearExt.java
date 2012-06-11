package com.ufsoft.report.sysplugin.edit;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;

public class ClearExt extends EditClearContentExt{

    public ClearExt(UfoReport report) {
    	super(report);
    }
    
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo1000103"));
        uiDes.setImageFile("reportcore/delete.gif");
        uiDes.setToolBar(true);
        uiDes.setGroup(MultiLang.getString("edit"));
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0
				));
        return new ActionUIDes[]{uiDes};
    }
}
