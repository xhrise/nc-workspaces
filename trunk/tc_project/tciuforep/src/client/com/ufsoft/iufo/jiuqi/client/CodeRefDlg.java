package com.ufsoft.iufo.jiuqi.client;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import com.ufsoft.iufo.jiuqi.pub.CodeInfo;
import com.ufsoft.iufo.jiuqi.pub.CodeRef;
import com.ufsoft.report.util.MultiLang;

/**
 * <p>Title: ����ӿڿͻ��˹ؼ���ֵ���ս�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CodeRefDlg extends JDialog {
    private JTree m_tree = null;
    private JScrollPane m_treePane = null;
    private String m_SelValue = null;
    //�������
    public class CodeTreeNode extends DefaultMutableTreeNode
    {
        private String m_strId = null; //����
        private String m_strContent = null;//����
        public CodeTreeNode(String strId, String strContent){
            m_strId = strId;
            m_strContent = strContent;
        }
        /**
         *
         * @return String
         */
        public String getDisplayText() {
            return m_strContent;
        }
        /**
         *
         * @return String
         */
        public String getReturnValue() {
            return m_strId;
        }
        /**
         *
         * @param newDisplayText String
         */
        public void setDisplayText(String newDisplayText) {
            m_strContent = newDisplayText;
        }
        /**
         *
         * @param newReturnValue String
         */
        public void setReturnValue(String newReturnValue) {
            m_strId = newReturnValue;
        }
        /**
         * @return String
         */
        public String toString() {
            return '('+m_strId+')'+m_strContent;
        }
    }
    /**
     * ������
     * @param parentDlg Dialog
     * @param codeRef CodeRef
     * @throws HeadlessException
     */
    public CodeRefDlg(Dialog parentDlg, CodeRef codeRef) throws Exception {
        super(parentDlg);
        setLocationRelativeTo(parentDlg);
        init(codeRef);
    }
    /**
     * ��ʼ������
     * @param codeRef CodeRef
     * @i18n uiuforep00100=�������
     */
    private void init(CodeRef codeRef)
    {
        CodeInfo rootCodeInfo = codeRef.getRootCodeInfo();
        setTitle(MultiLang.getString("uiuforep00100"));
        setSize(350,310);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getJTreePane(rootCodeInfo),BorderLayout.CENTER);
    }
    /**
     * �õ���������
     * @param rootCodeInfo CodeInfo
     * @return JScrollPane
     */
    private JScrollPane getJTreePane(CodeInfo rootCodeInfo)
    {
        m_treePane = new JScrollPane(getTree(rootCodeInfo));
        return m_treePane;
    }
    /**
     * ��ʶ����
     * @param rootCodeInfo CodeInfo
     * @return JTree
     */
    private JTree getTree(CodeInfo rootCodeInfo)
    {
        CodeTreeNode rootNode = new CodeTreeNode(rootCodeInfo.getId(),"");
        initTree(rootCodeInfo,rootNode);
        m_tree = new JTree(rootNode);
        //�������壬��ֹ��������
//        Font font = new Font("����",0,15);
//        m_tree.setFont(font);

        DefaultTreeSelectionModel selModel = new DefaultTreeSelectionModel();
        selModel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
        m_tree.setSelectionModel(selModel);
        m_tree.addMouseListener(new MouseAdapter()
                                {
                                    public void mouseClicked( MouseEvent event )
                                    {
                                        int[] row = m_tree.getSelectionRows();
                                        if(row == null ||row.length > 1 || row[0] == 0)
                                            return;
                                        TreePath selPath = m_tree.getSelectionPath();
                                        CodeTreeNode node = (CodeTreeNode)selPath.getLastPathComponent();
                                        if(!node.isRoot())
                                        {
                                            m_SelValue = node.getReturnValue();
                                            close();
                                        }
                                    }
                                });
        return m_tree;
    }
    /**
     * �رղ��ս���
     */
    public void close()
    {
        this.dispose();
    }
    /**
     * ����CodeInfo������
     * @param pCodeInfo CodeInfo
     * @param pNode CodeTreeNode
     */
    private void initTree(CodeInfo pCodeInfo,CodeTreeNode pNode)
    {
        if(pCodeInfo != null)
        {
            ArrayList arrayList = pCodeInfo.getChildren();
            CodeInfo child = null;
            CodeTreeNode cNode = null;
            int size = arrayList.size();
            if(arrayList != null && size > 0 )
            {
                for(int i = 0; i < size ; i++ )
                {
                    child = (CodeInfo)arrayList.get(i);
                    cNode = new CodeTreeNode(child.getId(), child.getContent());
                    initTree(child, cNode);
                    pNode.add(cNode);
                }
            }
        }
    }
    /**
     * �õ�ȫ���Ĳ���Map
     * key:CodeInfo.getContent()
     * value:CodeInfo.getId()
     * @param pCodeInfo CodeInfo
     * @param map HashMap
     */
    public static void loadALLCodeName(CodeInfo pCodeInfo,HashMap map)
    {
        if(pCodeInfo != null)
        {
            ArrayList arrayList = pCodeInfo.getChildren();
            CodeInfo child = null;
            int size = arrayList.size();
            if(arrayList != null && size > 0 )
            {
                for(int i = 0; i < size ; i++ )
                {
                    child = (CodeInfo)arrayList.get(i);
                    map.put(child.getId(), child.getContent());
                    loadALLCodeName(child, map);
                }
            }
        }

    }
    /**
     * �õ�����ֵ
     * @return String
     */
    public String getReturnValue()
    {
        return m_SelValue;
    }
}
 