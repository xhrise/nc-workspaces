/*
 * 创建日期 2005-5-24
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.ml.NCLangRes;
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
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryModelTree;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.JoinCondVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.ValueObject;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zjb
 * 
 * 连接条件设置界面
 */
public class SetJoinPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0055";//"连接条件";

	//编辑器数组
	private UIRefCellEditor[] m_refEditors = null;

	//生成表达式对话框实例
	private FldGenDlg m_fldGenDlg = null;

	//常量
	public final static int COL_LEFTTABLE = 1;

	public final static int COL_JOINMODE = 2;

	public final static int COL_RIGHTTABLE = 3;

	public final static int COL_LEFTFLD = 4;

	public final static int COL_OPERATOR = 5;

	public final static int COL_RIGHTFLD = 6;

	public final static int COL_EXPRESSION = 4;

	private UIPanel ivjPnEast = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UIPanel ivjPnWest = null;

	private UIButton ivjBnDel = null;

	private UIPanel ivjPnEast1 = null;

	private UIPanel ivjPnEast2 = null;

	private UIButton ivjBnAdd = null;

	private ButtonGroup ivjBnGroup = null;

	private UIRadioButton ivjRadioBnChoose = null;

	private UIRadioButton ivjRadioBnHand = null;

	private UIButton ivjBnDown = null;

	private UIButton ivjBnUp = null;

	private UIPanel ivjPnEast3 = null;

	private UIPanel ivjPnEast4 = null;

	private UIButton ivjBnTurn = null;

	private UIPanel ivjPnEast5 = null;

	private UITablePane ivjTablePn1 = null;

	private UITablePane ivjTablePn2 = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetJoinPanel.this.getBnAdd())
				connEtoC1(e);
			if (e.getSource() == SetJoinPanel.this.getBnDel())
				connEtoC2(e);
			if (e.getSource() == SetJoinPanel.this.getRadioBnChoose())
				connEtoC3(e);
			if (e.getSource() == SetJoinPanel.this.getRadioBnHand())
				connEtoC4(e);
			if (e.getSource() == SetJoinPanel.this.getBnUp())
				connEtoC6(e);
			if (e.getSource() == SetJoinPanel.this.getBnDown())
				connEtoC7(e);
			if (e.getSource() == SetJoinPanel.this.getBnTurn())
				connEtoC8(e);
		};
	};

	/**
	 * SetTablePanel 构造子注解。
	 */
	public SetJoinPanel() {
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
		getTable().setValueAt(value, row, col);

		if (isSimple()) {
			//获得数据字典实例
			ObjectTree dd = getTabPn().getDatadict();
			if (col == COL_LEFTTABLE) {
				//获得新选中的表1
				FromTableVO ft = (FromTableVO) getTM().getValueAt(row,
						COL_LEFTTABLE);
				//获得字段1列
				TableColumn tc = getTable().getColumnModel().getColumn(
						COL_LEFTFLD);
				//获得定义数据源
				//String dsName =
				// getQueryDefTabbedPn().getQueryBaseDef().getDsName();
				String dsName = getTabPn().getDefDsName();
				//查询对应表1列的字段
				ObjectTree tree = (QueryUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
						.getInstance(dsName)
						: dd;
				SelectFldVO[] sfs = QueryUtil.getFldsFromTable(ft
						.getTablecode(), ft.getTablealias(), tree);
				//设置字段列1的编辑器(数据源切换可能导致空)
				JComboBox cbbFld = (sfs == null) ? new UIComboBox()
						: new UIComboBox(sfs);
				tc.setCellEditor(new UIRefCellEditor(cbbFld));
				//设置单元值
				getTM().setValueAt(cbbFld.getItemAt(0), row, COL_LEFTFLD);
			} else if (col == COL_RIGHTTABLE) {
				//获得新选中的表2
				FromTableVO ft = (FromTableVO) getTM().getValueAt(row,
						COL_RIGHTTABLE);
				//获得字段2列
				TableColumn tc = getTable().getColumnModel().getColumn(
						COL_RIGHTFLD);
				//获得执行数据源
				//String dsName =
				// getTabPn().getQueryBaseDef().getDsName();
				String dsName = getTabPn().getDefDsName();
				//查询对应表2列的字段
				ObjectTree tree = (QueryUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
						.getInstance(dsName)
						: dd;
				SelectFldVO[] sfs = QueryUtil.getFldsFromTable(ft
						.getTablecode(), ft.getTablealias(), tree);
				//设置字段列2的编辑器(数据源切换可能导致空)
				JComboBox cbbFld = (sfs == null) ? new UIComboBox()
						: new UIComboBox(sfs);
				tc.setCellEditor(new UIRefCellEditor(cbbFld));
				//设置单元值
				getTM().setValueAt(cbbFld.getItemAt(0), row, COL_RIGHTFLD);
			}
		}
	}

	/**
	 * add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//获得行数
		int iRowCount = getTable().getRowCount();
		FromTableVO[] fts = getTabPn().getQueryBaseDef().getFromTables();
		int iLen = (fts == null) ? 0 : fts.length;
		if (iRowCount == 0 && iLen < 2) {
			MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000099")/*
																  * @res "查询引擎"
																  */, NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000905")/*
										  * @res "当前查询表不足两个，无法定义连接条件"
										  */);
			return;
		}
		//获得缺省值
		JComboBox cbbTable1 = (JComboBox) m_refEditors[0].getComponent();
		JComboBox cbbTable2 = (JComboBox) m_refEditors[2].getComponent();
		if (isSimple()) {
			JComboBox cbbFld1 = (JComboBox) m_refEditors[3].getComponent();
			JComboBox cbbFld2 = (JComboBox) m_refEditors[5].getComponent();
			if (iRowCount == 0) {
				//界面加行
				getTM()
						.addRow(
								new Object[] {
										"",
										cbbTable1.getItemAt(0),
										NCLangRes.getInstance().getStrByID(
												"10241201",
												"UPP10241201-000863")/*
																	  * @res
																	  * "内连接"
																	  */, cbbTable2.getItemAt(0),
										cbbFld1.getItemAt(0), "=",
										cbbFld2.getItemAt(0) });
				//设置编辑器下几行会做强制转换
				setValueEditor(0, COL_RIGHTTABLE);
				setValueEditor(0, COL_RIGHTFLD);
			} else {
				//界面加行
				getTM()
						.addRow(
								new Object[] {
										"",
										getTM().getValueAt(iRowCount - 1,
												COL_RIGHTTABLE),
										NCLangRes.getInstance().getStrByID(
												"10241201",
												"UPP10241201-000863")/*
																	  * @res
																	  * "内连接"
																	  */,
										cbbTable2.getItemAt(0),
										getTM().getValueAt(iRowCount - 1,
												COL_RIGHTFLD), "=",
										cbbFld2.getItemAt(0) });
			}
		} else {
			if (iRowCount == 0) {
				//界面加行
				getTM().addRow(
						new Object[] {
								"",
								cbbTable1.getItemAt(0),
								NCLangRes.getInstance().getStrByID("10241201",
										"UPP10241201-000863")/*
															  * @res "内连接"
															  */, cbbTable2.getItemAt(0), "" });
			} else {
				//界面加行
				getTM().addRow(
						new Object[] {
								"",
								getTM().getValueAt(iRowCount - 1,
										COL_RIGHTTABLE),
								NCLangRes.getInstance().getStrByID("10241201",
										"UPP10241201-000863")/*
															  * @res "内连接"
															  */, cbbTable2.getItemAt(0), "" });
			}
		}
		//选中新增行
		int iIndex = getTM().getRowCount();
		getTable().getSelectionModel().setSelectionInterval(iIndex - 1,
				iIndex - 1);
		return;
	}

	/**
	 * del
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iRowCount = getTable().getRowCount();
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1 && iSelIndex < iRowCount) {
			//界面刷新
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (iRowCount != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
		return;
	}

	/**
	 * down
	 */
	public void bnDown_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//
		int iSelIndex = getTable().getSelectedRow();
		getTable().scrollRectToVisible(
				getTable().getCellRect(iSelIndex, 0, false));
		if (iSelIndex != -1 && iSelIndex < getTable().getRowCount() - 1) {
			//移行
			getTM().moveRow(iSelIndex, iSelIndex, iSelIndex + 1);
			//选中
			getTable().getSelectionModel().setSelectionInterval(iSelIndex + 1,
					iSelIndex + 1);

			/* 以下为一致性处理 */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//合并VO数组
			JoinCondVO[] oldJcs = qbd.getJoinConds();
			JoinCondVO[] newJcs = new JoinCondVO[oldJcs.length];
			for (int i = 0; i < oldJcs.length; i++)
				if (i == iSelIndex)
					newJcs[i] = oldJcs[iSelIndex + 1];
				else if (i == iSelIndex + 1)
					newJcs[i] = oldJcs[iSelIndex];
				else
					newJcs[i] = oldJcs[i];
			qbd.setJoinConds(newJcs);
		}
	}

	/**
	 * up
	 */
	public void bnTurn_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			if (isSimple())
				doTurn(iSelIndex);
			else
				doJoin(iSelIndex);
		}
	}

	/**
	 * up
	 */
	public void bnUp_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//
		int iSelIndex = getTable().getSelectedRow();
		getTable().scrollRectToVisible(
				getTable().getCellRect(iSelIndex, 0, false));
		if (iSelIndex > 0) {
			//移行
			getTM().moveRow(iSelIndex, iSelIndex, iSelIndex - 1);
			//选中
			getTable().getSelectionModel().setSelectionInterval(iSelIndex - 1,
					iSelIndex - 1);

			/* 以下为一致性处理 */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//合并VO数组
			JoinCondVO[] oldJcs = qbd.getJoinConds();
			JoinCondVO[] newJcs = new JoinCondVO[oldJcs.length];
			for (int i = 0; i < oldJcs.length; i++)
				if (i == iSelIndex - 1)
					newJcs[i] = oldJcs[iSelIndex];
				else if (i == iSelIndex)
					newJcs[i] = oldJcs[iSelIndex - 1];
				else
					newJcs[i] = oldJcs[i];
			qbd.setJoinConds(newJcs);
		}
	}

	/**
	 * connEtoC1: (BnAdd.action.actionPerformed(ActionEvent) -->
	 * SetJoinPanel.bnAdd_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
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
	 * connEtoC2: (BnDown.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnDown_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(ActionEvent arg1) {
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
	 * connEtoC3: (RadioBnChoose.action.actionPerformed(ActionEvent) -->
	 * SetJoinPanel.radioBnChoose_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
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
	 * connEtoC4: (RadioBnHand.action.actionPerformed(ActionEvent) -->
	 * SetJoinPanel.radioBnHand_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(ActionEvent arg1) {
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
	 * connEtoC6: (BnUp.action.actionPerformed(ActionEvent) -->
	 * SetJoinPanel.bnUp_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnUp_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7: (BnDown.action.actionPerformed(ActionEvent) -->
	 * SetJoinPanel.bnDown_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC7(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnDown_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC8: (BnTurn.action.actionPerformed(ActionEvent) -->
	 * SetJoinPanel.bnTurn_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC8(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnTurn_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 手工录入
	 */
	public void doJoin(int iSelIndex) {

		String oldExp = getTable2().getValueAt(iSelIndex, COL_EXPRESSION)
				.toString();
		//弹出对话框
		FldGenDlg dlg = getFldGenDlg();
		dlg.doSetInfo(new Object[] { null, null, oldExp, null });
		dlg.setBnAddEnabled(false);
		dlg.setSomeInvisibled();
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//终止编辑态
			if (getTable2().getCellEditor() != null)
				getTable2().getCellEditor().stopCellEditing();
			getTable2().setValueAt(dlg.getExp(), iSelIndex, COL_EXPRESSION);
		}
		return;
	}

	/**
	 * 互换 创建日期：(2003-11-13 15:53:55)
	 * 
	 * @param iSelIndex
	 *            int
	 */
	private void doTurn(int iSelIndex) {

//		/* 以下为一致性处理 */
//		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
//		//合并VO数组
//		JoinCondVO[] oldJcs = qbd.getJoinConds();
//		String leftTable = oldJcs[iSelIndex].getLefttable();
//		String leftFld = oldJcs[iSelIndex].getLeftfld();
//		oldJcs[iSelIndex].setLefttable(oldJcs[iSelIndex].getRighttable());
//		oldJcs[iSelIndex]
//				.setLeftfld(oldJcs[iSelIndex].getRightfld().toString());
//		oldJcs[iSelIndex].setRighttable(leftTable);
//		oldJcs[iSelIndex].setRightfld(leftFld);

		//重置
		Object[] objrows = new Object[] { "",
				getTM().getValueAt(iSelIndex, COL_RIGHTTABLE),
				getTM().getValueAt(iSelIndex, COL_JOINMODE),
				getTM().getValueAt(iSelIndex, COL_LEFTTABLE),
				getTM().getValueAt(iSelIndex, COL_RIGHTFLD),
				getTM().getValueAt(iSelIndex, COL_OPERATOR),
				getTM().getValueAt(iSelIndex, COL_LEFTFLD) };
		getTM().removeRow(iSelIndex);
		getTM().insertRow(iSelIndex, objrows);
		//setResultToJoin(oldJcs);
		//选中
		getTable().getSelectionModel().setSelectionInterval(iSelIndex,
				iSelIndex);
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
				ivjBnAdd.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000330")/* @res "增加" */);
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
	 * 返回 BnDel 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnDel() {
		if (ivjBnDel == null) {
			try {
				ivjBnDel = new UIButton();
				ivjBnDel.setName("BnDel");
				ivjBnDel.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000091")/* @res "删除" */);
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
	 * 返回 BnDown 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnDown() {
		if (ivjBnDown == null) {
			try {
				ivjBnDown = new UIButton();
				ivjBnDown.setName("BnDown");
				ivjBnDown.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000146")/* @res "下移" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnDown;
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
	 * 返回 BnTurn 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnTurn() {
		if (ivjBnTurn == null) {
			try {
				ivjBnTurn = new UIButton();
				ivjBnTurn.setName("BnTurn");
				ivjBnTurn.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000906")/* @res "互换" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnTurn;
	}

	/**
	 * 返回 BnUp 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnUp() {
		if (ivjBnUp == null) {
			try {
				ivjBnUp = new UIButton();
				ivjBnUp.setName("BnUp");
				ivjBnUp.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000145")/* @res "上移" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnUp;
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
				getPnEast().add(getPnEast1(), getPnEast1().getName());
				getPnEast().add(getPnEast2(), getPnEast2().getName());
				getPnEast().add(getPnEast3(), getPnEast3().getName());
				getPnEast().add(getPnEast4(), getPnEast4().getName());
				getPnEast().add(getPnEast5(), getPnEast5().getName());
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
				getPnEast2().add(getBnDel(), getBnDel().getName());
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
	 * 返回 PnEast11 特性值。
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
				getPnEast3().add(getBnUp(), getBnUp().getName());
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
	 * 返回 PnEast21 特性值。
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
				getPnEast4().add(getBnDown(), getBnDown().getName());
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
	 * 返回 PnEast5 特性值。
	 * 
	 * @return UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private UIPanel getPnEast5() {
		if (ivjPnEast5 == null) {
			try {
				ivjPnEast5 = new UIPanel();
				ivjPnEast5.setName("PnEast5");
				ivjPnEast5.setLayout(new UIButtonLayout());
				getPnEast5().add(getBnTurn(), getBnTurn().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnEast5;
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
			ivjPnEastGridLayout = new GridLayout(5, 1);
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
				ivjRadioBnChoose.setText(NCLangRes.getInstance().getStrByID(
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
				ivjRadioBnHand.setText(NCLangRes.getInstance().getStrByID(
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
	 * 获得连接条件数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.JoinCondVO[]
	 */
	public JoinCondVO[] getResultFromJoin() {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iRowCount = getTable().getRowCount();
		if (iRowCount == 0)
			return null;
		JoinCondVO[] jcs = new JoinCondVO[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			//获得左表
			FromTableVO ftLeft = (FromTableVO) getTM().getValueAt(i,
					COL_LEFTTABLE);
			String tableLeftCode = ftLeft.getTablecode() + " as "
					+ ftLeft.getTablealias();
			//获得右表
			FromTableVO ftRight = (FromTableVO) getTM().getValueAt(i,
					COL_RIGHTTABLE);
			String tableRightCode = ftRight.getTablecode() + " as "
					+ ftRight.getTablealias();
			//获得左字段
			String fldLeftCode = null;
			if (isSimple())
				fldLeftCode = ((SelectFldVO) getTM().getValueAt(i, COL_LEFTFLD))
						.getFldalias();
			//获得右字段
			String fldRightCode = null;
			if (isSimple())
				fldRightCode = ((SelectFldVO) getTM().getValueAt(i,
						COL_RIGHTFLD)).getFldalias();
			//
			jcs[i] = new JoinCondVO();
			jcs[i].setLefttable(tableLeftCode);
			String strType = getTM().getValueAt(i, COL_JOINMODE).toString();
			if (strType.equals(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000863")/* @res "内连接" */))
				strType = "I";
			else if (strType.equals(NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000907")/* @res "左连接" */))
				strType = "L";
			else if (strType.equals(NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000908")/* @res "右连接" */))
				strType = "R";
			else if (strType.equals(NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000909")/* @res "全连接" */))
				strType = "F";
			jcs[i].setTypeflag(strType);
			jcs[i].setRighttable(tableRightCode);
			jcs[i].setLeftfld(fldLeftCode);
			if (isSimple())
				jcs[i].setOperator(getTM().getValueAt(i, COL_OPERATOR)
						.toString());
			else
				jcs[i].setExpression0(getTM().getValueAt(i, COL_EXPRESSION)
						.toString());
			jcs[i].setRightfld(fldRightCode);
		}
		return jcs;
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
	private UITablePane getTablePn1() {
		if (ivjTablePn1 == null) {
			try {
				ivjTablePn1 = new UITablePane();
				ivjTablePn1.setName("TablePn1");
				// user code begin {1}
				UITable table = new UITable() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void editingStopped(ChangeEvent e) {
						int row = editingRow;
						int col = editingColumn;
						TableCellEditor editor = getCellEditor();
//						Object oldValue = getValueAt(row, col);
						//结束编辑状态
						if (editor != null) {
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
				ivjTablePn2.setBounds(63, 317, 229, 133);
				// user code begin {1}
				UITable table = new UITable() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void editingStopped(ChangeEvent e) {
						int row = editingRow;
						int col = editingColumn;
						TableCellEditor editor = getCellEditor();
//						Object oldValue = getValueAt(row, col);
						//结束编辑状态
						if (editor != null) {
							removeEditor();
							afterEdit(editor, row, col);
						}
					}
				};
				ivjTablePn2.setTable(table);
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
		getBnUp().addActionListener(ivjEventHandler);
		getBnDown().addActionListener(ivjEventHandler);
		getBnTurn().addActionListener(ivjEventHandler);
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
		m_refEditors = new UIRefCellEditor[6];
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//设置表1列编辑器
		FromTableVO[] fts = qbd.getFromTables();
		if (fts == null) {
			return;
		}
		JComboBox cbbTable1 = new UIComboBox(fts);
		m_refEditors[0] = new UIRefCellEditor(cbbTable1);
		//设置连接方式列编辑器
		JComboBox cbbRelation = new UIComboBox(new Object[] {
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000863")/* @res "内连接" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000907")/* @res "左连接" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000908")/* @res "右连接" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000909") /* @res "全连接" */});
		m_refEditors[1] = new UIRefCellEditor(cbbRelation);
		//设置表2列编辑器
		JComboBox cbbTable2 = new UIComboBox(fts);
		m_refEditors[2] = new UIRefCellEditor(cbbTable2);

		//if (isSimple()) {
		//获得数据字典实例
		ObjectTree dd = getTabPn().getDatadict();
		//获得执行数据源
		//String dsName = getTabPn().getQueryBaseDef().getDsName();
		String dsName = getTabPn().getDefDsName();
		//设置字段列1编辑器(数据源切换可能导致空)
		SelectFldVO[] sfs = null;
		if (fts.length != 0) {
			ObjectTree tree = (QueryUtil.isTempTable(fts[0].getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			sfs = QueryUtil.getFldsFromTable(fts[0].getTablecode(), fts[0]
					.getTablealias(), tree);
		}
		JComboBox cbbFld1 = (sfs == null) ? new UIComboBox()
				: new UIComboBox(sfs);
		m_refEditors[3] = new UIRefCellEditor(cbbFld1);
		//设置操作符列编辑器
		JComboBox cbbOperator = new UIComboBox(new Object[] { "=", ">", "<",
				"<>", ">=", "<=" });
		m_refEditors[4] = new UIRefCellEditor(cbbOperator);
		//设置字段列2编辑器(数据源切换可能导致空)
		JComboBox cbbFld2 = (sfs == null) ? new UIComboBox()
				: new UIComboBox(sfs);
		//公用sfs
		m_refEditors[5] = new UIRefCellEditor(cbbFld2);
		//}
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
			add(getPnWest(), "West");
			add(getPnEast(), "East");
			add(getTablePn1(), "Center");
			add(getPnSouth(), "South");
			add(getPnNorth(), "North");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		getBnGroup().add(getRadioBnChoose());
		getBnGroup().add(getRadioBnHand());
		getRadioBnChoose().setSelected(true);
		//getTFHandCond().setEnabled(false);
		//getBnHand().setEnabled(false);
		//
		initTable1();
		initTable2();
		setTableCell();
		getTable1().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable1().setColumnWidth(new int[] { 20, 98, 58, 100, 112, 44, 112 });
		getTable2().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable2().setColumnWidth(new int[] { 20, 98, 58, 100, 268 });
		// user code end
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable1() {
		//表模型
		DefaultTableModel tm1 = new DefaultTableModel(new Object[] {
				"",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000862")/* @res "连接表1" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000910")/* @res "连接方式" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000911")/* @res "连接表2" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000912")/* @res "连接字段1" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000874")/* @res "比较符" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000913") /* @res "连接字段2" */}, 0) {
			public int getColumnCount() {
				return 7;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return false;
				else {
					//设置编辑器
					setValueEditor(row, col);
					return true;
				}
			}
		};
		getTable1().setModel(tm1);
		//设置表属性
		getTable1().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable1().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable1().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
		makeMultiHeader(true);
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable2() {
		//表模型
		DefaultTableModel tm2 = new DefaultTableModel(new Object[] {
				"",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000862")/* @res "连接表1" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000910")/* @res "连接方式" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000911")/* @res "连接表2" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000914") /* @res "连接表达式" */}, 0) {
			public int getColumnCount() {
				return 5;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return false;
				else {
					//设置编辑器
					setValueEditor(row, col);
					return true;
				}
			}
		};
		getTable2().setModel(tm2);
		//设置表属性
		getTable2().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable2().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable2().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
		makeMultiHeader(false);
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
	 * 多表头 创建日期：(2003-4-2 14:45:10)
	 */
	private void makeMultiHeader(boolean bSimple) {
		UITable table = (bSimple) ? getTable1() : getTable2();
		//
		TableColumnModel cm = table.getColumnModel();
		GroupableTableHeader header = (GroupableTableHeader) table
				.getTableHeader();
		ColumnGroup cg = new ColumnGroup(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000915")/* @res "JOIN 部分" */);
		cg.add(cm.getColumn(COL_LEFTTABLE));
		cg.add(cm.getColumn(COL_JOINMODE));
		cg.add(cm.getColumn(COL_RIGHTTABLE));
		header.addColumnGroup(cg);
		if (bSimple) {
			cg = new ColumnGroup(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000916")/* @res "ON 部分" */);
			cg.add(cm.getColumn(COL_LEFTFLD));
			cg.add(cm.getColumn(COL_OPERATOR));
			cg.add(cm.getColumn(COL_RIGHTFLD));
			header.addColumnGroup(cg);
		}
	}

	/**
	 * 选择录入
	 */
	public void radioBnChoose_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn2());
		add(getTablePn1(), BorderLayout.CENTER);
		getBnTurn().setText(
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000906")/* @res "互换" */);
		validate();
		repaint();
	}

	/**
	 * 手工录入
	 */
	public void radioBnHand_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn1());
		add(getTablePn2(), BorderLayout.CENTER);
		getBnTurn().setText(
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000917")/* @res "关联" */);
		validate();
		repaint();
	}

	/**
	 * 设空 创建日期：(2003-4-3 9:47:03)
	 * 
	 * @return nc.ui.iuforeport.businessquery.FldGenDlg
	 */
	public void setFldGenDlgAsNull() {
		m_fldGenDlg = null;
	}

	/**
	 * 设置where条件数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public void setResultToJoin(JoinCondVO[] jcs) {
		int iLen = (jcs == null) ? 0 : jcs.length;
		if (iLen == 0)
			return;

		//设置单选按钮
		boolean bSimple = (jcs[0].getExpression0() == null);
		getRadioBnChoose().setSelected(bSimple);
		getRadioBnHand().setSelected(!bSimple);
		if (!bSimple) {
			remove(getTablePn1());
			add(getTablePn2(), BorderLayout.CENTER);
			getBnTurn().setText(
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000917")/* @res "关联" */);
			validate();
			repaint();
		}

		//表别名-表显示名哈希表
		Hashtable hashTableId = ((SetTablePanel) getTabPn().getSetPanel(
				SetTablePanel.TAB_TITLE)).getHashTableId();
		for (int i = 0; i < iLen; i++) {
			String strType = (jcs[i].getTypeflag() == null) ? "" : jcs[i]
					.getTypeflag().toString();
			if (strType.equals("L"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000907")/* @res "左连接" */;
			else if (strType.equals("R"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000908")/* @res "右连接" */;
			else if (strType.equals("F"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000909")/* @res "全连接" */;
			else if (strType.equals("I"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000863")/* @res "内连接" */;

			if (jcs[i].getLefttable() == null)
				return;
			//转换查询字段表达式（左）
			int iIndexAs = jcs[i].getLefttable().indexOf(" as ");
			String leftTableCode = null;
			String leftTableName = null;
			String leftTableAlias = null;
			if (iIndexAs == -1) {
				leftTableCode = jcs[i].getLefttable();
				leftTableAlias = jcs[i].getLefttable();
			} else {
				leftTableCode = jcs[i].getLefttable().substring(0, iIndexAs);
				leftTableAlias = jcs[i].getLefttable().substring(iIndexAs + 4);
			}
			//名称（同名表查询时名称有变化）
			if (hashTableId.containsKey(leftTableAlias))
				leftTableName = hashTableId.get(leftTableAlias).toString();
			//字段
			String leftFldCode = jcs[i].getLeftfld();

			if (jcs[i].getRighttable() == null)
				return;
			//转换查询字段表达式（右）
			iIndexAs = jcs[i].getRighttable().indexOf(" as ");
			String rightTableCode = null;
			String rightTableName = null;
			String rightTableAlias = null;
			if (iIndexAs == -1) {
				rightTableCode = jcs[i].getRighttable();
				rightTableAlias = jcs[i].getRighttable();
			} else {
				rightTableCode = jcs[i].getRighttable().substring(0, iIndexAs);
				rightTableAlias = jcs[i].getRighttable()
						.substring(iIndexAs + 4);
			}
			//名称（同名表查询时名称有变化）
			if (hashTableId.containsKey(rightTableAlias))
				rightTableName = hashTableId.get(rightTableAlias).toString();
			//字段
			String rightFldCode = (jcs[i].getRightfld() == null) ? null
					: jcs[i].getRightfld().toString();

			//获得数据字典实例
			ObjectTree dd = getTabPn().getDatadict();
			//获得执行数据源
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//获得左表对应的树身体
			ObjectTree tree = (QueryUtil.isTempTable(leftTableCode)) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			//获得左表和字段详细信息
			ValueObject[] vos = QueryUtil.getTableFldVO(leftTableCode,
					leftTableName, leftTableAlias, leftFldCode, tree);
			if (vos == null) {
				AppDebug.debug("vosLeft == null!");//@devTools System.out.println("vosLeft == null!");
				return;
			}
			FromTableVO ftLeft = (FromTableVO) vos[0];
			SelectFldVO sfLeft = (SelectFldVO) vos[1];
			//获得右表对应的树身体（可能与左表不同，因为可能一个是表另一个是查询）
			tree = (QueryUtil.isTempTable(rightTableCode)) ? BIQueryModelTree
					.getInstance(dsName) : dd;
			//获得右表和字段详细信息
			vos = QueryUtil.getTableFldVO(rightTableCode, rightTableName,
					rightTableAlias, rightFldCode, tree);
			if (vos == null) {
				AppDebug.debug("vosRight == null!");//@devTools System.out.println("vosRight == null!");
				return;
			}
			FromTableVO ftRight = (FromTableVO) vos[0];
			SelectFldVO sfRight = (SelectFldVO) vos[1];

			if (bSimple) {
				//添加
				Object[] objrows = new Object[] { "", ftLeft, strType, ftRight,
						sfLeft, jcs[i].getOperator(), sfRight };
				getTM().addRow(objrows);
			} else {
				//高级模式
				Object[] objrows = new Object[] { "", ftLeft, strType, ftRight,
						jcs[i].getExpression0() };
				getTM().addRow(objrows);
			}
		}

		getTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	/**
	 * 设置居中对齐 创建日期：(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		DefaultTableCellRenderer renderer = null;
		for (int i = 0; i < getTable1().getColumnCount(); i++) {
			TableColumn tc = getTable1().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNoCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
		for (int i = 0; i < getTable2().getColumnCount(); i++) {
			TableColumn tc = getTable2().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNoCellRenderer();
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
		if (!isSimple() && iCol == COL_EXPRESSION)
			return;
		else if (iCol != COL_LEFTFLD && iCol != COL_RIGHTFLD)
			//设置关系符、表、操作符列编辑器
			tc.setCellEditor(m_refEditors[iCol - 1]);
		else if (iCol == COL_LEFTFLD) {
			//获得旧的选中索引
//			int iOldSelIndex = 0;
			if (getTable().getCellEditor(iRow, iCol) instanceof UIRefCellEditor) {
//				UIRefCellEditor refEditor = (UIRefCellEditor) getTable()
//						.getCellEditor(iRow, iCol);
//				JComboBox cbbOld = (JComboBox) refEditor.getComponent();
//				iOldSelIndex = cbbOld.getSelectedIndex();
			}
			//设置字段列1编辑器
			FromTableVO ft1 = (FromTableVO) getTM().getValueAt(iRow,
					COL_LEFTTABLE);
			//获得数据字典实例
			ObjectTree dd = getTabPn().getDatadict();
			//获得执行数据源
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//查询对应表列1的字段
			ObjectTree tree = (QueryUtil.isTempTable(ft1.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs1 = QueryUtil.getFldsFromTable(ft1.getTablecode(),
					ft1.getTablealias(), tree);
			//数据源切换可能导致空
			JComboBox cbbFld1 = (sfs1 == null) ? new UIComboBox()
					: new UIComboBox(sfs1);
			cbbFld1.setSelectedItem(getTM().getValueAt(iRow, iCol));
			tc.setCellEditor(new UIRefCellEditor(cbbFld1));
		} else if (iCol == COL_RIGHTFLD) {
			//设置字段列2编辑器
			FromTableVO ft2 = (FromTableVO) getTM().getValueAt(iRow,
					COL_RIGHTTABLE);
			//获得数据字典实例
			ObjectTree dd = getTabPn().getDatadict();
			//获得执行数据源
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//查询对应表列2的字段
			ObjectTree tree = (QueryUtil.isTempTable(ft2.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs2 = QueryUtil.getFldsFromTable(ft2.getTablecode(),
					ft2.getTablealias(), tree);
			//获得旧的选中索引
			UIRefCellEditor refEditor = null;
			//
			//数据源切换可能导致空
			JComboBox cbbFld2 = (sfs2 == null) ? new UIComboBox()
					: new UIComboBox(sfs2);
			//cbbFld2.setSelectedIndex(iOldSelIndex);
			//if (refEditor == null)
			refEditor = new UIRefCellEditor(cbbFld2);
			tc.setCellEditor(refEditor);
		}
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
		//回设
		qmd.getQueryBaseVO().setJoinConds(getResultFromJoin());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setResultToJoin(qmd.getQueryBaseVO().getJoinConds());
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		return null;
	}

	/**
	 * 刷新查询基本定义
	 */
	public void refreshQbd() {
		//获得筛选条件定义
		JoinCondVO[] jcs = getResultFromJoin();
		//刷新
		getTabPn().getQueryBaseDef().setJoinConds(jcs);
	}
} 