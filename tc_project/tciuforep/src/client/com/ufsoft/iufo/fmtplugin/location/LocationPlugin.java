package com.ufsoft.iufo.fmtplugin.location;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * ��λ���
 * 
 * @author ����� 2008-5-19
 * 
 */
public class LocationPlugin extends AbstractPlugIn {

    /*
     * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
     */
    public IPluginDescriptor createDescriptor() {
        return new AbstractPlugDes(this){

            protected IExtension[] createExtensions() {
        		ICommandExt extGoto = new GotoExt(getReport());//��λ
        		return new IExtension[]{extGoto};
            }

        };
    }
}
