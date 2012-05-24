package nc.ui.eh.sc.h0471505;

import java.awt.Container;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0471005.ScSbplanBVO;
import nc.vo.eh.sc.h0471005.ScSbplanVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵��:�����ε��ݽ���(�豸ά�޼ƻ�->ά��)
 * @author wangbing
 * 2008-12-20 13:25:06
 */ 
public class ZB29TOZB30DLG extends AbstractBTOBDLG {
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZB29TOZB30DLG(String pkField, String pkCorp, String operator, String funNode,
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
                ScSbplanVO.class.getName(),
                ScSbplanBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0471005","�豸ά�޼ƻ�");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
        StringBuffer strWherePart = new StringBuffer();
        //����ҵ�����ͣ���˾���룬����״̬��ѯ
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("'and vbillstatus="+IBillStatus.CHECKPASS+" " +
                            "and (isnull(lock_flag,'N')='N' or lock_flag='')   and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
    public String getBodyCondition() {
        m_whereStr = null;
        StringBuffer strWherePart = new StringBuffer();
        strWherePart.append("  1=1 ");
        
        return strWherePart.toString();
    }
   
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {

        	//��û�����ε��ݿ��Բ���ʱ ������ʾ
            if (getbillListPanel().getHeadBillModel().getRowCount() == 0){
                super.close();
                JOptionPane.showMessageDialog(null,"û�п��Բ��յĵ���!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                
                return;
            }
         
            if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
                retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
                retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
                
                if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                    JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;
                }
                
                if(retBillVos.length>1||retBillVos.length==0){
               	 	JOptionPane.showMessageDialog(null,"ֻ����һ�����֪ͨ��!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;          	
               }
               
            }
            this.getAlignmentX();
            this.closeOK();

        }   

}