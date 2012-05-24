/**
 * @(#)PubTools.java	V3.1 2008-4-15
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.pub;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.dap.pub.IDapSendMessage;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.fi.pub.GLOrgBookAcc;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.vo.bd.b09.CumandocVO;
import nc.vo.bd.b54.GlorgbookVO;
import nc.vo.dap.out.DapMsgVO;
import nc.vo.eh.ipub.ISQLChange;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.pub.KcVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

public class PubTools {
    
	nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
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
     * 返回日期之后year年的日期(用于有效期的设值)
     * @param date
     * @param year
     * @return
     */
    public static UFDate getDateAfter(UFDate date,int year){
    	int years = date.getYear()+year;
    	String strdate = years+"-"+date.getMonth()+"-"+date.getDay();
    	return new UFDate(strdate);
    }
    /**
     * 计算表中物料的总数量公用方法
     * @param tablename
     * @param amountname
     * @param vsourcebillids
     * @return
     */ 
    @SuppressWarnings("unchecked")
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
    
   /**
    * 对判断是否可用单位判断(只队单位进行判断时, 不对任何单价，数量的修改)
    * @param pk_invbasdoc 物料
    * @retuen hm 此物料的所有可以换算的单位
    * 
    */
    @SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	
	 /**
     * 将一个String数组变成带括号字符,便于带in条件的SQL语句使用
     * 例:('111','222','333','')
     * @param array
     * @return
     */
    public static String combinArrayToString(String[] array){
      
        StringBuffer str=new StringBuffer("('");
        for (int i = 0; i < array.length; i++) {
            str.append(array[i]);
            str.append("','");
        }
        str.append("')");
        return str.toString();
    }
    
    /**
     * 将一个String数组变成带括号字符,便于带in条件的SQL语句使用
     * 例:'111','222','333',''
     * @param array
     * @return
     */
    public static String combinArrayToString2(String[] array){
      
        StringBuffer str=new StringBuffer("");
        for (int i = 0; i < array.length; i++) {
        	str.append("'");
        	str.append(array[i]);
            str.append("',");
        }
        str.append("''");
        return str.toString();
    }
    
    /**
     * 将一个String数组组合成一个字符串 比如单据号的组合
     * 例: SB0807110953, SB0807110971
     * @param array
     * @return
     */
    public static String combinArrayToString3(String[] array){
      
        StringBuffer str=new StringBuffer("");
        for (int i = 0; i < array.length; i++) {
        	str.append(array[i]);
        	if(i!=array.length-1){
        		str.append(",");
        	}
           
        }
        return str.toString();
    }
    
    /**
     * 根据一个日期得到此月份的开始日期和结束日期
     * @param date
     * @return
     */
    public UFDate[] getSEdateofMonth(UFDate date){
    	UFDate[] sedate = new UFDate[2];
    	int year = date.getYear();
    	int month = date.getMonth();
    	Calendar cal = new GregorianCalendar(year, month, 1);
 	    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH); 
 	    UFDate startdate = new UFDate(date.toString().substring(0, 7)+"01");
 	    UFDate enddate = new UFDate(date.toString().substring(0, 7)+String.valueOf(1+days));
 	    sedate[0] = startdate;
 	    sedate[1] = enddate;
 	    return sedate;
    }
    
    /**根据所给年度月份得到上一个年度月份
	 * @param date
	 * @return
	 */
	public static String getLastDate(String date){
		String lastperiod = null;
		String lastdate = null;
		String periodyear = date.substring(0,4);
		String month = date.substring(5,7);
		String[] allmonth = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		for(int i=0; i<allmonth.length; i++){
			if(month.equals("01")){
				lastperiod = Integer.parseInt(periodyear)-1+"-12";
				return lastperiod;
			}
			if(month.equals(allmonth[i])){
				lastdate = allmonth[i-1];
				lastperiod = periodyear+"-"+lastdate;
			}
			
		}
		return lastperiod;
	}
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getPresentDate(){
	 	String nowDate = "";
		try{
			Calendar theCa = new GregorianCalendar();
			theCa.setTime(new Date());
		   
			 nowDate = theCa.get(Calendar.YEAR)+"-"
	       +(theCa.get(Calendar.MONTH)+1)+"-"
		   +theCa.get(Calendar.DATE)+" "
		   +(12*theCa.get(Calendar.AM_PM)
		   +theCa.get(Calendar.HOUR))+":"
		   +theCa.get(Calendar.MINUTE)+":"
		   +theCa.get(Calendar.SECOND);
	     }
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		
		return nowDate;
	}
	
    /**
     * 得到折扣率
     * @return
     */
    public UFDouble getDiscountrate(){
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    	String sql = "select discountrate from eh_discountrate where pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0";
    	UFDouble discountrate = null;
    	try {
			discountrate = new UFDouble(iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?"100":iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		discountrate = discountrate.div(100);
		return discountrate;
    }
    
    /**
    功能：把源计量单位转换成目标计量单位，返回其转换率,如克->千克为(千克换算率/克换算率)得出
    作者：newyear
    日期：2008-6-12 下午07:13:16
    @param souerMeasdoc     源计量单位
    @param targetMeasdoc    目标计量单位
    @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getMeasRate(String souerMeasdoc,String targetMeasdoc) {
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        UFDouble rate = null;
        try{
            String sql = "  select pk_measdoc,oppdimen,scalefactor from bd_measdoc where pk_measdoc in ('"+souerMeasdoc+"','"+targetMeasdoc+"')"+
                         " and isnull(dr,0)=0 ";
            ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
            if(al==null || al.size()==0)
                return new UFDouble(-1);
            else{
                HashMap hm1 = (HashMap)al.get(0);
                HashMap hm2 = (HashMap)al.get(1);
                
                if (hm1==null || hm2==null || hm1.size()==0 || hm2.size()==0)
                    return new UFDouble(-1);
                
                //分别对二个结果集进行处理
                //1.计量单位主键
                String pk_measdoc1 = hm1.get("pk_measdoc")==null?"":hm1.get("pk_measdoc").toString();
                @SuppressWarnings("unused")
				String pk_measdoc2 = hm2.get("pk_measdoc")==null?"":hm2.get("pk_measdoc").toString();
                //2.所属量钢
                String oppdimen1 = hm1.get("oppdimen")==null?"":hm1.get("oppdimen").toString();
                String oppdimen2 = hm2.get("oppdimen")==null?"":hm2.get("oppdimen").toString();
                //3.换算关系
                UFDouble scalefactor1 = new UFDouble(hm1.get("scalefactor")==null?"-1":hm1.get("scalefactor").toString());
                UFDouble scalefactor2 = new UFDouble(hm2.get("scalefactor")==null?"-1":hm2.get("scalefactor").toString());
                
                //首先判断二个是否属于同一个纲领
                if (!oppdimen1.equalsIgnoreCase(oppdimen2))
                    return new UFDouble(-1);
                //不能没有换算关系 
                if(scalefactor1.equals("-1") || scalefactor2.equals("-1")){
                    return new UFDouble(-1);
                }
                
                //接下来才是真正的换算
                //计算方法：rate = 目标计量单位换算率/源计量单位换算率
                if (souerMeasdoc.equalsIgnoreCase(pk_measdoc1))
                    rate = scalefactor1.div(scalefactor2);
                else
                    rate = scalefactor2.div(scalefactor1);
    
            }
            
            
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return rate;
    }
    
    /**
     * 得到一个物料的所有单位转换率
     * @param pk_invbasdoc
     * @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getInvRate(String pk_invbasdoc,String pk_targetMeasdoc){
    	@SuppressWarnings("unused")
		HashMap hm = new HashMap();
    	StringBuffer sql = new StringBuffer()
        .append(" select c.pk_measdoc,a.def3 price,b.mainmeasrate changerate from bd_invmandoc d,bd_invbasdoc a,bd_convert b,bd_measdoc c")
        .append(" where d.pk_invbasdoc = a.pk_invbasdoc and a.pk_invbasdoc = b.pk_invbasdoc")
        .append(" and b.pk_measdoc = c.pk_measdoc")
        .append(" and d.pk_invmandoc = '"+pk_invbasdoc+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 ")
        .append(" union all ")
        .append(" select c.pk_measdoc,a.def3 price,1 changerate")
        .append(" from  bd_invmandoc d,bd_invbasdoc a,bd_convert b,bd_measdoc c")
        .append(" where d.pk_invbasdoc = a.pk_invbasdoc and a.pk_invbasdoc = b.pk_invbasdoc and a.pk_measdoc = c.pk_measdoc")
        .append(" and d.pk_invmandoc = '"+pk_invbasdoc+"' and nvl(a.dr,0)=0 and nvl(c.dr,0)=0");

        try {
        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
		    if(vc!=null&&vc.size()>0){
		    	for(int i=0; i<vc.size(); i++){
		    		Vector vcc = (Vector)vc.get(i);
		    		String pk_measdoc = vcc.get(0)==null?"":vcc.get(0).toString();
		    		UFDouble changerate = new UFDouble(vcc.get(2)==null?"0":vcc.get(2).toString());
		    		if(pk_measdoc.equalsIgnoreCase(pk_targetMeasdoc))
                        return changerate;
		    	}
		    }
         }catch (BusinessException e1) {
 			e1.printStackTrace();
 		}
        return new UFDouble(-1);
    }
    
    /**
     * 根据物料找出库存量
     * @author wb
     * 2008-6-14 10:46:40
     * @param pk_invbasdoc
     * @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getKCamountByinv_Back(String pk_invbasdoc,String pk_corp,UFDate date){
    	UFDouble amount = new UFDouble(0);
    	try {
    	    
    	String sql = "select nvl(b.is_flag,'1') flag from bd_invmandoc a,eh_stordoc b where a.def1=b.pk_bdstordoc and a.pk_invmandoc='"+pk_invbasdoc+"'";    
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	Map map = (Map) iUAPQueryBS.executeQuery(sql,new MapProcessor());
    	
    	String flag = "1";
    	if(null != map && null != map.get("flag")){
    	    flag =  map.get("flag").toString() ;
    	}
			HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc,date,flag,pk_corp);
			amount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         return amount;
    }
    
    /**
     * 根据物料仓库仓位找出库存量
     * @author wb
     * 2008-6-14 10:46:40
     * @param pk_invbasdoc
     * @param pk_store
     * @param pk_pos
     * @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getKCamount_Back(KcVO kcVO,UFDate date){
    	UFDouble amount = new UFDouble(0);
    	String pk_store = kcVO.getPk_store();
    	String pk_invbasdoc = kcVO.getPk_invbasdoc();
    	String pk_corp = kcVO.getPk_corp();

		try {
			HashMap hmkc = new PubTools().getDateinvKC(pk_store, pk_invbasdoc,date, "0",pk_corp);
			amount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
         return amount;
    }
    
    /**
     * 根据仓库,物料按照取单价的方式计算材料出库时的单价
     * @param kcVO
     * @param type 取数方式 1为取上月库存月报表的价格,2为(本月期初金额+本月出库金额)/(本月期初数量+本月出库金额),其他方式在以后补充
     * @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getCKPrice(CalcKcybbVO kcVO,int type){
    	String date =kcVO.getPk_period().substring(0, 7);    // 年度+月份 2008-07
    	int nyear = kcVO.getNyear();         //年度
    	int nmonth = kcVO.getNmonth();       //月份
    	int pyear = nyear,pmonth =0;
        if(nmonth==1){
            pmonth=12;
            pyear = nyear - 1;
        }else
            pmonth = nmonth - 1;
        
        
    	UFDouble price = new UFDouble(0);
    	if(type==1){                            // 取上月库存月报表的价格
	    	StringBuffer sql = new StringBuffer()
	    	.append(" select b.qmje from eh_calc_kcybb a,eh_calc_kcybb_b b ")
	    	.append(" where a.pk_kcybb = b.pk_kcybb")
	    	.append(" and a.pk_store = '").append(kcVO.getPk_store())
	    	.append("' and b.pk_invbasdoc = '"+kcVO.getPk_kcybb()+"'")
	    	.append(" and a.nyear = '"+pyear+"' and a.month = '"+pmonth+"' and a.pk_corp = '"+kcVO.getPk_corp()+"' ")
	    	.append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0");
	    	try {
	         	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	         	price = new UFDouble(iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor())==null?"0":
	         		 				iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor()).toString());
	    	 }catch(Exception ex){
	    		 ex.printStackTrace();
	    	 }
    	} 
    	if(type==2){                          // (本月期初金额+本月出库金额)/(本月期初数量+本月出库数量)
    		StringBuffer qcSQL = new StringBuffer()
            .append(" select b.pk_invbasdoc,sum(isnull(b.qmsl,0)) qcsl ,sum(isnull(b.qmje,0)) qcje")
            .append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")
            .append(" where a.pk_kcybb = b.pk_kcybb")
            .append(" and a.nyear='"+pyear+"' and a.nmonth = '"+pmonth+"' and a.pk_corp = '"+kcVO.getPk_corp()+"' and a.pk_store = '").append(kcVO.getPk_store())
            .append("' and b.pk_invbasdoc = '"+kcVO.getPk_kcybb()+"'")
            .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0")
            .append(" group by b.pk_invbasdoc");
    		
    		StringBuffer ckSQL = new StringBuffer()
            .append(" select case when pk_tdinvbasdoc is null then pk_invbasdoc else  pk_tdinvbasdoc end pk_invbasdoc,sum(isnull(b.blmount,0)) cksl,sum(isnull(b.summoney,0)) ckje")
            .append(" from eh_sc_ckd a, eh_sc_ckd_b b")
            .append(" where a.pk_ckd = b.pk_ckd")
            .append(" and b.pk_store = '"+kcVO.getPk_store()+"'")
            .append(" and (b.pk_invbasdoc = '"+kcVO.getPk_kcybb()+"' or b.pk_tdinvbasdoc = '"+kcVO.getPk_kcybb()+"' )")
            .append(" and a.pk_corp='"+kcVO.getPk_corp()+"' and a.dmakedate like '"+date+"%' ")
            .append(" and a.vbillstatus= ").append(IBillStatus.CHECKPASS)                  // 审批通过
            .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0")
            .append(" group by pk_tdinvbasdoc,pk_invbasdoc");
    		try {
	         	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	         	ArrayList qcArr = (ArrayList) iUAPQueryBS.executeQuery(qcSQL.toString(), new MapListProcessor());
	         	ArrayList ckArr = (ArrayList) iUAPQueryBS.executeQuery(ckSQL.toString(), new MapListProcessor());
	         	UFDouble qcamount = new UFDouble(0);
	         	UFDouble qcje = new UFDouble(0);
	         	UFDouble ckamount = new UFDouble(0);
	         	UFDouble ckje = new UFDouble(0);
	         	if(qcArr!=null && qcArr.size()>0){
	         		HashMap qcHM = (HashMap)qcArr.get(0);
	         		qcamount = new UFDouble(qcHM.get("qcsl")==null?"0":qcHM.get("qcsl").toString());   // 期初数量
	         		qcje = new UFDouble(qcHM.get("qcje")==null?"0":qcHM.get("qcje").toString());   	   // 期初金额
	         	}
	         	if(ckArr!=null && ckArr.size()>0){
	         		HashMap ckHM = (HashMap)ckArr.get(0);
	         		ckamount = new UFDouble(ckHM.get("cksl")==null?"0":ckHM.get("cksl").toString());   // 本月出库数量
	         		ckje = new UFDouble(ckHM.get("ckje")==null?"0":ckHM.get("ckje").toString());   	   // 本月出库金额
	         	}
	         	UFDouble amount = qcamount.add(ckamount)== new UFDouble(0)?new UFDouble(-1):qcamount.add(ckamount);
	         	price = (qcje.add(ckje)).div(amount);
    		}catch(Exception ex){
	    		 ex.printStackTrace();
	    	 }

    	}
         return price;
    }
  //审批时卡片按钮
    public static int[] getSPCButton(){
    	int [] CardButton=new int[]{
    			IBillButton.Refresh,
                IBillButton.Audit,
                IBillButton.Print,
                IBillButton.Return
    	};
    	return CardButton;
    }
    //审批时列表按钮
    public static int[] getSPLButton(){
    	int [] ListButton=new int[]{
                IBillButton.Query,
                IBillButton.Card,
                IBillButton.Refresh
    	};
    	return ListButton;
    }
    //数量一次下达的情况进行回写
    public static void fullChange(CircularlyAccessibleValueObject[] vos ,String yname,String name ,String sql){
 	   int sum=0;
 	   for(int i=0;i<vos.length;i++){
 		   UFDouble yamount=new UFDouble(vos[i].getAttributeValue(yname)==null?"":vos[i].getAttributeValue(yname).toString());
 		   UFDouble amount = new UFDouble(vos[i].getAttributeValue(name)==null?"":vos[i].getAttributeValue(name).toString());
 		   if(yamount.toDouble()==0 && yamount.toDouble()==amount.toDouble()){
 			   sum=sum+1;
 		   }
 	   }
 	   if(sum==vos.length-1){
 		   PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
 		   try {
 			pubitf.updateSQL(sql);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	   }
    }
    
    /**
	    功能：把源计量单位转换成目标计量单位，返回其转换率,如吨->袋
	    注：将计量档案和物料档案的单位转换集中的一个方法
	    作者：wb
	    日期：2008-6-25 12:44:14
	    @param souerMeasdoc     源计量单位
	    @param targetMeasdoc    目标计量单位
	    @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getInvMeasRate(String pk_invbasdoc,String souerMeasdoc,String targetMeasdoc) {
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        UFDouble rate = new UFDouble(0);
        try{
            String sql = "  select pk_measdoc,oppdimen,scalefactor from bd_measdoc where pk_measdoc in ('"+souerMeasdoc+"','"+targetMeasdoc+"')"+
                         " and isnull(dr,0)=0 ";
            ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
            if(al==null || al.size()==0)
            	rate = new UFDouble(-1);
            else{
                HashMap hm1 = (HashMap)al.get(0);
                HashMap hm2 = (HashMap)al.get(1);
                
                if (hm1==null || hm2==null || hm1.size()==0 || hm2.size()==0)
                	rate = new UFDouble(-1);
                
                //分别对二个结果集进行处理
                //1.计量单位主键
                String pk_measdoc1 = hm1.get("pk_measdoc")==null?"":hm1.get("pk_measdoc").toString();
                @SuppressWarnings("unused")
				String pk_measdoc2 = hm2.get("pk_measdoc")==null?"":hm2.get("pk_measdoc").toString();
                //2.所属量钢
                String oppdimen1 = hm1.get("oppdimen")==null?"":hm1.get("oppdimen").toString();
                String oppdimen2 = hm2.get("oppdimen")==null?"":hm2.get("oppdimen").toString();
                //3.换算关系
                UFDouble scalefactor1 = new UFDouble(hm1.get("scalefactor")==null?"-1":hm1.get("scalefactor").toString());
                UFDouble scalefactor2 = new UFDouble(hm2.get("scalefactor")==null?"-1":hm2.get("scalefactor").toString());
                
                //首先判断二个是否属于同一个纲领
                if (!oppdimen1.equalsIgnoreCase(oppdimen2))
                	rate = new UFDouble(-1);
                //不能没有换算关系 
                if(scalefactor1.equals("-1") || scalefactor2.equals("-1")){
                	rate = new UFDouble(-1);
                }
                
                if(rate.compareTo(new UFDouble(-1))==0){
                	//在计量档案中无法得到转换率时到物料档案中找转换率
                	rate = getInvRate233(pk_invbasdoc,souerMeasdoc, targetMeasdoc);
                    return rate;
                }
                //接下来才是真正的换算
                //计算方法：rate = 目标计量单位换算率/源计量单位换算率
            	if (souerMeasdoc.equalsIgnoreCase(pk_measdoc1))
                    rate = scalefactor2.div(scalefactor1);
                else
                    rate = scalefactor1.div(scalefactor2);
            	
                
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        return rate;
    }
    
    /**
     * 功能：把一物料的源计量单位转换成目标计量单位，返回其转换率,如吨->袋
     * 在上面 getInvMeasRate()方法中调用
     * */
    @SuppressWarnings("unchecked")
	public UFDouble getInvRate233(String pk_invbasdoc,String pk_sourceMeasdoc,String pk_targetMeasdoc){
    	UFDouble rate = null;
    	StringBuffer sql = new StringBuffer()
        .append(" select b.pk_measdoc,b.changerate from eh_invbasdoc a,eh_invbasdoc_b b")
        .append(" where a.pk_invbasdoc = b.pk_invbasdoc")
        .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and b.pk_measdoc in ('"+pk_sourceMeasdoc+"','"+pk_targetMeasdoc+"') and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 ")
        .append(" union all ")
        .append(" select a.pk_measdoc,1 changerate")
        .append(" from  eh_invbasdoc a")
        .append(" where  a.pk_invbasdoc = '"+pk_invbasdoc+"' and a.pk_measdoc in ('"+pk_sourceMeasdoc+"','"+pk_targetMeasdoc+"') and isnull(a.dr,0)=0");

        try {
        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(al==null || al.size()==0||al.size()==1)
            	rate = new UFDouble(-1);
            else{
                HashMap hm1 = (HashMap)al.get(0);
                HashMap hm2 = (HashMap)al.get(1);
	    		String pk_measdocA = hm1.get("pk_measdoc").toString();
	    		UFDouble changerateA = new UFDouble(hm1.get("changerate")==null?"-1":hm1.get("changerate").toString());
	    		UFDouble changerateB = new UFDouble(hm2.get("changerate")==null?"-1":hm2.get("changerate").toString());
	    		if (pk_sourceMeasdoc.equalsIgnoreCase(pk_measdocA))
                    rate = changerateB.div(changerateA);
                else
                    rate = changerateA.div(changerateB);
		    }
         }catch (BusinessException e1) {
 			e1.printStackTrace();
 		}
        return rate;
    }
    
   /**
    * 
    * @param bvos  			下游子表VO的集合
    * @param yname 			保存后的数量名称
    * @param name			保存时的数量名称
    * @param mount			上游一共的数量名称
    * @param mainPK			存放上游主表主键PK字段名称
    * @param childpk		存放上游子表主键PK字段名称
    * @param maintablename	上游主表的名称
    * @param childtablename	上游子表的名称
    * @param flag			修改的标记（主子表要同名）
    * @param pkname			上游主表主键的名称
    * @param pk_bname		上游子表主键的名称
    * @throws Exception 
    */
    @SuppressWarnings("unchecked")
	public static void fullChange(CircularlyAccessibleValueObject[] bvos ,String yname,String name ,String mount,String mainPK,String childpk,String maintablename,String childtablename,String mainflag,String childflag,String pkname,String pk_bname,HashMap hm3) throws Exception{
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
 	   //对表体的回写
    	String [] pk_b=new String [bvos.length];//放的是表体的需要回写PK集合
    	String [] pk_bs=new String [bvos.length];//放的是全部表体PK集合
    	String [] pk=new String [bvos.length];//放的是全部上游主表PK集合
 	   for(int i=0;i<bvos.length;i++){
 		   String pk_bc=bvos[i].getAttributeValue(childpk)==null?"":bvos[i].getAttributeValue(childpk).toString();
 		  UFDouble yamount=new UFDouble(hm3.get(pk_bc)==null?"":hm3.get(pk_bc).toString());
// 		  UFDouble yamount=new UFDouble(bvos[i].getAttributeValue(yname)==null?"":bvos[i].getAttributeValue(yname).toString());
 		  UFDouble inamount = new UFDouble(bvos[i].getAttributeValue(name)==null?"":bvos[i].getAttributeValue(name).toString());
 		  UFDouble amount=new UFDouble(bvos[i].getAttributeValue(mount)==null?"":bvos[i].getAttributeValue(mount).toString());
 		  double amou=amount.sub((yamount.add(inamount))).doubleValue();
 		  pk[i]=bvos[i].getAttributeValue(mainPK)==null?"":bvos[i].getAttributeValue(mainPK).toString();
 		  pk_bs[i]=bvos[i].getAttributeValue(childpk)==null?"":bvos[i].getAttributeValue(childpk).toString(); 
 		  pk_b[i]="";
 		   if(amou==0){
 			  pk_b[i]=bvos[i].getAttributeValue(childpk)==null?"":bvos[i].getAttributeValue(childpk).toString(); 
 		   }
 	   }
 	  String pk_b_one=combinArrayToString(pk_b);
 	  String pk_b_ones=combinArrayToString(pk_bs);
 	  String pk_one=combinArrayToString(pk);
 	  String sql1="update "+childtablename+"  set "+childflag+"='N' where "+pk_bname+" in "+pk_b_ones+" and isnull(dr,0)=0 ";
 	  String sql2="update "+childtablename+"  set "+childflag+"='Y' where "+pk_bname+" in "+pk_b_one+" and isnull(dr,0)=0 ";
 	  PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
 	  pubitf.updateSQL(sql1);
 	  pubitf.updateSQL(sql2);
 	  
 	 //对表头判断
 	  String sql3="select count(*) amount,"+pkname+" pk,'A' flag from "+childtablename+" where "+pkname+" in"+pk_one+" " +
			 		" and  "+childflag+"='Y' group by "+pkname+" union all  select count(*) amount,"+pkname+" pk ,'B' flag from " +
			 		" "+childtablename+" where "+pkname+" in "+pk_one+" group by "+pkname+" ";
 	 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql3, new MapListProcessor());
	 HashMap hm1=new HashMap();//放的做标记的个
	 HashMap hm2=new HashMap();//放的一共的个数
	 ArrayList alPK=new ArrayList();
	 String [] fullflag =new String [al.size()];
	 for(int i=0;i<al.size();i++){
		 HashMap hm=(HashMap) al.get(i);
		 UFDouble amountc =new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
		 String pkc=hm.get("pk")==null?"":hm.get("pk").toString();
		 String flagc=hm.get("flag")==null?"":hm.get("flag").toString();
		 if(flagc.equals("A")){
			 hm1.put(pkc, amountc);
			 fullflag[i]="";
		 }
		 if(flagc.equals("B")){
			 hm2.put(pkc, amountc);
			 alPK.add(pkc);
			 fullflag[i]=pkc;
		 }
	 }
	 String[] samepk=new String[alPK.size()];//对比后能关闭表
	 for(int i=0;i<alPK.size();i++){
		 String pk_same=alPK.get(i)==null?"":alPK.get(i).toString();
		 double amountflag=new UFDouble(hm1.get(pk_same)==null?"-1000":hm1.get(pk_same).toString()).doubleValue();
		 double amount=new UFDouble(hm2.get(pk_same)==null?"-2000":hm2.get(pk_same).toString()).doubleValue();
		 
		 if(amountflag==amount){
			 samepk[i]=pk_same;
		 }else{
			 samepk[i]="";
		 }
	 }
	 String backpk_order=combinArrayToString(samepk);//要回写的主表
	 String fullpk_order=combinArrayToString(fullflag);//要回写的主表
	 String sql4="update "+maintablename+" set "+mainflag+"='Y' where "+pkname+" in"+backpk_order;
	 String sql5="update "+maintablename+" set "+mainflag+"='N' where "+pkname+" in"+fullpk_order;
	 pubitf.updateSQL(sql5);
	 pubitf.updateSQL(sql4);
 	  
    }
    
    /** 
     * 根据制单日期查询一个物料的牌价,
     * @param pk_invbasdoc 物料管理档案PK
     * @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getInvPrice(String pk_invbasdoc,UFDate date){
    	//String sql = "select def3 from bd_invbasdoc where pk_invbasdoc=(" +
    	//		" select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') and nvl(dr,0)=0";
    	//modify by houcq 2011-04-13 牌价来源从牌价调整单取，不从基本存货管理档案取    	
    	//String sql = "select newprice from eh_price_b  where substr(pk_price,1,4)='"+ce.getCorporation().getPk_corp()+"' and nvl(dr,0)=0 and pk_invbasdoc ='"+pk_invbasdoc+"' order by ts desc" ;
    	//modify by houcq 2011-04-18 牌价来源从牌价调整单取，不从基本存货管理档案取  
    	StringBuilder sb = new StringBuilder()
    	.append(" select b.newprice newprice from eh_price a,eh_price_b b ")
    	.append(" where a.pk_price = b.pk_price")
    	.append(" and a.vbillstatus=1")
    	.append(" and nvl(a.dr,0)=0")
    	.append(" and nvl(b.dr,0)=0")
    	.append(" and a.pk_corp='")
    	.append(ce.getCorporation().getPk_corp())
    	.append("' and '")
    	.append(date)
    	.append("' between a.zxdate and a.yxdate ")
    	.append(" and b.pk_invbasdoc='")
    	.append(pk_invbasdoc)
    	//.append("' order by a.dapprovedate desc ");
    	.append("' order by to_date(nvl(a.dapprovetime,a.dapprovedate),'yyyy-mm-dd:hh24:mi:ss') desc ");//modify by houcq 2011-04-25
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	
    	UFDouble price = new UFDouble(0);
    	try {
			//Object objprice = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
			//ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());	
			ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sb.toString(), new MapListProcessor());	
			if(arr!=null &&arr.size()>0){
				HashMap hm =(HashMap) arr.get(0);
				price = new UFDouble(hm.get("newprice").toString());
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
    	return price;
    }
    
    /**
     * 说明:根据表名,当前日期生成批次号
     * wb 2008-7-17 16:11:42
     * @param tablename
     * @param nowdate
     * @return
     */
    @SuppressWarnings("unchecked")
	public static String getPC(String tablename,UFDate nowdate){
    	@SuppressWarnings("unused")
		String pc = null;
    	String strdate = nowdate.getYear()+nowdate.toString().substring(5,7)+nowdate.toString().substring(8,10);
    	String sql = "select count(*) pc from "+tablename+" where dmakedate = '"+nowdate+"'";
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
         try {
             HashMap hm=(HashMap)iUAPQueryBS.executeQuery(sql.toString(),new MapProcessor());
             DecimalFormat df = new DecimalFormat("000");

             if(hm!=null&&!hm.isEmpty()){
                 String num = hm.get("pc").toString().trim();
                 pc = strdate+"-"+df.format((Integer.parseInt(num)+1));
             }else{
                 pc =strdate+"-001";
             }
         } catch (BusinessException e) {
 			e.printStackTrace();
 		}
     	return null;
    }
    
    /**
     * 得到盘点差异比例
     * wb 2008-10-20 11:18:49
     * @return
     */
    public UFDouble getPdrate(){
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    	String sql = "select checkrate from eh_store_checkrate where pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0";
    	UFDouble discountrate = null;
    	try {
    		Object objrate = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
			discountrate = new UFDouble(objrate==null?"100":objrate.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		discountrate = discountrate.div(100);
		return discountrate;
    }
    
    /**
     * 安全库存量
     * wb 2008年10月21日13:59:13
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap getInvSafeKC(String pk_invbasdocly){
    	HashMap hm = new HashMap();
    	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    	if(pk_invbasdocly==null){
    		pk_invbasdocly =  "%%";
    	}
    	String sql = "select pk_invbasdoc,safeamount from eh_sc_safekc where pk_invbasdoc like '"+pk_invbasdocly+"' and pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0";
    	String pk_invbasdoc = null;	
    	UFDouble safekcamount = new UFDouble(0);
    	try {
    		ArrayList  arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
    			for(int i=0;i<arr.size();i++){
    				HashMap hmA = (HashMap)arr.get(i);
    				pk_invbasdoc = hmA.get("pk_invbasdoc")==null?null:hmA.get("pk_invbasdoc").toString();
    				safekcamount = new UFDouble(hmA.get("safeamount")==null?"0":hmA.get("safeamount").toString());
    				hm.put(pk_invbasdoc, safekcamount);
    			}
    		}  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hm;
    }
    
    /**得到某个日期某个仓库所有物料当天的库存数量
     * 应用于盘点单及查询具体日期的库存数量
     * @param pk_stroe
     * @param pk_invbasdoc
     * @param calcdate
     * @param invtype 原料 0 成品 1 , 
     * @return
     * @throws Exception
     * @author blueskye 2008-11-27 19:13:22
     */
    @SuppressWarnings({ "unchecked" })
	public HashMap getDateinvKC(String pk_store,String pk_invbasdoc,UFDate calcdate,String invtype,String pk_corp) throws Exception{
    	HashMap hm = new HashMap();
    	String calcSQL = getCalcKcSql(pk_corp, calcdate, pk_store, pk_invbasdoc, invtype);
    	//String calcSQL = getKcSql(pk_corp, calcdate, pk_store, pk_invbasdoc, invtype);
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                    
        ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(calcSQL.toString(), new MapListProcessor());
        if(all!=null && all.size()>0){
            for(int i=0;i<all.size();i++){
                HashMap hmA = (HashMap)all.get(i);
                String pk_invbasdocca = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
                UFDouble calckc = new UFDouble(hmA.get("amount")==null?"0":hmA.get("amount").toString());
                hm.put(pk_invbasdocca,calckc);
            }
        }
        return hm;
    }
    
    public static UFDate[] getLastDate(UFDate calcDate){
		 UFDate[] date = new UFDate[5];
		 UFDate lastbegindate = null;
		 UFDate lastenddate = null;
		 UFDate calcbegindate = null;
		 DecimalFormat df = new DecimalFormat("00"); 
		 int nyear = calcDate.getYear();
         int nmonth = calcDate.getMonth();
         String strmonth = df.format(nmonth);
         String day = df.format(calcDate.getDay());
         //1.取上月的期末数量
         int pyear = nyear,pmonth =0;
         if(nmonth==1){
             pmonth=12;
             pyear = nyear - 1;
         }else
             pmonth = nmonth - 1;
         String strpmonth = df.format(pmonth);
         calcbegindate = new UFDate(nyear+"-"+strmonth+"-"+"01");
         lastbegindate = new UFDate(pyear+"-"+strpmonth+"-"+"01");
         int days = lastbegindate.getDaysMonth();
         if(calcDate.getDaysMonth()!=Integer.valueOf(day)){
        	 days =  Integer.valueOf(day); 
        	 if(lastbegindate.getMonth()==2&&Integer.valueOf(day)>=lastbegindate.getDaysMonth()){  //与上个月是2月时特殊处理
        		 days = lastbegindate.getDaysMonth();
        	 }
         }
         lastenddate = new UFDate(pyear+"-"+strpmonth+"-"+days);
         UFDate lastyearbegindate=new UFDate((nyear-1)+"-"+strmonth+"-"+"01");
         UFDate lastyearenddate=new UFDate((nyear-1)+"-"+strmonth+"-"+day);
         date[0] = lastbegindate;
         date[1] = lastenddate;
         date[2] = calcbegindate;
         date[3]=lastyearbegindate;
         date[4]=lastyearenddate;
         
		 return date;
	 }   
    /***
     * 计算某天物料的库存sql
     * @param pk_corp
     * @param calcdate
     * @param pk_store
     * @param pk_invbasdoc
     * @param invtype
     * @return
     */
    @SuppressWarnings("static-access")
	public String getCalcKcSql(String pk_corp,UFDate calcdate,String pk_store,String pk_invbasdoc,String invtype){
    	if(pk_invbasdoc==null){
    		pk_invbasdoc = "%%";
    	}
    	if(pk_store==null){
    		pk_store = "%%";
    	}
    	UFDate[] date6= this.getLastDate(calcdate);//日期08.7.5
		@SuppressWarnings("unused")
		UFDate lastenddate=date6[1]; 		//08.6.5
		UFDate calcbegindate=date6[2];		//08.7.1
		int nmonth = calcdate.getMonth();
		int nyear = calcdate.getYear();
		int pyear = nyear,pmonth =0;
        if(nmonth==1){
            pmonth=12;
            pyear = nyear - 1;
        }else
            pmonth = nmonth - 1;
        
		StringBuffer br  = null;
    	if(invtype.equals("1")){   //成品库存SQL
			br = new StringBuffer();
			br.append(" select  	");//成品
			br.append("	t.pk_invbasdoc pk_invbasdoc,d.def1 pk_stordoc,c.pk_measdoc pk_measdoc,");
			br.append("    CAST ( sum(t.zrkc)+sum(t.liscrk)+sum(t.ljhbrk)+sum(t.ljthrk)-sum(t.ljxsck)-sum(t.ljhbck)-sum(t.ljhjck)AS DECIMAL(20,5)) amount ");
			br.append("	from ( ");
			br.append("	select  	b.pk_invbasdoc pk_invbasdoc, 	0 zrkc, 	");
			br.append("	sum(nvl(rkmount,0)) drscrk, 	0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	");
			br.append("	0 drthrk, 	0 ljthrk, 	0 drxsck, 	0 ljxsck, 	0 drhbck, 	0 ljhbck, 	");
			br.append("	0 drhjck, 	0 ljhjck,0 zcl  from 	eh_sc_cprkd a,eh_sc_cprkd_b b  ");
			br.append("	where  	a.pk_rkd=b.pk_rkd and  a.dmakedate='"+calcdate+"' ");
			br.append("	and  vbilltype='ZA47'  	and nvl(a.dr,0)=0 and nvl(b.dr,0)=0    	");
			br.append("	and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
			br.append("	and pk_intype='"+ISQLChange.INTYPE_SC+"' group by  	b.pk_invbasdoc  ");
			br.append(" union all  ");
			br.append("    select  	b.pk_invbasdoc pk_invbasdoc, 	0 zrkc, 	0 drscrk, 	");
			br.append("    sum(nvl(rkmount,0)) liscrk, 	0 drhbrk, 	0 ljhbrk, 	0 drthrk, 	");
			br.append("    0 ljthrk, 	0 drxsck, 	0 ljxsck, 	0 drhbck, 	0 ljhbck, 	0 drhjck, 	");
			br.append("    0 ljhjck,0 zcl  from 	eh_sc_cprkd a,eh_sc_cprkd_b b  ");
			br.append("    where  	a.pk_rkd=b.pk_rkd and  a.dmakedate>='"+calcbegindate+"' ");
			br.append("    and  a.dmakedate<='"+calcdate+"' 	and nvl(a.dr,0)=0 ");
			br.append("    and nvl(b.dr,0)=0 and  vbilltype='ZA47' ");
			br.append("    and pk_intype='"+ISQLChange.INTYPE_SC+"'  	and a.vbillstatus=1 ");
			br.append("    and a.pk_corp='"+pk_corp+"' group by  	b.pk_invbasdoc  ");
			br.append(" union all  ");
			br.append("    select  	b.pk_dr_inv pk_invbasdoc, 	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	sum(nvl(b.drrmount,0)) drhbrk, 	0 ljhbrk, 	0 drthrk, 	");
			br.append("    0 ljthrk, 	0 drxsck, 	0 ljxsck, 	0 drhbck, 	0 ljhbck, 	0 drhjck, 	");
			br.append("    0 ljhjck,0 zcl  from 	eh_sc_dbd a,eh_sc_dbd_b b  where  	");
			br.append("    a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+calcdate+"' 	and nvl(a.dr,0)=0 ");
			br.append("    and nvl(b.dr,0)=0   	and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
			br.append("    and pk_intype='"+ISQLChange.INTYPE_HB+"' and dbtype=1  group by  	");
			br.append("    b.pk_dr_inv  ");
			br.append(" union all  ");
			br.append("    select  	b.pk_dr_inv pk_invbasdoc,  	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	0 drhbrk, 	sum(nvl(b.drrmount,0)) ljhbrk, 	0 drthrk, 	");
			br.append("    0 ljthrk, 	0 drxsck, 	0 ljxsck, 	0 drhbck, 	0 ljhbck, 	0 drhjck, 	");
			br.append("    0 ljhjck,0 zcl  from 	eh_sc_dbd a,eh_sc_dbd_b b   where  ");
			br.append("    a.pk_dbd=b.pk_dbd and  a.dmakedate>='"+calcbegindate+"'  ");
			br.append("    and a.dmakedate<='"+calcdate+"' 	and nvl(a.dr,0)=0 ");
			br.append("    and nvl(b.dr,0)=0 and a.pk_corp='"+pk_corp+"' ");
			br.append("    and pk_intype='"+ISQLChange.INTYPE_HB+"' and dbtype=1 	");
			br.append("    and a.vbillstatus=1 group by  	b.pk_dr_inv 	 ");
			br.append(" union all  ");												//退货入库改为取成品出库单中退货入库类型的数据 edit by wb 2009-11-17 17:19:37
			br.append("    select  	b.pk_invbasdoc pk_invbasdoc, 	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	 sum(abs(nvl(b.outamount,0))) drthrk, 	");
			br.append("    0 ljthrk, 	0 drxsck, 	0 ljxsck, 	0 drhbck, 	0 ljhbck, 	0 drhjck, 	");
			br.append("    0 ljhjck,0 zcl  from 	eh_icout a, eh_icout_b b   where  	");
			br.append("    a.pk_icout = b.pk_icout and  a.dmakedate='"+calcdate+"' and a.pk_outtype='"+ISQLChange.INTYPE_TH+"'   	");
			br.append("    and nvl(a.dr,0)=0 and nvl(b.dr,0)=0  	");
			br.append("    and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' group by  	b.pk_invbasdoc   ");
			br.append(" union all  ");															
			br.append("    select  	b.pk_invbasdoc pk_invbasdoc, 	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	0 drthrk, 	");
			br.append("    sum(abs(nvl(b.outamount,0))) ljthrk, 	0 drxsck, 	0 ljxsck, 	0 drhbck, 	");
	 		br.append("    0 ljhbck, 	0 drhjck, 	0 ljhjck,0 zcl  from 	");
			br.append("    eh_icout a, eh_icout_b b  where  	a.pk_icout = b.pk_icout and   ");
			br.append("    a.dmakedate<='"+calcdate+"' and a.dmakedate>='"+calcbegindate+"'  ");
			br.append("    and a.pk_outtype='"+ISQLChange.INTYPE_TH+"'  	and nvl(a.dr,0)=0 and nvl(b.dr,0)=0  	");
			br.append("    and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' group by  	");
			br.append("    b.pk_invbasdoc   ");
			br.append(" union all  ");
			br.append("    select   	b.pk_invbasdoc pk_invbasdoc, 	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	0 drthrk, 	0 ljthrk, 	");
			br.append("    sum(nvl(b.outamount,0)) drxsck, 	0 ljxsck, 	0 drhbck, 	0 ljhbck, 	");
			br.append("    0 drhjck, 	0 ljhjck,0 zcl  from 	eh_icout a,eh_icout_b b  ");
			br.append("    where  	a.pk_icout=b.pk_icout and  a.dmakedate='"+calcdate+"'  	");
			br.append("    and nvl(a.dr,0)=0 and nvl(b.dr,0)=0  	and a.vbillstatus=1 ");
			br.append("    and a.pk_corp='"+pk_corp+"' 	and pk_outtype='"+ISQLChange.OUTTYPE_XS+"'  ");
			br.append("    group by  	b.pk_invbasdoc  ");
			br.append(" union all  ");
			br.append("    select  	b.pk_invbasdoc pk_invbasdoc,  	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	0 drthrk, 	0 ljthrk, 	0 drxsck, 	");
			br.append("    sum(nvl(b.outamount,0)) ljxsck, 	0 drhbck, 	0 ljhbck, 	0 drhjck, 	");
			br.append("    0 ljhjck,0 zcl  from 	eh_icout a,eh_icout_b b  where  	");
			br.append("    a.pk_icout=b.pk_icout and  a.dmakedate<='"+calcdate+"' ");
			br.append("    and a.dmakedate>='"+calcbegindate+"'  	and nvl(a.dr,0)=0 ");
			br.append("    and nvl(b.dr,0)=0  	and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' 	");
			br.append("    and pk_outtype='"+ISQLChange.OUTTYPE_XS+"' group by  	b.pk_invbasdoc  ");
			br.append(" union all  ");
			br.append("    select  	b.pk_dc_inv pk_invbasdoc , 	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	0 drthrk, 	0 ljthrk, 	");
			br.append("    0 drxsck, 	0 ljxsck, 	sum(nvl(b.dcmount,0)) drhbck, 	");
			br.append("    0 ljhbck, 	0 drhjck, 	0 ljhjck,0 zcl  from 	");
			br.append("    eh_sc_dbd a,eh_sc_dbd_b b  where  	a.pk_dbd=b.pk_dbd and  ");
			br.append("    a.dmakedate='"+calcdate+"'     and nvl(a.dr,0)=0 and nvl(b.dr,0)=0  ");
			br.append("    and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
			br.append("    and pk_outtype='"+ISQLChange.OUTTYPE_HB+"' and dbtype=1 group by b.pk_dc_inv  ");
			br.append(" union all  ");
			br.append("    select  	b.pk_invbasdoc pk_invbasdoc, 	sum(nvl(b.qmsl,0)) zrkc, ");
			br.append("    0 drscrk, 	0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	0 drthrk, 	0 ljthrk, 	");
			br.append("    0 drxsck, 	0 ljxsck, 	0 drhbck, 	0 ljhbck, 	0 drhjck, 	");
			br.append("    0 ljhjck,0 zcl   from  	eh_calc_kcybb a,eh_calc_kcybb_b b  	 ");
			br.append("    where  	a.pk_kcybb=b.pk_kcybb and a.invtype='C'  and a.nyear='"+pyear+"' ");
			br.append("    and a.nmonth='"+pmonth+"'   and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 ");
			br.append("    and a.pk_corp='"+pk_corp+"' group by  	 b.pk_invbasdoc 	 ");
			br.append(" union all  ");
			br.append("    select  	b.pk_dc_inv pk_invbasdoc , 	0 zrkc, 	0 drscrk, 	");
			br.append("    0 liscrk, 	0 drhbrk, 	0 ljhbrk, 	0 drthrk, 	0 ljthrk, 	0 drxsck, 	");
			br.append("    0 ljxsck, 	0 drhbck, 	sum(nvl(b.dcmount,0)) ljhbck, 	");
			br.append("    0 drhjck, 	0 ljhjck,0 zcl  from 	eh_sc_dbd a,eh_sc_dbd_b b where  	");
			br.append("    a.pk_dbd=b.pk_dbd and  a.dmakedate>='"+calcbegindate+"'  ");
			br.append("    and a.dmakedate<='"+calcdate+"'     	and nvl(a.dr,0)=0 ");
			br.append("    and nvl(b.dr,0)=0  	and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
			br.append("    and pk_outtype='"+ISQLChange.OUTTYPE_HB+"' and dbtype=1 group by  	");
			br.append("    b.pk_dc_inv ");
			br.append(" union all  	 ");
			br.append("    select b.pk_invbasdoc pk_invbasdoc , 0 zrkc , 0 drscrk , 0 liscrk , ");
			br.append("    0 drhbrk , 0 ljhbrk ,  	0 drthrk , 0 ljthrk ,0  drxsck , 0 ljxsck , ");
			br.append("    0 drhbck , 	 0 ljhbck,sum ( nvl ( b.outamount , 0 ) ) drhjck,0 ljhjck,");
			br.append("    0 zcl  from eh_icout a , eh_icout_b b where a.pk_icout = b.pk_icout  	");
			br.append("    and a.dmakedate = '"+calcdate+"' and nvl ( a.dr , 0 ) = 0 ");
			br.append("    and nvl ( b.dr , 0 ) = 0 	and pk_outtype='"+ISQLChange.OUTTYPE_HJ+"'  	");
			br.append("    and a.vbillstatus = 1 and a.pk_corp = '"+pk_corp+"' ");
			br.append("    group by b.pk_invbasdoc  ");
			br.append(" union all 	");
			br.append("    select b.pk_invbasdoc pk_invbasdoc , 0 zrkc , 0 drscrk , 0 liscrk , ");
			br.append("    0 drhbrk , 0 ljhbrk , 	 0 drthrk , 0 ljthrk , 0 drxsck , 0 ljxsck , ");
			br.append("    0 drhbck ,  	0 ljhbck,0 drhjck,");
			br.append("    sum ( nvl ( b.outamount , 0 ) ) ljhjck,0 zcl ");
			br.append("    from eh_icout a , eh_icout_b b where a.pk_icout = b.pk_icout  	");
			br.append("    and a.dmakedate <= '"+calcdate+"' and a.dmakedate >= '"+calcbegindate+"'  	");
			br.append("    and nvl ( a.dr , 0 ) = 0 and nvl ( b.dr , 0 ) = 0 ");
			br.append("    and a.vbillstatus = 1  	and pk_outtype='"+ISQLChange.OUTTYPE_HJ+"' 	");
			br.append("    and a.pk_corp = '"+pk_corp+"' group by b.pk_invbasdoc ");
			br.append(" union all ");
			br.append("    select  b.pk_invbasdoc pk_invbasdoc ,  0 zrkc , 0 drscrk , ");
			br.append("    0 liscrk , 0 drhbrk , 0 ljhbrk , 0 drthrk ,  0 ljthrk , 0 drxsck , ");
			br.append("    0 ljxsck , 0 drhbck , 0 ljhbck , 0 drhjck , 0 ljhjck, ");
			br.append("    sum ( nvl ( b.pgamount , 0 ) ) zcl from eh_sc_pgd a , eh_sc_pgd_b b ");
			br.append("    where a.pk_pgd = b.pk_pgd  and a.dmakedate = '"+calcdate+"'  ");
			br.append("    and nvl ( a.dr , 0 ) = 0 and nvl ( b.dr , 0 ) = 0  ");
			br.append("    and a.vbillstatus = 3 and nvl(rk_flag,'N')='N' ");
			br.append("    and nvl(lock_flag,'N')='N' and a.pk_corp = '"+pk_corp+"' ");
			br.append("    group by b.pk_invbasdoc  ");
			br.append("	 ) t ,bd_invmandoc d,bd_invbasdoc z,bd_measdoc c");
			br.append("	where  t.pk_invbasdoc=d.pk_invmandoc and d.pk_invbasdoc=z.pk_invbasdoc AND z.pk_measdoc=c.pk_measdoc");
			br.append("	and nvl(z.dr,0)=0 and nvl(d.sealflag,'N')<> 'Y' and t.pk_invbasdoc like '"+pk_invbasdoc+"' and nvl(d.def1,' ') like '"+pk_store+"' AND d.pk_corp = '"+pk_corp+"' ");
			br.append("	group by  t.pk_invbasdoc ,c.pk_measdoc,d.def1");
    	}
		
		if(invtype.equals("0")){				//原料库存的SQL
			br = new StringBuffer();
			br.append("	select  t.pk_invbasdoc pk_invbasdoc,d.def1 pk_stordoc,g.pk_measdoc pk_measdoc,");
			br.append(" sum ( t.ycjc )+sum ( t.rklj )- sum ( t.lisc )-sum ( t.ljqt ) amount");
			br.append(" from ( ");
			br.append("	select b.pk_invbasdoc pk_invbasdoc ,  ");
			br.append("    sum ( nvl ( b.qmsl , 0 ) ) ycjc , 0 rkbr , 0 rklj , 0 scbr , 0 lisc , ");
			br.append("    0 brqt , 0 ljqt  from eh_calc_kcybb a , eh_calc_kcybb_b b  ");
			br.append("	where a.pk_kcybb = b.pk_kcybb and a.invtype = 'Y' and a.nyear = '"+pyear+"'");
			br.append("	and a.nmonth = '"+pmonth+"'  and nvl ( a.dr , 0 ) = 0 ");
			br.append("	and nvl ( b.dr , 0 ) = 0 and a.pk_corp = '"+pk_corp+"'  ");
			br.append("	group by b.pk_invbasdoc  ");
			br.append(" union all ");
			br.append("    select pk_invbasdoc ,0 ycjc,sum(a.rkbr+a.lsdbsl) rkbr, 0 rklj , ");
			br.append("    0 scbr , 0 lisc , 0 brqt , 0 ljqt   from  (");
			br.append("    	select b.pk_invbasdoc pk_invbasdoc , ");
			br.append("    	sum ( nvl ( inamount , 0 ) ) rkbr,0 lsdbsl  ");
			br.append("    	from eh_stock_in a , eh_stock_in_b b  where a.pk_in = b.pk_in ");
			br.append("    	and a.dmakedate = '"+calcdate+"' and nvl ( a.dr , 0 ) = 0  ");
			br.append("    	and nvl ( b.dr , 0 ) = 0 and a.vbillstatus = 1 ");
			br.append("    	and a.pk_corp = '"+pk_corp+"'  group by b.pk_invbasdoc  ");
			br.append("    	 union all  ");
			br.append("    	select b.pk_dr_inv pk_invbasdoc,0 rkbr, ");
			br.append("    	sum(nvl(b.drrmount,0)) lsdbsl from eh_sc_dbd a,eh_sc_dbd_b b  ");
			br.append("    	where a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+calcdate+"' ");
			br.append("    	and nvl(a.dr,0)=0 and nvl(b.dr,0)=0   and a.vbillstatus=1 ");
			br.append("    	and a.pk_corp='"+pk_corp+"' and pk_intype='"+ISQLChange.INTYPE_DB+"' ");
			br.append("    	and dbtype=0 group by  b.pk_dr_inv) a");
			br.append("    group by pk_invbasdoc  ");
			br.append(" union all ");
			br.append("    select pk_invbasdoc ,0 ycjc,0 rkbr,sum(a.rkbr+a.lsdbsl) rklj , 0 scbr , ");
			br.append("    0 lisc , 0 brqt , 0 ljqt   from (select b.pk_invbasdoc pk_invbasdoc , ");
			br.append("    sum ( nvl ( inamount , 0 ) ) rkbr,0 lsdbsl  ");
			br.append("    from eh_stock_in a , eh_stock_in_b b  where a.pk_in = b.pk_in ");
			br.append("    and a.dmakedate >= '"+calcbegindate+"' and  a.dmakedate< = '"+calcdate+"' ");
			br.append("    and nvl ( a.dr , 0 ) = 0  and nvl ( b.dr , 0 ) = 0 ");
			br.append("    and a.vbillstatus = 1 and a.pk_corp = '"+pk_corp+"'  group by b.pk_invbasdoc  ");
			br.append("    union all  ");
			br.append("	select 	 b.pk_dr_inv pk_invbasdoc,0 rkbr, sum(nvl(b.drrmount,0)) lsdbsl ");
			br.append("	from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  ");
			br.append("	and  a.dmakedate>='"+calcbegindate+"' and  a.dmakedate<='"+calcdate+"' ");
			br.append("	and nvl(a.dr,0)=0 and nvl(b.dr,0)=0   and a.vbillstatus=1 ");
			br.append("	and a.pk_corp='"+pk_corp+"' and pk_intype='"+ISQLChange.INTYPE_DB+"' ");
			br.append("	and dbtype=0 group by  b.pk_dr_inv) a group by pk_invbasdoc  ");
			br.append(" union all ");
			br.append("    select pk_invbasdoc, 0 ycjc,0 rkbr,0 rklj  ,");
			br.append("    sum(a.scbr+a.lsqt+a.lsdb) scbr , 0 lisc , 0 brqt , 0 ljqt ");
			br.append("    from  (select b.pk_invbasdoc pk_invbasdoc , ");
			br.append("    sum ( nvl ( blmount , 0 ) ) scbr ,0 lsqt,0 lsdb ");
			br.append("    from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  ");
			br.append("    and a.dmakedate = '"+calcdate+"' and nvl ( a.dr , 0 ) = 0  ");
			br.append("    and nvl ( b.dr , 0 ) = 0 and (vbilltype = 'ZA46' or vbilltype = 'ZB25') and a.pk_corp = '"+pk_corp+"'  ");
			br.append("    and a.vbillstatus = 1 and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' ");
			br.append("    group by b.pk_invbasdoc  ");
			br.append("    union all  select b.pk_invbasdoc pk_invbasdoc , 0 scbr ,");
			br.append("    sum ( nvl ( blmount , 0 ) ) lsqt,0 lsdb from eh_sc_ckd a , ");
			br.append("    eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  and a.dmakedate = '"+calcdate+"' ");
			br.append("    and nvl ( a.dr , 0 ) = 0  and nvl ( b.dr , 0 ) = 0 ");
			br.append("    and vbilltype = 'ZA49' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
			br.append("    and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  ");
			br.append("    union all select b.pk_dr_inv pk_invbasdoc, 0 scbr ,0 lsqt,");
			br.append("    sum(nvl(b.drrmount,0)) lsdb from eh_sc_dbd a,eh_sc_dbd_b b  ");
			br.append("    where a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+calcdate+"' ");
			br.append("    and nvl(a.dr,0)=0 and nvl(b.dr,0)=0   and a.vbillstatus=1 ");
			br.append("    and a.pk_corp='"+pk_corp+"' and pk_outtype='"+ISQLChange.OUTTYPE_DB+"' ");
			br.append("    and dbtype=0 group by  b.pk_dr_inv) a ");
			br.append("    group by pk_invbasdoc ");
			br.append(" union all ");
			br.append("    select pk_invbasdoc, 0 ycjc,0 rkbr,0 rklj  ,0 scbr , ");
			br.append("    sum(a.scbr+a.lsqt+a.lsdb) lisc , 0 brqt , 0 ljqt from  (");
			br.append("    select b.pk_invbasdoc pk_invbasdoc , sum ( nvl ( blmount , 0 ) ) scbr ,");
			br.append("    0 lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  ");
			br.append("    and a.dmakedate> = '"+calcbegindate+"'  and a.dmakedate <= '"+calcdate+"' ");
			br.append("    and nvl ( a.dr , 0 ) = 0  and nvl ( b.dr , 0 ) = 0 ");
			br.append("    and (vbilltype = 'ZA46' or vbilltype = 'ZB25') and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
			br.append("    and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  ");
			br.append("    union all  select b.pk_invbasdoc pk_invbasdoc , 0 scbr ,");
			br.append("    sum ( nvl ( blmount , 0 ) ) lsqt,0 lsdb from eh_sc_ckd a , ");
			br.append("    eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  and a.dmakedate = '"+calcdate+"' ");
			br.append("    and nvl ( a.dr , 0 ) = 0  and nvl ( b.dr , 0 ) = 0 ");
			br.append("    and vbilltype = 'ZA49' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 ");
			br.append("    and  pk_outtype='"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  ");
			br.append("    union all select  b.pk_dr_inv pk_invbasdoc, 0 scbr ,0 lsqt,");
			br.append("    sum(nvl(b.drrmount,0)) lsdb from eh_sc_dbd a,eh_sc_dbd_b b  ");
			br.append("    where a.pk_dbd=b.pk_dbd  and  a.dmakedate<='"+calcdate+"' ");
			br.append("    and  a.dmakedate>='"+calcbegindate+"' and nvl(a.dr,0)=0 ");
			br.append("    and nvl(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
			br.append("    and pk_outtype='"+ISQLChange.OUTTYPE_DB+"' and dbtype=0 ");
			br.append("    group by  b.pk_dr_inv) a ");
			br.append("    group by pk_invbasdoc ");
			br.append(" union all ");
			br.append("    select pk_invbasdoc , 0 ycjc,0 rkbr,0 rklj  ,0 scbr , 0 lisc  ,");
			br.append("    sum(a.scbr+a.lsqt+a.lsdb) brqt,0 ljqt from  (");
			br.append("    select b.pk_invbasdoc pk_invbasdoc , sum ( nvl ( blmount , 0 ) ) scbr ,");
			br.append("    0 lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  ");
			br.append("    and a.dmakedate = '"+calcdate+"' and nvl ( a.dr , 0 ) = 0  ");
			br.append("    and nvl ( b.dr , 0 ) = 0 and (vbilltype = 'ZA46' or vbilltype = 'ZB25') ");
			br.append("    and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 and  pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  union all  select b.pk_invbasdoc pk_invbasdoc , 0 scbr ,sum ( nvl ( blmount , 0 ) ) lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  and a.dmakedate = '"+calcdate+"' and nvl ( a.dr , 0 ) = 0  and nvl ( b.dr , 0 ) = 0 and vbilltype = 'ZA49' and a.pk_corp = '"+pk_corp+"'  and a.vbillstatus = 1 and  pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc  union all select 						 b.pk_dr_inv pk_invbasdoc, 0 scbr ,0 lsqt,sum(nvl(b.drrmount,0)) lsdb from eh_sc_dbd a,eh_sc_dbd_b b  where a.pk_dbd=b.pk_dbd  and  a.dmakedate='"+calcdate+"' and nvl(a.dr,0)=0 and nvl(b.dr,0)=0   and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' and pk_outtype<>'"+ISQLChange.OUTTYPE_DB+"' and dbtype=0 group by  b.pk_dr_inv) a ");
			br.append("    group by pk_invbasdoc ");
			br.append(" union all ");
			br.append("    select pk_invbasdoc , 0 ycjc,0 rkbr,0 rklj  ,0 scbr , 0 lisc  ,");
			br.append("    0 brqt,sum(a.scbr+a.lsqt+a.lsdb) ljqt from (");
			br.append("    select b.pk_invbasdoc pk_invbasdoc , sum ( nvl ( blmount , 0 ) ) scbr ,");
			br.append("    0 lsqt,0 lsdb from eh_sc_ckd a , eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd  ");
			br.append("    and a.dmakedate <= '"+calcdate+"' and a.dmakedate >= '"+calcbegindate+"' ");
			br.append("    and nvl ( a.dr , 0 ) = 0  and nvl ( b.dr , 0 ) = 0 ");
			br.append("    and (vbilltype = 'ZA46' or vbilltype = 'ZB25') and a.pk_corp = '"+pk_corp+"' 	and a.vbillstatus = 1 ");
			br.append("    and  pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc 	");
			br.append("    union all 	select b.pk_invbasdoc pk_invbasdoc ,	0 scbr ,");
			br.append("    sum ( nvl ( blmount , 0 ) ) lsqt,0 lsdb	from eh_sc_ckd a , ");
			br.append("    eh_sc_ckd_b b where a.pk_ckd = b.pk_ckd 	and a.dmakedate <= '"+calcdate+"'");
			br.append("    and a.dmakedate >= '"+calcbegindate+"' and nvl ( a.dr , 0 ) = 0 	");
			br.append("    and nvl ( b.dr , 0 ) = 0 and vbilltype = 'ZA49' ");
			br.append("    and a.pk_corp = '"+pk_corp+"' 	and a.vbillstatus = 1 ");
			br.append("    and  pk_outtype<>'"+ISQLChange.OUTTYPE_SC+"' group by b.pk_invbasdoc 	");
			br.append("    union all	select 							");
			br.append("    b.pk_dr_inv pk_invbasdoc,	0 scbr ,0 lsqt,");
			br.append("    sum(nvl(b.drrmount,0)) lsdb	from	eh_sc_dbd a,eh_sc_dbd_b b 	");
			br.append("    where	a.pk_dbd=b.pk_dbd  and  a.dmakedate<='"+calcdate+"'");
			br.append("    and a.dmakedate >= '"+calcbegindate+"'	and nvl(a.dr,0)=0 ");
			br.append("    and nvl(b.dr,0)=0  	and a.vbillstatus=1 and a.pk_corp='"+pk_corp+"' ");
			br.append("    and pk_outtype<>'"+ISQLChange.OUTTYPE_DB+"' and dbtype=0	group by 	");
			br.append("    b.pk_dr_inv) a");
			br.append("    group by pk_invbasdoc");
			br.append(" ) t,bd_invmandoc d ,bd_invbasdoc z , bd_invcl x ,bd_measdoc g	");
			br.append(" where  t.pk_invbasdoc = d.pk_invmandoc and d.pk_invbasdoc = z.pk_invbasdoc ");
			br.append(" and z.pk_invcl = x.pk_invcl ");
			br.append(" AND z.pk_measdoc=g.pk_measdoc and t.pk_invbasdoc like '"+pk_invbasdoc+"' and nvl(d.def1,' ') like '"+pk_store+"' and nvl(d.sealflag,'N')<> 'Y' AND d.pk_corp = '"+pk_corp+"'");
			br.append(" and nvl(d.dr,0)=0 and nvl(z.dr,0)=0 group by t.pk_invbasdoc , z.invname ,d.def1,g.pk_measdoc");
		}
		return br.toString();
    }
    /***
     * 计算截止到计算日期的某个物料的库存(所有仓库)
     * add by houcq 2011-06-17 17:48     * 
	 * 该物料所有期初+采购入库+产品入库+材料调拨单调入-材料出库-产品出库-材料调拨单调出。
     * @param pk_corp
     * @param calcdate
     * @param pk_store
     * @param pk_invbasdoc
     * @return
     */
    public UFDouble getInvKcAmount(String pk_corp,UFDate calcdate,String pk_invbasdoc){ 
    	int nmonth = calcdate.getMonth();
		int nyear = calcdate.getYear();
		int pyear = nyear;
		int pmonth =0;
        if(nmonth==1){
            pmonth=12;
            pyear = nyear - 1;
        }else
            pmonth = nmonth - 1;
        String beginDate = calcdate.toString().substring(0,8)+"01";
		StringBuffer sb  =  new StringBuffer()
		.append(" select sum(kcamount) kcamount from (")
		.append(" select t.pk_invbasdoc pk_invbasdoc, ")
		.append(" CAST(t.qcamount+t.cgrkamount + t.cprkamount + t.dbdramount")
		.append(" - t.dbdcamount - t.clckamount-t.cpckamount AS DECIMAL(20, 5)) kcamount")
		.append(" from (")
		//期初库存取上月的期末数量
		.append(" select b.pk_invbasdoc pk_invbasdoc,sum(nvl(qmsl,0)) qcamount,")
		.append(" 0 cgrkamount, 0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount, 0 cpckamount")
		.append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")	
		.append(" where a.pk_kcybb = b.pk_kcybb and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")		
		.append(" and a.nyear="+pyear)
		.append(" and a.nmonth="+pmonth)
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" group by b.pk_invbasdoc")
		//--经营采购入库
		.append(" union all")
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" sum(nvl(b.inamount,0)) cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_stock_in a, eh_stock_in_b b")
		.append(" where a.pk_in = b.pk_in")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all")	
		//--成品入库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,sum(nvl(b.rkmount,0)) cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b")        
		.append(" where a.pk_rkd = b.pk_rkd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all")
		//--材料调拨单调入
		.append(" select b.pk_dr_inv pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,sum(nvl(b.drrmount, 0)) dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")		
		.append(" where a.pk_dbd = b.pk_dbd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1")
		.append(" and b.pk_dr_inv='"+pk_invbasdoc+"'")
		.append(" group by b.pk_dr_inv")
		.append(" union all")
		//--材料调拨单调出
		.append(" select b.pk_dc_inv pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,sum(nvl(b.dcmount, 0)) dbdcamount,0 clckamount,0 cpckamount")		
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")
		.append(" where a.pk_dbd = b.pk_dbd")		
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1")
		.append(" and b.pk_dc_inv='"+pk_invbasdoc+"'")
		.append(" group by b.pk_dc_inv")		
		.append(" union all")
		//--材料出库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")		
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,sum(nvl(b.blmount,0)) clckamount,0 cpckamount")
		.append(" from eh_sc_ckd a, eh_sc_ckd_b b")
		.append(" where a.pk_ckd = b.pk_ckd").append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all")	
		//-- 产品出库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,sum(nvl(b.outamount,0)) cpckamount")
		.append(" from eh_icout a, eh_icout_b b")
		.append(" where a.pk_icout = b.pk_icout")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" ) t) group by pk_invbasdoc");
		 IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());   
		 Object ob =null;
	     try {
	    	 ob =  iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    if (ob==null)  
	    {
	    	ob = new UFDouble(0);
	    }
		return new UFDouble(ob.toString());
    }
    /**
     * 销售税率 采购税率
     * wb i=0 销售税率 i=1 采购税率
     * 2009-2-11 17:50:53
     * @return
     */
    @SuppressWarnings("unchecked")
	public UFDouble getXSCgRate(int i){
    	UFDouble[] rates = new UFDouble[2];
    	rates[0] = new UFDouble(0);
    	rates[1] = new UFDouble(0);
    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    	String sql = "select xsrate,cgrate from eh_rate where pk_corp = '"+ce.getCorporation().getPk_corp()+"' and nvl(dr,0)=0";
    	try {
    		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
    			HashMap hm = (HashMap)arr.get(0);
    			rates[0] = new UFDouble(hm.get("xsrate")==null?"0":hm.get("xsrate").toString()).div(100);	//销售税率
    			rates[1] = new UFDouble(hm.get("cgrate")==null?"0":hm.get("cgrate").toString()).div(100);	//采购税率
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rates[i];
    }
    
    /**
     * 错误信息写成TXT文件写到C下面
     *  1。现判断是否存在文件
     *  2。把原来的文件内容读写出来
     *  3。把原来的文件给删除
     *  4。生成新的文件
     *  文件名称NCPZERROR.txt
     * @throws Exception 
     */
    public static void WriteError(String errorInfo,String filename) throws Exception{
	   File f = new File("C:/NCVoucher/");
	   if (!f.exists()) {
           f.mkdirs();
	   }

      String filepath = "C:/NCVoucher/"+filename+"手工NC导U8凭证信息.txt";
  	  File file=new File(filepath); 
  	  StringBuffer brError=new StringBuffer();
  	   if(file.exists()){//文件存在的情况
  		   BufferedReader  br=new BufferedReader(new FileReader(file)); 
  		   String line=null;
  		   while((line=br.readLine())!=null){
  			  brError.append(line+"\r\n");
  		   }
  		   file.delete();  //文件已经删除
  		   br.close();
  	   }
  	   String errors= null;
  	   if(brError.toString()==null){
  		  errors=errorInfo;
  	   }else{
  		  errors=errorInfo+brError.toString();//新的错误信息
  	   }
  	   errors = ClientEnvironment.getServerTime().toString()+"   "+errors;
  	   //写文件
  	   FileOutputStream fos = new FileOutputStream(new File(filepath));
  	   BufferedOutputStream Buff=new BufferedOutputStream(fos);
  	   byte[] a=errors.getBytes();
  	   Buff.write(a);
  	   Buff.close();
  	   fos.close();
     }
    
    /***
	  * 得到公司会计主体，主体账簿
	  * @param pk_corp
	  * @return  pk_glbook pk_glorg
	  * wb
	  * 2009-10-30 11:05:35
	  */
	 @SuppressWarnings("unchecked")
	public String[] getGlbook(String pk_corp){
		 String[] glbooks = new String[2];
		 StringBuffer sql = new StringBuffer()
		 .append("  SELECT b.pk_glbook,c.pk_glorg")
		 .append("  FROM bd_glorgbook a, bd_glbook b,bd_glorg c ")
		 .append("  where a.pk_glbook = b.pk_glbook and a.pk_glorg = c.pk_glorg")
		 .append("  and c.pk_entityorg = '"+pk_corp+"'")
		 .append("  and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0");
	     try {
	    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			glbooks[0] = "";
			glbooks[1] = "";
			if(arr!=null&&arr.size()>0){
				HashMap hm = (HashMap)arr.get(0);
				glbooks[0] = hm.get("pk_glbook")==null?"":hm.get("pk_glbook").toString();
				glbooks[1] = hm.get("pk_glorg")==null?"":hm.get("pk_glorg").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return glbooks;
	 }
	
	 
	 /***
	  * 判断单据是否有凭证模板
	  * wb 2009-10-30 10:22:01
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	public boolean hasVoucherTemplet(String billtype,String pk_corp){
		 String[] glbooks = getGlbook(pk_corp);
		 StringBuffer sql = new StringBuffer()
		 .append("  select * ")
		 .append("  from dap_vouchtemp")
		 .append("  where ((pk_busitype is null or pk_busitype = ''))")
		 .append("  and (1 = 1 and pk_corp = '"+pk_corp+"' and pk_sys = 'EH' and pk_proc = '"+billtype+"' and")
		 .append("  destsystem = 0 and pk_glorg = '"+glbooks[1]+"' and")
		 .append("  pk_glbook = '"+glbooks[0]+"')");
	     try {
	    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				return true;
			}
	     }catch (Exception e) {
				e.printStackTrace();
	     }
		 return false; 
	 }

	 /***
	  * 根据单据VO生成凭证
	  * @param aggVO
	  * @param pk_corp
	  * @param pk_billtype
	  * @return
	  * @throws Exception
	  */
	 public boolean sendMessageByAggVO(AggregatedValueObject aggVO,String pk_corp,String pk_billtype) throws Exception{
			 	
		 		boolean res = false;
			 	GlorgbookVO glorgbook = GLOrgBookAcc.getDefaultGlOrgBookVOByPk_EntityOrg(pk_corp);
				IDapSendMessage bo = (IDapSendMessage) NCLocator.getInstance().lookup(IDapSendMessage.class.getName());
				
				SuperVO hvo = (SuperVO)aggVO.getParentVO();
				DapMsgVO inVo = new DapMsgVO();
				
				inVo.setCorp(pk_corp);
				//增加消息
				inVo.setMsgType (DapMsgVO.ADDMSG);
				inVo.setSys("EH"); 
				inVo.setProc(pk_billtype); 
				inVo.setProcMsg(hvo.getPrimaryKey());
				//modify by houcq 2011-11-07将业务日期改为登录日期，不取制单日期
				//inVo.setBusiDate(new UFDate(hvo.getAttributeValue("dmakedate")==null?ce.getDate().toString():hvo.getAttributeValue("dmakedate").toString()));
				inVo.setBusiDate(ce.getDate());
				inVo.setBillCode(hvo.getAttributeValue("billno")==null?"":hvo.getAttributeValue("billno").toString());
				inVo.setOperator(hvo.getAttributeValue("coperatorid")==null?ce.getUser().getPrimaryKey():hvo.getAttributeValue("coperatorid").toString());
				inVo.setPkAccount(glorgbook.getPk_glbook());
				inVo.setPkAccOrg(glorgbook.getPk_glorg());
				inVo.setCurrency("00010000000000000001");			//人民币
				
				/**增加审批人及凭证金额 add by zqy 2010年11月29日18:45:46**/
				inVo.setChecker(hvo.getAttributeValue("vapproveid")==null?"":hvo.getAttributeValue("vapproveid").toString());
				UFDouble pzmoney = Getmoney(pk_billtype, hvo.getPrimaryKey());
				inVo.setMoney(pzmoney);
				/**********END**************/
				bo.sendMessage(inVo, aggVO);
				
				//以下代码由houcq取消注释
				try {
					Thread.sleep(1000);				//处理时差问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 	
//			    if(retVo!=null&&retVo.getOperateResult() == RetVoucherVO.OPE_SUCCEED){            	//实时凭证生成成功
				if(hadPZ(hvo, pk_corp)){
			    	if(hasPz_flag(hvo)){					// 更新单据中凭证标记
			    		IVOPersistence ivo = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			    		hvo.setAttributeValue("pz_flag", new UFBoolean(true));
			    		ivo.updateVO(hvo);
			    	}
			    	res = true;
			    }
				return res;
				
		}
	
	 /**
	  * 获取来源凭证的凭证金额 add by zqy 2010年11月29日18:49:20
	  * @param billtype
	  * @param PrimaryKey
	  * @return
	  * @throws Exception
	  */
	 @SuppressWarnings("unchecked")
	public static UFDouble Getmoney(String billtype,String PrimaryKey ) throws Exception { 
		 UFDouble pzmoney = new UFDouble();
		 IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 if(billtype.equals("ZA59")){		//采购发票	taxinmony	eh_arap_stockinvoice
			 StringBuffer sql1 = new StringBuffer()
			 .append(" select sum(nvl(taxinmony,0)) taxinmony from eh_arap_stockinvoice a ,eh_arap_stockinvoices_b b ")
			 .append(" where a.pk_stockinvoice = b.pk_stockinvoice and a.pk_stockinvoice = '"+PrimaryKey+"' ")
			 .append(" and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql1.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 if(billtype.equals("ZA57")){		//付款单  fkje	 eh_arap_fk
			 StringBuffer sql2 = new StringBuffer()
			 .append(" select fkje from eh_arap_fk where pk_fk = '"+PrimaryKey+"' and nvl(dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql2.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 if(billtype.equals("ZA61")){		//收款单      eh_arap_sk zje
			 StringBuffer sql3 = new StringBuffer()
			 .append(" select zje from eh_arap_sk where pk_sk = '"+PrimaryKey+"' and nvl(dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql3.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 if(billtype.equals("ZA14")){		//销售发票  eh_invoice  totalprice
			 StringBuffer sql4 = new StringBuffer()
			 .append(" select totalprice from eh_invoice where pk_invoice = '"+PrimaryKey+"' and nvl(dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql4.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 if(billtype.equals("ZB35")){		//运费支付单  fkje  eh_arap_fk
			 StringBuffer sql5 = new StringBuffer()
			 .append(" select fkje from eh_arap_fk where pk_fk = '"+PrimaryKey+"' and nvl(dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql5.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 if(billtype.equals("ZA10")){		//提货通知单 eh_ladingbill  bcyfje
			 StringBuffer sql6 = new StringBuffer()
			 .append(" select bcyfje from eh_ladingbill where pk_ladingbill = '"+PrimaryKey+"' and nvl(dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql6.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 //add by houcq 2011-11-18 begin		 
		 if(billtype.equals("ZA45")){		//材料调拨单 eh_ladingbill  bcyfje
			 StringBuffer sql6 = new StringBuffer()
			 .append(" select sum(round(b.dcmount*b.dcprice,2)) je from eh_sc_dbd a,eh_sc_dbd_b b where a.pk_dbd=b.pk_dbd and a.pk_dbd=" +
			 		"'"+PrimaryKey+"' and nvl(dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql6.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 if(billtype.equals("ZB42")){		//回机料核算eh_ladingbill  bcyfje
			 StringBuffer sql6 = new StringBuffer()
			 .append(" select sum(round(outamount*unitprice,2)) je from eh_hjlhs a,eh_hjlhs_b b where a.pk_hjlhs=b.pk_hjlhs and a.pk_hjlhs='"+PrimaryKey+"' and nvl(dr,0)=0 ");
			 
			 Vector ve = (Vector)iUAPQueryBS.executeQuery(sql6.toString(),new VectorProcessor());
			 if(ve!=null && ve.size()>0){
				 Vector vc = (Vector)ve.get(0);
				 pzmoney = new UFDouble(vc.get(0)==null?"0":vc.get(0).toString());
			 }
		 }
		 //add by houcq 2011-11-18 end
		 return pzmoney;
	 }
	 
	 
	 
	 /***
	  * 判断单据中是否有凭证标记
	  * @param hvo
	  * @return
	  * wb 2009-10-30 15:13:19
	  */
    public boolean hasPz_flag(CircularlyAccessibleValueObject hvo){
    	String[] fields = hvo.getAttributeNames();
    	if(fields!=null&&fields.length>0){
    		String field = null;
    		for(int i=0;i<fields.length;i++){
    			field = fields[i];
    			if(field.equals("pz_flag")){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    
    /***
	  * 判断单据中是否已经生成凭证
	  * @param hvo
	  * @return
	  * wb 2009-10-30 15:13:19
     * @throws BusinessException 
	  */
   @SuppressWarnings("unchecked")
public boolean hadPZ(CircularlyAccessibleValueObject hvo,String pk_corp) throws BusinessException{
	   String billno = hvo.getAttributeValue("billno")==null?"":hvo.getAttributeValue("billno").toString();
	   String sql = "select * from dap_finindex where billcode = '"+billno+"' and pk_corp = '"+pk_corp+"' and nvl(dr,0)=0 ";
	   IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		if(arr!=null&&arr.size()>0){
			System.out.println("==========生成"+billno+"成功!");
			return true;
		}else{
			System.out.println("==========没有生成"+billno+"!");
		}
   	return false;
   }
    /***
     * 根据客商管理档案主键查找客商基本档案主键
     * @param pk_cumandoc
     * @return
     * @throws Exception
     * wb 2009-11-4 15:38:33
     */
    public String getPk_cubasdoc(String pk_cumandoc) throws Exception{
    	String pk_cubasdoc = null;
    	IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	CumandocVO cuvo = (CumandocVO)iUAPQueryBS.retrieveByPK(CumandocVO.class, pk_cumandoc);
    	if(cuvo!=null){
    		pk_cubasdoc = cuvo.getPk_cubasdoc();
    	}
    	return pk_cubasdoc;
    }
    
    /***
     * 查询客商余额
     * @param pk_cumandoc
     * @return
     * @throws Exception
     * wb 2009-11-4 15:55:08
     */
    public UFDouble getCustOverage(String pk_cumandoc,String pk_corp,String sysdate) throws Exception{
    	String nyear = null;
    	String nmonth =null;
    	int lastyear = 0;
    	int lastmonth = 0;
    	if(sysdate.length()>0){
    		 nyear = sysdate.substring(0,4);
        	 //nmonth = sysdate.substring(6,7);
    		 nmonth = sysdate.substring(5,7);//add by houcq modify 2010-10-06
        	
    		//modified by byb :   月份应取两位
    		//if(nmonth.equals("1")){
    		 if(nmonth.equals("01")){
	        	  lastmonth = 12;
	        	  lastyear = Integer.parseInt(nyear)-1;
	          }else{
	        	  lastmonth = Integer.parseInt(nmonth)-1;
	        	  lastyear = Integer.parseInt(nyear);
	          }
    	}
    	
    	 StringBuffer yesql = new StringBuffer()         
//         .append(" select overage from eh_custoverage ")
//         .append(" where pk_cubasdoc ='"+getPk_cubasdoc(pk_cumandoc)+"' and pk_corp = '"+pk_corp+"' and isnull(dr,0)=0 ");
    	 .append(" select  ")
    	 .append("        t.coumon ")
    	 .append("   from (SELECT pk_cubasdoc,                ")
    	 .append("                SUM(befmon) + SUM(collmon) + SUM(stocmon) - SUM(paymon) - ")
    	 .append("                SUM(ladmon) coumon ")
    	 .append("           FROM (SELECT n.pk_cubasdoc, ")
    	 .append("                        SUM(NVL(a.zje, 0)) collmon, ")
    	 .append("                        0 paymon, ")
    	 .append("                        0 ladmon, ")
    	 .append("                        0 stocmon, ")
    	 .append("                        0 befmon ")
    	 .append("                   from eh_arap_sk a, bd_cumandoc m, bd_cubasdoc n ")
    	 .append("                  where a.pk_cubasdoc = m.pk_cumandoc ")
    	 .append("                    AND m.pk_cubasdoc = n.pk_cubasdoc ")
    	 .append("                    AND SUBSTR(a.dmakedate, 1, 4) = "+nyear+" ")
    	 .append("                    AND SUBSTR(a.dmakedate, 6, 2) = "+nmonth+" ")
    	 .append("                    AND a.vbillstatus = '1' ")
    	 .append("                    and a.pk_corp = '"+pk_corp+"' ")
    	 .append("                    and NVL(a.dr, 0) = 0 ")
    	 .append("                  GROUP BY n.pk_cubasdoc ")
    	 .append("                 UNION ALL ")
    	 .append("                 SELECT n.pk_cubasdoc, ")
    	 .append("                        0 collmon, ")
    	 .append("                        SUM(NVL(b.fkje, 0)) paymon, ")
    	 .append("                        0 ladmon, ")
    	 .append("                        0 stocmon, ")
    	 .append("                        0 befmon ")
    	 .append("                   from eh_arap_fk b, bd_cumandoc m, bd_cubasdoc n ")
    	 .append("                  where b.pk_cubasdoc = m.pk_cumandoc ")
    	 .append("                    AND m.pk_cubasdoc = n.pk_cubasdoc ")    	 
    	 .append("                    AND SUBSTR(b.dmakedate, 1, 4) = "+nyear+" ")
    	 .append("                    AND SUBSTR(b.dmakedate, 6, 2) = "+nmonth+" ")
    	 .append("                    AND b.vbillstatus = '1' ")
    	 .append("                    and b.pk_corp = '"+pk_corp+"' ")
    	 .append("                    and NVL(b.dr, 0) = 0 ")
    	 .append("                    AND b.vbilltype = 'ZA57' ")//add by houcq 2011-02-09去除运费分摊
    	 .append("                  GROUP BY n.pk_cubasdoc ")
    	 .append("                 UNION ALL ")
    	 .append("                 SELECT n.pk_cubasdoc, ")
    	 .append("                        0 collmon, ")
    	 .append("                        0 paymon, ")
    	 .append("                        SUM(NVL(c.bcyfje, 0)) ladmon, ")
    	 .append("                        0 stocmon, ")
    	 .append("                        0 befmon ")
    	 .append("                   from eh_ladingbill c, bd_cumandoc m, bd_cubasdoc n ")
    	 .append("                  where c.pk_cubasdoc = m.pk_cumandoc ")
    	 .append("                    AND m.pk_cubasdoc = n.pk_cubasdoc ")
    	 .append("                    AND SUBSTR(c.dmakedate, 1, 4) = "+nyear+" ")
    	 .append("                    AND SUBSTR(c.dmakedate, 6, 2) = "+nmonth+" ")
    	 .append("                    AND c.vbillstatus = '1' ")
    	 .append("                    and c.pk_corp = '"+pk_corp+"' ")
    	 .append("                    and NVL(c.dr, 0) = 0 ")
    	 .append("                  GROUP BY n.pk_cubasdoc ")
    	 .append("                 UNION ALL ")
    	 .append("                 SELECT n.pk_cubasdoc, ")
    	 .append("                        0 collmon, ")
    	 .append("                        0 paymon, ")
    	 .append("                        0 ladmon, ")
    	 .append("                        SUM(NVL(e.taxinmony, 0)) stocmon, ")
    	 .append("                        0 befmon ")
    	 .append("                   from eh_arap_stockinvoice    d, ")
    	 .append("                        eh_arap_stockinvoices_b e, ")
    	 .append("                        bd_cumandoc             m, ")
    	 .append("                        bd_cubasdoc             n ")
    	 .append("                  where d.pk_cubasdoc = m.pk_cumandoc ")
    	 .append("                    AND m.pk_cubasdoc = n.pk_cubasdoc ")
    	 .append("                    AND d.pk_stockinvoice = e.pk_stockinvoice ")
    	 .append("                    and d.pk_cubasdoc is not null ")
    	 .append("                    AND SUBSTR(d.dmakedate, 1, 4) = "+nyear+" ")
    	 .append("                    AND SUBSTR(d.dmakedate, 6, 2) = "+nmonth+" ")
    	 .append("                    AND d.vbillstatus = '1' ")
    	 .append("                    and d.pk_corp = '"+pk_corp+"' ")
    	 .append("                    and NVL(d.dr, 0) = 0 ")
    	 .append("                    and NVL(e.dr, 0) = 0 ")
    	 .append("                  GROUP BY n.pk_cubasdoc ")
    	 .append("                 UNION ALL ")
    	 .append("                 SELECT s.pk_cubasdoc, ")
    	 .append("                        0 collmon, ")
    	 .append("                        0 paymon, ")
    	 .append("                        0 ladmon, ")
    	 .append("                        0 stocmon, ")
    	 .append("                        SUM(s.coumon) befmon ")
    	 .append("                   FROM eh_custoverage_h r, eh_custoverage s ")
    	 .append("                  WHERE r.nyear = "+lastyear+" ")
    	 .append("                    AND r.nmonth = "+lastmonth+" ")
    	 .append("                    AND r.pk_custoverage_h = s.pk_custoverage_h ")
    	 .append("                    and r.pk_corp = '"+pk_corp+"' ")
    	 .append("                    and NVL(r.dr, 0) = 0 ")
    	 .append("                  GROUP BY s.pk_cubasdoc) ")
    	 .append("          GROUP BY pk_cubasdoc) t, ")
    	 .append("        bd_cubasdoc invbas ")
    	 .append("  where t.pk_cubasdoc = invbas.pk_cubasdoc ")
    	 .append("    and t.pk_cubasdoc = '"+getPk_cubasdoc(pk_cumandoc)+"' ");
    	 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
     	 Object overageobj = iUAPQueryBS.executeQuery(yesql.toString(), new ColumnProcessor());
         UFDouble overage = new UFDouble(overageobj==null?"0":overageobj.toString());
         return overage;
    }
    
    /***
     * 查询客商代表
     * @param pk_cumandoc
     * @return
     * @throws Exception
     * wb 2009-11-4 15:55:08
     */
    public String getPk_custpsndoc(String pk_cumandoc,String pk_corp) throws Exception{
    	 StringBuffer sql = new StringBuffer()
    	 .append("   select a.pk_psndoc ")
    	 .append("   from eh_custyxdb a,bd_cumandoc b,bd_cubasdoc c")
    	 .append("   where a.pk_cubasdoc = b.pk_cumandoc")
    	 .append("   and b.pk_cubasdoc = c.pk_cubasdoc")
    	 .append("   and c.pk_cubasdoc = '"+getPk_cubasdoc(pk_cumandoc)+"'")
    	 .append("   and b.pk_corp = '"+pk_corp+"'")
    	 .append("   and a.ismain ='Y'")
    	 .append("   and nvl(a.dr,0)=0 ")
    	 .append("   and nvl(b.dr,0)=0 ");
    	 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
     	 Object obj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
         String pk_cubasdoc = obj==null?null:obj.toString();
         return pk_cubasdoc;
    }
    //add by houcq 2011-06-01
    public void recordTime(String vbilltype,String pk,String pk_corp,String coperatorid)
    {
    	StringBuilder sb = new StringBuilder()
        .append("insert into eh_operate_log(vbilltype, pk, pk_corp, coperatorid,opetype)")
        .append("  values ('")
        .append(vbilltype)
        .append("','")
        .append(pk)
        .append("','")
        .append(pk_corp)
        .append("','")
        .append(coperatorid)
        .append("','")
        .append("0')");
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
    	try {
			pubitf.updateSQL(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /***
     * 计算截止到计算日期的某个仓库所有物料的库存
     * add by houcq 2011-06-17 17:48     * 
	 * 该物料所有期初+采购入库+产品入库+材料调拨单调入-材料出库-产品出库-材料调拨单调出。
     * @param pk_corp
     * @param calcdate
     * @param pk_store
     * @param pk_invbasdoc
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap getInvKcAmountByStore(String pk_corp,UFDate calcdate,String pk_store){ 
    	int nmonth = calcdate.getMonth();
		int nyear = calcdate.getYear();
		int pyear = nyear;
		int pmonth =0;
        if(nmonth==1){
            pmonth=12;
            pyear = nyear - 1;
        }else
            pmonth = nmonth - 1;        
        String beginDate = calcdate.toString().substring(0,8)+"01";
		StringBuffer sb  =  new StringBuffer()
		.append(" select pk_invbasdoc,sum(nvl(kcamount,0)) kcamount from (")
		.append(" select t.pk_invbasdoc pk_invbasdoc,")
		.append(" CAST(t.qcamount+t.cgrkamount + t.cprkamount + t.dbdramount")
		.append(" - t.dbdcamount - t.clckamount-t.cpckamount AS DECIMAL(20, 5)) kcamount")
		.append(" from (")
		//期初库存取上月的期末数量
		.append(" select b.pk_invbasdoc pk_invbasdoc,sum(nvl(qmsl,0)) qcamount,")
		.append(" 0 cgrkamount, 0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount, 0 cpckamount")
		.append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")	
		.append(" where a.pk_kcybb = b.pk_kcybb and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")		
		.append(" and a.nyear="+pyear)
		.append(" and a.nmonth="+pmonth);
		if ("".equals(pk_store))
		{
			sb.append(" and b.pk_store is null");
		}else
		{
			sb.append(" and b.pk_store='"+pk_store+"'");
		}		
		sb.append(" group by b.pk_store,b.pk_invbasdoc")
		//--经营采购入库
		.append(" union all")
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" sum(nvl(b.inamount,0)) cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_stock_in a, eh_stock_in_b b")
		.append(" where a.pk_in = b.pk_in")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'");
		if ("".equals(pk_store))
		{
			sb.append(" and a.pk_stock is null");
		}else
		{
			sb.append(" and a.pk_stock='"+pk_store+"'");
		}
		sb.append(" group by a.pk_stock,b.pk_invbasdoc")
		.append(" union all")	
		//--成品入库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,sum(nvl(b.rkmount,0)) cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b")        
		.append(" where a.pk_rkd = b.pk_rkd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'");
		if ("".equals(pk_store))
		{
			sb.append(" and b.pk_store is null");
		}else
		{
			sb.append(" and b.pk_store='"+pk_store+"'");
		}
		sb.append(" group by a.pk_store,b.pk_invbasdoc")
		.append(" union all")
		//--材料调拨单调入
		.append(" select b.pk_dr_inv pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,sum(nvl(b.drrmount, 0)) dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")		
		.append(" where a.pk_dbd = b.pk_dbd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1");
		if ("".equals(pk_store))
		{
			sb.append(" and a.pk_dr_store is null");
		}else
		{
			sb.append(" and a.pk_dr_store='"+pk_store+"'");
		}		
		sb.append(" group by a.pk_dr_store,b.pk_dr_inv")
		.append(" union all")
		//--材料调拨单调出
		.append(" select b.pk_dc_inv pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,sum(nvl(b.dcmount, 0)) dbdcamount,0 clckamount,0 cpckamount")		
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")
		.append(" where a.pk_dbd = b.pk_dbd")		
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1");
		if ("".equals(pk_store))
		{
			sb.append(" and a.pk_dc_store is null");
		}else
		{
			sb.append(" and a.pk_dc_store='"+pk_store+"'");
		}		
		sb.append(" group by a.pk_dc_store,b.pk_dc_inv")		
		.append(" union all")
		//--材料出库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")		
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,sum(nvl(b.blmount,0)) clckamount,0 cpckamount")
		.append(" from eh_sc_ckd a, eh_sc_ckd_b b")
		.append(" where a.pk_ckd = b.pk_ckd").append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'");
		if ("".equals(pk_store))
		{
			sb.append(" and b.pk_store is null");
		}else
		{
			sb.append(" and b.pk_store='"+pk_store+"'");
		}
		sb.append(" group by a.pk_store,b.pk_invbasdoc")
		.append(" union all")	
		//-- 产品出库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,sum(nvl(b.outamount,0)) cpckamount")
		.append(" from eh_icout a, eh_icout_b b")
		.append(" where a.pk_icout = b.pk_icout")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'");
		if ("".equals(pk_store))
		{
			sb.append(" and a.pk_stock is null");
		}else
		{
			sb.append(" and a.pk_stock ='"+pk_store+"'");
		}
		sb.append(" group by a.pk_stock,b.pk_invbasdoc) t")
		.append(" )  group by pk_invbasdoc");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		HashMap hmList = new HashMap();
		ArrayList all;
		try {
			all = (ArrayList) iUAPQueryBS.executeQuery(sb.toString(), new MapListProcessor());
			if(all!=null && all.size()>0){
	            for(int i=0;i<all.size();i++){
	                HashMap hm = (HashMap)all.get(i);
	               hmList.put(hm.get("pk_invbasdoc"), hm.get("kcamount"));
	            } 
	        }
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return hmList;
    }
    /***
     * 计算截止到计算日期的所有物料的库存(所有仓库)
     * add by houcq 2011-07-05 17:48     * 
	 * 所有期初+采购入库+产品入库+材料调拨单调入-材料出库-产品出库-材料调拨单调出。
     * @param pk_corp
     * @param calcdate
     * @param pk_store
     * @param pk_invbasdoc
     * @return
     */
    @SuppressWarnings("unchecked")
	public HashMap getAllInvKcAmount(String pk_corp,UFDate calcdate){ 
    	int nmonth = calcdate.getMonth();
		int nyear = calcdate.getYear();
		int pyear = nyear;
		int pmonth =0;
        if(nmonth==1){
            pmonth=12;
            pyear = nyear - 1;
        }else
            pmonth = nmonth - 1;
        String beginDate = calcdate.toString().substring(0,8)+"01";
		StringBuffer sb  =  new StringBuffer()
		.append(" select pk_invbasdoc,sum(kcamount) kcamount from (")
		.append(" select t.pk_invbasdoc pk_invbasdoc, ")
		.append(" CAST(t.qcamount+t.cgrkamount + t.cprkamount + t.dbdramount")
		.append(" - t.dbdcamount - t.clckamount-t.cpckamount AS DECIMAL(20, 5)) kcamount")
		.append(" from (")
		//期初库存取上月的期末数量
		.append(" select b.pk_invbasdoc pk_invbasdoc,sum(nvl(qmsl,0)) qcamount,")
		.append(" 0 cgrkamount, 0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount, 0 cpckamount")
		.append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")	
		.append(" where a.pk_kcybb = b.pk_kcybb and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")		
		.append(" and a.nyear="+pyear)
		.append(" and a.nmonth="+pmonth)
		.append(" group by b.pk_invbasdoc")
		//--经营采购入库
		.append(" union all")
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" sum(nvl(b.inamount,0)) cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_stock_in a, eh_stock_in_b b")
		.append(" where a.pk_in = b.pk_in")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all")	
		//--成品入库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,sum(nvl(b.rkmount,0)) cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b")        
		.append(" where a.pk_rkd = b.pk_rkd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all")
		//--材料调拨单调入
		.append(" select b.pk_dr_inv pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,sum(nvl(b.drrmount, 0)) dbdramount,0 dbdcamount,0 clckamount,0 cpckamount")
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")		
		.append(" where a.pk_dbd = b.pk_dbd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1")
		.append(" group by b.pk_dr_inv")
		.append(" union all")
		//--材料调拨单调出
		.append(" select b.pk_dc_inv pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,sum(nvl(b.dcmount, 0)) dbdcamount,0 clckamount,0 cpckamount")		
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")
		.append(" where a.pk_dbd = b.pk_dbd")		
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and a.vbillstatus = 1")
		.append(" group by b.pk_dc_inv")		
		.append(" union all")
		//--材料出库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")		
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,sum(nvl(b.blmount,0)) clckamount,0 cpckamount")
		.append(" from eh_sc_ckd a, eh_sc_ckd_b b")
		.append(" where a.pk_ckd = b.pk_ckd").append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all")	
		//-- 产品出库
		.append(" select b.pk_invbasdoc pk_invbasdoc,0 qcamount,")
		.append(" 0 cgrkamount,0 cprkamount,0 dbdramount,0 dbdcamount,0 clckamount,sum(nvl(b.outamount,0)) cpckamount")
		.append(" from eh_icout a, eh_icout_b b")
		.append(" where a.pk_icout = b.pk_icout")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" ) t) group by pk_invbasdoc");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());   
		HashMap hmList = new HashMap();
		try {
			ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sb.toString(), new MapListProcessor());
			if(all!=null && all.size()>0){
		        for(int i=0;i<all.size();i++){
		           HashMap hm = (HashMap)all.get(i);
		           hmList.put(hm.get("pk_invbasdoc"), hm.get("kcamount"));
		        } 
		    }
		} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}		
		return hmList;
    }
}

