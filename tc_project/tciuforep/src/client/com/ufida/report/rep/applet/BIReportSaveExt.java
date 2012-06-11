package com.ufida.report.rep.applet;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;

import nc.vo.bi.report.manager.ReportResource;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.file.FileSaveExt;

public class BIReportSaveExt extends FileSaveExt {
	BIPlugIn m_plugIn = null;
	int m_initOperation = UfoReport.OPERATION_FORMAT;
	public BIReportSaveExt(BIPlugIn plugin) {
		super(plugin.getReport());
		m_initOperation = getReport().getOperationState();
		m_plugIn =  plugin;
	}

	/**
	 * @i18n mbiadhoc00006=±£´æ
	 * @i18n mbiadhoc00004=ÎÄ¼þ
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setImageFile("reportcore/savefile.gif");
		uiDes1.setName(StringResource.getStringResource("mbiadhoc00006"));
		uiDes1.setPaths(new String[] { StringResource
				.getStringResource("mbiadhoc00004") });
		ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
		uiDes2.setPaths(new String[] {});
		uiDes2.setToolBar(true);
		return new ActionUIDes[] { uiDes1, uiDes2 };
	}

	public boolean isEnabled(Component focusComp) {
		if (m_initOperation == UfoReport.OPERATION_INPUT || m_plugIn.getOperationState() == UfoReport.OPERATION_INPUT)
			return false;
		return super.isEnabled(focusComp);
	}

	public void initListenerByComp(final Component stateChangeComp) {
		PropertyChangeListener lis = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (stateChangeComp instanceof JButton) {
					((JButton) stateChangeComp).setEnabled(isEnabled(stateChangeComp));
				}
			}

		};
		if (stateChangeComp instanceof JButton) {
			((JButton) stateChangeComp).setEnabled(isEnabled(stateChangeComp));
		}
		m_plugIn.getModel().addPropertyChangeListener(
				ReportResource.OPERATE_TYPE, lis);

	}
}
