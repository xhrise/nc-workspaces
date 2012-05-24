package nc.ui.eh.report.h090602025;

import java.awt.BorderLayout;
import java.util.ArrayList;
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
import nc.vo.eh.report.h090602025.ReportVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * 
 *功能：片区原料进耗存日报表
 *作者：张志远 
 *日期：2009-10-13 下午15:33:53
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
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090602025",
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
		dlg.setTempletID(this.getCorpPrimaryKey(), "H090602025", null, null);
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
	@SuppressWarnings("unchecked")
	public void onQuery() throws Exception {
		this.getReportBase().getBillModel().clearBodyData();
		QueryConditionClient uidialog = getQryDlg();
		String data = ce.getDate().toString();// 当天的日期
		UFDate lastdayoflastMonth = null;//上月的最后一天
		int maxday = 0;//查询月份天数
		uidialog.setDefaultValue("querydate", data, "");//将当天日期默认为查询日期
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		ArrayList<ReportVO> list = new ArrayList<ReportVO>();
		ReportVO[] rvos = null;// 自定义VO
		if (getQryDlg().getResult() == 1) {
			ConditionVO[] date = getQryDlg().getConditionVOsByFieldCode("querydate"); // 开始日期
			UFDate querydate = null;//查询日期
			UFDate qstar_date = null;
			UFDate qend_date = null;
			if (date.length > 0) {
				querydate = new UFDate(date[0].getValue());
				qstar_date = new UFDate(querydate.toString().substring(0, 7)+"-01");
				maxday = querydate.getDaysMonth();
				qend_date = new UFDate(querydate.toString().substring(0, 7)+"-"+maxday);
				lastdayoflastMonth = qstar_date.getDateBefore(1);

			}
			if (date.length == 0 ) {
				this.showErrorMessage("请选择查询日期！");
				return;
			}
			StringBuffer sql = new StringBuffer()
			.append(" SELECT pk_invbasdoc pk_invbasdoc ")
			.append(" ,pk_corp pk_corp ")
			.append(" ,unitname unitname ")
			.append(" ,invname invname ")
			.append(" ,SUM(inaa) inaa ")
			.append(" ,SUM(inbb) inbb ")
			.append(" ,SUM(incc) incc ")
			.append(" ,SUM(inamount) inamount ")
			.append(" ,SUM(subinamount) subinamount ")
			.append(" ,SUM(iamount) iamount ")
			.append(" ,SUM(blaa) blaa ")
			.append(" ,SUM(blbb) blbb ")
			.append(" ,SUM(blcc) blcc ")
			.append(" ,SUM(blmount) blmount ")
			.append(" ,SUM(subblmount) subblmount ")
			.append(" ,SUM(bamount) bamount ")
			.append(" FROM ")
			.append(" ( ")
			.append(" SELECT  ")
			.append(" TABONE.pk_invbasdoc ")
			.append(" ,TABONE.pk_corp ")
			.append(" ,TABONE.unitname ")
			.append(" ,TABONE.invname ")
			.append("  ")
			.append(" ,TABTWO.inamount inaa ")
			.append(" ,TABTWO.subinamount  inbb ")
			.append(" ,TABTWO.amount incc ")
			.append("  ")
			.append(" ,TABONE.inamount inamount ")
			.append(" ,TABONE.subinamount subinamount ")
			.append(" ,TABONE.amount iamount ")
			.append("  ")
			.append(" ,0 blaa ")
			.append(" ,0 blbb ")
			.append(" ,0 blcc ")
			.append("  ")
			.append(" ,0 blmount ")
			.append(" ,0 subblmount ")
			.append(" ,0 bamount ")
			.append("  ")
			.append(" FROM ")
			.append(" (SELECT TABLEONE.pk_invbasdoc ")
			.append(" ,TABLEONE.pk_corp ")
			.append(" ,TABLEONE.unitname ")
			.append(" ,TABLEONE.invname ")
			.append(" ,TABLEONE.inamount ")
			.append("  ")
			.append(" ,TABLETWO.amount-TABLEONE.inamount subinamount ")
			.append(" ,TABLETWO.amount ")
			.append(" FROM( ")
			.append(" SELECT  ")
			.append(" invbas.pk_invbasdoc pk_invbasdoc ")
			.append(" ,stockin.pk_corp pk_corp ")
			.append(" ,corp.unitname ")
			.append(" ,invbas.invname invname ")
			.append(" , SUM(stockinb.inamount) inamount ")
			.append(" FROM eh_stock_in stockin  ")
			.append(" ,eh_stock_in_b stockinb ")
			.append(" ,bd_invmandoc invman ")
			.append(" ,bd_invbasdoc invbas ")
			.append(" ,bd_corp corp ")
			.append(" WHERE stockin.pk_in = stockinb.pk_in ")
			.append(" AND stockinb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND stockin.pk_corp = corp.pk_corp ")
			.append(" AND stockin.vbillstatus = '1' ")
			.append(" AND NVL(stockin.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND stockin.indate BETWEEN '"+qstar_date+"' AND '"+qend_date+"' ")
			.append(" GROUP BY invbas.pk_invbasdoc,stockin.pk_corp,corp.unitname,invbas.invname ")
			.append(" )TABLEONE ")
			.append(" , ")
			.append(" (SELECT ")
			.append(" stockin.pk_corp pk_corp ")
			.append(" ,SUM(stockinb.inamount) amount ")
			.append(" FROM eh_stock_in stockin  ")
			.append(" ,eh_stock_in_b stockinb ")
			.append(" ,bd_invmandoc invman  ")
			.append(" ,bd_invbasdoc invbas ")
			.append(" WHERE stockin.pk_in = stockinb.pk_in ")
			.append(" AND stockinb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND stockin.vbillstatus = '1' ")
			.append(" AND NVL(stockin.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND stockin.indate BETWEEN '"+qstar_date+"' AND '"+qend_date+"' ")
			.append(" GROUP BY stockin.pk_corp)TABLETWO ")
			.append(" WHERE TABLEONE.pk_corp = TABLETWO.pk_corp)TABONE, ")
			.append(" ( ")
			.append(" SELECT TABLEONE.pk_invbasdoc ")
			.append(" ,TABLEONE.pk_corp ")
			.append(" ,TABLEONE.invname ")
			.append(" ,TABLEONE.inamount ")
			.append(" ,TABLETWO.amount-TABLEONE.inamount subinamount ")
			.append(" ,TABLETWO.amount ")
			.append(" FROM( ")
			.append(" SELECT  ")
			.append(" invbas.pk_invbasdoc pk_invbasdoc ")
			.append(" ,stockin.pk_corp pk_corp ")
			.append(" ,invbas.invname invname ")
			.append(" , SUM(stockinb.inamount) inamount ")
			.append(" FROM eh_stock_in stockin  ")
			.append(" ,eh_stock_in_b stockinb ")
			.append(" ,bd_invmandoc invman  ")
			.append(" ,bd_invbasdoc invbas ")
			.append(" WHERE stockin.pk_in = stockinb.pk_in ")
			.append(" AND stockinb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND stockin.vbillstatus = '1' ")
			.append(" AND NVL(stockin.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND stockin.indate = '"+querydate+"' ")
			.append(" GROUP BY invbas.pk_invbasdoc,stockin.pk_corp,invbas.invname ")
			.append(" ORDER BY invbas.pk_invbasdoc)TABLEONE ")
			.append(" , ")
			.append(" (SELECT ")
			.append(" stockin.pk_corp pk_corp ")
			.append(" ,SUM(stockinb.inamount) amount ")
			.append(" FROM eh_stock_in stockin  ")
			.append(" ,eh_stock_in_b stockinb ")
			.append(" ,bd_invmandoc invman  ")
			.append(" ,bd_invbasdoc invbas ")
			.append(" WHERE stockin.pk_in = stockinb.pk_in ")
			.append(" AND stockinb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND stockin.vbillstatus = '1' ")
			.append(" AND NVL(stockin.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND stockin.indate = '"+querydate+"' ")
			.append(" GROUP BY stockin.pk_corp)TABLETWO ")
			.append(" WHERE TABLEONE.pk_corp = TABLETWO.pk_corp  ")
			.append(" )TABTWO  ")
			.append(" where TABONE.pk_invbasdoc = TABTWO.pk_invbasdoc ")
			.append(" AND TABONE.pk_corp = TABTWO.pk_corp ")
			.append("  ")
			.append(" UNION ALL ")
			.append("  ")
			.append(" SELECT  ")
			.append(" TABONE.pk_invbasdoc ")
			.append(" ,TABONE.pk_corp ")
			.append(" ,TABONE.unitname ")
			.append(" ,TABONE.invname ")
			.append("  ")
			.append(" ,0 inaa ")
			.append(" ,0  inbb ")
			.append(" ,0 incc ")
			.append("  ")
			.append(" ,0 inamount ")
			.append(" ,0 subinamount ")
			.append(" ,0 iamount ")
			.append("  ")
			.append("  ")
			.append(" ,TABTWO.blmount blaa ")
			.append(" ,TABTWO.subblmount blbb ")
			.append(" ,TABTWO.amount blcc ")
			.append("  ")
			.append(" ,TABONE.blmount ")
			.append(" ,TABONE.subblmount ")
			.append(" ,TABONE.amount ")
			.append(" FROM ")
			.append(" (SELECT TABLEONE.pk_invbasdoc,TABLEONE.pk_corp ")
			.append(" ,TABLEONE.unitname ")
			.append(" ,TABLEONE.invname ")
			.append(" ,TABLEONE.blmount,(TABLETWO.amount-TABLEONE.blmount)subblmount,TABLETWO.amount ")
			.append(" FROM ")
			.append(" (SELECT invbas.pk_invbasdoc ")
			.append(" ,sckd.pk_corp ")
			.append(" ,corp.unitname ")
			.append(" ,invbas.invname ")
			.append(" ,SUM(sckdb.blmount)blmount  ")
			.append(" FROM eh_sc_ckd sckd , eh_sc_ckd_b sckdb  ")
			.append(" ,bd_invmandoc invman ,bd_invbasdoc invbas ")
			.append(" ,bd_corp corp  ")
			.append(" WHERE sckd.pk_ckd = sckdb.pk_ckd ")
			.append(" AND sckdb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND sckd.pk_corp = corp.pk_corp ")
			.append(" AND sckd.vbillstatus = '1' ")
			.append(" AND NVL(sckd.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND sckd.ckdate BETWEEN '"+qstar_date+"' AND '"+qend_date+"' ")
			.append(" GROUP BY invbas.pk_invbasdoc,sckd.pk_corp,corp.unitname,invbas.invname)TABLEONE, ")
			.append("  ")
			.append(" (SELECT sckd.pk_corp,SUM(sckdb.blmount)amount FROM eh_sc_ckd sckd , eh_sc_ckd_b sckdb  ")
			.append(" ,bd_invmandoc invman ,bd_invbasdoc invbas ")
			.append(" WHERE sckd.pk_ckd = sckdb.pk_ckd ")
			.append(" AND sckdb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND sckd.vbillstatus = '1' ")
			.append(" AND NVL(sckd.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND sckd.ckdate BETWEEN '"+qstar_date+"' AND '"+qend_date+"' ")
			.append(" GROUP BY sckd.pk_corp)TABLETWO ")
			.append(" WHERE TABLEONE.pk_corp = TABLETWO.pk_corp)TABONE, ")
			.append(" ( ")
			.append(" SELECT TABLEONE.pk_invbasdoc,TABLEONE.pk_corp,TABLEONE.invname ")
			.append(" ,TABLEONE.blmount,(TABLETWO.amount-TABLEONE.blmount)subblmount,TABLETWO.amount ")
			.append(" FROM ")
			.append(" (SELECT invbas.pk_invbasdoc,sckd.pk_corp,invbas.invname,SUM(sckdb.blmount)blmount  ")
			.append(" FROM eh_sc_ckd sckd , eh_sc_ckd_b sckdb  ")
			.append(" ,bd_invmandoc invman ,bd_invbasdoc invbas ")
			.append(" WHERE sckd.pk_ckd = sckdb.pk_ckd ")
			.append(" AND sckdb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND sckd.vbillstatus = '1' ")
			.append(" AND NVL(sckd.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND sckd.ckdate = '"+querydate+"' ")
			.append(" GROUP BY invbas.pk_invbasdoc,sckd.pk_corp,invbas.invname)TABLEONE, ")
			.append(" (SELECT sckd.pk_corp,SUM(sckdb.blmount)amount FROM eh_sc_ckd sckd , eh_sc_ckd_b sckdb  ")
			.append(" ,bd_invmandoc invman ,bd_invbasdoc invbas ")
			.append(" WHERE sckd.pk_ckd = sckdb.pk_ckd ")
			.append(" AND sckdb.pk_invbasdoc = invman.pk_invmandoc ")
			.append(" AND invman.pk_invbasdoc = invbas.pk_invbasdoc ")
			.append(" AND sckd.vbillstatus = '1' ")
			.append(" AND NVL(sckd.dr,0) = '0' ")
			.append(" AND NVL(invman.dr,0) = '0' ")
			.append(" AND sckd.ckdate = '"+querydate+"' ")
			.append(" GROUP BY sckd.pk_corp)TABLETWO ")
			.append(" WHERE TABLEONE.pk_corp = TABLETWO.pk_corp ")
			.append(" )TABTWO ")
			.append(" WHERE TABONE.pk_invbasdoc = TABTWO.pk_invbasdoc ")
			.append(" AND TABONE.pk_corp = TABTWO.pk_corp ")
			.append(" ) ")
			.append(" GROUP BY pk_invbasdoc ")
			.append(" ,pk_corp ")
			.append(" ,unitname ")
			.append(" ,invname ")
			.append(" ORDER BY pk_invbasdoc ");

			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			String pk_invbasdoc = null;
			String invname = null;//物料名称
			String pk_corp = null;
			String unitname = null;//公司名称
			UFDouble firstkc = new UFDouble(0);//期初库存(上月末库存)
			UFDouble inaa = new UFDouble(0);//本日购进
			UFDouble inbb = new UFDouble(0);//本日其它入库
			UFDouble incc = new UFDouble(0);//本日入库合计
			UFDouble inamount = new UFDouble(0);//累计购进(本月)
			UFDouble subinamount = new UFDouble(0);//累计其它入库(本月)
			UFDouble iamount = new UFDouble(0);//累计入库合计(本月)
			UFDouble blaa = new UFDouble(0);//本日生产耗用
			UFDouble blbb = new UFDouble(0);//本日其它出库
			UFDouble blcc = new UFDouble(0);//本日出库合计
			UFDouble blmount = new UFDouble(0);//累计出库(本月)
			UFDouble subblmount = new UFDouble(0);//累计其他出库(本月)
			UFDouble bamount = new UFDouble(0);//累计出库合计(本月)
			UFDouble invrest = new UFDouble(0);//结存
			UFDouble usedays = new UFDouble(0);//可使用天数

			HashMap invKcHm = null;//各个物料库存
			
			if (all != null && all.size() > 0) {
				for (int i = 0; i < all.size(); i++) {
					HashMap hm = (HashMap) all.get(i);
					pk_invbasdoc = hm.get("pk_invbasdoc") == null ? "" : hm.get("pk_invbasdoc").toString();
					invname = hm.get("invname") == null ? "" : hm.get("invname").toString();
					pk_corp = hm.get("pk_corp") == null ? "" : hm.get("pk_corp").toString();
					unitname = hm.get("unitname") == null ? "" : hm.get("unitname").toString();
					inaa = new UFDouble(hm.get("inaa") == null ? "0" : hm.get("inaa").toString());
					inbb = new UFDouble(hm.get("inbb") == null ? "0" : hm.get("inbb").toString());
					incc = new UFDouble(hm.get("incc") == null ? "0" : hm.get("incc").toString());
					inamount = new UFDouble(hm.get("inamount") == null ? "0" : hm.get("inamount").toString());
					subinamount = new UFDouble(hm.get("subinamount") == null ? "0" : hm.get("subinamount").toString());
					iamount = new UFDouble(hm.get("iamount") == null ? "0" : hm.get("iamount").toString());
					blaa = new UFDouble(hm.get("blaa") == null ? "0" : hm.get("blaa").toString());
					blbb = new UFDouble(hm.get("blbb") == null ? "0" : hm.get("blbb").toString());
					blcc = new UFDouble(hm.get("blcc") == null ? "0" : hm.get("blcc").toString());
					blmount = new UFDouble(hm.get("blmount") == null ? "0" : hm.get("blmount").toString());
					subblmount = new UFDouble(hm.get("subblmount") == null ? "0" : hm.get("subblmount").toString());
					bamount = new UFDouble(hm.get("bamount") == null ? "0" : hm.get("bamount").toString());
					
					//期初库存列表取值
					invKcHm = new PubTools().getDateinvKC(null, null, lastdayoflastMonth, "0", pk_corp);
					// 对应各物料的期初库存
					firstkc = new UFDouble(invKcHm.get(pk_invbasdoc) == null ? "0" : invKcHm.get(pk_invbasdoc).toString());
					//结存=期初库存+累计入库-累计出库
					invrest = firstkc.add(iamount).sub(bamount); 
					//可使用天数=结存/(累计出库/当月天数)
					usedays = invrest.div((bamount.div(maxday)));
					
					ReportVO rvo = new ReportVO();
					rvo.setInvname(invname);
					rvo.setUnitname(unitname);
					rvo.setFirstkc(firstkc);
					rvo.setInaa(inaa);
					rvo.setInbb(inbb);
					rvo.setIncc(incc);
					rvo.setInamount(inamount);
					rvo.setSubinamount(subinamount);
					rvo.setIamount(iamount);
					rvo.setBlaa(blaa);
					rvo.setBlbb(blbb);
					rvo.setBlcc(blcc);
					rvo.setBlmount(blmount);
					rvo.setSubblmount(subblmount);
					rvo.setBamount(bamount);
					rvo.setInvrest(invrest);
					rvo.setUsedays(usedays);
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
                String[] strValKeys = {"firstkc,inaa,inbb,incc,inamount,subinamount,iamount" +
                		",blaa,blbb,blcc,blmount,subblmount,bamount,invrest,usedays"}; 
                SubtotalContext stctx = new SubtotalContext();
                String[] strgrpValKeys = {"invname"};
                stctx.setGrpKeys(strgrpValKeys);
                stctx.setSubtotalCols(strValKeys);          //配置要进行合计的字段
                stctx.setIsSubtotal(true);                  //需要小计
                stctx.setLevelCompute(true);
                stctx.setSubtotalName("小计");
                stctx.setTotalNameColKeys("unitname");      //设置合计项显示列位置
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

		}	


