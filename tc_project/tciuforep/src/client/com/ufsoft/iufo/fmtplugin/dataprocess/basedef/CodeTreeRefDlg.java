package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

import nc.vo.iufo.code.CodeVO;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.fmtplugin.businessquery.AbsRepToolTreeRefDlg;
import com.ufsoft.iufo.fmtplugin.businessquery.RepToolTreeRefHelper;
import com.ufsoft.iufo.fmtplugin.businessquery.TreeRefBaseNode;
import com.ufsoft.iufo.resource.StringResource;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-4-2 10:42:17)
 * @author：刘良萍
 */
public class CodeTreeRefDlg extends AbsRepToolTreeRefDlg{
    /**
     * UnitTreeRefDlg 构造子注解。
     */
    public CodeTreeRefDlg(){
        super();
    }

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     */
    public CodeTreeRefDlg(java.awt.Container parent){
        super(parent);
    }

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     * @param refVO ValueObject
     */
    public CodeTreeRefDlg(java.awt.Container parent, ValueObject refVO){
        super(parent, refVO);
    }
    /**
     * 返回对话框标题
     * @return String
     */
    public String getTitle(){
        return StringResource.getStringResource("miufo1001253");  //"编码树参照"
    }
    /**
     * 得到参照树模型。
     *
     * 创建日期：(2004-4-2 10:25:02)
     * @author：刘良萍
     * @return javax.swing.tree.DefaultTreeModel
     */
    protected javax.swing.tree.DefaultMutableTreeNode createTreeRefRoot(){
        TreeRefBaseNode rootNode = null;
        if(m_oRefVO != null && m_oRefVO instanceof CodeVO){
            CodeVO codeVO = (CodeVO)m_oRefVO;
            //创建树的参照根节点
            try{
                rootNode = RepToolTreeRefHelper.createCodeTreeRefRoot(codeVO);
            }catch(Exception ex){

            }
        } else{
            //无编码VO
            rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1001254"), "");
        }

        return rootNode;
    }

    /**
     * 得到返回值,需要子类实现。
     *
     * 创建日期：(2004-4-02 10:16:13)
     * @author：刘良萍
     * @return java.lang.String
     * @param nType int
     */
    public String getReturnValue(int nType){
        String strReturnValue = null;
        if(m_oSelRefNode != null){
            TreeRefBaseNode refNode = (TreeRefBaseNode)m_oSelRefNode;
                strReturnValue = refNode.getReturnValue();
        }
        return strReturnValue;
    }
}


