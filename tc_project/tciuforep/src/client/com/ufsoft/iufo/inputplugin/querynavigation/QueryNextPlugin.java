package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class QueryNextPlugin extends AbstractPlugIn  {


	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(QueryNextPlugin.this){

			protected IExtension[] createExtensions() {

//				QueryNaviMenu[] menus = QueryNavigation.getSingleton().genmenus();
//				IExtension[] arrE = new IExtension[menus.length];
//				for (int i = 0; i < menus.length; i++) {
//					ICommandExt e = new QueryNavigationExt(menus[i]);
//					arrE[i] = e;
//				}
//        		 
				IExtension[] arrE = new IExtension[2];
				arrE[0] = new QueryNextExt(false, getReport());
				arrE[1] = new QueryNextExt(true, getReport());
				return arrE;
				
			}
			
		};
	}
}
