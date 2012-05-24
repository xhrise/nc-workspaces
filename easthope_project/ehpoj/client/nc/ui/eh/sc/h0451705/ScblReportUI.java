/*
 * �������� 2006-7-1
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.sc.h0451705;

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
import nc.ui.pub.report.ReportItem;
import nc.vo.eh.ipub.Iinvtype;
import nc.vo.eh.ipub.InvtypeSQL;
import nc.vo.eh.sc.h0451705.ScblVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * �������ϵ�
 * wb
 * 2009-2-7 18:16:58
 */
@SuppressWarnings("serial")
public class ScblReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	public ScblReportUI() {
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0451705", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0451705", null, null);
	    UFDate date = ce.getDate();
	    // ������ʼ����
	    dlg.setDefaultValue("begindate",date.toString(),"");
	    dlg.setDefaultValue("enddate",date.toString(),"");
	    dlg.setNormalShow(false);
	    return dlg;
	}
	
	// ���ô�ӡ����
	  public void onPrint() throws Exception{
	  		this.getReportBase().previewData(); 
	  }
	  // ��ѯ����
	public void onQuery() throws Exception{
	  		this.getReportBase().getHeadItem("begindate").clearViewData();
	  		this.getReportBase().getHeadItem("enddate").clearViewData();
	  		this.getReportBase().getTailItem("bz").clearViewData();
	  		this.getReportBase().getTailItem("invtype").clearViewData();
	  		this.getReportBase().getBillModel().clearBodyData();
		    getQryDlg().showModal();
		   if(getQryDlg().getResult() == 1){
		   	  
		   	  ConditionVO[] conbegindate  = getQryDlg().getConditionVOsByFieldCode("begindate");
			  ConditionVO[] conenddate  = getQryDlg().getConditionVOsByFieldCode("enddate");
			  ConditionVO[] conpk_team  = getQryDlg().getConditionVOsByFieldCode("pk_team");
			  ConditionVO[] coninvtype  = getQryDlg().getConditionVOsByFieldCode("invtype");
			  
			  UFDate begindate = conbegindate!=null&&conbegindate.length>0?new UFDate(conbegindate[0].getValue()):null;
		   	  UFDate enddate = conenddate!=null&&conenddate.length>0?new UFDate(conenddate[0].getValue()):null;
			  String pk_team = conpk_team!=null&&conpk_team.length>0?conpk_team[0].getValue():"";
			  String invtype = coninvtype!=null&&coninvtype.length>0?coninvtype[0].getValue():"";
			  this.getReportBase().getTailItem("invtype").setValue(invtype);
			  if(invtype!=null&&invtype.length()>0){
				  if(invtype.equals("����")){
					  invtype = "0";
				  }
				  if(invtype.equals("С��")){
					  invtype = "1";
				  }
				  if(invtype.equals("��֯��")){
					  invtype = "3";
				  }
				  if(invtype.equals("��ǩ")){
					  invtype = "4";
				  }
			  }
			  ScblVO[] vos = getBvos(begindate,enddate,pk_team,invtype);
		   	 if (vos!=null&&vos.length>0){
	   	  		this.getReportBase().getHeadItem("begindate").setValue(begindate);
	   	  		this.getReportBase().getHeadItem("enddate").setValue(enddate);
	   	  		this.getReportBase().getTailItem("bz").setValue(getTeamNames(begindate, enddate, pk_team));
	   	  		//add by houcq begin 2011-09-26  ������ֵ�;���Ϊ3
	   	  	    ReportItem[] items=getReportBase().getBody_Items();
	   	  	    for (int j=0;j<items.length;j++)
	   	  	    {
	   	  	         items[j].setDecimalDigits(5);
	   	  	    }				   
	   	  		//add by houcq end
	   	  		this.getReportBase().setBodyDataVO(vos);
	   	  		// ��ʾ�ϼ���
	   	  		String[] strsubValKeys = new String[]{"kcamount","needamount","cy"};
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
	public String getTeamNames(UFDate begindate,UFDate enddate,String pk_team) throws Exception{
		StringBuffer teamnames = null;
		String pk_corp = ce.getCorporation().getPk_corp();
		StringBuffer sql = new StringBuffer()
		  .append(" select distinct b.teamname")
		  .append(" from eh_sc_pgd a ,eh_bd_team b")
		  .append(" where  a.pk_team = b.pk_team")
		  .append(" and a.pgdate between '"+begindate+"' and '"+enddate+"'")
		  .append(" AND a.pk_team like '%"+pk_team+"%'")
		  .append(" and a.pk_corp = '"+pk_corp+"'")
		  .append(" and isnull(a.dr,0)=0")
		  .append(" and isnull(b.dr,0)=0");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	      if(arr!=null&&arr.size()>0){
	    	  teamnames = new StringBuffer();
	    	  for(int i=0;i<arr.size();i++){
	    		  HashMap hmA = (HashMap)arr.get(i);
	    		  String teamname =  hmA.get("teamname")==null?"":hmA.get("teamname").toString();
	    		  teamnames.append(teamname+" ");
	    	  }
	      }
	      return teamnames.toString();
	}
	
	  @SuppressWarnings("unchecked")
	public ScblVO[] getBvos(UFDate begindate,UFDate enddate,String pk_team,String invtype) throws Exception{
		  ScblVO[] cgvos = null;
		  String pk_corp = ce.getCorporation().getPk_corp();
		  StringBuffer sql = new StringBuffer()
		  .append(" SELECT a.ylpk pk_invbasdoc,b.invcode,b.invname,b.invspec,b.invtype,d.brandname,SUM(ISNULL(a.ylamount,0)) needamount")
		  .append(" FROM ")
		  .append(" (select a.pk_invbasdoc cppk,b.ver,a.pgamount,c.pk_invbasdoc ylpk,a.pgamount*c.zamount ylamount")
		  .append(" from ")
		  .append(" (select b.pk_invbasdoc,b.ver,sum(nvl(b.pgamount,0)) pgamount")
		  .append(" from eh_sc_pgd a ,eh_sc_pgd_b b")
		  .append(" where  a.pk_pgd = b.pk_pgd")
		  .append(" and a.pgdate between '"+begindate+"' and '"+enddate+"'")
		  .append(" AND a.pk_team like '%"+pk_team+"%'")
		  .append(" and a.pk_corp = '"+pk_corp+"'")
		  .append(" and nvl(a.dr,0)=0")
		  .append(" and nvl(b.dr,0)=0")
		  .append(" group by b.pk_invbasdoc,b.ver")
		  .append(" ) a,eh_bom b,eh_bom_b c")
		  .append(" where a.pk_invbasdoc = b.pk_invbasdoc")
		  .append(" and b.pk_bom = c.pk_bom")
		  .append(" and a.ver = b.ver");
		  if(invtype.equals("0")||invtype.equals("1")){
			  sql.append(" and c.invptype = '"+invtype+"'");
		  }
		  sql.append(" and b.pk_corp = '"+pk_corp+"'")
		  .append(" and nvl(b.dr,0)=0")
		  .append(" and nvl(c.dr,0)=0")
		  .append(" ) a  join bd_invmandoc c on a.ylpk = c.pk_invmandoc JOIN bd_invbasdoc b on b.pk_invbasdoc = c.pk_invbasdoc")
		  .append(" LEFT JOIN eh_brand d ON b.invpinpai = d.pk_brand")
		  .append(" where c.pk_corp ='"+pk_corp+"'");
		  if(invtype.equals("3")){				//��װ
			  sql.append(" and "+InvtypeSQL.getInvBySecendCatalog(Iinvtype.bz01, "b"));
		  }
		  if(invtype.equals("4")){				//��ǩ
			  sql.append(" and "+InvtypeSQL.getInvBySecendCatalog(Iinvtype.bq, "b"));
		  }
		  sql.append(" GROUP BY a.ylpk,b.invcode,b.invname,b.invtype,b.invspec,d.brandname");
		  IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                    
	      ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	      if(arr!=null&&arr.size()>0){
	    	  String pk_invbasdoc = null;
	    	  String invcode = null;
	    	  String invname = null;
	    	  String invspec = null;
	    	  String invtypev = null;
	    	  String brand = null;
	    	  String colour = null;
	    	  UFDouble kcamount = new UFDouble(0);			//���п��
	    	  UFDouble needamount = new UFDouble(0);		//����
	    	  UFDouble cy = new UFDouble(0);				//����
	    	  HashMap hmkc = new PubTools().getDateinvKC(null, null,enddate, "0", pk_corp);		//���п��
	    	  ArrayList arrVO = new ArrayList();
	    	  for(int i=0;i<arr.size();i++){
	    		  HashMap hmA = (HashMap)arr.get(i);
	    		  ScblVO cgVO = new ScblVO();
	    		  pk_invbasdoc =  hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
	    		  invcode =  hmA.get("invcode")==null?"":hmA.get("invcode").toString();
	    		  invname =  hmA.get("invname")==null?"":hmA.get("invname").toString();
	    		  invspec =  hmA.get("invspec")==null?"":hmA.get("invspec").toString();
	    		  invtypev =  hmA.get("invtype")==null?"":hmA.get("invtype").toString();
	    		  brand =  hmA.get("brand")==null?"":hmA.get("brand").toString();
	    		  colour =  hmA.get("colour")==null?"":hmA.get("colour").toString();
	    		  kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"0":hmkc.get(pk_invbasdoc).toString());
	    		  needamount = new UFDouble(hmA.get("needamount")==null?"0":hmA.get("needamount").toString());
	    		  cy = kcamount.sub(needamount);
	    		  
	    		  cgVO.setPk_invbasdoc(pk_invbasdoc);
	    		  cgVO.setInvcode(invcode);
	    		  cgVO.setInvname(invname);
	    		  cgVO.setInvspec(invspec);
	    		  cgVO.setInvtype(invtypev);
	    		  cgVO.setBrand(brand);
	    		  cgVO.setColour(colour);
	    		  cgVO.setKcamount(kcamount);
	    		  cgVO.setNeedamount(needamount);
	    		  cgVO.setCy(cy);
	    		  arrVO.add(cgVO);
	    	  }
	    	  cgvos = (ScblVO[])arrVO.toArray(new ScblVO[arrVO.size()]);
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
}
