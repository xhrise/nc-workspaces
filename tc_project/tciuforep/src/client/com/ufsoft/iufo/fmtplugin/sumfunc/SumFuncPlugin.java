package com.ufsoft.iufo.fmtplugin.sumfunc;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class SumFuncPlugin extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this){
			protected IExtension[] createExtensions() {
				return new IExtension[]{
						new AbsSumFuncExt(){
							protected boolean isDownwardNotRightward() {
								return false;
							}							
						},
						new AbsSumFuncExt(){
							protected boolean isDownwardNotRightward() {
								return true;
							}							
						}
						};
			}			
		};
	}

}
