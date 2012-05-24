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
 * �����Է��빤���ࡣ
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
	 * �����Է��롣���ڿͻ��˵��á�
	 * 
	 * @param result
	 * @return
	 */
	public static String getResultMessage(int result) {
		String resID = "CA" + result;
		return NCLangRes.getInstance().getStrByID("caresult", resID);
	}

	/**
	 * �����Է��롣���ڿͻ��˵��á�
	 * 
	 * @param e
	 *            �쳣����д����롣
	 * @return
	 */
	public static String getResultMessage(SecurityException e) {
		String resID = "CA" + e.getErrorCode();
		return NCLangRes.getInstance().getStrByID("caresult", resID);
	}
	
	
	/**
	 * ��ʾ�����������
	 * 
	 * @return ����
	 */
	public byte[] inputPasswd() {

		byte[] pw = null;
		ArrayList al = new ArrayList();
		JPasswordField pwField = new JPasswordField();
		JLabel lb = new JLabel(getResultMessage(ISecurityConsts.USB_KEY_NEED_PASSWD)) /* "������USB Key����" */;
		al.add(lb);
		al.add(pwField);

		if (JOptionPane.showConfirmDialog(null, al.toArray(new Object[0]),
			getResultMessage(ISecurityConsts.PASSWORD)/* "����" */, JOptionPane.OK_OPTION,
			JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
			pw = new String(pwField.getPassword()).getBytes();
		}
		
//		String pd = JOptionPane.showInputDialog(null,
//				getResultMessage(ISecurityConsts.USB_KEY_NEED_PASSWD)/* "������USB Key����" */
//				,getResultMessage(ISecurityConsts.PASSWORD)/* "����" */,JOptionPane.QUESTION_MESSAGE);
//		if(pd != null){
//			pw = pd.getBytes();
//		}
		return pw;
	}	
}
