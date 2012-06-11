package com.ufida.report.anareport.applet;

import nc.ui.pub.beans.UIDialog;
import nc.vo.bi.report.manager.ReportResource;

import com.ufida.report.anareport.model.AnaReportModel;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.exarea.ExAreaCombineExt;
import com.ufsoft.table.exarea.ExAreaDeleteExt;
import com.ufsoft.table.exarea.ExAreaExt;
import com.ufsoft.table.exarea.ExAreaMngExt;
import com.ufsoft.table.exarea.ExAreaPlugin;
import com.ufsoft.table.exarea.ExAreaSeperateByColExt;
import com.ufsoft.table.exarea.ExAreaSeperateByRowExt;

/**
 * 
 * @author wangyga
 * 
 */
public class AnaExAreaPlugin extends ExAreaPlugin {

	@Override
	public CellsModel getCellsModel() {
		return getAanRepModel().getFormatModel();
	}

	private AnaReportModel getAanRepModel() {
		return getAanPlugin().getModel();
	}

	private AnaReportPlugin getAanPlugin() {
		return (AnaReportPlugin) getReport().getPluginManager().getPlugin(AnaReportPlugin.class.getName());
	}

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				return new IExtension[] { new ExAreaExt((ExAreaPlugin) getPlugin()) {
					protected boolean doMngDlgShow(UfoDialog dlg) {
						return procDlgShow(dlg);
					}
				}, new ExAreaDeleteExt((ExAreaPlugin) getPlugin()), new ExAreaCombineExt((ExAreaPlugin) getPlugin()),
						new ExAreaSeperateByRowExt((ExAreaPlugin) getPlugin()),
						new ExAreaSeperateByColExt((ExAreaPlugin) getPlugin()),
						new ExAreaMngExt((ExAreaPlugin) getPlugin()) {
							protected boolean doMngDlgShow(UfoDialog dlg) {
								return procDlgShow(dlg);
							}
						}, };
			}

		};
	}

	private boolean procDlgShow(UfoDialog dlg) {
		ContextVO context = getReport().getContextVo();
		Integer obj = (Integer)context.getAttribute(ReportResource.OPEN_IN_MODAL_DIALOG);
		boolean isModal = (obj==null||obj <0) ? false :true;
		if (isModal) {
			return (dlg.showModal() == UIDialog.ID_OK);
		} else {
			dlg.show();
			return true;
		}
	}
}
