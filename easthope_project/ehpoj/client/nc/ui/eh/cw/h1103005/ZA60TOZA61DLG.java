/**
 * @(#)ZA60TOZA61DLG.java	V3.1 2008-5-30
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.cw.h1103005;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.cw.h1102005.ArapQuerymnyBVO;
import nc.vo.eh.cw.h1102005.ArapQuerymnyVO;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;

public class ZA60TOZA61DLG extends AbstractBTOBDLG {
    static ArrayList al = null;

    public ZA60TOZA61DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
    }
    
    @Override
	protected String[] getBillVos() {
        return new String[] { PubBillVO.class.getName(),
                ArapQuerymnyVO.class.getName(),
                ArapQuerymnyBVO.class.getName()
               };
    }

    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H1102005","查款单");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }
    
    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (isnull(def_4,'N')='N' or def_4='') " +
                " and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='') ");//def_4为审批通过回写的标记和lock_flag关闭标记
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {
        //当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的查款单!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            //在上游的def_5里塞个0,带到下游的收款类型里面 
            ArapQuerymnyVO arvo = (ArapQuerymnyVO) retBillVo.getParentVO();
            arvo.setDef_5(0);
                    
             HashMap hm = new HashMap();  //存放相同的客户PK  add by zqy 2008-6-6 18:53:27
          
             for(int i=0;i<retBillVos.length;i++){
                 ArapQuerymnyVO avo = (ArapQuerymnyVO) retBillVos[i].getParentVO();
                 String pk_cubasdoc = avo.getPk_cubasdoc()==null?"":avo.getPk_cubasdoc().toString();                
                 
                 if(!hm.containsKey(pk_cubasdoc) && hm.size()!=0){
                     JOptionPane.showMessageDialog(null,"请选择相同的客商名称!","提示",JOptionPane.INFORMATION_MESSAGE);
                     return;
                 }else{
                     hm.put(pk_cubasdoc, pk_cubasdoc);
                 }                 
             }
             
//             al = new ArrayList();
//             for(int j=0;j<retBillVos.length;j++){
//                 String pk_querymny = retBillVos[j].getParentVO().getAttributeValue("pk_querymny").toString();
//                 al.add(pk_querymny);  //al存放多张上游单据的主PK              
//             }
             

             String pk_querymny = null;
             if(retBillVos.length>0){
                 int length = retBillVos.length;
                 String[] pk_querymnys = new String[length];
                 ArapQuerymnyVO avo = new ArapQuerymnyVO();
                 
                 for(int i=0;i<length;i++){
                     avo = (ArapQuerymnyVO) retBillVos[i].getParentVO();
                     pk_querymny = avo.getPk_querymny();
                     pk_querymnys[i] = pk_querymny;
                 }
                 pk_querymny=PubTools.combinArrayToString2(pk_querymnys);
                 retBillVo.getParentVO().setAttributeValue("pk_querymnys", pk_querymny);//将差款单中多条PK放到pk_querymnys中带到下游pk_querymnys中
             }
 
            
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {
                    list.add(childs[j]);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    }   

    
}

