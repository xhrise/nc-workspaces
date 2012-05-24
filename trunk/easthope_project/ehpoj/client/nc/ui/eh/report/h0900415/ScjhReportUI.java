/*
 * �������� 2006-7-1
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.report.h0900415;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0900415.ScjhVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * �����ƻ���
 * wb
 * 2009-2-13 11:10:22
 */
public class ScjhReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	public ScjhReportUI() {
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900415", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900415", null, null);
	    UFDate date = ce.getDate();
	    //������ʼ����
	    dlg.setDefaultValue("begindate", date.toString(), "");
	    dlg.setDefaultValue("enddate", date.toString(), "");
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
		    getQryDlg().showModal();
		    getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		   if(getQryDlg().getResult() == 1){
		   	 ConditionVO[] conbegindate  = getQryDlg().getConditionVOsByFieldCode("begindate");
		   	 ConditionVO[] conenddate  = getQryDlg().getConditionVOsByFieldCode("enddate");
		   	 
		   	 UFDate begindate = new UFDate(conbegindate[0].getValue());
		   	 UFDate enddate = new UFDate(conenddate[0].getValue());
		   	 
		   	 String unitcode = ce.getCorporation().getUnitcode();			//��˾����
		   	 String pk_corp = ce.getCorporation().getPk_corp();
		   	 
		   	 if(!isExitPFdata(unitcode)){
		   		 this.showErrorMessage("�޷�����΢�����ݿ�,���ݿ���û����Ϊ pf"+unitcode+" �����ݿ�,����!");
		   		 return;
		   	 }
		   	 
		   	 ScjhVO[] vos = getBvos(begindate.toString(), enddate.toString(), unitcode,pk_corp);
		   	 if (vos!=null&&vos.length>0){
	   	  		this.getReportBase().getHeadItem("begindate").setValue(begindate);
	   	  		this.getReportBase().getHeadItem("enddate").setValue(enddate);
	   	  		this.getReportBase().setBodyDataVO(vos);
	   	  		
	   	  		//��ʾ�ϼ���
	   	  		String[] strsubValKeys = {"jhsc","cdsc","ysc","wsc"};
		        
		        SubtotalContext stctx = new SubtotalContext();
//		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//����Ҫ���кϼƵ��ֶ�
//		        stctx.setIsSubtotal(true);	//��ҪС��
//		        stctx.setLevelCompute(false);
//		        stctx.setSubtotalName("С��");
		        stctx.setTotalNameColKeys("invcode");	//���úϼ�����ʾ��λ��
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
	  
	  /***
	   * ��΢�����ݿ���ȡ�����ƻ�
	   * @param begin
	   * @param end
	   * @param unitcode
	   * @return
	   * @throws Exception
	   */
	  public ScjhVO[] getBvos(String begin,String end,String unitcode,String pk_corp) throws Exception{
		  ScjhVO[] bvos = null;
		  StringBuffer sql = new StringBuffer()
		  .append("  SELECT a.pfbm invcode,a.pfmc invname,b.invspec,b.invtype,b.colour,")
		  .append("  	   SUM(ISNULL(a.jhsc,0)) jhsc,SUM(ISNULL(a.pfsc,0)) pfsc,SUM(ISNULL(a.fjhsc,0)) cdsc,SUM(ISNULL(a.sjsc,0)) ysc,SUM(ISNULL(a.wsc,0)) wsc")
		  .append("  FROM pf"+unitcode+".dbo.jhb a,eh_invbasdoc b")
		  .append("  WHERE a.pfbm = b.invcode")
		  .append("  AND a.ddrq BETWEEN '"+begin+"' AND '"+end+"'")
		  .append("  AND b.pk_corp = '"+pk_corp+"'")
		  .append("  GROUP BY a.pfbm,a.pfmc,b.invspec,b.invtype,b.colour");
		  IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		  ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		  if(arr!=null&&arr.size()>0){
			  String invcode = null;
			  String invname = null;
			  String invspec = null;
			  String invtype = null;
			  String colour = null;
			  UFDouble jhsc = new UFDouble(0);			//�ƻ�����
			  UFDouble pfsc = new UFDouble(0);			//�䷽����
			  UFDouble cdsc = new UFDouble(0);		
			  UFDouble ysc = new UFDouble(0);
			  UFDouble wsc = new UFDouble(0);
			  
			  bvos = new ScjhVO[arr.size()];
			  for(int i=0;i<arr.size();i++){
				  HashMap hmA = (HashMap)arr.get(i);
				  invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();
				  invname = hmA.get("invname")==null?"":hmA.get("invname").toString();
				  invspec = hmA.get("invspec")==null?"":hmA.get("invspec").toString();
				  invtype = hmA.get("invtype")==null?"":hmA.get("invtype").toString();
				  colour = hmA.get("colour")==null?"":hmA.get("colour").toString();
				  jhsc = new UFDouble(hmA.get("jhsc")==null?"0":hmA.get("jhsc").toString());
				  pfsc = new UFDouble(hmA.get("pfsc")==null?"0":hmA.get("pfsc").toString());
				  cdsc = new UFDouble(hmA.get("cdsc")==null?"0":hmA.get("cdsc").toString());
				  ysc = new UFDouble(hmA.get("ysc")==null?"0":hmA.get("ysc").toString());
				  wsc = new UFDouble(hmA.get("wsc")==null?"0":hmA.get("wsc").toString());
				  
				  bvos[i] = new ScjhVO();
				  bvos[i].setInvcode(invcode);
				  bvos[i].setInvname(invname);
				  bvos[i].setInvspec(invspec);
				  bvos[i].setInvtype(invtype);
				  bvos[i].setColour(colour);
				  bvos[i].setJhsc(jhsc);
				  bvos[i].setPfsc(pfsc);
				  bvos[i].setCdsc(cdsc);
				  bvos[i].setYsc(ysc);
				  bvos[i].setWsc(wsc);
			  }
		  }
		  return bvos;
	  }
	  
	  /***
	   * �ж��Ƿ����΢�����ݿ�
	   * wb 2009��2��13��14:11:02
	   * @return
	   */
	  public boolean isExitPFdata(String unitcode) throws Exception{
		   String sql = " SELECT * FROM sys.databases WHERE NAME = 'pf"+unitcode+"' ";
		   IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		   ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		   if(arr==null||arr.size()==0){
			   return false;
		   }
		   return true;
	  }
}
