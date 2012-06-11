package com.ufsoft.report.plugin;

import java.util.EventObject;


/**
 * ����¼������� �����������״̬�ĸı䡣����֪ͨ�����ߺ�����ʵ�ֲ���ӿڵ��෢���ı䡣 ͨ��Ӧ�ý������Ϊ�������룬�������������߻�ò����״̬��
 * ������Ӽ���ͨ���¼����ͣ������ߴ������¼���ʱ�򣬻�����¼�����������ˢ�µ������Ƿ���չ���еȡ�
 * ʹ����Ӧ�þ����ܴ����¼�Ӱ��������˿��Ա�������������ʵ������Ҫ���ٴ��������
 * 
 * @author wupeng 2004-7-30
 */
public class PlugEvent extends EventObject {
	/**
	 * <code>ADD</code>�¼����ͱ�ǣ��������
	 */
	public final static String ADD = "ADD";
	/**
	 * <code>MODIFY</code>�¼����ͱ�ǣ��޸�����
	 */
	public final static String MODIFY = "MODIFY";
	/**
	 * <code>REMOVE</code>�¼����ͱ�ǣ�ɾ������
	 */
	public final static String REMOVE = "REMOVE";

	/**
	 * ���캯��
	 * 
	 * @param source
	 *            �¼�Դ
	 */
	public PlugEvent(Object source) {
		super(source);
	}

}
