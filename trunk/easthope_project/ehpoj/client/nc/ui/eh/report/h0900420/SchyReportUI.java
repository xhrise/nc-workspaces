/*
 * 创建日期 2006-7-1
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.report.h0900420;

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
import nc.vo.eh.report.h0900410.ScjlVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 生产耗用表
 * wb
 * 2009-2-25 15:36:28
 */
public class SchyReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	public SchyReportUI() {
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900420", null, null);
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
	@Override
	public String getTitle() {
		// TODO 自动生成方法存根
		return m_report.getReportTitle();
	}

	/* （非 Javadoc）
	 * @see nc.ui.pub.ToftPanel#onButtonClicked(nc.ui.pub.ButtonObject)
	 */
	@Override
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900420", null, null);
	    UFDate date = ce.getDate();
	    String time = ClientEnvironment.getServerTime().toString();
	    //结束开始日期
	    dlg.setDefaultValue("begindate", date.getDateBefore(1).toString(), "");
//	    dlg.setDefaultValue("begintime","19:00:00","");
	    dlg.setDefaultValue("enddate", date.toString(), "");
	    dlg.setDefaultValue("endtime",time,"");
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
	   	  		this.getReportBase().getHeadItem("begindate").setValue(begin);
	   	  		this.getReportBase().getHeadItem("enddate").setValue(end);
//	   	  		this.getReportBase().getHeadItem("bz").setValue(bz=="%%"?null:bz);
	   	  		this.getReportBase().setBodyDataVO(vos);
	   	  		//	   	  	显示合计项
	   	  		String[] strsubValKeys = {"llz","sjz","cy"};
		        
		        SubtotalContext stctx = new SubtotalContext();
//		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//配置要进行合计的字段
//		        stctx.setIsSubtotal(true);	//需要小计
//		        stctx.setLevelCompute(false);
//		        stctx.setSubtotalName("小计");
		        stctx.setTotalNameColKeys("ylname");	//设置合计项显示列位置
		        stctx.setSumtotalName("合计");	//设置合计项显示名称
		        this.getReportBase().setSubtotalContext(stctx);
		        this.getReportBase().subtotal();
		   	  	this.getReportBase().execHeadLoadFormulas();
	        	this.getReportBase().execTailLoadFormulas();
		   	  } 
		   	  else
		   	  		this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
		   }
	  }
	  
	  /***
	   * 到微机数据库中取生产记录
	   * @param begin
	   * @param end
	   * @param unitcode
	   * @return
	   * @throws Exception
	   */
	  public ScjlVO[] getBvos(String begin,String end,String unitcode,String bc,String pk_corp) throws Exception{
		  ScjlVO[] bvos = null;
		  StringBuffer sql = new StringBuffer()
		  .append("  SELECT a.lm ylname,sum(isnull(a.llz,0)) llz,sum(isnull(a.sjz,0)) sjz,sum(isnull(a.wc,0)) wc")
		  .append("  FROM ")
		  .append("  pf"+unitcode+".dbo.sjzk a")
		  .append(" where  a.rqsj between '"+begin+"' and '"+end+"' group by a.lm ");
		  IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		  if(arr!=null&&arr.size()>0){
			  String ylname = null;
			  UFDouble llz = new UFDouble(0);
			  UFDouble sjz = new UFDouble(0);
			  String cy = null;
			  bvos = new ScjlVO[arr.size()];
			  for(int i=0;i<arr.size();i++){
				  HashMap hmA = (HashMap)arr.get(i);
				  ylname = hmA.get("ylname")==null?"":hmA.get("ylname").toString();
				  llz = new UFDouble(hmA.get("llz")==null?"0":hmA.get("llz").toString());
				  sjz = new UFDouble(hmA.get("sjz")==null?"0":hmA.get("sjz").toString());
				  cy = hmA.get("wc")==null?"0":hmA.get("wc").toString();
				  
				  bvos[i] = new ScjlVO();
				  bvos[i].setYlname(ylname);
				  bvos[i].setLlz(llz);
				  bvos[i].setSjz(sjz);
				  bvos[i].setCy(cy);
			  }
		  }
		  return bvos;
	  }
	  
	  /***
	   * 判断是否存在微机数据库
	   * wb 2009年2月13日14:11:02
	   * @return
	   */
	  public boolean isExitPFdata(String unitcode) throws Exception{
		   String sql = " SELECT * FROM tabs WHERE table_name = 'pf"+unitcode+"' ";
		   IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		   ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		   if(arr==null||arr.size()==0){
			   return false;
		   }
		   return true;
	  }
}
