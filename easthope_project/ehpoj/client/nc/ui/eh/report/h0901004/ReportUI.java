package nc.ui.eh.report.h0901004;

import java.awt.BorderLayout;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0901004.CustbjgxVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;

/**
 * 
���ܣ��ص�ͻ��߼ʹ��׷�����
���ߣ�zqy
���ڣ�2008-12-17 ����04:16:12
 */

public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    
	public ReportUI() {
		super();
		m_boQuery = new ButtonObject("��ѯ", "��ѯ����", 0);
        m_boPrint = new ButtonObject("��ӡ", "��ӡ����", 0);
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0901004", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0901004", null, null);
	    
	    //������ʼ����
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	//���ô�ӡ����
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
      
	  //��ѯ����
	  @SuppressWarnings("unchecked")
    public void onQuery() throws Exception{
	  		this.getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		    if(getQryDlg().getResult() == 1){
                ConditionVO[] nyear  = getQryDlg().getConditionVOsByFieldCode("nyear");             //���
                ConditionVO[] nmonth  = getQryDlg().getConditionVOsByFieldCode("nmonth");           //�¶�
                Integer year = null;
                Integer month = null;
                if(nyear!=null && nyear.length>0){
                    year = new Integer(nyear[0].getValue()==null?"":nyear[0].getValue().toString());
                }
                if(nmonth!=null && nmonth.length>0){
                    month = new Integer(nmonth[0].getValue()==null?"":nmonth[0].getValue().toString());
                }
                if(nyear==null || nmonth==null ){
                    this.showErrorMessage(" ������¶�Ϊ������!!���ʵ!! ");
                    return;
                }
                String pk_corp = ce.getCorporation().getPk_corp();
                CustbjgxVO vo = new CustbjgxVO();
                vo.setYear(year);
                vo.setMonth(month);
                vo.setPk_corp(pk_corp);
                
                IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                String SQL = " select unitname from bd_corp where pk_corp='"+pk_corp+"' and isnull(dr,0)=0  ";
                Vector arr = (Vector)iUAPQueryBS.executeQuery(SQL, new VectorProcessor());
                String unitname = null;
                if(arr!=null && arr.size()>0){
                    Vector ve = (Vector)arr.get(0);
                    unitname = ve.get(0)==null?"":ve.get(0).toString();
                }
                String tjdate = ""+year+"��"+month+"��";
                String dw = "�֡���Ԫ��Ԫ/��";
                
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                CustbjgxVO[] cvo = pubitf.Costbjgx(vo);
                if(cvo!=null && cvo.length>0){
                    this.getReportBase().setHeadItem("corpname", unitname);
                    this.getReportBase().setHeadItem("tjdate", tjdate);
                    this.getReportBase().setHeadItem("dw", dw);
                    this.getReportBase().setBodyDataVO(cvo);
                    this.getReportBase().setTailItem("cwbjl", "");
                    this.getReportBase().setTailItem("zjl", "");
                    this.getReportBase().setTailItem("yxbjl", "");
                    this.getReportBase().setTailItem("zdname", ce.getUser().getUserName());
                    this.getReportBase().setTailItem("zddate", ce.getDate().toString());
                    
                    
//                    /*��ʾ�ϼ���*/
//                    String[] strValKeys = {"znsmount","rzmount","qtmount","qnsmount","dqmount","rqmount","ymount","tzscmount"
//                            ,"fzmount","anthormount","sumsj","bl","sxprice","lastdiscount","discount","sjprice","sjxsprice",
//                            "zjf","bjgxe","bjgxze","qjfyze","lse","bjgx","bjgxcj"};
//                    SubtotalContext stctx = new SubtotalContext();
//                    stctx.setSubtotalCols(strValKeys);              //����Ҫ���кϼƵ��ֶ�
//                    stctx.setTotalNameColKeys("custname");          //���úϼ�����ʾ��λ��
//                    stctx.setSumtotalName("�ϼ�");                  //���úϼ�����ʾ����
//                    this.getReportBase().setSubtotalContext(stctx);
//                    this.getReportBase().subtotal();
//                     
//                    this.getReportBase().execHeadLoadFormulas();
//                    this.getReportBase().execTailLoadFormulas();
//                    updateUI();
                }else{
                    this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
                }   
            }
	  }
      
   
}
