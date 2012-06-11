package com.ufsoft.iufo.fmtplugin.businessquery;

/**
 @update 2003-11-20 16:13 liulp
  控制列表必须选择一项
 @end
 @update 2003-11-05 09:38 liulp
  增加报表查询删除的确认提示框
 @end
 @update 2003-10-31 09:58 liulp
  增加帮助链接
 @end
 @update
 2002-10-14 17:31
   完成报表查询修改、删除管理功能
 @end
 * 报表查询修改、删除管理界面。
 * 创建日期：(2003-10-8 15:26:21)
 * @author：王海涛
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
    //JList的内容
    private Vector m_oQueryVec = null;
    //JList控件
    private JList queryList = null;
    /**
     * 选择的GUID数组
     */
    private String[] m_strSelectGUIDs = null;
    public static int ID_UPDATE = 10;
    public static int ID_DELETE = 11;
/**
 * 构造器
 */
/* 警告：此方法将重新生成。 */
public ReportQueryMngDlg() {
	super();
	initialize();
}
/**
 * ReportQueryMngDlg 构造子注解。
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
 * ReportQueryMngDlg 构造子注解。
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
    //取消
    if (e.getSource() == getJButtonCancel()) {
        setResult(ID_CANCEL);
        close();
    }
    //修改
    else
        if (e.getSource() == ivjJButtonUpdate) {
            ReportQueryMngData data = (ReportQueryMngData) getJList().getSelectedValue();
            if (data != null) {
                m_strSelectGUIDs = new String[] { data.getGUID()};
            }
            if(m_strSelectGUIDs == null || m_strSelectGUIDs.length <= 0 )
            {
	            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001394"),this);  //"请选择一个查询!"
	            return;
            }
            setResult(ID_UPDATE);
            close();
        }
    //删除
    else
        if (e.getSource() == ivjJButtonDelete) {
            Object[] datas = getJList().getSelectedValues();
            if (datas != null && datas.length > 0) {
                int iLen = datas.length;
                if (iLen > 0) {
                    //用户确认提示
                    String strMessage = StringResource.getStringResource("miufo1001395");  //"确信删除所选中的报表查询吗？"
                    int nRs =
                        javax.swing.JOptionPane.showConfirmDialog(
                            this,
                            strMessage,
                            StringResource.getStringResource("miufopublic384"),  //"提示信息"
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
	           UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001394"),this);  //"请选择一个查询!"
            }
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
    hb.enableHelpKey(getContentPane(), "TM_Data_Query_Update", null);

}
/**
 * 返回 JButtonCancel 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JButtonDelete 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JButtonUpdate 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJButtonUpdate() {
	if (ivjJButtonUpdate == null) {
		try {
			ivjJButtonUpdate = new nc.ui.pub.beans.UIButton();
			ivjJButtonUpdate.setName("JButtonUpdate");
			ivjJButtonUpdate.setText(StringResource.getStringResource("miufo1001396"));  //"修  改"
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
 * 返回 JLabelList 特性值。
 * @return JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelList() {
	if (ivjJLabelList == null) {
		try {
			ivjJLabelList = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelList.setName("JLabelList");
			ivjJLabelList.setText("QueryList");
			ivjJLabelList.setBounds(26, 19, 73, 14);
			// user code begin {1}
			String strQueryList = StringResource.getStringResource("miufo1001397");  //"查询列表:"
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
 * 得到自定义的列表控件。
 * 修改日期：(2003-10-13 20:23:50)
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
 * 返回 JScrollPane1 特性值。
 * @return JScrollPane
 */
/* 警告：此方法将重新生成。 */
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
 * 得到选择的报表查询。
 * 
 * 创建日期：(2003-10-13 20:31:48)
 * @return java.lang.String[]
 */
public java.lang.String[] getSelectGUIDs() {
    return m_strSelectGUIDs;
}
/**
 * 返回 UfoDialogContentPane 特性值。
 * @return JPanel
 */
/* 警告：此方法将重新生成。 */
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
		setName("ReportQueryMngDlg");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(363, 314);
		setTitle(StringResource.getStringResource("miufo1001398"));  //"报表查询管理"
		setContentPane(getUfoDialogContentPane());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	addHelp();
	// user code end
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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


