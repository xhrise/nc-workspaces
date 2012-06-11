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
 * ��ѯ��ƶԻ��� �������ڣ�(2005-5-16 19:02:05)
 * 
 * @author���쿡��
 */
public class QueryDesignDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��ѯ������ʵ��
	private QueryDesignPanel m_designPn = null;

	private JPanel ivjUIDialogContentPane = null;

	/**
	 * QueryDesignDlg ������ע�⡣
	 * 
	 * @param parent
	 *            Container
	 */
	public QueryDesignDlg(Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * ��ò�ѯ������ʵ�� �������ڣ�(2005-5-16 19:05:13)
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
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
	 * ���ò�ѯ������ʵ�� �������ڣ�(2005-5-16 19:05:13)
	 */
	public void setDesignPn(QueryDesignPanel newDesignPn) {
		m_designPn = newDesignPn;
	}

	/**
	 * ���ò�ѯ�������ҳǩ �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setTabPn(AbstractQueryDesignTabPanel tabPn) {
		getDesignPn().setTabPn(tabPn);
	}

	/**
	 * ���ò�ѯ���� �������ڣ�(2005-5-16 18:38:58)
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
	 * ���ö�������Դ �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setDefDsName(String dsNameForDef) {
		getDesignPn().setDefDsName(dsNameForDef);
	}
}
 