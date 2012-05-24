
package nc.ui.eh.report.h0900403;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0900403.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/*
 * 功能:派工与入库完成情况明细表
 * 作者:zqy
 * 时间:2008-08-02
 */

public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private UFDate ptoday = null;
    private ClientEnvironment ce=ClientEnvironment.getInstance();

	public ReportUI() {
		super();
		m_boQuery = new ButtonObject("查询", "查询报表", 0);
        m_boPrint = new ButtonObject("打印", "打印报表", 0);
        initialize(); 
	}
	
	private void initialize(){
	    setName("GeneralPane");
	    setLayout(new BorderLayout());
	    setSize(1024, 768);
	    add(getReportBase(), "Center");
	    setButtons(getBtnAry());
	    updateButtons();
	    getReportBase().setShowNO(true);
	    int colcount = getReportBase().getBillTable().getColumnCount();
	    String st[] = new String[colcount];
	    String skey = "";
	    for(int i=0;i<colcount;i++){
	    	 skey = getReportBase().getBodyShowItems()[i].getKey().trim();
	    	 st[i] = skey;
	    }
	    
	    //得到当前的日期
	    ptoday = this.getClientEnvironment().getDate();
	    getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry(){
	    return (new ButtonObject[] {
	        m_boQuery, m_boPrint
	    }
     );	   
	}
    
	public ReportBaseClass getReportBase(){
	    if(m_report == null)
	        try{
	            m_report = new ReportBaseClass();
	            m_report.setName("ReportBase");
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900403", null, null);
	        }
	        catch(Exception ex){
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}

	public String getTitle() {
		return m_report.getReportTitle();
	}

	public void onButtonClicked(ButtonObject bo) {
		try{
	        if(bo == m_boQuery){
	            onQuery();
	        }else if(bo == m_boPrint){
	            onPrint();
	        }
	    }
	    catch(BusinessException ex){
	        showErrorMessage(ex.getMessage());
	        ex.printStackTrace();
	    }
	    catch(Exception e) {
	        showErrorMessage("\u672A\u77E5\u9519\u8BEF:" + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public QueryConditionClient getQryDlg(){
	    if(m_qryDlg == null){
	        m_qryDlg = createQueryDLG();	       
	    }
	    return m_qryDlg;
	}
	
	protected QueryConditionClient createQueryDLG(){
		QueryConditionClient dlg = new QueryConditionClient();
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900403", null, null);
        
        String nowday = ce.getDate().toString();
        String subday = nowday.substring(0, 7);
        String endday = ""+subday+"-01";
        dlg.setDefaultValue("startdate", endday, "");
        dlg.setDefaultValue("enddate", nowday, "");
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	  //设置打印方法
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
      
	  //查询方法
	  @SuppressWarnings("unchecked")
    public void onQuery() throws Exception{
          this.getReportBase().getBillModel().clearBodyData();
            String pk_corp = this.getCorpPrimaryKey();
            getQryDlg().showModal();
            ArrayList list = new ArrayList();
            ReportVO[] vos = null;
            getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示   
            if(getQryDlg().getResult() == 1){
                ConditionVO[] startdate  = getQryDlg().getConditionVOsByFieldCode("startdate");          //开始时间
                ConditionVO[] enddate  = getQryDlg().getConditionVOsByFieldCode("enddate");              //结束时间
                UFDate start_date = null;
                UFDate end_date = null;
                if(startdate!=null && startdate.length>0){
                    start_date = new UFDate(startdate[0].getValue()==null?"":startdate[0].getValue().toString());
                }
                if(enddate!=null && enddate.length>0){
                    end_date = new UFDate(enddate[0].getValue()==null?"":enddate[0].getValue().toString());
                }
               
                /***加上生产订单时间******************/
                ConditionVO[] startsj  = getQryDlg().getConditionVOsByFieldCode("startsj");          	 //开始时间
                ConditionVO[] endsj  = getQryDlg().getConditionVOsByFieldCode("endsj");              	 //结束时间
                int start_sj = 0;
                int end_sj = 24;
                if(startsj!=null && startsj.length>0){
                	start_sj = Integer.parseInt(startsj[0].getValue()==null?"0":startsj[0].getValue().toString());
                }
                if(endsj!=null && endsj.length>0){
                	end_sj = Integer.parseInt(endsj[0].getValue()==null?"24":endsj[0].getValue().toString());
                }
                
               HashMap hmrkmount = getRkData(start_date, end_date, start_sj, end_sj, pk_corp);			//入库数据
               
               IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
               StringBuffer sql = new StringBuffer()
//               .append(" select a.pgdate ,b.pk_invbasdoc, c.invname ,c.invspec ,c.invtype ,c.colour , d.brandname,sum(isnull(b.pgamount,0)) pgamount ")
//               .append(" from eh_sc_pgd a,eh_sc_pgd_b b ,eh_invbasdoc c ,eh_brand d ")
//               .append(" where a.pk_pgd=b.pk_pgd and b.pk_invbasdoc = c.pk_invbasdoc and c.brand = d.pk_brand ")
//               .append(" and isnull(a.dr,0)=0 and a.pk_corp='"+pk_corp+"' and xdflag='Y' and isnull(a.lock_flag,'N')<>'Y' ")
//               .append(" and a.dmakedate between '"+start_date+"' and '"+end_date+"' AND SUBSTRING (a.ts,12,2) BETWEEN "+start_sj+" AND "+end_sj+"")
//               .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 ")
//               .append(" group by a.pgdate, b.pk_invbasdoc,c.invname ,c.invspec ,c.invtype ,c.colour , d.brandname ");
               .append(" select a.pgdate, ")
               .append("        b.pk_invbasdoc, ")
               .append("        c.invname, ")
               .append("        c.invspec, ")
               .append("        c.invtype, ")
               .append("        c.def1 colour, ")
               .append("        d.brandname, ")
               .append("        sum(isnull(b.pgamount, 0)) pgamount ")
               .append("   from eh_sc_pgd a, eh_sc_pgd_b b, bd_invbasdoc c,bd_invmandoc cc, eh_brand d ")
               .append("  where a.pk_pgd = b.pk_pgd ")
               .append("    and b.pk_invbasdoc = cc.pk_invmandoc ")
               .append("    and c.pk_invbasdoc=cc.pk_invbasdoc ")
               .append("    and c.invpinpai = d.pk_brand ")
               .append("    and isnull(a.dr, 0) = 0 ")
               .append("    and a.pk_corp = '"+pk_corp+"' ")
               .append("    and xdflag = 'Y' ")
               .append("    and isnull(a.lock_flag, 'N') <> 'Y' ")
               .append("    and a.dmakedate between '"+start_date+"' and '"+end_date+"' ")
               .append("    AND SUBSTR(a.ts, 12, 2) BETWEEN "+start_sj+" AND "+end_sj+" ")
               .append("    and isnull(a.dr, 0) = 0 ")
               .append("    and isnull(b.dr, 0) = 0 ")
               .append("  group by a.pgdate, ")
               .append("           b.pk_invbasdoc, ")
               .append("           c.invname, ")
               .append("           c.invspec, ")
               .append("           c.invtype, ")
               .append("           c.def1, ")
               .append("           d.brandname ");

               ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
               if(al!=null && al.size()>0){
                   for(int i=0;i<al.size();i++){
                       HashMap hm = (HashMap)al.get(i);
                       ReportVO vo = new ReportVO();
                       String pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                       UFDate pgdate=new UFDate(hm.get("pgdate")==null?"":hm.get("pgdate").toString());     //派工日期
                       String invname=hm.get("invname")==null?"":hm.get("invname").toString();              //物料名称
                       String invspec=hm.get("invspec")==null?"":hm.get("invspec").toString();              //物料规格
                       String invtype=hm.get("invtype")==null?"":hm.get("invtype").toString();              //型号
                       String colour=hm.get("colour")==null?"":hm.get("colour").toString();                 //颜色
                       String brandname=hm.get("brandname")==null?"":hm.get("brandname").toString();        //品牌
                       UFDouble pgamount=new UFDouble(hm.get("pgamount")==null?"0":hm.get("pgamount").toString());  //派工数量
                       UFDouble rkamount=new UFDouble(hmrkmount.get(pk_invbasdoc)==null?"0":hmrkmount.get(pk_invbasdoc).toString());  //入库数量
                       UFDouble amount = pgamount.sub(rkamount);        //差异
                       
                       vo.setAmount(amount);
                       vo.setBrandname(brandname);
                       vo.setColour(colour);
                       vo.setInvname(invname);
                       vo.setInvspec(invspec);
                       vo.setInvtype(invtype);
                       vo.setPgamount(pgamount);
                       vo.setPgdate(pgdate);
                       vo.setRkamount(rkamount);
                       vo.setAmount(amount);
                       
                       list.add(vo);
                   }
               }
               if(list.size()>0){
                   vos = (ReportVO[]) list.toArray(new ReportVO[0]);
               }
               if(vos!=null && vos.length>0){
                   this.getReportBase().setBodyDataVO(vos);
                   
                   /*显示合计项*/
                   String[] strValKeys = {"pgamount","rkamount","amount"};
                   SubtotalContext stctx = new SubtotalContext();
                   stctx.setSubtotalCols(strValKeys);  //配置要进行合计的字段                  
                   stctx.setTotalNameColKeys("pgdate");  //设置合计项显示列位置
                   stctx.setSumtotalName("合计");    //设置合计项显示名称
                   this.getReportBase().setSubtotalContext(stctx);
                   this.getReportBase().subtotal();
                   this.getReportBase().execHeadLoadFormulas();
                   this.getReportBase().execTailLoadFormulas();
                   updateUI();
               }
               else{
                   this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
               }
           }
      }
	  
	  /***
		 * 根据选择区间找到入库单中的数据
		 * wb 2009-3-25 14:40:59
		 * @param start_date
		 * @param end_date
		 * @param start_sj
		 * @param end_sj
		 * @param pk_corp
		 * @return
		 * @throws Exception
		 */
		  @SuppressWarnings("unchecked")
		public HashMap getRkData(UFDate start_date,UFDate end_date,int start_sj,int end_sj,String pk_corp) throws Exception{
			  HashMap hm = new HashMap();
			  //先找到期间段所有的计划单pk
			  StringBuffer pgdsql = new StringBuffer()					
	          .append("  select pk_pgd")
	          .append("  from eh_sc_pgd")
	          .append("  WHERE xdflag = 'Y' and isnull(lock_flag,'N')<>'Y'")
	          .append("  and scdate between '"+start_date+"' and '"+end_date+"'")
	          .append("  AND SUBSTRING (ts,12,2) BETWEEN "+start_sj+" AND "+end_sj+"")
	          .append("  and pk_corp='"+pk_corp+"' ")
	          .append("  and isnull(dr,0)=0");
			  IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	          ArrayList pgdarr = (ArrayList)iUAPQueryBS.executeQuery(pgdsql.toString(),new MapListProcessor());
	          if(pgdarr!=null && pgdarr.size()>0){
	        	  StringBuffer addCondtion = new StringBuffer(" and (  ");
	              String pk_pgd = null;
	              for(int i=0;i<pgdarr.size();i++){
	                  HashMap hmA = (HashMap)pgdarr.get(i);
	                  pk_pgd = hmA.get("pk_pgd")==null?"":hmA.get("pk_pgd").toString();
	                  if(i!=0){
	                	  addCondtion.append(" or ");
	                  }
	                  addCondtion.append(" a.vsourcebillid LIKE '%"+pk_pgd+"%'");
	              }
	              addCondtion.append(" )");
	              
	              //到入库单中找到对应的入库单数据
	              StringBuffer rksql = new StringBuffer()
	              .append(" select b.pk_invbasdoc ,sum(isnull(b.rkmount,0)) rkmount ")
	               .append(" from eh_sc_cprkd a,eh_sc_cprkd_b b  ")
	               .append(" where a.pk_rkd=b.pk_rkd and a.pk_corp='"+pk_corp+"' and a.vbillstatus=1 ")
	               .append(" and isnull(a.vsourcebillid,' ')<>' ' and a.vbilltype='ZA47' and vsourcebilltype='ZA43' ")
//	               .append(" and a.dmakedate between '"+start_date+"' and '"+end_date+"'  ")
	               .append(addCondtion.toString())
	               .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 group by b.pk_invbasdoc ");
	              ArrayList rkarr = (ArrayList)iUAPQueryBS.executeQuery(rksql.toString(),new MapListProcessor());
	              if(rkarr!=null && rkarr.size()>0){
	            	  String pk_invbasdoc = null;
	                  UFDouble rkmount = null;
	                  for(int i=0;i<rkarr.size();i++){
	                      HashMap hmA = (HashMap)rkarr.get(i);
	                      pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
	                      rkmount = new UFDouble(hmA.get("rkmount")==null?"0":hmA.get("rkmount").toString());
	                      hm.put(pk_invbasdoc,rkmount);
	                  }
	              }
	          }
	          
	          return hm;
		  }
	          
}
