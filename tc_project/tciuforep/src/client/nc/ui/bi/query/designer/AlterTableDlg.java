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
 * ��̬�����ṹ�Ի��� �������ڣ�(2005-6-17 10:34:39)
 * 
 * @author���쿡��
 */
public class AlterTableDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��̬�����ṹ���ʵ��
	private AlterTablePanel m_alterPn = null;

	private JPanel ivjUIDialogContentPane = null;

	/**
	 * AlterTableDlg ������ע�⡣
	 * 
	 * @param parent
	 *            Container
	 */
	public AlterTableDlg(Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 * @i18n miufo00155=--------- δ��׽�����쳣 ---------
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ñ����ṹ���ʵ�� �������ڣ�(2005-5-16 19:05:13)
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
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���ö�������Դ
	 */
	public void setDsNameForDef(String dsNameForDef) {
		getAlterPn().setDsNameForDef(dsNameForDef);
	}
}  