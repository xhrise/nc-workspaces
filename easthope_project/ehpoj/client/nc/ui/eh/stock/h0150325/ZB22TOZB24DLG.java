package nc.ui.eh.stock.h0150325;

import java.awt.Container;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150305.StockApplyBVO;
import nc.vo.eh.stock.h0150305.StockApplyVO;
import nc.vo.trade.pub.IBillStatus;


/**
 * ˵��:���ɹ�����->������
 * @author wb
 * 2009-6-11 9:57:26
 */
@SuppressWarnings("serial")
public class ZB22TOZB24DLG extends AbstractBTOBDLG {
	
   
    public ZB22TOZB24DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		 StockApplyVO.class.getName(),
                 StockApplyBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0150305","���ɹ�����");
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
        strWherePart.append("' and vbillstatus = 1 and isnull(rk_flag,'N')<>'Y'  and isnull(lock_flag,'N')<>'Y'   and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;

    }
    @Override
    public String getBodyCondition()
    {
    	StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" isnull(rk_flag,'N')<>'Y' and isnull(dr,0)=0 ");       
		return strWherePart.toString();    	
    }
	@Override
	@SuppressWarnings("deprecation")
	public void onOk() {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            //���ͷ�Ĵ���
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"������ѡ��һ������!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            if(retBillVos.length>1){
            	JOptionPane.showMessageDialog(null,"ֻ��ѡ��һ�����ɹ�����!","��ʾ",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
        }
        this.getAlignmentX();
        this.closeOK();
    } 
}