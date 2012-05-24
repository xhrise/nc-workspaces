/*
 * 创建日期 2006-7-1
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.report.h0900410;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
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
import nc.vo.eh.report.h0900410.ScjlVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

/**
 * 生产记录表
 * wb
 * 2009-2-13 11:10:22
 */
public class ScjlReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
    String formula1 = "<html><font color = 'red' size = '4'>";
    String formula2 = "</font></html>";
	/**
	 * 
	 */
	public ScjlReportUI() {
		super();
		m_boQuery = new ButtonObject("查询", "查询报表", 0);
        m_boPrint = new ButtonObject("打印", "打印报表", 0);
        initialize(); 
	}
	
	private void initialize()
	{
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
	    getReportBase().setNotSortCols(st);
	    
	}

	public ButtonObject[] getBtnAry()
	{
	    return (new ButtonObject[] {
	        m_boQuery, m_boPrint
	    });
	   
	}
	public ReportBaseClass getReportBase()
	{
	    if(m_report == null)
	        try
	        {
	            m_report = new ReportBaseClass();
	            m_report.setName("ReportBase");
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900410", null, null);
	      }
	        catch(Exception ex)
	        {
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}
	/* （非 Javadoc）
	 * @see nc.ui.pub.ToftPanel#getTitle()
	 */
	public String getTitle() {
		// TODO 自动生成方法存根
		return m_report.getReportTitle();
	}

	/* （非 Javadoc）
	 * @see nc.ui.pub.ToftPanel#onButtonClicked(nc.ui.pub.ButtonObject)
	 */
	public void onButtonClicked(ButtonObject bo) {
		// TODO 自动生成方法存根
		try
	    {
	        if(bo == m_boQuery){
	            onQuery();
	        }else if(bo == m_boPrint){
	            onPrint();
	        }
	    }
	    catch(BusinessException ex)
	    {
	        showErrorMessage(ex.getMessage());
	        ex.printStackTrace();
	    }
	    catch(Exception e)
	    {
	        showErrorMessage("\u672A\u77E5\u9519\u8BEF:" + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	public QueryConditionClient getQryDlg()
	{
	    if(m_qryDlg == null){
	        m_qryDlg = createQueryDLG();
	       
	    }
	    return m_qryDlg;
	}
	
	protected QueryConditionClient createQueryDLG()
	{
		QueryConditionClient dlg = new QueryConditionClient();
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900410", null, null);
	    UFDate date = ce.getDate();
	    String time = ce.getServerTime().toString();
	    //结束开始日期
	    dlg.setDefaultValue("begindate", date.getDateBefore(1).toString(), "");
//	    dlg.setDefaultValue("begintime",time,"");
	    dlg.setDefaultValue("enddate", date.toString(), "");
//	    dlg.setDefaultValue("endtime",time,"");
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	//设置打印方法
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
	  //查询方法
	  @SuppressWarnings({ "unchecked", "unchecked" })
	public void onQuery() throws Exception{
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		   if(getQryDlg().getResult() == 1){
		   	 ConditionVO[] conbegindate  = getQryDlg().getConditionVOsByFieldCode("begindate");
		   	 ConditionVO[] conbegintime  = getQryDlg().getConditionVOsByFieldCode("begintime");
		   	 ConditionVO[] conenddate  = getQryDlg().getConditionVOsByFieldCode("enddate");
		   	 ConditionVO[] conendtime  = getQryDlg().getConditionVOsByFieldCode("endtime");
		   	 ConditionVO[] conbz  = getQryDlg().getConditionVOsByFieldCode("bz");
		   	 
		   	 UFDate begindate = new UFDate(conbegindate[0].getValue());
		   	 String begintime = conbegintime!=null&&conbegintime.length>0?conbegintime[0].getValue()==null?"00:00:00":conbegintime[0].getValue():"00:00:00";					//开始时间
		   	 UFDate enddate = new UFDate(conenddate[0].getValue());
		   	 String endtime = conendtime!=null&&conendtime.length>0?conendtime[0].getValue()==null?"00:00:00":conendtime[0].getValue():"00:00:00";						//结束时间
		   	 String begin = begindate.toString()+" "+begintime;
		   	 String end = enddate.toString()+" "+endtime;
		   	 String bz = conbz!=null&&conbz.length>0?conbz[0].getValue()==null?"%%":conbz[0].getValue():"%%";											//班组
		   	 
		   	 String unitcode = ce.getCorporation().getUnitcode();			//公司编码
		   	 
		   	 if(!isExitPFdata(unitcode)){
		   		 this.showErrorMessage("无法连接微机数据库,数据库中没有名为 pf"+unitcode+" 的数据库,请检查!");
		   		 return;
		   	 }
		   	 
		   	 ScjlVO[] vos = getBvos(begin, end, unitcode,bz,ce.getCorporation().getPk_corp());
		   	 if (vos!=null&&vos.length>0){
	   	  		this.getReportBase().getHeadItem("begintime").setValue(begin);
	   	  		this.getReportBase().getHeadItem("endtime").setValue(end);
	   	  		this.getReportBase().getHeadItem("bz").setValue(bz=="%%"?null:bz);
	   	  		this.getReportBase().setBodyDataVO(vos);
	   	  		
//	   	  		//显示合计项
//	   	  		String[] strsubValKeys = {"llz","sjz","cy"};
//		        String[] strgrpValKeys = {"invname","pc"};
//		        
//		        String sumStr = "小计";
//		        SubtotalContext stctx = new SubtotalContext();
//		        stctx.setGrpKeys(strgrpValKeys);
//		        stctx.setSubtotalCols(strsubValKeys);	//配置要进行合计的字段
//		        stctx.setIsSubtotal(true);	//需要小计
//		        stctx.setLevelCompute(false);
//		        stctx.setSubtotalName(sumStr);
//		        stctx.setTotalNameColKeys("invcode");	//设置合计项显示列位置
//		        stctx.setSumtotalName("合计");	//设置合计项显示名称
//		        this.getReportBase().setSubtotalContext(stctx);
//		        this.getReportBase().subtotal();
//		   	  	this.getReportBase().execHeadLoadFormulas();
//	        	this.getReportBase().execTailLoadFormulas();
		   	  } 
		   	  else
		   	  		this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
		   }
	  }
	  
	  /***
	   * 到微机数据库中取生产记录明细数据
	   * @param begin
	   * @param end
	   * @param unitcode
	   * @return
	   * @throws Exception
	   */
	  @SuppressWarnings({ "unchecked", "unchecked" })
	public ScjlVO[] getBvos(String begin,String end,String unitcode,String bc,String pk_corp) throws Exception{
		  ScjlVO[] bvos = null;
		  StringBuffer sql = new StringBuffer()
		  .append("  SELECT a.pfbm invcode,a.pfh invname,b.invspec,b.invtype,b.colour,a.pc,a.bc bz,a.wlbm ylcode,a.lm ylname,a.llz,a.sjz,a.wc,a.rqsj")
		  .append("  FROM ")
		  .append("  pf"+unitcode+".dbo.sjzk a  left join eh_invbasdoc b")
		  .append("  on a.pfbm = b.invcode where  a.rqsj between '"+begin+"' and '"+end+"' and a.bc like '"+bc+"'  order by a.rqsj ");
		  IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		  if(arr!=null&&arr.size()>0){
			  ArrayList arrvos = new ArrayList();
			  
			  String invcode = null;
			  String invname = null;
			  String invspec = null;
			  String invtype = null;
			  String colour = null;
			  String pc = null;
			  String bz = null;
			  String ylcode = null;
			  String ylname = null;
			  UFDouble llz = new UFDouble(0);
			  UFDouble sjz = new UFDouble(0);
			  String cy = null;
			  String scdate = null;
			  
			  String oldInv = "";
			  HashMap hmSub = getSubtotalBvos(begin, end, unitcode, bc, pk_corp);
			  for(int i=0;i<arr.size();i++){
				  HashMap hmA = (HashMap)arr.get(i);
				  invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();
				  invname = hmA.get("invname")==null?"":hmA.get("invname").toString();
				  invspec = hmA.get("invspec")==null?"":hmA.get("invspec").toString();
				  invtype = hmA.get("invtype")==null?"":hmA.get("invtype").toString();
				  colour = hmA.get("colour")==null?"":hmA.get("colour").toString();
				  pc = hmA.get("pc")==null?"":hmA.get("pc").toString();
				  bz = hmA.get("bz")==null?"":hmA.get("bz").toString();
				  ylcode = hmA.get("ylcode")==null?"":hmA.get("ylcode").toString();
				  ylname = hmA.get("ylname")==null?"":hmA.get("ylname").toString();
				  llz = new UFDouble(hmA.get("llz")==null?"0":hmA.get("llz").toString());
				  sjz = new UFDouble(hmA.get("sjz")==null?"0":hmA.get("sjz").toString());
				  cy = hmA.get("wc")==null?"0":hmA.get("wc").toString();
				  scdate = hmA.get("rqsj")==null?"":hmA.get("rqsj").toString();
					  
				  ScjlVO bvo = new ScjlVO();
				  bvo.setInvcode(invcode);
				  bvo.setInvname(invname);
				  bvo.setInvspec(invspec);
				  bvo.setInvtype(invtype);
				  bvo.setColour(colour);
				  bvo.setPc(pc);
				  bvo.setBz(bz);
				  bvo.setYlcode(ylcode);
				  bvo.setYlname(ylname);
				  bvo.setLlz(llz);
				  bvo.setSjz(sjz);
				  if(new UFDouble(cy).abs().toDouble()>5){
					  cy = formula1+cy+formula2;
				  }
				  bvo.setCy(cy);
				  bvo.setScdate(scdate);
				  
				  String newInv = invcode+invname+invspec+invtype+colour+pc+bz;
				  if(!newInv.equals(oldInv) && i>0){//加上小计
						if(hmSub.get(oldInv)!=null){
		            		ScjlVO subvo = (ScjlVO)hmSub.get(oldInv);
		            		arrvos.add(subvo);
		            								//加上每盘间隔时间
		            		ScjlVO subjgvo = new ScjlVO();
		            		String preendtime = subvo.getEndtime();
//		            		subvo.setPc(null);
		            		String nextbegintime = scdate;
		            		String gjtime = getTimecj(preendtime,nextbegintime);
		            		subjgvo.setInvcode("每盘间隔时间");
		            		if(isSuper5or6(gjtime,6)){	//超过6分钟用红色标记
		            			subjgvo.setScdate(formula1+gjtime+formula2);
		            		}else{
		            			subjgvo.setScdate(gjtime);
		            		}
		            		arrvos.add(subjgvo);
						}
		            }
				  oldInv = newInv;
		          
				  arrvos.add(bvo);
				  
				  if(i==arr.size()-1){   //加上最后的合计
					  ScjlVO sumvo = getSumBvo(begin, end, unitcode, bc, pk_corp);
					  arrvos.add(sumvo);
				  }
			  }
			  bvos = (ScjlVO[])arrvos.toArray(new ScjlVO[arrvos.size()]);
		  }
		  return bvos;
	  }
	  
	  /***
	   * 根据产品生产批次小计 
	   * @param begin
	   * @param end
	   * @param unitcode
	   * @return
	   * @throws Exception
	   */
	  @SuppressWarnings("unchecked")
	public HashMap getSubtotalBvos(String begin,String end,String unitcode,String bc,String pk_corp) throws Exception{
		  HashMap hm = new HashMap();
		  StringBuffer sql = new StringBuffer()
		  .append("  SELECT a.pfbm invcode,a.pfh invname,b.invspec,b.invtype,b.colour,a.pc,a.bc bz,")
		  .append("          SUM(ISNULL(a.llz,0)) llz,SUM(ISNULL (a.sjz,0)) sjz,SUM(ISNULL(a.wc,0)) wc,MIN(rqsj) minsj,MAX(rqsj) maxsj ")
		  .append("  FROM  pf"+unitcode+".dbo.sjzk a  left join eh_invbasdoc b")
		  .append("   on a.pfbm = b.invcode where  a.rqsj between '"+begin+"' and '"+end+"'")
		  .append("   and a.bc like '%"+bc+"%'")
		  .append("  GROUP BY a.pfbm,a.pfh,b.invspec,b.invtype,b.colour,a.pc,a.bc");
		  IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		  if(arr!=null&&arr.size()>0){
			  String invcode = null;
			  String invname = null;
			  String invspec = null;
			  String invtype = null;
			  String colour = null;
			  String pc = null;
			  String bz = null;
			  UFDouble llz = new UFDouble(0);
			  UFDouble sjz = new UFDouble(0);
			  String cy = null;
			  String begintime = null;
			  String endtime = null;
			  String sjcy = null;
			  
			  for(int i=0;i<arr.size();i++){
				  HashMap hmA = (HashMap)arr.get(i);
				  invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();
				  invname = hmA.get("invname")==null?"":hmA.get("invname").toString();
				  invspec = hmA.get("invspec")==null?"":hmA.get("invspec").toString();
				  invtype = hmA.get("invtype")==null?"":hmA.get("invtype").toString();
				  colour = hmA.get("colour")==null?"":hmA.get("colour").toString();
				  pc = hmA.get("pc")==null?"":hmA.get("pc").toString();
				  bz = hmA.get("bz")==null?"":hmA.get("bz").toString();
				  llz = new UFDouble(hmA.get("llz")==null?"0":hmA.get("llz").toString());
				  sjz = new UFDouble(hmA.get("sjz")==null?"0":hmA.get("sjz").toString());
				  cy = hmA.get("wc")==null?"0":hmA.get("wc").toString();
				  begintime = hmA.get("minsj")==null?"":hmA.get("minsj").toString();			//一批料开始时间
				  endtime = hmA.get("maxsj")==null?"2009-02-24 08:00:00":hmA.get("maxsj").toString();				//一批料结束时间
				  sjcy = getTimecj(begintime, endtime);
				  
				  ScjlVO bvo = new ScjlVO();
				  bvo.setInvcode("每盘生产时间");
				  bvo.setEndtime(endtime);
//				  bvo.setBz(bz);
				  bvo.setLlz(llz);
				  bvo.setSjz(sjz);
				  if(new UFDouble(cy).abs().toDouble()>5){
					  cy = formula1+cy+formula2;
				  }
				  bvo.setCy(cy);
				  if(isSuper5or6(sjcy,5)){	//超过5分钟用红色标记
					  bvo.setScdate(formula1+sjcy+formula2);
	          	  }else{
	          		bvo.setScdate(sjcy);
	          	  }
				  
				  hm.put(invcode+invname+invspec+invtype+colour+pc+bz, bvo);
			  }
		  }
		  return hm;
	  }
	  
	  /***
	   * 总合计
	   * @param begin
	   * @param end
	   * @param unitcode
	   * @return
	   * @throws Exception
	   */
	  public ScjlVO getSumBvo(String begin,String end,String unitcode,String bc,String pk_corp) throws Exception{
		  ScjlVO bvo = new ScjlVO();
		  StringBuffer sql = new StringBuffer()
		  .append("  SELECT  SUM(ISNULL(a.llz,0)) llz,SUM(ISNULL (a.sjz,0)) sjz,SUM(ISNULL(a.wc,0)) wc,MIN(rqsj) minsj,MAX(rqsj) maxsj ")
		  .append("  FROM  pf"+unitcode+".dbo.sjzk a  left join eh_invbasdoc b")
		  .append("   on a.pfbm = b.invcode where  a.rqsj between '"+begin+"' and '"+end+"'")
		  .append("   and a.bc like '%"+bc+"%'");
		  IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		  if(arr!=null&&arr.size()>0){
			  UFDouble llz = new UFDouble(0);
			  UFDouble sjz = new UFDouble(0);
			  String cy = null;
			  String begintime = null;
			  String endtime = null;
			  String sjcy = null;
			  
			  HashMap hmA = (HashMap)arr.get(0);
			  llz = new UFDouble(hmA.get("llz")==null?"0":hmA.get("llz").toString());
			  sjz = new UFDouble(hmA.get("sjz")==null?"0":hmA.get("sjz").toString());
			  cy = hmA.get("wc")==null?"0":hmA.get("wc").toString();
			  begintime = hmA.get("minsj")==null?"":hmA.get("minsj").toString();			//一批料开始时间
			  endtime = hmA.get("maxsj")==null?"":hmA.get("maxsj").toString();				//一批料结束时间
			  sjcy = getTimecj(begintime, endtime);
			  
			  bvo.setInvcode("合计");
			  bvo.setLlz(llz);
			  bvo.setSjz(sjz);
			  if(new UFDouble(cy).abs().toDouble()>5){
				  cy = formula1+cy+formula2;
			  }
			  bvo.setCy(cy);
			  bvo.setScdate(formula1+sjcy+formula2);
		  }
		  return bvo;
	  }
	  
	  
	  /***
	   * 判断是否存在微机数据库
	   * wb 2009年2月13日14:11:02
	   * @return
	   */
	  public boolean isExitPFdata(String unitcode) throws Exception{
		   String sql = " SELECT * FROM sys.databases WHERE NAME = 'pf"+unitcode+"' ";
		   IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		   ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		   if(arr==null||arr.size()==0){
			   return false;
		   }
		   return true;
	  }
	  
	  
	  /***
	   * 两个时间间的差异
	   * @param begintime
	   * @param endtime
	   * @return
	   * @throws Exception
	   */
	  public String getTimecj(String begintime, String endtime) throws Exception {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(endtime==null||endtime.length()==0){
				endtime = "2009-02-24 22:00:00";
			}
			java.util.Date now = df.parse(endtime);
			java.util.Date date = df.parse(begintime);
			long l = now.getTime() - date.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
			StringBuffer cys = new StringBuffer();
			if(day!=0){
				cys.append(day+"天");
			}
			if(hour!=0){
				cys.append(hour+"小时");
			}
			if(min!=0){
				cys.append(min+"分");
			}
			if(s!=0){
				cys.append(s+"秒");
			}
			return cys.toString();
	}
	
	public boolean isSuper5or6(String time,int flag){
		boolean isSuper = false;
		if(time.indexOf("小时")<=0){
			if(time.indexOf("分")>0){
				int miniutes = Integer.parseInt(time.substring(0,time.indexOf("分")));
				if(miniutes==flag){
					if(time.length()>(time.indexOf("分")+1)){
						int seconds = Integer.parseInt(time.substring(time.indexOf("分")+1,time.indexOf("秒")));
						if(seconds>0){
							isSuper = true;
						}
					}
				}else if(miniutes>flag){
					isSuper = true;
				}
			}
		}else{
			isSuper = true;
		}
		return isSuper;
	}
	  
}
