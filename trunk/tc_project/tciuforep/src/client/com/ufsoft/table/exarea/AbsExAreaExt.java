package com.ufsoft.table.exarea;

import java.awt.EventQueue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JDialog;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * 设置为可扩展区
 */
public abstract class AbsExAreaExt extends AbsActionExt{
	private ExAreaPlugin m_plugin = null;
	  
	private ExAreaMngDlg dlgExAreaMng=null;
	/**
	 * 构造函数
	 * @param ExAreaPlug
	 */
	public AbsExAreaExt(ExAreaPlugin ExAreaPlug){
		m_plugin = ExAreaPlug;
	}

 
	public UfoCommand getCommand() {
		return null;
	}

	/**
	 * 可扩展区域设置实现；
	 * ExAreaMngDlg.ID_ADD为右键添加可扩展区，ExAreaMngDlg.ID_UPDATE为修改可扩展区，ExAreaMngDlg.ID_DELETE为删除可扩展区。
	 * 
	 * @param report
	 * @i18n uiiufofmt00001=不能添加可扩展区域:
	 * @i18n uiiufofmt00002=不能删除可扩展区域:
	 */
	
	protected void showMngDlg(UfoReport report){
		showMngDlg(report, null);
	}
	
	/**
	 * @i18n miufo00082=错误提示
	 */
	protected void showMngDlg(final UfoReport report, ExAreaCell preCell){
			//直接显示可扩展区域管理对话框
			getPlugIn().getExAreaModel().resetCache();
			ExAreaMngDlg dlgExAreaMng=getMngDialog(preCell);
			doMngDlgShow(dlgExAreaMng);			
	}
	
	private ExAreaMngDlg getMngDialog(ExAreaCell preCell) {
		if (dlgExAreaMng == null) {
			dlgExAreaMng = new ExAreaMngDlg(getReport(), getPlugIn()
					.getExAreaModel(), preCell);
			dlgExAreaMng.addComponentListener(new ComponentListener() {

				public void componentHidden(ComponentEvent e) {
					int nRet = dlgExAreaMng.getResult();

					ExAreaCell cell = (ExAreaCell) dlgExAreaMng
							.getCurrentSelectedAreaCell();
					switch (nRet) {
					case ExAreaMngDlg.ID_ADD:// 添加可扩展区域操作
					case ExAreaMngDlg.ID_UPDATE:// 修改可扩展区域
						ExAreaCell cell2 = null;

						cell2 = showSettingDlg(getReport(), dlgExAreaMng, cell);

						// showMngDlg(report, cell2);
						break;

					case ExAreaMngDlg.ID_DELETE:// 删除可扩展区域
						String error = cell.fireUIEvent(
								ExAreaModelListener.REMOVE, cell, cell);
						if (error != null && error.length() > 1) {
							UfoPublic.showErrorDialog(getPlugIn().getReport(),
									error, MultiLang.getString("miufo00082"));
						} else {
							getPlugIn().getExAreaModel().removeExArea(cell);

							if (cell == null) {
								return;
							}
							// //重新绘制可扩展区域
							// IArea area = cell.getArea();
							// getPlugIn().getReport().repaint(area);
							getPlugIn().getReport().repaint();

						}

						EventQueue.invokeLater(new Runnable() {
							public void run() {
								showMngDlg(getReport());

							}
						});
						break;
					}

				}

				public void componentMoved(ComponentEvent e) {
				}

				public void componentResized(ComponentEvent e) {
				}

				public void componentShown(ComponentEvent e) {
				}

			});
		}else{
			dlgExAreaMng.setAreaCell(preCell);
		}
		return dlgExAreaMng;
	}
	protected ExAreaCell showSettingDlg(UfoReport report, JDialog preDialog,ExAreaCell cell){
		
		ExAreaSetDlg dlg = new ExAreaSetDlg(report,preDialog,getPlugIn().getExAreaModel(),cell);
		 
//		dlg.show();
		doMngDlgShow(dlg);
		
		cell= dlg.getExArea();
		
		return cell;
		
	}
	// @edit by ll at 2009-5-13,上午11:08:13,增加可继承的方法，用于子类处理需要模态的情形
	protected boolean doMngDlgShow(UfoDialog dlg){
		dlg.show();
		return true;
	}

	
	
	
	/**
	 * @return 返回报表工具。
	 */
	private UfoReport getReport() {
		return this.getPlugIn().getReport();
	}
	
	/**
	 * @return 返回可扩展区域插件。
	 */
	protected ExAreaPlugin getPlugIn() {
		return m_plugin;
	}
	
	@Override
	public Object[] getParams(UfoReport report){
	 
		excuteImpl(report);
		return null;
	}
	
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getDesName());
        uiDes.setPaths(new String[]{MultiLang.getString("format")});
        uiDes.setGroup("exareaSetting");
        ActionUIDes uiDes2 = (ActionUIDes)uiDes.clone();
        uiDes2.setPopup(true);
        uiDes2.setPaths(new String[]{});
        return new ActionUIDes[]{uiDes, uiDes2};
	}

	public abstract String getDesName(); 
	
	public abstract void excuteImpl(UfoReport report);
}
 