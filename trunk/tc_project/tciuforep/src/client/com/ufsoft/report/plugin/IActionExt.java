package com.ufsoft.report.plugin;

import java.awt.Component;
import java.util.EventListener;

/**
 * ������չ�ӿ�
 * @author zzl 2005-6-28
 */
public interface IActionExt extends ICommandExt {
	public ActionUIDes[] getUIDesArr();

	/**
	 * ��ǰ״̬��չ��Ӧ������Ƿ���á�
	 * 
	 * @param focusComp
	 * @return boolean
	 */
	public boolean isEnabled(Component focusComp);

	/**
	 * ����ļ�������Ҫ���޸Ĳ˵����������ȵ���ʾ״̬�� �����������������л�����λ������Ƿ�ѡ��״̬��
	 * 
	 * @param stateChangeComp
	 * @return EventListener[]
	 * @deprecated ��ʹ��initListenerByComp.
	 */
	public EventListener getListener(Component stateChangeComp);

	/**
	 * ��������д���,���Թ���������ӵ��ʵ��Ķ�����,Ҳ����ֱ���޸����name,image��.
	 * 
	 * @param stateChangeComp
	 *            void
	 */
	public void initListenerByComp(Component stateChangeComp);
}
