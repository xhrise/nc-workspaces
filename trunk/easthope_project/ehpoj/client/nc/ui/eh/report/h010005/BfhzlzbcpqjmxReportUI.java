/*
 * �������� 2006-7-1
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.report.h010005;

import java.awt.BorderLayout;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.vo.eh.report.h010005.BfhzlzbcpqjmxVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * ����ϣ�����Ų���������ָ���Ʒ�ڼ���ϸ��
 * houcq 2011-09-27 11:23:03
 */
public class BfhzlzbcpqjmxReportUI extends ToftPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public BfhzlzbcpqjmxReportUI() {
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
	            m_report.setTempletID(getCorpPrimaryKey(), "H0906008", null, null);
	      }
	        catch(Exception ex)
	        {
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}
	/* ���� Javadoc��
	 * @see nc.ui.pub.ToftPanel#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO �Զ����ɷ������
		return m_report.getReportTitle();
	}

	/* ���� Javadoc��
	 * @see nc.ui.pub.ToftPanel#onButtonClicked(nc.ui.pub.ButtonObject)
	 */
	@Override
	public void onButtonClicked(ButtonObject bo) {
		// TODO �Զ����ɷ������
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
	    dlg.setTempletID(getCorpPrimaryKey(), "H0906008", null, null);
	    UFDate date = ce.getDate();
	    //������ʼ����
	    String begindate = date.toString().substring(0,8)+"01";
	    String enddate = date.toString();
	    dlg.setDefaultValue("begindate",begindate,"");
	    dlg.setDefaultValue("enddate",enddate,"");
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	//���ô�ӡ����
	  public void onPrint() throws Exception{
	  		getReportBase().previewData(); 
	  }
	  //��ѯ����
	  @SuppressWarnings({ "unchecked" })
	public void onQuery() throws Exception{
	  		getReportBase().getHeadItem("begindate").clearViewData();
	  		getReportBase().getHeadItem("enddate").clearViewData();
	  		getReportBase().showZeroLikeNull(true);
	  		getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
		   if(getQryDlg().getResult() == 1)
		   {
		   	  ConditionVO[] begincondate  = getQryDlg().getConditionVOsByFieldCode("begindate");
		   	  ConditionVO[] endcondate  = getQryDlg().getConditionVOsByFieldCode("enddate");
		   	  UFDate begindate = new UFDate(begincondate[0].getValue());
		   	  UFDate enddate = new UFDate(endcondate[0].getValue());
		   	  PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		   	  BfhzlzbcpqjmxVO[] vos= pubitf.bfhzlzbcpqjmx(begindate,enddate);
			 
		   	 if (vos!=null&&vos.length>0){
		   		getReportBase().getHeadItem("begindate").setValue(begindate);
		   	  	getReportBase().getHeadItem("enddate").setValue(enddate);
		   	  	ReportItem[] items=getReportBase().getBody_Items();
		   	  	for (int j=0;j<items.length;j++)
		   	  	{
		   	  	    items[j].setDecimalDigits(3);
		   	  	}
		   	  	getReportBase().setBodyDataVO(vos);
		   	  	ArrayList arrFileds = new ArrayList();
	   	  		String[] fileds = getReportBase().getBodyFields();
	   	  		for(int i=0;i<fileds.length;i++){
	   	  			String filed = fileds[i];
	   	  			if(filed.endsWith("cl")||filed.endsWith("x")||filed.endsWith("z")){
	   	  			   arrFileds.add(filed);
	   	  			}
	   	  		}
	   	  		//��ʾ�ϼ���
	   	  		String[] strsubValKeys = (String[])arrFileds.toArray(new String[arrFileds.size()]);
		        String[] strgrpValKeys = {"pqname","scqyname","corpname"};
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//����Ҫ���кϼƵ��ֶ�
		        stctx.setIsSubtotal(true);	//��ҪС��
		        stctx.setLevelCompute(true);
		        stctx.setSubtotalName("С��");
		        stctx.setTotalNameColKeys("pqname");	//���úϼ�����ʾ��λ��
		        stctx.setSumtotalName("�ϼ�");	//���úϼ�����ʾ����
		        getReportBase().setSubtotalContext(stctx);
		        getReportBase().subtotal();
		   	  	getReportBase().execHeadLoadFormulas();
	        	getReportBase().execTailLoadFormulas();
		   	  } 
		   	  else
		   	  	showErrorMessage("�����������ı�������!�����²�ѯ!!");
		   }
	  }
	  
}
