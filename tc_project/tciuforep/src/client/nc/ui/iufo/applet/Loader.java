package nc.ui.iufo.applet;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import nc.bs.framework.ServiceConfig;
import nc.bs.framework.common.RuntimeEnv;
import nc.ui.pub.style.Style;
import nc.ui.sm.login.ClientInstallDirectory;
import nc.vo.logging.Debug;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.applet.MainApplet;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.report.applet.AppletCache;
import com.ufsoft.report.lookandfeel.Office2003LookAndFeel;

public class Loader {

	private JApplet container = null;

	private HashMap m_appletDebugParams = null;
	
	public Loader(javax.swing.JApplet applet) {
		super();
		try {
			System.out.println("=======applet Loader:" + this.getClass().getName());
			
			container = applet;
			
//			
//			ServiceConfig.setFileBase(applet.getAppletInfo());
//			ServiceConfig.setBaseHttpURL(servletURL);
			
			initCodeCacheParams();
			
			loadInvokeProxy();
			  			
			initLang();
	
			String appletName =  getParameter("appletName");
			
			System.out.println("=========appletName:  " + appletName) ;
			
			MultiLangUtil.saveLanguage(System.getProperty("localCode"), true);
			 
//			loadInvokeProxy();
			
			initStyle();

			JApplet l2 = (JApplet) Class.forName(appletName).newInstance();// new
																			// ReportFormatApplet();
			if(l2 instanceof MainApplet){
				String id = getParameter("id");
				MainApplet mainApplet = (MainApplet) l2;
				(mainApplet).setId(id);
				AppletCache.put(id, mainApplet);
				
			}
			
			
			l2.setStub(new com.ufida.zior.applet.MyAppletStub(applet));
			// l2.setStubToMySelf(MyAppletStub.getInstance());
			// MyAppletStub.getInstance().setApplet(applet);

			applet.getContentPane().removeAll();
			applet.getContentPane().add(l2);
			// liuyy.2007-10-31. 需要先添加到contentPane再init（）
			l2.init();
			//add by guogang 2008-1-10 UAP的appletContainer.start()中没有调用该applet.start()
			l2.start();
			
			l2.setLocation(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	

	private void initCodeCacheParams() {

//		System.out.println("开始初始化本地代码缓存：initCodeCacheParams()");

		String clientHome = System.getProperty("nc.client.location");
		if (clientHome == null || clientHome.trim().length() == 0)
			clientHome = getParameter("ClientHome");
		if (clientHome != null && clientHome.trim().length() > 0
				&& !"null".equals(clientHome)) {
			System.setProperty("user.home", clientHome);
		}
		String strCodeBase = ClientInstallDirectory.initNCCodeBase(
				getParameter("SERVER_HOST_NAME"),
				getParameter("SERVER_WORKINGDIR"), getParameter("SERVER_PORT"));
		System.setProperty("client.code.dir", strCodeBase);

		String baseHttpURL = getSysURLBaseString();
		String fileBase = strCodeBase;
		if (!fileBase.endsWith("/"))
			fileBase += "/";
		fileBase += "CODE";
		// set the file base
		ServiceConfig.setFileBase(fileBase);
		ServiceConfig.setBaseHttpURL(baseHttpURL);

//		// 初始化本地缓存
//		LocalPackageVIDCache.getInstance().initPackageVIDCache(
//				baseHttpURL + "/" + UAPConstants.STR_CODE_SYN_SERVLET);
	}

	public String getSysURLBaseString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getParameter("SCHEME"));
		sb.append("://");
		sb.append(getParameter("SERVER_IP"));
		sb.append(":");
		sb.append(getParameter("SERVER_PORT"));
		return sb.toString();
	}

	/**
	 * 参照MainApplet的loadInvokeProxy方法。
	 * 
	 */
	private void loadInvokeProxy() {
		RuntimeEnv.getInstance().setRunningInServer(false);

		// 得到接入的服务器IP
		Properties properties = new Properties();
		try {
			properties.put("codesyncServletURL", "/CodeSynServlet");
			properties.put("SERVICELOOKUP_URL", "/ServiceLookuperServlet");
			properties.put("SERVICEDISPATCH_URL", "/ServiceDispatcherServlet");
			properties.put("CLIENT_COMMUNICATOR",
					"nc.bs.framework.comn.cli.JavaURLCommunicator");

			RuntimeEnv.getInstance().setProperty("CLIENT_COMMUNICATOR",
					properties.getProperty("CLIENT_COMMUNICATOR"));
			String serviceLookuperURL = ServiceConfig.getBaseHttpURL()
					+ properties.getProperty("SERVICELOOKUP_URL");
			String serviceDispatcherURL = ServiceConfig.getBaseHttpURL()
					+ properties.getProperty("SERVICEDISPATCH_URL");

			Debug.debug("The ServiceLookuper URL is " + serviceDispatcherURL);
			RuntimeEnv.getInstance().setProperty("SERVICELOOKUP_URL",
					serviceLookuperURL);
			RuntimeEnv.getInstance().setProperty("SERVICEDISPATCH_URL",
					serviceDispatcherURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initLang() {
		initWhenDebug();
		// 初始化语种信息。
		String strLocalCode = getParameter("localCode");
		// com.ufsoft.iufo.i18n.MultiLangUtil.saveLanguage(strLocalCode,true);
		System.setProperty("localCode", strLocalCode);// 供在Loader中调用。这里不想引入MultiLangUtil类。

		// setDefaultFont(strLocalCode);

		// 设置当前java虚拟机的语言类型。
		Locale curLocale = null;
		if (strLocalCode.equals("simpchn")) {
			curLocale = Locale.SIMPLIFIED_CHINESE;
		} else if (strLocalCode.equals("tradchn")) {
			curLocale = Locale.TRADITIONAL_CHINESE;
		} else if (strLocalCode.equals("english")) {
			curLocale = Locale.US;
		}
		Locale.setDefault(curLocale);
		JComponent.setDefaultLocale(curLocale);
		UIManager.getDefaults().setDefaultLocale(curLocale);
		UIManager.getLookAndFeelDefaults().setDefaultLocale(curLocale);
	}
	
	private void initWhenDebug() {
		if (new Boolean(container.getParameter("debug")).booleanValue()) {
			ServiceConfig.setBaseHttpURL(getParameter("CODEBASE").substring(1,
					getParameter("CODEBASE").length() - 1));
		}
	}
	
	private void initStyle() {
		// 加载NC UI组件风格. liuyy
		Style.refreshStyle();

		// 复制UAP代码。
		try {
			// 确保在初始化界面控件之前装入风格
			javax.swing.UIManager
					.setLookAndFeel(new Office2003LookAndFeel());
			Style.getCurrentStyle();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	

	public String getParameter(String param) {
		if ("USER_WIDTH".equals(param) || "USER_HEIGHT".equals(param)) {
			Dimension siScreen = Toolkit.getDefaultToolkit().getScreenSize();
			if ("USER_WIDTH".equals(param))
				return "" + ((int) siScreen.getWidth());
			else
				return "" + ((int) siScreen.getHeight());
		}

		if (!"true".equals(container.getParameter("debug"))) {
			return container.getParameter(param);
		} else {
			AppDebug.SYS_DEBUG = true;
			if (m_appletDebugParams == null) {
				initDebugAppletParams();
			}
			return (String) m_appletDebugParams.get(param);
		}
	}

	private void initDebugAppletParams() {
		BufferedReader in = null;
		try {
			File root = new File("c:");
			String[] fileNames = root.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".txt");
				}
			});
			JComboBox box = new JComboBox(fileNames);
			JOptionPane.showMessageDialog(container, box, "请选择参数文件：", JOptionPane.INFORMATION_MESSAGE);
			String FILENAME = (String) box.getSelectedItem();

			in = new BufferedReader(new FileReader("c:\\" + FILENAME));
			String strLine = null;
			m_appletDebugParams = new HashMap();
			// 第一个等号到第一个空格之间为param，第2个等号到">"之间为value
			while ((strLine = in.readLine()) != null) {
				if (strLine.trim().length() == 0)
					continue;
				int pos1 = strLine.indexOf("=");
				int pos2 = strLine.indexOf(" ", pos1);
				int pos3 = strLine.indexOf("=", pos2);
				int pos4 = strLine.indexOf(">", pos2);
				m_appletDebugParams.put(strLine.substring(pos1 + 1, pos2),
						strLine.substring(pos3 + 1, pos4).trim());
			}
		} catch (Exception e) {
			// AppDebug.debug(e);//@devTools
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					// AppDebug.debug(e1);//@devTools
					e1.printStackTrace();
				}
			}
		}
	}
}
 