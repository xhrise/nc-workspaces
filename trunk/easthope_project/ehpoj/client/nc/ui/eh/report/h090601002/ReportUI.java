package nc.ui.eh.report.h090601002;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass; 
import nc.ui.pub.report.ReportItem;
import nc.vo.eh.report.h090601002.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
 *���ܣ�ԭ�Ͽ���ڼ������
 *���ߣ���־Զ 
 *���ڣ�2009-9-27 ����09:06:53
 */

public class ReportUI extends ToftPanel {

	private static final long serialVersionUID = 1L;
	public ButtonObject m_boQuery;
	public ButtonObject m_boPrint;
	private ReportBaseClass m_report;
	private QueryConditionClient m_qryDlg;
	private ClientEnvironment ce = ClientEnvironment.getInstance();
	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(
			IUAPQueryBS.class.getName());

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
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090601002",
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
		dlg.setTempletID(this.getCorpPrimaryKey(), "H090601002", null, null);
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
		//String pk_corp = this.getCorpPrimaryKey();
		QueryConditionClient uidialog = getQryDlg();
		String data = ce.getDate().toString();// ���������
		String ksdata = data.substring(0, 7);
		String start_data = "" + ksdata + "-01";// ȡ�õ��µĵ�һ��
		// �÷���ȡ��ĳ�µ�����
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
		int maxday = cal.get(Calendar.DAY_OF_MONTH);// ȡ�õ��µ��������
		String end_date = "" + ksdata + "-" + maxday + "";// ȡ�õ��µ����һ��
		uidialog.setDefaultValue("startdate", start_data, "");
		uidialog.setDefaultValue("enddate", data, "");
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		ArrayList list = new ArrayList();
		ReportVO[] rvos = null;// �Զ���VO
		if (getQryDlg().getResult() == 1) {
			ConditionVO[] date = getQryDlg().getConditionVOsByFieldCode(
					"startdate"); // ��ʼ����
			ConditionVO[] date2 = getQryDlg().getConditionVOsByFieldCode(
					"enddate"); // ��������
			UFDate startdate = null;
			UFDate enddate = null;
			if (date.length > 0) {
				startdate = new UFDate(date[0].getValue());
			}
			if (date2.length > 0) {
				enddate = new UFDate(date2[0].getValue());
			}
			if (date.length == 0 || date2.length == 0) {
				this.showErrorMessage("��ѡ��ʼ���ںͽ������ڣ�");
				return;
			}
			StringBuffer sql = new StringBuffer()
			.append(" SELECT  eh_stock.docname docname,eh_stock.docname docnamesec,eh_stock.unitname unitname,eh_stock.pk_corp, ")
					.append(" eh_stock.pk_invbasdoc pk_invbasdoc, eh_stock.invname invname,eh_stock.inamount inamount,eh_queryp.qprice qprice from ")
					.append(" ( SELECT  bddef.docname docname,bddefsec.docname docnamesec,bdcorp.unitname unitname,stock_in.pk_corp pk_corp, ")
					.append(" invbas.invname AS invname,stock_in_b.pk_invbasdoc AS pk_invbasdoc,SUM(stock_in_b.inamount) AS inamount ")
					.append(" FROM eh_stock_in stock_in   ")
					.append(" ,eh_stock_in_b stock_in_b  ")
					.append(" ,bd_invmandoc invman ")
					.append(" ,bd_invbasdoc invbas ")
					.append(" ,bd_corp bdcorp, bd_defdoc bddef, bd_defdoc bddefsec ")
					.append(" WHERE stock_in.pk_in = stock_in_b.pk_in ")
					.append(" AND stock_in.dmakedate BETWEEN '" + startdate + "' AND '" + enddate + "' ")
					.append(" AND stock_in_b.pk_invbasdoc = invman.pk_invmandoc ")
					.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
					.append(" AND bdcorp.pk_corp = stock_in.pk_corp AND bdcorp.def1 = bddef.pk_defdoc(+) AND bdcorp.region = bddefsec.pk_defdoc(+) ")
					.append(" AND NVL(stock_in.dr,0) = '0' ")
					.append(" AND stock_in.vbillstatus = '1' ")
					.append(" GROUP BY stock_in_b.pk_invbasdoc,invbas.invname,bddef.docname,bddefsec.docname, bdcorp.unitname,stock_in.pk_corp )eh_stock ")
					.append(" ,(SELECT  st_queryp_b.pk_invbasdoc AS pk_invbasdoc,SUM(st_queryp_b.dcj)/COUNT(st_queryp_b.pk_invbasdoc) AS qprice ")
					.append(" FROM eh_stock_queryprice st_queryp ")
					.append(" ,eh_stock_queryprice_b st_queryp_b ")
					.append(" WHERE  ")
					.append(" st_queryp.pk_queryprice = st_queryp_b.pk_queryprice  ")
					.append(" AND st_queryp.querydate BETWEEN '" + startdate + "' AND '" + enddate + "' ")
					.append(" AND NVL(st_queryp.dr,0) = '0' ")
					.append(" AND st_queryp.vbillstatus = '1' ")
					.append(" GROUP BY st_queryp_b.pk_invbasdoc ")
					.append(" )eh_queryp ")
					.append(" WHERE eh_stock.pk_invbasdoc = eh_queryp.pk_invbasdoc(+) ");

			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			String docname = null;//Ƭ��
			String docnamesec = null;//ʡ��
			String unitname = null;//��˾����
			String pkCorp = null;//����˾��Ӧ��pk_corp
			String pk_invbasdoc = null;// ����PK
			String invname = null;// ��������
			UFDouble amount = null;// ������
			UFDouble qprice = null;// �г��۸�
			UFDouble kcamount = null;// �����
			UFDouble kcprice = null;// ���۸�
			UFDouble cyprice = null;// �۸����
			UFDouble conincome = null;// ����
			// ѡ������֮�����������
			int days = UFDate.getDaysBetween(startdate, enddate);
			// ���ڽ�������
			UFDate lastEndDate = startdate.getDateBefore(1);
			// ���ڿ�ʼ����
			UFDate lastStartDate = lastEndDate.getDateBefore(days);
			//���ڿ���б�
			HashMap invKcHm = null;
			
			if (all != null && all.size() > 0) {
				for (int i = 0; i < all.size(); i++) {
					HashMap hm = (HashMap) all.get(i);
					docname = hm.get("docname") == null ? "" : hm.get("docname").toString();
					docnamesec = hm.get("docnamesec") == null ? "" : hm.get("docnamesec").toString();
					unitname = hm.get("unitname") == null ? "" : hm.get("unitname").toString();
					pkCorp = hm.get("pk_corp") == null ? "" : hm.get("pk_corp").toString();
					pk_invbasdoc = hm.get("pk_invbasdoc") == null ? "" : hm.get("pk_invbasdoc").toString();
					invname = hm.get("invname") == null ? "" : hm.get("invname").toString();
					amount = new UFDouble(hm.get("inamount") == null ? "0" : hm.get("inamount").toString());
					qprice = new UFDouble(hm.get("qprice") == null ? "0" : hm.get("qprice").toString());

					//���ڿ���б�ȡֵ
					invKcHm = new PubTools().getDateinvKC(null, null, enddate, "0", pkCorp);
					// ��Ӧ�����ϵı��ڿ��
					kcamount = new UFDouble(invKcHm.get(pk_invbasdoc) == null ? "0" : invKcHm.get(pk_invbasdoc).toString());
					// ÿ��ʣ����(�����-ÿ�βɹ���)
					UFDouble syamount = kcamount;
					// ���۸��ܶ�
					UFDouble kcAllAmount = new UFDouble(0);
					// ����ÿ�βɹ������ͼ۸�
					ArrayList arr = null;
					// ����ÿ�βɹ������ͼ۸�
					ArrayList lastArr = null;
					// ���������ڱ��ڿ����ʱ
					if (amount.compareTo(kcamount) > 0) {
						arr = this.getNowAmount(pk_invbasdoc, startdate,enddate, pkCorp);
						for (int ai = 0; ai < arr.size(); ai++) {
							HashMap al = (HashMap) arr.get(ai);
							// ÿ�βɹ�����
							UFDouble eveAmount = new UFDouble(al.get("amount").toString());
							// ÿ�βɹ��۸�
							UFDouble evePrice = new UFDouble(al.get("taxinprice").toString());
							if (syamount.compareTo(eveAmount) >= 0) {
								syamount = syamount.sub(eveAmount);
								kcAllAmount = kcAllAmount.add(eveAmount.multiply(evePrice));
							} else {
								kcAllAmount = kcAllAmount.add(syamount.multiply(evePrice));
							}
						}
					} else {// ������С�ڱ��ڿ����ʱ
						// ���ڲɹ������ͼ۸�
						arr = this.getNowAmount(pk_invbasdoc, startdate,enddate, pkCorp);
						for (int ai = 0; ai < arr.size(); ai++) {
							HashMap al = (HashMap) arr.get(ai);
							// ÿ�βɹ�����
							UFDouble eveAmount = new UFDouble(al.get("amount").toString());
							// ÿ�βɹ��۸�
							UFDouble evePrice = new UFDouble(al.get("taxinprice").toString());
							syamount = syamount.sub(eveAmount);
							kcAllAmount = kcAllAmount.add(eveAmount.multiply(evePrice));
						}
						// ���ڲɹ������ͼ۸�
						lastArr = this.getNowAmount(pk_invbasdoc,lastStartDate, lastEndDate, pkCorp);
						for (int ai = 0; ai < lastArr.size(); ai++) {
							HashMap al = (HashMap) lastArr.get(ai);
							// ����ÿ�βɹ�����
							UFDouble eveAmount = new UFDouble(al.get("amount").toString());
							// ����ÿ�βɹ��۸�
							UFDouble evePrice = new UFDouble(al.get("taxinprice").toString());
							if (syamount.compareTo(eveAmount) >= 0) {
								syamount = syamount.sub(eveAmount);
								kcAllAmount = kcAllAmount.add(eveAmount.multiply(evePrice));
							} else {
								kcAllAmount = kcAllAmount.add(syamount.multiply(evePrice));
							}
						}
					}
					//���۸�
					kcprice = kcAllAmount.div(kcamount);
					//�۸����
					cyprice = qprice.sub(kcprice);
					//����=���г��۸�-���۸�*�����
					conincome = cyprice.multiply(kcamount);
					ReportVO rvo = new ReportVO();
					rvo.setDocname(docname);
					rvo.setDocnamesec(docnamesec);
					rvo.setUnitname(unitname);
					rvo.setInvname(invname);
					rvo.setKcamount(kcamount);
					rvo.setKcprice(kcprice);
					rvo.setQprice(qprice);
					rvo.setCyprice(cyprice);
					rvo.setConincome(conincome);
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
                String[] strValKeys = {"conincome"}; 
                SubtotalContext stctx = new SubtotalContext();
                String[] strgrpValKeys = {"invname"};
                //stctx.setGrpKeys(strgrpValKeys);
                stctx.setSubtotalCols(strValKeys);          //����Ҫ���кϼƵ��ֶ�
                //stctx.setIsSubtotal(true);                  //��ҪС��
                //stctx.setLevelCompute(true);
                //stctx.setSubtotalName("С��");
                stctx.setTotalNameColKeys("invname");      //���úϼ�����ʾ��λ��
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

	// ȡ����ʱ�����ÿ�εĹ������ͼ۸�
	public ArrayList getNowAmount(String pk_invbasdoc, UFDate startDate, UFDate endDate, String pk_corp) {
		ArrayList all = null;
		StringBuffer sql = new StringBuffer()
				.append(" SELECT st_contract_b.amount AS amount, st_contract_b.taxinprice AS taxinprice ")
				.append(" FROM eh_stock_contract st_contract ,eh_stock_contract_b st_contract_b  ")
				.append(" WHERE st_contract.pk_contract = st_contract_b.pk_contract ")
				.append(" AND st_contract.dmakedate BETWEEN '" + startDate + "' AND '" + endDate + "' ")
				.append(" AND st_contract_b.pk_invbasdoc = '" + pk_invbasdoc + "' ")
				.append(" AND st_contract.pk_corp = '" + pk_corp + "' ")
				.append(" AND NVL(st_contract.dr,0) = '0' ")
				.append(" AND st_contract.vbillstatus = '1' ")
				.append(" order by st_contract_b.ts desc ");
		try {
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all;
	}

}
