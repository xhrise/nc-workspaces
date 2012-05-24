package nc.ui.eh.report.h0901011;

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
import nc.vo.eh.ipub.Iinvtype;
import nc.vo.eh.ipub.InvtypeSQL;
import nc.vo.eh.report.h0901011.TradeCheckcustVO;
import nc.vo.eh.trade.z00120.InvbasdocVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

/*
 * ����:��Ʒӯ��������ϸ�����ͻ���
 * ����:zqy
 * ʱ��:2008��10��14��19:22:17
 */

public class ReportUI extends ToftPanel {
	
	public ButtonObject m_boQuery;
    public ButtonObject m_boPrint;
    private ReportBaseClass m_report;
    private QueryConditionClient m_qryDlg;
//    private UFDate ptoday = null;
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
//	    ptoday = this.getClientEnvironment().getDate();
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
	            m_report.setTempletID(this.getCorpPrimaryKey(), "H0901011", null, null);
	        }
	        catch(Exception ex){
	            System.out.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	        }
	    return m_report;
	}

	public String getTitle() {
		return m_report.getReportTitle();
	}

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
	    dlg.setTempletID(this.getCorpPrimaryKey(), "H0901011", null, null);
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
            String pk_corp = this.getCorpPrimaryKey();                  
            QueryConditionClient uidialog = getQryDlg();
            String date = ce.getDate().toString();
            uidialog.setDefaultValue("dmakedate", date, "");
            getQryDlg().showModal();
            getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
            ArrayList list = new ArrayList();
            TradeCheckcustVO[] tvo = null;   
            if(getQryDlg().getResult() == 1){
                IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
                ConditionVO[] makedate  = getQryDlg().getConditionVOsByFieldCode("dmakedate");        //��ѯӯ��������������
                ConditionVO[] pkcubasdoc  = getQryDlg().getConditionVOsByFieldCode("pk_cubasdoc");    //�ͻ�
                ConditionVO[] Pkinvbasdoc  = getQryDlg().getConditionVOsByFieldCode("pk_invbasdoc");   //����
                
                StringBuffer sql = new StringBuffer();
//                sql.append(" select pk_invbasdoc, invtype1 , invtype2 ,invtype3 ,pp ,invname ,glair ,zbno ,custname , ");
//                sql.append(" planamount , pfcost ,packagcost ,gz ,df, pf1 ,refcost1 , pf2 ,managefy , ");
//                sql.append(" sellfy ,financefy ,sumfy ,refcost2 ,price, firstdiscount ,avgnmonth ,avgjd ,avgnyear, ");
//                sql.append(" (firstdiscount+avgnmonth+avgjd+avgnyear) yh ,(price-(avgnmonth+avgjd+avgnyear)) jsr , ");
//                sql.append(" ((price-(avgnmonth+avgjd+avgnyear))-(avgnmonth+avgjd+avgnyear)) dwlse , ");
//                sql.append(" ((price-(avgnmonth+avgjd+avgnyear))-refcost1) bjgx ,Taxmoney ,avgprofit ");
//                sql.append(" from  eh_trade_surpluscheck where pk_corp='"+pk_corp+"' and NVL(dr,0)=0 ");
                sql.append(" select a.pk_invbasdoc, a.invtype1 , a.invtype2 ,a.invtype3 ,a.pp ,a.invname ,c.invcode ,a.glair ,a.zbno ,a.custname , ");
                sql.append(" a.planamount , a.pfcost ,a.packagcost ,a.gz ,a.df, a.pf1 ,a.refcost1 , a.pf2 ,a.managefy , ");
                sql.append(" a.sellfy ,a.financefy ,a.sumfy ,a.refcost2 ,a.price, a.firstdiscount ,a.avgnmonth ,a.avgjd ,aa.vgnyear, ");
                sql.append(" (a.firstdiscount+a.avgnmonth+a.avgjd+a.avgnyear) yh ,(a.price-(a.avgnmonth+a.avgjd+a.avgnyear)) jsr , ");
                sql.append(" ((a.price-(a.avgnmonth+a.avgjd+a.avgnyear))-(a.avgnmonth+a.avgjd+a.avgnyear)) dwlse , ");
                sql.append(" ((a.price-(a.avgnmonth+a.avgjd+avgnyear))-a.refcost1) bjgx ,a.Taxmoney ,a.avgprofit ");
                sql.append(" from  eh_trade_surpluscheck a,bd_invmandoc b,bd_invbasdoc c where b.pk_invbasdoc=c.pk_invbasdoc and a.pk_invbasdoc=b.pk_invmandoc and a.pk_corp='"+pk_corp+"' and NVL(a.dr,0)=0 ");
                //��ǰ������ӯ�������в鲻�����ݵĻ����Ͳ�����뵱ǰ���������ӯ����������(ȡ֮ǰ������)
                if(makedate.length>0){
                    String datesql = " select * from eh_trade_surpluscheck where dmakedate='"+makedate[0].getValue()+"' and NVL(dr,0)=0 and pk_corp="+pk_corp+" ";
                    ArrayList all = (ArrayList)iUAPQueryBS.executeQuery(datesql.toString(),new MapListProcessor());
                    if(all!=null && all.size()>0){
                    	//sql.append(" and dmakedate = '"+makedate[0].getValue()+"' ");
                        sql.append(" and dmakedate = '"+makedate[0].getValue()+"' ");//modify by houcq 2011-01-31
                    }else{
                        String SQL = " select max(dmakedate) dmakedate from eh_trade_surpluscheck where dmakedate<'"+makedate[0].getValue()+"' and NVL(dr,0)=0 and pk_corp="+pk_corp+" ";
                        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(SQL.toString(),new MapListProcessor());
                        
                        if(arr!=null && arr.size()>0){
                        	for(int i=0;i<arr.size();i++){
                        		HashMap ve = (HashMap)arr.get(0);
                        		if(!(ve.get("dmakedate")==null)){
                        			//sql.append(" and dmakedate = '"+new UFDate(ve.get("dmakedate")==null?"":ve.get("dmakedate").toString())+"' ");//modify by houcq 2011-01-31
                        			sql.append(" and a.dmakedate = '"+new UFDate(ve.get("dmakedate")==null?"":ve.get("dmakedate").toString())+"' ");
                        		}else{
                                    //sql.append(" and dmakedate = '"+makedate[0].getValue()+"' ");//modify by houcq 2011-01-31
                                    sql.append(" and a.dmakedate = '"+makedate[0].getValue()+"' ");
                                }
                                
                        	}
                        }
                    }
                }
                if(pkcubasdoc.length>0){
                    //sql.append(" and pk_cubasdoc = '"+pkcubasdoc[0].getValue()+"' ");
                    sql.append(" and a.pk_cubasdoc = '"+pkcubasdoc[0].getValue()+"' ");//modify by houcq 2011-01-31
                }
                if(Pkinvbasdoc.length>0){
                    //sql.append(" and pk_invbasdoc = '"+Pkinvbasdoc[0].getValue()+"' ");
                    sql.append(" and a.pk_invbasdoc = '"+Pkinvbasdoc[0].getValue()+"' ");//modify by houcq 2011-01-31
                }
                
                String invtype1 = null;          //һ������
                String invtype2 = null;          //��������
                String invtype3 = null;          //��������
                String invname = null;           //��������
                String invcode = null;           //���ϱ���  add by houcq 2011-01-31
                String pp = null;                //Ʒ��
                String custname = null;          //�ͻ�����
                String glair = null;             //���׺���
                String zbno = null;              //�ܲ��䷽��
                UFDouble planamount = null;      //���¼ƻ�����
                UFDouble pfcost = null;          //�䷽�ɱ�
                UFDouble packagcost = null;      //��װ��ɱ�
                UFDouble gz = null;              //�������˹��ʼ�����
                UFDouble df = null;              //���
                UFDouble pf1 = null;             //�������1
                UFDouble refcost1 = null;        //���۲��ճɱ�1
                UFDouble pf2 = null;             //��������۾ɷ�2
                UFDouble managefy = null;        //�������
                UFDouble sellfy = null;          //���۷���
                UFDouble financefy =  null;      //�������
                UFDouble sumfy = null;           //����С��
                UFDouble refcost2 = null;        //���۲��ճɱ�2
                UFDouble price = null;           //�Ƽ�
                UFDouble firstdiscount = null;   //һ���ۿ�
                UFDouble avgnmonth = null;       //����
                UFDouble avgjd =null;            //����
                UFDouble avgnyear=null;          //����
                UFDouble avgdiscount = null;     //ƽ���Ż�
                UFDouble avgincome = null;       //��λ��Ʒ������
                UFDouble taxmonty = null;        //��˰��
                UFDouble avgprofit = null;       //ƽ���߼ʹ��׶�
                UFDouble profittaxmoney = null;  //��λ��Ʒ��˰��
                UFDouble porfit = null;          //�߼ʹ��׶�
                String invtype = null;           //���
                String invspec = null;           //�ͺ�
                String colour = null;            //��ɫ
                String pk_invbasdoc = null;
                HashMap hmsj = Getsj();
                
                UFDouble sumplanmount = new UFDouble(0);                //�ϼƼƻ�����
                UFDouble sumpfcost = new UFDouble(0);                   //�ϼ��䷽�ɱ�
                UFDouble sumpackcost = new UFDouble(0);                 //�ϼư�װ��ɱ�
                UFDouble sumgz = new UFDouble(0);                       //�ϼ��������˹��ʼ�����
                UFDouble sumdf = new UFDouble(0);                       //�ϼƵ��
                UFDouble summake = new UFDouble(0);                     //�ϼ��������1
                UFDouble summake2 = new UFDouble(0);                    //�ϼ��������2
                UFDouble summanage = new UFDouble(0);                   //�ϼƹ������
                UFDouble sumxs = new UFDouble(0);                       //�ϼ����۷���
                UFDouble sumcw = new UFDouble(0);                       //�ϼƲ������
                UFDouble sumprice = new UFDouble(0);                    //�ϼ��Ƽ�
                UFDouble sumfirstdiscount = new UFDouble(0);            //�ϼ�һ���ۿ�
                UFDouble sumavgnmonth = new UFDouble(0);                //�ϼ�����
                UFDouble sumavgjd = new UFDouble(0);                    //�ϼƼ���
                UFDouble sumavgnyear = new UFDouble(0);                 //�ϼ�����
                UFDouble sumxj = new UFDouble(0);                       //�ϼ�С��
                UFDouble sumrefcost1 = new UFDouble(0);                 //�ϼƶ��۲��ճɱ�1
                UFDouble sumrefcost2 = new UFDouble(0);                 //�ϼƶ��۲��ճɱ�2
                UFDouble sumavgdiscount = new UFDouble(0);              //�ϼƲ�Ʒ�Ż�
                UFDouble sumavgincome = new UFDouble(0);                //�ϼƲ�Ʒ������
                UFDouble sumprofittaxmoney = new UFDouble(0);           //�ϼƲ�Ʒ��˰��
                UFDouble sumavgprofit = new UFDouble(0);                //�ϼƲ�Ʒ�߼ʹ��׶�
                UFDouble sumtaxmonty = new UFDouble(0);                 //�ϼƲ�Ʒӯ����˰��
                UFDouble sumporfit = new UFDouble(0);                   //�ϼƲ�Ʒӯ���߼ʹ��׶�
                
                ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());             
                if(all!=null && all.size()>0){
                    for(int i=0;i<all.size();i++){
                        HashMap hm = (HashMap)all.get(i);
                        pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                        invtype1 = hm.get("invtype1")==null?"":hm.get("invtype1").toString();
                        invtype2 = hm.get("invtype2")==null?"":hm.get("invtype2").toString();
                        invtype3 = hm.get("invtype3")==null?"":hm.get("invtype3").toString();
                        pp = hm.get("pp")==null?"":hm.get("pp").toString();
                        InvbasdocVO ivo = (InvbasdocVO)hmsj.get(pk_invbasdoc);
                        if(ivo!=null){
                            invtype = ivo.getInvtype()==null?"":ivo.getInvtype().toString();
                            invspec = ivo.getInvspec()==null?"":ivo.getInvspec().toString();
                            colour = ivo.getColour()==null?"":ivo.getColour().toString();
                        }else{
                            invtype = "";
                            invspec = "";
                            colour = "";
                        }
                        invname = hm.get("invname")==null?"":hm.get("invname").toString();
                        invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();//add by houcq 2011-01-31
                        glair = hm.get("glair")==null?"0":hm.get("glair").toString();
                        zbno = hm.get("zbno")==null?"":hm.get("zbno").toString();
                        custname = hm.get("custname")==null?"":hm.get("custname").toString();
                        planamount = new UFDouble(hm.get("planamount")==null?"0":hm.get("planamount").toString());
                        sumplanmount = sumplanmount.add(planamount);      //�ϼƼƻ�����
                        
                        pfcost = new UFDouble(hm.get("pfcost")==null?"0":hm.get("pfcost").toString(),2);
                        sumpfcost = sumpfcost.add(pfcost.multiply(planamount));  //�ϼ��䷽�ɱ�
                        
                        packagcost = new UFDouble(hm.get("packagcost")==null?"0":hm.get("packagcost").toString(),2);
                        sumpackcost = sumpackcost.add(packagcost.multiply(planamount));  //�ϼư�װ��ɱ�
                        
                        gz = new UFDouble(hm.get("gz")==null?"0":hm.get("gz").toString());
                        sumgz = sumgz.add(gz.multiply(planamount));       //�ϼƹ���
                        
                        df = new UFDouble(hm.get("df")==null?"0":hm.get("df").toString());
                        sumdf = sumdf.add(df.multiply(planamount));       //�ϼƵ��
                        
                        pf1 = new UFDouble(hm.get("pf1")==null?"0":hm.get("pf1").toString());
                        summake = summake.add(pf1.multiply(planamount));  //�ϼ��������1
                        
                        refcost1 = new UFDouble(hm.get("refcost1")==null?"0":hm.get("refcost1").toString());
                        sumrefcost1 = sumrefcost1.add(planamount.multiply(refcost1));    //�ϼƶ��۲��ճɱ�1
                        
                        pf2 = new UFDouble(hm.get("pf2")==null?"0":hm.get("pf2").toString());
                        summake2 = summake2.add(pf2.multiply(planamount)); //�ϼ��������2
                        
                        managefy = new UFDouble(hm.get("managefy")==null?"0":hm.get("managefy").toString());
                        summanage = summanage.add(managefy.multiply(planamount)); //�ϼƹ������
                        
                        sellfy = new UFDouble(hm.get("sellfy")==null?"0":hm.get("sellfy").toString());
                        sumxs = sumxs.add(sellfy.multiply(planamount));    //�ϼ����۷���
                        
                        financefy = new UFDouble(hm.get("financefy")==null?"0":hm.get("financefy").toString());
                        sumcw = sumcw.add(financefy.multiply(planamount)); //�ϼƲ���
                        
                        sumfy = new UFDouble(hm.get("sumfy")==null?"0":hm.get("sumfy").toString());
                        sumxj = sumxj.add(planamount.multiply(sumfy));       //�ϼ�С��
                        
                        refcost2 = new UFDouble(hm.get("refcost2")==null?"0":hm.get("refcost2").toString());
                        sumrefcost2 = sumrefcost2.add(planamount.multiply(refcost2));   //�ϼƶ��۲��ճɱ�2
                        
                        price = new UFDouble(hm.get("price")==null?"0":hm.get("price").toString());
                        sumprice = sumprice.add(price.multiply(planamount));    //�ϼƲ�Ʒ�����Ƽ�
                        
                        firstdiscount = new UFDouble(hm.get("firstdiscount")==null?"0":hm.get("firstdiscount").toString());
                        sumfirstdiscount = sumfirstdiscount.add(firstdiscount.multiply(planamount));  //�ϼ�һ���ۿ�
                        
                        avgnmonth = new UFDouble(hm.get("avgnmonth")==null?"0":hm.get("avgnmonth").toString());
                        sumavgnmonth = sumavgnmonth.add(avgnmonth.multiply(planamount));       //�ϼ�����
                        
                        avgjd = new UFDouble(hm.get("avgjd")==null?"0":hm.get("avgjd").toString());
                        sumavgjd = sumavgjd.add(avgjd.multiply(planamount));       //�ϼƼ���
                        
                        avgnyear = new UFDouble(hm.get("avgnyear")==null?"0":hm.get("avgnyear").toString());
                        sumavgnyear = sumavgnyear.add(avgnyear.multiply(planamount));   //�ϼ�����
                        
                        avgdiscount = new UFDouble(hm.get("yh")==null?"0":hm.get("yh").toString());     //��λ��Ʒ�Ż�
                        sumavgdiscount = sumavgdiscount.add(planamount.multiply(avgdiscount));    //�ϼƲ�Ʒ�Ż�
                        
                        avgincome = price.sub(avgdiscount);       //��λ��Ʒ������
                        sumavgincome = sumavgincome.add(avgincome.multiply(planamount));    //�ϼƲ�Ʒ������
                        
                        profittaxmoney = avgincome.sub(refcost2);  //��λ��Ʒ��˰��
                        sumprofittaxmoney = sumprofittaxmoney.add(profittaxmoney.multiply(planamount)); //�ϼƲ�Ʒ��˰��
                        
                        avgprofit = avgincome.sub(refcost1);       //ƽ���߼ʹ��׶�
                        sumavgprofit = sumavgprofit.add(avgprofit.multiply(planamount));   //�ϼƲ�Ʒ�߼ʹ��׶�
                        
                        taxmonty =  (profittaxmoney.multiply(planamount)).div(10000);  //��˰��
                        sumtaxmonty = sumtaxmonty.add(taxmonty);
                        
                        porfit = (avgprofit.multiply(planamount)).div(10000);    //�߼ʹ��׶�
                        sumporfit = sumporfit.add(porfit);
                        
                        TradeCheckcustVO vo = new TradeCheckcustVO();
                        vo.setInvtype1(invtype1);
                        vo.setInvtype2(invtype2);
                        vo.setAllinvtype(invtype3);
                        vo.setPp(pp);
                        vo.setGg(invtype);
                        vo.setXh(invspec);
                        vo.setYs(colour);
                        vo.setInvname(invname);
                        vo.setInvcode(invcode);//add by houcq 2011-01-31
                        vo.setGlair(glair);
                        vo.setZbno(zbno);
                        vo.setCustname(custname);
                        vo.setPlanamount(planamount);
                        vo.setPfcost(pfcost);
                        vo.setPackagcost(packagcost);
                        vo.setGz(gz);
                        vo.setDf(df);
                        vo.setPf1(pf1);
                        vo.setRefcost1(refcost1);
                        vo.setPf2(pf2);
                        vo.setManagefy(managefy);
                        vo.setSellfy(sellfy);
                        vo.setFinancefy(financefy);
                        vo.setSumfy(sumfy);
                        vo.setRefcost2(refcost2);
                        vo.setPrice(price);
                        vo.setFirstdiscount(firstdiscount);
                        vo.setAvgnmonth(avgnmonth);
                        vo.setAvgjd(avgjd);
                        vo.setAvgnyear(avgnyear);
                        vo.setAvgdisocunt(avgdiscount);
                        vo.setMinamount(avgincome); 
                        vo.setTaxmoney(profittaxmoney);
                        vo.setAvgprofit(avgprofit);
                        vo.setMininprofit(taxmonty);
                        vo.setProfit(porfit);
                        
                        list.add(vo);
                    }
                }
               
                TradeCheckcustVO sumvo = new TradeCheckcustVO();
                sumvo.setInvtype1("�ϼ�");
                sumvo.setPlanamount(sumplanmount);          //�ϼƼƻ�����
                sumvo.setPfcost(sumpfcost);                 //�ϼ��䷽�ɱ�
                sumvo.setPackagcost(sumpackcost);           //�ϼư�װ��ɱ�
                sumvo.setGz(sumgz);                         //�ϼƹ���
                sumvo.setDf(sumdf);                         //�ϼƵ��
                sumvo.setPf1(summake);                      //�ϼ��������1
                sumvo.setRefcost1(sumrefcost1);             //�ϼƶ��۲��ճɱ�1
                sumvo.setPf2(summake2);                     //�ϼ��������2
                sumvo.setManagefy(summanage);               //�ϼƹ������
                sumvo.setSellfy(sumxs);                     //�ϼ����۷���
                sumvo.setFinancefy(sumcw);                  //�ϼƲ������
                sumvo.setSumfy(sumxj);                      //�ϼ�С��
                sumvo.setRefcost2(sumrefcost2);             //�ϼƶ��۲��ճɱ�2
                sumvo.setPrice(sumprice);                   //�ϼ��Ƽ�
                sumvo.setFirstdiscount(sumfirstdiscount);   //�ϼ�һ���ۿ�
                sumvo.setAvgnmonth(sumavgnmonth);           //�ϼ�����
                sumvo.setAvgjd(sumavgjd);                   //�ϼƼ���
                sumvo.setAvgnyear(sumavgnyear);             //�ϼ�����
                sumvo.setAvgdisocunt(sumavgdiscount);       //�ϼ�����Ż�
                sumvo.setMinamount(sumavgincome);           //�ϼƲ�Ʒ������
                sumvo.setTaxmoney(sumprofittaxmoney);       //�ϼƲ�Ʒ��˰��
                sumvo.setAvgprofit(sumavgprofit);           //�ϼƲ�Ʒ�߼ʹ��׶�
                sumvo.setMininprofit(sumtaxmonty);          //�ϼ���˰��
                sumvo.setProfit(sumporfit);                 //�ϼƱ߼ʹ��׶�
                
                list.add(sumvo);
                
                   if(list.size()>0){
                       tvo = (TradeCheckcustVO[]) list.toArray(new TradeCheckcustVO[0]);
                   }               
                   if(tvo!=null && tvo.length>0){
                       this.getReportBase().setBodyDataVO(tvo);
                       /*��ʾ�ϼ���*/
//                       String[] strValKeys = {"planamount","pfcost","packagcost","gz","df","pf1","refcost1","pf2","managefy",
//                               "sellfy","financefy","sumfy","refcost2","price","firstdiscount","avgnmonth","avgjd","avgnyear",
//                               "avgdisocunt","minamount","taxmoney","avgprofit","mininprofit","profit"};
//                       SubtotalContext stctx = new SubtotalContext();
//                       stctx.setSubtotalCols(strValKeys);  //����Ҫ���кϼƵ��ֶ�
//                       stctx.setTotalNameColKeys("invtype1");  //���úϼ�����ʾ��λ��
//                       stctx.setSumtotalName("�ϼ�");    //���úϼ�����ʾ����
//                       this.getReportBase().setSubtotalContext(stctx);
//                       this.getReportBase().subtotal();
//                        
//                       this.getReportBase().execHeadLoadFormulas();
//                       this.getReportBase().execTailLoadFormulas();
                       updateUI();
                   }else{
                       this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
                   }
            }
            
         }
      
      @SuppressWarnings("unchecked")
    public HashMap Getsj() throws BusinessException{
          HashMap hmsj = new HashMap();
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());  
          StringBuffer sql = new StringBuffer()
          //<�޸�>����:���������.ʱ��:2009-08-19.����:��־Զ
          //.append(" select pk_invbasdoc ,invtype ,invspec ,colour from eh_invbasdoc where NVL(dr,0)=0  ")
          //.append(" and NVL(lock_flag,'N')='N' ");
          .append(" SELECT pk_invmandoc pk_invbasdoc, invbas.invtype, invbas.invspec, invbas.def1 colour FROM bd_invbasdoc invbas,bd_invmandoc invman ")
          .append(" WHERE invman.pk_invbasdoc = invbas.pk_invbasdoc AND NVL(invman.dr, 0) = 0 AND NVL(invman.sealflag, 'N') = 'N' AND  "+InvtypeSQL.getInvBySecendCatalog(Iinvtype.yl, "invbas")+" and invman.pk_corp='"+this.getCorpPrimaryKey()+"' ");
          ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
          if(arr!=null && arr.size()>0){
              String pk_invbasdoc = null;
              String invtype = null;
              String invspec = null;
              String colour = null;
              for(int i=0;i<arr.size();i++){
                  HashMap hm = (HashMap)arr.get(i);
                  pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                  invtype = hm.get("invtype")==null?"":hm.get("invtype").toString();
                  invspec = hm.get("invspec")==null?"":hm.get("invspec").toString();
                  colour = hm.get("colour")==null?"":hm.get("colour").toString();
                  InvbasdocVO vo = new InvbasdocVO();
                  vo.setInvtype(invtype);
                  vo.setInvspec(invspec);
                  vo.setColour(colour);
                  hmsj.put(pk_invbasdoc,vo);
              }
          }
        return hmsj;
      }

}
