package nc.ui.eh.report.h0901003;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0901003.CustyhVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
���ܣ��ͻ��Żݷ��ű�
���ߣ�zqy
���ڣ�2008-12-17 ����10:35:20
 */

public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    private ClientEnvironment ce=ClientEnvironment.getInstance();
    public String nextstartdate;                    //�¸�����Ŀ�ʼ����
    public String nextenddate;                      //�¸�����Ľ�������
    public String pk_corp;
    public Integer vyear = null;
    public Integer vmonth = null;
    
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0901003", null, null);
	      }
	        catch(Exception ex)
	        {
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}
	public String getTitle() {
		return m_report.getReportTitle();
	}

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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0901003", null, null);
	    
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
            pk_corp = ce.getCorporation().getPk_corp();
            ArrayList list = new ArrayList();
            CustyhVO[] VO = null;
            String year = null;
            String month = null;
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		    if(getQryDlg().getResult() == 1){
                ConditionVO[] nyear  = getQryDlg().getConditionVOsByFieldCode("nyear");      //���
                ConditionVO[] nmonth  = getQryDlg().getConditionVOsByFieldCode("nmonth");    //�¶�
                if(nyear.length>0){
                    vyear = new Integer(nyear[0].getValue()==null?"":nyear[0].getValue().toString());
                    year = nyear[0].getValue()==null?"":nyear[0].getValue().toString();
                }
                if(nmonth.length>0){
                    vmonth = new Integer(nmonth[0].getValue()==null?"":nmonth[0].getValue().toString());
                    month = nmonth[0].getValue()==null?"":nmonth[0].getValue().toString();
                }
                
                if(vmonth<10){
                    Integer nextmonth = vmonth+1;
                    nextstartdate = ""+vyear+"-0"+nextmonth+"-01";
                    nextenddate = ""+vyear+"-0"+nextmonth+"-31";
                }
                if(vmonth==10 || vmonth==11 ){
                    Integer nextmonth = vmonth+1;
                    nextstartdate = ""+vyear+"-"+nextmonth+"-01";
                    nextenddate = ""+vyear+"-"+nextmonth+"-31";
                }
                if(vmonth==12){
                    Integer nextyear = vyear+1;
                    nextstartdate = ""+nextyear+"-01-01";
                    nextenddate = ""+nextyear+"-01-31";
                }
                
                String SQL = " select unitname from bd_corp where pk_corp='"+pk_corp+"' and NVL(dr,0)=0  ";
                Vector arr1 = (Vector)iUAPQueryBS.executeQuery(SQL, new VectorProcessor());
                String unitname = null;
                if(arr1!=null && arr1.size()>0){
                    Vector ve = (Vector)arr1.get(0);
                    unitname = ve.get(0)==null?"":ve.get(0).toString();
                }
                
                String tjdate = ""+year+"��"+month+"��";
                
                StringBuffer sjsql = new StringBuffer()
                .append(" select * from eh_perioddiscount_h  ")
                .append(" where vyear='"+year+"' and vmonth='"+month+"' and NVL(dr,0)=0 ");
                ArrayList all = (ArrayList)iUAPQueryBS.executeQuery(sjsql.toString(),new MapListProcessor());
                if(all!=null && all.size()>0){
                    StringBuffer sql = new StringBuffer()
                    //.append(" select c.pk_cubasdoc ,c.custname, ")
                    .append(" select c.pk_cubasdoc ,c.custcode, c.custname, ")////modify by houcq 2011-02-12
                    .append(" sum(NVL(b.bdiscount,0)) sqmoney, ")
                    .append(" sum(NVL(b.mdiscount,0)) bqyh, ")
                    .append(" sum(NVL(b.yydiscount,0)) bqsj, ")
                    .append(" sum(NVL(b.ediscount,0)) bqmoney ")
                    .append(" from eh_perioddiscount_h a ,eh_perioddiscount b ,bd_cumandoc cuman, bd_cubasdoc c ")
                    .append(" where a.pk_perioddiscount_h = b.pk_perioddiscount_h ")
                    //.append(" and b.pk_cubasdoc = c.pk_cubasdoc  ")
                    .append(" and b.pk_cubasdoc = cuman.pk_cumandoc and cuman.pk_cubasdoc = c.pk_cubasdoc ")
                    .append(" and a.vyear='"+year+"' and a.vmonth='"+month+"' ")
                    .append(" AND a.pk_corp =   '"+pk_corp+"' ")
                    .append(" AND cuman.pk_corp =   '"+pk_corp+"' ")
                    .append(" and NVL(a.dr,0)=0 and NVL(b.dr,0)=0 ")
                    .append(" and NVL(c.dr,0)=0 ")
                    //.append(" group by c.pk_cubasdoc ,c.custname ");
                    .append(" group by c.pk_cubasdoc ,c.custcode,c.custname ");//modify by houcq 2011-02-12
                    
                    ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                    String pk_cubasdoc = null;                                  //�ͻ�PK
                    String custcode = null;                                     //�ͻ�����add by houcq 2011-02-12
                    String custname = null;                                     //�ͻ�����
                    UFDouble sqmoney = null;                                    //�������
                    UFDouble bqyh = null;                                       //�����Ż�
                    UFDouble bqsj = null;                                       //����ʵ��
                    UFDouble bqmoney = null;                                    //�������
                    UFDouble lsdiscount = null;                                 //��ʱ�ۿ�
                    HashMap hmlsdiscount = Getlsdiscount();                     //��ѯ�������¸��¶���������пͻ���Ӧ����ʱ�ۿ�
                    if(arr!=null && arr.size()>0){
                        for(int i=0;i<arr.size();i++){
                            HashMap hm = (HashMap)arr.get(i);
                            pk_cubasdoc = hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
                            custname = hm.get("custname")==null?"":hm.get("custname").toString();
                            custcode = hm.get("custcode")==null?"":hm.get("custcode").toString();//add by houcq 2011-02-12
                            sqmoney = new UFDouble(hm.get("sqmoney")==null?"0":hm.get("sqmoney").toString());
                            lsdiscount = new UFDouble(hmlsdiscount.get(pk_cubasdoc)==null?"0":hmlsdiscount.get(pk_cubasdoc).toString(),2);
                            bqyh = new UFDouble(hm.get("bqyh")==null?"0":hm.get("bqyh").toString()).sub(lsdiscount);
                            bqsj = new UFDouble(hm.get("bqsj")==null?"0":hm.get("bqsj").toString());
                            bqmoney = new UFDouble(hm.get("bqmoney")==null?"0":hm.get("bqmoney").toString());
                            
                            CustyhVO vo = new CustyhVO();
                            vo.setCustcode(custcode);//add by houcq 2011-02-12
                            vo.setCustname(custname);
                            vo.setSqmoney(sqmoney);
                            vo.setBqyh(bqyh);
                            vo.setBqsj(bqsj);
                            vo.setBqmoney(bqmoney);
                            
                            list.add(vo);
                        }
                        if(list!=null && list.size()>0){
                            VO = (CustyhVO[]) list.toArray(new CustyhVO[list.size()]);
                        }
                        if(VO!=null && VO.length>0){
                            this.getReportBase().setHeadItem("corpname", unitname);
                            this.getReportBase().setHeadItem("tjdate", tjdate);
                            this.getReportBase().setHeadItem("dw", "Ԫ");
                            this.getReportBase().setBodyDataVO(VO);
                            this.getReportBase().setTailItem("zjl", "");
                            this.getReportBase().setTailItem("cwjl", "");
                            this.getReportBase().setTailItem("zdname", ce.getUser().getUserName());
                            this.getReportBase().setTailItem("zddate", ce.getDate().toString());
                            
                            /*��ʾ�ϼ���*/
                            String[] strValKeys = {"sqmoney","bqyh","bqsj","bqmoney"};
                            SubtotalContext stctx = new SubtotalContext();
                            stctx.setSubtotalCols(strValKeys);          //����Ҫ���кϼƵ��ֶ�
                            stctx.setTotalNameColKeys("custname");      //���úϼ�����ʾ��λ��
                            stctx.setSumtotalName("�ϼ�");              //���úϼ�����ʾ����
                            this.getReportBase().setSubtotalContext(stctx);
                            this.getReportBase().subtotal();
                             
                            this.getReportBase().execHeadLoadFormulas();
                            this.getReportBase().execTailLoadFormulas();
                            updateUI();
                        }
                    } 
                }else{
                    this.showErrorMessage(" ���ȼ���("+year+")��("+month+")�·ݵ��ۿ��ڼ����!! ");
                }
		   }
            
	  }
      
   /**
    * 
    * @author 
    ���ܣ�ȡ��ǰ��ѯ�������¸��¶�������пͻ���Ӧ����ʱ�ۿ�
    ���ߣ�zqy
    ���ڣ�2009-2-25 ����01:42:56
    * @return
    * @throws BusinessException
    */
      @SuppressWarnings("unchecked")
    public HashMap Getlsdiscount() throws BusinessException{
        HashMap hmlsdiscount = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        StringBuffer sql = new StringBuffer()
        .append(" select pk_cubasdoc ,sum(NVL(tempdiscount,0)) lsdiscount ")
        .append(" from eh_ladingbill ")
        .append(" where vbillstatus = 1  ")
        .append(" and dmakedate between '"+nextstartdate+"' and '"+nextenddate+"' ")
        .append(" and pk_corp='"+pk_corp+"' and NVL(dr,0)=0  ")
        .append(" group by pk_cubasdoc ");
        
        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
        if(arr!=null && arr.size()>0){
            String pk_cubasdoc = null;                          //�ͻ�PK
            UFDouble lsdiscount = null;                         //��ʱ�ۿ�
            for(int i=0;i<arr.size();i++){
                HashMap hm = (HashMap)arr.get(i);
                pk_cubasdoc = hm.get("pk_cubasdoc")==null?"":hm.get("pk_cubasdoc").toString();
                lsdiscount = new UFDouble(hm.get("lsdiscount")==null?"0":hm.get("lsdiscount").toString());
                hmlsdiscount.put(pk_cubasdoc,lsdiscount);
            }
        }
        return hmlsdiscount;
      }
      
      
}
