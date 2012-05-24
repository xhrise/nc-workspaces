package nc.ui.eh.ca;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.sf.ICAConfigQueryService;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ca.security.AuthenticatorClient;
import nc.ui.sm.identityverify.ILoginPretreatment;
import nc.vo.ca.security.Authenticator;
import nc.vo.framework.rsa.Encode;
import nc.vo.pub.BusinessException;
import nc.vo.sm.login.LoginSessBean;
import nc.vo.uap.security.ISecurityConsts;
import nc.vo.uap.security.SecurityException;

/**
 * 
 * ��������:2005-12-14
 * 
 * @author licp
 * @since 5.0
 * 
 */
public class CALoginPretreatment implements ILoginPretreatment {

	public synchronized void pretreatment(LoginSessBean lsb) throws Exception {
		String res = verifyUser(lsb);
		if(res!=null&&res.length()>0){
			throw new SecurityException("��¼ʧ�ܣ�"+res+"",
					ISecurityConsts.USB_KEY_PASSWD_INVALID);
		}	
//		String code = pubItf.readFile();
//		if(code!=null&&code.length()>0){
//			JOptionPane.showMessageDialog(null, "�ļ�", "���ݣ�"+code, JOptionPane.ERROR_MESSAGE);
//		}
		AuthenticatorClient client = new AuthenticatorClient();
		String pwd = client.inputPasswd2();
		if (null == pwd||(pwd!=null&&pwd.length()==0)) {
			// �û�û����������
			throw new SecurityException("�����붯̬����",
					ISecurityConsts.USB_KEY_NEED_PASSWD);
		}
		Authenticator.passwd = pwd;
		Authenticator auth = new Authenticator(lsb.getPk_crop(),lsb.getUserCode());
		ICAConfigQueryService certChallengerService = (ICAConfigQueryService) NCLocator.getInstance().lookup(
				ICAConfigQueryService.class.getName());
		String[] chall = certChallengerService.getChallengePair();
		String signCode = auth.sign(chall[1]);
		String userID = auth.getUserID();
		lsb.put("key_userid", userID);
		lsb.put("ca_challeng_key", chall[0]);
		lsb.put("ca_challeng_sign", signCode);
	}
	
	/***
	 * �û���½��֤
	 * @param lsb
	 * @return
	 * @throws BusinessException
	 */
	public String verifyUser(LoginSessBean lsb) throws BusinessException{
		String usercode = lsb.getUserCode();
		String pwd = lsb.getPassword()==null?"":lsb.getPassword();
		Encode code = new Encode();
		pwd = code.encode(pwd);
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		String sql = "select user_password from sm_user where user_code = '"+usercode+"'";
		Object obj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
		if(obj==null){
			return "�û��������ڣ�";
		}
		String pwddata = obj==null?"":obj.toString();
		if(!pwddata.equals(pwd)){
			return "�������";
		}
		return null;
	}
	
}
