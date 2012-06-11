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
  添加可多次参照的功能
@end
@update 2003-10-31 09:58 liulp
 增加帮助链接
@end
* 报表查询映射关键字的参照对话框。
* 
* 创建日期：(2003-9-15 15:16:13)
* @author：刘良萍
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
     * 报表的所有关键字列表
     */
    ValueObject[] m_okeyVOs = null;
    /**
     * 报表内的所有关键字的表模型
     */
    private ReportQueryKeyWordRefTM m_oRefKeyListMoldel = null;
    /**
    * 选中的参照关键字
    */
    private ReportQueryRefKeyWordVO m_returnVO = null;
    /**
     * 初始完毕标识
     */
    public boolean isInitOver = false;
	private DialogRefListener _dialogRefListener;
/**
 * ReportQueryKeyWordRefDlg 构造子注解。
 */
public ReportQueryKeyWordRefDlg() {
	super();
	initialize();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-15 15:20:26)
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
        //已被其他查询映射的不能再被本查询映射
        if (selectVO.getMapStatus() == ReportQueryRefKeyWordVO.MAP_STATUS_OTHER) {
            return;
        }else
        	selectVO.setMapStatus(ReportQueryRefKeyWordVO.MAP_STATUS_SELF);

        //设置返回结果
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
 * 返回 JBtnOK 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnOK() {
    if (ivjJBtnOK == null) {
        try {
            ivjJBtnOK = new nc.ui.pub.beans.UIButton();
            ivjJBtnOK.setName("JBtnOK");
            ivjJBtnOK.setText("Ref");
            ivjJBtnOK.setBounds(159, 231, 75, 22);
            // user code begin {1}
            String strRef = StringResource.getStringResource("miufopublic283");  //"参照"
            ivjJBtnOK.setText(strRef);
			
//            if (this.getParent() instanceof DialogRefListener) {
                //注册参照界面用来激发设置值操作的组件:按钮"参 照"
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
 * 返回 JPanel1 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 获得报表格式模型里的所有关键字列表（包括固定区域关键字和所有动态区域关键字(过滤掉"行号")）。
 *
 * 创建日期：(2003-9-15 15:38:22)
 * @author：刘良萍
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
 * 此处插入方法描述。
 * 创建日期：(2003-9-15 16:11:56)
 * @author：刘良萍
 * @return nc.vo.iufo.keydef.KeyVO
 */
public ReportQueryRefKeyWordVO getRefKeyVO() {
    return m_returnVO;
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
        setName("ReportQueryKeyWordRefDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(359, 295);
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    setTitle(StringResource.getStringResource("miufo1001386"));  //"报表查询-映射关键字"
    addHelp();
    isInitOver = true;
    // user code end
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-15 15:42:26)
 * @author：刘良萍
 */
private void initKeyListTable() {
    m_oRefKeyListMoldel = new ReportQueryKeyWordRefTM(m_okeyVOs);
    keyListTable.setModel(m_oRefKeyListMoldel);

    //设置表属性
    keyListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    keyListTable.sizeColumnsToFit(-1);
    keyListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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


