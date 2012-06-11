package com.ufida.report.anareport.applet;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;


import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.component.ProcessController;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

public class AnaReportSaveExt extends AbsActionExt{
	
	private AnaReportPlugin m_plugin = null;
	
    public AnaReportSaveExt(AnaReportPlugin plugin){
    	super();
    	this.m_plugin=plugin;
    	
    }
    
	@Override
	public UfoCommand getCommand() {
		return new UfoCommand() {
			
			public void execute(Object[] params) {
				ProcessController controller = new ProcessController(true);
				Runnable doWorkRunnable=new Runnable() {
					public void run() {
						String message=m_plugin.anaReportSave();
						if(message != null && message.length() > 0){
							 m_plugin.getReport().getStatusBar().setHintMessage(message);
						}
					}
				};
				controller.setRunnable(doWorkRunnable);
				controller.setString(MultiLang.getString("save"));
				m_plugin.getReport().getStatusBar().setProcessDisplay(controller);
				
			}
		};
	}

	@Override
	public Object[] getParams(UfoReport container) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void initListenerByComp(Component stateChangeComp) {
		m_plugin.getCellsModel().getSelectModel().addSelectModelListener(new SelectListener(){

			public void selectedChanged(SelectEvent e) {
				// TODO Auto-generated method stub
				m_plugin.getReport().getStatusBar().repaint();
			}
			
		});
	}
	@Override
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setImageFile("reportcore/savefile.gif");
        uiDes1.setName(MultiLang.getString("uiuforep0000884"));
        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes2.setPaths(new String[]{});
        uiDes2.setToolBar(true);
        uiDes2.setGroup(MultiLang.getString("file"));
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
}
