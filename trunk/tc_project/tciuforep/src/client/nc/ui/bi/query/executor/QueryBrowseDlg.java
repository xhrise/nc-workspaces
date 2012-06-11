package nc.ui.bi.query.executor;
import java.awt.Dimension;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UISpanTable;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UIViewport;
import nc.ui.pub.beans.table.AttributiveCellTableModel;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.DefaultCellAttribute;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.ui.pub.beans.table.RowHeaderRenderer;
import nc.ui.pub.querymodel.CreateTableDlg;
import nc.ui.pub.querymodel.DefDsWrapper;
import nc.ui.pub.querymodel.PeneRuleDlg;
import nc.ui.pub.querymodel.ProgressDlg;
import nc.ui.pub.querymodel.RotateCrossSetDlg;
import nc.ui.pub.querymodel.SimpleCrossSetDlg;
import nc.ui.pub.querymodel.UIUtil;
import nc.ui.reportquery.demo.ExampleFileFilter;
import nc.vo.bi.query.manager.BIDatasetUtil;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.dbbase.QEDataSetAfterProcessDescriptor;
import nc.vo.pub.querymodel.PenetrateRuleVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.pub.querymodel.RotateCrossVO;
import nc.vo.pub.querymodel.SimpleCrossVO;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.TextDataFile;
import com.borland.dx.dataset.Variant;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ��ѯԤ���Ի��� �������ڣ�(2002-11-9 13:55:06)
 * 
 * @author���쿡��
 */
public class QueryBrowseDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��������Դ
	protected DefDsWrapper m_defDsWrapper = null;

	//��ѯ������ʵ��
	private QueryBrowsePanel m_pnBrowse = null;

	//��ѯִ�����ݿ�����
	private String m_strDbtype = null;

	//��ѯ�������ݿ�����
	private String m_strDbtypeForDef = null;

	//������ѯID���䣩
	private String m_queryId = null;

	//��͸��ʷ����
	private Vector<Object[]> m_vecPeneHistory = new Vector<Object[]>();

	//ͶӰ���涨�壨�䣩
	private SimpleCrossVO[] m_scs = null;

	//��ת���涨�壨�䣩
	private RotateCrossVO m_rc = null;

	//��ѯ�������壨���䣩
	private QueryBaseDef m_qbd = null;

	//ԭʼ��ѯģ�Ͷ��壨���䣩
	private QueryModelDef m_originQmd = null;

	//����������ϣ��
	private Hashtable m_hashParam = null;

	//�����Ի���ʵ��
	private UIFileChooser m_fileChooser = null;

	private UIButton ivjBnCancel = null;

	private UIPanel ivjPnCenter = null;

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnCreate = null;

	private UIButton ivjBnPenetrate = null;

	private UIPanel ivjPnNorth = null;

	private UIButton ivjBnBack = null;

	private UIButton ivjBnExportData = null;

	private UIButton ivjBnCross = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements java.awt.event.ActionListener,
			java.awt.event.MouseListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == QueryBrowseDlg.this.getBnCancel())
				connEtoC2(e);
			if (e.getSource() == QueryBrowseDlg.this.getBnCreate())
				connEtoC1(e);
			if (e.getSource() == QueryBrowseDlg.this.getBnPenetrate())
				connEtoC3(e);
			if (e.getSource() == QueryBrowseDlg.this.getBnBack())
				connEtoC4(e);
			if (e.getSource() == QueryBrowseDlg.this.getBnExportData())
				connEtoC7(e);
			if (e.getSource() == QueryBrowseDlg.this.getBnCross())
				connEtoC8(e);
		};

		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == QueryBrowseDlg.this.getPnSouth())
				connEtoC5(e);
		};

		public void mouseEntered(java.awt.event.MouseEvent e) {
		};

		public void mouseExited(java.awt.event.MouseEvent e) {
		};

		public void mousePressed(java.awt.event.MouseEvent e) {
		};

		public void mouseReleased(java.awt.event.MouseEvent e) {
		};
	};

	/**
	 * InfoDlg ������ע�⡣
	 */
	public QueryBrowseDlg() {
		super();
		initialize();
	}

	/**
	 * InfoDlg ������ע�⡣
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public QueryBrowseDlg(java.awt.Container parent, DefDsWrapper defDsWrapper) {
		super(parent);
		m_defDsWrapper = defDsWrapper;
		initialize();
	}

	/**
	 * ����
	 */
	public void bnBack_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

		int iSize = m_vecPeneHistory.size();
		//if (m_scs != null || m_rc != null)
		getBnCross().setEnabled(true);
		if (iSize == 0)
			return;
		//��ȡ��͸��ʷ����
		Object[] objDsId = (Object[]) m_vecPeneHistory.elementAt(iSize - 1);
		m_vecPeneHistory.removeElementAt(iSize - 1);
		//���ǰһ����ѯID�ͽ����
		StorageDataSet ds = (StorageDataSet) objDsId[0];
		m_queryId = (String) objDsId[1];
		m_scs = (SimpleCrossVO[]) objDsId[2];
		m_rc = (RotateCrossVO) objDsId[3];
		int iSelRow = ((Integer) objDsId[4]).intValue();
		int iSelCol = ((Integer) objDsId[5]).intValue();
		//����Ԥ�����
		getPnBrowse().setDataset(ds, getWidth());
		getPnBrowse().setSelRow(iSelRow);
		//��ť�����Ե���
		getBnPenetrate().setEnabled(true);
		if (iSize == 1) {
			getBnCreate().setEnabled(true);
			getBnBack().setEnabled(false);
		}
		//��ͷ����
		changeUI(m_scs, m_rc, null, iSelCol, ds.getRowCount());
	}

	/**
	 * ȡ��
	 */
	public void bnCancel_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * ����
	 */
	public void bnCreate_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		//������ݼ�
		StorageDataSet dataSet = getPnBrowse().getDataset();
		//����
		CreateTableDlg dlg = new CreateTableDlg(this, m_defDsWrapper
				.getDefDsName());
		dlg.setDbtype(m_strDbtype, m_strDbtypeForDef);
		dlg.setQueryIdAndName(m_queryId, m_originQmd.getDisplayName());
		dlg.setData(dataSet);
		dlg.showModal();
		dlg.destroy();
	}

	/**
	 * ����
	 */
	public void bnCross_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (m_scs != null) {
			if (BIModelUtil.isMergeQuery(m_originQmd)) {
				//��֧�ֽ���
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "��ѯ����" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000080")/* @res "�ϲ���ѯ��֧���ٽ���" */);
				return;
			}
			SimpleCrossSetDlg dlg = new SimpleCrossSetDlg(this);
			dlg.setScs(m_scs);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				m_scs = dlg.getScs();
				//ִ�н���
				try {
					StorageDataSet sds = BIDatasetUtil
							.getDatasetForSimpleCross(m_scs, m_qbd, m_hashParam);
					getPnBrowse().setDataset(sds, getWidth());
					//����ˢ��
					procRowHeader(sds.getRowCount());
				} catch (Exception e) {
					AppDebug.debug(e);
				}
				getBnCreate().setEnabled(false);
			}
		} else {
			//if (m_rc != null) {
			//���ԭʼ�������ԭʼ����Ϣ
			StorageDataSet dsBeforeRotate = getPnBrowse().getDsBeforeRotate();
			if (dsBeforeRotate == null) {
				dsBeforeRotate = getPnBrowse().getDataset();
				getPnBrowse().setDsBeforeRotate(dsBeforeRotate);
			}
			SelectFldVO[] sfs = BIDatasetUtil
					.getSelectFldByDataset(dsBeforeRotate.getColumns());
			//����
			RotateCrossSetDlg dlg = new RotateCrossSetDlg(this);
			dlg.setRotateCross(m_rc, sfs);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				m_rc = dlg.getRotateCross();
				//ִ�н���
				try {
					Object[] objs = BIDatasetUtil.getDatasetForRotateCross(
							m_rc, dsBeforeRotate);
					StorageDataSet sds = (StorageDataSet) objs[0];
					//�۸�����
					int iRowCount = (m_rc.getStrRows() == null) ? 0 : m_rc
							.getStrRows().length;
					BIDatasetUtil.tamperColname(sds, iRowCount);
					//
					getPnBrowse().setDataset(sds, getWidth());
					//��ö��ͷ
					FldgroupVO[] fldgroups = (FldgroupVO[]) objs[1];
					//��ת������ͷ����
					procColHeader(getPnBrowse().getTable(), fldgroups);
					getBnCreate().setEnabled(false);
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
		}
	}

	/**
	 * ��������
	 */
	public void bnExportData_ActionPerformed(
			java.awt.event.ActionEvent actionEvent) {

		doExport();
	}

	/**
	 * �Ǳ���
	 */
	public void bnMeter_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	}

	/**
	 * ��͸
	 * @i18n miufo00373=��ֵͶӰ��
	 */
	public void bnPenetrate_ActionPerformed(
			java.awt.event.ActionEvent actionEvent) {

		try {
			//���ѡ�д�͸�еĹ�ϣ��
			Hashtable hashPeneRow = getPnBrowse().getPeneRow();
			if (hashPeneRow == null)
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "��ѯ����" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000821")/* @res "δѡ��͸��" */);
			else {
				//���ģ�Ͷ���
				QueryModelDef qmd = BIModelUtil.getQueryDef(m_queryId,
						m_defDsWrapper.getDefDsName());
				//���ȫ����͸������
				PenetrateRuleVO[] prs = qmd.getPenetrateRules();
				//ѡ��͸������
				PeneRuleDlg dlg = new PeneRuleDlg(this,m_defDsWrapper
						.getDefDsName(), false);
				dlg.setPenetrateRules(prs);
				dlg.showModal();
				dlg.destroy();
				if (dlg.getResult() == UIDialog.ID_OK) {
					int iSelRow = dlg.getSelRow();
					if (iSelRow == -1)
						return;
					//��ô�͸������
					PenetrateRuleVO pr = prs[iSelRow];
					//��ť�����Ե���
					getBnCreate().setEnabled(false);
					getBnCross().setEnabled(false);
					getBnBack().setEnabled(true);
					//���봩͸��ʷ����
					iSelRow = getPnBrowse().getSelRow();
					int iSelCol = getPnBrowse().getSelCol();
					Object[] objDsId = new Object[] {
							getPnBrowse().getDataset(), m_queryId, m_scs, m_rc,
							new Integer(iSelRow), new Integer(iSelCol) };
					m_vecPeneHistory.addElement(objDsId);
					//���ѡ�е�Ԫ��ͶӰ��������VO
					SimpleCrossVO[] scs = null;
					if (m_scs != null) {
						//��ֵͶӰ���⴦��
						SelectFldVO[] sfs = qmd.getQueryBaseVO()
								.getSelectFlds();
						int iSfLen = (sfs == null) ? 0 : sfs.length;
						if (iSfLen != 1) {
							System.out
									.println(StringResource
											.getStringResource("miufo00373") + iSelCol + "/" + iSfLen);
							iSelCol = iSelCol / iSfLen;
						}
						//���촫����̨������VO
						scs = BIModelUtil.getRowColScs(m_scs, iSelRow, iSelCol);
					}
					//����
					ProgressDlg dlgProg = new ProgressDlg(this);
					dlgProg
							.setTitle(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("10241201",
											"UPP10241201-000822")/* @res "���ݴ�͸" */);
					dlgProg.setHint(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10241201", "UPP10241201-000823")/*
																		  * @res
																		  * "����ִ�����ݴ�͸
																		  * ..."
																		  */);
					//���������߳�
					//				ThreadProgRun thProgRun = new ThreadProgRun();
					//				thProgRun.setDsName(m_defDsWrapper.getDefDsName());
					//				thProgRun.setDlg(dlgProg);
					//				thProgRun.setType(4);
					//				thProgRun.setPeneInfo(pr, m_hashParam, hashPeneRow, scs);
					//				thProgRun.start();
					//��ʾ��
					dlgProg.setDsName(m_defDsWrapper.getDefDsName());
					dlgProg.setType(4);
					dlgProg.setPeneInfo(pr, m_hashParam, hashPeneRow, scs);
					int result = dlgProg.startProgress();
					//				dlgProg.destroy();
					if (result == UIDialog.ID_OK) {
						//��ô�͸���
						QEDataSetAfterProcessDescriptor dsdwc = dlgProg
								.getQEDataSetDescriptor();
						//DataSetDataWithColumn dsdwc =
						// PenetrateRuleUtilBO_Client.penetrate(pr, m_hashParam,
						// hashPeneRow, scs, ModelUtil.getLoginEnv());
						//����²�ѯID�ͽ����
						m_queryId = dsdwc.getQmdID();
						StorageDataSet ds = makeDataSet(dsdwc);
						//����Ԥ�����
						getPnBrowse().setDataset(ds, getWidth());
						//���ģ�Ͷ���
						qmd = BIModelUtil.getQueryDef(m_queryId, m_defDsWrapper
								.getDefDsName());
						//���ȫ����͸������
						prs = qmd.getPenetrateRules();
						if (prs == null || prs.length == 0)
							getBnPenetrate().setEnabled(false);
						//��ͷ����
						changeUI(qmd.getScs(), qmd.getRotateCross(), qmd
								.getFldgroups(), iSelCol, ds.getRowCount());
						//
						m_scs = qmd.getScs();
						m_rc = qmd.getRotateCross();
						//System.out.println("[��������] " +
						// ModelUtil.getHashString(dsdwc.getHashParam()));
					}
					dlgProg.clear();
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000824")/* @res "��͸ʧ��" */);
		}
	}

	/**
	 * ������ͷ�Ľ��洦�� �������ڣ�(2003-11-19 13:04:17)
	 * 
	 * @param scs
	 *            nc.vo.pub.querymodel.SimpleCrossVO
	 */
	private void changeUI(SimpleCrossVO[] scs, RotateCrossVO rc,
			FldgroupVO[] fldgroups, int iSelCol, int iRowCount) {

		if (scs == null) {
			getPnBrowse().getTable().setColumnSelectionAllowed(false);
			//�����ͷ
			JViewport vp = getPnBrowse().getTablePnPub().getRowHeader();
			if (vp != null)
				getPnBrowse().getTablePnPub().remove(vp);
			//��ת������ͷ����
			procColHeader(getPnBrowse().getTable(), fldgroups);
			//��ť������
			//getBnCross().setEnabled(rc != null);
		} else {
			if (iSelCol != -1)
				getPnBrowse().getTable().setColumnSelectionInterval(iSelCol,
						iSelCol);
			procRowHeader(iRowCount);
			//��ť������
			getBnCross().setEnabled(true);
		}
		validate();
	}

	/**
	 * connEtoC1: (BnOK.action.actionPerformed(java.awt.event.ActionEvent) -->
	 * InfoDlg.bnOK_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC1(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnCreate_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (BnCancel.action.actionPerformed(java.awt.event.ActionEvent)
	 * --> InfoDlg.bnCancel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnCancel_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3:
	 * (BnPenetrate.action.actionPerformed(java.awt.event.ActionEvent) -->
	 * QueryBrowseDlg.bnPenetrate_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC3(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnPenetrate_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BnBack.action.actionPerformed(java.awt.event.ActionEvent) -->
	 * QueryBrowseDlg.bnBack_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC4(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnBack_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (PnSouth.mouse.mouseClicked(java.awt.event.MouseEvent) -->
	 * QueryBrowseDlg.pnSouth_MouseClicked(Ljava.awt.event.MouseEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.MouseEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC5(java.awt.event.MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.pnSouth_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7:
	 * (BnExportData.action.actionPerformed(java.awt.event.ActionEvent) -->
	 * QueryBrowseDlg.bnExportData_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC7(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnExportData_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC8: (BnCross.action.actionPerformed(java.awt.event.ActionEvent)
	 * -->
	 * QueryBrowseDlg.bnCross_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * 
	 * @param arg1
	 *            java.awt.event.ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
	private void connEtoC8(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnCross_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * ���ݵ��� �������ڣ�(2004-5-14 8:43:41)
	 */
	protected void doExport() {
		doExportTxt(true);
	}

	/**
	 * ���ݵ���Ϊ�ı� �������ڣ�(2004-5-14 8:43:41)
	 */
	private void doExportTxt(boolean bChangeScale) {
		//����
		try {
			TextDataFile tdf = new TextDataFile();
			tdf.setDelimiter("'");
			//��õ����ļ�·��
			String path = getExportPath(false);
			if (path.equals(""))
				return;
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					path));
			DataSet dataSet = getPnBrowse().getDataset();
			//д����
			int colCount = dataSet.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				//�۸�ORACLE�̶�
				if (bChangeScale
						&& dataSet.getColumn(i).getDataType() == Variant.BIGDECIMAL
						&& dataSet.getColumn(i).getScale() == 0)
					dataSet.getColumn(i).setScale(4);
				//
				out.write(dataSet.getColumn(i).getCaption().getBytes());
				out.write(new byte[] { (byte) '\t' });
				//out.write(tdf.getSeparator().getBytes());
				//out.write(new byte[] { Byte.parseByte(tdf.getSeparator())});
			}
			out.write(new byte[] { (byte) '\r', (byte) '\n' });
			//д����
			tdf.save(dataSet, out, null);
			out.flush();
			out.close();
			//tdf.setFileName(path);
			//tdf.save(getPnBrowse().getDataset());
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000825")/* @res "�������" */);
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000826")/* @res "����ʧ��" */);
		}
	}

	/**
	 * ���� BnBack ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnBack() {
		if (ivjBnBack == null) {
			try {
				ivjBnBack = new nc.ui.pub.beans.UIButton();
				ivjBnBack.setName("BnBack");
				ivjBnBack.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000071")/* @res "����" */);
				// user code begin {1}
				ivjBnBack.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnBack, ivjBnBack.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnBack;
	}

	/**
	 * ���� BnCancel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000405")/* @res "�ر�" */);
				// user code begin {1}
				ivjBnCancel.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnCancel, ivjBnCancel.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnCancel;
	}

	/**
	 * ���� BnOK ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnCreate() {
		if (ivjBnCreate == null) {
			try {
				ivjBnCreate = new nc.ui.pub.beans.UIButton();
				ivjBnCreate.setName("BnCreate");
				ivjBnCreate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000827")/* @res "����" */);
				// user code begin {1}
				ivjBnCreate.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnCreate, ivjBnCreate.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnCreate;
	}

	/**
	 * ���� BnCross ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnCross() {
		if (ivjBnCross == null) {
			try {
				ivjBnCross = new nc.ui.pub.beans.UIButton();
				ivjBnCross.setName("BnCross");
				ivjBnCross.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10241201", "UPP10241201-000078")/*
																	  * @res
																	  * "�򵥽���"
																	  */);
				ivjBnCross.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000070")/* @res "����" */);
				// user code begin {1}
				ivjBnCross.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnCross, ivjBnCross.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnCross;
	}

	/**
	 * ���� BnExportData ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnExportData() {
		if (ivjBnExportData == null) {
			try {
				ivjBnExportData = new nc.ui.pub.beans.UIButton();
				ivjBnExportData.setName("BnExportData");
				ivjBnExportData
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000107")/* @res "����" */);
				// user code begin {1}
				ivjBnExportData.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnExportData, ivjBnExportData.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnExportData;
	}

	/**
	 * ���� BnPenetrate ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnPenetrate() {
		if (ivjBnPenetrate == null) {
			try {
				ivjBnPenetrate = new nc.ui.pub.beans.UIButton();
				ivjBnPenetrate.setName("BnPenetrate");
				ivjBnPenetrate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000060")/* @res "��͸" */);
				// user code begin {1}
				ivjBnPenetrate.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnPenetrate, ivjBnPenetrate.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnPenetrate;
	}

	/**
	 * ��õ���·�� �������ڣ�(2003-10-29 15:45:31)
	 * 
	 * @return java.lang.String
	 */
	private String getExportPath(boolean bImport) {
		if (m_fileChooser == null)
			return "";
		//ѡ�񵼳�·��
		String strFullName = "";
		UIFileChooser fileChooser = m_fileChooser;
		javax.swing.filechooser.FileFilter[] oldFilters = fileChooser
				.getChoosableFileFilters();
		javax.swing.filechooser.FileFilter oldFilter = null;
		if (oldFilters != null && oldFilters.length > 0) {
			oldFilter = oldFilters[0];
			fileChooser.removeChoosableFileFilter(oldFilter);
		}
		javax.swing.filechooser.FileFilter newFilter = new ExampleFileFilter(
				"txt");
		fileChooser.addChoosableFileFilter(newFilter);
		fileChooser.setFileFilter(newFilter);

		while (true) {
			if (fileChooser.showDialog(this, nc.ui.ml.NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000107")/*
																  * @res "����"
																  */) == UIFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				String strFileName = fileChooser.getName(file);
				if (!strFileName.endsWith(".txt"))
					strFileName += ".txt";
				strFullName = fileChooser.getCurrentDirectory() + "";
				if (!strFullName.endsWith(System.getProperty("file.separator")))
					strFullName += System.getProperty("file.separator");
				strFullName += strFileName;
				file = new File(strFullName);
				if (file.exists())
					if (bImport)
						break;
					else if (MessageDialog.showYesNoDlg(fileChooser,
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-000828")/*
																	  * @res
																	  * "ȷ���ļ�����"
																	  */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-000829", null,
									new String[] { strFullName })/*
																  * @res
																  * "�ļ�\"{0}\"�Ѿ����ڣ�ȷ��Ҫ���Ǹ��ļ���"
																  */) != UIDialog.ID_YES) {
						strFullName = "";
						break;
					}
			}
			break;
		}
		fileChooser.removeChoosableFileFilter(newFilter);
		if (oldFilter != null) {
			fileChooser.addChoosableFileFilter(oldFilter);
			fileChooser.setFileFilter(oldFilter);
		}
		return strFullName;
	}

//	/**
//	 * ���ԭʼ����� �������ڣ�(2003-11-28 10:29:09)
//	 * 
//	 * @return com.borland.dx.dataset.StorageDataSet
//	 */
//	private DataSet getOriginDataset() {
//		int iSize = m_vecPeneHistory.size();
//		if (iSize == 0)
//			return getPnBrowse().getDataset();
//		//��ȡ��͸��ʷ����
//		Object[] objDsId = (Object[]) m_vecPeneHistory.elementAt(iSize - 1);
//		return null;
//	}

	/**
	 * ��ò�ѯ������ʵ�� �������ڣ�(2003-4-3 17:00:54)
	 * 
	 * @return nc.ui.iuforeport.businessquery.QueryDefTabbedPn
	 */
	public QueryBrowsePanel getPnBrowse() {
		if (m_pnBrowse == null)
			m_pnBrowse = new QueryBrowsePanel();
		return m_pnBrowse;
	}

	/**
	 * ���� PnCenter ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnCenter() {
		if (ivjPnCenter == null) {
			try {
				ivjPnCenter = new nc.ui.pub.beans.UIPanel();
				ivjPnCenter.setName("PnCenter");
				ivjPnCenter.setBorder(new javax.swing.border.EtchedBorder());
				ivjPnCenter.setLayout(new java.awt.BorderLayout());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnCenter;
	}

	/**
	 * ���� PnNorth ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new java.awt.Dimension(10, 1));
				ivjPnNorth.setLayout(getPnNorthFlowLayout());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * ���� PnNorthFlowLayout ����ֵ��
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private java.awt.FlowLayout getPnNorthFlowLayout() {
		java.awt.FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* �������� */
			ivjPnNorthFlowLayout = new java.awt.FlowLayout();
			ivjPnNorthFlowLayout.setVgap(8);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
	}

	/**
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new nc.ui.pub.beans.UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setPreferredSize(new java.awt.Dimension(200, 32));
				ivjPnSouth.setLayout(getPnSouthFlowLayout());
				getPnSouth().add(getBnCross(), getBnCross().getName());
				getPnSouth().add(getBnCreate(), getBnCreate().getName());
				getPnSouth().add(getBnBack(), getBnBack().getName());
				getPnSouth().add(getBnPenetrate(), getBnPenetrate().getName());
				getPnSouth()
						.add(getBnExportData(), getBnExportData().getName());
				getPnSouth().add(getBnCancel(), getBnCancel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * ���� PnSouthFlowLayout ����ֵ��
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private java.awt.FlowLayout getPnSouthFlowLayout() {
		java.awt.FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* �������� */
			ivjPnSouthFlowLayout = new java.awt.FlowLayout();
			ivjPnSouthFlowLayout.setVgap(5);
			ivjPnSouthFlowLayout.setHgap(5);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * �����ͷ��ģ�� �������ڣ�(2003-11-1 14:30:52)
	 * 
	 * @return javax.swing.table.DefaultTableModel
	 */
	private AttributiveCellTableModel getTMRowHeader(Object[][] data,
			Object[] column) {

		AttributiveCellTableModel fixedModel = new AttributiveCellTableModel(
				data, column) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return 1;
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void setDataVector(Vector newData, Vector columnNames) {
				if (newData == null)
					throw new IllegalArgumentException(
							"setDataVector() - Null parameter");
				dataVector = new Vector(0);
				if (columnNames != null) {
					columnIdentifiers = columnNames;
				} else {
					columnIdentifiers = new Vector();
				}

				fireTableStructureChanged();
				dataVector = newData;

				//
				cellAtt = new DefaultCellAttribute(dataVector.size(),
						columnIdentifiers.size());

				newRowsAdded(new TableModelEvent(this, 0, getRowCount() - 1,
						TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
			}
		};
		return fixedModel;
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
				ivjUIDialogContentPane.setLayout(new java.awt.BorderLayout());
				getUIDialogContentPane().add(getPnSouth(), "South");
				getUIDialogContentPane().add(getPnCenter(), "Center");
				getUIDialogContentPane().add(getPnNorth(), "North");
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
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBnCancel().addActionListener(ivjEventHandler);
		getBnCreate().addActionListener(ivjEventHandler);
		getBnPenetrate().addActionListener(ivjEventHandler);
		getBnBack().addActionListener(ivjEventHandler);
		getPnSouth().addMouseListener(ivjEventHandler);
		getBnExportData().addActionListener(ivjEventHandler);
		getBnCross().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("InfoDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000830")/* @res "��ѯ���Ԥ��" */);
			setSize(720, 512);
			setResizable(true);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		//getPnSouth().setVisible(false);
		getBnBack().setEnabled(false);
		getBnCreate().setVisible(false);
		// user code end
	}

	/**
	 * ���ݽ�������ݻ�ý���� �������ڣ�(2003-10-8 12:56:58)
	 * 
	 * @return com.borland.dx.dataset.StorageDataSet
	 * @param dsdwc
	 *            nc.vo.pub.querymodel.DataSetDataWithColumn
	 */
	private StorageDataSet makeDataSet(QEDataSetAfterProcessDescriptor dsdwc) {
		StorageDataSet dataSet = new StorageDataSet();
		dsdwc.getDataSetData().loadDataSet(dataSet);
		//�������
		for (int i = 0; i < dataSet.getColumnCount(); i++)
			dataSet.getColumn(i).setCaption(dsdwc.getCaptions()[i]);
		return dataSet;
	}

	/**
	 * ������¼�
	 */
	public void pnSouth_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 2) {
			//��ť�ɼ�
			if (mouseEvent.isControlDown())
				doExportTxt(false);
			else if (mouseEvent.isAltDown())
				; //getBnCreate().setVisible(!getBnCreate().isVisible());
			else
				setButtonVisible(!getBnCross().isVisible());
		}
	}

	/**
	 * Ϊ������ɶ��ͷ �������ڣ�(2003-11-26 14:22:45)
	 * 
	 * @param fldgrps
	 *            nc.vo.pub.cquery.FldgroupVO[]
	 */
	private void procColHeader(UITable table, FldgroupVO[] fldgrps) {
		try {
			TableColumnModel tcm = table.getColumnModel();
			Hashtable<String, ColumnGroup> htCgs = new Hashtable<String, ColumnGroup>();
			ArrayList<ColumnGroup> topCgs = new ArrayList<ColumnGroup>();
			int iLen = (fldgrps == null) ? 0 : fldgrps.length;
			for (int i = 0; i < iLen; i++) {
				ColumnGroup cg = (ColumnGroup) htCgs.get(fldgrps[i]
						.getGroupname());
				if (cg == null) {
					String text = fldgrps[i].getGroupname();
					int index = text.indexOf(QueryConst.CROSS_SEPERATOR);
					if (index > 0) {
						text = text.substring(index + 1);
					}
					cg = new ColumnGroup(text);
					htCgs.put(fldgrps[i].getGroupname(), cg);
				}
				String type = fldgrps[i].getGrouptype();
				if (type.equals("0")) {
					String item1 = fldgrps[i].getItem1();
					if (item1 != null) {
						int colIndex = table.convertColumnIndexToView(Integer
								.parseInt(item1));
						if (colIndex >= 0)
							cg.add(tcm.getColumn(colIndex));
					}
					String item2 = fldgrps[i].getItem2();
					if (item2 != null) {
						int colIndex = table.convertColumnIndexToView(Integer
								.parseInt(item2));
						if (colIndex >= 0)
							cg.add(tcm.getColumn(colIndex));
					}
				} else if (type.equals("1")) {
					String item1 = fldgrps[i].getItem1();
					if (item1 != null) {
						int colIndex = table.convertColumnIndexToView(Integer
								.parseInt(item1));
						if (colIndex >= 0)
							cg.add(tcm.getColumn(colIndex));
					}
					String item2 = fldgrps[i].getItem2();
					if (item2 != null) {
						ColumnGroup subCg = (ColumnGroup) htCgs.get(item2);
						if (subCg != cg)
							cg.add(subCg);
					}
				} else if (type.equals("2")) {
					String item1 = fldgrps[i].getItem1();
					if (item1 != null) {
						ColumnGroup subCg = (ColumnGroup) htCgs.get(item1);
						if (subCg != cg)
							cg.add(subCg);
					}
					String item2 = fldgrps[i].getItem2();
					if (item2 != null) {
						int colIndex = table.convertColumnIndexToView(Integer
								.parseInt(item2));
						if (colIndex >= 0)
							cg.add(tcm.getColumn(colIndex));
					}
				} else {
					String item1 = fldgrps[i].getItem1();
					if (item1 != null) {
						ColumnGroup subCg = (ColumnGroup) htCgs.get(item1);
						if (subCg != cg)
							cg.add(subCg);
					}
					String item2 = fldgrps[i].getItem2();
					if (item2 != null) {
						ColumnGroup subCg = (ColumnGroup) htCgs.get(item2);
						if (subCg != cg)
							cg.add(subCg);
					}
				}
				if (fldgrps[i].getToplevelflag().equalsIgnoreCase("Y"))
					topCgs.add(cg);
			}
			GroupableTableHeader header = (GroupableTableHeader) (table
					.getTableHeader());
			header.clearColumnGroups();
			for (int i = 0; i < topCgs.size(); i++)
				header.addColumnGroup((ColumnGroup) topCgs.get(i));
			//�д���
			for (int i = 0; i < tcm.getColumnCount(); i++) {
				TableColumn tc = tcm.getColumn(i);
				String headerValue = "" + tc.getHeaderValue();
				int index = headerValue.lastIndexOf(QueryConst.CROSS_SEPERATOR);
				if (index > 0) {
					headerValue = headerValue.substring(index + 1);
				}
				tc.setHeaderValue(headerValue);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * ��ͷ���� �������ڣ�(2003-11-1 14:26:39)
	 */
	private void procRowHeader(int iRowCount) {

		//������ѡȡģʽ
		getPnBrowse().getTable().setColumnSelectionAllowed(true);
		getPnBrowse().getTable().getColumnModel().getSelectionModel()
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//������ͷ��
		String[] rowHeaders = BIModelUtil.getRowHeaders(m_scs);
		Object[][] data = new Object[iRowCount][];
		int[] iRows = new int[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			data[i] = new Object[] { rowHeaders[i] };
			iRows[i] = i;
		}

		//��ģ��
		AttributiveCellTableModel fixModel = getTMRowHeader(data,
				new Object[] { "" });
		//����
		UISpanTable fixTable = new UISpanTable(fixModel);
		fixTable.setAutoResizeMode(UITable.AUTO_RESIZE_OFF);
		fixTable.setColumnWidth(new int[] { 100 });
		fixTable.setBackground(QueryConst.HEADER_BACK_COLOR);
		fixTable.setForeground(QueryConst.HEADER_FORE_COLOR);
		//����
		TableColumn tc = fixTable.getColumnModel().getColumn(0);
		RowHeaderRenderer renderer = new RowHeaderRenderer(fixTable);
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		tc.setCellRenderer(renderer);

		//�����ͷ��
		UIViewport viewport = new UIViewport();
		viewport.setView(fixTable);
		viewport.setPreferredSize(fixTable.getPreferredSize());
		//����ؼ�
		getPnBrowse().getTablePnPub().setRowHeader(viewport);

		/*
		 * //��Ԫ�ϲ����� CellSpan cellAtt = (CellSpan) fixModel.getCellAttribute();
		 * cellAtt.combine(iRows, new int[] { 0 }); //�п�ѡ UITable table =
		 * getPnBrowse().getTable(); table.setColumnSelectionAllowed(true); //��ͷ
		 * ColumnGroup cg = new ColumnGroup("��"); for (int i = 0; i <
		 * table.getColumnCount(); i++)
		 * cg.add(table.getColumnModel().getColumn(i)); GroupableTableHeader
		 * header = (GroupableTableHeader) table.getTableHeader();
		 * header.addColumnGroup(cg); header.setUpdateTableInRealTime(true);
		 * //����Ͻ�CORNER Object[] title = new Object[] { "" }; JList fillList =
		 * new JList(title); fillList.setFixedCellWidth(50);
		 * fillList.setFixedCellHeight( ((GroupableTableHeaderUI)
		 * (header.getUI())).getHeaderHeight());
		 * fillList.setBackground(Color.lightGray);
		 * //fillList.setCellRenderer(new RowHeaderRenderer(table));
		 * getPnBrowse().getTablePnPub().setCorner(
		 * ScrollPaneConstants.UPPER_LEFT_CORNER, fillList);
		 */
	}
//
//	/**
//	 * ѡ����һ�� �������ڣ�(2003-9-26 16:38:28)
//	 */
//	private void selectNext(final int iRowCount) {
//		Thread th = new Thread() {
//			public void run() {
//				for (int i = 0; i < iRowCount; i++) {
//					getPnBrowse().setSelRow(i);
//					try {
//						sleep(200);
//					} catch (InterruptedException e) {
//					}
//				}
//			}
//		};
//		th.start();
//	}

	/**
	 * ���ð�ť�ɼ��ԣ�FOR BPM�� �������ڣ�(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setButtonVisible(boolean bVisible) {
		getBnCross().setVisible(bVisible);
		getBnPenetrate().setVisible(bVisible);
		getBnBack().setVisible(bVisible);
		//getBnCreate().setVisible(bVisible);
		getBnExportData().setVisible(bVisible);
	}

	/**
	 * ���ò�ѯִ�кͲ�ѯ�������ݿ����� �������ڣ�(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setDbtype(String newDbtype, String newDbtypeForDef) {
		m_strDbtype = newDbtype;
		m_strDbtypeForDef = newDbtypeForDef;
	}

	/**
	 * ���õ����Ի��� �������ڣ�(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setFileChooser(UIFileChooser fileChooser) {
		m_fileChooser = fileChooser;
	}

	/**
	 * ���ô���������ϣ�� �������ڣ�(2003-9-17 13:23:55)
	 * 
	 * @param newHashParam
	 *            java.util.Hashtable
	 */
	public void setHashParam(Hashtable newHashParam) {
		m_hashParam = newHashParam;
	}

	/**
	 * ���ò�ѯ�������� �������ڣ�(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setQbd(QueryBaseDef newQbd) {
		m_qbd = newQbd;
	}

	/**
	 * ���ò�ѯ������Ϣ �������ڣ�(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setQmdInfo(QueryModelDef qmd) {
		m_originQmd = qmd;
		m_queryId = qmd.getID();
		//��һ�ཻ��
		m_scs = null;
		SimpleCrossVO[] newScs = qmd.getScs();
		int iLen = (newScs == null) ? 0 : newScs.length;
		if (iLen != 0) {
			m_scs = new SimpleCrossVO[iLen];
			for (int i = 0; i < iLen; i++)
				m_scs[i] = (SimpleCrossVO) newScs[i].clone();
		}
		//�ڶ��ཻ��
		RotateCrossVO newRc = qmd.getRotateCross();
		m_rc = (newRc == null) ? null : (RotateCrossVO) newRc.clone();
	}

	/**
	 * ���ò�ѯ�������� �������ڣ�(2003-4-2 18:34:06)
	 * 
	 * @return nc.vo.iuforeport.businessquery.QueryBaseDef
	 */
	public void setQueryDef(StorageDataSet dataSet,
			StorageDataSet dsBeforeRotate, Hashtable hashParam) {

		try {
			m_hashParam = hashParam;
			//��ʼ��
			getPnBrowse().setDataset(dataSet, getWidth());
			getPnBrowse().setDsBeforeRotate(dsBeforeRotate);
			getPnCenter().add(getPnBrowse(), "Center");
			//dataSet.close(); //���ܼ�

			//�����ͷ
			JViewport vp = getPnBrowse().getTablePnPub().getRowHeader();
			if (vp != null)
				getPnBrowse().getTablePnPub().remove(vp);

			//���ģ�Ͷ���
			QueryModelDef qmd = BIModelUtil.getQueryDef(m_queryId,
					m_defDsWrapper.getDefDsName());
			if (qmd == null) {
				return;
			}
			//���ȫ����͸������
			PenetrateRuleVO[] prs = qmd.getPenetrateRules();
			if (prs == null || prs.length == 0)
				getBnPenetrate().setEnabled(false);
			else
				getBnPenetrate().setEnabled(true);
			//����ť�����Գ�ʼ������Ϊ���Ի����Ǹ���ʵ���ģ�
			getBnCreate().setEnabled(true);
			getBnCross().setEnabled(true);
			//��ͷ����
			changeUI(m_scs, m_rc, qmd.getFldgroups(), -1, dataSet.getRowCount());
			//
			//System.out.println("[��������] " +
			// ModelUtil.getHashString(hashParam));
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}
}  