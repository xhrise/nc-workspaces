package nc.ui.eh.report.h0900303;

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
 *�����ձ���(����)
 * @author ����
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
	    
//	  �õ���ǰ������
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900303", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900303", null, null);
	    
	    //������ʼ����
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	//���ô�ӡ����
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
	  //��ѯ����
	  public void onQuery() throws Exception{
		  String cuername = ce.getUser().getUserName();   // ����Ա
//	  		this.getReportBase().getHeadItem("today").clearViewData();
	  		this.getReportBase().getBillModel().clearBodyData();
			QueryConditionClient uidialog = getQryDlg();
			uidialog.setDefaultValue("date", ce.getDate().toString(), "");
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		   if(getQryDlg().getResult() == 1){
			   ConditionVO[] condate  = getQryDlg().getConditionVOsByFieldCode("date");	
			   CalcKcybbVO kcVO = new CalcKcybbVO();
			   if(condate==null||condate.length==0){
					  showErrorMessage("��ѡ������!");
					  return;
				  }
			   UFDate selectdate = new UFDate(condate[0].getValue());	
			   kcVO.setPk_corp(ce.getCorporation().getPk_corp());
			   kcVO.setCalcdate(selectdate);
			   getReportBase().setHeadItem("zddate", selectdate);
			   getReportBase().setHeadItem("zdpeoson", ce.getUser().getUserName().toString());
			   
			   PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			   nc.vo.eh.report.h0900303.ReportVO[] vos=pubitf.getxsrbData(kcVO);
			   //add by houcq begin 2010-11-27  ������ֵ�;���Ϊ3
	   	  	    ReportItem[] items=getReportBase().getBody_Items();
	   	  	    for (int j=0;j<items.length;j++)
	   	  	    {
	   	  	         items[j].setDecimalDigits(3);
	   	  	    }				   
	   	  		//add by houcq end
			   this.getReportBase().setBodyDataVO(vos);
//				 ��ʾ�ϼ���
	   	  		String[] strsubValKeys =new  String[] {"drxsl","ljdrxsl","syljdrxsl","hb","qnljdrxsl","tb","byjh"};
		        String[] strgrpValKeys = {"dddinvclassname","ddinvclassname","dinvclassname","zinvclassname"};		        
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//����Ҫ���кϼƵ��ֶ�
		        stctx.setIsSubtotal(true);	//��ҪС��
		        stctx.setLevelCompute(true);
		        stctx.setSubtotalName("С��");
		        stctx.setTotalNameColKeys("dddinvclassname");	//���úϼ�����ʾ��λ��
		        stctx.setSumtotalName("�ϼ�");	//���úϼ�����ʾ����
		        this.getReportBase().setSubtotalContext(stctx);

		        this.getReportBase().subtotal();

		   	  	this.getReportBase().execHeadLoadFormulas();
	        	this.getReportBase().execTailLoadFormulas();
			   
		   }
	  }
}
