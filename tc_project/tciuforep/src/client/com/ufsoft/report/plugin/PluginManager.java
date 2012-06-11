package com.ufsoft.report.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * 插件管理器。负责插件的加载。目前加载信息写在 该类在报表工具实例化后被调用。
 * 
 * @author wupess-8-3
 */
public class PluginManager {
	/** 记录当前使用的所有的插件信息。 */
	private HashMap m_hmPlugin;
	private UfoReport _report;

	/**
	 * 构造函数
	 */
	public PluginManager(UfoReport report) {
		super();
		m_hmPlugin = new HashMap();
		_report = report;
		init();
	}

	/**
	 * 删除所有插件，只保留系统默认插件。
	 */
	public void init() {
		m_hmPlugin.clear();
	}

	/**
	 * 添加插件
	 * 
	 * @param piName
	 */
	public void addPlugIn(String piName, IPlugIn piInstance) {
		if (piName == null || m_hmPlugin.containsKey(piName)) {
			return;
		}
		addPlugInImpl(piName, piInstance, new ArrayList());
	}
	
	/**
	 * 删除插件。如果插件有依赖关系，不建议调用此接口。
	 * liuyy.
	 * 2007-10-17
	 * @param piName
	 */
	public void removePlugIn(String piName){
		m_hmPlugin.remove(piName);
		
	}

	/**
	 * 执行添加动作,并确保: 非循环引用情况下,前置插件在当前插件前加入; 循环引用情况下,不限制加入顺序,但要保证不会进入死循环.
	 * 
	 * @param piName
	 *            插件的名字
	 * @param list
	 *            此插件的父插件列表.
	 */
	private void addPlugInImpl(String piName, IPlugIn piInstance, ArrayList list) {
		if (piName == null || piName.equals("")
				|| m_hmPlugin.containsKey(piName)) {
			return;
		}
		String[] requestPlugs = piInstance.getDescriptor()
				.getPluginPrerequisites();
		int length = (requestPlugs == null) ? 0 : requestPlugs.length;
		for (int i = 0; i < length; i++) {
			if (list.contains(requestPlugs[i])) {
				// 存在循环引用,不再保证加入顺序.
				return;
			} else {
				ArrayList newList = (ArrayList) list.clone();
				newList.add(piName);
				addPlugInImpl(requestPlugs[i],
						createNewPlugin(requestPlugs[i]), newList);
				_report.addPluginExtImpl(requestPlugs[i]);
			}
		}
		// 加入当前插件。
		m_hmPlugin.put(piName, piInstance);
	}

	/**
	 * 根据插件名称,新建一个插件实例.
	 * 
	 * @param piClassName
	 *            插件名称
	 * @return 插件实例
	 */
	public IPlugIn createNewPlugin(String piClassName) {
		IPlugIn pi = null;
		try {
			pi = ((IPlugIn) Class.forName(piClassName).newInstance());
		} catch (Exception e) {
			UfoPublic.sendWarningMessage(
					MultiLang.getString("miuforep0000301"),// "不能正确加载插件,请联系管理员解决！"
					_report);
			AppDebug.debug(e);
		}
		return pi;
	}

	/**
	 * 得到所有的插件信息。 此方法保证返回信息之间的依赖关系。例如B如果依赖A，那么数组中A必须在B之前
	 * 
	 * @return IPlugIn[]
	 */
	public IPlugIn[] getAllPlugin() {
		Collection col = m_hmPlugin.values();
		int size = col.size();
		IPlugIn[] plugins = new IPlugIn[size];
		col.toArray(plugins);
		return plugins;
	}

	/**
	 * 工具插件名称得到插件。
	 * 
	 * @param plugName
	 *            插件名称。
	 * @return IPlugIn
	 */
	public IPlugIn getPlugin(String plugName) {
		IPlugIn plugin = null;
		if (plugName != null) {
			plugin = (IPlugIn) (m_hmPlugin.get(plugName));
		}
		return plugin;
	}

}
