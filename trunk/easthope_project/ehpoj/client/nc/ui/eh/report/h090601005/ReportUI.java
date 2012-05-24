package nc.ui.eh.report.h090601005;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import nc.vo.eh.ipub.LableAndPackageCode;
import nc.vo.eh.report.h090601005.ReportVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass; 
import nc.ui.pub.report.ReportItem;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
 *���ܣ���ǩ���ܱ�
 *���ߣ���־Զ 
 *���ڣ�2009-10-14  ����16:50:53
 */

public class ReportUI extends ToftPanel {

	private static final long serialVersionUID = 1L;
	public ButtonObject m_boQuery;
	public ButtonObject m_boPrint;
	private ReportBaseClass m_report;
	private QueryConditionClient m_qryDlg;
	private ClientEnvironment ce = ClientEnvironment.getInstance();
	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());

	@SuppressWarnings("deprecation")
	public ReportUI() {
		super();
		m_boQuery = new ButtonObject("��ѯ", "��ѯ����", 0);
		m_boPrint = new ButtonObject("��ӡ", "��ӡ����", 0);
		initialize();
	}

	private void initialize() {
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
		for (int i = 0; i < colcount; i++) {
			skey = getReportBase().getBodyShowItems()[i].getKey().trim();
			st[i] = skey;
		}
		getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry() {
		return (new ButtonObject[] { m_boQuery, m_boPrint });
	}

	public ReportBaseClass getReportBase() {
		if (m_report == null)
			try {
				m_report = new ReportBaseClass();
				m_report.setName("ReportBase");
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090601005",
						null, null);
			} catch (Exception ex) {
				System.out
						.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
			}
		return m_report;
	}

	public String getTitle() {
		return m_report.getReportTitle();
	}

	public void onButtonClicked(ButtonObject bo) {
		try {
			if (bo == m_boQuery) {
				onQuery();
			} else if (bo == m_boPrint) {
				onPrint();
			}
		} catch (BusinessException ex) {
			showErrorMessage(ex.getMessage());
			ex.printStackTrace();
		} catch (Exception e) {
			showErrorMessage("\u672A\u77E5\u9519\u8BEF:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public QueryConditionClient getQryDlg() {
		if (m_qryDlg == null) {
			m_qryDlg = createQueryDLG();
		}
		return m_qryDlg;
	}

	protected QueryConditionClient createQueryDLG() {
		QueryConditionClient dlg = new QueryConditionClient();
		dlg.setTempletID(this.getCorpPrimaryKey(), "H090601005", null, null);
		String today = ce.getDate().toString();
		String sql = "select pk_period FROM eh_period WHERE nyear = "
				+ today.substring(0, 4) + " and nmonth = "
				+ today.substring(5, 7) + " and pk_corp = '"
				+ ce.getCorporation().getPk_corp() + "'";
		try {
			String pk_period = iUAPQueryBS.executeQuery(sql,
					new ColumnProcessor()) == null ? null : iUAPQueryBS
					.executeQuery(sql, new ColumnProcessor()).toString();
			dlg.setDefaultValue("pk_period", pk_period, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		dlg.setNormalShow(false);
		return dlg;
	}

	// ���ô�ӡ����
	public void onPrint() throws Exception {
		this.getReportBase().previewData();
	}

	// ��ѯ����
	public void onQuery() throws Exception {
		this.getReportBase().getBillModel().clearBodyData();
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		ArrayList list = new ArrayList();
		ReportVO[] rvos = null;// �Զ���VO

		if (getQryDlg().getResult() == 1) {
			ConditionVO[] quyear = getQryDlg().getConditionVOsByFieldCode("queryyear"); // ��ѯ���
			ConditionVO[] qumonth = getQryDlg().getConditionVOsByFieldCode("querymonth"); // ��ѯ�·�
			//��ѯҳ�����
			int queryy = Integer.parseInt(quyear[0].getValue());
			int querym = Integer.parseInt(qumonth[0].getValue());
			//��ѯ����
			String querydate = null; 
			if(querym <10){
			querydate = queryy+"-0"+querym;
			}else{
			querydate = queryy+"-"+querym;
			}
			
			//��ѯ���ڵ�ǰ1����
			int oneyearago = 0;
			int onemonthago = 0;	
			if(querym==1){
				oneyearago = queryy - 1;
				onemonthago = 12;
			}else{
				oneyearago = queryy;
				onemonthago = querym - 1;
			}
			
			//��ѯ���ڵ�ǰ2����
			String querylastdate = null;	
			if(querym==1){
				querylastdate = (queryy-1)+"-11";
			}else if(querym==2){
				querylastdate = (queryy-1)+"-12";
			}else if(querym > 2 && querym <12){
				querylastdate = queryy+"-0"+(querym-2);
			}else{
				querylastdate = queryy+"-"+(querym-2);
			}
			//��ѯ���ڵ��¸���
			int  nextqueryy = 0;
			int nextquerym = 0;
			if(querym==12){
				nextqueryy = queryy+1;
				nextquerym = 1;
			}else{
				nextqueryy = queryy;
				nextquerym = querym+1;
			}
			
			//��ѯ�·ݵ�����
			int days = new UFDate(querydate+"-01").getDaysMonth();

			//��ѯ�·���ĩ����
			UFDate endquerydate = new UFDate(querydate+"-"+days);

			//��ѯ�·���2���ڳ�����
			UFDate startquerylastdate = new UFDate(querylastdate+"-01");
			
			//��ǩ
			String vLable = LableAndPackageCode.vLable;

			StringBuffer sqlamount = new StringBuffer()
			.append(" SELECT  ")
			.append(" bddef.docname docname ")
			.append(" ,bddefsec.docname docnamesec ")
			.append(" ,bdcorp.unitname unitname ")
			.append(" ,TABONE.inamount inamount ")
			.append(" ,TABONE.blmount blamount ")
			.append(" ,TABONE.output output ")
			.append(" ,TABONE.qmsl thismonthkc ")
			.append(" ,TABONE.lastqmsl lastmonthkc ")
			.append(" ,TABONE.inprice inprice ")
			.append(" ,TABONE.kcprice kcprice ")
			.append(" ,TABONE.qmje usingmoney ")
			.append(" ,TABONE.neveruseamount neveruse ")
			.append(" ,TABONE.kcrl kcrl ")
			.append(" ,TABONE.planamount planpurchase ")
			.append(" FROM ( ")
			.append(" SELECT  ")
			.append(" pk_corp ")
			.append(" ,SUM(inamount) inamount ")
			.append(" ,SUM(blmount) blmount ")
			.append(" ,SUM(output) output ")
			.append(" ,SUM(qmsl) qmsl ")
			.append(" ,SUM(lastqmsl) lastqmsl ")
			.append(" ,SUM(inprice) inprice ")
			.append(" ,SUM(kcprice) kcprice ")
			.append(" ,SUM(qmje) qmje ")
			.append(" ,SUM(neveruseamount) neveruseamount ")
			.append(" ,SUM(kcrl) kcrl ")
			.append(" ,SUM(planamount) planamount ")
			.append(" FROM( ")
			.append(" SELECT stockin.pk_corp ")
			.append(" ,CASE SUM(stockinb.inamount) ")
			.append(" WHEN 0 THEN 0 ")
			.append(" ELSE SUM(stockinb.def_6)/SUM(stockinb.inamount)  ")
			.append(" END inprice ")
			.append(" ,SUM(stockinb.inamount) inamount ")
			.append(" ,0 blmount ")
			.append(" ,0 output ")
			.append(" ,0 kcprice ")
			.append(" ,0 qmsl ")
			.append(" ,0 qmje ")
			.append(" ,0 kcrl ")
			.append(" ,0 lastqmsl ")
			.append(" ,0 planamount ")
			.append(" ,0 neveruseamount ")
			.append(" FROM eh_stock_in stockin,eh_stock_in_b stockinb ")
			.append(" ,bd_invmandoc invman,bd_invbasdoc invbas ")
			.append(" ,bd_invcl invcl ")
			.append(" where stockin.pk_in = stockinb.pk_in ")
			.append(" and stockinb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" and invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" and invbas.pk_invcl = invcl.pk_invcl ")
			.append(" and substr(invcl.invclasscode,1,6)='"+vLable+"' ")
			.append(" and SUBSTR(stockin.indate,1,7) = '"+querydate+"' ")
			.append(" and stockin.vbillstatus = '1' ")
			.append(" AND NVL(stockin.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" GROUP BY stockin.pk_corp ")
			.append("  ")
			.append(" UNION ALL ")
			.append("  ")
			.append(" SELECT ckd.pk_corp ")
			.append(" ,0 inprice ")
			.append(" ,0 inamount ")
			.append(" ,SUM(ckdb.blmount) blmount ")
			.append(" ,SUM(ckdb.blmount*substr(invbas.invspec,1,length(invbas.invspec)-2)/1000) output ")
			.append(" ,0 kcprice ")
			.append(" ,0 qmsl ")
			.append(" ,0 qmje ")
			.append(" ,0 kcrl ")
			.append(" ,0 lastqmsl ")
			.append(" ,0 planamount ")
			.append(" ,0 neveruseamount ")
			.append(" FROM eh_sc_ckd ckd,eh_sc_ckd_b ckdb ")
			.append(" ,bd_invmandoc invman,bd_invbasdoc invbas ")
			.append(" ,bd_invcl invcl ")
			.append(" where ckd.pk_ckd = ckdb.pk_ckd ")
			.append(" and ckdb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" and invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" and invbas.pk_invcl = invcl.pk_invcl ")
			.append(" and substr(invcl.invclasscode,1,6)='"+vLable+"' ")
			.append(" and SUBSTR(ckd.ckdate,1,7) = '"+querydate+"' ")
			.append(" and ckd.vbillstatus = '1' ")
			.append(" and NVL(ckd.dr,0) = '0' ")
			.append(" and NVL(invman.dr,0) = '0' ")
			.append(" GROUP BY ckd.pk_corp ")
			.append("  ")
			.append(" UNION ALL ")
			.append("  ")
			.append(" SELECT kcya.pk_corp ")
			.append(" ,0 inprice ")
			.append(" ,0 inamount ")
			.append(" ,0 blmount ")
			.append(" ,0 output ")
			.append(" , ")
			.append(" CASE SUM(kcyb.qmsl) ")
			.append(" WHEN 0 THEN 0 ")
			.append(" ELSE SUM(kcyb.qmje)/SUM(kcyb.qmsl) ")
			.append(" END kcprice ")
			.append(" ,SUM(kcyb.qmsl) qmsl ")
			.append(" ,SUM(kcyb.qmje) qmje ")
			.append(" ,SUM(kcyb.qmsl*substr(invbas.invspec,1,length(invbas.invspec)-2))/1000 kcrl ")
			.append(" ,0 lastqmsl   ")
			.append(" ,0 planamount ")
			.append(" ,0 neveruseamount ")
			.append("   FROM eh_calc_kcybb kcya, eh_calc_kcybb_b kcyb ")
			.append("   ,bd_invmandoc invman ,bd_invbasdoc invbas ,bd_invcl invcl ")
			.append("   WHERE kcya.pk_Kcybb = kcyb.pk_kcybb ")
			.append("   and kcyb.pk_invbasdoc = invman.pk_invmandoc ")
			.append("   and invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("   and invbas.pk_invcl = invcl.pk_invcl ")
			.append("   and substr(invcl.invclasscode,1,6)='"+vLable+"' ")
			.append("   and kcya.nyear = "+queryy+" and kcya.nmonth = "+querym+" ")
			.append("   and NVL(kcya.dr,0) = '0' ")
			.append("   AND NVL(invman.dr,0) = '0' ")
			.append("  GROUP BY  kcya.pk_corp ")
			.append("  ")
			.append(" UNION ALL ")
			.append("  ")
			.append(" SELECT kcya.pk_corp ")
			.append(" ,0 inprice ")
			.append(" ,0 inamount ")
			.append(" ,0 blmount ")
			.append(" ,0 output ")
			.append(" ,0 kcprice ")
			.append(" ,0 qmsl ")
			.append(" ,0 qmje ")
			.append(" ,0 kcrl ")
			.append(" ,SUM(kcyb.qmsl) lastqmsl ")
			.append(" ,0 planamount ")
			.append(" ,0 neveruseamount ")
			.append("   FROM eh_calc_kcybb kcya, eh_calc_kcybb_b kcyb ")
			.append("   ,bd_invmandoc invman ,bd_invbasdoc invbas ,bd_invcl invcl ")
			.append("   WHERE kcya.pk_Kcybb = kcyb.pk_kcybb ")
			.append("   and kcyb.pk_invbasdoc = invman.pk_invmandoc ")
			.append("   and invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("   and invbas.pk_invcl = invcl.pk_invcl ")
			.append("   and substr(invcl.invclasscode,1,6)='"+vLable+"' ")
			.append("   and kcya.nyear = "+oneyearago+" and kcya.nmonth = "+onemonthago+" ")
			.append("   and NVL(kcya.dr,0) = '0' ")
			.append("   AND NVL(invman.dr,0) = '0' ")
			.append("  GROUP BY  kcya.pk_corp ")
			.append("   ")
			.append("  UNION ALL ")
			.append("   ")
			.append("  SELECT trplan.pk_corp ")
			.append("  ,0 inprice ")
			.append(" ,0 inamount ")
			.append(" ,0 blmount ")
			.append(" ,0 output ")
			.append(" ,0 kcprice ")
			.append(" ,0 qmsl ")
			.append(" ,0 qmje ")
			.append(" ,0 kcrl ")
			.append(" ,0 lastqmsl ")
			.append(" ,SUM(bomb.zamount*trplanb.amount) planamount  ")
			.append(" ,0 neveruseamount ")
			.append(" from eh_bom bom ")
			.append(" ,eh_bom_b bomb ")
			.append(" ,eh_trade_plan trplan  ")
			.append(" ,eh_trade_plan_b trplanb ")
			.append(" ,bd_invmandoc invman ")
			.append(" ,bd_invbasdoc invbas ")
			.append(" ,bd_invcl invcl  ")
			.append(" WHERE bom.pk_bom = bomb.pk_bom ")
			.append(" and bom.pk_invbasdoc = trplanb.pk_invbasdoc ")
			.append(" and trplan.pk_plan = trplanb.pk_plan ")
			.append("  and trplan.nyear = "+nextqueryy+" and trplan.nmonth = "+nextquerym+" ")
			.append("  and bomb.pk_invbasdoc = invman.pk_invmandoc ")
			.append("  and invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append("  and invbas.pk_invcl = invcl.pk_invcl ")
			.append("  and substr(invcl.invclasscode,1,6)='"+vLable+"' ")
			.append("  AND trplan.vbillstatus = '1' ")
			.append("  AND bom.sc_flag = 'Y' ")
			.append("  AND NVL(bom.dr,0) = '0' ")
			.append("  AND NVL(invman.dr,0) = '0' ")
			.append("  GROUP BY trplan.pk_corp ")
			.append("   ")
			.append("  UNION ALL ")
			.append("   ")
			.append(" SELECT a.pk_corp ")
			.append("  ,0 inprice ")
			.append(" ,0 inamount ")
			.append(" ,0 blmount ")
			.append(" ,0 output ")
			.append(" ,0 kcprice ")
			.append(" ,0 qmsl ")
			.append(" ,0 qmje ")
			.append(" ,0 kcrl ")
			.append(" ,0 lastqmsl ")
			.append(" ,0 planamount  ")
			.append(" ,SUM(INAMOUNT)neveruseamount ")
			.append(" FROM eh_stock_in a,eh_stock_in_b b ")
			.append(" ,bd_invmandoc invman,bd_invbasdoc invbas ")
			.append(" ,bd_invcl invcl ")
			.append(" where a.pk_in = b.pk_in ")
			.append(" and b.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" and invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" and invbas.pk_invcl = invcl.pk_invcl ")
			.append(" and substr(invcl.invclasscode,1,6)='"+vLable+"' ")
			.append(" and a.indate between '"+startquerylastdate+"' and '"+endquerydate+"' ")
			.append(" and a.vbillstatus = '1' ")
			.append(" AND NVL(a.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" and invbas.pk_invbasdoc not in( ")
			.append(" SELECT  ")
			.append(" invbas.pk_invbasdoc ")
			.append(" FROM eh_sc_ckd aa,eh_sc_ckd_b bb ")
			.append(" ,bd_invmandoc invman,bd_invbasdoc invbas ")
			.append(" ,bd_invcl invcl ")
			.append(" where aa.pk_ckd = bb.pk_ckd ")
			.append(" and bb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" and invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" and invbas.pk_invcl = invcl.pk_invcl ")
			.append(" and substr(invcl.invclasscode,1,6)='"+vLable+"' ")
			.append(" and aa.ckdate between '"+startquerylastdate+"' and '"+endquerydate+"' ")
			.append(" and aa.vbillstatus = '1' ")
			.append(" and NVL(aa.dr,0) = '0' ")
			.append(" and NVL(invman.dr,0) = '0' ")
			.append(" ) ")
			.append(" GROUP BY a.pk_corp ")
			.append(" ) ")
			.append(" GROUP BY pk_corp)TABONE ")
			.append(" , bd_corp bdcorp ")
			.append(" ,bd_defdoc bddef  ")
			.append(" ,bd_defdoc bddefsec ")
			.append(" where  bdcorp.pk_corp = TABONE.pk_corp ")
			.append(" AND bdcorp.def1 = bddef.pk_defdoc(+) ")
			.append(" AND bdcorp.region = bddefsec.pk_defdoc(+) ");
			IUAPQueryBS iUAPQueryBSAmount = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList allAmount = (ArrayList) iUAPQueryBSAmount.executeQuery(sqlamount.toString(), new MapListProcessor());

			UFDouble kcrl = new UFDouble(0);//�������
			String docname = null;//Ƭ��
			String docnamesec = null;	//�г�����
			String unitname = null;	//��˾����
			UFDouble inamont = new UFDouble(0);	//��������	
			UFDouble blamount = new UFDouble(0);	//������
			UFDouble thismonthkc = new UFDouble(0);	//���¿��
			UFDouble lastmonthkc = new UFDouble(0);	//���¿��
			UFDouble inprice = new UFDouble(0);	//��������	
			UFDouble kcprice = new UFDouble(0); 	//��浥��
			UFDouble usingmoney = new UFDouble(0);	//ռ���ʽ�
			UFDouble canusedays = new UFDouble(0);	//��������
			UFDouble neveruse = new UFDouble(0);	//����δʹ����
			UFDouble output = new UFDouble(0);	//����
			UFDouble planpurchase = new UFDouble(0);//�ƻ��ɹ���	
			
			if (allAmount != null && allAmount.size() > 0) {
			for (int iam = 0; iam < allAmount.size(); iam++) {
				HashMap hmam = (HashMap) allAmount.get(iam);
				//Ƭ��
				docname = hmam.get("docname")==null?"":hmam.get("docname").toString();

				//�г�����
				docnamesec = hmam.get("docnamesec")==null?"":hmam.get("docnamesec").toString();

				//��˾����
				unitname = hmam.get("unitname")==null?"":hmam.get("unitname").toString();

				//��������
				inamont = new UFDouble(hmam.get("inamount")==null?"0":hmam.get("inamount").toString());

				//������
				blamount = new UFDouble(hmam.get("blamount")==null?"0":hmam.get("blamount").toString());

				//����
				output = new UFDouble(hmam.get("output")==null?"0":hmam.get("output").toString());

				//���¿��
				thismonthkc = new UFDouble(hmam.get("thismonthkc")==null?"0":hmam.get("thismonthkc").toString());

				//���¿��
				lastmonthkc = new UFDouble(hmam.get("lastmonthkc")==null?"0":hmam.get("lastmonthkc").toString());
				//��������
				inprice = new UFDouble(hmam.get("inprice")==null?"0":hmam.get("inprice").toString());

				//��浥��
				kcprice = new UFDouble(hmam.get("kcprice")==null?"0":hmam.get("kcprice").toString());

				//ռ���ʽ�
				usingmoney = new UFDouble(hmam.get("usingmoney")==null?"0":hmam.get("usingmoney").toString());

				//������δ����
				neveruse = new UFDouble(hmam.get("neveruse")==null?"0":hmam.get("neveruse").toString());
				
				//�������
				kcrl = new UFDouble(hmam.get("kcrl")==null?"0":hmam.get("kcrl").toString());

				//�ƻ��ɹ���
				planpurchase = new UFDouble(hmam.get("planpurchase")==null?"0":hmam.get("planpurchase").toString());
				
				//��������(�������*��������/������)
				canusedays = kcrl.multiply(days).div(blamount);
				
				ReportVO rvo = new ReportVO();
				rvo.setDocname(docname);
				rvo.setDocnamesec(docnamesec);
				rvo.setUnitname(unitname);
				rvo.setInamont(inamont);
				rvo.setBlamount(blamount);
				rvo.setThismonthkc(thismonthkc);
				rvo.setLastmonthkc(lastmonthkc);
				rvo.setInprice(inprice);
				rvo.setKcprice(kcprice);
				rvo.setUsingmoney(usingmoney);
				rvo.setCanusedays(canusedays);
				rvo.setNeveruse(neveruse);
				rvo.setOutput(output);
				rvo.setPlanpurchase(planpurchase);
				
				list.add(rvo);
			}
			}
			

			if(list.size()>0){
                rvos = (ReportVO[]) list.toArray(new ReportVO[0]);
            }          
			if(rvos!=null && rvos.length>0){
				ReportItem [] newItems = null;
				this.getReportBase().addBodyItem(newItems);
                this.getReportBase().setBodyDataVO(rvos);
                
                /*��ʾ�ϼ���*/
                String[] strValKeys = {"inamont,blamount,thismonthkc,lastmonthkc" +
                		",kcprice,usingmoney,canusedays,neveruse,output,planpurchase"}; 
                SubtotalContext stctx = new SubtotalContext();
                stctx.setSubtotalCols(strValKeys);          //����Ҫ���кϼƵ��ֶ�
                stctx.setTotalNameColKeys("unitname");      //���úϼ�����ʾ��λ��
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

