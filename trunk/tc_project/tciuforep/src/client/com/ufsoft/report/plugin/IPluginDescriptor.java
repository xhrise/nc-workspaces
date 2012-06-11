//Source file: F:\\workspace\\reportTool\\src\\com\\ufsoft\\report\\plugin\\IPluginDescriptor.java

package com.ufsoft.report.plugin;

/**
 * ���ڲ����Ϣ��������
 * 
 * @author wupeng 2004-7-31
 */

public interface IPluginDescriptor {
	/**
	 * �������
	 * 
	 * @return String
	 */
	public String getName();

	/**
	 * �õ������˵����Ϣ
	 * 
	 * @return String
	 */
	public String getNote();

	/**
	 * �õ���ǰ���������Ҫ�����������Ϣ��
	 * 
	 * @return String[] ����ֵ�ǲ�����������Ƶ����顣
	 */
	public String[] getPluginPrerequisites();

	/**
	 * �õ�����ҽӵĹ��ܵ�
	 * 
	 * @return ICommandExt[]
	 */
	public IExtension[] getExtensions();

	/**
	 * �õ������ļ��Ľڵ�
	 * 
	 * @return String
	 */
	public String getHelpNode();

}