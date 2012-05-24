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
 * �˴���������˵���� �������ڣ�(2001-4-27 16:19:12)
 * 
 * @author������� �޸ģ��Լ̽�
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-7 10:15:12)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-11-15 19:41:52)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-6-24 11:36:28)
	 * 
	 * @return nc.ui.sm.cmenu.MainMenu
	 */
	public static Desktop getApplet() {
		return instance;// (Desktop) getCE().getDesktopApplet();
	}

	/**
	 * ���ع��ڴ�СӦ�ó������Ϣ��
	 * 
	 * @return ���ع��ڴ�СӦ�ó�����Ϣ���ַ�����
	 */
	public String getAppletInfo() {
		return "CustommenuTest\n" + "\n" + "�˴���������˵����\n"
				+ "�������ڣ�(2001-4-27 16:19:10)\n" + "@author�������\n" + "";
	}

	// /**
	// * ���� ButtonPanel1 ����ֵ��
	// *
	// * @return nc.ui.sm.cmenu.ButtonPanel
	// */
	// /* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2001-6-23 21:04:16)
	 * 
	 * @return nc.ui.pub.ClientEnvironment
	 */
	public static nc.ui.pub.ClientEnvironment getCE() {

		return ClientEnvironment.getInstance();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 13:01:25)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 15:13:24)
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
		// Ĭ�Ϸֱ���Ϊ800*600:
		return DESKTOP_HEIGHT_800;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 15:13:11)
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
		// Ĭ�Ϸֱ���Ϊ800*600:
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
	 * ���� JAppletContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-14 11:03:12)
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
	 * ���� MainPanel ����ֵ��
	 * 
	 * @return nc.ui.sm.cmenu.MainPanel
	 */
	// /* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 15:43:58)
	 * 
	 * @return int
	 */
	int getMainPanelHeight() {

		return getDesktopHeight() - 94;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 15:43:43)
	 * 
	 * @return int
	 */
	int getMainPanelWidth() {

		return getDesktopWidth() / 4;
	}

	// /**
	// * ���� MessageBasePanel ����ֵ��
	// *
	// * @return nc.ui.pub.beans.UIPanel
	// */
	// /* ���棺�˷������������ɡ� */
	// private nc.ui.pub.beans.UIPanel getMessageBasePanel() {
	// if (ivjMessageBasePanel == null) {
	// try {
	// ivjMessageBasePanel = new nc.ui.pub.beans.UIPanel();
	// ivjMessageBasePanel.setName("MessageBasePanel");
	// // ivjMessageBasePanel.setBackground(java.awt.Color.white);
	// ivjMessageBasePanel.setBackground(new Color(247, 247, 247));
	// // user code begin {1}
	// if (!(isSuperUser() || isSystemAdm())) {
	// // zsb update:���Ϲ̶��ļ��
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
	// * ���� MessagePanel ����ֵ��
	// *
	// * @return nc.ui.pub.msg.MessagePanel
	// */
	// /* ���棺�˷������������ɡ� */
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 12:53:46)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 15:13:24)
	 * 
	 * @return int
	 */
	public int getScreenHeight() {
		return ClientAssistant.getUserHeight();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-10 15:13:11)
	 * 
	 * @return int
	 */
	public int getScreenWidth() {
		return ClientAssistant.getUserWidth();
	}

	/**
	 * ��÷�������ַ�� �������ڣ�(2001-10-9 14:32:57)
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		System.out.println("--------- δ��׽�����쳣 ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ��СӦ�ó���
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
			// ���ý�����ʽ���
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
			// ��ʼ���ͻ��˻�����
			initClientEnvironment();
			//
			// /** ������־������� */
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
			// �޸�����
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
		/** ����ض��򣬲���Զ�̵������ܷ��� */
		if (!RuntimeEnv.getInstance().isDevelopMode())
			nc.servlet.call.NCPreformanceFrame.getShareNCPreformanceFrame()
					.init();
		
		// �ɷ������ʼ���¼�
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
						.getStrByID("smcomm", "UPP1005-000272")/* "��������" */,
				changeHint);
		dlg.showModal();
	}

	private void showvalidateHint(String hint) {
		MessageDialog.showHintDlg(this, null, hint);
	}

	/**
	 * ��ʼ���ͻ��˻������� �������ڣ�(2001-5-8 18:38:54)
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
		// ������Ϣ��
		nc.vo.sm.AccountVO account = new nc.vo.sm.AccountVO();
		account.setAccountCode(getParameter("ACCOUNT_ID"));
		ceSingleton.setAccount(account);
		//
		nc.vo.sm.config.Account ca = getConfigAccount(getParameter("ACCOUNT_ID"));
		ceSingleton.setConfigAccount(ca);
		// �Ƿ�̬������֤���û�
		IAConfVO conf = (IAConfVO) getLoginSessBean().get(
				IAConfVO.class.getName());
		if (conf != null)
			ceSingleton.setStaticUser(conf.isStaticPWDMode());
		// ��˾��Ϣ��
		nc.vo.bd.CorpVO corp = null;
		// �����û���
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
																	 * @res "����"
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
		// ϵͳ����Ա��
		else if (isSystemAdm()) {
			ceSingleton.setUserType(ClientEnvironment.SYSTEM_ADM);
		}
		// ���׹���Ա��¼��
		else if (isAccountAdm()) {
			ceSingleton.setUserType(ClientEnvironment.ACCOUNT_ADM);
			ceSingleton.setIsGroup(true);
			corp = new nc.vo.bd.CorpVO();
			corp.setPk_corp(nc.ui.pub.CommonMark.GROUP_CODE);
			corp.setUnitname(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"smcomm", "UPP1005-000005")/*
												 * @res "����"
												 */);
			corp.setUnitcode(nc.ui.pub.CommonMark.GROUP_CODE);
			ceSingleton.setCorporation(corp);
		}
		// ��ͨ�û���¼��
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
				// �û���Ϣ��
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
		// //������Ϣ��
		String language = getParameter("LANGUAGE");
		ceSingleton.setLanguage(language);

		ClientEnvironment.getInstance();
		// ceSingleton.setLanguage(getParameter("LANG_ID"));
		// ҵ�����ڣ�
		//UFDate workdate = new UFDate(getParameter("WORK_DATE"));
		UFDate workdate = ClientEnvironment.getServerTime().getDate();
		ceSingleton.setDate(workdate);
		System.setProperty("userlogintime", workdate.getMillis()+"");
		// ���ʱ����Ϣ��
		if (!isSystemAdm()) {
			// ���ٻ������ڼ�
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
			// ���Ʊ�2005-2-28+�����õ�¼��˾��Ĭ���ʲ�
			if (corp == null) {
				ceSingleton
						.removeValue(nc.ui.pub.ClientEnvironment.GLORGBOOKPK);
			} else {
				//XXX:����Զ�̵��ã���̨һ�β�ѯ������ֱ�ӻ�ȡ
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-4 18:22:09)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-5 10:07:39)
	 * 
	 * @return boolean
	 */
	public boolean isBusy() {
		return m_isBusy;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-4 18:22:09)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-4 18:22:09)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-9-4 18:22:09)
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
	// // ��ʾ������
	// if (keyCode == java.awt.event.KeyEvent.VK_F1 && modifiers == 0) {
	// String strUrl = getServerURL();
	// // ����URL����ʾ�����ļ���
	// try {
	// java.net.URL url = new java.net.URL(strUrl + "helpmain.jsp");
	// ClientEnvironment.getInstance().getDesktopApplet().getAppletContext().showDocument(url,
	// "_blank");
	// } catch (java.net.MalformedURLException ex) {
	// ex.printStackTrace();
	// nc.ui.pub.beans.MessageDialog.showErrorDlg(this,
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019")/*
	// * @res "����"
	// */, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm",
	// "UPP1005-000062")/*
	// * @res "�Ҳ��������ļ���"
	// */);
	// }
	// } else if (keyCode == KeyEvent.VK_F4 && modifiers == 2) {
	// System.out.println("ϵͳע��");
	// // ע����¼
	// int result = MessageDialog.showYesNoDlg(Desktop.getApplet(),
	// nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000143")/*
	// * @res "ע����¼"
	// */, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm",
	// "UPP1005-000144")/*
	// * @res "ע����¼���ر������Ѵ򿪵Ĺ��ܴ��ڣ���ȷ��Ҫע����¼��"
	// */);
	// if (result == MessageDialog.ID_YES) {
	// Desktop.getApplet().logout();
	// }
	// requestFocus();
	// }
	// // else if (keyCode == KeyEvent.VK_F4 && modifiers == 8) {
	// // System.out.println("ϵͳ�˳�");
	// // //�Ƴ�ϵͳ
	// // exitSystem();
	// // }
	//
	// }

	// public void keyReleased(java.awt.event.KeyEvent e) {
	// // int keyCode = e.getKeyCode();
	// // int modifiers = e.getModifiers();
	// // //��ʾ������
	// // if (keyCode == java.awt.event.KeyEvent.VK_F1) {
	// // System.out.println("F1 pressed in Desktop!");
	// // }
	// }

	// public void keyTyped(java.awt.event.KeyEvent e) {
	// }

	/**
	 * ͨ���������رտͻ��ˡ� �������ڣ�(2001-10-25 15:57:14)
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
	 * �˴����뷽��˵���� �������ڣ�(2001-11-15 19:41:52)
	 */
	public void logout() {
		/** һ��ע����������ʾ��ԴID�� */
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
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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
			System.err.println("javax.swing.JApplet �� main() �з����쳣");
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-9-5 10:07:39)
	 * 
	 * @param isBusy
	 *            boolean
	 */
	public synchronized void setBusy(boolean isBusy) {

		m_isBusy = isBusy;
		//
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2001-7-3 9:47:29)
	 */
	public void start() {
		Logger.debug("--------------------");
		Logger.debug("Desktop::start()");
		Logger.debug("--------------------");

		// ע��sid����Ϣ�������ڹص�ie����ʱ���ע���û��Ĳ�����Ϣ
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
	 * �˴����뷽��˵���� �������ڣ�(2001-7-3 10:31:12)
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
		// ���sid
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
		// ֹͣ�Զ�ˢ�½�����߳�
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
	 * ���� UIButton1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-5-14 9:45:14)
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

	// ����һ����������ʾ��ǰʹ�õ�����
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
	// //�Զ��رյ��̣߳�
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
	 * �˴����뷽��˵���� �������ڣ�(2003-7-16 20:16:38)
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
			// :0000:ϵͳ����:��������,0513:2003-05-14:2003-05-14:��������
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
				// ���Ʊ�ע�ͣ�����Ĭ������
				// String lang = stt.nextToken();

				if (accCode.equalsIgnoreCase(accountcode)) {
					vos.setAccountCode(accCode);
					vos.setAccountName(accName);
					vos.setDataSourceName(dsName);
					// ���Ʊ�ע�ͣ�����Ĭ������
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
	 * �˴����뷽��˵���� �������ڣ�(2003-5-29 9:33:19)
	 * 
	 * @return nc.vo.sm.login.LoginSessBean
	 */
	public nc.vo.sm.login.LoginSessBean getLoginSessBean() {
		return m_LoginSessBean;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-10-10 17:29:36)
	 * 
	 * @return nc.vo.sm.log.OperatelogVO
	 */
	public nc.vo.sm.log.OperatelogVO getOperateLog(FuncRegisterVO frVO) {
		nc.vo.sm.log.OperatelogVO log = null;

		if (nc.ui.sm.cmenu.Desktop.getApplet().isSystemAdm()) {
		}
		// �����û���
		else if (nc.ui.sm.cmenu.Desktop.getApplet().isSuperUser()) {
		}
		// ���׹���Ա����ͨ�û���
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
				// ȷ����־��¼����ϸ�̶ȣ�
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
	 * �˴����뷽��˵���� �������ڣ�(2001-11-15 19:41:52)
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
	 * �˴����뷽��˵���� �������ڣ�(2003-5-29 9:33:19)
	 * 
	 * @param newLoginSessBean
	 *            nc.vo.sm.login.LoginSessBean
	 */
	public void setLoginSessBean(nc.vo.sm.login.LoginSessBean newLoginSessBean) {
		m_LoginSessBean = newLoginSessBean;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-9-16 11:56:01)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusAccount(String text) {
		getStatusBar().getStatusAccount().setText(" " + text);

	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-9-16 11:56:57)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusCorp(String text) {
		getStatusBar().getStatusCorp().setText(" " + text);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-9-16 11:57:42)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusDate(String text) {
		getStatusBar().getStatusDate().setText(" " + text);
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-9-16 11:58:20)
	 * 
	 * @param text
	 *            java.lang.String
	 */
	public void setStatusUser(String text) {
		getStatusBar().getStatusUser().setText(" " + text);
	}

	/**
	 * ����һЩ��Ҫ�ڽ���������������̷߳ŵ����������ͳһ������ �������ڣ�(2003-5-15 19:47:11)
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
			// ����һ��ʱ���Զ��˳����߳�
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
	 * ���ʶ��ε�½���ô������ر����д򿪴��ڲ���ˢ�¹����� �������ڣ�(2004-12-30 15:35:39) ���ߣ����Ʊ�
	 * ����ǰ�����õ�ǰʹ�������ʲ�����������GLORGBOOKPK��
	 */
	public void login4GLZhangbu() {
		ValueObject vo = (ValueObject) getCE().getValue(
				ClientEnvironment.GLORGBOOKPK);
		if (vo == null) {
			// ��ǰʹ�������ʲ�Ϊ�գ�������
			return;
		}
		// 1 �ر����д򿪴���
		closeAllOpenWindow(true);

		// 2 ˢ�¹�����
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
	 * �ر����д򿪴���
	 */
	@SuppressWarnings("unchecked")
	public void closeAllOpenWindow(boolean forceClose) {
		// �رտͻ��˵����д�ģ�飺
		boolean close = closeOpenMainframe(forceClose);
		if (forceClose || close) {
			// �ر����е�����UIFrame:
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
			// ������Ϣ�Ի���
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