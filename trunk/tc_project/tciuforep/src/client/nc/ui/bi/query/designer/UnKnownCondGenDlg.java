/*
 * �������� 2006-10-31
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
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
 * �����������ʽ���ɶԻ���
 * �������ڣ�(2003-4-2 19:24:22)
 * @author���쿡��
 */
public class UnKnownCondGenDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//��ѯ��������
	private QueryBaseDef m_qbd = null;
	//�����
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
 * FldGenDlg ������ע�⡣
 * @deprecated
 */
public UnKnownCondGenDlg() {
	super();
	initialize();
}
/**
 * FldGenDlg ������ע�⡣
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
		MessageDialog.showWarningDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000099")/*@res "��ѯ����"*/, strErr);
		return;
	}
	closeOK();
}
/**
 * �Ϸ��Լ��
 * �������ڣ�(2003-4-4 13:57:49)
 * @return java.lang.String
 */
private String check() {
	if (getExp().equals(""))
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000938")/*@res "���ʽ����Ϊ��"*/;
	if (getVal().equals(""))
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000939")/*@res "��������Ϊ��"*/;
	return null;
}
/**
 * connEtoC1:  (BnCancel.action.actionPerformed(java.awt.event.ActionEvent) --> FldGenDlg.bnCancel_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* ���棺�˷������������ɡ� */
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
/* ���棺�˷������������ɡ� */
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
/* ���棺�˷������������ɡ� */
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
/* ���棺�˷������������ɡ� */
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
/* ���棺�˷������������ɡ� */
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
/* ���棺�˷������������ɡ� */
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
/* ���棺�˷������������ɡ� */
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
 * ����������Ϣ
 * �������ڣ�(2003-4-3 10:13:00)
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
 * ���� BnCancel ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getBnCancel() {
	if (ivjBnCancel == null) {
		try {
			ivjBnCancel = new nc.ui.pub.beans.UIButton();
			ivjBnCancel.setName("BnCancel");
			ivjBnCancel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000000")/*@res "ȡ��"*/);
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
 * ���� BnClear ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getBnClear() {
	if (ivjBnClear == null) {
		try {
			ivjBnClear = new nc.ui.pub.beans.UIButton();
			ivjBnClear.setName("BnClear");
			ivjBnClear.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000318")/*@res "���"*/);
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
 * ���� BnOK ����ֵ��
 * @return nc.ui.pub.beans.UIButton
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UIButton getBnOK() {
	if (ivjBnOK == null) {
		try {
			ivjBnOK = new nc.ui.pub.beans.UIButton();
			ivjBnOK.setName("BnOK");
			ivjBnOK.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000001")/*@res "ȷ��"*/);
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
 * ���� CbbParam ����ֵ��
 * @return nc.ui.pub.beans.UIComboBox
 */
/* ���棺�˷������������ɡ� */
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
 * ���� CbbRela ����ֵ��
 * @return nc.ui.pub.beans.UIComboBox
 */
/* ���棺�˷������������ɡ� */
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
 * ������ɵı��ʽ
 * �������ڣ�(2003-4-3 9:09:08)
 * @return nc.vo.iuforeport.businessquery.SelectFldVO
 */
public String getExp() {
	return getTAExp().getText().trim();
}
/**
 * ���� LabelTable1 ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getLabelFld() {
	if (ivjLabelFld == null) {
		try {
			ivjLabelFld = new nc.ui.pub.beans.UILabel();
			ivjLabelFld.setName("LabelFld");
			ivjLabelFld.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelFld.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000693")/*@res "�ֶΣ�"*/);
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
 * ���� LabelTable2 ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getLabelFunc() {
	if (ivjLabelFunc == null) {
		try {
			ivjLabelFunc = new nc.ui.pub.beans.UILabel();
			ivjLabelFunc.setName("LabelFunc");
			ivjLabelFunc.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelFunc.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000694")/*@res "������"*/);
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
 * ���� LabelTable3 ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getLabelOperator() {
	if (ivjLabelOperator == null) {
		try {
			ivjLabelOperator = new nc.ui.pub.beans.UILabel();
			ivjLabelOperator.setName("LabelOperator");
			ivjLabelOperator.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelOperator.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000940")/*@res "��������"*/);
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
 * ���� LabelTable ����ֵ��
 * @return nc.ui.pub.beans.UILabel
 */
/* ���棺�˷������������ɡ� */
private nc.ui.pub.beans.UILabel getLabelTable() {
	if (ivjLabelTable == null) {
		try {
			ivjLabelTable = new nc.ui.pub.beans.UILabel();
			ivjLabelTable.setName("LabelTable");
			ivjLabelTable.setPreferredSize(new java.awt.Dimension(100, 22));
			ivjLabelTable.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000697")/*@res "��"*/);
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
 * ���� ListTable1 ����ֵ��
 * @return nc.ui.pub.beans.UIList
 */
/* ���棺�˷������������ɡ� */
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
 * ���� ListTable2 ����ֵ��
 * @return nc.ui.pub.beans.UIList
 */
/* ���棺�˷������������ɡ� */
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
 * ���� ListTable3 ����ֵ��
 * @return nc.ui.pub.beans.UIList
 */
/* ���棺�˷������������ɡ� */
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
 * ���� ListTable ����ֵ��
 * @return nc.ui.pub.beans.UIList
 */
/* ���棺�˷������������ɡ� */
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
 * ����ֶ��б�ģ��
 * �������ڣ�(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMFld() {
	return (DefaultListModel) getListFld().getModel();
}
/**
 * ��ú����б�ģ��
 * �������ڣ�(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMFunc() {
	return (DefaultListModel) getListFunc().getModel();
}
/**
 * ��ò������б�ģ��
 * �������ڣ�(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMOperator() {
	return (DefaultListModel) getListOperator().getModel();
}
/**
 * ��ñ��б�ģ��
 * �������ڣ�(2003-4-2 20:18:29)
 * @return javax.swing.DefaultListModel
 */
private DefaultListModel getLMTable() {
	return (DefaultListModel) getListTable().getModel();
}
/**
 * ���� PnCenter ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnCenterGridLayout ����ֵ��
 * @return java.awt.GridLayout
 */
/* ���棺�˷������������ɡ� */
private java.awt.GridLayout getPnCenterGridLayout() {
	java.awt.GridLayout ivjPnCenterGridLayout = null;
	try {
		/* �������� */
		ivjPnCenterGridLayout = new java.awt.GridLayout();
		ivjPnCenterGridLayout.setHgap(5);
		ivjPnCenterGridLayout.setColumns(4);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjPnCenterGridLayout;
}
/**
 * ���� PnListTable1 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListTableNorth1 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListFldNorthFlowLayout ����ֵ��
 * @return java.awt.FlowLayout
 */
/* ���棺�˷������������ɡ� */
private java.awt.FlowLayout getPnListFldNorthFlowLayout() {
	java.awt.FlowLayout ivjPnListFldNorthFlowLayout = null;
	try {
		/* �������� */
		ivjPnListFldNorthFlowLayout = new java.awt.FlowLayout();
		ivjPnListFldNorthFlowLayout.setVgap(2);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjPnListFldNorthFlowLayout;
}
/**
 * ���� PnListTable2 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListTableNorth2 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListFuncNorthFlowLayout ����ֵ��
 * @return java.awt.FlowLayout
 */
/* ���棺�˷������������ɡ� */
private java.awt.FlowLayout getPnListFuncNorthFlowLayout() {
	java.awt.FlowLayout ivjPnListFuncNorthFlowLayout = null;
	try {
		/* �������� */
		ivjPnListFuncNorthFlowLayout = new java.awt.FlowLayout();
		ivjPnListFuncNorthFlowLayout.setVgap(2);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjPnListFuncNorthFlowLayout;
}
/**
 * ���� PnListTable3 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListTableNorth3 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListOperatorNorthFlowLayout ����ֵ��
 * @return java.awt.FlowLayout
 */
/* ���棺�˷������������ɡ� */
private java.awt.FlowLayout getPnListOperatorNorthFlowLayout() {
	java.awt.FlowLayout ivjPnListOperatorNorthFlowLayout = null;
	try {
		/* �������� */
		ivjPnListOperatorNorthFlowLayout = new java.awt.FlowLayout();
		ivjPnListOperatorNorthFlowLayout.setVgap(2);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjPnListOperatorNorthFlowLayout;
}
/**
 * ���� PnListTable ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListTableNorth ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnListTableNorthFlowLayout ����ֵ��
 * @return java.awt.FlowLayout
 */
/* ���棺�˷������������ɡ� */
private java.awt.FlowLayout getPnListTableNorthFlowLayout() {
	java.awt.FlowLayout ivjPnListTableNorthFlowLayout = null;
	try {
		/* �������� */
		ivjPnListTableNorthFlowLayout = new java.awt.FlowLayout();
		ivjPnListTableNorthFlowLayout.setVgap(2);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjPnListTableNorthFlowLayout;
}
/**
 * ���� UIPanel1 ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnSouth ����ֵ��
 * @return nc.ui.pub.beans.UIPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���� PnSouthFlowLayout ����ֵ��
 * @return java.awt.FlowLayout
 */
/* ���棺�˷������������ɡ� */
private java.awt.FlowLayout getPnSouthFlowLayout() {
	java.awt.FlowLayout ivjPnSouthFlowLayout = null;
	try {
		/* �������� */
		ivjPnSouthFlowLayout = new java.awt.FlowLayout();
		ivjPnSouthFlowLayout.setVgap(8);
		ivjPnSouthFlowLayout.setHgap(20);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjPnSouthFlowLayout;
}
/**
 * ��ñȽϲ�����
 * �������ڣ�(2003-4-3 9:09:08)
 * @return nc.vo.iuforeport.businessquery.SelectFldVO
 */
public String getRela() {
	Object objRela = getCbbRela().getSelectedItem();
	return (objRela == null) ? "" : objRela.toString();
}
/**
 * ���� UIScrollPane1 ����ֵ��
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* ���棺�˷������������ɡ� */
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
 * ���� SclPnListTable1 ����ֵ��
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* ���棺�˷������������ɡ� */
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
 * ���� SclPnListTable2 ����ֵ��
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* ���棺�˷������������ɡ� */
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
 * ���� SclPnListTable3 ����ֵ��
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* ���棺�˷������������ɡ� */
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
 * ���� SclPnListTable ����ֵ��
 * @return nc.ui.pub.beans.UIScrollPane
 */
/* ���棺�˷������������ɡ� */
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
 * ���� TAExp ����ֵ��
 * @return nc.ui.pub.beans.UITextArea
 */
/* ���棺�˷������������ɡ� */
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
 * ���� UIDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
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
 * ���ȡֵ
 * �������ڣ�(2003-4-3 9:09:08)
 * @return nc.vo.iuforeport.businessquery.SelectFldVO
 */
public String getVal() {
	Object objVal = getCbbParam().getSelectedItem();
	return (objVal == null) ? "" : objVal.toString();
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	 AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
	AppDebug.debug(exception);//@devTools  exception.printStackTrace(System.out);
}
/**
 * ��ʼ������
 * @exception java.lang.Exception �쳣˵����
 */
/* ���棺�˷������������ɡ� */
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
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("FldGenDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201","UPP10241201-000699")/*@res "���ɱ��ʽ"*/);
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
 * ��ʼ���б�
 * �������ڣ�(2003-4-2 19:56:10)
 */
public void initList() {
	//�����б�ģ��
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
	//���õ�ѡģʽ
	getListTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	getListFld().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	getListFunc().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	getListOperator().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	//���ù�ϵ���������ʼֵ
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
//		//��ø��������Ϣ
//		if (m_parent instanceof SetCondPanel) {
//			SetCondPanel pn = ((SetCondPanel) m_parent);
//			dlg = pn.getTabPn().getRefDlg();
//		}
//		//�������տ��¼��
//		if (dlg != null) {
//			dlg.showModal();
//			dlg.destroy();
//			if (dlg.getResult() == UIDialog.ID_OK)
//				//������ս��
//				onSelect(dlg.getValue(), 0);
//		}
	}
}
/**
 * �ֶ��б�����Ӧ
 */
public void listFld_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int iClickCount = mouseEvent.getClickCount();
	if (iClickCount == 2) {
		//���ѡ���ֶ�
		String str = null;
		int iIndex = getListFld().getSelectedIndex();
		if (iIndex != -1) {
			SelectFldVO sf = (SelectFldVO) getLMFld().getElementAt(iIndex);
			str = sf.getFldalias();
			//���ѡ�б�
			iIndex = getListTable().getSelectedIndex();
			if (iIndex != -1) {
				//���ѡ�б�
				FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);
				str = ft.getTablealias() + "." + str;
			}
		}
		//ѡ��
		if (str != null)
			onSelect(str, 0);
	}
	return;
}
/**
 * �����б�����Ӧ
 */
public void listFunc_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int iClickCount = mouseEvent.getClickCount();
	if (iClickCount == 2) {
		//���ѡ�к���
		String sel = null;
		int iIndex = getListFunc().getSelectedIndex();
		if (iIndex != -1)
			sel = getLMFunc().getElementAt(iIndex).toString();
		//ѡ��
		if (sel != null) {
			int iFld = 1;
			onSelect(sel, iFld);
		}
	}
	return;
}
/**
 * �������б�����Ӧ
 */
public void listOperator_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int iClickCount = mouseEvent.getClickCount();
	if (iClickCount == 2) {
		//���ѡ�к���
		String sel = null;
		int iIndex = getListOperator().getSelectedIndex();
		if (iIndex != -1)
			sel = getLMOperator().getElementAt(iIndex).toString();
		//ѡ��
		if (sel != null)
			onSelect(sel, 0);
	}
	return;
}
/**
 * ������б���Ӧ
 */
public void listTable_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int iIndex = getListTable().getSelectedIndex();
	if (iIndex != -1) {
		//���ѡ�б�
		FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);

		//��������ֵ�ʵ��
		AbstractQueryDesignTabPanel tabPn = null;
		if (m_parent instanceof SetCondPanel)
			tabPn = ((SetCondPanel) m_parent).getTabPn();
		ObjectTree dd = tabPn.getDatadict();
		String dsName = tabPn.getDefDsName();

		//��ѯ��Ӧ���ֶ�
		ObjectTree tree =
			(QueryUtil.isTempTable(ft.getTablecode()))
				? QueryModelTree.getInstance(dsName)
				: dd;
		SelectFldVO[] sfs =
			QueryUtil.getFldsFromTable(ft.getTablecode(), ft.getTablealias(), tree);
		//�����ֶ��б�
		if (sfs != null)
			resetFld(sfs);
	}
	return;
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 * @i18n miufo00295=nc.ui.pub.beans.UIDialog �� main() �з����쳣
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
		System.err.println("nc.ui.pub.beans.UIDialog �� main() �з����쳣");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * ˫��ѡ��
 * �������ڣ�(2003-4-2 20:38:58)
 * @param str java.lang.String
 */
private void onSelect(String str, int iFunc) {
	//ѡ��
	int pos = getTAExp().getSelectionStart();
	getTAExp().insert(str, pos);
	getTAExp().requestFocus();
	getTAExp().setSelectionStart(pos + str.length() - iFunc);
	getTAExp().setSelectionEnd(pos + str.length() - iFunc);
}
/**
 * �����ֶ��б�
 * �������ڣ�(2003-4-2 20:11:19)
 * @param tds nc.bs.com.datadict.TableDef[]
 */
private void resetFld(SelectFldVO[] sfs) {
	getLMFld().removeAllElements();
	if (sfs != null)
		for (int i = 0; i < sfs.length; i++)
			getLMFld().addElement(sfs[i]);
}
/**
 * ���ñ��б�
 * �������ڣ�(2003-4-2 20:11:19)
 * @param tds nc.bs.com.datadict.TableDef[]
 */
private void resetTable(FromTableVO[] fts) {
	getLMTable().removeAllElements();
	if (fts != null)
		for (int i = 0; i < fts.length; i++)
			getLMTable().addElement(fts[i]);
}
/**
 * ���ò�ѯ��������Ͳ���
 * �������ڣ�(2003-4-2 18:34:06)
 * @return nc.vo.iuforeport.businessquery.QueryBaseDef
 */
public void setQueryBaseDef(QueryBaseDef qbd, ParamVO[] params) {
	m_qbd = qbd;
	//���ñ�
	FromTableVO[] fts = m_qbd.getFromTables();
	resetTable(fts);
	//���ò����������ʼֵ
	getCbbParam().removeAllItems();
	int iLen = (params == null) ? 0 : params.length;
	for (int i = 0; i < iLen; i++)
		getCbbParam().addItem(params[i].getParamCode());
}
}

  