package com.ufsoft.report.sysplugin.xml;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class XMLImpExpPlugin extends AbstractPlugIn {
	
	private XMLParserManager _manager = new XMLParserManager();
		
	public void startup() {
		XmlParserUtil.initManager(getXMLObjectPaserManager());
	}
	
	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			protected IExtension[] createExtensions() {
				return new IExtension[]{new XMLExportExt(),new XMLImportExt()};
			}			
		};
	}
	public XMLParserManager getXMLObjectPaserManager(){
		return _manager;
	}
}
