package com.ufsoft.iufo.fmtplugin.businessquery;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iuforeport.businessquery.QuerySimpleDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.iuforeport.businessquery.WhereCondVO;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;

/*
@update
  2003-12-31 09:02 liulp
   ҵ���ѯwhereCondVO�ķ��������޸�
 @end
 @update
  2003-11-04 15:29 liulp
   ��whereCondVO���������͡��գ���˫���Ž�����ͳһ��У�鴦��
 @end
 @update 2003-10-30 20:40 liulp
  ���������������ֶ�������ִ�в�ѯǰ�����¶��󣬽��丳ֵ��leftOpe������
 @end
 @update 2003-10-23 13:21 liulp
  ����Ի���Ӧ����ģ̬��
 @end
 @update 2003-10-22 15:37
   ѡ��û�ж����ѯ�����ֶε�ҵ���ѯ��������岻����ʾ����ֵ
 @end
 * ˵��: ѡ��ɸѡ����
 * ���ߣ�������
 * �������ڣ�(2003-4-3 8:55:06)
 * �汾��
 */
public class SetConditionPage extends nc.ui.pub.beans.UIPanel implements ActionListener {
    private JLabel ivjJLabel1 = null;
    private JPanel ivjJPanel1 = null;
    private JButton ivjBtnDel = null;
    private JButton ivjBtnDown = null;
    private JButton ivjBtnUp = null;
    private JButton ivjJBtnRightFldRef = null;
    //�༭������
    //private DefaultCellEditor[] m_refEditors = null;
    private JTable whereConTable = null;
    private JButton ivjBtnAdd = null;
    /**
      * ���еı༭��
      */
    private DefaultCellEditor[] m_oEditors = null;
    /**
      * ��ѯ������Ϣ
      */
    private ReportQueryVO m_oReportQueryVO = null;
    /**
     * ѡ��������Ϣ
     */
    private WhereConTableModel m_oWhereConModel = null;
    /**
     * ��һ����ѯ���涨��ѡ���ֶε��ֶ�����
     */
    private String m_strFirstSelFldName = "";
    /**
     * ��ѯ���涨��ѡ���ֶμ��ϵ��ֶ�����ֵHash(name,Integer(colType)
     */
    private Hashtable m_hashColTypes = null;
    /**
     * ��ѯ���涨��ѡ���ֶμ��ϵ�Hash(name,Expresion)
     */
    private Hashtable m_hashExpresions = null;
    /**
     *�ֶ��б༭����ֵ����
     */
    private JComboBox m_oFldComboBox = null;
    
    private Context m_ufoContext=null;
/**
 * SetConditionPage ������ע�⡣
 */
public SetConditionPage() {
	super();
	initialize();
}
/**
 * SetConditionPage ������ע�⡣
 * @param layout java.awt.LayoutManager
 */
public SetConditionPage(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * SetConditionPage ������ע�⡣
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public SetConditionPage(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * SetConditionPage ������ע�⡣
 * @param isDoubleBuffered boolean
 */
public SetConditionPage(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent e) {
    //����
	TableCellEditor cellEditor = getWhereConTable().getCellEditor();
	if(cellEditor != null){
		cellEditor.stopCellEditing();
	}
    if (e.getSource() == getBtnAdd()) {
        //��ֹ�༭̬
        if (whereConTable.getCellEditor() != null)
            whereConTable.getCellEditor().stopCellEditing();

        //�������
        int iIndex = m_oWhereConModel.getRowCount();

        WhereCondVO whereCondVO = new WhereCondVO();
        whereCondVO.setTypeflag("C");
        if (iIndex == 0) {
            whereCondVO.setRelationflag("where");
        } else {
            whereCondVO.setRelationflag("and");
        }
        whereCondVO.setLeftfld(m_strFirstSelFldName);
        whereCondVO.setOperator("=");
        whereCondVO.setRightfld("");
        m_oWhereConModel.addToTable(whereCondVO, whereConTable);

        //ѡ��������
        whereConTable.getSelectionModel().setSelectionInterval(iIndex, iIndex);
        Rectangle rect = whereConTable.getCellRect(iIndex, 4, true);
        whereConTable.scrollRectToVisible(rect);
        return;

    }

    //ɾ��
    else
        if (e.getSource() == getBtnDel()) {

            //��ֹ�༭̬
            if (whereConTable.getCellEditor() != null)
                whereConTable.getCellEditor().stopCellEditing();
            int iSelIndex = whereConTable.getSelectedRow();
            if (iSelIndex != -1) {
                m_oWhereConModel.removeFromTable(iSelIndex, whereConTable);
            }
            return;

        }
    //����
    else
        if (e.getSource() == getBtnDown()) {
            int selIndex = whereConTable.getSelectedRow();
            //����where���У�������"����"
            if (selIndex != 0) {
                m_oWhereConModel.moveDown(whereConTable);
            }
        }

    //����
    else
        if (e.getSource() == getBtnUp()) {
            int selIndex = whereConTable.getSelectedRow();
            //�ǵڶ��У����������ƶ�����֤"where"��
            if (selIndex > 1) {
                m_oWhereConModel.moveUp(whereConTable);
            }
        }
    //�Ҳ���������
    else
        if (e.getSource() == getJBtnRightFldRef()) {
            onRightFldRef();
        }
}
/**
 * ���� BtnAdd ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnAdd() {
	if (ivjBtnAdd == null) {
		try {
			ivjBtnAdd = new nc.ui.pub.beans.UIButton();
			ivjBtnAdd.setName("BtnAdd");
	//		ivjBtnAdd.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnAdd.setText("Add");
			ivjBtnAdd.setBounds(513, 170, 75, 22);
			// user code begin {1}
			String strAdd = StringResource.getStringResource("miufo1000080");  //"����"
			ivjBtnAdd.setText(strAdd);
			ivjBtnAdd.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnAdd;
}
/**
 * ���� JButton1 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnDel() {
	if (ivjBtnDel == null) {
		try {
			ivjBtnDel = new nc.ui.pub.beans.UIButton();
			ivjBtnDel.setName("BtnDel");
	//		ivjBtnDel.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnDel.setText("Delete");
			ivjBtnDel.setBounds(513, 212, 75, 22);
			// user code begin {1}
			String strDelete = StringResource.getStringResource("miufopublic243");  //"ɾ��"
			ivjBtnDel.setText(strDelete);
			ivjBtnDel.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnDel;
}
/**
 * ���� JButton3 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnDown() {
	if (ivjBtnDown == null) {
		try {
			ivjBtnDown = new nc.ui.pub.beans.UIButton();
			ivjBtnDown.setName("BtnDown");
	//		ivjBtnDown.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnDown.setText("Down");
			ivjBtnDown.setBounds(514, 298, 75, 22);
			// user code begin {1}
			String strDown = StringResource.getStringResource("miufo1001290");	//����
			ivjBtnDown.setText(strDown);
			ivjBtnDown.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnDown;
}
/**
 * ���� JButton2 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnUp() {
	if (ivjBtnUp == null) {
		try {
			ivjBtnUp = new nc.ui.pub.beans.UIButton();
			ivjBtnUp.setName("BtnUp");
	//		ivjBtnUp.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnUp.setText("Up");
			ivjBtnUp.setBounds(515, 255, 75, 22);
			// user code begin {1}
			String strUp = StringResource.getStringResource("miufo1001298");  //"����"
			ivjBtnUp.setText(strUp);
			ivjBtnUp.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnUp;
}
/**
 * ���� JBtnRightFldRef ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnRightFldRef() {
    if (ivjJBtnRightFldRef == null) {
        try {
            ivjJBtnRightFldRef = new nc.ui.pub.beans.UIButton();
            ivjJBtnRightFldRef.setName("JBtnRightFldRef");
            ivjJBtnRightFldRef.setText("RightFldRef");
            ivjJBtnRightFldRef.setBounds(478, 49, 75, 22);
            // user code begin {1}
            String strRightFldRef = StringResource.getStringResource("miufo1001433");  //"�Ҳ���������"
            ivjJBtnRightFldRef.setText(strRightFldRef);
            ivjJBtnRightFldRef.addActionListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBtnRightFldRef;
}
/**
 * ���� JLabel1 ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabel1.setName("JLabel1");
	//		ivjJLabel1.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabel1.setText("FilterCond");
			ivjJLabel1.setBounds(13, 15, 70, 16);
			ivjJLabel1.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strFilterCond = StringResource.getStringResource("miufo1001434");  //"ɸѡ����"
			ivjJLabel1.setText(strFilterCond);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}
/**
 * ���� JPanel1 ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getJPanel1() {
    if (ivjJPanel1 == null) {
        try {
            ivjJPanel1 = new UIPanel();
            ivjJPanel1.setName("JPanel1");
            ivjJPanel1.setLayout(null);
            ivjJPanel1.setBounds(13, 41, 460, 288);
            // user code begin {1}
            JScrollPane ps = new UIScrollPane(getWhereConTable());
            ivjJPanel1.setLayout(new BorderLayout());
            ivjJPanel1.add(ps, BorderLayout.CENTER);

            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJPanel1;
}
/**
 * ���where��������
 * �������ڣ�(2003-4-3 16:16:01)
 * @return nc.vo.iuforeport.businessquery.WhereCondVO[]
 */
public WhereCondVO[] getResultFromCond(StringBuffer sbWhereErr) {
    int nCount = m_oWhereConModel.getRowCount();
    WhereCondVO[] whereCondVOs = null;
    if (nCount > 0) {
        whereCondVOs = new WhereCondVO[nCount];
        Vector vec = m_oWhereConModel.getAll();
        int iLen = vec != null ? vec.size() : 0;
        whereCondVOs = new WhereCondVO[iLen];
        for (int i = 0; i < iLen; i++) {
            whereCondVOs[i] = (WhereCondVO) vec.get(i);
            String strLeftFld = whereCondVOs[i].getLeftfld();
            Object objRightFld = whereCondVOs[i].getRightfld();

            whereCondVOs[i].setRightfld(
                ReportQueryUtil.checkValidWhereCond(
                    ((Integer) m_hashColTypes.get(strLeftFld)).intValue(),
                    strLeftFld,
                    objRightFld,
                    sbWhereErr));

            //����������������ֶ�����ʱ����Expresion������ʱ���ڿ�¡�������leftFld��
            whereCondVOs[i].setExpression0((String) m_hashExpresions.get(strLeftFld));
            //����������������ֵ
            whereCondVOs[i].setColtype((Integer) m_hashColTypes.get(strLeftFld));
        }
    }
    //debug
    //sbWhereErr.append("debug...");
    //end
    return whereCondVOs;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-16 13:31:27)
 * @author������Ƽ
 * @return javax.swing.JTable
 */
private JTable getWhereConTable() {
    if (whereConTable == null) {
        whereConTable = new nc.ui.pub.beans.UITable();
        whereConTable.setAutoCreateColumnsFromModel(false);
        initWhereConTable();

        //������
        String[] colStrs = m_oWhereConModel.getHead();
        for (int k = 0; k < colStrs.length; k++) {
            TableCellRenderer renderer = new DefaultTableCellRenderer();
            TableCellEditor editor = null;
            TableColumn column = new TableColumn(k, 80, renderer, editor);
            whereConTable.addColumn(column);
        }

    }

    return whereConTable;
}
/**
 * ÿ�������׳��쳣ʱ������
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
	// System.out.println("--------- δ��׽�����쳣 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * ��ʼ����Ԫ�༭����
 *
 * �������ڣ�(2003-9-16 16:10:04)
 * @author������Ƽ
 */
private void initEditorValue() {
    int iLen = m_oWhereConModel.getHead().length;
    m_oEditors = new DefaultCellEditor[iLen];
    for (int i = 0; i < iLen; i++) {

        //���ù�ϵ���б༭��
        if (i == WhereConTableModel.COLUMN_RELATION_SYNBOL) {
            JComboBox cbbRelation = new UIComboBox(new Object[] { "and", "or" });
            m_oEditors[i] = new DefaultCellEditor(cbbRelation);
        }

        //�����ֶ��б༭��
        else
            if (i == WhereConTableModel.COLUMN_FIELD) {
                JComboBox cbbFld = cbbFld = new UIComboBox();
                m_oEditors[i] = new DefaultCellEditor(cbbFld);
                m_oFldComboBox = cbbFld;
            }

        //���ò������б༭��
        else
            if (i == WhereConTableModel.COLUMN_COMPARE_SYNBOL) {
                JComboBox cbbOperator =
                    new UIComboBox(new Object[] { "=", ">", "<", "<>", ">=", "<=", "like", "in" });
                m_oEditors[i] = new DefaultCellEditor(cbbOperator);
            }

        //�����Ҳ������༭��
        else
            if (i == WhereConTableModel.COLUMN_RIGHT_OPE) {
                m_oEditors[i] = new DefaultCellEditor(new UITextField());
            }
    }
}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SetConditionPage");
		setLayout(null);
		setSize(611, 350);
		add(getJLabel1(), getJLabel1().getName());
		add(getJPanel1(), getJPanel1().getName());
		add(getBtnDel(), getBtnDel().getName());
		add(getBtnUp(), getBtnUp().getName());
		add(getBtnDown(), getBtnDown().getName());
		add(getBtnAdd(), getBtnAdd().getName());
		add(getJBtnRightFldRef(), getJBtnRightFldRef().getName());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	// user code end
}
/**
 * ��ʼ����ѡ�񱨱��ѯ�ֶεı�ģ�͡�
 *
 * �������ڣ�(2003-9-16 13:51:17)
 * @author������Ƽ
 */
private void initWhereConTable() {
    Vector vecWhereCon = new Vector();
    m_oWhereConModel = new WhereConTableModel(vecWhereCon);
    whereConTable.setModel(m_oWhereConModel);

    //���ñ�����
    whereConTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    whereConTable.sizeColumnsToFit(-1);
    whereConTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new UIFrame();
		SetConditionPage aSetConditionPage;
		aSetConditionPage = new SetConditionPage();
		frame.setContentPane(aSetConditionPage);
		frame.setSize(aSetConditionPage.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoPanel's main():Exception");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * �Ҳ��������ա�
 *
 * �������ڣ�(2003-9-17 11:39:45)
 * @author������Ƽ
 */
private void onRightFldRef() {
    int selIndex = whereConTable.getSelectedRow();
    if (selIndex >= 0) {
        WhereCondVO whereCondVO =
            (WhereCondVO) m_oWhereConModel.getObjectByRow(selIndex);
        Object oldRightFld = whereCondVO.getRightfld();
        if (oldRightFld instanceof Double) {
            oldRightFld = new Double(((Double) oldRightFld).doubleValue());
        } else {
            oldRightFld = new String((String) oldRightFld);
        }

        RightFldRefDlg rightFldRefDlg =
            new RightFldRefDlg(
                this,
                (Integer) m_hashColTypes.get(whereCondVO.getLeftfld()),
                oldRightFld);
        rightFldRefDlg.setUfoContext(getUfoContext());
        rightFldRefDlg.setModal(true);
        rightFldRefDlg.showModal();

        //�õ��༭���
        if (rightFldRefDlg.getResult() == UfoDialog.ID_OK) {
            Object newRightFld = rightFldRefDlg.getReturnRightFld();
            whereCondVO.setRightfld(newRightFld);
        }

    } else {
        UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001435"),this);  //"����ѡ��һ�У�"
    }
}
/**
 * �򵼵�һ��ҳ��ѡ�񱨱��ѯ��������Ӧ��wherecon��ʼ���棺
 *
 * ���ߣ�������
 * �������ڣ�(2003-4-9 10:52:58)
 * �޸��ߣ�����Ƽ
 * �޸����ڣ�(2003-9-16 09:52:58)
 * ������ @param��<|>
 * ����ֵ��@return��
 *
 * @param newReportQueryVO com.ufsoft.iuforeport.reporttool.data.ReportQueryVO
 */
public void setReportQueryVO(ReportQueryVO newReportQueryVO) {
    if (newReportQueryVO == null || newReportQueryVO.getQuerydef() == null) {
        return;
    }
    m_oReportQueryVO = newReportQueryVO;

    String[] strSelFldNames = null;
    QuerySimpleDef def = m_oReportQueryVO.getQuerydef();
    SelectFldVO[] sfs = def.getSelectFlds();
    int iLen = sfs != null ? sfs.length : 0;
    if (iLen <= 0) {
        return;
    }
    //��ʼ����ģ�͵ı༭�����Լ���һ��ѡ���ֶε�ֵ
    strSelFldNames = new String[iLen];
    m_hashColTypes = new Hashtable();
    m_hashExpresions = new Hashtable();
    for (int i = 0; i < iLen; i++) {
        strSelFldNames[i] = sfs[i].getFldname();
        m_hashColTypes.put(strSelFldNames[i], sfs[i].getColtype());
        m_hashExpresions.put(strSelFldNames[i], sfs[i].getExpression());
    }
    m_strFirstSelFldName = strSelFldNames[0];
    if (m_oEditors == null) {
        initEditorValue();

        //�����б༭��
        setValueEditor();
    }
    if (m_oFldComboBox != null) {
        m_oFldComboBox.removeAllItems();
        int iSize = strSelFldNames != null ? strSelFldNames.length : 0;
        for (int i = 0; i < iSize; i++) {
            m_oFldComboBox.addItem(strSelFldNames[i]);
        }
    }

    //��������where������ģ������
    WhereCondVO[] whereConVOs = m_oReportQueryVO.getWhereCondVOs();
    Vector vecWhereCons = new Vector();
    if (whereConVOs == null || whereConVOs.length == 0) {
        vecWhereCons = new Vector();
    } else {
        vecWhereCons = new Vector(Arrays.asList(whereConVOs));
    }
    m_oWhereConModel.resetModel(vecWhereCons);
}
/**
 * ����ֵ��CellEditor
 *
 * �������ڣ�(2003-9-16 16:28:20)
 * @author������Ƽ
 */
private void setValueEditor() {
    if (m_oEditors != null) {
        int iLen = m_oWhereConModel.getHead().length;
        for (int iCol = 0; iCol < iLen; iCol++) {
            TableColumn tc = getWhereConTable().getColumnModel().getColumn(iCol);
            //��ö���
            if (iCol == 0 || iCol == 2 || iCol == 3) {
                //���ù�ϵ�����������б༭��
                tc.setCellEditor(m_oEditors[iCol]);
            } else
                if (iCol == 1) {
                    tc.setCellEditor(m_oEditors[iCol]);
                }
        }
    }
}
/**
 * ����ֵ��CellEditor
 * �������ڣ�(2001-7-17 13:18:15)
 * @param iRow int
 * @param iCol int
 */
private void setValueEditor(int iRow, int iCol) {
    TableColumn tc = getWhereConTable().getColumnModel().getColumn(iCol);

    //��ö���
    if (iCol == 0 || iCol == 2 || iCol == 3) {
        //���ù�ϵ�����������б༭��
        tc.setCellEditor(m_oEditors[iCol]);
    } else
        if (iCol == 1) {
            tc.setCellEditor(m_oEditors[iCol]);
        }

}

public Context getUfoContext() {
	return m_ufoContext;
}
public void setUfoContext(Context context) {
	m_ufoContext = context;
}
}


