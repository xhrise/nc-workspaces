
package nc.ui.eh.report.h0900404;

/*
 * 功能:生产任务与派工完成情况明细表
 * 作者:zqy
 * 时间:2008-08-05
 */

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
import nc.vo.eh.report.h0900404.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900404", null, null);
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
	    catch(Exception e){
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900404", null, null);
        
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
            ReportVO[] rvos = null;         
            getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
            if(getQryDlg().getResult() == 1) {
                ConditionVO[] startdate  = getQryDlg().getConditionVOsByFieldCode("startdate");          //开始日期
                ConditionVO[] enddate  = getQryDlg().getConditionVOsByFieldCode("enddate");              //结束日期
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
                
                HashMap hmpgmount = getPlanData(start_date, end_date, start_sj, end_sj, pk_corp);		//计划表中数据
                
                IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                StringBuffer sql = new StringBuffer()
//                .append(" select b.pk_invbasdoc ,a.scdate ,c.invname ,c.invspec ,c.invtype ,c.colour,d.brandname ,sum(isnull(b.scmount,0)) scamount ")
//                .append(" from eh_sc_posm a , eh_sc_posm_b b ,eh_invbasdoc c ,eh_brand d ")
//                .append(" where a.pk_posm=b.pk_posm and b.pk_invbasdoc = c.pk_invbasdoc ")
//                .append(" and c.brand = d.pk_brand and a.vbillstatus=1 ")
//                .append(" and a.scdate between '"+start_date+"' and '"+end_date+"' AND SUBSTRING (a.ts,12,2) BETWEEN "+start_sj+" AND "+end_sj+" and a.pk_corp='"+pk_corp+"' ")
//                .append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 ")
//                .append(" group by b.pk_invbasdoc ,a.scdate ,c.invname ,c.invspec ,c.invtype ,c.colour,d.brandname ");
                .append(" select b.pk_invbasdoc, ")
                .append("        a.scdate, ")
                .append("        c.invname, ")
                .append("        c.invspec, ")
                .append("        c.invtype, ")
                .append("        c.def1 colour, ")
                .append("        d.brandname, ")
                .append("        sum(nvl(b.scmount, 0)) scamount ")
                .append("   from eh_sc_posm   a, ")
                .append("        eh_sc_posm_b b, ")
                .append("        bd_invbasdoc c, ")
                .append("        bd_invmandoc cc, ")
                .append("        eh_brand     d ")
                .append("  where a.pk_posm = b.pk_posm ")
                .append("    and b.pk_invbasdoc = cc.pk_invmandoc ")
                .append("    and c.pk_invbasdoc = cc.pk_invbasdoc ")
                .append("    and c.invpinpai = d.pk_brand ")
                .append("    and a.vbillstatus = 1 ")
                .append("    and a.scdate between '"+start_date+"' and '"+end_date+"' ")
                .append("    AND SUBSTR(a.ts, 12, 2) BETWEEN "+start_sj+" AND "+end_sj+" ")
                .append("    and a.pk_corp = '"+pk_corp+"' ")
                .append("    and nvl(a.dr, 0) = 0 ")
                .append("    and nvl(b.dr, 0) = 0 ")
                .append("  group by b.pk_invbasdoc, ")
                .append("           a.scdate, ")
                .append("           c.invname, ")
                .append("           c.invspec, ")
                .append("           c.invtype, ")
                .append("           c.def1, ")
                .append("           d.brandname ");
                ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                if(al!=null && al.size()>0){
                    for(int i=0;i<al.size();i++){
                        HashMap hm = (HashMap)al.get(i);
                        String pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                        UFDate rwdate = new UFDate(hm.get("scdate")==null?"":hm.get("scdate").toString());
                        String invname = hm.get("invname")==null?"":hm.get("invname").toString();
                        String invtype = hm.get("invtype")==null?"":hm.get("invtype").toString();
                        String invspec = hm.get("invspec")==null?"":hm.get("invspec").toString();
                        String colour = hm.get("colour")==null?"":hm.get("colour").toString();
                        String brandname = hm.get("brandname")==null?"":hm.get("brandname").toString();
                        UFDouble rwamount = new UFDouble(hm.get("scamount")==null?"0":hm.get("scamount").toString());
                        UFDouble pgamount = new UFDouble(hmpgmount.get(pk_invbasdoc+rwdate)==null?"0":hmpgmount.get(pk_invbasdoc+rwdate).toString());
                        UFDouble amount = rwamount.sub(pgamount);
                        
                        ReportVO rvo = new ReportVO();
                        rvo.setAmount(amount);
                        rvo.setBrandname(brandname);
                        rvo.setColour(colour);
                        rvo.setInvname(invname);
                        rvo.setInvspec(invspec);
                        rvo.setInvtype(invtype);
                        rvo.setPgamount(pgamount);
                        rvo.setRwamount(rwamount);
                        rvo.setRwdate(rwdate);
                        list.add(rvo);
                    }
                }
                if(list.size()>0){
                    rvos = (ReportVO[]) list.toArray(new ReportVO[0]);
                }
                if(rvos!=null && rvos.length>0){
                    this.getReportBase().setBodyDataVO(rvos);
                    
                    /*显示合计项*/
                    String[] strValKeys = {"rwamount","pgamount","amount"};
                    SubtotalContext stctx = new SubtotalContext();
                    stctx.setSubtotalCols(strValKeys);  //配置要进行合计的字段
                    stctx.setTotalNameColKeys("rwdate");  //设置合计项显示列位置
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
	 * 根据选择区间找到计划表中的数据
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
	public HashMap getPlanData(UFDate start_date,UFDate end_date,int start_sj,int end_sj,String pk_corp) throws Exception{
		  HashMap hm = new HashMap();
		  //先找到期间段所有的生产订单pk
		  StringBuffer posmsql = new StringBuffer()					
          .append("  select pk_posm")
          .append("  from eh_sc_posm")
          .append("  WHERE vbillstatus=1 ")
          .append("  and scdate between '"+start_date+"' and '"+end_date+"'")
          .append("  AND SUBSTRING (ts,12,2) BETWEEN "+start_sj+" AND "+end_sj+"")
          .append("  and pk_corp='"+pk_corp+"' ")
          .append("  and isnull(dr,0)=0");
		  IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          ArrayList posmarr = (ArrayList)iUAPQueryBS.executeQuery(posmsql.toString(),new MapListProcessor());
          if(posmarr!=null && posmarr.size()>0){
        	  StringBuffer addCondtion = new StringBuffer(" and (  ");
              String pk_posm = null;
              for(int i=0;i<posmarr.size();i++){
                  HashMap hmA = (HashMap)posmarr.get(i);
                  pk_posm = hmA.get("pk_posm")==null?"":hmA.get("pk_posm").toString();
                  if(i!=0){
                	  addCondtion.append(" or ");
                  }
                  addCondtion.append(" a.vsourcebillid LIKE '%"+pk_posm+"%'");
              }
              addCondtion.append(" )");
              
              //到计划表中找到对应的计划数据
              StringBuffer pgdsql = new StringBuffer()
              .append("   select b.pk_invbasdoc ,a.pgdate,sum(isnull(b.pgamount,0)) pgamount ")
              .append("   from eh_sc_pgd a,eh_sc_pgd_b b ")
              .append("   where a.pk_pgd=b.pk_pgd and a.vsourcebilltype='ZA44' ")
              .append("   and a.pk_corp='"+pk_corp+"' ")
              .append(addCondtion.toString())
              .append("   and a.xdflag = 'Y' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0")
              .append("   group by b.pk_invbasdoc,a.pgdate");
              ArrayList pgdarr = (ArrayList)iUAPQueryBS.executeQuery(pgdsql.toString(),new MapListProcessor());
              if(pgdarr!=null && pgdarr.size()>0){
            	  String pk_invbasdoc = null;
            	  String pgdate = null;
                  UFDouble pgamount = null;
                  for(int i=0;i<pgdarr.size();i++){
                      HashMap hmA = (HashMap)pgdarr.get(i);
                      pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
                      pgdate = hmA.get("pgdate")==null?"":hmA.get("pgdate").toString();
                      pgamount = new UFDouble(hmA.get("pgamount")==null?"0":hmA.get("pgamount").toString());
                      hm.put(pk_invbasdoc+pgdate,pgamount);
                  }
              }
          }
          
          return hm;
	  }
          
}
