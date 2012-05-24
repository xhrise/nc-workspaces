package nc.ui.eh.sc.h0450705;

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
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450705.ScMrpBVO;
import nc.vo.eh.trade.z0206005.OrderBVO;
import nc.vo.eh.trade.z0206005.OrderVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:上下游单据界面
 * @author 王明
 * 2007-9-29 下午04:02:28
 */
@SuppressWarnings("serial")
public class ZA09TOZB32DLG extends AbstractBTOBDLG {
	
      
	public static ScMrpBVO[] bvos = null;
	//上游表体的物料PK集合。时间：2009-12-31
	public static String[] invs = null;
	public ZA09TOZB32DLG(String pkField, String pkCorp, String operator, String funNode,
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
        		OrderVO.class.getName(),
        		OrderBVO.class.getName()
               };
    }
    
    @Override
	protected AbstractBTOBBillQueryDLG getQueryDlg() {
        if (queryCondition == null) {
            queryCondition = new AbstractBTOBBillQueryDLG(this, getPkCorp(), getOperator(),
                    getFunNode(), getBusinessType(), getCurrentBillType(), getBillType(), null ,"H0206005","销售订单");
            queryCondition.hideNormal();
        }
        return queryCondition;
    }

    @Override
	public String getHeadCondition() {
    	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    	String sDate = ce.getDate().toString();
    	StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" " +
                "and enddate >= '"+sDate+"' " +
                "and isnull(dr,0)=0 and (isnull(lock_flag,'N')='N' or lock_flag='') and pk_order in (select pk_order from eh_order_b where isnull(ck_flag,'N')<>'Y' and isnull(dr,0)=0)");//增加关闭状态标记
        String strwp = strWherePart.toString();
        return strwp;
    }
   @Override
   //表体判断
	public String getBodyCondition() {
	   		m_whereStr=null;
		   StringBuffer strWherePart = new StringBuffer();
		   strWherePart.append(" isnull(ck_flag,'N')<> 'Y'");
		   String strwp = strWherePart.toString();
		   return strwp;
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void onOk()  {
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {
            getbillListPanel().setBodyModelDataCopy(getbillListPanel().getHeadBillModel().convertIntoModelRow(getbillListPanel().getHeadTable().getSelectedRow()));        
            retBillVo = getbillListPanel().getSelectedVO(m_billVo, m_billHeadVo, m_billBodyVo);
            retBillVos = getbillListPanel().getMultiSelectedVOs(m_billVo, m_billHeadVo, m_billBodyVo);
            //多表头的处理
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            HashMap hmsame=new HashMap();
            ArrayList arr = new ArrayList();
            String[] order_pks = new String[retBillVos.length];
            for(int i=0;i<retBillVos.length;i++){
	           	 String pk_ord = ((OrderVO)retBillVos[i].getParentVO()).getPk_order();
	           	order_pks[i] = pk_ord;
           	 }
            String order_pk = PubTools.combinArrayToString2(order_pks);
            
            if(order_pk!=null&&order_pk.length()>2500){
            	JOptionPane.showMessageDialog(null, "所选订单记录太多,不允许超过100条记录!", "提示",JOptionPane.INFORMATION_MESSAGE);
				return;
            }
            
            try {
				HashMap outhm = getOrderAmount(order_pk);		//订单对应出库数量
				HashMap billnohm = getBillnos(order_pk);		//订单对应的单据号
				HashMap verhm = getBomVer(order_pk);			//订单产品对应的BOM版本
				for(int i=0;i<retBillVos.length;i++){
            	 String pk_ord = ((OrderVO)retBillVos[i].getParentVO()).getPk_order();
            	 arr.add(pk_ord);
            	 OrderBVO[] orders=(OrderBVO[]) retBillVos[i].getChildrenVO();
            	 if(orders!=null&&orders.length>0){
	                 for(int j=0;j<orders.length;j++){
	                 	OrderBVO bvo=orders[j];
	                 	String pk_order_b = orders[j].getPk_order_b()==null?"":orders[j].getPk_order_b().toString();			//订单子表pk
	                 	String pk_order = orders[j].getPk_order()==null?"":orders[j].getPk_order().toString();					//订单主表pk
	                 	bvo.setPk_order_b("'"+pk_order_b+"'");
	                 	bvo.setPk_order("'"+pk_order+"'");
	                 	String  billno = null;
	                 	String pk_invbas = orders[j].getPk_invbasdoc()==null?"":orders[j].getPk_invbasdoc().toString();
	                 	UFDouble amout= new UFDouble(orders[j].getFzamount()==null?"0":orders[j].getFzamount().toString());
	                	UFDouble outamount = new UFDouble(outhm.get(pk_order_b)==null?"0":outhm.get(pk_order_b).toString());		//出库数量
	                	
	                	bvo.setDef_8(outamount);							//出库数量
	                	bvo.setDef_6(amout.sub(outamount));					//未生产量
	            		Integer ver = verhm.get(pk_invbas)==null?null:Integer.parseInt(verhm.get(pk_invbas).toString());
	    				bvo.setDr(ver);					//BOM版本
	    			    billno = billnohm.get(pk_order)==null?null:billnohm.get(pk_order).toString();
	    				bvo.setVsourcebillid(billno);	//单据号
	    				
	                 	if(hmsame.containsKey(pk_invbas)){
	                 		OrderBVO oldbvo=(OrderBVO) hmsame.get(pk_invbas);
	                 		UFDouble number=oldbvo.getFzamount();
	                 		amout=amout.add(number);				//订单数量累加
	                 		oldbvo.setFzamount(amout);
	                 		outamount = outamount.add(oldbvo.getDef_8()==null?new UFDouble(0):oldbvo.getDef_8());		//已经出库数量累加
	                 		String pk_order_bs = "'"+pk_order_b+"',"+oldbvo.getPk_order_b();
	                 		String pk_orders = "'"+pk_order+"',"+oldbvo.getPk_order();
	                 		String billnos = billno+","+oldbvo.getVsourcebillid();
	                 		oldbvo.setPk_order_b(pk_order_bs);
	                 		oldbvo.setPk_order(pk_orders);
	                 		oldbvo.setVsourcebillid(billnos);
	                 		oldbvo.setDef_8(outamount);				//已生产量
	                 		oldbvo.setDef_6(amout.sub(outamount));	//未生产量
	                 		hmsame.put(pk_invbas, oldbvo);
	                 	}else{
	                 		hmsame.put(pk_invbas, bvo);
	                 	}
	                 }
            	 }
            }
			Object[] keyset=(Object[])hmsame.keySet().toArray();
            OrderBVO[] neworder=new OrderBVO[hmsame.size()];
            ClientEnvironment ce = ClientEnvironment.getInstance();
            String pk_corp = ce.getCorporation().getPk_corp();
            //HashMap hmkc = new PubTools().getDateinvKC(null, null, ce.getDate(), "1",pk_corp);
//            HashMap hmsc = getScamount(pk_corp);
            for(int i=0;i<neworder.length;i++){
            	neworder[i]=(OrderBVO) hmsame.get(keyset[i].toString());
//            	String pk_invbasdoc = neworder[i].getPk_invbasdoc();
//            	UFDouble scamount = new UFDouble(hmsc.get(pk_invbasdoc)==null?"":hmsc.get(pk_invbasdoc).toString());			//在生产量
////            	UFDouble kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());			//库存
////				neworder[i].setDef_9(kcamount.add(scamount));
//            	
            }
            String[] pk_orders = (String[])arr.toArray(new String[arr.size()]);
            String pk_orderss = PubTools.combinArrayToString2(pk_orders);
            retBillVo.getParentVO().setAttributeValue("pk_order", pk_orderss);
            retBillVo.setChildrenVO(neworder); 
            //modify by houcq 2011-11-10根据订单取库存方法改成和自制时一样
            bvos = new ScMrpBVO[neworder.length];
            invs = new String[neworder.length];
            for(int i=0;i<neworder.length;i++){
            	bvos[i] = new ScMrpBVO();
            	String pk_invbasdoc=neworder[i].getPk_invbasdoc();
            	bvos[i].setPk_invbasdoc(pk_invbasdoc);
            	bvos[i].setVsourcebillno(neworder[i].getVsourcebillid());
            	bvos[i].setVsourcebillid(neworder[i].getPk_order());
            	bvos[i].setVsourcebillrowid(neworder[i].getPk_order_b());
            	bvos[i].setYscamount(neworder[i].getDef_8());
            	bvos[i].setOrderamunt(neworder[i].getFzamount());
            	bvos[i].setBcamount(neworder[i].getDef_6());
            	bvos[i].setVer(neworder[i].getDr());
            	UFDouble kcamount = new PubTools().getInvKcAmount(pk_corp,ce.getDate(),pk_invbasdoc);
            	bvos[i].setKcamount(kcamount);
            	//物料PK
            	invs[i] = neworder[i].getPk_invbasdoc();
            	
            }
           }catch (Exception e) {
        	   e.printStackTrace();
    	   }
        } 
        this.getAlignmentX();
        this.closeOK();
    } 
	
	
	/***
	 * 根据订单找出已出库数量
	 * wb 2009-2-18 16:16:58
	 * @param pk_orders
	 * @return
	 * @throws Exception
	 */ 
	@SuppressWarnings("unchecked")
	public HashMap getOrderAmount(String pk_orders) throws Exception{
		  HashMap hm = new HashMap();
		  StringBuffer sql = new StringBuffer()
		  .append("  SELECT a.pk_order_b,sum(isnull(b.outamount,0)) outamount")
		  .append("  FROM ")
		  .append("   (SELECT b.pk_order_b,d.pk_ladingbill_b,b.fzamount")
		  .append("   FROM eh_order a , eh_order_b b ,eh_ladingbill c,eh_ladingbill_b d")
		  .append("   WHERE  a.pk_order =  b.pk_order")
		  .append("   AND a.pk_order = c.vsourcebillid")
		  .append("   AND c.pk_ladingbill = d.pk_ladingbill")
		  .append("   AND b.pk_order_b = d.vsourcebillid")
		  .append("   AND a.pk_order IN("+pk_orders+")")
		  .append("   AND c.vbillstatus = 1 ")
		  .append("   and ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0 ")
		  .append("  AND ISNULL(c.dr,0)=0 AND ISNULL(d.dr,0)=0 ")
		  .append("   ) a LEFT JOIN ")
		  .append("   ( SELECT a.vsourcebillid ,SUM(ISNULL(a.outamount,0)) outamount")
		  .append("   FROM eh_icout_b a,eh_icout b where a.pk_icout = b.pk_icout and b.vbillstatus = 1")
		  .append("   and a.vsourcebillid IS NOT NULL ")
		  .append("   GROUP BY a.vsourcebillid")
		  .append("   ) b ON a.pk_ladingbill_b = b.vsourcebillid")
		  .append(" group by a.pk_order_b");
		  IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
          if(arr!=null&&arr.size()>0){
        	  String pk_order_b = null;					//订单子表主键
        	  UFDouble outamount = new UFDouble(0);		//出库数量
        	  for(int i=0;i<arr.size();i++){
        		  HashMap hmA = (HashMap)arr.get(i);
        		  pk_order_b = hmA.get("pk_order_b")==null?"":hmA.get("pk_order_b").toString();
        		  outamount = new UFDouble(hmA.get("outamount")==null?"":hmA.get("outamount").toString());
        		  hm.put(pk_order_b, outamount);
        	  }
          }
          return hm;
	}
	
	/***
	 * 根据订单主键找到单据号
	 * @param pk_orders
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap getBillnos(String pk_orders) throws Exception{
		HashMap hm = new HashMap();
		StringBuffer sql = new StringBuffer()
		.append(" select pk_order, billno from eh_order where pk_order in ("+pk_orders+")");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        if(arr!=null&&arr.size()>0){
        	String pk_order = null;
        	String billno = null;
        	for(int i=0;i<arr.size();i++){
      		  HashMap hmA = (HashMap)arr.get(i);
      		  pk_order = hmA.get("pk_order")==null?"":hmA.get("pk_order").toString();
      		  billno = hmA.get("billno")==null?"":hmA.get("billno").toString();
      		  hm.put(pk_order, billno);
        	}
       }
        return hm;
	}
	
	/***
	 * 根据订单得到产品的BOM版本
	 * @param pk_orders
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap getBomVer(String pk_orders) throws Exception{
		HashMap hm = new HashMap();
		StringBuffer sql = new StringBuffer()
		.append("  SELECT DISTINCT b.pk_invbasdoc,c.ver")
		.append("  FROM eh_order a JOIN eh_order_b b ON a.pk_order = b.pk_order")
		.append("  LEFT JOIN eh_bom c ON b.pk_invbasdoc = c.pk_invbasdoc")
		.append("  WHERE c.sc_flag = 'Y'")
		.append("  AND a.pk_order IN ("+pk_orders+")")
		.append("  AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0 ")
		.append("  AND ISNULL(c.dr,0)=0 ");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        if(arr!=null&&arr.size()>0){
        	String pk_invbasdoc = null;
        	String ver = null;
        	for(int i=0;i<arr.size();i++){
      		  HashMap hmA = (HashMap)arr.get(i);
      		  pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
      		  ver = hmA.get("ver")==null?"":hmA.get("ver").toString();
      		  hm.put(pk_invbasdoc, ver);
        	}
       }
        return hm;
	}
	
	/***
	 * 已派工未入库数量
	 * @param pk_orders
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap getScamount(String pk_corp) throws Exception{
		HashMap hm = new HashMap();
		StringBuffer sql = new StringBuffer()
		.append(" select b.pk_invbasdoc,sum(isnull(b.pgamount,0)) pgamount")
		.append(" from eh_sc_pgd a,eh_sc_pgd_b b")
		.append(" where a.pk_pgd = b.pk_pgd")
		.append(" and isnull(a.lock_flag,'N') <> 'Y'")
		.append(" and isnull(a.rk_flag,'N')<>'Y'")
		.append(" and isnull(a.xdflag,'N')='Y'")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and isnull(a.dr,0) = 0")
		.append(" and isnull(b.dr,0) = 0")
		.append(" group by b.pk_invbasdoc");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        if(arr!=null&&arr.size()>0){
        	String pk_invbasdoc = null;
        	UFDouble pgamount = null;
        	for(int i=0;i<arr.size();i++){
      		  HashMap hmA = (HashMap)arr.get(i);
      		  pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
      		  pgamount = new UFDouble(hmA.get("pgamount")==null?"0":hmA.get("pgamount").toString());
      		  hm.put(pk_invbasdoc, pgamount);
        	}
       }
        return hm;
	}
}