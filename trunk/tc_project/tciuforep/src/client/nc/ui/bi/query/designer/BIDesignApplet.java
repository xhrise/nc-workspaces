package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.beans.Beans;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UIPanel;
import nc.vo.bd.CorpVO;
import nc.vo.ml.Language;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.applet.UfoApplet;


/**
 * BI设计（Applet）界面基类
 * 
 * 创建日期：(2005-5-12 18:38:02)
 * 
 * @author：朱俊彬
 */
public abstract class BIDesignApplet extends UfoApplet {

	private JPanel ivjJAppletContentPane = null;

	/**
	 * 返回关于此小应用程序的信息。
	 * 
	 * @return 返回关于此小应用程序信息的字符串。





	 */
	public String getAppletInfo() {
		return "BIDesignApplet\n" + "\n创建日期：(2005-5-12 18:38:02)\n"
				+ "@author：zjb\n" + "";
	}

	/**
	 * 返回 JAppletContentPane 特性值。
	 * 
	 * @return JPanel
	 */
	/* 警告：此方法将重新生成。 */
	protected JPanel getJAppletContentPane() {
		if (ivjJAppletContentPane == null) {
			try {
				ivjJAppletContentPane = new UIPanel();
				ivjJAppletContentPane.setName("JAppletContentPane");
				ivjJAppletContentPane.setLayout(new BorderLayout());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJAppletContentPane;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	public void handleException(Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化小应用程序。
	 * 
	 * @see #start
	 * @see #stop
	 * @see #destroy
	 */
	public void init() {
		try {			
			setName("BIDesignApplet");
			setSize(426, 240);
			setContentPane(getJAppletContentPane());
			// user code begin {1}
			initClientEnvironment();
			//初始化界面
			initUI();
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]

	 */
	public static void main(String[] args) {
		try {
			JFrame frame = new UIFrame();
			BIDesignApplet aQueryDesignApplet;
			Class iiCls = Class
					.forName("nc.ui.bi.query.designer.BIDesignApplet");
			ClassLoader iiClsLoader = iiCls.getClassLoader();
			aQueryDesignApplet = (BIDesignApplet) Beans.instantiate(
					iiClsLoader, "nc.ui.bi.query.designer.BIDesignApplet");
			frame.getContentPane().add("Center", aQueryDesignApplet);
			frame.setSize(aQueryDesignApplet.getSize());
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("JApplet 的 main() 中发生异常");
			AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
		}
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

		//在此处插入用来绘制小应用程序的代码。
	}

	/**
	 * 初始化界面
	 */
	abstract public void initUI();
	
	/**
	 * 根据参数中传来的'业务日期''单位''用户'信息初始化客户端环境。 创建日期：(00-7-19 10:35:05)

	 */
	protected void initClientEnvironment() {
		//初始化数据源和公司主键
		String dsn = System.getProperty("UserDataSource", "design");
		String pkCorp = "0001";
		String user = "88888888888888888888";
		
		if(getParameter("DSNAME_DEF") != null) {
			System.setProperty("UserDataSource", getParameter("DSNAME_DEF"));
			System.setProperty("DefaultDataSource",getParameter("DSNAME_DEF"));
		}
		try {
			dsn = getParameter("DATASOURCE") == null ? dsn
					: getParameter("DATASOURCE");
			pkCorp = getParameter("PKCORP") == null ? pkCorp
					: getParameter("PKCORP");
			user = getParameter("USER") == null ? user : getParameter("USER");
			AppDebug.debug(dsn + " -- " + pkCorp + " -- " + user);//@devTools System.out.println(dsn + " -- " + pkCorp + " -- " + user);
			
			String sessionID = getParameter("JSESSIONID");
			if(sessionID != null && sessionID.length() > 0)
				System.getProperties().setProperty("JSESSIONID", sessionID);
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools System.out.println(e);
		}
		//test
		AppDebug.debug("dsn = " + dsn);//@devTools System.out.println("dsn = " + dsn);
		//设置语种
		String classname = "nc.vo.ml.translator.SimpleChineseTranslator";
		Language lang = new Language();
		lang.setCode("simpchn");
		lang.setDisplayName("简体中文");
		lang.setTranslatorClassName(classname);
		NCLangRes.getInstance().setCurrLanguage(lang);
		//		NCLangRes.getInstance().setCurrLanguage(
		//				NCLangRes.getInstance().getLanguage(langCode));
		//初始化客户端
		ClientEnvironment ce = ClientEnvironment.getInstance();
		ce.setLanguage(NCLangRes.getInstance().getCurrLanguage().getCode());
		try {
			//帐套信息
			ce.setConfigAccount(new Account());
			ce.getConfigAccount().setDataSourceName(dsn);
			//公司信息
			ce.setCorporation(new CorpVO());
			ce.getCorporation().setPrimaryKey(pkCorp);
			//用户信息
			ce.setUser(new UserVO());
			ce.getUser().setPrimaryKey(user);
		} catch (Exception e) {
			handleException(e);
		}
	}
	/**
	 * 是否需要检查和提示保存
	 * @return
	 */
	public boolean needSave(){
		return true;
	}
    public boolean isDirty(){
    	if(needSave())
    		return getUfoReport().isDirty() || getUfoReport().getCellsModel().getPrintSet().isDirty();
    	return false;
    }
    public String save(){
    	getUfoReport().store();
    	return "";
    }

}