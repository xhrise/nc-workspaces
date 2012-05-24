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
import nc.vo.eh.trade.z0206510.LadingbillBVO;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;
/**
 * 说明:上下游单据界面 提货通知->销售发票
 * @author 王明
 * 2007-9-29 下午04:02:28
 */
public class ZA10TOZA14DLG extends AbstractBTOBDLG {
	
      
    public ZA10TOZA14DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		LadingbillVO.class.getName(),
        		LadingbillBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0206510","提货通知单");
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
        strWherePart.append(" and isnull(used_flag,'N')='N'");
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
            LadingbillVO lad=(LadingbillVO) retBillVo.getParentVO();  
            lad.setInvoiceno("ZA10");
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }              
//            if(retBillVos.length>1||retBillVos.length==0){
//            	 JOptionPane.showMessageDialog(null,"只能有一张提货通知单!","提示",JOptionPane.INFORMATION_MESSAGE); 
//                 return;          	
//            }
            String [] pk_ladingbills=new String [retBillVos.length];
            HashMap hm=new HashMap();
            HashMap hmbillno=new HashMap();
            for(int i=0;i<retBillVos.length;i++){
            	LadingbillVO vo=(LadingbillVO) retBillVos[i].getParentVO();
            	String pk_cubasdoc=vo.getPk_cubasdoc()==null?"":vo.getPk_cubasdoc().toString();
            	String pk_ladbill=vo.getPk_ladingbill()==null?"":vo.getPk_ladingbill().toString();
            	String billno=vo.getBillno()==null?"":vo.getBillno().toString();
            	hmbillno.put(pk_ladbill, billno);
            	pk_ladingbills[i]=vo.getPk_ladingbill()==null?"":vo.getPk_ladingbill().toString();
            	hm.put(pk_cubasdoc, pk_cubasdoc);
            }
            if(hm.size()>=2){
            	 JOptionPane.showMessageDialog(null,"请选择同一客户开发票!","提示",JOptionPane.INFORMATION_MESSAGE); 
            	 return;          	
            }
            String pk_ladingbill=PubTool.combinArrayToString(pk_ladingbills); 
            
            
            
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
            	LadingbillBVO[] childs=(LadingbillBVO[]) retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) { 
                	LadingbillBVO bvo=childs[j];
                	String pk_ladbill=bvo.getPk_ladingbill()==null?"":bvo.getPk_ladingbill().toString();
                	String billno=hmbillno.get(pk_ladbill)==null?"":hmbillno.get(pk_ladbill).toString();
                	bvo.setDef_4(billno);
                    list.add(childs[j]);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);  
            LadingbillVO VO=(LadingbillVO) retBillVo.getParentVO();
            VO.setPk_ladingbill(pk_ladingbill);
            retBillVo.setParentVO(VO);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}