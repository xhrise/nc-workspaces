package com.ufsoft.iufo.inputplugin.hbdraft;

import java.awt.event.ActionEvent;

import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.vo.iufo.data.VerItem;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.Viewer;
import com.ufsoft.report.ReportAuthReadOnly;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.iufo.resource.StringResource;

public class UfoHBDraftAction extends AbstractPluginAction{

	/**
	 * @i18n miufo1000155=错误
	 */
	@Override
	public void execute(ActionEvent e) {
		Viewer viewer = getMainboard().getCurrentView();
		if (viewer != null && viewer instanceof RepDataEditor) {
			RepDataEditor editor = (RepDataEditor) viewer;
			String strDraftId = getHBDraftId(editor);
			HBDraftViewer draft =  (HBDraftViewer) viewer.openView(HBDraftViewer.class.getName(), strDraftId, viewer.getId());
			try {
				draft.setTitle(getHBDraftTitle(viewer.getTitle()));
				draft.initCellsModel(editor);
				draft.getTable().getCells().setCellsAuthorization(new ReportAuthReadOnly());
			} catch (Exception e1) {
				AppDebug.debug(e1);
				nc.ui.pub.beans.MessageDialog.showErrorDlg(draft, StringResource.getStringResource("miufo1000155"), e1.getMessage());
			}
			

		}
	}
	

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(MultiLang.getString("data"), "traceDataGroup");
		descriptor.setName(MultiLang.getString("uiuforep00096")+"...");//"合并底稿"
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		descriptor.setMemonic('D');
		return descriptor;
	}
	
	public boolean isEnabled() {
		Viewer viewer = getMainboard().getCurrentView();
		if(viewer == null)
			return false;
		if(viewer instanceof RepDataEditor){
			RepDataEditor editor = (RepDataEditor)viewer;
			VerItem verItem = editor.getVerFromPanel();
			if(verItem != null)
				return verItem.getVerPK().equals(HBBBSysParaUtil.VER_HBBB+"");
		}		
		return false;
	}
	
	private String getHBDraftId(RepDataEditor editor) {
		return "hbdraft_"+editor.getId();
	}
	
	/**
	 * @i18n miufohbbb00109=_合并底稿
	 */
	private String getHBDraftTitle(String strTitle){
		return strTitle+StringResource.getStringResource("miufohbbb00109");
	}
	

}
  