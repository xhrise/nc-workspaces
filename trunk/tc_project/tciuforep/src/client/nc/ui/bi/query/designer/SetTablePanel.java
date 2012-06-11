package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.pub.ValueObject;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 表设置界面 创建日期：(2005-5-13 16:56:34)
 * 
 * @author：朱俊彬
 */
public class SetTablePanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0059";//"数据表";

	//选择表对话框实例
	private TableInfoDlg m_tableInfoDlg = null;

	//选择临时表对话框实例
	private TempTableInfoDlg m_tempTableInfoDlg = null;

	//常量
	public final static int COL_TABLECODE = 1;

	public final static int COL_TABLENAME = 2;

	public final static int COL_TABLETYPE = 3;

	public final static int COL_TABLEALIAS = 4;

	private UIPanel ivjPnEast = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	private UIPanel ivjPnWest = null;

	private UITablePane ivjTablePn = null;

	private UIButton ivjBnAdd = null;

	private UIPanel ivjPnEast1 = null;

	private UIPanel ivjPnEast2 = null;

	private UIButton ivjBnAddQuery = null;

	private UIPanel ivjPnEast3 = null;

	private UIButton ivjBnAddDel = null;

	private UIButton ivjBnDown = null;

	private UIButton ivjBnUp = null;

	private UIPanel ivjPnEast4 = null;

	private UIPanel ivjPnEast5 = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	
//	 alias-note哈希表 5.01
	 private Hashtable<String, String> m_hashAliasNote = new Hashtable<String, String>();

	class IvjEventHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == SetTablePanel.this.getBnAddQuery())
				connEtoC3(e);
			if (e.getSource() == SetTablePanel.this.getBnAdd())
				connEtoC1(e);
			if (e.getSource() == SetTablePanel.this.getBnAddDel())
				connEtoC2(e);
			if (e.getSource() == SetTablePanel.this.getBnUp())
				connEtoC4(e);
			if (e.getSource() == SetTablePanel.this.getBnDown())
				connEtoC5(e);
		};
	};

	/**
	 * SetTablePanel 构造子注解。
	 */
	public SetTablePanel() {
		super();
		initialize();
	}

	/**
	 * add
	 */
	@SuppressWarnings("unchecked")
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		TableInfoDlg dlg = getTableInfoDlg();
		//传入用于判断重复的哈希表
		dlg.setHashTableId(getHashTableId());
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//获得选中表
			FromTableVO ft = dlg.getSelTableVO();
			//增加处理
			doAdd(ft);
		}
		return;
	}

	/**
	 * 增加查询
	 */
	public void bnAddQuery_ActionPerformed(ActionEvent actionEvent) {
		//获得查询设置的数据源
		String dsName = getTabPn().getDefDsName();
		
		//弹框
		TempTableInfoDlg dlg = getTempTableInfoDlg();
		dlg.initTree(dsName);
		//用于判断重复的哈希表
		Hashtable<Object, Object> hashTableId = new Hashtable<Object, Object>();
		for (int i = 0; i < getTM().getRowCount(); i++)
			hashTableId.put(getTM().getValueAt(i, COL_TABLECODE), getTM()
					.getValueAt(i, COL_TABLENAME));
		dlg.setHashTableId(hashTableId);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//获得选中表
			BIQueryModelDef qmd = dlg.getSelTableDef();
			//增加处理
			doAdd(qmd.getBaseModel());
		}
		return;
	}

	/**
	 * del
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//合法性检查

			//界面刷新
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);

			/* 以下为一致性处理 */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//合并VO数组
			ValueObject[] vos = BIModelUtil.delFromVOs(qbd.getFromTables(),
					iSelIndex);
			FromTableVO[] newfts = new FromTableVO[vos.length];
			for (int i = 0; i < vos.length; i++)
				newfts[i] = (FromTableVO) vos[i];
			qbd.setFromTables(newfts);

			//更新字段生成框
			((SetFldPanel) getTabPn().getSetPanel(SetFldPanel.TAB_TITLE))
					.setFldGenDlgAsNull();
			((SetJoinPanel) getTabPn().getSetPanel(SetJoinPanel.TAB_TITLE))
					.setFldGenDlgAsNull();
			//getTabPn().setRefDlgAsNull();
			//初始化where条件和连接条件表格单元编辑器
			((SetCondPanel) getTabPn().getSetPanel(SetCondPanel.TAB_TITLE))
					.initEditorValue();
			((SetJoinPanel) getTabPn().getSetPanel(SetJoinPanel.TAB_TITLE))
					.initEditorValue();
		}
		return;
	}

	/**
	 * down
	 */
	public void bnDown_ActionPerformed(ActionEvent actionEvent) {
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1 && iSelIndex < getTable().getRowCount() - 1) {
			//移行
			getTM().moveRow(iSelIndex, iSelIndex, iSelIndex + 1);
			//选中
			getTable().getSelectionModel().setSelectionInterval(iSelIndex + 1,
					iSelIndex + 1);

			/* 以下为一致性处理 */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//合并VO数组
			FromTableVO[] oldfts = qbd.getFromTables();
			FromTableVO[] newfts = new FromTableVO[oldfts.length];
			for (int i = 0; i < oldfts.length; i++)
				if (i == iSelIndex)
					newfts[i] = oldfts[iSelIndex + 1];
				else if (i == iSelIndex + 1)
					newfts[i] = oldfts[iSelIndex];
				else
					newfts[i] = oldfts[i];
			qbd.setFromTables(newfts);
		}
	}

	/**
	 * up
	 */
	public void bnUp_ActionPerformed(ActionEvent actionEvent) {
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex > 0) {
			//移行
			getTM().moveRow(iSelIndex, iSelIndex, iSelIndex - 1);
			//选中
			getTable().getSelectionModel().setSelectionInterval(iSelIndex - 1,
					iSelIndex - 1);

			/* 以下为一致性处理 */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//合并VO数组
			FromTableVO[] oldfts = qbd.getFromTables();
			FromTableVO[] newfts = new FromTableVO[oldfts.length];
			for (int i = 0; i < oldfts.length; i++)
				if (i == iSelIndex - 1)
					newfts[i] = oldfts[iSelIndex];
				else if (i == iSelIndex)
					newfts[i] = oldfts[iSelIndex - 1];
				else
					newfts[i] = oldfts[i];
			qbd.setFromTables(newfts);
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
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000918")/* @res "没有选择表" */;
		for (int i = 0; i < iLen; i++) {
			if (getTM().getValueAt(i, 2) == null
					|| getTM().getValueAt(i, 2).toString().trim().equals(""))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000919")/* @res "表显示名不能为空" */;
			if (getTM().getValueAt(i, 4) == null
					|| getTM().getValueAt(i, 4).toString().trim().equals(""))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000920")/* @res "表别名不能为空" */;
		}
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
	 * connEtoC3: (BnAddQuery.action.actionPerformed(ActionEvent) -->
	 * SetTablePanel.bnAddQuery_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnAddQuery_ActionPerformed(arg1);
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
	 * SetTablePanel.bnUp_ActionPerformed(LActionEvent;)V)
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
	 * SetTablePanel.bnDown_ActionPerformed(LActionEvent;)V)
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
	 * 增加表处理 创建日期：(2003-4-4 13:45:23)
	 */
	public void doAdd(FromTableVO ft) {
		//获得表信息
		String tableCode = ft.getTablecode();
		String tableName = ft.getTabledisname();
		String tableAlias = ft.getTablealias();
		  // 记录alias-note哈希表 5.01
		if (ft.getNote() != null) {
			m_hashAliasNote.put(tableAlias, ft.getNote());
		}

		//类型
		boolean bPersistent = BIModelUtil.isPersistentTable(tableCode);
		String tableType = bPersistent ? NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000921")/* @res "物化临时表" */
		: NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000922")/*
																			   * @res
																			   * "数据表"
																			   */;
		//添加表格
		getTM()
				.addRow(
						new Object[] { "", tableCode, tableName, tableType,
								tableAlias });
		//选中新增行
		getTable().getSelectionModel().setSelectionInterval(
				getTM().getRowCount() - 1, getTM().getRowCount() - 1);

		/* 以下为一致性处理 */
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//合并VO数组
		ValueObject[] vos = BIModelUtil.addToVOs(qbd.getFromTables(), ft);
		FromTableVO[] newfts = new FromTableVO[vos.length];
		for (int i = 0; i < vos.length; i++)
			newfts[i] = (FromTableVO) vos[i];
		qbd.setFromTables(newfts);

		//更新字段生成框
		((SetFldPanel) getTabPn().getSetPanel(SetFldPanel.TAB_TITLE))
				.setFldGenDlgAsNull();
		((SetJoinPanel) getTabPn().getSetPanel(SetJoinPanel.TAB_TITLE))
				.setFldGenDlgAsNull();
		//getTabPn().setRefDlgAsNull();
		//初始化where条件和连接条件表格单元编辑器
		((SetCondPanel) getTabPn().getSetPanel(SetCondPanel.TAB_TITLE))
				.initEditorValue();
		((SetJoinPanel) getTabPn().getSetPanel(SetJoinPanel.TAB_TITLE))
				.initEditorValue();
	}

	/**
	 * 增加临时表处理 创建日期：(2003-4-4 13:45:23)
	 */
	public void doAdd(QueryModelDef qmd) {
		String id = QueryConst.TEMP_PREFIX + qmd.getID();
		//添加表格
		getTM().addRow(
				new Object[] {
						"",
						id,
						qmd.getDisplayName(),
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000923")/* @res "普通临时表" */, id });
		//选中新增行
		getTable().getSelectionModel().setSelectionInterval(
				getTM().getRowCount() - 1, getTM().getRowCount() - 1);

		/* 以下为一致性处理 */
		//转换为VO
		FromTableVO ft = new FromTableVO();
		ft.setTablecode(id);
		ft.setTabledisname(qmd.getDisplayName());
		//更新QueryBaseDef
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//合并VO数组
		ValueObject[] vos = BIModelUtil.addToVOs(qbd.getFromTables(), ft);
		FromTableVO[] newfts = new FromTableVO[vos.length];
		for (int i = 0; i < vos.length; i++)
			newfts[i] = (FromTableVO) vos[i];
		qbd.setFromTables(newfts);

		//更新字段生成框
		((SetFldPanel) getTabPn().getSetPanel(SetFldPanel.TAB_TITLE))
				.setFldGenDlgAsNull();
		((SetJoinPanel) getTabPn().getSetPanel(SetJoinPanel.TAB_TITLE))
				.setFldGenDlgAsNull();
		//getTabPn().setRefDlgAsNull();
		//初始化where条件和连接条件表格单元编辑器
		((SetCondPanel) getTabPn().getSetPanel(SetCondPanel.TAB_TITLE))
				.initEditorValue();
		((SetJoinPanel) getTabPn().getSetPanel(SetJoinPanel.TAB_TITLE))
				.initEditorValue();
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
						"UPP10241201-000924")/* @res "增加表" */);
				ivjBnAdd.setMaximumSize(new Dimension(80, 22));
				ivjBnAdd.setPreferredSize(new Dimension(80, 22));
//				ivjBnAdd.setFont(new Font("dialog", 0, 12));
				ivjBnAdd.setMinimumSize(new Dimension(80, 22));
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
	 * 返回 BnAddQuery1 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnAddDel() {
		if (ivjBnAddDel == null) {
			try {
				ivjBnAddDel = new UIButton();
				ivjBnAddDel.setName("BnAddDel");
				ivjBnAddDel.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000925")/* @res "删除表" */);
				ivjBnAddDel.setMaximumSize(new Dimension(80, 22));
				ivjBnAddDel.setPreferredSize(new Dimension(80, 22));
//				ivjBnAddDel.setFont(new Font("dialog", 0, 12));
				ivjBnAddDel.setMinimumSize(new Dimension(80, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddDel;
	}

	/**
	 * 返回 BnAddQuery 特性值。
	 * 
	 * @return UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private UIButton getBnAddQuery() {
		if (ivjBnAddQuery == null) {
			try {
				ivjBnAddQuery = new UIButton();
				ivjBnAddQuery.setName("BnAddQuery");
				ivjBnAddQuery.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000926")/* @res "临时表" */);
				ivjBnAddQuery.setMaximumSize(new Dimension(80, 22));
				ivjBnAddQuery.setPreferredSize(new Dimension(80, 22));
//				ivjBnAddQuery.setFont(new Font("dialog", 0, 12));
				ivjBnAddQuery.setMinimumSize(new Dimension(80, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnAddQuery;
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
				ivjBnDown.setMaximumSize(new Dimension(80, 22));
				ivjBnDown.setPreferredSize(new Dimension(80, 22));
//				ivjBnDown.setFont(new Font("dialog", 0, 12));
				ivjBnDown.setMinimumSize(new Dimension(80, 22));
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
				ivjBnUp.setMaximumSize(new Dimension(80, 22));
				ivjBnUp.setPreferredSize(new Dimension(80, 22));
//				ivjBnUp.setFont(new Font("dialog", 0, 12));
				ivjBnUp.setMinimumSize(new Dimension(80, 22));
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
	 * 用于判断重复的哈希表 创建日期：(2003-10-28 9:04:53)
	 */
	public Hashtable getHashTableId() {
		Hashtable<Object, Object> hashTableId = new Hashtable<Object, Object>();
		for (int i = 0; i < getTM().getRowCount(); i++)
			hashTableId.put(getTM().getValueAt(i, COL_TABLEALIAS), getTM()
					.getValueAt(i, COL_TABLENAME));
		return hashTableId;
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
				getPnEast2().add(getBnAddQuery(), getBnAddQuery().getName());
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
	 * 返回 PnEast2 特性值。
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
				getPnEast3().add(getBnAddDel(), getBnAddDel().getName());
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
	 * 获得查询表数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public FromTableVO[] getResultFromTable() {
		int iRowCount = getTable().getRowCount();
		FromTableVO[] fts = new FromTableVO[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			fts[i] = new FromTableVO();
			fts[i].setTablecode(getTM().getValueAt(i, COL_TABLECODE)
							.toString());
			fts[i].setTabledisname(getTM().getValueAt(i, COL_TABLENAME)
					.toString());
			String tableAlias = getTM().getValueAt(i, COL_TABLEALIAS).toString();
			fts[i].setTablealias(tableAlias);
			// 设置note
			if (m_hashAliasNote.containsKey(tableAlias)) {
			    fts[i].setNote(m_hashAliasNote.get(tableAlias).toString());
			}

		}
		return fts;
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
	 * 获得选择表对话框实例 创建日期：(2003-4-2 16:43:02)
	 * 
	 * @return nc.ui.iuforeport.businessquery.TableInfoDlg
	 */
	public TableInfoDlg getTableInfoDlg() {
		if (m_tableInfoDlg == null)
			m_tableInfoDlg = new TableInfoDlg(this);
		return m_tableInfoDlg;
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
	 * 获得选择临时表对话框实例 创建日期：(2003-4-2 16:43:02)
	 * 
	 * @return nc.ui.iuforeport.businessquery.TableInfoDlg
	 */
	public TempTableInfoDlg getTempTableInfoDlg() {
		if (m_tempTableInfoDlg == null) {
			m_tempTableInfoDlg = new TempTableInfoDlg(this);
		}
		return m_tempTableInfoDlg;
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
		getBnAddQuery().addActionListener(ivjEventHandler);
		getBnAdd().addActionListener(ivjEventHandler);
		getBnAddDel().addActionListener(ivjEventHandler);
		getBnUp().addActionListener(ivjEventHandler);
		getBnDown().addActionListener(ivjEventHandler);
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
			add(getTablePn(), "Center");
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initTable();
		setTableCell();
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 204, 204, 116, 180 });
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
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000927")/* @res "表物理名" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000928")/* @res "表显示名" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000929")/* @res "表类型" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000930") /* @res "表别名" */}, 0) {
			public int getColumnCount() {
				return 5;
			}

			public boolean isCellEditable(int row, int col) {
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
	 * 设置查询表数组 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public void setResultToTable(FromTableVO[] fts) {
		int iLen = (fts == null) ? 0 : fts.length;
		for (int i = 0; i < iLen; i++) {
			String tableType = NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000922")/* @res "数据表" */;
			if (BIModelUtil.isTempTable(fts[i].getTablecode()))
				tableType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000923")/* @res "普通临时表" */;
			else if (BIModelUtil.isPersistentTable(fts[i].getTablecode()))
				tableType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000921")/* @res "物化临时表" */;
			//添加行
			Object[] objrows = new Object[] { "", fts[i].getTablecode(),
					fts[i].getTabledisname(), tableType, fts[i].getTablealias() };
			getTM().addRow(objrows);
			 // 记录alias-note哈希表 5.01
			if (fts[i].getNote() != null) {
			    m_hashAliasNote.put(fts[i].getTablealias(), fts[i].getNote());
			}

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

	/**
	 * 获得面板标题 创建日期：(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultsFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//回设
		qmd.getQueryBaseVO().setFromTables(getResultFromTable());
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultsToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setResultToTable(qmd.getQueryBaseVO().getFromTables());
	}

	/**
	 * 刷新查询基本定义
	 */
	public void refreshQbd() {
		//获得筛选条件定义
		FromTableVO[] fts = getResultFromTable();
		//刷新
		getTabPn().getQueryBaseDef().setFromTables(fts);
	}
} 