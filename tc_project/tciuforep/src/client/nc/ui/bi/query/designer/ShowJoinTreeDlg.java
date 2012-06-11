/*
 * �������� 2005-7-18
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
 * ������������ʾ��
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
	 * ��ʼ����
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
	 * ���� UIDialogContentPane ����ֵ��
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
	 * ÿ�������׳��쳣ʱ������
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ���� TreeTableFld ����ֵ��
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
	 * ���� SclPnTree ����ֵ��
	 * 
	 * @return UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ����
	 */
	public void initTree(DefaultMutableTreeNode root) {
		DefaultTreeModel tm = new DefaultTreeModel(root);
		getTree().setModel(tm);
	}
}
 