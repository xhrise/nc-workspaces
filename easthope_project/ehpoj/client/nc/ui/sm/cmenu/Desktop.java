package nc.ui.sm.cmenu;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import nc.bs.framework.common.RuntimeEnv;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.servlet.call.NCPreformanceFrame;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.WindowCleaner;
import nc.ui.pub.beans.ExtTabbedPane;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIComponentUtil;
import nc.ui.pub.style.DefaultStyle;
import nc.ui.pub.style.Style;
import nc.ui.sm.clientsetup.ClientSetupCache;
import nc.ui.sm.desktop.DesktopEvent;
import nc.ui.sm.desktop.DesktopListener;
import nc.ui.sm.desktop.DesktopPanel;
import nc.ui.sm.desktop.DesktopTopBar;
import nc.ui.sm.identityverify.ILoginClientRunnable;
import nc.ui.sm.log.OperateLog;
import nc.ui.sm.login.ClientAssistant;
import nc.ui.sm.login.Loader;
import nc.ui.sm.login.NCAppletStub;
import nc.ui.sm.nodepower.Util4Power;
import nc.ui.sm.sysfunc.action.HelpAction;
import nc.ui.sm.sysfunc.action.LogOutAction;
import nc.ui.sm.task.TaskGroupFactory;
import nc.ui.uap.sf.SFServiceHelper;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.bd.period.AccperiodVO;
import nc.vo.bd.period2.AccperiodmonthVO;
import nc.vo.ml.LanguageTranslatorFactor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.sm.UserVO;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.identityverify.IAConfVO;
import nc.vo.sm.login.ConfigParser;
import nc.vo.sm.login.IControlConstant;
import nc.vo.sm.login.IViewConstant;
import nc.vo.sm.login.LoginSessBean;

/**
 * 此处插入类型说明。 创建日期：(2001-4-27 16:19:12)
 * 
 * @author：贾玉淼 修改：赵继江
 */
@SuppressWarnings("serial")
public class Desktop extends JPanel// javax.swing.JApplet
		implements IViewConstant, IControlConstant {
	private static Desktop instance = null;
	
	private DesktopPanel desktopPanel = null;
	
	private DesktopTopBar topBar = null;
	private ILoginClientRunnable loginClientRun = null;
	// private nc.ui.pub.beans.UILabel ivjImageLabel = null;

	// private nc.ui.pub.beans.UIPanel ivjImagePanel = null;

	// private MainPanel ivjMainPanel = null;

	// private nc.ui.pub.beans.UIPanel ivjMessageBasePanel = null;

	// private ButtonPanel ivjButtonPanel = null;

	// private MessagePanel ivjMessagePanel = null;
	// private UIPanel ivjMessagePanel = null;

	// private UIPanel ivjNorthPanel = null;

	// private UIPanel ivjCenterPanel = null;

	private DesktopStatusBar ivjStatusBar = null;

	//
	private TaskGroupFactory taskGroupFactory = null;
	
	public TaskGroupFactory getTaskGroupFactory(){
		if(taskGroupFactory == null){
			taskGroupFactory = new TaskGroupFactory(getRootPane().getLayeredPane());
		}
		return taskGroupFactory;
	}




	//
	TimerThread m_timer = null;

	//
	// boolean m_isLegalLogin = false;
	boolean m_isBusy = false;

	//
	nc.ui.sm.log.OperateLog m_log = null;
	private boolean isInited = false;
	/**
	 * 此处插入方法说明。 创建日期：(2001-9-7 10:15:12)
	 */
	public void destroy() {
//		if (NCEnv.isDebug()) {
//			System.out.println("--------------------");
//			System.out.println("Desktop::destroy()");
//			System.out.println("--------------------");
//		}
	}

	public String getParameter(String name) {
		return NCAppletStub.getInstance().getParameter(name);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-15 19:41:52)
	 */
	void exitSystem() {

		try {
			ClientSetupCache.storeCurrentClientSetup();
			dispatchDesktopEvent(DesktopEvent.EXITDESKTOP);
			stop();
			removeUser();
//			new ClientDBFacade().stopServer();
			DBCacheFacade.destroyDBCache();
			Thread.sleep(500);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public AppletContext getAppletContext() {
		return ClientAssistant.getApplet().getAppletContext();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-6-24 11:36:28)
	 * 
	 * @return nc.ui.sm.cmenu.MainMenu
	 */
	public static Desktop getApplet() {
		return instance;// (Desktop) getCE().getDesktopApplet();
	}

	/**
	 * 返回关于此小应用程序的信息。
	 * 
	 * @return 返回关于此小应用程序信息的字符串。
	 */
	public String getAppletInfo() {
		return "CustommenuTest\n" + "\n" + "此处插入类型说明。\n"
				+ "创建日期：(2001-4-27 16:19:10)\n" + "@author：贾玉淼\n" + "";
	}

	// /**
	// * 返回 ButtonPanel1 特性值。
	// *
	// * @return nc.ui.sm.cmenu.ButtonPanel
	// */
	// /* 警告：此方法将重新生成。 */
	// public ButtonPanel getButtonPanel() {
	// if (ivjButtonPanel == null) {
	// try {
	// ivjButtonPanel = new nc.ui.sm.cmenu.ButtonPanel();
	// ivjButtonPanel.setName("ButtonPanel");
	// // user code begin {1}
	// ivjButtonPanel.setPreferredSize(new Dimension(100, 23));
	// // user code end
	// } catch (java.lang.Throwable ivjExc) {
	// // user code begin {2}
	// // user code end
	// handleException(ivjExc);
	// }
	// }
	// return ivjButtonPanel;
	// }

	/**
	 * 此处插入方法说明。 创建日期：(2001-6-23 21:04:16)
	 * 
	 * @return nc.ui.pub.ClientEnvironment
	 */
	public static nc.ui.pub.ClientEnvironment getCE() {

		return ClientEnvironment.getInstance();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 13:01:25)
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	// public nc.ui.pub.beans.UIPanel getCenterPanel() {
	//
	// if (ivjCenterPanel == null) {
	// ivjCenterPanel = new UIPanel();
	// ivjCenterPanel.setBorder(null);
	// ivjCenterPanel.setLayout(new BorderLayout());
	// UISplitPane splitpan = new UISplitPane(
	// UISplitPane.HORIZONTAL_SPLIT, getMainPanel(),
	// getMessageBasePanel());
	// splitpan.setDividerLocation(220);
	// splitpan.setDividerSize(1);
	// ivjCenterPanel.add(splitpan, "Center");
	// // ivjCenterPanel.add(getMainPanel(), "West");
	// // ivjCenterPanel.add(getMessageBasePanel(), "Center");
	// }
	// return ivjCenterPanel;
	// }
	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 15:13:24)
	 * 
	 * @return int
	 */
	public int getDesktopHeight() {

		String height = getParameter("DESKTOP_HEIGHT");
		if (height != null) {
			try {
				return new Integer(height).intValue();
			} catch (NumberFormatException e) {
			}
		}
		// 默认分辨率为800*600:
		return DESKTOP_HEIGHT_800;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 15:13:11)
	 * 
	 * @return int
	 */
	public int getDesktopWidth() {

		String width = getParameter("DESKTOP_WIDTH");
		if (width != null) {
			try {
				return new Integer(width).intValue();
			} catch (NumberFormatException e) {
				Logger.debug(e.getMessage());
//				if (NCEnv.isDebug()) {
//					e.printStackTrace();
//				}
			}
		}
		// 默认分辨率为800*600:
		return DESKTOP_WIDTH_800;
	}

	public ExtTabbedPane getDesktopTabbedPane() {
		return getDesktopPanel().getTabbedPane();
	}

	public DesktopTopBar getTopBar() {
		if (topBar == null) {
			topBar = new DesktopTopBar();

		}
		return topBar;
	}

	/**
	 * 返回 JAppletContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
	public DesktopPanel getDesktopPanel() {
		if (desktopPanel == null) {
			desktopPanel = new DesktopPanel();
			// try {
			// ivjJAppletContentPane = new javax.swing.JPanel();
			// ivjJAppletContentPane.setName("JAppletContentPane");
			// ivjJAppletContentPane.setLayout(new BorderLayout());
			// // user code begin {1}
			//
			// ivjJAppletContentPane.add(getNorthPanel(), "North");
			// //
			// if (isSystemAdm()) {
			// DESKTOP_VER = DESKTOP_OLD;
			// }
			// //
			// if (DESKTOP_VER == DESKTOP_OLD) {
			// getButtonPanel().add(getMainPanel().getMainButtonPanel(),
			// "West");
			// ivjJAppletContentPane.add(getCenterPanel(), "Center");
			// } else if (DESKTOP_VER == DESKTOP_V30) {
			// getButtonPanel().add(
			// getDesktopCenterPanelV30().getBtnsPanel(), "West");
			// ivjJAppletContentPane.add(getDesktopCenterPanelV30(),
			// "Center");
			// }
			// ivjJAppletContentPane.add(getStatusBar(), "South");
			// // user code end
			// } catch (java.lang.Throwable ivjExc) {
			// // user code begin {2}
			// // user code end
			// handleException(ivjExc);
			// }
		}
		return desktopPanel;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-14 11:03:12)
	 * 
	 * @return nc.ui.sm.log.OperateLog
	 */
	public nc.ui.sm.log.OperateLog getLog() {

		if (m_log == null) {
			m_log = new nc.ui.sm.log.OperateLog();
		}
		return m_log;
	}

	/**
	 * 返回 MainPanel 特性值。
	 * 
	 * @return nc.ui.sm.cmenu.MainPanel
	 */
	// /* 警告：此方法将重新生成。 */
	// public MainPanel getMainPanel() {
	// if (ivjMainPanel == null) {
	// try {
	// ivjMainPanel = new nc.ui.sm.cmenu.MainPanel();
	// ivjMainPanel.setName("MainPanel");
	// // ivjMainPanel.setBackground(java.awt.Color.white);
	// // ivjMainPanel.setBackground(new Color(233, 223, 223));
	// // user code begin {1}
	// // ivjMainPanel.setPreferredSize(new
	// // Dimension(getMainPanelWidth(), getMainPanelHeight()));
	// // user code end
	// } catch (java.lang.Throwable ivjExc) {
	// // user code begin {2}
	// // user code end
	// handleException(ivjExc);
	// }
	// }
	// return ivjMainPanel;
	// }
	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 15:43:58)
	 * 
	 * @return int
	 */
	int getMainPanelHeight() {

		return getDesktopHeight() - 94;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 15:43:43)
	 * 
	 * @return int
	 */
	int getMainPanelWidth() {

		return getDesktopWidth() / 4;
	}

	// /**
	// * 返回 MessageBasePanel 特性值。
	// *
	// * @return nc.ui.pub.beans.UIPanel
	// */
	// /* 警告：此方法将重新生成。 */
	// private nc.ui.pub.beans.UIPanel getMessageBasePanel() {
	// if (ivjMessageBasePanel == null) {
	// try {
	// ivjMessageBasePanel = new nc.ui.pub.beans.UIPanel();
	// ivjMessageBasePanel.setName("MessageBasePanel");
	// // ivjMessageBasePanel.setBackground(java.awt.Color.white);
	// ivjMessageBasePanel.setBackground(new Color(247, 247, 247));
	// // user code begin {1}
	// if (!(isSuperUser() || isSystemAdm())) {
	// // zsb update:加上固定的间距
	// ivjMessageBasePanel.setLayout(new GridBagLayout());
	// java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
	// gbc.insets = new Insets(10, 10, 10, 10);
	// gbc.fill = GridBagConstraints.BOTH;
	// gbc.weightx = 1;
	// gbc.weighty = 1;
	// ivjMessageBasePanel.add(getMessagePanel(), gbc);
	// // ivjMessageBasePanel.setLayout(new BorderLayout());
	// // ivjMessageBasePanel.add(getMessagePanel(), "Center");
	// }
	// // zsb comment
	// // int width = getDesktopWidth() - getMainPanelWidth();
	// // int height = getMainPanelHeight();
	// // ivjMessageBasePanel.setPreferredSize(new Dimension(width,
	// // height));
	// // user code end
	// } catch (java.lang.Throwable ivjExc) {
	// // user code begin {2}
	// // user code end
	// handleException(ivjExc);
	// }
	// }
	// return ivjMessageBasePanel;
	// }

	// /**
	// * 返回 MessagePanel 特性值。
	// *
	// * @return nc.ui.pub.msg.MessagePanel
	// */
	// /* 警告：此方法将重新生成。 */
	// private nc.ui.pub.beans.UIPanel getMessagePanel() {
	// if (ivjMessagePanel == null) {
	// try {
	// ivjMessagePanel = (UIPanel) Class.forName(
	// "nc.ui.pub.msg.MessagePanel").newInstance();
	// ivjMessagePanel.setName("MessagePanel");
	// // ivjMessagePanel.setBackground(java.awt.Color.white);
	// ivjMessagePanel.setBackground(new Color(247, 247, 247));
	// // user code begin {1}
	// // ivjMessagePanel.setUserPK(getParameter("USER_ID"));
	// // ivjMessagePanel.setCorpPK(getParameter("CORP_ID"));
	// // ivjMessagePanel.addPFMessageListener(this);
	// System.out.println("message panel loaded...");
	// // user code end
	// } catch (java.lang.Throwable ivjExc) {
	// // user code begin {2}
	// // user code end
	// handleException(ivjExc);
	// }
	// }
	// return ivjMessagePanel;
	// }

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 12:53:46)
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	// public nc.ui.pub.beans.UIPanel getNorthPanel() {
	// if (ivjNorthPanel == null) {
	// ivjNorthPanel = new UIPanel();
	// ivjNorthPanel.setLayout(new BorderLayout());
	// ivjNorthPanel.add(getImagePanel(), "Center");
	// ivjNorthPanel.add(getButtonPanel(), "South");
	// ivjNorthPanel.setPreferredSize(new Dimension(getDesktopWidth(),
	// getNorthPanelHeight()));
	// }
	// return ivjNorthPanel;
	// }
	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 15:13:24)
	 * 
	 * @return int
	 */
	public int getScreenHeight() {
		return ClientAssistant.getUserHeight();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-10 15:13:11)
	 * 
	 * @return int
	 */
	public int getScreenWidth() {
		return ClientAssistant.getUserWidth();
	}

	/**
	 * 获得服务器地址。 创建日期：(2001-10-9 14:32:57)
	 * 
	 * @return java.lang.String
	 */
	public String getServerURL() {

		StringBuffer sb = new StringBuffer();
		sb.append(getParameter("SCHEME"));
		sb.append("://");
		sb.append(getParameter("SERVER_IP"));
		sb.append(":");
		sb.append(getParameter("SERVER_PORT"));
		sb.append(getParameter("CONTEXT_PATH"));
		sb.append("/");
		return sb.toString();
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		System.out.println("--------- 未捕捉到的异常 ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * 初始化小应用程序。
	 * 
	 * @see #start
	 * @see #stop
	 * @see #destroy
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		try {
			// getCE().setDesktopApplet(this);
			instance = this;
			// 设置界面样式风格：
			new DefaultStyle();
			// nc.ui.pub.style.Style.getCurrentStyle();
			String strLoginClientRunnable =System.getProperty("_LoginClientRunnable_");
			if(strLoginClientRunnable != null && strLoginClientRunnable.trim().length() > 0){
	            try {
	                Class cls = Class.forName(strLoginClientRunnable);
	                loginClientRun = (ILoginClientRunnable) cls.newInstance();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
			}

			// start thread
			startThread();
			// check this session is valid:
			// if (isUser()) {
			// nc.ui.pub.style.Style.getCurrentStyle(getParameter("USER_ID"));
			// } else {
			// }
			// 初始化客户端环境：
			initClientEnvironment();
			//
			// /** 设置日志输出级别 */
			NCPreformanceFrame.getShareNCPreformanceFrame()
					.setDefaultLogLevel();
			NCPreformanceFrame.getShareNCPreformanceFrame()
					.setCbWriteToFileSelState();
			// /////////////////////////////////


			// user code end
			setName("Desktop");
			// setSize(getDesktopWidth(), getDesktopHeight());
			setSize(ClientAssistant.getApplet().getSize());
			setPreferredSize(ClientAssistant.getApplet().getSize());

			setLayout(new BorderLayout());
			if (!ClientAssistant.isPortalClient()) {
				add(getTopBar(), BorderLayout.NORTH);
			}

			add(getDesktopPanel(), BorderLayout.CENTER);
			// setContentPane(getDesktopPanel());
			// 修改密码
			// Integer pwdTypeInteger = (Integer)
			// getLoginSessBean().get("UserPwdType");
			// int pwdtype = pwdTypeInteger == null ? UserVO.PWDTYPE_UNLIMIT :
			// pwdTypeInteger.intValue();
			String validateResult = (String) getLoginSessBean().get(
					"UserPwdValidateResult");
			String validateHintTip = (String) getLoginSessBean().get(
					"validateHintTip");

			if (validateResult != null && !validateResult.equals("ok")) {
				updatePwd(validateResult);
			}

			if (validateResult != null
					&& !validateHintTip.equalsIgnoreCase("noMessage")) {
				showvalidateHint(validateHintTip);
			}

		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
		regiKeyAction();
		/** 输出重定向，并且远程调用性能分析 */
		if (!RuntimeEnv.getInstance().isDevelopMode())
			nc.servlet.call.NCPreformanceFrame.getShareNCPreformanceFrame()
					.init();
		
		// 派发桌面初始化事件
		dispatchDesktopEvent(DesktopEvent.ENTERDESKTOP);
		isInited = true;
	}

	private void regiKeyAction() {
		InputMap input = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap action = getActionMap();
		// F10, logout
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "logout");
		action.put("logout", new LogOutAction());
		// F1 help
		input.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "help");
		action.put("help", new HelpAction());

	}

	private void updatePwd(String changeHint) {
		UserPwdUpdateDlg dlg = new UserPwdUpdateDlg(
				ClientAssistant.getApplet(), NCLangRes.getInstance()
						.getStrByID("smcomm", "UPP1005-000272")/* "更新密码" */,
				changeHint);
		dlg.showModal();
	}

	private void showvalidateHint(String hint) {
		MessageDialog.showHintDlg(this, null, hint);
	}

	/**
	 * 初始化客户端环境变量 创建日期：(2001-5-8 18:38:54)
	 */
	private void initClientEnvironment() {
		nc.ui.pub.ClientEnvironment ceSingleton = nc.ui.pub.ClientEnvironment
				.getInstance();
		// ceSingleton.setDesktopApplet(this);
		System.out.println("init ClientEnvironment!");
		java.applet.Applet app = ClientAssistant.getApplet();// ClientEnvironment.getInstance().getDesktopApplet();
		System.setProperty("MIDDLE_WARE_URL", app.getCodeBase().getHost()
				+ ":3001");
		// /////////////////
		// 帐套信息：
		nc.vo.sm.AccountVO account = new nc.vo.sm.AccountVO();
		account.setAccountCode(getParameter("ACCOUNT_ID"));
		ceSingleton.setAccount(account);
		//
		nc.vo.sm.config.Account ca = getConfigAccount(getParameter("ACCOUNT_ID"));
		ceSingleton.setConfigAccount(ca);
		// 是否静态密码认证的用户
		IAConfVO conf = (IAConfVO) getLoginSessBean().get(
				IAConfVO.class.getName());
		if (conf != null)
			ceSingleton.setStaticUser(conf.isStaticPWDMode());
		// 公司信息：
		nc.vo.bd.CorpVO corp = null;
		// 超级用户：
		if (isSuperUser()) {
			ceSingleton.setUserType(ClientEnvironment.SUPER_USER);
			try {
				if (getParameter("CORP_ID").equals("")
						|| getParameter("CORP_ID").trim().equals(
								nc.ui.pub.CommonMark.GROUP_CODE)) {
					corp = new nc.vo.bd.CorpVO();
					corp.setPrimaryKey(nc.ui.pub.CommonMark.GROUP_CODE);
					corp.setUnitname(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("smcomm", "UPP1005-000005")/*
																	 * @res "集团"
																	 */);
				} else {
					// modified by leijun
					// corp =
					// nc.ui.bd.CorpBO_Client.findByPrimaryKey(getParameter("CORP_ID"));
					corp = (nc.vo.bd.CorpVO) getLoginSessBean().get("CORPVO");
					if (corp == null)
						corp = SFServiceHelper.getCorpQryService()
								.findCorpVOByPK(getParameter("CORP_ID"));
				}
			} catch (Exception e) {
			}
			if (corp != null) {
				ceSingleton.setCorporation(corp);
			}
		}
		// 系统管理员：
		else if (isSystemAdm()) {
			ceSingleton.setUserType(ClientEnvironment.SYSTEM_ADM);
		}
		// 帐套管理员登录：
		else if (isAccountAdm()) {
			ceSingleton.setUserType(ClientEnvironment.ACCOUNT_ADM);
			ceSingleton.setIsGroup(true);
			corp = new nc.vo.bd.CorpVO();
			corp.setPk_corp(nc.ui.pub.CommonMark.GROUP_CODE);
			corp.setUnitname(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"smcomm", "UPP1005-000005")/*
												 * @res "集团"
												 */);
			corp.setUnitcode(nc.ui.pub.CommonMark.GROUP_CODE);
			ceSingleton.setCorporation(corp);
		}
		// 普通用户登录：
		else if (isUser()) {
			ceSingleton.setUserType(ClientEnvironment.USER);
			ceSingleton.setIsGroup(false);
			try {
				// modified by leijun
				// corp =
				// nc.ui.bd.CorpBO_Client.findByPrimaryKey(getParameter("CORP_ID"));
				corp = (nc.vo.bd.CorpVO) getLoginSessBean().get("CORPVO");
				if (corp == null)
					corp = SFServiceHelper.getCorpQryService().findCorpVOByPK(
							getParameter("CORP_ID"));

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (corp != null) {
				ceSingleton.setCorporation(corp);
				// 用户信息：
				UserVO user = null;
				try {
					// modified by leijun
					// user =
					// nc.ui.sm.user.UserBO_Client.findUserByPrimaryKey(getParameter("USER_ID"));
					user = (UserVO) getLoginSessBean().get("USERVO");
					if (user == null)
						user = SFServiceFacility.getUserQueryService().getUser(
								getParameter("USER_ID"));

				} catch (Exception e) {
					e.printStackTrace();
				}
				if (user != null) {
					ceSingleton.setUser(user);
				}
			}
		}
		//
		if (!isUser()) {
			UserVO user = (UserVO) getLoginSessBean().get("USERVO");
			if (user == null) {
				user = new UserVO();
				user.setPrimaryKey(getParameter("USER_ID"));
				user.setUserCode(getParameter("USER_CODE"));
				user.setUserName(getParameter("USER_NAME"));

			}
			ceSingleton.setUser(user);
		}
		// //语种信息：
		String language = getParameter("LANGUAGE");
		ceSingleton.setLanguage(language);

		ClientEnvironment.getInstance();
		// ceSingleton.setLanguage(getParameter("LANG_ID"));
		// 业务日期：
		//UFDate workdate = new UFDate(getParameter("WORK_DATE"));
		UFDate workdate = ClientEnvironment.getServerTime().getDate();
		ceSingleton.setDate(workdate);
		System.setProperty("userlogintime", workdate.getMillis()+"");
		// 会计时间信息：
		if (!isSystemAdm()) {
			// 不再缓存会计期间
			// try {
			// modified by leijun 10/10/2003

			// accPeriod =
			// nc.ui.bd.pub.CallPeriod.findByUFDate(ceSingleton.getDate());
			// AccountCalendar calendar = AccountCalendar.getInstance();
			// calendar.setDate( ceSingleton.getDate());
			//
			// AccperiodVO accPeriod = calendar.getAccperiodVO();
			// ceSingleton.setAccountPeriodVO(accPeriod);
			// ceSingleton.setAccountYear(accPeriod.getPeriodyear());
			// ceSingleton.setAccountMonth(calendar.getAccperiodmonthVO().getMonth());
			// ceSingleton.setStartDateOfAccountPeriod(accPeriod
			// .getBegindate());
			// ceSingleton.setEndDateOfAccountPeriod(accPeriod
			// .getEnddate());
			// ceSingleton.setAccountPeriod(accPeriod.getBegindate());

			//				
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// 周善保2005-2-28+：设置登录公司的默认帐簿
			if (corp == null) {
				ceSingleton
						.removeValue(nc.ui.pub.ClientEnvironment.GLORGBOOKPK);
			} else {
				//XXX:减少远程调用，后台一次查询后这里直接获取
				AccperiodVO yearVo = (AccperiodVO) getLoginSessBean().get("year");
				AccperiodmonthVO monthVo = (AccperiodmonthVO) getLoginSessBean().get("month");
				ceSingleton.setYearVo(yearVo);
				ceSingleton.setMonthVO(monthVo);
				
				GlorgbookVO poweredBookVo = (GlorgbookVO) getLoginSessBean().get("poweredglorgbook");
				AccperiodVO yearVoOfPoweredBook = (AccperiodVO) getLoginSessBean().get("yearOfpoweredglorgbook");
				AccperiodmonthVO monthVoOfPoweredBook = (AccperiodmonthVO) getLoginSessBean().get("monthOfpoweredglorgbook");
				if(poweredBookVo == null) {
					ceSingleton.removeValue(ClientEnvironment.GLORGBOOKPK);
					Desktop.getApplet().setStatusCorp(
							Desktop.getApplet().getStatusLoginCorpText());
				}else {
					Util4Power.setGlorgBookToEnv2(poweredBookVo, yearVoOfPoweredBook, monthVoOfPoweredBook);
				}
				
//				String sGlorgbookPK = Util4Power.getZBPkByCorpPK(corp
//						.getPk_corp());
//				if (sGlorgbookPK == null) {
//					ceSingleton.removeValue(ClientEnvironment.GLORGBOOKPK);
//					Desktop.getApplet().setStatusCorp(
//							Desktop.getApplet().getStatusLoginCorpText());
//				} else {
//					GlorgbookVO vo = BDGLOrgBookAccessor
//							.getGlOrgBookVOByPrimaryKey(sGlorgbookPK);
//					Util4Power.setGlorgBookToEnv(vo);
//				}
			}

		}

		try {
			ceSingleton.setServerURL(new URL(getServerURL()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-4 18:22:09)
	 * 
	 * @return boolean
	 */
	public boolean isAccountAdm() {

		// if
		// (nc.vo.sm.login.LoginSessBean.ACCOUNT_ADM.equals(getParameter("LOGIN_TYPE")))
		// {
		// return true;
		// }
		if (getLoginSessBean().getUserType().equals(LoginSessBean.ACCOUNT_ADM))
			return true;
		else
			return false;
	}

	public boolean isGroupUser() {
		if (getLoginSessBean().getUserType().equals(LoginSessBean.ACCOUNT_ADM)
				&& getLoginSessBean().getAccAdmType() == LoginSessBean.ACCOUNT_ADM_FROM_DATABASE)
			return true;
		else
			return false;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-5 10:07:39)
	 * 
	 * @return boolean
	 */
	public boolean isBusy() {
		return m_isBusy;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-4 18:22:09)
	 * 
	 * @return boolean
	 */
	boolean isSuperUser() {

		if (nc.vo.sm.login.LoginSessBean.SUPER_USER
				.equals(getParameter("LOGIN_TYPE"))) {
			return true;
		}
		return false;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-4 18:22:09)
	 * 
	 * @return boolean
	 */
	public boolean isSystemAdm() {

		if (nc.vo.sm.login.LoginSessBean.SYSTEM_ADM
				.equals(getParameter("LOGIN_TYPE"))) {
			return true;
		}
		return false;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-4 18:22:09)
	 * 
	 * @return boolean
	 */
	public boolean isUser() {
		if (nc.vo.sm.login.LoginSessBean.USER
				.equals(getParameter("LOGIN_TYPE"))) {
			return true;
		}
		return false;
	}

	public void insertTabToWorkaround(String title, Icon icon,
			Component component, String tip, int index) {

		ExtTabbedPane tabbedPanel = getDesktopTabbedPane();
		int count = tabbedPanel.getTabCount();
		if (index < 0 || index > count) {
			index = count;
		}
		tabbedPanel.insertTab(title, icon, component, tip, index, true);
	}

	// public void keyPressed(java.awt.event.KeyEvent e) {
	// int keyCode = e.getKeyCode();
	// int modifiers = e.getModifiers();
	// System.out.println("keyCode=" + keyCode + "/modifiers=" + modifiers);
	//
	// // 显示帮助：
	// if (keyCode == java.awt.event.KeyEvent.VK_F1 && modifiers == 0) {
	// String strUrl = getServerURL();
	// // 构造URL，显示帮助文件：
	// try {
	// java.net.URL url = new java.net.URL(strUrl + "helpmain.jsp");
	// ClientEnvironment.getInstance().getDesktopApplet().getAppletContext().showDocument(url,
	// "_blank");
	// } catch (java.net.MalformedURLException ex) {
	// ex.printStackTrace();
	// nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019")/*
	// * @res "错误"
	// */, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm",
	// "UPP1005-000062")/*
	// * @res "找不到帮助文件！"
	// */);
	// }
	// } else if (keyCode == KeyEvent.VK_F4 && modifiers == 2) {
	// System.out.println("系统注销");
	// // 注销登录
	// int result = MessageDialog.showYesNoDlg(Desktop.getApplet(),
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000143")/*
	// * @res "注销登录"
	// */, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm",
	// "UPP1005-000144")/*
	// * @res "注销登录将关闭所有已打开的功能窗口，您确定要注销登录吗？"
	// */);
	// if (result == MessageDialog.ID_YES) {
	// Desktop.getApplet().logout();
	// }
	// requestFocus();
	// }
	// // else if (keyCode == KeyEvent.VK_F4 && modifiers == 8) {
	// // System.out.println("系统退出");
	// // //推出系统
	// // exitSystem();
	// // }
	//
	// }

	// public void keyReleased(java.awt.event.KeyEvent e) {
	// // int keyCode = e.getKeyCode();
	// // int modifiers = e.getModifiers();
	// // //显示帮助：
	// // if (keyCode == java.awt.event.KeyEvent.VK_F1) {
	// // System.out.println("F1 pressed in Desktop!");
	// // }
	// }

	// public void keyTyped(java.awt.event.KeyEvent e) {
	// }

	/**
	 * 通过本方法关闭客户端。 创建日期：(2001-10-25 15:57:14)
	 */
	public void kill() {
		try {
			stop();
			Thread.sleep(500);
			// //////
			String loginUIType = getParameter("LoginUIType");
			javax.swing.JPanel container = (javax.swing.JPanel) getParent();
			mySelfAppletStub.clear();
			UIComponentUtil.removeAllComponentRefrence(container);
			container.removeAll();
			Loader.showLoginUI(ClientAssistant.getApplet(), loginUIType);
			// ///////
			// getAppletContext().showDocument(new URL(getServerURL() +
			// "servlet/nc.bs.sm.login.Index"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-15 19:41:52)
	 */
	public void logout() {
		/** 一旦注销，不再显示资源ID号 */
		LanguageTranslatorFactor.getInstance().setNullTrans(false);
		boolean isClosed = closeOpenMainframe(false);
		if (!isClosed)
			return;
		try {
			dispatchDesktopEvent(DesktopEvent.LOGOUTDESKTOP);
			stop();
			removeUser();
			Thread.sleep(500);
			// ///////
			String loginUIType = getParameter("LoginUIType");
			mySelfAppletStub.clear();
			Container container = ClientAssistant.getApplet().getContentPane();
			UIComponentUtil.removeAllComponentRefrence(container);
			container.removeAll();
			String ssoKey = getParameter("LOGINKEY");
			if (ssoKey != null && ssoKey.trim().length() > 0) {
				System.exit(0);
			} else {
				Loader.showLoginUI(ClientAssistant.getApplet(), loginUIType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	@SuppressWarnings("unchecked")
	public static void main(java.lang.String[] args) {
		try {
			javax.swing.JFrame frame = new javax.swing.JFrame();
			Desktop aDesktop;
			Class iiCls = Class.forName("nc.ui.sm.cmenu.Desktop");
			ClassLoader iiClsLoader = iiCls.getClassLoader();
			aDesktop = (Desktop) java.beans.Beans.instantiate(iiClsLoader,
					"nc.ui.sm.cmenu.Desktop");
			frame.getContentPane().add("Center", aDesktop);
			frame.setSize(aDesktop.getSize());
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			// frame.show();
			java.awt.Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("javax.swing.JApplet 的 main() 中发生异常");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-9-5 10:07:39)
	 * 
	 * @param isBusy
	 *            boolean
	 */
	public synchronized void setBusy(boolean isBusy) {

		m_isBusy = isBusy;
		//
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-3 9:47:29)
	 */
	public void start() {
		Logger.debug("--------------------");
		Logger.debug("Desktop::start()");
		Logger.debug("--------------------");

		// 注册sid等信息，用于在关掉ie窗口时获得注销用户的参数信息
		String sid = getParameter("SID");
		String userId = getParameter("USER_ID");
		String dsname = getParameter("DS_NAME");
		System.setProperty("SID", sid);
		System.setProperty("USERID", userId);
		System.setProperty("DSNAME", dsname);

		// System.out.println("sid=" + sid);
		// System.out.println("USER_ID=" + userId);
		// System.out.println("dsname=" + dsname);
		// System.out.println("corpid=" + getParameter("CORP_ID"));
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-7-3 10:31:12)
	 */
	public void stop() {
		if(taskGroupFactory != null){
			taskGroupFactory.removeAllTaskGroup();
		}
		ClientSetupCache.storeCurrentClientSetup();

		closeAllOpenWindow(true);
//		if (NCEnv.isDebug()) {
			Logger.debug("--------------------");
			Logger.debug("Desktop::stop()");
			Logger.debug("--------------------");
//		}

		// if(ivjMessagePanel != null)
		// ivjMessagePanel.stopReceiveMessage();
		// 清空sid
		System.setProperty("SID", "");
		System.setProperty("DSNAME", "");
		System.setProperty("USERID", "");
		System.getProperties().remove("SID");
		System.getProperties().remove("DSNAME");
		System.getProperties().remove("USERID");
		
		System.setProperty("userlogintime", "");
		System.getProperties().remove("userlogintime");
		
		
		try{
			if(loginClientRun != null){
				loginClientRun.stopRun();
				loginClientRun = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		// 停止自动刷新界面的线程
		// nc.servlet.call.FrasherThread.getInstance().removeRootComponet(this);
		// if (!getCE().isInDebug()) {
		try {
			if (m_timer != null) {
				m_timer.kill();
				m_timer = null;
			}
			if (m_autoLogoutThread != null) {

				m_autoLogoutThread.kill();
				m_autoLogoutThread = null;
			}
		} catch (Exception e) {
		}

		//
		nc.ui.pub.ClientEnvironment.newInstance();
		// getCE().setDesktopApplet(null);
		instance = null;
		isInited = false;
	}

	//
	private nc.ui.sm.login.NCAppletStub mySelfAppletStub;

	/**
	 * 返回 UIButton1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */

	/**
	 * 此处插入方法说明。 创建日期：(2002-5-14 9:45:14)
	 */
	public void removeUser() {
		String userId = getParameter("USER_ID");
		String dsname = getParameter("DS_NAME");
		String userSID = getParameter("SID");
		try {
			SFServiceFacility.getSMVerifyService().userLogout(dsname, userId,
					userSID);
		} catch (Exception e) {
			System.out.println("==========removeUser exception==========");
			System.out.println("DSName:" + dsname);
			System.out.println("UserID:" + userId);
			System.out.println("========================================");
			e.printStackTrace();

		}

	}

	public final void setStubToMySelf(AppletStub stub) {
		mySelfAppletStub = (nc.ui.sm.login.NCAppletStub) stub;
		// super.setStub(stub);
	}

	// 设置一个变量来标示当前使用的桌面
	public static int DESKTOP_OLD = 0;

	public static int DESKTOP_V30 = 1;

	public static int DESKTOP_VER = DESKTOP_OLD;

	//
	AutoLogoutThread m_autoLogoutThread = null;

	//
	// private DesktopCenterPanelV30 m_DesktopCenterPanelV30 = null;

	//
	// private class LogoutListener extends java.awt.event.MouseAdapter
	// implements MouseMotionListener {
	// boolean isButtonShown = false;
	// boolean isButtonShowing = false;
	// int showTimeCounter = 0;
	// public void mouseEntered(MouseEvent e) {
	// if (!isButtonShown) {
	// if (e.getPoint().getX() > getDesktopWidth() - getMainBarHeight() * 2 + 5)
	// {
	// showButton();
	// }
	// }
	// }
	// public void mouseMoved(MouseEvent e) {
	// if (!isButtonShown) {
	// if (e.getPoint().getX() > getDesktopWidth() - getMainBarHeight() * 2 + 5)
	// {
	// showButton();
	// }
	// }
	// }
	// public void mouseDragged(MouseEvent e) {
	// }
	// public synchronized void showButton() {
	// Runnable run = new Runnable() {
	// public void run() {
	// if (isButtonShowing) {
	// return;
	// }
	// isButtonShowing = true;
	// getExitPanel().setVisible(true);
	// int width = getMainBarHeight() * 2;
	// for (int i = 0; i < 20; i++) {
	// getExitPanel().setBounds(getDesktopWidth() - width * i / 20, 0,
	// getMainBarHeight() * 2, getMainBarHeight());
	// getImageLabel().setSize(getDesktopWidth() - width * i / 20,
	// getMainBarHeight());
	// try {
	// Thread.currentThread().sleep(10);
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// isButtonShowing = false;
	// isButtonShown = true;
	// //自动关闭的线程：
	// Runnable run2 = new Runnable() {
	// public void run() {
	// while (isButtonShown) {
	// showTimeCounter++;
	// if (showTimeCounter > 100) {
	// hideButton();
	// }
	// //
	// try {
	// Thread.currentThread().sleep(50);
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// };
	// }
	// };
	// new Thread(run2).start();
	// }
	// };
	// new Thread(run).start();
	// }
	// public synchronized void hideButton() {
	// Runnable run = new Runnable() {
	// public void run() {
	// if (isButtonShowing) {
	// return;
	// }
	// getExitPanel().setVisible(false);
	// getImageLabel().setBounds(0, 0, getDesktopWidth(), getMainBarHeight());
	// getExitPanel().setBounds(getDesktopWidth(), 0, getMainBarHeight() * 2,
	// getMainBarHeight());
	// isButtonShown = false;
	// showTimeCounter = 0;
	// }
	// };
	// javax.swing.SwingUtilities.invokeLater(run);
	// }
	// }

	private nc.vo.sm.login.LoginSessBean m_LoginSessBean = null;

	/**
	 * 此处插入方法说明。 创建日期：(2003-7-16 20:16:38)
	 * 
	 * @return nc.vo.sm.config.Account
	 * @param accountcode
	 *            java.lang.String
	 */
	private nc.vo.sm.config.Account getConfigAccount(String accountcode) {
		System.out.println("--->accountcode = "+accountcode);
		String strAccountInfo = getParameter("ACCOUNT");
		if (strAccountInfo != null) {
			strAccountInfo = strAccountInfo.trim();
			// :0000:系统管理:简体中文,0513:2003-05-14:2003-05-14:简体中文
			java.util.StringTokenizer st = new java.util.StringTokenizer(
					strAccountInfo, ",");
			nc.vo.sm.config.Account vos = new nc.vo.sm.config.Account();
			while (st.hasMoreElements()) {
				String tmp = st.nextToken();
				if (tmp.startsWith(":"))
					tmp = "/" + tmp;
				StringTokenizer stt = new java.util.StringTokenizer(tmp, ":");
				String dsName = stt.nextToken();
				if (dsName.equals("/"))
					dsName = "";
				String accCode = stt.nextToken();
				String accName = stt.nextToken();
				// 周善保注释：不用默认语种
				// String lang = stt.nextToken();

				if (accCode.equalsIgnoreCase(accountcode)) {
					vos.setAccountCode(accCode);
					vos.setAccountName(accName);
					vos.setDataSourceName(dsName);
					// 周善保注释：不用默认语种
					// vos.setLang(lang);

					break;
				} else {
					continue;
				}
			}
			return vos;
		}else{
			nc.vo.sm.config.Account a =null;
			try {
				a = SFServiceFacility.getConfigService().getAccountByCode(accountcode);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
			return a;
		}
	}

	// public DesktopCenterPanelV30 getDesktopCenterPanelV30() {
	// if (m_DesktopCenterPanelV30 == null) {
	// m_DesktopCenterPanelV30 = new DesktopCenterPanelV30();
	// }
	// return m_DesktopCenterPanelV30;
	// }

	/**
	 * 此处插入方法说明。 创建日期：(2003-5-29 9:33:19)
	 * 
	 * @return nc.vo.sm.login.LoginSessBean
	 */
	public nc.vo.sm.login.LoginSessBean getLoginSessBean() {
		return m_LoginSessBean;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-10-10 17:29:36)
	 * 
	 * @return nc.vo.sm.log.OperatelogVO
	 */
	public nc.vo.sm.log.OperatelogVO getOperateLog(FuncRegisterVO frVO) {
		nc.vo.sm.log.OperatelogVO log = null;

		if (nc.ui.sm.cmenu.Desktop.getApplet().isSystemAdm()) {
		}
		// 超级用户：
		else if (nc.ui.sm.cmenu.Desktop.getApplet().isSuperUser()) {
		}
		// 帐套管理员和普通用户：
		else {
			log = new nc.vo.sm.log.OperatelogVO();
			try {
				String enterTime = ClientEnvironment.getServerTime().toString();// SFServiceFacility.getServiceProviderService().getServerTime().toString();
				log.setLoginTime(enterTime);

				log.setCompanyname(ClientEnvironment.getInstance()
						.getCorporation().getUnitname());
                log.setPKCorp(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());

				log.setOperatorId(ClientEnvironment.getInstance().getUser()
						.getPrimaryKey());
				// log.setEnterdate(
				// nc.ui.pub.services.ServiceProviderBO_Client.getServerTime().getDate());
				log.setEnterip(nc.ui.sm.cmenu.Desktop.getApplet().getParameter(
						"USER_IP"));
				// log.setEntertime(
				// nc.ui.pub.services.ServiceProviderBO_Client.getServerTime().getTime());
				log.setOperatetype(nc.vo.sm.log.OperatelogVO.ENTER_FUNCTION);
				log.setOpratorname(ClientEnvironment.getInstance().getUser()
						.getUserName());
				log.setEnterfunction(frVO.getFunName());
				log.setEnterfunctioncode(frVO.getFunCode());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			//
			if (log.getOperatetype().equals(
					nc.vo.sm.log.OperatelogVO.CLICK_BUTTON)) {
				// 确定日志记录的明细程度：
				if (!OperateLog.isNeedButtonLog()) {
					log = null;
				}
			}
		}
		//
		return log;
	}

	private DesktopStatusBar getStatusBar() {
		if (ivjStatusBar == null) {
			ivjStatusBar = new DesktopStatusBar(this);
		}
		return ivjStatusBar;
	}

	//
	public nc.ui.sm.login.AppletContainer getStubApplet() {
		return mySelfAppletStub.getAppletContainer();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2001-11-15 19:41:52)
	 */
	public void removeUserAndStop() {
		try {
			Style.refreshStyle();
			removeUser();
			stop();
//			new ClientDBFacade().stopServer();
			DBCacheFacade.destroyDBCache();
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 此处插入方法说明。 创建日期：(2003-5-29 9:33:19)
	 * 
	 * @param newLoginSessBean
	 *            nc.vo.sm.login.LoginSessBean
	 */
	public void setLoginSessBean(nc.vo.sm.login.LoginSessBean newLoginSessBean) {
		m_LoginSessBean = newLoginSessBean;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-16 11:56:01)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusAccount(String text) {
		getStatusBar().getStatusAccount().setText(" " + text);

	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-16 11:56:57)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusCorp(String text) {
		getStatusBar().getStatusCorp().setText(" " + text);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-16 11:57:42)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusDate(String text) {
		getStatusBar().getStatusDate().setText(" " + text);
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-9-16 11:58:20)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusUser(String text) {
		getStatusBar().getStatusUser().setText(" " + text);
	}

	/**
	 * 对于一些需要在进入桌面后启动的线程放到这个方法里统一启动。 创建日期：(2003-5-15 19:47:11)
	 */
	private void startThread() {
		try {
		if (isUser() || isAccountAdm()) {
//			nc.ui.dbcache.VersionCheckService.getInstance()
//					.startVersionChecker();
			DBCacheFacade.initDBCache();
			nc.ui.dbcache.RefreshWhenLogin.startRefresh(); 
		}
		}catch(Throwable th){
			Logger.error(th.getMessage(), th);
		}
		try{
			if(loginClientRun != null){
				loginClientRun.startRun();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		if (getLoginSessBean().isHideLogin())
			return;
		try {
			ConfigParser parser = (ConfigParser) getLoginSessBean().get(
					"CONFIGPARSER");
			if (parser == null)
				parser = SFServiceFacility.getConfigService().getConfigParser();

			m_timer = new TimerThread();
			if (parser.getClaimingInterval() > 500) {
				m_timer.setSleepTime(parser.getClaimingInterval() / 2);
			}
			m_timer.start();
			// 启动一段时间自动退出的线程
			int autoLogoutTime = parser.getAutoLogoutTime();
			if (autoLogoutTime > 0) {
				m_autoLogoutThread = new AutoLogoutThread(autoLogoutTime);
				m_autoLogoutThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 总帐二次登陆调用处理：将关闭所有打开窗口并且刷新功能树 创建日期：(2004-12-30 15:35:39) 作者：周善保
	 * 调用前先设置当前使用主体帐簿环境变量（GLORGBOOKPK）
	 */
	public void login4GLZhangbu() {
		ValueObject vo = (ValueObject) getCE().getValue(
				ClientEnvironment.GLORGBOOKPK);
		if (vo == null) {
			// 当前使用主体帐簿为空，不处理
			return;
		}
		// 1 关闭所有打开窗口
		closeAllOpenWindow(true);

		// 2 刷新功能树
		// getMainPanel().getCMenu().refreshMenuTree();
		getDesktopPanel().getFuncCtrlPanel().refreshPower();
	}

	@SuppressWarnings("unchecked")
	public boolean closeOpenMainframe(boolean forceClose) {
		List modules = getCE().getOpenModules();
		IFuncWindow[] openFuncs = (IFuncWindow[]) modules
				.toArray(new IFuncWindow[0]);
		boolean isClosed = true;
		for (int i = 0; i < openFuncs.length; i++) {
			if (forceClose) {
				openFuncs[i].forceCloseWindow();
			} else {
				isClosed = openFuncs[i].closeWindow();
				if (!isClosed)
					break;
			}
		}
		getDesktopPanel().getTabbedPane().validate();
		return isClosed;
	}

	/**
	 * 关闭所有打开窗口
	 */
	@SuppressWarnings("unchecked")
	public void closeAllOpenWindow(boolean forceClose) {
		// 关闭客户端的所有打开模块：
		boolean close = closeOpenMainframe(forceClose);
		if (forceClose || close) {
			// 关闭所有的其他UIFrame:
			Iterator it = null;//
			int pos = 0;
			LinkedList list = getCE().getOpenFrames();
			nc.ui.pub.beans.UIFrame[] frames = new nc.ui.pub.beans.UIFrame[list
					.size()];
			it = list.iterator();
			pos = 0;
			while (it.hasNext()) {
				frames[pos++] = (nc.ui.pub.beans.UIFrame) it.next();
			}
			for (int i = 0; i < frames.length; i++) {
				frames[i].setVisible(false);
				frames[i].dispose();
			}
			// 消除消息对话框：
			nc.ui.pub.msg.MessageSendDlg dlg = (nc.ui.pub.msg.MessageSendDlg) getCE()
					.getValue("nc.ui.pub.FramePanel.MESSAGE_SENDER");
			if (dlg != null) {
				dlg.destroy();
				getCE().removeValue("nc.ui.pub.FramePanel.MESSAGE_SENDER");
			}
			//
			WindowCleaner.windowCleaner(JOptionPane
					.getFrameForComponent(ClientAssistant.getApplet()));
		}

	}

	public String getStatusLoginCorpText() {
		String sText = "";
		String corpName = nc.ui.sm.login.LoginPanelRes
				.getDesktopStateCorpName();
		if (Desktop.getApplet().isSystemAdm())
			;
		else if (Desktop.getApplet().isAccountAdm()) {
			String groupName = nc.ui.sm.login.LoginPanelRes.getGroupName();
			sText = corpName + ":" + groupName;
		} else if (Desktop.getApplet().isUser()) {
			sText = corpName + ":" + getCE().getCorporation().getUnitname();
		}
		return sText;
	}

	private void dispatchDesktopEvent(int id) {
		if (!(isSuperUser() || isSystemAdm())) {
			DesktopEvent event = new DesktopEvent(this, id);
			DesktopListener[] listeners = DesktopListenerImplClass
					.getDesktopListener();
			int count = (listeners == null ? 0 : listeners.length);
			for (int i = 0; i < count; i++) {
				if (listeners[i] != null) { 
					Logger.debug("desktopLitener dispatch :"+listeners[i].getClass().getName());
					try {
						switch (id) {
						case DesktopEvent.ENTERDESKTOP:
							listeners[i].enterDesktop(event);
							break;
						case DesktopEvent.LOGOUTDESKTOP:
							listeners[i].logoutDesktop(event);
							break;
						case DesktopEvent.EXITDESKTOP:
							listeners[i].exitDesktop(event);
							break;
						default:
							break;
						}
					} catch (Throwable th) {
						th.printStackTrace();
					}
				}
			}
		}
	}
	public void showMessageHint(String msg){
		showMessageHint(msg, null);
	}
	
	public void showMessageHint(String msg, Action action){
		MessageHintWindow.getInstance().showWindow(msg, action);
	}

	public boolean isInited() {
		return isInited;
	}
}