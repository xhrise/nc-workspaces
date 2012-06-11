package com.ufsoft.report.sysplugin.pagination;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.util.MultiLang;

/**
 * @author zzl
 */
public class PaginationDescriptor extends AbstractPlugDes {


    /**
     * @param plugin
     */
    public PaginationDescriptor(IPlugIn plugin) {
        super(plugin);
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getName()
     */
    public String getName() {
        return MultiLang.getString("miufo1001541");
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getNote()
     */
    public String getNote() {
        return MultiLang.getString("miufo1001541");
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getPluginPrerequisites()
     */
    public String[] getPluginPrerequisites() {
        return null;
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getExtensions()
     */
    public IExtension[] createExtensions() {
        return new ICommandExt[]{new InsertPaginationExt(),new DeletePaginationExt()};
    }

    /* （非 Javadoc）
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
     */
    public String getHelpNode() {
        return null;
    }
}
