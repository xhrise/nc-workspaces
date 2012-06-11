 
package com.ufsoft.report.dialog;

import java.awt.Component;

/**
 * 错误信息对话框 当用户没有指定错误详细信息时,对话框将不显示细节按钮
 *  
 */
@Deprecated
public class ErrorDialog extends com.ufida.zior.dialog.ErrorDialog {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 构造函数
	 */
	public ErrorDialog() {
		this(null,null,(String)null);
	}
	public ErrorDialog(Component parent){
		this(parent,null,(String)null);
	}
	/**
	 * 构造函数,指定错误消息,对话框将不显示细节按钮
	 * 
	 * @param errorMessage -
	 *            错误消息
	 */
	public ErrorDialog(String errorMessage) {
		this(null,errorMessage,(String)null);
	}
	/**
	 * 构造函数,指定错误消息和错误详细信息,对话框将显示细节按钮
	 * 
	 * @param errorMessage -
	 *            错误消息
	 * @param errorDetail -
	 *            错误详细信息
	 */
	public ErrorDialog(Component parent,String errorMessage, String errorDetail) {
		super(parent,errorMessage,errorDetail);
	}
	/**
	 * 构造函数,指定错误消息和异常信息,对话框将显示异常的堆栈信息
	 * 
	 * @param errorMessage -
	 *            错误消息
	 * @param t 异常实例。
	 */
	public ErrorDialog(String errorMessage, Throwable t) {
		this(null,errorMessage,(String)null);
	}
} 