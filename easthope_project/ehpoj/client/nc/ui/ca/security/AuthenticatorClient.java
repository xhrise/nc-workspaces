/* 
 * @(#)ResultLangTool.java 1.0 2005-12-23
 *
 * Copyright 2005 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */ 
package nc.ui.ca.security;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
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
	@SuppressWarnings("unchecked")
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
	
	/**
	 * ��ʾ�����������
	 * 
	 * @return ����
	 */
	@SuppressWarnings("unchecked")
	public String inputPasswd2() {

		String pw = null;
		ArrayList al = new ArrayList();
		JPasswordField pwField = new JPasswordField();
		JLabel lb = new JLabel("������Ķ�̬����:") /* "������USB Key����" */;
		al.add(lb);
		al.add(pwField);

		if (JOptionPane.showConfirmDialog(null, al.toArray(new Object[0]),
			"��̬����"/* "����" */, JOptionPane.OK_OPTION,
			JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
			pw = new String(pwField.getPassword());
		}
		return pw;
	}	
	
	/**
	 * ͬ�����Ʋ���
	 * 
	 * @return ���
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void syncOTP(String strUser) throws SecurityException {
		String res = null;
		if(JOptionPane.showConfirmDialog(null, "���ƴ����첽״̬,�Ƿ���Ҫͬ������?", "ͬ������", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
			ArrayList al = new ArrayList();
			JPasswordField pwField1 = new JPasswordField();
			JPasswordField pwField2 = new JPasswordField();
			JLabel lb = new JLabel("��һ����̬���� �ڶ�����̬����") /* "������USB Key����" */;
			al.add(lb);
			al.add(pwField1);
			al.add(pwField2);
			int ope = JOptionPane.showConfirmDialog(null, al.toArray(new Object[0]),
					"ͬ������", JOptionPane.OK_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if(ope==JOptionPane.OK_OPTION){
				String pwd1 = new String(pwField1.getPassword());
				String pwd2 = new String(pwField2.getPassword());
				if(pwd1==null||pwd2==null){
					JOptionPane.showMessageDialog(null, "����", "�����Ϊ��!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
				res = pubItf.syncOtp(strUser, pwd1, pwd2);	//У������ͬ���Ƿ�ɹ�	
				if(res!=null&&res.length()>0){
					throw new SecurityException(res,
							ISecurityConsts.USB_KEY_NEED_PASSWD);
				}
			}else{
				throw new SecurityException("δͬ������",
						ISecurityConsts.USB_KEY_NEED_PASSWD);
			}
		}
		else{
			throw new SecurityException("δͬ������",
					ISecurityConsts.USB_KEY_NEED_PASSWD);
		}
	}	
}
