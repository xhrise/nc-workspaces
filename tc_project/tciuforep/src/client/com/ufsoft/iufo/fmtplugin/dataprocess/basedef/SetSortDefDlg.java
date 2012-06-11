package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;



public class SetSortDefDlg
    extends com.ufsoft.report.dialog.UfoDialog
    implements java.awt.event.ActionListener {
    /**
     * 数据处理定义对象
     */
    private DefaultDataProcessDef m_oDataProcessDef = null;
    /**
     *	对话框组件
     */
    private JButton ivjJBtnCancel = null;
    private JButton ivjJBtnOK = null;
    private JPanel ivjUfoDialogContentPane = null;
    private JButton ivjJBDown = null;
    private JButton ivjJBToBottom = null;
    private JButton ivjJBToTop = null;
    private JButton ivjJBUp = null;
    private JPanel ivjJPanelList = null;
    private JScrollPane ivjJSPItemList = null;
    /**
    *	内部变量
    */
    private JTable m_oTable = null;
    private ItemTableModel m_oTableModel = null;
/**
 * 此处插入方法描述。
 * 
 * 创建日期：(2003-8-22 14:16:23)
 * @author：刘良萍
 * @param parent java.awt.Container
 * @param vecAllDynAreaDPFld java.util.Vector - 元素为DataProcessFld
 * @param processDef com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
 */
public SetSortDefDlg(
    java.awt.Container parent,
    Vector vecAllDynAreaDPFld,
    DefaultDataProcessDef processDef) {
    super(parent);
    this.m_oDataProcessDef = processDef;

    initTable(vecAllDynAreaDPFld);

    initialize();
}
/**
 * SetSortDefDlg 构造子注解。
 */
public SetSortDefDlg(Vector fullItem) {
	super();
	initTable(fullItem);
	initialize();
}
/**
 * SetSortDefDlg 构造子注解。
 */
public SetSortDefDlg(Vector fullItem,DefaultDataProcessDef processDef) {
	super();
	initTable(fullItem);
	this.m_oDataProcessDef = processDef;
	initialize();
}
/**
 * Invoked when an action occurs.
 */
public void actionPerformed(java.awt.event.ActionEvent e) {
    if (e.getSource() == getJBtnCancel()) {
        this.setResult(UfoDialog.ID_CANCEL);
        close();
    } else
        if (e.getSource() == getJBtnOK()) {
            //设置数据处理定义的排序
            OrderByFld[] flds = new OrderByFld[m_oTableModel.getSize()];
            m_oTableModel.getAll().copyInto(flds);
            m_oDataProcessDef.setOrderByFlds(flds);

            setResult(UfoDialog.ID_OK);
            close();
        } else
            if (e.getSource() == ivjJBDown) {
                m_oTableModel.moveDown(m_oTable);
            } else
                if (e.getSource() == ivjJBUp) {
                    m_oTableModel.moveUp(m_oTable);
                } else
                    if (e.getSource() == ivjJBToBottom) {
                        m_oTableModel.moveTOBottom(m_oTable);
                    } else
                        if (e.getSource() == ivjJBToTop) {
                            m_oTableModel.moveTOTop(m_oTable);
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
    hb.enableHelpKey(getContentPane(), "TM_Data_Process_Order", null);

}
/**
 * 此处插入方法描述。
 * 
 * 创建日期：(2003-8-12 14:48:16)
 * @author：刘良萍
 * @return com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
 */
public DefaultDataProcessDef getDataProcessDef() {	
	return m_oDataProcessDef;
}
/**
 * 返回 JBDown 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
private JButton getJBDown() {
	if (ivjJBDown == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltodown.jpg");
			ivjJBDown = new UIButton(icon);
			ivjJBDown.setName("JBDown");
			ivjJBDown.setToolTipText("seltodown");
			ivjJBDown.setText("");
			ivjJBDown.setBounds(390, 200, icon.getIconWidth(), icon.getIconHeight());
			ivjJBDown.addActionListener(this);
			// user code begin {1}
			String strSeltoDown = StringResource.getStringResource("miufo1001262");  //"向下移"
			ivjJBDown.setToolTipText(strSeltoDown);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBDown;
}
/**
 * 返回 JBtnCancel 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
private JButton getJBtnCancel() {
	if (ivjJBtnCancel == null) {
		try {
			ivjJBtnCancel = new UIButton();
			ivjJBtnCancel.setName("JBtnCancel");
			ivjJBtnCancel.setText("Cancel");
			ivjJBtnCancel.setBounds(393, 375, 85, 27);
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
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
private JButton getJBtnOK() {
	if (ivjJBtnOK == null) {
		try {
			ivjJBtnOK = new UIButton();
			ivjJBtnOK.setName("JBtnOK");
			ivjJBtnOK.setText("OK");
			ivjJBtnOK.setBounds(278, 375, 85, 27);
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
 * 返回 JBToBottom 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
private JButton getJBToBottom() {
	if (ivjJBToBottom == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltobottom.jpg");
			ivjJBToBottom = new UIButton(icon);
			ivjJBToBottom.setName("seltobottom");
			ivjJBToBottom.setToolTipText("");
			ivjJBToBottom.setText("");
			ivjJBToBottom.setBounds(390, 250, icon.getIconWidth(), icon.getIconHeight());
			ivjJBToBottom.addActionListener(this);
			// user code begin {1}
			String strSelToBottom  =StringResource.getStringResource("miufo1001312");  //"到最后"
			ivjJBToBottom.setToolTipText(strSelToBottom);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBToBottom;
}
/**
 * 返回 JBToTop 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
private JButton getJBToTop() {
	if (ivjJBToTop == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltotop.jpg");
			ivjJBToTop = new UIButton(icon);
			ivjJBToTop.setName("JBToTop");
			ivjJBToTop.setToolTipText("seltotop");
			ivjJBToTop.setText("");
			ivjJBToTop.setBounds(390, 150, icon.getIconWidth(), icon.getIconHeight());
			ivjJBToTop.addActionListener(this);
			// user code begin {1}
			String strSelToTop = StringResource.getStringResource("miufo1001313");  //"到最前"
			ivjJBToTop.setToolTipText(strSelToTop);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBToTop;
}
/**
 * 返回 JBUp 特性值。
 * @return JButton
 */
/* 警告：此方法将重新生成。 */
private JButton getJBUp() {
	if (ivjJBUp == null) {
		try {
			ImageIcon icon = ResConst.getImageIcon("iufoplugin/seltoup.jpg");
			ivjJBUp = new UIButton(icon);
			ivjJBUp.setName("JBUp");
			ivjJBUp.setToolTipText("seltoup");
			ivjJBUp.setText("");
			ivjJBUp.setBounds(390, 100, icon.getIconWidth(), icon.getIconHeight());
			ivjJBUp.addActionListener(this);
			// user code begin {1}
			String strSelToUp = StringResource.getStringResource("miufo1001263");  //"向上移"
			ivjJBUp.setToolTipText(strSelToUp);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJBUp;
}
/**
 * 返回 JPanelList 特性值。
 * @return JPanel
 */
/* 警告：此方法将重新生成。 */
private JPanel getJPanelList() {
	if (ivjJPanelList == null) {
		try {
			ivjJPanelList = new UIPanel();
			ivjJPanelList.setName("JPanelList");
			ivjJPanelList.setLayout(null);
			ivjJPanelList.setBounds(15, 15, 465, 340);
			getJPanelList().add(getJSPItemList(), getJSPItemList().getName());
			getJPanelList().add(getJBUp(), getJBUp().getName());
			getJPanelList().add(getJBToTop(), getJBToTop().getName());
			getJPanelList().add(getJBDown(), getJBDown().getName());
			getJPanelList().add(getJBToBottom(), getJBToBottom().getName());

			Border etched = BorderFactory.createEtchedBorder();
			Border title = BorderFactory.createTitledBorder(etched,StringResource.getStringResource("miufo1001314"));  //"排序列表"
			getJPanelList().setBorder(title);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanelList;
}
/**
 * 返回 JSPItemList 特性值。
 * @return JScrollPane
 */
/* 警告：此方法将重新生成。 */
private JScrollPane getJSPItemList() {
    if (ivjJSPItemList == null) {
        try {
            m_oTable = new nc.ui.pub.beans.UITable();
            m_oTable.setAutoCreateColumnsFromModel(false);
            m_oTable.setModel(m_oTableModel);
            
            ivjJSPItemList = new UIScrollPane(m_oTable);
            ivjJSPItemList.setName("JSPItemList");
            ivjJSPItemList.setBounds(10, 15, 360, 315);
            for (int k = 0; k < m_oTableModel.getHead().length; k++) {
                TableCellRenderer renderer;
                DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer();
                renderer = textRenderer;

                TableCellEditor editor = null;

                if (k == ItemTableModel.SORT_TYPE) {
                    editor = new DefaultCellEditor(new UICheckBox());
                }
                TableColumn column = new TableColumn(k, 50, renderer, editor);
                m_oTable.addColumn(column);

            }
            JComboBox comboBox = new UIComboBox(); //
//            for (int i = 0; i < ItemTableModel.getSortType().length; i++)
//                comboBox.addItem(ItemTableModel.getSortType()[i]);
            comboBox.addItem(ItemTableModel.getSortTypeUIStr(OrderByFld.ORDER_NULL_STR));
            comboBox.addItem(ItemTableModel.getSortTypeUIStr(OrderByFld.ORDER_AESC_STR));
            comboBox.addItem(ItemTableModel.getSortTypeUIStr(OrderByFld.ORDER_DESC_STR));
          
            TableColumnModel tcm = m_oTable.getColumnModel();
            TableColumn typetc = tcm.getColumn(ItemTableModel.SORT_TYPE);
            typetc.setCellEditor(new DefaultCellEditor(comboBox));
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJSPItemList;
}
/**
 * 返回 UfoDialogContentPane 特性值。
 * @return JPanel
 */
/* 警告：此方法将重新生成。 */
private JPanel getUfoDialogContentPane() {
	if (ivjUfoDialogContentPane == null) {
		try {
			ivjUfoDialogContentPane = new UIPanel();
			ivjUfoDialogContentPane.setName("UfoDialogContentPane");
			ivjUfoDialogContentPane.setLayout(null);
			getUfoDialogContentPane().add(getJBtnOK(), getJBtnOK().getName());
			getUfoDialogContentPane().add(getJBtnCancel(), getJBtnCancel().getName());
			getUfoDialogContentPane().add(getJPanelList(), getJPanelList().getName());
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
		setName("SetSortDefDlg");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 450);
		setResizable(false);
		setContentPane(getUfoDialogContentPane());
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	addHelp();
	// user code end
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-21 13:30:32)
 * @param fullItem java.util.Vector
 */
private void initTable(Vector fullItem) {
    if (fullItem == null) {
        return;
    }
    //根据动态区域的所有数据处理字段集合和数据处理定义，初始化表格
    OrderByFld[] orderByFlds = m_oDataProcessDef.getOrderByFlds();
    //当前存在的数据处理字段Map
    int iCurSize = fullItem.size();
    HashMap mapFullItem = new HashMap(iCurSize);
    Iterator iteratorFullItem = fullItem.iterator();
    while(iteratorFullItem.hasNext()){
  		FieldMap fieldMap = (FieldMap)iteratorFullItem.next();
  		mapFullItem.put(fieldMap.getMapName(),fieldMap.getMapName());
  }
    //已有的排序字段的排序方式和排序位置定义
    HashMap mapOrderByFld = new HashMap();
    Vector vecOrderFlds = new Vector();
    int iSize = orderByFlds != null ? orderByFlds.length : 0;
    for (int i = 0; i < iSize; i++) {
    	if(mapFullItem.containsKey(orderByFlds[i].getMapName())){
    		//只有当前存在的才能被列出进行定义
	        mapOrderByFld.put(orderByFlds[i].getMapName(), orderByFlds[i]);
	        vecOrderFlds.add(orderByFlds[i]);
    	}
    }

    //添加没有被定义过的数据处理字段
    String strMapName = null;
    OrderByFld tempOrderByFld = null;
    FieldMap fieldMap = null;
    for (int i = 0; i < iCurSize; i++) {
        fieldMap = (FieldMap) fullItem.get(i);
        strMapName = fieldMap.getMapName();
        if (!mapOrderByFld.containsKey(strMapName)) {
            tempOrderByFld = new OrderByFld(fieldMap);
            vecOrderFlds.add(tempOrderByFld);
        }
    }
        this.m_oTableModel = new ItemTableModel(vecOrderFlds);
        m_oTableModel.setHead(new String[] { 
        		StringResource.getStringResource("miufo1001277"),   //"指标/关键字"
				StringResource.getStringResource("miufo1001315")  //"排序方式"
				 });
}
/**
 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	//try {
		//SetSortDefDlg aSetSortDefDlg;
		////aSetSortDefDlg = new SetSortDefDlg();
		//aSetSortDefDlg.setModal(true);
		//aSetSortDefDlg.addWindowListener(new java.awt.event.WindowAdapter() {
			//public void windowClosing(java.awt.event.WindowEvent e) {
				//System.exit(0);
			//};
		//});
		//aSetSortDefDlg.show();
		//java.awt.Insets insets = aSetSortDefDlg.getInsets();
		//aSetSortDefDlg.setSize(aSetSortDefDlg.getWidth() + insets.left + insets.right, aSetSortDefDlg.getHeight() + insets.top + insets.bottom);
		//aSetSortDefDlg.setVisible(true);
	//} catch (Throwable exception) {
		//System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main() :Exception");
		//exception.printStackTrace(System.out);
	//}
}
/**
 * 此处插入方法描述。
 * 
 * 创建日期：(2003-8-12 14:48:16)
 * @author：刘良萍
 * @param newDataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
 */
void setDataProcessDef(DefaultDataProcessDef newDataProcessDef) {
	m_oDataProcessDef = newDataProcessDef;
}
}


