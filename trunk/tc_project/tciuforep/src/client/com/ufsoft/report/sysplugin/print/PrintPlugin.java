package com.ufsoft.report.sysplugin.print;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * 打印插件
 * 实现的功能有： 打印设置
 *             打印预览
 *             打印
 * @author zzl 2005-5-25
 */
public class PrintPlugin extends AbstractPlugIn {
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
            	

        		ICommandExt printSetting = new PrintSettingExt(getReport());//页面设置...
            	
//        		ICommandExt extPrintPageSet = new PrintPageSetExt(getReport());//页面设置
        		ICommandExt extPrintPreView = new PrintPreViewExt(getReport());//打印预览		
        		ICommandExt extPrint = new PrintExt(getReport());//打印
//        		ICommandExt extHeaderFooter = new HeaderFooterExt();
                return new IExtension[]{printSetting, extPrintPreView, extPrint};
            }
            
        };
    }
}
