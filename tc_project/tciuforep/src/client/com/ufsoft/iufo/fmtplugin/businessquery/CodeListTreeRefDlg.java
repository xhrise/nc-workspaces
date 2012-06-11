package com.ufsoft.iufo.fmtplugin.businessquery;
import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import nc.pub.iufo.cache.base.CodeCache;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.vo.iufo.code.CodeRuleVO;
import nc.vo.iufo.code.CodeVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.util.UfoPublic;
/**
@update 2003-10-31 09:58 liulp
 ���Ӱ�������
@end
@update 2003-10-23 13:21 liulp
 ����Ի���Ӧ����ģ̬��
@end
* �����б�����ֵ���������ա�
*
* �������ڣ�(2003-9-17 20:00:38)
* @author������Ƽ
*/
public class CodeListTreeRefDlg extends com.ufsoft.report.dialog.UfoDialog implements java.awt.event.
    ActionListener, java.awt.event.MouseListener{
    private JButton ivjJBtnCancel = null;
    private JButton ivjJBtnOK = null;
    private JLabel ivjJLabelCodeRef = null;
    private JPanel ivjJPanel1 = null;
    private JScrollPane ivjJScrollPane1 = null;
    private JTree ivjJTree1 = null;
    private JPanel ivjUfoDialogContentPane = null;
    private JTable codeListTable = null;
    /**
     * ѡ�еĽ��ֵ
     */
    private String m_strReturnValue = null;
    /**
     * ϵͳ���б�����б�
     */
    private CodeRefTableModel m_oCodeRefTableModel = null;
    /**
     * ������Ϣ��ģ��
     */
    private CodeTreeRefModel m_oCodeTreeRefModel = null;
    /**
     * ������Ϣ��ѡ�е����ڵ�
     */
    private TreeRefBaseNode m_oSelRefNode = null;
    /**
     * CodeListTreeRefDlg ������ע�⡣
     */
    public CodeListTreeRefDlg(){
        super();
        initialize();
    }

    /**
     * CodeListTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     */
    public CodeListTreeRefDlg(java.awt.Container parent){
        super(parent);

        initialize();
    }
    /**
     * Invoked when an action occurs.
     * @param e java.awt.event.ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent e){
        //ȷ��
        if(e.getSource() == getJBtnOK()){
            //�õ��������ϵ�ǰѡ�еĽڵ�
            TreePath curPath = ivjJTree1.getSelectionPath();
            if(curPath != null){
                m_oSelRefNode = (TreeRefBaseNode) (curPath.getLastPathComponent());
            } else{
                m_oSelRefNode = null;
            }
            //����ѡ�б������Ͽ�ѡ�Ľڵ�
            if(m_oSelRefNode == null){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001370"),this);  //"����ѡ�б������ϵ�һ���ڵ㣡"
                return;
            }
            //ѡ�еĲ��Ǳ�����Ϣ�ڵ�
            else if(m_oSelRefNode.getReturnValue() == null || m_oSelRefNode.getReturnValue().length() <= 0){
                return;
            }

            //���÷���ֵ
            m_strReturnValue = m_oSelRefNode.getReturnValue();
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
     * ���Ӱ�����
     *
     * �������ڣ�(2003-10-31 09:56:54)
     * �����ߣ�����Ƽ
     */
    private void addHelp(){
        javax.help.HelpBroker hb = ResConst.getHelpBroker();
        if(hb == null){
            return;
        }
        hb.enableHelpKey(getContentPane(), "TM_Data_Query_Add", null);

    }

    /**
     * ��������������¼���
     *
     * �������ڣ�(2003-9-20 21:31:36)
     * @author������Ƽ
     * @param e java.awt.event.MouseEvent
     */
    private void doSetCodeTree(java.awt.event.MouseEvent e){
        if(e.getSource() == codeListTable){
            try{
                setCodeTree();
            } catch(Exception ex){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001371"),this);  //"�ñ���ı�����Ϣ���д�"
            }
        }
    }

    /**
     * �õ�������
     *
     * �������ڣ�(2003-9-17 20:48:50)
     * @author������Ƽ
     * @return javax.swing.JTable
     */
    private javax.swing.JTable getCodeListTable(){
        if(codeListTable == null){

            //���ɱ�����ģ��
            codeListTable = new nc.ui.pub.beans.UITable();
            codeListTable.setAutoCreateColumnsFromModel(false);
            codeListTable.addMouseListener(this);
            initCodeListTable();

            int nColCount = CodeRefTableModel.COUNT_OF_COLUMNS;
            for(int k = 0; k < nColCount; k++){
                TableCellRenderer renderer = new DefaultTableCellRenderer();
                TableCellEditor editor = new DefaultCellEditor(new JTextField());

                TableColumn column = new TableColumn(k, 80, renderer, editor);
                codeListTable.addColumn(column);

            }
 //           codeListTable.getTableHeader().setFont(new Font("dialog", 0, 12));
 //           codeListTable.setFont(new Font("dialog", 0, 12));

            return codeListTable;
        }

        return null;
    }

    /**
     * �õ�ϵͳ���еı��룬��ת���ɱ�����ն������顣
     *
     * �������ڣ�(2003-9-17 21:11:28)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.query.CodeRefVO[]
     */
    private CodeRefVO[] getCodeRefVOs(){
        //�õ�ϵͳ�����б���
        CodeCache codeCache = CacheProxy.getSingleton().getCodeCache();
        nc.vo.iufo.code.CodeVO[] codeVOs = codeCache.getAllCode();
        //ת���ɱ�����ն�������
        int iLen = codeVOs != null ? codeVOs.length : 0;
                   CodeRefVO[] codeRefVOs = new CodeRefVO[iLen];
        for(int i = 0; i < iLen; i++){
            codeRefVOs[i] = new CodeRefVO(codeVOs[i]);

            //#�������� x-x-x-x ��ʽ�ı������
            CodeRuleVO[] vRule = codeCache.getAllCodeRule(codeVOs[i]);
            String strRule = new String("");
            if(vRule != null){
                for(int j = 0; j < vRule.length; j++){
                    strRule += "" + vRule[j].getLen();
                    if(j < vRule.length - 1){
                        strRule += "-";
                    }
                }
            }
            // ���û�б������
            if(strRule.length() <= 0){
                strRule = StringResource.getStringResource(StringResource.UNDEFINE);

                //#���ñ������
            }
            codeRefVOs[i].setCodeRuleStr(strRule);
        }

        return codeRefVOs;
    }

    /**
     * ���� JBtnCancel ����ֵ��
     * @return javax.swing.JButton
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JButton getJBtnCancel(){
        if(ivjJBtnCancel == null){
            try{
                ivjJBtnCancel = new nc.ui.pub.beans.UIButton();
                ivjJBtnCancel.setName("JBtnCancel");
                ivjJBtnCancel.setText("Cancel");
                ivjJBtnCancel.setBounds(407, 293, 75, 22);
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
                ivjJBtnOK.setBounds(286, 294, 75, 22);
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
     * ���� JLabelCodeRef ����ֵ��
     * @return javax.swing.JLabel
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JLabel getJLabelCodeRef(){
        if(ivjJLabelCodeRef == null){
            try{
                ivjJLabelCodeRef = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLabelCodeRef.setName("JLabelCodeRef");
                ivjJLabelCodeRef.setText("CodeRef:");
                ivjJLabelCodeRef.setBounds(25, 13, 65, 14);
                // user code begin {1}
                String strCodeRef = StringResource.getStringResource("miufo1001372");  //"�������:"
                ivjJLabelCodeRef.setText(strCodeRef);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJLabelCodeRef;
    }

    /**
     * ���� JPanel1 ����ֵ��
     * @return javax.swing.JPanel
     */
    /* ���棺�˷������������ɡ� */
    private javax.swing.JPanel getJPanel1(){
        if(ivjJPanel1 == null){
            try{
                ivjJPanel1 = new UIPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(null);
                ivjJPanel1.setBounds(26, 31, 223, 257);
                // user code begin {1}
                JScrollPane ps1 = new UIScrollPane(getCodeListTable());
                ivjJPanel1.setLayout(new BorderLayout());
                ivjJPanel1.add(ps1, BorderLayout.CENTER);
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
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
                ivjJScrollPane1.setBounds(263, 30, 254, 260);
                getJScrollPane1().setViewportView(getJTree1());
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
    private javax.swing.JTree getJTree1(){
        if(ivjJTree1 == null){
            try{
                ivjJTree1 = new UITree();
                ivjJTree1.setName("JTree1");
                ivjJTree1.setBounds(0, 0, 78, 72);
                // user code begin {1}
                setCodeTree();
                // user code end
            } catch(java.lang.Throwable ivjExc){
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJTree1;
    }

    /**
     * �õ�ѡ�еı���ֵ��
     *
     * �������ڣ�(2003-9-17 20:03:57)
     * @author������Ƽ
     * @return java.lang.String
     */
    public String getReturnValue(){
        return m_strReturnValue;
    }

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
                getUfoDialogContentPane().add(getJLabelCodeRef(), getJLabelCodeRef().getName());
                getUfoDialogContentPane().add(getJPanel1(), getJPanel1().getName());
                getUfoDialogContentPane().add(getJScrollPane1(), getJScrollPane1().getName());
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
     * ��ʼ�������ģ�͡�
     *
     * �������ڣ�(2003-9-17 21:08:58)
     * @author������Ƽ
     */
    private void initCodeListTable(){
        m_oCodeRefTableModel = new CodeRefTableModel(getCodeRefVOs());
        codeListTable.setModel(m_oCodeRefTableModel);

        //���ñ�����
        codeListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        codeListTable.sizeColumnsToFit( -1);
        codeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * ��ʼ���ࡣ
     */
    /* ���棺�˷������������ɡ� */
    private void initialize(){
        try{
            // user code begin {1}
            // user code end
            setName("CodeListTreeRefDlg");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(541, 360);
            setContentPane(getUfoDialogContentPane());
        } catch(java.lang.Throwable ivjExc){
            handleException(ivjExc);
        }
        // user code begin {2}
        setTitle(StringResource.getStringResource("miufo1000770"));  //"ָ�����"
        try{
            setCodeTree();
        } catch(Exception ex){
            //com.ufsoft.iuforeport.reporttool.toolkit.MessageBox.ErrMessageBox("�õ�������Ϣ���д�");
AppDebug.debug(ex);//@devTools             ex.printStackTrace(System.out);
        }
        addHelp();
        // user code end
    }

    /**
     * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args){
        try{
            CodeListTreeRefDlg aCodeListTreeRefDlg;
            aCodeListTreeRefDlg = new CodeListTreeRefDlg();
            aCodeListTreeRefDlg.setModal(true);
            aCodeListTreeRefDlg.addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosing(java.awt.event.WindowEvent e){
                    System.exit(0);
                };
            });
            aCodeListTreeRefDlg.show();
            java.awt.Insets insets = aCodeListTreeRefDlg.getInsets();
            aCodeListTreeRefDlg.setSize(aCodeListTreeRefDlg.getWidth() + insets.left + insets.right,
                                        aCodeListTreeRefDlg.getHeight() + insets.top + insets.bottom);
            aCodeListTreeRefDlg.setVisible(true);
        } catch(Throwable exception){
            System.err.println("com.ufsoft.iuforeport.reporttool.pub.UfoDialog 's main():Exception");
AppDebug.debug(exception);//@devTools             exception.printStackTrace(System.out);
        }
    }

    /**
     * Invoked when the mouse has been clicked on a component.
     * @param e java.awt.event.MouseEvent
     */
    public void mouseClicked(java.awt.event.MouseEvent e){
        //doSetCodeTree(e);
    }

    /**
     * Invoked when the mouse enters a component.
     * @param e java.awt.event.MouseEvent
     */
    public void mouseEntered(java.awt.event.MouseEvent e){}

    /**
     * Invoked when the mouse exits a component.
     * @param e java.awt.event.MouseEvent
     */
    public void mouseExited(java.awt.event.MouseEvent e){}

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param e java.awt.event.MouseEvent
     */
    public void mousePressed(java.awt.event.MouseEvent e){}

    /**
     * Invoked when a mouse button has been released on a component.
     * @param e java.awt.event.MouseEvent
     */
    public void mouseReleased(java.awt.event.MouseEvent e){
        doSetCodeTree(e);
    }

    /**
     * ���ݱ����б�ѡ����Ŀ��������Ӧ�ı�����������
     *
     * �������ڣ�(2003-9-18 9:41:36)
     * @author������Ƽ
     * @throws Exception
     */
    private void setCodeTree() throws Exception{
        int iSelIndex = codeListTable.getSelectedRow();
        if(iSelIndex < 0){
            iSelIndex = 0;
        }
        CodeRefVO codeRefVO = (CodeRefVO)m_oCodeRefTableModel.getVO(iSelIndex);
        //#�õ�ѡ�������ı�����Ϣ
        if(codeRefVO != null){
            String strReturnType = CodeVO.ReturnType_Code;
            //����VO�ı��ˢ���ұ�����Ϣ��
            String strDisplayText = codeRefVO.getCodeVO().getName();
            if(m_oCodeTreeRefModel == null ||
                (m_oCodeTreeRefModel != null &&
                !strDisplayText.equals( ( (TreeRefBaseNode)m_oCodeTreeRefModel.getRoot()).getDisplayText()))){
                //�õ�������Ϣ�����ڵ㣨�������ӽڵ���Ϣ��
                TreeRefBaseNode rootNode = RepToolTreeRefHelper.createCodeTreeRefRoot(codeRefVO.getCodeVO());
                if(m_oCodeTreeRefModel == null){
                    m_oCodeTreeRefModel = new CodeTreeRefModel(rootNode);
                    ivjJTree1.setModel(m_oCodeTreeRefModel);
                } else{
                    m_oCodeTreeRefModel.setRoot(rootNode);
                }
            }
        }
        //#ֻ��iSelIndex=0������Ϊ�����б�Ϊ�յò���codeRefVO
        else{
            TreeRefBaseNode rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1001254"), "");
            m_oCodeTreeRefModel = new CodeTreeRefModel(rootNode);
            ivjJTree1.setModel(m_oCodeTreeRefModel);
        }

    }
}


