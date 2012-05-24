
package nc.ui.eh.trade.z00116;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.neethi.All;

import com.ibm.db2.jcc.a.a;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.bd.b09.CumandocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 供应商资料完善
 * @throws Exception
 * @author 王明
 * 2008年12月26日14:59:17
 */
public class ClientEventHandler extends CardEventHandler {    
	
	private HashMap<String,CumandocVO> hmObject=new  HashMap<String,CumandocVO>();           //所有的VO
	private HashMap<String,CumandocVO> hmAllCus=new  HashMap<String,CumandocVO>();           //要更新的
	ClientEnvironment ce = ClientEnvironment.getInstance();
	
	CumandocVO[] cubasdocs = null;
	
	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}

	
    protected void onBoSave() throws Exception {
            //设置公司编码
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
            for (int i = 0; i < row; i++) {               
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");               
            }
            //非空的有效性判断
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
            super.onBoSave();
            ((ClientUI)getBillUI()).setDefaultData();
    }
    
    

    private QueryConditionClient dlg = null;
    protected QueryConditionClient getQueryDLG(){        
        if(dlg == null){
            dlg = createQueryDLG();
        }
        return dlg;
    }
	
    protected QueryConditionClient createQueryDLG() {
    	QueryConditionClient dlg = new QueryConditionClient();
	    dlg.setTempletID(_getCorp().getPk_corp(), "H00116", null, null); 
	    dlg.setNormalShow(false);
	    UFDate date = ce.getDate();
	    dlg.setDefaultValue("startdate", date.toString().substring(0,8)+"01", "");
	    dlg.setDefaultValue("enddate", date.toString(), "");
        return dlg;
    }
 
    @Override
    protected void onBoBodyQuery() throws Exception {
    	 getQueryDLG().showModal();  
    	 if(getQueryDLG().getResult() == 1){
    	   this.getBufferData().clear();
  		   ConditionVO[] startdate  = getQueryDLG().getConditionVOsByFieldCode("startdate");	//开始日期
  		   ConditionVO[] enddate  = getQueryDLG().getConditionVOsByFieldCode("enddate");		//结束日期
  		   ConditionVO[] pk_cubasdoc  = getQueryDLG().getConditionVOsByFieldCode("pk_cubasdoc");	//客商
  		   if(startdate==null || enddate==null || startdate.length<0 ||enddate.length<0  ){
  			   getBillUI().showErrorMessage("请输入 开始日期，结束日期 ！");
  			   return;
  		   }
  		   String startdate2 = new String(startdate[0].getValue()); 
  		   String enddate2=new String(enddate[0].getValue()); 
  		   String pk_cubasdoc2 = pk_cubasdoc!=null&&pk_cubasdoc.length>0?pk_cubasdoc[0].getValue():"%";
  		   int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
          int[] rows=new int[rowcount];
           for(int i=rowcount - 1;i>=0;i--){
              rows[i]=i;
           }
           getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
           this.getBillUI().updateUI();
  		   
  		   hmAllCus=getAllCubasdoc();
  		   
  		   String pk_corp = ce.getCorporation().getPk_corp();
  		   //查询第一列 //供货合格率 
  		   String sql1="select count(*) times, def_2 from eh_stock_checkreport where NVL(dr,0)=0 and def_2<>'' and def_2 like '"+pk_cubasdoc2+"' " +
  		   		" and  resulst=0 and dmakedate between '"+startdate2+"' and '"+enddate2+"' and pk_corp = '"+pk_corp+"' group by def_2"; //查询累计合格次数
  		   String sql2="select count(*) times, def_2 from eh_stock_checkreport where NVL(dr,0)=0 and def_2<>'' and pk_corp = '"+pk_corp+"' and def_2 like '"+pk_cubasdoc2+"'  " +
  		   		"and dmakedate between '"+startdate2+"' and '"+enddate2+"' " +
  	   		"  group by def_2";    //查询累计检测次数
  		   getFirstLine(sql1,sql2); //第一列计算的结果
  		   
  		   //查询第二列 // 交货逾期率
  		   String sql3="select a.pk_cubasdoc,a.enddate,b.dmakedate from " +
  		   		" eh_stock_contract a,eh_stock_receipt b where a.pk_contract=b.vsourcebillid and " +
  		   		" NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and a.pk_corp = '"+pk_corp+"' and b.pk_corp = '"+pk_corp+"' and a.pk_cubasdoc like '"+pk_cubasdoc2+"'  and b.dmakedate between '"+startdate2+"' and '"+enddate2+"'";
  		   getSecondLine(sql3);
  		   
  		   //查询第三列 //合同履行率
//  		   String sql4="select a.pk_cubasdoc,sum(b.amount) a,sum(d.inamount) b " +
//  		   		"from eh_stock_contract a,eh_stock_contract_b b ,eh_stock_receipt c,eh_stock_receipt_b d " +
//  		   		"where a.pk_contract=b.pk_contract and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 and a.pk_corp = '"+pk_corp+"' and c.pk_corp = '"+pk_corp+"' " +
//  		   		"and a.pk_contract=c.vsourcebillid and c.pk_receipt=d.pk_receipt  and a.pk_cubasdoc like '"+pk_cubasdoc2+"'" +
//  		   		"and c.dmakedate between '"+startdate2+"' and '"+enddate2+"' group by  a.pk_cubasdoc";
  		   StringBuffer sql4 = new StringBuffer()//取采购合同中采购数量

  		 .append(" SELECT  ")
  		.append(" trec.pk_cubasdoc,(NVL(tcon.amount,0)+NVL(trec.inamount,0))inamount,tsto.stinamount ")
  		.append(" FROM ( ")
  		.append(" SELECT st_con.pk_cubasdoc pk_cubasdoc ,SUM(NVL(st_conb.amount,0)) amount ")
  		.append(" FROM eh_stock_contract st_con,eh_stock_contract_b st_conb ")
  		.append(" WHERE st_con.pk_contract = st_conb.pk_contract ")
  		.append(" AND st_con.pk_cubasdoc like '"+pk_cubasdoc2+"' ")
  		.append(" AND st_con.pk_corp = '"+pk_corp+"' ")
  		.append(" AND st_con.Vbillstatus = '1' ")
  		.append(" AND NVL(st_con.dr,0)= 0 ")
  		.append(" AND st_con.dmakedate BETWEEN '"+startdate2+"' AND '"+enddate2+"' ")
  		.append(" AND st_con.pk_contract in ( ")
  		.append(" SELECT st_rec.vsourcebillid ")
  		.append(" FROM  eh_stock_receipt st_rec,eh_stock_receipt_b st_recb ")
  		.append(" WHERE st_rec.pk_receipt = st_recb.pk_receipt ")
  		.append(" AND st_rec.pk_cubasdoc like '"+pk_cubasdoc2+"' ")
  		.append(" AND st_rec.pk_corp = '"+pk_corp+"' ")
  		.append(" AND st_rec.vbillstatus = '1' ")
  		.append(" AND NVL(st_rec.dr,0)= 0 ")
  		.append(" AND st_rec.dmakedate BETWEEN '"+startdate2+"' AND '"+enddate2+"') ")
  		.append(" GROUP BY st_con.pk_cubasdoc ")
  		.append(" )tcon ")
  		.append(" , ")
  		.append(" ( ")
  		.append(" SELECT st_rec.pk_cubasdoc pk_cubasdoc ,st_recb.inamount inamount ")
  		.append(" FROM  eh_stock_receipt st_rec,eh_stock_receipt_b st_recb ")
  		.append(" WHERE st_rec.pk_receipt = st_recb.pk_receipt ")
  		.append(" AND st_rec.pk_cubasdoc like '"+pk_cubasdoc2+"' ")
  		.append(" AND st_rec.pk_corp = '"+pk_corp+"' ")
  		.append(" AND st_rec.vbillstatus = '1' ")
  		.append(" AND NVL(st_rec.dr,0)= 0 ")
  		.append(" AND st_rec.dmakedate BETWEEN '"+startdate2+"' AND '"+enddate2+"' ")
  		.append(" AND st_rec.vsourcebillid IS NULL ")
  		.append(" )trec ")
  		.append(" , ")
  		.append(" ( ")
  		.append(" SELECT st_in.pk_cubasdoc pk_cubasdoc,SUM(NVL(st_inb.inamount,0)) stinamount ")
  		.append(" FROM eh_stock_in st_in,eh_stock_in_b st_inb ")
  		.append(" WHERE st_in.pk_in = st_inb.pk_in ")
  		.append(" AND st_in.pk_cubasdoc like '"+pk_cubasdoc2+"' ")
  		.append(" AND st_in.pk_corp = '"+pk_corp+"' ")
  		.append(" AND st_in.vbillstatus = '1' ")
  		.append(" AND NVL(st_in.dr,0) = 0 ")
  		.append(" AND st_in.vbilltype = 'ZA22' ")
  		.append(" AND st_in.dmakedate BETWEEN '"+startdate2+"' AND '"+enddate2+"' ")
  		.append(" GROUP BY st_in.pk_cubasdoc ")
  		.append(" )tsto ")
  		.append(" WHERE tcon.pk_cubasdoc(+) = trec.pk_cubasdoc ")
  		.append(" AND tsto.pk_cubasdoc(+)= trec.pk_cubasdoc ");
  		   

  		 
  		   getThirdLine(sql4.toString());
  		   
  		   if(hmObject!=null&&hmObject.size()>0){
	  		   String[] keys=hmObject.keySet().toArray(new String[hmObject.size()]);
	  		   HashMap hmPj = getCubasPj(new UFDate(startdate2), new UFDate(enddate2), pk_corp,pk_cubasdoc2);
	  		   cubasdocs=new CumandocVO[keys.length];
	  		   for(int i=0;i<keys.length;i++){
	  			   cubasdocs[i]=hmObject.get(keys[i]);
	  			   if(cubasdocs[i].getDef1() == null){	  				   
	  				   cubasdocs[i].setDef1(new UFDouble(0).toString());
	  			   	}
	  			   
	  			   //添加功能：交货逾期率为NULL时用0输出。时间：2009-11-30。作者：张志远
	  			 if(cubasdocs[i].getDef2() == null){	  				   
	  				   cubasdocs[i].setDef2(new UFDouble(0).toString());
	  			   	}
	  			   
	  			   CumandocVO cvo = (CumandocVO)hmPj.get(keys[i])==null?new CumandocVO():(CumandocVO)hmPj.get(keys[i]);
	  			   UFDouble fs = cvo.getDef14()==null?null: new UFDouble( cvo.getDef14());		//综合评分
	  			   String py = cvo.getDef15();                                                   // 综合评语
	  			   cubasdocs[i].setDef14(fs==null?null:fs.toString());					//用def14保存评价分
	  			   cubasdocs[i].setDef15(py);											//用def15保存评语
	  			   
	  		   }
		         //需要先清空
		         getBufferData().clear();
		
		         if (cubasdocs != null) {
		             HYBillVO billVO = new HYBillVO();
		             //加载数据到单据
		             billVO.setChildrenVO(cubasdocs);
		             //加载数据到缓冲
		             if (getBufferData().isVOBufferEmpty()) {
		                 getBufferData().addVOToBuffer(billVO);
		             } else {
		                 getBufferData().setCurrentVO(billVO);
		             }
		
		             //设置当前行
		             getBufferData().setCurrentRow(0);
		         } else {
		             getBufferData().setCurrentRow(-1);		             
		         }
  		   }else{
  			 this.getBillUI().showErrorMessage("没有符合要求的数据");
  		   }
  		  hmObject = new HashMap();
  		  
  	   }
    }
 
    
   
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.ToCusbasdoc:    //文档管理
                 toChange();
        }   
    }
    public void toChange(){
    	if (getBillUI().showYesNoMessage("是否当前的供应商信息更新到客商档案中?") == UIDialog.ID_YES) {
	    	IVOPersistence  iVOPersistence =    (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
	    	try {
				iVOPersistence.updateVOArray(cubasdocs);
				getBillUI().showWarningMessage("更新成功！");
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			
    	}
    }
    /** 
     * 返回第一列的数据 设置供货合格率(bd_cumandoc def1保存)
     * @param sql1 被除数SQL（合格次数）
     * @param sql2 除数SQL（不合格次数）
     * @return
     */
    public void getFirstLine(String sql1,String sql2){
    	HashMap<String,UFDouble> hm1=new HashMap<String,UFDouble>();//合格次数
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	try {
			ArrayList al1=(ArrayList) iUAPQueryBS.executeQuery(sql1, new MapListProcessor());
			
			for(int i=0;i<al1.size();i++){
				HashMap hm=(HashMap) al1.get(i);
				UFDouble times1=new UFDouble(hm.get("times")==null?"0":hm.get("times").toString());
				String pk_cuabsdoc1=hm.get("def_2")==null?"":hm.get("def_2").toString();
				hm1.put(pk_cuabsdoc1, times1);
			}
			ArrayList al2=(ArrayList) iUAPQueryBS.executeQuery(sql2, new MapListProcessor());
			for(int i=0;i<al2.size();i++){
				HashMap hm=(HashMap) al2.get(i);
				UFDouble times2=new UFDouble(hm.get("times")==null?"0":hm.get("times").toString());
				String pk_cuabsdoc2=hm.get("def_2")==null?"":hm.get("def_2").toString();
				if(hm1.containsKey(pk_cuabsdoc2)){
					//含有合格数量
					UFDouble times1=hm1.get(pk_cuabsdoc2);
					UFDouble helv=times1.div(times2);
					CumandocVO vo=hmAllCus.get(pk_cuabsdoc2);
					vo.setDef1(helv.toString());
					hmObject.put(pk_cuabsdoc2, vo);
				}
//				else{
//					///不含有合格数量
//					CumandocVO vo=hmAllCus.get(pk_cuabsdoc2);
//					vo.setDef1("0");
//					hmObject.put(pk_cuabsdoc2, vo);
//				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
    }
    /**
     *  返回第二列的数据 交货逾期率(bd_cumandoc def2保存)
     * @param sql SQL
     * @return
     */
    public void getSecondLine(String sql){
    	HashMap<String,UFDouble> hm1=new HashMap<String,UFDouble>();//过时间的
    	HashMap<String,UFDouble> hm2=new HashMap<String,UFDouble>();
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
			for(int i=0;i<al.size();i++){
				HashMap hm=(HashMap) al.get(i);
				String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
				UFDate enddate=new UFDate(hm.get("enddate")==null?"":hm.get("enddate").toString());
				UFDate stratdate=new UFDate(hm.get("dmakedate")==null?"":hm.get("dmakedate").toString());
				
				if(stratdate.compareTo(enddate)<=0){//负数情况
					if(hm1.containsKey(pk_cubasdoc)){
						UFDouble one=new UFDouble(hm1.get(pk_cubasdoc)==null?"0":hm1.get(pk_cubasdoc).toString());
						UFDouble two=one.add(new UFDouble(1));
						hm1.put(pk_cubasdoc, two);
					}else{
						hm1.put(pk_cubasdoc, new UFDouble(1));
					}
				}
				if(hm2.containsKey(pk_cubasdoc)){// 全部数量
					UFDouble one=new UFDouble(hm2.get(pk_cubasdoc)==null?"0":hm2.get(pk_cubasdoc).toString());
					UFDouble two=one.add(new UFDouble(1));
					hm2.put(pk_cubasdoc, two);
				}else{
					hm2.put(pk_cubasdoc, new UFDouble(1));
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		String[] keys=hm2.keySet().toArray(new String[hm2.size()]);
		for(int i=0;i<keys.length;i++){
			if(hm1.containsKey(keys[i])){
				UFDouble a=hm1.get(keys[i]);
				UFDouble b=hm2.get(keys[i]);
				CumandocVO vo=null;
				if(hmObject.containsKey(keys[i])){
					vo=hmObject.get(keys[i]);
				}else{
					vo=hmAllCus.get(keys[i]);
				}
				vo.setDef2((a.div(b)).toString());
				hmObject.put(keys[i], vo);
			}else{
				CumandocVO vo=null;
				if(hmObject.containsKey(keys[i])){
					vo=hmObject.get(keys[i]);
				}else{
					vo=hmAllCus.get(keys[i]);
				}
				vo.setDef2("1");
				hmObject.put(keys[i], vo);
			}
		}
    }
    /**
     *  返回第三列的数据 供货完成率(bd_cumandoc def3保存)
     *  改成 合同履行率 累计合同履行率=[累计入库数量*（1±允许误率）]/累计采购合同数量*100%
     * @param sql SQL
     * @return
     */
    public void getThirdLine(String sql4){
    	
    	//取采购合同中的合同PK和采购数量
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql4, new MapListProcessor());
			for(int i=0;i<al.size();i++){
				HashMap hm=(HashMap) al.get(i);
				String pk_cubasdoc=hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
				UFDouble inamount = new UFDouble(hm.get("inamount")==null?"0":hm.get("inamount").toString());		//累计合同数量
				UFDouble stinamount = new UFDouble(hm.get("stinamount")==null?"0":hm.get("stinamount").toString());	 	//累计入库数量
				UFDouble deviation = new UFDouble(0); //允许误差率
				if(stinamount.compareTo(inamount)>0){
					deviation = new UFDouble(-5).div(100);
				}else if(stinamount.compareTo(inamount)<0){
					deviation = new UFDouble(5).div(100);
				}
				
				CumandocVO vo=null;
				if(hmObject.containsKey(pk_cubasdoc)){
					vo=hmObject.get(pk_cubasdoc);
				}else{
					vo=hmAllCus.get(pk_cubasdoc);
				}
				UFDouble wcl = stinamount.multiply(new UFDouble(1).add(deviation)).div(inamount);			//完成率 = 累计入库数量*（1±允许误率）/累计采购合同数量
				
				vo.setDef3(wcl.toString());
				hmObject.put(pk_cubasdoc, vo);
				
			}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    }
    
    /****
     * 供应商的综合评分、综合评价 
     * 综合评分取期间段的平均分,评价将期间段的评价相连
     * wb 2009-4-9 14:19:01
     * @param begindate
     * @param enddate
     * @param pk_corp
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
	public HashMap getCubasPj(UFDate begindate,UFDate enddate,String pk_corp,String pk_cubasdoc2) throws BusinessException{
    	String begin = begindate.toString().substring(0,7);
    	String end = enddate.toString().substring(0,7);
    	HashMap hm = new HashMap();
    	StringBuffer sql = new StringBuffer()
		.append("  	SELECT b.pk_cubasdoc,e.fs,d.begindate,b.memo")
		.append("	FROM eh_gyspj a,eh_gyspj_b b,bd_cumandoc c,eh_period d, ")
		.append("  	(SELECT b.pk_cubasdoc,AVG(NVL(b.workpoint,0)) fs ")
		.append("  	FROM eh_gyspj a,eh_gyspj_b b,bd_cumandoc c,eh_period d ")
		.append("  	WHERE a.pk_gyspj = b.pk_gyspj ")
		.append("  	AND b.pk_cubasdoc = c.pk_cumandoc ")
		.append("  	AND a.pk_period = d.pk_period ")
		.append("  	AND a.pk_corp = '"+pk_corp+"' and b.pk_cubasdoc like '"+pk_cubasdoc2+"'")
		.append("  	AND NVL(a.dr,0)=0 AND NVL(b.dr,0)=0 ")
		.append("  	AND (d.begindate LIKE '"+begin+"%' OR d.begindate LIKE '"+end+"%') ")
		.append("  	GROUP BY b.pk_cubasdoc) e ")
		.append("  	WHERE a.pk_gyspj = b.pk_gyspj ")
		.append("  	AND b.pk_cubasdoc = c.pk_cumandoc ")
		.append("  	AND a.pk_period = d.pk_period ")
		.append("  	AND b.pk_cubasdoc = e.pk_cubasdoc ")
		.append("  	AND (d.begindate LIKE '"+begin+"%' OR d.begindate LIKE '"+end+"%') ")
		.append("  	AND a.pk_corp = '"+pk_corp+"' ")
		.append("  	AND NVL(a.dr,0)=0 AND NVL(b.dr,0)=0 and b.pk_cubasdoc like '"+pk_cubasdoc2+"'");
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList arr =(ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		if(arr!=null&&arr.size()>0){
			CumandocVO cvo = null;
			for(int i=0;i<arr.size();i++){
				HashMap hmA =(HashMap) arr.get(i);
				String pk_cubasdoc = hmA.get("pk_cubasdoc")==null?"":hmA.get("pk_cubasdoc").toString();
				UFDouble fs = new UFDouble( hmA.get("fs")==null?"0":hmA.get("fs").toString());					//综合分数
				UFDate date = new UFDate(hmA.get("begindate")==null?"":hmA.get("begindate").toString());		//月份
				String memo =  hmA.get("memo")==null?null:hmA.get("memo").toString().trim();
				int year = date.getYear();
				int month = date.getMonth();
				String py = null;													//评语
				if(memo!=null){
					py = year+"年"+month+"月:"+memo;
				}
				if(hm.containsKey(pk_cubasdoc)){
					((CumandocVO)hm.get(pk_cubasdoc)).setDef14(fs.toString());
					if(py!=null){
						//String def = ((CumandocVO)hm.get(pk_cubasdoc)).getDef1()==null?"":((CumandocVO)hm.get(pk_cubasdoc)).getDef1()+", ";
						String def = ((CumandocVO)hm.get(pk_cubasdoc)).getDef15()==null?"":((CumandocVO)hm.get(pk_cubasdoc)).getDef15()+", ";
						((CumandocVO)hm.get(pk_cubasdoc)).setDef15(def+py);	 	//将涉及的期间段的评语综合
					}
				}else{
					cvo = new CumandocVO();
					cvo.setDef14(fs.toString());
					if(py!=null){
						cvo.setDef15(py);
					}
					hm.put(pk_cubasdoc, cvo);
				}
			}
		}
    	return hm;
    }
    
    /**
     *  取到所有的供应商
     * @throws BusinessException 
     */
    private HashMap<String,CumandocVO> getAllCubasdoc() throws BusinessException{
    	HashMap<String,CumandocVO> hm=new HashMap<String,CumandocVO>();
    	String sql = "select * from bd_cumandoc where nvl(dr,0)=0 and pk_corp='"+ce.getCorporation().getPk_corp()+"'";
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new BeanListProcessor(CumandocVO.class));
    	for(int i=0;i<al.size();i++){
    		CumandocVO value=(CumandocVO) al.get(i);
    		String key=value.getPrimaryKey()==null?"":value.getPrimaryKey().toString();
    		hm.put(key, value);
    	}
    	return hm;
    }
    
    
    
    
    
    
    
    
}
