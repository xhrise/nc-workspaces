/*
 * GotoExt.java
 * Created on 2004-10-19 by CaiJie
 * Copyright 2004  Beijing Ufsoft LTM. All rights reserved.
 */
package com.ufsoft.iufo.fmtplugin.location;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.KeyStroke;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.event.SelectEvent;

/**
 * 定位插件描述。
 * @author 王宇光
 * 2008-5-19
 */
public class GotoExt extends AbsActionExt implements IStatusBarExt{ // IMainMenuExt,IStatusBarExt{

	private UfoReport m_Report;
    private GotoDialog goToDialog = null;
	/**
	 * 
	 * CaiJie 2004-10-19
	 * @param rep
	 */
	public GotoExt(UfoReport rep) {
		super();
		m_Report = rep;
	}
	
	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new GotoCommand(m_Report);
	}

	/* (non-Javadoc)
	 * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {		
	    GotoDialog gotoDialog = getGoToDialog(m_Report);
		gotoDialog.show();

		if (gotoDialog.getResult() == UfoDialog.ID_OK) {
			Object obj = gotoDialog.getSelectObject();
			Object[] values = null;
			if (obj != null) {
				values = new Object[] { obj };
			}
			m_Report.getTable().getCells().repaint();
			return values;
		}
		return null;
	}


    /*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
    /**
	 * @i18n edit=编辑
	 */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("uiuforep0000881"));
        uiDes.setPaths(new String[]{MultiLang.getString("edit")});
        uiDes.setGroup("locationGroup");
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				KeyEvent.CTRL_MASK));
        return new ActionUIDes[]{uiDes};
    }

	public void initListenerByComp(final Component stateChangeComp) {
		SelectModel selectModel = m_Report.getCellsModel().getSelectModel();
		if(stateChangeComp instanceof JLabel){
			selectModel.addSelectModelListener(new SelectListener(){
				public void selectedChanged(SelectEvent e) {
					if(e.getProperty() == SelectEvent.ANCHOR_CHANGED){
						String newAnchorPos = m_Report.getCellsModel().getSelectModel().getSelectedArea().toString();
						((JLabel)stateChangeComp).setText(MultiLang.getString("uiuforep0000883")+":"+newAnchorPos);
					}
				}
				
			});
		}
	}
	
	private GotoDialog getGoToDialog(UfoReport rep){
		if(goToDialog == null){
			goToDialog = new GotoDialog(rep);
		}else{
			goToDialog.init();
		}		
		return goToDialog;
	}

}
  