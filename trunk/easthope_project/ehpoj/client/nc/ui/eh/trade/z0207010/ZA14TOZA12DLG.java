package nc.ui.eh.trade.z0207010;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0207501.InvoiceBVO;
import nc.vo.eh.trade.z0207501.InvoiceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵��:�����ε��ݽ���
 * @author ����Դ
 * 2008-4-9
 */
public class ZA14TOZA12DLG extends AbstractBTOBDLG {
      
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    public ZA14TOZA12DLG(String pkField, String pkCorp, String operator, String funNode,
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
        strWherePart.append("' and vbillstatus=1 and isnull(th_flag,'N')='N' and isnull(dr,0)=0 " +
                "and (isnull(lock_flag,'N')='N' or lock_flag='') ");//���ӹر�״̬���
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
    public String getBodyCondition() {
    	m_whereStr=null;
    	
    	return null;
    }
    /**
     * ����:�ж����ζ�����ϸ�Ƿ�������ͬһ���ͻ�
     * @author ����Դ
     * 2008-4-9
     */
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
           	 	JOptionPane.showMessageDialog(null,"ֻ����һ�����۷�Ʊ!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
           }
            
           String pk_psndoc = null;//��Ա����PK
           String psnname = null;//��Ա����
           String pk_cubasdoc = retBillVo.getParentVO().getAttributeValue("pk_cubasdoc")==null?"":
               retBillVo.getParentVO().getAttributeValue("pk_cubasdoc").toString();//ȡ�����ε��ݵĿͻ�PK
           String sql ="select a.pk_psndoc pk_psndoc,a.psnname psnname from bd_psndoc a,eh_custyxdb b where a.pk_psndoc=b.pk_psndoc " +
                " and b.pk_cubasdoc='"+pk_cubasdoc+"' and b.ismain='Y' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 ";
           try {
            ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_psndoc=hm.get("pk_psndoc")==null?"":hm.get("pk_psndoc").toString();
                    psnname=hm.get("psnname")==null?"":hm.get("psnname").toString();
                  }
                }
            } catch (BusinessException e) {
                e.printStackTrace();
            }
               
            InvoiceVO ivo = (InvoiceVO) retBillVo.getParentVO();
            ivo.setDef_1(pk_psndoc);//�����εõ�����Ա������PK�ŵ�����def_1�д�������yxdbӪ��������
            ivo.setDef_2(psnname);//����Ա���Ʒŵ�def_2�д�������vyxdb��
        
            
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