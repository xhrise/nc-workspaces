package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.pub.ddc.datadict.Datadict;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 查询设计对话框 创建日期：(2005-5-16 19:02:05)
 * 
 * @author：朱俊彬
 */
public class QueryDesignDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//查询设计面板实例
	private QueryDesignPanel m_designPn = null;

	private JPanel ivjUIDialogContentPane = null;

	/**
	 * QueryDesignDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public QueryDesignDlg(Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * 获得查询设计面板实例 创建日期：(2005-5-16 19:05:13)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignPanel
	 */
	public QueryDesignPanel getDesignPn() {
		if (m_designPn == null) {
			m_designPn = new QueryDesignPanel();
			m_designPn.setDesignDlg(this);
		}
		return m_designPn;
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
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
			setName("QueryDesignDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(720, 420);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setResizable(true);
		getUIDialogContentPane().add(getDesignPn(), BorderLayout.CENTER);
		// user code end
	}

	/**
	 * 设置查询设计面板实例 创建日期：(2005-5-16 19:05:13)
	 */
	public void setDesignPn(QueryDesignPanel newDesignPn) {
		m_designPn = newDesignPn;
	}

	/**
	 * 设置查询设计设置页签 创建日期：(2005-5-16 18:38:58)
	 */
	public void setTabPn(AbstractQueryDesignTabPanel tabPn) {
		getDesignPn().setTabPn(tabPn);
	}

	/**
	 * 设置查询主键 创建日期：(2005-5-16 18:38:58)
	 */
	public void setQueryModelDef(BIQueryModelDef qmd) {
		getDesignPn().setQueryModelDef(qmd);
	}

	/**
	 * @param datadict
	 */
	public void setDatadict(Datadict datadict) {
		getDesignPn().setDatadict(datadict);
	}

	/**
	 * 设置定义数据源 创建日期：(2005-5-16 18:38:58)
	 */
	public void setDefDsName(String dsNameForDef) {
		getDesignPn().setDefDsName(dsNameForDef);
	}
}
 