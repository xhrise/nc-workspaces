package com.ufsoft.iufo.fmtplugin.businessquery;

/**
 @update 2003-11-20 16:13 liulp
  �����б����ѡ��һ��
 @end
 @update 2003-11-05 09:38 liulp
  ���ӱ����ѯɾ����ȷ����ʾ��
 @end
 @update 2003-10-31 09:58 liulp
  ���Ӱ�������
 @end
 @update
 2002-10-14 17:31
   ��ɱ����ѯ�޸ġ�ɾ��������
 @end
 * �����ѯ�޸ġ�ɾ��������档
 * �������ڣ�(2003-10-8 15:26:21)
 * @author��������
 */
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JList;

import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iuforeport.businessquery.QuerySimpleDef;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;

public class ReportQueryMngDlg
    extends com.ufsoft.report.dialog.UfoDialog
    implements java.awt.event.ActionListener {
    private javax.swing.JButton ivjJButtonCancel = null;
    private javax.swing.JButton ivjJButtonDelete = null;
    private javax.swing.JButton ivjJButtonUpdate = null;
    private javax.swing.JLabel ivjJLabelList = null;
    private javax.swing.JScrollPane ivjJSPaneList = null;
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    //JList������
    private Vector m_oQueryVec = null;
    //JList�ؼ�
    private JList queryList = null;
    /**
     * ѡ���GUID����
     */
    private String[] m_strSelectGUIDs = null;
    public static int ID_UPDATE = 10;
    public static int ID_DELETE = 11;
/**
 * ������
 */
/* ���棺�˷������������ɡ� */
public ReportQueryMngDlg() {
	super();
	initialize();
}
/**
 * ReportQueryMngDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public ReportQueryMngDlg(java.awt.Container parent, Vector queryVec) {
    super(parent);
    if (queryVec == null)
        this.m_oQueryVec = new Vector();
    else
        this.m_oQueryVec = queryVec;

    initialize();
}
/**
 * ReportQueryMngDlg ������ע�⡣
 * @param parent java.awt.Container
 */
public ReportQueryMngDlg(java.awt.Dialog parent, Vector queryVec) {
    super(parent);
    if (queryVec == null)
        this.m_oQueryVec = new Vector();
    else
        this.m_oQueryVec = queryVec;

    initialize();
}
public void actionPerformed(ActionEvent e) {
    //ȡ��
    if (e.getSource() == getJButtonCancel()) {
        setResult(ID_CANCEL);
        close();
    }
    //�޸�
    else
        if (e.getSource() == ivjJButtonUpdate) {
            ReportQueryMngData data = (ReportQueryMngData) getJList().getSelectedValue();
            if (data != null) {
                m_strSelectGUIDs = new String[] { data.getGUID()};
            }
            if(m_strSelectGUIDs == null || m_strSelectGUIDs.length <= 0 )
            {
	            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001394"),this);  //"��ѡ��һ����ѯ!"
	            return;
            }
            setResult(ID_UPDATE);
            close();
        }
    //ɾ��
    else
        if (e.getSource() == ivjJButtonDelete) {
            Object[] datas = getJList().getSelectedValues();
            if (datas != null && datas.length > 0) {
                int iLen = datas.length;
                if (iLen > 0) {
                    //�û�ȷ����ʾ
                    String strMessage = StringResource.getStringResource("miufo1001395");  //"ȷ��ɾ����ѡ�еı����ѯ��"
                    int nRs =
                        javax.swing.JOptionPane.showConfirmDialog(
                            this,
                            strMessage,
                            StringResource.getStringResource("miufopublic384"),  //"��ʾ��Ϣ"
                            javax.swing.JOptionPane.OK_CANCEL_OPTION);
                    if (nRs == javax.swing.JOptionPane.OK_OPTION) {
                        m_strSelectGUIDs = new String[iLen];
                        for (int i = 0; i < iLen; i++) {
                            m_strSelectGUIDs[i] = ((ReportQueryMngData) datas[i]).getGUID();
                        }
                        setResult(ID_DELETE);
                        close();
                    }
                }

            }else
            {
	           UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001394"),this);  //"��ѡ��һ����ѯ!"
            }
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
    hb.enableHelpKey(getContentPane(), "TM_Data_Query_Update", null);

}
/**
 * ���� JButtonCancel ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJButtonCancel() {
	if (ivjJButtonCancel == null) {
		try {
			ivjJButtonCancel = new nc.ui.pub.beans.UIButton();
			ivjJButtonCancel.setName("JButtonCancel");
			ivjJButtonCancel.setText("Cancel");
			ivjJButtonCancel.setBounds(242, 194, 75, 22);
			// user code begin {1}
			String strCancel = StringResource.getStringResource(StringResource.CANCEL);
			ivjJButtonCancel.setText(strCancel);
            ivjJButtonCancel.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJButtonCancel;
}
/**
 * ���� JButtonDelete ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJButtonDelete() {
	if (ivjJButtonDelete == null) {
		try {
			ivjJButtonDelete = new nc.ui.pub.beans.UIButton();
			ivjJButtonDelete.setName("JButtonDelete");
			ivjJButtonDelete.setText("Delete");
			ivjJButtonDelete.setBounds(243, 137, 75, 22);
			// user code begin {1}
			String strDelete = StringResource.getStringResource(StringResource.DELETE);
			ivjJButtonDelete.setText(strDelete);
            ivjJButtonDelete.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJButtonDelete;
}
/**
 * ���� JButtonUpdate ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJButtonUpdate() {
	if (ivjJButtonUpdate == null) {
		try {
			ivjJButtonUpdate = new nc.ui.pub.beans.UIButton();
			ivjJButtonUpdate.setName("JButtonUpdate");
			ivjJButtonUpdate.setText(StringResource.getStringResource("miufo1001396"));  //"��  ��"
			ivjJButtonUpdate.setBounds(241, 74, 75, 22);
			// user code begin {1}
			String strUpdate = StringResource.getStringResource(StringResource.MODIFY);
			ivjJButtonUpdate.setText(strUpdate);
            ivjJButtonUpdate.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJButtonUpdate;
}
/**
 * ���� JLabelList ����ֵ��
 * @return JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabelList() {
	if (ivjJLabelList == null) {
		try {
			ivjJLabelList = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelList.setName("JLabelList");
			ivjJLabelList.setText("QueryList");
			ivjJLabelList.setBounds(26, 19, 73, 14);
			// user code begin {1}
			String strQueryList = StringResource.getStringResource("miufo1001397");  //"��ѯ�б�:"
			ivjJLabelList.setText(strQueryList);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelList;
}
/**
 * �õ��Զ�����б�ؼ���
 * �޸����ڣ�(2003-10-13 20:23:50)
 * @return java.lang.String
 */
private JList getJList() {
    if (queryList == null) {
        try {
            Vector nameVec = new Vector();
            for (int i = 0; i < m_oQueryVec.size(); i++) {
                ReportQueryVO queryVO = (ReportQueryVO) m_oQueryVec.get(i);
                QuerySimpleDef def = queryVO.getQuerydef();
                ReportQueryMngData data = new ReportQueryMngData();
                data.setDisplayName(def.getDisplayName());
                data.setGUID(def.getGUID());
                nameVec.addElement(data);
            }
            queryList = new UIList(nameVec);
            queryList.setName("JList");
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }
    return queryList;
}
/**
 * ���� JScrollPane1 ����ֵ��
 * @return JScrollPane
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JScrollPane getJSPaneList() {
    if (ivjJSPaneList == null) {
        try {
            ivjJSPaneList = new UIScrollPane(getJList());
            ivjJSPaneList.setName("JSPaneList");
            ivjJSPaneList.setBounds(26, 38, 174, 228);
            // user code begin {1}
            //JList nameList = getJList();
            //ivjJSPaneList.set(nameList);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJSPaneList;
}
/**
 * �õ�ѡ��ı����ѯ��
 * 
 * �������ڣ�(2003-10-13 20:31:48)
 * @return java.lang.String[]
 */
public java.lang.String[] getSelectGUIDs() {
    return m_strSelectGUIDs;
}
/**
 * ���� UfoDialogContentPane ����ֵ��
 * @return JPanel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JPanel getUfoDialogContentPane() {
	if (ivjUfoDialogContentPane == null) {
		try {
			ivjUfoDialogContentPane = new UIPanel();
			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
			ivjUfoDialogContentPane.setLayout(null);
			getUfoDialogContentPane().add(getJSPaneList(), getJSPaneList().getName());
			getUfoDialogContentPane().add(getJLabelList(), getJLabelList().getName());
			getUfoDialogContentPane().add(getJButtonUpdate(), getJButtonUpdate().getName());
			getUfoDialogContentPane().add(getJButtonDelete(), getJButtonDelete().getName());
			getUfoDialogContentPane().add(getJButtonCancel(), getJButtonCancel().getName());
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
		setName("ReportQueryMngDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(363, 314);
		setTitle(StringResource.getStringResource("miufo1001398"));  //"�����ѯ����"
		setContentPane(getUfoDialogContentPane());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	addHelp();
	// user code end
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	//try {
		//ReportQueryMngDlg aReportQueryMngDlg;
		//aReportQueryMngDlg = new ReportQueryMngDlg();
		//aReportQueryMngDlg.setModal(true);
		//aReportQueryMngDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			//public void windowClosing(java.awt.event.WindowEvent e) {
				//System.exit(0);
			//};
		//});
		//aReportQueryMngDlg.show();
		//java.awt.Insets insets = aReportQueryMngDlg.getInsets();
		//aReportQueryMngDlg.setSize(aReportQueryMngDlg.getWidth() + insets.left + insets.right, aReportQueryMngDlg.getHeight() + insets.top + insets.bottom);
		//aReportQueryMngDlg.setVisible(true);
	//} catch (Throwable exception) {
		//System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main():Exception");
		//exception.printStackTrace(System.out);
	//}
}
}


