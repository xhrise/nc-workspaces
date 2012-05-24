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
 * <b>报表模板的抽象类</b> 本报表模板默认有两个按钮，查询和打印
 */
public abstract class AbstractReportUI extends ToftPanel {
    private ButtonObject boQuery; // 查询按钮
    private ButtonObject boPrint; // 打印按钮
    private ReportBaseClass report; // 报表模板
    private QueryConditionClient queryDlg; // 查询对话框
    private SubtotalContext stctx; // 辅助报表合计
    private ClientEnvironment ce;
 
    public AbstractReportUI() { 
	super();
	boQuery = new ButtonObject("查询", "查询报表", 0);
	boPrint = new ButtonObject("打印", "打印报表", 0);
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
     * 查询
     */
    protected void onQuery() {
	// 清楚报表中的数据
	ReportItem[] items = getReport().getHead_Items();
	if (null != items && items.length > 0) {
	    for (int i = 0; i < items.length; i++) {
		items[i].clearViewData();
	    }
	}
	getReport().getBillModel().clearBodyData();
	// 弹出一查询对框
	QueryConditionClient uidialog = getQueryDlg();
	// 对对话框里的值进行每日设置
	setQueryDlgValue(uidialog);
	// 显示查询对话框
	getQueryDlg().showModal();
	if (getQueryDlg().getResult() != 1)
	    return;
	ConditionVO[] conditionVOs = getQueryDlg().getConditionVO();
	SuperVO[] vo = getVos(conditionVOs);
	if (null == vo || vo.length <= 0){
	    this.showErrorMessage("无满足条件的报表数据!请重新查询!!");
	    return;
	}
	//处理报表模板，一般是对表头的设置
	dealReport(getReport());
	
	//VO加入模板中
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
     * 打印
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
     * 返回一查询对话框
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
     * 客户端信息
     */
    public ClientEnvironment getCe() {
	if (null == ce) {
	    ce = ClientEnvironment.getInstance();
	}
	return ce;
    }
    
    

    /**
     * 对报表模板进行设置，一般是表头的设置
     */
    protected void dealReport(ReportBaseClass report) {

    }

    /**
     * 设置查询对话框的默认值
     */
    protected void setQueryDlgValue(QueryConditionClient uidialog) {

    }

    /**
     * 返回节点编码
     */
    protected abstract String getNodeCode();

    /**
     * 返回报表模板中存放的VOS
     * 
     * @param 查询对话框中的参数
     */
    protected abstract SuperVO[] getVos(ConditionVO[] conditionVOs);

    /**
     * 返回SubtotalContext来辅助报表合计
     */
    public SubtotalContext getStctx() {
	return stctx;
    }
}
