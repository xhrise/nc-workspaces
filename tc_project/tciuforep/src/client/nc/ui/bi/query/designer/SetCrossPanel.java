/*
 * 创建日期 2005-5-24
 *
 */
package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iuforeport.businessquery.RotateCrossPanel;
import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.ValueObject;
import nc.vo.pub.querymodel.QueryBaseVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.RotateCrossVO;
import nc.vo.pub.querymodel.SimpleCrossVO;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zjb
 * 
 * 交叉设置面板
 */
public class SetCrossPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0053";//"交叉属性";

	//生成表达式对话框实例
	private FldGenDlg m_fldGenDlg = null;

	//单元编辑器数组
	private UIRefCellEditor[] m_refEditors = null;

	//常量
	public final static int COL_FLDCODE = 1;

	public final static int COL_FLDNAME = 2;

	//public final static int COL_FLDTYPE = 3;
	public final static int COL_FLDWHERE = 3;

	public final static int COL_FLDLOCATE = 4;

	private UIPanel ivjPnEast = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UIPanel ivjPnWest = null;

	private UITablePane ivjTablePn = null;

	private UIButton ivjBnAdd = null;

	private UIButton ivjBnDel = null;

	private UIPanel ivjPnEast1 = null;

	private UIPanel ivjPnEast2 = null;

	private UIPanel ivjPnEast3 = null;

	private UIButton ivjBnModify = null;

	private UIButton ivjBnUp = null;

	private UIPanel ivjPnEast4 = null;

	private UIButton ivjBnDown = null;

	private UIPanel ivjPnEast5 = null;

	private ButtonGroup ivjButtonGroup = null;

	private UIRadioButton ivjRadioBnNone = null;

	private UIRadioButton ivjRadioBnRotate = null;

	private UIRadioButton ivjRadioBnSimple = null;

	private RotateCrossPanel ivjPnRotateCross = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetCrossPanel.this.getBnAdd())
				connEtoC1(e);
			if (e.getSource() == SetCrossPanel.this.getBnDel())
				connEtoC3(e);
			if (e.getSource() == SetCrossPanel.this.getBnModify())
				connEtoC2(e);
			if (e.getSource() == SetCrossPanel.this.getBnUp())
				connEtoC4(e);
			if (e.getSource() == SetCrossPanel.this.getBnDown())
				connEtoC5(e);
			if (e.getSource() == SetCrossPanel.this.getRadioBnSimple())
				connEtoC6(e);
			if (e.getSource() == SetCrossPanel.this.getRadioBnNone())
				connEtoC7(e);
			if (e.getSource() == SetCrossPanel.this.getRadioBnRotate())
				connEtoC8(e);
		};
	};

	/**
	 * SetTablePanel 构造子注解。
	 */
	public SetCrossPanel() {
		super();
		initialize();
	}

	/**
	 * 增行 创建日期：(2003-8-8 15:14:24)
	 */
	private void addLine(String strWhere) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//界面加行
		int iIndex = getTM().getRowCount();
		Object[] objrows = new Object[] {
				"",
				"",
				"",
				strWhere,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000878") /* @res "列头" */};
		getTM().addRow(objrows);
		//选中新增行
		getTable().getSelectionModel().setSelectionInterval(iIndex, iIndex);

		/* 以下为一致性处理 */
		SimpleCrossVO newsc = new SimpleCrossVO();
		//获得属性
		newsc.setFldCode("");
		newsc.setFldName("");
		//条件
		newsc.setWhereCond(strWhere);
		//位置
		newsc.setLocate(QueryConst.CROSS_COL);
		//
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		SimpleCrossVO[] oldscs = qbd.getScs();
		//合并VO数组
		ValueObject[] vos = BIModelUtil.addToVOs(oldscs, newsc);
		SimpleCrossVO[] newscs = new SimpleCrossVO[vos.length];
		for (int i = 0; i < vos.length; i++)
			newsc = (SimpleCrossVO) vos[i];
		qbd.setScs(newscs);
	}

	/**
	 * add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
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
			//增加表处理
			addLine(strWhere);
		}
		return;
	}

	/**
	 * del
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//界面刷新
			delLine();

			/* 以下为一致性处理 */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//合并VO数组
			ValueObject[] vos = BIModelUtil.delFromVOs(qbd.getScs(), iSelIndex);
			SimpleCrossVO[] newscs = new SimpleCrossVO[vos.length];
			for (int i = 0; i < vos.length; i++)
				newscs[i] = (SimpleCrossVO) vos[i];
			qbd.setScs(newscs);
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
			SimpleCrossVO[] oldscs = qbd.getScs();
			SimpleCrossVO[] newscs = new SimpleCrossVO[oldscs.length];
			for (int i = 0; i < oldscs.length; i++)
				if (i == iSelIndex)
					newscs[i] = oldscs[iSelIndex + 1];
				else if (i == iSelIndex + 1)
					newscs[i] = oldscs[iSelIndex];
				else
					newscs[i] = oldscs[i];
			qbd.setScs(newscs);
		}
	}

	/**
	 * modify
	 */
	public void bnModify_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//弹出对话框
			FldGenDlg dlg = getFldGenDlg();
			//清空
			dlg.doSetInfo(new Object[] { null, null,
					getTM().getValueAt(iSelIndex, COL_FLDWHERE), null });
			dlg.setBnAddEnabled(false);
			dlg.setSomeInvisibled();
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				String strWhere = dlg.getExp();
				//终止编辑态
				if (getTable().getCellEditor() != null)
					getTable().getCellEditor().stopCellEditing();
				//设置
				getTM().setValueAt(strWhere, iSelIndex, COL_FLDWHERE);
				//选中
				getTable().getSelectionModel().setSelectionInterval(iSelIndex,
						iSelIndex);

				/* 以下为一致性处理 */
				QueryBaseDef qbd = getTabPn().getQueryBaseDef();
				//合并VO数组
				SimpleCrossVO[] oldscs = qbd.getScs();
				int iLen = (oldscs == null) ? 0 : oldscs.length;
				SimpleCrossVO[] newscs = new SimpleCrossVO[iLen];
				for (int i = 0; i < iLen; i++) {
					newscs[i] = (SimpleCrossVO) oldscs[i].clone();
					newscs[iSelIndex].setWhereCond(strWhere);
				}
				qbd.setScs(newscs);
			}
		}
		return;
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
			SimpleCrossVO[] oldscs = qbd.getScs();
			SimpleCrossVO[] newscs = new SimpleCrossVO[oldscs.length];
			for (int i = 0; i < oldscs.length; i++)
				if (i == iSelIndex - 1)
					newscs[i] = oldscs[iSelIndex];
				else if (i == iSelIndex)
					newscs[i] = oldscs[iSelIndex - 1];
				else
					newscs[i] = oldscs[i];
			qbd.setScs(newscs);
		}
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		if (isCrossValid() == QueryConst.CROSSTYPE_SIMPLE) {
			int iLen = getTM().getRowCount();
			Vector<String> vec = new Vector<String>();
			boolean bRow = false;
			boolean bCol = false;
			for (int i = 0; i < iLen; i++) {
				Object objCode = getTM().getValueAt(i, COL_FLDCODE);
				String strCode = (objCode == null) ? "" : objCode.toString()
						.trim();
				if (strCode.equals(""))
					return NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000879")/* @res "编码不能为空" */;
				if (getTM().getValueAt(i, COL_FLDNAME) == null
						|| getTM().getValueAt(i, COL_FLDNAME).toString().trim()
								.equals(""))
					return NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000880")/* @res "名称不能为空" */;
				//if (getTM().getValueAt(i, COL_FLDWHERE) == null
				//|| getTM().getValueAt(i,
				// COL_FLDWHERE).toString().trim().equals(""))
				//return "条件不能为空";
				if (vec.indexOf(strCode) != -1)
					return NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000881")/* @res "编码不能重复" */;
				vec.addElement(strCode);
				String strLocate = getTM().getValueAt(i, COL_FLDLOCATE)
						.toString();
				if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000882")/* @res "行头" */))
					bRow = true;
				if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000878")/* @res "列头" */))
					bCol = true;
			}
			if (!bRow)
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000883")/* @res "不能没有行头项" */;
			if (!bCol)
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000884")/* @res "不能没有列头项" */;
			return null;
		} else if (isCrossValid() == QueryConst.CROSSTYPE_ROTATE)
			return getPnRotateCross().check(true);
		else
			return null;
	}

	/**
	 * 清空旋转交叉面板 创建日期：(2003-11-25 11:31:58)
	 * 
	 * @return nc.ui.iuforeport.businessquery.RotateCrossPanel
	 */
	public void clearRotateCross(SelectFldVO[] sfs) {
		getPnRotateCross().setRotateCross(null, sfs);
	}

	/**
	 * connEtoC1: (BnAdd.action.actionPerformed(ActionEvent) -->
	 * SetTablePanel.bnAdd_ActionPerformed(LActionEvent;)V)
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
	 * connEtoC2: (BnDel.action.actionPerformed(ActionEvent) -->
	 * SetTablePanel.bnDel_ActionPerformed(LActionEvent;)V)
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
	 * connEtoC3: (BnJoin.action.actionPerformed(ActionEvent) -->
	 * SetTablePanel.bnJoin_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
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
	 * connEtoC4: (BnUp.action.actionPerformed(ActionEvent) -->
	 * SetFldPanel.bnUp_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(ActionEvent arg1) {
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
	 * connEtoC5: (BnDown.action.actionPerformed(ActionEvent) -->
	 * SetFldPanel.bnDown_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(ActionEvent arg1) {
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
	 * connEtoC6: (CheckBoxCross.action.actionPerformed(ActionEvent) -->
	 * SetCrossPanel.checkBoxCross_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnSimple_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7: (RadioBnNone.action.actionPerformed(ActionEvent) -->
	 * SetCrossPanel.radioBnNone_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC7(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnNone_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC8: (RadioBnRotate.action.actionPerformed(ActionEvent) -->
	 * SetCrossPanel.radioBnRotate_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC8(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.radioBnRotate_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 旋转交叉响应 创建日期：(2003-11-25 13:30:13)
	 */
	private void convertUI_rotate() {
		//界面刷新
		remove(getPnWest());
		remove(getTablePn());
		remove(getPnEast());
		add(getPnRotateCross(), BorderLayout.CENTER);
		validate();
		repaint();
		//按钮控制
		getPnRotateCross().setBnEnabled(true);
	}

	/**
	 * 投影交叉响应 创建日期：(2003-11-25 13:30:13)
	 */
	private void convertUI_simple() {

		//界面刷新
		remove(getPnRotateCross());
		add(getPnWest(), BorderLayout.WEST);
		add(getTablePn(), BorderLayout.CENTER);
		add(getPnEast(), BorderLayout.EAST);
		validate();
		repaint();
		//按钮控制
		setBnEnabled(true);
	}

	/**
	 * 删行 创建日期：(2003-8-8 15:14:24)
	 */
	private void delLine() {
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//界面刷新
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable().getRowCount() != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	/**
	 * 返回 BnAdd 特性值。
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
	 * 返回 BnDel 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnModify() {
		if (ivjBnModify == null) {
			try {
				ivjBnModify = new UIButton();
				ivjBnModify.setName("BnModify");
				ivjBnModify.setText(NCLangRes.getInstance().getStrByID(
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
	 * 返回 ButtonGroup 特性值。
	 * 
	 * @return javax.swing.ButtonGroup
	 */
	/* 警告：此方法将重新生成。 */
	private javax.swing.ButtonGroup getButtonGroup() {
		if (ivjButtonGroup == null) {
			try {
				ivjButtonGroup = new javax.swing.ButtonGroup();
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjButtonGroup;
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
				ivjPnEast.setPreferredSize(new java.awt.Dimension(100, 10));
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
				getPnEast2().add(getBnModify(), getBnModify().getName());
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
				getPnEast3().add(getBnDel(), getBnDel().getName());
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
	 * 返回 PnEast4 特性值。
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
				getPnEast4().add(getBnUp(), getBnUp().getName());
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
				getPnEast5().add(getBnDown(), getBnDown().getName());
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
	 * @return java.awt.GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.GridLayout getPnEastGridLayout() {
		java.awt.GridLayout ivjPnEastGridLayout = null;
		try {
			/* 创建部件 */
			ivjPnEastGridLayout = new java.awt.GridLayout(5, 1);
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
				getPnNorth().add(getRadioBnNone(), getRadioBnNone().getName());
				getPnNorth().add(getRadioBnSimple(),
						getRadioBnSimple().getName());
				getPnNorth().add(getRadioBnRotate(),
						getRadioBnRotate().getName());
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
	 * 返回 PnRotateCross 特性值。
	 * 
	 * @return nc.ui.iuforeport.businessquery.RotateCrossPanel
	 */
	/* 警告：此方法将重新生成。 */
	private RotateCrossPanel getPnRotateCross() {
		if (ivjPnRotateCross == null) {
			try {
				ivjPnRotateCross = new nc.ui.iuforeport.businessquery.RotateCrossPanel();
				ivjPnRotateCross.setName("PnRotateCross");
				ivjPnRotateCross.setLocation(566, 287);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnRotateCross;
	}

	/**
	 * 获得旋转交叉面板 创建日期：(2003-11-25 11:31:58)
	 * 
	 * @return nc.ui.iuforeport.businessquery.RotateCrossPanel
	 */
	public RotateCrossPanel getPnRotateCrossPub() {
		return getPnRotateCross();
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
	 * 返回 RadioBnNone 特性值。
	 * 
	 * @return UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnNone() {
		if (ivjRadioBnNone == null) {
			try {
				ivjRadioBnNone = new UIRadioButton();
				ivjRadioBnNone.setName("RadioBnNone");
				ivjRadioBnNone.setPreferredSize(new java.awt.Dimension(68, 22));
				ivjRadioBnNone.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000885")/* @res "不交叉" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnNone;
	}

	/**
	 * 返回 RadioBnRotate 特性值。
	 * 
	 * @return UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnRotate() {
		if (ivjRadioBnRotate == null) {
			try {
				ivjRadioBnRotate = new UIRadioButton();
				ivjRadioBnRotate.setName("RadioBnRotate");
				ivjRadioBnRotate
						.setPreferredSize(new java.awt.Dimension(80, 22));
				ivjRadioBnRotate.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000077")/* @res "旋转交叉" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnRotate;
	}

	/**
	 * 返回 RadioBnCross 特性值。
	 * 
	 * @return UIRadioButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIRadioButton getRadioBnSimple() {
		if (ivjRadioBnSimple == null) {
			try {
				ivjRadioBnSimple = new UIRadioButton();
				ivjRadioBnSimple.setName("RadioBnSimple");
				ivjRadioBnSimple
						.setPreferredSize(new java.awt.Dimension(80, 22));
				ivjRadioBnSimple.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000886")/* @res "投影交叉" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjRadioBnSimple;
	}

	/**
	 * 获得投影交叉定义 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param scs
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public RotateCrossVO getResultFromCross_rotate() {
		return getPnRotateCross().getRotateCross();
	}

	/**
	 * 获得简单交叉定义数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param scs
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public SimpleCrossVO[] getResultFromCross_simple() {
		SimpleCrossVO[] scs = null;
		int iLen = getTM().getRowCount();
		if (iLen != 0 && isCrossValid() == QueryConst.CROSSTYPE_SIMPLE) {
			scs = new SimpleCrossVO[iLen];
			for (int i = 0; i < iLen; i++) {
				scs[i] = new SimpleCrossVO();
				//获得属性
				scs[i]
						.setFldCode(getTM().getValueAt(i, COL_FLDCODE)
								.toString());
				scs[i]
						.setFldName(getTM().getValueAt(i, COL_FLDNAME)
								.toString());
				//条件
				scs[i].setWhereCond(getTM().getValueAt(i, COL_FLDWHERE)
						.toString());
				//位置
				String strLocate = getTM().getValueAt(i, COL_FLDLOCATE)
						.toString();
				int iLocate = QueryConst.CROSS_NONE;
				if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000882")/* @res "行头" */))
					iLocate = QueryConst.CROSS_ROW;
				else if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000878")/* @res "列头" */))
					iLocate = QueryConst.CROSS_COL;
				scs[i].setLocate(iLocate);
			}
		}
		return scs;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		return (UITable) getTablePn().getTable();
	}

	/**
	 * 返回 TablePn 特性值。
	 * 
	 * @return UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	private UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new UITablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
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
		getBnModify().addActionListener(ivjEventHandler);
		getBnUp().addActionListener(ivjEventHandler);
		getBnDown().addActionListener(ivjEventHandler);
		getRadioBnSimple().addActionListener(ivjEventHandler);
		getRadioBnNone().addActionListener(ivjEventHandler);
		getRadioBnRotate().addActionListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			//可视化存储后注意修改getCheckBoxCross()方法
			// user code end
			setName("SetTablePanel");
			setLayout(new java.awt.BorderLayout());
			setSize(400, 240);
			add(getPnSouth(), "South");
			add(getPnWest(), "West");
			add(getPnNorth(), "North");
			add(getPnEast(), "East");
			add(getTablePn(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		setBnEnabled(false);
		//按钮单选
		getButtonGroup().add(getRadioBnNone());
		getButtonGroup().add(getRadioBnSimple());
		getButtonGroup().add(getRadioBnRotate());
		getRadioBnNone().setSelected(true);
		//初始化
		initRefValue();
		initTable();
		setTableCell();
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 92, 108, 228, 96 });
		// user code end
	}

	/**
	 * 初始化值的CellEditor 创建日期：(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	private void initRefValue() {

		//初始化表单元编辑器
		m_refEditors = new UIRefCellEditor[1];
		//
		//JComboBox cbb = new UIComboBox();
		//cbb.addItem("整数");
		//cbb.addItem("小数");
		//cbb.addItem("字符");
		//m_refEditors[0] = new UIRefCellEditor(cbb);
		//
		JComboBox cbb = new UIComboBox();
		cbb.addItem(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-000878")/* @res "列头" */);
		cbb.addItem(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-000882")/* @res "行头" */);
		cbb.addItem(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-000887")/* @res "退化" */);
		m_refEditors[0] = new UIRefCellEditor(cbb);
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable() {
		//表模型
		DefaultTableModel tm = new DefaultTableModel(
				new Object[] {
						"",
						NCLangRes.getInstance().getStrByID("10241201",
								"UC000-0003279")/* @res "编码" */,
						NCLangRes.getInstance().getStrByID("10241201",
								"UC000-0001155")/* @res "名称" */,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000843")/* @res "筛选条件" */,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000888") /* @res "位置" */}, 0) {
			public int getColumnCount() {
				return 5;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				if (col == COL_FLDLOCATE)
					getTable().getColumn(
							NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-000888")/*
														  * @res "位置"
														  */).setCellEditor(m_refEditors[0]);
				return true;
			}
		};
		getTable().setModel(tm);
		//设置表属性
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * 判断设置是否有效 创建日期：(2003-11-8 16:25:38)
	 * 
	 * @return boolean
	 */
	public int isCrossValid() {
		if (getRadioBnSimple().isSelected())
			return 1;
		else if (getRadioBnRotate().isSelected())
			return 2;
		else
			return 0;
	}

	/**
	 * 不交叉
	 */
	public void radioBnNone_ActionPerformed(ActionEvent actionEvent) {

		setBnEnabled(false);
		getPnRotateCross().setBnEnabled(false);
	}

	/**
	 * 旋转交叉
	 */
	public void radioBnRotate_ActionPerformed(ActionEvent actionEvent) {

		convertUI_rotate();
	}

	/**
	 * 投影交叉
	 */
	public void radioBnSimple_ActionPerformed(ActionEvent actionEvent) {

		convertUI_simple();
	}

	/**
	 * 设置按钮可用性 创建日期：(2003-11-8 16:30:08)
	 * 
	 * @param bEnable
	 *            boolean
	 */
	public void setBnEnabled(boolean bEnable) {
		getBnAdd().setEnabled(bEnable);
		getBnDel().setEnabled(bEnable);
		getBnModify().setEnabled(bEnable);
		getBnUp().setEnabled(bEnable);
		getBnDown().setEnabled(bEnable);
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
	 * 设置交叉定义 创建日期：(2003-9-24 15:32:38)
	 * 
	 * @param cp
	 *            nc.vo.pub.codingwiz.CodeParamVO[]
	 */
	public void setResultToCross(SimpleCrossVO[] scs, RotateCrossVO rc) {
		//投影交叉
		if (scs != null && scs.length != 0) {
			getRadioBnSimple().setSelected(true);
			setBnEnabled(true);
			setResultToCross_simple(scs);
			return;
		}
		//旋转交叉
		SelectFldVO[] sfs = ((SetFldPanel) getTabPn().getSetPanel(
				SetFldPanel.TAB_TITLE)).getResultFromFld();
		getPnRotateCross().setRotateCross(rc, sfs);
		if (rc != null) {
			getRadioBnRotate().setSelected(true);
			convertUI_rotate();
		}
	}

	/**
	 * 设置简单交叉定义数组 创建日期：(2003-9-24 15:32:38)
	 * 
	 * @param cp
	 *            nc.vo.pub.codingwiz.CodeParamVO[]
	 */
	public void setResultToCross_simple(SimpleCrossVO[] scs) {

		//更新表格
		getTM().setNumRows(0);
		int iLen = (scs == null) ? 0 : scs.length;
		for (int i = 0; i < iLen; i++) {
			//数据类型
//			String strDataType = NCLangRes.getInstance().getStrByID("10241201",
//					"UPP10241201-000653")/* @res "字符" */;
			//if (scs[i].getDataType() == Variant.INT)
			//strDataType = "整数";
			//else
			//if (scs[i].getDataType() == Variant.DOUBLE)
			//strDataType = "小数";
			//位置
			String strLocate = NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000887")/* @res "退化" */;
			if (scs[i].getLocate() == QueryConst.CROSS_ROW)
				strLocate = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000882")/* @res "行头" */;
			else if (scs[i].getLocate() == QueryConst.CROSS_COL)
				strLocate = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000878")/* @res "列头" */;
			//增行
			Object[] objrows = new Object[] { "", scs[i].getFldCode(),
					scs[i].getFldName(),
					//strDataType,
					scs[i].getWhereCond(), strLocate };
			getTM().addRow(objrows);
		}
	}

	/**
	 * 设置居中对齐 创建日期：(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		DefaultTableCellRenderer renderer = null;
		for (int i = 0; i < getTable().getColumnCount(); i++) {
			TableColumn tc = getTable().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNoCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
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
		QueryBaseVO qb = qmd.getQueryBaseVO();
		int iCross = isCrossValid();
		if (iCross == QueryConst.CROSSTYPE_SIMPLE) {
			qb.setScs(getResultFromCross_simple());
			qb.setRotateCross(null);
		} else if (iCross == QueryConst.CROSSTYPE_ROTATE) {
			qb.setScs(null);
			qb.setRotateCross(getResultFromCross_rotate());
		} else {
			qb.setScs(null);
			qb.setRotateCross(null);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		QueryBaseVO qb = qmd.getQueryBaseVO();
		setResultToCross(qb.getScs(), qb.getRotateCross());
	}

}
 