package nc.ui.eh.cw.h1101005;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;

/** 
 * ���ܣ������ε��ݲ��� (��⵽���)
 * Edit: ����
 * Date:2008-05-31
 */

public class ZA22TOZA57DLG extends AbstractBTOBDLG {
      
    public ZA22TOZA57DLG(String pkField, String pkCorp, String operator, String funNode,
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
                 StockInVO.class.getName(),
                 StockInBVO.class.getName() 
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0250505","��ⵥ");
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
        strWherePart.append("' and cgfp_flag='Y' and (isnull(rklr_flag,'N')='N' or rklr_flag='' ) " +
        		" and vbillstatus = "+IBillStatus.CHECKPASS+" and isnull(dr,0)=0 ");		//��������ͨ����������,����������ͨ������  add by zqy 2010-11-16 14:31:06
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author ����Դ
     * 2008-4-9
     */
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
            //�ж��Ƿ�����ͬһ����ͬ
            HashMap hmpk_contract=new HashMap();//key=pk_contract
            for(int i=0;i<retBillVos.length;i++){
            	StockInVO invos=(StockInVO) retBillVos[i].getParentVO();
            	
            	String pk_contract=invos.getPk_contract()==null?"":invos.getPk_contract().toString();
            	if(!(hmpk_contract.containsKey(pk_contract)) && hmpk_contract.size()!=0 ){
            		JOptionPane.showMessageDialog(null,"��ѡ��ͬһ�ź�ͬ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;
            	}
            	else{
            		hmpk_contract.put(pk_contract, pk_contract);
            	}
            }
            //end
            
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