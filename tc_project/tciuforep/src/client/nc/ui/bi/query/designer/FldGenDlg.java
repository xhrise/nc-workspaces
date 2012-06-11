/*
 * 创建日期 2005-5-24
 *
 */
package nc.ui.bi.query.designer;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.UITree;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelTree;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.DataDictForNode;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 字段表达式生成对话框 创建日期：(2003-4-2 19:24:22)
 * 
 * @author：朱俊彬
 */
public class FldGenDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//查询基本定义
	private QueryBaseDef m_qbd = null;

	//父组件
	private Container m_parent = null;

	//用于判断字段别名重复的哈希表
	private Hashtable<String, String> m_hashFldAlias = null;

	//表对应的字段列表
	private Map<String, SelectFldVO[]> m_hmFlds = new java.util.HashMap<String, SelectFldVO[]>();

	//连接类型下拉框
	private UIComboBox ivjCbbJoinType = null;

	private UIButton ivjBnCancel = null;

	private UIButton ivjBnOK = null;

	private UIPanel ivjPnSouth = null;

	private JPanel ivjUIDialogContentPane = null;

	private UILabel ivjLabelTable = null;

	private UIList ivjListTable = null;

	private UIPanel ivjPnCenter = null;

	private UIPanel ivjPnListTable = null;

	private UIPanel ivjPnListTableNorth = null;

	private UIPanel ivjPnNorth = null;

	private UIScrollPane ivjSclPnListTable = null;

	private UILabel ivjLabelFld = null;

	private UILabel ivjLabelFunc = null;

	private UIList ivjListFld = null;

	private UIPanel ivjPnListFld = null;

	private UIPanel ivjPnListFldNorth = null;

	private UIPanel ivjPnListFunc = null;

	private UIPanel ivjPnListFuncNorth = null;

	private UIScrollPane ivjSclPnListFld = null;

	private UIScrollPane ivjSclPnListFunc = null;

	private UILabel ivjLabelAlias = null;

	private UILabel ivjLabelDisp = null;

	private UILabel ivjLabelOperator = null;

	private UIList ivjListOperator = null;

	private UIPanel ivjPnListOperator = null;

	private UIPanel ivjPnListOperatorNorth = null;

	private UIScrollPane ivjSclPnListOperator = null;

	private UITextField ivjTFAlias = null;

	private UITextField ivjTFDisp = null;

	private UILabel ivjLabelNote = null;

	private UITextField ivjTFNote = null;

	private UILabel ivjLabelExp = null;

	private UITextArea ivjTAExp = null;

	private UIScrollPane ivjSclPnExp = null;

	private UIButton ivjBnClear = null;

	private UIButton ivjBnAdd = null;

	private UITree ivjTreeFunc = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements ActionListener, MouseListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == FldGenDlg.this.getBnCancel())
				connEtoC1(e);
			if (e.getSource() == FldGenDlg.this.getBnOK())
				connEtoC2(e);
			if (e.getSource() == FldGenDlg.this.getBnClear())
				connEtoC7(e);
			if (e.getSource() == FldGenDlg.this.getBnAdd())
				connEtoC8(e);
		};

		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == FldGenDlg.this.getListTable())
				connEtoC3(e);
			if (e.getSource() == FldGenDlg.this.getListFld())
				connEtoC4(e);
			if (e.getSource() == FldGenDlg.this.getListOperator())
				connEtoC6(e);
			if (e.getSource() == FldGenDlg.this.getTreeFunc())
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
	 * FldGenDlg 构造子注解。
	 * @deprecateds
	 */
	public FldGenDlg() {
		super();
		initialize();
	}

	/**
	 * FldGenDlg 构造子注解。
	 * 
	 * @param parent
	 *            java.awt.Container
	 */
	public FldGenDlg(java.awt.Container parent) {
		super(parent);
		if (parent instanceof SetFldPanel)
			m_parent = (SetFldPanel) parent;
		else if (parent instanceof SetCondPanel)
			m_parent = (SetCondPanel) parent;
		else if (parent instanceof SetJoinPanel)
			m_parent = (SetJoinPanel) parent;
		else if (parent instanceof SetCrossPanel)
			m_parent = (SetCrossPanel) parent;
		else if (parent instanceof SetTableJoinPanel)
			m_parent = (SetTableJoinPanel) parent;
		initialize();
	}

	/**
	 * add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		boolean bMulti = isMultiSelect();
		//合法性检查
		String strErr = null;
		if (bMulti)
			strErr = checkMultiSelect();
		else
			strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "查询引擎" */, strErr);
			return;
		}
		//增加表处理
		if (m_parent instanceof SetFldPanel) {
			if (bMulti) {
				SelectFldVO[] newsfs = getGenFlds();
				((SetFldPanel) m_parent).doAdd(newsfs);
			} else {
				SelectFldVO newsf = getGenFld();
				((SetFldPanel) m_parent).doAdd(newsf);
			}
		}
		//else
		//if (m_parent instanceof SetOrderPanel)
		//((SetOrderPanel) m_parent).doAdd(newsf);
		doClear();
	}

	/**
	 * cancel
	 */
	public void bnCancel_ActionPerformed(ActionEvent actionEvent) {
		closeCancel();
	}

	/**
	 * clear
	 */
	public void bnClear_ActionPerformed(ActionEvent actionEvent) {
		doClear();
	}

	/**
	 * OK
	 */
	public void bnOK_ActionPerformed(ActionEvent actionEvent) {
		if (m_parent instanceof SetFldPanel) {
			//合法性检查
			String strErr = null;
			if (isMultiSelect())
				strErr = checkMultiSelect();
			else
				strErr = check();
			if (strErr != null) {
				MessageDialog
						.showWarningDlg(
								this,
								NCLangRes.getInstance().getStrByID("10241201",
										"UPP10241201-000099")/* @res "查询引擎" */,
								strErr);
				return;
			}
		}
		closeOK();
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 13:57:49)
	 * 
	 * @return java.lang.String
	 */
	private String check() {

		//字段别名校验
		String fldAlias = getTFAlias().getText().trim();
		if (fldAlias.equals(""))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000683")/* @res "字段别名不能为空" */;
		String strTemp = fldAlias.toLowerCase();
		int iLen = strTemp.length();
		for (int i = 0; i < iLen; i++) {
			char c = strTemp.charAt(i);
			if (i == 0 && (c < 'a' || c > 'z'))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000684")/*
											  * @res
											  * "字段别名请使用字母开头且由字母、数字、下划线构成的字符，不得使用SQL保留字"
											  */;
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '_')
				continue;
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000685", null, new String[] { "" + c })/*
																		 * @res
																		 * "存在非法字符：{0}\n字段别名请使用字母开头且由字母、数字、下划线构成的字符，不得使用SQL保留字"
																		 */;
		}

		//字段显示名校验
		String fldName = getTFDisp().getText().trim();
		if (fldName.equals(""))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000686")/* @res "字段显示名不能为空" */;
		for (int i = 0; i < fldName.length(); i++) {
			char c = fldName.charAt(i);
			if (String.valueOf(c).equals(QueryConst.CROSS_SEPERATOR))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000687", null,
						new String[] { QueryConst.CROSS_SEPERATOR })/*
																	 * @res
																	 * "请不要在显示名中使用'{0}'符号，谢谢合作"
																	 */;
		}

		//修改字段无妨
		if (m_hashFldAlias.containsKey(fldAlias))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000688")/* @res "字段别名不能重复：" */
					+ fldAlias + "(" + fldName + ")";
		if (m_hashFldAlias.containsValue(fldName))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000689")/* @res "字段显示名不能重复：" */
					+ fldName;

		//更新哈希表
		m_hashFldAlias.put(fldAlias, fldName);
		return null;
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 13:57:49)
	 * 
	 * @return java.lang.String
	 */
	private String checkMultiSelect() {

		SelectFldVO[] sfs = getGenFlds();
		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++) {
			String fldAlias = sfs[i].getFldalias();
			String fldName = sfs[i].getFldname();
			//修改字段无妨
			if (m_hashFldAlias.containsKey(fldAlias))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000688")/* @res "字段别名不能重复：" */
						+ fldAlias + "(" + fldName + ")";
			if (m_hashFldAlias.containsValue(fldName))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000689")/* @res "字段显示名不能重复：" */
						+ fldName;
			//
			for (int j = 0; j < fldName.length(); j++) {
				char c = fldName.charAt(j);
				if (String.valueOf(c).equals(QueryConst.CROSS_SEPERATOR))
					return NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000687", null,
							new String[] { QueryConst.CROSS_SEPERATOR })/*
																		 * @res
																		 * "请不要在显示名中使用'{0}'符号，谢谢合作"
																		 */;
			}
		}
		//注意分两次循环
		for (int i = 0; i < iLen; i++) {
			//更新哈希表
			m_hashFldAlias.put(sfs[i].getFldalias(), sfs[i].getFldname());
		}
		return null;
	}

	/**
	 * connEtoC1: (BnCancel.action.actionPerformed(ActionEvent) -->
	 * FldGenDlg.bnCancel_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC1(ActionEvent arg1) {
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
	 * connEtoC2: (BnOK.action.actionPerformed(ActionEvent) -->
	 * FldGenDlg.bnOK_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC2(ActionEvent arg1) {
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
	 * connEtoC3: (ListTable.mouse.mouseClicked(MouseEvent) -->
	 * FldGenDlg.listTable_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC3(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.listTable_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC4: (ListFld.mouse.mouseClicked(MouseEvent) -->
	 * FldGenDlg.listFld_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC4(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.listFld_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5: (ListFunc.mouse.mouseClicked(MouseEvent) -->
	 * FldGenDlg.listFunc_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC5(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.treeFunc_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6: (ListOperator.mouse.mouseClicked(MouseEvent) -->
	 * FldGenDlg.listOperator_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC6(MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.listOperator_MouseClicked(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC7: (BnClear.action.actionPerformed(ActionEvent) -->
	 * FldGenDlg.bnClear_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC7(ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.bnClear_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC8: (BnAdd.action.actionPerformed(ActionEvent) -->
	 * FldGenDlg.bnAdd_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* 警告：此方法将重新生成。 */
	private void connEtoC8(ActionEvent arg1) {
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
	 * 清空 创建日期：(2003-4-3 10:13:00)
	 */
	public void doClear() {
		doSetInfo(new String[] { "", "", "", "" });
	}

	/**
	 * 尝试定位 创建日期：(2004-7-9 9:28:55)
	 */
	private void doLocate(String strExp) {
		if (strExp != null)
			try {
				int iIndex = strExp.indexOf(".");
				if (iIndex != -1) {
					//鲁莽截取字段
					String table = strExp.substring(0, iIndex).trim();
					//选表
					int iSelIndex = -1;
					int iLen = getLMTable().getSize();
					for (int i = 0; i < iLen; i++) {
						FromTableVO ft = (FromTableVO) getLMTable()
								.getElementAt(i);
						if (table.equalsIgnoreCase(ft.getTablealias())) {
							iSelIndex = i;
							break;
						}
					}
					if (iSelIndex != -1) {
						//界面选表
						getListTable().setSelectedIndex(iSelIndex);
						selectTable(iSelIndex);
						//鲁莽截取字段
						String fld = strExp.substring(iIndex + 1).trim();
						//选字段
						iSelIndex = -1;
						iLen = getLMFld().getSize();
						for (int i = 0; i < iLen; i++) {
							SelectFldVO sf = (SelectFldVO) getLMFld()
									.getElementAt(i);
							if (fld.equalsIgnoreCase(sf.getFldalias())) {
								iSelIndex = i;
								break;
							}
						}
						if (iSelIndex != -1) {
							//界面选字段
							getListFld().setSelectedIndex(iSelIndex);
							getListFld().scrollRectToVisible(
									getListFld().getCellBounds(iSelIndex,
											iSelIndex));
						}
					}
				}
			} catch (Exception e) {
				AppDebug.debug(e);//@devTools System.out.println(e);
			}
	}

	/**
	 * 设置字段信息 创建日期：(2003-4-3 10:13:00)
	 */
	public void doSetInfo(Object[] objDispAliasExpNote) {
		String[] strDispAliasExpNote = new String[4];
		for (int i = 0; i < 4; i++)
			if (objDispAliasExpNote[i] == null)
				strDispAliasExpNote[i] = "";
			else
				strDispAliasExpNote[i] = objDispAliasExpNote[i].toString();
		getTFDisp().setText(strDispAliasExpNote[0]);
		getTFAlias().setText(strDispAliasExpNote[1]);
		getTAExp().setText(strDispAliasExpNote[2]);
		getTFNote().setText(strDispAliasExpNote[3]);

		//尝试定位
		doLocate(strDispAliasExpNote[2]);
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
				ivjBnCancel.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000000")/* @res "取消" */);
				// user code begin {1}
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
	 * 返回 BnClear 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIButton getBnClear() {
		if (ivjBnClear == null) {
			try {
				ivjBnClear = new nc.ui.pub.beans.UIButton();
				ivjBnClear.setName("BnClear");
				ivjBnClear.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000318")/* @res "清除" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBnClear;
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
				ivjBnOK.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000001")/* @res "确定" */);
				// user code begin {1}
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
	 * 获得生成的表达式 创建日期：(2003-4-3 9:09:08)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public String getExp() {
		return handleExp(getTAExp().getText());
	}

	/**
	 * 简单处理一下表达式，没加括号的我给加括号
	 * 
	 * @return
	 */
	private String handleExp(String exp) {
		exp = exp.trim();
		boolean shouldaddbracket = false;
		int lbracketCount = 0;
		for (int i = 0, in = exp.length() - 1; i < in; i++) {
			if (exp.charAt(i) == '(') {
				lbracketCount++;
			} else if (exp.charAt(i) == ')') {
				lbracketCount--;
			}
			if (lbracketCount == 0) {
				shouldaddbracket = true;
				break;
			}
		}
		if (shouldaddbracket) {
			exp = "(" + exp + ")";
		}
		return exp;
	}

	/**
	 * 获得生成的查询字段VO 创建日期：(2003-4-3 9:09:08)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public SelectFldVO getGenFld() {
		SelectFldVO sf = new SelectFldVO();
		sf.setFldname(getTFDisp().getText().trim());
		sf.setFldalias(getTFAlias().getText().trim());
		sf.setExpression(getTAExp().getText().trim());
		sf.setNote(getTFNote().getText().trim());
		return sf;
	}

	/**
	 * 获得生成的查询字段VO数组 创建日期：(2003-4-3 9:09:08)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public SelectFldVO[] getGenFlds() {
		//获得选中表
		int iIndex = getListTable().getSelectedIndex();
		String tableAlias = null;
		if (iIndex != -1) {
			FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);
			tableAlias = ft.getTablealias();
		}
		//获得选中字段
		int[] iSelIndices = getListFld().getSelectedIndices();
		int iSelCount = (iSelIndices == null) ? 0 : iSelIndices.length;
		SelectFldVO[] sfs = new SelectFldVO[iSelCount];
		for (int i = 0; i < iSelCount; i++) {
			sfs[i] = (SelectFldVO) getListFld().getModel().getElementAt(
					iSelIndices[i]);
			//sfs[i] = (SelectFldVO) sfs[i].clone();
			//构造表达式
			String strExp = sfs[i].getFldalias();
			if (tableAlias != null)
				strExp = tableAlias + "." + strExp;
			sfs[i].setExpression(strExp);
		}
		return sfs;
	}

	/**
	 * 返回 LabelAlias 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelAlias() {
		if (ivjLabelAlias == null) {
			try {
				ivjLabelAlias = new nc.ui.pub.beans.UILabel();
				ivjLabelAlias.setName("LabelAlias");
				ivjLabelAlias.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000690")/* @res "字段别名：" */);
				ivjLabelAlias.setBounds(324, 14, 68, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelAlias;
	}

	/**
	 * 返回 LabelDisp 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelDisp() {
		if (ivjLabelDisp == null) {
			try {
				ivjLabelDisp = new nc.ui.pub.beans.UILabel();
				ivjLabelDisp.setName("LabelDisp");
				ivjLabelDisp.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000691")/*
														  * @res "字段显示名："
														  */);
				ivjLabelDisp.setBounds(36, 14, 80, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelDisp;
	}

	/**
	 * 返回 LabelExp 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelExp() {
		if (ivjLabelExp == null) {
			try {
				ivjLabelExp = new nc.ui.pub.beans.UILabel();
				ivjLabelExp.setName("LabelExp");
				ivjLabelExp.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000692")/*
														  * @res "字段表达式："
														  */);
				ivjLabelExp.setBounds(36, 46, 80, 22);
				ivjLabelExp
						.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelExp;
	}

	/**
	 * 返回 LabelTable1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelFld() {
		if (ivjLabelFld == null) {
			try {
				ivjLabelFld = new nc.ui.pub.beans.UILabel();
				ivjLabelFld.setName("LabelFld");
				ivjLabelFld.setPreferredSize(new java.awt.Dimension(100, 22));
				ivjLabelFld.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000693")/* @res "字段：" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelFld;
	}

	/**
	 * 返回 LabelTable2 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelFunc() {
		if (ivjLabelFunc == null) {
			try {
				ivjLabelFunc = new nc.ui.pub.beans.UILabel();
				ivjLabelFunc.setName("LabelFunc");
				ivjLabelFunc.setPreferredSize(new java.awt.Dimension(100, 22));
				ivjLabelFunc.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000694")/* @res "函数：" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelFunc;
	}

	/**
	 * 返回 LabelNote 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelNote() {
		if (ivjLabelNote == null) {
			try {
				ivjLabelNote = new nc.ui.pub.beans.UILabel();
				ivjLabelNote.setName("LabelNote");
				ivjLabelNote.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000695")/* @res "字段备注：" */);
				ivjLabelNote.setBounds(48, 142, 68, 22);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelNote;
	}

	/**
	 * 返回 LabelTable3 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelOperator() {
		if (ivjLabelOperator == null) {
			try {
				ivjLabelOperator = new nc.ui.pub.beans.UILabel();
				ivjLabelOperator.setName("LabelOperator");
				ivjLabelOperator.setPreferredSize(new java.awt.Dimension(100,
						22));
				ivjLabelOperator.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000696")/* @res "符号：" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelOperator;
	}

	/**
	 * 返回 LabelTable 特性值。
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UILabel getLabelTable() {
		if (ivjLabelTable == null) {
			try {
				ivjLabelTable = new nc.ui.pub.beans.UILabel();
				ivjLabelTable.setName("LabelTable");
				ivjLabelTable.setPreferredSize(new java.awt.Dimension(100, 22));
				ivjLabelTable.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000697")/* @res "表：" */);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjLabelTable;
	}

	/**
	 * 返回 ListTable1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIList getListFld() {
		if (ivjListFld == null) {
			try {
				ivjListFld = new nc.ui.pub.beans.UIList();
				ivjListFld.setName("ListFld");
				ivjListFld.setBounds(0, 0, 160, 120);
				// user code begin {1}
				//ivjListFld.setFixedCellHeight(22);
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListFld;
	}

	/**
	 * 返回 ListTable3 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIList getListOperator() {
		if (ivjListOperator == null) {
			try {
				ivjListOperator = new nc.ui.pub.beans.UIList();
				ivjListOperator.setName("ListOperator");
				ivjListOperator.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListOperator;
	}

	/**
	 * 返回 ListTable 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIList
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIList getListTable() {
		if (ivjListTable == null) {
			try {
				ivjListTable = new nc.ui.pub.beans.UIList();
				ivjListTable.setName("ListTable");
				ivjListTable.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjListTable;
	}

	/**
	 * 获得字段列表模型 创建日期：(2003-4-2 20:18:29)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMFld() {
		return (DefaultListModel) getListFld().getModel();
	}

	/**
	 * 获得操作符列表模型 创建日期：(2003-4-2 20:18:29)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMOperator() {
		return (DefaultListModel) getListOperator().getModel();
	}

	/**
	 * 获得表列表模型 创建日期：(2003-4-2 20:18:29)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMTable() {
		return (DefaultListModel) getListTable().getModel();
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
				ivjPnCenter.setLayout(getPnCenterGridLayout());
				getPnCenter().add(getPnListTable(), getPnListTable().getName());
				getPnCenter().add(getPnListFld(), getPnListFld().getName());
				getPnCenter().add(getPnListFunc(), getPnListFunc().getName());
				getPnCenter().add(getPnListOperator(),
						getPnListOperator().getName());
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
	 * 返回 PnCenterGridLayout 特性值。
	 * 
	 * @return java.awt.GridLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.GridLayout getPnCenterGridLayout() {
		java.awt.GridLayout ivjPnCenterGridLayout = null;
		try {
			/* 创建部件 */
			ivjPnCenterGridLayout = new java.awt.GridLayout();
			ivjPnCenterGridLayout.setHgap(5);
			ivjPnCenterGridLayout.setColumns(4);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnCenterGridLayout;
	}

	/**
	 * 返回 PnListTable1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListFld() {
		if (ivjPnListFld == null) {
			try {
				ivjPnListFld = new nc.ui.pub.beans.UIPanel();
				ivjPnListFld.setName("PnListFld");
				ivjPnListFld.setLayout(new java.awt.BorderLayout());
				getPnListFld().add(getPnListFldNorth(), "North");
				getPnListFld().add(getSclPnListFld(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListFld;
	}

	/**
	 * 返回 PnListTableNorth1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListFldNorth() {
		if (ivjPnListFldNorth == null) {
			try {
				ivjPnListFldNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnListFldNorth.setName("PnListFldNorth");
				ivjPnListFldNorth.setPreferredSize(new java.awt.Dimension(10,
						26));
				ivjPnListFldNorth.setLayout(getPnListFldNorthFlowLayout());
				getPnListFldNorth().add(getLabelFld(), getLabelFld().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListFldNorth;
	}

	/**
	 * 返回 PnListFldNorthFlowLayout 特性值。
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.FlowLayout getPnListFldNorthFlowLayout() {
		java.awt.FlowLayout ivjPnListFldNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnListFldNorthFlowLayout = new java.awt.FlowLayout();
			ivjPnListFldNorthFlowLayout.setVgap(2);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnListFldNorthFlowLayout;
	}

	/**
	 * 返回 PnListTable2 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListFunc() {
		if (ivjPnListFunc == null) {
			try {
				ivjPnListFunc = new nc.ui.pub.beans.UIPanel();
				ivjPnListFunc.setName("PnListFunc");
				ivjPnListFunc.setLayout(new java.awt.BorderLayout());
				getPnListFunc().add(getPnListFuncNorth(), "North");
				getPnListFunc().add(getSclPnListFunc(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListFunc;
	}

	/**
	 * 返回 PnListTableNorth2 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListFuncNorth() {
		if (ivjPnListFuncNorth == null) {
			try {
				ivjPnListFuncNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnListFuncNorth.setName("PnListFuncNorth");
				ivjPnListFuncNorth.setPreferredSize(new java.awt.Dimension(10,
						26));
				ivjPnListFuncNorth.setLayout(getPnListFuncNorthFlowLayout());
				getPnListFuncNorth().add(getLabelFunc(),
						getLabelFunc().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListFuncNorth;
	}

	/**
	 * 返回 PnListFuncNorthFlowLayout 特性值。
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.FlowLayout getPnListFuncNorthFlowLayout() {
		java.awt.FlowLayout ivjPnListFuncNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnListFuncNorthFlowLayout = new java.awt.FlowLayout();
			ivjPnListFuncNorthFlowLayout.setVgap(2);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnListFuncNorthFlowLayout;
	}

	/**
	 * 返回 PnListTable3 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListOperator() {
		if (ivjPnListOperator == null) {
			try {
				ivjPnListOperator = new nc.ui.pub.beans.UIPanel();
				ivjPnListOperator.setName("PnListOperator");
				ivjPnListOperator.setLayout(new java.awt.BorderLayout());
				getPnListOperator().add(getPnListOperatorNorth(), "North");
				getPnListOperator().add(getSclPnListOperator(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListOperator;
	}

	/**
	 * 返回 PnListTableNorth3 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListOperatorNorth() {
		if (ivjPnListOperatorNorth == null) {
			try {
				ivjPnListOperatorNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnListOperatorNorth.setName("PnListOperatorNorth");
				ivjPnListOperatorNorth.setPreferredSize(new java.awt.Dimension(
						10, 26));
				ivjPnListOperatorNorth
						.setLayout(getPnListOperatorNorthFlowLayout());
				getPnListOperatorNorth().add(getLabelOperator(),
						getLabelOperator().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListOperatorNorth;
	}

	/**
	 * 返回 PnListOperatorNorthFlowLayout 特性值。
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.FlowLayout getPnListOperatorNorthFlowLayout() {
		java.awt.FlowLayout ivjPnListOperatorNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnListOperatorNorthFlowLayout = new java.awt.FlowLayout();
			ivjPnListOperatorNorthFlowLayout.setVgap(2);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnListOperatorNorthFlowLayout;
	}

	/**
	 * 返回 PnListTable 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListTable() {
		if (ivjPnListTable == null) {
			try {
				ivjPnListTable = new nc.ui.pub.beans.UIPanel();
				ivjPnListTable.setName("PnListTable");
				ivjPnListTable.setLayout(new java.awt.BorderLayout());
				getPnListTable().add(getPnListTableNorth(), "North");
				getPnListTable().add(getSclPnListTable(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListTable;
	}

	/**
	 * 返回 PnListTableNorth 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnListTableNorth() {
		if (ivjPnListTableNorth == null) {
			try {
				ivjPnListTableNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnListTableNorth.setName("PnListTableNorth");
				ivjPnListTableNorth.setPreferredSize(new java.awt.Dimension(10,
						26));
				ivjPnListTableNorth.setLayout(getPnListTableNorthFlowLayout());
				getPnListTableNorth().add(getLabelTable(),
						getLabelTable().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjPnListTableNorth;
	}

	/**
	 * 返回 PnListTableNorthFlowLayout 特性值。
	 * 
	 * @return java.awt.FlowLayout
	 */
	/* 警告：此方法将重新生成。 */
	private java.awt.FlowLayout getPnListTableNorthFlowLayout() {
		java.awt.FlowLayout ivjPnListTableNorthFlowLayout = null;
		try {
			/* 创建部件 */
			ivjPnListTableNorthFlowLayout = new java.awt.FlowLayout();
			ivjPnListTableNorthFlowLayout.setVgap(2);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnListTableNorthFlowLayout;
	}

	/**
	 * 返回 UIPanel1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new nc.ui.pub.beans.UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new java.awt.Dimension(10, 180));
				ivjPnNorth.setLayout(null);
				getPnNorth().add(getLabelDisp(), getLabelDisp().getName());
				getPnNorth().add(getTFDisp(), getTFDisp().getName());
				getPnNorth().add(getLabelAlias(), getLabelAlias().getName());
				getPnNorth().add(getTFAlias(), getTFAlias().getName());
				getPnNorth().add(getLabelNote(), getLabelNote().getName());
				getPnNorth().add(getTFNote(), getTFNote().getName());
				getPnNorth().add(getLabelExp(), getLabelExp().getName());
				getPnNorth().add(getSclPnExp(), getSclPnExp().getName());
				// user code begin {1}
				//zjb+
				getPnNorth().add(getCbbJoinType(), getCbbJoinType().getName());
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
				getPnSouth().add(getBnOK(), getBnOK().getName());
				getPnSouth().add(getBnCancel(), getBnCancel().getName());
				getPnSouth().add(getBnClear(), getBnClear().getName());
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
			ivjPnSouthFlowLayout.setVgap(8);
			ivjPnSouthFlowLayout.setHgap(20);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * 返回 UIScrollPane1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnExp() {
		if (ivjSclPnExp == null) {
			try {
				ivjSclPnExp = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnExp.setName("SclPnExp");
				ivjSclPnExp.setBounds(116, 46, 404, 86);
				getSclPnExp().setViewportView(getTAExp());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnExp;
	}

	/**
	 * 返回 SclPnListTable1 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnListFld() {
		if (ivjSclPnListFld == null) {
			try {
				ivjSclPnListFld = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnListFld.setName("SclPnListFld");
				getSclPnListFld().setViewportView(getListFld());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnListFld;
	}

	/**
	 * 返回 SclPnListTable2 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnListFunc() {
		if (ivjSclPnListFunc == null) {
			try {
				ivjSclPnListFunc = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnListFunc.setName("SclPnListFunc");
				getSclPnListFunc().setViewportView(getTreeFunc());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnListFunc;
	}

	/**
	 * 返回 SclPnListTable3 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnListOperator() {
		if (ivjSclPnListOperator == null) {
			try {
				ivjSclPnListOperator = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnListOperator.setName("SclPnListOperator");
				getSclPnListOperator().setViewportView(getListOperator());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnListOperator;
	}

	/**
	 * 返回 SclPnListTable 特性值。
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UIScrollPane getSclPnListTable() {
		if (ivjSclPnListTable == null) {
			try {
				ivjSclPnListTable = new nc.ui.pub.beans.UIScrollPane();
				ivjSclPnListTable.setName("SclPnListTable");
				getSclPnListTable().setViewportView(getListTable());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjSclPnListTable;
	}

	/**
	 * 返回 TAExp 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextArea getTAExp() {
		if (ivjTAExp == null) {
			try {
				ivjTAExp = new nc.ui.pub.beans.UITextArea();
				ivjTAExp.setName("TAExp");
				ivjTAExp.setLineWrap(true);
//				ivjTAExp.setFont(new java.awt.Font("serif", 0, 14));
				ivjTAExp.setBounds(0, 0, 160, 120);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTAExp;
	}

	/**
	 * 返回 TFAlias 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextField getTFAlias() {
		if (ivjTFAlias == null) {
			try {
				ivjTFAlias = new nc.ui.pub.beans.UITextField();
				ivjTFAlias.setName("TFAlias");
				ivjTFAlias.setBounds(392, 14, 128, 22);
				ivjTFAlias.setMaxLength(40);
				// user code begin {1}
				ivjTFAlias.setAllowAlphabetic(true);
				ivjTFAlias.setAllowNumeric(true);
				ivjTFAlias.setAllowOtherCharacter(true);
				ivjTFAlias.setAllowUnicode(false);
				ivjTFAlias.setToolTipText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000698")/*
														  * @res
														  * "别名请使用字母开头且由字母、数字、下划线构成的字符，不得使用SQL保留字"
														  */);
				//ivjTFAlias.setDelStr("_");
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTFAlias;
	}

	/**
	 * 返回 TFDisp 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextField getTFDisp() {
		if (ivjTFDisp == null) {
			try {
				ivjTFDisp = new nc.ui.pub.beans.UITextField();
				ivjTFDisp.setName("TFDisp");
				ivjTFDisp.setBounds(116, 14, 160, 22);
				ivjTFDisp.setMaxLength(100);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTFDisp;
	}

	/**
	 * 返回 TFNote 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITextField getTFNote() {
		if (ivjTFNote == null) {
			try {
				ivjTFNote = new nc.ui.pub.beans.UITextField();
				ivjTFNote.setName("TFNote");
				ivjTFNote.setBounds(116, 142, 404, 22);
				ivjTFNote.setMaxLength(40);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTFNote;
	}

	/**
	 * 返回 TreeFunc 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITree
	 */
	/* 警告：此方法将重新生成。 */
	private nc.ui.pub.beans.UITree getTreeFunc() {
		if (ivjTreeFunc == null) {
			try {
				ivjTreeFunc = new nc.ui.pub.beans.UITree();
				ivjTreeFunc.setName("TreeFunc");
				ivjTreeFunc.setBounds(0, 0, 78, 72);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTreeFunc;
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
				getUIDialogContentPane().add(getPnNorth(), "North");
				getUIDialogContentPane().add(getPnCenter(), "Center");
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
		getBnOK().addActionListener(ivjEventHandler);
		getListTable().addMouseListener(ivjEventHandler);
		getListFld().addMouseListener(ivjEventHandler);
		getListOperator().addMouseListener(ivjEventHandler);
		getBnClear().addActionListener(ivjEventHandler);
		getBnAdd().addActionListener(ivjEventHandler);
		getTreeFunc().addMouseListener(ivjEventHandler);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("FldGenDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000699")/* @res "生成表达式" */);
			setSize(560, 400);
			setResizable(true);
			setContentPane(getUIDialogContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initList();
		initTree();
		getListFld().setCellRenderer(new DataTypeCellRenderer());
		// user code end
	}

	/**
	 * 初始化列表 创建日期：(2003-4-2 19:56:10)
	 */
	public void initList() {
		//设置列表模型
		getListTable().setModel(new DefaultListModel());
		getListFld().setModel(new DefaultListModel());
		DefaultListModel lm = new DefaultListModel();
		if (m_parent instanceof SetFldPanel) {
			lm.addElement("+");
			lm.addElement("-");
			lm.addElement("*");
			lm.addElement("/");
			lm.addElement("||");
			lm.addElement("(");
			lm.addElement(")");
			lm.addElement("'");
			lm.addElement(",");
		} else if (m_parent instanceof SetCondPanel
				|| m_parent instanceof SetJoinPanel
				|| m_parent instanceof SetCrossPanel
				|| m_parent instanceof SetTableJoinPanel) {
			lm.addElement("=");
			lm.addElement(">");
			lm.addElement("<");
			lm.addElement(">=");
			lm.addElement("<=");
			lm.addElement("<>");
			lm.addElement("(");
			lm.addElement(")");
			lm.addElement(" like ");
			lm.addElement(" in ");
		}
		getListOperator().setModel(lm);
		//设置单选模式
		getListTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getListOperator().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * 初始化函数树和操作符列表 创建日期：(2000-08-01 15:10:00)
	 */
	public void initTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
				.getInstance().getStrByID("10241201", "UPP10241201-000283")/*
																		    * @res
																		    * "函数"
																		    */);
		DefaultMutableTreeNode parent, child;
		//初始化函数树
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000284")/* @res "数学函数" */);
		child = new DefaultMutableTreeNode("abs()");
		parent.add(child);
		child = new DefaultMutableTreeNode("sign()");
		parent.add(child);
		child = new DefaultMutableTreeNode("floor()");
		parent.add(child);
		child = new DefaultMutableTreeNode("ceiling()");
		parent.add(child);
		child = new DefaultMutableTreeNode("round()");
		parent.add(child);
		child = new DefaultMutableTreeNode("square()");
		parent.add(child);
		child = new DefaultMutableTreeNode("sqrt()");
		parent.add(child);
		root.add(parent);
		//文本函数
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000285")/* @res "文本函数" */);
		child = new DefaultMutableTreeNode("len()");
		parent.add(child);
		child = new DefaultMutableTreeNode("substring()");
		parent.add(child);
		child = new DefaultMutableTreeNode("lower()");
		parent.add(child);
		child = new DefaultMutableTreeNode("upper()");
		parent.add(child);
		child = new DefaultMutableTreeNode("ltrim()");
		parent.add(child);
		child = new DefaultMutableTreeNode("rtrim()");
		parent.add(child);
		root.add(parent);
		//统计函数
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000700")/* @res "统计函数" */);
		child = new DefaultMutableTreeNode("count()");
		parent.add(child);
		child = new DefaultMutableTreeNode("max()");
		parent.add(child);
		child = new DefaultMutableTreeNode("min()");
		parent.add(child);
		child = new DefaultMutableTreeNode("sum()");
		parent.add(child);
		child = new DefaultMutableTreeNode("avg()");
		parent.add(child);
		root.add(parent);
		//其它函数
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000701")/* @res "其它函数" */);
		child = new DefaultMutableTreeNode("case when");
		parent.add(child);
		child = new DefaultMutableTreeNode("isnull");
		parent.add(child);
		child = new DefaultMutableTreeNode("datediff");
		parent.add(child);
		root.add(parent);

		//设置树模型与单选模式
		getTreeFunc().setModel(new DefaultTreeModel(root));
		getTreeFunc().getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * 是否多选字段 创建日期：(2004-6-25 13:44:38)
	 * 
	 * @return boolean
	 */
	public boolean isMultiSelect() {
		int[] iSelIndices = getListFld().getSelectedIndices();
		int iSelCount = (iSelIndices == null) ? 0 : iSelIndices.length;
		return (iSelCount > 1);
	}

	/**
	 * Invoked when a key has been released.
	 */
	public void keyReleased(KeyEvent e) {
		/*
		 * if (e.getKeyCode() == KeyEvent.VK_F12) { QryCondRefDlg dlg = null;
		 * //获得父组件的信息 if (m_parent instanceof SetFldPanel) { SetFldPanel pn =
		 * ((SetFldPanel) m_parent); dlg = pn.getQueryDefTabbedPn().getRefDlg(); }
		 * else if (m_parent instanceof SetCondPanel) { SetCondPanel pn =
		 * ((SetCondPanel) m_parent); dlg =
		 * pn.getQueryDefTabbedPn().getRefDlg(); } else if (m_parent instanceof
		 * SetJoinPanel) { SetJoinPanel pn = ((SetJoinPanel) m_parent); dlg =
		 * pn.getQueryDefTabbedPn().getRefDlg(); } else if (m_parent instanceof
		 * SetCrossPanel) { SetCrossPanel pn = ((SetCrossPanel) m_parent); dlg =
		 * pn.getQueryDefTabbedPn().getRefDlg(); } //基础参照快捷录入 if (dlg != null) {
		 * dlg.showModal(); dlg.destroy(); if (dlg.getResult() ==
		 * UIDialog.ID_OK) //填入参照结果 onSelect(dlg.getValue(), 0); } }
		 */
	}

	/**
	 * 字段列表点击响应
	 */
	public void listFld_MouseClicked(MouseEvent mouseEvent) {
		//多选控制
		boolean bMulti = isMultiSelect();
		getTFDisp().setEnabled(!bMulti);
		getTFAlias().setEnabled(!bMulti);
		getTFNote().setEnabled(!bMulti);
		getTAExp().setEnabled(!bMulti);
		//
		int iClickCount = mouseEvent.getClickCount();
		if (iClickCount == 2) {
			//获得选中字段
			String str = null;
			int iIndex = getListFld().getSelectedIndex();
			if (iIndex != -1) {
				SelectFldVO sf = (SelectFldVO) getLMFld().getElementAt(iIndex);
				//str = sf.getExpression();
				str = sf.getFldalias();
				//设置显示名和别名
				if (getTFDisp().getText().trim().equals(""))
					getTFDisp().setText(sf.getFldname());
				if (getTFAlias().getText().trim().equals(""))
					getTFAlias().setText(str);
				//获得选中表
				iIndex = getListTable().getSelectedIndex();
				if (iIndex != -1) {
					//获得选中表
					FromTableVO ft = (FromTableVO) getLMTable().getElementAt(
							iIndex);
					str = ft.getTablealias() + "." + str;
				}
			}
			//选中
			if (str != null)
				onSelect(str, 0);
		}
		return;
	}

	/**
	 * 操作符列表点击响应
	 */
	public void listOperator_MouseClicked(MouseEvent mouseEvent) {
		int iClickCount = mouseEvent.getClickCount();
		if (iClickCount == 2) {
			//获得选中函数
			String sel = null;
			int iIndex = getListOperator().getSelectedIndex();
			if (iIndex != -1)
				sel = getLMOperator().getElementAt(iIndex).toString();
			//选中
			if (sel != null)
				onSelect(sel, 0);
		}
		return;
	}

	/**
	 * 点击表列表响应
	 */
	public void listTable_MouseClicked(MouseEvent mouseEvent) {
		int iIndex = getListTable().getSelectedIndex();
		if (iIndex != -1)
			selectTable(iIndex);
	}

	/**
	 * 双击选中 创建日期：(2003-4-2 20:38:58)
	 * 
	 * @param str
	 *            java.lang.String
	 */
	private void onSelect(String str, int iFunc) {
		//选中
		int pos = getTAExp().getSelectionStart();
		getTAExp().insert(str, pos);
		getTAExp().requestFocus();
		getTAExp().setSelectionStart(pos + str.length() - iFunc);
		getTAExp().setSelectionEnd(pos + str.length() - iFunc);
	}

	/**
	 * 重置字段列表 创建日期：(2003-4-2 20:11:19)
	 * 
	 * @param tds
	 *            nc.bs.com.datadict.TableDef[]
	 */
	private void resetFld(SelectFldVO[] sfs) {
		getLMFld().removeAllElements();
		int iLen = (sfs == null) ? 0 : sfs.length;
		if (iLen != 0) {
			//按显示名排序
			Arrays.sort(sfs);
			//获得最长显示名的长度
			int iMaxWidth = 0;
			String strMax = "";
			for (int i = 0; i < iLen; i++) {
				int iNameLen = sfs[i].getFldname().getBytes().length;
				if (iMaxWidth < iNameLen) {
					iMaxWidth = iNameLen;
					strMax = sfs[i].getFldname();
				}
				//添加列表元素
				getLMFld().addElement(sfs[i]);
			}
			//设置列表最佳宽度
			if (iMaxWidth != 0) {
				getListFld().setPrototypeCellValue(strMax);
			}
		}
	}

	/**
	 * 重置表列表 创建日期：(2003-4-2 20:11:19)
	 * 
	 * @param tds
	 *            nc.bs.com.datadict.TableDef[]
	 */
	private void resetTable(FromTableVO[] fts) {
		getLMTable().removeAllElements();
		if (fts != null)
			for (int i = 0; i < fts.length; i++)
				getLMTable().addElement(fts[i]);
	}

	/**
	 * 选中表 创建日期：(2004-7-9 9:19:16)
	 * 
	 * @param iIndex
	 *            int
	 */
	private void selectTable(int iIndex) {
		//获得选中表
		FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);

		SelectFldVO[] sfs = getFldVO(ft);
		//设置字段列表
		if (sfs != null) {
			//克隆（否则被其它查询引用后会颠倒顺序）
			SelectFldVO[] sfsClone = new SelectFldVO[sfs.length];
			for (int i = 0; i < sfs.length; i++) {
				sfsClone[i] = (SelectFldVO) sfs[i].clone();
				if (sfsClone[i].getFldname() == null) {
					sfsClone[i].setFldname("");
				}
			}
			//设置
			resetFld(sfsClone);
		} else
			getLMFld().removeAllElements();
	}

	/**
	 * 设置增加按钮可用性 创建日期：(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setBnAddEnabled(boolean bEnabled) {
		getBnAdd().setEnabled(bEnabled);
		//修改态不能多选
		if (bEnabled)
			getListFld().setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		else {
			getListFld().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			int iSelCount = (getListFld().getSelectedIndices() == null) ? 0
					: getListFld().getSelectedIndices().length;
			if (iSelCount > 1)
				getListFld().clearSelection();
		}
	}

	/**
	 * 设置字段别名哈希表 创建日期：(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setHashFldAlias(Hashtable<String, String> hashFldAlias) {
		m_hashFldAlias = hashFldAlias;
	}

	/**
	 * 设置查询基本定义 创建日期：(2003-4-2 18:34:06)
	 * 
	 * @return nc.vo.iuforeport.businessquery.QueryBaseDef
	 */
	public void setQueryBaseDef(QueryBaseDef qbd) {
		m_qbd = qbd;
		//设置表
		FromTableVO[] fts = m_qbd.getFromTables();
		resetTable(fts);
	}

	/**
	 * 设置显示属性可用性 创建日期：(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setSomeInvisibled() {
		getLabelDisp().setVisible(false);
		getTFDisp().setVisible(false);
		getLabelAlias().setVisible(false);
		getTFAlias().setVisible(false);
		getLabelNote().setVisible(false);
		getTFNote().setVisible(false);
		getLabelExp().setText(
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000702")/* @res "条件表达式：" */);

		//连接类型下拉框可见性处理
		boolean bJoinTypeVisible = (m_parent instanceof SetTableJoinPanel);
		getCbbJoinType().setVisible(bJoinTypeVisible);
	}

	/**
	 * 显示对话框
	 *  
	 */
	public int showModal1() {
		if (!isShowing()) {
			//调整位置，方便对照
			Point point = getLocation();
			point.y = m_parent.getLocation().y + 120;
			setLocation(point);

			//setLocationRelativeTo(getTopParent());
			return showModal();
		}
		return getResult();
	}

	/**
	 * 函数树点击响应
	 */
	public void treeFunc_MouseClicked(MouseEvent mouseEvent) {
		int iClickCount = mouseEvent.getClickCount();
		if (iClickCount == 2) {
			TreePath selPath = getTreeFunc().getSelectionPath();
			if (selPath != null) {
				TreeNode selNode = (TreeNode) (selPath.getPath()[selPath
						.getPathCount() - 1]);
				if (selNode.isLeaf()) {
					//获得选中函数
					String sel = selPath.getPathComponent(
							selPath.getPathCount() - 1).toString();
					//选中
					if (sel != null) {
						int iFld = (m_parent instanceof SetFldPanel) ? 1 : 0;
						if (sel.startsWith("case ")) {
							sel = "case  when  then  else  end";
							iFld = 22;
						} else if (sel.startsWith("datediff")) {
							sel = "datediff(yyyy, , getdate())";
							iFld = 12;
						} else if (sel.startsWith("isnull")) {
							sel = "isnull(, )";
							iFld = 3;
						}
						onSelect(sel, iFld);
					}
				}
			}
		}
		return;
	}

	private SelectFldVO[] getFldVO(FromTableVO ft) {
		String		strTablePK = ft.getTablecode();
		//  获得数据字典实例
		AbstractQueryDesignTabPanel tabPn = ((AbstractQueryDesignSetPanel) m_parent).getTabPn();
		ObjectTree dd = tabPn.getDatadict();
		if( dd != null && dd instanceof DataDictForNode ){
			if( ((DataDictForNode)dd).isIUFO() ){
				strTablePK = ft.getTablealias();
			}
		}
		if (!m_hmFlds.containsKey(strTablePK)) {
			String dsName = tabPn.getDefDsName();
			ObjectTree tree = (BIModelUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			//取得字段
			SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
					ft.getTablealias(), tree);
			m_hmFlds.put(strTablePK, sfs);
			return sfs;
		} else {
			return (SelectFldVO[]) m_hmFlds.get(strTablePK);
		}
//		if (!m_hmFlds.containsKey(ft.getTablecode())) {
//			//  获得数据字典实例
//			
//			//			if (m_parent instanceof SetFldPanel)
//			//				tabPn = ((SetFldPanel) m_parent).getQueryDefTabbedPn();
//			//			else if (m_parent instanceof SetCondPanel)
//			//				tabPn = ((SetCondPanel) m_parent).getQueryDefTabbedPn();
//			//			else if (m_parent instanceof SetJoinPanel)
//			//				tabPn = ((SetJoinPanel) m_parent).getQueryDefTabbedPn();
//			//			else if (m_parent instanceof SetCrossPanel)
//			//				tabPn = ((SetCrossPanel) m_parent).getQueryDefTabbedPn();
//			ObjectTree dd = tabPn.getDatadict();
//			//String dsName = tabPn.getQueryBaseDef().getDsName();
//			String dsName = tabPn.getDefDsName();
//			ObjectTree tree = (BIModelUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
//					.getInstance(dsName)
//					: dd;
//			SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
//					ft.getTablealias(), tree);
//			m_hmFlds.put(ft.getTablecode(), sfs);
//			return sfs;
//		} else {
//			return (SelectFldVO[]) m_hmFlds.get(ft.getTablecode());
//		}
	}

	/**
	 * 返回 TFDisp 特性值。
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 * @i18n mbiquery0153=内连接
	 * @i18n mbiquery0154=左连接
	 * @i18n mbiquery0155=右连接
	 */
	private UIComboBox getCbbJoinType() {
		if (ivjCbbJoinType == null) {
			try {
				ivjCbbJoinType = new UIComboBox();
				ivjCbbJoinType.setName("CbbJoinType");
				ivjCbbJoinType.setBounds(240, 14, 80, 22);
				//模型
				ivjCbbJoinType.addItem(StringResource.getStringResource("mbiquery0153"));
				ivjCbbJoinType.addItem(StringResource.getStringResource("mbiquery0154"));
				ivjCbbJoinType.addItem(StringResource.getStringResource("mbiquery0155"));
				//不可见
				ivjCbbJoinType.setVisible(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCbbJoinType;
	}

	/**
	 * 获得连接类型
	 */
	public String getJoinType() {
		int iIndex = getCbbJoinType().getSelectedIndex();
		String joinType = "I";
		if (iIndex == 1) {
			joinType = "L";
		} else if (iIndex == 2) {
			joinType = "R";
		}
		return joinType;
	}

	/**
	 * 设置连接类型
	 */
	public void setJoinType(String joinType) {
		int iIndex = 0;
		if (joinType.equals("L")) {
			iIndex = 1;
		} else if (joinType.equals("R")) {
			iIndex = 2;
		}
		getCbbJoinType().setSelectedIndex(iIndex);
	}
}  