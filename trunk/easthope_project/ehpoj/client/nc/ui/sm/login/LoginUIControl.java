/*
 * 该类控制登录以及注销时的界面切换等工作 创建日期： 2004-11-4 作者：licp
 *	modules\ uap\classes\nc\ ui\sm
 */
package nc.ui.sm.login;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLayeredPane;

import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.MyFocusManager;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.sm.identityverify.DefaultLoginPretreatment;
import nc.ui.sm.identityverify.ILoginPretreatment;
import nc.ui.sm.login.speceffe.AbstractSpecialEffectPanel;
import nc.ui.sm.login.speceffe.SpecialEffectConstant;
import nc.vo.pub.CommonConstant;
import nc.vo.pub.services.ServerEnvironment;
import nc.vo.sm.identityverify.IAConfVO;
import nc.vo.sm.login.ILoginResult;
import nc.vo.sm.login.LoginFailureInfo;
import nc.vo.sm.login.LoginSessBean;
import nc.vo.sm.login.ViewConstantTool;
import nc.vo.uap.security.ISecurityConsts;
import nc.vo.uap.security.SecurityException;

public class LoginUIControl {
    private static LoginUIControl instance = null;

    // private ILoginPretreatment pretreatement = null;
    /**
     *
     */
    public LoginUIControl() {
        super();

    }

    public static LoginUIControl getInstance() {
        if (instance == null) {
            instance = new LoginUIControl();
        }
        return instance;
    }

    /**
     * @param parent
     * @param lsb
     * @param serverURL
     * @return object数组，长度为2。 第一个元素是表示登录结果的id号，为0表示登录认证成功，其他值表示错误编号。 如果登录认证成功，第二个元素表示返回的LoginSessBean对象，否则为null
     * @throws Exception
     *             创建时间：2004-11-5 8:58:01
     */
    public Object[] login(LoginSessBean lsb, URL serverURL) throws Exception {
        Object[] objs = new Object[2];
        String langcode = lsb.getLanguage();
        nc.vo.ml.Language selLang = NCLangRes.getInstance().getLanguage(langcode);
        nc.ui.ml.NCLangRes.getInstance().setCurrLanguage(selLang);
        String dsName = lsb.getDataSourceName();
        if (dsName != null) {
            System.setProperty(nc.vo.pub.CommonConstant.USER_DATA_SOURCE, dsName);
            System.setProperty(nc.vo.pub.CommonConstant.DEFAULT_DATA_SOURCE, dsName);
        }
        int resultCode = ILoginResult.UNKNOWN_ERROR;
        objs[0] = new Integer(resultCode);
        try {
            IAConfVO conf = SFServiceFacility.getIAModeQueryService().getIAConfByuser(lsb.getUserCode(), lsb.getPk_crop(), lsb.getDataSourceName(), lsb.getAccountId());
            if (conf != null) {
                String LoginPretreatmentClassName = conf.getLoginPretreatmentClassName();
                String LoginResultClassName = conf.getLoginResultClassName();
                String loginClientRunnable = conf.getLoginClientRunnable();
                System.setProperty("_LoginPretreatmentClassName_", LoginPretreatmentClassName == null ? "" : LoginPretreatmentClassName);
                System.setProperty("_LoginResultClassName_", LoginResultClassName == null ? "" : LoginResultClassName);
                System.setProperty("_LoginClientRunnable_", loginClientRunnable == null ? "" : loginClientRunnable);
                   
            } else {
                System.setProperty("_LoginPretreatmentClassName_", "");
                System.setProperty("_LoginResultClassName_", "");
                System.setProperty("_LoginClientRunnable_", "");
                          }
            // 登录预处理
            loginPretreatment(lsb);
            // 验证输入
            validateLSB(lsb);
            /******在线人数控制 add by wb 2009-7-6 11:50:22*************/
            validataLoginUsers(lsb.getPk_crop());
            /******************* end **********************************/
            // // 提交登录请求：
            if (serverURL == null) {
                serverURL = getSysURL();
            }
            objs = login0(lsb, serverURL);
            resultCode = ((Integer) objs[0]).intValue();
            if (resultCode == ILoginResult.ALREADY_ONLINE) {
                if (MessageDialog.showOkCancelDlg(ClientAssistant.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000058")/* @res "提示" */, nc.ui.ml.NCLangRes.getInstance().getStrByID("sysframev5", "UPPsysframev5-000059")/* @res "该用户已在线，是否强制登录？" */) == UIDialog.ID_OK) {
                    lsb.setForcedLogin(true);
                    loginPretreatment(lsb);
                    objs = login0(lsb, serverURL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return objs;
    }

    /***
     * 在线人数控制
     * wb 2009-7-6 11:51:24
     * @throws Exception 
     */
    private void validataLoginUsers(String pk_corp) throws Exception {
    	ServerEnvironment se = null;
    	try 
    	{
    		se = getServerEnv(pk_corp);
    	} 
    	catch (Exception e) 
    	{
    		Logger.error("查询监控信息有误",e);
    		return;
    	}

    	//处理监控信息
    	if (se != null) {
    		LoginSessBean[] logins = getUserInfo(se.getLoginUsers());
			if(pk_corp!=null){
				logins = filterCorp(logins, pk_corp);
				if(logins!=null&&logins.length>=ItfUserCount.MaxLoginUser){
					throw new SecurityException("在线人数超过产品最高限制!",ISecurityConsts.USB_KEY_PASSWD_INVALID);
				}
			}
    	}
	}
    	
    	/**
    	 * 过滤掉非本公司的登录用户信息
    	 * 创建日期：(2002-10-11 9:55:33)
    	 * @return nc.vo.sm.login.LoginSessBean[]
    	 * @param loginAlls nc.vo.sm.login.LoginSessBean[]
    	 */
    	public static LoginSessBean[] filterCorp(LoginSessBean[] loginAlls, String pkCorp) {
    		Vector vec = new Vector();
    		if (loginAlls != null)
    			for (int i = 0; i < loginAlls.length; i++)
    				if (loginAlls[i].getPk_crop().equals(pkCorp))
    					vec.addElement(loginAlls[i]);
    		//组织返回值
    		LoginSessBean[] logins = null;
    		if (vec.size() != 0) {
    			logins = new LoginSessBean[vec.size()];
    			vec.copyInto(logins);
    		}
    		return logins;
    	}
    	
    /**
     * 获得服务器端运行环境
     * 创建日期：(01-6-27 12:57:12)
     */
    public static ServerEnvironment getServerEnv(String pk_corp) throws Exception {
    	ServerEnvironment se = null;
    	try {
    		se = SFServiceFacility.getServiceProviderService().getServerEnvByCorp(pk_corp);
    	} catch (Exception e) {
    		throw e;
    	}
    	return se;
    }
    
    /**
     * 获得登录用户信息
     * 创建日期：(01-6-27 9:48:03)
     * @return nc.vo.sm.login.LoginSessBean[]
     * @param map java.util.HashMap
     */
    public static LoginSessBean[] getUserInfo(Map map) {
    	LoginSessBean[] logins = null;
    	Iterator it = map.keySet().iterator();
    	Vector vecMap = new Vector();
    	while (it.hasNext()) {
    		Object key = it.next();
    		LoginSessBean lsb = (LoginSessBean)map.get(key);
    		if (lsb.isLogin())
    			vecMap.addElement(lsb);
    	}
    	if (vecMap.size() != 0) {
    		logins = new LoginSessBean[vecMap.size()];
    		vecMap.copyInto(logins);
    	}
    	return logins;
    }
    
	private Object[] login0(LoginSessBean lsb, URL serverURL) throws Exception {
        // NetObjectOutputStream netOut = null;
        // NetObjectInputStream netIn = null;
        Object[] objs = new Object[2];
        try {
            // URLConnection connection = null;
            // OutputStream out = null;
            // if (serverURL == null) {
            // serverURL = getSysURL();
            // }
            // connection = serverURL.openConnection();
            // connection.setDoOutput(true);
            // out = connection.getOutputStream();
            // netOut = new NetObjectOutputStream(out);
            // netOut.writeObject(lsb);
            // netOut.finish();
            // netOut.flush();
            // netOut.close();
            // netOut = null;
            // 读取登录结果：
            // netIn = new NetObjectInputStream(connection.getInputStream());
            //
            // Integer result = (Integer) netIn.readObject();
            // objs[0] = result;
            objs = SFServiceFacility.getSMVerifyService().login(lsb);
            int resultCode = ((Integer) objs[0]).intValue();
            if (resultCode == ILoginResult.LOGIN_SUCCESS) {
                // 进行注册公司
                System.setProperty("UserPKCorp", lsb.getPk_crop());
                // 进行注册用户PK
                System.setProperty("UserCode", lsb.getUserId());
                // 登录成功,刷新本地cookie：
                refreshCookie((LoginSessBean) objs[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return objs;
    }

    private void refreshCookie(LoginSessBean lsb) {
        String accountCode = lsb.getAccountId();
        String pk_corp = lsb.getPk_crop();
        String userCode = lsb.getUserCode();
        String langCode = lsb.getLanguage();
        LoginInfoCookie.getInstance().setAccountCode(accountCode);
        LoginInfoCookie.getInstance().setPk_corp(pk_corp);
        LoginInfoCookie.getInstance().setLangCode(langCode);
        LoginInfoCookie.getInstance().setUserCode(userCode);
        LoginInfoCookie.getInstance().writeToLocal();
    }

    private ILoginPretreatment getLoginPretreatment() {
        ILoginPretreatment pretreatement = null;
        String loginPretreatementClassName = System.getProperty("_LoginPretreatmentClassName_");// MyAppletStub.getInstance().getParameter("pretreatmentClassName");
        if (loginPretreatementClassName != null && loginPretreatementClassName.trim().length() > 0) {
            try {
                Class pretreatmentClass = Class.forName(loginPretreatementClassName);
                pretreatement = (ILoginPretreatment) pretreatmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pretreatement == null) {
            pretreatement = new DefaultLoginPretreatment();
            System.out.println("使用默认的登录预处理");
        }
        return pretreatement;
    }

    /**
     * 在登录前进行预处理。 创建日期：(2003-6-26 10:04:12)
     * 
     * @param lsb
     *            nc.vo.sm.login.LoginSessBean
     */
    private void loginPretreatment(LoginSessBean lsb) throws Exception {
        try {
            ILoginPretreatment pretreat = getLoginPretreatment();
            if (pretreat != null)
                pretreat.pretreatment(lsb);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * 此处插入方法说明。 创建日期：(2002-4-28 9:33:20)
     */
    private void setUpLoginInformtion(LoginSessBean lsb) {
        NCAppletStub appletStub = NCAppletStub.getInstance();
        appletStub.clear();
        appletStub.setParameter("LOGIN_TYPE", lsb.getUserType());
        appletStub.setParameter("USER_ID", lsb.getUserId());
        appletStub.setParameter("USER_CODE", lsb.getUserCode());
        appletStub.setParameter("USER_NAME", lsb.getUserName());
        appletStub.setParameter("ACC_ADM_TYPE", lsb.getAccAdmType() + "");
        appletStub.setParameter("USER_IP", lsb.getUserIp());
        appletStub.setParameter("ACCOUNT_ID", lsb.getAccountId());
        appletStub.setParameter("LANGUAGE", lsb.getLanguage());
        appletStub.setParameter("WORK_DATE", lsb.getWorkDate());
        appletStub.setParameter("CORP_ID", lsb.getPk_crop());
        // appletStub.setParameter("SERVER_IP", lsb.getServerName());
        // appletStub.setParameter("SERVER_PORT", lsb.getServerPort() + "");
        appletStub.setParameter("DS_NAME", lsb.getDataSourceName());
        // appletStub.setParameter("USER_WIDTH", lsb.getUserScreenWidth() + "");
        // appletStub.setParameter("USER_HEIGHT", lsb.getUserScreenHeight() +
        // "");
        int intHeight = ViewConstantTool.getDesktopHeightByUserScreenHeight(lsb.getUserScreenHeight());
        if (System.getProperty("nc.jstarter.start") != null)
            appletStub.setParameter("DESKTOP_HEIGHT", (intHeight + 100) + "");
        else
            appletStub.setParameter("DESKTOP_HEIGHT", intHeight + "");
        appletStub.setParameter("DESKTOP_WIDTH", ViewConstantTool.getDesktopWidthByUserScreenWidth(lsb.getUserScreenWidth()) + "");
        appletStub.setParameter("SID", lsb.getSID()); // 2002-11-13增加
    }

    public void showDesktop(LoginSessBean lsb) {
        String langcode = lsb.getLanguage();
        nc.vo.ml.Language selLang = NCLangRes.getInstance().getLanguage(langcode);
        nc.ui.ml.NCLangRes.getInstance().setCurrLanguage(selLang);

        setUpLoginInformtion(lsb);

        nc.ui.sm.cmenu.Desktop deskTop = new nc.ui.sm.cmenu.Desktop();
        deskTop.setLoginSessBean(lsb);
        deskTop.setStubToMySelf(NCAppletStub.getInstance());
        deskTop.init();
        deskTop.start();

        if (!lsb.isHideLogin()) {
            AbstractSpecialEffectPanel panel = null;
			Container parent = ClientAssistant.getApplet().getContentPane();
			parent.setLayout(new BorderLayout());
			if (AbstractSpecialEffectPanel.getSEID() != SpecialEffectConstant.SE_NOSPECEFFE) {
				Image img = getCurrImage(parent);
				panel = AbstractSpecialEffectPanel.getSEPanel(
						AbstractSpecialEffectPanel.getSEID(), img);
				if (panel != null) {
					JLayeredPane layer = ClientAssistant.getApplet()
							.getLayeredPane();
					layer.add(panel, new Integer(9999));
					layer.repaint();
				}
			}
			parent.removeAll();
			parent.add(deskTop, BorderLayout.CENTER);
			parent.validate();
			MyFocusManager.focusNextCompAsNeed(deskTop);
	        if (panel != null) {
	            new Thread(panel).start();
	        }
	    }else{
			deskTop.setBounds(0,0,0,0);
			JLayeredPane layer = ClientAssistant.getApplet().getLayeredPane();
			layer.add(deskTop, new Integer(10000));
			
		}


    }

    private void validateLSB(LoginSessBean lsb) throws Exception {
//        // workdate:
//        String workDate = lsb.getWorkDate();
//        if (workDate.equals("")) {
//        	throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000227")/* @res "您没有输入业务日期！" */);
//        }
//        nc.vo.pub.lang.UFDate date = new nc.vo.pub.lang.UFDate(workDate);
//        if (date.toString().equals("")) {
//            throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000228")/*
//                                                                                                         * @res "您输入的业务日期格式不合法，正确格式为yyyy-mm-dd！"
//                                                                                                         */);
//        }
//        // 只进行简单判断fgj2002-04-04
//        // 针对普通用户判断登录的日期是否在一个会计期间：
//        String pkCorp = lsb.getPk_crop();
//        if ((pkCorp != null) && (!pkCorp.trim().equals("")) && (!pkCorp.trim().equals(CommonConstant.GROUP_CODE))) {
//            try {
//                boolean isValideDate = false;
//                isValideDate = SFServiceFacility.getConfigService().isValidDate(date);
//                if (!isValideDate) {
//                    throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000229")/*
//                                                                                                                 * @res "您输入的业务日期不在系统定义的会计期间之内！"
//                                                                                                                 */);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new Exception(e.getMessage());
//                        
//            }
//        }

        String userCode = lsb.getUserCode();
        if (userCode == null || userCode.equals("")) {
            throw new Exception(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000231")/* @res "您没有输入用户编码！" */);
        }

    }

    private URL m_url = null;

    public URL getSysURL() {
        if (m_url == null) {
            String str = ClientAssistant.getSysURLContextString();
            StringBuffer sb = new StringBuffer(str);
            sb.append("service/LoginServlet");
            try {
                m_url = new URL(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return m_url;
    }

    private Image getCurrImage(Container parent) {
        int w = parent.getWidth();
        int h = parent.getHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = null;
        try {
            g2 = img.createGraphics();
            parent.paintAll(g2);
        } catch (Exception e) {
        } finally {
            if (g2 != null)
                g2.dispose();
        }

        return img;
    }
}