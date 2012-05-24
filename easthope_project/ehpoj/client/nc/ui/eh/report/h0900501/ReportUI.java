package nc.ui.eh.report.h0900501;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0900501.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
 * @author 
 ���ܣ���ִͬ�������
 ���ߣ�zqy
 ���ڣ�2009-2-25 ����11:30:22
 */

public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
    @SuppressWarnings("unused")
    private UFDate ptoday = null;
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
	    
	    //�õ���ǰ������
	    ptoday = this.getClientEnvironment().getDate();
	    getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry(){
	    return (new ButtonObject[] {
	            m_boQuery, m_boPrint
	        }
	    );	   
	}
    
	public ReportBaseClass getReportBase(){
	    if(m_report == null)
	        try{
	            m_report = new ReportBaseClass();
	            m_report.setName("ReportBase");
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0900501", null, null);
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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0900501", null, null);
        
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
          getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
            String pk_corp = this.getCorpPrimaryKey();                  
            QueryConditionClient uidialog = getQryDlg();
            String data = ce.getDate().toString();//���������
            String ksdata = data.substring(0, 7);
            String start_data = ""+ksdata+"-01";//ȡ�õ��µĵ�һ��

            //�÷���ȡ��ĳ�µ����� add by zqy 2008��10��13��10:28:08
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MONTH,cal.get(Calendar.MONTH)+1);
            cal.set(Calendar.DAY_OF_MONTH,1);
            cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
            int maxday = cal.get(Calendar.DAY_OF_MONTH);//ȡ�õ��µ��������
            
            String end_date = ""+ksdata+"-"+maxday+"";//ȡ�õ��µ����һ��
            uidialog.setDefaultValue("startdate", start_data, "");
            uidialog.setDefaultValue("enddate", end_date, "");
            getQryDlg().showModal();
            ArrayList list = new ArrayList();
            ReportVO[] rvos = null;   
            if(getQryDlg().getResult() == 1){
                ConditionVO[] date  = getQryDlg().getConditionVOsByFieldCode("startdate"); //��ʼ����
                ConditionVO[] date2  = getQryDlg().getConditionVOsByFieldCode("enddate"); //��������
                UFDate startdate = null;
                UFDate enddate = null;
                if(date.length>0){
                    startdate = new UFDate(date[0].getValue());
                }
                if(date2.length>0){
                    enddate = new UFDate(date2[0].getValue());
                }           
                if(date.length==0 || date2.length==0){
                    this.showErrorMessage("��ѡ��ʼ���ںͽ������ڣ�");
                    return;
                }
                
                StringBuffer sql = new StringBuffer()
                //.append(" SELECT  a.writedate,a.billno,a.contractname,c.custname,e.invname, ")
                .append(" SELECT  a.writedate,a.billno,a.contractname,c.custcode,c.custname,e.invcode,e.invname, ")
                .append(" NVL(d.amount,0) amount,NVL(d.taxinprice,0) taxinprice,NVL(d.amount*d.taxinprice,0) taxmoney, ")
                .append(" b.indate,NVL(f.inamount,0) inamount,NVL(f.inprice,0) price,NVL(f.def_6,0) je,  ")
                .append(" (NVL(d.amount,0)-NVL(f.inamount,0)) wzxsl,(NVL(d.taxinprice,0)-NVL(f.inprice,0)) dj, ")
                .append(" (NVL(d.amount*d.taxinprice,0)-NVL(f.def_6,0)) jee  ")
                .append(" FROM eh_stock_contract a LEFT JOIN eh_stock_in b  ")
                .append(" on a.pk_contract = b.pk_contract ")
                //<�޸�>���ܣ���ӿͻ���������ʱ�䣺2009-08-20.����:��־Զ
                //.append(" JOIN bd_cubasdoc c ON a.pk_cubasdoc = c.pk_cubasdoc ")
                .append(" JOIN bd_cumandoc cuman ON a.pk_cubasdoc = cuman.pk_cumandoc ")
                .append(" JOIN bd_cubasdoc c ON cuman.pk_cubasdoc = c.pk_cubasdoc ")
                .append(" JOIN eh_stock_contract_b d ON a.pk_contract = d.pk_contract ")
                //<�޸�>���ܣ���Ӵ����������ʱ�䣺2009-08-20.����:��־Զ
                //.append(" JOIN eh_invbasdoc e ON d.pk_invbasdoc = e.pk_invbasdoc ")
                .append(" JOIN bd_invmandoc invman ON d.pk_invbasdoc = invman.pk_invmandoc ")
                .append(" JOIN bd_invbasdoc e ON invman.pk_invbasdoc = e.pk_invbasdoc ")
                .append(" LEFT join eh_stock_in_b f ON f.pk_in = b.pk_in ")
                .append(" WHERE  a.dmakedate between '"+startdate+"' and '"+enddate+"' and a.vbillstatus=1 ")
                .append(" and a.pk_corp= '"+pk_corp+"' AND NVL(a.dr,0)=0 ");
          
                IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());     
                ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());             
                UFDate writedate= null;//�ɹ�����
                String billno= null;//�ɹ�����
                String contractname= null;//��ͬ����
                UFDouble amount= null;//�ɹ�����
                UFDouble taxinprice= null;//�ɹ��۸�
                UFDouble taxmoney= null;//�ɹ����
                String custname= null;//��Ӧ��
                String invname= null;//��������
                String custcode= null;//��Ӧ�̱���//add by houcq 2011-02-12
                String invcode= null;//���ϱ���//add by houcq 2011-02-12
                UFDouble dj=null;//����
                UFDouble jee=null;//���
                String indate = null;//�������
                UFDouble inamount = null;//�������
                UFDouble inprice = null;//���۸�
                UFDouble je = null;//�����
                UFDouble subamount = null;
                HashMap hmamount = new HashMap();//��ź�ͬ�����������
                
                 if(all!=null && all.size()>0){
                     for(int i=0;i<all.size();i++){
                        HashMap hm = (HashMap)all.get(i);
                        writedate=new UFDate(hm.get("writedate")==null?"":hm.get("writedate").toString());
                        billno=hm.get("billno")==null?"":hm.get("billno").toString();//��ͬ��
                        contractname=hm.get("contractname")==null?"":hm.get("contractname").toString();
                        amount=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());//�ɹ�����
                        taxinprice=new UFDouble(hm.get("taxinprice")==null?"0":hm.get("taxinprice").toString());
                        taxmoney=new UFDouble(hm.get("taxmoney")==null?"0":hm.get("taxmoney").toString());
                        custcode=hm.get("custcode")==null?"":hm.get("custcode").toString();//add by houcq 2011-02-12
                        invcode=hm.get("invcode")==null?"":hm.get("invcode").toString();//add by houcq 2011-02-12
                        custname=hm.get("custname")==null?"":hm.get("custname").toString();
                        invname=hm.get("invname")==null?"":hm.get("invname").toString();
                        dj=new UFDouble(hm.get("dj")==null?"0":hm.get("dj").toString());
                        jee=new UFDouble(hm.get("jee")==null?"0":hm.get("jee").toString());
                        inamount=new UFDouble(hm.get("inamount")==null?"0":hm.get("inamount").toString());//�������
                        inprice=new UFDouble(hm.get("price")==null?"0":hm.get("price").toString());
                        je=new UFDouble(hm.get("je")==null?"0":hm.get("je").toString());
                        indate=hm.get("indate")==null?"":hm.get("indate").toString();
                           
                        dj=taxinprice.sub(inprice);
                        jee=taxmoney.sub(je);
                        
                        //����ͬ�Ž��з���ۼ�
                        if(hmamount.containsKey(billno+invname)){
                            subamount = subamount.sub(inamount);                           
                         }else{
                            subamount = amount.sub(inamount);
                            hmamount.put(billno+invname, subamount);
                         }
                           
                         ReportVO rvo = new ReportVO();
                         rvo.setWritedate(writedate);
                         rvo.setBillno(billno);
                         rvo.setContractname(contractname);
                         rvo.setCustname(custname);
                         rvo.setInvname(invname);
                         rvo.setAmount(amount);
                         rvo.setTaxinprice(taxinprice);
                         rvo.setTaxmoney(taxmoney);
                         rvo.setIndate(indate);
                         rvo.setRksl(inamount);
                         rvo.setRkjg(inprice);
                         rvo.setJe(je);
                         rvo.setWzxsl(subamount);
                         rvo.setDj(dj);
                         rvo.setJee(jee);
                         rvo.setInvcode(invcode);//add by houcq 2011-02-12
                         rvo.setCustcode(custcode);//add by houcq 2011-02-12
                         list.add(rvo);
                        }                         
                   }      
      
                   if(list.size()>0){
                       rvos = (ReportVO[]) list.toArray(new ReportVO[0]);
                   }               
                   if(rvos!=null && rvos.length>0){
                       this.getReportBase().setBodyDataVO(rvos);
                       
                       /*��ʾ�ϼ���*/
                       String[] strValKeys = {"rksl","jee"}; 
                       SubtotalContext stctx = new SubtotalContext();
                       String[] strgrpValKeys = {"custname"};
                       stctx.setGrpKeys(strgrpValKeys);
                       stctx.setSubtotalCols(strValKeys);          //����Ҫ���кϼƵ��ֶ�
                       stctx.setIsSubtotal(true);                  //��ҪС��
                       stctx.setLevelCompute(true);
                       stctx.setSubtotalName("С��");
                       stctx.setTotalNameColKeys("custname");      //���úϼ�����ʾ��λ��
                       stctx.setSumtotalName("�ϼ�");              //���úϼ�����ʾ����
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
