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
 * �����߲������ĳ����ࡣ
 *
 * �������ڣ�(2004-4-02 10:20:38)
 * @author������Ƽ
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
     * ��ģ��
     */
    protected RepToolTreeRefModel m_oTreeRefModel = null;
    /**
     * ��λ��ѡ�е����ڵ�
     */
    protected DefaultMutableTreeNode m_oSelRefNode = null;
    /**
     * ��λ�����ڵ��Ӧ��VO����
     */
    protected ValueObject m_oRefVO = null;
    //���������ͣ���λ������������������
    public static final int TREE_REF_TYPE_UNIT = 1;
    public static final int TREE_REF_TYPE_CODE = 2;
    public static final int TREE_REF_TYPE_FUNC = 3;

    /**
     * UnitTreeRefDlg ������ע�⡣
     */
    public AbsRepToolTreeRefDlg(){
        super();
        initialize();
    }

    /**
     * UnitTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     */
    public AbsRepToolTreeRefDlg(java.awt.Container parent){
        super(parent);

        initialize();
    }

    /**
     * UnitTreeRefDlg ������ע�⡣
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
        //ȷ��
        if(e.getSource() == getJBtnOK()){
            TreePath curPath = ivjJTreeRef.getSelectionPath();
            m_oSelRefNode = (DefaultMutableTreeNode) (curPath.getLastPathComponent());
            setResult(UfoDialog.ID_OK);
            close();
        }
        //ȡ��
        else if(e.getSource() == getJBtnCancel()){
            setResult(UfoDialog.ID_CANCEL);
            close();
        }
    }

    /**
     * �õ�������ģ�͡�
     *
     * �������ڣ�(2004-4-2 10:25:02)
     * @author������Ƽ
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
     * �õ�������ģ�͡�
     *
     * �������ڣ�(2004-4-2 10:25:02)
     * @author������Ƽ
     * @return javax.swing.tree.DefaultTreeModel
     */
    protected abstract DefaultMutableTreeNode createTreeRefRoot();

    /**
     * ���� JBtnCancel ����ֵ��
     * @return javax.swing.JButton
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JBtnOK ����ֵ��
     * @return javax.swing.JButton
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JLabelUnitTree ����ֵ��
     * @return javax.swing.JLabel
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JLabel getJLabelUnitTree(){
        if(ivjJLabelUnitTree == null){
            try{
                ivjJLabelUnitTree = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLabelUnitTree.setName("JLabelUnitTree");
                ivjJLabelUnitTree.setText("RefUnitTree");
                ivjJLabelUnitTree.setBounds(10, 10, 45, 14);
                // user code begin {1}
                String strRefUnitTree = StringResource.getStringResource("miufo1001362");  //"������:"
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
     * ���� JScrollPane1 ����ֵ��
     * @return javax.swing.JScrollPane
     */
    /* ���棺�˷������������ɡ� */
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
     * ���� JTree1 ����ֵ��
     * @return javax.swing.JTree
     */
    /* ���棺�˷������������ɡ� */
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
     * �õ�����ֵ,��Ҫ����ʵ�֡�
     *
     * �������ڣ�(2004-4-02 10:16:13)
     * @author������Ƽ
     * @return java.lang.String
     * @param nType int
     */
    public abstract String getReturnValue(int nType);

    /**
     * ���� UfoDialogContentPane ����ֵ��
     * @return javax.swing.JPanel
     */
    /* ���棺�˷������������ɡ� */
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
     * ÿ�������׳��쳣ʱ������
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception){

        /* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
        // System.out.println("--------- δ��׽�����쳣 ---------");
        // exception.printStackTrace(System.out);
    }

    /**
     * ��ʼ���ࡣ
     */
    /* ���棺�˷������������ɡ� */
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
            strTitle = StringResource.getStringResource("miufo1001363");  //"������"
        }
        setTitle(strTitle);
        // user code end
    }

    /**
     * ��ʼ����λ��������
     * �������ڣ�(2003-9-17 14:49:49)
     * @author������Ƽ
     */
    private void initRefTree(){
        DefaultMutableTreeNode rootNode = createTreeRefRoot();
        m_oTreeRefModel = createTreeRefModel(rootNode);
        if(m_oTreeRefModel!=null)
        ivjJTreeRef.setModel(m_oTreeRefModel);
    }

    /**
     * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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


