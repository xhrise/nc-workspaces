package com.ufida.report.free.applet;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.rep.applet.BIReportSaveExt;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.excel.ExcelExpExt;
import com.ufsoft.report.util.MultiLang;

/**
 * 
 * @author zzl 2005-4-27
 */
public class FreeRepDescriptor implements IPluginDescriptor, PropertyChangeListener {

	private FreeQueryDesignePlugin m_freePlugin = null;

	public FreeRepDescriptor(FreeQueryDesignePlugin plugin) {
		super();
		m_freePlugin = plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		// 监听插件的模型切换事件
		if (evt.getPropertyName().equals(IMultiDimConst.PROPERTY_PLUGIN_MODEL_CHANGED)) {
		}
	}

	public IExtension[] getExtensions() {
		if (m_freePlugin.isFormat()) {
			return new IExtension[] { new ExcelExpExt() {
				public ActionUIDes[] getUIDesArr() {
					ActionUIDes[] des = super.getUIDesArr();
					des[0].setPaths(new String[] { MultiLang.getString("file"), MultiLang.getString("export") });
					return des;
				}
			}, new BIReportSaveExt(m_freePlugin) {
				public boolean isEnabled(Component focusComp) {
					return true;
				}

				public UfoCommand getCommand() {
					return new FreeRepSaveCmd(getReport(), m_freePlugin);
				}
			} };
		} else
			return null;
	}

	public String getHelpNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNote() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getPluginPrerequisites() {
		// TODO Auto-generated method stub
		return null;
	}

}