package nc.ui.eh.cw.h1101005;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150605.ArapStockinvoiceVO;
import nc.vo.eh.stock.h0150605.ArapStockinvoicesBVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照 (采购发票到付款单)
 * Edit: wangming
 * Date:2008-05-07
 */

public class ZA59TOZA57DLG extends AbstractBTOBDLG {
      
    public ZA59TOZA57DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		 ArapStockinvoiceVO.class.getName(),
                 ArapStockinvoicesBVO.class.getName()  
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0150605","采购发票");
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
        strWherePart.append("' and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 张起源
     * 2008-4-9
     */
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {
    	//当没有上游单据可以参照时 弹出提示
        if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
            super.close();
            JOptionPane.showMessageDialog(null,"没有可以参照的单据!","提示",JOptionPane.INFORMATION_MESSAGE); 
            return;
        }
     
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
//            
//            if(retBillVos.length>1||retBillVos.length==0){
//           	 	JOptionPane.showMessageDialog(null,"表头只能选择一条数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
//                return;          	
//           }
            
            ArrayList list=new ArrayList();
            UFDouble allMoney=new UFDouble();  //总金额
            ArapStockinvoiceVO parent=(ArapStockinvoiceVO) retBillVo.getParentVO();
            for(int i=0;i<retBillVos.length;i++){
            	ArapStockinvoicesBVO[] childs=(ArapStockinvoicesBVO[]) retBillVos[i].getChildrenVO();
            	
                for (int j = 0; j < childs.length; j++) {
                	UFDouble one = childs[j].getTaxinmony()==null?new UFDouble():childs[j].getTaxinmony();
                	allMoney=allMoney.add(one);
                    list.add(childs[j]);
                }
            }
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            parent.setExchangerate(allMoney);
            retBillVo.setChildrenVO(child);
            retBillVo.setParentVO(parent);
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}