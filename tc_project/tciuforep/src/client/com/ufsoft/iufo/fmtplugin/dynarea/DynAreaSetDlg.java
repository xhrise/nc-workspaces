package com.ufsoft.iufo.fmtplugin.dynarea;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.AreaSelectDlg;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;
/**
 @update
    取消动态区域最大单元数目的限制
 @end
 @update
    增加帮助的链接
 @end
 * 设置动态区域对话框。
 * 创建日期：(2001-1-16 18:31:06)
 * @author：岳益民
 * @update
 * 暂时屏蔽列向扩展功能
 * @end
 */
public class DynAreaSetDlg extends AreaSelectDlg implements ActionListener, FocusListener {
    /**
     * <code>serialVersionUID</code> 的注释
     */
    private static final long serialVersionUID = -4681468577745857338L;
    private JButton ivjJBCancel = null;
    private JButton ivjJBOK = null;
    private JLabel ivjJLabel1 = null;
    private JPanel ivjUfoDialogContentPane = null;
    private JButton ivjJBFold = null;
    private JButton ivjJBFoldArea = null;
    private JLabel ivjJLabel11 = null;
    private JTextField ivjJTFArea = null;
    private JTextField ivjJTFSelArea = null;

    private JRadioButton ivjJRBCol = null;
    private JRadioButton ivjJRBRow = null;

    private int FocusPlace=0;//0 焦点在TFArea
    private boolean autoFold = false; //标记是否是自动折行

    /**
     * 动态区域
     */
    private AreaPosition m_oArea = null;
    /**
     * 动态区域的方向
     */
    private int m_nDirection = DynAreaVO.DIRECTION_ROW;


/**
 * 此处插入方法描述。
 * 创建日期：(2002-4-22 10:30:42)
 * @param owner java.awt.Container
 */
public DynAreaSetDlg(Container owner,CellsModel cellsModel)
{
    super(owner,cellsModel);
    initialize();
}

/**
 * Invoked when an action occurs.
 */
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == ivjJBCancel) {
        this.setResult(this.ID_CANCEL);
    }
    if (e.getSource() == ivjJBOK || e.getSource() == ivjJTFArea) {
        //判断是否正确
        String strPos = getJTFArea().getText();

        if (strPos == null || strPos.equals("")) {
            showmessage(StringResource.getStringResource("miufo1000787"));  //"区域不能输入为空！"
            ivjJTFArea.requestFocus();
            ivjJTFArea.selectAll();
            return;
        }
        AreaPosition dynArea = null;
        try {
            dynArea = AreaPosition.getInstance(strPos);
        } catch (Exception ex) {
            showmessage(StringResource.getStringResource("miufo1001147")); //"区域名称不合法！"
            return;
        }

        //if ((dynArea.End.Col-dynArea.Start.Col+1)*(dynArea.End.Row-dynArea.Start.Row+1)>300){
            //showmessage("动态区域包含最大单元个数为300");
            //return;
        //}

        setArea(dynArea);
        //设置选中的方向
        if (getJRBRow().isSelected()) {
            m_nDirection = DynAreaVO.DIRECTION_ROW;
        } else if (getJRBCol().isSelected()) {
            m_nDirection = DynAreaVO.DIRECTION_COL;
        }
        setResult(this.ID_OK);
    }
    if (e.getSource() == ivjJBFoldArea) {
        FocusPlace = 0;
        autoFold = false;
        fold(true);
        return;
    }
    if (e.getSource() == ivjJBFold) {
        fold(false);
        return;
    }
}
/**
 * 此处插入方法说明。 创建日期：(2001-4-12 11:04:54)
 * @return String
 */
protected String getHelpID() {
    return "TM_Format_DynAreaSet";      
}
/**
 * “ESC”键按下之后，退出对话框所作处理。
 * 创建日期：(2000-11-30 15:51:01)
 * @return int
 */
public void closeDiolog() {
    setResult(ID_CANCEL);
}
/**
 * 此处插入方法说明。
 * 创建日期：(2000-12-27 12:48:01)
 * @param evt java.awt.event.FocusEvent
 */
public void focusGained(FocusEvent evt) {
    if (ivjJBFold.isVisible())
        return;
    if (evt.getSource().equals(ivjJTFArea)){
        FocusPlace=0;
    }else if (evt.getSource().equals(ivjJTFSelArea)){
        FocusPlace=0;
    }else{
        FocusPlace=-1;
    }
}
/**
 * 此处插入方法说明。
 * 创建日期：(2000-12-27 12:47:25)
 * @param evt java.awt.event.FocusEvent
 */
public void focusLost(FocusEvent evt) {
}
/**
 * 区域参照时，对窗口折叠或恢复。
 * 创建日期：(2001-1-15 9:29:52)
 * @param FoldType boolean
 */
private void fold(boolean FoldType) {
    setFold(FoldType);
    if(FoldType){
        ivjJTFSelArea.setVisible(true);
        ivjJBFold.setVisible(true);
        ivjJLabel1.setVisible(false);
        Rectangle r = getBounds();
        r.height=65;
        setBounds(r);
        ivjJTFSelArea.setText(ivjJTFArea.getText());
    }else{
        ivjJTFSelArea.setVisible(false);
        ivjJBFold.setVisible(false);
        ivjJLabel1.setVisible(true);
        Rectangle r = getBounds();
        r.height=208;
        setBounds(r);
        ivjJTFArea.setText(ivjJTFSelArea.getText());
        ivjJTFArea.requestFocus();
    }
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-27 10:29:07)
 * @return com.ufsoft.iuforeport.reporttool.pub.UfoArea
 */
public AreaPosition getArea() {
    return m_oArea;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-27 10:29:07)
 * @return int
 */
public int getDirection() {
    return m_nDirection;
}
/**
 * 返回 JBCancel 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBCancel() {
    if (ivjJBCancel == null) {
        try {
            ivjJBCancel = new nc.ui.pub.beans.UIButton();
            ivjJBCancel.setName("JBCancel");
    //      ivjJBCancel.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJBCancel.setText(StringResource.getStringResource("miufo1000274"));  //"取  消"
            ivjJBCancel.setBounds(117, 131, 75, 22);
            ivjJBCancel.setActionCommand("JBOK");
            // user code begin {1}
            ivjJBCancel.addActionListener(this);
            ivjJBCancel.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
            ivjJBCancel.addFocusListener(this);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBCancel;
}
/**
 * 返回 JBFold 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBFold() {
    if (ivjJBFold == null) {
        try {
            ivjJBFold = new nc.ui.pub.beans.UIButton();
            ivjJBFold.setName("JBFold");
            ivjJBFold.setText("");
            ivjJBFold.setBounds(185, 4, 18, 17);
            ivjJBFold.setVisible(false);
            // user code begin {1}
            ivjJBFold.addFocusListener(this);
            ivjJBFold.addActionListener(this);
            ivjJBFold.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
            ivjJBFold.setIcon(ResConst.getImageIcon("reportcore/down.gif"));
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBFold;
}
/**
 * 返回 JBFoldArea 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBFoldArea() {
    if (ivjJBFoldArea == null) {
        try {
            ivjJBFoldArea = new nc.ui.pub.beans.UIButton();
            ivjJBFoldArea.setName("JBFoldArea");
            ivjJBFoldArea.setText("");
            ivjJBFoldArea.setBounds(185, 87, 18, 18);
            // user code begin {1}
            ivjJBFoldArea.addActionListener(this);
            ivjJBFoldArea.addFocusListener(this);
            ivjJBFoldArea.setIcon(ResConst.getImageIcon("reportcore/up.gif"));
            ivjJBFoldArea.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBFoldArea;
}
/**
 * 返回 JBOK 特性值。
 * @return javax.swing.JButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JButton getJBOK() {
    if (ivjJBOK == null) {
        try {
            ivjJBOK = new nc.ui.pub.beans.UIButton();
            ivjJBOK.setName("JBOK");
    //      ivjJBOK.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJBOK.setText(StringResource.getStringResource("miufo1000790"));  //"确  定"
            ivjJBOK.setBounds(18, 130, 75, 22);
            ivjJBOK.setActionCommand("JBOK");
            // user code begin {1}
            ivjJBOK.addFocusListener(this);
            ivjJBOK.addActionListener(this);
            ivjJBOK.registerKeyboardAction(this,KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false),JComponent.WHEN_FOCUSED);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJBOK;
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
    //      ivjJLabel1.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJLabel1.setText(StringResource.getStringResource("miufo1001148"));  //"方向："
            ivjJLabel1.setBounds(10, 13, 123, 16);
            ivjJLabel1.setForeground(java.awt.Color.black);
            ivjJLabel1.setRequestFocusEnabled(false);
            // user code begin {1}
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
 * 返回 JLabel11 特性值。
 * @return javax.swing.JLabel
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JLabel getJLabel11() {
    if (ivjJLabel11 == null) {
        try {
            ivjJLabel11 = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
            ivjJLabel11.setName("JLabel11");
    //      ivjJLabel11.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJLabel11.setText(StringResource.getStringResource("miufo1000792"));  //"请选择区域："
            ivjJLabel11.setBounds(10, 65, 123, 16);
            ivjJLabel11.setForeground(java.awt.Color.black);
            ivjJLabel11.setRequestFocusEnabled(false);
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJLabel11;
}
/**
 * 返回 JRBCol 特性值。
 * @return javax.swing.JRadioButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JRadioButton getJRBCol() {
    if (ivjJRBCol == null) {
        try {
            ivjJRBCol = new UIRadioButton();
            ivjJRBCol.setName("JRBCol");
    //      ivjJRBCol.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJRBCol.setText(StringResource.getStringResource("miufopublic366"));  //"纵向"
            ivjJRBCol.setBounds(90, 30, 72, 24);
            // user code begin {1}
            //数据处理列扩展功能添加，不再禁用marked by liulp,2004-03-17
//          ivjJRBCol.setEnabled(false);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJRBCol;
}
/**
 * 返回 JRBRow 特性值。
 * @return javax.swing.JRadioButton
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JRadioButton getJRBRow() {
    if (ivjJRBRow == null) {
        try {
            ivjJRBRow = new UIRadioButton();
            ivjJRBRow.setName("JRBRow");
    //      ivjJRBRow.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJRBRow.setText(StringResource.getStringResource("miufopublic367"));  //"横向"
            ivjJRBRow.setBounds(20, 30, 72, 24);
            // user code begin {1}
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJRBRow;
}
/**
 * 返回 JTFArea 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTextField getJTFArea() {
    if (ivjJTFArea == null) {
        try {
            ivjJTFArea = new UITextField();
            ivjJTFArea.setName("JTFArea");
    //      ivjJTFArea.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJTFArea.setBounds(10, 86, 174, 20);
            // user code begin {1}
            ivjJTFArea.addActionListener(this);
            ivjJTFArea.addFocusListener(this);
            ivjJTFArea.setEditable(false);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJTFArea;
}
/**
 * 返回 JTFSelArea 特性值。
 * @return javax.swing.JTextField
 */
/* 警告：此方法将重新生成。 */
private javax.swing.JTextField getJTFSelArea() {
    if (ivjJTFSelArea == null) {
        try {
            ivjJTFSelArea = new UITextField();
            ivjJTFSelArea.setName("JTFSelArea");
    //      ivjJTFSelArea.setFont(new java.awt.Font("dialog", 0, 14));
            ivjJTFSelArea.setBounds(10, 2, 174, 20);
            ivjJTFSelArea.setVisible(false);
            // user code begin {1}
            ivjJTFSelArea.addActionListener(this);
            ivjJTFSelArea.addFocusListener(this);
            ivjJTFSelArea.setEditable(false);
            // user code end
        } catch (java.lang.Throwable ivjExc) {
            // user code begin {2}
            // user code end
            handleException(ivjExc);
        }
    }
    return ivjJTFSelArea;
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
            ivjUfoDialogContentPane.setRequestFocusEnabled(false);
            //getUfoDialogContentPane().add(getJTFMeasurePK(), getJTFMeasurePK().getName());
            //getUfoDialogContentPane().add(getJBRefMeasure(), getJBRefMeasure().getName());
            getUfoDialogContentPane().add(getJRBRow(), getJRBRow().getName());
            getUfoDialogContentPane().add(getJRBCol(), getJRBCol().getName());
            getUfoDialogContentPane().add(getJLabel1(), getJLabel1().getName());
            getUfoDialogContentPane().add(getJBOK(), getJBOK().getName());
            getUfoDialogContentPane().add(getJBCancel(), getJBCancel().getName());
            getUfoDialogContentPane().add(getJTFArea(), getJTFArea().getName());
            getUfoDialogContentPane().add(getJLabel11(), getJLabel11().getName());
            getUfoDialogContentPane().add(getJBFoldArea(), getJBFoldArea().getName());
            getUfoDialogContentPane().add(getJTFSelArea(), getJTFSelArea().getName());
            getUfoDialogContentPane().add(getJBFold(), getJBFold().getName());
            // user code begin {1}
            ButtonGroup rbg = new ButtonGroup();
            rbg.add(getJRBCol());
            rbg.add(getJRBRow());
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
        setName("CellFuncFilterDlg");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(StringResource.getStringResource("miufo1001149"));  //"选择区域"
        setSize(214, 208);
        setModal(true);
        setResizable(false);
        setContentPane(getUfoDialogContentPane());
    } catch (java.lang.Throwable ivjExc) {
        handleException(ivjExc);
    }
    // user code begin {2}
    setModal(false);
    setLocationRelativeTo(this);
    ivjJBOK.setNextFocusableComponent(ivjJBCancel);
    ivjJBCancel.setNextFocusableComponent(getJTFArea());
    getJTFArea().setNextFocusableComponent(ivjJBOK);
    getJRBRow().setSelected(true);
    // user code end
}
///**
// * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
// * @param args java.lang.String[]
// */
//public static void main(java.lang.String[] args) {
//    try {
//        AddAliasDlg aAddAliasDlg;
//        aAddAliasDlg = new AddAliasDlg();
//        aAddAliasDlg.setModal(true);
//        aAddAliasDlg.addWindowListener(new java.awt.event.WindowAdapter() {
//            public void windowClosing(java.awt.event.WindowEvent e) {
//                System.exit(0);
//            };
//        });
//        //whtao aAddAliasDlg.show();
//        aAddAliasDlg.showModal();
//        java.awt.Insets insets = aAddAliasDlg.getInsets();
//        aAddAliasDlg.setSize(aAddAliasDlg.getWidth() + insets.left + insets.right, aAddAliasDlg.getHeight() + insets.top + insets.bottom);
////      aAddAliasDlg.setVisible(true);
//    } catch (Throwable exception) {
//        System.err.println(StringResource.getStringResource("miufo1001150"));  //"nc.ui.iufo.pub.UfoDialog 的 main() 中发生异常"
//        exception.printStackTrace(System.out);
//    }
//}
private void myDispatchEvent(AWTEvent event) {
    if (event instanceof ActiveEvent) {
        ((ActiveEvent) event).dispatch();
    } else if (event.getSource() instanceof Component) {
        ((Component) event.getSource()).dispatchEvent(event);
    } else if (event.getSource() instanceof MenuComponent) {
        ((MenuComponent) event.getSource()).dispatchEvent(event);
    }
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-27 10:29:07)
 * @param newArea com.ufsoft.iuforeport.reporttool.pub.UfoArea
 */
public void setArea(AreaPosition newArea) {
    m_oArea = newArea;
    getJTFArea().setText(m_oArea.toString());
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-8-27 10:29:07)
 * @param newDirection int
 */
public void setDirection(int newDirection) {
    m_nDirection = newDirection;
    if (m_nDirection != -1) {
        if (m_nDirection == DynAreaVO.DIRECTION_COL) {
            getJRBCol().setSelected(true);
        } else if (m_nDirection == DynAreaVO.DIRECTION_ROW) {
            getJRBRow().setSelected(true);
        }
    } else {
        getJRBRow().setSelected(true);
    }
}
/**
 * 设置方向只读属性。
 * 创建日期：(2003-8-27 13:53:47)
 */
public void setDirectionRead() {
    getJRBCol().setEnabled(false);
    getJRBRow().setEnabled(false);
}
/**
 * 实现区域参照。 创建日期：(2001-1-4 15:01:43)
 * ERROR 以下代码逻辑不清晰，且有无用的代码，添加注释。
 */
public void show() {
    super.show();
    EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
    while (true) {
        try {
            AWTEvent evt = eq.getNextEvent();
            if ((evt.getSource() == this)
                    && (evt.getID() == WindowEvent.WINDOW_CLOSING)) {
                dispose();
                break;
            } else if ((evt.getSource() instanceof UfoReport)
                    && (evt.getID() == WindowEvent.WINDOW_CLOSING)) {
                Toolkit.getDefaultToolkit().beep();
            } else if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
                MouseEvent mevt = (MouseEvent) evt;
                //右键
                if ((mevt.getModifiers() & InputEvent.BUTTON3_MASK) == 0) {
                    Point p = getPointToScreen(mevt); //转换到相对于屏幕的坐标
                    if (getBounds().contains(p)) {
                        //如果点击对话框内消息正常处理
                        myDispatchEvent(mevt);
                    } else if (mevt.getSource() instanceof RootPaneContainer) {
                        if (true) {
                            Rectangle mm = super.getViewToScreenSize(mevt);
                            if (mm.contains(p)) {
                                if (!getIsFold()) {
                                    autoFold = true;
                                    fold(true);
                                }
                                myDispatchEvent(mevt);
                                String strAreaName = getViewAreaName(mevt); //得到视图选中区域

                                ivjJTFSelArea.setText(strAreaName);
                            } else
                                Toolkit.getDefaultToolkit().beep();
                        } else
                            Toolkit.getDefaultToolkit().beep();
                    } else if (mevt.getSource() instanceof JFrame) {
                        JFrame jf = (JFrame) mevt.getSource();
                        if (jf.getTitle().indexOf(
                                StringResource
                                        .getStringResource("miufo1000651")) > 0) //"帮助"
                        {
                            myDispatchEvent(mevt);
                        } else
                            Toolkit.getDefaultToolkit().beep();
                    } else
                        Toolkit.getDefaultToolkit().beep();
                } else
                    Toolkit.getDefaultToolkit().beep();

            } else if (evt.getID() == MouseEvent.MOUSE_DRAGGED) {
                if (evt.getSource() instanceof RootPaneContainer) {
                    myDispatchEvent(evt);

                    String strAreaName = getViewAreaName(evt);
                    ivjJTFSelArea.setText(strAreaName);
                }
                if (evt.getSource() == this)
                    myDispatchEvent(evt);
            } else if (evt.getID() == MouseEvent.MOUSE_RELEASED) {
                MouseEvent mevt = (MouseEvent) evt;
                //右键
                if ((mevt.getModifiers() & InputEvent.BUTTON3_MASK) == 0) {
                    //鼠标抬起，是自动折叠则自动恢复
                    if (evt.getSource() instanceof Component) {
                        myDispatchEvent(evt);
                        if (getIsFold() && autoFold)
                            fold(false);
                    }
                }
            } else {
                if (evt.getSource() instanceof Component) {
                    myDispatchEvent(evt);
                } else if (evt.getSource() instanceof MenuComponent) {
                    myDispatchEvent(evt);
                } else if (evt instanceof ActiveEvent) {
                    ((ActiveEvent) evt).dispatch();
                }
            }
        } catch (InterruptedException ie) {
        }
    }

}

	/**
	 * 错误信息。 创建日期：(00-11-28 14:58:24)
	 */
	private void showmessage(String errs) {
		UfoPublic.sendErrorMessage(errs, this, null);
	}
}


