package nc.ui.eh.trade.z0207501;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.pub.PubTool;
import nc.vo.eh.trade.z0207010.BackbillBVO;
import nc.vo.eh.trade.z0207010.BackbillVO;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;
/**
 * 说明:上下游单据界面 (销售退回->销售发票)
 * @author wb
 * 2008-7-24 21:28:15
 * 
 */
public class ZA12TOZA14DLG extends AbstractBTOBDLG {
	
      
    public ZA12TOZA14DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
        
    }

    @Override
	public String[] getBillVos(){
        return new String[] { 
        		PubBillVO.class.getName(),
        		BackbillVO.class.getName(),
	            BackbillBVO.class.getName() 
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0207010","销售退回单");
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
        strWherePart.append("' ");
        strWherePart.append(" and vbillstatus=1");
        strWherePart.append(" and isnull(ykfp_flag,'N')='N'");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 王明
     * 2007-11-1 下午03:14:05
     */
    @Override
	@SuppressWarnings("deprecation")
	public void onOk() {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
//            LadingbillVO lad=(LadingbillVO) retBillVo.getParentVO();  
//            lad.setInvoiceno("ZA10");
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }              
//            if(retBillVos.length>1||retBillVos.length==0){
//            	 JOptionPane.showMessageDialog(null,"只能有一张提货通知单!","提示",JOptionPane.INFORMATION_MESSAGE); 
//                 return;          	
//            }
            String [] pk_backbills=new String [retBillVos.length];
            HashMap hm=new HashMap();
            HashMap hmbillno=new HashMap();
            for(int i=0;i<retBillVos.length;i++){
            	BackbillVO vo=(BackbillVO) retBillVos[i].getParentVO();
            	String pk_cubasdoc=vo.getPk_cubasdoc()==null?"":vo.getPk_cubasdoc().toString();
            	String pk_backbill = vo.getPk_backbill()==null?"":vo.getPk_backbill().toString();
            	String billno=vo.getBillno()==null?"":vo.getBillno().toString();
            	hmbillno.put(pk_backbill, billno);
            	pk_backbills[i]=pk_backbill;
            	hm.put(pk_cubasdoc, pk_cubasdoc);
            }
            if(hm.size()>=2){
            	 JOptionPane.showMessageDialog(null,"请选择同一客户开发票!","提示",JOptionPane.INFORMATION_MESSAGE); 
            	 return;          	
            }
            String pk_ladingbill=PubTool.combinArrayToString(pk_backbills); 
            
            
            
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
            	BackbillBVO[] childs=(BackbillBVO[]) retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) { 
                	BackbillBVO bvo=childs[j];
                	String pk_backbill=bvo.getPk_backbill()==null?"":bvo.getPk_backbill().toString();
                	String billno=hmbillno.get(pk_backbill)==null?"":hmbillno.get(pk_backbill).toString();
                	bvo.setDef_4(billno);
                    list.add(childs[j]);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);  
            BackbillVO VO=(BackbillVO) retBillVo.getParentVO();
            VO.setPk_backbill(pk_ladingbill);
            retBillVo.setParentVO(VO);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}