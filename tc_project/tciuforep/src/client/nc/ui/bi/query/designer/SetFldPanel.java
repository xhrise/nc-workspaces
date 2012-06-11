/*
 * 创建日期 2005-5-24
 */
package nc.ui.bi.query.designer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.ValueObject;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zjb
 * 
 * 字段设置界面
 */
public class SetFldPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0051";//"查询字段";

	//生成表达式对话框实例
	private FldGenDlg m_fldGenDlg = null;

	//父组件实例
	private SetColumnPanel m_columnPanel = null;

	//常量
	public final static int COL_FLDEXP = 1;

	public final static int COL_FLDNAME = 2;

	public final static int COL_FLDALIAS = 3;

	//public final static int COL_FLDDESC = 4;
	public final static int COL_FLDNOTE = 4;

	private UIPanel ivjPnEast = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UIPanel ivjPnWest = null;

	private QueryFldTablePane ivjTablePn = null;

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

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetFldPanel.this.getBnAdd())
				connEtoC1(e);
			if (e.getSource() == SetFldPanel.this.getBnDel())
				connEtoC3(e);
			if (e.getSource() == SetFldPanel.this.getBnModify())
				connEtoC2(e);
			if (e.getSource() == SetFldPanel.this.getBnUp())
				connEtoC4(e);
			if (e.getSource() == SetFldPanel.this.getBnDown())
				connEtoC5(e);
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == SetFldPanel.this.getPnEast1())
				connEtoC6(e);
		};

		public void mouseEntered(MouseEvent e) {
		};

		public void mouseExited(MouseEvent e) {
		};

		public void mousePressed(MouseEvent e) {
		};

		public void mouseReleased(MouseEvent e) {
		};
	};

	/**
	 *  
	 */
	public SetFldPanel() {
		super();
		initialize();
	}

	/**
	 * add
	 */
	@SuppressWarnings("unchecked")
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		//弹出对话框
		FldGenDlg dlg = getFldGenDlg();
		//用于判断重复的哈希表
		Hashtable hashFldAlias = getHashFldAlias(-1);
		dlg.setHashFldAlias(hashFldAlias);
		//清空
		dlg.doClear();
		dlg.setBnAddEnabled(true);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			if (dlg.isMultiSelect()) {
				//获得生成字段
				SelectFldVO[] newsfs = dlg.getGenFlds();
				//增加表处理
				doAdd(newsfs);
			} else {
				//获得生成字段
				SelectFldVO newsf = dlg.getGenFld();
				//增加表处理
				doAdd(newsf);
			}
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
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable().getRowCount() != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);

			/* 以下为一致性处理 */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//合并VO数组
			ValueObject[] vos = BIModelUtil.delFromVOs(qbd.getSelectFlds(),
					iSelIndex);
			SelectFldVO[] newsfs = new SelectFldVO[vos.length];
			for (int i = 0; i < vos.length; i++)
				newsfs[i] = (SelectFldVO) vos[i];
			qbd.setSelectFlds(newsfs);

			//同步排序字段页签
			((SetOrderbyPanel) getTabPn()
					.getSetPanel(SetOrderbyPanel.TAB_TITLE))
					.setResultToOrderby(newsfs, null);
			//同步旋转交叉设置
			//((SetCrossPanel)
			// getTabPn().getSetPanel(BusiModelDesignUI.TAB_CROSS)).clearRotateCross(newsfs);
		}
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
			SelectFldVO[] oldSfs = qbd.getSelectFlds();
			SelectFldVO[] newsfs = new SelectFldVO[oldSfs.length];
			for (int i = 0; i < oldSfs.length; i++)
				if (i == iSelIndex)
					newsfs[i] = oldSfs[iSelIndex + 1];
				else if (i == iSelIndex + 1)
					newsfs[i] = oldSfs[iSelIndex];
				else
					newsfs[i] = oldSfs[i];
			qbd.setSelectFlds(newsfs);
		}
	}

	/**
	 * modify
	 */
	@SuppressWarnings("unchecked")
	public void bnModify_ActionPerformed(ActionEvent actionEvent) {
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//弹出对话框
			FldGenDlg dlg = getFldGenDlg();
			//用于判断重复的哈希表
			Hashtable hashFldAlias = getHashFldAlias(iSelIndex);
			dlg.setHashFldAlias(hashFldAlias);
			//清空
			dlg.doSetInfo(new Object[] {
					getTM().getValueAt(iSelIndex, COL_FLDNAME),
					getTM().getValueAt(iSelIndex, COL_FLDALIAS),
					getTM().getValueAt(iSelIndex, COL_FLDEXP),
					getTM().getValueAt(iSelIndex, COL_FLDNOTE) });
			dlg.setBnAddEnabled(false);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				//删行
				getTM().removeRow(iSelIndex);
				//获得生成字段
				SelectFldVO newsf = dlg.getGenFld();
				//加行
				getTM().insertRow(
						iSelIndex,
						new Object[] { "", newsf.getExpression(),
								newsf.getFldname(), newsf.getFldalias(),
								newsf.getNote() });
				//选中
				getTable().getSelectionModel().setSelectionInterval(iSelIndex,
						iSelIndex);

				/* 以下为一致性处理 */
				//更新QueryBaseDef
				QueryBaseDef qbd = getTabPn().getQueryBaseDef();
				//合并VO数组
				SelectFldVO[] oldSfs = qbd.getSelectFlds();
				SelectFldVO[] newsfs = new SelectFldVO[oldSfs.length];
				for (int i = 0; i < oldSfs.length; i++)
					if (i == iSelIndex)
						newsfs[i] = newsf;
					else
						newsfs[i] = oldSfs[i];
				qbd.setSelectFlds(newsfs);

				//同步排序字段页签
				((SetOrderbyPanel) getTabPn().getSetPanel(
						SetOrderbyPanel.TAB_TITLE)).setResultToOrderby(newsfs,
						null);
				//同步旋转交叉设置
				//((SetCrossPanel) getTabPn().getSetPanel(
				//		SetCrossPanel.TAB_TITLE)).clearRotateCross(newsfs);
			}
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
			SelectFldVO[] oldSfs = qbd.getSelectFlds();
			SelectFldVO[] newsfs = new SelectFldVO[oldSfs.length];
			for (int i = 0; i < oldSfs.length; i++)
				if (i == iSelIndex - 1)
					newsfs[i] = oldSfs[iSelIndex];
				else if (i == iSelIndex)
					newsfs[i] = oldSfs[iSelIndex - 1];
				else
					newsfs[i] = oldSfs[i];
			qbd.setSelectFlds(newsfs);
		}
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		int iLen = getTM().getRowCount();
		if (iLen == 0)
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000891")/* @res "没有选择字段" */;
		//for (int i = 0; i < iLen; i++) {
		//if (getTM().getValueAt(i, 2) == null
		//|| getTM().getValueAt(i, 2).toString().trim().equals(""))
		//return "显示名不能为空";
		//if (getTM().getValueAt(i, 4) == null
		//|| getTM().getValueAt(i, 4).toString().trim().equals(""))
		//return "别名不能为空";
		//}
		return null;
	}
	public String checkOnSwitch(){
		return null;
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
	 * connEtoC6: (PnEast1.mouse.mouseClicked(MouseEvent) -->
	 * SetFldPanel.pnEast1_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.pnEast1_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * 增加表处理 创建日期：(2003-4-4 13:55:06)
	 * 
	 * @param newsf
	 *            nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public void doAdd(SelectFldVO[] newsfs) {
		int iLen = (newsfs == null) ? 0 : newsfs.length;
		for (int i = 0; i < iLen; i++)
			doAdd(newsfs[i]);
	}

	/**
	 * 增加表处理 创建日期：(2003-4-4 13:55:06)
	 * 
	 * @param newsf
	 *            nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public void doAdd(SelectFldVO newsf) {
		//添加表格
		getTM().addRow(
				new Object[] { "", newsf.getExpression(), newsf.getFldname(),
						newsf.getFldalias(), newsf.getNote() });
		//选中新增行
		getTable().getSelectionModel().setSelectionInterval(
				getTM().getRowCount() - 1, getTM().getRowCount() - 1);

		/* 以下为一致性处理 */
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		SelectFldVO[] oldVOs = qbd.getSelectFlds();
		//合并VO数组
		ValueObject[] vos = BIModelUtil.addToVOs(oldVOs, newsf);
		SelectFldVO[] newsfs = new SelectFldVO[vos.length];
		for (int i = 0; i < vos.length; i++)
			newsfs[i] = (SelectFldVO) vos[i];
		qbd.setSelectFlds(newsfs);

		//同步排序字段页签
		((SetOrderbyPanel) getTabPn().getSetPanel(SetOrderbyPanel.TAB_TITLE))
				.setResultToOrderby(newsfs, null);
		//同步旋转交叉设置
		//((SetCrossPanel)
		// getTabPn().getSetPanel(BusiModelDesignUI.TAB_CROSS)).clearRotateCross(newsfs);
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
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000330")/* @res "增加" */);
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
				ivjBnDown.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
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
				ivjBnUp.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000145")/* @res "上移" */);
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
	 * 获得当前字段别名-字段VO哈希表 创建日期：(2003-11-25 10:59:36)
	 * 
	 * @return java.util.Hashtable
	 */
	public Hashtable getHashAliasFldVo() {
		Hashtable<String, SelectFldVO> hashAliasFld = new Hashtable<String, SelectFldVO>();
		SelectFldVO[] sfs = getResultFromFld();
		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++)
			hashAliasFld.put(sfs[i].getFldalias(), sfs[i]);
		return hashAliasFld;
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
	 * 获得查询字段数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO[]
	 */
	public SelectFldVO[] getResultFromFld() {
		int iRowCount = getTable().getRowCount();
		SelectFldVO[] sfs = new SelectFldVO[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			sfs[i] = new SelectFldVO();
			sfs[i].setExpression(getTM().getValueAt(i, COL_FLDEXP).toString());
			sfs[i].setFldname(getTM().getValueAt(i, COL_FLDNAME).toString());
			sfs[i].setFldalias(getTM().getValueAt(i, COL_FLDALIAS).toString());
			//
			Object objNote = getTM().getValueAt(i, COL_FLDNOTE);
			String strNote = (objNote == null) ? "" : objNote.toString();
			sfs[i].setNote(strNote);
		}
		return sfs;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public QueryFldTable getTable() {
		return (QueryFldTable) getTablePn().getTable();
	}

	/**
	 * 返回 TablePn 特性值。
	 * 
	 * @return UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	public QueryFldTablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new QueryFldTablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}
				//设置可接收拖动的表格
				QueryFldTable table = new QueryFldTable();
				ivjTablePn.setTable(table);
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
		getPnEast1().addMouseListener(ivjEventHandler);
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
		initTable();
		setTableCell();
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 244, 120, 88, 72 });
		// user code end
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable() {
		//表模型
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				"",
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000892")/* @res "字段表达式" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000893")/* @res "显示名" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000894")/* @res "别名" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000142") /* @res "描述" */}, 0) {
			public int getColumnCount() {
				return 5;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				//if (col == COL_FLDDESC)
				//getTable().getColumn("是否降序").setCellEditor(m_refEditor);
				//return (col == COL_FLDDESC);
				return false;
			}
		};
		getTable().setModel(tm);
		//设置表属性
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * KILLBPM
	 */
	public void pnEast1_MouseClicked(MouseEvent mouseEvent) {
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
	 * 设置查询字段数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public void setResultToFld(SelectFldVO[] sfs) {
		//设置
		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++) {
			String alias = sfs[i].getFldalias();
			Object[] objrows = new Object[] { "", sfs[i].getExpression(),
					sfs[i].getFldname(), alias, sfs[i].getNote() };
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
		qmd.getQueryBaseVO().setSelectFlds(getResultFromFld());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setResultToFld(qmd.getQueryBaseVO().getSelectFlds());
	}

	/*
	 * 获得判断重复的哈希表
	 */
	@SuppressWarnings("unchecked")
	public Hashtable getHashFldAlias(int iSelIndex) {
		Hashtable hashFldAlias = new Hashtable();
		for (int i = 0; i < getTM().getRowCount(); i++) {
			if (i != iSelIndex) {
				//别名-字段
				String fldName = (getTM().getValueAt(i, COL_FLDNAME) == null) ? ""
						: getTM().getValueAt(i, COL_FLDNAME).toString();
				hashFldAlias.put(getTM().getValueAt(i, COL_FLDALIAS), fldName);
			}
		}
		return hashFldAlias;
	}

	/*
	 * 获得父组件
	 */
	public SetColumnPanel getColumnPanel() {
		return m_columnPanel;
	}

	/*
	 * 获得父组件
	 */
	public void setColumnPanel(SetColumnPanel columnPanel) {
		m_columnPanel = columnPanel;
		//设置本实例
		getTablePn().setColumnPanel(columnPanel);
		getTable().setColumnPanel(columnPanel);
		getTable().setHeader(
				new QueryFldTableHeader(getTable().getColumnModel()));
	}

	/**
	 * 刷新查询基本定义
	 */
	public void refreshQbd() {
		//获得查询字段定义
		SelectFldVO[] sfs = getResultFromFld();
		//刷新
		getTabPn().getQueryBaseDef().setSelectFlds(sfs);
	}
}
 