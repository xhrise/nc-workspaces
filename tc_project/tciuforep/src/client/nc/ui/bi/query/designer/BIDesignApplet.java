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
 * BI��ƣ�Applet���������
 * 
 * �������ڣ�(2005-5-12 18:38:02)
 * 
 * @author���쿡��
 */
public abstract class BIDesignApplet extends UfoApplet {

	private JPanel ivjJAppletContentPane = null;

	/**
	 * ���ع��ڴ�СӦ�ó������Ϣ��
	 * 
	 * @return ���ع��ڴ�СӦ�ó�����Ϣ���ַ�����





	 */
	public String getAppletInfo() {
		return "BIDesignApplet\n" + "\n�������ڣ�(2005-5-12 18:38:02)\n"
				+ "@author��zjb\n" + "";
	}

	/**
	 * ���� JAppletContentPane ����ֵ��
	 * 
	 * @return JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	public void handleException(Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ��СӦ�ó���
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
			//��ʼ������
			initUI();
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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
			System.err.println("JApplet �� main() �з����쳣");
			AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
		}
	}

	/**
	 * ����СӦ�ó��� �����СӦ�ó�����Ҫ���ƣ����磬�����ֻ������ AWT ������һ��������������԰�ȫ�س�ȥ�˷�����
	 * 
	 * @param g
	 *            ָ���ġ�ͼ�Ρ�����
	 * @see #update
	 */
	public void paint(Graphics g) {
		super.paint(g);

		//�ڴ˴�������������СӦ�ó���Ĵ��롣
	}

	/**
	 * ��ʼ������
	 */
	abstract public void initUI();
	
	/**
	 * ���ݲ����д�����'ҵ������''��λ''�û�'��Ϣ��ʼ���ͻ��˻����� �������ڣ�(00-7-19 10:35:05)

	 */
	protected void initClientEnvironment() {
		//��ʼ������Դ�͹�˾����
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
		//��������
		String classname = "nc.vo.ml.translator.SimpleChineseTranslator";
		Language lang = new Language();
		lang.setCode("simpchn");
		lang.setDisplayName("��������");
		lang.setTranslatorClassName(classname);
		NCLangRes.getInstance().setCurrLanguage(lang);
		//		NCLangRes.getInstance().setCurrLanguage(
		//				NCLangRes.getInstance().getLanguage(langCode));
		//��ʼ���ͻ���
		ClientEnvironment ce = ClientEnvironment.getInstance();
		ce.setLanguage(NCLangRes.getInstance().getCurrLanguage().getCode());
		try {
			//������Ϣ
			ce.setConfigAccount(new Account());
			ce.getConfigAccount().setDataSourceName(dsn);
			//��˾��Ϣ
			ce.setCorporation(new CorpVO());
			ce.getCorporation().setPrimaryKey(pkCorp);
			//�û���Ϣ
			ce.setUser(new UserVO());
			ce.getUser().setPrimaryKey(user);
		} catch (Exception e) {
			handleException(e);
		}
	}
	/**
	 * �Ƿ���Ҫ������ʾ����
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