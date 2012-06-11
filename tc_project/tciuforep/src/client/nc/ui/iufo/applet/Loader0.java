package nc.ui.iufo.applet;

import java.util.Properties;

import javax.swing.JApplet;

import nc.bs.framework.ServiceConfig;
import nc.bs.framework.common.RuntimeEnv;
import nc.ui.pub.style.Style;
import nc.vo.logging.Debug;

import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.report.lookandfeel.Office2003LookAndFeel;

/**
 * 此处插入类型说明。 创建日期：(2002-5-19 14:51:54)
 * 
 * @author：wss
 * @deprecated liuyy.
 */
public class Loader0 {
	/**
	 * Loader 构造子注解。
	 */
	public Loader0(javax.swing.JApplet applet, javax.swing.JPanel container,
			String fileBase, String servletURL, String appletName) {
		super();
		MultiLangUtil.saveLanguage(System.getProperty("localCode"), true);
		ServiceConfig.setFileBase(fileBase);
		ServiceConfig.setBaseHttpURL(servletURL);
		loadInvokeProxy();
		try {
			initStyle();
			// initLocalVIDCache(fileBase, servletURL);
			JApplet l2 = (JApplet) Class.forName(appletName).newInstance();// new
																			// ReportFormatApplet();
			l2.setStub(new com.ufida.zior.applet.MyAppletStub(applet));
			// l2.setStubToMySelf(MyAppletStub.getInstance());
			// MyAppletStub.getInstance().setApplet(applet);

			applet.getContentPane().removeAll();
			applet.getContentPane().add(l2);
			// liuyy.2007-10-31. 需要先添加到contentPane再init（）
			l2.init();

			l2.setLocation(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
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
}
