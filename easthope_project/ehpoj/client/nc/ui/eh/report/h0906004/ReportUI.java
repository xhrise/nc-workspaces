package nc.ui.eh.report.h0906004;

import java.awt.BorderLayout;



import nc.bs.framework.common.NCLocator;

import nc.itf.eh.trade.pub.PubItf;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.ipub.Iinvtype;

import nc.vo.eh.report.h0906004.JTXspzjgbdVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;


/**
 * 功能:主要销售品种结构变动(集团) </br>
 * 报表节点编码：H090600203
 * 作者:XMQ 时间:2009年09月27日09:22:17
 */

public class ReportUI extends ToftPanel {
 
	protected ButtonObject m_boQuery;
	protected ButtonObject m_boPrint;
	protected ReportBaseClass m_report;
	protected QueryConditionClient m_qryDlg;
	protected UFDate ptoday = null;
	protected ClientEnvironment ce = ClientEnvironment.getInstance();

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

		// 得到当前的日期
		ptoday = this.getClientEnvironment().getDate();
		getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry() {
		return (new ButtonObject[] { m_boQuery, m_boPrint });
	}



	@Override
	public String getTitle() {
		return m_report.getReportTitle();
	}

	@Override
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
			m_qryDlg = createQueryDLG(getNodeCode());
		}
		return m_qryDlg;
	}

	/**
	 * 返回节点编码
	 */
	protected String getNodeCode() {
		// TODO Auto-generated method stub
		return "H090600203";
	}
	
	/**
	 * 返回当前报表类型
	 */
	protected int getBBType(){
		return Iinvtype.ALLINT;
	}

	/**
	 * 返回一报表模板
	 */
	public ReportBaseClass getReportBase() {
		if (m_report == null)
			try {
				m_report = new ReportBaseClass();
				m_report.setName("ReportBase");
				m_report.setTempletID(this.getCorpPrimaryKey(), getNodeCode(),
						null, null);
			} catch (Exception ex) {
				System.out
						.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
			}
		return m_report;
	}
	
	protected QueryConditionClient createQueryDLG(String nodecode) {
		QueryConditionClient dlg = new QueryConditionClient();
		dlg.setTempletID(this.getCorpPrimaryKey(), nodecode, null, null);
		dlg.setNormalShow(false);
		return dlg;
	}

	/**
	 * 打印
	 */
	public void onPrint() throws Exception {
		this.getReportBase().previewData();
	}

	/**
	 * 查询
	 */ 
	public void onQuery() throws Exception {
		this.getReportBase().getHeadItem("calcdate").clearViewData();
		this.getReportBase().getHeadItem("calcpsn").clearViewData();
		this.getReportBase().getBillModel().clearBodyData();
		QueryConditionClient uidialog = getQryDlg();
		String date = ce.getDate().toString();
		uidialog.setDefaultValue("startdate", date, "");
		uidialog.setDefaultValue("enddate", date, ""); 
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		if (getQryDlg().getResult() == 1) {
			String startDate = null;
			String endDate = null;
			getReportBase().getHeadItem("calcdate").setValue(date);
			getReportBase().getHeadItem("calcpsn").setValue(ce.getUser().getUserName());
			ConditionVO[] condate  = getQryDlg().getConditionVO();
			if(condate != null && condate.length > 1){
				startDate = condate[0].getValue();
				endDate = condate[1].getValue();
				 
			}
			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(
					PubItf.class.getName());
			JTXspzjgbdVO[] vos = pubItf.calcJTXspzjgbd(new String[]{startDate,endDate},getBBType());
			if (vos != null && vos.length > 0) {
				this.getReportBase().setBodyDataVO(vos);
				/* 显示合计项 */

		        String[] strgrpValKeys = {JTXspzjgbdVO.SCQUYU,JTXspzjgbdVO.PIANQU};
		        
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        
		        //配置要进行合计的字段
		        switch (getBBType()) {
				case Iinvtype.ALLINT:
					stctx.setSubtotalCols(JTXspzjgbdVO.hjall);	
					break;
				case Iinvtype.ZLINT:
					stctx.setSubtotalCols(JTXspzjgbdVO.hjzl);	
					break;
				case Iinvtype.QLINT:
					stctx.setSubtotalCols(JTXspzjgbdVO.hjqlorqt);	
					break;
				case Iinvtype.SCINT:
					stctx.setSubtotalCols(JTXspzjgbdVO.hjsc);	
					break;
				case Iinvtype.QTINT:
					stctx.setSubtotalCols(JTXspzjgbdVO.hjqlorqt);	
					break;
				default:
					break;
				}
		        stctx.setIsSubtotal(true);	//需要小计
		        stctx.setLevelCompute(true);
		        stctx.setSubtotalName("小计");
		        stctx.setTotalNameColKeys(JTXspzjgbdVO.SCQUYU);	//设置合计项显示列位置
		        stctx.setSumtotalName("合计");	//设置合计项显示名称
		        this.getReportBase().setSubtotalContext(stctx);
		        this.getReportBase().subtotal();
		   	  	this.getReportBase().execHeadLoadFormulas();
	        	this.getReportBase().execTailLoadFormulas();
				updateUI();
			} else {
				this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
			}
		}
	}
}
