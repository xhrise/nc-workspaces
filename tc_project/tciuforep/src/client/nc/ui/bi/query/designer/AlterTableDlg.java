package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 动态变更表结构对话框 创建日期：(2005-6-17 10:34:39)
 * 
 * @author：朱俊彬
 */
public class AlterTableDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//动态变更表结构面板实例
	private AlterTablePanel m_alterPn = null;

	private JPanel ivjUIDialogContentPane = null;

	/**
	 * AlterTableDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public AlterTableDlg(Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 * @i18n miufo00155=--------- 未捕捉到的异常 ---------
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("AlterTableDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(640, 480);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setResizable(true);
		getUIDialogContentPane().add(getAlterPn(), BorderLayout.CENTER);
		// user code end
	}

	/**
	 * 获得变更表结构面板实例 创建日期：(2005-5-16 19:05:13)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignPanel
	 */
	public AlterTablePanel getAlterPn() {
		if (m_alterPn == null) {
			m_alterPn = new AlterTablePanel();
			m_alterPn.setDlg(this);
		}
		return m_alterPn;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjUIDialogContentPane;
	}

	/**
	 * 设置定义数据源
	 */
	public void setDsNameForDef(String dsNameForDef) {
		getAlterPn().setDsNameForDef(dsNameForDef);
	}
}  