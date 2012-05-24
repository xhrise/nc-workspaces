/*
 * 创建日期 2006-7-1
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.report.h0901010;

import java.awt.BorderLayout;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.report.h0901010.CalcMoneyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 现金流量分析
 * wb 2008-8-7 19:28:46
 */
public class CalcMoneyReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	public CalcMoneyReportUI() {
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0901010", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0901010", null, null);
//	    UFDate date = ce.getDate();
//	    //结束开始日期
//	    dlg.setDefaultValue("date",date.toString(),"");
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
	  		this.getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		   if(getQryDlg().getResult() == 1){
		   	  ConditionVO[] conbegindate  = getQryDlg().getConditionVOsByFieldCode("begindate");	
			  if(conbegindate==null||conbegindate.length==0){
				  showErrorMessage("请选择开始日期!");
				  return;
			  }
			 UFDate begindate = new UFDate(conbegindate[0].getValue());
			 ConditionVO[] conenddate  = getQryDlg().getConditionVOsByFieldCode("enddate");	
			  if(conenddate==null||conenddate.length==0){
				  showErrorMessage("请选择结束日期!");
				  return;
			  }
			 UFDate enddate = new UFDate(conenddate[0].getValue());
			 CalcKcybbVO kcVO = new CalcKcybbVO();
			 kcVO.setPk_corp(ce.getCorporation().getPk_corp());
			 kcVO.setCalcdate(begindate);
			 kcVO.setPk_period(enddate.toString());
			 
			 PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
			 CalcMoneyVO[] vos= pubItf.CalcMoney(kcVO);
		   	  if (vos!=null&&vos.length>0){
		   		this.getReportBase().setBodyDataVO(vos);
		   	  	ArrayList arrFileds = new ArrayList();
	   	  		String[] fileds = getReportBase().getBodyFields();
	   	  		for(int i=0;i<fileds.length;i++){
	   	  			String filed = fileds[i];
	   	  			if(filed.endsWith("je")){
	   	  			   arrFileds.add(filed);
	   	  			}
	   	  		}
	   	  		//显示合计项
	   	  		String[] strsubValKeys = (String[])arrFileds.toArray(new String[arrFileds.size()]);
		        String[] strgrpValKeys = {"date"};
		        
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//配置要进行合计的字段
		        stctx.setIsSubtotal(true);	//需要小计
//		        stctx.setLevelCompute(true);
		        stctx.setSubtotalName("小计");
		        stctx.setTotalNameColKeys("date");	//设置合计项显示列位置
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
}
