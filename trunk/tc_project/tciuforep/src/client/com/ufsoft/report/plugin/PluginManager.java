package com.ufsoft.report.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * ������������������ļ��ء�Ŀǰ������Ϣд�� �����ڱ�����ʵ�����󱻵��á�
 * 
 * @author wupess-8-3
 */
public class PluginManager {
	/** ��¼��ǰʹ�õ����еĲ����Ϣ�� */
	private HashMap m_hmPlugin;
	private UfoReport _report;

	/**
	 * ���캯��
	 */
	public PluginManager(UfoReport report) {
		super();
		m_hmPlugin = new HashMap();
		_report = report;
		init();
	}

	/**
	 * ɾ�����в����ֻ����ϵͳĬ�ϲ����
	 */
	public void init() {
		m_hmPlugin.clear();
	}

	/**
	 * ��Ӳ��
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
	 * ɾ���������������������ϵ����������ô˽ӿڡ�
	 * liuyy.
	 * 2007-10-17
	 * @param piName
	 */
	public void removePlugIn(String piName){
		m_hmPlugin.remove(piName);
		
	}

	/**
	 * ִ����Ӷ���,��ȷ��: ��ѭ�����������,ǰ�ò���ڵ�ǰ���ǰ����; ѭ�����������,�����Ƽ���˳��,��Ҫ��֤���������ѭ��.
	 * 
	 * @param piName
	 *            ���������
	 * @param list
	 *            �˲���ĸ�����б�.
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
				// ����ѭ������,���ٱ�֤����˳��.
				return;
			} else {
				ArrayList newList = (ArrayList) list.clone();
				newList.add(piName);
				addPlugInImpl(requestPlugs[i],
						createNewPlugin(requestPlugs[i]), newList);
				_report.addPluginExtImpl(requestPlugs[i]);
			}
		}
		// ���뵱ǰ�����
		m_hmPlugin.put(piName, piInstance);
	}

	/**
	 * ���ݲ������,�½�һ�����ʵ��.
	 * 
	 * @param piClassName
	 *            �������
	 * @return ���ʵ��
	 */
	public IPlugIn createNewPlugin(String piClassName) {
		IPlugIn pi = null;
		try {
			pi = ((IPlugIn) Class.forName(piClassName).newInstance());
		} catch (Exception e) {
			UfoPublic.sendWarningMessage(
					MultiLang.getString("miuforep0000301"),// "������ȷ���ز��,����ϵ����Ա�����"
					_report);
			AppDebug.debug(e);
		}
		return pi;
	}

	/**
	 * �õ����еĲ����Ϣ�� �˷�����֤������Ϣ֮���������ϵ������B�������A����ô������A������B֮ǰ
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
	 * ���߲�����Ƶõ������
	 * 
	 * @param plugName
	 *            ������ơ�
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
