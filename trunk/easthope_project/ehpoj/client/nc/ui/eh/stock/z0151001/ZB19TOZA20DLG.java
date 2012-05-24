package nc.ui.eh.stock.z0151001;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.stock.h0150220.StockDecisionBVO;
import nc.vo.eh.stock.h0150220.StockDecisionBillVO;
import nc.vo.eh.stock.h0150220.StockDecisionCVO;
import nc.vo.eh.stock.h0150220.StockDecisionDVO;
import nc.vo.eh.stock.h0150220.StockDecisionEVO;
import nc.vo.eh.stock.h0150220.StockDecisionVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IBillStatus;
/**
 * 说明:上下游单据界面 采购决策->收货通知
 * @author houcq
 * 2011-03-03 
 */
@SuppressWarnings("serial")
public class ZB19TOZA20DLG extends AbstractBTOBDLG {
	
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());   
    
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	String sDate = ce.getDate().toString();
	public ZB19TOZA20DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		 StockDecisionBillVO.class.getName(),
        		 StockDecisionVO.class.getName(), 
        		 StockDecisionBVO.class.getName(),        		  		 
        		 StockDecisionCVO.class.getName(),
        		 StockDecisionDVO.class.getName(),
        		 StockDecisionEVO.class.getName()     
        		 
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0150220","采购决策");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS); 
        strWherePart.append(" and nvl(dr,0)=0 and nvl(lock_flag,'N')='N'");
        String strwp = strWherePart.toString();
        return strwp;
    }
    @Override
    public String getBodyCondition() {    	
        return m_whereStr;
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 王明
     * 2007-11-1 下午03:14:05
     */
	@Override
	@SuppressWarnings({ "deprecation", "unchecked" })
    public void onOk() {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
             if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }                 
            if(retBillVos.length>1){
                JOptionPane.showMessageDialog(null,"只能选择一张采购决策!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;             
           }
           StockDecisionVO vo=(StockDecisionVO)( retBillVos[0].getParentVO()); 
           String pk_cubasdoc=vo.getPk_cubasdoc();
           try {
			vo.setPk_psndoc(new PubTools().getPk_custpsndoc(pk_cubasdoc, vo.getPk_corp()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           StockDecisionBVO[] evo=(StockDecisionBVO[])retBillVos[0].getChildrenVO() ;          
           evo[0].setPk_invbasdoc(vo.getPk_invbasdoc());
           String SQL = " select NVL(def2,'N') ischeck from bd_invmandoc where pk_invmandoc='"+vo.getPk_invbasdoc()+"' " +
           "and NVL(dr,0)=0 and (NVL(sealflag,'N')='N' or sealflag='' ) ";  
           
           String ischeck = null;
           try {
               ArrayList al = (ArrayList)iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
               if(al!=null && al.size()>0){
                       HashMap hm1 = (HashMap) al.get(0);
                       ischeck = new UFBoolean(hm1.get("ischeck")==null?"":hm1.get("ischeck").toString()).toString();  
               }
           } catch (BusinessException e) {
               e.printStackTrace();
           }
           evo[0].setIscheck(ischeck);           
           StockDecisionBVO[] sbvo = new StockDecisionBVO[]{evo[0]};
           retBillVo.setChildrenVO(sbvo);  
            
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}