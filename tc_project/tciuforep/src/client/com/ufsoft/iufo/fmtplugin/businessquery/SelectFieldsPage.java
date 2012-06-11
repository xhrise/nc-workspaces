package com.ufsoft.iufo.fmtplugin.businessquery;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
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

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.businessquery.SelectFldVO;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.FieldMap;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.FieldMapUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.OrderByFld;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.measure.DialogRefListener;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsModel;

public class SelectFieldsPage
    extends nc.ui.pub.beans.UIPanel
    implements ActionListener ,DialogRefListener {

    private JLabel ivjJLabel1 = null;
    private JLabel ivjJLabel2 = null;
    private JPanel ivjJPanelWaitFor = null;
    private JPanel ivjJPanelHaveSel = null;
    private JTable waitForSelectTable = null;
    private JTable haveSelectTable = null;
    private JButton ivjJBtnKeyRef = null;
    private JButton ivjJBtnMeasRef = null;
    private JLabel ivjJLabelMapKey = null;
    private JLabel ivjJLabelMapMeas = null;
    private JButton ivjBtnBottom = null;
    private JButton ivjBtnDown = null;
    private JButton ivjBtnTop = null;
    private JButton ivjBtnUp = null;
    private JButton ivjBtnLeft = null;
    private JButton ivjBtnRight = null;
    private JButton ivjBtnToLeft = null;
    //�����б༭��
    //private DefaultCellEditor[] m_refEditors = null;
    private UIRefCellEditor m_refEditor = null;
    private JButton ivjBtnToRight = null;
    /**
     * ��ѯ������Ϣ
     */
    private ReportQueryVO m_ReportQueryVO = null;
    /**
     * ��ѡ��ѯ�����ĳ��ѯ�ֶ���Ϣ
     */
    private FieldTableModel m_oWaitForSelMoldel = null;
    /**
     * ��ѡ�ı����ѯ�ֶ��б�
     */
    private FieldTableModel m_oHaveSelMoldel = null;
    /**
     * �����ڶ���
     */
    BQueryPropertyDlg m_ParentDlg = null;

    //���նԻ���ӿ���Ҫ���������
	private Component refcomp = null;
	private Component refDialog = null;
	private CellsModel _cellsModel;
	private Context _contextVO;
	private boolean bValidMapped = false;
/**
 * SelectFieldsPage ������ע�⡣
 */
private SelectFieldsPage() {
	super();
	initialize();
}
/**
 * �����ڲ�ѯ���Զ��崰�ڵĹ���������
 *
 * �������ڣ�(2003-9-11 12:13:20)
 * @author������Ƽ
 * @param parentDlg com.ufsoft.iuforeport.reporttool.query.BQueryPropertyDlg
 */
public SelectFieldsPage(BQueryPropertyDlg parentDlg,CellsModel cellsModel,Context contextVO) {
	super();
	initialize();
	this.m_ParentDlg = parentDlg;
	_cellsModel = cellsModel;
	_contextVO = contextVO;
}
///**
// * SelectFieldsPage ������ע�⡣
// * @param layout java.awt.LayoutManager
// */
//public SelectFieldsPage(java.awt.LayoutManager layout) {
//	super(layout);
//}
///**
// * SelectFieldsPage ������ע�⡣
// * @param layout java.awt.LayoutManager
// * @param isDoubleBuffered boolean
// */
//public SelectFieldsPage(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
//	super(layout, isDoubleBuffered);
//}
///**
// * SelectFieldsPage ������ע�⡣
// * @param isDoubleBuffered boolean
// */
//public SelectFieldsPage(boolean isDoubleBuffered) {
//	super(isDoubleBuffered);
//}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent event) {
    //���
    if (event.getSource() == getBtnBottom()) {
        m_oHaveSelMoldel.moveTOBottom(haveSelectTable);
    }

    //����
    else
        if (event.getSource() == getBtnDown()) {
            m_oHaveSelMoldel.moveDown(haveSelectTable);
        }
    //����
    else
        if (event.getSource() == getBtnTop()) {
            m_oHaveSelMoldel.moveTOTop(haveSelectTable);
        }
    //����
    else
        if (event.getSource() == getBtnUp()) {
            m_oHaveSelMoldel.moveUp(haveSelectTable);
        }

    //ȫ��ѡ
    else
        if (event.getSource() == getBtnLeft()) {
            int nCount = haveSelectTable.getRowCount();
            if (nCount > 0) {

                for (int i = 0; i < nCount; i++) {
                    ReportSelectFldVO repSelFldVO =
                        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(0);

                    //�Զ�ƥ�䱨����������ͬ��ָ��/�ؼ��֣�
                    //...mapname,maptype,mappk
                    //У���ѯ�����ֶ��Ƿ��ѱ�ƥ��,��У�����������Ƿ����,ͬ���ڡ�ȷ���н��С�

                    m_oHaveSelMoldel.removeFromTable(0, haveSelectTable);
                    m_oWaitForSelMoldel.addToTable(
                        repSelFldVO.getSelectFldVO(),
                        waitForSelectTable);
                }

                Rectangle rect =
                    waitForSelectTable.getCellRect(waitForSelectTable.getRowCount(), 4, true);
                waitForSelectTable.scrollRectToVisible(rect);
            }
            return;

        }
    //ȫѡ
    else
        if (event.getSource() == getBtnRight()) {
            int nCount = waitForSelectTable.getRowCount();
            if (nCount > 0) {

                for (int i = 0; i < nCount; i++) {
                    SelectFldVO selectFldVO = (SelectFldVO) m_oWaitForSelMoldel.getObjectByRow(0);
                    //ת��Ϊ�����ѯ�ֶ�����
                    ReportSelectFldVO repSelFldVO = new ReportSelectFldVO(selectFldVO);

                    //�Զ�ƥ�䱨����������ͬ��ָ��/�ؼ��֣�
                    //...mapname,maptype,mappk
                    //У���ѯ�����ֶ��Ƿ��ѱ�ƥ��,��У�����������Ƿ����,ͬ���ڡ�ȷ���н��С�

                    m_oWaitForSelMoldel.removeFromTable(0, waitForSelectTable);
                    m_oHaveSelMoldel.addToTable(repSelFldVO, haveSelectTable);
                }

                Rectangle rect =
                    haveSelectTable.getCellRect(haveSelectTable.getRowCount(), 4, true);
                haveSelectTable.scrollRectToVisible(rect);
            }
            return;

        }
    //����
    else
        if (event.getSource() == getBtnToLeft()) {
            int[] iSelIndexes = haveSelectTable.getSelectedRows();
            int iSelCount = iSelIndexes != null ? iSelIndexes.length : 0;
            for (int i = 0; i < iSelCount; i++) {
                int iSelIndex = haveSelectTable.getSelectedRow();
                if (iSelIndex != -1) {
                    //����ˢ��
                    ReportSelectFldVO repSelFldVO =
                        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);
                    m_oHaveSelMoldel.removeFromTable(iSelIndex, haveSelectTable);
                    m_oWaitForSelMoldel.addToTable(
                        repSelFldVO.getSelectFldVO(),
                        waitForSelectTable);
                    if (i == iSelCount - 1) {
                        Rectangle rect = waitForSelectTable.getCellRect(iSelIndex, 4, true);
                        waitForSelectTable.scrollRectToVisible(rect);
                    }
                }
            }
            return;

        }

    //����
    else
        if (event.getSource() == getBtnToRight()) {
            int[] iSelIndexes = waitForSelectTable.getSelectedRows();
            int iSelCount = iSelIndexes != null ? iSelIndexes.length : 0;
            for (int i = 0; i < iSelCount; i++) {
                int iSelIndex = waitForSelectTable.getSelectedRow();
                if (iSelIndex != -1) {
                    SelectFldVO selectFldVO =
                        (SelectFldVO) m_oWaitForSelMoldel.getObjectByRow(iSelIndex);
                    //ת��Ϊ�����ѯ�ֶ�����
                    ReportSelectFldVO repSelFldVO = new ReportSelectFldVO(selectFldVO);

                    //�Զ�ƥ�䱨����������ͬ��ָ��/�ؼ��֣�
                    //...mapname,maptype,mappk
                    //У���ѯ�����ֶ��Ƿ��ѱ�ƥ��,��У�����������Ƿ����,ͳһ�ڡ�ȷ���н��С�
                    m_oWaitForSelMoldel.removeFromTable(iSelIndex, waitForSelectTable);
                    m_oHaveSelMoldel.addToTable(repSelFldVO, haveSelectTable);
                    if (i == iSelCount - 1) {
                        Rectangle rect = haveSelectTable.getCellRect(iSelIndex, 4, true);
                        haveSelectTable.scrollRectToVisible(rect);
                    }
                }
            }
            return;

        }
    //ָ�����
    else
        if (event.getSource() == ivjJBtnMeasRef) {
            onMeasureRef();
        }
    //�ؼ��ֲ���
    else
        if (event.getSource() == ivjJBtnKeyRef) {
            onKeyRef();
        }

}
/**
	�رղ��նԻ���֮ǰ�����ע������
	ע�⣺���ڿ��ܳ���ͬʱ����������մ��ڵ������ÿ�����ڹر�ʱ������ô˷�������Ҫ�жϹرյĴ����Ƿ�Ϊ��ǰע��Ĵ���
*/
public void beforeDialogClosed(java.awt.Component refDlg) {
    //ֻ�е�ǰע��Ĵ��ڲ�����������ȡ��ע�����
    if (this.refDialog == refDlg) {
        //��ע������ÿ�
        refcomp = null;
        refDialog = null;
    }
}
/**
 * ���յ���ǰ�ļ��顣
 *
 * �������ڣ�(2003-9-11 14:53:21)
 * @author������Ƽ
 * @return boolean
 * @param iSelIndex int
 */
private boolean doCheckRef(int iSelIndex) {
    //���û��ѡ��һ�н��б༭�����������
    if (iSelIndex < 0) {
        UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001427")+"!",this);//����ѡ��һ��\"��ѡ��Ŀ\"��
        return false;
    }

    return true;
}
/**
 * ���� JButton7 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnBottom() {
	if (ivjBtnBottom == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltobottom.jpg");
			ivjBtnBottom = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnBottom.setName("JBtnToBottom");
			ivjBtnBottom.setToolTipText("seltobottom");
	//		ivjBtnBottom.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnBottom.setBounds(469, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToBottom = StringResource.getStringResource("miufo1001294");  //"������β"
			ivjBtnBottom.setToolTipText(strSelToBottom);
			ivjBtnBottom.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnBottom;
}
/**
 * ���� JButton5 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnDown() {
	if (ivjBtnDown == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltodown.jpg");
			ivjBtnDown = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnDown.setName("JBtnDown");
			ivjBtnDown.setToolTipText("seltodown");
	//		ivjBtnDown.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnDown.setBounds(345, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToDown = StringResource.getStringResource("miufo1001290");  //"����"
			ivjBtnDown.setToolTipText(strSelToDown);
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
 * ���� JButton4 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnLeft() {
	if (ivjBtnLeft == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/selstoleft.jpg");
			ivjBtnLeft = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnLeft.setName("BtnLeft");
			ivjBtnLeft.setToolTipText("selstoleft");
			ivjBtnLeft.setBounds(194, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelsToLeft = StringResource.getStringResource("miufo1001292");  //"��������"
			ivjBtnLeft.setToolTipText(strSelsToLeft);
			ivjBtnLeft.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnLeft;
}
/**
 * ���� JButton2 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnRight() {
	if (ivjBtnRight == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/selstoright.jpg");
			ivjBtnRight = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnRight.setName("JBtnSelsToR");
			ivjBtnRight.setToolTipText("selstoright");
			ivjBtnRight.setBounds(85, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelsToRight = StringResource.getStringResource("miufo1001293");  //"��������"
			ivjBtnRight.setToolTipText(strSelsToRight);
			ivjBtnRight.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnRight;
}
/**
 * ���� JButton3 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnToLeft() {
	if (ivjBtnToLeft == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoleft.jpg");
			ivjBtnToLeft = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnToLeft.setName("BtnToLeft");
			ivjBtnToLeft.setToolTipText("seltoleft");
			ivjBtnToLeft.setBounds(141, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToLeft = StringResource.getStringResource("miufo1001295");  //"����"
			ivjBtnToLeft.setToolTipText(strSelToLeft);
			ivjBtnToLeft.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnToLeft;
}
/**
 * ���� JButton8 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnTop() {
	if (ivjBtnTop == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltotop.jpg");
			ivjBtnTop = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnTop.setName("JBtnToTop");
			ivjBtnTop.setToolTipText("seltotop");
			ivjBtnTop.setBounds(530, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToTop = StringResource.getStringResource("miufo1001297");  //"������ͷ"
			ivjBtnTop.setToolTipText(strSelToTop);
			ivjBtnTop.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnTop;
}
/**
 * ���� JButton1 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnToRight() {
	if (ivjBtnToRight == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoright.jpg");
			ivjBtnToRight = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnToRight.setName("JBtnToR");
			ivjBtnToRight.setToolTipText("seltoright");
			ivjBtnToRight.setBounds(33, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToRight = StringResource.getStringResource("miufo1001296");  //"����"
			ivjBtnToRight.setToolTipText(strSelToRight);
			ivjBtnToRight.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnToRight;
}
/**
 * ���� JButton6 ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getBtnUp() {
	if (ivjBtnUp == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoup.jpg");
			ivjBtnUp = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnUp.setName("JBtnUp");
			ivjBtnUp.setToolTipText("seltoup");
			ivjBtnUp.setBounds(408, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToUp = StringResource.getStringResource("miufo1001298");  //"����"
			ivjBtnUp.setToolTipText(strSelToUp);
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
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-4 9:39:22)
 * ������ @param��<|>
 * ����ֵ��@return��
 *
 * @return javax.swing.JTable
 */
private JTable getHaveSelectTable() {
    if (haveSelectTable == null) {
        haveSelectTable = new nc.ui.pub.beans.UITable();
        haveSelectTable.setAutoCreateColumnsFromModel(false);
        initHaveSelectTable();
        haveSelectTable.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        String[] colStrs = m_oHaveSelMoldel.getHead();
        for (int k = 0; k < colStrs.length; k++) {
            TableCellRenderer renderer;
            DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
            //textRenderer.setHorizontalAlignment( colStrs[k].alignment);
            renderer = textRenderer;

            TableCellEditor editor = null;

            if (k == FieldTableModel.COLUMN_SORT) {
            	String[] strSortTypes = new String[]{
            			FieldTableModel.getSortTypeUIStr(OrderByFld.ORDER_NULL_STR),
						FieldTableModel.getSortTypeUIStr(OrderByFld.ORDER_AESC_STR),
						FieldTableModel.getSortTypeUIStr(OrderByFld.ORDER_DESC_STR),
            	};
                JComboBox c0 = new UIComboBox(strSortTypes);//FieldTableModel.getSortType()
                editor = new DefaultCellEditor(c0);
                //TableColumnModel tcm = haveSelectTable.getColumnModel();
                //TableColumn typetc = tcm.getColumn(FieldTableModel.COLUMN_SORT);
                //typetc.setCellEditor(new DefaultCellEditor(c0));

            } else {
                editor = new DefaultCellEditor(new UITextField());
            }

            TableColumn column = new TableColumn(k, 80, renderer, editor);
            haveSelectTable.addColumn(column);

        }
 //       haveSelectTable.getTableHeader().setFont(new Font("dialog", 0, 12));
 //       haveSelectTable.setFont(new Font("dialog", 0, 12));
    }
    return haveSelectTable;

}
/**
 * ��ñ��
 * �������ڣ�(2003-4-2 13:48:52)
 * @return nc.ui.pub.beans.UITable
 */
private FieldTableModel getHaveSelectTM() {
	return (FieldTableModel) haveSelectTable.getModel();
}
/**
 * ���� JBtnKeyRef ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnKeyRef() {
	if (ivjJBtnKeyRef == null) {
		try {
			ivjJBtnKeyRef = new nc.ui.pub.beans.UIButton();
			ivjJBtnKeyRef.setName("JBtnKeyRef");
			ivjJBtnKeyRef.setText("...");
			ivjJBtnKeyRef.setBounds(513, 318, 22, 22);
			// user code begin {1}
			ivjJBtnKeyRef.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBtnKeyRef;
}
/**
 * ���� JBtnMeasRef ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnMeasRef() {
	if (ivjJBtnMeasRef == null) {
		try {
			ivjJBtnMeasRef = new nc.ui.pub.beans.UIButton();
			ivjJBtnMeasRef.setName("JBtnMeasRef");
			ivjJBtnMeasRef.setText("...");
			ivjJBtnMeasRef.setBounds(367, 316, 22, 22);
			// user code begin {1}
			ivjJBtnMeasRef.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBtnMeasRef;
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
			ivjJLabel1.setText("BeSelectItem:");
			ivjJLabel1.setBounds(15, 11, 71, 16);
			ivjJLabel1.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strBeSelectItem = StringResource.getStringResource("miufo1001428");  //"��ѡ��Ŀ"
			ivjJLabel1.setText(strBeSelectItem);
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
 * ���� JLabel2 ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabel2() {
	if (ivjJLabel2 == null) {
		try {
			ivjJLabel2 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabel2.setName("JLabel2");
	//		ivjJLabel2.setFont(new java.awt.Font("dialog", 0, 12));
			ivjJLabel2.setText("SelectedItems:");
			ivjJLabel2.setBounds(271, 11, 89, 16);
			ivjJLabel2.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strSelectedItems =  StringResource.getStringResource("miufo1001429");  //"��ѡ��Ŀ"
			ivjJLabel2.setText(strSelectedItems);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel2;
}
/**
 * ���� JLabelMapKey ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabelMapKey() {
	if (ivjJLabelMapKey == null) {
		try {
			ivjJLabelMapKey = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelMapKey.setName("JLabelMapKey");
			ivjJLabelMapKey.setText("MapKey:");
			ivjJLabelMapKey.setBounds(416, 321, 75, 16);
			// user code begin {1}
			String strMapKey = StringResource.getStringResource("miufo1001430");  //"ӳ��ؼ���"
			ivjJLabelMapKey.setText(strMapKey);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelMapKey;
}
/**
 * ���� JLabelMapMeas ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabelMapMeas() {
	if (ivjJLabelMapMeas == null) {
		try {
			ivjJLabelMapMeas = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelMapMeas.setName("JLabelMapMeas");
			ivjJLabelMapMeas.setText("MapMeasure:");
			ivjJLabelMapMeas.setBounds(300, 323, 64, 16);
			// user code begin {1}
			String strMapMeasure = StringResource.getStringResource("miufo1001431");  //"ӳ��ָ��"
			ivjJLabelMapMeas.setText(strMapMeasure);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelMapMeas;
}
/**
 * ���� JPanel2 ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getJPanelHaveSel() {
	if (ivjJPanelHaveSel == null) {
		try {
			ivjJPanelHaveSel = new UIPanel();
			ivjJPanelHaveSel.setName("JPanelHaveSel");
			ivjJPanelHaveSel.setLayout(null);
			ivjJPanelHaveSel.setBounds(271, 30, 318, 247);
			// user code begin {1}
			JScrollPane ps = new UIScrollPane(getHaveSelectTable());
			ivjJPanelHaveSel.setLayout(new BorderLayout());
			ivjJPanelHaveSel.add(ps, BorderLayout.CENTER);

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanelHaveSel;
}
/**
 * ���� JPanel1 ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getJPanelWaitFor() {
	if (ivjJPanelWaitFor == null) {
		try {
			ivjJPanelWaitFor = new UIPanel();
			ivjJPanelWaitFor.setName("JPanelWaitFor");
			ivjJPanelWaitFor.setLayout(null);
			ivjJPanelWaitFor.setBounds(12, 29, 241, 247);
			// user code begin {1}
			JScrollPane ps2 = new UIScrollPane(getWaitForSelectTable());
			ivjJPanelWaitFor.setLayout(new BorderLayout());
			ivjJPanelWaitFor.add(ps2, BorderLayout.CENTER);

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanelWaitFor;
}
/**
	�õ����նԻ������
*/
public java.awt.Component getRefDialog() {
	return refDialog;
}
/**
	�õ����ղ������
*/
public java.awt.Component getRefOper() {
	return refcomp;
}
/**
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-9 10:52:40)
 * ������ @param��<|>
 * ����ֵ��@return��
 *
 * @return com.ufsoft.iuforeport.reporttool.data.ReportQueryVO
 */
public ReportQueryVO getReportQueryVO() {
	return m_ReportQueryVO;
}
/**
 * ���reportselectfldvo����
 * �������ڣ�(2003-4-3 16:16:01)
 * @return ReportSelectFldVO[]
 */
public ReportSelectFldVO[] getResultSelected() {
    int nCount = m_oHaveSelMoldel.getRowCount();
    ReportSelectFldVO[] repSelFldVOs = null;
    if (nCount > 0) {
        repSelFldVOs = new ReportSelectFldVO[nCount];
        Vector vec = m_oHaveSelMoldel.getAll();
        vec.copyInto(repSelFldVOs);
    }

    return repSelFldVOs;
}
/**
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-4 9:39:22)
 * ������ @param��<|>
 * ����ֵ��@return��
 *
 * @return javax.swing.JTable
 */
private JTable getWaitForSelectTable() {
    //waitForSelectTable = null;
    if (waitForSelectTable == null) {
        waitForSelectTable = new nc.ui.pub.beans.UITable();
        waitForSelectTable.setAutoCreateColumnsFromModel(false);
        initWaitForSelectTable();
        waitForSelectTable.setSelectionMode(
            ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        String[] colStrs = m_oWaitForSelMoldel.getHead();
        for (int k = 0; k < colStrs.length; k++) {
            TableCellRenderer renderer;
            DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
            //textRenderer.setHorizontalAlignment( colStrs[k].alignment);
            renderer = textRenderer;

            TableCellEditor editor = null;

            {
                editor = new DefaultCellEditor(new UITextField());
            }

            TableColumn column = new TableColumn(k, 80, renderer, editor);
            waitForSelectTable.addColumn(column);

        }
 //       waitForSelectTable.getTableHeader().setFont(new Font("dialog", 0, 12));
 //       waitForSelectTable.setFont(new Font("dialog", 0, 12));
    }

    return waitForSelectTable;
}
/**
 * ��ñ��
 * �������ڣ�(2003-4-2 13:48:52)
 * @return nc.ui.pub.beans.UITable
 */
private FieldTableModel getWaitForSelectTM() {
	return (FieldTableModel) waitForSelectTable.getModel();
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
 * ��ʼ����ѡ�񱨱��ѯ�ֶεı�ģ��
 * �������ڣ�(2003-4-2 13:57:39)
 */
private void initHaveSelectTable() {
	Vector vecHaveSelect = null;
	vecHaveSelect = new Vector();
	m_oHaveSelMoldel = new FieldTableModel(vecHaveSelect, false);
	haveSelectTable.setModel(m_oHaveSelMoldel);

	//���ñ�����
	haveSelectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	haveSelectTable.sizeColumnsToFit(-1);
	haveSelectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

}
/**
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
    try {
        // user code begin {1}
        // user code end
        setName("SelectFieldsPage");
 //       setFont(new java.awt.Font("dialog", 0, 12));
        setLayout(null);
        setSize(611, 356);
        add(getJLabel1(), getJLabel1().getName());
        add(getJLabel2(), getJLabel2().getName());
        add(getBtnToRight(), getBtnToRight().getName());
        add(getBtnRight(), getBtnRight().getName());
        add(getBtnToLeft(), getBtnToLeft().getName());
        add(getBtnLeft(), getBtnLeft().getName());
        add(getBtnDown(), getBtnDown().getName());
        add(getBtnUp(), getBtnUp().getName());
        add(getBtnBottom(), getBtnBottom().getName());
        add(getBtnTop(), getBtnTop().getName());
        add(getJPanelWaitFor(), getJPanelWaitFor().getName());
        add(getJPanelHaveSel(), getJPanelHaveSel().getName());
        add(getJLabelMapMeas(), getJLabelMapMeas().getName());
        add(getJBtnMeasRef(), getJBtnMeasRef().getName());
        add(getJLabelMapKey(), getJLabelMapKey().getName());
        add(getJBtnKeyRef(), getJBtnKeyRef().getName());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    // user code end
}
/**
 * ��ʼ����ѡ�񱨱��ѯ�ֶεı�ģ�͡�
 *
 * �������ڣ�(2003-9-10 11:49:00)
 * @author������Ƽ
 */
private void initWaitForSelectTable() {
    Vector vecWaitForFld = null;
    vecWaitForFld = new Vector();
    m_oWaitForSelMoldel = new FieldTableModel(vecWaitForFld, true);
    waitForSelectTable.setModel(m_oWaitForSelMoldel);

    //���ñ�����
    waitForSelectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    waitForSelectTable.sizeColumnsToFit(-1);
    waitForSelectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new UIFrame();
		SelectFieldsPage aSelectFieldsPage;
		aSelectFieldsPage = new SelectFieldsPage();
		frame.setContentPane(aSelectFieldsPage);
		frame.setSize(aSelectFieldsPage.getSize());
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
		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoPanel's main() :Exception");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * ��Ӧ�ؼ��ֲ��ա�
 *
 * �������ڣ�(2003-9-11 11:59:56)
 * @author������Ƽ
 */
private void onKeyRef() {

    //���û��ѡ��һ�н��б༭�����������
    int iSelIndex = haveSelectTable.getSelectedRow();
    if (!doCheckRef(iSelIndex)) {
        return;
    }

    ReportSelectFldVO repSelFldVO =
        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);

  
    //�����ѯ�ֶε����������Ƿ����ƥ��Ϊ�ؼ���:colTypeĿǰû��ֵ
    ReportQueryUtil.isMappedDataType((repSelFldVO.getSelectFldVO().getColtype()==null)?MeasureVO.TYPE_CHAR:
        repSelFldVO.getSelectFldVO().getColtype().intValue(),
        MeasureVO.TYPE_CHAR);

    //�õ�ȫ��Ĺؼ����б����(��ӳ��״̬)
    String strQueryGUID = null;
    if (m_ReportQueryVO != null && m_ReportQueryVO.getQuerydef() != null) {
        strQueryGUID = m_ReportQueryVO.getQuerydef().getGUID();
    }
//    ReportSelectFldVO[] selFlds = m_ReportQueryVO.getSelectFlds();
    Vector  vecSelFld = m_oHaveSelMoldel.getAll();
//    ReportSelectFldVO[] selFlds = (ReportSelectFldVO[])m_oHaveSelMoldel.getAll().toArray();
    
    ReportSelectFldVO[] selFlds = new ReportSelectFldVO[vecSelFld.size()];
    for(int i=0;i<vecSelFld.size();i++){
    	selFlds[i] = (ReportSelectFldVO)vecSelFld.get(i);
    }
    ReportQueryRefKeyWordVO[] allRefKeyVOs =
        ReportQueryUtil.getReportAllRQRefKeyVOs(selFlds, _cellsModel);
    
    ReportQueryKeyWordRefDlg repQueryKeyWordRefDlg =
        new ReportQueryKeyWordRefDlg(m_ParentDlg,this, allRefKeyVOs);
//    repQueryKeyWordRefDlg.setReportQueryVO(m_ReportQueryVO);
    //repQueryKeyWordRefDlg.setLocationRelativeTo(this);
    //repQueryKeyWordRefDlg.setModal(false);
    //repQueryKeyWordRefDlg.show();

    while (true) {
        if (repQueryKeyWordRefDlg.isInitOver) {
            repQueryKeyWordRefDlg.setLocationRelativeTo(this);
            repQueryKeyWordRefDlg.setModal(false);
            repQueryKeyWordRefDlg.show();
            break;
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ee) {
                break;
            }
        }
    }
}
/**
 * ��Ӧָ����ա�
 *
 * �������ڣ�(2003-9-11 12:03:44)
 * @author������Ƽ
 */
private void onMeasureRef() {
    //���û��ѡ��һ�н��б༭�����������
    int iSelIndex = haveSelectTable.getSelectedRow();
    if (!doCheckRef(iSelIndex)) {
        return;
    }

    ReportSelectFldVO repSelFldVO =
        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);
  //�õ�ȫ���ָ���б����
    String strQueryGUID = null;
    if (m_ReportQueryVO != null && m_ReportQueryVO.getQuerydef() != null) {
        strQueryGUID = m_ReportQueryVO.getQuerydef().getGUID();
    }
    Vector  vecSelFld = m_oHaveSelMoldel.getAll();
    ReportSelectFldVO[] selFlds = new ReportSelectFldVO[vecSelFld.size()];
    for(int i=0;i<vecSelFld.size();i++){
    	selFlds[i] = (ReportSelectFldVO)vecSelFld.get(i);
    }
    ReportQueryRefMeasVO[] allRqRefMeasureVOs =
        ReportQueryUtil.getReportAllRQRefMeasureVOs(selFlds, _cellsModel, _contextVO);
    
    ReportQueryMeasRefDlg repQueryMeasRefDlg =
        new ReportQueryMeasRefDlg(
        		m_ParentDlg,
            this,
            repSelFldVO,
            ReportBusinessQuery.getInstance(_cellsModel),
            allRqRefMeasureVOs);
    //repQueryMeasRefDlg.setLocationRelativeTo(this);
    //repQueryMeasRefDlg.setModal(false);
    //repQueryMeasRefDlg.show();
    while (true) {
        if (repQueryMeasRefDlg.isInitOver) {
            repQueryMeasRefDlg.setLocationRelativeTo(this);
            repQueryMeasRefDlg.setModal(false);
            repQueryMeasRefDlg.show();
            break;
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ee) {
                break;
            }
        }
    }

}
/**
	��Ӧ�����������
*/
public void onRef(java.util.EventObject ev) {
    try {
        if (ev.getSource() == refcomp) {
            int iSelIndex = haveSelectTable.getSelectedRow();
            if (this.refDialog instanceof ReportQueryMeasRefDlg) {
                MeasureVO refMeasVO = ((ReportQueryMeasRefDlg) refDialog).getRefMeasVO();
                if (refMeasVO != null) {
                    ReportSelectFldVO repSelFldVO =
                        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);
                    //����ƥ����飺ָ�����������Ƿ��ڲ�ѯ�����ֶ�����ƥ��
                    int nMeasType = refMeasVO.getType();
                    bValidMapped = (repSelFldVO.getSelectFldVO().getColtype() == null)?false:
                        ReportQueryUtil.isMappedDataType(
                            repSelFldVO.getSelectFldVO().getColtype().intValue(),
                            nMeasType);
                    
                    if (!bValidMapped) {
                    	
                        UfoPublic.sendWarningMessage(
                            StringResource.getStringResource("miufo1001432")+"!",this);
                        return;
                    }
                    //��ӳ����Ϣ���õ������ѯѡ���ֶ�
                    repSelFldVO.setName(refMeasVO.getName());
                    repSelFldVO.setMapPK(refMeasVO.getCode());
                    repSelFldVO.setMapType(FieldMap.FIELD_MAP_MEASURE);
                    repSelFldVO.setMapName(
                        FieldMapUtil.getMapName(FieldMap.FIELD_MAP_MEASURE, refMeasVO.getCode()));
                }
                //KeyVO refvo = ((KeywordRefDialog) refDialog).getRefVO();
                //if (refvo != null) {
                //m_data.setRefkey(currentRow, refvo);
                //}
            } else 
            	if (this.refDialog instanceof ReportQueryKeyWordRefDlg) {
                    //���û��ѡ��һ�н��б༭�����������
                    if (!doCheckRef(iSelIndex)) {
                        return;
                    }

                    ReportSelectFldVO repSelFldVO =
                        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);
                    ReportQueryRefKeyWordVO refKeyVO =
                        ((ReportQueryKeyWordRefDlg) refDialog).getRefKeyVO();
                    if (refKeyVO != null) {
                        //��ӳ����Ϣ���õ������ѯѡ���ֶ�
                        repSelFldVO.setName(refKeyVO.getName());
                        repSelFldVO.setMapPK(refKeyVO.getKeywordPK());
                        repSelFldVO.setMapType(FieldMap.FIELD_MAP_KEYWORD);
                        repSelFldVO.setMapName(
                            FieldMapUtil.getMapName(FieldMap.FIELD_MAP_KEYWORD, refKeyVO.getKeywordPK()));
                    }
                }
            m_oHaveSelMoldel.fireTableDataChanged(); //ˢ����ʾ
            haveSelectTable.getSelectionModel().setSelectionInterval(iSelIndex, iSelIndex);
        }
        //setStateMessage("", false);
    } catch (Exception ce) {
        UfoPublic.sendWarningMessage(ce.getMessage(),this);
    }

}
/**
ע����ս���������������ֵ��������������簴ť
Class refDialog �ǲ��ս�����
subComp �ǲ��ս����ϼ�������ֵ���������
*/
public void setRefDialogAndRefOper(
    java.awt.Component refDlg,
    java.awt.Component comp) {
//    if (this.refDialog == null && this.refcomp == null) {
        this.refDialog = refDlg;
        this.refcomp = comp;
//    }

}
/**
 * ˵����
 * ���ߣ�������
 * �������ڣ�(2003-4-9 10:52:40)
 * ������ @param��<|>
 * ����ֵ��@return��
 *
 * @param newReportQueryVO ReportQueryVO
 */
public void setReportQueryVO(ReportQueryVO newReportQueryVO) {
    if (newReportQueryVO == null) {
        return;
    }
    m_ReportQueryVO = newReportQueryVO;

    //���ģ����������������
    if (m_ReportQueryVO.getQuerydef() != null) {

        //�õ���ѡ�ı����ѯ�ֶΣ������õ���ģ����
        ReportSelectFldVO[] selectFldVOs = m_ReportQueryVO.getSelectFlds();
        int iLen = selectFldVOs != null ? selectFldVOs.length : 0;
        Hashtable hashHaveSel = new Hashtable();
        Vector vecHaveSelFld = new Vector();
        for (int i = 0; i < iLen; i++) {
            hashHaveSel.put(selectFldVOs[i].getSelectFldVO().getFldname(), selectFldVOs[i]);
            vecHaveSelFld.add(selectFldVOs[i]);
        }
        m_oHaveSelMoldel.resetModel(vecHaveSelFld);

        //������ѡ�ı����ѯ�ֶΣ��õ���ѡ�Ĳ�ѯ�����ֶ��б�
        //�����õ���ģ����
        SelectFldVO[] waitFldVOs = m_ReportQueryVO.getQuerydef().getSelectFlds();
        iLen = waitFldVOs != null ? waitFldVOs.length : 0;
        Vector vecWaitForFld = new Vector();
        for (int i = 0; i < iLen; i++) {
            if (!hashHaveSel.containsKey(waitFldVOs[i].getFldname())) {
                vecWaitForFld.add(waitFldVOs[i]);
            }
        }
        m_oWaitForSelMoldel.resetModel(vecWaitForFld);
    }
}
/**
 * ����ֵ��CellEditor
 * �������ڣ�(2001-7-17 13:18:15)
 * @param iRow int
 * @param iCol int
 */
private void setValueEditor(int iRow, int iCol) {
	TableColumn tc = getHaveSelectTable().getColumnModel().getColumn(iCol);

	if (iCol == 2) {
		//��������༭���Ķ���
		tc.setCellEditor(m_refEditor);
	}
}
public boolean isValidMapped() {
	return bValidMapped;
}
public void setValidMapped(boolean validMapped) {
	bValidMapped = validMapped;
}
}


