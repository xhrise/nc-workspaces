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
 * ����:�������ۼƻ�(����) </br>
 * ����ڵ���룺H090600208
 * ����:XMQ ʱ��:2009��09��28��09:22:17
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
	 * ����һ����ģ��
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
	 * ��ӡ
	 */
	public void onPrint() throws Exception {
		this.getReportBase().previewData();
	}

	/**
	 * ��ѯ
	 */ 
	public void onQuery() throws Exception {
		getReportBase().getBillModel().clearBodyData();
		getReportBase().showZeroLikeNull(true);//add by houcq 2010-12-23����0����ʾ
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
					/* ��ʾ�ϼ��� */
		   	  		String[] strsubValKeys = {"bqxlaccount","zljclaccount","zlrzlaccount","zlznslaccount","zlqtlaccount",
		   	  				"zlallaccount","qlrjlaccount","qldjlaccount","qlrylaccount","qldylaccount",
		   	  				"qlaclaccount","qlqtlaccount","qlallaccount","scylaccount","scxxlaccount",
		   	  				"scqtlaccount","scallaccount","qtnnlaccount","qtrnlaccount","qtylaccount",
		   	  				"qttlaccount","qthdlaccount","qtqtlaccount","qtallaccount",
		   	  				"dlznsaccount","dlrjnsaccounbt","dldjnsaccount","dlqtnsaccounbt","dlallaccount"};
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
					showErrorMessage("�����������ı�������!�����²�ѯ!!");
				}
			}
			else
			{
				showErrorMessage("���±���ѡ��!");
				return;
			}
			
		}
	}
}
