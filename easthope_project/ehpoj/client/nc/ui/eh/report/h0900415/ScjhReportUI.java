/*
 * 创建日期 2006-7-1
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.report.h0900415;

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
import nc.vo.eh.report.h0900415.ScjhVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 生产计划表
 * wb
 * 2009-2-13 11:10:22
 */
public class ScjhReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	public ScjhReportUI() {
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900415", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900415", null, null);
	    UFDate date = ce.getDate();
	    //结束开始日期
	    dlg.setDefaultValue("begindate", date.toString(), "");
	    dlg.setDefaultValue("enddate", date.toString(), "");
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
		   	 ConditionVO[] conenddate  = getQryDlg().getConditionVOsByFieldCode("enddate");
		   	 
		   	 UFDate begindate = new UFDate(conbegindate[0].getValue());
		   	 UFDate enddate = new UFDate(conenddate[0].getValue());
		   	 
		   	 String unitcode = ce.getCorporation().getUnitcode();			//公司编码
		   	 String pk_corp = ce.getCorporation().getPk_corp();
		   	 
		   	 if(!isExitPFdata(unitcode)){
		   		 this.showErrorMessage("无法连接微机数据库,数据库中没有名为 pf"+unitcode+" 的数据库,请检查!");
		   		 return;
		   	 }
		   	 
		   	 ScjhVO[] vos = getBvos(begindate.toString(), enddate.toString(), unitcode,pk_corp);
		   	 if (vos!=null&&vos.length>0){
	   	  		this.getReportBase().getHeadItem("begindate").setValue(begindate);
	   	  		this.getReportBase().getHeadItem("enddate").setValue(enddate);
	   	  		this.getReportBase().setBodyDataVO(vos);
	   	  		
	   	  		//显示合计项
	   	  		String[] strsubValKeys = {"jhsc","cdsc","ysc","wsc"};
		        
		        SubtotalContext stctx = new SubtotalContext();
//		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//配置要进行合计的字段
//		        stctx.setIsSubtotal(true);	//需要小计
//		        stctx.setLevelCompute(false);
//		        stctx.setSubtotalName("小计");
		        stctx.setTotalNameColKeys("invcode");	//设置合计项显示列位置
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
	   * 到微机数据库中取生产计划
	   * @param begin
	   * @param end
	   * @param unitcode
	   * @return
	   * @throws Exception
	   */
	  public ScjhVO[] getBvos(String begin,String end,String unitcode,String pk_corp) throws Exception{
		  ScjhVO[] bvos = null;
		  StringBuffer sql = new StringBuffer()
		  .append("  SELECT a.pfbm invcode,a.pfmc invname,b.invspec,b.invtype,b.colour,")
		  .append("  	   SUM(ISNULL(a.jhsc,0)) jhsc,SUM(ISNULL(a.pfsc,0)) pfsc,SUM(ISNULL(a.fjhsc,0)) cdsc,SUM(ISNULL(a.sjsc,0)) ysc,SUM(ISNULL(a.wsc,0)) wsc")
		  .append("  FROM pf"+unitcode+".dbo.jhb a,eh_invbasdoc b")
		  .append("  WHERE a.pfbm = b.invcode")
		  .append("  AND a.ddrq BETWEEN '"+begin+"' AND '"+end+"'")
		  .append("  AND b.pk_corp = '"+pk_corp+"'")
		  .append("  GROUP BY a.pfbm,a.pfmc,b.invspec,b.invtype,b.colour");
		  IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		  if(arr!=null&&arr.size()>0){
			  String invcode = null;
			  String invname = null;
			  String invspec = null;
			  String invtype = null;
			  String colour = null;
			  UFDouble jhsc = new UFDouble(0);			//计划生产
			  UFDouble pfsc = new UFDouble(0);			//配方生产
			  UFDouble cdsc = new UFDouble(0);		
			  UFDouble ysc = new UFDouble(0);
			  UFDouble wsc = new UFDouble(0);
			  
			  bvos = new ScjhVO[arr.size()];
			  for(int i=0;i<arr.size();i++){
				  HashMap hmA = (HashMap)arr.get(i);
				  invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();
				  invname = hmA.get("invname")==null?"":hmA.get("invname").toString();
				  invspec = hmA.get("invspec")==null?"":hmA.get("invspec").toString();
				  invtype = hmA.get("invtype")==null?"":hmA.get("invtype").toString();
				  colour = hmA.get("colour")==null?"":hmA.get("colour").toString();
				  jhsc = new UFDouble(hmA.get("jhsc")==null?"0":hmA.get("jhsc").toString());
				  pfsc = new UFDouble(hmA.get("pfsc")==null?"0":hmA.get("pfsc").toString());
				  cdsc = new UFDouble(hmA.get("cdsc")==null?"0":hmA.get("cdsc").toString());
				  ysc = new UFDouble(hmA.get("ysc")==null?"0":hmA.get("ysc").toString());
				  wsc = new UFDouble(hmA.get("wsc")==null?"0":hmA.get("wsc").toString());
				  
				  bvos[i] = new ScjhVO();
				  bvos[i].setInvcode(invcode);
				  bvos[i].setInvname(invname);
				  bvos[i].setInvspec(invspec);
				  bvos[i].setInvtype(invtype);
				  bvos[i].setColour(colour);
				  bvos[i].setJhsc(jhsc);
				  bvos[i].setPfsc(pfsc);
				  bvos[i].setCdsc(cdsc);
				  bvos[i].setYsc(ysc);
				  bvos[i].setWsc(wsc);
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
		   String sql = " SELECT * FROM sys.databases WHERE NAME = 'pf"+unitcode+"' ";
		   IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		   ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		   if(arr==null||arr.size()==0){
			   return false;
		   }
		   return true;
	  }
}
