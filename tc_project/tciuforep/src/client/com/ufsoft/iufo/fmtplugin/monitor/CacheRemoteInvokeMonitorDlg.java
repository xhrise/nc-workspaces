package com.ufsoft.iufo.fmtplugin.monitor;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import nc.pub.iufo.cache.base.proxy.AppletCacheInvokeMonitor;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 
 * @author liuyy
 *
 */
public class CacheRemoteInvokeMonitorDlg  extends UfoDialog {
 
	private JTable table;
	private static final long serialVersionUID = 1L;
	/**
	 * @i18n miufo00535=缓存远程调用监控
	 */
	public CacheRemoteInvokeMonitorDlg(UfoReport container) {
		super(container);
		
		setSize(950, 520);
		setModal(true); 
		
		getContentPane().setLayout(new BorderLayout());
		setTitle(StringResource.getStringResource("miufo00535"));
 
		final JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, BorderLayout.CENTER);

		table = new JTable();
		scrollPane_1.setViewportView(table);
		//table.setEnabled(false);
		
		init();
		
	}
	
	/**
	 * @i18n miufo00086=平均响应时间(秒)
	 * @i18n miufo00087=访问次数
	 * @i18n miufo00536=总响应时间(秒)
	 * @i18n miufo00071=缓存类型
	 * @i18n miufo00089=调用方法
	 * @i18n miufo00090=最小响应时间(秒)
	 * @i18n miufo00091=最大响应时间(秒)
	 * @i18n miufo00092=最大响应发生时间
	 * @i18n miufo00094=最后访问时间
	 */
	private void init(){
		
		AppletCacheInvokeMonitor.MonitorVO[] vos = AppletCacheInvokeMonitor.getSingleton().list();
	 	int num = vos.length;
    	Object[][] datas = new Object[num][];
		for (int i = 0; i < num; i++) {
			Object[] data = new Object[9];
			int index = 0;
			AppletCacheInvokeMonitor.MonitorVO vo = vos[i];
			data[index++] = (Math.ceil(vo.getAverResTime())) / 1000;
			data[index++] = vo.getReqNum();
			data[index++] = (vo.getTotalResTime() / 1000);
			data[index++] = vo.getCacheName();
			data[index++] = vo.getMethodName();
			data[index++] = (vo.getMinResTime() / 1000);
			data[index++] = (vo.getMaxResTime() / 1000);
			data[index++] = vo.getMaxResHappenedTime();
			data[index++] = vo.getLastReqTime();
			
			datas[i] = data;
			
		}
 
		DefaultTableModel tm = new DefaultTableModel(datas, new Object[] {
				StringResource.getStringResource("miufo00086"), StringResource.getStringResource("miufo00087"), StringResource.getStringResource("miufo00536"), StringResource.getStringResource("miufo00071"), StringResource.getStringResource("miufo00089"), StringResource.getStringResource("miufo00090"), StringResource.getStringResource("miufo00091"), StringResource.getStringResource("miufo00092"), StringResource.getStringResource("miufo00094")});
		table.setModel(tm);
		 
		 
	}
}