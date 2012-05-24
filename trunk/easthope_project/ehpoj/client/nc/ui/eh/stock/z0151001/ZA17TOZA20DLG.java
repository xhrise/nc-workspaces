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
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.stock.z0150501.StockBillVO;
import nc.vo.eh.stock.z0150501.StockContractBVO;
import nc.vo.eh.stock.z0150501.StockContractEventVO;
import nc.vo.eh.stock.z0150501.StockContractTermsVO;
import nc.vo.eh.stock.z0150501.StockContractVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;
/**
 * 说明:上下游单据界面
 * @author 王明
 * 2007-9-29 下午04:02:28
 */
public class ZA17TOZA20DLG extends AbstractBTOBDLG {
	
    static IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());   
    
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	String sDate = ce.getDate().toString();
	public ZA17TOZA20DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		 StockBillVO.class.getName(),
                 StockContractVO.class.getName(),
                 StockContractBVO.class.getName(),
                 StockContractTermsVO.class.getName(),
                 StockContractEventVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0150501","采购合同");
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
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+"  and enddate >= '"+sDate+"'  and startdate <= '"+sDate+"' and (NVL(sh_flag,'N')='N' or sh_flag='' ) and (NVL(lock_flag,'N')='N' or lock_flag='' ) and NVL(copy_flag,'N')!='Y'"); 
        String strwp = strWherePart.toString();
        return strwp;
    }
    @Override
    public String getBodyCondition() {
    	m_whereStr=null;
    	StringBuffer strWherePart = new StringBuffer();
        strWherePart.append(" (NVL(sh_flag,'N')='N' or sh_flag='') ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 王明
     * 2007-11-1 下午03:14:05
     */
	@Override
	@SuppressWarnings("unchecked")
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
                JOptionPane.showMessageDialog(null,"只能选择一张合同!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;             
           }
            
            StockContractBVO[] bvo=(StockContractBVO[])( retBillVos[0].getChildrenVO());
            String[] vsourcebillids=new String[bvo.length];
            for(int i=0;i<bvo.length;i++){
                String vsourcebillid=bvo[i].getPk_contract_b();
                vsourcebillids[i]=vsourcebillid;
            }
            
            String billids = Toolkits.combinArrayToString(vsourcebillids);
            HashMap hm=new PubTools().calamount("eh_stock_receipt_b", "inamount",billids);
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {      
                    StockContractBVO childvo=(StockContractBVO)childs[j];
                    String vsourcebillid=childvo.getPk_contract_b()==null?"":childvo.getPk_contract_b().toString();
                    UFDouble yamount= (hm.get(vsourcebillid)==null?new UFDouble(0):new UFDouble(hm.get(vsourcebillid).toString()));
                    
                    String pk_invbasdoc = childvo.getPk_invbasdoc()==null?"":childvo.getPk_invbasdoc().toString();
                    
                    String SQL = " select NVL(def2,'N') ischeck from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"' " +
                    "and NVL(dr,0)=0 and (NVL(sealflag,'N')='N' or sealflag='' ) ";  
                    
                    String ischeck = null;
                    try {
                        ArrayList al = (ArrayList)iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
                        if(al!=null && al.size()>0){
                            for(int x=0;x<al.size();x++){
                                HashMap hm1 = (HashMap) al.get(x);
                                ischeck = new UFBoolean(hm1.get("ischeck")==null?"":hm1.get("ischeck").toString()).toString();                              
                            }                                
                        }
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                    
                    
                    childvo.setDef_9(yamount);
                    childvo.setDef_5(ischeck);
                    list.add(childs[j]);
                }
            }   
            StockContractBVO[] child=new StockContractBVO[list.size()] ;
            child = (StockContractBVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);  
            
            
//            ArrayList arr = new ArrayList();
//            if(retBillVos.length>0){
//                int length = retBillVos.length; 
//                for(int i=0;i<length;i++){
//                    CircularlyAccessibleValueObject[] vo = retBillVos[i].getChildrenVO();
//                    String pk_invbasdoc = null;
//                    for(int j=0;j<vo.length;j++){
//                        StockContractBVO sbvo = (StockContractBVO) vo[j];
//                        pk_invbasdoc = sbvo.getPk_invbasdoc()==null?"":sbvo.getPk_invbasdoc().toString();
//                        String SQL = " select isnull(ischeck,'N') ischeck from eh_invbasdoc where pk_invbasdoc='"+pk_invbasdoc+"' " +
//                                "and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='' ) ";                       
//                        try {
//                            ArrayList al = (ArrayList)iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
//                            if(al!=null && al.size()>0){
//                                for(int x=0;x<al.size();x++){
//                                    StockContractBVO stbvo = new StockContractBVO();
//                                    HashMap hm1 = (HashMap) al.get(x);
//                                    String ischeck = new UFBoolean(hm1.get("ischeck")==null?"":hm1.get("ischeck").toString()).toString();
//                                    stbvo.setDef_4(pk_invbasdoc);
//                                    stbvo.setDef_5(ischeck);
//                                    
//                                    arr.add(stbvo);
//                                }                                
//                            }
//                        } catch (BusinessException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                
//            }
//            SuperVO[] childb=new SuperVO[arr.size()] ;
//            childb = (SuperVO[])arr.toArray(childb);
//            retBillVo.setChildrenVO(childb);
            
            
        }
        this.getAlignmentX();
        this.closeOK();

    }   
}