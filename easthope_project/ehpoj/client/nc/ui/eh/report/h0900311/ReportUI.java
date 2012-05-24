package nc.ui.eh.report.h0900311;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0900311.CustdiscountVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
功能：客户折扣结算明细表
作者：zqy
日期：2008-12-30 下午02:18:01
 */

public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900311", null, null);
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
	    catch(Exception e)
	    {
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900311", null, null);
        String nowday = ce.getDate().toString();
        String nowyear = nowday.substring(0, 4);
        String nowmonth = nowday.substring(5,7);
        
        dlg.setDefaultValue("nyear", "2009", "");
        dlg.setDefaultValue("nmonth","02","");
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
                ConditionVO[] nyear  = getQryDlg().getConditionVOsByFieldCode("nyear");             //年度
                ConditionVO[] nmonth  = getQryDlg().getConditionVOsByFieldCode("nmonth");           //月度
                ConditionVO[] pkcubasdoc  = getQryDlg().getConditionVOsByFieldCode("pkcubasdoc");   //客户
                String pk_cubasdoc = null;                              //客户PK
                Integer year = null;                                    //年度
                Integer month = null;                                   //月度
                String pk_corp = ce.getCorporation().getPk_corp();      //公司PK 
                if(nyear!=null && nyear.length>0){
                    year = new Integer(nyear[0].getValue()==null?"":nyear[0].getValue().toString());
                }
                if(nmonth!=null && nmonth.length>0){
                    month = new Integer(nmonth[0].getValue()==null?"":nmonth[0].getValue().toString());
                }
                if(pkcubasdoc!=null && pkcubasdoc.length>0){
                    pk_cubasdoc = pkcubasdoc[0].getValue()==null?"":pkcubasdoc[0].getValue().toString();
                }
                
                CustdiscountVO cvo = new CustdiscountVO();
                cvo.setNyear(year);
                cvo.setNmonth(month);
                cvo.setPk_cubasdoc(pk_cubasdoc);
                cvo.setPk_corp(pk_corp);
                
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                CustdiscountVO[] VO = pubitf.Costcustdiscount(cvo);
                if(VO!=null && VO.length>0){
                    this.getReportBase().setHeadItem("zdname", ce.getUser().getUserName());
                    this.getReportBase().setHeadItem("zddate", ce.getDate().toString());
                    this.getReportBase().setBodyDataVO(VO);
                    
                    /*显示合计项*/
                    String[] strValKeys = {"xlmount","yjsseconddiscount","wjsseconddiscount","lszkdiscount","wszkdiscount",
                            "zkhjdiscount","wkhjdiscount"};
                    SubtotalContext stctx = new SubtotalContext();
                    String[] strgrpValKeys = {"custname"};
                    stctx.setGrpKeys(strgrpValKeys);
                    stctx.setSubtotalCols(strValKeys);   //配置要进行合计的字段
                    stctx.setIsSubtotal(true);  //需要小计
                    stctx.setLevelCompute(true);
                    stctx.setSubtotalName("小计");
                    stctx.setTotalNameColKeys("custname"); //设置合计项显示列位置
                    stctx.setSumtotalName("合计");    //设置合计项显示名称
                    this.getReportBase().setSubtotalContext(stctx);
                     
                    this.getReportBase().subtotal();
                    this.getReportBase().execHeadLoadFormulas();
                    this.getReportBase().execTailLoadFormulas();
                    updateUI();
                }else{
                    this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
                }   
            }
	  }
      
   
}
