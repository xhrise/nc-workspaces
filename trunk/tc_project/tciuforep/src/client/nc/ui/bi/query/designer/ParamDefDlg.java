package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import nc.ui.bd.def.DefaultDefdocRefModel;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.bd.ref.IBusiType;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.querymodel.BaseRefEditor;
import nc.ui.pub.querymodel.ChooseQmdDlg;
import nc.ui.pub.querymodel.QERefModel;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.querymodel.ParamConst;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 参数定义对话框 创建日期：(2003-8-8 14:41:00)
 * 
 * @author：朱俊彬
 */
public class ParamDefDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//定义数据源
	private String m_defDsName = null;

	//参照枚举项文本域
	private UITextField m_tfRef = null;

	//查询执行数据源
	private String m_dsName = null;

	//常量
	public final static int COL_PARAMCODE = 0;

	public final static int COL_PARAMNAME = 1;

	public final static int COL_OPR = 2;

	public final static int COL_DATATYPE = 3;

	public final static int COL_CONSULTCODE = 4;

	public final static int COL_DEFAULTVALUE = 5;

	public final static int COL_IFMUST = 6;

	public final static int COL_REFDEPEND = 7;

	public final static int COL_IFVISIBLE = 8;

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UIButton ivjBnAdd = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnDel = null;

	private UIButton ivjBnOK = null;

	private UIPanel ivjPnNorth = null;

	private UITablePane ivjTablePn = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == ParamDefDlg.this.getBnAdd())
				connEtoC1(e);
			if (e.getSource() == ParamDefDlg.this.getBnDel())
				connEtoC2(e);
			if (e.getSource() == ParamDefDlg.this.getBnCancel())
				connEtoC3(e);
			if (e.getSource() == ParamDefDlg.this.getBnOK())
				connEtoC4(e);
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == ParamDefDlg.this.getPnSouth())
				connEtoC5(e);
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
	 * ParamDefDlg 构造子注解。
	 * @deprecated
	 */
	public ParamDefDlg() {
		super();
		initialize();
	}

	/**
	 * ParamDefDlg 构造子注解。
	 * 
	 * @param parent
	 *            Container
	 */
	public ParamDefDlg(Container parent, String defDsName) {
		super(parent);
		m_defDsName = defDsName;
		initialize();
	}

	/**
	 * 增行 创建日期：(2003-8-8 15:14:24)
	 */
	private void addLine() {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//界面加行
		int iIndex = getTM().getRowCount();
		Object[] objrows = new Object[] { "", "", "",
				new ParamConst().DATATYPE_NOTE[0], "", "", new Boolean(false),
				"", new Boolean(false) };
		getTM().addRow(objrows);
		//选中新增行
		getTable().getSelectionModel().setSelectionInterval(iIndex, iIndex);
	}

	/**
	 * 增加参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void addParamVOs(ParamVO[] params) {
		int iLen = (params == null) ? 0 : params.length;

		ParamConst paramConst = new ParamConst();
		//增加数组
		for (int i = 0; i < iLen; i++) {
			//类型
			String strDataType = "";
			if (params[i].getDataType() == null)
				strDataType = paramConst.DATATYPE_NOTE[0];
			else
				strDataType = paramConst.DATATYPE_NOTE[params[i].getDataType()
						.intValue()];
			//增行
			Object[] objrows = new Object[] { params[i].getParamCode(),
					params[i].getParamName(), params[i].getOperaCode(),
					strDataType, params[i].getConsultCode(),
					params[i].getValue(),
					new Boolean(params[i].getIfMust().booleanValue()),
					params[i].getRefDepend(),
					new Boolean(params[i].isInvisible()) };
			getTM().addRow(objrows);
		}
	}

	/**
	 * 停止编辑后，设置参照结果和显示值 创建日期：(2001-7-31 13:02:41)
	 * 
	 * @param value
	 *            java.lang.Object
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterEdit(TableCellEditor editor, int row, int col) {

		Object value = editor.getCellEditorValue();
		getTable().setValueAt(value, row, col);

		if ((editor instanceof UIRefCellEditor)) {
			Object temp = ((UIRefCellEditor) editor).getComponent();
			if (temp instanceof UIRefPane) {
				UIRefPane pane = (UIRefPane) temp;
				//数据类型
				String strDataType = getTable().getValueAt(row, COL_DATATYPE)
						.toString();
				//参照返回值类型
				int iReturnType = 0;
				if (strDataType
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001150")/* @res "编码参照" */))
					iReturnType = 1;
				else if (strDataType
						.equals(nc.ui.ml.NCLangRes.getInstance().getStrByID(
								"10241201", "UPP10241201-001151")/* @res "名称参照" */))
					iReturnType = 2;
				//设置输入值
				String strOpr = getTable().getValueAt(row, 2).toString().trim();
				if (strOpr.equals("like")) {
					String strResult = getRefResult(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable().setValueAt(strResult + "%", row, col);
				} else if (strOpr.equals("in")) {
					String strResult = getRefResult_in(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable().setValueAt(strResult, row, col);
				} else {
					String strResult = getRefResult(pane, iReturnType);
					if (strResult != null && !strResult.equals(""))
						getTable().setValueAt(strResult, row, col);
				}
			}
		}
	}

	/**
	 * 增行
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		addLine();
	}

	/**
	 * 取消
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * 删行
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		delLine();
	}

	/**
	 * 确定
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, strErr);
			return;
		}
		closeOK();
	}

	/**
	 * 合法性校验 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public String check() {
		Hashtable<String, String> hashParamCode = new Hashtable<String, String>();
		int iLen = getTM().getRowCount();
		for (int i = 0; i < iLen; i++) {
			String paramCode = (getTM().getValueAt(i, COL_PARAMCODE) == null) ? ""
					: getTM().getValueAt(i, COL_PARAMCODE).toString().trim();
			if (paramCode.equals(""))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001152", null,
						new String[] { "" + (i + 1) })/* @res "第{0}行参数名为空" */;
			String paramName = (getTM().getValueAt(i, COL_PARAMNAME) == null) ? ""
					: getTM().getValueAt(i, COL_PARAMNAME).toString().trim();
			if (paramName.equals(""))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001153", null,
						new String[] { "" + (i + 1) })/* @res "第{0}行显示名为空" */;
			if (hashParamCode.containsKey(paramCode))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001154")/* @res "参数名不能重复：" */
						+ paramCode;
			else
				hashParamCode.put(paramCode, paramName);
			//参照依赖
			String paramType = (getTM().getValueAt(i, COL_DATATYPE) == null) ? ""
					: getTM().getValueAt(i, COL_DATATYPE).toString();
			String refDepend = (getTM().getValueAt(i, COL_REFDEPEND) == null) ? ""
					: getTM().getValueAt(i, COL_REFDEPEND).toString();
			if (!refDepend.equals("")
					&& !paramType
							.endsWith(nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("10241201",
											"UPP10241201-000056")/* @res "参照" */))
				return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001155")/* @res "参照型参数才需要设置参照依赖条件" */;
		}
		return null;
	}

	/**
	 * connEtoC1: (BnAdd.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnAdd_ActionPerformed(LActionEvent;)V)
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
	 * ParamDefDlg.bnDel_ActionPerformed(LActionEvent;)V)
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
	 * connEtoC3: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(ActionEvent arg1) {
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
	 * connEtoC4: (BnOK.action.actionPerformed(ActionEvent) -->
	 * ParamDefDlg.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnOK_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (PnSouth.mouse.mouseClicked(MouseEvent) -->
	 * ParamDefDlg.pnSouth_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(MouseEvent arg1) {
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
	 * 删行 创建日期：(2003-8-8 15:14:24)
	 */
	private void delLine() {
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
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
	 * 设置默认值列参照模型 处理参照依赖 创建日期：(2004-4-28 15:55:07)
	 */
	private void doRefDepend(ParamVO[] params, TableCellEditor editor, int row,
			int column) {

		try {
			UIRefCellEditor refCellEditor = (UIRefCellEditor) editor;
			//替换依赖
			boolean bRefParam = getTable().getValueAt(row, COL_DATATYPE)
					.toString().endsWith(
							nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"10241201", "UPP10241201-000056")/*
																	  * @res
																	  * "参照"
																	  */);
			boolean bUserCond = false;
			String strDepend = null;
			if (bRefParam) {
				//获得依赖关系
				strDepend = params[row].getRefDepend();
				if (strDepend != null && !strDepend.trim().equals("")) {
					bUserCond = true;
					//根据参数依赖动态替换过滤条件
					int[][] iRefs = BIModelUtil.getParamRefDepends(params);
					int iLen = (iRefs == null) ? 0 : iRefs.length;
					for (int i = 0; i < iLen; i++) {
						if (iRefs[i][0] == row) {
							//获得依赖行
							int iDependedRow = iRefs[i][1];
							//获得依赖值
							Object obj = getTable().getValueAt(iDependedRow,
									COL_DEFAULTVALUE);
							String strDependValue = (obj == null) ? "" : obj
									.toString();
							if (strDependValue.equals(""))
								continue;
							String strDependParam = getTable().getValueAt(
									iDependedRow, COL_PARAMCODE).toString();
							//替换依赖关系
							strDepend = StringUtil.replaceAllString(strDepend,
									"[" + strDependParam + "]", strDependValue);
						}
					}
				}
			}
			//获得参照面板
			UIRefPane refPn = (UIRefPane) refCellEditor.getComponent();
			refPn.getUITextField().setSingleQuoteInputEnabled(true);
			if (!bRefParam)
				refPn.setButtonVisible(false);
			else {
				refPn.setButtonVisible(true);
				//枚举项
				String strConsult = params[row].getConsultCode();
				//比较符
				String strOpr = params[row].getOperaCode();
				//多选
				refPn.setMultiSelectedEnabled(strOpr.equals("in"));
				//自检
				//refPn.setAutoCheck(!strOpr.equals("like") &&
				// !strOpr.equals("in"));
				refPn.setAutoCheck(false);
				//最大长度
				refPn.setMaxLength(200);
				//不用缓存
				refPn.setCacheEnabled(false);
				//动态构造参照模型
				AbstractRefModel rm = procRefModel(strConsult, strOpr);
				if (rm == null) {
					//系统参照
					refPn.setRefNodeName(strConsult);
					rm = refPn.getRefModel();
				} else {
					//非末级不可选（绕过宋学军的陷阱）
					refPn.setNotLeafSelectedEnabled(rm
							.isNotLeafSelectedEnabled());
					//自定义参照
					refPn.setRefModel(rm);
				}
				//设置数据源
				rm.setDataSource(m_dsName);
				if (bUserCond) {
					//rm.setUseDataPower(false);
					//rm.setWherePart(rm.getWherePart() + " and " + strDepend);
					AppDebug.debug("替换后条件：" + strDepend);//@devTools System.out.println("替换后条件：" + strDepend);
					String[] subConds = strDepend.split(";");//数组长度至少为1
					if (subConds[0].trim().length() > 0) {
						rm.setWherePart(subConds[0]);
					}
					//树参照有可能需要调用setClassWherePart条件
					if (subConds.length > 1 && subConds[1].trim().length() > 0
							&& rm instanceof AbstractRefTreeModel) {
						((AbstractRefTreeModel) rm)
								.setClassWherePart(subConds[1]);
					}

					//强制刷新（难道没有别的方法？）
					rm.reloadData();
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/**
	 * 返回 BnAdd 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new nc.ui.pub.beans.UIButton();
				ivjBnAdd.setName("BnAdd");
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000648")/* @res "增行" */);
				// user code begin {1}
				ivjBnAdd.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnAdd, ivjBnAdd.getText());
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
								"10241201", "UPP10241201-000000")/* @res "取消" */);
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
	 * 返回 BnDel 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnDel() {
		if (ivjBnDel == null) {
			try {
				ivjBnDel = new nc.ui.pub.beans.UIButton();
				ivjBnDel.setName("BnDel");
				ivjBnDel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000649")/* @res "删行" */);
				// user code begin {1}
				ivjBnDel.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnDel, ivjBnDel.getText());
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
	 * 返回 BnOK 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnOK() {
		if (ivjBnOK == null) {
			try {
				ivjBnOK = new nc.ui.pub.beans.UIButton();
				ivjBnOK.setName("BnOK");
				ivjBnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000001")/* @res "确定" */);
				// user code begin {1}
				ivjBnOK.setPreferredSize(new Dimension(90, 22));
				UIUtil.autoSizeComp(ivjBnOK, ivjBnOK.getText());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnOK;
	}

	/**
	 * 获得参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public ParamVO[] getParamVOs() {
		ParamVO[] params = null;
		int iLen = getTM().getRowCount();
		if (iLen != 0) {
			params = new ParamVO[iLen];
			for (int i = 0; i < iLen; i++)
				params[i] = getSelParamVO(i);
		}
		return params;
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
				ivjPnNorth.setPreferredSize(new Dimension(10, 5));
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
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new nc.ui.pub.beans.UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setLayout(getPnSouthFlowLayout());
				getPnSouth().add(getBnAdd(), getBnAdd().getName());
				getPnSouth().add(getBnDel(), getBnDel().getName());
				getPnSouth().add(getBnOK(), getBnOK().getName());
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
	 * @return FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private FlowLayout getPnSouthFlowLayout() {
		FlowLayout ivjPnSouthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnSouthFlowLayout = new FlowLayout();
			ivjPnSouthFlowLayout.setVgap(8);
			ivjPnSouthFlowLayout.setHgap(20);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * 根据返回值类型获得参照返回值 创建日期：(02-3-19 17:29:56)
	 * 
	 * @return java.lang.String
	 * @param refPn
	 *            nc.ui.pub.beans.UIRefPane
	 * @param iReturnType
	 *            int
	 */
	private String getRefResult(UIRefPane refPn, int iReturnType) {
		String strResult = null;
		switch (iReturnType) {
		case 0: {
			strResult = refPn.getRefPK();
			break;
		}
		case 1: {
			strResult = refPn.getRefCode();
			break;
		}
		case 2: {
			strResult = refPn.getRefName();
			break;
		}
		}
		return strResult;
	}

	/**
	 * 根据返回值类型获得多选参照返回值（用于in） 创建日期：(02-3-19 17:29:56)
	 * 
	 * @return java.lang.String
	 * @param refPn
	 *            nc.ui.pub.beans.UIRefPane
	 * @param iReturnType
	 *            int
	 */
	private String getRefResult_in(UIRefPane refPn, int iReturnType) {
		String[] strResults = null;
		switch (iReturnType) {
		case 0: {
			strResults = refPn.getRefPKs();
			break;
		}
		case 1: {
			strResults = refPn.getRefCodes();
			break;
		}
		case 2: {
			strResults = refPn.getRefNames();
			break;
		}
		}
		String strInResult = null;
		if (strResults != null) {
			strInResult = "(";
			for (int i = 0; i < strResults.length; i++) {
				strInResult += "'" + strResults[i] + "'";
				if (i < strResults.length - 1)
					strInResult += ",";
			}
			strInResult += ")";
		}
		return strInResult;
	}

	/**
	 * 获得选中的参数VO 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public ParamVO getSelParamVO() {
		int iSelIndex = getTable().getSelectedRow();
		return getSelParamVO(iSelIndex);
	}

	/**
	 * 获得选中的参数VO 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	private ParamVO getSelParamVO(int iSelIndex) {
		ParamVO param = null;
		if (iSelIndex != -1) {
			param = new ParamVO();
			//获得属性
			param.setParamCode(getTM().getValueAt(iSelIndex, COL_PARAMCODE)
					.toString());
			param.setParamName(getTM().getValueAt(iSelIndex, COL_PARAMNAME)
					.toString());
			param.setOperaCode(getTM().getValueAt(iSelIndex, COL_OPR)
					.toString());
			param.setValue(getTM().getValueAt(iSelIndex, COL_DEFAULTVALUE)
					.toString());

			//数据类型
			String strDataType = getTM().getValueAt(iSelIndex, COL_DATATYPE)
					.toString();
			int iDataType = ParamConst.getIndex(strDataType);
			param.setDataType(new Integer(iDataType));

			param.setConsultCode(getTM().getValueAt(iSelIndex, COL_CONSULTCODE)
					.toString());
			Object obj = getTM().getValueAt(iSelIndex, COL_IFMUST);
			if (obj == null)
				param.setIfMust(UFBoolean.valueOf(false));
			else
				param.setIfMust(UFBoolean.valueOf(obj.toString()));
			obj = getTM().getValueAt(iSelIndex, COL_REFDEPEND);
			param.setRefDepend((obj == null) ? "" : obj.toString());
			obj = getTM().getValueAt(iSelIndex, COL_IFVISIBLE);
			if (obj == null || obj.toString().equals(""))
				param.setInvisible(false);
			else
				param.setInvisible(((Boolean) obj).booleanValue());
		}
		return param;
	}

	/**
	 * 获得表格 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public UITable getTable() {
		return (UITable) getTablePn().getTable();
	}

	/**
	 * 返回 TablePn 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	@SuppressWarnings("serial")
	private nc.ui.pub.beans.UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new nc.ui.pub.beans.UITablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}

				UITable table = new UITable() {
					public Component prepareEditor(TableCellEditor editor,
							int row, int column) {
						Component comp = super.prepareEditor(editor, row,
								column);
						if (column == COL_DEFAULTVALUE) {
							//处理参照依赖
							doRefDepend(getParamVOs(), editor, row, column);
						}
						return comp;
					}

					public void editingStopped(ChangeEvent e) {
						int row = editingRow;
						int col = editingColumn;
						TableCellEditor editor = getCellEditor();
						//结束编辑状态
						if (editor != null && row != -1) {
							//获得旧值
							Object obj = getValueAt(row, col);
							String oldValue = (obj == null) ? "" : obj
									.toString();
							//填入新值
							removeEditor();
							if (col == COL_DEFAULTVALUE)
								afterEdit(editor, row, col);
							else {
								setValueAt(editor.getCellEditorValue(), row,
										col);
								if (col == COL_DATATYPE) {
									String strType = getValueAt(row,
											COL_DATATYPE).toString();
									if (strType.endsWith(nc.ui.ml.NCLangRes
											.getInstance().getStrByID(
													"10241201",
													"UPP10241201-000056")/*
																		  * @res
																		  * "参照"
																		  */)
											&& !oldValue
													.endsWith(nc.ui.ml.NCLangRes
															.getInstance()
															.getStrByID(
																	"10241201",
																	"UPP10241201-000056")/*
																						  * @res
																						  * "参照"
																						  */)) {
										setValueAt(IBusiType.BUSITYPE[0], row,
												COL_CONSULTCODE);
										setValueAt("", row, COL_DEFAULTVALUE);
									}
									//else
									//if (!strType.endsWith("参照") &&
									// oldValue.endsWith("参照"))
									//setValueAt("", row, COL_CONSULTCODE);
								} else if (col == COL_CONSULTCODE) {
									String strConsult = getValueAt(row,
											COL_CONSULTCODE).toString();
									if (!strConsult.equals(oldValue))
										setValueAt("", row, COL_DEFAULTVALUE);
								}
							}
						}
					}
				};
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
	 * 获得表模型 创建日期：(2003-4-2 13:48:52)
	 * 
	 * @return nc.ui.pub.beans.UITable
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
	}

	/**
	 * 返回 UIDialogContentPane 特性值。
	 * 
	 * @return JPanel
	 */
	/* 警告：此方法将重新生成。 */
	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			try {
				ivjUIDialogContentPane = new UIPanel();
				ivjUIDialogContentPane.setName("UIDialogContentPane");
				ivjUIDialogContentPane.setLayout(new BorderLayout());
				getUIDialogContentPane().add(getPnSouth(), "South");
				getUIDialogContentPane().add(getPnNorth(), "North");
				getUIDialogContentPane().add(getTablePn(), "Center");
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
		getBnAdd().addActionListener(ivjEventHandler);
		getBnDel().addActionListener(ivjEventHandler);
		getBnCancel().addActionListener(ivjEventHandler);
		getBnOK().addActionListener(ivjEventHandler);
		getPnSouth().addMouseListener(ivjEventHandler);
	}

	/**
	 * 设置列编辑器 创建日期：(2003-9-17 14:50:41)
	 */
	public void initEditorValue() {
		//数据类型列编辑器
		JComboBox cbbOperator = new UIComboBox(new ParamConst().DATATYPE_NOTE);
		TableColumn tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UC000-0002279")/* @res "数据类型" */);
		tc.setCellEditor(new UIRefCellEditor(cbbOperator));
		//枚举项列编辑器
		m_tfRef = new UITextField("");
		//m_tfRef.addKeyListener(this);
		tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001156")/* @res "枚举项" */);
		//tc.setCellEditor(new UIRefCellEditor(m_tfRef));
		BaseRefEditor editorandRenderer = new BaseRefEditor(m_tfRef);
		tc.setCellEditor(editorandRenderer);
		tc.setCellRenderer(editorandRenderer);
		//默认值
		tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001159")/* @res "默认值" */);
		tc.setCellEditor(new UIRefCellEditor(new UIRefPane()));
		//参照依赖
		tc = getTable().getColumn(
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001161")/* @res "参照依赖" */);
		tc.setCellEditor(new DefaultCellEditor(new UITextField()));
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("ParamDefDlg");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setSize(640, 360);
			setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-001157")/* @res "参数定义" */);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initTable();
		initEditorValue();
		// user code end
	}

	/**
	 * 初始化表格 创建日期：(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable() {
		//表模型
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000974")/* @res "参数名" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000893")/* @res "显示名" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001158")/* @res "操作符提示" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UC000-0002279")/* @res "数据类型" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001156")/* @res "枚举项" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001159")/* @res "默认值" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001160")/* @res "是否必选" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001161")/* @res "参照依赖" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-001162") /* @res "是否隐藏" */}, 0) {
			public int getColumnCount() {
				return 9;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				if (col == COL_IFMUST || col == COL_IFVISIBLE)
					return Boolean.class;
				else
					return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				return true;
			}
		};
		getTable().setModel(tm);
		//设置表属性
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(
				new int[] { 80, 100, 88, 100, 88, 172, 80, 160, 80 });
		UIUtil.autoSizeTableColumn(getTable());
		getTable().sizeColumnsToFit(-1);
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * 南部面板点击响应
	 */
	public void pnSouth_MouseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.isAltDown()) {
			//显示参照依赖报告
			int[][] iRefDepends = BIModelUtil.getParamRefDepends(getParamVOs());
			//构造报告
			StringBuffer strBuf = new StringBuffer();
			int iLen = (iRefDepends == null) ? 0 : iRefDepends.length;
			for (int i = 0; i < iLen; i++) {
				int iParam1 = iRefDepends[i][0] + 1;
				int iParam2 = iRefDepends[i][1] + 1;
				strBuf.append(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-001163", null,
						new String[] { "" + iParam1, "" + iParam2 })/*
																	 * @res
																	 * "参数{0}依赖于参数{1}"
																	 */);
				if (i < iLen - 1)
					strBuf.append(",");
			}
			MessageDialog.showHintDlg(this,
					nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-001164")/* @res "参照依赖报告" */, strBuf
							.toString());
		} else {
			//引用参数
			ChooseQmdDlg dlg = new ChooseQmdDlg(this);
			dlg.initTree(m_defDsName);
			dlg.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-001165")/* @res "引用参数" */);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				//获得选中QMD
				QueryModelDef qmd = dlg.getSelQmd();
				//添加引用参数
				addParamVOs(qmd.getParamVOs());
			}
		}
		return;
	}

	/**
	 * 生成参照模型 创建日期：(2003-9-17 15:24:54)
	 * @i18n miufo00307=自定义项参照
	 * @i18n miufo00308=一般自定义参照
	 */
	private AbstractRefModel procRefModel(String strConsult, String strOpr) {

		AbstractRefModel rm = null;
		try {
			if (strConsult.toLowerCase().startsWith("<html>"))
				return null;
			int iBeginIndex = strConsult.indexOf("<");
			int iEndIndex = strConsult.indexOf(">");
			if (iBeginIndex != -1 && iEndIndex != -1) {
				//自定义参照
				String className = strConsult.substring(iBeginIndex + 1,
						iEndIndex);
				//处理
				int iLen = strConsult.length();
				if (iEndIndex != iLen - 1) {
					AppDebug.debug("自定义项参照");//@devTools System.out.println("自定义项参照");
					//自定义项参照
					String pkDefDef = strConsult.substring(iEndIndex + 1, iLen);
					rm = (DefaultDefdocRefModel) Class.forName(className)
							.newInstance();
					((DefaultDefdocRefModel) rm).setPkdefdef(pkDefDef);
				} else {
					AppDebug.debug("一般自定义参照");//@devTools System.out.println("一般自定义参照");
					//一般自定义参照
					rm = (AbstractRefModel) Class.forName(className)
							.newInstance();
				}
			} else {
				iBeginIndex = strConsult.indexOf("[");
				iEndIndex = strConsult.indexOf("]");
				if (iBeginIndex != -1 && iEndIndex != -1) {
					//查询制导参照
					String info = strConsult.substring(iBeginIndex + 1,
							iEndIndex);
					//取出查询ID，参照标题，主键序号，编码序号，名称序号
					StringTokenizer st = new StringTokenizer(info, ",");
					String queryId = st.nextToken();
					String refTitle = st.nextToken();
					String idIndex = st.nextToken();
					String codeIndex = st.nextToken();
					String nameIndex = st.nextToken();
					//
					String[] strRefInfos = new String[] { queryId, m_defDsName,
							refTitle };
					int[] iFldIndices = new int[] { Integer.parseInt(idIndex),
							Integer.parseInt(codeIndex),
							Integer.parseInt(nameIndex) };
					//设置查询制导参照模型
					rm = new QERefModel();
					((QERefModel) rm).setRefInfo(strRefInfos, iFldIndices);
					((QERefModel) rm).setUseDataPower(false);
				}
			}
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return rm;
	}

	/**
	 * 设置参数VO数组 创建日期：(2003-8-8 14:57:25)
	 * 
	 * @param params
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public void setParamVOs(ParamVO[] params, String dsName) {
		getTM().setNumRows(0);
		addParamVOs(params);
		m_dsName = dsName;
	}

	/**
	 * 设置居中对齐 创建日期：(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		for (int i = 0; i < getTable().getColumnCount(); i++) {
			TableColumn tc = getTable().getColumnModel().getColumn(i);
			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
	}
}   