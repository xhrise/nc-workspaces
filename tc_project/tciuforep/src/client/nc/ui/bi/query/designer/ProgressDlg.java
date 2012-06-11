/*
 * 创建日期 2005-7-4
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
 * 进度对话框
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
	//关闭标志
	private boolean bClosed = false;

	//进度条方向
	boolean bIncrease = true;

	//工作线程
	private ThreadProgress progressThread = null;

	//刷新线程
	private RefreshThread refresh = null;

	//刷新间隔
	int interval = 100;

	//启动类型（1-DD，2-QEC，3-QEQ，4-PR）
	private int m_iType = 0;

	//定义数据源
	private String m_dsName = null;

	//多类型数据库并存优先级
	private int m_iAdvantage = QueryConst.DB_NC;

	//查询模型定义
	private QueryModelDef m_qmd = null;

	//参数哈希表
	private Hashtable m_hashParam = null;

	//查询执行器
	private BIQueryExecutor m_qe = null;

	//	//含列查询结果集
	//	private DataSetDataWithColumn m_dsdwc = null;
	//穿透规则
	private PenetrateRuleVO m_pr = null;

	//穿透行哈希表
	protected Hashtable m_hashPeneRow = null;

	//交叉VO
	private SimpleCrossVO[] m_scs = null;

	//数据字典实例（用于线程DD）
	private Datadict m_dd = null;

	//查询记录数（用于线程QEC）
	private int m_iRecordCount = 0;

	//查询结果集（用于线程QEQ）
	private BIMultiDataSet m_mds = null;

	//含列查询结果集（用于线程PR）
	private DataSetDataWithColumn m_dsdwc = null;

	//业务数据字典接口
	private IBusiDatadict m_iBusiDatadict = null;

	private JPanel ivjUIDialogContentPane = null;

	private UILabel ivjLabelHint = null;

	/**
	 * ProgressDlg 构造子注解。
	 * @deprecated
	 */
	public ProgressDlg() {
		super();
		initialize();
	}

	/**
	 * ProgressDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public ProgressDlg(java.awt.Container parent) {
		super(parent);
		initialize();
	}

	/**
	 * 此处插入方法说明。 创建日期：(2004-6-9 15:09:38)
	 */
	public void clear() {
		//数据字典实例（用于线程DD）
		m_dd = null;

		//查询结果集（用于线程QEQ）
		m_mds = null;
		//含列查询结果集（用于线程PR）
		m_dsdwc = null;
	}

	/**
	 * 创建日期：(2003-10-21 13:00:33)
	 * 
	 * @return nc.vo.pub.ddc.datadict.Datadict
	 */
	public Datadict getDatadict() {
		return m_dd;
	}

	/**
	 * 创建日期：(2003-10-21 12:53:27)
	 * 
	 * @return nc.vo.pub.ddc.datadict.Datadict
	 */
	public DataSetDataWithColumn getDataSetDataWithColumn() {
		return m_dsdwc;
	}

	/**
	 * 返回 LabelHint 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelHint() {
		if (ivjLabelHint == null) {
			try {
				ivjLabelHint = new nc.ui.pub.beans.UILabel();
				ivjLabelHint.setName("LabelHint");
				ivjLabelHint.setText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10241201", "UPP10241201-001176")/*
																	  * @res
																	  * "正在加载数据字典..."
																	  */);
				ivjLabelHint.setForeground(java.awt.Color.black);
				ivjLabelHint
						.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				ivjLabelHint.setILabelType(0/** Java默认(自定义) */
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
	 * 获得多结果集 创建日期：(2003-10-22 11:42:56)
	 * 
	 * @return com.borland.dx.dataset.StorageDataSet
	 */
	public BIMultiDataSet getMultiDataSet() {
		return m_mds;
	}

	/**
	 * 获得记录行数 创建日期：(2003-10-22 11:41:32)
	 * 
	 * @return int
	 */
	public int getRecordCount() {
		return m_iRecordCount;
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return javax.swing.JPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 加载数据字典的线程 创建日期：(2003-10-21 12:59:12)
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
	 * 穿透查询线程 创建日期：(2003-10-21 12:59:12)
	 */
	public ThreadProgress getThread_PR(PenetrateRuleVO pr, Hashtable hashParam,
			Hashtable hashPeneRow, SimpleCrossVO[] scs) {
		ThreadProgress_PR thProgress = new ThreadProgress_PR();
		thProgress.setPeneInfo(pr, hashParam, hashPeneRow, scs);
		return thProgress;
	}

	/**
	 * 启动线程 创建日期：(2003-10-21 12:59:12)
	 */
	public ThreadProgress getThread_QEC(QueryModelDef qmd, Hashtable hashParam,
			BIQueryExecutor qe, String dsName) {
		ThreadProgress_QECreate thProgress = new ThreadProgress_QECreate();
		thProgress.setQueryInfo(qmd, hashParam, qe, dsName);
		return thProgress;
	}

	/**
	 * 启动线程 创建日期：(2003-10-21 12:59:12)
	 */
	public ThreadProgress getThread_QEQ(QueryModelDef qmd, Hashtable hashParam,
			BIQueryExecutor qe, String dsName) {
		ThreadProgress_QEQuery thProgress = new ThreadProgress_QEQuery();
		thProgress.setQueryInfo(qmd, hashParam, qe, dsName);
		return thProgress;
	}

	/**
	 * 设置文本提示 创建日期：(2003-10-22 9:47:28)
	 * 
	 * @param strHint
	 *            java.lang.String
	 */
	public void setHint(String strHint) {
		getLabelHint().setText(strHint);
	}

	/**
	 * 设置多类型数据库并存时优先级 创建日期：(2003-10-21 12:53:27)
	 * 
	 * @param iAdv
	 *            int
	 */
	public void setAdvantage(int iAdv) {
		m_iAdvantage = iAdv;
	}

	/**
	 * 创建日期：(2003-10-21 12:53:27)
	 * 
	 * @param newDsName
	 *            java.lang.String
	 */
	public void setDsName(java.lang.String newDsName) {
		m_dsName = newDsName;
	}

	/**
	 * 设置穿透信息 创建日期：(2003-10-22 11:36:43)
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
	 * 设置查询信息 创建日期：(2003-10-22 11:36:43)
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
	 * 设置启动类型 创建日期：(2003-10-22 12:09:57)
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
	 * （非 Javadoc）
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
			AppDebug.debug("工作线程已在运行");//@devTools System.out.println("工作线程已在运行");
		}
	}

	/**
	 * 设置业务数据字典接口
	 * 
	 * @param newIBusiDatadict
	 *            nc.ui.pub.querymodel.IBusiDatadict
	 */
	public void setBusiDatadict(IBusiDatadict newIBusiDatadict) {
		m_iBusiDatadict = newIBusiDatadict;
	}
}
   