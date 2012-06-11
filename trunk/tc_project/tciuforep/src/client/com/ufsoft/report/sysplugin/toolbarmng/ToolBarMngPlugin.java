package com.ufsoft.report.sysplugin.toolbarmng;

import java.awt.Component;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.MultiLang;

public class ToolBarMngPlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(null){
			protected IExtension[] createExtensions() {
				return new IExtension[]{
						new ToolBarMngExt(),
						new ToolBarMngExt(){
							public ActionUIDes[] getUIDesArr() {
								ActionUIDes uiDes = new ActionUIDes();
								uiDes.setName(MultiLang.getString("toolBarMng"));
								uiDes.setPaths(new String[]{MultiLang.getString("tool")});
								return new ActionUIDes[]{uiDes};
							}
							public boolean isEnabled(Component focusComp) {
								return true;
							}
						}
				};
			}			
		};
	}

}
