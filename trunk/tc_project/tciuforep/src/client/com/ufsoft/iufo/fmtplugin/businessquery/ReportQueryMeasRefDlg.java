package com.ufsoft.iufo.fmtplugin.businessquery;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.fmtplugin.measure.DialogRefListener;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.resource.ResConst;

/**
 @update 2004-03-18 17:36
   �޸Ķ�β���ʱ��������ƥ�������ʾ��bug�����ͼ���Ӧ���ڸ����������
 @end
 @update
	���ָ�����ʱ��˸�����⣺
	����ԭ�������ڸ����ڵ���show()ʱִ�е�����һ���߳�ȥ��ʾ������ִ��show()ʱ������Ŀ¼����û�д������
		�������������˸��
	����������ڱ�����������һ��isInitOver��ʶ���ڸ����ڵ���show()֮ǰ��ѭ���ж�isInitOver�Ƿ�Ϊtrue�������false,������500ms,
		Ŀ���ǵȴ�����ĳ�ʼ����ȫ����֮���ٽ���show()����
 @end
 @update 2003-11-20 16:11 liulp
   ��ӿɶ�β��յĹ���
 @end
 @update 2003-10-31 09:58 liulp
  ���Ӱ�������
 @end
 @update 2003-10-23 13:21 liulp
  ����Ի���Ӧ����ģ̬��
 @end
 * �����ѯӳ��ָ���ָ����նԻ���
 *
 * �������ڣ�(2003-9-13 9:44:39)
 * @author������Ƽ
 */
public class ReportQueryMeasRefDlg
    extends com.ufsoft.report.dialog.UfoDialog
    implements ActionListener {
	private static final long serialVersionUID = 5373939648208544240L;
	private JButton ivjJBtnCancel = null;
    private JButton ivjJBtnOK = null;
    private JPanel ivjUfoDialogContentPane = null;
    private JPanel ivjJPanelMeasList = null;
    private JTable measListTable = null;
    /**
     * �����ڵ�����ָ��ı�ģ��
     */
    private ReportQueryMeasRefTM m_oRefMeasListMoldel = null;
    /**
    * �����ڵ�����ָ���б�����
    */
    private ValueObject[] m_oRqRefMeasVOs = null;
    /**
    * ѡ�еĲ���ָ��
    */
    private ReportQueryRefMeasVO m_returnVO = null;
    /**
    * Ҫ��ӳ��ı����ѯ�ֶ�
    */
    private ReportSelectFldVO m_oRepSelFldVO = null;
    /**
    * Ҫ��ӳ��ı����ѯ�ֶ�
    */
    private ReportBusinessQuery m_oRepBusinessQuery = null;
    /**
     * ��ʼ��ϱ�ʶ
     */
    public boolean isInitOver = false;
	private DialogRefListener _dialogRefListener;
///**
// * ������
// */
///* ���棺�˷������������ɡ� */
//public ReportQueryMeasRefDlg() {
//	super();
//	initialize();
//}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-15 20:12:22)
 * @author������Ƽ
 * @param parent java.awt.Container
 * @param repSelFldVO com.ufsoft.iuforeport.reporttool.query.ReportSelectFldVO
 * @param repBusinessQuery com.ufsoft.iuforeport.reporttool.query.ReportBusinessQuery
 * @param rqRefMeasVOs nc.vo.pub.ValueObject[] - ����ΪReportQueryRefMeasVO
 */
public ReportQueryMeasRefDlg(
    Dialog parent,
    DialogRefListener dialogRefListener,
    ReportSelectFldVO repSelFldVO,
    ReportBusinessQuery repBusinessQuery,
    ValueObject[] rqRefMeasVOs) {
    super(parent);
    this._dialogRefListener = dialogRefListener;
    this.m_oRepBusinessQuery = repBusinessQuery;

    this.m_oRqRefMeasVOs = rqRefMeasVOs;

    this.m_oRepSelFldVO = repSelFldVO;

    initialize();
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent event) {
	if(getMeasListTable().getCellEditor()!=null)
		getMeasListTable().getCellEditor().stopCellEditing();
	//����
    if (event.getSource() == ivjJBtnOK) {
        ReportQueryRefMeasVO selectVO = getSelectRefMeasVO();
        if (selectVO != null) {
            //������ѱ�ӳ��ģ�����Ӧ
            if (selectVO.getMapStatus() == ReportQueryRefKeyWordVO.MAP_STATUS_OTHER) {
                return;
            }
            	
            //����Ϊ���ؽ��,����ƥ������ڸ����������
            
            SelectFieldsPage selectPage = (SelectFieldsPage)_dialogRefListener;
            boolean bValidMap = selectPage.isValidMapped();
            if(bValidMap){
            	selectVO.setMapStatus(ReportQueryRefKeyWordVO.MAP_STATUS_SELF);
            }
            m_returnVO = selectVO;
            _dialogRefListener.onRef(event);
        }

        //setResult(ID_OK);
        //this.close();
    }
    //�ر�
    else
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
public void dispose() {
    setResult(ID_CANCEL);
    _dialogRefListener.beforeDialogClosed(this);
    super.dispose();
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
			ivjJBtnCancel.setBounds(274, 284, 75, 22);
			// user code begin {1}
			String strClose = StringResource.getStringResource("miufopublic253");  //"�ر�"
			ivjJBtnCancel.setText(strClose);			
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
            ivjJBtnOK.setBounds(154, 285, 75, 22);
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
private javax.swing.JPanel getJPanelMeasList() {
    if (ivjJPanelMeasList == null) {
        try {
            ivjJPanelMeasList = new UIPanel();
            ivjJPanelMeasList.setName("JPanelMeasList");
            ivjJPanelMeasList.setLayout(null);
            ivjJPanelMeasList.setBounds(7, 6, 366, 268);
            // user code begin {1}
            JScrollPane ps = new UIScrollPane(getMeasListTable());
            ivjJPanelMeasList.setLayout(new BorderLayout());
            ivjJPanelMeasList.add(ps, BorderLayout.CENTER);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJPanelMeasList;
}
/**
 * ��ñ����ʽģ���������ָ���б������̶�����ָ������ж�̬����ָ�꣩��
 * 
 * �������ڣ�(2003-9-13 11:15:50)
 * @return javax.swing.JTable
 */
private JTable getMeasListTable() {
    if (measListTable == null) {
        measListTable = new nc.ui.pub.beans.UITable();
        measListTable.setAutoCreateColumnsFromModel(false);
        initMeasListTable();
        int nColCount = ReportQueryMeasRefTM.COUNT_OF_COLUMN;
        for (int k = 0; k < nColCount; k++) {
            TableCellRenderer renderer;
            DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
            renderer = textRenderer;

            //TableCellEditor editor = null;

            //if (k == FieldTableModel.COLUMN_SORT) {
            //JComboBox c0 = new UIComboBox(FieldTableModel.m_oSortType);
            //editor = new DefaultCellEditor(c0);
            //} else {
            //editor = new DefaultCellEditor(new UITextField());
            //}
            TableColumn column = new TableColumn(k, 80, renderer, null);
            measListTable.addColumn(column);

        }
 //       measListTable.getTableHeader().setFont(new Font("dialog", 0, 12));
 //       measListTable.setFont(new Font("dialog", 0, 12));
    }
    return measListTable;
}
/**
 * �õ�ѡ��Ĳ���ָ�ꡣ
 * �������ڣ�(2003-9-13 15:24:31)
 * @return nc.vo.iufo.measure.MeasureVO
 */
public MeasureVO getRefMeasVO() {
    return m_returnVO;
}
/**
 * �õ���ǰѡ�еĲ��ն���
 * 
 * �������ڣ�(2003-11-20 13:50:09)
 * @author������Ƽ
 * @return com.ufsoft.iuforeport.reporttool.query.ReportQueryRefMeasVO
 */
private ReportQueryRefMeasVO getSelectRefMeasVO() {
    int row = measListTable.getSelectedRow();
    if (row < 0)
        row = 0;
    return (ReportQueryRefMeasVO) m_oRefMeasListMoldel.getVO(row);
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
			getUfoDialogContentPane().add(getJPanelMeasList(), getJPanelMeasList().getName());
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
        setName("ReportQueryMeasRefDlg");
//        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(382, 347);
        setContentPane(getUfoDialogContentPane());
        setModal(false);
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    setTitle(StringResource.getStringResource("miufo1001389"));  //"�����ѯ-ӳ��ָ��"
    addHelp();
    isInitOver = true;
    // user code end
}
/**
 * ��ʼ����������ָ��ı�ģ�͡�
 * 
 * �������ڣ�(2003-9-13 11:42:17)
 */
private void initMeasListTable() {
    m_oRefMeasListMoldel = new ReportQueryMeasRefTM(m_oRqRefMeasVOs);
    measListTable.setModel(m_oRefMeasListMoldel);

    //���ñ�����
    measListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    measListTable.sizeColumnsToFit(-1);
    measListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	//try {
		//ReportQueryMeasRefDlg aReportQueryMeasRefDlg;
		//aReportQueryMeasRefDlg = new ReportQueryMeasRefDlg(this);
		//aReportQueryMeasRefDlg.setModal(true);
		//aReportQueryMeasRefDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			//public void windowClosing(java.awt.event.WindowEvent e) {
				//System.exit(0);
			//};
		//});
		//aReportQueryMeasRefDlg.show();
		//java.awt.Insets insets = aReportQueryMeasRefDlg.getInsets();
		//aReportQueryMeasRefDlg.setSize(aReportQueryMeasRefDlg.getWidth() + insets.left + insets.right, aReportQueryMeasRefDlg.getHeight() + insets.top + insets.bottom);
		//aReportQueryMeasRefDlg.setVisible(true);
	//} catch (Throwable exception) {
		//System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog �� main() �з����쳣");
		//exception.printStackTrace(System.out);
	//}
}
}


