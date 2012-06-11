package nc.ui.iufo.applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ufida.iufo.pub.tools.AppDebug;

import nc.bs.framework.ServiceConfig;
import nc.bs.framework.codesync.client.LocalPackageVIDCache;
import nc.bs.framework.codesync.client.NCClassLoader;
import nc.ui.sm.login.ClientInstallDirectory;
import nc.vo.logging.Debug;
import nc.vo.uap.UAPConstants;

/**
 * @update by syang at (2005-5-18 9:55:15) 修正Cluster模式下报表工具的代码无法下载的问题
 * @end
 * @update by 谢东 at (2003-11-24 20:41:15)
 *         修改IUFO报表工具代码的缓存目录，使其在cluster模式下，只保留一份代码的缓存
 * @end 此处插入类型说明。 该类作为登录界面和桌面Applet的容器 创建日期：(2002-5-10 14:55:26)
 * @author：wss
 * 
 * 
 * @deprecated
 */
public class AppletContainer extends javax.swing.JApplet {

	private static final long serialVersionUID = 801204003218114496L;

	String m_ncCodeBase = null;
	java.net.URL m_url = null;
	private javax.swing.JPanel m_oMainPanel;

	/**
	 * 该方法返回存放nc客户端缓存代码的路径
	 * 
	 * 创建日期：(2002-4-9 15:04:01)
	 * 
	 * @return java.net.URL
	 */
	public AppletContainer() {
		super();

	}

	/**
	 * 返回关于此小应用程序的信息。
	 * 
	 * @return 返回关于此小应用程序信息的字符串。
	 */
	public String getAppletInfo() {
		return "AppletContainer";
	}

	/**
	 * 该方法返回存放nc客户端缓存代码的路径
	 * 
	 * 创建日期：(2002-4-9 15:04:01)
	 * 
	 * @return java.net.URL
	 */
	public String getNCCodeBase() {
		if (m_ncCodeBase == null) {
			StringBuffer sb = new StringBuffer();
			String userHome = System.getProperty("user.home").trim();
			sb.append(userHome);
			if (!userHome.endsWith("/"))
				sb.append("/");
			sb.append(UAPConstants.STR_CODE_CACHE + "/");
			sb.append(getParameter("SERVER_HOST_NAME")).append("_");
			sb.append(
					getParameter("SERVER_WORKINGDIR").replace('\\', '-')
							.replace(':', '-').replace('/', '-')).append("_");
			sb.append(getParameter("SERVER_PORT"));
			sb.append("/").append("CODE");
			m_ncCodeBase = sb.toString().replace('\\', '/');

			Debug.debug("Code cache path: " + m_ncCodeBase);
			try {
				File f = new File(m_ncCodeBase);
				if (!f.exists())
					f.mkdirs();
			} catch (Exception e) {
			}
		}
		return m_ncCodeBase;
	}

	/**
	 * 初始化小应用程序。
	 * 
	 * @see #start
	 * @see #stop
	 * @see #destroy
	 */
	public void init() {
		System.out.println("AppletContainer开始初始化...");
		super.init();
		initLogo();
		initLang();
		// 在此处插入用来初始化小应用程序的代码
		getContentPane().setLayout(new BorderLayout());
		initMainPanel("appletContainer");
		this.getContentPane().add(m_oMainPanel);

		System.setProperty("nc.run.side", "client");

		try {
			// 初始化客户端代码路径
			initCodeCacheParams();

			Thread.currentThread().setContextClassLoader(
					NCClassLoader.getNCClassLoader());
			if (!SwingUtilities.isEventDispatchThread()) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							Thread.currentThread().setContextClassLoader(
									NCClassLoader.getNCClassLoader());
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			System.out.println("ServiceConfig.FileBase: "
					+ ServiceConfig.getFileBase());

			// Class iiCls = Class.forName("nc.ui.iufo.applet.AppletContainer");
			// ClassLoader iiClsLoader = iiCls.getClassLoader();

			System.out.println("ClassLoader:NCClassLoader.getNCClassLoader();");
			NCClassLoader clsLoader = NCClassLoader.getNCClassLoader();// NCClassLoader.getNCClassLoader(ServiceConfig.getFileBase(),
																		// iiClsLoader);
			Class c = Class
					.forName("nc.ui.iufo.applet.Loader0", true, clsLoader);  
			Class cp[] = { JApplet.class, JPanel.class, String.class,
					String.class, String.class };
			Constructor con = c.getConstructor(cp);
			Object oa[] = { this, m_oMainPanel, ServiceConfig.getFileBase(),
					ServiceConfig.getBaseHttpURL(), getParameter("appletName") };
			con.newInstance(oa);

			// new Loader(this, m_oMainPanel,
			// ServiceConfig.getFileBase(),ServiceConfig.getBaseHttpURL(),getParameter("appletName"));

		} catch (Throwable e) {
			System.out.println("AppletContainer初始化失败！！！");
			e.printStackTrace(System.out);
		}

	}

	private Image logoImg = null;
	private MediaTracker mt = new MediaTracker(this);
	private Panel ncLogoPanel = null;
	private boolean imgUpdate = true;

	private void initLogo() {
		initLogoImg();
		if (logoImg != null) {
			Container container = getParent();
			Component[] comps = container.getComponents();
			int count = comps == null ? 0 : comps.length;
			for (int i = 0; i < count; i++) {
				if (!comps[i].equals(this)) {
					container.remove(comps[i]);
				}
			}
			container.add(getNcLogoPanel(), BorderLayout.CENTER);
			getNcLogoPanel().repaint();
		}
	}

	private Panel getNcLogoPanel() {
		if (ncLogoPanel == null) {
			final int imgW = logoImg.getWidth(this);
			final int imgH = logoImg.getHeight(this);
			// System.out.println("logo, w="+imgW+" h="+imgH);
			final int x = (getSize().width - imgW) / 2;
			final int y = (getSize().height - imgH) / 2;
			ncLogoPanel = new Panel() {
				public void update(Graphics g) {
					paint(g);
				}
			};
			ncLogoPanel.setBackground(Color.white);
			ncLogoPanel.setLayout(null);// (new BorderLayout());
			ncLogoPanel.setSize(getSize());
			if (logoImg != null) {
				JPanel iconPanel = new JPanel() {
					public void paintComponent(Graphics g) {
						super.paintComponent(g);
						g.drawImage(logoImg, 0, 0, this);
					}

					public boolean imageUpdate(Image img, int infoflags, int x,
							int y, int w, int h) {
						boolean b = super.imageUpdate(img, infoflags, x, y, w,
								h);
						return b && imgUpdate;
					}
				};
				iconPanel.setDoubleBuffered(true);
				iconPanel.setBounds(x, y, imgW, imgH);
				iconPanel.setBackground(Color.white);
				ncLogoPanel.add(iconPanel);
			}
		}
		return ncLogoPanel;
	}

	private void initLogoImg() {
		if (logoImg == null) {
			String strlogoImg = getParameter("logoimage");
			if (strlogoImg != null && strlogoImg.trim().length() > 0) {
				try {
					URL imgURL = new URL(getCodeBase(), strlogoImg);
					logoImg = getImage(imgURL);
					mt.addImage(logoImg, 0);
					mt.waitForID(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (logoImg == null || logoImg.getHeight(this) == -1) {
			logoImg = loadDefaultLogoImg();
		}
	}

	private Image loadDefaultLogoImg() {

		URL url = null;
		try {
			url = new URL(getCodeBase(), "logo/images/iufo_logo.gif");
			// url = getClass().getClassLoader().getResource("iufo_logo.gif");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (url == null) {
			System.out.println("logo pic is null");
		} else {
			System.out.println("logo pic is: " + url);
		}

		Image img = null;
		if (url != null) {
			img = Toolkit.getDefaultToolkit().createImage(url);
			mt.addImage(img, 0);
			try {
				mt.waitForID(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		return img;
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
		if ("true".equals(super.getParameter("debug"))) {
			ServiceConfig.setBaseHttpURL(getParameter("CODEBASE").substring(1,
					getParameter("CODEBASE").length() - 1));
		}
	}

	// /**
	// * 按照语言设置界面字体.
	// * @param lan
	// */
	// private void setDefaultFont(String lan){
	// Font menuFont,dlgFont;
	// if("english".equals(lan)){
	// menuFont = new Font("dialog", 0, 12);//new Font("Arial", 0, 12);
	// dlgFont = new Font("dialog", 0, 12);//new Font("Arial", 0, 9);
	// }else if("tradchn".equals(lan)){
	// menuFont = new Font("dialog", 0, 12);//new Font("dialog", 0, 12);
	// dlgFont = new Font("dialog", 0, 14);//new Font("細明體", 0, 14);
	// }else {
	// menuFont = new Font("dialog", 0, 12);//new Font("dialog", 0, 12);
	// dlgFont = new Font("dialog", 0, 14);//new Font("宋体", 0, 14);
	// }
	// FontUIResource fontRes = new FontUIResource(dlgFont);
	// java.util.Enumeration keys = UIManager.getDefaults().keys();
	// //替换所有的缺省字体.
	// while (keys.hasMoreElements()) {
	// Object key = keys.nextElement();
	// Object value = UIManager.get(key);
	// if (value instanceof javax.swing.plaf.FontUIResource){
	// UIManager.put(key, fontRes);
	// }
	// }
	// //菜单字体。
	// UIManager.put("Menu.font", new FontUIResource(menuFont));
	// UIManager.put("MenuBar.font", new FontUIResource(menuFont));
	// UIManager.put("MenuItem.font", new FontUIResource(menuFont));
	// }

	/**
	 * 获得主面板
	 * 
	 * @param strPanelName
	 */
	private void initMainPanel(String strPanelName) {
		if (m_oMainPanel == null) {
			m_oMainPanel = new JPanel();
			m_oMainPanel.setName(strPanelName);
			m_oMainPanel.setLayout(null);
			m_oMainPanel.removeAll();
		}
	}

	/**
	 * 保存报表。
	 * 
	 * @return String
	 */
	public String onFileSaveForIE() {
		Class c = getWrappedApplet().getClass();
		Method concatMethod;
		String returnValue = null;
		try {
			concatMethod = c.getMethod("save", null);
			returnValue = (String) concatMethod
					.invoke(getWrappedApplet(), null);
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.toString();
		}
		return returnValue;
	}

	/*
	 * 检查报表是否修改。 @return int
	 */
	public int isDirty() {
		Class c = getWrappedApplet().getClass();
		Method concatMethod;
		int change = 0;
		try {
			concatMethod = c.getMethod("isDirty", null);
			Boolean result = (Boolean) concatMethod.invoke(getWrappedApplet(),
					null);
			change = result.booleanValue() ? 1 : 0;
		} catch (Exception ex) {
			// 没有isDirty方法时,默认不需要保存!
		}

		// removeCustomUIStyle();

		return change;
	}

	/**
	 * 绘制小应用程序。 如果此小应用程序不需要绘制（例如，如果它只是其他 AWT 部件的一个容器），则可以安全地除去此方法。
	 * 
	 * @param g
	 *            指定的“图形”窗口
	 * @see #update
	 */
	public void paint(Graphics g) {
		super.paint(g);

		// 在此处插入用来绘制小应用程序的代码。
	}

	private void initCodeCacheParams() {

		System.out.println("开始初始化本地代码缓存：initCodeCacheParams()");

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

		// 初始化本地缓存
		LocalPackageVIDCache.getInstance().initPackageVIDCache(
				baseHttpURL + "/" + UAPConstants.STR_CODE_SYN_SERVLET);
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

	private HashMap m_appletDebugParams = null;

	public String getParameter(String param) {
		if ("USER_WIDTH".equals(param) || "USER_HEIGHT".equals(param)) {
			Dimension siScreen = Toolkit.getDefaultToolkit().getScreenSize();
			if ("USER_WIDTH".equals(param))
				return "" + ((int) siScreen.getWidth());
			else
				return "" + ((int) siScreen.getHeight());
		}

		if (!"true".equals(super.getParameter("debug"))) {
			return super.getParameter(param);
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
			JOptionPane.showMessageDialog(this, box, "请选择参数文件：", JOptionPane.INFORMATION_MESSAGE);
			String FILENAME = (String) box.getSelectedItem();
//			String FILENAME = "zior.txt";
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

	private JApplet getWrappedApplet() {
		return (JApplet) getContentPane().getComponent(0);
	}

	@Override
	public void start() {
		imgUpdate = false;
		getWrappedApplet().start();
	}

	//
	// /**
	// * 打印UIDefaults调试信息。
	// * liuyy.
	// */
	// private static void printUIStyleInfo(){
	// Hashtable hash = UIManager.getDefaults();
	// Object[] keys = hash.keySet().toArray();
	// StringBuffer bf = new StringBuffer();
	// for (int i = 0; i < keys.length; i++) {
	// try{
	// Object key = keys[i];
	// if(!(key instanceof String)){
	// key = key.getClass().getName();
	// }
	// Object value = hash.get(keys[i]);
	// if(value != null && !(value instanceof String)){
	// value = value.getClass().getName();
	// }
	// bf.append(key + "= " + value + "\r\n");
	// } catch(Throwable e){
	// e.printStackTrace();
	// }
	// }
	// // FileUtil.writeFile("c:\\UIInfo_" + DateUtil.getCurTimeInt() + ".txt",
	// bf.toString());
	// System.out.println(bf);
	// }
	//
	//
	// /**
	// * 删除NC定义的UIStyle
	// * liuyy.
	// */
	// private static void removeCustomUIStyle(){
	// System.out.println("删除UIDefault-->");
	//	
	// printUIStyleInfo();
	//	
	// Hashtable hash = UIManager.getDefaults();
	// Object[] keys = hash.keySet().toArray();
	// for (int i = 0; i < keys.length; i++) {
	// Object key = keys[i];
	// if(!(key instanceof String)){
	// key = key.getClass().getName();
	// }
	// Object value = hash.get(keys[i]);
	// if(!(value instanceof String)){
	// value = value.getClass().getName();
	// }
	//		
	// if(key.toString().toLowerCase().indexOf("nc.ui.") >= 0 ||
	// value.toString().toLowerCase().indexOf("nc.ui.") >= 0 ){
	// hash.remove(keys[i]);
	// System.out.println("删除UIDefault：" + key + "=" + value);
	// }
	// }
	// }

}
 