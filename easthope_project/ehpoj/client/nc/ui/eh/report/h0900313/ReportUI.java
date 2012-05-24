package nc.ui.eh.report.h0900313;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0900313.CustzktjVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
���ܣ��ͻ��ۿ�ͳ�Ʊ�
���ߣ�zqy
���ڣ�2009-1-6 ����02:08:55
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900313", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900313", null, null);
        
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
                String pk_corp = ce.getCorporation().getPk_corp();
                ConditionVO[] nyear  = getQryDlg().getConditionVOsByFieldCode("nyear");             //���
                ConditionVO[] nmonth  = getQryDlg().getConditionVOsByFieldCode("nmonth");           //�¶�
                ConditionVO[] pkcubasdoc  = getQryDlg().getConditionVOsByFieldCode("pk_cubasdoc");  //�ͻ�
                Integer month = null;
                Integer year = null;
                String pk_cubasdoc = null;
                if(nyear!=null && nyear.length>0){
                    year = new Integer(nyear[0].getValue()==null?"":nyear[0].getValue().toString());
                }
                if(nmonth!=null && nmonth.length>0){
                    month = new Integer(nmonth[0].getValue()==null?"":nmonth[0].getValue().toString());
                }
                if(pkcubasdoc!=null && pkcubasdoc.length>0){
                    pk_cubasdoc = pkcubasdoc[0].getValue()==null?"":pkcubasdoc[0].getValue().toString();
                }
                
                CustzktjVO cvo = new CustzktjVO();
                cvo.setPk_corp(pk_corp);
                cvo.setNmonth(month);
                cvo.setNyear(year);
                cvo.setPk_cubasdoc(pk_cubasdoc);
                
                PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                CustzktjVO[] VO = pubitf.Costzktj(cvo);
                if(VO!=null && VO.length>0){
                    this.getReportBase().setHeadItem("zdname", ce.getUser().getUserName());
                    this.getReportBase().setHeadItem("zddate", ce.getDate().toString());
                    this.getReportBase().setBodyDataVO(VO);
                    
                    /*��ʾ�ϼ���*/
                    String[] strValKeys = {"xlmount","firstdiscount","monthdiscount","avgdiscount","yeardiscount",
                            "lsdiscount","sumdiscount"};
                    SubtotalContext stctx = new SubtotalContext();
                    String[] strgrpValKeys = {"custname"};
                    stctx.setGrpKeys(strgrpValKeys);
                    stctx.setSubtotalCols(strValKeys);   //����Ҫ���кϼƵ��ֶ�
                    stctx.setIsSubtotal(true);  //��ҪС��
                    stctx.setLevelCompute(true);
                    stctx.setSubtotalName("С��");
                    stctx.setTotalNameColKeys("custname"); //���úϼ�����ʾ��λ��
                    stctx.setSumtotalName("�ϼ�");    //���úϼ�����ʾ����
                    this.getReportBase().setSubtotalContext(stctx);
                     
                    this.getReportBase().subtotal();
                    this.getReportBase().execHeadLoadFormulas();
                    this.getReportBase().execTailLoadFormulas();
                    updateUI();
                }else{
                    this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
                }   
            }
	  }
      
   
}
