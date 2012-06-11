package com.ufsoft.report.plugin;

import com.ufsoft.report.UfoReport;

/**
 * 插件描述默认实现
 * @author zzl 2005-5-23
 */
public abstract class AbstractPlugDes implements IPluginDescriptor {

	private IExtension[] m_exts;
	private IPlugIn m_plugin;

	/**
	 * 
	 */
	public AbstractPlugDes(IPlugIn plugin) {
		super();
		m_plugin = plugin;
	}

	public UfoReport getReport() {
		return m_plugin.getReport();
	}

	public IPlugIn getPlugin() {
		return m_plugin;
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
	public final IExtension[] getExtensions() {
		if (m_exts == null) {
			m_exts = createExtensions();
		}
		return m_exts;
	}

	/**
	 * @return
	 * @return IExtension[]
	 */
	protected abstract IExtension[] createExtensions();

	/*
	 * @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
	 */
	public String getHelpNode() {
		return null;
	}

}
