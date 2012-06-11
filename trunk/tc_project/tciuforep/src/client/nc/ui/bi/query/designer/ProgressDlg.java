/*
 * �������� 2005-7-4
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.querymodel.ThreadProgress;
import nc.ui.pub.querymodel.ThreadProgress_DD;
import nc.ui.pub.querymodel.ThreadProgress_PR;
import nc.vo.bi.query.manager.BIQueryExecutor;
import nc.vo.iufo.freequery.BIMultiDataSet;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.querymodel.DataSetDataWithColumn;
import nc.vo.pub.querymodel.IBusiDatadict;
import nc.vo.pub.querymodel.PenetrateRuleVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.pub.querymodel.SimpleCrossVO;

import com.ufida.iufo.pub.AppThread;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.util.WebGlobalValue;

/**
 * @author zjb
 * 
 * ���ȶԻ���
 */
public class ProgressDlg extends UIDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class DlgWindowsAdapter extends WindowAdapter {
		public void windowActivated(WindowEvent e) {
			ProgressDlg.this.myWindowActivated(e);
		}
	}

	static class RefreshThread extends AppThread {
		private ProgressDlg dlg;

		private boolean bContinue = true;

		/**
		 * RefreshThread
		 * 
		 * @param dlg
		 *            ProgressDlg
		 * @param name
		 *            String
		 */
		public RefreshThread(final ProgressDlg dlg, String name) {
			super(name);
			this.dlg = dlg;
		}

		/**
		 * When an object implementing interface <code>Runnable</code> is used
		 * to create a thread, starting the thread causes the object's
		 * <code>run</code> method to be called in that separately executing
		 * thread.
		 *  
		 */
		public void run() {
			while (bContinue) {
				try {
					synchronized (this) {
						wait(dlg.interval);
					}
					if (!dlg.shouldContinue()) {
						dlg.closeIfUnClosed();
						bContinue = false;
					}
				} catch (InterruptedException ex) {
				}
			}
		}
	}

	static Icon gearicon = new ImageIcon(ProgressDlg.class
			.getResource(WebGlobalValue.IMAGE_PATH+"reportquery/gears.gif"));

	//nc.ui.pub.style.Style.getImage("waiting.banner");
	//�رձ�־
	private boolean bClosed = false;

	//����������
	boolean bIncrease = true;

	//�����߳�
	private ThreadProgress progressThread = null;

	//ˢ���߳�
	private RefreshThread refresh = null;

	//ˢ�¼��
	int interval = 100;

	//�������ͣ�1-DD��2-QEC��3-QEQ��4-PR��
	private int m_iType = 0;

	//��������Դ
	private String m_dsName = null;

	//���������ݿⲢ�����ȼ�
	private int m_iAdvantage = QueryConst.DB_NC;

	//��ѯģ�Ͷ���
	private QueryModelDef m_qmd = null;

	//������ϣ��
	private Hashtable m_hashParam = null;

	//��ѯִ����
	private BIQueryExecutor m_qe = null;

	//	//���в�ѯ�����
	//	private DataSetDataWithColumn m_dsdwc = null;
	//��͸����
	private PenetrateRuleVO m_pr = null;

	//��͸�й�ϣ��
	protected Hashtable m_hashPeneRow = null;

	//����VO
	private SimpleCrossVO[] m_scs = null;

	//�����ֵ�ʵ���������߳�DD��
	private Datadict m_dd = null;

	//��ѯ��¼���������߳�QEC��
	private int m_iRecordCount = 0;

	//��ѯ������������߳�QEQ��
	private BIMultiDataSet m_mds = null;

	//���в�ѯ������������߳�PR��
	private DataSetDataWithColumn m_dsdwc = null;

	//ҵ�������ֵ�ӿ�
	private IBusiDatadict m_iBusiDatadict = null;

	private JPanel ivjUIDialogContentPane = null;

	private UILabel ivjLabelHint = null;

	/**
	 * ProgressDlg ������ע�⡣
	 * @deprecated
	 */
	public ProgressDlg() {
		super();
		initialize();
	}

	/**
	 * ProgressDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public ProgressDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2004-6-9 15:09:38)
	 */
	public void clear() {
		//�����ֵ�ʵ���������߳�DD��
		m_dd = null;

		//��ѯ������������߳�QEQ��
		m_mds = null;
		//���в�ѯ������������߳�PR��
		m_dsdwc = null;
	}

	/**
	 * �������ڣ�(2003-10-21 13:00:33)
	 * 
	 * @return nc.vo.pub.ddc.datadict.Datadict
	 */
	public Datadict getDatadict() {
		return m_dd;
	}

	/**
	 * �������ڣ�(2003-10-21 12:53:27)
	 * 
	 * @return nc.vo.pub.ddc.datadict.Datadict
	 */
	public DataSetDataWithColumn getDataSetDataWithColumn() {
		return m_dsdwc;
	}

	/**
	 * ���� LabelHint ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelHint() {
		if (ivjLabelHint == null) {
			try {
				ivjLabelHint = new nc.ui.pub.beans.UILabel();
				ivjLabelHint.setName("LabelHint");
				ivjLabelHint.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10241201", "UPP10241201-001176")/*
																	  * @res
																	  * "���ڼ��������ֵ�..."
																	  */);
				ivjLabelHint.setForeground(java.awt.Color.black);
				ivjLabelHint
						.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				ivjLabelHint.setILabelType(0/** JavaĬ��(�Զ���) */
				);
				ivjLabelHint.setHorizontalAlignment(SwingConstants.CENTER);
				ivjLabelHint.setIconTextGap(5);
				ivjLabelHint.setIcon(gearicon);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelHint;
	}

	/**
	 * ��ö����� �������ڣ�(2003-10-22 11:42:56)
	 * 
	 * @return com.borland.dx.dataset.StorageDataSet
	 */
	public BIMultiDataSet getMultiDataSet() {
		return m_mds;
	}

	/**
	 * ��ü�¼���� �������ڣ�(2003-10-22 11:41:32)
	 * 
	 * @return int
	 */
	public int getRecordCount() {
		return m_iRecordCount;
	}

	/**
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());

				getUIDialogContentPane().add(getLabelHint(),
						BorderLayout.CENTER);
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
			setName("ProgressDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setSize(320, 160);
			setContentPane(getUIDialogContentPane());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		addWindowListener(new DlgWindowsAdapter());
		// user code end
	}

	/**
	 * ���������ֵ���߳� �������ڣ�(2003-10-21 12:59:12)
	 */
	public ThreadProgress getThread_DD(String dsName, int iAdvantage,
			IBusiDatadict iBusiDatadict) {
		ThreadProgress_DD thProgress = new ThreadProgress_DD();
		thProgress.setDsName(dsName);
		thProgress.setAdvantage(iAdvantage);
		thProgress.setBusiDatadict(iBusiDatadict);
		return thProgress;
	}

	/**
	 * ��͸��ѯ�߳� �������ڣ�(2003-10-21 12:59:12)
	 */
	public ThreadProgress getThread_PR(PenetrateRuleVO pr, Hashtable hashParam,
			Hashtable hashPeneRow, SimpleCrossVO[] scs) {
		ThreadProgress_PR thProgress = new ThreadProgress_PR();
		thProgress.setPeneInfo(pr, hashParam, hashPeneRow, scs);
		return thProgress;
	}

	/**
	 * �����߳� �������ڣ�(2003-10-21 12:59:12)
	 */
	public ThreadProgress getThread_QEC(QueryModelDef qmd, Hashtable hashParam,
			BIQueryExecutor qe, String dsName) {
		ThreadProgress_QECreate thProgress = new ThreadProgress_QECreate();
		thProgress.setQueryInfo(qmd, hashParam, qe, dsName);
		return thProgress;
	}

	/**
	 * �����߳� �������ڣ�(2003-10-21 12:59:12)
	 */
	public ThreadProgress getThread_QEQ(QueryModelDef qmd, Hashtable hashParam,
			BIQueryExecutor qe, String dsName) {
		ThreadProgress_QEQuery thProgress = new ThreadProgress_QEQuery();
		thProgress.setQueryInfo(qmd, hashParam, qe, dsName);
		return thProgress;
	}

	/**
	 * �����ı���ʾ �������ڣ�(2003-10-22 9:47:28)
	 * 
	 * @param strHint
	 *            java.lang.String
	 */
	public void setHint(String strHint) {
		getLabelHint().setText(strHint);
	}

	/**
	 * ���ö��������ݿⲢ��ʱ���ȼ� �������ڣ�(2003-10-21 12:53:27)
	 * 
	 * @param iAdv
	 *            int
	 */
	public void setAdvantage(int iAdv) {
		m_iAdvantage = iAdv;
	}

	/**
	 * �������ڣ�(2003-10-21 12:53:27)
	 * 
	 * @param newDsName
	 *            java.lang.String
	 */
	public void setDsName(java.lang.String newDsName) {
		m_dsName = newDsName;
	}

	/**
	 * ���ô�͸��Ϣ �������ڣ�(2003-10-22 11:36:43)
	 * 
	 * @param qmd
	 *            nc.vo.pub.querymodel.QueryModelDef
	 * @param hashParam
	 *            java.util.Hashtable
	 * @param qe
	 *            nc.vo.iuforeport.businessquery.QueryExecutor
	 */
	public void setPeneInfo(PenetrateRuleVO pr, Hashtable hashParam,
			Hashtable hashPeneRow, SimpleCrossVO[] scs) {

		m_pr = pr;
		m_hashParam = hashParam;
		m_hashPeneRow = hashPeneRow;
		m_scs = scs;
	}

	/**
	 * ���ò�ѯ��Ϣ �������ڣ�(2003-10-22 11:36:43)
	 * 
	 * @param qmd
	 *            nc.vo.pub.querymodel.QueryModelDef
	 * @param hashParam
	 *            java.util.Hashtable
	 * @param qe
	 *            nc.vo.iuforeport.businessquery.QueryExecutor
	 */
	public void setQueryInfo(QueryModelDef qmd, Hashtable hashParam,
			BIQueryExecutor qe) {

		m_qmd = qmd;
		m_hashParam = hashParam;
		m_qe = qe;
	}

	/**
	 * ������������ �������ڣ�(2003-10-22 12:09:57)
	 * 
	 * @param iType
	 *            int
	 */
	public void setType(int iType) {
		m_iType = iType;
	}

	boolean shouldContinue() {
		return !(isClosed() || progressThread == null || progressThread.isEnd());
	}

	synchronized boolean isClosed() {
		return bClosed;
	}

	synchronized void closeIfUnClosed() {
		if (!bClosed) {
			getProgressResult();
			closeOK();
		}
	}

	synchronized void setClosed(boolean bClosed) {
		this.bClosed = bClosed;
	}

	protected void close() {
		setClosed(true);
		super.close();
	}

	public int startProgress() {
		progressThread = getProgressThread();
		if (progressThread == null) {
			setClosed(true);
			return UIDialog.ID_CANCEL;
		}
		setClosed(false);
		return showModal();
	}

	private ThreadProgress getProgressThread() {
		try {
			switch (m_iType) {
			case 1: {
				return getThread_DD(m_dsName, m_iAdvantage, m_iBusiDatadict);
			}
			case 2: {
				return getThread_QEC(m_qmd, m_hashParam, m_qe, m_dsName);
			}
			case 3: {
				return getThread_QEQ(m_qmd, m_hashParam, m_qe, m_dsName);
			}
			case 4: {
				return getThread_PR(m_pr, m_hashParam, m_hashPeneRow, m_scs);
			}
			default:
				return progressThread;
			}
		} catch (Exception e) {
			AppDebug.debug(e);//@devTools System.out.println(e);
			return null;
		}
	}

	void getProgressResult() {
		Object result = progressThread.getResult();
		try {
			switch (m_iType) {
			case 1: {
				m_dd = (Datadict) result;
				break;
			}
			case 2: {
				ArrayList resultList = (ArrayList) result;
				m_iRecordCount = ((Integer) resultList.get(0)).intValue();
				m_mds = (BIMultiDataSet) resultList.get(1);
				break;
			}
			case 3: {
				m_mds = (BIMultiDataSet) result;
				break;
			}
			case 4: {
				m_dsdwc = (DataSetDataWithColumn) result;
				break;
			}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	public void setProgressThread(ThreadProgress progressThread) {
		this.progressThread = progressThread;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	/**
	 */
	private void myWindowActivated(WindowEvent e) {
		try {
			progressThread.start();
			refresh = new RefreshThread(this, "refresh");
			refresh.start();
		} catch (IllegalThreadStateException e1) {
			AppDebug.debug("�����߳���������");//@devTools System.out.println("�����߳���������");
		}
	}

	/**
	 * ����ҵ�������ֵ�ӿ�
	 * 
	 * @param newIBusiDatadict
	 *            nc.ui.pub.querymodel.IBusiDatadict
	 */
	public void setBusiDatadict(IBusiDatadict newIBusiDatadict) {
		m_iBusiDatadict = newIBusiDatadict;
	}
}
   