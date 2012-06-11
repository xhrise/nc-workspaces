/*
 * 创建日期 2006-10-31
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.query.designer;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.iuforeport.businessquery.FromTableVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.core.ObjectTree;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryModelTree;
import com.ufsoft.iufo.resource.StringResource;




/**
 * 待定条件表达式生成对话框
 * 创建日期：(2003-4-2 19:24:22)
 * @author：朱俊彬
 */
public class UnKnownCondGenDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//查询基本定义
	private QueryBaseDef m_qbd = null;
	//父组件
	private Container m_parent = null;
	private UIButton ivjBnCancel = null;
	private UIButton ivjBnOK = null;
	private UIPanel ivjPnSouth = null;
	//private FlowLayout ivjPnSouthFlowLayout = null;
	private JPanel ivjUIDialogContentPane = null;
	private UILabel ivjLabelTable = null;
	private UIList ivjListTable = null;
	private UIPanel ivjPnCenter = null;
	//private GridLayout ivjPnCenterGridLayout = null;
	private UIPanel ivjPnListTable = null;
	private UIPanel ivjPnListTableNorth = null;
	//private FlowLayout ivjPnListTableNorthFlowLayout = null;
	private UIPanel ivjPnNorth = null;
	private UIScrollPane ivjSclPnListTable = null;
	private UILabel ivjLabelFld = null;
	private UILabel ivjLabelFunc = null;
	private UIList ivjListFld = null;
	private UIList ivjListFunc = null;
	private UIPanel ivjPnListFld = null;
	private UIPanel ivjPnListFldNorth = null;
	//private FlowLayout ivjPnListFldNorthFlowLayout = null;
	private UIPanel ivjPnListFunc = null;
	private UIPanel ivjPnListFuncNorth = null;
	//private FlowLayout ivjPnListFuncNorthFlowLayout = null;
	private UIScrollPane ivjSclPnListFld = null;
	private UIScrollPane ivjSclPnListFunc = null;
	private UILabel ivjLabelOperator = null;
	private UIList ivjListOperator = null;
	private UIPanel ivjPnListOperator = null;
	private UIPanel ivjPnListOperatorNorth = null;
	//private FlowLayout ivjPnListOperatorNorthFlowLayout = null;
	private UIScrollPane ivjSclPnListOperator = null;
	private UITextArea ivjTAExp = null;
	private UIScrollPane ivjSclPnExp = null;
	private UIButton ivjBnClear = null;
	private UIComboBox ivjCbbParam = null;
	private UIComboBox ivjCbbRela = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

	class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.MouseListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == UnKnownCondGenDlg.this.getBnCancel())
				connEtoC1(e);
			if (e.getSource() == UnKnownCondGenDlg.this.getBnOK())
				connEtoC2(e);
			if (e.getSource() == UnKnownCondGenDlg.this.getBnClear())
				connEtoC7(e);
		};
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == UnKnownCondGenDlg.this.getListTable())
				connEtoC3(e);
			if (e.getSource() == UnKnownCondGenDlg.this.getListFld())
				connEtoC4(e);
			if (e.getSource() == UnKnownCondGenDlg.this.getListFunc())
				connEtoC5(e);
			if (e.getSource() == UnKnownCondGenDlg.this.getListOperator())
				connEtoC6(e);
		};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mousePressed(java.awt.event.MouseEvent e) {};
		public void mouseReleased(java.awt.event.MouseEvent e) {};
	};
/**
 * FldGenDlg 构造子注解。
 * @deprecated
 */
public UnKnownCondGenDlg() {
	super();
	initialize();
}
/**
 * FldGenDlg 构造子注解。
 * @param parent java.awt.Container
 */
public UnKnownCondGenDlg(java.awt.Container parent) {
	super(parent);
	if (parent instanceof SetCondPanel)
		m_parent = (SetCondPanel) parent;
	initialize();
}
/**
 * cancel
 */
public void bnCancel_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	closeCancel();
}
/**
 * clear
 */
public void bnClear_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	getTAExp().setText("");
}
/**
 * OK
 */
public void bnOK_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
	String strErr = check();
	if (strErr != null) {
		MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000099")/*@res "查询引擎"*/, strErr);
		return;
	}
	closeOK();
}
/**
 * 合法性检查
 * 创建日期：(2003-4-4 13:57:49)
 * @return java.lang.String
 */
private String check() {
	if (getExp().equals(""))
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000938")/*@res "表达式不能为空"*/;
	if (getVal().equals(""))
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000939")/*@res "参数不能为空"*/;
	return null;
}
/**
 * connEtoC1:  (BnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> FldGenDlg.bnCancel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
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
 * connEtoC2:  (BnOK.action.actionPerformed(java.awt.event.ActionEvent) --> FldGenDlg.bnOK_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
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
 * connEtoC3:  (ListTable.mouse.mouseClicked(java.awt.event.MouseEvent) --> FldGenDlg.listTable_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC3(java.awt.event.MouseEvent arg1) {
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
 * connEtoC4:  (ListFld.mouse.mouseClicked(java.awt.event.MouseEvent) --> FldGenDlg.listFld_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC4(java.awt.event.MouseEvent arg1) {
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
 * connEtoC5:  (ListFunc.mouse.mouseClicked(java.awt.event.MouseEvent) --> FldGenDlg.listFunc_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC5(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.listFunc_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (ListOperator.mouse.mouseClicked(java.awt.event.MouseEvent) --> FldGenDlg.listOperator_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC6(java.awt.event.MouseEvent arg1) {
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
 * connEtoC7:  (BnClear.action.actionPerformed(java.awt.event.ActionEvent) --> FldGenDlg.bnClear_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* 警告：此方法将重新生成。 */
private void connEtoC7(java.awt.event.ActionEvent arg1) {
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
 * 设置条件信息
 * 创建日期：(2003-4-3 10:13:00)
 */
public void doSetInfo(Object[] objExpRelaVal) {
	String strExp = (objExpRelaVal[0] == null) ? "" : objExpRelaVal[0].toString();
	getTAExp().setText(strExp);
	String strRela = (objExpRelaVal[1] == null) ? "" : objExpRelaVal[1].toString();
	if (strRela.equals(""))
		getCbbRela().setSelectedIndex(0);
	else
		getCbbRela().setSelectedItem(strRela);
	String strVal = (objExpRelaVal[2] == null) ? "" : objExpRelaVal[2].toString();
	if (strVal.equals(""))
		getCbbParam().setSelectedIndex(0);
	else
		getCbbParam().setSelectedItem(strVal);
}
/**
 * 返回 BnCancel 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBnCancel() {
	if (ivjBnCancel == null) {
		try {
			ivjBnCancel = new nc.ui.pub.beans.UIButton();
			ivjBnCancel.setName("BnCancel");
			ivjBnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000000")/*@res "取消"*/);
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
 * 返回 BnClear 特性值。
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBnClear() {
	if (ivjBnClear == null) {
		try {
			ivjBnClear = new nc.ui.pub.beans.UIButton();
			ivjBnClear.setName("BnClear");
			ivjBnClear.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000318")/*@res "清除"*/);
			// user code begin {1}
			ivjBnClear.setPreferredSize(new Dimension(90, 22));
			UIUtil.autoSizeComp(ivjBnClear, ivjBnClear.getText());
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
 * @return nc.ui.pub.beans.UIButton
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIButton getBnOK() {
	if (ivjBnOK == null) {
		try {
			ivjBnOK = new nc.ui.pub.beans.UIButton();
			ivjBnOK.setName("BnOK");
			ivjBnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000001")/*@res "确定"*/);
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
 * 返回 CbbParam 特性值。
 * @return nc.ui.pub.beans.UIComboBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIComboBox getCbbParam() {
	if (ivjCbbParam == null) {
		try {
			ivjCbbParam = new nc.ui.pub.beans.UIComboBox();
			ivjCbbParam.setName("CbbParam");
			ivjCbbParam.setBounds(450, 26, 100, 48);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCbbParam;
}
/**
 * 返回 CbbRela 特性值。
 * @return nc.ui.pub.beans.UIComboBox
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIComboBox getCbbRela() {
	if (ivjCbbRela == null) {
		try {
			ivjCbbRela = new nc.ui.pub.beans.UIComboBox();
			ivjCbbRela.setName("CbbRela");
			ivjCbbRela.setBounds(340, 26, 100, 48);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCbbRela;
}
/**
 * 获得生成的表达式
 * 创建日期：(2003-4-3 9:09:08)
 * @return nc.vo.iuforeport.businessquery.SelectFldVO
 */
public String getExp() {
	return getTAExp().getText().trim();
}
/**
 * 返回 LabelTable1 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLabelFld() {
	if (ivjLabelFld == null) {
		try {
			ivjLabelFld = new nc.ui.pub.beans.UILabel();
			ivjLabelFld.setName("LabelFld");
			ivjLabelFld.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelFld.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000693")/*@res "字段："*/);
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
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLabelFunc() {
	if (ivjLabelFunc == null) {
		try {
			ivjLabelFunc = new nc.ui.pub.beans.UILabel();
			ivjLabelFunc.setName("LabelFunc");
			ivjLabelFunc.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelFunc.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000694")/*@res "函数："*/);
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
 * 返回 LabelTable3 特性值。
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLabelOperator() {
	if (ivjLabelOperator == null) {
		try {
			ivjLabelOperator = new nc.ui.pub.beans.UILabel();
			ivjLabelOperator.setName("LabelOperator");
			ivjLabelOperator.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelOperator.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000940")/*@res "操作符："*/);
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
 * @return nc.ui.pub.beans.UILabel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UILabel getLabelTable() {
	if (ivjLabelTable == null) {
		try {
			ivjLabelTable = new nc.ui.pub.beans.UILabel();
			ivjLabelTable.setName("LabelTable");
			ivjLabelTable.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelTable.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000697")/*@res "表："*/);
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
 * 返回 ListTable2 特性值。
 * @return nc.ui.pub.beans.UIList
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIList getListFunc() {
	if (ivjListFunc == null) {
		try {
			ivjListFunc = new nc.ui.pub.beans.UIList();
			ivjListFunc.setName("ListFunc");
			ivjListFunc.setBounds(0, 0, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjListFunc;
}
/**
 * 返回 ListTable3 特性值。
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
 * 获得字段列表模型
 * 创建日期：(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMFld() {
	return (DefaultListModel) getListFld().getModel();
}
/**
 * 获得函数列表模型
 * 创建日期：(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMFunc() {
	return (DefaultListModel) getListFunc().getModel();
}
/**
 * 获得操作符列表模型
 * 创建日期：(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMOperator() {
	return (DefaultListModel) getListOperator().getModel();
}
/**
 * 获得表列表模型
 * 创建日期：(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMTable() {
	return (DefaultListModel) getListTable().getModel();
}
/**
 * 返回 PnCenter 特性值。
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
			getPnCenter().add(getPnListOperator(), getPnListOperator().getName());
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
	};
	return ivjPnCenterGridLayout;
}
/**
 * 返回 PnListTable1 特性值。
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
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnListFldNorth() {
	if (ivjPnListFldNorth == null) {
		try {
			ivjPnListFldNorth = new nc.ui.pub.beans.UIPanel();
			ivjPnListFldNorth.setName("PnListFldNorth");
			ivjPnListFldNorth.setPreferredSize(new java.awt.Dimension(10, 26));
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
	};
	return ivjPnListFldNorthFlowLayout;
}
/**
 * 返回 PnListTable2 特性值。
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
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnListFuncNorth() {
	if (ivjPnListFuncNorth == null) {
		try {
			ivjPnListFuncNorth = new nc.ui.pub.beans.UIPanel();
			ivjPnListFuncNorth.setName("PnListFuncNorth");
			ivjPnListFuncNorth.setPreferredSize(new java.awt.Dimension(10, 26));
			ivjPnListFuncNorth.setLayout(getPnListFuncNorthFlowLayout());
			getPnListFuncNorth().add(getLabelFunc(), getLabelFunc().getName());
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
	};
	return ivjPnListFuncNorthFlowLayout;
}
/**
 * 返回 PnListTable3 特性值。
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
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnListOperatorNorth() {
	if (ivjPnListOperatorNorth == null) {
		try {
			ivjPnListOperatorNorth = new nc.ui.pub.beans.UIPanel();
			ivjPnListOperatorNorth.setName("PnListOperatorNorth");
			ivjPnListOperatorNorth.setPreferredSize(new java.awt.Dimension(10, 26));
			ivjPnListOperatorNorth.setLayout(getPnListOperatorNorthFlowLayout());
			getPnListOperatorNorth().add(getLabelOperator(), getLabelOperator().getName());
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
	};
	return ivjPnListOperatorNorthFlowLayout;
}
/**
 * 返回 PnListTable 特性值。
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
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnListTableNorth() {
	if (ivjPnListTableNorth == null) {
		try {
			ivjPnListTableNorth = new nc.ui.pub.beans.UIPanel();
			ivjPnListTableNorth.setName("PnListTableNorth");
			ivjPnListTableNorth.setPreferredSize(new java.awt.Dimension(10, 26));
			ivjPnListTableNorth.setLayout(getPnListTableNorthFlowLayout());
			getPnListTableNorth().add(getLabelTable(), getLabelTable().getName());
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
	};
	return ivjPnListTableNorthFlowLayout;
}
/**
 * 返回 UIPanel1 特性值。
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnNorth() {
	if (ivjPnNorth == null) {
		try {
			ivjPnNorth = new nc.ui.pub.beans.UIPanel();
			ivjPnNorth.setName("PnNorth");
			ivjPnNorth.setPreferredSize(new java.awt.Dimension(10, 100));
			ivjPnNorth.setLayout(null);
			getPnNorth().add(getSclPnExp(), getSclPnExp().getName());
			getPnNorth().add(getCbbRela(), getCbbRela().getName());
			getPnNorth().add(getCbbParam(), getCbbParam().getName());
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
 * @return nc.ui.pub.beans.UIPanel
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIPanel getPnSouth() {
	if (ivjPnSouth == null) {
		try {
			ivjPnSouth = new nc.ui.pub.beans.UIPanel();
			ivjPnSouth.setName("PnSouth");
			ivjPnSouth.setLayout(getPnSouthFlowLayout());
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
	};
	return ivjPnSouthFlowLayout;
}
/**
 * 获得比较操作符
 * 创建日期：(2003-4-3 9:09:08)
 * @return nc.vo.iuforeport.businessquery.SelectFldVO
 */
public String getRela() {
	Object objRela = getCbbRela().getSelectedItem();
	return (objRela == null) ? "" : objRela.toString();
}
/**
 * 返回 UIScrollPane1 特性值。
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIScrollPane getSclPnExp() {
	if (ivjSclPnExp == null) {
		try {
			ivjSclPnExp = new nc.ui.pub.beans.UIScrollPane();
			ivjSclPnExp.setName("SclPnExp");
			ivjSclPnExp.setBounds(10, 26, 320, 48);
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
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UIScrollPane getSclPnListFunc() {
	if (ivjSclPnListFunc == null) {
		try {
			ivjSclPnListFunc = new nc.ui.pub.beans.UIScrollPane();
			ivjSclPnListFunc.setName("SclPnListFunc");
			getSclPnListFunc().setViewportView(getListFunc());
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
 * @return nc.ui.pub.beans.UITextArea
 */
/* 警告：此方法将重新生成。 */
private nc.ui.pub.beans.UITextArea getTAExp() {
	if (ivjTAExp == null) {
		try {
			ivjTAExp = new nc.ui.pub.beans.UITextArea();
			ivjTAExp.setName("TAExp");
			ivjTAExp.setLineWrap(true);
			ivjTAExp.setFont(new java.awt.Font("serif", 0, 14));
			ivjTAExp.setText("");
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
 * 返回 UIDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JPanel getUIDialogContentPane() {
	if (ivjUIDialogContentPane == null) {
		try {
			ivjUIDialogContentPane = new javax.swing.JPanel();
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
 * 获得取值
 * 创建日期：(2003-4-3 9:09:08)
 * @return nc.vo.iuforeport.businessquery.SelectFldVO
 */
public String getVal() {
	Object objVal = getCbbParam().getSelectedItem();
	return (objVal == null) ? "" : objVal.toString();
}
/**
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	 AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
	AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
}
/**
 * 初始化连接
 * @exception java.lang.Exception 异常说明。
 */
/* 警告：此方法将重新生成。 */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getBnCancel().addActionListener(ivjEventHandler);
	getBnOK().addActionListener(ivjEventHandler);
	getListTable().addMouseListener(ivjEventHandler);
	getListFld().addMouseListener(ivjEventHandler);
	getListFunc().addMouseListener(ivjEventHandler);
	getListOperator().addMouseListener(ivjEventHandler);
	getBnClear().addActionListener(ivjEventHandler);
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
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000699")/*@res "生成表达式"*/);
		setSize(560, 320);
		setResizable(false);
		setContentPane(getUIDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	initList();
	getListFld().setCellRenderer(new DataTypeCellRenderer());
	// user code end
}
/**
 * 初始化列表
 * 创建日期：(2003-4-2 19:56:10)
 */
public void initList() {
	//设置列表模型
	getListTable().setModel(new DefaultListModel());
	getListFld().setModel(new DefaultListModel());
	DefaultListModel lm = new DefaultListModel();
	if (m_parent instanceof SetCondPanel) {
		lm.addElement(" and ");
		lm.addElement(" or ");
	}
	getListFunc().setModel(lm);
	lm = new DefaultListModel();
	if (m_parent instanceof SetCondPanel) {
		lm.addElement("=");
		lm.addElement(">");
		lm.addElement("<");
		lm.addElement(">=");
		lm.addElement("<=");
		lm.addElement("<>");
		lm.addElement("(");
		lm.addElement(")");
	}
	getListOperator().setModel(lm);
	//设置单选模式
	getListTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	getListFld().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	getListFunc().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	getListOperator().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	//设置关系符下拉框初始值
	Object[] objRelas =
		new Object[] { "=", ">", "<", "<>", ">=", "<=", "like", "in" };
	for (int i = 0; i < objRelas.length; i++)
		getCbbRela().addItem(objRelas[i]);
}
/**
 * Invoked when a key has been released.
 */
public void keyReleased(java.awt.event.KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_F12) {
//		QryCondRefDlg dlg = null;
//		//获得父组件的信息
//		if (m_parent instanceof SetCondPanel) {
//			SetCondPanel pn = ((SetCondPanel) m_parent);
//			dlg = pn.getTabPn().getRefDlg();
//		}
//		//基础参照快捷录入
//		if (dlg != null) {
//			dlg.showModal();
//			dlg.destroy();
//			if (dlg.getResult() == UIDialog.ID_OK)
//				//填入参照结果
//				onSelect(dlg.getValue(), 0);
//		}
	}
}
/**
 * 字段列表点击响应
 */
public void listFld_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int iClickCount = mouseEvent.getClickCount();
	if (iClickCount == 2) {
		//获得选中字段
		String str = null;
		int iIndex = getListFld().getSelectedIndex();
		if (iIndex != -1) {
			SelectFldVO sf = (SelectFldVO) getLMFld().getElementAt(iIndex);
			str = sf.getFldalias();
			//获得选中表
			iIndex = getListTable().getSelectedIndex();
			if (iIndex != -1) {
				//获得选中表
				FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);
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
 * 函数列表点击响应
 */
public void listFunc_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int iClickCount = mouseEvent.getClickCount();
	if (iClickCount == 2) {
		//获得选中函数
		String sel = null;
		int iIndex = getListFunc().getSelectedIndex();
		if (iIndex != -1)
			sel = getLMFunc().getElementAt(iIndex).toString();
		//选中
		if (sel != null) {
			int iFld = 1;
			onSelect(sel, iFld);
		}
	}
	return;
}
/**
 * 操作符列表点击响应
 */
public void listOperator_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
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
public void listTable_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int iIndex = getListTable().getSelectedIndex();
	if (iIndex != -1) {
		//获得选中表
		FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);

		//获得数据字典实例
		AbstractQueryDesignTabPanel tabPn = null;
		if (m_parent instanceof SetCondPanel)
			tabPn = ((SetCondPanel) m_parent).getTabPn();
		ObjectTree dd = tabPn.getDatadict();
		String dsName = tabPn.getDefDsName();

		//查询对应的字段
		ObjectTree tree =
			(QueryUtil.isTempTable(ft.getTablecode()))
				? QueryModelTree.getInstance(dsName)
				: dd;
		SelectFldVO[] sfs =
			QueryUtil.getFldsFromTable(ft.getTablecode(), ft.getTablealias(), tree);
		//设置字段列表
		if (sfs != null)
			resetFld(sfs);
	}
	return;
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 * @i18n miufo00295=nc.ui.pub.beans.UIDialog 的 main() 中发生异常
 */
public static void main(java.lang.String[] args) {
	try {
		FldGenDlg aFldGenDlg;
		aFldGenDlg = new FldGenDlg();
		aFldGenDlg.setModal(true);
		aFldGenDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aFldGenDlg.setVisible(true);
		java.awt.Insets insets = aFldGenDlg.getInsets();
		aFldGenDlg.setSize(aFldGenDlg.getWidth() + insets.left + insets.right, aFldGenDlg.getHeight() + insets.top + insets.bottom);
		aFldGenDlg.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("nc.ui.pub.beans.UIDialog 的 main() 中发生异常");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * 双击选中
 * 创建日期：(2003-4-2 20:38:58)
 * @param str java.lang.String
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
 * 重置字段列表
 * 创建日期：(2003-4-2 20:11:19)
 * @param tds nc.bs.com.datadict.TableDef[]
 */
private void resetFld(SelectFldVO[] sfs) {
	getLMFld().removeAllElements();
	if (sfs != null)
		for (int i = 0; i < sfs.length; i++)
			getLMFld().addElement(sfs[i]);
}
/**
 * 重置表列表
 * 创建日期：(2003-4-2 20:11:19)
 * @param tds nc.bs.com.datadict.TableDef[]
 */
private void resetTable(FromTableVO[] fts) {
	getLMTable().removeAllElements();
	if (fts != null)
		for (int i = 0; i < fts.length; i++)
			getLMTable().addElement(fts[i]);
}
/**
 * 设置查询基本定义和参数
 * 创建日期：(2003-4-2 18:34:06)
 * @return nc.vo.iuforeport.businessquery.QueryBaseDef
 */
public void setQueryBaseDef(QueryBaseDef qbd, ParamVO[] params) {
	m_qbd = qbd;
	//设置表
	FromTableVO[] fts = m_qbd.getFromTables();
	resetTable(fts);
	//设置参数下拉框初始值
	getCbbParam().removeAllItems();
	int iLen = (params == null) ? 0 : params.length;
	for (int i = 0; i < iLen; i++)
		getCbbParam().addItem(params[i].getParamCode());
}
}

  