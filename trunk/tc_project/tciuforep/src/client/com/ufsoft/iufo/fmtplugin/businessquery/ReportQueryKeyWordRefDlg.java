package com.ufsoft.iufo.fmtplugin.businessquery;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.fmtplugin.measure.DialogRefListener;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.resource.ResConst;
/** 
@update 2003-11-20 16:11 liulp
  ��ӿɶ�β��յĹ���
@end
@update 2003-10-31 09:58 liulp
 ���Ӱ�������
@end
* �����ѯӳ��ؼ��ֵĲ��նԻ���
* 
* �������ڣ�(2003-9-15 15:16:13)
* @author������Ƽ
*/
public class ReportQueryKeyWordRefDlg
    extends com.ufsoft.report.dialog.UfoDialog
    implements java.awt.event.ActionListener {
	private static final long serialVersionUID = 6928915497279992508L;
	private javax.swing.JButton ivjJBtnCancel = null;
    private javax.swing.JButton ivjJBtnOK = null;
    private javax.swing.JPanel ivjJPanel1 = null;
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private JTable keyListTable = null;
    /**
     * ��������йؼ����б�
     */
    ValueObject[] m_okeyVOs = null;
    /**
     * �����ڵ����йؼ��ֵı�ģ��
     */
    private ReportQueryKeyWordRefTM m_oRefKeyListMoldel = null;
    /**
    * ѡ�еĲ��չؼ���
    */
    private ReportQueryRefKeyWordVO m_returnVO = null;
    /**
     * ��ʼ��ϱ�ʶ
     */
    public boolean isInitOver = false;
	private DialogRefListener _dialogRefListener;
/**
 * ReportQueryKeyWordRefDlg ������ע�⡣
 */
public ReportQueryKeyWordRefDlg() {
	super();
	initialize();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-15 15:20:26)
 * @param parent java.awt.Container
 * @param keyVOs nc.vo.pub.ValueObject[]
 */
public ReportQueryKeyWordRefDlg(Dialog parent,
    DialogRefListener dialogRefListener,
    nc.vo.pub.ValueObject[] keyVOs) {
    super(parent);
    _dialogRefListener = dialogRefListener;
    this.m_okeyVOs = keyVOs;

    initialize();
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent event) {
    if (event.getSource() == ivjJBtnOK) {
        int row = keyListTable.getSelectedRow();
        if (row < 0)
            row = 0;
        ReportQueryRefKeyWordVO selectVO =
            (ReportQueryRefKeyWordVO) m_oRefKeyListMoldel.getVO(row);
        //�ѱ�������ѯӳ��Ĳ����ٱ�����ѯӳ��
        if (selectVO.getMapStatus() == ReportQueryRefKeyWordVO.MAP_STATUS_OTHER) {
            return;
        }else
        	selectVO.setMapStatus(ReportQueryRefKeyWordVO.MAP_STATUS_SELF);

        //���÷��ؽ��
        m_returnVO = selectVO;

        setResult(ID_OK);
        _dialogRefListener.onRef(event);

    } else
        if (event.getSource() == ivjJBtnCancel) {
            setResult(ID_CANCEL);
            _dialogRefListener.beforeDialogClosed(this);
            this.close();
        }
}
/**
 * ���Ӱ�����
 * 
 * �������ڣ�(2003-10-31 09:56:54)
 * �����ߣ�����Ƽ
 */
private void addHelp() {
    javax.help.HelpBroker hb = ResConst.getHelpBroker();
    if (hb == null)
        return;
    hb.enableHelpKey(getContentPane(), "TM_Data_Query_Add", null);

}
/**
 * ���� JBtnCancel ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnCancel() {
    if (ivjJBtnCancel == null) {
        try {
            ivjJBtnCancel = new nc.ui.pub.beans.UIButton();
            ivjJBtnCancel.setName("JBtnCancel");
            ivjJBtnCancel.setText("Cancel");
            ivjJBtnCancel.setBounds(267, 231, 75, 22);
            // user code begin {1}
            String strCancel = StringResource.getStringResource(StringResource.CANCEL);
            ivjJBtnCancel.setText(strCancel);
            ivjJBtnCancel.addActionListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBtnCancel;
}
/**
 * ���� JBtnOK ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnOK() {
    if (ivjJBtnOK == null) {
        try {
            ivjJBtnOK = new nc.ui.pub.beans.UIButton();
            ivjJBtnOK.setName("JBtnOK");
            ivjJBtnOK.setText("Ref");
            ivjJBtnOK.setBounds(159, 231, 75, 22);
            // user code begin {1}
            String strRef = StringResource.getStringResource("miufopublic283");  //"����"
            ivjJBtnOK.setText(strRef);
			
//            if (this.getParent() instanceof DialogRefListener) {
                //ע����ս���������������ֵ���������:��ť"�� ��"
            _dialogRefListener.setRefDialogAndRefOper(this, ivjJBtnOK);
                if (_dialogRefListener.getRefOper() != this.ivjJBtnOK) {
                    ivjJBtnOK.setEnabled(false);
                }
//            }	
            ivjJBtnOK.addActionListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBtnOK;
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
            ivjJPanel1.setBounds(2, 2, 355, 209);
            // user code begin {1}
            JScrollPane ps = new UIScrollPane(getKeyListTable());
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
 * ��ñ����ʽģ��������йؼ����б������̶�����ؼ��ֺ����ж�̬����ؼ���(���˵�"�к�")����
 *
 * �������ڣ�(2003-9-15 15:38:22)
 * @author������Ƽ
 * @return javax.swing.JTable
 */
private JTable getKeyListTable() {
    if (keyListTable == null) {
        keyListTable = new nc.ui.pub.beans.UITable();
        keyListTable.setAutoCreateColumnsFromModel(false);
        initKeyListTable();
        int nColCount = ReportQueryKeyWordRefTM.COUNT_OF_COLUMN;
        for (int k = 0; k < nColCount; k++) {
            TableCellRenderer renderer;
            DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
            renderer = textRenderer;
            TableColumn column = new TableColumn(k, 80, renderer, null);
            keyListTable.addColumn(column);

        }
 //       keyListTable.getTableHeader().setFont(new Font("dialog", 0, 12));
 //       keyListTable.setFont(new Font("dialog", 0, 12));
    }
    return keyListTable;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-15 16:11:56)
 * @author������Ƽ
 * @return nc.vo.iufo.keydef.KeyVO
 */
public ReportQueryRefKeyWordVO getRefKeyVO() {
    return m_returnVO;
}
/**
 * ���� UfoDialogContentPane ����ֵ��
 * @return javax.swing.JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (ivjUfoDialogContentPane == null) {
		try {
			ivjUfoDialogContentPane = new UIPanel();
			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
			ivjUfoDialogContentPane.setLayout(null);
			getUfoDialogContentPane().add(getJPanel1(), getJPanel1().getName());
			getUfoDialogContentPane().add(getJBtnOK(), getJBtnOK().getName());
			getUfoDialogContentPane().add(getJBtnCancel(), getJBtnCancel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjUfoDialogContentPane;
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
 * ��ʼ���ࡣ
 */
/* ���棺�˷������������ɡ� */
private void initialize() {
    try {
        // user code begin {1}
        // user code end
        setName("ReportQueryKeyWordRefDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(359, 295);
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    setTitle(StringResource.getStringResource("miufo1001386"));  //"�����ѯ-ӳ��ؼ���"
    addHelp();
    isInitOver = true;
    // user code end
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-15 15:42:26)
 * @author������Ƽ
 */
private void initKeyListTable() {
    m_oRefKeyListMoldel = new ReportQueryKeyWordRefTM(m_okeyVOs);
    keyListTable.setModel(m_oRefKeyListMoldel);

    //���ñ�����
    keyListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    keyListTable.sizeColumnsToFit(-1);
    keyListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		ReportQueryKeyWordRefDlg aReportQueryKeyWordRefDlg;
		aReportQueryKeyWordRefDlg = new ReportQueryKeyWordRefDlg();
		aReportQueryKeyWordRefDlg.setModal(true);
		aReportQueryKeyWordRefDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aReportQueryKeyWordRefDlg.show();
		java.awt.Insets insets = aReportQueryKeyWordRefDlg.getInsets();
		aReportQueryKeyWordRefDlg.setSize(aReportQueryKeyWordRefDlg.getWidth() + insets.left + insets.right, aReportQueryKeyWordRefDlg.getHeight() + insets.top + insets.bottom);
		aReportQueryKeyWordRefDlg.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main() :Exception");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
}


