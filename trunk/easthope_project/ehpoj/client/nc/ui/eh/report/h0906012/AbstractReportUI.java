package nc.ui.eh.report.h0906012;

import java.awt.BorderLayout;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.report.SubtotalContext;

/**
 * <b>����ģ��ĳ�����</b> ������ģ��Ĭ����������ť����ѯ�ʹ�ӡ
 */
public abstract class AbstractReportUI extends ToftPanel {
    private ButtonObject boQuery; // ��ѯ��ť
    private ButtonObject boPrint; // ��ӡ��ť
    private ReportBaseClass report; // ����ģ��
    private QueryConditionClient queryDlg; // ��ѯ�Ի���
    private SubtotalContext stctx; // ��������ϼ�
    private ClientEnvironment ce;
 
    public AbstractReportUI() { 
	super();
	boQuery = new ButtonObject("��ѯ", "��ѯ����", 0);
	boPrint = new ButtonObject("��ӡ", "��ӡ����", 0);
	initialize();
    }

    private void initialize() {
	setName("GeneralPane");
	setLayout(new BorderLayout());
	setSize(1024, 768);
	add(getReport(), "Center");
	setButtons(new ButtonObject[] { boQuery, boPrint });
	updateButtons();
	getReport().setShowNO(true);
	int colcount = getReport().getBillTable().getColumnCount();
	String st[] = new String[colcount];
	String skey = "";
	for (int i = 0; i < colcount; i++) {
	    skey = getReport().getBodyShowItems()[i].getKey().trim();
	    st[i] = skey;
	}
	getReport().setNotSortCols(st);
    }

    @Override
    public String getTitle() {
	// TODO Auto-generated method stub
	return getReport().getName();
    }

    @Override
    public void onButtonClicked(ButtonObject bo) {
	try {
	    if (bo == boQuery) {
		onQuery();
	    } else if (bo == boPrint) {
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

    /**
     * ��ѯ
     */
    protected void onQuery() {
	// ��������е�����
	ReportItem[] items = getReport().getHead_Items();
	if (null != items && items.length > 0) {
	    for (int i = 0; i < items.length; i++) {
		items[i].clearViewData();
	    }
	}
	getReport().getBillModel().clearBodyData();
	// ����һ��ѯ�Կ�
	QueryConditionClient uidialog = getQueryDlg();
	// �ԶԻ������ֵ����ÿ������
	setQueryDlgValue(uidialog);
	// ��ʾ��ѯ�Ի���
	getQueryDlg().showModal();
	if (getQueryDlg().getResult() != 1)
	    return;
	ConditionVO[] conditionVOs = getQueryDlg().getConditionVO();
	SuperVO[] vo = getVos(conditionVOs);
	if (null == vo || vo.length <= 0){
	    this.showErrorMessage("�����������ı�������!�����²�ѯ!!");
	    return;
	}
	//������ģ�壬һ���ǶԱ�ͷ������
	dealReport(getReport());
	
	//VO����ģ����
	getReport().setBodyDataVO(vo);
	
	if(getStctx() != null){
	    getReport().setSubtotalContext(getStctx());
	    getReport().subtotal();
	}   
	
	getReport().execHeadLoadFormulas();
	getReport().execTailLoadFormulas();
	updateUI();
    }

    /**
     * ��ӡ
     */
    protected void onPrint() throws Exception {
	this.getReport().previewData();
    }

    protected ReportBaseClass getReport() {
	if (report == null)
	    try {
		report = new ReportBaseClass();
		report.setName("ReportBase");
		report.setTempletID(this.getCorpPrimaryKey(), getNodeCode(),
			null, null);
	    } catch (Exception ex) {
		System.out
			.println("\u57FA\u7C7B:\u672A\u627E\u5230\u62A5\u8868\u6A21\u677F......");
	    }
	return report;
    }

    /**
     * ����һ��ѯ�Ի���
     */
    public QueryConditionClient getQueryDlg() {
	if (queryDlg == null) {
	    queryDlg = new QueryConditionClient();
	    queryDlg.setTempletID(this.getCorpPrimaryKey(), getNodeCode(),
		    null, null);
	    queryDlg.setNormalShow(false);
	}
	return queryDlg;
    }

    /**
     * �ͻ�����Ϣ
     */
    public ClientEnvironment getCe() {
	if (null == ce) {
	    ce = ClientEnvironment.getInstance();
	}
	return ce;
    }
    
    

    /**
     * �Ա���ģ��������ã�һ���Ǳ�ͷ������
     */
    protected void dealReport(ReportBaseClass report) {

    }

    /**
     * ���ò�ѯ�Ի����Ĭ��ֵ
     */
    protected void setQueryDlgValue(QueryConditionClient uidialog) {

    }

    /**
     * ���ؽڵ����
     */
    protected abstract String getNodeCode();

    /**
     * ���ر���ģ���д�ŵ�VOS
     * 
     * @param ��ѯ�Ի����еĲ���
     */
    protected abstract SuperVO[] getVos(ConditionVO[] conditionVOs);

    /**
     * ����SubtotalContext����������ϼ�
     */
    public SubtotalContext getStctx() {
	return stctx;
    }
}
