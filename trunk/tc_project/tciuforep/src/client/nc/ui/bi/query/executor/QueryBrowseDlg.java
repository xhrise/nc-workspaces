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
 * 查询预览对话框 创建日期：(2002-11-9 13:55:06)
 * 
 * @author：朱俊彬
 */
public class QueryBrowseDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//定义数据源
	protected DefDsWrapper m_defDsWrapper = null;

	//查询定义向导实例
	private QueryBrowsePanel m_pnBrowse = null;

	//查询执行数据库类型
	private String m_strDbtype = null;

	//查询定义数据库类型
	private String m_strDbtypeForDef = null;

	//所属查询ID（变）
	private String m_queryId = null;

	//穿透历史向量
	private Vector<Object[]> m_vecPeneHistory = new Vector<Object[]>();

	//投影交叉定义（变）
	private SimpleCrossVO[] m_scs = null;

	//旋转交叉定义（变）
	private RotateCrossVO m_rc = null;

	//查询基本定义（不变）
	private QueryBaseDef m_qbd = null;

	//原始查询模型定义（不变）
	private QueryModelDef m_originQmd = null;

	//待定参数哈希表
	private Hashtable m_hashParam = null;

	//导出对话框实例
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
	 * InfoDlg 构造子注解。
	 */
	public QueryBrowseDlg() {
		super();
		initialize();
	}

	/**
	 * InfoDlg 构造子注解。
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
	 * 回退
	 */
	public void bnBack_ActionPerformed(java.awt.event.ActionEvent actionEvent) {

		int iSize = m_vecPeneHistory.size();
		//if (m_scs != null || m_rc != null)
		getBnCross().setEnabled(true);
		if (iSize == 0)
			return;
		//提取穿透历史向量
		Object[] objDsId = (Object[]) m_vecPeneHistory.elementAt(iSize - 1);
		m_vecPeneHistory.removeElementAt(iSize - 1);
		//获得前一个查询ID和结果集
		StorageDataSet ds = (StorageDataSet) objDsId[0];
		m_queryId = (String) objDsId[1];
		m_scs = (SimpleCrossVO[]) objDsId[2];
		m_rc = (RotateCrossVO) objDsId[3];
		int iSelRow = ((Integer) objDsId[4]).intValue();
		int iSelCol = ((Integer) objDsId[5]).intValue();
		//设置预览表格
		getPnBrowse().setDataset(ds, getWidth());
		getPnBrowse().setSelRow(iSelRow);
		//按钮可用性调整
		getBnPenetrate().setEnabled(true);
		if (iSize == 1) {
			getBnCreate().setEnabled(true);
			getBnBack().setEnabled(false);
		}
		//行头处理
		changeUI(m_scs, m_rc, null, iSelCol, ds.getRowCount());
	}

	/**
	 * 取消
	 */
	public void bnCancel_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * 建表
	 */
	public void bnCreate_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		//获得数据集
		StorageDataSet dataSet = getPnBrowse().getDataset();
		//弹框
		CreateTableDlg dlg = new CreateTableDlg(this, m_defDsWrapper
				.getDefDsName());
		dlg.setDbtype(m_strDbtype, m_strDbtypeForDef);
		dlg.setQueryIdAndName(m_queryId, m_originQmd.getDisplayName());
		dlg.setData(dataSet);
		dlg.showModal();
		dlg.destroy();
	}

	/**
	 * 交叉
	 */
	public void bnCross_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (m_scs != null) {
			if (BIModelUtil.isMergeQuery(m_originQmd)) {
				//不支持交叉
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000080")/* @res "合并查询不支持再交叉" */);
				return;
			}
			SimpleCrossSetDlg dlg = new SimpleCrossSetDlg(this);
			dlg.setScs(m_scs);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				m_scs = dlg.getScs();
				//执行交叉
				try {
					StorageDataSet sds = BIDatasetUtil
							.getDatasetForSimpleCross(m_scs, m_qbd, m_hashParam);
					getPnBrowse().setDataset(sds, getWidth());
					//界面刷新
					procRowHeader(sds.getRowCount());
				} catch (Exception e) {
					AppDebug.debug(e);
				}
				getBnCreate().setEnabled(false);
			}
		} else {
			//if (m_rc != null) {
			//获得原始结果集和原始列信息
			StorageDataSet dsBeforeRotate = getPnBrowse().getDsBeforeRotate();
			if (dsBeforeRotate == null) {
				dsBeforeRotate = getPnBrowse().getDataset();
				getPnBrowse().setDsBeforeRotate(dsBeforeRotate);
			}
			SelectFldVO[] sfs = BIDatasetUtil
					.getSelectFldByDataset(dsBeforeRotate.getColumns());
			//弹框
			RotateCrossSetDlg dlg = new RotateCrossSetDlg(this);
			dlg.setRotateCross(m_rc, sfs);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				m_rc = dlg.getRotateCross();
				//执行交叉
				try {
					Object[] objs = BIDatasetUtil.getDatasetForRotateCross(
							m_rc, dsBeforeRotate);
					StorageDataSet sds = (StorageDataSet) objs[0];
					//篡改列名
					int iRowCount = (m_rc.getStrRows() == null) ? 0 : m_rc
							.getStrRows().length;
					BIDatasetUtil.tamperColname(sds, iRowCount);
					//
					getPnBrowse().setDataset(sds, getWidth());
					//获得多表头
					FldgroupVO[] fldgroups = (FldgroupVO[]) objs[1];
					//旋转交叉列头处理
					procColHeader(getPnBrowse().getTable(), fldgroups);
					getBnCreate().setEnabled(false);
				} catch (Exception e) {
					AppDebug.debug(e);
				}
			}
		}
	}

	/**
	 * 导出数据
	 */
	public void bnExportData_ActionPerformed(
			java.awt.event.ActionEvent actionEvent) {

		doExport();
	}

	/**
	 * 仪表盘
	 */
	public void bnMeter_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	}

	/**
	 * 穿透
	 * @i18n miufo00373=多值投影：
	 */
	public void bnPenetrate_ActionPerformed(
			java.awt.event.ActionEvent actionEvent) {

		try {
			//获得选中穿透行的哈希表
			Hashtable hashPeneRow = getPnBrowse().getPeneRow();
			if (hashPeneRow == null)
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "查询引擎" */,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000821")/* @res "未选择穿透行" */);
			else {
				//获得模型定义
				QueryModelDef qmd = BIModelUtil.getQueryDef(m_queryId,
						m_defDsWrapper.getDefDsName());
				//获得全部穿透规则定义
				PenetrateRuleVO[] prs = qmd.getPenetrateRules();
				//选择穿透规则定义
				PeneRuleDlg dlg = new PeneRuleDlg(this,m_defDsWrapper
						.getDefDsName(), false);
				dlg.setPenetrateRules(prs);
				dlg.showModal();
				dlg.destroy();
				if (dlg.getResult() == UIDialog.ID_OK) {
					int iSelRow = dlg.getSelRow();
					if (iSelRow == -1)
						return;
					//获得穿透规则定义
					PenetrateRuleVO pr = prs[iSelRow];
					//按钮可用性调整
					getBnCreate().setEnabled(false);
					getBnCross().setEnabled(false);
					getBnBack().setEnabled(true);
					//加入穿透历史向量
					iSelRow = getPnBrowse().getSelRow();
					int iSelCol = getPnBrowse().getSelCol();
					Object[] objDsId = new Object[] {
							getPnBrowse().getDataset(), m_queryId, m_scs, m_rc,
							new Integer(iSelRow), new Integer(iSelCol) };
					m_vecPeneHistory.addElement(objDsId);
					//获得选中单元的投影交叉行列VO
					SimpleCrossVO[] scs = null;
					if (m_scs != null) {
						//多值投影特殊处理
						SelectFldVO[] sfs = qmd.getQueryBaseVO()
								.getSelectFlds();
						int iSfLen = (sfs == null) ? 0 : sfs.length;
						if (iSfLen != 1) {
							System.out
									.println(StringResource
											.getStringResource("miufo00373") + iSelCol + "/" + iSfLen);
							iSelCol = iSelCol / iSfLen;
						}
						//构造传到后台的行列VO
						scs = BIModelUtil.getRowColScs(m_scs, iSelRow, iSelCol);
					}
					//进度
					ProgressDlg dlgProg = new ProgressDlg(this);
					dlgProg
							.setTitle(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("10241201",
											"UPP10241201-000822")/* @res "数据穿透" */);
					dlgProg.setHint(nc.ui.ml.NCLangRes.getInstance()
							.getStrByID("10241201", "UPP10241201-000823")/*
																		  * @res
																		  * "正在执行数据穿透
																		  * ..."
																		  */);
					//启动进度线程
					//				ThreadProgRun thProgRun = new ThreadProgRun();
					//				thProgRun.setDsName(m_defDsWrapper.getDefDsName());
					//				thProgRun.setDlg(dlgProg);
					//				thProgRun.setType(4);
					//				thProgRun.setPeneInfo(pr, m_hashParam, hashPeneRow, scs);
					//				thProgRun.start();
					//显示框
					dlgProg.setDsName(m_defDsWrapper.getDefDsName());
					dlgProg.setType(4);
					dlgProg.setPeneInfo(pr, m_hashParam, hashPeneRow, scs);
					int result = dlgProg.startProgress();
					//				dlgProg.destroy();
					if (result == UIDialog.ID_OK) {
						//获得穿透结果
						QEDataSetAfterProcessDescriptor dsdwc = dlgProg
								.getQEDataSetDescriptor();
						//DataSetDataWithColumn dsdwc =
						// PenetrateRuleUtilBO_Client.penetrate(pr, m_hashParam,
						// hashPeneRow, scs, ModelUtil.getLoginEnv());
						//获得新查询ID和结果集
						m_queryId = dsdwc.getQmdID();
						StorageDataSet ds = makeDataSet(dsdwc);
						//设置预览表格
						getPnBrowse().setDataset(ds, getWidth());
						//获得模型定义
						qmd = BIModelUtil.getQueryDef(m_queryId, m_defDsWrapper
								.getDefDsName());
						//获得全部穿透规则定义
						prs = qmd.getPenetrateRules();
						if (prs == null || prs.length == 0)
							getBnPenetrate().setEnabled(false);
						//行头处理
						changeUI(qmd.getScs(), qmd.getRotateCross(), qmd
								.getFldgroups(), iSelCol, ds.getRowCount());
						//
						m_scs = qmd.getScs();
						m_rc = qmd.getRotateCross();
						//System.out.println("[参数条件] " +
						// ModelUtil.getHashString(dsdwc.getHashParam()));
					}
					dlgProg.clear();
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000824")/* @res "穿透失败" */);
		}
	}

	/**
	 * 有无行头的界面处理 创建日期：(2003-11-19 13:04:17)
	 * 
	 * @param scs
	 *            nc.vo.pub.querymodel.SimpleCrossVO
	 */
	private void changeUI(SimpleCrossVO[] scs, RotateCrossVO rc,
			FldgroupVO[] fldgroups, int iSelCol, int iRowCount) {

		if (scs == null) {
			getPnBrowse().getTable().setColumnSelectionAllowed(false);
			//清空行头
			JViewport vp = getPnBrowse().getTablePnPub().getRowHeader();
			if (vp != null)
				getPnBrowse().getTablePnPub().remove(vp);
			//旋转交叉列头处理
			procColHeader(getPnBrowse().getTable(), fldgroups);
			//按钮可用性
			//getBnCross().setEnabled(rc != null);
		} else {
			if (iSelCol != -1)
				getPnBrowse().getTable().setColumnSelectionInterval(iSelCol,
						iSelCol);
			procRowHeader(iRowCount);
			//按钮可用性
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	/* 警告：此方法将重新生成。 */
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
	 * 数据导出 创建日期：(2004-5-14 8:43:41)
	 */
	protected void doExport() {
		doExportTxt(true);
	}

	/**
	 * 数据导出为文本 创建日期：(2004-5-14 8:43:41)
	 */
	private void doExportTxt(boolean bChangeScale) {
		//导出
		try {
			TextDataFile tdf = new TextDataFile();
			tdf.setDelimiter("'");
			//获得导出文件路径
			String path = getExportPath(false);
			if (path.equals(""))
				return;
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					path));
			DataSet dataSet = getPnBrowse().getDataset();
			//写列名
			int colCount = dataSet.getColumnCount();
			for (int i = 0; i < colCount; i++) {
				//篡改ORACLE刻度
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
			//写数据
			tdf.save(dataSet, out, null);
			out.flush();
			out.close();
			//tdf.setFileName(path);
			//tdf.save(getPnBrowse().getDataset());
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000825")/* @res "导出完成" */);
		} catch (Exception e) {
			AppDebug.debug(e);
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000826")/* @res "导出失败" */);
		}
	}

	/**
	 * 返回 BnBack 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnBack() {
		if (ivjBnBack == null) {
			try {
				ivjBnBack = new nc.ui.pub.beans.UIButton();
				ivjBnBack.setName("BnBack");
				ivjBnBack.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000071")/* @res "回退" */);
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
	 * 返回 BnCancel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000405")/* @res "关闭" */);
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
	 * 返回 BnOK 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnCreate() {
		if (ivjBnCreate == null) {
			try {
				ivjBnCreate = new nc.ui.pub.beans.UIButton();
				ivjBnCreate.setName("BnCreate");
				ivjBnCreate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000827")/* @res "建表" */);
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
	 * 返回 BnCross 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnCross() {
		if (ivjBnCross == null) {
			try {
				ivjBnCross = new nc.ui.pub.beans.UIButton();
				ivjBnCross.setName("BnCross");
				ivjBnCross.setToolTipText(nc.ui.ml.NCLangRes.getInstance()
						.getStrByID("10241201", "UPP10241201-000078")/*
																	  * @res
																	  * "简单交叉"
																	  */);
				ivjBnCross.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000070")/* @res "交叉" */);
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
	 * 返回 BnExportData 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnExportData() {
		if (ivjBnExportData == null) {
			try {
				ivjBnExportData = new nc.ui.pub.beans.UIButton();
				ivjBnExportData.setName("BnExportData");
				ivjBnExportData
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000107")/* @res "导出" */);
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
	 * 返回 BnPenetrate 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnPenetrate() {
		if (ivjBnPenetrate == null) {
			try {
				ivjBnPenetrate = new nc.ui.pub.beans.UIButton();
				ivjBnPenetrate.setName("BnPenetrate");
				ivjBnPenetrate
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000060")/* @res "穿透" */);
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
	 * 获得导出路径 创建日期：(2003-10-29 15:45:31)
	 * 
	 * @return java.lang.String
	 */
	private String getExportPath(boolean bImport) {
		if (m_fileChooser == null)
			return "";
		//选择导出路径
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
																  * @res "导出"
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
																	  * "确认文件覆盖"
																	  */, nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-000829", null,
									new String[] { strFullName })/*
																  * @res
																  * "文件\"{0}\"已经存在，确定要覆盖该文件吗？"
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
//	 * 获得原始结果集 创建日期：(2003-11-28 10:29:09)
//	 * 
//	 * @return com.borland.dx.dataset.StorageDataSet
//	 */
//	private DataSet getOriginDataset() {
//		int iSize = m_vecPeneHistory.size();
//		if (iSize == 0)
//			return getPnBrowse().getDataset();
//		//提取穿透历史向量
//		Object[] objDsId = (Object[]) m_vecPeneHistory.elementAt(iSize - 1);
//		return null;
//	}

	/**
	 * 获得查询浏览面板实例 创建日期：(2003-4-3 17:00:54)
	 * 
	 * @return nc.ui.iuforeport.businessquery.QueryDefTabbedPn
	 */
	public QueryBrowsePanel getPnBrowse() {
		if (m_pnBrowse == null)
			m_pnBrowse = new QueryBrowsePanel();
		return m_pnBrowse;
	}

	/**
	 * 返回 PnCenter 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 PnNorth 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 PnNorthFlowLayout 特性值。
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.FlowLayout getPnNorthFlowLayout() {
		java.awt.FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnNorthFlowLayout = new java.awt.FlowLayout();
			ivjPnNorthFlowLayout.setVgap(8);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 返回 PnSouthFlowLayout 特性值。
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.FlowLayout getPnSouthFlowLayout() {
		java.awt.FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* 创建部件 */
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
	 * 获得行头表模型 创建日期：(2003-11-1 14:30:52)
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
	 * 初始化连接
	 * 
	 * @exception java.lang.Exception
	 *                异常说明。
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("InfoDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000830")/* @res "查询结果预览" */);
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
	 * 根据结果集数据获得结果集 创建日期：(2003-10-8 12:56:58)
	 * 
	 * @return com.borland.dx.dataset.StorageDataSet
	 * @param dsdwc
	 *            nc.vo.pub.querymodel.DataSetDataWithColumn
	 */
	private StorageDataSet makeDataSet(QEDataSetAfterProcessDescriptor dsdwc) {
		StorageDataSet dataSet = new StorageDataSet();
		dsdwc.getDataSetData().loadDataSet(dataSet);
		//添加列名
		for (int i = 0; i < dataSet.getColumnCount(); i++)
			dataSet.getColumn(i).setCaption(dsdwc.getCaptions()[i]);
		return dataSet;
	}

	/**
	 * 鼠标点击事件
	 */
	public void pnSouth_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 2) {
			//按钮可见
			if (mouseEvent.isControlDown())
				doExportTxt(false);
			else if (mouseEvent.isAltDown())
				; //getBnCreate().setVisible(!getBnCreate().isVisible());
			else
				setButtonVisible(!getBnCross().isVisible());
		}
	}

	/**
	 * 为表格生成多表头 创建日期：(2003-11-26 14:22:45)
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
			//列处理
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
	 * 行头处理 创建日期：(2003-11-1 14:26:39)
	 */
	private void procRowHeader(int iRowCount) {

		//设置行选取模式
		getPnBrowse().getTable().setColumnSelectionAllowed(true);
		getPnBrowse().getTable().getColumnModel().getSelectionModel()
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//设置行头表
		String[] rowHeaders = BIModelUtil.getRowHeaders(m_scs);
		Object[][] data = new Object[iRowCount][];
		int[] iRows = new int[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			data[i] = new Object[] { rowHeaders[i] };
			iRows[i] = i;
		}

		//表模型
		AttributiveCellTableModel fixModel = getTMRowHeader(data,
				new Object[] { "" });
		//修饰
		UISpanTable fixTable = new UISpanTable(fixModel);
		fixTable.setAutoResizeMode(UITable.AUTO_RESIZE_OFF);
		fixTable.setColumnWidth(new int[] { 100 });
		fixTable.setBackground(QueryConst.HEADER_BACK_COLOR);
		fixTable.setForeground(QueryConst.HEADER_FORE_COLOR);
		//对齐
		TableColumn tc = fixTable.getColumnModel().getColumn(0);
		RowHeaderRenderer renderer = new RowHeaderRenderer(fixTable);
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		tc.setCellRenderer(renderer);

		//添加行头表
		UIViewport viewport = new UIViewport();
		viewport.setView(fixTable);
		viewport.setPreferredSize(fixTable.getPreferredSize());
		//加入控件
		getPnBrowse().getTablePnPub().setRowHeader(viewport);

		/*
		 * //单元合并属性 CellSpan cellAtt = (CellSpan) fixModel.getCellAttribute();
		 * cellAtt.combine(iRows, new int[] { 0 }); //列可选 UITable table =
		 * getPnBrowse().getTable(); table.setColumnSelectionAllowed(true); //列头
		 * ColumnGroup cg = new ColumnGroup("列"); for (int i = 0; i <
		 * table.getColumnCount(); i++)
		 * cg.add(table.getColumnModel().getColumn(i)); GroupableTableHeader
		 * header = (GroupableTableHeader) table.getTableHeader();
		 * header.addColumnGroup(cg); header.setUpdateTableInRealTime(true);
		 * //填补左上角CORNER Object[] title = new Object[] { "" }; JList fillList =
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
//	 * 选中下一行 创建日期：(2003-9-26 16:38:28)
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
	 * 设置按钮可见性（FOR BPM） 创建日期：(2003-9-11 13:50:40)
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
	 * 设置查询执行和查询定义数据库类型 创建日期：(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setDbtype(String newDbtype, String newDbtypeForDef) {
		m_strDbtype = newDbtype;
		m_strDbtypeForDef = newDbtypeForDef;
	}

	/**
	 * 设置导出对话框 创建日期：(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setFileChooser(UIFileChooser fileChooser) {
		m_fileChooser = fileChooser;
	}

	/**
	 * 设置待定参数哈希表 创建日期：(2003-9-17 13:23:55)
	 * 
	 * @param newHashParam
	 *            java.util.Hashtable
	 */
	public void setHashParam(Hashtable newHashParam) {
		m_hashParam = newHashParam;
	}

	/**
	 * 设置查询基本定义 创建日期：(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setQbd(QueryBaseDef newQbd) {
		m_qbd = newQbd;
	}

	/**
	 * 设置查询定义信息 创建日期：(2003-9-11 13:50:40)
	 * 
	 * @param newM_hashDsDbtype
	 *            java.util.Hashtable
	 */
	public void setQmdInfo(QueryModelDef qmd) {
		m_originQmd = qmd;
		m_queryId = qmd.getID();
		//第一类交叉
		m_scs = null;
		SimpleCrossVO[] newScs = qmd.getScs();
		int iLen = (newScs == null) ? 0 : newScs.length;
		if (iLen != 0) {
			m_scs = new SimpleCrossVO[iLen];
			for (int i = 0; i < iLen; i++)
				m_scs[i] = (SimpleCrossVO) newScs[i].clone();
		}
		//第二类交叉
		RotateCrossVO newRc = qmd.getRotateCross();
		m_rc = (newRc == null) ? null : (RotateCrossVO) newRc.clone();
	}

	/**
	 * 设置查询基本定义 创建日期：(2003-4-2 18:34:06)
	 * 
	 * @return nc.vo.iuforeport.businessquery.QueryBaseDef
	 */
	public void setQueryDef(StorageDataSet dataSet,
			StorageDataSet dsBeforeRotate, Hashtable hashParam) {

		try {
			m_hashParam = hashParam;
			//初始化
			getPnBrowse().setDataset(dataSet, getWidth());
			getPnBrowse().setDsBeforeRotate(dsBeforeRotate);
			getPnCenter().add(getPnBrowse(), "Center");
			//dataSet.close(); //不能加

			//清空行头
			JViewport vp = getPnBrowse().getTablePnPub().getRowHeader();
			if (vp != null)
				getPnBrowse().getTablePnPub().remove(vp);

			//获得模型定义
			QueryModelDef qmd = BIModelUtil.getQueryDef(m_queryId,
					m_defDsWrapper.getDefDsName());
			if (qmd == null) {
				return;
			}
			//获得全部穿透规则定义
			PenetrateRuleVO[] prs = qmd.getPenetrateRules();
			if (prs == null || prs.length == 0)
				getBnPenetrate().setEnabled(false);
			else
				getBnPenetrate().setEnabled(true);
			//建表按钮可用性初始化（因为本对话框是复用实例的）
			getBnCreate().setEnabled(true);
			getBnCross().setEnabled(true);
			//行头处理
			changeUI(m_scs, m_rc, qmd.getFldgroups(), -1, dataSet.getRowCount());
			//
			//System.out.println("[参数条件] " +
			// ModelUtil.getHashString(hashParam));
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}
}  