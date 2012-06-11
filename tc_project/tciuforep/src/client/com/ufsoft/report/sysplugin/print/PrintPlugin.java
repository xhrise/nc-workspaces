package com.ufsoft.report.sysplugin.print;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * ��ӡ���
 * ʵ�ֵĹ����У� ��ӡ����
 *             ��ӡԤ��
 *             ��ӡ
 * @author zzl 2005-5-25
 */
public class PrintPlugin extends AbstractPlugIn {
    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
            	

        		ICommandExt printSetting = new PrintSettingExt(getReport());//ҳ������...
            	
//        		ICommandExt extPrintPageSet = new PrintPageSetExt(getReport());//ҳ������
        		ICommandExt extPrintPreView = new PrintPreViewExt(getReport());//��ӡԤ��		
        		ICommandExt extPrint = new PrintExt(getReport());//��ӡ
//        		ICommandExt extHeaderFooter = new HeaderFooterExt();
                return new IExtension[]{printSetting, extPrintPreView, extPrint};
            }
            
        };
    }
}
