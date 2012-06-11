package com.ufsoft.iufo.fmtplugin.businessquery;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
/**
 * 报表工具参照树的抽象父类。
 *
 * 创建日期：(2004-4-02 10:20:38)
 * @author：刘良萍
 */
public abstract class AbsRepToolTreeRefDlg extends UfoDialog implements java.awt.
    event.ActionListener{
    private javax.swing.JButton ivjJBtnCancel = null;
    private javax.swing.JButton ivjJBtnOK = null;
    private javax.swing.JLabel ivjJLabelUnitTree = null;
    private javax.swing.JPanel ivjUfoDialogContentPane = null;
    protected javax.swing.JTree ivjJTreeRef = null;
    private javax.swing.JScrollPane ivjJScrollPane1 = null;
    /**
     * 树模型
     */
    protected RepToolTreeRefModel m_oTreeRefModel = null;
    /**
     * 单位树选中的树节点
     */
    protected DefaultMutableTreeNode m_oSelRefNode = null;
    /**
     * 单位树根节点对应的VO对象
     */
    protected ValueObject m_oRefVO = null;
    //参照树类型：单位树，编码树，函数树
    public static final int TREE_REF_TYPE_UNIT = 1;
    public static final int TREE_REF_TYPE_CODE = 2;
    public static final int TREE_REF_TYPE_FUNC = 3;

    /**
     * UnitTreeRefDlg 构造子注解。
     */
    public AbsRepToolTreeRefDlg(){
        super();
        initialize();
    }

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     */
    public AbsRepToolTreeRefDlg(java.awt.Container parent){
        super(parent);

        initialize();
    }

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     * @param refVO ValueObject
     */
    public AbsRepToolTreeRefDlg(java.awt.Container parent, ValueObject refVO){
        super(parent);
        this.m_oRefVO = refVO;

        initialize();
    }

    /**
     * Invoked when an action occurs.
     * @param e java.awt.event.ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent e){
        //确定
        if(e.getSource() == getJBtnOK()){
            TreePath curPath = ivjJTreeRef.getSelectionPath();
            m_oSelRefNode = (DefaultMutableTreeNode) (curPath.getLastPathComponent());
            setResult(UfoDialog.ID_OK);
            close();
        }
        //取消
        else if(e.getSource() == getJBtnCancel()){
            setResult(UfoDialog.ID_CANCEL);
            close();
        }
    }

    /**
     * 得到参照树模型。
     *
     * 创建日期：(2004-4-2 10:25:02)
     * @author：刘良萍
     * @return RepToolTreeRefModel
     * @param rootNode DefaultMutableTreeNode
     */
    protected RepToolTreeRefModel createTreeRefModel(DefaultMutableTreeNode rootNode){
        if(rootNode !=null){
            return new RepToolTreeRefModel(rootNode);
        }
        return null;
//        if(rootNode instanceof UnitTreeRefNode){
//            return new RepToolTreeRefModel( (UnitTreeRefNode)rootNode);
//        } else{
//            return new RepToolTreeRefModel(rootNode);
//        }
    }

    /**
     * 得到参照树模型。
     *
     * 创建日期：(2004-4-2 10:25:02)
     * @author：刘良萍
     * @return javax.swing.tree.DefaultTreeModel
     */
    protected abstract DefaultMutableTreeNode createTreeRefRoot();

    /**
     * 返回 JBtnCancel 特性值。
     * @return javax.swing.JButton
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(167, 297, 75, 22);
                // user code begin {1}
                String strCancel = StringResource.getStringResource(StringResource.CANCEL);
                ivjJBtnCancel.setText(strCancel);
                ivjJBtnCancel.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
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
    private javax.swing.JButton getJBtnOK(){
        if(ivjJBtnOK == null){
            try{
                ivjJBtnOK = new nc.ui.pub.beans.UIButton();
                ivjJBtnOK.setName("JBtnOK");
                ivjJBtnOK.setText("OK");
                ivjJBtnOK.setBounds(41, 296, 75, 22);
                // user code begin {1}
                String strOK = StringResource.getStringResource(StringResource.OK);
                ivjJBtnOK.setText(strOK);
                ivjJBtnOK.addActionListener(this);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJBtnOK;
    }

    /**
     * 返回 JLabelUnitTree 特性值。
     * @return javax.swing.JLabel
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JLabel getJLabelUnitTree(){
        if(ivjJLabelUnitTree == null){
            try{
                ivjJLabelUnitTree = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLabelUnitTree.setName("JLabelUnitTree");
                ivjJLabelUnitTree.setText("RefUnitTree");
                ivjJLabelUnitTree.setBounds(10, 10, 45, 14);
                // user code begin {1}
                String strRefUnitTree = StringResource.getStringResource("miufo1001362");  //"参照树:"
                ivjJLabelUnitTree.setText(strRefUnitTree);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabelUnitTree;
    }

    /**
     * 返回 JScrollPane1 特性值。
     * @return javax.swing.JScrollPane
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JScrollPane getJScrollPane1(){
        if(ivjJScrollPane1 == null){
            try{
                ivjJScrollPane1 = new UIScrollPane();
                ivjJScrollPane1.setName("JScrollPane1");
                ivjJScrollPane1.setBounds(10, 27, 272, 257);
                getJScrollPane1().setViewportView(getJTreeRef());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane1;
    }

    /**
     * 返回 JTree1 特性值。
     * @return javax.swing.JTree
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JTree getJTreeRef(){
        if(ivjJTreeRef == null){
            try{
                ivjJTreeRef = new UITree();
                ivjJTreeRef.setName("JTreeUnit");
                ivjJTreeRef.setBounds(0, 0, 269, 284);
                // user code begin {1}
                initRefTree();
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTreeRef;
    }

    /**
     * 得到返回值,需要子类实现。
     *
     * 创建日期：(2004-4-02 10:16:13)
     * @author：刘良萍
     * @return java.lang.String
     * @param nType int
     */
    public abstract String getReturnValue(int nType);

    /**
     * 返回 UfoDialogContentPane 特性值。
     * @return javax.swing.JPanel
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JPanel getUfoDialogContentPane(){
        if(ivjUfoDialogContentPane == null){
            try{
                ivjUfoDialogContentPane = new UIPanel();
                ivjUfoDialogContentPane.setName("UfoDialogContentPane");
                ivjUfoDialogContentPane.setLayout(null);
                getUfoDialogContentPane().add(getJScrollPane1(), getJScrollPane1().getName());
                getUfoDialogContentPane().add(getJLabelUnitTree(), getJLabelUnitTree().getName());
                getUfoDialogContentPane().add(getJBtnOK(), getJBtnOK().getName());
                getUfoDialogContentPane().add(getJBtnCancel(), getJBtnCancel().getName());
                // user code begin {1}
                // user code end
            } catch(java.lang.Throwable ivjExc){
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
    private void handleException(java.lang.Throwable exception){

        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        // System.out.println("--------- 未捕捉到的异常 ---------");
        // exception.printStackTrace(System.out);
    }

    /**
     * 初始化类。
     */
    /* 警告：此方法将重新生成。 */
    private void initialize(){
        try{
            // user code begin {1}
            // user code end
            setName("UnitTreeRefDlg");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(295, 363);
            setContentPane(getUfoDialogContentPane());
        } catch(java.lang.Throwable ivjExc){
            handleException(ivjExc);
        }
        // user code begin {2}
        String strTitle = getTitle();
        if(strTitle == null){
            strTitle = StringResource.getStringResource("miufo1001363");  //"树参照"
        }
        setTitle(strTitle);
        // user code end
    }

    /**
     * 初始化单位参照树。
     * 创建日期：(2003-9-17 14:49:49)
     * @author：刘良萍
     */
    private void initRefTree(){
        DefaultMutableTreeNode rootNode = createTreeRefRoot();
        m_oTreeRefModel = createTreeRefModel(rootNode);
        if(m_oTreeRefModel!=null)
        ivjJTreeRef.setModel(m_oTreeRefModel);
    }

    /**
     * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args){
        //try {
        //UnitTreeRefDlg aUnitTreeRefDlg;
        //aUnitTreeRefDlg = new UnitTreeRefDlg();
        //aUnitTreeRefDlg.setModal(true);
        //aUnitTreeRefDlg.addWindowListener(new java.awt.event.WindowAdapter() {
        //public void windowClosing(java.awt.event.WindowEvent e) {
        //System.exit(0);
        //};
        //});
        //aUnitTreeRefDlg.show();
        //java.awt.Insets insets = aUnitTreeRefDlg.getInsets();
        //aUnitTreeRefDlg.setSize(aUnitTreeRefDlg.getWidth() + insets.left + insets.right, aUnitTreeRefDlg.getHeight() + insets.top + insets.bottom);
        //aUnitTreeRefDlg.setVisible(true);
        //} catch (Throwable exception) {
        //System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main():Exception");
        //exception.printStackTrace(System.out);
        //}
    }
}


