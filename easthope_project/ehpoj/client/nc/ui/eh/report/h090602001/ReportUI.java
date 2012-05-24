package nc.ui.eh.report.h090602001;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.report.h090602001.ReportVO;
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
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

/**
 * 
 *功能：公司原料质量总表
 *作者：张志远 
 *日期：2009-9-27  下午16:06:53
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
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090602001",
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
		dlg.setTempletID(this.getCorpPrimaryKey(), "H090602001", null, null);
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
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		ArrayList list = new ArrayList();
		ReportVO[] rvos = null;// 自定义VO
		//合计总吨数
		UFDouble hjzds = new UFDouble("0");
		//理化指标内合计总吨数
		UFDouble lhnzds = new UFDouble("0");
		//理化指标内合计总吨数占总吨数比例
		UFDouble perslhnzdu = new UFDouble("0");
		//理化指标外至收货范围内合计总吨数
		UFDouble betweenlhsh = new UFDouble("0");
		//理化指标外至收货范围内合计总吨数占总吨数比例
		UFDouble persbeweenlhsh = new UFDouble("0");
		//超收货范围经批准合计总吨数
		UFDouble cshzds = new UFDouble("0");
		//超收货范围经批准合计总吨数占总吨数比例
		UFDouble perscshzds = new UFDouble("0");
		
	if (getQryDlg().getResult() == 1) {
		ConditionVO[] period = getQryDlg().getConditionVOsByFieldCode("qudate"); // 查询日期PK
		String pk_period = null; 
		//查询日期
		String querydate = null;
		if (period.length == 0) {
			this.showErrorMessage("请选择查询日期！");
			return;
		}else{
			pk_period = period[0].getValue();
			PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
			if(perVO.getNmonth() < 10){
				querydate = perVO.getNyear()+"-0"+perVO.getNmonth();
			}else{
				querydate = perVO.getNyear()+"-"+perVO.getNmonth();
			}
		}
			//取得采购入库录入中所有的公司
			StringBuffer sql = new StringBuffer()
			.append(" SELECT distinct pk_corp FROM eh_stock_in skin ");
			IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if (all != null && all.size() > 0) {
			for (int i = 0; i < all.size(); i++) {
				HashMap hm = (HashMap) all.get(i);
				String pkCorp = hm.get("pk_corp")==null?"":hm.get("pk_corp").toString();
			StringBuffer sqlamount = new StringBuffer()
			.append(" SELECT bddef.docname docname,bdcorp.unitname unitname, ")
			.append(" skamone.inamount inneramount,skamsec.inamount betweamount,skamthi.inamount okeyouteramount ")
			.append(" FROM(SELECT  ")
			.append(" SUM(skinb.inamount)inamount ")
			.append(" FROM  ")
			.append(" eh_stock_in skin ")
			.append(" ,eh_stock_in_b skinb ")
			.append(" ,eh_stock_checkreport skckrt ")
			.append(" WHERE skin.pk_in = skinb.pk_in ")
			.append(" AND SUBSTR(skin.vsourcebillid,3,20)= skckrt.pk_checkreport ")
			.append(" AND skckrt.resulst = '0' ")
			.append(" AND SUBSTR(skin.indate,1,7) = '"+querydate+"' ")
			.append(" AND NVL(skckrt.tc_flag,'N') = 'N' ")
			.append(" AND skin.vbillstatus = '1' ")
			.append(" AND NVL(skin.dr,'0') = '0' ")
			.append(" AND skin.pk_corp = '"+pkCorp+"' ")
			.append(" )skamone  ")
			.append(" ,(SELECT  ")
			.append(" SUM(skinb.inamount)inamount ")
			.append(" FROM  ")
			.append(" eh_stock_in skin ")
			.append(" ,eh_stock_in_b skinb ")
			.append(" ,eh_stock_checkreport skckrt ")
			.append(" WHERE skin.pk_in = skinb.pk_in ")
			.append(" AND SUBSTR(skin.vsourcebillid,3,20)= skckrt.pk_checkreport ")
			.append(" AND skckrt.resulst in('2','3','4') ")
			.append(" AND SUBSTR(skin.indate,1,7) = '"+querydate+"' ")
			.append(" AND NVL(skckrt.tc_flag,'N') = 'N' ")
			.append(" AND skin.vbillstatus = '1' ")
			.append(" AND NVL(skin.dr,'0') = '0' ")
			.append(" AND skin.pk_corp = '"+pkCorp+"' ")
			.append(" )skamsec  ")
			.append(" ,(SELECT  ")
			.append(" SUM(skinb.inamount)inamount ")
			.append(" FROM  ")
			.append(" eh_stock_in skin ")
			.append(" ,eh_stock_in_b skinb ")
			.append(" ,eh_stock_checkreport skckrt ")
			.append(" WHERE skin.pk_in = skinb.pk_in ")
			.append(" AND SUBSTR(skin.vsourcebillid,3,20)= skckrt.pk_checkreport ")
			.append(" AND skckrt.resulst in('0','2','3','4') ")
			.append(" AND SUBSTR(skin.indate,1,7) = '"+querydate+"' ")
			.append(" AND NVL(skckrt.tc_flag,'N') = 'Y' ")
			.append(" AND skin.vbillstatus = '1' ")
			.append(" AND NVL(skin.dr,'0') = '0' ")
			.append(" AND skin.pk_corp = '"+pkCorp+"' ")
			.append(" )skamthi  ")
			.append(" , bd_corp bdcorp ")
			.append(" ,bd_defdoc bddef ")
			.append(" where  bdcorp.pk_corp = '"+pkCorp+"' ")
			.append(" AND bdcorp.def1 = bddef.pk_defdoc(+) ");
			IUAPQueryBS iUAPQueryBSAmount = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList allAmount = (ArrayList) iUAPQueryBSAmount.executeQuery(sqlamount.toString(), new MapListProcessor());
			String docname = null;//片区
			String unitname = null;//公司名称
			UFDouble invamount = new UFDouble(0); //总吨数
			UFDouble inneramount = new UFDouble(0); //理化内吨数
			UFDouble persinner = new UFDouble(0); //理化内占吨数
			UFDouble betweamount = new UFDouble(0); //收货范围内
			UFDouble persbetwe = new UFDouble(0); //收货范围占吨数
			UFDouble okeyouteramount = new UFDouble(0); //超收经批准
			UFDouble perokeyouter = new UFDouble(0); //超收经批准占吨数
			if (allAmount != null && allAmount.size() > 0) {
			for (int iam = 0; iam < allAmount.size(); iam++) {
				HashMap hmam = (HashMap) allAmount.get(iam);
				//片区
				docname = hmam.get("docname")==null?"":hmam.get("docname").toString();
				//公司名称
				unitname = hmam.get("unitname")==null?"":hmam.get("unitname").toString();
				//理化内吨数
				inneramount = new UFDouble(hmam.get("inneramount")==null?"0":hmam.get("inneramount").toString());
				//理化外收货内吨数
				betweamount = new UFDouble(hmam.get("betweamount")==null?"0":hmam.get("betweamount").toString());
				//超收经批吨数
				okeyouteramount = new UFDouble(hmam.get("okeyouteramount")==null?"0":hmam.get("okeyouteramount").toString());
				//采购总吨数
				invamount = inneramount.add(betweamount).add(okeyouteramount);
				//理化内吨数占总吨数比例
				persinner = inneramount.div(invamount).multiply(100);
				//理化外收货内占总吨数比例
				persbetwe = betweamount.div(invamount).multiply(100);
				//超收经批吨数占总数比例
				perokeyouter = okeyouteramount.div(invamount).multiply(100);
				
				//所有公司采购吨数合计
				hjzds=hjzds.add(invamount);
				//所有公司理化内吨数合计
				lhnzds=lhnzds.add(inneramount);
				//所有公司理化外收货内吨数合计
				betweenlhsh=betweenlhsh.add(betweamount);
				//所有超收经批吨数合计
				cshzds=cshzds.add(okeyouteramount);
				
				ReportVO rvo = new ReportVO();
				rvo.setDocname(docname);
				rvo.setUnitname(unitname);
				rvo.setInvamount(invamount);
				rvo.setInneramount(inneramount);
				rvo.setPersinner(persinner);
				rvo.setBetweamount(betweamount);
				rvo.setPersbetwe(persbetwe);
				rvo.setOkeyouteramount(okeyouteramount);
				rvo.setPerokeyouter(perokeyouter);
				list.add(rvo);
			}
			}
			}
			/**合计行显示的数据*/
			//理化内总合计比例
			perslhnzdu = lhnzds.div(hjzds).multiply(100);
			//理化外收货内总合计比例
			persbeweenlhsh = betweenlhsh.div(hjzds).multiply(100);
			//超收经批总合计比例
			perscshzds = cshzds.div(hjzds).multiply(100);
			ReportVO rvo = new ReportVO();
			rvo.setDocname("");
			rvo.setUnitname("合计");
			rvo.setInvamount(hjzds);
			rvo.setInneramount(lhnzds);
			rvo.setPersinner(perslhnzdu);
			rvo.setBetweamount(betweenlhsh);
			rvo.setPersbetwe(persbeweenlhsh);
			rvo.setOkeyouteramount(cshzds);
			rvo.setPerokeyouter(perscshzds);
			list.add(rvo);
			}

			if(list.size()>0){
                rvos = (ReportVO[]) list.toArray(new ReportVO[0]);
            }          
			if(rvos!=null && rvos.length>0){
				ReportItem [] newItems = null;
				this.getReportBase().addBodyItem(newItems);				
                this.getReportBase().setBodyDataVO(rvos);
                this.getReportBase().execHeadLoadFormulas();
                this.getReportBase().execTailLoadFormulas();
                updateUI();
            }else{
                this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
            }
		}
	}
}
