package com.ufsoft.iufo.inputplugin.querynavigation;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class QueryNavigationPlugin extends AbstractPlugIn  {


	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(QueryNavigationPlugin.this){

			protected IExtension[] createExtensions() {

				QueryNavigation.getSingleton().validateMenus();
				
				QueryNaviMenu[] menus = QueryNavigation.getSingleton().getMenus();
				IExtension[] arrE = new IExtension[menus.length];
				for (int i = 0; i < menus.length; i++) {
					QueryNaviMenu menu = menus[i];
					if(!menu.hasItem()){
						continue;
					}
					ICommandExt e = new QueryNavigationExt(menu);
					arrE[i] = e;
				}
        		 
				return arrE;
			}
			
		};
	}
	
	
//	public void refreshMenu(UfoReport report){
//		IPluginDescriptor ds = createDescriptor();
//		setDescriptor(ds);
////		ds.getExtensions();
//		report.addPluginExtImpl(QueryNavigationPlugin.class.getName());
//		
//	}
}
