package com.ufsoft.iufo.fmtplugin.measure;

/**
@update
���շ�ʽ�ı䣬����ʵ�ֲ��ܱȶԻ����β���
@end
 * �����˲���ʱ�Ի��򲻹رյ���ؽӿڣ�����ע������������Ӧ�����¼�
 * �������ڣ�(2003-10-30 19:34:21)
 * @author��������
 */
import java.awt.Component;
import java.util.EventListener;
public interface DialogRefListener extends EventListener {
/**
	�رղ��նԻ���֮ǰ�����ע������
	ע�⣺���ڿ��ܳ���ͬʱ����������մ��ڵ������ÿ�����ڹر�ʱ������ô˷�������Ҫ�жϹرյĴ����Ƿ�Ϊ��ǰע��Ĵ���
*/
public void beforeDialogClosed(Component refDialog);
/**
	�õ����նԻ������
*/	
public Component getRefDialog();
/**
	�õ����ղ������
*/		
public Component getRefOper();
/**
	��Ӧ�����������
*/	
public void onRef(java.util.EventObject ev);
/**
ע����ս���������������ֵ��������������簴ť
Class refDialog �ǲ��ս�����
subComp �ǲ��ս����ϼ�������ֵ���������
*/	
public void setRefDialogAndRefOper(Component refDialog,Component subComp) ;
}
