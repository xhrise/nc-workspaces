package nc.ui.eh.report.h0901005;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0901005.JtfkVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
功能：集团应收账款月报
作者：zqy
日期：2008-12-26 上午09:31:35
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0901005", null, null);
	        }catch(Exception ex){
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
	    }catch(BusinessException ex){
	        showErrorMessage(ex.getMessage());
	        ex.printStackTrace();
	    }catch(Exception e){
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0901005", null, null);
	    
	    //结束开始日期
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
                ConditionVO[] nyear  = getQryDlg().getConditionVOsByFieldCode("nyear");                   //年度
                ConditionVO[] nmonth  = getQryDlg().getConditionVOsByFieldCode("nmonth");                 //月度
                Integer year = null;                //年度
                Integer month = null;               //月度
                if(nyear!=null && nyear.length>0){
                    year = new Integer(nyear[0].getValue()==null?"":nyear[0].getValue().toString());
                }
                if(nmonth!=null && nmonth.length>0){
                    month = new Integer(nmonth[0].getValue()==null?"":nmonth[0].getValue().toString());
                }
                String pk_corp = ce.getCorporation().getPk_corp();
                String lastenddate = null;              //查询的结束日期
                @SuppressWarnings("unused")
                String laststartdate = null;            //查询的开始日期
                if(month==1){
                    Integer lastyear = year-1;
                    laststartdate = ""+lastyear+"-12-16";
                    lastenddate = ""+year+"-01-15";
                }else{
                    if(month-10>0){
                        Integer lastnmonth = month-1;
                        laststartdate = ""+year+"-"+lastnmonth+"-16";
                        lastenddate = ""+year+"-"+month+"-15";
                    }
                    if(month==10){
                        laststartdate = ""+year+"-09-16";
                        lastenddate = ""+year+"-10-15";
                    }
                    if(month-10<0){
                        Integer lastmonth = month-1;
                        laststartdate = ""+year+"-0"+lastmonth+"-16";
                        lastenddate = ""+year+"-0"+month+"-15";
                    }
                }
                String today = ce.getDate().toString();
                String dlrq = today.substring(8, 10);
                if(!dlrq.equals("15")){
                    this.showErrorMessage("应收账款月报表只有每个月15号才可以查询!请核实日期!");
                    return;
                }
                
                IUAPQueryBS iUAPQueryBS1 =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                String SQL = " select unitname from bd_corp where pk_corp='"+pk_corp+"' and NVL(dr,0)=0  ";
                Vector arr = (Vector)iUAPQueryBS1.executeQuery(SQL, new VectorProcessor());
                String unitname = null;
                if(arr!=null && arr.size()>0){
                    Vector ve = (Vector)arr.get(0);
                    unitname = ve.get(0)==null?"":ve.get(0).toString();
                }
                String tjdate = ""+year+"年"+month+"月15日";
                
                JtfkVO  jvo = new JtfkVO();
                jvo.setNyear(year);
                jvo.setNmonth(month);
                jvo.setPk_corp(pk_corp);
                
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                String delesql = " delete from eh_yszkmonth where pk_corp='"+pk_corp+"' and dmakedate='"+lastenddate+"' and NVL(dr,0)=0 ";
                pubitf.updateSQL(delesql);
                JtfkVO[] JVO = pubitf.Costjtyszk(jvo);
                IVOPersistence iUAPQueryBS =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
                String custname = null;                 //客户名称
                UFDouble qcye = null;                   //期初余额
                UFDouble byjfmoney = null;              //本月借方发生额
                UFDouble bydfmoney = null;              //本月贷方发生额
                UFDouble qmye = null;                   //期末金额
                ArrayList list = new ArrayList();
                JtfkVO[] jtvo = null;
                if(JVO!=null && JVO.length>0){
                    iUAPQueryBS.insertVOArray(JVO);
                }else{
                    this.showErrorMessage("无满足条件的报表数据!请核实!");
                    return;
                }
                for(int i=0;i<JVO.length;i++){
                    JtfkVO vo = JVO[i];
                    custname = vo.getCustname()==null?"":vo.getCustname().toString();
                    qcye = new UFDouble(vo.getQcye()==null?"0":vo.getQcye().toString());
                    byjfmoney = new UFDouble(vo.getByjfmoney()==null?"0":vo.getByjfmoney().toString());
                    bydfmoney = new UFDouble(vo.getBydfmoney()==null?"0":vo.getBydfmoney().toString());
                    qmye = new UFDouble(vo.getQmye()==null?"0":vo.getQmye().toString());
                    
                    JtfkVO VO = new JtfkVO();
                    VO.setCustname(custname);
                    VO.setQcye(qcye);
                    VO.setBydfmoney(bydfmoney);
                    VO.setByjfmoney(byjfmoney);
                    VO.setQmye(qmye);
                    
                    list.add(VO);
                }
                if(list!=null && list.size()>0){
                    jtvo = (JtfkVO[]) list.toArray(new JtfkVO[list.size()]);
                }
                if(jtvo!=null && jtvo.length>0){
                    this.getReportBase().setHeadItem("corpname", unitname);
                    this.getReportBase().setHeadItem("tjdate", tjdate);
                    this.getReportBase().setHeadItem("dw", "元");
                    this.getReportBase().setBodyDataVO(jtvo);
                    this.getReportBase().setTailItem("zjl", "");
                    this.getReportBase().setTailItem("cwjl", "");
                    this.getReportBase().setTailItem("zdname", ce.getUser().getUserName());
                    this.getReportBase().setTailItem("zddate", ce.getDate().toString());
                    
                    /*显示合计项*/
                    String[] strValKeys = {"qcye","byjfmoney","bydfmoney","qmye"};
                    SubtotalContext stctx = new SubtotalContext();
                    stctx.setSubtotalCols(strValKeys);        //配置要进行合计的字段
                    stctx.setTotalNameColKeys("custname");    //设置合计项显示列位置
                    stctx.setSumtotalName("合计");            //设置合计项显示名称
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
