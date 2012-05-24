package nc.ui.eh.stock.h0150315;

import java.awt.Container;

import javax.swing.JOptionPane;

import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.h0150305.StockApplyBVO;
import nc.vo.eh.stock.h0150305.StockApplyVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionBVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


/**
 * 说明:五金采购申请->五金采购决策
 * @author wb
 * 2009-2-23 13:36:45
 */
public class ZB22TOZB23DLG extends AbstractBTOBDLG {
	
    public static StockWjdecisionBVO[] wjbvos = null;
    public static StockWjdecisionCVO[] wjcvos = null;
    public ZB22TOZB23DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		StockApplyBVO.class.getName(),
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0150305","采购申请");
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
        strWherePart.append("' and vbillstatus = 1 and (isnull(jc_flag,'N')='N' or jc_flag='') and (isnull(lock_flag,'N')='N' or lock_flag='')   and isnull(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;

    }
	@Override
	@SuppressWarnings("deprecation")
	public void onOk() {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            //多表头的处理
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            if(retBillVos.length>1){
            	JOptionPane.showMessageDialog(null,"只能选择一个采购申请!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            for(int i=0;i<retBillVos.length;i++){
            	StockApplyBVO[] bvos = (StockApplyBVO[])retBillVos[i].getChildrenVO();
            	wjbvos = new StockWjdecisionBVO[bvos.length];
            	wjcvos = new StockWjdecisionCVO[bvos.length];
            	for(int j=0;j<bvos.length;j++){
            		StockApplyBVO bvo = bvos[j];
            		String pk_invbasdoc= bvo.getPk_invbasdoc();
            		UFDouble amount = bvo.getAmount()==null?new UFDouble(0):bvo.getAmount();
            		UFDouble[] kc = new ClientUI().getWJamount(pk_invbasdoc);
            		
            		wjbvos[j] = new StockWjdecisionBVO();
            		wjbvos[j].setPk_invbasdoc(pk_invbasdoc);
            		wjbvos[j].setKcamount(kc[2]);
            		wjbvos[j].setBzkcuseday(kc[0]);
            		wjbvos[j].setCgamount(amount);
            		
            		wjcvos[j] = new StockWjdecisionCVO();
            		wjcvos[j].setPk_invbasdoc(pk_invbasdoc);
            		wjcvos[j].setCgamount(amount);
            		
            	}
            }
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}