/**
 * @(#)PubTools.java	V3.1 2008-4-15
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.wb.tools;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.bill.BillModel;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

public class PubTools {
    
    @SuppressWarnings("unchecked")
    public int uniqueCheck(BillModel bm,String[] code){
        int res=0;
        HashMap hm=new HashMap();
        for(int i=0;i<bm.getRowCount();i++){
            String key="key";
            for(int j=0;j<code.length;j++){
                key+=bm.getValueAt(i,code[j])==null?"":bm.getValueAt(i,code[j]).toString();
            }
            if(hm.containsKey(key)){
                return 1;
            }
            hm.put(key, i+"");
        }
        return res;
    }
    
    /**
     * 计算表中物料的总数量公用方法
     * @param tablename
     * @param amountname
     * @param vsourcebillids
     * @return
     */
    public HashMap calamount(String tablename,String amountname,String vsourcebillids){
        StringBuffer sql=new StringBuffer("")
        .append(" select vsourcebillid,sum(isnull(").append(amountname).append(",0)) amount from ").append(tablename)
        .append(" where vsourcebillid in ").append(vsourcebillids).append(" and isnull(dr,0)=0 group by vsourcebillid");
        
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        HashMap hm=new HashMap();
        try {
            ArrayList arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(!arr.isEmpty()){
                for(int i=0;i<arr.size();i++){
                    HashMap rs=(HashMap)arr.get(i);
                    String vsourcebillid = rs.get("vsourcebillid").toString();
                    UFDouble amount=new UFDouble(rs.get("amount").toString());
                    hm.put(vsourcebillid, amount);
                }
            }
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hm;
    }
    
    /**
     * 计算表中物料的总数量公用方法
     * @param tablename
     * @param amountname
     * @param vsourcebillids
     * @author wb
     * 2008-5-15 10:28:55
     * @return
     */
    public static UFDouble calTotalamount(String tablename,String amountname,String vsourcebillid,String strpk_now,String pk_now){
    	UFDouble amount = new UFDouble(0);
    	StringBuffer sql=new StringBuffer("")
        .append(" select sum(isnull(").append(amountname).append(",0)) amount from ").append(tablename)
        .append(" where vsourcebillid = '").append(vsourcebillid).append("'");
        if(pk_now!=null){
        	sql.append(" and ").append(strpk_now).append(" not in ('").append(pk_now).append("')");
        }
        sql.append(" and isnull(dr,0)=0 ");
        
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
            Object objamount = iUAPQueryBS.executeQuery(sql.toString(),new ColumnProcessor());
            if(objamount!=null){
                amount = new UFDouble(objamount.toString());
            }
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return amount;
    }
    
//    /**
//     * 在单位换算的前要先执行公式(对单位的更换同时对数量)
//     * 单位换算的公用方法
//     * @param pk_invbasdoc
//     * @param pk_unit
//     * @param aomunt
//     * @param price
//     * @author 王明
//     * 2008-5-15 10:28:55
//     * 
//     */
//    public ArrayList changUnit(String pk_invbasdoc,String pk_unit,UFDouble aomunt,UFDouble price){
//    	//查询出所有的可换的单位
//    	HashMap<String,UFDouble> hm=new HashMap<String,UFDouble>();
//        ArrayList al=new ArrayList();
//    	String sql="select pk_measdoc,changerate from eh_invbasdoc_b  where pk_invbasdoc='"+pk_invbasdoc+"' and  isnull(dr,0)=0";
//        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//        try {
//			ArrayList<HashMap> almeasdoc=(ArrayList<HashMap>) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
//			for(int i=0;i<almeasdoc.size();i++){
//				HashMap hmone=almeasdoc.get(i);
//				String pk_measdoc=hmone.get("pk_measdoc")==null?"":hmone.get("pk_measdoc").toString().trim(); //换算计量单位
//				UFDouble changerate=new UFDouble(hmone.get("changerate")==null?"":hmone.get("changerate").toString().trim());//换算系数
//				hm.put(pk_measdoc, changerate);
//			}
//		} catch (BusinessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//先是判断是否有换算单位
//		if(hm.containsKey(pk_unit)){//存在的情况下的动作
//			UFDouble changerate=new UFDouble(hm.get(pk_unit)==null?"0":hm.get(pk_unit).toString());//换算系数
//			UFDouble newaomunt=changerate.multiply(aomunt);
//			UFDouble newprice=null;
//			if(price.equals("0")||price.equals("")||price.equals("0.0")){
//				newprice=new UFDouble();
//			}else{
//				newprice=price.div(changerate);
//			}
//			al.add(newaomunt);
//			al.add(newprice);
//		}
//		return al;
//    }
    
   /**
    * 对判断是否可用单位判断(只队单位进行判断时, 不对任何单价，数量的修改)
    * @param pk_invbasdoc 物料
    * @retuen hm 此物料的所有可以换算的单位
    * 
    */
    public HashMap canChange(String pk_invbasdoc){
    	String sql="select a.pk_measdoc apk_measdoc,b.pk_measdoc  bpk_measdoc from eh_invbasdoc a,eh_invbasdoc_b b " +
    			"where a.pk_invbasdoc=b.pk_invbasdoc and a.pk_invbasdoc='"+pk_invbasdoc+"' and  isnull(a.dr,0)=0";
    	HashMap hm=new HashMap();
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int i=0;i<al.size();i++){
				HashMap hmone=(HashMap) al.get(i);
				String apk_measdoc=hmone.get("apk_measdoc")==null?"":hmone.get("apk_measdoc").toString();
				String bpk_measdoc=hmone.get("bpk_measdoc")==null?"":hmone.get("bpk_measdoc").toString();
				hm.put(apk_measdoc, apk_measdoc);
				hm.put(bpk_measdoc, bpk_measdoc);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return hm;

    }
    
    /**
	 * 主物料的单位
	 * @param pk_invbasdoc
	 * @return HashMap
	 */
	public static HashMap getMainMeasdoc(String pk_invbasdoc){
		HashMap hm=new HashMap();
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql="select pk_measdoc from eh_invbasdoc where pk_invbasdoc='"+pk_invbasdoc+"' and isnull(dr,0)=0";
		try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			if(!(al==null||al.size()<=0)){
				HashMap hmone=(HashMap) al.get(0);
				String pk_measdoc=hmone.get("pk_measdoc")==null?"":hmone.get("pk_measdoc").toString();
				hm.put(pk_measdoc, pk_measdoc);
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hm;
		
	}
	/**
	 * 返回辅助单位
	 * @param pk_invbasdoc
	 * @return HashMap
	 */
	public static HashMap getMeasdoc(String pk_invbasdoc){
		HashMap hm=new HashMap();
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		String sql="select pk_measdoc,changerate from eh_invbasdoc_b where pk_invbasdoc='"+pk_invbasdoc+"' and isnull(dr,0)=0";
		try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int i=0;i<al.size();i++){
				HashMap hmone=(HashMap) al.get(i);
				String pk_measdoc=hmone.get("pk_measdoc")==null?"":hmone.get("pk_measdoc").toString();
				UFDouble changerate=new UFDouble(hmone.get("changerate")==null?"":hmone.get("changerate").toString());
				hm.put(pk_measdoc, changerate);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return hm;
		
	}
	

}

