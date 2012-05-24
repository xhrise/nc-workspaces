package nc.ui.eh.stock.h0150210;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.vo.eh.stock.z00140.SwVO;
import nc.vo.pub.lang.UFDouble;
public class YfTools {

	 /***铁路运输**/
	 String tlyscode =  "001"; 
	
	 public static final String[] tlysmc = {"上站费","请车费","路运费","取货费","其他费"};			//铁路运输
	 
	 public static final String[] tlysfilds = {"tlszf","tlqcf","tllyf","tlqhf","tlqtf"}; 
	 
	 public static final String tlyszd = PubTools.combinArrayToString3(tlysfilds);
		 
	 /***汽车运输***/
	 String glyscode =  "002"; 
	 public static final String[] glysmc = {"公路运费"};
	 
	 public static final String[] glysfilds = {"glyf"};

	 public static final String glyszd = PubTools.combinArrayToString3(glysfilds);
	 
	 /***水路运输**/
	 String slyscode1 =  "003";
	 String slyscode2 =  "004";
	 public static final String[] slysmc = {"海运费","转运费","短途运费","其他费"};		
	 
	 public static final String[] slysfilds = {"slhyf","slzyf","sldtyf","slqtf"}; 
	 
	 public static final String slyszd = PubTools.combinArrayToString3(slysfilds);
	 
	 /***铁路集装箱运输**/
	 String jzxyscode = "005";
	 public static final String[] jzxysmc = {"上站费","铁路费用","取货费","其他费"};		
	 
	 public static final String[] jzxysfilds = {"jzxszf","jzxtlf","jzxqhf","jzxqtf"}; 
	 
	 public static final String jzxyszd = PubTools.combinArrayToString3(jzxysfilds);
		
	 
	 /**国际货运 add by wb 2009年4月8日16:21:58***/
	 public static String gjyscode = "006";
	 
	 public static final String[] gjysmc = {"海运费","保险费","关税税率(%)","增值税税率(%)","汇率","港杂费","短途运费"};
	 
	 public static final String[] gjfilds = {"gjhyf","gjbxf","gjgsrate","gjzzsrate","gjhlrate","gjgzf","gjdtyf"}; 
	 
	 public static final String gjyszd = PubTools.combinArrayToString3(gjfilds);
	 
	 /***
	  * 根据物料，地区，运费方式得到合计运费及明细
	  * wb 2009-3-3 14:34:02
	  * @param pk_invbasdoc
	  * @param pk_areacl
	  * @param yscode
	  * @param pk_corp
	  * @return arr 0 合计运费 1 运费明细
	 * @throws Exception 
	  */
	 @SuppressWarnings("unchecked")
	public ArrayList getYf(String pk_invbasdoc,String pk_areacl,String pk_sw,String pk_corp,UFDouble tpprice)   {
		 ArrayList yf = new ArrayList();
		 
		 String[] ysmc = null;
		 String[] ysfilds = null;
		 String yszd = null;
	 try {
		 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 SwVO swvo = (SwVO)iUAPQueryBS.retrieveByPK(SwVO.class, pk_sw);
		 String yscode = swvo!=null?swvo.getSwcode():"";
		 //判断运输方式
		 if(yscode.equals(tlyscode)){
			 ysmc = tlysmc;
			 ysfilds = tlysfilds;
			 yszd = tlyszd;
		 } 
		 if(yscode.equals(glyscode)){
			 ysmc = glysmc;
			 ysfilds = glysfilds;
			 yszd = glyszd;
		 } 
		 if(yscode.equals(slyscode1)||yscode.equals(slyscode2)){
			 ysmc = slysmc;
			 ysfilds = slysfilds;
			 yszd = slyszd;
		 } 
		 if(yscode.equals(jzxyscode)){
			 ysmc = jzxysmc;
			 ysfilds = jzxysfilds;
			 yszd = jzxyszd;
		 } 
		 if(yscode.equals(gjyscode)){
			 ysmc = gjysmc;
			 ysfilds = gjfilds;
			 yszd = gjyszd;
		 } 
		 
		 StringBuffer sql = new StringBuffer()
		 .append("  SELECT "+yszd+"")
		 .append("  FROM eh_stock_rwcarriage")
		 .append("  WHERE pk_invbasdoc = '"+pk_invbasdoc+"'  ")
		 .append("  AND pk_areacl = '"+pk_areacl+"' AND pk_corp = '"+pk_corp+"'")
		 .append("  AND ISNULL(dr,0)=0");
		 ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				StringBuffer memo = new StringBuffer();		//运费明细
				UFDouble sumyf = new UFDouble(0);			//合计运费
				for(int i=0;i<ysfilds.length;i++){			//非国际货运时将费用相加
					UFDouble yfmx = new UFDouble(hmA.get(ysfilds[i])==null?"0":hmA.get(ysfilds[i]).toString());
					if(yfmx.toDouble()!=0){
						sumyf = sumyf.add(yfmx);
						memo.append(ysmc[i]+":"+yfmx+" ");
					}
				}
				
				if(yscode.equals(gjyscode)){				//国际货运时										
					/***国际货运 到岸价=询价单中的 供方报价+海运费+保险费
						到岸完税价格=到岸价*（1+关税税率）*（1+增值税税率）
						询价单中到厂价（人民币）=到岸完税价格X汇率+港杂费+短途运费。
					****/
					UFDouble gjhyf = new UFDouble(hmA.get("gjhyf")==null?"0":hmA.get("gjhyf").toString());		//海运费
					UFDouble gjbxf = new UFDouble(hmA.get("gjbxf")==null?"0":hmA.get("gjbxf").toString());		//保险费
					UFDouble gjgsrate = new UFDouble(hmA.get("gjgsrate")==null?"0":hmA.get("gjgsrate").toString()).div(100);			//关税税率/100
					UFDouble gjzzsrate = new UFDouble(hmA.get("gjzzsrate")==null?"0":hmA.get("gjzzsrate").toString()).div(100);			//增值税税率/100
					UFDouble gjhlrate = new UFDouble(hmA.get("gjhlrate")==null?"0":hmA.get("gjhlrate").toString());			//汇率
					UFDouble gjgzf = new UFDouble(hmA.get("gjgzf")==null?"0":hmA.get("gjgzf").toString());					//杂物费
					UFDouble gjdtyf = new UFDouble(hmA.get("gjdtyf")==null?"0":hmA.get("gjdtyf").toString());				//短运费
					
					UFDouble daj = tpprice.add(gjhyf).add(gjbxf);				//到岸价=供方报价+海运费+保险费
					UFDouble dawsj = daj.multiply(new UFDouble(1).add(gjgsrate)).multiply(new UFDouble(1).add(gjzzsrate));//到岸完税价格=到岸价X（1+关税税率）X（1+增值税税率） 
					sumyf = dawsj.multiply(gjhlrate).add(gjgzf).add(gjdtyf);			//到厂价 = 到岸完税价格X汇率+港杂费+短途运费。
					memo.append("到岸价:"+new UFDouble(daj.toDouble(),2)+" "+"到岸完税价:"+new UFDouble(dawsj.toDouble(),2)+" ");
				}
			
				yf.add(sumyf);				//到厂价
				yf.add(memo.toString());	//明细设置
			}
		 }catch (Exception e) {
			 e.printStackTrace();
		 }
		 return yf;
	 }
}
