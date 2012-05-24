package nc.ui.eh.report.h090040603;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.vo.eh.report.h090040603.CompfnewsVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
功能：公司配方信息反馈月报表
作者：zqy
日期：2008-12-24 下午02:57:00
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
	    getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry(){
	    return (new ButtonObject[] {
	        m_boQuery, m_boPrint
	    });
	   
	}
	public ReportBaseClass getReportBase(){
	    if(m_report == null)
	        try{
	            m_report = new ReportBaseClass();
	            m_report.setName("ReportBase");
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H090040603", null, null);
	        }
	        catch(Exception ex){
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H090040603", null, null);
	    
        String nowday = ce.getDate().toString();
        String subday = nowday.substring(0, 7);
        String endday = ""+subday+"-01";
        dlg.setDefaultValue("startdate", endday, "");
        dlg.setDefaultValue("enddate", nowday, "");
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
	  		this.getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
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
                if(startdate==null && enddate==null){
                    this.showErrorMessage("查询开始日期与查询结束日期为必输项!!请核实!!");
                    return;
                }
                String pk_corp = ce.getCorporation().getPk_corp();
                String zdname = ce.getUser().getUserName();
                String zddate = ce.getDate().toString();
                CompfnewsVO cvo = new CompfnewsVO();
                cvo.setStartdate(start_date);
                cvo.setEnddate(end_date);
                cvo.setPk_corp(pk_corp);
                
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                CompfnewsVO[] VO = pubitf.Costpfnews(cvo);
                if(VO!=null && VO.length>0){
                    this.getReportBase().setHeadItem("zdname", zdname);
                    this.getReportBase().setHeadItem("zddate", zddate);
		   	  	    ReportItem[] items=getReportBase().getBody_Items();
		   	  	    for (int j=0;j<items.length;j++)
		   	  	    {
		   	  	         items[j].setDecimalDigits(5);
		   	  	    }
                    getReportBase().setBodyDataVO(VO);
                    
                    /*显示合计项*/
//                    String[] strValKeys = {"monthxl","kcprice","marketprice"};
//                    SubtotalContext stctx = new SubtotalContext();
//                    String[] strgrpValKeys = {"smallfl"};
//                    stctx.setGrpKeys(strgrpValKeys);
//                    stctx.setSubtotalCols(strValKeys);   //配置要进行合计的字段
//                    stctx.setIsSubtotal(true);  //需要小计
//                    stctx.setLevelCompute(true);
//                    stctx.setSubtotalName("小计");
//                    stctx.setTotalNameColKeys("smallfl"); //设置合计项显示列位置
//                    stctx.setSumtotalName("合计");    //设置合计项显示名称
//                    this.getReportBase().setSubtotalContext(stctx);
//                     
//                    this.getReportBase().subtotal();
//                    this.getReportBase().execHeadLoadFormulas();
//                    this.getReportBase().execTailLoadFormulas();
                    updateUI();
                }else{
                    this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
                }   
            }
	  }
      
   
}
