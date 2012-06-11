/*
 * �������� 2005-5-24
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
 * �����������
 */
public class SetCrossPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0053";//"��������";

	//���ɱ��ʽ�Ի���ʵ��
	private FldGenDlg m_fldGenDlg = null;

	//��Ԫ�༭������
	private UIRefCellEditor[] m_refEditors = null;

	//����
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
	 * SetTablePanel ������ע�⡣
	 */
	public SetCrossPanel() {
		super();
		initialize();
	}

	/**
	 * ���� �������ڣ�(2003-8-8 15:14:24)
	 */
	private void addLine(String strWhere) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//�������
		int iIndex = getTM().getRowCount();
		Object[] objrows = new Object[] {
				"",
				"",
				"",
				strWhere,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000878") /* @res "��ͷ" */};
		getTM().addRow(objrows);
		//ѡ��������
		getTable().getSelectionModel().setSelectionInterval(iIndex, iIndex);

		/* ����Ϊһ���Դ��� */
		SimpleCrossVO newsc = new SimpleCrossVO();
		//�������
		newsc.setFldCode("");
		newsc.setFldName("");
		//����
		newsc.setWhereCond(strWhere);
		//λ��
		newsc.setLocate(QueryConst.CROSS_COL);
		//
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		SimpleCrossVO[] oldscs = qbd.getScs();
		//�ϲ�VO����
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
		//�����Ի���
		FldGenDlg dlg = getFldGenDlg();
		//���
		dlg.doClear();
		dlg.setBnAddEnabled(false);
		dlg.setSomeInvisibled();
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//��������ֶ�
			String strWhere = dlg.getExp();
			//���ӱ���
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
			//����ˢ��
			delLine();

			/* ����Ϊһ���Դ��� */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//�ϲ�VO����
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
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//
		int iSelIndex = getTable().getSelectedRow();
		getTable().scrollRectToVisible(
				getTable().getCellRect(iSelIndex, 0, false));
		if (iSelIndex != -1 && iSelIndex < getTable().getRowCount() - 1) {
			//����
			getTM().moveRow(iSelIndex, iSelIndex, iSelIndex + 1);
			//ѡ��
			getTable().getSelectionModel().setSelectionInterval(iSelIndex + 1,
					iSelIndex + 1);

			/* ����Ϊһ���Դ��� */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//�ϲ�VO����
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
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//�����Ի���
			FldGenDlg dlg = getFldGenDlg();
			//���
			dlg.doSetInfo(new Object[] { null, null,
					getTM().getValueAt(iSelIndex, COL_FLDWHERE), null });
			dlg.setBnAddEnabled(false);
			dlg.setSomeInvisibled();
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				String strWhere = dlg.getExp();
				//��ֹ�༭̬
				if (getTable().getCellEditor() != null)
					getTable().getCellEditor().stopCellEditing();
				//����
				getTM().setValueAt(strWhere, iSelIndex, COL_FLDWHERE);
				//ѡ��
				getTable().getSelectionModel().setSelectionInterval(iSelIndex,
						iSelIndex);

				/* ����Ϊһ���Դ��� */
				QueryBaseDef qbd = getTabPn().getQueryBaseDef();
				//�ϲ�VO����
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
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//
		int iSelIndex = getTable().getSelectedRow();
		getTable().scrollRectToVisible(
				getTable().getCellRect(iSelIndex, 0, false));
		if (iSelIndex > 0) {
			//����
			getTM().moveRow(iSelIndex, iSelIndex, iSelIndex - 1);
			//ѡ��
			getTable().getSelectionModel().setSelectionInterval(iSelIndex - 1,
					iSelIndex - 1);

			/* ����Ϊһ���Դ��� */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//�ϲ�VO����
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
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
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
							"UPP10241201-000879")/* @res "���벻��Ϊ��" */;
				if (getTM().getValueAt(i, COL_FLDNAME) == null
						|| getTM().getValueAt(i, COL_FLDNAME).toString().trim()
								.equals(""))
					return NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000880")/* @res "���Ʋ���Ϊ��" */;
				//if (getTM().getValueAt(i, COL_FLDWHERE) == null
				//|| getTM().getValueAt(i,
				// COL_FLDWHERE).toString().trim().equals(""))
				//return "��������Ϊ��";
				if (vec.indexOf(strCode) != -1)
					return NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000881")/* @res "���벻���ظ�" */;
				vec.addElement(strCode);
				String strLocate = getTM().getValueAt(i, COL_FLDLOCATE)
						.toString();
				if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000882")/* @res "��ͷ" */))
					bRow = true;
				if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000878")/* @res "��ͷ" */))
					bCol = true;
			}
			if (!bRow)
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000883")/* @res "����û����ͷ��" */;
			if (!bCol)
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000884")/* @res "����û����ͷ��" */;
			return null;
		} else if (isCrossValid() == QueryConst.CROSSTYPE_ROTATE)
			return getPnRotateCross().check(true);
		else
			return null;
	}

	/**
	 * �����ת������� �������ڣ�(2003-11-25 11:31:58)
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ��ת������Ӧ �������ڣ�(2003-11-25 13:30:13)
	 */
	private void convertUI_rotate() {
		//����ˢ��
		remove(getPnWest());
		remove(getTablePn());
		remove(getPnEast());
		add(getPnRotateCross(), BorderLayout.CENTER);
		validate();
		repaint();
		//��ť����
		getPnRotateCross().setBnEnabled(true);
	}

	/**
	 * ͶӰ������Ӧ �������ڣ�(2003-11-25 13:30:13)
	 */
	private void convertUI_simple() {

		//����ˢ��
		remove(getPnRotateCross());
		add(getPnWest(), BorderLayout.WEST);
		add(getTablePn(), BorderLayout.CENTER);
		add(getPnEast(), BorderLayout.EAST);
		validate();
		repaint();
		//��ť����
		setBnEnabled(true);
	}

	/**
	 * ɾ�� �������ڣ�(2003-8-8 15:14:24)
	 */
	private void delLine() {
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1) {
			//����ˢ��
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable().getRowCount() != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);
		}
	}

	/**
	 * ���� BnAdd ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new UIButton();
				ivjBnAdd.setName("BnAdd");
				ivjBnAdd.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000330")/* @res "����" */);
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
	 * ���� BnDel ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnDel() {
		if (ivjBnDel == null) {
			try {
				ivjBnDel = new UIButton();
				ivjBnDel.setName("BnDel");
				ivjBnDel.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000091")/* @res "ɾ��" */);
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
	 * ���� BnDown ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnDown() {
		if (ivjBnDown == null) {
			try {
				ivjBnDown = new UIButton();
				ivjBnDown.setName("BnDown");
				ivjBnDown.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000146")/* @res "����" */);
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
	 * ���� BnDel ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnModify() {
		if (ivjBnModify == null) {
			try {
				ivjBnModify = new UIButton();
				ivjBnModify.setName("BnModify");
				ivjBnModify.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000401")/* @res "�޸�" */);
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
	 * ���� BnUp ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnUp() {
		if (ivjBnUp == null) {
			try {
				ivjBnUp = new UIButton();
				ivjBnUp.setName("BnUp");
				ivjBnUp.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000145")/* @res "����" */);
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
	 * ���� ButtonGroup ����ֵ��
	 * 
	 * @return javax.swing.ButtonGroup
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ������ɱ��ʽ�Ի���ʵ�� �������ڣ�(2003-4-3 9:47:03)
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
	 * ���� PnEast ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnEast1 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnEast2 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnEast3 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnEast4 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnEast5 ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnEastGridLayout ����ֵ��
	 * 
	 * @return java.awt.GridLayout
	 */
	/* ���棺�˷������������ɡ� */
	private java.awt.GridLayout getPnEastGridLayout() {
		java.awt.GridLayout ivjPnEastGridLayout = null;
		try {
			/* �������� */
			ivjPnEastGridLayout = new java.awt.GridLayout(5, 1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnEastGridLayout;
	}

	/**
	 * ���� PnNorth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnRotateCross ����ֵ��
	 * 
	 * @return nc.ui.iuforeport.businessquery.RotateCrossPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * �����ת������� �������ڣ�(2003-11-25 11:31:58)
	 * 
	 * @return nc.ui.iuforeport.businessquery.RotateCrossPanel
	 */
	public RotateCrossPanel getPnRotateCrossPub() {
		return getPnRotateCross();
	}

	/**
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnWest ����ֵ��
	 * 
	 * @return UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� RadioBnNone ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnNone() {
		if (ivjRadioBnNone == null) {
			try {
				ivjRadioBnNone = new UIRadioButton();
				ivjRadioBnNone.setName("RadioBnNone");
				ivjRadioBnNone.setPreferredSize(new java.awt.Dimension(68, 22));
				ivjRadioBnNone.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000885")/* @res "������" */);
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
	 * ���� RadioBnRotate ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnRotate() {
		if (ivjRadioBnRotate == null) {
			try {
				ivjRadioBnRotate = new UIRadioButton();
				ivjRadioBnRotate.setName("RadioBnRotate");
				ivjRadioBnRotate
						.setPreferredSize(new java.awt.Dimension(80, 22));
				ivjRadioBnRotate.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000077")/* @res "��ת����" */);
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
	 * ���� RadioBnCross ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnSimple() {
		if (ivjRadioBnSimple == null) {
			try {
				ivjRadioBnSimple = new UIRadioButton();
				ivjRadioBnSimple.setName("RadioBnSimple");
				ivjRadioBnSimple
						.setPreferredSize(new java.awt.Dimension(80, 22));
				ivjRadioBnSimple.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000886")/* @res "ͶӰ����" */);
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
	 * ���ͶӰ���涨�� �������ڣ�(2003-8-8 14:57:25)
	 * 
	 * @param scs
	 *            nc.vo.iuforeport.businessquery.ParamVO[]
	 */
	public RotateCrossVO getResultFromCross_rotate() {
		return getPnRotateCross().getRotateCross();
	}

	/**
	 * ��ü򵥽��涨������ �������ڣ�(2003-8-8 14:57:25)
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
				//�������
				scs[i]
						.setFldCode(getTM().getValueAt(i, COL_FLDCODE)
								.toString());
				scs[i]
						.setFldName(getTM().getValueAt(i, COL_FLDNAME)
								.toString());
				//����
				scs[i].setWhereCond(getTM().getValueAt(i, COL_FLDWHERE)
						.toString());
				//λ��
				String strLocate = getTM().getValueAt(i, COL_FLDLOCATE)
						.toString();
				int iLocate = QueryConst.CROSS_NONE;
				if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000882")/* @res "��ͷ" */))
					iLocate = QueryConst.CROSS_ROW;
				else if (strLocate.equals(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000878")/* @res "��ͷ" */))
					iLocate = QueryConst.CROSS_COL;
				scs[i].setLocate(iLocate);
			}
		}
		return scs;
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		return (UITable) getTablePn().getTable();
	}

	/**
	 * ���� TablePn ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ������
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			//���ӻ��洢��ע���޸�getCheckBoxCross()����
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
		//��ť��ѡ
		getButtonGroup().add(getRadioBnNone());
		getButtonGroup().add(getRadioBnSimple());
		getButtonGroup().add(getRadioBnRotate());
		getRadioBnNone().setSelected(true);
		//��ʼ��
		initRefValue();
		initTable();
		setTableCell();
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 92, 108, 228, 96 });
		// user code end
	}

	/**
	 * ��ʼ��ֵ��CellEditor �������ڣ�(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	private void initRefValue() {

		//��ʼ����Ԫ�༭��
		m_refEditors = new UIRefCellEditor[1];
		//
		//JComboBox cbb = new UIComboBox();
		//cbb.addItem("����");
		//cbb.addItem("С��");
		//cbb.addItem("�ַ�");
		//m_refEditors[0] = new UIRefCellEditor(cbb);
		//
		JComboBox cbb = new UIComboBox();
		cbb.addItem(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-000878")/* @res "��ͷ" */);
		cbb.addItem(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-000882")/* @res "��ͷ" */);
		cbb.addItem(NCLangRes.getInstance().getStrByID("10241201",
				"UPP10241201-000887")/* @res "�˻�" */);
		m_refEditors[0] = new UIRefCellEditor(cbb);
	}

	/**
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable() {
		//��ģ��
		DefaultTableModel tm = new DefaultTableModel(
				new Object[] {
						"",
						NCLangRes.getInstance().getStrByID("10241201",
								"UC000-0003279")/* @res "����" */,
						NCLangRes.getInstance().getStrByID("10241201",
								"UC000-0001155")/* @res "����" */,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000843")/* @res "ɸѡ����" */,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000888") /* @res "λ��" */}, 0) {
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
														  * @res "λ��"
														  */).setCellEditor(m_refEditors[0]);
				return true;
			}
		};
		getTable().setModel(tm);
		//���ñ�����
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
	}

	/**
	 * �ж������Ƿ���Ч �������ڣ�(2003-11-8 16:25:38)
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
	 * ������
	 */
	public void radioBnNone_ActionPerformed(ActionEvent actionEvent) {

		setBnEnabled(false);
		getPnRotateCross().setBnEnabled(false);
	}

	/**
	 * ��ת����
	 */
	public void radioBnRotate_ActionPerformed(ActionEvent actionEvent) {

		convertUI_rotate();
	}

	/**
	 * ͶӰ����
	 */
	public void radioBnSimple_ActionPerformed(ActionEvent actionEvent) {

		convertUI_simple();
	}

	/**
	 * ���ð�ť������ �������ڣ�(2003-11-8 16:30:08)
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
	 * ��� �������ڣ�(2003-4-3 9:47:03)
	 * 
	 * @return nc.ui.iuforeport.businessquery.FldGenDlg
	 */
	public void setFldGenDlgAsNull() {
		m_fldGenDlg = null;
	}

	/**
	 * ���ý��涨�� �������ڣ�(2003-9-24 15:32:38)
	 * 
	 * @param cp
	 *            nc.vo.pub.codingwiz.CodeParamVO[]
	 */
	public void setResultToCross(SimpleCrossVO[] scs, RotateCrossVO rc) {
		//ͶӰ����
		if (scs != null && scs.length != 0) {
			getRadioBnSimple().setSelected(true);
			setBnEnabled(true);
			setResultToCross_simple(scs);
			return;
		}
		//��ת����
		SelectFldVO[] sfs = ((SetFldPanel) getTabPn().getSetPanel(
				SetFldPanel.TAB_TITLE)).getResultFromFld();
		getPnRotateCross().setRotateCross(rc, sfs);
		if (rc != null) {
			getRadioBnRotate().setSelected(true);
			convertUI_rotate();
		}
	}

	/**
	 * ���ü򵥽��涨������ �������ڣ�(2003-9-24 15:32:38)
	 * 
	 * @param cp
	 *            nc.vo.pub.codingwiz.CodeParamVO[]
	 */
	public void setResultToCross_simple(SimpleCrossVO[] scs) {

		//���±��
		getTM().setNumRows(0);
		int iLen = (scs == null) ? 0 : scs.length;
		for (int i = 0; i < iLen; i++) {
			//��������
//			String strDataType = NCLangRes.getInstance().getStrByID("10241201",
//					"UPP10241201-000653")/* @res "�ַ�" */;
			//if (scs[i].getDataType() == Variant.INT)
			//strDataType = "����";
			//else
			//if (scs[i].getDataType() == Variant.DOUBLE)
			//strDataType = "С��";
			//λ��
			String strLocate = NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000887")/* @res "�˻�" */;
			if (scs[i].getLocate() == QueryConst.CROSS_ROW)
				strLocate = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000882")/* @res "��ͷ" */;
			else if (scs[i].getLocate() == QueryConst.CROSS_COL)
				strLocate = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000878")/* @res "��ͷ" */;
			//����
			Object[] objrows = new Object[] { "", scs[i].getFldCode(),
					scs[i].getFldName(),
					//strDataType,
					scs[i].getWhereCond(), strLocate };
			getTM().addRow(objrows);
		}
	}

	/**
	 * ���þ��ж��� �������ڣ�(01-5-14 13:17:27)
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
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//����
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
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		QueryBaseVO qb = qmd.getQueryBaseVO();
		setResultToCross(qb.getScs(), qb.getRotateCross());
	}

}
 