package nc.ui.eh.trade.z0206510;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractBTOBBillQueryDLG;
import nc.ui.eh.uibase.AbstractBTOBDLG;
import nc.ui.pub.bill.BillItem;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.pub.Toolkits;
import nc.vo.eh.trade.z0206005.OrderBVO;
import nc.vo.eh.trade.z0206005.OrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明:上下游单据界面
 * @author honghai
 * 2007-9-29 下午04:02:28
 */
public class ZA09TOZA10DLG extends AbstractBTOBDLG {
    
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	String sDate = ce.getDate().toString();
    
    public ZA09TOZA10DLG(String pkField, String pkCorp, String operator, String funNode,
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
        StringBuffer strWherePart = new StringBuffer();
        //根据业务类型，公司编码，单据状态查询
        strWherePart.append(" pk_corp = '");
        strWherePart.append(getPkCorp());
        strWherePart.append("' and vbillstatus="+IBillStatus.CHECKPASS+" and (isnull(th_flag,'N')='N' or th_flag='') and isnull(lock_flag,'N')='N' and enddate >= '"+sDate+"'");
        String strwp = strWherePart.toString();
        return strwp;
    }
    
    @Override
    public String getBodyCondition() {
    	m_whereStr = null;
    	String sql = addBodyCondtion("pk_order_b", "eh_ladingbill_b", "vsourcebillrowid", "vsourcebillid", "ladingamount","orderamount");
    	return sql;
    }
    /**
     * 功能:判断上游多行明细是否来自与同一个客户
     * @author honghai
     * 2007-11-1 下午03:14:05
     */
    @Override
	@SuppressWarnings("unchecked")
    public void onOk() {
    
        if (getbillListPanel().getHeadBillModel().getRowCount() > 0) {         
            AggregatedValueObject[] selectedBillVOs = getbillListPanel().getMultiSelectedVOs(m_billVo,
                    m_billHeadVo, m_billBodyVo);
            retBillVo = selectedBillVOs.length > 0 ? selectedBillVOs[0] : null;
            retBillVos = selectedBillVOs;
            
            if(retBillVo.getChildrenVO()==null){
                JOptionPane.showMessageDialog(null,"请至少选择一行数据!","提示",JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            String pk_cubasdoc=((OrderVO)retBillVos[0].getParentVO()).getPk_cubasdoc();
            UFDouble discunt = getAllDiscount(pk_cubasdoc);             // 客户的二次折扣总额
            retBillVo.getParentVO().setAttributeValue("secondamount", discunt);
            // 得到 销售订单 子表主键数组得到已提货量 add by wb at 2008-5-15 11:23:06
            // 得到 选择的销售订单中所有子表的物料 pk 数组
            ArrayList arrB = new ArrayList();   // 所有选择的销售订单子表 pk
            ArrayList arrC = new ArrayList();   // 所有选择的物料
            String pk_order_b = null;
            String pk_invbasdoc = null;
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs = retBillVos[i].getChildrenVO();
                for (int j = 0; j < childs.length; j++) {
                    OrderBVO bvo = (OrderBVO)childs[j];
                    pk_order_b = bvo.getPk_order_b();
                    pk_invbasdoc = bvo.getPk_invbasdoc();
                    arrB.add(pk_order_b);
                    arrC.add(pk_invbasdoc);
                }
            }
            String[] pk_order_bs = new String[arrB.size()];
            pk_order_bs = (String[])arrB.toArray(pk_order_bs);
            String billids = Toolkits.combinArrayToString(pk_order_bs);
            HashMap hm = new PubTools().calamount("eh_ladingbill_b", "ladingamount",billids);                      // 已提货数量
            
            String[] pk_invbasdocs = new String[arrC.size()];
            pk_invbasdocs = (String[])arrC.toArray(pk_invbasdocs);
            pk_invbasdoc = Toolkits.combinArrayToString(pk_invbasdocs);
            
            IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//            //取出物料的所有库存
//            StringBuffer sql=new StringBuffer("")
//            .append(" select pk_invbasdoc,sum(isnull(amount,0)) amount from eh_kc where ")
//            .append(" pk_invbasdoc in "+pk_invbasdoc+" and isnull(dr,0)=0 group by pk_invbasdoc");
//            ArrayList arr=new ArrayList();
//            try {
//                arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
//            } catch (BusinessException e) {
//                e.printStackTrace();
//            }
//            HashMap hminv=new HashMap();
//            if(!arr.isEmpty()){
//                for(int i=0;i<arr.size();i++){
//                    HashMap hmdoc =(HashMap)arr.get(i);
//                    String pk_invbasdoc2 = hmdoc.get("pk_invbasdoc").toString();
//                    UFDouble amount=new UFDouble(hmdoc.get("amount")==null?"0":hmdoc.get("amount").toString());
//                    hminv.put(pk_invbasdoc2, amount);
//                    }
//            }
            
            ArrayList list=new ArrayList();
            for(int i=0;i<retBillVos.length;i++){
                CircularlyAccessibleValueObject[] childs=retBillVos[i].getChildrenVO();
                String cubasdoc=((OrderVO)retBillVos[i].getParentVO()).getPk_cubasdoc();
                if(!pk_cubasdoc.equals(cubasdoc)){
                    JOptionPane.showMessageDialog(null,"请选择相同的客户的订单!","提示",JOptionPane.INFORMATION_MESSAGE); 
                    return;
                }
                
                for (int j = 0; j < childs.length; j++) {
                    OrderBVO bvo = (OrderBVO)childs[j];
                    pk_invbasdoc = bvo.getPk_invbasdoc();
                    //String pk_measdoc = bvo.getPk_measdoc();
                    //UFDouble price = new UFDouble(getInvRateInfo(pk_invbasdoc).get(pk_measdoc).toString());          // 实时的根据单位转换的牌价
                    String vsourcebillid = bvo.getPk_order_b();
//                    UFDouble amountkc = new UFDouble(hminv.get(pk_invbasdoc)==null?"0":hminv.get(pk_invbasdoc).toString());
                    UFDouble ytamount = (hm.get(vsourcebillid)==null?new UFDouble(0):new UFDouble(hm.get(vsourcebillid).toString()));
                    //bvo.setPrice(price);
//                    bvo.setDef_10(amountkc);
                    bvo.setDef_6(ytamount);
                    //bvo.setOneprice(getPrice(pk_invbasdoc));//modify by houcq 2011-05-09
                    list.add(bvo);
                }
            }   
            SuperVO[] child=new SuperVO[list.size()] ;
            child = (SuperVO[])list.toArray(child);
            retBillVo.setChildrenVO(child);
        }
        this.getAlignmentX();
        this.closeOK();

    } 
    
    /**
     * 得到物料中的牌价
     */
    public UFDouble getPrice(String pk_invbasdoc){
    	UFDouble price=new UFDouble(0);
    	
    	String sql="select def3 price from bd_invbasdoc where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') and nvl(dr,0)=0";
    	
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int i=0;i<al.size();i++){
				HashMap hm=(HashMap) al.get(i);
				price=new UFDouble(hm.get("price")==null?"":hm.get("price").toString());
				
				
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return price;
    	
    }
    /**
     * 得到一个物料的所有单位转换率
     * @param pk_invbasdoc
     * @return
     */
    public HashMap getInvRateInfo(String pk_invbasdoc){
    	HashMap hm = new HashMap();
//    	StringBuffer sql = new StringBuffer()
//        .append(" select c.pk_measdoc,a.price,b.changerate from eh_invbasdoc a,eh_invbasdoc_b b,bd_measdoc c")
//        .append(" where a.pk_invbasdoc = b.pk_invbasdoc")
//        .append(" and b.pk_measdoc = c.pk_measdoc")
//        .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ")
//        .append(" union all ")
//        .append(" select c.pk_measdoc,a.price,1 changerate")
//        .append(" from  eh_invbasdoc a,bd_measdoc c")
//        .append(" where a.pk_measdoc = c.pk_measdoc")
//        .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(c.dr,0)=0");
    	
    	String sql = " select c.pk_measdoc,a.def3 price,b.mainmeasrate changerate "+
		 " from bd_invbasdoc a,bd_convert b,bd_measdoc c "+
		 " where a.pk_invbasdoc = b.pk_invbasdoc "+
		 " and b.pk_measdoc = c.pk_measdoc "+
		 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') "+
		 " and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 "+
		 " union all "+
		 " select c.pk_measdoc,a.def3 price,1 changerate "+
		 " from  bd_invbasdoc a,bd_measdoc c "+
		 " where a.pk_measdoc = c.pk_measdoc "+
		 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') "+
		 " and nvl(a.dr,0)=0 and nvl(c.dr,0)=0 ";

        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
        	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
		    if(vc!=null&&vc.size()>0){
		    	for(int i=0; i<vc.size(); i++){
		    		Vector vcc = (Vector)vc.get(i);
		    		String pk_measdoc = vcc.get(0)==null?"":vcc.get(0).toString();
		    		UFDouble price = new UFDouble(vcc.get(1)==null?"0":vcc.get(1).toString());
		    		UFDouble changerate = new UFDouble(vcc.get(2)==null?"1":vcc.get(2).toString());
		    		hm.put(pk_measdoc, price.multiply(changerate));
		    	}
		    }
         }catch (BusinessException e1) {
 			e1.printStackTrace();
 		}
        return hm;
    }   
    
    //取得客户的二次折扣总额
    public UFDouble getAllDiscount(String pk_cubasdoc){
    	UFDouble discount = new UFDouble(0);
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	int date=ce.getDate().getYear()*100+ce.getDate().getMonth();
        StringBuffer sql=new StringBuffer("select pk_cubasdoc keys,sum(isnull(ediscount,0)) value from eh_perioddiscount ")
        .append(" where pk_corp='"+ce.getCorporation().getPk_corp()+"' and (nyear*100+nmonth)='"+date)
        .append("' and pk_cubasdoc='"+pk_cubasdoc+"' and isnull(dr,0)=0 group by pk_cubasdoc");
        try {
	        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
	        if(!arr.isEmpty()){
	             HashMap hm = (HashMap) arr.get(0);
	             discount = new UFDouble(hm.get("value")==null?"0":hm.get("value").toString());
	        }
        } catch (BusinessException e) {
			e.printStackTrace();
		}
        return discount;        
    }
}