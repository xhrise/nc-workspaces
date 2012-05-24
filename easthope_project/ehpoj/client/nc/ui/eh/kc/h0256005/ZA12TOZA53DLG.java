package nc.ui.eh.kc.h0256005;

import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0207010.BackbillBVO;
import nc.vo.eh.trade.z0207010.BackbillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;

/** 
 * ���ܣ������ε��ݲ��գ������˻ص��˻���ⵥ�� 
 * Edit: ����Դ 
 * Date:2008-7-17 14:39:56
 */
public class ZA12TOZA53DLG extends AbstractBTOBDLG {
      
    public ZA12TOZA53DLG(String pkField, String pkCorp, String operator, String funNode,
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
                BackbillVO.class.getName(),
                BackbillBVO.class.getName() 
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"z0207010","�����˻�");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" " +
                " and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='') " +
                " and (isnull(def_2,'N')='N' or def_2='') ");//���ӹر�״̬���,def_2Ϊ��д�������˻��еĻ�д���
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
            retBillVo.getParentVO().setAttributeValue("vbilltype", IBillType.eh_z0207010);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            
            if(retBillVos==null || retBillVo ==null || retBillVos.length==0 || retBillVos.length==0){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            if(retBillVos.length>1||retBillVos.length==0){
           	 	JOptionPane.showMessageDialog(null,"��ͷֻ��ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
           }
            
            BackbillBVO[] bvo = (BackbillBVO[]) retBillVos[0].getChildrenVO();
//            int length = bvo.length;
//            for(int i=0;i<length;i++){
//                UFDouble realbackamount = new UFDouble(bvo[i].getRealbackamount()==null?"0":bvo[i].getRealbackamount().toString());
//                BackbillBVO backbvo = bvo[i];
//                backbvo.setDef_6(realbackamount);                
//            }           
            
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