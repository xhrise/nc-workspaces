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
   对whereCondVO的数据类型、空，单双引号进行了统一的校验处理
 @end
 @update 2003-10-31 09:58 liulp
  增加帮助链接
 @end
 @update 2003-10-23 13:21 liulp
  错误对话框应该是模态的
 @end
 * 此处插入类型描述。
 * 创建日期：(2003-9-17 11:07:12)
 * @author：刘良萍
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
     * 返回的右操作数的值
     */
    private Object m_oReturnRightFld = null;
    /**
    * 对应左操作数的sql数据类型
    */
    private Integer m_nColType = null;
    /**
    * 对应左操作数的sql数据类型
    */
    private String[] m_oRefItemsList =
        new String[] { 
    		StringResource.getStringResource("miufopublic269"),   //"单位编码"
    		StringResource.getStringResource("miufopublic101"),   //"单位名称"
			StringResource.getStringResource("miufopublic270"),  //"单位级次编码"
			StringResource.getStringResource("miufopublic315"),   //"日期"
			StringResource.getStringResource("miufo1001012")   //"编码"
			};

    private javax.swing.JButton ivjJBtnCancel = null;
    private javax.swing.JButton ivjJBtnOK = null;
    
    private Context m_ufoContext=null;
/**
 * RightFldRefDlg 构造子注解。
 */
public RightFldRefDlg() {
	super();
	initialize();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-17 11:21:21)
 * @author：刘良萍
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
        //单位编码、单位名称、单位级次编码
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
        //日期参照
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

        //编码参照
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
    //确定
    else
        if (e.getSource() == getJBtnOK()) {
            //#校验“右操作数”编辑框的值是否有效
            //数据类型只有：数值的Double和字符的String
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
    //取消
    else
        if (e.getSource() == getJBtnCancel()) {
            setResult(UfoDialog.ID_CANCEL);
            close();
        }
}
/**
 * 增加帮助。
 * 
 * 创建日期：(2003-10-31 09:56:54)
 * 创建者：刘良萍
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
 * 返回 JBtnCancel 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JBtnOK 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JBtnRef 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnRef() {
    if (ivjJBtnRef == null) {
        try {
            ivjJBtnRef = new nc.ui.pub.beans.UIButton();
            ivjJBtnRef.setName("JBtnRef");
            ivjJBtnRef.setText("Ref");
            ivjJBtnRef.setBounds(435, 59, 75, 22);
            // user code begin {1}
            String strRef = StringResource.getStringResource("miufopublic283");  //"参照"
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
 * 返回 JComboBoxRefItemsList 特性值。
 * @return javax.swing.JComboBox
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JComboBox getJComboBoxRefItemsList() {
    if (ivjJComboBoxRefItemsList == null) {
        try {
            ivjJComboBoxRefItemsList = new UIComboBox();
            ivjJComboBoxRefItemsList.setName("JComboBoxRefItemsList");
            ivjJComboBoxRefItemsList.setBounds(298, 60, 130, 23);
            // user code begin {1}
            //增加可参照的项目列表值
            int iLen = m_oRefItemsList.length;
            for (int i = 0; i < iLen; i++) {
                ivjJComboBoxRefItemsList.addItem(m_oRefItemsList[i]);
            }
            ivjJComboBoxRefItemsList.setSelectedIndex(0);

            //增加事件侦听器
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
 * 返回 JLabelRightFld 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelRightFld() {
	if (ivjJLabelRightFld == null) {
		try {
			ivjJLabelRightFld = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelRightFld.setName("JLabelRightFld");
			ivjJLabelRightFld.setText("RightFld");
			ivjJLabelRightFld.setBounds(27, 32, 70, 14);
			// user code begin {1}
			String strRightFld = StringResource.getStringResource("miufo1001419");  //"右操作数:"
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
 * 返回 JScrollPane1 特性值。
 * @return javax.swing.JScrollPane
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JTextAreaRightFld 特性值。
 * @return javax.swing.JTextArea
 */
/* 警告：此方法将重新生成。 */
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
 * 此处插入方法描述。
 * 创建日期：(2003-9-17 11:56:40)
 * @author：刘良萍
 * @return java.lang.Object
 */
public java.lang.Object getReturnRightFld() {
	return m_oReturnRightFld;
}
/**
 * 返回 UfoDialogContentPane 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 每当部件抛出异常时被调用
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
	// System.out.println("--------- 未捕捉到的异常 ---------");
	// exception.printStackTrace(System.out);
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
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
    setTitle(StringResource.getStringResource("miufo1001420"));  //"右参照数编辑的参照"
    addHelp();
    // user code end
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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


