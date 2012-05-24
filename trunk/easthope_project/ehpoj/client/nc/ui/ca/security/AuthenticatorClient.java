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
	@SuppressWarnings("unchecked")
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
	
	/**
	 * 显示口令输入界面
	 * 
	 * @return 口令
	 */
	@SuppressWarnings("unchecked")
	public String inputPasswd2() {

		String pw = null;
		ArrayList al = new ArrayList();
		JPasswordField pwField = new JPasswordField();
		JLabel lb = new JLabel("请输入的动态口令:") /* "请输入USB Key密码" */;
		al.add(lb);
		al.add(pwField);

		if (JOptionPane.showConfirmDialog(null, al.toArray(new Object[0]),
			"动态口令"/* "密码" */, JOptionPane.OK_OPTION,
			JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
			pw = new String(pwField.getPassword());
		}
		return pw;
	}	
	
	/**
	 * 同步令牌操作
	 * 
	 * @return 结果
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void syncOTP(String strUser) throws SecurityException {
		String res = null;
		if(JOptionPane.showConfirmDialog(null, "令牌处于异步状态,是否需要同步令牌?", "同步令牌", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
			ArrayList al = new ArrayList();
			JPasswordField pwField1 = new JPasswordField();
			JPasswordField pwField2 = new JPasswordField();
			JLabel lb = new JLabel("第一个动态口令 第二个动态口令") /* "请输入USB Key密码" */;
			al.add(lb);
			al.add(pwField1);
			al.add(pwField2);
			int ope = JOptionPane.showConfirmDialog(null, al.toArray(new Object[0]),
					"同步令牌", JOptionPane.OK_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if(ope==JOptionPane.OK_OPTION){
				String pwd1 = new String(pwField1.getPassword());
				String pwd2 = new String(pwField2.getPassword());
				if(pwd1==null||pwd2==null){
					JOptionPane.showMessageDialog(null, "错误", "口令不能为空!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
				res = pubItf.syncOtp(strUser, pwd1, pwd2);	//校验令牌同步是否成功	
				if(res!=null&&res.length()>0){
					throw new SecurityException(res,
							ISecurityConsts.USB_KEY_NEED_PASSWD);
				}
			}else{
				throw new SecurityException("未同步令牌",
						ISecurityConsts.USB_KEY_NEED_PASSWD);
			}
		}
		else{
			throw new SecurityException("未同步令牌",
					ISecurityConsts.USB_KEY_NEED_PASSWD);
		}
	}	
}
