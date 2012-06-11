/*
 * 创建日期 2005-5-24
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iuforeport.businessquery.CondCellRenderer;
import nc.ui.iuforeport.businessquery.CondExpCellRenderer;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.table.ColumnGroup;
import nc.ui.pub.beans.table.GroupableTableHeader;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryModelTree;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.iuforeport.businessquery.WhereCondVO;
import nc.vo.pub.ValueObject;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 筛选条件设置界面
 */
public class SetCondPanel extends AbstractQueryDesignSetPanel implements
		KeyListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0052";//"筛选条件";

	//编辑器数组
	private UIRefCellEditor[] m_refEditors = null;

	//生成表达式对话框实例
	private FldGenDlg m_fldGenDlg = null;

	//常量
	public final static int COL_RELATION = 1;

	public final static int COL_TABLE = 2;

	public final static int COL_FLD = 3;

	public final static int COL_OPERATOR = 4;

	public final static int COL_VALUE = 5;

	public final static int COL_UNKNOWN = 6;

	private UIPanel ivjPnEast = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UIPanel ivjPnWest = null;

	private UIPanel ivjPnEast1 = null;

	private UIPanel ivjPnEast2 = null;

	private UIButton ivjBnAdd = null;

	private UIButton ivjBnDel = null;

	private UIRadioButton ivjRadioBnHand = null;

	private UIRadioButton ivjRadioBnChoose = null;

	private ButtonGroup ivjBnGroup = null;

	private UITablePane ivjTablePn1 = null;

	private UITablePane ivjTablePn2 = null;

	private UIButton ivjBnAddUnknown = null;

	private UIPanel ivjPnEast3 = null;

	private UIButton ivjBnModify = null;

	private UIPanel ivjPnEast4 = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetCondPanel.this.getBnAdd())
				connEtoC3(e);
			if (e.getSource() == SetCondPanel.this.getBnDel())
				connEtoC4(e);
			if (e.getSource() == SetCondPanel.this.getRadioBnChoose())
				connEtoC6(e);
			if (e.getSource() == SetCondPanel.this.getRadioBnHand())
				connEtoC7(e);
			if (e.getSource() == SetCondPanel.this.getBnAddUnknown())
				connEtoC1(e);
			if (e.getSource() == SetCondPanel.this.getBnModify())
				connEtoC2(e);
		};
	};

	/**
	 * SetTablePanel 构造子注解。
	 */
	public SetCondPanel() {
		super();
		initialize();
	}

	/**
	 * 停止编辑后处理 创建日期：(2001-7-31 13:02:41)
	 * 
	 * @param value
	 *            java.lang.Object
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterEdit(TableCellEditor editor, int row, int col) {
		//设置单元值
		Object value = editor.getCellEditorValue();
		getTable1().setValueAt(value, row, col);

		if (col == COL_TABLE) {
			//获得新选中的表
			FromTableVO ft = (FromTableVO) getTM1().getValueAt(row, COL_TABLE);
			//获得字段列
			TableColumn tc = getTable1().getColumnModel().getColumn(COL_FLD);
			//获得数据字典实例
			ObjectTree dd = getTabPn().getDatadict();
			//获得定义数据源
			//String dsName =
			// getQueryDefTabbedPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//查询对应表列的字段
			ObjectTree tree = (BIModelUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
					ft.getTablealias(), tree);
			//设置字段列的编辑器(数据源切换可能导致空)
			JComboBox cbbFld = (sfs == null) ? new UIComboBox() : new UIComboBox(
					sfs);
			tc.setCellEditor(new UIRefCellEditor(cbbFld));
			//设置单元值
			getTM1().setValueAt(cbbFld.getItemAt(0), row, COL_FLD);
		}
	}

	/**
	 * add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		if (isSimple()) {
			if (m_refEditors == null)
				return;
			//获得缺省控件
			JComboBox cbbTable = (JComboBox) m_refEditors[1].getComponent();
			JComboBox cbbFld = (JComboBox) m_refEditors[2].getComponent();
			//界面加行
			int iIndex = getTM1().getRowCount();
			getTM1().addRow(
					new Object[] { "", "and", cbbTable.getItemAt(0),
							cbbFld.getItemAt(0), "=", "", new Boolean(false) });
			//选中新增行
			getTable1().getSelectionModel()
					.setSelectionInterval(iIndex, iIndex);
		} else {
			//弹出对话框
			FldGenDlg dlg = getFldGenDlg();
			//清空
			dlg.doClear();
			dlg.setBnAddEnabled(false);
			dlg.setSomeInvisibled();
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				//获得生成字段
				String strWhere = dlg.getExp();
				//界面加行
				int iIndex = getTM2().getRowCount();
				getTM2()
						.addRow(
								new Object[] { "", "and", strWhere,
										new Boolean(false) });
				//选中新增行
				getTable2().getSelectionModel().setSelectionInterval(iIndex,
						iIndex);
			}
		}
	}

	/**
	 * 增加待定条件
	 */
	public void bnAddUnknown_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		if (isSimple()) {
			if (m_refEditors == null)
				return;
			//获得缺省控件
			JComboBox cbbTable = (JComboBox) m_refEditors[1].getComponent();
			JComboBox cbbFld = (JComboBox) m_refEditors[2].getComponent();
			//界面加行
			int iIndex = getTM1().getRowCount();
			getTM1().addRow(
					new Object[] { "", "and", cbbTable.getItemAt(0),
							cbbFld.getItemAt(0), "=", "", new Boolean(true) });
			//选中新增行
			getTable1().getSelectionModel()
					.setSelectionInterval(iIndex, iIndex);
		} else {
			//获得正在编辑的节点
			//			ObjectNode node = getTabPn().getQueryDefDlg()
			//					.getEditNode();
			//			QueryModelDef qmd = (QueryModelDef) node.getObject();
			QueryModelDef qmd = getTabPn().getQueryModelDef().getBaseModel();
			ParamVO[] params = qmd.getParamVOs();
			int iLen = (params == null) ? 0 : params.length;
			if (iLen == 0) {
				MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes
						.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/*
													  * @res "查询引擎"
													  */, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000865")/*
											  * @res "当前查询对象未定义参数，不能增加待定条件"
											  */);
				return;
			}
			//弹框
			UnKnownCondGenDlg dlg = new UnKnownCondGenDlg(this);
			dlg.setQueryBaseDef(getTabPn().getQueryBaseDef(), params);
			dlg.doSetInfo(new Object[] { "", "", "" });
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				String strExp = dlg.getExp();
				String strRela = dlg.getRela();
				String strVal = dlg.getVal();
				String strCondExp = strExp + QueryConst.CROSS_SEPERATOR
						+ strRela + QueryConst.CROSS_SEPERATOR + strVal;
				//界面加行
				int iIndex = getTM2().getRowCount();
				getTM2()
						.addRow(
								new Object[] { "", "and", strCondExp,
										new Boolean(true) });
				//选中新增行
				getTable2().getSelectionModel().setSelectionInterval(iIndex,
						iIndex);
			}
		}
	}

	/**
	 * del
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//界面刷新
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable1().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable1().getRowCount() != 0)
				getTable1().getSelectionModel().setSelectionInterval(0, 0);

			/*
			 * 以下为一致性处理 QueryBaseDef qbd = m_parent.getQueryBaseDef(); //合并VO数组
			 * ValueObject[] vos =
			 * QueryDefTabbedPn.delFromVOs(qbd.getSelectFlds(), iSelIndex);
			 * SelectFldVO[] newsfs = new SelectFldVO[vos.length]; for (int i =
			 * 0; i < vos.length; i++) newsfs[i] = (SelectFldVO) vos[i];
			 * qbd.setSelectFlds(newsfs);
			 */
		}
		return;
	}

	/**
	 * 修改
	 */
	public void bnModify_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable2().getCellEditor() != null)
			getTable2().getCellEditor().stopCellEditing();
		int iSelIndex = getTable2().getSelectedRow();
		if (iSelIndex != -1) {
			boolean bUnknown = ((Boolean) getTable2().getValueAt(iSelIndex, 3))
					.booleanValue();
			if (bUnknown) {
				//修改待定条件
				String strCondExp = getTable2().getValueAt(iSelIndex, 2)
						.toString();
				String[] strExpRelaVals = BIModelUtil.delimString(strCondExp,
						QueryConst.CROSS_SEPERATOR);
				String strExp = strExpRelaVals[0];
				String strRela = strExpRelaVals[1];
				String strVal = strExpRelaVals[2];
				//获得正在编辑的节点
				QueryModelDef qmd = getTabPn().getQueryModelDef().getBaseModel();
				ParamVO[] params = qmd.getParamVOs();
				//弹框
				UnKnownCondGenDlg dlg = new UnKnownCondGenDlg(this);
				dlg.setQueryBaseDef(getTabPn().getQueryBaseDef(), params);
				dlg.doSetInfo(new Object[] { strExp, strRela, strVal });
				dlg.showModal();
				dlg.destroy();
				if (dlg.getResult() == UIDialog.ID_OK) {
					strExp = dlg.getExp();
					strRela = dlg.getRela();
					strVal = dlg.getVal();
					strCondExp = strExp + QueryConst.CROSS_SEPERATOR + strRela
							+ QueryConst.CROSS_SEPERATOR + strVal;
					//界面修改
					getTM2().setValueAt(strCondExp, iSelIndex, 2);
				}
			} else {
				//修改固定条件
				String strCondExp = getTable2().getValueAt(iSelIndex, 2)
						.toString();
				//弹出对话框
				FldGenDlg dlg = getFldGenDlg();
				//清空
				dlg.doSetInfo(new Object[] { "", "", strCondExp, "" });
				dlg.setBnAddEnabled(false);
				dlg.setSomeInvisibled();
				dlg.showModal();
				dlg.destroy();
				if (dlg.getResult() == UIDialog.ID_OK) {
					//获得生成字段
					strCondExp = dlg.getExp();
					//界面修改
					getTM2().setValueAt(strCondExp, iSelIndex, 2);
				}
			}
		}
		return;
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();

		int iLen = getTM().getRowCount();
		if (iLen != 0 && isSimple()) {
			//准备参数
			ParamVO[] params = getTabPn().getQueryModelDef().getBaseModel().getParamVOs();
			//			try {
			//				params = BIModelUtil.getParams(getTabPn().getQueryBaseDef()
			//						.getID(), getTabPn().getDefDsName());
			//			} catch (Exception e) {
			//				System.out.println(e);
			//			}
			int iLenParam = (params == null) ? 0 : params.length;

			//if (iLenParam != 0) {
			//无参数也要检查
			Hashtable<String, ParamVO> hashParamKey = new Hashtable<String, ParamVO>();
			for (int i = 0; i < iLenParam; i++)
				hashParamKey.put(params[i].getParamCode(), params[i]);
			//检查待定条件右操作数
			for (int i = 0; i < iLen; i++) {
				Object objRight = getTM().getValueAt(i, COL_VALUE);
				String strRight = (objRight == null) ? "" : objRight
						.toString();
				Object objUnknown = getTM().getValueAt(i, COL_UNKNOWN);
				boolean bUnknown = ((Boolean) objUnknown).booleanValue();
				if (bUnknown) {
					if (!hashParamKey.containsKey(strRight))
						return nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000866")/*
																  * @res
																  * "待定条件的右操作数不存在于参数列表中"
																  */;
				}else{
					//如果是非待定参数，则要求条件的右值不能为空
					if( strRight.length() == 0){
						return StringResource.getStringResource("mbiquery0128");//条件右操作数不能为空
					}
				}
			}
			//}
		}
		return null;
	}

	/**
	 * connEtoC1: (BnAddUnknown.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnAddUnknown_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAddUnknown_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2: (BnModify.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnModify_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnModify_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3: (BnDel.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnDel_ActionPerformed1(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAdd_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (BnDel1.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnDel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDel_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6: (RadioBnChoose.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.radioBnChoose_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnChoose_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7: (RadioBnHand.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.radioBnHand_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC7(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnHand_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 设置待定参数名 创建日期：(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 * @param oldParam
	 *            java.lang.String
	 */
	private String fillParam(int row, int col) {
		String strParam = "";
		//获得正在编辑的节点
		//ObjectNode node = getTabPn().getQueryDefDlg().getEditNode();
		QueryModelDef qmd = getTabPn().getQueryModelDef().getBaseModel();
		ParamVO[] params = qmd.getParamVOs();
		//弹框
		ParamDefDlg dlg = new ParamDefDlg(this, getTabPn().getDefDsName());
		dlg.setParamVOs(params, qmd.getDsName());
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//存储
			params = dlg.getParamVOs();
			qmd.setParamVOs(params);
			//node.saveObject(qmd);
			//node.setObject(qmd);
			//获得选中参数
			ParamVO selParam = dlg.getSelParamVO();
			if (selParam != null) {
				strParam = selParam.getParamCode();
			}
			if (row != -1) {
				getTable1().setValueAt(strParam, row, col);
			}
		}
		return strParam;
	}

	/**
	 * 返回 BnDel 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new UIButton();
				ivjBnAdd.setName("BnAdd");
				ivjBnAdd.setPreferredSize(new Dimension(70, 22));
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000867")/* @res "固定" */);
				//ivjBnAdd.setText(StringResource.getStringResource("miufo1000080"));//增加
				ivjBnAdd.setMinimumSize(new Dimension(88, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAdd;
	}

	/**
	 * 返回 BnAddUnknown 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnAddUnknown() {
		if (ivjBnAddUnknown == null) {
			try {
				ivjBnAddUnknown = new UIButton();
				ivjBnAddUnknown.setName("BnAddUnknown");
				ivjBnAddUnknown.setPreferredSize(new Dimension(70, 22));
				ivjBnAddUnknown
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000868")/* @res "待定" */);
				ivjBnAddUnknown.setMinimumSize(new Dimension(88, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddUnknown;
	}

	/**
	 * 返回 BnDel1 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnDel() {
		if (ivjBnDel == null) {
			try {
				ivjBnDel = new UIButton();
				ivjBnDel.setName("BnDel");
				ivjBnDel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000091")/* @res "删除" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDel;
	}

	/**
	 * 返回 BnGroup 特性值。
	 * 
	 * @return javax.swing.ButtonGroup
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.ButtonGroup getBnGroup() {
		if (ivjBnGroup == null) {
			try {
				ivjBnGroup = new javax.swing.ButtonGroup();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnGroup;
	}

	/**
	 * 返回 BnDel1 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnModify() {
		if (ivjBnModify == null) {
			try {
				ivjBnModify = new UIButton();
				ivjBnModify.setName("BnModify");
				ivjBnModify
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000401")/* @res "修改" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnModify;
	}

	/**
	 * 获得生成表达式对话框实例 创建日期：(2003-4-3 9:47:03)
	 * 
	 * @return nc.ui.iuforeport.businessquery.FldGenDlg
	 */
	public FldGenDlg getFldGenDlg() {
		if (m_fldGenDlg == null) {
			m_fldGenDlg = new FldGenDlg(this);
			m_fldGenDlg.setQueryBaseDef(getTabPn().getQueryBaseDef());
		}
		return m_fldGenDlg;
	}

	/**
	 * 返回 PnEast 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnEast() {
		if (ivjPnEast == null) {
			try {
				ivjPnEast = new UIPanel();
				ivjPnEast.setName("PnEast");
				ivjPnEast.setPreferredSize(new Dimension(100, 10));
				ivjPnEast.setLayout(getPnEastGridLayout());
				ivjPnEast.setMinimumSize(new Dimension(100, 171));
				getPnEast().add(getPnEast1(), getPnEast1().getName());
				getPnEast().add(getPnEast2(), getPnEast2().getName());
				getPnEast().add(getPnEast3(), getPnEast3().getName());
				getPnEast().add(getPnEast4(), getPnEast4().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast;
	}

	/**
	 * 返回 PnEast1 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnEast1() {
		if (ivjPnEast1 == null) {
			try {
				ivjPnEast1 = new UIPanel();
				ivjPnEast1.setName("PnEast1");
				ivjPnEast1.setLayout(new UIButtonLayout());
				getPnEast1().add(getBnAdd(), getBnAdd().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast1;
	}

	/**
	 * 返回 PnEast2 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnEast2() {
		if (ivjPnEast2 == null) {
			try {
				ivjPnEast2 = new UIPanel();
				ivjPnEast2.setName("PnEast2");
				ivjPnEast2.setLayout(new UIButtonLayout());
				getPnEast2()
						.add(getBnAddUnknown(), getBnAddUnknown().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast2;
	}

	/**
	 * 返回 PnEast3 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnEast3() {
		if (ivjPnEast3 == null) {
			try {
				ivjPnEast3 = new UIPanel();
				ivjPnEast3.setName("PnEast3");
				ivjPnEast3.setLayout(new UIButtonLayout());
				getPnEast3().add(getBnModify(), getBnModify().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast3;
	}

	/**
	 * 返回 PnEast2 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnEast4() {
		if (ivjPnEast4 == null) {
			try {
				ivjPnEast4 = new UIPanel();
				ivjPnEast4.setName("PnEast4");
				ivjPnEast4.setLayout(new UIButtonLayout());
				getPnEast4().add(getBnDel(), getBnDel().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast4;
	}

	/**
	 * 返回 PnEastGridLayout 特性值。
	 * 
	 * @return GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private GridLayout getPnEastGridLayout() {
		GridLayout ivjPnEastGridLayout = null;
		try {
			/* 创建部件 */
			ivjPnEastGridLayout = new GridLayout(4, 1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnEastGridLayout;
	}

	/**
	 * 返回 PnNorth 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setLayout(getPnNorthFlowLayout());
				getPnNorth().add(getRadioBnChoose(),
						getRadioBnChoose().getName());
				getPnNorth().add(getRadioBnHand(), getRadioBnHand().getName());
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
	 * @return FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private FlowLayout getPnNorthFlowLayout() {
		FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnNorthFlowLayout = new FlowLayout();
			ivjPnNorthFlowLayout.setHgap(10);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setPreferredSize(new Dimension(10, 10));
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
	 * 返回 PnWest 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnWest() {
		if (ivjPnWest == null) {
			try {
				ivjPnWest = new UIPanel();
				ivjPnWest.setName("PnWest");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnWest;
	}

	/**
	 * 返回 RadioBnChoose 特性值。
	 * 
	 * @return UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnChoose() {
		if (ivjRadioBnChoose == null) {
			try {
				ivjRadioBnChoose = new UIRadioButton();
				ivjRadioBnChoose.setName("RadioBnChoose");
				ivjRadioBnChoose.setSelected(false);
				ivjRadioBnChoose.setPreferredSize(new Dimension(80, 22));
				ivjRadioBnChoose
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000869")/* @res "典型模式" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnChoose;
	}

	/**
	 * 返回 RadioBnHand 特性值。
	 * 
	 * @return UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnHand() {
		if (ivjRadioBnHand == null) {
			try {
				ivjRadioBnHand = new UIRadioButton();
				ivjRadioBnHand.setName("RadioBnHand");
				ivjRadioBnHand.setPreferredSize(new Dimension(80, 22));
				ivjRadioBnHand
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-000870")/* @res "高级模式" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnHand;
	}

	/**
	 * 获得where条件数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.WhereCondVO[]
	 * @i18n miufo00363=待定条件
	 * @i18n miufo00364=确定条件
	 */
	public WhereCondVO[] getResultFromCond() {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		WhereCondVO[] wcs = null;
		if (isSimple()) {
			int iRowCount = getTable1().getRowCount();
			wcs = new WhereCondVO[iRowCount];
			for (int i = 0; i < iRowCount; i++) {
				wcs[i] = new WhereCondVO();
				wcs[i].setTypeflag("C");
				wcs[i].setrelationflag(getTM1().getValueAt(i, COL_RELATION)
						.toString());
				//获得表名
				FromTableVO ft = (FromTableVO) getTM1()
						.getValueAt(i, COL_TABLE);
				String tableCode = ft.getTablecode();
				String tableAlias = ft.getTablealias();
				//获得字段名
				String fldCode = ((SelectFldVO) getTM1().getValueAt(i, COL_FLD))
						.getFldalias();
				wcs[i].setLeftfld(tableCode + " as " + tableAlias + "."
						+ fldCode);
				wcs[i].setOperator(getTM1().getValueAt(i, COL_OPERATOR)
						.toString());
				wcs[i].setRightfld(getTM1().getValueAt(i, COL_VALUE));
				//获得确定条件标志
				boolean bUnknown = ((Boolean) getTM1().getValueAt(i,
						COL_UNKNOWN)).booleanValue();
				wcs[i].setCertain(bUnknown ? StringResource.getStringResource("miufo00363") : StringResource.getStringResource("miufo00364"));//不需翻译
			}
		} else {
			int iRowCount = getTable2().getRowCount();
			wcs = new WhereCondVO[iRowCount];
			for (int i = 0; i < iRowCount; i++) {
				wcs[i] = new WhereCondVO();
				wcs[i].setTypeflag("C");
				Object relaValue = getTM2().getValueAt(i, COL_RELATION);
				wcs[i].setrelationflag(relaValue== null ?"":relaValue.toString());
				//获得确定条件标志
				boolean bUnknown = ((Boolean) getTM2().getValueAt(i, 3))
						.booleanValue();
				wcs[i].setCertain(bUnknown ? StringResource.getStringResource("miufo00363") : StringResource.getStringResource("miufo00364"));//不需翻译
				//获得表达式
				String condExp = getTM2().getValueAt(i, 2).toString();
				if (bUnknown) {
					String[] strExpOprVals = BIModelUtil.delimString(condExp,
							QueryConst.CROSS_SEPERATOR);
					wcs[i].setLeftfld(strExpOprVals[0]);
					wcs[i].setOperator(strExpOprVals[1]);
					wcs[i].setRightfld(strExpOprVals[2]);
					wcs[i].setExpression0(strExpOprVals[0] + " "
							+ strExpOprVals[1] + " " + strExpOprVals[2]);
				} else
					wcs[i].setExpression0(condExp);
			}
		}
		return wcs;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		if (isSimple())
			return getTable1();
		else
			return getTable2();
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable1() {
		return (UITable) getTablePn1().getTable();
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable2() {
		return (UITable) getTablePn2().getTable();
	}

	/**
	 * 返回 TablePn 特性值。
	 * 
	 * @return UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("serial")
	private UITablePane getTablePn1() {
		if (ivjTablePn1 == null) {
			try {
				ivjTablePn1 = new UITablePane();
				ivjTablePn1.setName("TablePn1");
				// user code begin {1}
				UITable table = new UITable() {
					public void editingStopped(ChangeEvent e) {
						int row = editingRow;
						int col = editingColumn;
						TableCellEditor editor = getCellEditor();
//						Object oldValue = getValueAt(row, col);
						//结束编辑状态
						if (editor != null) {
							//转移到afterEdit(editor, row,
							// col)中去了，因为参照没有停止编辑前的取值可能是错的。
							removeEditor();
							afterEdit(editor, row, col);

						}
					}
				};
				ivjTablePn1.setTable(table);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn1;
	}

	/**
	 * 返回 TablePn2 特性值。
	 * 
	 * @return UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	private UITablePane getTablePn2() {
		if (ivjTablePn2 == null) {
			try {
				ivjTablePn2 = new UITablePane();
				ivjTablePn2.setName("TablePn2");
				ivjTablePn2.setBounds(157, 394, 239, 92);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn2;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public DefaultTableModel getTM() {
		if (isSimple())
			return getTM1();
		else
			return getTM2();
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM1() {
		return (DefaultTableModel) getTable1().getModel();
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM2() {
		return (DefaultTableModel) getTable2().getModel();
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
		getBnAdd().addActionListener(ivjEventHandler);
		getBnDel().addActionListener(ivjEventHandler);
		getRadioBnChoose().addActionListener(ivjEventHandler);
		getRadioBnHand().addActionListener(ivjEventHandler);
		getBnAddUnknown().addActionListener(ivjEventHandler);
		getBnModify().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化单元编辑器 创建日期：(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	public void initEditorValue() {
		m_refEditors = new UIRefCellEditor[5];
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//设置关系符列编辑器
		JComboBox cbbRelation = new UIComboBox(new Object[] { "and", "or" });
		m_refEditors[0] = new UIRefCellEditor(cbbRelation);
		//设置表列编辑器
		FromTableVO[] fts = qbd.getFromTables();

		if (fts == null) {
			fts = new FromTableVO[0];
		}

		JComboBox cbbTable = new UIComboBox(fts);
		m_refEditors[1] = new UIRefCellEditor(cbbTable);
		//获得数据字典实例
		ObjectTree dd = getTabPn().getDatadict();
		//获得执行数据源
		//String dsName = getTabPn().getQueryBaseDef().getDsName();
		String dsName = getTabPn().getDefDsName();
		//设置字段列编辑器
		SelectFldVO[] sfs = null;
		if (fts.length != 0) {
			ObjectTree tree = (BIModelUtil.isTempTable(fts[0].getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			sfs = BIModelUtil.getFldsFromTable(fts[0].getTablecode(), fts[0]
					.getTablealias(), tree);
		}
		//数据源切换可能导致空
		JComboBox cbbFld = (sfs == null) ? new UIComboBox() : new UIComboBox(sfs);
		m_refEditors[2] = new UIRefCellEditor(cbbFld);
		//设置操作符列编辑器
		JComboBox cbbOperator = new UIComboBox(new Object[] { "=", ">", "<",
				"<>", ">=", "<=", "like", "in", "is", "not like", "not in" });
		m_refEditors[3] = new UIRefCellEditor(cbbOperator);

		//右操作数加热键监听
		JTextField tf = new JTextField();
		tf.addKeyListener(this);
		tf.addMouseListener(this);
		m_refEditors[4] = new UIRefCellEditor(tf);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetTablePanel");
			setLayout(new BorderLayout());
			setSize(400, 240);
			add(getPnSouth(), "South");
			add(getPnWest(), "West");
			add(getPnNorth(), "North");
			add(getPnEast(), "East");
			add(getTablePn1(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getBnGroup().add(getRadioBnChoose());
		getBnGroup().add(getRadioBnHand());
		getRadioBnChoose().setSelected(true);
		getBnModify().setEnabled(false);
		//初始化表格
		initTable1();
		initTable2();
		setTableCell();
		getTable1().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable1().setColumnWidth(new int[] { 20, 62, 96, 108, 72, 124, 60 });
		getTable2().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable2().setColumnWidth(new int[] { 20, 62, 380, 80 });
		// user code end
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable1() {
		//表模型
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				"",
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000864")/* @res "关系符" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000048")/* @res "表" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000873")/* @res "字段" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000874")/* @res "比较符" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000875")/* @res "右操作数" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000871") /* @res "待定条件" */}, 0) {
			public int getColumnCount() {
				return 7;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				if (col == COL_VALUE)
					return String.class;
				else if (col == COL_UNKNOWN)
					return Boolean.class;
				else
					return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return false;
				else if (row == 0 && col == COL_RELATION)
					return false;
				else if (col == COL_UNKNOWN)
					return false;
				else {
					//设置编辑器
					setValueEditor(row, col);
					//弹出参数定义框
					boolean bUnknown = ((Boolean) getValueAt(row,
					 COL_UNKNOWN)).booleanValue();
					if (bUnknown && col == COL_VALUE){
//						String strParam = fillParam(row, col);
					}
					return true;
				}
			}
		};
		getTable1().setModel(tm);
//		TableColumn	 tc = getTable1().getColumnModel().getColumn(COL_UNKNOWN);
//		if( tc != null ){
//			getTable1().removeColumn(tc);
//		}
		//设置表属性
		getTable1().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable1().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
		makeMultiHeader();
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable2() {
		//表模型
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				"",
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000864")/* @res "关系符" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000876")/* @res "条件表达式" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000871") /* @res "待定条件" */}, 0) {
			public int getColumnCount() {
				return 4;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				if (col == 2)
					return String.class;
				else if (col == 3)
					return Boolean.class;
				else
					return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				if (row == 0 && col == COL_RELATION)
					return false;
				else if (col == COL_RELATION) {
					setValueEditor(row, COL_RELATION);
					return true;
				} else
					return false;
			}
		};
		getTable2().setModel(tm);
//		TableColumn	 tc = getTable2().getColumnModel().getColumn(3);
//		if( tc != null ){
//			getTable2().removeColumn(tc);
//		}
		//设置表属性
		getTable2().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable2().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * 是否为典型模式 创建日期：(2003-11-13 14:35:00)
	 * 
	 * @return boolean
	 */
	public boolean isSimple() {
		return getRadioBnChoose().isSelected();
	}

	/**
	 * Invoked when a key has been pressed.
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Invoked when a key has been released.
	 */
	public void keyReleased(KeyEvent e) {
		int iSelIndex = getTable1().getSelectedRow();
		if (iSelIndex != -1) {
			//是否待定条件
			boolean bUnknown = ((Boolean) getTable1().getValueAt(iSelIndex,
					COL_UNKNOWN)).booleanValue();
			JTextField tf = (JTextField) m_refEditors[4].getComponent();
			if (e.getKeyCode() == KeyEvent.VK_F12) {
				if (bUnknown) {
					//参数录入
					String str = fillParam(-1, -1);
					tf.setText(str);
				} else {
					/*
					 * //基础参照快捷录入 QryCondRefDlg dlg = getTabPn().getRefDlg(); if
					 * (dlg != null) { dlg.showModal(); dlg.destroy(); if
					 * (dlg.getResult() == UIDialog.ID_OK) //填入参照结果
					 * tf.setText(dlg.getValue()); }
					 */
				}
			} else if (e.getKeyCode() == KeyEvent.VK_F11) {
				if (!bUnknown) {
					//环境参数录入
					String str = fillEnvParam();
					if (str != null)
						tf.setText(str);
				}
			}
		}
	}

	/**
	 * Invoked when a key has been typed. This event occurs when a key press is
	 * followed by a key release.
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * 多表头 创建日期：(2003-4-2 14:45:10)
	 */
	private void makeMultiHeader() {
		TableColumnModel cm = getTable1().getColumnModel();
		GroupableTableHeader header = (GroupableTableHeader) getTable1()
				.getTableHeader();
		ColumnGroup cg = new ColumnGroup(nc.ui.ml.NCLangRes.getInstance()
				.getStrByID("10241201", "UPP10241201-000877")/* @res "左操作数" */);
		cg.add(cm.getColumn(COL_TABLE));
		cg.add(cm.getColumn(COL_FLD));
		header.addColumnGroup(cg);
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 */
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int iSelIndex = getTable1().getSelectedRow();
			if (iSelIndex != -1) {
				//是否待定条件
				boolean bUnknown = ((Boolean) getTable1().getValueAt(iSelIndex,
						COL_UNKNOWN)).booleanValue();
				if (bUnknown) {
					JTextField tf = (JTextField) m_refEditors[4].getComponent();
					String str = fillParam(-1, -1);
					if (!str.equals(""))
						tf.setText(str);
				}
			}
		}
	}

	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * 选择录入
	 */
	public void radioBnChoose_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn2());
		add(getTablePn1(), BorderLayout.CENTER);
		getBnModify().setEnabled(false);
		validate();
		repaint();
	}

	/**
	 * 手工录入
	 */
	public void radioBnHand_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn1());
		add(getTablePn2(), BorderLayout.CENTER);
		getBnModify().setEnabled(true);
		validate();
		repaint();
	}

	/**
	 * 设置where条件数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 * @i18n miufo00363=待定条件
	 */
	public void setResultToCond(WhereCondVO[] wcs) {
		int iLen = (wcs == null) ? 0 : wcs.length;
		if (iLen == 0)
			return;
		boolean bSimple = (wcs[0].getExpression0() == null);
		if (!bSimple) {
			//更新布局
			remove(getTablePn1());
			add(getTablePn2(), BorderLayout.CENTER);
			getBnModify().setEnabled(true);
			validate();
			repaint();
			//手工录入模式
			getRadioBnHand().setSelected(true);
			for (int i = 0; i < iLen; i++) {
				//获得确定标志
				boolean bCertain = true;
				if (StringResource.getStringResource("miufo00363").equals(wcs[i].getCertain()))//无需翻译
					bCertain = false;
				//解析条件表达式
				String condExp = null;
				if (bCertain)
					condExp = wcs[i].getExpression0();
				else
					condExp = wcs[i].getLeftfld() + QueryConst.CROSS_SEPERATOR
							+ wcs[i].getOperator() + QueryConst.CROSS_SEPERATOR
							+ wcs[i].getRightfld();
				//添加
				Object[] objrows = new Object[] { "", wcs[i].getRelationflag(),
						condExp, new Boolean(!bCertain) };
				getTM2().addRow(objrows);
			}
		} else {
			//表别名-表显示名哈希表
			Hashtable hashTableId = getTabPn().getSetPanel(0).getHashTableId();
			for (int i = 0; i < iLen; i++) {
				String tablefld = wcs[i].getLeftfld();
				int iIndex = tablefld.indexOf(".");

				//转换查询字段表达式
				String table = tablefld.substring(0, iIndex);
				int iIndexAs = table.indexOf(" as ");
				String tableCode = null;
				String tableName = null;
				String tableAlias = null;
				if (iIndexAs == -1) {
					tableCode = table;
					tableAlias = table;
				} else {
					tableCode = table.substring(0, iIndexAs);
					tableAlias = table.substring(iIndexAs + 4);
				}
				//名称（同名表查询时名称有变化）
				if (hashTableId.containsKey(tableAlias))
					tableName = hashTableId.get(tableAlias).toString();
				//字段
				String fldCode = tablefld.substring(iIndex + 1);

				//获得数据字典实例
				ObjectTree dd = getTabPn().getDatadict();
				//获得执行数据源
				//String dsName =
				// getTabPn().getQueryBaseDef().getDsName();
				String dsName = getTabPn().getDefDsName();
				//获得对应的树身体
				ObjectTree tree = (BIModelUtil.isTempTable(tableCode)) ? BIQueryModelTree
						.getInstance(dsName)
						: dd;
				//获得表和字段详细信息
				ValueObject[] vos = BIModelUtil.getTableFldVO(tableCode,
						tableName, tableAlias, fldCode, tree);
				if (vos == null) {
					AppDebug.debug("vosWhere == null!");//@devTools System.out.println("vosWhere == null!");
					return;
				}
				FromTableVO ft = (FromTableVO) vos[0];
				SelectFldVO sf = (SelectFldVO) vos[1];
				//获得确定标志
				boolean bCertain = true;
				if (StringResource.getStringResource("miufo00363").equals(wcs[i].getCertain()))
					bCertain = false;

				//添加
				Object[] objrows = new Object[] { "", wcs[i].getRelationflag(),
						ft, sf, wcs[i].getOperator(), wcs[i].getRightfld(),
						new Boolean(!bCertain) };
				getTM1().addRow(objrows);
			}
		}
		//得到最后一列，删除
		
	}

	/**
	 * 设置居中对齐 创建日期：(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		DefaultTableCellRenderer renderer = null;
		for (int i = 0; i < getTable1().getColumnCount() - 1; i++) {
			TableColumn tc = getTable1().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNumCellRenderer();
			else if (i == 1)
				renderer = new CondCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}

		for (int i = 0; i < getTable2().getColumnCount() - 1; i++) {
			TableColumn tc = getTable2().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNumCellRenderer();
			else if (i == 1)
				renderer = new CondCellRenderer();
			else if (i == 2)
				renderer = new CondExpCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
	}

	/**
	 * 设置值的CellEditor 创建日期：(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	private void setValueEditor(int iRow, int iCol) {
		TableColumn tc = getTable().getColumnModel().getColumn(iCol);
		//获得定义
//		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		if (iCol != COL_FLD) {
			//设置关系符、表、操作符列编辑器
			tc.setCellEditor(m_refEditors[iCol - 1]);
		} else {
			//设置字段列编辑器
			FromTableVO ft = (FromTableVO) getTM().getValueAt(iRow, COL_TABLE);
			//获得数据字典实例
			ObjectTree dd = getTabPn().getDatadict();
			//获得执行数据源
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//查询对应表列的字段
			ObjectTree tree = (BIModelUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
					ft.getTablealias(), tree);
			//数据源切换可能导致空
			JComboBox cbbFld = (sfs == null) ? new UIComboBox() : new UIComboBox(
					sfs);
			tc.setCellEditor(new UIRefCellEditor(cbbFld));
		}
	}

	/**
	 * 设置环境参数名 创建日期：(2003-11-13 9:05:55)
	 * 
	 * @return java.lang.String
	 * @param oldParam
	 *            java.lang.String
	 */
	private String fillEnvParam() {
		String strParam = null;
		/*
		 * //获得全部环境参数（缺省+自定义接口实现） IEnvParam iEnvParam =
		 * getTabPn().getQueryDefDlg().getIEnvParam(); String[][] envParams =
		 * ModelUtil.getEnvParams(iEnvParam); //弹框 ChooseEnvParamDlg dlg = new
		 * ChooseEnvParamDlg(this); dlg.setEnvParams(envParams);
		 * dlg.showModal(); dlg.destroy(); if (dlg.getResult() ==
		 * UIDialog.ID_OK) { //获得选中参数名 strParam = dlg.getEnvParams(); }
		 */
		return strParam;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//回设
		qmd.getQueryBaseVO().setWhereConds(getResultFromCond());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setResultToCond(qmd.getQueryBaseVO().getWhereConds());
	}

	/**
	 * 刷新查询基本定义
	 */
	public void refreshQbd() {
		//获得筛选条件定义
		WhereCondVO[] wcs = getResultFromCond();
		//刷新
		getTabPn().getQueryBaseDef().setWhereConds(wcs);
	}
	private class RowNumCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * CondCellRenderer 构造子注解。
		 */
		public RowNumCellRenderer() {
			super();
		}
//		 implements javax.swing.table.TableCellRenderer
		public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {

			if (isSelected) {
				super.setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				super.setForeground(table.getForeground());
				super.setBackground(table.getBackground());
			}
			setFont(table.getFont());
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					super.setForeground(UIManager.getColor("Table.focusCellForeground"));
					super.setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(noFocusBorder);
			}

			setValue(String.valueOf(row + 1));
			return this;
		}
		}
}  