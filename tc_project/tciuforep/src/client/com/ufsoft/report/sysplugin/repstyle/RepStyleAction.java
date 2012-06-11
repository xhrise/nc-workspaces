/**
 * 
 */
package com.ufsoft.report.sysplugin.repstyle;

import java.awt.event.ActionEvent;

import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.AbstractRepPluginAction;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.sysplugin.style.StylePanelDialog;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserUIEvent;

/**
 * @author wangyga
 * 报表显示风格
 * @created at 2009-9-3,下午02:08:29
 *
 */
public class RepStyleAction extends AbstractRepPluginAction{

	//避免每次点击都实例化
	private StylePanelDialog styleDialog = null;
	
	@Override
	public void execute(ActionEvent e) {
		UFOTable table = getTable();
		ReportStyle style = table.getStyle();
		if(style==null){
			style = new ReportStyle();
			table.setStyle(style);
		}
		//利用参数构建对话框
		StylePanelDialog styleDialog = getStyleDialog();
		styleDialog.setReportStyle(style);
		styleDialog.setVisible(true);
		
		if(styleDialog.getSelectOption()==BaseDialog.CANCEL_OPTION){
			return ;
		}
		table.setStyle(styleDialog.getReportStyle());
		
		dispatchEvent();
	}

	private void dispatchEvent(){
		UFOTable table = getTable();
		UserUIEvent e = new UserUIEvent("repPercent",15,null,table.getStyle().getPercent());
		getMainboard().getEventManager().dispatch(e);
	}
	
	private StylePanelDialog getStyleDialog(){
		if(styleDialog == null){
			styleDialog = new StylePanelDialog(getMainboard());
		}
		return styleDialog;
	}
	
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor(MultiLang.getString("miufo1001189"));
		desc.setExtensionPoints(XPOINT.MENU);
		desc.setIcon("/images/reportcore/set_group.gif");
		desc.setGroupPaths(new String[]{MultiLang.getString("format"),MultiLang.getString("miufo1001189")});
		desc.setShowDialog(true);
		return desc;
	}

}
