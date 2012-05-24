package nc.ui.eh.stock.z06005;

import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z0151001.StockReceiptBVO;
import nc.vo.eh.stock.z0151001.StockReceiptVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵��:�����ε��ݽ���
 * @author ����Դ
 * 2008-4-9
 */
public class ZA20TOZA18DLG extends AbstractBTOBDLG {
      
    public ZA20TOZA18DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		StockReceiptVO.class.getName(),
                StockReceiptBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0151001","�ջ�֪ͨ��");
//            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
//                    getFunNode(), getBusinessType(), getCurrentBillType(), "ZA70", null ,"H0205599","�ջ�֪ͨ��");
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
        strWherePart.append("' and pk_receipt  in (select pk_receipt from eh_stock_receipt_b where isnull(issb,'N')='Y' and isnull(dr,0)=0 ) and (isnull(lock_flag,'N')='N' or lock_flag='' ) and isnull(dr,0)=0 and vbillstatus = ").append(IBillStatus.CHECKPASS);
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
    public String getBodyCondition() {
    	m_whereStr = null;
    	return "isnull(issb,'N')='Y' and isnull(dr,0)=0";
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
           	 	JOptionPane.showMessageDialog(null,"��ͷֻ����һ���ջ�֪ͨ��!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
           }
            
            StockReceiptBVO[] stockreceiptbvo = (StockReceiptBVO[]) retBillVo.getChildrenVO();
            if(stockreceiptbvo==null || stockreceiptbvo.length ==0 || stockreceiptbvo.length >1){
            	JOptionPane.showMessageDialog(null,"����ֻ��ѡ��һ�����ݻ�����ѡ��һ�����ݣ�","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;          	
            }
            
            
            ArrayList list=new ArrayList();
            
            //ȡ�����β��յ��ݱ��������PK
            String pk_invbasdoc = retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc")==null?"":
								retBillVo.getChildrenVO()[0].getAttributeValue("pk_invbasdoc").toString();
            //ȡ�������ջ�֪ͨ���ӱ�����
            String pk_receipt_b = retBillVo.getChildrenVO()[0].getAttributeValue("pk_receipt_b")==null?"":
								retBillVo.getChildrenVO()[0].getAttributeValue("pk_receipt_b").toString();
            
            //ȡ�����ջ�֪ͨ������İ�װ����
            UFDouble packagweight=new UFDouble(retBillVo.getChildrenVO()[0].getAttributeValue("packagweight")==null?"0":
				retBillVo.getChildrenVO()[0].getAttributeValue("packagweight").toString());
            
            
            
           
            //�ѱ��������PK������ͷ���Զ�����Def_1
            StockReceiptVO stockreceipvo = (StockReceiptVO) retBillVo.getParentVO();
            stockreceipvo.setDef_1(pk_invbasdoc);
            stockreceipvo.setDef_2(pk_receipt_b);
            stockreceipvo.setDef_8(packagweight);
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    }
    @Override
	public String[] getBodyHideCol() {
//      return null;
      return new String[]{"taxinprice"};
  }
}