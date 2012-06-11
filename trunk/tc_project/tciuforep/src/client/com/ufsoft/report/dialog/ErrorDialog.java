 
package com.ufsoft.report.dialog;

import java.awt.Component;

/**
 * ������Ϣ�Ի��� ���û�û��ָ��������ϸ��Ϣʱ,�Ի��򽫲���ʾϸ�ڰ�ť
 *  
 */
@Deprecated
public class ErrorDialog extends com.ufida.zior.dialog.ErrorDialog {
	private static final long serialVersionUID = 1L;
	
	/**
	 * ���캯��
	 */
	public ErrorDialog() {
		this(null,null,(String)null);
	}
	public ErrorDialog(Component parent){
		this(parent,null,(String)null);
	}
	/**
	 * ���캯��,ָ��������Ϣ,�Ի��򽫲���ʾϸ�ڰ�ť
	 * 
	 * @param errorMessage -
	 *            ������Ϣ
	 */
	public ErrorDialog(String errorMessage) {
		this(null,errorMessage,(String)null);
	}
	/**
	 * ���캯��,ָ��������Ϣ�ʹ�����ϸ��Ϣ,�Ի�����ʾϸ�ڰ�ť
	 * 
	 * @param errorMessage -
	 *            ������Ϣ
	 * @param errorDetail -
	 *            ������ϸ��Ϣ
	 */
	public ErrorDialog(Component parent,String errorMessage, String errorDetail) {
		super(parent,errorMessage,errorDetail);
	}
	/**
	 * ���캯��,ָ��������Ϣ���쳣��Ϣ,�Ի�����ʾ�쳣�Ķ�ջ��Ϣ
	 * 
	 * @param errorMessage -
	 *            ������Ϣ
	 * @param t �쳣ʵ����
	 */
	public ErrorDialog(String errorMessage, Throwable t) {
		this(null,errorMessage,(String)null);
	}
} 