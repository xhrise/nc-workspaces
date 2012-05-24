package nc.ui.eh.report.h0906003;

import java.awt.BorderLayout;


import nc.bs.framework.common.NCLocator;

import nc.itf.eh.trade.pub.PubItf;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.vo.eh.report.h0906003.JTXsdbVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * ����:�ڼ��ڼ������Ա�(����) </br>
 * ����ڵ���룺H090600202
 * ����:XMQ ʱ��:2009��09��24��09:22:17
 */

public class ReportUI extends ToftPanel {
 
	public ButtonObject m_boQuery;
	public ButtonObject m_boPrint;
	private ReportBaseClass m_report;
	private QueryConditionClient m_qryDlg;
	private UFDate ptoday = null;
	private ClientEnvironment ce = ClientEnvironment.getInstance();

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

		// �õ���ǰ������
		ptoday = this.getClientEnvironment().getDate();
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
				m_report.setTempletID(this.getCorpPrimaryKey(), "H090600202",
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
		dlg.setTempletID(this.getCorpPrimaryKey(), "H090600202", null, null);
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
		String date = ce.getDate().toString();
		uidialog.setDefaultValue("startdate", date, "");
		uidialog.setDefaultValue("enddate", date, "");
		getQryDlg().showModal();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
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
			JTXsdbVO[] vos = pubItf.calcJTXsdb(new String[]{startDate,endDate});
			if (vos != null && vos.length > 0) {
				this.getReportBase().setBodyDataVO(vos);
				/* ��ʾ�ϼ��� */
	   	  		String[] strsubValKeys = {JTXsdbVO.ALLXL,JTXsdbVO.BQQQXL,JTXsdbVO.BQQQZJ,
	   	  			JTXsdbVO.BQSYTQXL,JTXsdbVO.BQSYTQZJ,
	   	  			JTXsdbVO.BQQNTQXL,JTXsdbVO.BQQNTQZJ,
	   	  			JTXsdbVO.YDYXL,JTXsdbVO.YDYJH,JTXsdbVO.YSYXL,JTXsdbVO.YSYZJ,JTXsdbVO.YQNTYXL,JTXsdbVO.YQNTYZJ,
	   	  			JTXsdbVO.NDNXL,JTXsdbVO.NDNJH,JTXsdbVO.NQNXL,JTXsdbVO.NQNZJ};
		        String[] strgrpValKeys = {JTXsdbVO.SHICHANG,JTXsdbVO.PIANQU};
		        
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        stctx.setSubtotalCols(strsubValKeys);	//����Ҫ���кϼƵ��ֶ�
		        stctx.setIsSubtotal(true);	//��ҪС��
		        stctx.setLevelCompute(true);
		        stctx.setSubtotalName("С��");
		        stctx.setTotalNameColKeys(JTXsdbVO.SHICHANG);	//���úϼ�����ʾ��λ��
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
