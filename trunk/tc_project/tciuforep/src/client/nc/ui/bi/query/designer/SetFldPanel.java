/*
 * �������� 2005-5-24
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
 * �ֶ����ý���
 */
public class SetFldPanel extends AbstractQueryDesignSetPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0051";//"��ѯ�ֶ�";

	//���ɱ��ʽ�Ի���ʵ��
	private FldGenDlg m_fldGenDlg = null;

	//�����ʵ��
	private SetColumnPanel m_columnPanel = null;

	//����
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
		//�����Ի���
		FldGenDlg dlg = getFldGenDlg();
		//�����ж��ظ��Ĺ�ϣ��
		Hashtable hashFldAlias = getHashFldAlias(-1);
		dlg.setHashFldAlias(hashFldAlias);
		//���
		dlg.doClear();
		dlg.setBnAddEnabled(true);
		dlg.showModal();
		dlg.destroy();
		if (dlg.getResult() == UIDialog.ID_OK) {
			if (dlg.isMultiSelect()) {
				//��������ֶ�
				SelectFldVO[] newsfs = dlg.getGenFlds();
				//���ӱ���
				doAdd(newsfs);
			} else {
				//��������ֶ�
				SelectFldVO newsf = dlg.getGenFld();
				//���ӱ���
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
			//����ˢ��
			getTM().removeRow(iSelIndex);
			if (iSelIndex != 0)
				getTable().getSelectionModel().setSelectionInterval(
						iSelIndex - 1, iSelIndex - 1);
			else if (getTable().getRowCount() != 0)
				getTable().getSelectionModel().setSelectionInterval(0, 0);

			/* ����Ϊһ���Դ��� */
			QueryBaseDef qbd = getTabPn().getQueryBaseDef();
			//�ϲ�VO����
			ValueObject[] vos = BIModelUtil.delFromVOs(qbd.getSelectFlds(),
					iSelIndex);
			SelectFldVO[] newsfs = new SelectFldVO[vos.length];
			for (int i = 0; i < vos.length; i++)
				newsfs[i] = (SelectFldVO) vos[i];
			qbd.setSelectFlds(newsfs);

			//ͬ�������ֶ�ҳǩ
			((SetOrderbyPanel) getTabPn()
					.getSetPanel(SetOrderbyPanel.TAB_TITLE))
					.setResultToOrderby(newsfs, null);
			//ͬ����ת��������
			//((SetCrossPanel)
			// getTabPn().getSetPanel(BusiModelDesignUI.TAB_CROSS)).clearRotateCross(newsfs);
		}
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
			//�����Ի���
			FldGenDlg dlg = getFldGenDlg();
			//�����ж��ظ��Ĺ�ϣ��
			Hashtable hashFldAlias = getHashFldAlias(iSelIndex);
			dlg.setHashFldAlias(hashFldAlias);
			//���
			dlg.doSetInfo(new Object[] {
					getTM().getValueAt(iSelIndex, COL_FLDNAME),
					getTM().getValueAt(iSelIndex, COL_FLDALIAS),
					getTM().getValueAt(iSelIndex, COL_FLDEXP),
					getTM().getValueAt(iSelIndex, COL_FLDNOTE) });
			dlg.setBnAddEnabled(false);
			dlg.showModal();
			dlg.destroy();
			if (dlg.getResult() == UIDialog.ID_OK) {
				//ɾ��
				getTM().removeRow(iSelIndex);
				//��������ֶ�
				SelectFldVO newsf = dlg.getGenFld();
				//����
				getTM().insertRow(
						iSelIndex,
						new Object[] { "", newsf.getExpression(),
								newsf.getFldname(), newsf.getFldalias(),
								newsf.getNote() });
				//ѡ��
				getTable().getSelectionModel().setSelectionInterval(iSelIndex,
						iSelIndex);

				/* ����Ϊһ���Դ��� */
				//����QueryBaseDef
				QueryBaseDef qbd = getTabPn().getQueryBaseDef();
				//�ϲ�VO����
				SelectFldVO[] oldSfs = qbd.getSelectFlds();
				SelectFldVO[] newsfs = new SelectFldVO[oldSfs.length];
				for (int i = 0; i < oldSfs.length; i++)
					if (i == iSelIndex)
						newsfs[i] = newsf;
					else
						newsfs[i] = oldSfs[i];
				qbd.setSelectFlds(newsfs);

				//ͬ�������ֶ�ҳǩ
				((SetOrderbyPanel) getTabPn().getSetPanel(
						SetOrderbyPanel.TAB_TITLE)).setResultToOrderby(newsfs,
						null);
				//ͬ����ת��������
				//((SetCrossPanel) getTabPn().getSetPanel(
				//		SetCrossPanel.TAB_TITLE)).clearRotateCross(newsfs);
			}
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
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		int iLen = getTM().getRowCount();
		if (iLen == 0)
			return nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000891")/* @res "û��ѡ���ֶ�" */;
		//for (int i = 0; i < iLen; i++) {
		//if (getTM().getValueAt(i, 2) == null
		//|| getTM().getValueAt(i, 2).toString().trim().equals(""))
		//return "��ʾ������Ϊ��";
		//if (getTM().getValueAt(i, 4) == null
		//|| getTM().getValueAt(i, 4).toString().trim().equals(""))
		//return "��������Ϊ��";
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
	 * connEtoC6: (PnEast1.mouse.mouseClicked(MouseEvent) -->
	 * SetFldPanel.pnEast1_MouseClicked(LMouseEvent;)V)
	 * 
	 * @param arg1
	 *            MouseEvent
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���ӱ��� �������ڣ�(2003-4-4 13:55:06)
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
	 * ���ӱ��� �������ڣ�(2003-4-4 13:55:06)
	 * 
	 * @param newsf
	 *            nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public void doAdd(SelectFldVO newsf) {
		//��ӱ��
		getTM().addRow(
				new Object[] { "", newsf.getExpression(), newsf.getFldname(),
						newsf.getFldalias(), newsf.getNote() });
		//ѡ��������
		getTable().getSelectionModel().setSelectionInterval(
				getTM().getRowCount() - 1, getTM().getRowCount() - 1);

		/* ����Ϊһ���Դ��� */
		QueryBaseDef qbd = getTabPn().getQueryBaseDef();
		SelectFldVO[] oldVOs = qbd.getSelectFlds();
		//�ϲ�VO����
		ValueObject[] vos = BIModelUtil.addToVOs(oldVOs, newsf);
		SelectFldVO[] newsfs = new SelectFldVO[vos.length];
		for (int i = 0; i < vos.length; i++)
			newsfs[i] = (SelectFldVO) vos[i];
		qbd.setSelectFlds(newsfs);

		//ͬ�������ֶ�ҳǩ
		((SetOrderbyPanel) getTabPn().getSetPanel(SetOrderbyPanel.TAB_TITLE))
				.setResultToOrderby(newsfs, null);
		//ͬ����ת��������
		//((SetCrossPanel)
		// getTabPn().getSetPanel(BusiModelDesignUI.TAB_CROSS)).clearRotateCross(newsfs);
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
				ivjBnAdd.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000330")/* @res "����" */);
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
				ivjBnDel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000091")/* @res "ɾ��" */);
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
				ivjBnDown.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
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
				ivjBnModify
						.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
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
				ivjBnUp.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000145")/* @res "����" */);
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
	 * ��õ�ǰ�ֶα���-�ֶ�VO��ϣ�� �������ڣ�(2003-11-25 10:59:36)
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
	 * ��ò�ѯ�ֶ����� �������ڣ�(2003-4-3 16:16:01)
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
	 * ��ñ�� �������ڣ�(2003-4-2 13:48:52)
	 * 
	 * @return UITable
	 */
	public QueryFldTable getTable() {
		return (QueryFldTable) getTablePn().getTable();
	}

	/**
	 * ���� TablePn ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	public QueryFldTablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new QueryFldTablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}
				//���ÿɽ����϶��ı��
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
		getPnEast1().addMouseListener(ivjEventHandler);
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
	 * ��ʼ����� �������ڣ�(2003-4-2 13:57:39)
	 */
	@SuppressWarnings("serial")
	public void initTable() {
		//��ģ��
		DefaultTableModel tm = new DefaultTableModel(new Object[] {
				"",
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000892")/* @res "�ֶα��ʽ" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000893")/* @res "��ʾ��" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000894")/* @res "����" */,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000142") /* @res "����" */}, 0) {
			public int getColumnCount() {
				return 5;
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return Object.class;
			}

			public boolean isCellEditable(int row, int col) {
				//if (col == COL_FLDDESC)
				//getTable().getColumn("�Ƿ���").setCellEditor(m_refEditor);
				//return (col == COL_FLDDESC);
				return false;
			}
		};
		getTable().setModel(tm);
		//���ñ�����
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
	 * ��� �������ڣ�(2003-4-3 9:47:03)
	 * 
	 * @return nc.ui.iuforeport.businessquery.FldGenDlg
	 */
	public void setFldGenDlgAsNull() {
		m_fldGenDlg = null;
	}

	/**
	 * ���ò�ѯ�ֶ����� �������ڣ�(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public void setResultToFld(SelectFldVO[] sfs) {
		//����
		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++) {
			String alias = sfs[i].getFldalias();
			Object[] objrows = new Object[] { "", sfs[i].getExpression(),
					sfs[i].getFldname(), alias, sfs[i].getNote() };
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
		qmd.getQueryBaseVO().setSelectFlds(getResultFromFld());
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {
		setResultToFld(qmd.getQueryBaseVO().getSelectFlds());
	}

	/*
	 * ����ж��ظ��Ĺ�ϣ��
	 */
	@SuppressWarnings("unchecked")
	public Hashtable getHashFldAlias(int iSelIndex) {
		Hashtable hashFldAlias = new Hashtable();
		for (int i = 0; i < getTM().getRowCount(); i++) {
			if (i != iSelIndex) {
				//����-�ֶ�
				String fldName = (getTM().getValueAt(i, COL_FLDNAME) == null) ? ""
						: getTM().getValueAt(i, COL_FLDNAME).toString();
				hashFldAlias.put(getTM().getValueAt(i, COL_FLDALIAS), fldName);
			}
		}
		return hashFldAlias;
	}

	/*
	 * ��ø����
	 */
	public SetColumnPanel getColumnPanel() {
		return m_columnPanel;
	}

	/*
	 * ��ø����
	 */
	public void setColumnPanel(SetColumnPanel columnPanel) {
		m_columnPanel = columnPanel;
		//���ñ�ʵ��
		getTablePn().setColumnPanel(columnPanel);
		getTable().setColumnPanel(columnPanel);
		getTable().setHeader(
				new QueryFldTableHeader(getTable().getColumnModel()));
	}

	/**
	 * ˢ�²�ѯ��������
	 */
	public void refreshQbd() {
		//��ò�ѯ�ֶζ���
		SelectFldVO[] sfs = getResultFromFld();
		//ˢ��
		getTabPn().getQueryBaseDef().setSelectFlds(sfs);
	}
}
 