package nc.ui.eh.trade.z0255001;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.trade.z0206510.LadingbillBVO;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


/**
 * 说明:上下游单据界面
 * @author honghai
 * 2007-9-29 下午04:02:28
 */
public class ZA10TOZA11DLG extends AbstractBTOBDLG {
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
      
    public ZA10TOZA11DLG(String pkField, String pkCorp, String operator, String funNode,
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
                LadingbillVO.class.getName(),
                LadingbillBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"Z02020","提货通知单");
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
        strWherePart.append("'and vbillstatus="+IBillStatus.CHECKPASS+" " +
                            "and (isnull(lock_flag,'N')='N' or lock_flag='') and (isnull(ck_flag,'N')='N' or ck_flag='') and isnull(dr,0)=0  ");
        
        

        String strwp = strWherePart.toString();
        return strwp;
    }
    
    
    
    @Override
    public String getBodyCondition() {
        m_whereStr = null;
        StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" isnull(isfull,'N')='N' or isfull='' ");
        
        return strWherePart.toString();
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author honghai
     * 2007-11-1 下午03:14:05
     */
    @Override
	@SuppressWarnings("unchecked")
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
               	 	JOptionPane.showMessageDialog(null,"只能有一张提货通知单!","提示",JOptionPane.INFORMATION_MESSAGE); 
                    return;          	
               }
                // 得到 提货通知单 子表主键数组得到已出库量 add by wb at 2008-7-11 11:14:27
                ArrayList arrA = new ArrayList();   // 所有选择的提货通知单子表 pk
                String pk_ladingbill_b = null;
                for(int i=0;i<retBillVos.length;i++){
                    CircularlyAccessibleValueObject[] childs = retBillVos[i].getChildrenVO();
                    for (int j = 0; j < childs.length; j++) {
                        LadingbillBVO bvo = (LadingbillBVO)childs[j];
                        pk_ladingbill_b = bvo.getPk_ladingbill_b();
                        arrA.add(pk_ladingbill_b);
                    }
                }
                
                String[] pk_ladingbill_bs = new String[arrA.size()];
                pk_ladingbill_bs = (String[])arrA.toArray(pk_ladingbill_bs);
                String billids = Toolkits.combinArrayToString(pk_ladingbill_bs);
                HashMap hm = new PubTools().calamount("eh_icout_b", "outamount",billids);                      // 已提货数量
                
                ArrayList list = new ArrayList();
                for(int i=0;i<retBillVos.length;i++){
                    CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                    for (int j = 0; j < childs.length; j++) {
                    	 LadingbillBVO bvo = (LadingbillBVO)childs[j];
                    	 pk_ladingbill_b = bvo.getPk_ladingbill_b();
                    	 bvo.setDef_6(hm.get(pk_ladingbill_b)==null?new UFDouble(0):new UFDouble(hm.get(pk_ladingbill_b).toString()));
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