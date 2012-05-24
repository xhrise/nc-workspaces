package nc.ui.eh.report.h0900401;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 存货进耗存日报表
 * @author houcq 2011-06-08
 */
@SuppressWarnings("serial")
public class ReportUI extends ToftPanel {
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    
    @SuppressWarnings("deprecation")
	public ReportUI() {
    	
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
	    
this.getClientEnvironment().getDate();
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900401", null, null);
	      }
	        catch(Exception ex)
	        {
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}
	@Override
	public String getTitle() {
		return m_report.getReportTitle();
	}

	@Override
	public void onButtonClicked(ButtonObject bo) {
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900401", null, null);
	    
	    //结束开始日期
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	//设置打印方法
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
	  //查询方法
	  public void onQuery() throws Exception{
			String cuername = ce.getUser().getUserName();   // 操作员
	  		this.getReportBase().getHeadItem("date").clearViewData();
	  		this.getReportBase().getHeadItem("penson").clearViewData();
	  		this.getReportBase().getBillModel().clearBodyData();
			QueryConditionClient uidialog = getQryDlg();
			uidialog.setDefaultValue("date", ce.getDate().toString(), "");
			
		    getQryDlg().showModal();
		   if(getQryDlg().getResult() == 1){
			   ConditionVO[] condate  = getQryDlg().getConditionVOsByFieldCode("date");	
			   CalcKcybbVO kcVO = new CalcKcybbVO();
			   if(condate==null||condate.length==0){
					  showErrorMessage("请选择日期!");
					  return;
				  }
			   ConditionVO[] pk_store  = getQryDlg().getConditionVOsByFieldCode("pk_stordoc");	
			   StringBuilder sql = new StringBuilder("('");
			   if (pk_store!=null)
			   {
				   for (int i=0;i<pk_store.length;i++)
				   {
					   String tmp =pk_store[i].getValue();
					   sql.append(tmp+"','");
					   System.out.println(tmp);
				   }
				   
			   }
			   sql.append("')");
			   if ("('')".equals(sql.toString()))
			   {
				   kcVO.setPk_store(null);
			   }
			   else
			   {
				   kcVO.setPk_store(sql.toString());
			   }			  
			   UFDate selectdate = new UFDate(condate[0].getValue());	
			   kcVO.setPk_corp(ce.getCorporation().getPk_corp());
			   kcVO.setCalcdate(selectdate);
			   getReportBase().setHeadItem("date", selectdate);
			   getReportBase().setHeadItem("penson", cuername);
			   getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示			   
			   
			   PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			   nc.vo.eh.report.h0900401.ReportVO[] vos=pubitf.getcpData(kcVO);
			   
			   if(vos!=null && vos.length>0){
				 //add by houcq begin 2011-06-07  设置数值型精度为5
		   	  	    ReportItem[] items=getReportBase().getBody_Items();
		   	  	    for (int j=0;j<items.length;j++)
		   	  	    {
		   	  	         items[j].setDecimalDigits(5);
		   	  	    }				   
		   	  		//add by houcq end
		   	  	    getReportBase().getBody_Item("qmjcd").setDecimalDigits(0);
		   	  		this.getReportBase().setBodyDataVO(vos);
			   
//				 显示合计项
	   	  		String[] strsubValKeys =new  String[] {"qcjc","drrk","ljrk","drhbrk","ljhbrk","drth","ljth","drck","ljck","drhj","ljhj","drhbck","ljhbck","qmjc","qmjcd","ztl"};
		        String[] strgrpValKeys = {"dinvclassname","zinvclassname","xinvclassname"};		        
		        SubtotalContext stctx = new SubtotalContext();
		        
		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//配置要进行合计的字段
		        stctx.setIsSubtotal(true);	//需要小计
		        stctx.setSubtotalName("小计");
		        stctx.setLevelCompute(false);
		        stctx.setTotalNameColKeys("dinvclassname");	//设置合计项显示列位置
		        stctx.setSumtotalName("合计");	//设置合计项显示名称
		        this.getReportBase().setSubtotalContext(stctx);
		        this.getReportBase().subtotal();
		   	  	this.getReportBase().execHeadLoadFormulas();
	        	this.getReportBase().execTailLoadFormulas();
			   } else{
                     this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
                 }
		   }
	  }
}
