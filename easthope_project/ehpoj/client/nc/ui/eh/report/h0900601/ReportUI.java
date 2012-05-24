package nc.ui.eh.report.h0900601;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 原料进耗存日报表
 * 
 */
public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    
    private UFDate ptoday = null;

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
	    
//	  得到当前的日期
	    ptoday = this.getClientEnvironment().getDate();
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900601", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900601", null, null);
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
			String cuername = ce.getUser().getUserName();   // 操作员
	  		this.getReportBase().getBillModel().clearBodyData();
			QueryConditionClient uidialog = getQryDlg();
			uidialog.setDefaultValue("enddate", ce.getDate().toString(), "");
			uidialog.setDefaultValue("tody", ce.getDate().toString().substring(0,8)+"01", "");
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		   if(getQryDlg().getResult() == 1){
			   ConditionVO[] condate  = getQryDlg().getConditionVOsByFieldCode("tody");	
//			   ConditionVO[] yltype  = getQryDlg().getConditionVOsByFieldCode("yltype");	
			   ConditionVO[] enddate  = getQryDlg().getConditionVOsByFieldCode("enddate");	
			   ConditionVO[] pk_invbasdoc  = getQryDlg().getConditionVOsByFieldCode("pk_invbasdoc");
			   ConditionVO[] invclcodes = getQryDlg().getConditionVOsByFieldCode("pk_invcl");
			   ConditionVO[] pkstordoc = getQryDlg().getConditionVOsByFieldCode("pk_stordoc");
			   if(condate==null||condate.length==0){
					  showErrorMessage("请选择开始日期!");
					  return;
				  }
//			   if(invclcodes==null||invclcodes.length==0){
//					  showErrorMessage("请选择物料类型!");
//					  return;
//				  }
			   if(enddate==null||enddate.length==0){
					  showErrorMessage("请选择结束日期!");
					  return;
			   }
			   String pk_stordoc = "%";
			   if(pkstordoc!=null&&pkstordoc.length!=0){
					 pk_stordoc = pkstordoc[0].getValue();
			   }
			   
			   CalcKcybbVO kcVO = new CalcKcybbVO();
			   
			   String selectdate = new String(condate[0].getValue());//开始日期
			   String selectpk_invbasdoc = new String();//物料
			   if(pk_invbasdoc!=null && pk_invbasdoc.length>0){
				   selectpk_invbasdoc=pk_invbasdoc[0].getValue();
			   }
			   
			   UFDate selectenddate = new UFDate(enddate[0].getValue());  //结束日期
			   if(new UFDate(selectdate).getMonth() != selectenddate.getMonth() || new UFDate(selectdate).getYear() != selectenddate.getYear() ){
				   showErrorMessage("请选择同一年月的日期!");
				   return;
			   }
			   kcVO.setPk_corp(ce.getCorporation().getPk_corp());
			   kcVO.setCalcdate(selectenddate);
			   kcVO.setInvtype(selectpk_invbasdoc);
			   kcVO.setPk_period(selectdate);
			   kcVO.setPk_store(pk_stordoc);
//			   showErrorMessage("*****************"+pk_stordoc);
			   /**得到查询模板的物料分类编码**/
			   StringBuffer addSQL = new StringBuffer(" (z.invcode like '' "); 
			   if(invclcodes!=null&&invclcodes.length>0){
				   for(int i=0;i<invclcodes.length;i++){
					   String invcl = invclcodes[i].getValue()==null?null:invclcodes[i].getValue().toString();
					   if(invcl!=null){
						   addSQL.append(" or z.invcode like '"+invcl+"%'");
					   }
				  }
			   }else{
				   addSQL.append(" or z.invcode like '%'");
			   }
			   addSQL.append(")");
			   kcVO.setMemo(addSQL.toString());
			   
//			   if((yltype[0].getValue().toString()).equals("标签")){
//				   
//				   kcVO.setMemo(" = '009' "); 
//			   }
//			   if((yltype[0].getValue().toString()).equals("药品原料")){
//				   kcVO.setMemo(" <> '009'"); 
//			   }
			   

			   PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			   nc.vo.eh.report.h0900601.ReportVO[] vos=pubitf.getylrbbData(kcVO);
//			   vos = (nc.vo.eh.report.h0900601.ReportVO[])new PubTool().modelVOs(vos);
			   if(vos!=null && vos.length>0){
				   
				 //将数值的小数位精确到5位小数。时间：2010-01-05作者：张志远
				   	this.getReportBase().getBody_Item("ycjc").setDecimalDigits(5);
       	  	   		this.getReportBase().getBody_Item("rkbr").setDecimalDigits(5);
       	  			this.getReportBase().getBody_Item("rklj").setDecimalDigits(5);
       	  			this.getReportBase().getBody_Item("scbr").setDecimalDigits(5);
       	  			this.getReportBase().getBody_Item("lisc").setDecimalDigits(5);
       	  			this.getReportBase().getBody_Item("brqt").setDecimalDigits(5);
       	  			this.getReportBase().getBody_Item("ljqt").setDecimalDigits(5);
       	  			this.getReportBase().getBody_Item("zmjcs").setDecimalDigits(5);
       	  			
       	  			this.getReportBase().setBodyDataVO(vos);
                   
                   
    			   String [] fileds=new String []{"jdhwrksl","yjkyts","pcs","yks","jtbzts","cj"};
    			   this.getReportBase().setBodyItemsEditable(fileds, true);
                   
                   /*显示合计项*/
                   String[] strValKeys = {"rkbr","rklj","scbr","lisc","brqt","ljqt","zmjcs","ycjc","jdhwrksl"};
                   SubtotalContext stctx = new SubtotalContext();
                   stctx.setSubtotalCols(strValKeys);  //配置要进行合计的字段
                   stctx.setTotalNameColKeys("invname");  //设置合计项显示列位置
                   stctx.setSumtotalName("合计");    //设置合计项显示名称
                   this.getReportBase().setSubtotalContext(stctx);
                   this.getReportBase().subtotal();
                    
                   this.getReportBase().execHeadLoadFormulas();
                   this.getReportBase().execTailLoadFormulas();
                   updateUI();
               }
               else{
                   this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
               }		   
		   }
	  }
}
