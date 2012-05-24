/*
 * �������� 2006-7-1
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.report.h0901010;

import java.awt.BorderLayout;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.report.h0901010.CalcMoneyVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * �ֽ���������
 * wb 2008-8-7 19:28:46
 */
public class CalcMoneyReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	public CalcMoneyReportUI() {
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0901010", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0901010", null, null);
//	    UFDate date = ce.getDate();
//	    //������ʼ����
//	    dlg.setDefaultValue("date",date.toString(),"");
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	//���ô�ӡ����
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
	  //��ѯ����
	  @SuppressWarnings({ "unchecked", "unchecked" })
	public void onQuery() throws Exception{
	  		this.getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		   if(getQryDlg().getResult() == 1){
		   	  ConditionVO[] conbegindate  = getQryDlg().getConditionVOsByFieldCode("begindate");	
			  if(conbegindate==null||conbegindate.length==0){
				  showErrorMessage("��ѡ��ʼ����!");
				  return;
			  }
			 UFDate begindate = new UFDate(conbegindate[0].getValue());
			 ConditionVO[] conenddate  = getQryDlg().getConditionVOsByFieldCode("enddate");	
			  if(conenddate==null||conenddate.length==0){
				  showErrorMessage("��ѡ���������!");
				  return;
			  }
			 UFDate enddate = new UFDate(conenddate[0].getValue());
			 CalcKcybbVO kcVO = new CalcKcybbVO();
			 kcVO.setPk_corp(ce.getCorporation().getPk_corp());
			 kcVO.setCalcdate(begindate);
			 kcVO.setPk_period(enddate.toString());
			 
			 PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
			 CalcMoneyVO[] vos= pubItf.CalcMoney(kcVO);
		   	  if (vos!=null&&vos.length>0){
		   		this.getReportBase().setBodyDataVO(vos);
		   	  	ArrayList arrFileds = new ArrayList();
	   	  		String[] fileds = getReportBase().getBodyFields();
	   	  		for(int i=0;i<fileds.length;i++){
	   	  			String filed = fileds[i];
	   	  			if(filed.endsWith("je")){
	   	  			   arrFileds.add(filed);
	   	  			}
	   	  		}
	   	  		//��ʾ�ϼ���
	   	  		String[] strsubValKeys = (String[])arrFileds.toArray(new String[arrFileds.size()]);
		        String[] strgrpValKeys = {"date"};
		        
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//����Ҫ���кϼƵ��ֶ�
		        stctx.setIsSubtotal(true);	//��ҪС��
//		        stctx.setLevelCompute(true);
		        stctx.setSubtotalName("С��");
		        stctx.setTotalNameColKeys("date");	//���úϼ�����ʾ��λ��
		        stctx.setSumtotalName("�ϼ�");	//���úϼ�����ʾ����
		        this.getReportBase().setSubtotalContext(stctx);
		        this.getReportBase().subtotal();
		   	  	this.getReportBase().execHeadLoadFormulas();
	        	this.getReportBase().execTailLoadFormulas();
		   	  }
		   	  else
		   	  		this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
		   }
	  }
}
