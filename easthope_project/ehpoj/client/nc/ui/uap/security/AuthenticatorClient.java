/* 
 * @(#)ResultLangTool.java 1.0 2005-12-23
 *
 * Copyright 2005 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */ 
package nc.ui.uap.security;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import nc.ui.ml.NCLangRes;
import nc.vo.uap.security.ISecurityConsts;
import nc.vo.uap.security.SecurityException;


/**
 * 
 * 多语言翻译工具类。
 * <DL>
 * <DT><B>Provider:</B></DT>
 * <DD>NC-UAP</DD>
 * </DL>
 * 
 * @author chenxy
 * @since 5.0
 */
public class AuthenticatorClient {
	
	/**
	 * 多语言翻译。请在客户端调用。
	 * 
	 * @param result
	 * @return
	 */
	public static String getResultMessage(int result) {
		String resID = "CA" + result;
		return NCLangRes.getInstance().getStrByID("caresult", resID);
	}

	/**
	 * 多语言翻译。请在客户端调用。
	 * 
	 * @param e
	 *            异常类带有错误码。
	 * @return
	 */
	public static String getResultMessage(SecurityException e) {
		String resID = "CA" + e.getErrorCode();
		return NCLangRes.getInstance().getStrByID("caresult", resID);
	}
	
	
	/**
	 * 显示口令输入界面
	 * 
	 * @return 口令
	 */
	public byte[] inputPasswd() {

		byte[] pw = null;
		ArrayList al = new ArrayList();
		JPasswordField pwField = new JPasswordField();
		JLabel lb = new JLabel(getResultMessage(ISecurityConsts.USB_KEY_NEED_PASSWD)) /* "请输入USB Key密码" */;
		al.add(lb);
		al.add(pwField);

		if (JOptionPane.showConfirmDialog(null, al.toArray(new Object[0]),
			getResultMessage(ISecurityConsts.PASSWORD)/* "密码" */, JOptionPane.OK_OPTION,
			JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
			pw = new String(pwField.getPassword()).getBytes();
		}
		
//		String pd = JOptionPane.showInputDialog(null,
//				getResultMessage(ISecurityConsts.USB_KEY_NEED_PASSWD)/* "请输入USB Key密码" */
//				,getResultMessage(ISecurityConsts.PASSWORD)/* "密码" */,JOptionPane.QUESTION_MESSAGE);
//		if(pd != null){
//			pw = pd.getBytes();
//		}
		return pw;
	}	
}
