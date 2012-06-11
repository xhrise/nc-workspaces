/*
 * DynAreDescriptor.java
 * 创建日期 2004-11-26
 * Created by CaiJie
 */
package com.ufsoft.iufo.fmtplugin.dynarea;



import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * @author caijie
 * @since 3.1
 */
public class DynAreaDescriptor implements IPluginDescriptor{    

    /**
     * 动态区域插件
     */
    private DynAreaDefPlugIn m_dynAreaPlugIn = null;

    /**
     * @param report
     * @param plugin
     */
    public DynAreaDescriptor(DynAreaDefPlugIn plugin) {
        super();       
        m_dynAreaPlugIn = plugin;
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getName()
     */
    public String getName() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getNote()
     */
    public String getNote() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getPluginPrerequisites()
     */
    public String[] getPluginPrerequisites() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getExtensions()
     */
    public IExtension[] getExtensions() {
        return new ICommandExt[] {
        		new DynAreaSetExt(m_dynAreaPlugIn),
        		new DynAreaMngExt(m_dynAreaPlugIn),
        		new RowNumberExt(m_dynAreaPlugIn.getReport())};
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
     */
    public String getHelpNode() {
        return null;
    }

}