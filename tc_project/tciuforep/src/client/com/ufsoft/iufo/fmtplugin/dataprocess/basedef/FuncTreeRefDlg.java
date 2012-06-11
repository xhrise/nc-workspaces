package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.fmtplugin.businessquery.AbsRepToolTreeRefDlg;
import com.ufsoft.iufo.fmtplugin.businessquery.RepToolTreeRefHelper;
import com.ufsoft.iufo.fmtplugin.businessquery.TreeRefBaseNode;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;

/**
 * �˴���������������
 * �������ڣ�(2004-4-2 10:42:17)
 * @author������Ƽ
 */
public class FuncTreeRefDlg extends AbsRepToolTreeRefDlg{
    /**
     * UnitTreeRefDlg ������ע�⡣
     */
    public FuncTreeRefDlg(){
        super();
    }

    /**
     * UnitTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     */
    public FuncTreeRefDlg(java.awt.Container parent){
        super(parent);
    }

    /**
     * UnitTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     * @param refVO ValueObject
     */
    public FuncTreeRefDlg(java.awt.Container parent, ValueObject refVO){
        super(parent, refVO);
    }

    /**
     * ���ضԻ������
     * @return String
     */
    public String getTitle(){
        return StringResource.getStringResource("miufo1001279");  //"����������"
    }

    /**
     * �õ�������ģ�͡�
     *
     * �������ڣ�(2004-4-2 10:25:02)
     * @author������Ƽ
     * @return javax.swing.tree.DefaultTreeModel
     */
    protected javax.swing.tree.DefaultMutableTreeNode createTreeRefRoot(){
        TreeRefBaseNode rootNode = null;
        try{
            rootNode = RepToolTreeRefHelper.createFuncTreeRefRoot();
        } catch(Exception ex){
            //�޺���
            rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1001280"), "");
            UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1001281"),this);  //"ϵͳװ�غ�����Ϣ���ܷ�������"
        }
        return rootNode;
    }

    /**
     * �õ�����ֵ,��Ҫ����ʵ�֡�
     *
     * �������ڣ�(2004-4-02 10:16:13)
     * @author������Ƽ
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


