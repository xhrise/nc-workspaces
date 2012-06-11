/*
 * �������� 2005-5-24
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
 * �����������ý���
 */
public class SetJoinPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0055";//"��������";

	//�༭������
	private UIRefCellEditor[] m_refEditors = null;

	//���ɱ��ʽ�Ի���ʵ��
	private FldGenDlg m_fldGenDlg = null;

	//����
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
	 * SetTablePanel ������ע�⡣
	 */
	public SetJoinPanel() {
		super();
		initialize();
	}

	/**
	 * ֹͣ�༭���� �������ڣ�(2001-7-31 13:02:41)
	 * 
	 * @param value
	 *            java.lang.Object
	 * @param row
	 *            int
	 * @param col
	 *            int
	 */
	protected void afterEdit(TableCellEditor editor, int row, int col) {
		//���õ�Ԫֵ
		Object value = editor.getCellEditorValue();
		getTable().setValueAt(value, row, col);

		if (isSimple()) {
			//��������ֵ�ʵ��
			ObjectTree dd = getTabPn().getDatadict();
			if (col == COL_LEFTTABLE) {
				//�����ѡ�еı�1
				FromTableVO ft = (FromTableVO) getTM().getValueAt(row,
						COL_LEFTTABLE);
				//����ֶ�1��
				TableColumn tc = getTable().getColumnModel().getColumn(
						COL_LEFTFLD);
				//��ö�������Դ
				//String dsName =
				// getQueryDefTabbedPn().getQueryBaseDef().getDsName();
				String dsName = getTabPn().getDefDsName();
				//��ѯ��Ӧ��1�е��ֶ�
				ObjectTree tree = (QueryUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
						.getInstance(dsName)
						: dd;
				SelectFldVO[] sfs = QueryUtil.getFldsFromTable(ft
						.getTablecode(), ft.getTablealias(), tree);
				//�����ֶ���1�ı༭��(����Դ�л����ܵ��¿�)
				JComboBox cbbFld = (sfs == null) ? new UIComboBox()
						: new UIComboBox(sfs);
				tc.setCellEditor(new UIRefCellEditor(cbbFld));
				//���õ�Ԫֵ
				getTM().setValueAt(cbbFld.getItemAt(0), row, COL_LEFTFLD);
			} else if (col == COL_RIGHTTABLE) {
				//�����ѡ�еı�2
				FromTableVO ft = (FromTableVO) getTM().getValueAt(row,
						COL_RIGHTTABLE);
				//����ֶ�2��
				TableColumn tc = getTable().getColumnModel().getColumn(
						COL_RIGHTFLD);
				//���ִ������Դ
				//String dsName =
				// getTabPn().getQueryBaseDef().getDsName();
				String dsName = getTabPn().getDefDsName();
				//��ѯ��Ӧ��2�е��ֶ�
				ObjectTree tree = (QueryUtil.isTempTable(ft.getTablecode())) ? BIQueryModelTree
						.getInstance(dsName)
						: dd;
				SelectFldVO[] sfs = QueryUtil.getFldsFromTable(ft
						.getTablecode(), ft.getTablealias(), tree);
				//�����ֶ���2�ı༭��(����Դ�л����ܵ��¿�)
				JComboBox cbbFld = (sfs == null) ? new UIComboBox()
						: new UIComboBox(sfs);
				tc.setCellEditor(new UIRefCellEditor(cbbFld));
				//���õ�Ԫֵ
				getTM().setValueAt(cbbFld.getItemAt(0), row, COL_RIGHTFLD);
			}
		}
	}

	/**
	 * add
	 */
	public void bnAdd_ActionPerformed(ActionEvent actionEvent) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		//�������
		int iRowCount = getTable().getRowCount();
		FromTableVO[] fts = getTabPn().getQueryBaseDef().getFromTables();
		int iLen = (fts == null) ? 0 : fts.length;
		if (iRowCount == 0 && iLen < 2) {
			MessageDialog.showWarningDlg(this, NCLangRes.getInstance()
					.getStrByID("10241201", "UPP10241201-000099")/*
																  * @res "��ѯ����"
																  */, NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000905")/*
										  * @res "��ǰ��ѯ�����������޷�������������"
										  */);
			return;
		}
		//���ȱʡֵ
		JComboBox cbbTable1 = (JComboBox) m_refEditors[0].getComponent();
		JComboBox cbbTable2 = (JComboBox) m_refEditors[2].getComponent();
		if (isSimple()) {
			JComboBox cbbFld1 = (JComboBox) m_refEditors[3].getComponent();
			JComboBox cbbFld2 = (JComboBox) m_refEditors[5].getComponent();
			if (iRowCount == 0) {
				//�������
				getTM()
						.addRow(
								new Object[] {
										"",
										cbbTable1.getItemAt(0),
										NCLangRes.getInstance().getStrByID(
												"10241201",
												"UPP10241201-000863")/*
																	  * @res
																	  * "������"
																	  */, cbbTable2.getItemAt(0),
										cbbFld1.getItemAt(0), "=",
										cbbFld2.getItemAt(0) });
				//���ñ༭���¼��л���ǿ��ת��
				setValueEditor(0, COL_RIGHTTABLE);
				setValueEditor(0, COL_RIGHTFLD);
			} else {
				//�������
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
																	  * "������"
																	  */,
										cbbTable2.getItemAt(0),
										getTM().getValueAt(iRowCount - 1,
												COL_RIGHTFLD), "=",
										cbbFld2.getItemAt(0) });
			}
		} else {
			if (iRowCount == 0) {
				//�������
				getTM().addRow(
						new Object[] {
								"",
								cbbTable1.getItemAt(0),
								NCLangRes.getInstance().getStrByID("10241201",
										"UPP10241201-000863")/*
															  * @res "������"
															  */, cbbTable2.getItemAt(0), "" });
			} else {
				//�������
				getTM().addRow(
						new Object[] {
								"",
								getTM().getValueAt(iRowCount - 1,
										COL_RIGHTTABLE),
								NCLangRes.getInstance().getStrByID("10241201",
										"UPP10241201-000863")/*
															  * @res "������"
															  */, cbbTable2.getItemAt(0), "" });
			}
		}
		//ѡ��������
		int iIndex = getTM().getRowCount();
		getTable().getSelectionModel().setSelectionInterval(iIndex - 1,
				iIndex - 1);
		return;
	}

	/**
	 * del
	 */
	public void bnDel_ActionPerformed(ActionEvent actionEvent) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iRowCount = getTable().getRowCount();
		int iSelIndex = getTable().getSelectedRow();
		if (iSelIndex != -1 && iSelIndex < iRowCount) {
			//����ˢ��
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
		//��ֹ�༭̬
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
	 * connEtoC2: (BnDown.action.actionPerformed(ActionEvent) -->
	 * SetCondPanel.bnDown_ActionPerformed(LActionEvent;)V)
	 * 
	 * @param arg1
	 *            ActionEvent
	 */
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * �ֹ�¼��
	 */
	public void doJoin(int iSelIndex) {

		String oldExp = getTable2().getValueAt(iSelIndex, COL_EXPRESSION)
				.toString();
		//�����Ի���
		FldGenDlg dlg = getFldGenDlg();
		dlg.doSetInfo(new Object[] { null, null, oldExp, null });
		dlg.setBnAddEnabled(false);
		dlg.setSomeInvisibled();
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			//��ֹ�༭̬
			if (getTable2().getCellEditor() != null)
				getTable2().getCellEditor().stopCellEditing();
			getTable2().setValueAt(dlg.getExp(), iSelIndex, COL_EXPRESSION);
		}
		return;
	}

	/**
	 * ���� �������ڣ�(2003-11-13 15:53:55)
	 * 
	 * @param iSelIndex
	 *            int
	 */
	private void doTurn(int iSelIndex) {

//		/* ����Ϊһ���Դ��� */
//		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
//		//�ϲ�VO����
//		JoinCondVO[] oldJcs = qbd.getJoinConds();
//		String leftTable = oldJcs[iSelIndex].getLefttable();
//		String leftFld = oldJcs[iSelIndex].getLeftfld();
//		oldJcs[iSelIndex].setLefttable(oldJcs[iSelIndex].getRighttable());
//		oldJcs[iSelIndex]
//				.setLeftfld(oldJcs[iSelIndex].getRightfld().toString());
//		oldJcs[iSelIndex].setRighttable(leftTable);
//		oldJcs[iSelIndex].setRightfld(leftFld);

		//����
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
		//ѡ��
		getTable().getSelectionModel().setSelectionInterval(iSelIndex,
				iSelIndex);
	}

	/**
	 * ���� BnDel ����ֵ��
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
	 * ���� BnGroup ����ֵ��
	 * 
	 * @return javax.swing.ButtonGroup
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� BnTurn ����ֵ��
	 * 
	 * @return UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIButton getBnTurn() {
		if (ivjBnTurn == null) {
			try {
				ivjBnTurn = new UIButton();
				ivjBnTurn.setName("BnTurn");
				ivjBnTurn.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000906")/* @res "����" */);
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
	 * ���� PnEast11 ����ֵ��
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
	 * ���� PnEast21 ����ֵ��
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
	 * ���� PnEastGridLayout ����ֵ��
	 * 
	 * @return GridLayout
	 */
	/* ���棺�˷������������ɡ� */
	private GridLayout getPnEastGridLayout() {
		GridLayout ivjPnEastGridLayout = null;
		try {
			/* �������� */
			ivjPnEastGridLayout = new GridLayout(5, 1);
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
	 * ���� PnNorthFlowLayout ����ֵ��
	 * 
	 * @return FlowLayout
	 */
	/* ���棺�˷������������ɡ� */
	private FlowLayout getPnNorthFlowLayout() {
		FlowLayout ivjPnNorthFlowLayout = null;
		try {
			/* �������� */
			ivjPnNorthFlowLayout = new FlowLayout();
			ivjPnNorthFlowLayout.setHgap(10);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		;
		return ivjPnNorthFlowLayout;
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
	 * ���� RadioBnChoose ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnChoose() {
		if (ivjRadioBnChoose == null) {
			try {
				ivjRadioBnChoose = new UIRadioButton();
				ivjRadioBnChoose.setName("RadioBnChoose");
				ivjRadioBnChoose.setSelected(false);
				ivjRadioBnChoose.setPreferredSize(new Dimension(80, 22));
				ivjRadioBnChoose.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000869")/* @res "����ģʽ" */);
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
	 * ���� RadioBnHand ����ֵ��
	 * 
	 * @return UIRadioButton
	 */
	/* ���棺�˷������������ɡ� */
	private UIRadioButton getRadioBnHand() {
		if (ivjRadioBnHand == null) {
			try {
				ivjRadioBnHand = new UIRadioButton();
				ivjRadioBnHand.setName("RadioBnHand");
				ivjRadioBnHand.setPreferredSize(new Dimension(80, 22));
				ivjRadioBnHand.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000870")/* @res "�߼�ģʽ" */);
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
	 * ��������������� �������ڣ�(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.JoinCondVO[]
	 */
	public JoinCondVO[] getResultFromJoin() {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		int iRowCount = getTable().getRowCount();
		if (iRowCount == 0)
			return null;
		JoinCondVO[] jcs = new JoinCondVO[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			//������
			FromTableVO ftLeft = (FromTableVO) getTM().getValueAt(i,
					COL_LEFTTABLE);
			String tableLeftCode = ftLeft.getTablecode() + " as "
					+ ftLeft.getTablealias();
			//����ұ�
			FromTableVO ftRight = (FromTableVO) getTM().getValueAt(i,
					COL_RIGHTTABLE);
			String tableRightCode = ftRight.getTablecode() + " as "
					+ ftRight.getTablealias();
			//������ֶ�
			String fldLeftCode = null;
			if (isSimple())
				fldLeftCode = ((SelectFldVO) getTM().getValueAt(i, COL_LEFTFLD))
						.getFldalias();
			//������ֶ�
			String fldRightCode = null;
			if (isSimple())
				fldRightCode = ((SelectFldVO) getTM().getValueAt(i,
						COL_RIGHTFLD)).getFldalias();
			//
			jcs[i] = new JoinCondVO();
			jcs[i].setLefttable(tableLeftCode);
			String strType = getTM().getValueAt(i, COL_JOINMODE).toString();
			if (strType.equals(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000863")/* @res "������" */))
				strType = "I";
			else if (strType.equals(NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000907")/* @res "������" */))
				strType = "L";
			else if (strType.equals(NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000908")/* @res "������" */))
				strType = "R";
			else if (strType.equals(NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000909")/* @res "ȫ����" */))
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable1() {
		return (UITable) getTablePn1().getTable();
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private UITable getTable2() {
		return (UITable) getTablePn2().getTable();
	}

	/**
	 * ���� TablePn ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
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
						//�����༭״̬
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
	 * ���� TablePn2 ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
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
						//�����༭״̬
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM1() {
		return (DefaultTableModel) getTable1().getModel();
	}

	/**
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	private DefaultTableModel getTM2() {
		return (DefaultTableModel) getTable2().getModel();
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
		getRadioBnChoose().addActionListener(ivjEventHandler);
		getRadioBnHand().addActionListener(ivjEventHandler);
		getBnUp().addActionListener(ivjEventHandler);
		getBnDown().addActionListener(ivjEventHandler);
		getBnTurn().addActionListener(ivjEventHandler);
	}

	/**
	 * ��ʼ����Ԫ�༭�� �������ڣ�(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	public void initEditorValue() {
		m_refEditors = new UIRefCellEditor[6];
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		//���ñ�1�б༭��
		FromTableVO[] fts = qbd.getFromTables();
		if (fts == null) {
			return;
		}
		JComboBox cbbTable1 = new UIComboBox(fts);
		m_refEditors[0] = new UIRefCellEditor(cbbTable1);
		//�������ӷ�ʽ�б༭��
		JComboBox cbbRelation = new UIComboBox(new Object[] {
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000863")/* @res "������" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000907")/* @res "������" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000908")/* @res "������" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000909") /* @res "ȫ����" */});
		m_refEditors[1] = new UIRefCellEditor(cbbRelation);
		//���ñ�2�б༭��
		JComboBox cbbTable2 = new UIComboBox(fts);
		m_refEditors[2] = new UIRefCellEditor(cbbTable2);

		//if (isSimple()) {
		//��������ֵ�ʵ��
		ObjectTree dd = getTabPn().getDatadict();
		//���ִ������Դ
		//String dsName = getTabPn().getQueryBaseDef().getDsName();
		String dsName = getTabPn().getDefDsName();
		//�����ֶ���1�༭��(����Դ�л����ܵ��¿�)
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
		//���ò������б༭��
		JComboBox cbbOperator = new UIComboBox(new Object[] { "=", ">", "<",
				"<>", ">=", "<=" });
		m_refEditors[4] = new UIRefCellEditor(cbbOperator);
		//�����ֶ���2�༭��(����Դ�л����ܵ��¿�)
		JComboBox cbbFld2 = (sfs == null) ? new UIComboBox()
				: new UIComboBox(sfs);
		//����sfs
		m_refEditors[5] = new UIRefCellEditor(cbbFld2);
		//}
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable1() {
		//��ģ��
		DefaultTableModel tm1 = new DefaultTableModel(new Object[] {
				"",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000862")/* @res "���ӱ�1" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000910")/* @res "���ӷ�ʽ" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000911")/* @res "���ӱ�2" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000912")/* @res "�����ֶ�1" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000874")/* @res "�ȽϷ�" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000913") /* @res "�����ֶ�2" */}, 0) {
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
					//���ñ༭��
					setValueEditor(row, col);
					return true;
				}
			}
		};
		getTable1().setModel(tm1);
		//���ñ�����
		getTable1().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable1().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable1().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
		makeMultiHeader(true);
	}

	/**
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable2() {
		//��ģ��
		DefaultTableModel tm2 = new DefaultTableModel(new Object[] {
				"",
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000862")/* @res "���ӱ�1" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000910")/* @res "���ӷ�ʽ" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000911")/* @res "���ӱ�2" */,
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000914") /* @res "���ӱ��ʽ" */}, 0) {
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
					//���ñ༭��
					setValueEditor(row, col);
					return true;
				}
			}
		};
		getTable2().setModel(tm2);
		//���ñ�����
		getTable2().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable2().getTableHeader()
				.setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable2().getTableHeader()
				.setForeground(QueryConst.HEADER_FORE_COLOR);
		makeMultiHeader(false);
	}

	/**
	 * �Ƿ�Ϊ����ģʽ �������ڣ�(2003-11-13 14:35:00)
	 * 
	 * @return boolean
	 */
	public boolean isSimple() {
		return getRadioBnChoose().isSelected();
	}

	/**
	 * ���ͷ �������ڣ�(2003-4-2 14:45:10)
	 */
	private void makeMultiHeader(boolean bSimple) {
		UITable table = (bSimple) ? getTable1() : getTable2();
		//
		TableColumnModel cm = table.getColumnModel();
		GroupableTableHeader header = (GroupableTableHeader) table
				.getTableHeader();
		ColumnGroup cg = new ColumnGroup(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000915")/* @res "JOIN ����" */);
		cg.add(cm.getColumn(COL_LEFTTABLE));
		cg.add(cm.getColumn(COL_JOINMODE));
		cg.add(cm.getColumn(COL_RIGHTTABLE));
		header.addColumnGroup(cg);
		if (bSimple) {
			cg = new ColumnGroup(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000916")/* @res "ON ����" */);
			cg.add(cm.getColumn(COL_LEFTFLD));
			cg.add(cm.getColumn(COL_OPERATOR));
			cg.add(cm.getColumn(COL_RIGHTFLD));
			header.addColumnGroup(cg);
		}
	}

	/**
	 * ѡ��¼��
	 */
	public void radioBnChoose_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn2());
		add(getTablePn1(), BorderLayout.CENTER);
		getBnTurn().setText(
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000906")/* @res "����" */);
		validate();
		repaint();
	}

	/**
	 * �ֹ�¼��
	 */
	public void radioBnHand_ActionPerformed(ActionEvent actionEvent) {

		remove(getTablePn1());
		add(getTablePn2(), BorderLayout.CENTER);
		getBnTurn().setText(
				NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000917")/* @res "����" */);
		validate();
		repaint();
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
	 * ����where�������� �������ڣ�(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public void setResultToJoin(JoinCondVO[] jcs) {
		int iLen = (jcs == null) ? 0 : jcs.length;
		if (iLen == 0)
			return;

		//���õ�ѡ��ť
		boolean bSimple = (jcs[0].getExpression0() == null);
		getRadioBnChoose().setSelected(bSimple);
		getRadioBnHand().setSelected(!bSimple);
		if (!bSimple) {
			remove(getTablePn1());
			add(getTablePn2(), BorderLayout.CENTER);
			getBnTurn().setText(
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000917")/* @res "����" */);
			validate();
			repaint();
		}

		//�����-����ʾ����ϣ��
		Hashtable hashTableId = ((SetTablePanel) getTabPn().getSetPanel(
				SetTablePanel.TAB_TITLE)).getHashTableId();
		for (int i = 0; i < iLen; i++) {
			String strType = (jcs[i].getTypeflag() == null) ? "" : jcs[i]
					.getTypeflag().toString();
			if (strType.equals("L"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000907")/* @res "������" */;
			else if (strType.equals("R"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000908")/* @res "������" */;
			else if (strType.equals("F"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000909")/* @res "ȫ����" */;
			else if (strType.equals("I"))
				strType = NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000863")/* @res "������" */;

			if (jcs[i].getLefttable() == null)
				return;
			//ת����ѯ�ֶα��ʽ����
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
			//���ƣ�ͬ�����ѯʱ�����б仯��
			if (hashTableId.containsKey(leftTableAlias))
				leftTableName = hashTableId.get(leftTableAlias).toString();
			//�ֶ�
			String leftFldCode = jcs[i].getLeftfld();

			if (jcs[i].getRighttable() == null)
				return;
			//ת����ѯ�ֶα��ʽ���ң�
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
			//���ƣ�ͬ�����ѯʱ�����б仯��
			if (hashTableId.containsKey(rightTableAlias))
				rightTableName = hashTableId.get(rightTableAlias).toString();
			//�ֶ�
			String rightFldCode = (jcs[i].getRightfld() == null) ? null
					: jcs[i].getRightfld().toString();

			//��������ֵ�ʵ��
			ObjectTree dd = getTabPn().getDatadict();
			//���ִ������Դ
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//�������Ӧ��������
			ObjectTree tree = (QueryUtil.isTempTable(leftTableCode)) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			//��������ֶ���ϸ��Ϣ
			ValueObject[] vos = QueryUtil.getTableFldVO(leftTableCode,
					leftTableName, leftTableAlias, leftFldCode, tree);
			if (vos == null) {
				AppDebug.debug("vosLeft == null!");//@devTools System.out.println("vosLeft == null!");
				return;
			}
			FromTableVO ftLeft = (FromTableVO) vos[0];
			SelectFldVO sfLeft = (SelectFldVO) vos[1];
			//����ұ��Ӧ�������壨���������ͬ����Ϊ����һ���Ǳ���һ���ǲ�ѯ��
			tree = (QueryUtil.isTempTable(rightTableCode)) ? BIQueryModelTree
					.getInstance(dsName) : dd;
			//����ұ���ֶ���ϸ��Ϣ
			vos = QueryUtil.getTableFldVO(rightTableCode, rightTableName,
					rightTableAlias, rightFldCode, tree);
			if (vos == null) {
				AppDebug.debug("vosRight == null!");//@devTools System.out.println("vosRight == null!");
				return;
			}
			FromTableVO ftRight = (FromTableVO) vos[0];
			SelectFldVO sfRight = (SelectFldVO) vos[1];

			if (bSimple) {
				//���
				Object[] objrows = new Object[] { "", ftLeft, strType, ftRight,
						sfLeft, jcs[i].getOperator(), sfRight };
				getTM().addRow(objrows);
			} else {
				//�߼�ģʽ
				Object[] objrows = new Object[] { "", ftLeft, strType, ftRight,
						jcs[i].getExpression0() };
				getTM().addRow(objrows);
			}
		}

		getTable().getSelectionModel().setSelectionInterval(0, 0);
	}

	/**
	 * ���þ��ж��� �������ڣ�(01-5-14 13:17:27)
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
	 * ����ֵ��CellEditor �������ڣ�(2001-7-17 13:18:15)
	 * 
	 * @param iRow
	 *            int
	 * @param iCol
	 *            int
	 */
	private void setValueEditor(int iRow, int iCol) {
		TableColumn tc = getTable().getColumnModel().getColumn(iCol);
		//��ö���
//		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		if (!isSimple() && iCol == COL_EXPRESSION)
			return;
		else if (iCol != COL_LEFTFLD && iCol != COL_RIGHTFLD)
			//���ù�ϵ�������������б༭��
			tc.setCellEditor(m_refEditors[iCol - 1]);
		else if (iCol == COL_LEFTFLD) {
			//��þɵ�ѡ������
//			int iOldSelIndex = 0;
			if (getTable().getCellEditor(iRow, iCol) instanceof UIRefCellEditor) {
//				UIRefCellEditor refEditor = (UIRefCellEditor) getTable()
//						.getCellEditor(iRow, iCol);
//				JComboBox cbbOld = (JComboBox) refEditor.getComponent();
//				iOldSelIndex = cbbOld.getSelectedIndex();
			}
			//�����ֶ���1�༭��
			FromTableVO ft1 = (FromTableVO) getTM().getValueAt(iRow,
					COL_LEFTTABLE);
			//��������ֵ�ʵ��
			ObjectTree dd = getTabPn().getDatadict();
			//���ִ������Դ
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//��ѯ��Ӧ����1���ֶ�
			ObjectTree tree = (QueryUtil.isTempTable(ft1.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs1 = QueryUtil.getFldsFromTable(ft1.getTablecode(),
					ft1.getTablealias(), tree);
			//����Դ�л����ܵ��¿�
			JComboBox cbbFld1 = (sfs1 == null) ? new UIComboBox()
					: new UIComboBox(sfs1);
			cbbFld1.setSelectedItem(getTM().getValueAt(iRow, iCol));
			tc.setCellEditor(new UIRefCellEditor(cbbFld1));
		} else if (iCol == COL_RIGHTFLD) {
			//�����ֶ���2�༭��
			FromTableVO ft2 = (FromTableVO) getTM().getValueAt(iRow,
					COL_RIGHTTABLE);
			//��������ֵ�ʵ��
			ObjectTree dd = getTabPn().getDatadict();
			//���ִ������Դ
			//String dsName =
			// getTabPn().getQueryBaseDef().getDsName();
			String dsName = getTabPn().getDefDsName();
			//��ѯ��Ӧ����2���ֶ�
			ObjectTree tree = (QueryUtil.isTempTable(ft2.getTablecode())) ? BIQueryModelTree
					.getInstance(dsName)
					: dd;
			SelectFldVO[] sfs2 = QueryUtil.getFldsFromTable(ft2.getTablecode(),
					ft2.getTablealias(), tree);
			//��þɵ�ѡ������
			UIRefCellEditor refEditor = null;
			//
			//����Դ�л����ܵ��¿�
			JComboBox cbbFld2 = (sfs2 == null) ? new UIComboBox()
					: new UIComboBox(sfs2);
			//cbbFld2.setSelectedIndex(iOldSelIndex);
			//if (refEditor == null)
			refEditor = new UIRefCellEditor(cbbFld2);
			tc.setCellEditor(refEditor);
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
		qmd.getQueryBaseVO().setJoinConds(getResultFromJoin());
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setResultToJoin(qmd.getQueryBaseVO().getJoinConds());
	}

	/**
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		return null;
	}

	/**
	 * ˢ�²�ѯ��������
	 */
	public void refreshQbd() {
		//���ɸѡ��������
		JoinCondVO[] jcs = getResultFromJoin();
		//ˢ��
		getTabPn().getQueryBaseDef().setJoinConds(jcs);
	}
} 