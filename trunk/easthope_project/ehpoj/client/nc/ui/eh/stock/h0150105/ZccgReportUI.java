/*
 * �������� 2006-7-1
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.stock.h0150105;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.stock.h0150105.ZccgVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * �����ɹ��ƻ� wb 2008-12-23 14:56:49
 */
public class ZccgReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	public ZccgReportUI() {
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0150105", null, null);
	      }
	        catch(Exception ex)
	        {
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}
	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.pub.ToftPanel#getTitle()
	 */
	public String getTitle() {
		// TODO �Զ����ɷ������
		return m_report.getReportTitle();
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.pub.ToftPanel#onButtonClicked(nc.ui.pub.ButtonObject)
	 */
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0150105", null, null);
	    UFDate date = ce.getDate();
	    // ������ʼ����
	    dlg.setDefaultValue("dmakedate",date.toString(),"");
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	// ���ô�ӡ����
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
	  // ��ѯ����
	@SuppressWarnings("unchecked")
	public void onQuery() throws Exception{
	  		this.getReportBase().getHeadItem("dmakedate").clearViewData();
	  		this.getReportBase().getHeadItem("coper").clearViewData();
	  		this.getReportBase().getHeadItem("dw").clearViewData();
	  		this.getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
		   if(getQryDlg().getResult() == 1){
		   	  ConditionVO[] condate  = getQryDlg().getConditionVOsByFieldCode("dmakedate");	
			  if(condate==null||condate.length==0){
				  showErrorMessage("��ѡ������!");
				  return;
			  }
			 UFDate selectdate = new UFDate(condate[0].getValue());
			 CalcKcybbVO kcVO = new CalcKcybbVO();
			 kcVO.setPk_corp(ce.getCorporation().getPk_corp());
			 kcVO.setCalcdate(selectdate);
			 String cuername = ce.getUser().getUserName();   // ����Ա
			 ZccgVO[] vos = getBvos(kcVO);
		   	 if (vos!=null&&vos.length>0){
	   	  		this.getReportBase().getHeadItem("dmakedate").setValue(selectdate);
	   	  		this.getReportBase().getHeadItem("coper").setValue(cuername);
	   	  		this.getReportBase().getHeadItem("dw").setValue("��");
	   	  		this.getReportBase().setBodyDataVO(vos);
	   	  		// ��ʾ�ϼ���
	   	  		String[] strsubValKeys = new String[]{"kcamount","qhyamount","needamount","planamount"};
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setSubtotalCols(strsubValKeys);	// ����Ҫ���кϼƵ��ֶ�
		        stctx.setLevelCompute(true);
		        stctx.setTotalNameColKeys("invcode");	// ���úϼ�����ʾ��λ��
		        stctx.setSumtotalName("�ϼ�");			// ���úϼ�����ʾ����
		        this.getReportBase().setSubtotalContext(stctx);
		        this.getReportBase().subtotal();
		   	  	this.getReportBase().execHeadLoadFormulas();
	        	this.getReportBase().execTailLoadFormulas();
		   	  } 
		   	  else
		   	  		this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
		   }
	  }
	  
	  @SuppressWarnings("unchecked")
	public ZccgVO[] getBvos(CalcKcybbVO kcVO) throws Exception{
		  ZccgVO[] cgvos = null;
		  String pk_corp = kcVO.getPk_corp();
		  UFDate calcdate = kcVO.getCalcdate();
		  UFDate beforedate = calcdate.getDateBefore(10);	//ǰʮ������
		  int[] xuns = getXun_flag(calcdate);				//��Ѯ
		  int pyear = xuns[0];
		  int pmonth = xuns[1];
		  int xun_flag = xuns[2];
		  StringBuffer sql = new StringBuffer()
		  .append(" SELECT a.pk_invbasdoc,a.invcode,a.invname,a.invspec,a.invtype,a.brand,a.colour,a.amount needamount,b.ckamount")
		  .append(" FROM")
		  .append(" 	(SELECT a.pk_invbasdoc,a.invcode,a.invname,a.invspec,a.invtype,a.brand,a.colour,SUM(ISNULL(a.zamount,0)) amount")
		  .append(" 	FROM ")
		  .append(" 	(SELECT a.pk_invbasdoc cppk,d.invcode cpcode,d.invname cpname,c.pk_invbasdoc,b.ver,e.invcode,e.invname,e.invspec,e.invtype,e.invpinpai brand,e.def1 colour,a.amount*ISNULL (c.zamount,0) zamount")
		  .append(" 	 FROM ")
		  .append(" 	(SELECT b.pk_invbasdoc,SUM(ISNULL(b.amount,0)) amount")
		  .append(" 	FROM eh_trade_periodplan a ,eh_trade_periodplan_b b")
		  .append(" 	WHERE a.pk_periodplan = b.pk_periodplan")
		  .append(" 	AND SUBSTRING (a.plandate,1,4) = "+pyear+"")
		  .append(" 	AND SUBSTRING(a.plandate,6,2) = "+pmonth+"")
		  .append(" 	AND a.xun_flag = "+xun_flag+" AND a.vbillstatus = 1 ")
		  .append(" 	AND a.pk_corp = '"+pk_corp+"'")
		  .append(" 	AND ISNULL(a.new_flag,'N') = 'Y'")
		  .append(" 	AND ISNULL(a.lock_flag,'N')<>'Y'")
		  .append(" 	AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0")
		  .append(" 	GROUP BY b.pk_invbasdoc")
		  .append(" 	) a,eh_bom b,eh_bom_b c,bd_invbasdoc d,bd_invmandoc dd,bd_invbasdoc e,bd_invmandoc ee")
		  .append(" 	WHERE a.pk_invbasdoc = b.pk_invbasdoc")
		  .append(" 	AND b.pk_bom = c.pk_bom")
		  .append(" 	AND d.pk_invbasdoc=dd.pk_invbasdoc")
		  .append(" 	AND e.pk_invbasdoc=ee.pk_invbasdoc")
		  .append(" 	AND b.pk_bom = c.pk_bom")
		  .append(" 	AND a.pk_invbasdoc = dd.pk_invmandoc")
		  .append(" 	AND c.pk_invbasdoc = ee.pk_invmandoc")
		  .append(" 	AND b.pk_corp = '"+pk_corp+"'")
		  .append(" 	AND ISNULL(b.sc_flag,'N')='Y'")
		  .append(" 	AND ISNULL(b.dr,0)= 0")
		  .append(" 	AND ISNULL(c.dr,0)= 0 ")
		  .append(" 	) a")
		  .append(" 	GROUP BY a.pk_invbasdoc,a.invcode,a.invname,a.invspec,a.invtype,a.brand,a.colour")
		  .append(" ) a LEFT JOIN")
		  .append(" (SELECT b.pk_invbasdoc,SUM(ISNULL (b.blmount,0)) ckamount ")
		  .append(" FROM eh_sc_ckd a,eh_sc_ckd_b b")
		  .append(" WHERE a.pk_ckd = b.pk_ckd")
		  .append(" AND a.dmakedate BETWEEN '"+beforedate+"' AND '"+calcdate+"'")
		  .append(" AND a.vbillstatus = 1")
		  .append(" AND a.pk_corp = '"+pk_corp+"'")
		  .append(" AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0")
		  .append(" GROUP BY b.pk_invbasdoc")
		  .append(" ) b ON a.pk_invbasdoc = b.pk_invbasdoc");
		  IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                    
	      ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	      if(arr!=null&&arr.size()>0){
	    	  String pk_invbasdoc = null;
	    	  String invcode = null;
	    	  String invname = null;
	    	  String invspec = null;
	    	  String invtype = null;
	    	  String brand = null;
	    	  String colour = null;
	    	  UFDouble kcamount = new UFDouble(0);			//���п��
	    	  UFDouble ckamount = new UFDouble(0);			//ǰʮ�����
	    	  UFDouble needamount = new UFDouble(0);		//��ʮ������
	    	  UFDouble planamount = new UFDouble(0);		//����ƻ�
	    	  UFDouble nowkcuseday = new UFDouble(0);							//���п��ʹ������
	          UFDouble bzkcuseday = new UFDouble(0);							//���ʹ��������׼
	    	  //HashMap hmkc = new PubTools().getDateinvKC(null, null, ce.getDate(), "0", pk_corp);		//���п��
	    	  HashMap hmkcbz = getKCStanded(pk_corp);					//���ʹ��������׼
	    	  ArrayList arrVO = new ArrayList();
	    	  for(int i=0;i<arr.size();i++){
	    		  String msg = null;									//��ʾ��Ϣ
	    		  HashMap hmA = (HashMap)arr.get(i);
	    		  ZccgVO cgVO = new ZccgVO();
	    		  pk_invbasdoc =  hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
	    		  invcode =  hmA.get("invcode")==null?"":hmA.get("invcode").toString();
	    		  invname =  hmA.get("invname")==null?"":hmA.get("invname").toString();
	    		  invspec =  hmA.get("invspec")==null?"":hmA.get("invspec").toString();
	    		  invtype =  hmA.get("invtype")==null?"":hmA.get("invtype").toString();
	    		  brand =  hmA.get("brand")==null?"":hmA.get("brand").toString();
	    		  colour =  hmA.get("colour")==null?"":hmA.get("colour").toString();
	    		  //kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"0":hmkc.get(pk_invbasdoc).toString(),2);
	    		  //modify by houcq 2011-06-20�޸�ȡ��淽��
	    		  kcamount = new PubTools().getInvKcAmount(pk_corp,ce.getDate(),pk_invbasdoc);
	    		  ckamount = new UFDouble(hmA.get("ckamount")==null?"0":hmA.get("ckamount").toString(),2);
	    		  needamount = new UFDouble(hmA.get("needamount")==null?"0":hmA.get("needamount").toString(),2);
	    		  planamount = new UFDouble(needamount.sub(kcamount).toDouble(),2);
	    		  
	    		  nowkcuseday = new UFDouble(kcamount.div(needamount.div(10)).toDouble(),2);				//���п��ʹ������ = ���п��/(��ʮ������/10) A
	    		  bzkcuseday = new UFDouble(hmkcbz.get(pk_invbasdoc)==null?"0":hmkcbz.get(pk_invbasdoc).toString(),2);	//���ʹ��������׼
	    		  //��� ���п��ʹ������A�����ʹ��������׼ֵ��ϵͳԤ����ʾ����Ϣ�����ƫ��
	    		  //��� ���п��ʹ������ A�����ʹ��������׼ֵ+5��ϵͳԤ����ʾ����Ϣ����治����
	    		   if(nowkcuseday.sub(bzkcuseday).toDouble()<0){
	    			   msg = "���ƫ��";
	    		   }
	    		   if(nowkcuseday.sub(bzkcuseday.add(5)).toDouble()>0||kcamount.toDouble()<0){
	    			   msg = "��治����";
	    		   }
	    		  
	    		  cgVO.setPk_invbasdoc(pk_invbasdoc);
	    		  cgVO.setInvcode(invcode);
	    		  cgVO.setInvname(invname);
	    		  cgVO.setInvspec(invspec);
	    		  cgVO.setInvtype(invtype);
	    		  cgVO.setBrand(brand);
	    		  cgVO.setColour(colour);
	    		  cgVO.setKcamount(kcamount);
	    		  cgVO.setQhyamount(ckamount);
	    		  cgVO.setPlanamount(planamount);
	    		  cgVO.setNeedamount(needamount);
	    		  cgVO.setNowkcuseday(nowkcuseday);
	    		  cgVO.setBzkcuseday(bzkcuseday);
	    		  cgVO.setMsg(msg);
	    		  
	    		  arrVO.add(cgVO);
	    	  }
	    	  cgvos = (ZccgVO[])arrVO.toArray(new ZccgVO[arrVO.size()]);
	      }
		  return cgvos;
	  }
	
	  
	/***
	 * ���ݵ�ǰ�����ҵ���Ѯ�ı���ֶ� 
	 * �ɹ��ƻ��к�ʮ������
	 */
	public int[] getXun_flag(UFDate nowdate){
		int[] next = new int[3];
		int nextxun_flag = 0;
		int nyear = nowdate.getYear();
		int nmonth = nowdate.getMonth();
		int pyear = nyear,pmonth = nmonth;
		
		int day = nowdate.getDay();
    	if(day>0&&day<=10) nextxun_flag = 1;
    	if(day>10&&day<=20) nextxun_flag = 2;;
    	if(day>20){ 
    	    nextxun_flag = 0;
    	    pmonth = nmonth+1;
    	    if(nmonth==12){
                pmonth=01;
                pyear = nyear + 1;
            }
    	}
    	next[0] = pyear;			//��
    	next[1] = pmonth;			//��
    	next[2] = nextxun_flag;		//Ѯ���
		return next;
	}
	
	/***
	 * �õ�����׼
	 * wb 2009-4-3 15:08:13
	 * @param pk_corp
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap getKCStanded(String pk_corp) throws Exception{
		HashMap hm = new HashMap();
		StringBuffer sql = new StringBuffer()
		 .append(" SELECT b.pk_invbasdoc ,a.kcuseday")
		 .append(" FROM eh_stock_standard a,eh_stock_standard_b b")
		 .append(" WHERE a.pk_standard = b.pk_standard")
		 .append(" AND ISNULL(a.dr,0)=0 AND ISNULL(b.dr,0)=0")
		 .append(" and a.pk_corp = '"+pk_corp+"'");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                    
	    ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	    if(arr!=null&&arr.size()>0){
	    	String pk_invbasdoc = null;
	    	UFDouble kcbz = new UFDouble(0);
	    	for(int i=0;i<arr.size();i++){
	    		HashMap hmA = (HashMap)arr.get(i);
	    		pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
	    		kcbz = new UFDouble(hmA.get("kcuseday")==null?"0":hmA.get("kcuseday").toString());
	    		hm.put(pk_invbasdoc, kcbz);
	    	}
	    }
	    return hm;
	}
}
