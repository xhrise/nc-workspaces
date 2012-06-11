package com.ufsoft.iufo.fmtplugin.businessquery;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;

/**
 @update
  2003-11-04 15:29 liulp
   ��whereCondVO���������͡��գ���˫���Ž�����ͳһ��У�鴦��
 @end
 @update 2003-10-31 09:58 liulp
  ���Ӱ�������
 @end
 @update 2003-10-23 13:21 liulp
  ����Ի���Ӧ����ģ̬��
 @end
 * �˴���������������
 * �������ڣ�(2003-9-17 11:07:12)
 * @author������Ƽ
 */
public class RightFldRefDlg
    extends com.ufsoft.report.dialog.UfoDialog
    implements java.awt.event.ActionListener, javax.swing.event.AncestorListener {
    private javax.swing.JButton ivjJBtnRef = null;
    private javax.swing.JComboBox ivjJComboBoxRefItemsList = null;
    private javax.swing.JLabel ivjJLabelRightFld = null;
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    private javax.swing.JTextArea ivjJTextAreaRightFld = null;
    private javax.swing.JScrollPane ivjJScrollPane1 = null;
    /**
     * ���ص��Ҳ�������ֵ
     */
    private Object m_oReturnRightFld = null;
    /**
    * ��Ӧ���������sql��������
    */
    private Integer m_nColType = null;
    /**
    * ��Ӧ���������sql��������
    */
    private String[] m_oRefItemsList =
        new String[] { 
    		StringResource.getStringResource("miufopublic269"),   //"��λ����"
    		StringResource.getStringResource("miufopublic101"),   //"��λ����"
			StringResource.getStringResource("miufopublic270"),  //"��λ���α���"
			StringResource.getStringResource("miufopublic315"),   //"����"
			StringResource.getStringResource("miufo1001012")   //"����"
			};

    private javax.swing.JButton ivjJBtnCancel = null;
    private javax.swing.JButton ivjJBtnOK = null;
    
    private Context m_ufoContext=null;
/**
 * RightFldRefDlg ������ע�⡣
 */
public RightFldRefDlg() {
	super();
	initialize();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-17 11:21:21)
 * @author������Ƽ
 * @param parent java.awt.Container
 * @param nColType java.lang.Integer
 * @param objRightFld java.lang.Object
 */
public RightFldRefDlg(
    java.awt.Container parent,
    Integer nColType,
    Object objRightFld) {
    super(parent);

    this.m_nColType = nColType;

    this.m_oReturnRightFld = objRightFld;

    initialize();
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent e) {
    if (e.getSource() == getJBtnRef()) {
        //��λ���롢��λ���ơ���λ���α���
        int iSelIndex = getJComboBoxRefItemsList().getSelectedIndex();
        String strReturnValue = null;
        if (iSelIndex == DataProcessUtil.ITEM_UNIT_LEVEL_CODE
            || iSelIndex == DataProcessUtil.ITEM_UNIT_CODE
            || iSelIndex == DataProcessUtil.ITEM_UNIT_NAME) {
            UnitTreeRefDlg dlg = new UnitTreeRefDlg(this);
            dlg.setUfoContext(getUfoContext());
            dlg.setModal(true);
            dlg.showModal();

            if (dlg.getResult() == UfoDialog.ID_OK) {
                strReturnValue = dlg.getReturnValue(iSelIndex);
            }
        }
        //���ڲ���
        else
            if (iSelIndex == DataProcessUtil.ITEM_DATE) {
                //RightFldCalenderRefDlg
                nc.ui.pub.beans.calendar.UICalendar calendar =
                    new nc.ui.pub.beans.calendar.UICalendar(this, "1000-01-01", "3003-12-31");
                try {
                    nc.vo.pub.lang.UFDate date = new nc.vo.pub.lang.UFDate(new java.util.Date());
                    calendar.setNewdate(date);
                } catch (Exception ex) {
                }
                int iReturnButtonCode = calendar.showModal();
                //if (getReturnButtonCode() == calendar.ID_OK) {}
                calendar.destroy();
                strReturnValue = calendar.getCalendarString();

            }

        //�������
        else
            if (iSelIndex == DataProcessUtil.ITEM_CODE) {
                CodeListTreeRefDlg dlg = new CodeListTreeRefDlg(this);
                dlg.setModal(true);
                dlg.showModal();

                if (dlg.getResult() == UfoDialog.ID_OK) {
                    strReturnValue = dlg.getReturnValue();
                }
            }
        ivjJTextAreaRightFld.setText(strReturnValue);
    }
    //ȷ��
    else
        if (e.getSource() == getJBtnOK()) {
            //#У�顰�Ҳ��������༭���ֵ�Ƿ���Ч
            //��������ֻ�У���ֵ��Double���ַ���String
            StringBuffer sbError = new StringBuffer();
            Object returnRightFld =
                ReportQueryUtil.checkValidWhereCond(
                    m_nColType.intValue(),
                    "",
                    ivjJTextAreaRightFld.getText(),
                    sbError);
            if (sbError.length() > 0) {
                UfoPublic.sendWarningMessage(sbError.toString(),this);
                return;
            }

            m_oReturnRightFld = returnRightFld;

            setResult(UfoDialog.ID_OK);
            close();
        }
    //ȡ��
    else
        if (e.getSource() == getJBtnCancel()) {
            setResult(UfoDialog.ID_CANCEL);
            close();
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
	 * Called when the source or one of its ancestors is made visible
	 * either by setVisible(true) being called or by its being
	 * added to the component hierarchy.  The method is only called
	 * if the source has actually become visible.  For this to be true
	 * all its parents must be visible and it must be in a hierarchy
	 * rooted at a Window
	 */
public void ancestorAdded(javax.swing.event.AncestorEvent event) {}
	/**
	 * Called when either the source or one of its ancestors is moved.
	 */
public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
	/**
	 * Called when the source or one of its ancestors is made invisible
	 * either by setVisible(false) being called or by its being
	 * remove from the component hierarchy.  The method is only called
	 * if the source has actually become invisible.  For this to be true
	 * at least one of its parents must by invisible or it is not in
	 * a hierarchy rooted at a Window
	 */
public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
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
            ivjJBtnCancel.setBounds(192, 235, 75, 22);
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
			ivjJBtnOK.setText("OK");
			ivjJBtnOK.setBounds(78, 236, 75, 22);
			// user code begin {1}
			String strOK = StringResource.getStringResource(StringResource.OK);
			ivjJBtnOK.setText(strOK);
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
 * ���� JBtnRef ����ֵ��
 * @return javax.swing.JButton
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JButton getJBtnRef() {
    if (ivjJBtnRef == null) {
        try {
            ivjJBtnRef = new nc.ui.pub.beans.UIButton();
            ivjJBtnRef.setName("JBtnRef");
            ivjJBtnRef.setText("Ref");
            ivjJBtnRef.setBounds(435, 59, 75, 22);
            // user code begin {1}
            String strRef = StringResource.getStringResource("miufopublic283");  //"����"
            ivjJBtnRef.setText(strRef);
            ivjJBtnRef.addActionListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBtnRef;
}
/**
 * ���� JComboBoxRefItemsList ����ֵ��
 * @return javax.swing.JComboBox
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JComboBox getJComboBoxRefItemsList() {
    if (ivjJComboBoxRefItemsList == null) {
        try {
            ivjJComboBoxRefItemsList = new UIComboBox();
            ivjJComboBoxRefItemsList.setName("JComboBoxRefItemsList");
            ivjJComboBoxRefItemsList.setBounds(298, 60, 130, 23);
            // user code begin {1}
            //���ӿɲ��յ���Ŀ�б�ֵ
            int iLen = m_oRefItemsList.length;
            for (int i = 0; i < iLen; i++) {
                ivjJComboBoxRefItemsList.addItem(m_oRefItemsList[i]);
            }
            ivjJComboBoxRefItemsList.setSelectedIndex(0);

            //�����¼�������
            ivjJComboBoxRefItemsList.addActionListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJComboBoxRefItemsList;
}
/**
 * ���� JLabelRightFld ����ֵ��
 * @return javax.swing.JLabel
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JLabel getJLabelRightFld() {
	if (ivjJLabelRightFld == null) {
		try {
			ivjJLabelRightFld = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelRightFld.setName("JLabelRightFld");
			ivjJLabelRightFld.setText("RightFld");
			ivjJLabelRightFld.setBounds(27, 32, 70, 14);
			// user code begin {1}
			String strRightFld = StringResource.getStringResource("miufo1001419");  //"�Ҳ�����:"
			ivjJLabelRightFld.setText(strRightFld);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabelRightFld;
}
/**
 * ���� JScrollPane1 ����ֵ��
 * @return javax.swing.JScrollPane
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JScrollPane getJScrollPane1() {
	if (ivjJScrollPane1 == null) {
		try {
			ivjJScrollPane1 = new UIScrollPane();
			ivjJScrollPane1.setName("JScrollPane1");
			ivjJScrollPane1.setBounds(25, 50, 270, 177);
			getJScrollPane1().setViewportView(getJTextAreaRightFld());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJScrollPane1;
}
/**
 * ���� JTextAreaRightFld ����ֵ��
 * @return javax.swing.JTextArea
 */
/* ���棺�˷������������ɡ� */
private javax.swing.JTextArea getJTextAreaRightFld() {
    if (ivjJTextAreaRightFld == null) {
        try {
            ivjJTextAreaRightFld = new UITextArea();
            ivjJTextAreaRightFld.setName("JTextAreaRightFld");
            ivjJTextAreaRightFld.setBounds(0, 0, 268, 173);
            // user code begin {1}
            String strRightFld = "";
            if (m_oReturnRightFld instanceof Double) {
                strRightFld = m_oReturnRightFld.toString();
            } else
                if (m_oReturnRightFld instanceof String) {
                    strRightFld = (String) m_oReturnRightFld;
                }
            ivjJTextAreaRightFld.setText(strRightFld);
            ivjJTextAreaRightFld.addAncestorListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJTextAreaRightFld;
}
/**
 * �˴����뷽��������
 * �������ڣ�(2003-9-17 11:56:40)
 * @author������Ƽ
 * @return java.lang.Object
 */
public java.lang.Object getReturnRightFld() {
	return m_oReturnRightFld;
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
			getUfoDialogContentPane().add(getJLabelRightFld(), getJLabelRightFld().getName());
			getUfoDialogContentPane().add(getJComboBoxRefItemsList(), getJComboBoxRefItemsList().getName());
			getUfoDialogContentPane().add(getJBtnRef(), getJBtnRef().getName());
			getUfoDialogContentPane().add(getJScrollPane1(), getJScrollPane1().getName());
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
        setName("RightFldRefDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(523, 304);
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    setTitle(StringResource.getStringResource("miufo1001420"));  //"�Ҳ������༭�Ĳ���"
    addHelp();
    // user code end
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		RightFldRefDlg aRightFldRefDlg;
		aRightFldRefDlg = new RightFldRefDlg();
		aRightFldRefDlg.setModal(true);
		aRightFldRefDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aRightFldRefDlg.show();
		java.awt.Insets insets = aRightFldRefDlg.getInsets();
		aRightFldRefDlg.setSize(aRightFldRefDlg.getWidth() + insets.left + insets.right, aRightFldRefDlg.getHeight() + insets.top + insets.bottom);
		aRightFldRefDlg.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog's  main():Exception");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}

public Context getUfoContext() {
	return m_ufoContext;
}
public void setUfoContext(Context context) {
	m_ufoContext = context;
}

}


