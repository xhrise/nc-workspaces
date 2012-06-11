package com.ufsoft.iufo.fmtplugin.monitor;

import java.awt.BorderLayout;
import java.lang.Thread.State;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.ufida.iufo.pub.AppThread;
import com.ufida.iufo.pub.AppThreadMonitor;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;

class AppThreadMonitorDlg extends UfoDialog {
 
	private JTable table;
	private static final long serialVersionUID = 1L;
	/**
	 * @i18n miufo00070=线程监控
	 */
	public AppThreadMonitorDlg(UfoReport container) {
		super(container);
		
		setSize(750, 520);
		setModal(true); 
		
		getContentPane().setLayout(new BorderLayout());
		setTitle(StringResource.getStringResource("miufo00070"));
 
		final JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, BorderLayout.CENTER);

		table = new JTable();
		scrollPane_1.setViewportView(table);
		//table.setEnabled(false);
		
		init();
		
	}
	
	/**
	 * @i18n miufo00132=线程ID
	 * @i18n miufo00133=线程名称
	 * @i18n miufo00134=线程状态
	 * @i18n miufo00135=优先级
	 * @i18n miufo00136=线程类
	 */
	private void init(){
		AppThread[] thrds = AppThreadMonitor.getAll();
    	
    	int num = thrds.length;
    	Object[][] datas = new Object[num][];
		for (int i = 0; i < num; i++) {
			Object[] data = new Object[5];
			int index = 0;
			AppThread t = thrds[i];
			if(t == null){
				continue;
			}
			String id = "", priority = "";
			State state = null;
			if(t.getAssociatedThread() != null){
				id = t.getAssociatedThread().getId() + "";
				priority = t.getAssociatedThread().getPriority() + "";
				state = t.getAssociatedThread().getState();
			}
			 
			data[index++] = id;
			data[index++] = t.getName();
			data[index++] = state;
			data[index++] = priority;
			 
			data[index++] = t.getClass().getName();
			
			datas[i] = data;
			
		} 

		DefaultTableModel tm = new DefaultTableModel(datas,new Object[]{StringResource.getStringResource("miufo00132"), StringResource.getStringResource("miufo00133"), StringResource.getStringResource("miufo00134"), StringResource.getStringResource("miufo00135"), StringResource.getStringResource("miufo00136")});
		table.setModel(tm);
		 
		 
	}

}
