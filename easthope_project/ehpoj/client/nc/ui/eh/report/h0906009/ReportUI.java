package nc.ui.eh.report.h0906009;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;


/**
 * 功能:本月销售计划(集团) </br>
 * 报表节点编码：H090600208
 * 作者:XMQ 时间:2009年09月28日09:22:17
 */

@SuppressWarnings("serial")
public class ReportUI extends ToftPanel {
 
	protected ButtonObject m_boQuery;
	protected ButtonObject m_boPrint;
	protected ReportBaseClass m_report;
	protected QueryConditionClient m_qryDlg;
	protected UFDate ptoday = null;
	protected ClientEnvironment ce = ClientEnvironment.getInstance();

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
			m_qryDlg = createQueryDLG();
		}
		return m_qryDlg;
	}

	/**
	 * 返回一报表模板
	 */
	
	public ReportBaseClass getReportBase() {
		if (m_report == null)
			try {
				m_report = new ReportBaseClass();
				m_report.setName("ReportBase");
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090600208",null, null);
			} catch (Exception ex) {
				System.out
						.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
			}
		return m_report;
	}
	protected QueryConditionClient createQueryDLG()
	{
		QueryConditionClient dlg = new QueryConditionClient();
	    dlg.setTempletID(getCorpPrimaryKey(), "H090600208", null, null);
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
		getReportBase().getBillModel().clearBodyData();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23设置0不显示
		getQryDlg().showModal();
		if (getQryDlg().getResult() == 1) {
			String year = null;
			String month = null;
			getReportBase().getHeadItem("calcpsn").setValue(ce.getUser().getUserName());
			ConditionVO[] condate  = getQryDlg().getConditionVO();
			if(condate != null && condate.length > 1){
				year = condate[0].getValue();
				month = condate[1].getValue();
				getReportBase().getHeadItem("year").setValue(year);
				getReportBase().getHeadItem("month").setValue(month);
				PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(
						PubItf.class.getName());
				SuperVO[] vos = pubItf.calcJTXsjh(new String[]{year,month},0);
				if (vos != null && vos.length > 0) {
					ReportItem[] items=getReportBase().getBody_Items();
		   	  	    for (int j=0;j<items.length;j++)
		   	  	    {
		   	  	         items[j].setDecimalDigits(3);
		   	  	    }	
					this.getReportBase().setBodyDataVO(vos);
					/* 显示合计项 */
		   	  		String[] strsubValKeys = {"bqxlaccount","zljclaccount","zlrzlaccount","zlznslaccount","zlqtlaccount",
		   	  				"zlallaccount","qlrjlaccount","qldjlaccount","qlrylaccount","qldylaccount",
		   	  				"qlaclaccount","qlqtlaccount","qlallaccount","scylaccount","scxxlaccount",
		   	  				"scqtlaccount","scallaccount","qtnnlaccount","qtrnlaccount","qtylaccount",
		   	  				"qttlaccount","qthdlaccount","qtqtlaccount","qtallaccount",
		   	  				"dlznsaccount","dlrjnsaccounbt","dldjnsaccount","dlqtnsaccounbt","dlallaccount"};
			       String[] strgrpValKeys = {"areaname","scqy"};
			        
			        SubtotalContext stctx = new SubtotalContext();
			        stctx.setGrpKeys(strgrpValKeys);
			        stctx.setSubtotalCols(strsubValKeys);	//配置要进行合计的字段
			        stctx.setIsSubtotal(true);	//需要小计
			        stctx.setLevelCompute(false);
			        stctx.setSubtotalName("小计");
			        stctx.setTotalNameColKeys("areaname");	//设置合计项显示列位置
			        stctx.setSumtotalName("合计");	//设置合计项显示名称
			        this.getReportBase().setSubtotalContext(stctx);
			        this.getReportBase().subtotal();
			   	  	this.getReportBase().execHeadLoadFormulas();
		        	this.getReportBase().execTailLoadFormulas();
					updateUI();
				} else {
					showErrorMessage("无满足条件的报表数据!请重新查询!!");
				}
			}
			else
			{
				showErrorMessage("年月必须选择!");
				return;
			}
			
		}
	}
}
