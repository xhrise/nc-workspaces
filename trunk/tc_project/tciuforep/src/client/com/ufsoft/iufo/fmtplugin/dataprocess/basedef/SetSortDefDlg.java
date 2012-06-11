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
     * ���ݴ��������
     */
    private DefaultDataProcessDef m_oDataProcessDef = null;
    /**
     *	�Ի������
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
    *	�ڲ�����
    */
    private JTable m_oTable = null;
    private ItemTableModel m_oTableModel = null;
/**
 * �˴����뷽��������
 * 
 * �������ڣ�(2003-8-22 14:16:23)
 * @author������Ƽ
 * @param parent java.awt.Container
 * @param vecAllDynAreaDPFld java.util.Vector - Ԫ��ΪDataProcessFld
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
 * SetSortDefDlg ������ע�⡣
 */
public SetSortDefDlg(Vector fullItem) {
	super();
	initTable(fullItem);
	initialize();
}
/**
 * SetSortDefDlg ������ע�⡣
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
            //�������ݴ����������
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
 * ���Ӱ�����
 * 
 * �������ڣ�(2003-10-31 09:56:54)
 * �����ߣ�����Ƽ
 */
private void addHelp() {
    javax.help.HelpBroker hb = ResConst.getHelpBroker();
    if (hb == null)
        return;
    hb.enableHelpKey(getContentPane(), "TM_Data_Process_Order", null);

}
/**
 * �˴����뷽��������
 * 
 * �������ڣ�(2003-8-12 14:48:16)
 * @author������Ƽ
 * @return com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
 */
public DefaultDataProcessDef getDataProcessDef() {	
	return m_oDataProcessDef;
}
/**
 * ���� JBDown ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
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
			String strSeltoDown = StringResource.getStringResource("miufo1001262");  //"������"
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
 * ���� JBtnCancel ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
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
 * ���� JBtnOK ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
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
 * ���� JBToBottom ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
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
			String strSelToBottom  =StringResource.getStringResource("miufo1001312");  //"�����"
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
 * ���� JBToTop ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
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
			String strSelToTop = StringResource.getStringResource("miufo1001313");  //"����ǰ"
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
 * ���� JBUp ����ֵ��
 * @return JButton
 */
/* ���棺�˷������������ɡ� */
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
			String strSelToUp = StringResource.getStringResource("miufo1001263");  //"������"
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
 * ���� JPanelList ����ֵ��
 * @return JPanel
 */
/* ���棺�˷������������ɡ� */
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
			Border title = BorderFactory.createTitledBorder(etched,StringResource.getStringResource("miufo1001314"));  //"�����б�"
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
 * ���� JSPItemList ����ֵ��
 * @return JScrollPane
 */
/* ���棺�˷������������ɡ� */
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
 * ���� UfoDialogContentPane ����ֵ��
 * @return JPanel
 */
/* ���棺�˷������������ɡ� */
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
 * �˴����뷽��������
 * �������ڣ�(2003-8-21 13:30:32)
 * @param fullItem java.util.Vector
 */
private void initTable(Vector fullItem) {
    if (fullItem == null) {
        return;
    }
    //���ݶ�̬������������ݴ����ֶμ��Ϻ����ݴ����壬��ʼ�����
    OrderByFld[] orderByFlds = m_oDataProcessDef.getOrderByFlds();
    //��ǰ���ڵ����ݴ����ֶ�Map
    int iCurSize = fullItem.size();
    HashMap mapFullItem = new HashMap(iCurSize);
    Iterator iteratorFullItem = fullItem.iterator();
    while(iteratorFullItem.hasNext()){
  		FieldMap fieldMap = (FieldMap)iteratorFullItem.next();
  		mapFullItem.put(fieldMap.getMapName(),fieldMap.getMapName());
  }
    //���е������ֶε�����ʽ������λ�ö���
    HashMap mapOrderByFld = new HashMap();
    Vector vecOrderFlds = new Vector();
    int iSize = orderByFlds != null ? orderByFlds.length : 0;
    for (int i = 0; i < iSize; i++) {
    	if(mapFullItem.containsKey(orderByFlds[i].getMapName())){
    		//ֻ�е�ǰ���ڵĲ��ܱ��г����ж���
	        mapOrderByFld.put(orderByFlds[i].getMapName(), orderByFlds[i]);
	        vecOrderFlds.add(orderByFlds[i]);
    	}
    }

    //���û�б�����������ݴ����ֶ�
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
        		StringResource.getStringResource("miufo1001277"),   //"ָ��/�ؼ���"
				StringResource.getStringResource("miufo1001315")  //"����ʽ"
				 });
}
/**
 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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
 * �˴����뷽��������
 * 
 * �������ڣ�(2003-8-12 14:48:16)
 * @author������Ƽ
 * @param newDataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
 */
void setDataProcessDef(DefaultDataProcessDef newDataProcessDef) {
	m_oDataProcessDef = newDataProcessDef;
}
}


