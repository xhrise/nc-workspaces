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
 增加帮助链接
@end
@update 2003-10-23 13:21 liulp
 错误对话框应该是模态的
@end
* 编码列表及编码值（树）参照。
*
* 创建日期：(2003-9-17 20:00:38)
* @author：刘良萍
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
     * 选中的结果值
     */
    private String m_strReturnValue = null;
    /**
     * 系统所有编码的列表
     */
    private CodeRefTableModel m_oCodeRefTableModel = null;
    /**
     * 编码信息树模型
     */
    private CodeTreeRefModel m_oCodeTreeRefModel = null;
    /**
     * 编码信息树选中的树节点
     */
    private TreeRefBaseNode m_oSelRefNode = null;
    /**
     * CodeListTreeRefDlg 构造子注解。
     */
    public CodeListTreeRefDlg(){
        super();
        initialize();
    }

    /**
     * CodeListTreeRefDlg 构造子注解。
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
        //确定
        if(e.getSource() == getJBtnOK()){
            //得到编码树上当前选中的节点
            TreePath curPath = ivjJTree1.getSelectionPath();
            if(curPath != null){
                m_oSelRefNode = (TreeRefBaseNode) (curPath.getLastPathComponent());
            } else{
                m_oSelRefNode = null;
            }
            //必须选中编码树上可选的节点
            if(m_oSelRefNode == null){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001370"),this);  //"必须选中编码树上的一个节点！"
                return;
            }
            //选中的不是编码信息节点
            else if(m_oSelRefNode.getReturnValue() == null || m_oSelRefNode.getReturnValue().length() <= 0){
                return;
            }

            //设置返回值
            m_strReturnValue = m_oSelRefNode.getReturnValue();
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
     * 增加帮助。
     *
     * 创建日期：(2003-10-31 09:56:54)
     * 创建者：刘良萍
     */
    private void addHelp(){
        javax.help.HelpBroker hb = ResConst.getHelpBroker();
        if(hb == null){
            return;
        }
        hb.enableHelpKey(getContentPane(), "TM_Data_Query_Add", null);

    }

    /**
     * 处理编码表鼠标点击事件。
     *
     * 创建日期：(2003-9-20 21:31:36)
     * @author：刘良萍
     * @param e java.awt.event.MouseEvent
     */
    private void doSetCodeTree(java.awt.event.MouseEvent e){
        if(e.getSource() == codeListTable){
            try{
                setCodeTree();
            } catch(Exception ex){
                UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001371"),this);  //"该编码的编码信息树有错！"
            }
        }
    }

    /**
     * 得到编码表格。
     *
     * 创建日期：(2003-9-17 20:48:50)
     * @author：刘良萍
     * @return javax.swing.JTable
     */
    private javax.swing.JTable getCodeListTable(){
        if(codeListTable == null){

            //生成编码表格模型
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
     * 得到系统所有的编码，并转换成编码参照对象数组。
     *
     * 创建日期：(2003-9-17 21:11:28)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.query.CodeRefVO[]
     */
    private CodeRefVO[] getCodeRefVOs(){
        //得到系统的所有编码
        CodeCache codeCache = CacheProxy.getSingleton().getCodeCache();
        nc.vo.iufo.code.CodeVO[] codeVOs = codeCache.getAllCode();
        //转换成编码参照对象数组
        int iLen = codeVOs != null ? codeVOs.length : 0;
                   CodeRefVO[] codeRefVOs = new CodeRefVO[iLen];
        for(int i = 0; i < iLen; i++){
            codeRefVOs[i] = new CodeRefVO(codeVOs[i]);

            //#构造形如 x-x-x-x 格式的编码规则串
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
            // 如果没有编码规则
            if(strRule.length() <= 0){
                strRule = StringResource.getStringResource(StringResource.UNDEFINE);

                //#设置编码规则串
            }
            codeRefVOs[i].setCodeRuleStr(strRule);
        }

        return codeRefVOs;
    }

    /**
     * 返回 JBtnCancel 特性值。
     * @return javax.swing.JButton
     */
    /* 警告：此方法将重新生成。 */
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
     * 返回 JLabelCodeRef 特性值。
     * @return javax.swing.JLabel
     */
    /* 警告：此方法将重新生成。 */
    private javax.swing.JLabel getJLabelCodeRef(){
        if(ivjJLabelCodeRef == null){
            try{
                ivjJLabelCodeRef = new com.ufsoft.iuforeport.reporttool.dialog.IUFOLabel();
                ivjJLabelCodeRef.setName("JLabelCodeRef");
                ivjJLabelCodeRef.setText("CodeRef:");
                ivjJLabelCodeRef.setBounds(25, 13, 65, 14);
                // user code begin {1}
                String strCodeRef = StringResource.getStringResource("miufo1001372");  //"编码参照:"
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
     * 返回 JPanel1 特性值。
     * @return javax.swing.JPanel
     */
    /* 警告：此方法将重新生成。 */
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
     * 返回 JScrollPane1 特性值。
     * @return javax.swing.JScrollPane
     */
    /* 警告：此方法将重新生成。 */
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
     * 返回 JTree1 特性值。
     * @return javax.swing.JTree
     */
    /* 警告：此方法将重新生成。 */
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
     * 得到选中的编码值。
     *
     * 创建日期：(2003-9-17 20:03:57)
     * @author：刘良萍
     * @return java.lang.String
     */
    public String getReturnValue(){
        return m_strReturnValue;
    }

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
     * 每当部件抛出异常时被调用
     * @param exception java.lang.Throwable
     */
    private void handleException(java.lang.Throwable exception){

        /* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
        // System.out.println("--------- 未捕捉到的异常 ---------");
        // exception.printStackTrace(System.out);
    }

    /**
     * 初始化编码表模型。
     *
     * 创建日期：(2003-9-17 21:08:58)
     * @author：刘良萍
     */
    private void initCodeListTable(){
        m_oCodeRefTableModel = new CodeRefTableModel(getCodeRefVOs());
        codeListTable.setModel(m_oCodeRefTableModel);

        //设置表属性
        codeListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        codeListTable.sizeColumnsToFit( -1);
        codeListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * 初始化类。
     */
    /* 警告：此方法将重新生成。 */
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
        setTitle(StringResource.getStringResource("miufo1000770"));  //"指标参照"
        try{
            setCodeTree();
        } catch(Exception ex){
            //com.ufsoft.iuforeport.reporttool.toolkit.MessageBox.ErrMessageBox("得到编码信息树有错！");
AppDebug.debug(ex);//@devTools             ex.printStackTrace(System.out);
        }
        addHelp();
        // user code end
    }

    /**
     * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
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
     * 根据编码列表选中项目，生成相应的编码内容树。
     *
     * 创建日期：(2003-9-18 9:41:36)
     * @author：刘良萍
     * @throws Exception
     */
    private void setCodeTree() throws Exception{
        int iSelIndex = codeListTable.getSelectedRow();
        if(iSelIndex < 0){
            iSelIndex = 0;
        }
        CodeRefVO codeRefVO = (CodeRefVO)m_oCodeRefTableModel.getVO(iSelIndex);
        //#得到选中索引的编码信息
        if(codeRefVO != null){
            String strReturnType = CodeVO.ReturnType_Code;
            //编码VO改变后刷新右编码信息树
            String strDisplayText = codeRefVO.getCodeVO().getName();
            if(m_oCodeTreeRefModel == null ||
                (m_oCodeTreeRefModel != null &&
                !strDisplayText.equals( ( (TreeRefBaseNode)m_oCodeTreeRefModel.getRoot()).getDisplayText()))){
                //得到编码信息树根节点（含所有子节点信息）
                TreeRefBaseNode rootNode = RepToolTreeRefHelper.createCodeTreeRefRoot(codeRefVO.getCodeVO());
                if(m_oCodeTreeRefModel == null){
                    m_oCodeTreeRefModel = new CodeTreeRefModel(rootNode);
                    ivjJTree1.setModel(m_oCodeTreeRefModel);
                } else{
                    m_oCodeTreeRefModel.setRoot(rootNode);
                }
            }
        }
        //#只有iSelIndex=0可能因为编码列表为空得不到codeRefVO
        else{
            TreeRefBaseNode rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1001254"), "");
            m_oCodeTreeRefModel = new CodeTreeRefModel(rootNode);
            ivjJTree1.setModel(m_oCodeTreeRefModel);
        }

    }
}


