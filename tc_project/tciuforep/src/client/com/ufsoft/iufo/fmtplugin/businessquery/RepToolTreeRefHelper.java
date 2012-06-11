package com.ufsoft.iufo.fmtplugin.businessquery;

import java.util.Vector;

import nc.pub.iufo.cache.base.CodeCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.vo.iufo.code.CodeInfoVO;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.script.function.UfoFuncList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RepToolTreeRefHelper{
    // Suppresses default constructor, ensuring non-instantiability.
    private RepToolTreeRefHelper(){
    }

    /**
     * �õ��������ĸ��ڵ㣨�������ӽڵ㣩
     * @param rootUnitVO UnitInfoVO ��λ���ĸ���λ
     * @return UnitTreeRefNode
     */
    public static UnitTreeRefNode createUnitTreeRefRoot(UnitInfoVO rootUnitVO,String strOrgPK){
        if(rootUnitVO == null){
            return null;
        }
        //�õ����ĸ��ڵ�
        UnitTreeRefNode rootNode = new UnitTreeRefNode(rootUnitVO,strOrgPK);
        //�������ϵ������ӽڵ�
        creatUnitInfoTree(rootUnitVO.getPK(), rootNode, 0, true,strOrgPK);

        return rootNode;
    }
    /**

    * �õ��������ĸ��ڵ㣨�������ӽڵ㣩
    * @param rootUnitVO UnitInfoVO ��λ���ĸ���λ
    * @return TreeRefBaseNode
    */
   public static TreeRefBaseNode createFuncTreeRefRoot(){
       //�õ����ĸ��ڵ�
       TreeRefBaseNode rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1000177"), "");
       //�����������ϵ������ӽڵ㣨�ǵڹ飩
       createFuncInfoTree(rootNode);

       return rootNode;
   }

    /**
     * ������Ϣ�����ڵ㣨�������ӽڵ���Ϣ��
     *
     * @param refCodeVO CodeVO
     * @return TreeRefBaseNode
     * @throws Exception
     */
    public static TreeRefBaseNode createCodeTreeRefRoot(CodeVO refCodeVO) throws Exception{
        TreeRefBaseNode rootNode = null;
        if(refCodeVO != null){
            String strReturnType = CodeVO.ReturnType_Code;
            //���ɱ����������ĸ��ڵ㣬���������
            String strReturnValue = "";
            String strDisplayText = refCodeVO.getName();
            rootNode = new TreeRefBaseNode(strDisplayText, strReturnValue);
            CodeCache codeCache = CacheProxy.getSingleton().getCodeCache();
            //�õ�����ĸ�������Ϣ
            CodeVO code = codeCache.findCodeByID(refCodeVO.getId());
            CodeInfoVO[] rootCodeInfo = codeCache.getRootCodeInfo(code);
            //ѭ����ӱ�����Ϣ
            if(rootCodeInfo != null && rootCodeInfo.length > 0){
                int iRootCodeInfoLen = rootCodeInfo.length;
                for(int i = 0; i < iRootCodeInfoLen; i++){
                    createCodeInfoTree(rootNode, rootCodeInfo[i], "", strReturnType);
                }
            }
        }
        return rootNode;
    }

    /**
     * ���ɺ�����Ϣ��
     * @param rootNode
     */
    private static void createFuncInfoTree(TreeRefBaseNode rootNode){
        if(rootNode == null){
            return;
        }
        // �õ����������б�
        UfoSimpleObject[] cats = UfoFuncList.getCatList();
        if(cats == null){
            return;
        }
        int iLen = cats.length;
        for(int i = 0; i < iLen; i++){
            //�õ����Ͷ�Ӧͼ�꣨ǰ׺��
            String strPre = "";
            byte nCatID = (byte)cats[i].getID();
            if(UfoFuncList.DATEFUNC == nCatID){
                strPre = "f(d)";
            }else if(UfoFuncList.MATHFUNC == nCatID){
                strPre = "f(m)";
            }else if(UfoFuncList.STRFUNC == nCatID){
                strPre = "f(a)";
            }
            TreeRefBaseNode refNode = new TreeRefBaseNode(strPre+cats[i].getName(),"");
            rootNode.add(refNode);

            //�õ�ָ�����͵ĺ����б�
            UfoSimpleObject[] funcs = UfoFuncList.getFuncList(cats[i].getID());
            if(funcs != null){
                for(int j = 0; j < funcs.length; j++){
                    String strFmt = UfoFuncList.getFuncFmt(funcs[j].getID(), funcs[j].getName());
                    TreeRefBaseNode refSubNode = new TreeRefBaseNode(funcs[j].getName(),strFmt);
                    refNode.add(refSubNode);
                }
            }
        }

    }

    /**
     * ����ĳһ����������Ϣ�ı�����Ϣ����
     *
     * @param parentNode TreeRefBaseNode
     * @param codeInfo CodeInfoVO
     * @param strParentContent String
     * @param strReturnType String
     * @throws Exception
     */
    private static void createCodeInfoTree(TreeRefBaseNode parentNode, CodeInfoVO codeInfo, String strParentContent,
                                           String strReturnType) throws Exception{

        String strVal = "";
        if(strReturnType.equals(CodeVO.ReturnType_Code)){
            strVal = codeInfo.getId();
        } else if(strReturnType.equals(CodeVO.ReturnType_Content)){
            strVal = codeInfo.getContent();
        } else{
            strVal = strParentContent + codeInfo.getContent();
        }
        //��Ӹò���Ϣ�ڵ�
        TreeRefBaseNode curInfoNode = new TreeRefBaseNode(codeInfo.getId() + " | " + codeInfo.getContent(), strVal);
        parentNode.add(curInfoNode);
        //�ڹ�����ӱ�����Ϣ�ڵ�
        CodeInfoVO[] childInfoVOs = CacheProxy.getSingleton().getCodeCache().getChildCodeInfoVO(codeInfo.getCode_id(),
            codeInfo.getId());
        if(childInfoVOs != null){
            for(int i = 0; i < childInfoVOs.length; i++){
                if(childInfoVOs[i] != null){
                    createCodeInfoTree(curInfoNode, childInfoVOs[i], strVal, strReturnType);
                }
            }
        }

    }

    /**
     * ����ĳһ��ָ����λ���ĵ�λ��
     * @param unitPK String
     * @param node UnitTreeRefNode
     * @param treeIdType int
     * @param haveLeaf boolean
     */
    private static void creatUnitInfoTree(String unitPK, UnitTreeRefNode node, int treeIdType, boolean haveLeaf,String strOrgPK){
        UnitCache unitCache = CacheProxy.getSingleton().getUnitCache();
        Vector vecSubInfoVOs = unitCache.getSubUnits(unitPK,strOrgPK);

        if(vecSubInfoVOs != null){
            for(int i = 0; i < vecSubInfoVOs.size(); i++){
                UnitInfoVO subInfoVO = (UnitInfoVO)vecSubInfoVOs.get(i);

                if(haveLeaf || unitCache.getSubUnits(subInfoVO.getPK(),strOrgPK) != null){

                    UnitTreeRefNode subNode = null;
                    subNode = new UnitTreeRefNode(subInfoVO,strOrgPK);
                    node.add(subNode);

                    creatUnitInfoTree(subInfoVO.getPK(), subNode, treeIdType, haveLeaf,strOrgPK);
                }
            }
        }
    }

}


