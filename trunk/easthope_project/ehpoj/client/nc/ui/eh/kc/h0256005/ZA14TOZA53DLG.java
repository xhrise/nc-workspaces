package nc.ui.eh.kc.h0256005;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0207501.InvoiceBVO;
import nc.vo.eh.trade.z0207501.InvoiceVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵��:�����ε��ݽ��棨���۷�Ʊ���˻���ⵥ��
 * @author ����Դ
 * ʱ�䣺2008-5-27 11:02:11
 */
public class ZA14TOZA53DLG extends AbstractBTOBDLG {
      
    public ZA14TOZA53DLG(String pkField, String pkCorp, String operator, String funNode,
            String queryWhere, String billType, String businessType, String templateId,
            String currentBillType, String nodeKey, Object userObj, Container parent) {
        super(pkField, pkCorp, operator, funNode, queryWhere, billType, businessType, templateId,
                currentBillType, nodeKey, userObj, parent);
        
        BillItem billItem = getbillListPanel().getBillListData().getHeadItem("vbillstatus");
        initComboBox(billItem, IBillStatus.strStateRemark,true);
    }

    @Override
	public String[] getBillVos(){
        return new String[] { PubBillVO.class.getName(),
                InvoiceVO.class.getName(),
                InvoiceBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0207501","���۷�Ʊ");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (isnull(def_1,'N') = 'N' or def_1='') and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
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
                    JOptionPane.showMessageDialog(null,"��ͷֻ��ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                    return;             
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