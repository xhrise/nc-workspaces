/*
 * 创建日期 2006-11-9
 */
package com.ufida.report.spreedsheet.applet;

import com.ufsoft.iufo.fmtplugin.formula.CalculateFmlExt;
import com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class SpreadFormulaPlugin extends FormulaDefPlugin {
	private class SpreadSheet extends CalculateFmlExt{
		public Object[] getParams(UfoReport container) {
			return new Object[]{container,new Boolean(true)};
		}
	}
	public IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){
            protected IExtension[] createExtensions() {
//            	ICommandExt cellDefExt = new CalculateFmlExt();//单元公式
            	ICommandExt cellDefExt=new SpreadSheet();//单元公式
                return new IExtension[]{cellDefExt
                };
            }
        };
    }
}
