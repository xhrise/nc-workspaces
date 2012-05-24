package nc.ui.eh.stock.h0150605;

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
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.stock.z0250505.StockInBVO;
import nc.vo.eh.stock.z0250505.StockInVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 功能：上下游单据参照
 * Edit: 王明
 * Date:2008-05-29
 */ 

public class ZA22TOZA59DLG extends AbstractBTOBDLG {
      
    public ZA22TOZA59DLG(String pkField, String pkCorp, String operator, String funNode,
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
                 StockInVO.class.getName(),
                 StockInBVO.class.getName()  
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0250505","入库单");
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
        strWherePart.append("' and (NVL(cgfp_flag,'N')='N' or cgfp_flag='')and (NVL(lock_flag,'N')='N' or lock_flag='' ) and pk_cubasdoc is not NULL and vbillstatus="+IBillStatus.CHECKPASS+" and NVL(dr,0)=0 ");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override

    public String getBodyCondition() {
   		m_whereStr=null;
	   StringBuffer strWherePart = new StringBuffer();
	   strWherePart.append(" (NVL(cgfp_flag,'N')='N' or cgfp_flag='') ");
	   String strwp = strWherePart.toString();
	   return strwp;
}
    
    
    
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author 张起源
     * 2008-4-9
     */
    @Override
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
            
            HashMap hm=new HashMap();//key=pk_in
            HashMap hmpk_contract=new HashMap();//key=pk_contract
            //add by houcq begin 2010-10-11
            HashMap hmcubasdoc = new HashMap();
            //add by houcq end 
            for(int i=0;i<retBillVos.length;i++){
            	StockInVO invos=(StockInVO) retBillVos[i].getParentVO();
            	 //add by houcq begin 2010-10-11
            	  String pk_cubasdoc = (invos.getPk_cubasdoc() == null) ? "" : invos.getPk_cubasdoc().toString();
                  if ((!(hmcubasdoc.containsKey(pk_cubasdoc))) && (hmcubasdoc.size() > 0)) {
                    JOptionPane.showMessageDialog(null, "来源单据中,请选择相同的供应商!", "提示", 1);
                    hmcubasdoc.clear();
                    return;
                  }
                  hmcubasdoc.put(pk_cubasdoc, pk_cubasdoc);
                //add by houcq end 
            	
            	String pk_contract=invos.getPk_contract()==null?"":invos.getPk_contract().toString();
            	if(!(hmpk_contract.containsKey(pk_contract)) && hmpk_contract.size()!=0 ){
            		JOptionPane.showMessageDialog(null,"请选择同一张合同!","提示",JOptionPane.INFORMATION_MESSAGE); 
                    return;
            	}
            	else{
            		hm.put(invos.getPk_in(), invos.getBillno());
            		hmpk_contract.put(pk_contract, pk_contract);
            	}
            }
            
            //实时校验
            StockInBVO[] bvo=(StockInBVO[])( retBillVos[0].getChildrenVO());
            String[] vsourcebillids=new String[bvo.length];
            for(int i=0;i<bvo.length;i++){
                String vsourcebillid=bvo[i].getPk_in_b();
                vsourcebillids[i]=vsourcebillid;
            }
            String billids = Toolkits.combinArrayToString(vsourcebillids);
            HashMap hms=calamount("eh_arap_stockinvoices_b", "fpmount",billids);
            
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
            	StockInBVO[] childs=(StockInBVO[]) retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {
                	UFDouble rkamount=new UFDouble( childs[j].getInamount()==null?"0":childs[j].getInamount().toString());//入库数量
                	String pk_in=childs[j].getPk_in()==null?"":childs[j].getPk_in().toString();
                	String billno=hm.get(pk_in)==null?"":hm.get(pk_in).toString();
                	childs[j].setDef_1(billno);
                     String vsourcebillid=childs[j].getPk_in_b();
                     UFDouble yramount= (hms.get(vsourcebillid)==null?new UFDouble(0): //已入库数量
                         new UFDouble(hms.get(vsourcebillid).toString()));
                     UFDouble fpmount=rkamount.sub(yramount);//可以入库数量
//                     UFDouble inprice=new UFDouble(childs[j].getInprice()==null?"0":childs[j].getInprice().toString());  //含税单价
                     childs[j].setDef_9(yramount);
                     childs[j].setDef_8(fpmount);
//                     childs[j].setDef_7(fpmount.multiply(inprice));
//                     UFDouble taxmny=inprice.multiply(fpmount).multiply(0.17);//税额
//                     childs[j].setDef_10(taxmny);
//                     UFDouble a=(fpmount.multiply(inprice)).sub(taxmny);
//                     childs[j].setDef_6((fpmount.multiply(inprice)).sub(taxmny));
                     
                     
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
    
    public HashMap calamount(String tablename,String amountname,String vsourcebillids){
        StringBuffer sql=new StringBuffer("")
        .append(" select vsourcebillrowid,sum(NVL(").append(amountname).append(",0)) amount from ").append(tablename)
        .append(" where vsourcebillrowid in ").append(vsourcebillids).append(" and NVL(dr,0)=0 group by vsourcebillrowid");
        
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        HashMap hm=new HashMap();
        try {
            ArrayList arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(!arr.isEmpty()){
                for(int i=0;i<arr.size();i++){
                    HashMap rs=(HashMap)arr.get(i);
                    String vsourcebillid = rs.get("vsourcebillrowid").toString();
                    UFDouble amount=new UFDouble(rs.get("amount").toString());
                    hm.put(vsourcebillid, amount);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hm;
    }
}