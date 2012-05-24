package nc.ui.eh.report.h0900504;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.report.h0900504.GystjbbVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
 * @author 
 功能：供应商月统计报表
 作者：zqy
 日期：2009-1-19 上午10:01:53
 */
 
public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900504", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900504", null, null);
        String today = ce.getDate().toString(); 
        String sql = "select pk_period FROM eh_period WHERE nyear = "+today.substring(0, 4)+" and nmonth = "+today.substring(5,7)+" and pk_corp = '"+ce.getCorporation().getPk_corp()+"'";
        try {
        	String pk_period = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
		    dlg.setDefaultValue("pk_period", pk_period, "");
        }catch(Exception e){
        	e.printStackTrace();
        }
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
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		    if(getQryDlg().getResult() == 1){
                ConditionVO[] pk_periodcon  = getQryDlg().getConditionVOsByFieldCode("pk_period");           //期间
                ConditionVO[] pkcubasdoc  = getQryDlg().getConditionVOsByFieldCode("pk_cubasdoc");           //客户PK
                ConditionVO[] pkinvbasdoc  = getQryDlg().getConditionVOsByFieldCode("pk_invbasdoc");         //物料PK
                
                String pk_corp = ce.getCorporation().getPk_corp();
                String pk_period = null;
                String pk_cubasdoc = null;
                String pk_invbasdoc = null;
                if(pk_periodcon!=null && pk_periodcon.length>0){
                	pk_period = pk_periodcon[0].getValue()==null?"":pk_periodcon[0].getValue().toString();
                }
                if(pkcubasdoc!=null && pkcubasdoc.length>0){
                    pk_cubasdoc = pkcubasdoc[0].getValue()==null?"":pkcubasdoc[0].getValue().toString();
                }
                if(pkinvbasdoc!=null && pkinvbasdoc.length>0){
                    pk_invbasdoc = pkinvbasdoc[0].getValue()==null?"":pkinvbasdoc[0].getValue().toString();
                }
                
                PeriodVO perVO = (PeriodVO)iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
                String startdate = perVO.getBegindate().toString();
                String enddate = perVO.getEnddate().toString();
                
                GystjbbVO gvo = new GystjbbVO();
                gvo.setPk_corp(pk_corp);
                gvo.setPrimaryKey(pk_period);
                gvo.setPk_cubasdoc(pk_cubasdoc);
                gvo.setPk_invbasdoc(pk_invbasdoc);
                gvo.setStartdate(startdate);
                gvo.setEnddate(enddate);
                
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                GystjbbVO[] VO = pubitf.Costgystj(gvo);
               
                if(VO!=null && VO.length>0){
                    this.getReportBase().setHeadItem("zdname", ce.getUser().getUserName());
                    this.getReportBase().setHeadItem("zddate", ce.getDate().toString());
                    this.getReportBase().setBodyDataVO(VO);
                    
                    /*显示合计项*/
                    String[] strValKeys = {"ghmount","ghmoney","yearghmount","yearghmoney"}; 
                    SubtotalContext stctx = new SubtotalContext();
                    String[] strgrpValKeys = {"custname"};
                    stctx.setGrpKeys(strgrpValKeys);
                    stctx.setSubtotalCols(strValKeys);          //配置要进行合计的字段
                    stctx.setIsSubtotal(true);                  //需要小计
                    stctx.setLevelCompute(true);
                    stctx.setSubtotalName("小计");
                    stctx.setTotalNameColKeys("custname");      //设置合计项显示列位置
                    stctx.setSumtotalName("合计");              //设置合计项显示名称
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
