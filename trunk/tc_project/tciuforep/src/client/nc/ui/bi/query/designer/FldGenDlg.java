/*
 * �������� 2005-5-24
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
 * �ֶα��ʽ���ɶԻ��� �������ڣ�(2003-4-2 19:24:22)
 * 
 * @author���쿡��
 */
public class FldGenDlg extends UIDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��ѯ��������
	private QueryBaseDef m_qbd = null;

	//�����
	private Container m_parent = null;

	//�����ж��ֶα����ظ��Ĺ�ϣ��
	private Hashtable<String, String> m_hashFldAlias = null;

	//���Ӧ���ֶ��б�
	private Map<String, SelectFldVO[]> m_hmFlds = new java.util.HashMap<String, SelectFldVO[]>();

	//��������������
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
	 * FldGenDlg ������ע�⡣
	 * @deprecateds
	 */
	public FldGenDlg() {
		super();
		initialize();
	}

	/**
	 * FldGenDlg ������ע�⡣
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
		//�Ϸ��Լ��
		String strErr = null;
		if (bMulti)
			strErr = checkMultiSelect();
		else
			strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000099")/* @res "��ѯ����" */, strErr);
			return;
		}
		//���ӱ���
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
			//�Ϸ��Լ��
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
										"UPP10241201-000099")/* @res "��ѯ����" */,
								strErr);
				return;
			}
		}
		closeOK();
	}

	/**
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 13:57:49)
	 * 
	 * @return java.lang.String
	 */
	private String check() {

		//�ֶα���У��
		String fldAlias = getTFAlias().getText().trim();
		if (fldAlias.equals(""))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000683")/* @res "�ֶα�������Ϊ��" */;
		String strTemp = fldAlias.toLowerCase();
		int iLen = strTemp.length();
		for (int i = 0; i < iLen; i++) {
			char c = strTemp.charAt(i);
			if (i == 0 && (c < 'a' || c > 'z'))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000684")/*
											  * @res
											  * "�ֶα�����ʹ����ĸ��ͷ������ĸ�����֡��»��߹��ɵ��ַ�������ʹ��SQL������"
											  */;
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '_')
				continue;
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000685", null, new String[] { "" + c })/*
																		 * @res
																		 * "���ڷǷ��ַ���{0}\n�ֶα�����ʹ����ĸ��ͷ������ĸ�����֡��»��߹��ɵ��ַ�������ʹ��SQL������"
																		 */;
		}

		//�ֶ���ʾ��У��
		String fldName = getTFDisp().getText().trim();
		if (fldName.equals(""))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000686")/* @res "�ֶ���ʾ������Ϊ��" */;
		for (int i = 0; i < fldName.length(); i++) {
			char c = fldName.charAt(i);
			if (String.valueOf(c).equals(QueryConst.CROSS_SEPERATOR))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000687", null,
						new String[] { QueryConst.CROSS_SEPERATOR })/*
																	 * @res
																	 * "�벻Ҫ����ʾ����ʹ��'{0}'���ţ�лл����"
																	 */;
		}

		//�޸��ֶ��޷�
		if (m_hashFldAlias.containsKey(fldAlias))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000688")/* @res "�ֶα��������ظ���" */
					+ fldAlias + "(" + fldName + ")";
		if (m_hashFldAlias.containsValue(fldName))
			return NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000689")/* @res "�ֶ���ʾ�������ظ���" */
					+ fldName;

		//���¹�ϣ��
		m_hashFldAlias.put(fldAlias, fldName);
		return null;
	}

	/**
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 13:57:49)
	 * 
	 * @return java.lang.String
	 */
	private String checkMultiSelect() {

		SelectFldVO[] sfs = getGenFlds();
		int iLen = (sfs == null) ? 0 : sfs.length;
		for (int i = 0; i < iLen; i++) {
			String fldAlias = sfs[i].getFldalias();
			String fldName = sfs[i].getFldname();
			//�޸��ֶ��޷�
			if (m_hashFldAlias.containsKey(fldAlias))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000688")/* @res "�ֶα��������ظ���" */
						+ fldAlias + "(" + fldName + ")";
			if (m_hashFldAlias.containsValue(fldName))
				return NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000689")/* @res "�ֶ���ʾ�������ظ���" */
						+ fldName;
			//
			for (int j = 0; j < fldName.length(); j++) {
				char c = fldName.charAt(j);
				if (String.valueOf(c).equals(QueryConst.CROSS_SEPERATOR))
					return NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000687", null,
							new String[] { QueryConst.CROSS_SEPERATOR })/*
																		 * @res
																		 * "�벻Ҫ����ʾ����ʹ��'{0}'���ţ�лл����"
																		 */;
			}
		}
		//ע�������ѭ��
		for (int i = 0; i < iLen; i++) {
			//���¹�ϣ��
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	/* ���棺�˷������������ɡ� */
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
	 * ��� �������ڣ�(2003-4-3 10:13:00)
	 */
	public void doClear() {
		doSetInfo(new String[] { "", "", "", "" });
	}

	/**
	 * ���Զ�λ �������ڣ�(2004-7-9 9:28:55)
	 */
	private void doLocate(String strExp) {
		if (strExp != null)
			try {
				int iIndex = strExp.indexOf(".");
				if (iIndex != -1) {
					//³ç��ȡ�ֶ�
					String table = strExp.substring(0, iIndex).trim();
					//ѡ��
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
						//����ѡ��
						getListTable().setSelectedIndex(iSelIndex);
						selectTable(iSelIndex);
						//³ç��ȡ�ֶ�
						String fld = strExp.substring(iIndex + 1).trim();
						//ѡ�ֶ�
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
							//����ѡ�ֶ�
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
	 * �����ֶ���Ϣ �������ڣ�(2003-4-3 10:13:00)
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

		//���Զ�λ
		doLocate(strDispAliasExpNote[2]);
	}

	/**
	 * ���� BnAdd ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnAdd() {
		if (ivjBnAdd == null) {
			try {
				ivjBnAdd = new nc.ui.pub.beans.UIButton();
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
	 * ���� BnCancel ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnCancel() {
		if (ivjBnCancel == null) {
			try {
				ivjBnCancel = new nc.ui.pub.beans.UIButton();
				ivjBnCancel.setName("BnCancel");
				ivjBnCancel.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000000")/* @res "ȡ��" */);
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
	 * ���� BnClear ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnClear() {
		if (ivjBnClear == null) {
			try {
				ivjBnClear = new nc.ui.pub.beans.UIButton();
				ivjBnClear.setName("BnClear");
				ivjBnClear.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000318")/* @res "���" */);
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
	 * ���� BnOK ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIButton
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIButton getBnOK() {
		if (ivjBnOK == null) {
			try {
				ivjBnOK = new nc.ui.pub.beans.UIButton();
				ivjBnOK.setName("BnOK");
				ivjBnOK.setText(NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000001")/* @res "ȷ��" */);
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
	 * ������ɵı��ʽ �������ڣ�(2003-4-3 9:09:08)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public String getExp() {
		return handleExp(getTAExp().getText());
	}

	/**
	 * �򵥴���һ�±��ʽ��û�����ŵ��Ҹ�������
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
	 * ������ɵĲ�ѯ�ֶ�VO �������ڣ�(2003-4-3 9:09:08)
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
	 * ������ɵĲ�ѯ�ֶ�VO���� �������ڣ�(2003-4-3 9:09:08)
	 * 
	 * @return nc.vo.iuforeport.businessquery.SelectFldVO
	 */
	public SelectFldVO[] getGenFlds() {
		//���ѡ�б�
		int iIndex = getListTable().getSelectedIndex();
		String tableAlias = null;
		if (iIndex != -1) {
			FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);
			tableAlias = ft.getTablealias();
		}
		//���ѡ���ֶ�
		int[] iSelIndices = getListFld().getSelectedIndices();
		int iSelCount = (iSelIndices == null) ? 0 : iSelIndices.length;
		SelectFldVO[] sfs = new SelectFldVO[iSelCount];
		for (int i = 0; i < iSelCount; i++) {
			sfs[i] = (SelectFldVO) getListFld().getModel().getElementAt(
					iSelIndices[i]);
			//sfs[i] = (SelectFldVO) sfs[i].clone();
			//������ʽ
			String strExp = sfs[i].getFldalias();
			if (tableAlias != null)
				strExp = tableAlias + "." + strExp;
			sfs[i].setExpression(strExp);
		}
		return sfs;
	}

	/**
	 * ���� LabelAlias ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelAlias() {
		if (ivjLabelAlias == null) {
			try {
				ivjLabelAlias = new nc.ui.pub.beans.UILabel();
				ivjLabelAlias.setName("LabelAlias");
				ivjLabelAlias.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000690")/* @res "�ֶα�����" */);
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
	 * ���� LabelDisp ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelDisp() {
		if (ivjLabelDisp == null) {
			try {
				ivjLabelDisp = new nc.ui.pub.beans.UILabel();
				ivjLabelDisp.setName("LabelDisp");
				ivjLabelDisp.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000691")/*
														  * @res "�ֶ���ʾ����"
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
	 * ���� LabelExp ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelExp() {
		if (ivjLabelExp == null) {
			try {
				ivjLabelExp = new nc.ui.pub.beans.UILabel();
				ivjLabelExp.setName("LabelExp");
				ivjLabelExp.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000692")/*
														  * @res "�ֶα��ʽ��"
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
	 * ���� LabelTable1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelFld() {
		if (ivjLabelFld == null) {
			try {
				ivjLabelFld = new nc.ui.pub.beans.UILabel();
				ivjLabelFld.setName("LabelFld");
				ivjLabelFld.setPreferredSize(new java.awt.Dimension(100, 22));
				ivjLabelFld.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000693")/* @res "�ֶΣ�" */);
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
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelFunc() {
		if (ivjLabelFunc == null) {
			try {
				ivjLabelFunc = new nc.ui.pub.beans.UILabel();
				ivjLabelFunc.setName("LabelFunc");
				ivjLabelFunc.setPreferredSize(new java.awt.Dimension(100, 22));
				ivjLabelFunc.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000694")/* @res "������" */);
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
	 * ���� LabelNote ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelNote() {
		if (ivjLabelNote == null) {
			try {
				ivjLabelNote = new nc.ui.pub.beans.UILabel();
				ivjLabelNote.setName("LabelNote");
				ivjLabelNote.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000695")/* @res "�ֶα�ע��" */);
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
	 * ���� LabelTable3 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelOperator() {
		if (ivjLabelOperator == null) {
			try {
				ivjLabelOperator = new nc.ui.pub.beans.UILabel();
				ivjLabelOperator.setName("LabelOperator");
				ivjLabelOperator.setPreferredSize(new java.awt.Dimension(100,
						22));
				ivjLabelOperator.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000696")/* @res "���ţ�" */);
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
	 * 
	 * @return nc.ui.pub.beans.UILabel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UILabel getLabelTable() {
		if (ivjLabelTable == null) {
			try {
				ivjLabelTable = new nc.ui.pub.beans.UILabel();
				ivjLabelTable.setName("LabelTable");
				ivjLabelTable.setPreferredSize(new java.awt.Dimension(100, 22));
				ivjLabelTable.setText(NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000697")/* @res "��" */);
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
	 * 
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
	 * ���� ListTable3 ����ֵ��
	 * 
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
	 * 
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
	 * ����ֶ��б�ģ�� �������ڣ�(2003-4-2 20:18:29)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMFld() {
		return (DefaultListModel) getListFld().getModel();
	}

	/**
	 * ��ò������б�ģ�� �������ڣ�(2003-4-2 20:18:29)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMOperator() {
		return (DefaultListModel) getListOperator().getModel();
	}

	/**
	 * ��ñ��б�ģ�� �������ڣ�(2003-4-2 20:18:29)
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getLMTable() {
		return (DefaultListModel) getListTable().getModel();
	}

	/**
	 * ���� PnCenter ����ֵ��
	 * 
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
	 * ���� PnCenterGridLayout ����ֵ��
	 * 
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
		}
		;
		return ivjPnCenterGridLayout;
	}

	/**
	 * ���� PnListTable1 ����ֵ��
	 * 
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
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnListFldNorthFlowLayout ����ֵ��
	 * 
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
		}
		;
		return ivjPnListFldNorthFlowLayout;
	}

	/**
	 * ���� PnListTable2 ����ֵ��
	 * 
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
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnListFuncNorthFlowLayout ����ֵ��
	 * 
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
		}
		;
		return ivjPnListFuncNorthFlowLayout;
	}

	/**
	 * ���� PnListTable3 ����ֵ��
	 * 
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
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnListOperatorNorthFlowLayout ����ֵ��
	 * 
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
		}
		;
		return ivjPnListOperatorNorthFlowLayout;
	}

	/**
	 * ���� PnListTable ����ֵ��
	 * 
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
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnListTableNorthFlowLayout ����ֵ��
	 * 
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
		}
		;
		return ivjPnListTableNorthFlowLayout;
	}

	/**
	 * ���� UIPanel1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnSouth ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� PnSouthFlowLayout ����ֵ��
	 * 
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
		}
		;
		return ivjPnSouthFlowLayout;
	}

	/**
	 * ���� UIScrollPane1 ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� SclPnListTable1 ����ֵ��
	 * 
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
	 * 
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� SclPnListTable3 ����ֵ��
	 * 
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
	 * 
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
	 * 
	 * @return nc.ui.pub.beans.UITextArea
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TFAlias ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* ���棺�˷������������ɡ� */
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
														  * "������ʹ����ĸ��ͷ������ĸ�����֡��»��߹��ɵ��ַ�������ʹ��SQL������"
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
	 * ���� TFDisp ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TFNote ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� TreeFunc ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITree
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���� UIDialogContentPane ����ֵ��
	 * 
	 * @return javax.swing.JPanel
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("FldGenDlg");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(NCLangRes.getInstance().getStrByID("10241201",
					"UPP10241201-000699")/* @res "���ɱ��ʽ" */);
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
	 * ��ʼ���б� �������ڣ�(2003-4-2 19:56:10)
	 */
	public void initList() {
		//�����б�ģ��
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
		//���õ�ѡģʽ
		getListTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getListOperator().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * ��ʼ���������Ͳ������б� �������ڣ�(2000-08-01 15:10:00)
	 */
	public void initTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(NCLangRes
				.getInstance().getStrByID("10241201", "UPP10241201-000283")/*
																		    * @res
																		    * "����"
																		    */);
		DefaultMutableTreeNode parent, child;
		//��ʼ��������
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000284")/* @res "��ѧ����" */);
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
		//�ı�����
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000285")/* @res "�ı�����" */);
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
		//ͳ�ƺ���
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000700")/* @res "ͳ�ƺ���" */);
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
		//��������
		parent = new DefaultMutableTreeNode(NCLangRes.getInstance().getStrByID(
				"10241201", "UPP10241201-000701")/* @res "��������" */);
		child = new DefaultMutableTreeNode("case when");
		parent.add(child);
		child = new DefaultMutableTreeNode("isnull");
		parent.add(child);
		child = new DefaultMutableTreeNode("datediff");
		parent.add(child);
		root.add(parent);

		//������ģ���뵥ѡģʽ
		getTreeFunc().setModel(new DefaultTreeModel(root));
		getTreeFunc().getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * �Ƿ��ѡ�ֶ� �������ڣ�(2004-6-25 13:44:38)
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
		 * //��ø��������Ϣ if (m_parent instanceof SetFldPanel) { SetFldPanel pn =
		 * ((SetFldPanel) m_parent); dlg = pn.getQueryDefTabbedPn().getRefDlg(); }
		 * else if (m_parent instanceof SetCondPanel) { SetCondPanel pn =
		 * ((SetCondPanel) m_parent); dlg =
		 * pn.getQueryDefTabbedPn().getRefDlg(); } else if (m_parent instanceof
		 * SetJoinPanel) { SetJoinPanel pn = ((SetJoinPanel) m_parent); dlg =
		 * pn.getQueryDefTabbedPn().getRefDlg(); } else if (m_parent instanceof
		 * SetCrossPanel) { SetCrossPanel pn = ((SetCrossPanel) m_parent); dlg =
		 * pn.getQueryDefTabbedPn().getRefDlg(); } //�������տ��¼�� if (dlg != null) {
		 * dlg.showModal(); dlg.destroy(); if (dlg.getResult() ==
		 * UIDialog.ID_OK) //������ս�� onSelect(dlg.getValue(), 0); } }
		 */
	}

	/**
	 * �ֶ��б�����Ӧ
	 */
	public void listFld_MouseClicked(MouseEvent mouseEvent) {
		//��ѡ����
		boolean bMulti = isMultiSelect();
		getTFDisp().setEnabled(!bMulti);
		getTFAlias().setEnabled(!bMulti);
		getTFNote().setEnabled(!bMulti);
		getTAExp().setEnabled(!bMulti);
		//
		int iClickCount = mouseEvent.getClickCount();
		if (iClickCount == 2) {
			//���ѡ���ֶ�
			String str = null;
			int iIndex = getListFld().getSelectedIndex();
			if (iIndex != -1) {
				SelectFldVO sf = (SelectFldVO) getLMFld().getElementAt(iIndex);
				//str = sf.getExpression();
				str = sf.getFldalias();
				//������ʾ���ͱ���
				if (getTFDisp().getText().trim().equals(""))
					getTFDisp().setText(sf.getFldname());
				if (getTFAlias().getText().trim().equals(""))
					getTFAlias().setText(str);
				//���ѡ�б�
				iIndex = getListTable().getSelectedIndex();
				if (iIndex != -1) {
					//���ѡ�б�
					FromTableVO ft = (FromTableVO) getLMTable().getElementAt(
							iIndex);
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
	 * �������б�����Ӧ
	 */
	public void listOperator_MouseClicked(MouseEvent mouseEvent) {
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
	public void listTable_MouseClicked(MouseEvent mouseEvent) {
		int iIndex = getListTable().getSelectedIndex();
		if (iIndex != -1)
			selectTable(iIndex);
	}

	/**
	 * ˫��ѡ�� �������ڣ�(2003-4-2 20:38:58)
	 * 
	 * @param str
	 *            java.lang.String
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
	 * �����ֶ��б� �������ڣ�(2003-4-2 20:11:19)
	 * 
	 * @param tds
	 *            nc.bs.com.datadict.TableDef[]
	 */
	private void resetFld(SelectFldVO[] sfs) {
		getLMFld().removeAllElements();
		int iLen = (sfs == null) ? 0 : sfs.length;
		if (iLen != 0) {
			//����ʾ������
			Arrays.sort(sfs);
			//������ʾ���ĳ���
			int iMaxWidth = 0;
			String strMax = "";
			for (int i = 0; i < iLen; i++) {
				int iNameLen = sfs[i].getFldname().getBytes().length;
				if (iMaxWidth < iNameLen) {
					iMaxWidth = iNameLen;
					strMax = sfs[i].getFldname();
				}
				//����б�Ԫ��
				getLMFld().addElement(sfs[i]);
			}
			//�����б���ѿ��
			if (iMaxWidth != 0) {
				getListFld().setPrototypeCellValue(strMax);
			}
		}
	}

	/**
	 * ���ñ��б� �������ڣ�(2003-4-2 20:11:19)
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
	 * ѡ�б� �������ڣ�(2004-7-9 9:19:16)
	 * 
	 * @param iIndex
	 *            int
	 */
	private void selectTable(int iIndex) {
		//���ѡ�б�
		FromTableVO ft = (FromTableVO) getLMTable().getElementAt(iIndex);

		SelectFldVO[] sfs = getFldVO(ft);
		//�����ֶ��б�
		if (sfs != null) {
			//��¡������������ѯ���ú��ߵ�˳��
			SelectFldVO[] sfsClone = new SelectFldVO[sfs.length];
			for (int i = 0; i < sfs.length; i++) {
				sfsClone[i] = (SelectFldVO) sfs[i].clone();
				if (sfsClone[i].getFldname() == null) {
					sfsClone[i].setFldname("");
				}
			}
			//����
			resetFld(sfsClone);
		} else
			getLMFld().removeAllElements();
	}

	/**
	 * �������Ӱ�ť������ �������ڣ�(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setBnAddEnabled(boolean bEnabled) {
		getBnAdd().setEnabled(bEnabled);
		//�޸�̬���ܶ�ѡ
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
	 * �����ֶα�����ϣ�� �������ڣ�(2003-4-2 17:12:21)
	 * 
	 * @param hashTableId
	 *            java.util.Hashtable
	 */
	public void setHashFldAlias(Hashtable<String, String> hashFldAlias) {
		m_hashFldAlias = hashFldAlias;
	}

	/**
	 * ���ò�ѯ�������� �������ڣ�(2003-4-2 18:34:06)
	 * 
	 * @return nc.vo.iuforeport.businessquery.QueryBaseDef
	 */
	public void setQueryBaseDef(QueryBaseDef qbd) {
		m_qbd = qbd;
		//���ñ�
		FromTableVO[] fts = m_qbd.getFromTables();
		resetTable(fts);
	}

	/**
	 * ������ʾ���Կ����� �������ڣ�(2003-4-2 17:12:21)
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
						"UPP10241201-000702")/* @res "�������ʽ��" */);

		//��������������ɼ��Դ���
		boolean bJoinTypeVisible = (m_parent instanceof SetTableJoinPanel);
		getCbbJoinType().setVisible(bJoinTypeVisible);
	}

	/**
	 * ��ʾ�Ի���
	 *  
	 */
	public int showModal1() {
		if (!isShowing()) {
			//����λ�ã��������
			Point point = getLocation();
			point.y = m_parent.getLocation().y + 120;
			setLocation(point);

			//setLocationRelativeTo(getTopParent());
			return showModal();
		}
		return getResult();
	}

	/**
	 * �����������Ӧ
	 */
	public void treeFunc_MouseClicked(MouseEvent mouseEvent) {
		int iClickCount = mouseEvent.getClickCount();
		if (iClickCount == 2) {
			TreePath selPath = getTreeFunc().getSelectionPath();
			if (selPath != null) {
				TreeNode selNode = (TreeNode) (selPath.getPath()[selPath
						.getPathCount() - 1]);
				if (selNode.isLeaf()) {
					//���ѡ�к���
					String sel = selPath.getPathComponent(
							selPath.getPathCount() - 1).toString();
					//ѡ��
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
		//  ��������ֵ�ʵ��
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
			//ȡ���ֶ�
			SelectFldVO[] sfs = BIModelUtil.getFldsFromTable(ft.getTablecode(),
					ft.getTablealias(), tree);
			m_hmFlds.put(strTablePK, sfs);
			return sfs;
		} else {
			return (SelectFldVO[]) m_hmFlds.get(strTablePK);
		}
//		if (!m_hmFlds.containsKey(ft.getTablecode())) {
//			//  ��������ֵ�ʵ��
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
	 * ���� TFDisp ����ֵ��
	 * 
	 * @return nc.ui.pub.beans.UITextField
	 * @i18n mbiquery0153=������
	 * @i18n mbiquery0154=������
	 * @i18n mbiquery0155=������
	 */
	private UIComboBox getCbbJoinType() {
		if (ivjCbbJoinType == null) {
			try {
				ivjCbbJoinType = new UIComboBox();
				ivjCbbJoinType.setName("CbbJoinType");
				ivjCbbJoinType.setBounds(240, 14, 80, 22);
				//ģ��
				ivjCbbJoinType.addItem(StringResource.getStringResource("mbiquery0153"));
				ivjCbbJoinType.addItem(StringResource.getStringResource("mbiquery0154"));
				ivjCbbJoinType.addItem(StringResource.getStringResource("mbiquery0155"));
				//���ɼ�
				ivjCbbJoinType.setVisible(false);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCbbJoinType;
	}

	/**
	 * �����������
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
	 * ������������
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