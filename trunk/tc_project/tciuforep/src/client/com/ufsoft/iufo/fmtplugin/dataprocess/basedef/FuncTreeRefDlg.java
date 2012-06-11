package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.fmtplugin.businessquery.AbsRepToolTreeRefDlg;
import com.ufsoft.iufo.fmtplugin.businessquery.RepToolTreeRefHelper;
import com.ufsoft.iufo.fmtplugin.businessquery.TreeRefBaseNode;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;

/**
 * 此处插入类型描述。
 * 创建日期：(2004-4-2 10:42:17)
 * @author：刘良萍
 */
public class FuncTreeRefDlg extends AbsRepToolTreeRefDlg{
    /**
     * UnitTreeRefDlg 构造子注解。
     */
    public FuncTreeRefDlg(){
        super();
    }

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     */
    public FuncTreeRefDlg(java.awt.Container parent){
        super(parent);
    }

    /**
     * UnitTreeRefDlg 构造子注解。
     * @param parent java.awt.Container
     * @param refVO ValueObject
     */
    public FuncTreeRefDlg(java.awt.Container parent, ValueObject refVO){
        super(parent, refVO);
    }

    /**
     * 返回对话框标题
     * @return String
     */
    public String getTitle(){
        return StringResource.getStringResource("miufo1001279");  //"函数树参照"
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
        try{
            rootNode = RepToolTreeRefHelper.createFuncTreeRefRoot();
        } catch(Exception ex){
            //无函数
            rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1001280"), "");
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001281"),this);  //"系统装载函数信息可能发生错误！"
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


