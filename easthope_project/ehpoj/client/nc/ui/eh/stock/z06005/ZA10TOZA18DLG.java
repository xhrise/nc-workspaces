package nc.ui.eh.stock.z06005;

import java.awt.Container;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0206510.LadingbillBVO;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * 说明:上下游单据界面
 * @author 张起源
 * 2008-4-9
 */
public class ZA10TOZA18DLG extends AbstractBTOBDLG {
      
    public ZA10TOZA18DLG(String pkField, String pkCorp, String operator, String funNode,
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
  
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 张起源
     * 2008-4-9
     */
    @Override
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
            
            if(retBillVos.length>1||retBillVos.length==0){
           	 	JOptionPane.showMessageDialog(null,"表头只能有一张收货通知单!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
           }
            
            LadingbillBVO[] stockreceiptbvo = (LadingbillBVO[]) retBillVo.getChildrenVO();
            if(stockreceiptbvo==null || stockreceiptbvo.length ==0 || stockreceiptbvo.length >1){
            	JOptionPane.showMessageDialog(null,"表体只能选择一条数据或至少选择一条数据！","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
            }
  
            
            //取出上游参照单据表体的物料PK
            String pk_invbasdoc = retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc")==null?"":
								retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc").toString();
            LadingbillVO lvo = (LadingbillVO) retBillVo.getParentVO();
            lvo.setDef_5(pk_invbasdoc);
          
        }
        this.getAlignmentX();
        this.closeOK();
    }   
    
    @Override
	public String[] getBodyHideCol(){
        return new String[] {"price","firstdiscount","seconddiscount","bcysje","vje"};
    }
}