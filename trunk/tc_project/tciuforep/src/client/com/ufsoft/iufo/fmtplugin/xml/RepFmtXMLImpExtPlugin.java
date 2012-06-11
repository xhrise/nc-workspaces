package com.ufsoft.iufo.fmtplugin.xml;

import java.util.EventObject;

import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.sysplugin.xml.XMLImpExpPlugin;
import com.ufsoft.report.sysplugin.xml.XMLParserManager;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;

public class RepFmtXMLImpExtPlugin extends AbstractPlugIn implements UserActionListner {
	
	

	public void startup() {
		XMLImpExpPlugin pi = (XMLImpExpPlugin) getReport().getPluginManager().getPlugin(XMLImpExpPlugin.class.getName());
		initManager(pi.getXMLObjectPaserManager());
	}

	public static void initManager(XMLParserManager manager) {
		IUFOXMLImpExpUtil.initManager(manager);	
	}

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			protected IExtension[] createExtensions() {
				return null;
			}
			public String[] getPluginPrerequisites() {
				return new String[]{XMLImpExpPlugin.class.getName()};
			}			
		};
	}

	public void userActionPerformed(UserUIEvent e) {
		if(e.getEventType() == UserUIEvent.MODEL_CHANGED){
			CellsModel newModel = (CellsModel) e.getNewValue();
			CellsModelOperator.initModelProperties((UfoContextVO)getReport().getContextVo(),newModel);
		}
	}

	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		return null;
	}
	
}
