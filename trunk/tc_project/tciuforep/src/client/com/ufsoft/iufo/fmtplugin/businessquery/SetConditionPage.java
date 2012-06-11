package com.ufsoft.iufo.fmtplugin.businessquery;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
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

import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIFrame;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextField;
import nc.vo.iuforeport.businessquery.QuerySimpleDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.iuforeport.businessquery.WhereCondVO;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;

/*
@update
  2003-12-31 09:02 liulp
   业务查询whereCondVO的方法名称修改
 @end
 @update
  2003-11-04 15:29 liulp
   对whereCondVO的数据类型、空，单双引号进行了统一的校验处理
 @end
 @update 2003-10-30 20:40 liulp
  记下条件的物理字段名，在执行查询前克窿新对象，将其赋值到leftOpe属性上
 @end
 @update 2003-10-23 13:21 liulp
  错误对话框应该是模态的
 @end
 @update 2003-10-22 15:37
   选择没有定义查询引擎字段的业务查询，右面面板不能显示属性值
 @end
 * 说明: 选择筛选条件
 * 作者：王少松
 * 创建日期：(2003-4-3 8:55:06)
 * 版本：
 */
public class SetConditionPage extends nc.ui.pub.beans.UIPanel implements ActionListener {
    private JLabel ivjJLabel1 = null;
    private JPanel ivjJPanel1 = null;
    private JButton ivjBtnDel = null;
    private JButton ivjBtnDown = null;
    private JButton ivjBtnUp = null;
    private JButton ivjJBtnRightFldRef = null;
    //编辑器数组
    //private DefaultCellEditor[] m_refEditors = null;
    private JTable whereConTable = null;
    private JButton ivjBtnAdd = null;
    /**
      * 各列的编辑器
      */
    private DefaultCellEditor[] m_oEditors = null;
    /**
      * 查询定义信息
      */
    private ReportQueryVO m_oReportQueryVO = null;
    /**
     * 选择条件信息
     */
    private WhereConTableModel m_oWhereConModel = null;
    /**
     * 第一个查询引擎定义选择字段的字段名称
     */
    private String m_strFirstSelFldName = "";
    /**
     * 查询引擎定义选择字段集合的字段类型值Hash(name,Integer(colType)
     */
    private Hashtable m_hashColTypes = null;
    /**
     * 查询引擎定义选择字段集合的Hash(name,Expresion)
     */
    private Hashtable m_hashExpresions = null;
    /**
     *字段列编辑器的值容器
     */
    private JComboBox m_oFldComboBox = null;
    
    private Context m_ufoContext=null;
/**
 * SetConditionPage 构造子注解。
 */
public SetConditionPage() {
	super();
	initialize();
}
/**
 * SetConditionPage 构造子注解。
 * @param layout java.awt.LayoutManager
 */
public SetConditionPage(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * SetConditionPage 构造子注解。
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public SetConditionPage(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * SetConditionPage 构造子注解。
 * @param isDoubleBuffered boolean
 */
public SetConditionPage(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent e) {
    //增加
	TableCellEditor cellEditor = getWhereConTable().getCellEditor();
	if(cellEditor != null){
		cellEditor.stopCellEditing();
	}
    if (e.getSource() == getBtnAdd()) {
        //终止编辑态
        if (whereConTable.getCellEditor() != null)
            whereConTable.getCellEditor().stopCellEditing();

        //界面加行
        int iIndex = m_oWhereConModel.getRowCount();

        WhereCondVO whereCondVO = new WhereCondVO();
        whereCondVO.setTypeflag("C");
        if (iIndex == 0) {
            whereCondVO.setRelationflag("where");
        } else {
            whereCondVO.setRelationflag("and");
        }
        whereCondVO.setLeftfld(m_strFirstSelFldName);
        whereCondVO.setOperator("=");
        whereCondVO.setRightfld("");
        m_oWhereConModel.addToTable(whereCondVO, whereConTable);

        //选中新增行
        whereConTable.getSelectionModel().setSelectionInterval(iIndex, iIndex);
        Rectangle rect = whereConTable.getCellRect(iIndex, 4, true);
        whereConTable.scrollRectToVisible(rect);
        return;

    }

    //删除
    else
        if (e.getSource() == getBtnDel()) {

            //终止编辑态
            if (whereConTable.getCellEditor() != null)
                whereConTable.getCellEditor().stopCellEditing();
            int iSelIndex = whereConTable.getSelectedRow();
            if (iSelIndex != -1) {
                m_oWhereConModel.removeFromTable(iSelIndex, whereConTable);
            }
            return;

        }
    //向下
    else
        if (e.getSource() == getBtnDown()) {
            int selIndex = whereConTable.getSelectedRow();
            //不是where的行，都可以"下移"
            if (selIndex != 0) {
                m_oWhereConModel.moveDown(whereConTable);
            }
        }

    //向上
    else
        if (e.getSource() == getBtnUp()) {
            int selIndex = whereConTable.getSelectedRow();
            //是第二行，都不能上移动，保证"where"行
            if (selIndex > 1) {
                m_oWhereConModel.moveUp(whereConTable);
            }
        }
    //右操作数参照
    else
        if (e.getSource() == getJBtnRightFldRef()) {
            onRightFldRef();
        }
}
/**
 * 返回 BtnAdd 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnAdd() {
	if (ivjBtnAdd == null) {
		try {
			ivjBtnAdd = new nc.ui.pub.beans.UIButton();
			ivjBtnAdd.setName("BtnAdd");
	//		ivjBtnAdd.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnAdd.setText("Add");
			ivjBtnAdd.setBounds(513, 170, 75, 22);
			// user code begin {1}
			String strAdd = StringResource.getStringResource("miufo1000080");  //"增加"
			ivjBtnAdd.setText(strAdd);
			ivjBtnAdd.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnAdd;
}
/**
 * 返回 JButton1 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnDel() {
	if (ivjBtnDel == null) {
		try {
			ivjBtnDel = new nc.ui.pub.beans.UIButton();
			ivjBtnDel.setName("BtnDel");
	//		ivjBtnDel.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnDel.setText("Delete");
			ivjBtnDel.setBounds(513, 212, 75, 22);
			// user code begin {1}
			String strDelete = StringResource.getStringResource("miufopublic243");  //"删除"
			ivjBtnDel.setText(strDelete);
			ivjBtnDel.addActionListener(this);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBtnDel;
}
/**
 * 返回 JButton3 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnDown() {
	if (ivjBtnDown == null) {
		try {
			ivjBtnDown = new nc.ui.pub.beans.UIButton();
			ivjBtnDown.setName("BtnDown");
	//		ivjBtnDown.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnDown.setText("Down");
			ivjBtnDown.setBounds(514, 298, 75, 22);
			// user code begin {1}
			String strDown = StringResource.getStringResource("miufo1001290");	//下移
			ivjBtnDown.setText(strDown);
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
 * 返回 JButton2 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getBtnUp() {
	if (ivjBtnUp == null) {
		try {
			ivjBtnUp = new nc.ui.pub.beans.UIButton();
			ivjBtnUp.setName("BtnUp");
	//		ivjBtnUp.setFont(new java.awt.Font("dialog", 0, 12));
			ivjBtnUp.setText("Up");
			ivjBtnUp.setBounds(515, 255, 75, 22);
			// user code begin {1}
			String strUp = StringResource.getStringResource("miufo1001298");  //"上移"
			ivjBtnUp.setText(strUp);
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
 * 返回 JBtnRightFldRef 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBtnRightFldRef() {
    if (ivjJBtnRightFldRef == null) {
        try {
            ivjJBtnRightFldRef = new nc.ui.pub.beans.UIButton();
            ivjJBtnRightFldRef.setName("JBtnRightFldRef");
            ivjJBtnRightFldRef.setText("RightFldRef");
            ivjJBtnRightFldRef.setBounds(478, 49, 75, 22);
            // user code begin {1}
            String strRightFldRef = StringResource.getStringResource("miufo1001433");  //"右操作数参照"
            ivjJBtnRightFldRef.setText(strRightFldRef);
            ivjJBtnRightFldRef.addActionListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBtnRightFldRef;
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
			ivjJLabel1.setText("FilterCond");
			ivjJLabel1.setBounds(13, 15, 70, 16);
			ivjJLabel1.setForeground(java.awt.Color.black);
			// user code begin {1}
			String strFilterCond = StringResource.getStringResource("miufo1001434");  //"筛选条件"
			ivjJLabel1.setText(strFilterCond);
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
            ivjJPanel1.setBounds(13, 41, 460, 288);
            // user code begin {1}
            JScrollPane ps = new UIScrollPane(getWhereConTable());
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
 * 获得where条件数组
 * 创建日期：(2003-4-3 16:16:01)
 * @return nc.vo.iuforeport.businessquery.WhereCondVO[]
 */
public WhereCondVO[] getResultFromCond(StringBuffer sbWhereErr) {
    int nCount = m_oWhereConModel.getRowCount();
    WhereCondVO[] whereCondVOs = null;
    if (nCount > 0) {
        whereCondVOs = new WhereCondVO[nCount];
        Vector vec = m_oWhereConModel.getAll();
        int iLen = vec != null ? vec.size() : 0;
        whereCondVOs = new WhereCondVO[iLen];
        for (int i = 0; i < iLen; i++) {
            whereCondVOs[i] = (WhereCondVO) vec.get(i);
            String strLeftFld = whereCondVOs[i].getLeftfld();
            Object objRightFld = whereCondVOs[i].getRightfld();

            whereCondVOs[i].setRightfld(
                ReportQueryUtil.checkValidWhereCond(
                    ((Integer) m_hashColTypes.get(strLeftFld)).intValue(),
                    strLeftFld,
                    objRightFld,
                    sbWhereErr));

            //将左操作数的物理字段名暂时放在Expresion里，计算的时候在客隆对象放在leftFld上
            whereCondVOs[i].setExpression0((String) m_hashExpresions.get(strLeftFld));
            //设置数据类型属性值
            whereCondVOs[i].setColtype((Integer) m_hashColTypes.get(strLeftFld));
        }
    }
    //debug
    //sbWhereErr.append("debug...");
    //end
    return whereCondVOs;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-9-16 13:31:27)
 * @author：刘良萍
 * @return javax.swing.JTable
 */
private JTable getWhereConTable() {
    if (whereConTable == null) {
        whereConTable = new nc.ui.pub.beans.UITable();
        whereConTable.setAutoCreateColumnsFromModel(false);
        initWhereConTable();

        //增加列
        String[] colStrs = m_oWhereConModel.getHead();
        for (int k = 0; k < colStrs.length; k++) {
            TableCellRenderer renderer = new DefaultTableCellRenderer();
            TableCellEditor editor = null;
            TableColumn column = new TableColumn(k, 80, renderer, editor);
            whereConTable.addColumn(column);
        }

    }

    return whereConTable;
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
 * 初始化单元编辑器。
 *
 * 创建日期：(2003-9-16 16:10:04)
 * @author：刘良萍
 */
private void initEditorValue() {
    int iLen = m_oWhereConModel.getHead().length;
    m_oEditors = new DefaultCellEditor[iLen];
    for (int i = 0; i < iLen; i++) {

        //设置关系符列编辑器
        if (i == WhereConTableModel.COLUMN_RELATION_SYNBOL) {
            JComboBox cbbRelation = new UIComboBox(new Object[] { "and", "or" });
            m_oEditors[i] = new DefaultCellEditor(cbbRelation);
        }

        //设置字段列编辑器
        else
            if (i == WhereConTableModel.COLUMN_FIELD) {
                JComboBox cbbFld = cbbFld = new UIComboBox();
                m_oEditors[i] = new DefaultCellEditor(cbbFld);
                m_oFldComboBox = cbbFld;
            }

        //设置操作符列编辑器
        else
            if (i == WhereConTableModel.COLUMN_COMPARE_SYNBOL) {
                JComboBox cbbOperator =
                    new UIComboBox(new Object[] { "=", ">", "<", "<>", ">=", "<=", "like", "in" });
                m_oEditors[i] = new DefaultCellEditor(cbbOperator);
            }

        //设置右操作数编辑器
        else
            if (i == WhereConTableModel.COLUMN_RIGHT_OPE) {
                m_oEditors[i] = new DefaultCellEditor(new UITextField());
            }
    }
}
/**
 * 初始化类。
 */
/* 警告：此方法将重新生成。 */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("SetConditionPage");
		setLayout(null);
		setSize(611, 350);
		add(getJLabel1(), getJLabel1().getName());
		add(getJPanel1(), getJPanel1().getName());
		add(getBtnDel(), getBtnDel().getName());
		add(getBtnUp(), getBtnUp().getName());
		add(getBtnDown(), getBtnDown().getName());
		add(getBtnAdd(), getBtnAdd().getName());
		add(getJBtnRightFldRef(), getJBtnRightFldRef().getName());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}

	// user code end
}
/**
 * 初始化待选择报表查询字段的表模型。
 *
 * 创建日期：(2003-9-16 13:51:17)
 * @author：刘良萍
 */
private void initWhereConTable() {
    Vector vecWhereCon = new Vector();
    m_oWhereConModel = new WhereConTableModel(vecWhereCon);
    whereConTable.setModel(m_oWhereConModel);

    //设置表属性
    whereConTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    whereConTable.sizeColumnsToFit(-1);
    whereConTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		javax.swing.JFrame frame = new UIFrame();
		SetConditionPage aSetConditionPage;
		aSetConditionPage = new SetConditionPage();
		frame.setContentPane(aSetConditionPage);
		frame.setSize(aSetConditionPage.getSize());
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
		System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoPanel's main():Exception");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}
}
/**
 * 右操作数参照。
 *
 * 创建日期：(2003-9-17 11:39:45)
 * @author：刘良萍
 */
private void onRightFldRef() {
    int selIndex = whereConTable.getSelectedRow();
    if (selIndex >= 0) {
        WhereCondVO whereCondVO =
            (WhereCondVO) m_oWhereConModel.getObjectByRow(selIndex);
        Object oldRightFld = whereCondVO.getRightfld();
        if (oldRightFld instanceof Double) {
            oldRightFld = new Double(((Double) oldRightFld).doubleValue());
        } else {
            oldRightFld = new String((String) oldRightFld);
        }

        RightFldRefDlg rightFldRefDlg =
            new RightFldRefDlg(
                this,
                (Integer) m_hashColTypes.get(whereCondVO.getLeftfld()),
                oldRightFld);
        rightFldRefDlg.setUfoContext(getUfoContext());
        rightFldRefDlg.setModal(true);
        rightFldRefDlg.showModal();

        //得到编辑结果
        if (rightFldRefDlg.getResult() == UfoDialog.ID_OK) {
            Object newRightFld = rightFldRefDlg.getReturnRightFld();
            whereCondVO.setRightfld(newRightFld);
        }

    } else {
        UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001435"),this);  //"请先选中一行！"
    }
}
/**
 * 向导第一个页面选择报表查询，设置相应的wherecon初始界面：
 *
 * 作者：王少松
 * 创建日期：(2003-4-9 10:52:58)
 * 修改者：刘良萍
 * 修改日期：(2003-9-16 09:52:58)
 * 参数： @param：<|>
 * 返回值：@return：
 *
 * @param newReportQueryVO com.ufsoft.iuforeport.reporttool.data.ReportQueryVO
 */
public void setReportQueryVO(ReportQueryVO newReportQueryVO) {
    if (newReportQueryVO == null || newReportQueryVO.getQuerydef() == null) {
        return;
    }
    m_oReportQueryVO = newReportQueryVO;

    String[] strSelFldNames = null;
    QuerySimpleDef def = m_oReportQueryVO.getQuerydef();
    SelectFldVO[] sfs = def.getSelectFlds();
    int iLen = sfs != null ? sfs.length : 0;
    if (iLen <= 0) {
        return;
    }
    //初始化表模型的编辑器，以及第一个选择字段的值
    strSelFldNames = new String[iLen];
    m_hashColTypes = new Hashtable();
    m_hashExpresions = new Hashtable();
    for (int i = 0; i < iLen; i++) {
        strSelFldNames[i] = sfs[i].getFldname();
        m_hashColTypes.put(strSelFldNames[i], sfs[i].getColtype());
        m_hashExpresions.put(strSelFldNames[i], sfs[i].getExpression());
    }
    m_strFirstSelFldName = strSelFldNames[0];
    if (m_oEditors == null) {
        initEditorValue();

        //设置列编辑器
        setValueEditor();
    }
    if (m_oFldComboBox != null) {
        m_oFldComboBox.removeAllItems();
        int iSize = strSelFldNames != null ? strSelFldNames.length : 0;
        for (int i = 0; i < iSize; i++) {
            m_oFldComboBox.addItem(strSelFldNames[i]);
        }
    }

    //重新设置where条件表模型数据
    WhereCondVO[] whereConVOs = m_oReportQueryVO.getWhereCondVOs();
    Vector vecWhereCons = new Vector();
    if (whereConVOs == null || whereConVOs.length == 0) {
        vecWhereCons = new Vector();
    } else {
        vecWhereCons = new Vector(Arrays.asList(whereConVOs));
    }
    m_oWhereConModel.resetModel(vecWhereCons);
}
/**
 * 设置值的CellEditor
 *
 * 创建日期：(2003-9-16 16:28:20)
 * @author：刘良萍
 */
private void setValueEditor() {
    if (m_oEditors != null) {
        int iLen = m_oWhereConModel.getHead().length;
        for (int iCol = 0; iCol < iLen; iCol++) {
            TableColumn tc = getWhereConTable().getColumnModel().getColumn(iCol);
            //获得定义
            if (iCol == 0 || iCol == 2 || iCol == 3) {
                //设置关系符、操作符列编辑器
                tc.setCellEditor(m_oEditors[iCol]);
            } else
                if (iCol == 1) {
                    tc.setCellEditor(m_oEditors[iCol]);
                }
        }
    }
}
/**
 * 设置值的CellEditor
 * 创建日期：(2001-7-17 13:18:15)
 * @param iRow int
 * @param iCol int
 */
private void setValueEditor(int iRow, int iCol) {
    TableColumn tc = getWhereConTable().getColumnModel().getColumn(iCol);

    //获得定义
    if (iCol == 0 || iCol == 2 || iCol == 3) {
        //设置关系符、操作符列编辑器
        tc.setCellEditor(m_oEditors[iCol]);
    } else
        if (iCol == 1) {
            tc.setCellEditor(m_oEditors[iCol]);
        }

}

public Context getUfoContext() {
	return m_ufoContext;
}
public void setUfoContext(Context context) {
	m_ufoContext = context;
}
}


