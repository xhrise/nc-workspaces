package com.ufida.report.rep.applet;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import nc.vo.bi.report.manager.ReportResource;

import com.ufida.bi.base.BIException;
import com.ufida.report.anareport.applet.AnaReportPlugin;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UICloseableTabbedPane;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.sysplugin.file.FileSaveExt;
import com.ufsoft.report.util.MultiLang;

public class BIReportPreViewExt extends FileSaveExt {
	BIPlugIn m_plugIn = null;

	int m_initOperation = UfoReport.OPERATION_FORMAT;
	private boolean isShowLastDataState=false;
	private ImageIcon preview;
	private ImageIcon preview_no;

	public BIReportPreViewExt(BIPlugIn plugin) {
		super(plugin.getReport());
		m_initOperation = plugin.getOperationState();
		m_plugIn = plugin;
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes au1 = new ActionUIDes();
		au1.setName(StringResource.getStringResource("mbiadhoc00007"));
		au1.setImageFile("reportcore/ana_preview.gif");
	    au1.setPaths(new String[]{MultiLang.getString("file")});
		ActionUIDes au2 = (ActionUIDes) au1.clone();
		au2.setPaths(new String[] {});
		au2.setToolBar(true);
        au2.setGroup(MultiLang.getString("file"));
		au2.setTooltip(StringResource.getStringResource("mbiadhoc00007"));
		return new ActionUIDes[] { au1, au2 };
	}

	public boolean isEnabled(Component focusComp) {
		// if (getReport().getOperationState() == UfoReport.OPERATION_INPUT)
//		if (m_initOperation == UfoReport.OPERATION_INPUT)
//			return false;
		return super.isEnabled(focusComp);
	}

	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				int newOption = (m_plugIn.getOperationState() == UfoReport.OPERATION_FORMAT) ? UfoReport.OPERATION_INPUT
						: UfoReport.OPERATION_FORMAT;
				try {
					m_plugIn.setOperationState(newOption);
				} catch (BIException ex) {
					JOptionPane.showMessageDialog(getReport(), ex.getMessage());
				}
				
				if (m_plugIn instanceof AnaReportPlugin) {
					AnaReportPlugin plugin = (AnaReportPlugin) m_plugIn;
					String strMenuName = plugin.getQueryPanel().getName();
					UICloseableTabbedPane tabs = plugin.getReport()
							.getReportNavPanel().getPanelById(
									ReportNavPanel.EAST_NAV);
					if (newOption == UfoReport.OPERATION_FORMAT) {// 说明上次是数据态
						isShowLastDataState = tabs.indexOfTab(strMenuName) >= 0;
					} else {
						plugin.getModel().getDataModel().setDirty(false);
					}
					// 分析报表中数据态数据集面板已经激活的话，再次切换状态时，面板自动显示
					boolean isSelected = newOption == UfoReport.OPERATION_FORMAT ? true
							: isShowLastDataState;
					if (isSelected) {
						tabs.add(strMenuName, plugin.getQueryPanel());
					} else {
						tabs.removeTabAt(strMenuName);
					}}
				
			}
		};
	}

	public Object[] getParams(UfoReport container) {
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IActionExt#getListeners(java.awt.Component)
	 */
	public EventListener getListener(final Component stateChangeComp) {

		PropertyChangeListener lis = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (stateChangeComp instanceof JMenuItem) {
					((JMenuItem) stateChangeComp).setText(getMenuName());
				}
				if (stateChangeComp instanceof JButton) {
					((JButton) stateChangeComp).setToolTipText(getMenuName());
					((JButton) stateChangeComp).setIcon(getButtonImage());
				}

			}

		};
		if (stateChangeComp instanceof JButton) {
			((JButton) stateChangeComp).setEnabled(isEnabled(stateChangeComp));
		}
		
		m_plugIn.getModel().addPropertyChangeListener(
				ReportResource.OPERATE_TYPE, lis);
		return null;
	}

	private String getMenuName() {
		boolean isFormatState = (m_plugIn.getOperationState() == UfoReport.OPERATION_FORMAT);
		return isFormatState ? StringResource
				.getStringResource("mbiadhoc00007") : StringResource
				.getStringResource("mbiadhoc00008");
	}
    private ImageIcon getButtonImage(){
    	boolean isFormatState = (m_plugIn.getOperationState() == UfoReport.OPERATION_FORMAT);
    	return isFormatState?getPreviewImage():getPreviewNoImage();
    }
	private ImageIcon getPreviewImage(){
		if(preview==null){
			preview=ResConst.getImageIcon("reportcore/ana_preview.gif");
		}
		return preview;
	}
	private ImageIcon getPreviewNoImage(){
		if(preview_no==null){
			preview_no=ResConst.getImageIcon("reportcore/ana_preview_no.gif");
		}
		return preview_no;
	}
}
