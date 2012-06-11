package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

import nc.vo.iufo.code.CodeVO;
import nc.vo.pub.ValueObject;

import com.ufsoft.iufo.fmtplugin.businessquery.AbsRepToolTreeRefDlg;
import com.ufsoft.iufo.fmtplugin.businessquery.RepToolTreeRefHelper;
import com.ufsoft.iufo.fmtplugin.businessquery.TreeRefBaseNode;
import com.ufsoft.iufo.resource.StringResource;
/**
 * �˴���������������
 * �������ڣ�(2004-4-2 10:42:17)
 * @author������Ƽ
 */
public class CodeTreeRefDlg extends AbsRepToolTreeRefDlg{
    /**
     * UnitTreeRefDlg ������ע�⡣
     */
    public CodeTreeRefDlg(){
        super();
    }

    /**
     * UnitTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     */
    public CodeTreeRefDlg(java.awt.Container parent){
        super(parent);
    }

    /**
     * UnitTreeRefDlg ������ע�⡣
     * @param parent java.awt.Container
     * @param refVO ValueObject
     */
    public CodeTreeRefDlg(java.awt.Container parent, ValueObject refVO){
        super(parent, refVO);
    }
    /**
     * ���ضԻ������
     * @return String
     */
    public String getTitle(){
        return StringResource.getStringResource("miufo1001253");  //"����������"
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
        if(m_oRefVO != null && m_oRefVO instanceof CodeVO){
            CodeVO codeVO = (CodeVO)m_oRefVO;
            //�������Ĳ��ո��ڵ�
            try{
                rootNode = RepToolTreeRefHelper.createCodeTreeRefRoot(codeVO);
            }catch(Exception ex){

            }
        } else{
            //�ޱ���VO
            rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1001254"), "");
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


