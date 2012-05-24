package nc.ui.eh.report.h0906001;

import java.awt.BorderLayout;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.vo.eh.report.h0906001.JTXstjVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * ����:�ڼ����۱���(����) ����:XMQ ʱ��:2009��09��22��19:22:17
 */

@SuppressWarnings("serial")
public class ReportUI extends ToftPanel {

	public ButtonObject m_boQuery;
	public ButtonObject m_boPrint;
	private ReportBaseClass m_report;
	private QueryConditionClient m_qryDlg;
	private ClientEnvironment ce = ClientEnvironment.getInstance();

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

		this.getClientEnvironment().getDate();
		getReportBase().setNotSortCols(st);
	}

	public ButtonObject[] getBtnAry() {
		return (new ButtonObject[] { m_boQuery, m_boPrint });
	}

	/**
	 * ����һ����ģ��
	 */
	public ReportBaseClass getReportBase() {
		if (m_report == null)
			try {
				m_report = new ReportBaseClass();
				m_report.setName("ReportBase");
				m_report.setTempletID(this.getCorpPrimaryKey(), "H0906001",
						null, null);
			} catch (Exception ex) {
				System.out
						.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
			}
		return m_report;
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

	protected QueryConditionClient createQueryDLG() {
		QueryConditionClient dlg = new QueryConditionClient();
		dlg.setTempletID(this.getCorpPrimaryKey(), "H0906001", null, null);
		dlg.setNormalShow(false);
		return dlg;
	}

	/**
	 * ��ӡ
	 */
	public void onPrint() throws Exception {
		this.getReportBase().previewData();
	}

	/**
	 * ��ѯ
	 */
	public void onQuery() throws Exception {
		this.getReportBase().getHeadItem("calcdate").clearViewData();
		this.getReportBase().getHeadItem("calcpsn").clearViewData();
		this.getReportBase().getBillModel().clearBodyData();
		QueryConditionClient uidialog = getQryDlg();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
		String date = ce.getDate().toString();
		uidialog.setDefaultValue("startdate", date, "");
		uidialog.setDefaultValue("enddate", date, "");
		getQryDlg().showModal();
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
			JTXstjVO[] vos = pubItf.calcJTXstj(new String[]{startDate,endDate});
			if (vos != null && vos.length > 0) {
				ReportItem[] items=getReportBase().getBody_Items();
	   	  	    for (int j=0;j<items.length;j++)
	   	  	    {
	   	  	         items[j].setDecimalDigits(3);
	   	  	    }	
				this.getReportBase().setBodyDataVO(vos);
				/* ��ʾ�ϼ��� */
	   	  		String[] strsubValKeys = {"bqxlaccount","bqsfaccount","bqkcaccount","bqkpwthaccount","zljclaccount","zlrzlaccount","zlznslaccount","zlqtlaccount",
	   	  				"zlallaccount","qlrjlaccount","qldjlaccount","qlrylaccount","qldylaccount",
	   	  				"qlaclaccount","qlqtlaccount","qlallaccount","scylaccount","scxxlaccount",
	   	  				"scqtlaccount","scallaccount","qtnnlaccount","qtrnlaccount","qtylaccount",
	   	  				"qttlaccount","qthdlaccount","qtqtlaccount","qtallaccount","byljaccount",
	   	  				"nljaccount","dlznsaccount","dlrjnsaccounbt","dldjnsaccount","dlqtnsaccounbt",
	   	  				"dlallaccount"};
		       String[] strgrpValKeys = {"areaname","scqy"};
		        
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//����Ҫ���кϼƵ��ֶ�
		        stctx.setIsSubtotal(true);	//��ҪС��
		        stctx.setLevelCompute(false);
		        stctx.setSubtotalName("С��");
		        stctx.setTotalNameColKeys("areaname");	//���úϼ�����ʾ��λ��
		        stctx.setSumtotalName("�ϼ�");	//���úϼ�����ʾ����
		        this.getReportBase().setSubtotalContext(stctx);
		        this.getReportBase().subtotal();
		   	  	this.getReportBase().execHeadLoadFormulas();
	        	this.getReportBase().execTailLoadFormulas();
				updateUI();
			} else {
				this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
			}
		}
	}
}
