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
 * <p>Title: 久其接口客户端关键字值参照界面类</p>
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
    //树结点类
    public class CodeTreeNode extends DefaultMutableTreeNode
    {
        private String m_strId = null; //编码
        private String m_strContent = null;//内容
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
     * 构造器
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
     * 初始化界面
     * @param codeRef CodeRef
     * @i18n uiuforep00100=编码参照
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
     * 得到树的容器
     * @param rootCodeInfo CodeInfo
     * @return JScrollPane
     */
    private JScrollPane getJTreePane(CodeInfo rootCodeInfo)
    {
        m_treePane = new JScrollPane(getTree(rootCodeInfo));
        return m_treePane;
    }
    /**
     * 初识化树
     * @param rootCodeInfo CodeInfo
     * @return JTree
     */
    private JTree getTree(CodeInfo rootCodeInfo)
    {
        CodeTreeNode rootNode = new CodeTreeNode(rootCodeInfo.getId(),"");
        initTree(rootCodeInfo,rootNode);
        m_tree = new JTree(rootNode);
        //设置字体，防止出现乱码
//        Font font = new Font("宋体",0,15);
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
     * 关闭参照界面
     */
    public void close()
    {
        this.dispose();
    }
    /**
     * 根据CodeInfo构造树
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
     * 得到全部的参照Map
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
     * 得到返回值
     * @return String
     */
    public String getReturnValue()
    {
        return m_SelValue;
    }
}
 