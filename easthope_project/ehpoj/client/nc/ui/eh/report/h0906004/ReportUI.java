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
 * ����:��Ҫ����Ʒ�ֽṹ�䶯(����) </br>
 * ����ڵ���룺H090600203
 * ����:XMQ ʱ��:2009��09��27��09:22:17
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
	 * ���ؽڵ����
	 */
	protected String getNodeCode() {
		// TODO Auto-generated method stub
		return "H090600203";
	}
	
	/**
	 * ���ص�ǰ��������
	 */
	protected int getBBType(){
		return Iinvtype.ALLINT;
	}

	/**
	 * ����һ����ģ��
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
			JTXspzjgbdVO[] vos = pubItf.calcJTXspzjgbd(new String[]{startDate,endDate},getBBType());
			if (vos != null && vos.length > 0) {
				this.getReportBase().setBodyDataVO(vos);
				/* ��ʾ�ϼ��� */

		        String[] strgrpValKeys = {JTXspzjgbdVO.SCQUYU,JTXspzjgbdVO.PIANQU};
		        
		        SubtotalContext stctx = new SubtotalContext();
		        stctx.setGrpKeys(strgrpValKeys);
		        
		        //����Ҫ���кϼƵ��ֶ�
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
		        stctx.setIsSubtotal(true);	//��ҪС��
		        stctx.setLevelCompute(true);
		        stctx.setSubtotalName("С��");
		        stctx.setTotalNameColKeys(JTXspzjgbdVO.SCQUYU);	//���úϼ�����ʾ��λ��
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
