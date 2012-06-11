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
   修改多次参照时数据类型匹配错误提示的bug：类型检验应该在父窗口里进行
 @end
 @update
	解决指标参照时闪烁的问题：
	问题原因：由于在父窗口调用show()时执行的是另一个线程去显示，而在执行show()时，报表目录树还没有处理完成
		这样就造成了闪烁。
	解决方法：在本类中增加了一个isInitOver标识，在父窗口调用show()之前，循环判断isInitOver是否为true，如果是false,则休眠500ms,
		目的是等待本类的初始化完全结束之后再进行show()操作
 @end
 @update 2003-11-20 16:11 liulp
   添加可多次参照的功能
 @end
 @update 2003-10-31 09:58 liulp
  增加帮助链接
 @end
 @update 2003-10-23 13:21 liulp
  错误对话框应该是模态的
 @end
 * 报表查询映射指标的指标参照对话框。
 *
 * 创建日期：(2003-9-13 9:44:39)
 * @author：刘良萍
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
     * 报表内的所有指标的表模型
     */
    private ReportQueryMeasRefTM m_oRefMeasListMoldel = null;
    /**
    * 报表内的所有指标列表数据
    */
    private ValueObject[] m_oRqRefMeasVOs = null;
    /**
    * 选中的参照指标
    */
    private ReportQueryRefMeasVO m_returnVO = null;
    /**
    * 要被映射的报表查询字段
    */
    private ReportSelectFldVO m_oRepSelFldVO = null;
    /**
    * 要被映射的报表查询字段
    */
    private ReportBusinessQuery m_oRepBusinessQuery = null;
    /**
     * 初始完毕标识
     */
    public boolean isInitOver = false;
	private DialogRefListener _dialogRefListener;
///**
// * 构造器
// */
///* 警告：此方法将重新生成。 */
//public ReportQueryMeasRefDlg() {
//	super();
//	initialize();
//}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-15 20:12:22)
 * @author：刘良萍
 * @param parent java.awt.Container
 * @param repSelFldVO com.ufsoft.iuforeport.reporttool.query.ReportSelectFldVO
 * @param repBusinessQuery com.ufsoft.iuforeport.reporttool.query.ReportBusinessQuery
 * @param rqRefMeasVOs nc.vo.pub.ValueObject[] - 类型为ReportQueryRefMeasVO
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
	//参照
    if (event.getSource() == ivjJBtnOK) {
        ReportQueryRefMeasVO selectVO = getSelectRefMeasVO();
        if (selectVO != null) {
            //如果是已被映射的，则不响应
            if (selectVO.getMapStatus() == ReportQueryRefKeyWordVO.MAP_STATUS_OTHER) {
                return;
            }
            	
            //设置为返回结果,类型匹配检验在父窗口里进行
            
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
    //关闭
    else
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
public void dispose() {
    setResult(ID_CANCEL);
    _dialogRefListener.beforeDialogClosed(this);
    super.dispose();
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
			ivjJBtnCancel.setBounds(274, 284, 75, 22);
			// user code begin {1}
			String strClose = StringResource.getStringResource("miufopublic253");  //"关闭"
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
            ivjJBtnOK.setBounds(154, 285, 75, 22);
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
 * 获得报表格式模型里的所有指标列表（包括固定区域指标和所有动态区域指标）。
 * 
 * 创建日期：(2003-9-13 11:15:50)
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
 * 得到选择的参照指标。
 * 创建日期：(2003-9-13 15:24:31)
 * @return nc.vo.iufo.measure.MeasureVO
 */
public MeasureVO getRefMeasVO() {
    return m_returnVO;
}
/**
 * 得到当前选中的参照对象。
 * 
 * 创建日期：(2003-11-20 13:50:09)
 * @author：刘良萍
 * @return com.ufsoft.iuforeport.reporttool.query.ReportQueryRefMeasVO
 */
private ReportQueryRefMeasVO getSelectRefMeasVO() {
    int row = measListTable.getSelectedRow();
    if (row < 0)
        row = 0;
    return (ReportQueryRefMeasVO) m_oRefMeasListMoldel.getVO(row);
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
        setName("ReportQueryMeasRefDlg");
//        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(382, 347);
        setContentPane(getUfoDialogContentPane());
        setModal(false);
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    setTitle(StringResource.getStringResource("miufo1001389"));  //"报表查询-映射指标"
    addHelp();
    isInitOver = true;
    // user code end
}
/**
 * 初始化报表所有指标的表模型。
 * 
 * 创建日期：(2003-9-13 11:42:17)
 */
private void initMeasListTable() {
    m_oRefMeasListMoldel = new ReportQueryMeasRefTM(m_oRqRefMeasVOs);
    measListTable.setModel(m_oRefMeasListMoldel);

    //设置表属性
    measListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    measListTable.sizeColumnsToFit(-1);
    measListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
		//System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 的 main() 中发生异常");
		//exception.printStackTrace(System.out);
	//}
}
}


