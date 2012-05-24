package nc.ui.eh.report.h0900506;

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
import nc.vo.eh.report.h0900506.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
 *功能：耗用期间受益表 
 *作者：张志远 
 *日期：2009-9-15 下午17:06:53
 *对应节点修改使用H090601001
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
		m_boQuery = new ButtonObject("查询", "查询报表", 0);
		m_boPrint = new ButtonObject("打印", "打印报表", 0);
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
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090601001",
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
		dlg.setTempletID(this.getCorpPrimaryKey(), "H090601001", null, null);
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

	// 设置打印方法
	public void onPrint() throws Exception {
		this.getReportBase().previewData();
	}

	// 查询方法
	public void onQuery() throws Exception {
		this.getReportBase().getBillModel().clearBodyData();
		//String pk_corp = this.getCorpPrimaryKey();
		QueryConditionClient uidialog = getQryDlg();
		String data = ce.getDate().toString();// 当天的日期
		String ksdata = data.substring(0, 7);
		String start_data = "" + ksdata + "-01";// 取得当月的第一天
		// 该方法取得某月的天数
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
		int maxday = cal.get(Calendar.DAY_OF_MONTH);// 取得当月的最大天数
		String end_date = "" + ksdata + "-" + maxday + "";// 取得当月的最后一天
		uidialog.setDefaultValue("startdate", start_data, "");
		uidialog.setDefaultValue("enddate", end_date, "");
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		ArrayList list = new ArrayList();
		ReportVO[] rvos = null;// 自定义VO
		if (getQryDlg().getResult() == 1) {
			ConditionVO[] date = getQryDlg().getConditionVOsByFieldCode(
					"startdate"); // 开始日期
			ConditionVO[] date2 = getQryDlg().getConditionVOsByFieldCode(
					"enddate"); // 结束日期
			UFDate startdate = null;
			UFDate enddate = null;
			if (date.length > 0) {
				startdate = new UFDate(date[0].getValue());
			}
			if (date2.length > 0) {
				enddate = new UFDate(date2[0].getValue());
			}
			if (date.length == 0 || date2.length == 0) {
				this.showErrorMessage("请选择开始日期和结束日期！");
				return;
			}
			StringBuffer sql = new StringBuffer()
			.append(" SELECT  eh_stock.docname docname,eh_stock.docname docnamesec,eh_stock.unitname unitname,eh_stock.pk_corp, ")
					.append(" eh_stock.pk_invbasdoc pk_invbasdoc, eh_stock.invname invname,eh_stock.inamount inamount,eh_stock.inprice inprice,eh_ckd.blmount blmount,eh_queryp.qprice qprice from ")
					.append(" ( SELECT  bddef.docname docname,bddefsec.docname docnamesec,bdcorp.unitname unitname,stock_in.pk_corp pk_corp, ")
					.append(" invbas.invname AS invname,stock_in_b.pk_invbasdoc AS pk_invbasdoc,SUM(stock_in_b.inamount) AS inamount ")
					.append(" , SUM(stock_in_b.inamount * stock_in_b.inprice)/SUM(stock_in_b.inamount) AS inprice  ")
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
					//.append(" AND stock_in.pk_corp = '" + pk_corp + "' ")
					.append(" AND NVL(stock_in.dr,0) = '0' ")
					.append(" AND stock_in.vbillstatus = '1' ")
					.append(" GROUP BY stock_in_b.pk_invbasdoc,invbas.invname,bddef.docname,bddefsec.docname, bdcorp.unitname,stock_in.pk_corp )eh_stock ")
					.append(" ,(SELECT  st_queryp_b.pk_invbasdoc AS pk_invbasdoc,SUM(st_queryp_b.dcj)/COUNT(st_queryp_b.pk_invbasdoc) AS qprice ")
					.append(" FROM eh_stock_queryprice st_queryp ")
					.append(" ,eh_stock_queryprice_b st_queryp_b ")
					.append(" WHERE  ")
					.append(" st_queryp.pk_queryprice = st_queryp_b.pk_queryprice  ")
					.append(" AND st_queryp.querydate BETWEEN '" + startdate + "' AND '" + enddate + "' ")
					//.append(" AND st_queryp.pk_corp = '" + pk_corp + "' ")
					.append(" AND NVL(st_queryp.dr,0) = '0' ")
					.append(" AND st_queryp.vbillstatus = '1' ")
					.append(" GROUP BY st_queryp_b.pk_invbasdoc ")
					.append(" )eh_queryp ")
					.append(" ,(SELECT sc_ckd_b.pk_invbasdoc,sum(sc_ckd_b.blmount) AS blmount ")
					.append(" FROM eh_sc_ckd sc_ckd ")
					.append(" ,eh_sc_ckd_b sc_ckd_b ")
					.append(" WHERE sc_ckd.pk_ckd = sc_ckd_b.pk_ckd ")
					.append(" AND sc_ckd.ckdate BETWEEN '" + startdate + "' AND '" + enddate + "'    ")
					//.append(" AND sc_ckd.pk_corp = '" + pk_corp + "'     ")
					.append(" AND sc_ckd.dr = '0' ")
					.append(" AND sc_ckd.vbillstatus = '1'            ")
					.append(" GROUP BY sc_ckd_b.pk_invbasdoc ")
					.append(" )eh_ckd ")
					.append(" WHERE eh_stock.pk_invbasdoc = eh_queryp.pk_invbasdoc(+) ")
					.append(" AND eh_stock.pk_invbasdoc = eh_ckd.pk_invbasdoc(+) ");

			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			//eh_stock.unitname unitname,eh_stock.pk_corp
			String docname = null;//片区
			String docnamesec = null;//省份
			String unitname = null;//公司名称
			String pkCorp = null;//各公司对应的pk_corp
			String pk_invbasdoc = null;// 物料PK
			String invname = null;// 物料名称
			UFDouble amount = null;// 购进量
			UFDouble taxinprice = null;// 购进价格
			UFDouble blmount = null;// 消耗量
			UFDouble qprice = null;// 市场价格
			UFDouble kcamount = null;// 库存量
			UFDouble kcprice = null;// 库存价格
			UFDouble conincome = null;// 耗用受益
			UFDouble lastKcAmount = null;// 上期库存量
			// 选择日期之间相隔的天数
			int days = UFDate.getDaysBetween(startdate, enddate);
			// 上期结束日期
			UFDate lastEndDate = startdate.getDateBefore(1);
			// 上期开始日期
			UFDate lastStartDate = lastEndDate.getDateBefore(days);
			//本期库存列表
			HashMap invKcHm = null;
			//上期库存列表
			HashMap lastInvKcHm = null;
			
			if (all != null && all.size() > 0) {
				for (int i = 0; i < all.size(); i++) {
					HashMap hm = (HashMap) all.get(i);
					docname = hm.get("docname") == null ? "" : hm.get("docname").toString();
					docnamesec = hm.get("docnamesec") == null ? "" : hm.get("docnamesec").toString();
					unitname = hm.get("unitname") == null ? "" : hm.get("unitname").toString();
					pkCorp = hm.get("pkCorp") == null ? "" : hm.get("pkCorp").toString();
					pk_invbasdoc = hm.get("pk_invbasdoc") == null ? "" : hm.get("pk_invbasdoc").toString();
					invname = hm.get("invname") == null ? "" : hm.get("invname").toString();
					amount = new UFDouble(hm.get("inamount") == null ? "0" : hm.get("inamount").toString());
					taxinprice = new UFDouble(hm.get("inprice") == null ? "0" : hm.get("inprice").toString());
					blmount = new UFDouble(hm.get("blmount") == null ? "0" : hm.get("blmount").toString());
					qprice = new UFDouble(hm.get("qprice") == null ? "0" : hm.get("qprice").toString());

					//本期库存列表取值
					invKcHm = new PubTools().getDateinvKC(null, null, enddate, "0", pkCorp);
					// 对应各物料的本期库存
					kcamount = new UFDouble(invKcHm.get(pk_invbasdoc) == null ? "0" : invKcHm.get(pk_invbasdoc).toString());
					//上期库存列表取值
					lastInvKcHm = new PubTools().getDateinvKC("", "", lastEndDate, "0", pkCorp);
					//对应各物料的上期库存
					lastKcAmount = new UFDouble(lastInvKcHm.get(pk_invbasdoc) == null ? "0" : lastInvKcHm.get(pk_invbasdoc).toString());
					// 每次剩余用(库存量-每次采购量)
					UFDouble syamount = kcamount;
					// 库存价格总额
					UFDouble kcAllAmount = new UFDouble(0);
					// 本期每次采购数量和价格
					ArrayList arr = null;
					// 上期每次采购数量和价格
					ArrayList lastArr = null;
					// 购进量大于本期库存量时
					if (amount.compareTo(kcamount) > 0) {
						arr = this.getNowAmount(pk_invbasdoc, startdate,enddate, pkCorp);
						for (int ai = 0; ai < arr.size(); ai++) {
							HashMap al = (HashMap) arr.get(ai);
							// 每次采购数量
							UFDouble eveAmount = new UFDouble(al.get("amount").toString());
							// 每次采购价格
							UFDouble evePrice = new UFDouble(al.get("taxinprice").toString());
							if (syamount.compareTo(eveAmount) >= 0) {
								syamount = syamount.sub(eveAmount);
								kcAllAmount = kcAllAmount.add(eveAmount.multiply(evePrice));
							} else {
								kcAllAmount = kcAllAmount.add(syamount.multiply(evePrice));
							}
						}
					} else {// 购进量小于本期库存量时

						// 本期采购数量和价格
						arr = this.getNowAmount(pk_invbasdoc, startdate,enddate, pkCorp);
						for (int ai = 0; ai < arr.size(); ai++) {
							HashMap al = (HashMap) arr.get(ai);
							// 每次采购数量
							UFDouble eveAmount = new UFDouble(al.get("amount").toString());
							// 每次采购价格
							UFDouble evePrice = new UFDouble(al.get("taxinprice").toString());
							syamount = syamount.sub(eveAmount);
							kcAllAmount = kcAllAmount.add(eveAmount.multiply(evePrice));
						}
						// 上期采购数量和价格
						lastArr = this.getNowAmount(pk_invbasdoc,lastStartDate, lastEndDate, pkCorp);
						for (int ai = 0; ai < lastArr.size(); ai++) {
							HashMap al = (HashMap) lastArr.get(ai);
							// 上期每次采购数量
							UFDouble eveAmount = new UFDouble(al.get("amount").toString());
							// 上期每次采购价格
							UFDouble evePrice = new UFDouble(al.get("taxinprice").toString());
							if (syamount.compareTo(eveAmount) >= 0) {
								syamount = syamount.sub(eveAmount);
								kcAllAmount = kcAllAmount.add(eveAmount.multiply(evePrice));
							} else {
								kcAllAmount = kcAllAmount.add(syamount.multiply(evePrice));
							}
						}
					}
					kcprice = kcAllAmount.mod(kcamount);
					// 上期库存大于本期耗用
					if (lastKcAmount.compareTo(blmount) >= 0) {
						// 受益:(市场价格-库存价格)*本期耗用量
						conincome = (qprice.sub(kcprice)).multiply(blmount);
					} else {// 上期库存小于本期耗用
						// 上期库存*（市场价格-库存价格）+（本期耗用-上期库存）*（市场价格-库存价格）
						conincome = (lastKcAmount.multiply(qprice.sub(kcprice)))
								.add((blmount.sub(lastKcAmount))
								.multiply(qprice.sub(kcprice)));
					}
					ReportVO rvo = new ReportVO();

					rvo.setDocname(docname);
					rvo.setDocnamesec(docnamesec);
					rvo.setUnitname(unitname);
					rvo.setPkCorp(pkCorp);
					rvo.setInvname(invname);
					rvo.setAmount(amount);
					rvo.setTaxinprice(taxinprice);
					rvo.setBlmount(blmount);
					rvo.setQprice(qprice);
					//上期库存量
					rvo.setKcamount(lastKcAmount);
					rvo.setKcprice(kcprice);
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
                
                /*显示合计项*/
                String[] strValKeys = {"amount","taxinprice","blmount","kcamount","conincome"}; 
                SubtotalContext stctx = new SubtotalContext();
                String[] strgrpValKeys = {"invname"};
                //stctx.setGrpKeys(strgrpValKeys);
                stctx.setSubtotalCols(strValKeys);          //配置要进行合计的字段
                //stctx.setIsSubtotal(true);                  //需要小计
                //stctx.setLevelCompute(true);
                //stctx.setSubtotalName("小计");
                stctx.setTotalNameColKeys("invname");      //设置合计项显示列位置
                stctx.setSumtotalName("合计");              //设置合计项显示名称
                this.getReportBase().setSubtotalContext(stctx);
                 
                this.getReportBase().subtotal();
                this.getReportBase().execHeadLoadFormulas();
                this.getReportBase().execTailLoadFormulas();
                updateUI();
            }else{
                this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
            }
		}
	}

	// 取给定时间段内每次的购入量和价格
	public ArrayList getNowAmount(String pk_invbasdoc, UFDate startDate,
			UFDate endDate, String pk_corp) {
		ArrayList all = null;
		StringBuffer sql = new StringBuffer()
				.append(
						" SELECT st_contract_b.amount AS amount, st_contract_b.taxinprice AS taxinprice ")
				.append(
						" FROM eh_stock_contract st_contract ,eh_stock_contract_b st_contract_b  ")
				.append(
						" WHERE st_contract.pk_contract = st_contract_b.pk_contract ")
				.append(
						" AND st_contract.dmakedate BETWEEN '" + startDate
								+ "' AND '" + endDate + "' ").append(
						" AND st_contract_b.pk_invbasdoc = '" + pk_invbasdoc
								+ "' ").append(
						" AND st_contract.pk_corp = '" + pk_corp + "' ")
				.append(" AND NVL(st_contract.dr,0) = '0' ").append(
						" AND st_contract.vbillstatus = '1' ").append(
						" order by st_contract_b.ts desc ");
		try {
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance()
					.lookup(IUAPQueryBS.class.getName());
			all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(),
					new MapListProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all;
	}

}
