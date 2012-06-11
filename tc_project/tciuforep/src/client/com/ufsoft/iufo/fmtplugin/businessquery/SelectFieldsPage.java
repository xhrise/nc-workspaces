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
    //排序列编辑器
    //private DefaultCellEditor[] m_refEditors = null;
    private UIRefCellEditor m_refEditor = null;
    private JButton ivjBtnToRight = null;
    /**
     * 查询定义信息
     */
    private ReportQueryVO m_ReportQueryVO = null;
    /**
     * 待选查询引擎的某查询字段信息
     */
    private FieldTableModel m_oWaitForSelMoldel = null;
    /**
     * 已选的报表查询字段列表
     */
    private FieldTableModel m_oHaveSelMoldel = null;
    /**
     * 父窗口对象
     */
    BQueryPropertyDlg m_ParentDlg = null;

    //参照对话框接口需要的组件属性
	private Component refcomp = null;
	private Component refDialog = null;
	private CellsModel _cellsModel;
	private Context _contextVO;
	private boolean bValidMapped = false;
/**
 * SelectFieldsPage 构造子注解。
 */
private SelectFieldsPage() {
	super();
	initialize();
}
/**
 * 从属于查询属性定义窗口的构件方法。
 *
 * 创建日期：(2003-9-11 12:13:20)
 * @author：刘良萍
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
// * SelectFieldsPage 构造子注解。
// * @param layout java.awt.LayoutManager
// */
//public SelectFieldsPage(java.awt.LayoutManager layout) {
//	super(layout);
//}
///**
// * SelectFieldsPage 构造子注解。
// * @param layout java.awt.LayoutManager
// * @param isDoubleBuffered boolean
// */
//public SelectFieldsPage(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
//	super(layout, isDoubleBuffered);
//}
///**
// * SelectFieldsPage 构造子注解。
// * @param isDoubleBuffered boolean
// */
//public SelectFieldsPage(boolean isDoubleBuffered) {
//	super(isDoubleBuffered);
//}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent event) {
    //最底
    if (event.getSource() == getBtnBottom()) {
        m_oHaveSelMoldel.moveTOBottom(haveSelectTable);
    }

    //向下
    else
        if (event.getSource() == getBtnDown()) {
            m_oHaveSelMoldel.moveDown(haveSelectTable);
        }
    //最上
    else
        if (event.getSource() == getBtnTop()) {
            m_oHaveSelMoldel.moveTOTop(haveSelectTable);
        }
    //向上
    else
        if (event.getSource() == getBtnUp()) {
            m_oHaveSelMoldel.moveUp(haveSelectTable);
        }

    //全不选
    else
        if (event.getSource() == getBtnLeft()) {
            int nCount = haveSelectTable.getRowCount();
            if (nCount > 0) {

                for (int i = 0; i < nCount; i++) {
                    ReportSelectFldVO repSelFldVO =
                        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(0);

                    //自动匹配报表中名称相同的指标/关键字，
                    //...mapname,maptype,mappk
                    //校验查询引擎字段是否已被匹配,并校验数据类型是否相符,同意在“确定中进行”

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
    //全选
    else
        if (event.getSource() == getBtnRight()) {
            int nCount = waitForSelectTable.getRowCount();
            if (nCount > 0) {

                for (int i = 0; i < nCount; i++) {
                    SelectFldVO selectFldVO = (SelectFldVO) m_oWaitForSelMoldel.getObjectByRow(0);
                    //转换为报表查询字段类型
                    ReportSelectFldVO repSelFldVO = new ReportSelectFldVO(selectFldVO);

                    //自动匹配报表中名称相同的指标/关键字，
                    //...mapname,maptype,mappk
                    //校验查询引擎字段是否已被匹配,并校验数据类型是否相符,同意在“确定中进行”

                    m_oWaitForSelMoldel.removeFromTable(0, waitForSelectTable);
                    m_oHaveSelMoldel.addToTable(repSelFldVO, haveSelectTable);
                }

                Rectangle rect =
                    haveSelectTable.getCellRect(haveSelectTable.getRowCount(), 4, true);
                haveSelectTable.scrollRectToVisible(rect);
            }
            return;

        }
    //向左
    else
        if (event.getSource() == getBtnToLeft()) {
            int[] iSelIndexes = haveSelectTable.getSelectedRows();
            int iSelCount = iSelIndexes != null ? iSelIndexes.length : 0;
            for (int i = 0; i < iSelCount; i++) {
                int iSelIndex = haveSelectTable.getSelectedRow();
                if (iSelIndex != -1) {
                    //界面刷新
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

    //向右
    else
        if (event.getSource() == getBtnToRight()) {
            int[] iSelIndexes = waitForSelectTable.getSelectedRows();
            int iSelCount = iSelIndexes != null ? iSelIndexes.length : 0;
            for (int i = 0; i < iSelCount; i++) {
                int iSelIndex = waitForSelectTable.getSelectedRow();
                if (iSelIndex != -1) {
                    SelectFldVO selectFldVO =
                        (SelectFldVO) m_oWaitForSelMoldel.getObjectByRow(iSelIndex);
                    //转换为报表查询字段类型
                    ReportSelectFldVO repSelFldVO = new ReportSelectFldVO(selectFldVO);

                    //自动匹配报表中名称相同的指标/关键字，
                    //...mapname,maptype,mappk
                    //校验查询引擎字段是否已被匹配,并校验数据类型是否相符,统一在“确定中进行”
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
    //指标参照
    else
        if (event.getSource() == ivjJBtnMeasRef) {
            onMeasureRef();
        }
    //关键字参照
    else
        if (event.getSource() == ivjJBtnKeyRef) {
            onKeyRef();
        }

}
/**
	关闭参照对话框之前，清除注册的组件
	注意：鉴于可能出现同时弹出多个参照窗口的情况，每个窗口关闭时都会调用此方法，需要判断关闭的窗口是否为当前注册的窗口
*/
public void beforeDialogClosed(java.awt.Component refDlg) {
    //只有当前注册的窗口才允许真正的取消注册组件
    if (this.refDialog == refDlg) {
        //将注册组件置空
        refcomp = null;
        refDialog = null;
    }
}
/**
 * 参照弹出前的检验。
 *
 * 创建日期：(2003-9-11 14:53:21)
 * @author：刘良萍
 * @return boolean
 * @param iSelIndex int
 */
private boolean doCheckRef(int iSelIndex) {
    //如果没有选中一行进行编辑，则不允许参照
    if (iSelIndex < 0) {
        UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001427")+"!",this);//必须选择一个\"已选项目\"！
        return false;
    }

    return true;
}
/**
 * 返回 JButton7 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
			String strSelToBottom = StringResource.getStringResource("miufo1001294");  //"移至列尾"
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
 * 返回 JButton5 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
			String strSelToDown = StringResource.getStringResource("miufo1001290");  //"下移"
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
 * 返回 JButton4 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnLeft() {
	if (ivjBtnLeft == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/selstoleft.jpg");
			ivjBtnLeft = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnLeft.setName("BtnLeft");
			ivjBtnLeft.setToolTipText("selstoleft");
			ivjBtnLeft.setBounds(194, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelsToLeft = StringResource.getStringResource("miufo1001292");  //"批量左移"
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
 * 返回 JButton2 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnRight() {
	if (ivjBtnRight == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/selstoright.jpg");
			ivjBtnRight = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnRight.setName("JBtnSelsToR");
			ivjBtnRight.setToolTipText("selstoright");
			ivjBtnRight.setBounds(85, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelsToRight = StringResource.getStringResource("miufo1001293");  //"批量右移"
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
 * 返回 JButton3 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnToLeft() {
	if (ivjBtnToLeft == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoleft.jpg");
			ivjBtnToLeft = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnToLeft.setName("BtnToLeft");
			ivjBtnToLeft.setToolTipText("seltoleft");
			ivjBtnToLeft.setBounds(141, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToLeft = StringResource.getStringResource("miufo1001295");  //"左移"
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
 * 返回 JButton8 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnTop() {
	if (ivjBtnTop == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltotop.jpg");
			ivjBtnTop = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnTop.setName("JBtnToTop");
			ivjBtnTop.setToolTipText("seltotop");
			ivjBtnTop.setBounds(530, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToTop = StringResource.getStringResource("miufo1001297");  //"移至列头"
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
 * 返回 JButton1 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnToRight() {
	if (ivjBtnToRight == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoright.jpg");
			ivjBtnToRight = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnToRight.setName("JBtnToR");
			ivjBtnToRight.setToolTipText("seltoright");
			ivjBtnToRight.setBounds(33, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToRight = StringResource.getStringResource("miufo1001296");  //"右移"
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
 * 返回 JButton6 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnUp() {
	if (ivjBtnUp == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoup.jpg");
			ivjBtnUp = new nc.ui.pub.beans.UIButton(icon);
			ivjBtnUp.setName("JBtnUp");
			ivjBtnUp.setToolTipText("seltoup");
			ivjBtnUp.setBounds(408, 282, icon.getIconWidth(), icon.getIconHeight());
			// user code begin {1}
			String strSelToUp = StringResource.getStringResource("miufo1001298");  //"上移"
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
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-4 9:39:22)
 * 参数： @param：<|>
 * 返回值：@return：
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
 * 获得表格
 * 创建日期：(2003-4-2 13:48:52)
 * @return nc.ui.pub.beans.UITable
 */
private FieldTableModel getHaveSelectTM() {
	return (FieldTableModel) haveSelectTable.getModel();
}
/**
 * 返回 JBtnKeyRef 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JBtnMeasRef 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JLabel1 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
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
			String strBeSelectItem = StringResource.getStringResource("miufo1001428");  //"备选项目"
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
 * 返回 JLabel2 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
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
			String strSelectedItems =  StringResource.getStringResource("miufo1001429");  //"已选项目"
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
 * 返回 JLabelMapKey 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelMapKey() {
	if (ivjJLabelMapKey == null) {
		try {
			ivjJLabelMapKey = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelMapKey.setName("JLabelMapKey");
			ivjJLabelMapKey.setText("MapKey:");
			ivjJLabelMapKey.setBounds(416, 321, 75, 16);
			// user code begin {1}
			String strMapKey = StringResource.getStringResource("miufo1001430");  //"映射关键字"
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
 * 返回 JLabelMapMeas 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabelMapMeas() {
	if (ivjJLabelMapMeas == null) {
		try {
			ivjJLabelMapMeas = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
			ivjJLabelMapMeas.setName("JLabelMapMeas");
			ivjJLabelMapMeas.setText("MapMeasure:");
			ivjJLabelMapMeas.setBounds(300, 323, 64, 16);
			// user code begin {1}
			String strMapMeasure = StringResource.getStringResource("miufo1001431");  //"映射指标"
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
 * 返回 JPanel2 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
 * 返回 JPanel1 特性值。
 * @return javax.swing.JPanel
 */
/* 警告：此方法将重新生成。 */
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
	得到参照对话框组件
*/
public java.awt.Component getRefDialog() {
	return refDialog;
}
/**
	得到参照操作组件
*/
public java.awt.Component getRefOper() {
	return refcomp;
}
/**
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-9 10:52:40)
 * 参数： @param：<|>
 * 返回值：@return：
 *
 * @return com.ufsoft.iuforeport.reporttool.data.ReportQueryVO
 */
public ReportQueryVO getReportQueryVO() {
	return m_ReportQueryVO;
}
/**
 * 获得reportselectfldvo数组
 * 创建日期：(2003-4-3 16:16:01)
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
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-4 9:39:22)
 * 参数： @param：<|>
 * 返回值：@return：
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
 * 获得表格
 * 创建日期：(2003-4-2 13:48:52)
 * @return nc.ui.pub.beans.UITable
 */
private FieldTableModel getWaitForSelectTM() {
	return (FieldTableModel) waitForSelectTable.getModel();
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
 * 初始化已选择报表查询字段的表模型
 * 创建日期：(2003-4-2 13:57:39)
 */
private void initHaveSelectTable() {
	Vector vecHaveSelect = null;
	vecHaveSelect = new Vector();
	m_oHaveSelMoldel = new FieldTableModel(vecHaveSelect, false);
	haveSelectTable.setModel(m_oHaveSelMoldel);

	//设置表属性
	haveSelectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	haveSelectTable.sizeColumnsToFit(-1);
	haveSelectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
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
 * 初始化待选择报表查询字段的表模型。
 *
 * 创建日期：(2003-9-10 11:49:00)
 * @author：刘良萍
 */
private void initWaitForSelectTable() {
    Vector vecWaitForFld = null;
    vecWaitForFld = new Vector();
    m_oWaitForSelMoldel = new FieldTableModel(vecWaitForFld, true);
    waitForSelectTable.setModel(m_oWaitForSelMoldel);

    //设置表属性
    waitForSelectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    waitForSelectTable.sizeColumnsToFit(-1);
    waitForSelectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
 * 响应关键字参照。
 *
 * 创建日期：(2003-9-11 11:59:56)
 * @author：刘良萍
 */
private void onKeyRef() {

    //如果没有选中一行进行编辑，则不允许参照
    int iSelIndex = haveSelectTable.getSelectedRow();
    if (!doCheckRef(iSelIndex)) {
        return;
    }

    ReportSelectFldVO repSelFldVO =
        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);

  
    //检验查询字段的数据类型是否可以匹配为关键字:colType目前没有值
    ReportQueryUtil.isMappedDataType((repSelFldVO.getSelectFldVO().getColtype()==null)?MeasureVO.TYPE_CHAR:
        repSelFldVO.getSelectFldVO().getColtype().intValue(),
        MeasureVO.TYPE_CHAR);

    //得到全表的关键字列表参照(含映射状态)
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
 * 响应指标参照。
 *
 * 创建日期：(2003-9-11 12:03:44)
 * @author：刘良萍
 */
private void onMeasureRef() {
    //如果没有选中一行进行编辑，则不允许参照
    int iSelIndex = haveSelectTable.getSelectedRow();
    if (!doCheckRef(iSelIndex)) {
        return;
    }

    ReportSelectFldVO repSelFldVO =
        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);
  //得到全表的指标列表参照
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
	响应参照组件参照
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
                    //类型匹配检验：指标数据类型是否于查询引擎字段类型匹配
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
                    //将映射信息设置到报表查询选择字段
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
                    //如果没有选中一行进行编辑，则不允许参照
                    if (!doCheckRef(iSelIndex)) {
                        return;
                    }

                    ReportSelectFldVO repSelFldVO =
                        (ReportSelectFldVO) m_oHaveSelMoldel.getObjectByRow(iSelIndex);
                    ReportQueryRefKeyWordVO refKeyVO =
                        ((ReportQueryKeyWordRefDlg) refDialog).getRefKeyVO();
                    if (refKeyVO != null) {
                        //将映射信息设置到报表查询选择字段
                        repSelFldVO.setName(refKeyVO.getName());
                        repSelFldVO.setMapPK(refKeyVO.getKeywordPK());
                        repSelFldVO.setMapType(FieldMap.FIELD_MAP_KEYWORD);
                        repSelFldVO.setMapName(
                            FieldMapUtil.getMapName(FieldMap.FIELD_MAP_KEYWORD, refKeyVO.getKeywordPK()));
                    }
                }
            m_oHaveSelMoldel.fireTableDataChanged(); //刷新显示
            haveSelectTable.getSelectionModel().setSelectionInterval(iSelIndex, iSelIndex);
        }
        //setStateMessage("", false);
    } catch (Exception ce) {
        UfoPublic.sendWarningMessage(ce.getMessage(),this);
    }

}
/**
注册参照界面用来激发设置值操作的组件，比如按钮
Class refDialog 是参照界面类
subComp 是参照界面上激发设置值操作的组件
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
 * 说明：
 * 作者：王少松
 * 创建日期：(2003-4-9 10:52:40)
 * 参数： @param：<|>
 * 返回值：@return：
 *
 * @param newReportQueryVO ReportQueryVO
 */
public void setReportQueryVO(ReportQueryVO newReportQueryVO) {
    if (newReportQueryVO == null) {
        return;
    }
    m_ReportQueryVO = newReportQueryVO;

    //向表模型里重新设置数据
    if (m_ReportQueryVO.getQuerydef() != null) {

        //得到已选的报表查询字段，并设置到表模型里
        ReportSelectFldVO[] selectFldVOs = m_ReportQueryVO.getSelectFlds();
        int iLen = selectFldVOs != null ? selectFldVOs.length : 0;
        Hashtable hashHaveSel = new Hashtable();
        Vector vecHaveSelFld = new Vector();
        for (int i = 0; i < iLen; i++) {
            hashHaveSel.put(selectFldVOs[i].getSelectFldVO().getFldname(), selectFldVOs[i]);
            vecHaveSelFld.add(selectFldVOs[i]);
        }
        m_oHaveSelMoldel.resetModel(vecHaveSelFld);

        //过滤已选的报表查询字段，得到待选的查询引擎字段列表
        //并设置到表模型里
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
 * 设置值的CellEditor
 * 创建日期：(2001-7-17 13:18:15)
 * @param iRow int
 * @param iCol int
 */
private void setValueEditor(int iRow, int iCol) {
	TableColumn tc = getHaveSelectTable().getColumnModel().getColumn(iCol);

	if (iCol == 2) {
		//设置排序编辑器的定义
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


