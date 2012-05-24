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
 * ˵��:�����ε��ݽ��� (�����˻�->���۷�Ʊ)
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
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0207010","�����˻ص�");
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
        strWherePart.append("' ");
        strWherePart.append(" and vbillstatus=1");
        strWherePart.append(" and isnull(ykfp_flag,'N')='N'");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author ����
     * 2007-11-1 ����03:14:05
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
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }              
//            if(retBillVos.length>1||retBillVos.length==0){
//            	 JOptionPane.showMessageDialog(null,"ֻ����һ�����֪ͨ��!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
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
            	 JOptionPane.showMessageDialog(null,"��ѡ��ͬһ�ͻ�����Ʊ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
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