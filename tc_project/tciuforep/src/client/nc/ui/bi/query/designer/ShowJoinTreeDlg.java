/*
 * 创建日期 2005-7-18
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zjb
 * 
 * 连接条件树显示框
 */
public class ShowJoinTreeDlg extends UIDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel ivjUIDialogContentPane = null;

	private UIScrollPane ivjSclPnTree = null;

	private UITree ivjTreeTableFld = null;

	/**
	 * @param parent
	 */
	public ShowJoinTreeDlg(Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * 初始化类
	 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("QueryDesignDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(400, 240);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setResizable(true);
		getUIDialogContentPane().add(getSclPnTree(), BorderLayout.CENTER);
		// user code end
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return JPanel
	 */
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
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 返回 TreeTableFld 特性值。
	 * 
	 * @return UITree
	 */
	private UITree getTree() {
		if (ivjTreeTableFld == null) {
			try {
				ivjTreeTableFld = new UITree();
				ivjTreeTableFld.setName("Tree");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTreeTableFld;
	}

	/**
	 * 返回 SclPnTree 特性值。
	 * 
	 * @return UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private UIScrollPane getSclPnTree() {
		if (ivjSclPnTree == null) {
			try {
				ivjSclPnTree = new UIScrollPane();
				ivjSclPnTree.setName("SclPnTree");
				ivjSclPnTree.setPreferredSize(new Dimension(168, 3));
				getSclPnTree().setViewportView(getTree());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnTree;
	}

	/**
	 * 初始化类
	 */
	public void initTree(DefaultMutableTreeNode root) {
		DefaultTreeModel tm = new DefaultTreeModel(root);
		getTree().setModel(tm);
	}
}
 