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
     * 得到参照树的根节点（含所有子节点）
     * @param rootUnitVO UnitInfoVO 单位树的根单位
     * @return UnitTreeRefNode
     */
    public static UnitTreeRefNode createUnitTreeRefRoot(UnitInfoVO rootUnitVO,String strOrgPK){
        if(rootUnitVO == null){
            return null;
        }
        //得到树的根节点
        UnitTreeRefNode rootNode = new UnitTreeRefNode(rootUnitVO,strOrgPK);
        //创建树上的所有子节点
        creatUnitInfoTree(rootUnitVO.getPK(), rootNode, 0, true,strOrgPK);

        return rootNode;
    }
    /**

    * 得到参照树的根节点（含所有子节点）
    * @param rootUnitVO UnitInfoVO 单位树的根单位
    * @return TreeRefBaseNode
    */
   public static TreeRefBaseNode createFuncTreeRefRoot(){
       //得到树的根节点
       TreeRefBaseNode rootNode = new TreeRefBaseNode(StringResource.getStringResource("miufo1000177"), "");
       //创建函数树上的所有子节点（非第归）
       createFuncInfoTree(rootNode);

       return rootNode;
   }

    /**
     * 编码信息树根节点（含所有子节点信息）
     *
     * @param refCodeVO CodeVO
     * @return TreeRefBaseNode
     * @throws Exception
     */
    public static TreeRefBaseNode createCodeTreeRefRoot(CodeVO refCodeVO) throws Exception{
        TreeRefBaseNode rootNode = null;
        if(refCodeVO != null){
            String strReturnType = CodeVO.ReturnType_Code;
            //生成编码内容树的根节点，不允许参照
            String strReturnValue = "";
            String strDisplayText = refCodeVO.getName();
            rootNode = new TreeRefBaseNode(strDisplayText, strReturnValue);
            CodeCache codeCache = CacheProxy.getSingleton().getCodeCache();
            //得到编码的根编码信息
            CodeVO code = codeCache.findCodeByID(refCodeVO.getId());
            CodeInfoVO[] rootCodeInfo = codeCache.getRootCodeInfo(code);
            //循环添加编码信息
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
     * 生成函数信息树
     * @param rootNode
     */
    private static void createFuncInfoTree(TreeRefBaseNode rootNode){
        if(rootNode == null){
            return;
        }
        // 得到函数类型列表
        UfoSimpleObject[] cats = UfoFuncList.getCatList();
        if(cats == null){
            return;
        }
        int iLen = cats.length;
        for(int i = 0; i < iLen; i++){
            //得到类型对应图标（前缀）
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

            //得到指定类型的函数列表
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
     * 生成某一个根编码信息的编码信息子树
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
        //添加该层信息节点
        TreeRefBaseNode curInfoNode = new TreeRefBaseNode(codeInfo.getId() + " | " + codeInfo.getContent(), strVal);
        parentNode.add(curInfoNode);
        //第归添加子编码信息节点
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
     * 生成某一个指定单位根的单位树
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


