package com.ufsoft.report.plugin;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

/**
 * �ýӿ���������һ����������е���Ϣ�� �����Ƿ��ڲ˵����Ҽ�����������״̬������ʾ����ʾ��ͼ�꣬��Ӧ������ִ���࣬�ӹ��ܵ��������˵���λ�õ�������
 * ���ÿ�����ܵ�ʵ�ָýӿڡ� <title>��ʾ�˵�</title> �������������ܵ��ڲ�ͬ�Ĳ˵��£�getMenuSlot���ز�ͬ��ֵ���ɡ�
 * ͬһ������µĹ��ܵ������������Ĺ��ܵ��ò˵��ָ����ָ�����Ҫʵ�ַּ��˵��Ĺ��ܣ����˵����getCommand��������Ϊ�ա�
 * <title>��ʾ�Ҽ��˵�</title> <title>��ʾ������</title> <title>��ʾ״̬��</title>
 * ���ϣ����״̬����ʾ��ʾ���ݣ���Ҫʵ�ַ���isStateBarSupported();getStatusMark();getStatusValue()�������ʱgetCommand()�ǿգ���ô˫��״̬����ʱ�򣬻���Ӧ���¼���
 * �������ڣ�(2004-5-17 14:37:15)
 * 
 * @author��wupeng
 */
public interface ICommandExt extends IExtension {

	// /**
	// * �õ�ͼƬ���ļ�������ͼ�꽫��ʾ�ڲ˵��͹������С�
	// * �������ڣ�(2004-5-17 14:49:11)
	// * @return java.lang.String ���ص����������ԴĿ¼���ļ����ơ��������Null��������ʾһ����ͼ�ꡣ
	// * @see com.ufsoft.report.resource.ResConst
	// */
	// public String getImageFile();

	// /**
	// * �õ���ݼ���ϡ�
	// * �ڲ��ע���ʱ����������Ƿ�ռ�á�
	// * @return int
	// */
	// public KeyStroke getAccelerator();

	/**
	 * �õ��˵���Ӧ������
	 * 
	 * @return UfoCommand
	 */
	public UfoCommand getCommand();

	/**
	 * �õ�����ִ��ʱ��Ĳ�����
	 * 
	 * @param container
	 *            ������
	 * @return Object[]
	 */
	public Object[] getParams(UfoReport container);
}