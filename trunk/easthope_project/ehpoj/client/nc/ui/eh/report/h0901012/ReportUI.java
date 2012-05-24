package nc.ui.eh.report.h0901012;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0901012.TradeSurpluscheckVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;

/*
 * 功能:产品盈亏考核汇总表
 * 作者:zqy
 * 时间:2008年10月14日19:22:17
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0901012", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0901012", null, null);
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
            String pk_corp = this.getCorpPrimaryKey();                  
            QueryConditionClient uidialog = getQryDlg();
            String date = ce.getDate().toString();
            uidialog.setDefaultValue("startdate", date, "");
            getQryDlg().showModal();
            getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
            if(getQryDlg().getResult() == 1){
                IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                ConditionVO[] startdate  = getQryDlg().getConditionVOsByFieldCode("startdate"); //开始日期
                ConditionVO[] pkinvbasdoc  = getQryDlg().getConditionVOsByFieldCode("pk_invbasdoc"); //物料
                UFDate start_date = null;
                String pk_invbasdoc = null;
                //当前日期在盈亏考核中查不出数据的话，就查出距离当前日期最近的盈亏考核数据(之前的日期)
                if(startdate.length>0){
                    StringBuffer sql = new StringBuffer()
                    .append(" select * from eh_trade_surpluscheck ")
                    .append(" where dmakedate='"+startdate[0].getValue()+"' and pk_corp = '"+pk_corp+"' and NVL(dr,0)=0 ");
                    ArrayList all = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                    if(all!=null && all.size()>0){
                        start_date = new UFDate(startdate[0].getValue()==null?"":startdate[0].getValue().toString());
                    }else{
                        StringBuffer SQL = new StringBuffer()
                        .append(" select max(dmakedate) dmakedate from eh_trade_surpluscheck ")
                        .append(" where dmakedate<'"+startdate[0].getValue()+"' and pk_corp = '"+pk_corp+"' and NVL(dr,0)=0 ");
                         Object dateobj = iUAPQueryBS.executeQuery(SQL.toString(),new ColumnProcessor());
                        if(dateobj!=null ){
                            start_date = new UFDate(dateobj.toString());
                        }else{
                            //start_date = new UFDate(startdate[0].getValue()==null?"":startdate[0].getValue().toString());
                        	this.showErrorMessage("截止该查询日期都没有盈亏考核数据，请核实查询日期！");
                        	return;
                        }
                    }
                }                
                if(pkinvbasdoc.length>0){
                    pk_invbasdoc = pkinvbasdoc[0].getValue()==null?"":pkinvbasdoc[0].getValue().toString();
                }
                TradeSurpluscheckVO tvo = new TradeSurpluscheckVO();
                tvo.setDmakedate(start_date);
                tvo.setPk_invbasdoc(pk_invbasdoc);
                tvo.setPk_corp(pk_corp);
                
                PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
                TradeSurpluscheckVO[] vos = pubItf.Allykcost(tvo);
                if(vos!=null && vos.length>0){
                    this.getReportBase().setBodyDataVO(vos);
                    
                    /*显示合计项*/
//                    String[] strValKeys = {"planamount","pfcost","packagcost","gz","df","pf1","refcost1","pf2","managefy","sellfy"
//                            ,"financefy","sumfy","refcost2","price","avgdisocunt","firstdiscount","avgnmonth","avgjd","avgnyear",
//                            "maxdiscount","avgincome","minincome","minamount","taxmoney","avgprofit","requireA","differenceA",
//                            "minprofit","requireB","differenceB","profittaxmoney","porfit","mininprofit"};
//                    SubtotalContext stctx = new SubtotalContext();
//                    stctx.setSubtotalCols(strValKeys);       //配置要进行合计的字段
//                    stctx.setTotalNameColKeys("invtype1");   //设置合计项显示列位置
//                    stctx.setSumtotalName("合计");           //设置合计项显示名称
//                    this.getReportBase().setSubtotalContext(stctx);
//                    this.getReportBase().subtotal();
//                     
//                    this.getReportBase().execHeadLoadFormulas();
//                    this.getReportBase().execTailLoadFormulas();
                    updateUI();
                }else{
                    this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
                }
            }
      }
}
