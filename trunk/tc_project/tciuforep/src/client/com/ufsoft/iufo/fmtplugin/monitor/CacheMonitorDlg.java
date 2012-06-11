package com.ufsoft.iufo.fmtplugin.monitor;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import nc.pub.iufo.cache.base.CacheMonitor;
import nc.pub.iufo.cache.base.ICacheObject;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.iufo.resource.StringResource;

class CacheMonitorDlg extends UfoDialog {
 
	private JTable table;
	private JList list;
	private static final long serialVersionUID = 1L;
	/**
	 * @i18n miufo00834=»º´æÄÚÈÝ¼à¿Ø
	 */
	public CacheMonitorDlg(UfoReport container) {
		super(container);
		
		setSize(750, 520);
		setModal(true); 
		
		getContentPane().setLayout(new BorderLayout());
		setTitle(StringResource.getStringResource("miufo00834"));

		final JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(8);
		splitPane.setDividerLocation(260);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		final JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		 
		list = new JList();
	
		scrollPane.setViewportView(list);
		list.addListSelectionListener(new ListSelectionListener(){
			/**
			 * @i18n miufo00072=¶ÔÏó
			 */
			public void valueChanged(ListSelectionEvent e) {
				String key  = (String) list.getSelectedValue();
				ICacheObject[] objs = CacheMonitor.getStoreObj(key);
				Object[][] datas = new Object[objs.length][]; 
				for (int i = 0; i < objs.length; i++) {
					ICacheObject o = objs[i];
					datas[i] = new Object[]{o.getUUID(), o};
				}
				DefaultTableModel tm = new DefaultTableModel(datas,new Object[]{"UID", StringResource.getStringResource("miufo00072")});
				table.setModel(tm);
//				tm.set
			}
			
		});

		final JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);
//		table.setEnabled(false);
		
		init();
		
	}
	
	private void init(){
		 
		DefaultListModel lm = new DefaultListModel();
		list.setModel(lm);
		String[] keys = CacheMonitor.getCacheTypes();
		for(String key: keys){
			lm.addElement(key);
		}
		 
		 
	}

}
