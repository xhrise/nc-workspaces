package nc.ui.so.so002;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;

import nc.itf.ic.service.IICToSO;

import nc.ui.ml.NCLangRes;
import nc.ui.pf.change.PfUtilUITools;
import nc.ui.pf.pub.PfUIDataCache;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIMenuItem;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillActionListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.file.DocumentManager;
import nc.ui.scm.goldtax.TransGoldTaxDlg;
import nc.ui.scm.plugin.InvokeEventProxy;
import nc.ui.scm.print.BillPrintTool;
import nc.ui.scm.print.IFreshTsListener;
import nc.ui.scm.print.PrintLogClient;
import nc.ui.scm.pub.panel.SetColor;
import nc.ui.scm.pub.report.BillRowNo;
import nc.ui.scm.service.LocalCallService;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.pub.IBatchWorker;
import nc.ui.so.pub.ProccDlg;
import nc.ui.so.pub.ShowToolsInThread;
import nc.ui.so.pub.plugin.SOPluginUI;
import nc.ui.so.so001.SaleOrderBO_Client;

import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.Pfi18nTools;
import nc.vo.pub.pfflow01.BillbusinessVO;
import nc.vo.scm.bd.SmartVODataUtils;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.goldtax.Configuration;
import nc.vo.scm.goldtax.GoldTaxHeadVO;
import nc.vo.scm.goldtax.GoldTaxVO;
import nc.vo.scm.plugin.Action;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.SmartVO;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.scm.service.ServcallVO;
import nc.vo.so.pub.Operlog;
import nc.vo.so.pub.SOCurrencyRateUtil;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.sodispart.SaleDispartVO;
import nc.vo.transfer.IEqualValueDownBill;
import nc.vo.transfer.IEqualValueUpBill;
import nc.vo.transfer.IPriority;
import nc.vo.transfer.UpToDownEqualValueTool;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * <ul>
 * <li>���۷�ƱUI
 * <li>...
 * </ul>
 * <p>
 * <b>�����ʷ����ѡ����</b>
 * <p>
 * XXX�汾����XXX��֧�֡�
 * <p>
 * <p>
 * 
 * @version ���汾��
 * @since ��һ�汾��
 * @author wangyf
 * @time 2007-3-6 ����11:03:07
 */

public class SaleInvoiceUI extends nc.ui.pub.ToftPanel implements
    BillTableMouseListener, nc.ui.pf.query.ICheckRetVO, ILinkApprove,
    ILinkQuery, IFreshTsListener, IInvoiceListPanel, IInvoiceCardPanel,
    ILinkMaintain, ILinkAdd, IBatchWorker,BillActionListener{
  
	private static final long serialVersionUID = 1L;
 
	private boolean iAddButn = false; 

  // ��ǰ����ʾ������LIST����CARD
  public static final int ListShow = 0;

  public static final int CardShow = 1;

  // �̶߳Ի���
  private ProccDlg m_proccdlg = null;

  // ������ʾ״̬
  private int m_iShowState = ListShow;

  // ��ѯ��
  private SaleInvoiceQueryDlg dlgQuery = null;

  // �Ƿ���Ϣ����
  private boolean m_bInMsgPanel = false;

  // ��ť
  private SaleInvoiceBtn m_buttons = null;

  // ��Ƭ����
  private SaleInvoiceCardPanel ivjBillCardPanel = null;

  // ��ǰ����״̬
  private int m_iOperationState = ISaleInvoiceOperState.STATE_BROWSE;

  private PrintLogClient m_PrintLogClient = null;
  // ���۷�Ʊ������
  SaleInvoiceTools st =  new SaleInvoiceTools();

  // ���۷�Ʊ������
  private SaleInvoiceVOCache m_vocache = new SaleInvoiceVOCache();

  // �б����
  private SaleInvoiceListPanel ivjBillListPanel = null;

  // �Ƿ�������ѯ��������ˢ�°�ť�Ŀ�����
  private boolean m_bEverQuery = false;
  
  //���������е����ν��20070917
 //  public Hashtable<String, UFDouble[]> hsSourceMny = new java.util.Hashtable<String, UFDouble[]>();
  
  //��ǣ�ȡ�ɱ����Ƿ���ڴ�����
  private boolean existErrRows = false;
  
  //V55���۷�Ʊ֧�ֶ��ο�����չ
  private InvokeEventProxy pluginproxy = null;
 
  /**
   * ����������������ȡ��������ࡣ
   * <b>����˵��</b>
   * @return
   * @time 2009-1-15 ����04:56:15
   */
  public InvokeEventProxy getPluginProxy(){
    try{
    if(null == this.pluginproxy)
      this.pluginproxy = new InvokeEventProxy("so", SaleBillType.SaleInvoice,
          new SOPluginUI(this, SaleBillType.SaleInvoice));
    }catch(Exception e){
      showErrorMessage("���۷�Ʊ֧�ֶ��ο�����չ����"+ e);
    }
    return this.pluginproxy;
  }
  /**
   * SaleInvoice ������ע�⡣
   */
  public SaleInvoiceUI() {
    super();
    initialize();
  }

  public void setButtonsState() {
    if (getOperationState() == ISaleInvoiceOperState.STATE_BROWSE) {
      setButtonsStateBrowse();
    }
    else if (getOperationState() == ISaleInvoiceOperState.STATE_EDIT) {
      setButtonsStateEdit();
    }
    else if (getOperationState() == ISaleInvoiceOperState.STATE_OPP) {
      setButtonsStateOPP();
    }
    else if (getOperationState() == ISaleInvoiceOperState.STATE_MSGCENTERAPPROVE) {
      setButtonsStateMsgCenterApprove();
    }
    else if (getOperationState() == ISaleInvoiceOperState.STATE_BILLSTATUS) {
      setButtonsStateByBillStatue();
    }
    else if (getOperationState() == ISaleInvoiceOperState.STATE_LINKQUERYBUSITYPE) {
      setButtonsStateByLinkQueryBusitype();
    }
    //  ���ο�����չ
    getPluginProxy().setButtonStatus();
  }

  /**
   * ���������� �������ڣ�(2001-6-1 13:12:36)
   */
  public void onAfterAction(ButtonObject bo) {
    
    SaleinvoiceVO voInv = getVOForAction();
    try {
      PfUtilClient.processAction(bo.getTag(), SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), voInv, voInv.getHeadVO()
              .getPrimaryKey());
      // getID(id)
      showHintMessage(bo.getName()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000067")/* @res "�ɹ���" */);
    }
    catch (Exception e) {
      showErrorMessage(bo.getName()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000069")/* @res "ʧ�ܣ�" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
  }

  /**
   * ������Card����ϸ��µ��ݺ󣬱����б�����ϵ�voͬʱ����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 ����04:31:53
   */
  private void updateCacheVOByList() {
    int iSelectedRow = getBillListPanel().getHeadTable().getSelectedRow();
    if (iSelectedRow < 0)
      return;

    SaleVO hvo = (SaleVO) getBillListPanel().getHeadBillModel()
        .getBodyValueRowVO(iSelectedRow, SaleVO.class.getName());
    SaleinvoiceBVO[] bvos = (SaleinvoiceBVO[]) getBillListPanel()
        .getBodyBillModel().getBodyValueVOs(SaleinvoiceBVO.class.getName());

    SaleinvoiceVO voInvoice = new SaleinvoiceVO();
    voInvoice.setParentVO(hvo);
    voInvoice.setChildrenVO(bvos);

    getVOCache().setSaleinvoiceVO(
        ((SaleVO) voInvoice.getParentVO()).getCsaleid(), voInvoice);

    getBillListPanel().getHeadBillModel().setBodyRowVO(voInvoice.getParentVO(),
        iSelectedRow);
//    try {
//      // ���ñ�ͷ����
//      BillTools.setMnyToModelByCurrency(getBillListPanel().getHeadBillModel(),
//          voInvoice.getParentVO(), iSelectedRow, getCorpPrimaryKey(),
//          "ccurrencytypeid", new String[] {
//              "ntotalsummny", "nstrikemny", "nnetmny"
//          });
//    }
//    catch (Exception e) {
//      SCMEnv.out(e);
//    }

  }

  /**
   * ���ݿ�Ƭ���»���
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 ����04:31:37
   */
  private void updateCacheVOByCard() {

    SaleinvoiceVO voInvoice = (SaleinvoiceVO) getBillCardPanel().getVO();

    SaleVO voHead = voInvoice.getHeadVO();
    UIRefPane ufref = (UIRefPane) getBillCardPanel().getHeadItem("ccalbodyid")
        .getComponent();
    voHead.setAttributeValue("ccalbodyname", ufref.getUITextField().getText());

    // ufref = (UIRefPane) getBillCardPanel().getHeadItem("ccustomerid")
    // .getComponent();
    // voHead.setAttributeValue("ccustomername", ufref.getUITextField()
    // .getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("cdeptid")
        .getComponent();
    voHead.setAttributeValue("cdeptname", ufref.getUITextField().getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("cemployeeid")
        .getComponent();
    voHead.setAttributeValue("cemployeename", ufref.getUITextField().getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("cfreecustid")
        .getComponent();
    voHead.setAttributeValue("cfreecustname", ufref.getUITextField().getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("creceiptcorpid")
        .getComponent();
    voHead.setAttributeValue("creceiptcorpname", ufref.getUITextField()
        .getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("creceiptcustomerid")
        .getComponent();
    voHead.setAttributeValue("creceiptcustomername", ufref.getUITextField()
        .getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("csalecorpid")
        .getComponent();
    voHead.setAttributeValue("csalecorpname", ufref.getUITextField().getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("ctermprotocolid")
        .getComponent();
    voHead.setAttributeValue("ctermprotocolname", ufref.getUITextField()
        .getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("ctransmodeid")
        .getComponent();
    voHead
        .setAttributeValue("ctransmodename", ufref.getUITextField().getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("cwarehouseid")
        .getComponent();
    voHead
        .setAttributeValue("cwarehousename", ufref.getUITextField().getText());

    ufref = (UIRefPane) getBillCardPanel().getHeadItem("vreceiveaddress")
        .getComponent();
    voHead.setAttributeValue("vreceiveaddress", ufref.getUITextField()
        .getText());

    ufref = (UIRefPane) getBillCardPanel().getTailItem("capproveid")
        .getComponent();
    voHead.setAttributeValue("capprovename", ufref.getUITextField().getText());

    ufref = (UIRefPane) getBillCardPanel().getTailItem("coperatorid")
        .getComponent();
    voHead.setAttributeValue("coperatorname", ufref.getUITextField().getText());

    voHead.setAttributeValue("fstatus", voInvoice.getParentVO()
        .getAttributeValue("fstatus"));

    voInvoice.setParentVO(voHead);

    // ԭ��ֻʹ��ID���£����������Ϊһ����
    // �����ĵ���ID����Ϊ�գ���˼������POS���µĴ���
    if (null == getVOCache().getVO_Load(voHead.getCsaleid())) {
      SaleinvoiceVO catchevo =  getVOCache().getVO_NotLoad(getVOCache().getPos());
      if(null == catchevo || null== catchevo.getHeadVO().getCsaleid()){
        //���ݳ���������ɣ���VO���뵽��������
        if (getBillCardPanel().isOutSumMakeInvoice())
          getVOCache().addVO(voInvoice);
        else
        getVOCache().setSaleinvoiceVO(getVOCache().getPos(), voInvoice);
      }else{
      getVOCache().addVO(voInvoice);
      }
    }else {
      getVOCache().setSaleinvoiceVO(voHead.getCsaleid(), voInvoice);
    }
    getVOCache().setPos(getVOCache().findPos(voInvoice.getHeadVO().getCsaleid()));

  }

  private void onPrifit() {

    nc.vo.so.so006.ProfitVO voProfit = new nc.vo.so.so006.ProfitVO();
    if (getShowState() == ListShow) {
      int row = getBillListPanel().getHeadTable().getSelectedRow();
      nc.vo.so.so006.ProfitHeaderVO headVO = new nc.vo.so.so006.ProfitHeaderVO();
      // ��˾ID
      headVO.setPkcorp(getCorpPrimaryKey());
      // �����֯ID
      headVO.setCcalbodyid((String) getBillListPanel().getHeadBillModel()
          .getValueAt(row, "ccalbodyid"));
      // �����֯����
      headVO.setCcalbodyname((String) getBillListPanel().getHeadBillModel()
          .getValueAt(row, "ccalbodyname"));
      // ��������
      headVO.setBilltype(getBillListPanel().getBillType());
      // ����
      // headVO.setCurrencyid(getBillCardPanel().getHeadItem("ccurrencyid").getValue());
      if (getBillListPanel().getBodyTable().getRowCount() > 0) {
        String curID = (String) (getBillListPanel().getBodyBillModel()
            .getValueAt(0, "ccurrencytypeid"));
        headVO.setCurrencyid(curID);
      }
      nc.vo.so.so006.ProfitItemVO[] bodyVOs = new nc.vo.so.so006.ProfitItemVO[getBillListPanel()
          .getBodyBillModel().getRowCount()];
      for (int i = 0; i < bodyVOs.length; i++) {
        nc.vo.so.so006.ProfitItemVO bodyVO = new nc.vo.so.so006.ProfitItemVO();
        // ���ID
        bodyVO.setCinventoryid((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cinventoryid"));
        // �������
        bodyVO.setCode((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cinventorycode"));
        // �������
        bodyVO.setName((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cinventoryname"));
        String gg = (String) getBillListPanel().getBodyBillModel().getValueAt(
            i, "GG");
        gg = gg == null ? "" : gg;
        String xx = (String) getBillListPanel().getBodyBillModel().getValueAt(
            i, "XX");
        xx = xx == null ? "" : xx;
        // ����ͺ�
        bodyVO.setSize(gg + xx);
        // ����
        bodyVO.setCbatchid((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cbatchid"));
        // ����
        bodyVO.setNumber((UFDouble) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "nnumber"));
        // ����
        bodyVO.setNnetprice((UFDouble) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "nnetprice"));

        // yt add 2003-11-22
        bodyVO.setCbodycalbodyid((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cadvisecalbodyid"));
        bodyVO.setCbodycalbodyname((String) getBillListPanel()
            .getBodyBillModel().getValueAt(i, "cadvisecalbodyname"));
        bodyVO.setCbodywarehouseid((String) getBillListPanel()
            .getBodyBillModel().getValueAt(i, "cbodywarehouseid"));
        bodyVO.setCbodywarehousename((String) getBillListPanel()
            .getBodyBillModel().getValueAt(i, "cbodywarehousename"));
        if (getBillListPanel().getBodyBillModel().getValueAt(i, "blargessflag") != null
            && getBillListPanel().getBodyBillModel().getValueAt(i,
                "blargessflag").toString().equals("false"))
          bodyVO.m_blargessflag = new UFBoolean(false);
        else
          bodyVO.m_blargessflag = new UFBoolean(true);
        // ��˰���
        bodyVO.setNmny((UFDouble) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "nmny"));
        bodyVOs[i] = bodyVO;
      }
      voProfit.setParentVO(headVO);
      voProfit.setChildrenVO(bodyVOs);
      // profit.validate();
    }
    else {
      nc.vo.so.so006.ProfitHeaderVO headVO = new nc.vo.so.so006.ProfitHeaderVO();
      // ��˾ID
      headVO.setPkcorp(getCorpPrimaryKey());
      // �����֯ID
      headVO.setCcalbodyid(getBillCardPanel().getHeadItem("ccalbodyid")
          .getValue());
      // �����֯����
      // headVO.setCcalbodyname(getBillCardPanel().getHeadItem("ccalbodyname").getValue());
      UIRefPane ccalbodyid = (UIRefPane) getBillCardPanel().getHeadItem(
          "ccalbodyid").getComponent();
      headVO.setCcalbodyname(ccalbodyid.getRefName());
      // ��������
      headVO.setBilltype(getBillCardPanel().getBillType());
      // ����
      headVO.setCurrencyid(getBillCardPanel().getHeadItem("ccurrencyid")
          .getValue());
      nc.vo.so.so006.ProfitItemVO[] bodyVOs = new nc.vo.so.so006.ProfitItemVO[getBillCardPanel()
          .getRowCount()];
      for (int i = 0; i < bodyVOs.length; i++) {
        nc.vo.so.so006.ProfitItemVO bodyVO = new nc.vo.so.so006.ProfitItemVO();
        // ���ID
        bodyVO.setCinventoryid((String) getBillCardPanel().getBodyValueAt(i,
            "cinventoryid"));
        // �������
        bodyVO.setCode((String) getBillCardPanel().getBodyValueAt(i,
            "cinventorycode"));
        // �������
        bodyVO.setName((String) getBillCardPanel().getBodyValueAt(i,
            "cinventoryname"));
        String gg = (String) getBillCardPanel().getBodyValueAt(i, "GG");
        gg = gg == null ? "" : gg;
        String xx = (String) getBillCardPanel().getBodyValueAt(i, "XX");
        xx = xx == null ? "" : xx;
        // ����ͺ�
        bodyVO.setSize(gg + xx);
        // ����
        bodyVO.setCbatchid((String) getBillCardPanel().getBodyValueAt(i,
            "cbatchid"));
        // ����
        bodyVO.setNumber((UFDouble) getBillCardPanel().getBodyValueAt(i,
            "nnumber"));
        // ����
        bodyVO.setNnetprice((UFDouble) getBillCardPanel().getBodyValueAt(i,
            "nnetprice"));

        // yt add 2003-11-22
        bodyVO.setCbodycalbodyid((String) getBillCardPanel().getBodyValueAt(i,
            "cadvisecalbodyid"));
        bodyVO.setCbodycalbodyname((String) getBillCardPanel().getBodyValueAt(
            i, "cadvisecalbodyname"));
        bodyVO.setCbodywarehouseid((String) getBillCardPanel().getBodyValueAt(
            i, "cbodywarehouseid"));
        bodyVO.setCbodywarehousename((String) getBillCardPanel()
            .getBodyValueAt(i, "cbodywarehousename"));
        if (getBillCardPanel().getBodyValueAt(i, "blargessflag") != null
                && getBillCardPanel().getBodyValueAt(i,
                    "blargessflag").toString().equals("false"))
              bodyVO.m_blargessflag = new UFBoolean(false);
            else
              bodyVO.m_blargessflag = new UFBoolean(true);
        // ��˰���
        bodyVO.setNmny((UFDouble) getBillCardPanel().getBodyValueAt(i, "nmny"));
        bodyVOs[i] = bodyVO;
      }

      voProfit.setParentVO(headVO);
      voProfit.setChildrenVO(bodyVOs);
    }

    // profit.validate();

    try {
      PfUtilClient.processAction(this, ISaleInvoiceAction.Prifit,
          nc.ui.scm.so.SaleBillType.SaleInvoice, getClientEnvironment()
              .getDate().toString(), getBillCardPanel().getVO(), voProfit);
    }
    catch (nc.vo.pub.ValidationException e) {
      showErrorMessage(e.getMessage());
    }
    catch (Exception e) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
          "UPP40060501-000069")/* @res "ʧ�ܣ�" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }

  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.pf.query.ICheckRetVO#getVo()
   */
  public nc.vo.pub.AggregatedValueObject getVo() {
    SaleinvoiceVO voInvoice = null;
    if (getShowState() == ListShow) {
      voInvoice = getBillListPanel().getVO();
    }
    else {
      voInvoice = getBillCardPanel().getVO();
    }

    return voInvoice;
  }

  /**
   * ����ִ�С� �޸ģ����Ӿ����޸��жϵĴ���ͨ�������쳣�����ñ�־���� �������ڣ�(2001-6-1 13:12:36) �޸�����
   * (2003-8-20) �
   */
  private void doUnApproveWork() {

    java.util.HashMap hsvos = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-000055")/*
                                                       * @res "���ڽ�������ǰ��׼��..."
                                                       */);
    try {
      hsvos = getBillListPanel().getApproveSaleInvoiceVOs();// ("SoUnApprove");
      if (hsvos == null || hsvos.size() <= 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000056")/* @res "��ѡ��������ķ�Ʊ��" */);
        return;
      }
    }
    catch (Exception ev) {
      showErrorMessage(ev.getMessage());
      return;
    }

    int max = m_proccdlg.getUIProgressBar1().getMaximum();
    int maxcount = hsvos.size();

    // long s1 = 0;
    // long s = System.currentTimeMillis();

    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-000057")/*
                                                       * @res "��ʼ����..."
                                                       */);

    SaleinvoiceVO saleinvoice = null;
    java.util.Iterator iter = hsvos.keySet().iterator();
    int count = 0;
    boolean isnext = true;

    while (iter.hasNext()) {
      Object key = "";
      if (isnext)
        key = iter.next();
      // Object key = iter.next();
      saleinvoice = (SaleinvoiceVO) hsvos.get(key);
      if (saleinvoice == null) {
        isnext = true;
        continue;
      }

      // saleinvoice.setCuruserid(getClientEnvironment().getUser()
      // .getPrimaryKey());

      ShowToolsInThread.showStatus(m_proccdlg, new Integer(
          (int) (max * (1.0 * count / maxcount))));
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000058", null,
              new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              }));
      // ShowToolsInThread.showMessage(proccdlg, "��������Ʊ...["
      // + ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
      // + "]");
      try {
        if (m_proccdlg.isInterrupted())
          break;
        if (onUnApprove(saleinvoice)) {

          hSuccess.put(key, saleinvoice);
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000059", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]����ɹ���", "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]����ɹ���");

        }
        else {
          hSuccess.put(key, saleinvoice);
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000060", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]" + "��������ѱ��û�ȡ����",
          // "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]"
          // + "��������ѱ��û�ȡ����");
        }
      }
      catch (Exception e) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000061", null, new String[] {
              ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += e.getMessage();
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);
        // ShowToolsInThread.showMessage(proccdlg, " ", "��Ʊ["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]����ʧ�ܣ�" + e.getMessage());
        if (m_proccdlg.getckHint().isSelected()) {
          sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000061", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          if (ShowToolsInThread.showYesNoDlg(m_proccdlg, sMsg
              + e.getMessage()
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
                  "UPP40060501-000062")/*
                                         * @res "�Ƿ�����������µķ�Ʊ��"
                                         */) == MessageDialog.ID_YES) {
            continue;
          }
          else {
            m_proccdlg.Interrupt();
            break;
          }
        }

      }
      finally {
        count++;
      }

      isnext = true;
    }

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(max));

    if (m_proccdlg.isInterrupted()) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000042")/*
                                                                       * @res
                                                                       * "����������û��жϣ�"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000044")/*
                                                                       * @res
                                                                       * "�������������"
                                                                       */);
    }

    try {
      Thread.sleep(500);
    }
    catch (Exception ex) {

    }

    if (hSuccess.size() > 0) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                       * @res
                                                                       * "���ڸ��½�������..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  protected java.util.HashMap getDeleteSaleInvoiceVOs()
      throws nc.vo.pub.ValidationException {
    if (getShowState() == ListShow) {
      int[] selrow = getBillListPanel().getHeadTable().getSelectedRows();

      if (selrow == null || selrow.length <= 0)
        return null;
      SaleinvoiceVO[] vos = new SaleinvoiceVO[selrow.length];
      java.util.HashMap reobj = new java.util.HashMap();
      java.util.HashMap hs = new java.util.HashMap();
      String csaleid = null;
      for (int i = 0; i < selrow.length; i++) {

        UFDouble nstrikemny = null;
        nstrikemny = (UFDouble) (getBillListPanel().getHeadBillModel()
            .getValueAt(selrow[i], "nstrikemny"));
        if (nstrikemny != null && nstrikemny.doubleValue() != 0) {
          continue;
        }

        csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(
            selrow[i], "csaleid");
        vos[i] = getVOCache().getVO_Load(csaleid);

        if (vos[i] == null)
          return null;
        vos[i].setOldSaleOrderVO(vos[i]);
        reobj.put(new Integer(selrow[i]), vos[i]);
        if (vos[i].getChildrenVO() == null
            || vos[i].getChildrenVO().length <= 0)
          hs.put(((SaleVO) vos[i].getParentVO()).getCsaleid(), vos[i]);
      }
      if (hs.size() <= 0)
        return reobj;
      String[] hvoids = (String[]) hs.keySet().toArray(new String[hs.size()]);
      try {
        for (int i = 0; i < hvoids.length; i++) {
          SaleinvoiceBVO[] singlebvos = SaleinvoiceBO_Client
              .queryBodyData(hvoids[i]);
          ((SaleinvoiceVO) hs.get(hvoids[i])).setChildrenVO(singlebvos);
          ((SaleinvoiceVO) hs.get(hvoids[i]))
              .setOldSaleOrderVO(((SaleinvoiceVO) hs.get(hvoids[i])));
          // vos[i].setChildrenVO(singlebvos);
        }
      }
      catch (Exception ex) {
        SCMEnv.out(ex.getMessage());
        return null;
      }
      return reobj;
    }
    return null;
  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.pf.query.ICheckRetVO#getVo()
   */
  private SaleinvoiceVO getVOForAction() {
    SaleinvoiceVO voInvoice = null;
    if (getShowState() == ListShow) {
      voInvoice = getBillListPanel().getVOForAction();
    }
    else {
      voInvoice = getBillCardPanel().getVOForAction();
    }

    if(voInvoice != null){
    	SaleinvoiceBVO bodyVO[] = voInvoice.getBodyVO();
    	if(null != bodyVO && bodyVO.length > 0){
    		for(int i = 0,iloop = bodyVO.length; i < iloop; i++){
    			if(null == bodyVO[i].getPk_corp()|| bodyVO[i].getPk_corp().trim().length() == 0) 
            bodyVO[i].setPk_corp(voInvoice.getHeadVO().getPk_corp());
    		}
    	}
    }
    return voInvoice;
  }

  /**
   * ������״̬���°�ť״̬
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b> iState ��ť״̬
   * 
   * @return
   * <p>
   * @author wangyf
   * @time 2007-3-6 ����10:33:09
   */

  private void setButtonsStateByBillStatue() {

    getBtns().m_boModify.setEnabled(false);

    switch (getBillCardPanel().getBillStatus()) {
      case BillStatus.AUDIT: {
        getBtns().m_boApprove.setEnabled(false);
        getBtns().m_boDocument.setEnabled(true);
        getBtns().m_boOrderQuery.setEnabled(true);
        // setImageType(IMAGE_AUDIT);
        getBtns().m_boSendAudit.setEnabled(false);
        break;
      }
      case BillStatus.FREE: {
        getBtns().m_boAction.setEnabled(true);
        getBtns().m_boApprove.setEnabled(true);
        getBtns().m_boDocument.setEnabled(false);
        getBtns().m_boOrderQuery.setEnabled(false);
        // setImageType(IMAGE_NULL);
        getBtns().m_boSendAudit.setEnabled(true);
        break;
      }
      case BillStatus.BLANKOUT: {
        getBtns().m_boAction.setEnabled(false);
        getBtns().m_boAssistant.setEnabled(false);
        getBtns().m_boPrint.setEnabled(false);
        getBtns().m_boDocument.setEnabled(false);
        getBtns().m_boOrderQuery.setEnabled(false);
        getBtns().m_boSendAudit.setEnabled(false);

        break;
      }
    }

    updateButtons();

  }

  private void doApproveWork() {
    java.util.HashMap hsvos = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-000045")/*
                                                       * @res "���ڽ�������ǰ��׼��..."
                                                       */);
    try {
      hsvos = getBillListPanel().getApproveSaleInvoiceVOs();// ("APPROVE");
      if (hsvos == null || hsvos.size() <= 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000046")/* @res "��ѡ��������ķ�Ʊ��" */);
        return;
      }
    }
    catch (Exception ev) {
      showErrorMessage(ev.getMessage());
      return;
    }

    int max = m_proccdlg.getUIProgressBar1().getMaximum();
    int maxcount = hsvos.size();

    // long s1 = 0;
    // long s = System.currentTimeMillis();

    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-000047")/*
                                                       * @res "��ʼ����..."
                                                       */);

    SaleinvoiceVO saleinvoice = null;
    java.util.Iterator iter = hsvos.keySet().iterator();
    int count = 0;

    while (iter.hasNext()) {
      Object key = iter.next();
      saleinvoice = (SaleinvoiceVO) hsvos.get(key);
      ShowToolsInThread.showStatus(m_proccdlg, new Integer(
          (int) (max * (1.0 * count / maxcount))));
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000048", null,
              new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              }));
      // ShowToolsInThread.showMessage(proccdlg, "����������Ʊ...["
      // + ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
      // + "]");
      saleinvoice
          .setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      try {
        if (m_proccdlg.isInterrupted())
          break;

        if (onApprove(saleinvoice)) {

          hSuccess.put(key, saleinvoice);
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000049", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]�����ɹ���", "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]�����ɹ���");
          // }

        }
        else {
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000051", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]" + "���������Ա��û�ȡ����",
          // "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]"
          // + "���������Ա��û�ȡ����");
        }

      }
      catch (Exception e) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000052", null, new String[] {
              ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += e.getMessage();
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);
        // ShowToolsInThread.showMessage(proccdlg, " ", "��Ʊ["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]����ʧ�ܣ�" + e.getMessage());
        if (m_proccdlg.getckHint().isSelected()) {
          sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000052", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          if (ShowToolsInThread.showYesNoDlg(m_proccdlg, e.getMessage()
              + "\n"
              + sMsg
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
                  "UPP40060501-000053")/*
                                         * @res "�Ƿ�����������µķ�Ʊ��"
                                         */) == MessageDialog.ID_YES) {
            continue;
          }
          else {
            m_proccdlg.Interrupt();
            break;
          }
        }
      }
      finally {
        count++;
      }
    }

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(max));

    if (m_proccdlg.isInterrupted()) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000041")/*
                                                                       * @res
                                                                       * "�����������û��жϣ�"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000043")/*
                                                                       * @res
                                                                       * "��������������"
                                                                       */);
    }

    try {
      Thread.sleep(500);
    }
    catch (Exception ex) {

    }

    if (hSuccess.size() > 0) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                       * @res
                                                                       * "���ڸ��½�������..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  /**
   * ���ɶԳ巢Ʊ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-19 ����03:48:03
   */
  private void onOpposeAct() {
    SaleinvoiceVO voOld = null;
    if (getShowState() == ListShow) {
      if (getBillListPanel().getHeadBillModel().getRowCount() == 0)
        return;
      int iSelect = getBillListPanel().getHeadTable().getSelectedRow();

      voOld = (SaleinvoiceVO) getBillListPanel().getBillValueVO(iSelect,
          SaleinvoiceVO.class.getName(), SaleVO.class.getName(),
          SaleinvoiceBVO.class.getName());
    }
    else {
      voOld = getBillCardPanel().getVO();
    }

    // ���ý���
    SaleinvoiceVO voOpp = null;
    try {
      voOpp = SaleInvoiceTools.getOppVO(voOld);
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, "", e.getMessage());
      return;
    }
    if (voOpp == null) {
      return;
    }

    if (getShowState() == ListShow) {
      remove(getBillListPanel());
      add(getBillCardPanel(), "Center");
    }

    // ���ý��桢���뻺��
    getBillCardPanel().setPanelWhenOPP(voOpp);
//  getVOCache().addVO(voOpp);
//  getVOCache().rollToLastPos();

    setShowState(CardShow);
    setButtonsStateBrowse();
    setOperationState(ISaleInvoiceOperState.STATE_OPP);
    setButtonsStateOPP();

    updateUI();
  }

  /**
   * ���״̬��ť
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-6 ����04:30:53
   */
  private void setButtonsStateBrowse() {

    // �Ƿ���VO��ѡ��
    boolean haveVOSelected = false;
    SaleinvoiceVO voFromPanel = null;
    if (getShowState() == ListShow) {
      voFromPanel = getBillListPanel().getVO();
    }
    else {
      if (getVOCache().isEmpty()) {
        voFromPanel = null;
      }
      else {
        voFromPanel = getBillCardPanel().getVO();
      }
    }
    haveVOSelected = !(voFromPanel == null);

    // �Ƿ񻺴�������δ����ĵ���
    boolean haveNewBill = false;
    if (haveVOSelected && voFromPanel.getHeadVO().isNew()) {
      // ֻ�����Ч��
      haveNewBill = true;
    }
    else {
      int iLen = getVOCache().getSize();
      for (int i = 0; i < iLen; i++) {
        if (getVOCache().getVO_NotLoad(i).getHeadVO().isNew()) {
          haveNewBill = true;
          break;
        }
      }
    }

    // ҵ������
    getBtns().m_boBusiType.setEnabled(!haveNewBill);

    // ����
    getBtns().m_boAdd.setEnabled(!haveNewBill);

    // ����
    getBtns().m_boSave.setEnabled(false);

    // ά��
    // �޸ģ�ȡ����ԭ����������ɾ����ԭ�����ϣ�������ת�����ϲ���Ʊ�������ϲ�
    getBtns().m_boMaintain.setEnabled(haveVOSelected);
    int iBillStatus = -1;
    {
      if (haveVOSelected) {
        iBillStatus = voFromPanel.getHeadVO().getFstatus();
      }
      if (haveVOSelected
          && (iBillStatus == BillStatus.NOPASS
              || iBillStatus == BillStatus.FREE)) {
        getBtns().m_boModify.setEnabled(true);
      // modify by fengjb 20080918 ������״̬��ť���Ƹı�
      }else if(iBillStatus == BillStatus.AUDITING){
        //�Ƿ���������
        boolean isApprove = voFromPanel.getHeadVO().getCapproveid() == null? false:true;
        getBtns().m_boModify.setEnabled(!isApprove);
      }
      else {
        getBtns().m_boModify.setEnabled(false);
      }
      getBtns().m_boCancel.setEnabled(false);
      // �ѱ�����ĵ��ݣ�״̬������ɾ������
      //modify by fengjb V55 ������״̬���ݲ�����ɾ��
      if (haveVOSelected
          && !voFromPanel.getHeadVO().isNew()
          && (iBillStatus == BillStatus.NOPASS
               || iBillStatus == BillStatus.FREE)) {
        getBtns().m_boBlankOut.setEnabled(true);
      }
      else {
        getBtns().m_boBlankOut.setEnabled(false);
      }
      // �����ǰ����IDΪ�գ������ת������
      // if (getShowState() == ListShow) {
      if (haveVOSelected && voFromPanel.getHeadVO().isNew()) {
        getBtns().m_boCancelTransfer.setEnabled(true);
      }
      else {
        getBtns().m_boCancelTransfer.setEnabled(false);
      }
      // }
      // else {
      // getBtns().m_boCancelTransfer.setEnabled(false);
      // }

//      if (getShowState() == CardShow) {
        getBtns().m_boUnite.setEnabled(false);
        getBtns().m_boUniteCancel.setEnabled(false);
//      }
//      else {
//        // �Գ����ɲ�����
//        if (!haveVOSelected
//            || voFromPanel.getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN) {
//          getBtns().m_boUnite.setEnabled(false);
//          getBtns().m_boUniteCancel.setEnabled(false);
//        }
//        else {
//          // ֻ�з�Ʊ�ܽ��>0 �ſɽ��г����
//         if(voFromPanel.getHeadVO().getFstatus() == BillStatus.FREE && voFromPanel.getHeadVO().isLgtZero() && !voFromPanel.getHeadVO().isStrike()){
//        	 getBtns().m_boUnite.setEnabled(true);
//         }else{
//        	 getBtns().m_boUnite.setEnabled(false);
//         }
//
//    	  if(voFromPanel.getHeadVO().getFstatus() == BillStatus.FREE && voFromPanel.getHeadVO().isLgtZero() && voFromPanel.getHeadVO().isStrike()){
//    		  getBtns().m_boUniteCancel.setEnabled(true);
//    	  }else{
//    		  getBtns().m_boUniteCancel.setEnabled(false);
//    	  }
//        }
//      }
    }
    if(!getBtns().m_boModify.isEnabled() && !getBtns().m_boCancel.isEnabled() && !getBtns().m_boBlankOut.isEnabled() 
    		&& !getBtns().m_boCancelTransfer.isEnabled() && !getBtns().m_boUnite.isEnabled() && !getBtns().m_boUniteCancel.isEnabled()){
    	getBtns().m_boMaintain.setEnabled(false);
    }

    // �в���
    // ���У�ɾ�У������У����ӣ��������У�ճ����
    getBtns().m_boLineOper.setEnabled(false);
    //��������
    getBtns().m_boRefAdd.setEnabled(false);
    //ȡ�ɱ���
    getBtns().m_boFetchCost.setEnabled(false);

    // ����
    getBtns().m_boApprove.setEnabled(haveVOSelected
        && !voFromPanel.getHeadVO().isNew()
        && iBillStatus != BillStatus.AUDIT);

    // ִ��
    // ��������
    getBtns().m_boAction.setEnabled(haveVOSelected);
    {
      if (haveVOSelected) {
	    boolean bCanSendAudit = false;
	    try{
	    	bCanSendAudit = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(
	            SaleBillType.SaleInvoice, voFromPanel.getHeadVO().getCbiztype(),
	            // NO_BUSINESS_TYPE,
	            getClientEnvironment().getCorporation().getPk_corp(),
	            getClientEnvironment().getUser().getPrimaryKey());
	    }catch(Exception e){
	    	e.printStackTrace();
	    }

        if (voFromPanel.getHeadVO().isNew()) {
          getBtns().m_boSendAudit.setEnabled(bCanSendAudit);
          getBtns().m_boUnApprove.setEnabled(false);
        }
        else {
          if (BillStatus.FREE == iBillStatus || BillStatus.NOPASS == iBillStatus) {
            getBtns().m_boSendAudit.setEnabled(bCanSendAudit);
            getBtns().m_boUnApprove.setEnabled(false);
          }
          else {
            getBtns().m_boSendAudit.setEnabled(false);
            getBtns().m_boUnApprove.setEnabled(true);
          }
        }
      }
    }

    // ��ѯ
    getBtns().m_boQuery.setEnabled(!haveNewBill);

    // ���
    // ˢ�£����ӣ�����λ����ҳ����ҳ��ԭ����һҳ������ҳ��ԭ����һҳ����ĩҳ��ȫѡ��ȫ��
    getBtns().m_boBrowse.setEnabled(true);
    {
      getBtns().m_boRefresh.setEnabled(m_bEverQuery && !haveNewBill);
      // ��ĩ����ҳ
      getBtns().m_boLocal.setEnabled(!getVOCache().isEmpty());
      if (getShowState() == CardShow) {
        getBtns().m_boFirst.setEnabled(!getVOCache().isEmpty());
        getBtns().m_boNext.setEnabled(!getVOCache().isEmpty());
        getBtns().m_boPrev.setEnabled(!getVOCache().isEmpty());
        getBtns().m_boLast.setEnabled(!getVOCache().isEmpty());

        if (getVOCache().getPos() == 0) {
          getBtns().m_boFirst.setEnabled(false);
          getBtns().m_boPrev.setEnabled(false);
        }
        if (getVOCache().getPos() == getVOCache().getSize() - 1) {
          getBtns().m_boNext.setEnabled(false);
          getBtns().m_boLast.setEnabled(false);
        }
      }
      else {
        getBtns().m_boFirst.setEnabled(false);
        getBtns().m_boNext.setEnabled(false);
        getBtns().m_boPrev.setEnabled(false);
        getBtns().m_boLast.setEnabled(false);
      }
      if (getShowState() == ListShow) {
        if (getVOCache().isEmpty()) {
          getBtns().m_boSelectAll.setEnabled(false);
          getBtns().m_boUnSelectAll.setEnabled(false);
        }
        else {
          int selectedNum = getBillListPanel().getHeadTable()
              .getSelectedRowCount();
          getBtns().m_boSelectAll.setEnabled(selectedNum != getVOCache()
              .getSize());
          getBtns().m_boUnSelectAll.setEnabled(selectedNum > 0);
        }
      }
      else {
        getBtns().m_boSelectAll.setEnabled(false);
        getBtns().m_boUnSelectAll.setEnabled(false);
      }
    }

    // �л����޻���ʱ�����е��ݱ�ѡ��ʱ
    if (getShowState() == CardShow) {
      getBtns().m_boCard.setName(NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000464")/*
                                               * @res "�б���ʾ"
                                               */);
      getBtns().m_boCard.setHint(NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000464"));
    }
    else {
      getBtns().m_boCard.setName(NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000463")/*
                                               * @res "��Ƭ��ʾ"
                                               */);
      getBtns().m_boCard.setHint(NCLangRes.getInstance().getStrByID(
              "SCMCOMMON", "UPPSCMCommon-000463"));
    }
    getBtns().m_boCard.setEnabled(getVOCache().isEmpty() || haveVOSelected);

    // ��ӡ
    // Ԥ������ӡ���ϲ���ʾ
    getBtns().m_boPrintManage.setEnabled(haveVOSelected);
    {
      getBtns().m_boPreview.setEnabled(true);
      getBtns().m_boPrint.setEnabled(true);
      if(getShowState() == CardShow) getBtns().m_boBillCombin.setEnabled(true);
      else getBtns().m_boBillCombin.setEnabled(false);
    }

    // ��������
    // ���ɶԳ巢Ʊ������˰���ĵ�����
    getBtns().m_boAssistFunction.setEnabled(true);
    {
      if (iBillStatus == BillStatus.AUDIT) {
        getBtns().m_boOpposeAct.setEnabled(true);
      }
      else {
        getBtns().m_boOpposeAct.setEnabled(false);
      }
      if (!haveVOSelected || voFromPanel.getHeadVO().isNew()) {
        getBtns().m_boSoTax.setEnabled(false);
      }
      else {
        getBtns().m_boSoTax.setEnabled(true);
      }
      getBtns().m_boDocument.setEnabled(true);
      // �������
//      Object cfreezeid = getBillCardPanel().getBodyValueAt(
//          getBillCardPanel().getBillTable().getSelectedRow(), "cfreezeid");
      // if (cfreezeid != null && cfreezeid.toString().trim().length() != 0) {
      // // ���ݵ���״̬���õ���
      // getBtns().m_boStockLock.setEnabled(false);
      // }
      // else {
      // if (getBillCardPanel().getBillStatus() == BillStatus.AUDIT)
      // getBtns().m_boStockLock.setEnabled(true);
      // else
      // getBtns().m_boStockLock.setEnabled(false);
      // }
    }

    // ������ѯ
    // ���飬������ʾ/���أ�ȡ��ԭ��������ť�����ܺͶ�����һ�£���������״̬��
    // �ͻ���Ϣ����Ʊִ��������ͻ����ã�ë��Ԥ��
    getBtns().m_boAssistant.setEnabled(haveVOSelected);
    {
      if(isInMsgPanel()) getBtns().m_boOrderQuery.setEnabled(true);
      else getBtns().m_boOrderQuery.setEnabled(haveVOSelected
          && !voFromPanel.getHeadVO().isNew());
      getBtns().m_boATP.setEnabled(true);
      getBtns().m_boAuditFlowStatus.setEnabled(true);
      getBtns().m_boCustInfo.setEnabled(true);
      getBtns().m_boExecRpt.setEnabled(true);
      getBtns().m_boCustCredit.setEnabled(true);
      getBtns().m_boPrifit.setEnabled(true);
    }

    updateButtons();
  }

  /**
   * ���õ���״̬�� �������ڣ�(2001-6-13 15:17:39)
   * 
   * @param iState
   *          int ����״̬ iOppState �Գ��־
   */
  public void setStateByBillStatus() {
    int iState = getBillListPanel().getBillStatus();
    switch (iState) {
      case BillStatus.FREE: {
        getBtns().m_boBlankOut.setEnabled(true);
        getBtns().m_boModify.setEnabled(true);
        getBtns().m_boApprove.setEnabled(true);
        getBtns().m_boUnApprove.setEnabled(false);
        // getBtns().m_boAfterAction.setEnabled(false);
        // getBtns().m_boStockLock.setEnabled(false);
        getBtns().m_boDocument.setEnabled(true);
        getBtns().m_boOrderQuery.setEnabled(true);
        getBtns().m_boOrderQuery.setEnabled(true);
        // strState = "FREE";
        break;
      }
      case BillStatus.AUDIT: {
        getBtns().m_boBlankOut.setEnabled(false);
        getBtns().m_boApprove.setEnabled(false);
        getBtns().m_boUnApprove.setEnabled(true);
        getBtns().m_boModify.setEnabled(false);
        // getBtns().m_boAfterAction.setEnabled(true);
        // getBtns().m_boStockLock.setEnabled(false);
        getBtns().m_boDocument.setEnabled(true);
        getBtns().m_boOrderQuery.setEnabled(true);
        getBtns().m_boOrderQuery.setEnabled(false);
        // setImageType(IMAGE_AUDIT);
        // strState = "AUDIT";
        break;
      }
      case BillStatus.BLANKOUT: {
        getBtns().m_boModify.setEnabled(false);
        getBtns().m_boAction.setEnabled(false);
        getBtns().m_boAssistant.setEnabled(false);
        // getBtns().m_boAfterAction.setEnabled(false);
        getBtns().m_boPrint.setEnabled(false);
        // getBtns().m_boStockLock.setEnabled(false);
        getBtns().m_boDocument.setEnabled(false);
        getBtns().m_boOrderQuery.setEnabled(false);
        getBtns().m_boOrderQuery.setEnabled(false);
        // Ԥ�����û�
        getBtns().m_boPreview.setEnabled(false);
        // ������״̬�û�
        getBtns().m_boAuditFlowStatus.setEnabled(false);
        // setImageType(IMAGE_CANCEL);
        // strState = "BLANKOUT";
        break;
      }
        // ��ӡ�����������״̬
      case BillStatus.AUDITING: {
        getBtns().m_boBlankOut.setEnabled(false);
        getBtns().m_boApprove.setEnabled(true);
        getBtns().m_boUnApprove.setEnabled(false);
        // getBtns().m_boStockLock.setEnabled(false);
        // getBtns().m_boAction.setEnabled(false);
        // getBtns().m_boAfterAction.setEnabled(false);
        getBtns().m_boSave.setEnabled(false);
        getBtns().m_boModify.setEnabled(false);
        // setImageType(IMAGE_APPROVING);
        // strState = "AUDITING";
        break;
      }// ��ӡ�����δͨ����״̬
      case BillStatus.NOPASS: {
        getBtns().m_boBlankOut.setEnabled(true);
        getBtns().m_boModify.setEnabled(true);
        getBtns().m_boSave.setEnabled(false);
        getBtns().m_boCancel.setEnabled(false);
        getBtns().m_boApprove.setEnabled(true);
        getBtns().m_boUnApprove.setEnabled(false);
        // getBtns().m_boAfterAction.setEnabled(false);
        // getBtns().m_boStockLock.setEnabled(false);
        getBtns().m_boDocument.setEnabled(true);
        getBtns().m_boOrderQuery.setEnabled(true);
        getBtns().m_boOrderQuery.setEnabled(true);

        // setImageType(IMAGE_APPROVEANDFAIL);
        // strState = "NOPASS";
        break;
      }
    }

    int selectRow = getBillListPanel().getHeadTable().getSelectedRow();
    int iOppStatus = 0;
    if (selectRow > -1) {
      // ����״̬
      if (getBillListPanel().getHeadItem("fcounteractflag") != null
          && getBillListPanel().getHeadBillModel().getValueAt(selectRow,
              "fcounteractflag") != null
          && getBillListPanel().getHeadItem("fcounteractflag").converType(
              getBillListPanel().getHeadBillModel().getValueAt(selectRow,
                  "fcounteractflag")) != null) {
        iOppStatus = Integer.parseInt(getBillListPanel().getHeadItem(
            "fcounteractflag").converType(
            getBillListPanel().getHeadBillModel().getValueAt(selectRow,
                "fcounteractflag")).toString());
      }
    }
    // �������ѶԳ嵥�ݣ����������޶�
    if (iState == BillStatus.AUDIT && iOppStatus == 1) {
      getBtns().m_boUnApprove.setEnabled(false);
    }
    // �����ҶԳ���Ϊ�������򵥾��ܶԳ�
    if (iState == BillStatus.AUDIT && iOppStatus == 0) {
      getBtns().m_boOpposeAct.setEnabled(true);
    }
    else {
      getBtns().m_boOpposeAct.setEnabled(false);
    }

  }

  /**
   * ��ʼ���ࡣ
   */
  private void setButtonsStateMsgCenterApprove() {
    getBtns().m_boAssistant.setEnabled(true);
    getBtns().m_boDocument.setEnabled(true);
    getBtns().m_boAuditFlowStatus.setEnabled(true);
    getBtns().m_boApprove.setEnabled(true);
    getBtns().m_boOrderQuery.setEnabled(true);
    
    updateButtons();
  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.so.pub.IBatchWorker#doThreadWork(int)
   */
  public void doThreadWork(String WorkName) {
    if (ISaleInvoiceAction.Approve.equals(WorkName)) {
      doApproveWork();
    }
    else if (ISaleInvoiceAction.UnApprove.equals(WorkName)) {
      doUnApproveWork();
    }
    else if (ISaleInvoiceAction.Unite.equals(WorkName)) {
      batchAutoUnit();
    }
    else if (ISaleInvoiceAction.UnUnite.equals(WorkName)) {
      batchCancelUnit();
    }
    else if (ISaleInvoiceAction.BlankOut.equals(WorkName)) {
      doDeleteWork();
    }

  }

  /**
   * �����������������۷�Ʊɾ������ʵ�֡�
   * <b>����˵��</b>
   * @author fengjb
   * @time 2008-11-26 ����10:18:41
   */
  private void onBlankOut() {
    
    if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000039"), nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UCH002")) != MessageDialog.ID_YES)
      return;
    //���Ҫɾ����VO
    SaleinvoiceVO voInvoice = (SaleinvoiceVO) getVOForAction();
   //��Ʊ�ϲ���Ʊ
    if (voInvoice.getHeadVO().isStrike()) {
      if(MessageDialog.showYesNoDlg(this,nc.ui.ml.NCLangRes.getInstance()
    	        .getStrByID("common", "UC001-0000039"), NCLangRes.getInstance().getStrByID("40060501",
          "UPP40060501-000161")/* @res"�Ƿ�ȡ���ϲ���Ʊ?" */) != MessageDialog.ID_YES){
        	  return;
          }
      
      if(voInvoice.getHeadVO().getPrimaryKey() != null 
          && (voInvoice.getHsSelectedARSubHVO() == null || voInvoice.getHsSelectedARSubHVO().size() == 0)){
      	try{
      		Hashtable t = SaleinvoiceBO_Client.queryStrikeData(voInvoice.getHeadVO().getPrimaryKey());
      		if(t != null && t.size() > 0) voInvoice.setHsSelectedARSubHVO(t);
      	}catch(Exception e){
      		e.printStackTrace();
      		return;
      	}
      }  
    }

    // �����������
    voInvoice.setAllinvoicevo(voInvoice);
    voInvoice.setBstrikeflag(new UFBoolean(false));
    voInvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
   
    try {
      // ���֧��
      getPluginProxy().beforeAction(Action.DELETE, new SaleinvoiceVO[] { voInvoice });
      onDelete(voInvoice);
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
      return;
    }

    // �ӻ�������ȥɾ����VO
    getVOCache().removeVOBy(voInvoice.getHeadVO().getCsaleid());

    if (getShowState() == ListShow) {
      getBillListPanel().getHeadBillModel().delLine(new int[] {
        getBillListPanel().getHeadTable().getSelectedRow()
      });
      getBillListPanel().updateUI();
    }
    else {
      // ��ȥ��ǰ���ݣ����Զ���������һ��
//      getVOCache().removeVOAt(getVOCache().getPos());
      getBillCardPanel().loadCardData(
          getVOCache().getVO_Load(getVOCache().getPos()));
    }
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-3-17 9:00:09)
   */
  private void onCard() {

    if (getShowState() == ListShow) {
      remove(getBillListPanel());
      add(getBillCardPanel(), "Center");

      getVOCache().setPos(getBillListPanel().getHeadTable().getSelectedRow());
    }

    getBillCardPanel().loadCardData(
        getVOCache().getVO_Load(getVOCache().getPos()));
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
        "UPP40060501-000065")/* @res "���ݼ��سɹ���" */);
    
    setShowState(CardShow);
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();
    getBillCardPanel().setEnabled(false);

    updateUI();

  }

  /**
   * �л����б����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-20 ����02:22:48
   */
  private void onList() {

    // ��ʾ
    remove(getBillCardPanel());
    add(getBillListPanel(), "Center");
    getBillListPanel().reLoadData(getVOCache());

    setShowState(ListShow);
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();
    
    updateUI();
    getBillListPanel().bodyRowChange(new BillEditEvent(getBillListPanel().getHeadTable(),-1,getVOCache().getPos()));

    getBillListPanel().getHeadTable().getSelectionModel()
	.setSelectionInterval(getVOCache().getPos(),getVOCache().getPos());
  }

  /**
   * ���� BillCardPanel ����ֵ��
   * 
   * @return nc.ui.pub.bill.BillCardPanel
   */
  /* ���棺�˷������������ɡ� */
  public SaleInvoiceCardPanel getBillCardPanel() {
    if (ivjBillCardPanel == null) {
      ivjBillCardPanel = new SaleInvoiceCardPanel(this);
      //���ñ���˵���
      UIMenuItem menu[] = ivjBillCardPanel.getBodyMenuItems();
      java.util.Vector<UIMenuItem> vTemp = new java.util.Vector<UIMenuItem>();
      for(UIMenuItem bodymenu: menu) 
        vTemp.addElement(bodymenu);
      menu = new UIMenuItem[vTemp.size()];
      vTemp.copyInto(menu);
      ivjBillCardPanel.setBodyMenu(menu);
      //����ѡ�б���ɫ
      ivjBillCardPanel.getBillTable().setRowSelectionAllowed(true);
      ivjBillCardPanel.getBillTable().setColumnSelectionAllowed(false);
      ivjBillCardPanel.getBillTable().setSelectionMode(
        javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      //���������ۿۡ���Ʊ�ۿ۾���Ϊ6λ
      ivjBillCardPanel.getHeadItem("ndiscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getHeadItem("ninvoicediscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getBodyItem("ndiscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getBodyItem("nitemdiscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getBodyItem("ninvoicediscountrate").setDecimalDigits(6);
    }
    return ivjBillCardPanel;
  }
  /**
   * ���������������ص�ǰ��Ƭ�����ϱ���ÿ���ϲ���Դ��ID��
   * <b>����˵��</b>
   * @return
   * @author fengjb
   * @time 2008-7-23 ����02:16:16
   */
  public HashSet getUpSourcrBillbid(){
	  HashSet hbodyid = new HashSet();  
    if (ISaleInvoiceOperState.STATE_EDIT == getOperationState()) {
      SaleinvoiceVO billvo = (SaleinvoiceVO) getBillCardPanel().getBillValueVO(
          SaleinvoiceVO.class.getName(), SaleVO.class.getName(),
          SaleinvoiceBVO.class.getName());
      if (billvo != null && billvo.getChildrenVO() != null
          && billvo.getChildrenVO().length > 0) {
        int iLength = billvo.getBodyVO().length;
        for (int i = 0; i < iLength; i++) {
          hbodyid.add(billvo.getBodyVO()[i].getCupsourcebillbodyid());
        }
      }
    }
	  return hbodyid;
  }
  /**
   * ������������������ֵ����������ֵ��������沢�ҵ���ˡ�ȷ������ť��ʱ�����õ�ǰ�����ϵķֵ�����ֵ��
   * <b>����˵��</b>
   * @author fengjb
   * @time 2008-8-29 ����03:45:35
   */
 public void setSplitCondition(SaleDispartVO[] dispvo){
   st.setSplitCondition(dispvo);
 }
 

  /**
   * ���˫���¼� �������ڣ�(2001-6-20 17:19:03)
   */
  public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
    if (e.getPos() == BillItem.HEAD) {
      onCard();
      // ������µ���,����ֱ���޸�
      
      // ˫��ʱ���ڽ����жϵ����Ƿ�Ϊ�µ���  // modify by river for 2012-09-21 
//      if (getBillCardPanel().isNewBill()) {
//        onModify();
//      }
    }
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-4-20 11:15:48)
   */
  private void onPrev() {

    getVOCache().rollToPreviousPos();
    SaleinvoiceVO voCur = getVOCache().getCurrentVO();

    // if (voCur != null && voCur.getChildrenVO() != null) {
    getBillCardPanel().loadCardData(voCur);
    // }
    // else {
    // getBillCardPanel().loadDataPart(voCur.getHeadVO().getCsaleid());
    // updateCacheVOByCard();
    // }

    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

  }

  /**
   * �����Զ��ϲ���Ʊ
   */
  private void batchAutoUnit() {
    java.util.HashMap hsvos = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-100045")/*
                                                       * @res "���ڽ��кϲ���Ʊǰ��׼��..."
                                                       */);
    try {
      hsvos = getBillListPanel().getApproveSaleInvoiceVOs();// ("UNIT");
      if (hsvos == null || hsvos.size() <= 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100046")/*
                                   * @res "��ѡ����ϲ���Ʊ�ķ�Ʊ��"
                                   */);
        return;
      }
    }
    catch (Exception ev) {
      showErrorMessage(ev.getMessage());
      return;
    }

    int max = m_proccdlg.getUIProgressBar1().getMaximum();
    int maxcount = hsvos.size();

    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-100047")/*
                                                       * @res "��ʼ�ϲ���Ʊ..."
                                                       */);

    SaleinvoiceVO saleinvoice = null;
    java.util.Iterator iter = hsvos.keySet().iterator();
    int count = 0;

    while (iter.hasNext()) {
      Object key = iter.next();
      saleinvoice = (SaleinvoiceVO) hsvos.get(key);
      ((SaleVO) saleinvoice.getParentVO()).setCapproveid(null);
      ShowToolsInThread.showStatus(m_proccdlg, new Integer(
          (int) (max * (1.0 * count / maxcount))));
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100048", null,
              new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              }));
      // ShowToolsInThread.showMessage(proccdlg, "���ںϲ���Ʊ...["
      // + ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
      // + "]");
      saleinvoice
          .setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      try {
        if (m_proccdlg.isInterrupted())
          break;
        if (saleinvoice.getHeadVO().getFstatus().intValue() != BillStatus.FREE)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40060501", "UPP40060501-100063")/*
                                                             * @res
                                                             * "������״̬�����ܺϲ���Ʊ"
                                                             */);

        if (saleinvoice.getHsArsubAcct() != null
            && saleinvoice.getHsArsubAcct().size() > 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40060501", "UPP40060501-100064")/*
                                                             * @res
                                                             * "�Ѻϲ���Ʊ����ȡ����Ż��ٴκϲ���Ʊ"
                                                             */);
        saleinvoice = SaleinvoiceBO_Client.autoUniteInvoiceToUI(saleinvoice,
            new ClientLink(ClientEnvironment.getInstance()));
        if (saleinvoice.getHsArsubAcct() != null
            && saleinvoice.getHsArsubAcct().size() > 0) {

          hSuccess.put(key, saleinvoice);
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100049", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]�ϲ���Ʊ�ɹ���", "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]�ϲ���Ʊ�ɹ���");
          // }

        }
        else {
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100051", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]" + "û�з��������ĳ�Ӧ�յ���",
          // "��Ʊ["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]"
          // + "û�з��������ĳ�Ӧ�յ���");
        }

      }
      catch (Exception e) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100052", null, new String[] {
              ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += e.getMessage();
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);
        // ShowToolsInThread.showMessage(proccdlg, " ", "��Ʊ["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]�ϲ���Ʊʧ�ܣ�" + e.getMessage());
        if (m_proccdlg.getckHint().isSelected()) {
          sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100052", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          if (ShowToolsInThread.showYesNoDlg(m_proccdlg, e.getMessage()
              + "\n"
              + sMsg
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
                  "UPP40060501-100053")/*
                                         * @res "�Ƿ�����ϲ���Ʊ���µķ�Ʊ��"
                                         */) == MessageDialog.ID_YES) {
            continue;
          }
          else {
            m_proccdlg.Interrupt();
            break;
          }
        }
      }
      finally {
        count++;
      }
    }

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(max));

    if (m_proccdlg.isInterrupted()) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100041")/*
                                                                       * @res
                                                                       * "�ϲ���Ʊ�������û��жϣ�"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100043")/*
                                                                       * @res
                                                                       * "�ϲ���Ʊ����������"
                                                                       */);
    }

    try {
      Thread.sleep(500);
    }
    catch (Exception ex) {

    }

    if (hSuccess.size() >= 0) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                       * @res
                                                                       * "���ڸ��½�������..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-4-20 11:16:08)
   */
  private void onNext() {

    getVOCache().rollToNextPos();
    SaleinvoiceVO voCur = getVOCache().getCurrentVO();

    // if (voCur != null && voCur.getChildrenVO() != null) {
    getBillCardPanel().loadCardData(voCur);
    // }
    // else {
    // getBillCardPanel().loadDataPart(voCur.getHeadVO().getCsaleid());
    // updateCacheVOByCard();
    // }

    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-4-20 11:15:26) �޸����ڣ�2003-12-02 �޸����ݣ����뻺��
   */
  private void onFirst() {
    getVOCache().rollToFirstPos();
    SaleinvoiceVO voCur = getVOCache().getCurrentVO();

    // if (voCur != null && voCur.getChildrenVO() != null) {
    getBillCardPanel().loadCardData(voCur);
    // }
    // else {
    // getBillCardPanel().loadDataPart(voCur.getHeadVO().getCsaleid());
    // updateCacheVOByCard();
    // }

    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-4-20 11:16:23) �޸����ڣ�2003-12-02 �޸����ݣ����뻺��
   */
  private void onLast() {
    getVOCache().rollToLastPos();
    SaleinvoiceVO voCur = getVOCache().getCurrentVO();

    // if (voCur != null && voCur.getChildrenVO() != null) {
    getBillCardPanel().loadCardData(voCur);
    // }
    // else {
    // getBillCardPanel().loadDataPart(voCur.getHeadVO().getCsaleid());
    // updateCacheVOByCard();
    // }

    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

  }

  /**
   * ���� BillListPanel ����ֵ��
   * 
   * @return nc.ui.pub.bill.BillListPanel
   */
  /* ���棺�˷������������ɡ� */
  public SaleInvoiceListPanel getBillListPanel() {
    if (ivjBillListPanel == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000135")/*
                                 * @res "��ʼ�����б�ģ��...."
                                 */);

      try {
        ivjBillListPanel = new SaleInvoiceListPanel(this);
        // ��ͷ�ӱ���ɫ
        ivjBillListPanel.getHeadTable().setRowSelectionAllowed(true);
        ivjBillListPanel.getHeadTable().setColumnSelectionAllowed(false);
        ivjBillListPanel.getHeadTable().setSelectionMode(
          javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // ����ӱ���ɫ
        ivjBillListPanel.getBodyTable().setRowSelectionAllowed(true);
        ivjBillListPanel.getBodyTable().setColumnSelectionAllowed(false);
        ivjBillListPanel.getBodyTable().setSelectionMode(
          javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      }
      catch (java.lang.Throwable ivjExc) {

        SCMEnv.out(ivjExc);
      }
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000147")/*
                                 * @res "�б�ģ����سɹ���"
                                 */);

      getBillListPanel().addMouseListener(this);
      //fengjb 20081020  V55���������ۿۡ���Ʊ�ۿ۾���Ϊ6λ
      getBillListPanel().getHeadItem("ndiscountrate").setDecimalDigits(6);
      getBillListPanel().getHeadItem("ninvoicediscountrate").setDecimalDigits(6);
      getBillListPanel().getBodyItem("ndiscountrate").setDecimalDigits(6);
      getBillListPanel().getBodyItem("nitemdiscountrate").setDecimalDigits(6);
      getBillListPanel().getBodyItem("ninvoicediscountrate").setDecimalDigits(6);
      
    }
    return ivjBillListPanel;
  }

  /**
   * ����ʵ�ָ÷���������ҵ�����ı��⡣
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    // return m_title;
    return getBillCardPanel().getBillData().getTitle();
  }

  /**
   * ���۷�Ʊ�����ʼ��
   */
  /* ���棺�˷������������ɡ� */
  private void initialize() {
    // ���ز���
    // getNodeInfo().getSysPara();
    // getNodeInfo().initDef();

    try {
      setName("SaleInvoice");
      setSize(774, 419);
      add(getBillCardPanel(), "Center");
    }
    catch (java.lang.Throwable ivjExc) {
      SCMEnv.out(ivjExc);
    }
    //�Ҽ��˵�����"�����к�"
    nc.ui.scm.pub.BillTools.addReSortRowNoToPopMenu(getBillCardPanel(), null);
    //�����Ҽ��˵���"��Ƭ�༭"
    nc.ui.scm.pub.BillTools. addCardEditToBodyMenus(getBillCardPanel(), null);
    //�����Ҽ��˵�
    UIMenuItem[] bodyMenuItems = getBillCardPanel().getBodyMenuItems();
    int ilength = bodyMenuItems.length;
    if( ilength > 2){
    bodyMenuItems[ilength-1].addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
              onBoCardEdit();            
        }});
    bodyMenuItems[ilength-2].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(), 
            SaleBillType.SaleInvoice, "crowno");          
      }});
    }
    
    getBillCardPanel().setBusiType(getBtns().getBusiType());

    setShowState(CardShow);
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtons(getBtns().getButtonArray());
    setButtonsStateBrowse();
    getBillCardPanel().addActionListener(this);
    getBillCardPanel().setEnabled(false);

  }

  /**
   * ������������һ�ŷ�Ʊ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-7-31 ����09:53:00
   */
  private void onApprove() {

    SaleinvoiceVO voInvoice = (SaleinvoiceVO) getVOForAction();
    voInvoice.setCl(new ClientLink(getClientEnvironment()));
    
    HashMap<String,SmartVO> hsnewvo= null; 
    try {
      if (voInvoice.getHeadVO().getDbilldate().compareTo(
          getClientEnvironment().getDate()) > 0)
        throw new nc.vo.pub.BusinessException("�������ڲ���С���Ƶ����ڣ�");
      onApproveCheckWorkflow(voInvoice);
      voInvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
      voInvoice.getHeadVO().setDaudittime(new UFDateTime(System.currentTimeMillis()));
      voInvoice.getHeadVO().setDapprovedate(getClientEnvironment().getDate());
      hsnewvo = (HashMap)PfUtilClient.processActionFlow(this, ISaleInvoiceAction.Approve,
          SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), voInvoice, null);
    }catch (Exception e) {
      showErrorMessage(e.getMessage());
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
      return;
    }
    /************��¼ҵ����־*************/
     insertOperLog(voInvoice,nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
     "UC001-0000027")/* @res"����" */);
    /************��¼ҵ����־* end ************/
    // -----------�ɹ�
    if (!PfUtilClient.isSuccess()) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
          "UPP40060301-000254")/*
                                 * @res "��������Ѿ����û�ȡ����"
                                 */);
      return;
    }
    
    updateUIValue(hsnewvo);
    
    //���ݵ���״̬��ʾ��˽��
    int status = -1;
    if (getShowState() == ListShow) 
      status = getVOCache().getCurrentVO().getHeadVO().getFstatus();
    else if (getShowState() == CardShow) 
      status = SmartVODataUtils.getInteger(getBillCardPanel().getHeadItem("fstatus").getValueObject());
    
   String res = "�������������";
        if(BillStatus.AUDIT == status)
          res += "����ͨ��";
        else if(BillStatus.AUDITING == status)
          res += "��������";
        else if(BillStatus.NOPASS == status)
          res +="����δͨ��";
        else if(BillStatus.FREE == status)
          res +="�����Ƶ���";
        else
          res += "δ֪״̬";
        showHintMessage(res);
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
//          "UPP40060501-000123")/* @res "��Ʊ�����ɹ���" */);
  }

  /**��������������V55 ��Ʊ���桢��ˡ�����ɾ������֧��ҵ����־��
   * <b>����˵��</b>
   * @param voinvoice
   * @param action
   * @author fengjb
   * @time 2008-8-26 ����07:15:04
   */
  private void insertOperLog(SaleinvoiceVO voinvoice, String action) {
    Operlog operlog=new Operlog();
    voinvoice.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    voinvoice.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    voinvoice.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    operlog.insertBusinessExceptionlog(voinvoice, action, null, nc.vo.scm.funcs.Businesslog.MSGMESSAGE, SaleBillType.SaleInvoice);
    
  }

  /**
   * ������������һ�ŷ�Ʊ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-19 ����11:48:04
   */
  private void onUnApprove() {
    
    SaleinvoiceVO voInvoice = (SaleinvoiceVO) getVOForAction();
    HashMap<String,SmartVO> hsnewvo = null;
    // �����������
    voInvoice.setAllinvoicevo(voInvoice);
    voInvoice.setBstrikeflag(UFBoolean.FALSE);
    voInvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
    try {

      hsnewvo = (HashMap)PfUtilClient.processActionFlow(this, ISaleInvoiceAction.UnApprove,
          SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), voInvoice, null);
    
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      nc.vo.scm.pub.SCMEnv.out(e);
      return;
    }
    if (PfUtilClient.isSuccess()) {
      
       updateUIValue(hsnewvo);
       
      /************��¼ҵ����־*************/
      insertOperLog(voInvoice, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000028")/* @res"����" */);
      /************��¼ҵ����־* end ************/
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
          "UPPSCMCommon-000184")/* @res "����ɹ���" */);
    }
  }

  /**
   * ������
   */
  private void onATP() {
    try {
      PfUtilClient.processAction(this, ISaleInvoiceAction.Atp,
          nc.ui.scm.so.SaleBillType.SaleInvoice, getClientEnvironment()
              .getDate().toString(), getBillCardPanel().getVO(),
          getBillCardPanel().getVOOnlySelectedRow());
    }
    catch (nc.vo.pub.BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace();
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
  }

  /**
   * �ͻ���Ϣ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-1 ����02:56:21
   */
  private void onCustInfo() {
    Object ob = null;
    if (getShowState() == ListShow) {
      ob = getBillListPanel().getHeadBillModel().getValueAt(
          getBillListPanel().getHeadTable().getSelectedRow(), "creceiptcorpid");
    }
    else {
      ob = getBillCardPanel().getHeadItem("creceiptcorpid").getValue();
    }

    try {
      PfUtilClient.processActionNoSendMessage(this,
          ISaleInvoiceAction.CustInfo, nc.ui.scm.so.SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), getVOForAction(), ob,
          null, null);
    }
    catch (Exception e) {
      showErrorMessage(getBtns().m_boCustInfo.getName()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000069")/* @res "ʧ�ܣ�" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
  }

  /**
   * �ͻ�����
   */
  private void onCustCredit() {
    String sCust = null;
    String sBiztype = null;
    String cproductid = null;
    if (getShowState() == ListShow) {
      int iselbodyrow = getBillListPanel().getBodyTable().getSelectedRow();
      if (iselbodyrow < 0)
        iselbodyrow = 0;
      Object oTemp = getBillListPanel().getBodyBillModel().getValueAt(
          iselbodyrow, "ccustomerid");
      sCust = oTemp == null ? null : oTemp.toString();
      oTemp = getBillListPanel().getHeadBillModel().getValueAt(
          getBillListPanel().getHeadTable().getSelectedRow(), "cbiztype");
      sBiztype = oTemp == null ? null : oTemp.toString();
      if (st.SO_27 != null
          && st.SO_27.booleanValue()
          && getBillListPanel().getBodyBillModel().getRowCount() > 0) {
        cproductid = (String) getBillListPanel().getBodyBillModel().getValueAt(
            0, "cprolineid");
      }
    }
    else {
      sCust = getBillCardPanel().getCCustomerid();
      sBiztype = getBillCardPanel().getBusiType();

      if (st.SO_27 != null
          && st.SO_27.booleanValue()
          && getBillCardPanel().getRowCount() > 0) {
        cproductid = (String) getBillCardPanel()
            .getBodyValueAt(0, "cprolineid");
      }
    }
    nc.vo.so.pub.CustCreditVO voCredit = new nc.vo.so.pub.CustCreditVO();
    voCredit.setPk_cumandoc(sCust);
    voCredit.setCbiztype(sBiztype);
    voCredit.setCproductline(cproductid);

    try {
//      SaleinvoiceVO saleinvoice = (SaleinvoiceVO) getBillCardPanel()
//          .getBillValueVO(SaleinvoiceVO.class.getName(),
//              SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      PfUtilClient.processActionFlow(this, ISaleInvoiceAction.CustCredit,
          SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), getVo(), voCredit);
    }
    catch (nc.vo.pub.BusinessException e1) {
      showErrorMessage(e1.getMessage());
      e1.printStackTrace();
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }

  }
	/**
	 * �����˰Ʊ��
	 * 
	 * @author ��ǿ��
	 * @since 2008-12-2
	 */
	private void onImportTaxCode() {
		GoldTaxVO[] goldTaxVOs = new TransGoldTaxDlg(this).importGoldTax();
		SCMEnv.out("�����˰VO���鳤�� goldTaxVOs.length = " + goldTaxVOs.length);
		SaleVO[] saleVOs = new SaleVO[goldTaxVOs.length];
		for (int i = 0; i < goldTaxVOs.length; i++) {
			GoldTaxHeadVO taxHeadVO = goldTaxVOs[i].getParentVO();
			SaleVO saleVO = new SaleVO();
			// ���۷�Ʊ���ݺ�
			saleVO.setVreceiptcode(taxHeadVO.getCode());
			// ��˰Ʊ��
			saleVO.setCgoldtaxcode(taxHeadVO.getTaxBillNo());
			saleVOs[i] = saleVO;
		}
		try {
			// ���ݵ��ݺŸ��½�˰Ʊ��
			SaleinvoiceBO_Client.updateSaleinvoiceValue(saleVOs, new String[]{"cgoldtaxcode"}, new String[]{"vreceiptcode"});
			showHintMessage("��˰Ʊ�ŵ���ɹ�");
		} catch (Exception e) {
			SCMEnv.error("���½�˰Ʊ�ŷ����쳣", e);
			showErrorMessage("���½�˰Ʊ�ŷ����쳣");
		}
	}

	/**
	 * ����˰
	 */
	private void onSoTax() {
		Map<String, SaleinvoiceVO> voMapOfId = new HashMap<String, SaleinvoiceVO>();
		List<GoldTaxVO> goldList = new ArrayList<GoldTaxVO>();
		if (getShowState() == ListShow) {
			SaleinvoiceVO[] saleinvoiceVOs = getBillListPanel().getSelectedVOs();
			for (SaleinvoiceVO saleinvoiceVO : saleinvoiceVOs) {
				goldList.add(saleinvoiceVO.convertGoldTaxVO());
				voMapOfId.put(((SaleVO)saleinvoiceVO.getParentVO()).getPrimaryKey(), saleinvoiceVO);
			}
		} else {
			getVOCache().setPos(getBillListPanel().getHeadTable().getSelectedRow());
			SaleinvoiceVO vo = getVOCache().getVO_Load(getVOCache().getPos());
			if (null != vo) {
				goldList.add(vo.convertGoldTaxVO());
				voMapOfId.put(((SaleVO)vo.getParentVO()).getPrimaryKey(), vo);
			}
		}

		TransGoldTaxDlg goldTaxDlg = new TransGoldTaxDlg(this);
		goldTaxDlg.setGoldTaxVOs(goldList.toArray(new GoldTaxVO[goldList.size()]));
		if (UIDialog.ID_OK == goldTaxDlg.showModal()) {
			SCMEnv.out("����˰�����ɹ�");
			try {
				// ����˰ʱ��
				UFDateTime transTime = ClientEnvironment.getServerTime();
				Map<String, UFDateTime> tsMap = SaleinvoiceBO_Client.updateWhenToGoldTax(new ArrayList<String>(voMapOfId.keySet()), transTime);
				if (getShowState() == ListShow) {
					int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
					// ѭ������TS���Ƿ񴫽�˰������˰ʱ��
					for (int row = 0; row < rowCount; row++) {
						String id = (String) getBillListPanel().getHeadBillModel().getValueAt(row, "csaleid");
						if (!StringUtil.isEmpty(id)) {
							UFDateTime ts = tsMap.get(id);
							if (null != ts) {
								getBillListPanel().getHeadBillModel().setValueAt(ts, row, "ts");
								getBillListPanel().getHeadBillModel().setValueAt(UFBoolean.TRUE, row, "btogoldtax");
								getBillListPanel().getHeadBillModel().setValueAt(transTime.toString(), row, "dtogoldtaxtime");

								SaleinvoiceVO saleinvoiceVO = voMapOfId.get(id);
								if (null != saleinvoiceVO) {
									saleinvoiceVO.getParentVO().setAttributeValue("ts", ts);
									saleinvoiceVO.getParentVO().setAttributeValue("btogoldtax", UFBoolean.TRUE);
									saleinvoiceVO.getParentVO().setAttributeValue("dtogoldtaxtime", transTime.toString());
									// ���»���
									getVOCache().setSaleinvoiceVO(((SaleVO)saleinvoiceVO.getParentVO()).getPrimaryKey(), saleinvoiceVO);
								}
							}
						}
					}
				} else if (getShowState() == CardShow) {
					String id = (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();
					if (!StringUtil.isEmpty(id)) {
						UFDateTime ts = tsMap.get(id);
						if (null != ts) {
							// ����TS���Ƿ񴫽�˰������˰ʱ��
							getBillCardPanel().setHeadItem("ts", ts);
							getBillCardPanel().setHeadItem("btogoldtax", UFBoolean.TRUE);
							getBillCardPanel().setHeadItem("dtogoldtaxtime", transTime.toString());

							SaleinvoiceVO saleinvoiceVO = voMapOfId.get(id);
							if (null != saleinvoiceVO) {
								saleinvoiceVO.getParentVO().setAttributeValue("ts", ts);
								saleinvoiceVO.getParentVO().setAttributeValue("btogoldtax", UFBoolean.TRUE);
								saleinvoiceVO.getParentVO().setAttributeValue("dtogoldtaxtime", transTime.toString());
								// ���»���
								getVOCache().setSaleinvoiceVO(((SaleVO)saleinvoiceVO.getParentVO()).getPrimaryKey(), saleinvoiceVO);
							}
						}
					}
				}
			} catch (Exception e) {
				SCMEnv.error("������˰ʱ����µ����ݿⷢ���쳣", e);
			}
		} else {
			SCMEnv.out("����˰δ�����ɹ�");
		}
	}

  /**
   * ҵ�����ͱ仯�� �������ڣ�(2001-9-14 9:41:00)
   * 
   * @param bo
   *          nc.ui.pub.ButtonObject
   */
  private void onBusiType(ButtonObject bo) {
    bo.setSelected(true);
//    getBillCardPanel().addNew();
    getBillCardPanel().setBusiType(bo.getTag());

    // �仯��ť
    getBtns().changeButtonsWhenBusiTypeSelected(bo);
    setButtons(getBtns().getButtonArray());
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

    setTitleText(getBillCardPanel().getTitle());

  }
  /**
   * �������������������������޸�ʱ���ݵ�ǰҵ����������"��������"�ɲ��յġ�
   * <b>����˵��</b>
   * @param nowBusitype
   * @author fengjb
   * @time 2008-7-29 ����02:39:36
   */
  private void changeRefAddButtonByBusiType(String nowBusitype){
	  if(nowBusitype == null || nowBusitype.trim().length() ==0){
		  getBtns().m_boRefAdd.removeAllChildren();
	  }
        //��øõ���������ĳҵ�����������õ�������Դ��������
		BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi(SaleInvoiceTools.getLoginPk_Corp(), 
				 SaleBillType.SaleInvoice, nowBusitype);
		 getBtns().m_boRefAdd.removeAllChildren();
		if (billBusiVOs == null)
			return;

		// ����Դ���ݶ���Ϊ�Ӳ˵���ť
		ButtonObject btnAddChild = null;
		for (int i = 0; i < billBusiVOs.length; i++) {
			String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[i].getPk_billtype(), billBusiVOs[i].getBilltypename());
			btnAddChild = new ButtonObject(showName);
			btnAddChild.setPowerContrl(false);
			// ���ð�ť��TAGΪ��3C:1001AA10000000004SG5��
			btnAddChild.setTag(billBusiVOs[i].getPk_billtype().trim() + ":" + billBusiVOs[i].getPk_businesstype().trim());
			getBtns().m_boRefAdd.addChildButton(btnAddChild);
     }
  }

  /**
   * ȡ����Ʊ������ �������ڣ�(2001-4-20 11:19:14)
   */

  private void onCancelSave() {
 
    // ����PANEL�����ɸ��ĺͺ������֮˳�����ڲ�ʹ���˵�ǰ����״̬�ж�
    if (null != getVOCache().getCurrentVO()
        && null != getVOCache().getCurrentVO().getHeadVO()
        && getVOCache().getCurrentVO().getHeadVO().isNew()) {
      // �����ڳ�����ܿ�Ʊ�����VO��δ�����뵽��������
      if (!getBillCardPanel().isOutSumMakeInvoice()) 
        getVOCache().removeVOAt(getVOCache().getPos()); 
    }
    //���ȡ��ǰ�б���ѡ�е�VO����
    if(null == getVOCache().getCurrentVO() && null != getBillListPanel().getSelectedVO()
        && null != getBillListPanel().getSelectedVO().getHeadVO()){
     String csaleid = (String) getBillListPanel().getSelectedVO().getHeadVO().getPrimaryKey();
     getVOCache().setPos(getVOCache().findPos(csaleid));
    }
   
    getBillCardPanel().setPanelAfterCancelSave(getVOCache().getCurrentVO());
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

    showHintMessage("");
    
    //�ǳ������
    getBillCardPanel().setOutSumMakeInvoice(false);

    updateUI();

  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-4-20 11:18:26)
   */
  private void onModify() {
    if (getShowState() == ListShow) {
      int iRow = getBillListPanel().getHeadTable().getSelectedRow();
      if (iRow < 0) {
        showErrorMessage("��ѡ���б��е�һ�з�Ʊ");
        return;
      }

      getVOCache().setPos(iRow);

      remove(getBillListPanel());
      repaint();
      add(getBillCardPanel(), "Center");

      setShowState(CardShow);
      getBillCardPanel().loadCardData(getVOCache().getCurrentVO());
    }

    // ------2-------------to card
    SaleinvoiceVO VO =  getVOCache().getCurrentVO();
    
    getBillCardPanel().modify(VO);
    //��Ʊ�޸ĵ�ʱ�����ݵ�ǰVO��ҵ������ȥ���ÿɲ��յĵ�������
    changeRefAddButtonByBusiType(VO.getHeadVO().getCbiztype());
    
    String custId = ((SaleVO) VO.getParentVO()).getCreceiptcorpid();
    String[][] results = SaleInvoiceTools.getCustInfo(custId);
    try {
      if(results == null){
      	results = SaleinvoiceBO_Client.getCustomerInfo(custId);
      	if(results != null) SaleInvoiceTools.setCustInfo(custId, results);
      }
      if (results != null && results.length != 0) {
        BillItem item = getBillCardPanel().getHeadItem("ccustbankid");
        UIRefPane bankref = null;
        if(item.getDataType() == BillItem.UFREF){
      	  bankref = (UIRefPane) item.getComponent();
      	  if(bankref.getRefModel() != null) 
             bankref.getRefModel().setPk_corp(getCorpPrimaryKey());
        }
        item = getBillCardPanel().getHeadItem("ccustomerbank");
        if(item.getDataType() == BillItem.UFREF){
	          bankref = (UIRefPane) item.getComponent();
	          if(bankref.getRefModel() != null)
              bankref.getRefModel().setPk_corp(getCorpPrimaryKey());
        }
        
        String bankId = ((SaleVO) VO.getParentVO()).getCcustbankid();
        bankref.setPK(bankId);
        String bankNo = bankref.getRefCode();
        getBillCardPanel().setHeadItem("ccustomerbankNo", bankNo);
        getBillCardPanel().setHeadItem("ccustomertel", results[0][0]);
        getBillCardPanel().setHeadItem("ccustomertaxNo", results[0][1]);
        getBillCardPanel().setHeadItem("vreceiveaddress", results[0][7]);

      }
      else {
    	  getBillCardPanel().setHeadItem("ccustomertel", "");
    	  getBillCardPanel().setHeadItem("ccustomertaxNo", "");
      }

    }
    catch (Exception e1) {
      SCMEnv.out(e1);
    }

    // --------3------------------------
    setOperationState(ISaleInvoiceOperState.STATE_EDIT);
    setButtonsStateEdit();

    this.showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        "SCMCommon", "UPPSCMCommon-000350")/*
                                             * @res "�༭����..."
                                             */);
  }

  // }
  /**
   * ����������������Ҫ�����������Ĺ��ܡ�
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-6 ����10:06:28
   */
  private void onOutSumMakeInvoice() {

    if (getShowState() == ListShow) {
      remove(getBillListPanel());
      add(getBillCardPanel(), "Center");
      
      setShowState(CardShow);
    }
    getBillCardPanel().setPanelWhenOutSumMakeInvoice();
    getBillCardPanel().getHeadItem("finvoicetype").setValue(getDefaultInvoiceType());
    setOperationState(ISaleInvoiceOperState.STATE_EDIT);
    setButtonsStateEdit();

  }

  /**
   * ����ȡ���ϲ���Ʊ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-7-30 ����04:50:26
   */
  private void batchCancelUnit() {
    java.util.HashMap hmapIdVsVO = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-100055")/*
                                                       * @res
                                                       * "���ڽ���ȡ���ϲ���Ʊǰ��׼��..."
                                                       */);
    try {
      hmapIdVsVO = getBillListPanel().getApproveSaleInvoiceVOs();// "UNIT");//
    }
    catch (Exception ev) {
      showErrorMessage(ev.getMessage());
      return;
    }
    if (hmapIdVsVO == null || hmapIdVsVO.size() <= 0) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
          "UPP40060501-100056")/*
                                 * @res "��ѡ����ϲ���Ʊ�ķ�Ʊ��"
                                 */);
      return;
    }

    int max = m_proccdlg.getUIProgressBar1().getMaximum();
    int maxcount = hmapIdVsVO.size();

    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-100057")/*
                                                       * @res "��ʼ�ϲ���Ʊ..."
                                                       */);

    SaleinvoiceVO voInvoice = null;
    java.util.Iterator iter = hmapIdVsVO.keySet().iterator();
    int count = 0;

    while (iter.hasNext()) {
      Object key = iter.next();
//       TODO �˴���ID��ֵ
      // id = Integer.parseInt(key.toString());
      voInvoice = (SaleinvoiceVO) hmapIdVsVO.get(key);

      ((SaleVO) voInvoice.getParentVO()).setCapproveid(null);

      ShowToolsInThread.showStatus(m_proccdlg, new Integer(
          (int) (max * (1.0 * count / maxcount))));
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100058", null,
              new String[] {
                ((SaleVO) voInvoice.getParentVO()).getVreceiptcode()
              })); /*
                     * proccdlg, "���ںϲ���Ʊ...[" /* + ((SaleVO)
                     * saleinvoice.getParentVO()).getVreceiptcode() /* + "]");
                     */
      voInvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      if (m_proccdlg.isInterrupted())
        break;

      // (1)�������ݵ�CARD����(2)�ϲ���Ʊ���ָ����ݣ���(3)������

      // (1)�������ݵ�CARD
      getBillCardPanel().loadCardData(voInvoice);
      // �����޸�ǰ���ݺ�
      SaleinvoiceVO hvo = (SaleinvoiceVO) getBillCardPanel().getVO();
      getBillCardPanel().setOldVO(hvo);
      getBillCardPanel().updateValue();
      // (2)�ϲ���Ʊ
      boolean needUnitCancel = getBillCardPanel().uniteCancel();
      if (!needUnitCancel) {
        continue;
      }
      // (3)����
      String sBusinessException = onSave();

      if (sBusinessException != null) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100061", null, new String[] {
              ((SaleVO) voInvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += sBusinessException;
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);/*
                                                               * ShowToolsInThread.showMessage(proccdlg, " ",
                                                               * "��Ʊ[" +
                                                               * ((SaleVO)
                                                               * saleinvoice.getParentVO())
                                                               * .getVreceiptcode() +
                                                               * "]�ϲ���Ʊʧ�ܣ�" +
                                                               * e.getMessage());
                                                               */
        if (m_proccdlg.getckHint().isSelected()) {
          sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100061", null, new String[] {
                ((SaleVO) voInvoice.getParentVO()).getVreceiptcode()
              });
          if (ShowToolsInThread.showYesNoDlg(m_proccdlg, sBusinessException
              + "\n"
              + sMsg
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
                  "UPP40060501-100062")/*
                                         * @res "�Ƿ�����ϲ���Ʊ���µķ�Ʊ��"
                                         */) == MessageDialog.ID_YES) {
            continue;
          }
          else {
            m_proccdlg.Interrupt();
            break;
          }
        }
      }
      else {
        // �ӽ���õ���VO
        voInvoice = (SaleinvoiceVO) getBillCardPanel().getBillValueVO(
            SaleinvoiceVO.class.getName(), SaleVO.class.getName(),
            SaleinvoiceBVO.class.getName());
        hSuccess.put(key, voInvoice);
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100059", null, new String[] {
              ((SaleVO) voInvoice.getParentVO()).getVreceiptcode()
            });
        ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);/*
                                                               * ShowToolsInThread.showMessage(proccdlg,
                                                               * "��Ʊ[" +
                                                               * ((SaleVO)
                                                               * saleinvoice.getParentVO())
                                                               * .getVreceiptcode() +
                                                               * "]�ϲ���Ʊ�ɹ���",
                                                               * "��Ʊ[" +
                                                               * ((SaleVO)
                                                               * saleinvoice.getParentVO())
                                                               * .getVreceiptcode() +
                                                               * "]�ϲ���Ʊ�ɹ���");}
                                                               */
      }
      count++;
    }

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(max));

    if (m_proccdlg.isInterrupted()) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100042")/*
                                                                       * @res
                                                                       * "�ϲ���Ʊ�������û��жϣ�"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100044")/*
                                                                       * @res
                                                                       * "�ϲ���Ʊ����������"
                                                                       */);
    }

    try {
      Thread.sleep(500);
    }
    catch (Exception ex) {

    }

    if (hSuccess.size() >= 0) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                       * @res
                                                                       * "���ڸ��½�������..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  /**
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-9 ����04:20:05
   */
  private void onNew(ButtonObject bo) {
    //ʱ��׮
    UFDouble chgvotime = null;
    UFDouble dealmnytime = null;
    UFDouble addtouitime = null;
    PfUtilClient.childButtonClicked(bo, SaleInvoiceTools.getLoginPk_Corp(),
        "40060302", SaleInvoiceTools.getLoginUserId(),
        SaleBillType.SaleInvoice, this);
    
    long s = System.currentTimeMillis();
    nc.vo.scm.pub.SCMEnv.out("����VO���տ�ʼ...");
    SaleinvoiceVO[] vosNew = null;
    if (!nc.ui.pub.pf.PfUtilClient.makeFlag) {
      if (PfUtilClient.isCloseOK()) {
        vosNew = (SaleinvoiceVO[]) PfUtilClient.getRetVos();
      }
    }
    
    chgvotime = SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s)
                          .div(1000);
    nc.vo.scm.pub.SCMEnv.out("VO���չ���ʱ[ "+ chgvotime  + " S] ");
    
    if (null == vosNew || vosNew.length == 0  
        || null == vosNew[0].getBodyVO() || vosNew[0].getBodyVO().length ==0)
      return;
    //���ε�������  
    String sUpSrcType = vosNew[0].getBodyVO()[0].getCupreceipttype();
    //��ʾ�������ֶ�
    if (SaleBillType.SaleOrder.equals(sUpSrcType)) {
	      getBillCardPanel().hideBodyTableCol("cupsourcebillcode");
	     }
    //��ʾ���ⵥ���ֶ�
	  else if (SaleBillType.SaleOutStore.equals(sUpSrcType)) {
	      getBillCardPanel().showBodyTableCol("cupsourcebillcode");
	      getBillCardPanel().hideBodyTableCol("csourcebillcode");
	     }
    //����Ĭ�Ϸ�Ʊ����
    vosNew[0].getHeadVO().setFinvoicetype(getDefaultInvoiceType());
    
    //�����������ġ�,��Ʊ�ͻ���ͬ�����۶�����һ��ƱʱӦΪ��һ��������Ʊ�ͻ���
    //ͨ������ƽ̨ȡ����VO����˳���ǵߵ���,���Ҫ������ת��
    if(vosNew.length > 1 && st.SO_30 .booleanValue() ){
    	SaleinvoiceVO temp = vosNew[0];
    	vosNew[0] = vosNew[vosNew.length-1];
    	vosNew[vosNew.length-1]= temp;
    }
    
  // 20070917
 //	saveSourceMny(vosNew);
   s = System.currentTimeMillis();
  nc.vo.scm.pub.SCMEnv.out("���зֵ���β���ʼ...");
  //���շֵ������ֵ�
	SaleinvoiceVO[] aryRetVO = st.mergeSourceVOs(vosNew);

	/** �����ο�Ʊβ�� */
	if (SaleBillType.SaleOutStore.equals(sUpSrcType)) {
		dealMny(aryRetVO);
	}
  dealmnytime = SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s)
   .div(1000);
  nc.vo.scm.pub.SCMEnv.out("�ֵ���β�����ʱ[ " + dealmnytime + " S] ");
  //���㷢Ʊ��ͷ�ĺϼƽ���������� ��ÿ�з�Ʊ�����еĳ��ǰ���
  calcTotalMny(aryRetVO);
	  
    // ����ת���б�һ���򵽿�Ƭ
    if (aryRetVO.length > 1) {
      //���û���
      getVOCache().setCacheData(aryRetVO);
      onList();

     //restoreSourceMnyList();
    }
    else {
      getVOCache().addVO(aryRetVO[0]);
      getVOCache().rollToLastPos();
      // ------2-------------to card
      if(ListShow == getShowState()){
    	  remove(getBillListPanel());
    	  add(getBillCardPanel(), "Center");
      }
      s = System.currentTimeMillis();
      nc.vo.scm.pub.SCMEnv.out("����������ƱVO����Ƭ���洦��ʼ...");
      // TODO �˴��ظ�����������״������Ż�
//      long s2 = System.currentTimeMillis();
//      nc.vo.scm.pub.SCMEnv.out("----------------------------------------------");
//      nc.vo.scm.pub.SCMEnv.out("loadCardData��������ʼ...");
    
      getBillCardPanel().loadCardData(aryRetVO[0]);
      
      
//      nc.vo.scm.pub.SCMEnv.out("loadCardData����������ʱ[ "+ SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s2).div(1000)+ " S] ");
//      
//      s2 = System.currentTimeMillis();
//      nc.vo.scm.pub.SCMEnv.out("modify��������ʼ...");
      
      
      getBillCardPanel().modify(aryRetVO[0]);
      
      
//      nc.vo.scm.pub.SCMEnv.out("modify����������ʱ[ "+ SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s2).div(1000)+ " S] ");
      addtouitime = SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s).div(1000);
      nc.vo.scm.pub.SCMEnv.out("����������ƱVO����Ƭ���洦����ʱ[ "+ addtouitime+ " S] ");
      // --------3------------------------
      setShowState(CardShow);
      setOperationState(ISaleInvoiceOperState.STATE_EDIT);
    
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon",
          "UPPSCMCommon-000350")
      /*
       * @res "�༭����..."
       */
      );
      
    //���ñ�ͷĬ��ֵ���˴�����Ǩ�Ƶ�ǰ�棬��Ϊ����൥��ʱ����޷������ͷ�Ľ��ϼ�
//    getBillCardPanel().setHeadItem("ninvoicediscountrate", new UFDouble(100));
//    getBillCardPanel().setHeadItem("ntotalsummny", getBillCardPanel().calcurateTotal("noriginalcursummny"));
//    getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));
//    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    setButtonsStateEdit();
//    if(null !=chgvotime && null != dealmnytime
//        && null != addtouitime ){
//      UFDouble totaltime = addtouitime.add(dealmnytime).add(addtouitime);
//      nc.vo.scm.pub.SCMEnv.out("��Ʊ�����������ֹ���ʱ[ " + totaltime+ " S ]"); 
//      
//      nc.vo.scm.pub.SCMEnv.out("VO����ռ�ñ���[ " 
//          + addtouitime.div(totaltime).multiply(100)+"% ]"); 
//      
//      nc.vo.scm.pub.SCMEnv.out("�ֵ���β���ռ�ñ���[ " 
//          + dealmnytime.div(totaltime).multiply(100)+"% ]"); 
//      
//      nc.vo.scm.pub.SCMEnv.out("����������ƱVO����Ƭ���洦��ռ�ñ���[ " 
//          + addtouitime.div(totaltime).multiply(100)+"% ]"); 
//    }
    }
  }
/**
 * �������۶���->���ⵥ->��Ʊ�����е�β��
 * ��Ϊǰ̨�ֵ�֮�����ݷ����仯��������Ҫ��ǰ̨���½���β���
 * @author jianghp 20080519
 */
private void dealMny(SaleinvoiceVO[] aryRetVO) {
  
	for (int i = 0,loop = aryRetVO.length; i < loop; i++) {
		//���ճ��ⵥ����Դ�������ͽ�����ֿ����ֱ����β���
		Hashtable<String, ArrayList<SaleinvoiceBVO>> ht = new Hashtable<String, ArrayList<SaleinvoiceBVO>>();
    //����;���������ڴ���β��ʱ����
    HashMap<String, UFDouble>  hswastagenum = new HashMap<String,UFDouble>();
		for (int j = 0,jloop = aryRetVO[i].getChildrenVO().length; j < jloop; j++) {
      
      SaleinvoiceBVO voicebvo = aryRetVO[i].getBodyVO()[j];
      //Դͷ��������
			String cfirsttype = voicebvo.getCreceipttype();
     if(null == cfirsttype || cfirsttype.length() == 0)
       continue;
      if(voicebvo.getNaccumwastnum() != null  
          && voicebvo.getNaccumwastnum().compareTo(new UFDouble(0))!= 0){
        if(hswastagenum.containsKey(voicebvo.getCsourcebillbodyid())){
          UFDouble wastnum = hswastagenum.get(voicebvo.getCsourcebillbodyid());
          wastnum = wastnum.add(voicebvo.getNaccumwastnum());
          hswastagenum.put(voicebvo.getCsourcebillbodyid(), wastnum);
        }
        else
        hswastagenum.put(voicebvo.getCsourcebillbodyid(),voicebvo.getNaccumwastnum());
      }
			if (null == ht.get(cfirsttype)) {
				ArrayList<SaleinvoiceBVO> aryinvoicebvo = new ArrayList<SaleinvoiceBVO>();
        aryinvoicebvo.add(voicebvo);
				ht.put(cfirsttype, aryinvoicebvo);
			} else {
				ht.get(cfirsttype).add(voicebvo);
			}
		}
		if (ht.size() > 0) {
			String[] firsttypes = new String[ht.size()];
			ht.keySet().toArray(firsttypes);
			for (int k = 0,kloop = firsttypes.length; k < kloop; k++) {
				String cfirsttype = firsttypes[k];
				String csourcebodyid = SaleBillType.SaleOrder.equals(cfirsttype)?
            "corder_bid":"pk_apply_b";
				IEqualValueUpBill[] voUpInfos = null;
				voUpInfos = getSourceItemsFrom4C(cfirsttype, ht
						.get(cfirsttype));
				
				IEqualValueDownBill[] voaOtherDownInfo = null;
				ArrayList<IEqualValueDownBill[]> voListOtherDownInf = new ArrayList<IEqualValueDownBill[]>();
				for (int j = 0,jloop = voUpInfos.length; j < jloop; j++) {
					try {
						voaOtherDownInfo = getOtherDownInfo(((CircularlyAccessibleValueObject) voUpInfos[j])
								.getPrimaryKey());
            
						if (null != voaOtherDownInfo) {
							voListOtherDownInf.add(voaOtherDownInfo);
						}
					} catch (Exception e) {
						if (e instanceof BusinessException) {
							MessageDialog.showErrorDlg(this, "", e
									.getMessage());
							return;
						} else {
							MessageDialog.showErrorDlg(this, "", e
									.getMessage());
							return;
						}
					}
				}
				IEqualValueDownBill[] ievdb = new IEqualValueDownBill[ht
						.get(cfirsttype).size()];
				ht.get(cfirsttype).toArray(ievdb);
        HashSet<String> hssourcebid = new HashSet<String>();
				ArrayList<IEqualValueUpBill> arytemp = new ArrayList<IEqualValueUpBill>();
      for(int j=0,jloop = voUpInfos.length;j<jloop;j++){
      String csourcebid = (String)((CircularlyAccessibleValueObject)voUpInfos[j]).getAttributeValue(csourcebodyid);
      if(!hssourcebid.contains(csourcebid)){
        hssourcebid.add(csourcebid);
        //Ҫ��ȥ�ۼ�;������
        if (hswastagenum.containsKey(csourcebid)) {
                UFDouble wastagenum = hswastagenum.get(csourcebid);
                CircularlyAccessibleValueObject voUpInfo = (CircularlyAccessibleValueObject) voUpInfos[j];
                // ���� = ���ε������� - �ۼ�;������
                voUpInfo.setAttributeValue("nnumber", SmartVODataUtils
                    .getUFDouble(voUpInfo.getAttributeValue("nnumber")).sub(
                        wastagenum));
                String price = "noriginalcurnetprice";
                String mny = "noriginalcurmny";
                // ��˰����
                if (getBTaxPrior().booleanValue()) {
                  price = "noriginalcurtaxnetprice";
                  mny = "noriginalcursummny";
                }
                voUpInfo.setAttributeValue(mny, SmartVODataUtils.getUFDouble(
                    voUpInfo.getAttributeValue(mny)).sub(
                    wastagenum.multiply(SmartVODataUtils.getUFDouble(voUpInfo
                        .getAttributeValue(price)))));

              }
        arytemp.add(voUpInfos[j]);
      }
    }
      voUpInfos = arytemp.toArray(new IEqualValueUpBill[0]);
				UpToDownEqualValueTool
						.setValueEqualToUpBillForStore(
								cfirsttype,
								"32",
								getBTaxPrior().booleanValue() ? IPriority.Priority_Tax
										: IPriority.Priority_NoTax,
								voUpInfos, voListOtherDownInf, ievdb);
			}
		}

		// ����β���֮�󣬿��ܵ��еļ�˰�ϼơ�˰���˰���֮��Ĺ�ϵ�����㣬������Ҫ���µ���
		try {
      for(int j =0,jloop = aryRetVO[i].getBodyVO().length;j<jloop;j++){
       SaleinvoiceBVO saleItem  =aryRetVO[i].getBodyVO()[j];
      //���ö����ۿ� = �����ۿ� * ��Ʒ�ۿ�
      UFDouble ndiscountrate = saleItem.getNdiscountrate() == null?new UFDouble(100):saleItem.getNdiscountrate();
      UFDouble nitemdiscountrate = saleItem.getNitemdiscountrate() == null?new UFDouble(100):saleItem.getNitemdiscountrate();
      saleItem.setNorderDiscount(ndiscountrate.multiply(nitemdiscountrate).div(new UFDouble(100)));
      }
			if (getBTaxPrior().booleanValue()) {
        
				nc.vo.scm.relacal.SCMRelationsCal
						.calculate(
								aryRetVO[i].getChildrenVO(),
								aryRetVO[i].getParentVO(),
								new int[] {
										RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE,
										RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE,
										RelationsCalVO.YES_LOCAL_FRAC },
								"noriginalcursummny", SaleinvoiceBVO
										.getDescriptionsTax(),
                    SaleinvoiceBVO.getKeysTax(),new int[]{SCMRelationsCal.TAXPRICE_ORIGINAL,
                                    SCMRelationsCal.PRICE_ORIGINAL,
                                    SCMRelationsCal.NET_TAXPRICE_ORIGINAL,
                                    SCMRelationsCal.NET_PRICE_ORIGINAL});
			} else {
				nc.vo.scm.relacal.SCMRelationsCal
						.calculate(
								aryRetVO[i].getChildrenVO(),
								aryRetVO[i].getParentVO(),
								new int[] {
										RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE,
										RelationsCalVO.ITEMDISCOUNTRATE_PRIOR_TO_PRICE,
										RelationsCalVO.YES_LOCAL_FRAC },
								"noriginalcurmny", SaleinvoiceBVO
										.getDescriptionsNoTax(),
                    SaleinvoiceBVO.getKeysNoTax(),new int[]{SCMRelationsCal.TAXPRICE_ORIGINAL,
                                    SCMRelationsCal.PRICE_ORIGINAL,
                                    SCMRelationsCal.NET_TAXPRICE_ORIGINAL,
                                    SCMRelationsCal.NET_PRICE_ORIGINAL});
			}
		} catch (BusinessException ex) {
			MessageDialog.showErrorDlg(this, "", ex.getMessage());
			return;
		}
		
	}
}
/**
 * ���������������������۷�Ʊ��ͷ���ϼ��ֶε���ֵ���������ñ����еĳ��ǰ��
 * <b>����˵��</b>
 * @param retvos
 * @time 2008-12-8 ����07:08:09
 */
	private void calcTotalMny(SaleinvoiceVO[] newinvoicevos) {
		for(SaleinvoiceVO newinvoicevo:newinvoicevos){
      //���ñ�ͷ��Ʊ�ۿ�Ĭ��ֵΪ100
      newinvoicevo.getHeadVO().setNinvoicediscountrate(new UFDouble(100));
      //��Ʊ��ͷ�ϼƽ��
      UFDouble ntotalsummny = new UFDouble(0);
			for(SaleinvoiceBVO newbvo:newinvoicevo.getBodyVO()){
        newbvo.setNsubsummny(newbvo.getNoriginalcursummny());
        newbvo.setNsubcursummny(newbvo.getNsummny());
        //���˵���Ʒ��
        if(null == newbvo.getBlargessflag() || !newbvo.getBlargessflag().booleanValue())
          ntotalsummny = ntotalsummny.add(newbvo.getNoriginalcursummny() == null ?
              new UFDouble(0):newbvo.getNoriginalcursummny());
        }
      newinvoicevo.getHeadVO().setNtotalsummny(ntotalsummny);
      newinvoicevo.getHeadVO().setNstrikemny(new UFDouble(0));
      newinvoicevo.getHeadVO().setNnetmny(ntotalsummny);
      
			}
	}
	
  /**
	 * ��ѯ����ǰ���ⵥ������ͬԴ���������ⵥ�еĽ����¼
	 * 
	 * @param corder_bid
	 * @param curitemid
	 * @return
	 * @throws Exception
	 */
	private IEqualValueDownBill[] getOtherDownInfo(String corder_bid) throws Exception {
		// ���ݳ��ⵥ��¼����Դ��id����ѯ��Դ�ж�Ӧ������������
		ArrayList<String> csourcebillbids = new ArrayList<String>();
		csourcebillbids.add(corder_bid);
		GeneralBillItemVO[] gbitems = getICServe().getGeneralBillItemVOsByCFBids(csourcebillbids).get(corder_bid)
				.toArray(new GeneralBillItemVO[0]);

		if (gbitems == null || gbitems.length == 0)
			return null;

		ArrayList<String> al_generalbids = new ArrayList<String>();
		for (int i = 0, len = gbitems.length; i < len; i++) {
			al_generalbids.add(gbitems[i].getCgeneralbid());
		}
		String[] str = new String[al_generalbids.size()];
		al_generalbids.toArray(str);
		
		return getOtherDownInfo(str).get(corder_bid)==null?null:getOtherDownInfo(str).get(corder_bid).toArray(new IEqualValueDownBill[0]);
	}
	private IICToSO icserve;

	private IICToSO getICServe() {
		if (icserve == null) {
			icserve = (IICToSO) NCLocator.getInstance().lookup(IICToSO.class.getName());
		}
		return icserve;
	}
  private ServcallVO si = new ServcallVO();
	/**
	 * ��ѯ��ǰ�����Ѿ����ڵ����з�Ʊ��¼
	 * 
	 * @param corder_bids
	 * @return
	 */
	private Hashtable<String,ArrayList<IEqualValueDownBill[]>> getOtherDownInfo(String[] corder_bids) {
		// ��ѯ���н����¼
		try {
			si.setBeanName("nc.impl.scm.so.so002.SaleinvoiceImpl");
			si.setMethodName("queryBodyDataByUpsourcebid");
			si.setParameter(new Object[] { corder_bids });
			si.setParameterTypes(new Class[] { String[].class });
			return (Hashtable<String,ArrayList<IEqualValueDownBill[]>>) LocalCallService.callService("so", new ServcallVO[] { si })[0];
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		return null;
	}
	
	private IEqualValueUpBill[] getSourceItemsFrom4C(String csourcetype,ArrayList<SaleinvoiceBVO> al) {
//		GeneralBillItemVO[] gbitems = preVo.getItemVOs();
//
//		int len = gbitems.length;
//		String csourcetype = gbitems[0].getCsourcetype();
		String[] csourcebids = new String[al.size()];
//
		for (int i = 0; i < al.size(); i++) {
			csourcebids[i] = al.get(i).getCsourcebillbodyid();
		}// end for

		try {
			si.setBeanName("nc.impl.scm.so.pub.UpToDownEqualValueUtil");
			si.setMethodName("queryFirstBodyByBID");
			si.setParameter(new Object[] { csourcetype, csourcebids });
			si.setParameterTypes(new Class[] { String.class, String[].class });
			return (IEqualValueUpBill[]) LocalCallService.callService("so", new ServcallVO[] { si })[0];
		} catch (Exception e) {
			SCMEnv.out(e);
		}

		return null;
	}
	private UFBoolean bTaxPrior;
	private UFBoolean getBTaxPrior() {
		if (bTaxPrior == null) {
			try {
				bTaxPrior = SysInitBO_Client.getParaBoolean(getClientEnvironment().getCorporation().getPk_corp(), "SA02");
			} catch (BusinessException e) {
				e.printStackTrace();
				return UFBoolean.FALSE;
			}
		}

		return bTaxPrior;
	}
  	/**
  	 * @author jianghp
  	 * �������۷�Ʊģ���ϵ�Ĭ�Ϸ�Ʊ��������ʼ��������
  	 * @return
  	 */
  private int getDefaultInvoiceType() {
      
   //ģ�������õ�Ĭ�Ϸ�Ʊ���� 
   String defaultInvoiceType = getBillCardPanel().getHeadItem(
          "finvoicetype").getDefaultValue();
   //����û�����Ĭ�Ϸ�Ʊ����
   if(defaultInvoiceType != null && defaultInvoiceType.trim().length() >0){
		try {
      String[][] invoiceType = SaleinvoiceBO_Client.getInvoiceType();
			for (int i = 0,loop = invoiceType.length; i < loop; i++) 
			  if (defaultInvoiceType.trim().equals(invoiceType[i][1].trim()))
					return i;
		} catch (Exception e) {
			SCMEnv.out(e);
		}
   }
		return 0;
	}

/**
   * ���淢Ʊ���� ����Щ������Ҫ�����쳣���������쳣ʱ�����쳣
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @return
   * <p>
   * @author wangyf
   * @time 2007-7-31 ����10:59:13
   */
  private String onSave() {

    // �õ��������VO��ͬʱ�����˼���
    SaleinvoiceVO voSaved = getBillCardPanel().getSaveVO();
    // �õ��Ĵ�����VOΪ��,����
    if (null == voSaved) 
      return null;
    //����ǰ̨clientlink
    voSaved.setCl(new ClientLink(getClientEnvironment()));
    //���ó�����ܿ�Ʊ
    voSaved.setIsOutSumMakeInvoice(getBillCardPanel().isOutSumMakeInvoice());
   
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
        "UPP40060501-000080")/* @res "��ʼ��������...." */);
    nc.vo.scm.pub.SCMEnv.out("��ʼ���棺" + System.currentTimeMillis());

//    ArrayList<String> listBillID = null;
    SaleinvoiceVO newinvoicevo = null;
    String sMessage = "";
    try {
      //  ���ο�����չ
      getPluginProxy().beforeAction(nc.vo.scm.plugin.Action.SAVE, new SaleinvoiceVO[]{voSaved});
      if (getBillCardPanel().isNewBill()) {
        newinvoicevo = (SaleinvoiceVO) PfUtilClient
            .processAction(this, ISaleInvoiceAction.PreSave,
                SaleBillType.SaleInvoice, getClientEnvironment().getDate()
                    .toString(), voSaved, null, null, null);

      }else {
        newinvoicevo = (SaleinvoiceVO) PfUtilClient
            .processActionNoSendMessage(this, ISaleInvoiceAction.PreModify,
                SaleBillType.SaleInvoice, getClientEnvironment().getDate()
                    .toString(), voSaved, null, null, null);
      }
      //    ���ο�����չ
      getPluginProxy().afterAction(nc.vo.scm.plugin.Action.SAVE, new SaleinvoiceVO[]{voSaved});
    }
    catch (Exception e) {
      sMessage = e.getMessage();
      showErrorMessage(sMessage);
      nc.vo.scm.pub.SCMEnv.out(e);

      return sMessage;

    }
    /************��¼ҵ����־*************/
    insertOperLog(voSaved, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC001-0000001")/* @res"����" */);
    /************��¼ҵ����־* end ************/
    
    nc.vo.scm.pub.SCMEnv.out("���������" + System.currentTimeMillis());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
        "UPP40060501-000083"));

    // �����Ʊ�ǶԳ����ɣ�������´˷�Ʊ��Դͷ��Ʊ
    // ԭ�������Ǹ���LIST���˴��޸�Ϊ����CACHE
    String sUpSrcType = (String) getBillCardPanel().getBodyValueAt(0,
        "cupreceipttype");
    if (getBillCardPanel().isNewBill()
        && SaleBillType.SaleInvoice.equals(sUpSrcType)) {
      SaleinvoiceVO voUp = getVOCache().getVO_Load(
          (String) voSaved.getBodyVO()[0].getCupsourcebillid());

      String strWhere = "so_saleinvoice.csaleid = '"
          + voUp.getHeadVO().getCsaleid() + "'";
      // ���¼��ر�ͷTS
      SaleVO[] voNewHead = null;
      try {
        voNewHead = SaleinvoiceBO_Client.queryHeadAllData(strWhere);
      }
      catch (Exception e) {
        SCMEnv.out(e);
        sMessage += e.getMessage();
      }

      if (voNewHead != null && voNewHead.length > 0) {
        voUp.getHeadVO().setTs(voNewHead[0].getTs());
        voUp.getHeadVO().setFcounteractflag(voNewHead[0].getFcounteractflag());
        voUp.setParentVO(voNewHead[0]);
        getVOCache().setSaleinvoiceVO(voUp.getHeadVO().getCsaleid(), voUp);
      }
    }
    //��Ƭ��ˢ�½���ͻ�������
    // ����PANEL
    getBillCardPanel().setPanelAfterSave(newinvoicevo, isInMsgPanel());

    // ���»���
    updateCacheVOByCard();
    //�ָ���ɫ
    nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
    //�������״̬
    getBillCardPanel().setOutSumMakeInvoice(false);

    // ��ť״̬
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
 
    setButtonsStateBrowse();
   

    return sMessage.length() == 0 ? null : sMessage;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2003-11-21 11:26:14)
   * 
   * @return java.util.ArrayList
   */
  protected ArrayList getFormulaItemHeader() {
    ArrayList arylistHeadField = new ArrayList();

    // ��ͷ����β�漰���Ĺ�ʽ
    String[] aryItemField1 = new String[] {
        "deptname", "cdeptname", "cdeptid"
    };
    arylistHeadField.add(aryItemField1);

    String[] aryItemField2 = new String[] {
        "psnname", "cemployeename", "cemployeeid"
    };
    arylistHeadField.add(aryItemField2);

    return arylistHeadField;
  }

  /**
   * ��������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param vo
   * @return
   * @throws Exception
   *           <p>
   * @author wangyf
   * @time 2007-8-2 ����02:57:54
   */
  private boolean onApprove(SaleinvoiceVO vo) throws Exception {

    if (vo == null)
      return false;

    SaleinvoiceVO saleinvoice = vo;

    if (vo.getChildrenVO() != null) {
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
      }
    }
    try {
      if (saleinvoice.getHeadVO().getDbilldate().compareTo(
          getClientEnvironment().getDate()) > 0)
        throw new nc.vo.pub.BusinessException("�������ڲ���С���Ƶ����ڣ�");

      onApproveCheckWorkflow(saleinvoice);
      Object otemp = PfUtilClient.processActionFlow(this,
          ISaleInvoiceAction.Approve, SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), saleinvoice, null);
      if (otemp != null) {
        String ErrMsg = otemp.toString();
        if (ErrMsg != null && ErrMsg.startsWith("ERR")) {
          ShowToolsInThread.showMessage(m_proccdlg, ErrMsg.substring(3));
        }
      }

      if (PfUtilClient.isSuccess()) {
        return true;
      }

      return false;
    }
    catch (Exception e) {
      throw e;
    }

  }

  /**
   * ��õ������͡� �������ڣ�(2001-11-15 8:52:43)
   * 
   * @return java.lang.String
   */
  private nc.ui.scm.sourcebill.SourceBillFlowDlg getSourceDlg() {
    nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
        this, SaleBillType.SaleInvoice,/* ��ǰ�������� */
        ((SaleinvoiceVO) getVo()).getHeadVO().getCsaleid(), getBillCardPanel()
            .getBusiType(),/* ��ǰҵ������ */
        getBillCardPanel().getOperator(),/* ��ǰ�û�ID */
        ((SaleinvoiceVO)getVo()).getHeadVO().getVreceiptcode()/* ���ݺ� */
    );
    return soureDlg;
  }

  public boolean isInMsgPanel() {
    return m_bInMsgPanel;
  }

  private SaleInvoiceQueryDlg getQueryDlg() {
    if (dlgQuery == null) {
      dlgQuery = new SaleInvoiceQueryDlg(this);
    }
    return dlgQuery;
  }

  /**
   * 
   */
  /**
   * ��Ʊִ�����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param bo
   *          <p>
   * @author wangyf
   * @time 2007-3-23 ����03:21:12
   */
  private void onExecQuery() {

    SaleinvoiceVO voInv = (SaleinvoiceVO) getVo();
    if (voInv == null) {
      return;
    }

    try {
      PfUtilClient.processActionNoSendMessage(this,
          ISaleInvoiceAction.ExecReport, nc.ui.scm.so.SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), voInv, voInv.getHeadVO()
              .getCsaleid(), null, null);
    }
    catch (Exception e) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
          "UPP40060501-000069")/* @res "ʧ�ܣ�" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
  }

  private void doDeleteWork() {

    java.util.HashMap hsvos = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    try {
      hsvos = getDeleteSaleInvoiceVOs();// ("Del");
      if (hsvos == null || hsvos.size() <= 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100071")/* @res "��ѡ��������ķ�Ʊ��" */);
        return;
      }
    }
    catch (Exception ev) {
      showErrorMessage(ev.getMessage());
      return;
    }

    int max = m_proccdlg.getUIProgressBar1().getMaximum();
    int maxcount = hsvos.size();

    SaleinvoiceVO saleinvoice = null;
    java.util.Iterator iter = hsvos.keySet().iterator();
    int count = 0;

    while (iter.hasNext()) {
      Object key = iter.next();
      saleinvoice = (SaleinvoiceVO) hsvos.get(key);
      ShowToolsInThread.showStatus(m_proccdlg, new Integer(
          (int) (max * (1.0 * count / maxcount))));

      saleinvoice
          .setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      try {
        if (m_proccdlg.isInterrupted())
          break;

        if (onDelete(saleinvoice)) {

          hSuccess.put(key, saleinvoice);
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100072", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
        }
        else {
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100073", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
        }

      }
      catch (Exception e) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100074", null, new String[] {
              ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += e.getMessage();
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);
        // ShowToolsInThread.showMessage(proccdlg, " ", "��Ʊ["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]����ʧ�ܣ�" + e.getMessage());
        if (m_proccdlg.getckHint().isSelected()) {
          sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100074", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          if (ShowToolsInThread.showYesNoDlg(m_proccdlg, e.getMessage()
              + "\n"
              + sMsg
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
                  "UPP40060501-100075")/*
                                         * @res "�Ƿ�����������µķ�Ʊ��"
                                         */) == MessageDialog.ID_YES) {
            continue;
          }
          else {
            m_proccdlg.Interrupt();
            break;
          }
        }
      }
      finally {
        count++;
      }
    }
    
    ShowToolsInThread.showStatus(m_proccdlg, new Integer(max));

    if (m_proccdlg.isInterrupted()) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100069")/*
                                                                       * @res
                                                                       * "�����������û��жϣ�"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100070")/*
                                                                       * @res
                                                                       * "��������������"
                                                                       */);
    }

    try {
      Thread.sleep(500);
    }
    catch (Exception ex) {

    }

    if (hSuccess.size() > 0) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000054")/*
                                                                       * @res
                                                                       * "���ڸ��½�������..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  private void setInMsgPanel(boolean newBInMsgPanel) {
    m_bInMsgPanel = newBInMsgPanel;
  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.pub.linkoperate.ILinkApprove#doApproveAction(nc.ui.pub.linkoperate.ILinkApproveData)
   */
  public void doApproveAction(ILinkApproveData approvedata) {
    setInMsgPanel(true);
    // ��������
    getBillCardPanel().loadDataWhole(approvedata.getBillID(), true);

    getBillCardPanel().setEnabled(false);



    SaleinvoiceVO vosInvoice = new SaleinvoiceVO(getBillCardPanel().getRowCount());
    getBillCardPanel().getBillValueVO(vosInvoice);
    getVOCache().setCacheData(new SaleinvoiceVO[]{vosInvoice});

    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setShowState(CardShow);
    if (!isInMsgPanel()) {
      setButtonsStateByBillStatue();
    }
    
    setButtonsStateMsgCenterApprove();
  }

  /**
   * IInvoiceListPanel�ӿڷ���
   * 
   * @see nc.ui.so.so002.IInvoiceListPanel#getVOCache()
   */
  public SaleInvoiceVOCache getVOCache() {
    // 
    return m_vocache;
  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.pub.linkoperate.ILinkQuery#doQueryAction(nc.ui.pub.linkoperate.ILinkQueryData)
   */
  public void doQueryAction(ILinkQueryData querydata) {
    // ��������
    getBillCardPanel().loadDataWhole(querydata.getBillID(), isInMsgPanel());
    // ���ݵ���״̬���õ���
    getBillCardPanel().setEnabled(false);
    
    SaleinvoiceVO vosInvoice = new SaleinvoiceVO(getBillCardPanel().getRowCount());
    getBillCardPanel().getBillValueVO(vosInvoice);
    getVOCache().setCacheData(new SaleinvoiceVO[]{vosInvoice});

    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setShowState(CardShow);
    if (!isInMsgPanel()) {
      setButtonsStateByBillStatue();
    }
    setButtonsStateByLinkQueryBusitype();
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2003-11-6 15:19:12)
   * 
   * @param invoicevo
   *          nc.vo.so.so002.SaleinvoiceVO
   */
  private void onApproveCheckWorkflow(SaleinvoiceVO voCheckFlow)
      throws nc.vo.pub.ValidationException {
    try {
      boolean isExist = false;
      isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(
          SaleBillType.SaleInvoice, voCheckFlow.getHeadVO().getCbiztype(),
          // NO_BUSINESS_TYPE,
          getClientEnvironment().getCorporation().getPk_corp(),
          getClientEnvironment().getUser().getPrimaryKey());
      if (isExist == true) {
        int iWorkflowstate = 0;
        iWorkflowstate = nc.ui.pub.pf.PfUtilClient
            .queryWorkFlowStatus(voCheckFlow.getHeadVO().getCbiztype(),
            // /NO_BUSINESS_TYPE,
                SaleBillType.SaleInvoice, voCheckFlow.getParentVO()
                    .getPrimaryKey());

        if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW) {
          throw new nc.vo.pub.ValidationException(nc.ui.ml.NCLangRes
              .getInstance().getStrByID("40060501", "UPP40060501-000070")/*
                                                                           * @res
                                                                           * "�������������˹����������õ���û���ڹ������С�"
                                                                           */);
        }
      }
    }
    catch (Throwable e) {
      throw new nc.vo.pub.ValidationException(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40060501", "UPP40060501-000070")/*
                                                         * @res
                                                         * "�������������˹����������õ���û���ڹ������С�"
                                                         */);
    }
  }

  /**
   * �ϲ���ʾ �������ڣ�
   */
  private void onBillCombin() {
    //fengjb  20080917 �޸ĳ�ʼ������
    SOCollectSettingDlg dlg = new SOCollectSettingDlg(this,SaleBillType.SaleInvoice,
        SaleInvoiceTools.getNodeCode(),SaleInvoiceTools.getLoginPk_Corp(),SaleInvoiceTools.getLoginUserId(),
        SaleinvoiceVO.class.getName(),SaleVO.class.getName(),SaleinvoiceBVO.class.getName());
    
    dlg.setBilltype(ScmConst.SO_Invoice);
    dlg.setNodecode(SaleInvoiceTools.getNodeCode());
    
    //  v55 �ϲ���ʾ������ ���Ӵ����ϲ�����
    Configuration configuation =  nc.vo.scm.goldtax.Configuration.load(getClientEnvironment().getCorporation().getPk_corp());
    ArrayList<String> aryfixgroup = new ArrayList<String>();
    //�����˰����Ϊ�������ϲ���Ĭ�Ϻϲ���ĿΪ�����������롢����������ơ�����
    if(configuation.isMergeInvClass()){
      aryfixgroup.add("invclasscode");
      aryfixgroup.add("invclassname");
      aryfixgroup.add("ccurrencytypename");
      getBillCardPanel().getBodyItem("invclasscode").setShow(true);
      getBillCardPanel().getBodyItem("invclassname").setShow(true);
    }
    //�����˰����Ϊ������ϲ����߲��ϲ���Ĭ�Ϻϲ���ĿΪ��������롢������ơ�����ͺš�����
    else{
      aryfixgroup.add("cinventorycode");
      aryfixgroup.add("cinventoryname");
      aryfixgroup.add("GG");
      aryfixgroup.add("XX");
      aryfixgroup.add("ccurrencytypename");
     
    }
    //�����ѡ�˰����ۺϲ���Ĭ�Ϻϲ���ʾ��Ŀ���ӣ���˰����
    if(configuation.isMergePrice())
      aryfixgroup.add("noriginalcurtaxnetprice");
    String[] fixgroup = new String[aryfixgroup.size()];
    aryfixgroup.toArray(fixgroup);
//    new String[] {
//        "noriginalcurtaxnetprice"
//      }
    
    dlg.initData(getBillCardPanel(), fixgroup,null , new String[] {
        "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny",
        "nnumber", "noriginalcurdiscountmny", "ntaxmny", "nmny", "nsummny",
        "ndiscountmny", "nsimulatecostmny", "ncostmny", "nsubsummny",
        "nsubcursummny"
    }, null, new String[] {
        "nsubtaxnetprice", "nsubqutaxnetpri", "nsubqunetpri", "nsubqutaxpri",
        "nsubqupri", "nqutaxnetprice", "nqunetprice", "nqutaxprice",
        "nquprice", "nqocurtaxnetpri", "nquoricurnetpri", "nquoricurtaxpri",
        "nquoricurpri", "ntaxnetprice", "nnetprice", "ntaxprice", "nprice",
        "noriginalcurtaxnetprice", "noriginalcurnetprice",
        "noriginalcurtaxprice", "noriginalcurprice"
    }, "nnumber");
    dlg.showModal();
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
        "UPT40060501-000058")/* @res "�ϲ���ʾ" */);

  }


	private void onDocument() {
		showHintMessage(getBtns().m_boDocument.getHint());

		String id;
		if (getShowState() == ListShow) {
			int selectrow = getBillListPanel().getHeadTable().getSelectedRow();
			id = getBillListPanel().getHeadBillModel().getValueAt(selectrow, "csaleid").toString();
		} else {
			id = getBillCardPanel().getHeadItem("csaleid").getValue();
		}

		DocumentManager.showDM(this, SaleBillType.SaleInvoice, id);
	}

  /**
	 * ���� �������ڣ�(2001-6-1 13:12:36)
	 */
  private void onOrderQuery() {
    showHintMessage(getBtns().m_boOrderQuery.getHint());
    getSourceDlg().showModal();
  }

  private PrintLogClient getPrintLogClient() {
    if (m_PrintLogClient == null) {
      m_PrintLogClient = new PrintLogClient();
      m_PrintLogClient.addFreshTsListener(this);
    }
    return m_PrintLogClient;
  }

  /**
   * ������������һ�ŷ�Ʊ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param vo
   * @return
   * @throws Exception
   *           <p>
   * @author wangyf
   * @time 2007-8-7 ����03:27:34
   */
  private boolean onUnApprove(SaleinvoiceVO vo) throws Exception {
    if (vo == null)
      return false;
    SaleinvoiceVO saleinvoice = vo;
    if (vo.getChildrenVO() != null) {
      for (int i = 0; i < vo.getChildrenVO().length; i++) {
        vo.getChildrenVO()[i].setStatus(VOStatus.UNCHANGED);
      }
    }
    try {
      PfUtilClient.processActionFlow(this, ISaleInvoiceAction.UnApprove,
          SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), saleinvoice, null);
    }
    catch (Exception e) {
      throw e;
    }

    if (PfUtilClient.isSuccess()) {
      /************��¼ҵ����־*************/
      insertOperLog(saleinvoice, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000028")/* @res"����" */);
      /************��¼ҵ����־* end ************/
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * ���ݴ�ӡ��ˢ��ts(2004-12-01 23:25:18)
   * 
   * @param
   */
  public void freshTs(String sBillID, String sTS, Integer iPrintCount) {
    if (sTS == null || sTS.trim().length() <= 0)
      return;
    if(ListShow == getShowState()){
      
      if (null == sBillID || sBillID.trim().length() <= 0)
        return;
      String csaleid = null;
      for (int i = 0, iloop = getBillListPanel().getHeadTable().getRowCount(); i < iloop; i++) {
        csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
        if (sBillID.equals(csaleid)) {
          getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");
          getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");
          break;
        }
      }
    
    }else{
    getBillCardPanel().setHeadItem("ts", sTS);
    getBillCardPanel().setTailItem("iprintcount", iPrintCount);
    String csaleid = null;
    for (int i = 0, iloop = getBillListPanel().getHeadTable().getRowCount(); i < iloop; i++) {
      csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
      if (sBillID.equals(csaleid)) {
        getBillListPanel().getHeadBillModel().setValueAt(sTS, i, "ts");
        getBillListPanel().getHeadBillModel().setValueAt(iPrintCount, i, "iprintcount");
        break;
      }
    }
    }
    SaleinvoiceVO ordvo = getVOCache().getVO_Load(sBillID);
    if (ordvo != null) {
      ordvo.getHeadVO().setIprintcount(iPrintCount);
      ordvo.getHeadVO().setTs(new UFDateTime(sTS.trim()));
    }
  }

  /**
   * ��������������V55�޸�Ϊʹ��billprinttoolsʵ��֧�ֶ�ģ��ѡ��
   * <b>����˵��</b>
   * @param previewflag
   * @author fengjb
   * @time 2008-11-26 ����01:13:02
   */
  private void onPrint(boolean previewflag) {
    
    boolean total = getBillCardPanel().getBodyPanel().isTatolRow();
    //����ӡС�ƺϼ��У�����û���ӡ��������
    getBillCardPanel().getBodyPanel().setTotalRowShow(false);
  
    try {
      ArrayList<SaleinvoiceVO> alPrintVO = new ArrayList<SaleinvoiceVO>();
      //�б���ʾʱ
      if (ListShow == getShowState() && !previewflag) {
        //�б���ѡ����
        int selectRows[] = getBillListPanel().getHeadTable().getSelectedRows();
        for (int i = 0,iloop = selectRows.length; i < iloop; i++) {
            //��������
            getBillCardPanel().loadCardData(
                getVOCache().getVO_Load(selectRows[i]));
      
            SaleinvoiceVO saleinvoice = (SaleinvoiceVO) getBillCardPanel()
            .getBillValueVO(SaleinvoiceVO.class.getName(),
                SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
             alPrintVO.add(saleinvoice);
        }
      }else{
      if(ListShow == getShowState()){
        //�б���ѡ����
        int selectRows[] = getBillListPanel().getHeadTable().getSelectedRows();
        //��������
        getBillCardPanel().loadCardData(
            getVOCache().getVO_Load(selectRows[0]));
      }
      SaleinvoiceVO saleinvoice = (SaleinvoiceVO) getBillCardPanel()
          .getBillValueVO(SaleinvoiceVO.class.getName(),
              SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
      alPrintVO.add(saleinvoice);
      }
     
      BillPrintTool printTool = new BillPrintTool(this, 
          SaleInvoiceTools.getNodeCode(), alPrintVO, getBillCardPanel().getBillData(),
          null, SaleInvoiceTools.getLoginPk_Corp(), SaleInvoiceTools
          .getLoginUserId(), "vreceiptcode", "csaleid");
    

      if(previewflag){
        printTool.onCardPrintPreview(getBillCardPanel(), getBillListPanel(), SaleBillType.SaleInvoice);
      }else{
        if(ListShow == getShowState())
          printTool.onBatchPrint(getBillListPanel(), SaleBillType.SaleInvoice,getPrintLogClient());
        else
          printTool.onCardPrint(getBillCardPanel(),getBillListPanel(), SaleBillType.SaleInvoice);
      }
      showHintMessage(getPrintLogClient().getPrintResultMsg(previewflag));
    }catch(Exception e){
      SCMEnv.out(e);  
    }
    finally {
      getBillCardPanel().getBodyPanel().setTotalRowShow(total);
    }
  }
  /**
   * ����������������������ɾ��һ�����۷�Ʊ��
   * <b>����˵��</b>
   * @param vo
   * @return
   * @throws Exception
   * @author fengjb
   * @time 2008-11-26 ����10:17:25
   */
  private boolean onDelete(SaleinvoiceVO vo) throws Exception {

    if (vo == null)
      return false;

    vo.getHeadVO().setVoldreceiptcode(vo.getHeadVO().getVreceiptcode());
    vo.setCl(new ClientLink(getClientEnvironment()));
    SaleVO newsourheadvo = null;
    try {
      newsourheadvo = (SaleVO)PfUtilClient.processActionFlow(this, ISaleInvoiceAction.BlankOut,
          SaleBillType.SaleInvoice,
          getClientEnvironment().getDate().toString(), vo, null);
    }
    catch (Exception e) {
      throw e;
    }
    // ��Ʊ�ǶԳ����� �����б��ϵ�ԭ��Ʊ�ĶԳ���
    SaleVO blankhead = vo.getHeadVO();
    if (SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN == blankhead.getFcounteractflag()
        && null != newsourheadvo) {
    String newbillid = newsourheadvo.getCsaleid();
      for (int i = 0,iloop = getBillListPanel().getHeadBillModel().getRowCount(); i <iloop; i++) {
        String currentpk = (String)getBillListPanel().getHeadBillModel().getValueAt(
            i, "csaleid");
        if (newbillid.equals(currentpk)) {
            SaleinvoiceVO oldvo = getVOCache().getVO_Load(newbillid);
            if (oldvo != null) {
              oldvo.getHeadVO().setTs(newsourheadvo.getTs());
              oldvo.getHeadVO().setFcounteractflag(newsourheadvo.getFcounteractflag());
              getVOCache().setSaleinvoiceVO(newbillid, oldvo);
              // ���ı�ͷ����
              getBillListPanel().getHeadBillModel().setValueAt(
                  newsourheadvo.getTs(), i, "ts");
              getBillListPanel().getHeadBillModel().setValueAt(
                  newsourheadvo.getFcounteractflag(), i, "fcounteractflag");
          }
        }
      }
    }
    if (PfUtilClient.isSuccess()) {
      /************��¼ҵ����־*************/
      insertOperLog(vo, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000039")/* @res"ɾ��" */);
     /************��¼ҵ����־* end ************/
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsopreor",
          "UPPscmsopreor-000055")/* @res "���ϳɹ���" */);
      return true;
    }

    return false;
  }

  /*****************************************************************************
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:2003-09-27 ���ߣ���С��
   ****************************************************************************/
  private void onSendAudit() {
	if(getOperationState() != ISaleInvoiceOperState.STATE_BROWSE)
     onSave();
  SaleinvoiceVO voCur = null;
    //���Ҫ�����VO
   if (getShowState() == ListShow) {
     voCur = getBillListPanel().getSelectedVO();
   }
   else if (getShowState() == CardShow) {
     voCur = getBillCardPanel().getVO();
   }       

   if (null == voCur || null == voCur.getParentVO()) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "��ѡ�񵥾�" */);
    } else {
      try {
        boolean isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(
            SaleBillType.SaleInvoice, voCur.getHeadVO().getCbiztype(),
            getClientEnvironment().getCorporation().getPk_corp(),
            getClientEnvironment().getUser().getPrimaryKey());
        if (! isExist) {
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "SCMCOMMON", "UPPSCMCommon-000111")/*
                                                   * @res "�ò���Աû������������"
                                                   */);
          return;
        }

        int iWorkflowstate = 0;
        iWorkflowstate = nc.ui.pub.pf.PfUtilClient.queryWorkFlowStatus(voCur
            .getHeadVO().getCbiztype(), SaleBillType.SaleInvoice, voCur
            .getParentVO().getPrimaryKey());
        if (iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.BILL_NOT_IN_WORKFLOW
            && iWorkflowstate != nc.vo.pub.pf.IWorkFlowStatus.NOT_APPROVED_IN_WORKFLOW) {
          if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.NOT_STARTED_IN_WORKFLOW)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000077")/*
                                                     * @res "�����ѷ��͵��������У�����δ��ʼ����"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_FINISHED)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000078")/*
                                                     * @res "�������������"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_ON_PROCESS)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000079")/*
                                                     * @res "��������������"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000112")/*
                                                     * @res "�õ�������û�����ù�����"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.ABNORMAL_WORKFLOW_STATUS)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000277")/*
                                                     * @res "δ֪���쳣״̬"
                                                     */);
          return;
        }

        HashMap<String,SmartVO> hsnewvo = (HashMap)PfUtilClient.
        processAction(ISaleInvoiceAction.SendAudit,SaleBillType.SaleInvoice, 
            getClientEnvironment().getDate().toString(), voCur);

        if (PfUtilClient.isSuccess()) {
          setButtonsStateBrowse();
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40060301", "UPP40060301-000291")/*
                                                 * @res "����ɹ�!"
                                                 */);
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40060301", "UPP40060301-000292")/*
                                                 * @res "��������Ѿ����û�ȡ����"
                                                 */);
        }
        //ˢ��ǰ̨����
        updateUIValue(hsnewvo);
        
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40060301", "UPP40060301-000291")/*
             * @res "����ɹ�!"
             */);
      }
      catch (Exception e) {
        showWarningMessage(e.getMessage()
            + nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                "UPPSCMCommon-000408")/*
                                       * @res "����ʧ�ܣ�"
                                       */);
      }
    }
  }

  /**�����������������µ�ǰ����UI���ݺ����ݿ���һ�¡�
   * <b>����˵��</b>
   * @param voCur
   * @param hsts
   * @author fengjb
   * @time 2008-11-19 ����11:26:12
   */
  private void updateUIValue(HashMap<String, SmartVO> hsnewvo) {
    
    //Ҫ����ֵΪ��
    if(hsnewvo == null || hsnewvo.size() ==0)
      return;
    
    //�б������
    if (getShowState() == ListShow) {
      //ˢ�½�����ʾ
      getBillListPanel().updateUIValue(hsnewvo);
      //ˢ�»���
      updateCacheVOByList();
      
      getBillListPanel().updateUI();
    }
    else if (getShowState() == CardShow) {
      // ����Ϊ������״̬
      getBillCardPanel().updateUIValue(hsnewvo);
      updateCacheVOByCard(); 
      
      getBillCardPanel().updateValue();
    }       
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();
    
  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.so.so002.IInvoiceCardPanel#setOperationState(int)
   */
  public void setOperationState(int iState) {
    m_iOperationState = iState;
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2001-4-20 11:21:59) �޸����ڣ�2003-9-8 �޸��ˣ����� �޸����ڣ�2003-11-07
   * �޸��ˣ����� �޸����ݣ������ⵥ�Ų�ѯ �޸����ڣ�2003-12-02 �޸����ݣ����ӻ��� �޸����ڣ�2003-12-12 �޸��ˣ�����
   * �޸����ݣ������Զ���������
   */
  private void onQuery() {

    if (getQueryDlg().showModal() == QueryConditionClient.ID_CANCEL)
      return;

    m_bEverQuery = true;

    onRefresh();
  }

  /**
   * ���ص�ǰ����״̬
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b> iStatie ����״̬
   * 
   * @return ҵ������
   *         <p>
   * @author wangyf
   * @time 2007-3-6 ����10:33:09
   */
  public int getOperationState() {

    return m_iOperationState;
  }

  /**
   * �˴����뷽��˵���� ��������: �������: ����ֵ: �쳣����: ����:
   */
  private void onAuditFlowStatus() {
    SaleinvoiceVO hvo = null;
    if (getShowState() == ListShow) hvo = getBillListPanel().getSelectedVO();
    else hvo = (SaleinvoiceVO) getBillCardPanel().getVO();
    if (hvo == null || hvo.getParentVO() == null) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "��ѡ�񵥾�" */);
    }
    else {
      SaleVO header = (SaleVO) hvo.getParentVO();
      String pk = header.getCsaleid();
      if (pk == null) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000067")/* @res "���ݺ�Ϊ��" */);
      }
      else {
        nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
            this, "32", pk);
        approvestatedlg.showModal();
      }
    }
  }

  /**
   * �ϲ���Ʊ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-7-30 ����04:45:46
   */
  private void onUnite() {
    if (getShowState() == ListShow) {

      String sCode = getVOCache().getCurrentVO().getHeadVO().getVreceiptcode();

      if (getBillListPanel().autoUnit()) {

        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100051", null, new String[] {
              sCode
            }/**��Ʊ[{0}]û�з��������ĳ�Ӧ�յ�**/);
        showHintMessage(sMsg);
      }
      if(getOperationState() == ISaleInvoiceOperState.STATE_EDIT) setButtonsStateEdit();
      else setButtonsStateBrowse();
    }
    else {
      if (!getBillCardPanel().unite()) {
        return;
      }

      setButtonsStateEdit();
    }

    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
        "UPT40060501-000048")/* @res "�ϲ���Ʊ" */);
  }

  /**
   * �˴����뷽��˵���� �������ڣ�(2004-02-06 10:38:09)
   * 
   * @param bo
   *          nc.ui.pub.ButtonObject
   */
  private void onUniteCancel() {

	if(getShowState() == CardShow){
	    if (!getBillCardPanel().uniteCancel()) {
	      return;
	    }
	    
	}else{
		onModify();
		onUniteCancel();
	}
    setButtonsStateEdit();
  }

  /**
   * �رմ��ڵĿͻ��˽ӿڡ����ڱ���������ɴ��ڹر�ǰ�Ĺ�����
   * 
   * @return boolean ����ֵΪtrue��ʾ�����ڹرգ�����ֵΪfalse��ʾ�������ڹرա� �������ڣ�(2001-8-8
   *         13:52:37)
   */
  public boolean onClosing() {
    if (getOperationState() == ISaleInvoiceOperState.STATE_EDIT) {
      int nReturn = MessageDialog.showYesNoCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "��ʾ" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH001")/* @res "�Ƿ񱣴����޸ĵ����ݣ�" */);
       
      if(nReturn == MessageDialog.ID_YES){
    	  onSave();
    	  return true;
      }else if(nReturn == MessageDialog.ID_NO){
    	  return true;
      }
      return false;
    }
    return true;
  }

  /**
   * ����ʵ�ָ÷�������Ӧ��ť�¼���
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */

  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    // �������޸� ��ʾ
    showHintMessage("");
  
    try {
		//  ���ο�����չ
		getPluginProxy().beforeButtonClicked(bo);
  } catch (BusinessException e) {
    SCMEnv.out(e);
    showErrorMessage(e.getMessage());
    return;
  }

    // �б�ѡ���д���
    if (getShowState() == ListShow) {
      if (getBillListPanel().getHeadTable().getSelectedRowCount() > 1) {
        if (bo == getBtns().m_boApprove || bo == getBtns().m_boBlankOut
            || bo == getBtns().m_boUnApprove || bo == getBtns().m_boUnite
            || bo == getBtns().m_boUniteCancel) {
          m_proccdlg = new ProccDlg(this, bo.getHint(), this, bo.getTag());
          m_proccdlg.showModal();
          return;
        }
        // ????? �˴�����ȷ��
        // else {
        // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        // "40060501", "UPP40060501-000072")/*
        // * @res "��ѡ����"
        // */);
        // return;
        // }

      }
      // else {
      // if (bo != getBtns().m_boLocal
      // && bo != getBtns().m_boQuery
      // && bo != getBtns().m_boDocument) {
      // showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
      // "40060501", "UPP40060501-000071")/*
      // * @res "��ѡ��һ������"
      // */);
      // return;
      // }
      //
      // }
      // }
    }
    else {
      getBillCardPanel().stopEditing();
    }

    // ҵ������
    if (bo.getParent() == getBtns().m_boBusiType) {
      onBusiType(bo);
    }
    // ����
    else if (bo.getParent() == getBtns().m_boAdd) {
      // ������ܿ�Ʊ
      if (bo == getBtns().m_boGather) {
        onOutSumMakeInvoice();
      }
      else {
        onNew(bo);
      }
    //��������
    }else if(bo.getParent() == getBtns().m_boRefAdd){
    	onRefAddLine(bo);
    }
    //ȡ�ɱ���
    else if(bo == getBtns().m_boFetchCost ){
    	onFetchCost();
    }
    // ����
    else if (bo == getBtns().m_boSave)
      onSave();
    // ����
    else if (bo == getBtns().m_boApprove)
      onApprove();
    // �л�
    else if (bo == getBtns().m_boCard) {
      if (getShowState() == CardShow) {
        onList();
      }
      else {
        onCard();
      }
    }
    // ά��
    else if (bo.getParent() == getBtns().m_boMaintain) {
      if (bo == getBtns().m_boModify)
        onModify();
      else if (bo == getBtns().m_boCancel)
        onCancelSave();
      else if (bo == getBtns().m_boBlankOut)
        onBlankOut();
      else if (bo == getBtns().m_boCancelTransfer) {
        onCancelTransfer();
      }
      else if (bo == getBtns().m_boUnite) {
        onUnite();
      }
      else if (bo == getBtns().m_boUniteCancel)
        onUniteCancel();
    }
    // �в���
    else if (bo.getParent() == getBtns().m_boLineOper) {
      if (bo == getBtns().m_boDelLine) {
        getBillCardPanel().actionDelLine();

        setOperationState(ISaleInvoiceOperState.STATE_EDIT);
        setButtonsStateEdit();
      }
      else if (bo == getBtns().m_boAddLine) {
        iAddButn = true;
        getBillCardPanel().actionAddLine();

        setOperationState(ISaleInvoiceOperState.STATE_EDIT);
        setButtonsStateEdit();
        iAddButn = false;
      }
      else if(bo == getBtns().m_boCopyLine){
        getBillCardPanel().actionCopyLine();
        setOperationState(ISaleInvoiceOperState.STATE_EDIT);
        setButtonsStateEdit();
      }
      else if(bo == getBtns().m_boPasteLine){
        getBillCardPanel().actionPasteLine();
        setOperationState(ISaleInvoiceOperState.STATE_EDIT);
        setButtonsStateEdit();
      }
      else if(bo == getBtns().m_boInsertLine){

        getBillCardPanel().actionInsertLine();
        setOperationState(ISaleInvoiceOperState.STATE_EDIT);
        setButtonsStateEdit();
      
      }
      else if(bo == getBtns().m_boPasteLineTail){
        getBillCardPanel().actionPasteLineToTail();
        setOperationState(ISaleInvoiceOperState.STATE_EDIT);
        setButtonsStateEdit();
      }
      else if(bo == getBtns().m_boCardEdit){
        getBillCardPanel().startRowCardEdit();
        setOperationState(ISaleInvoiceOperState.STATE_EDIT);
        setButtonsStateEdit();
      }
      else if(bo == getBtns().m_boReRowNO){
        nc.ui.scm.pub.report.BillRowNo.addNewRowNo(getBillCardPanel(), 
            SaleBillType.SaleInvoice, "crowno");
      }
        
    }
    // ִ��
    else if (bo.getParent() == getBtns().m_boAction) {
      if (bo == getBtns().m_boSendAudit)
        onSendAudit();
      else if (bo == getBtns().m_boUnApprove) {
        onUnApprove();
      }
    }
    // ��ѯ
    else if (bo == getBtns().m_boQuery) {
      onQuery();
      return;
    }
    // ���
    else if (bo.getParent() == getBtns().m_boBrowse) {
      if (bo == getBtns().m_boRefresh) {
        onRefresh();
      }
      else if (bo == getBtns().m_boLocal) {
        onLocal();
      }
      else if (bo == getBtns().m_boFirst) {
        onFirst();
      }
      else if (bo == getBtns().m_boPrev) {
        onPrev();
      }
      else if (bo == getBtns().m_boNext) {
        onNext();
      }
      else if (bo == getBtns().m_boLast) {
        onLast();
      }
      else if (bo == getBtns().m_boSelectAll) {
        onSelectAll();
      }
      else if (bo == getBtns().m_boUnSelectAll) {
        onUnSelectAll();
      }
    }
    // ��ӡ
    else if (bo.getParent() == getBtns().m_boPrintManage) {
      if (bo == getBtns().m_boPrint)
        onPrint(false);
      else if (bo == getBtns().m_boPreview) {
        onPrint(true);
      }
      // �ϲ���ʾ
      else if (bo == getBtns().m_boBillCombin)
        onBillCombin();
    }
    // ������ѯ
    else if (bo.getParent() == getBtns().m_boAssistant) {
      if (bo == getBtns().m_boOrderQuery)
        onOrderQuery();
      else if (bo == getBtns().m_boExecRpt)
        onExecQuery();
      else if (bo == getBtns().m_boATP)
        onATP();
      else if (bo == getBtns().m_boAuditFlowStatus)
        onAuditFlowStatus();
      else if (bo == getBtns().m_boCustInfo)
        onCustInfo();
      else if (bo == getBtns().m_boCustCredit)
        onCustCredit();
      else if (bo == getBtns().m_boPrifit)
        onPrifit();
    }
    // ��������
    else if (bo.getParent() == getBtns().m_boAssistFunction) {
      if (bo == getBtns().m_boOpposeAct) {
        onOpposeAct();
      }
      else if (bo == getBtns().m_boImportTaxCode) {
    	  onImportTaxCode();
      }
      else if (bo == getBtns().m_boSoTax) {
        onSoTax();
      }
      else if (bo == getBtns().m_boDocument) {
        onDocument();
      }
    }

    try{
	//  ���ο�����չ
	getPluginProxy().afterButtonClicked(bo);
    } catch (BusinessException e) {
        SCMEnv.out(e);
        showErrorMessage(e.getMessage());
      }
	
    if (!bo.getCode().equals(getBtns().m_boFetchCost.getCode()) && existErrRows) {
			SetColor.resetColor(getBillCardPanel(),getBillCardPanel().getRowCount());
			existErrRows=false;
		}
  }
  /**
   * ����������������Ƭ�༭����ʵ�֡�
   * <b>����˵��</b>
   * @author fengjb
   * @time 2008-9-11 ����01:36:38
   */
  private void onBoCardEdit(){
    getBillCardPanel().startRowCardEdit();
 }

  /**
   * ��������������V55���۷�Ʊ֧�����������ۣ����ӡ�ȡ�ɱ��ۡ���ť��
   * <b>����˵��</b>
   * @author fengjb
   * @time 2008-7-29 ����08:03:38
   */
  private void onFetchCost() {
    //ȡ��ǰ����VOֵ
  	SaleinvoiceVO saleinvoice = (SaleinvoiceVO)getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(),
  			SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
    
  	if(null == saleinvoice  || null == saleinvoice.getHeadVO()
  			|| null == saleinvoice.getBodyVO())
  		return ;
  	
   SaleinvoiceBVO[] body = saleinvoice.getBodyVO();
   Hashtable<String,Integer> saleout = new Hashtable<String,Integer>();
   ArrayList<String> aryrowno = new ArrayList<String>();
   ArrayList<Integer> errindex = new ArrayList<Integer>();
   for(int i=0,iloop = body.length;i<iloop;i++){
	   //������۷�Ʊ��Դ�����۳��ⵥ��ȡ�ɱ���
	   if(SaleBillType.SaleOutStore.equals(body[i].getCupreceipttype())
         && null != body[i].getCupsourcebillbodyid() ){
		 saleout.put(body[i].getCupsourcebillbodyid(),i);
	   //������Ϊ�Ǵ����У�����ʾ
	   }
   }
   Hashtable hcostprice = new Hashtable();
   Set<String> keyset = saleout.keySet();
   String[] saleoutbid = null;
   //�������������۳��ⵥ�ķ�Ʊ��
   if(keyset.size() > 0){
   try{
   saleoutbid= new String[keyset.size()];
   keyset.toArray(saleoutbid);
   //��ѯ��Ʊ���γ��ⵥ��Ӧ�д�������д�ĵ���
   hcostprice = SaleinvoiceBO_Client.queryCostPrice(saleoutbid);
   }catch(Exception e){
	   SCMEnv.out(e.getMessage());
	   MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
		          .getStrByID("scmcommon", "UPPSCMCommon-000059")/* @res "����" */, e
		          .getMessage());
   }
   //������ѯ�õ��Ľ��
  for(int i=0,iloop =saleoutbid.length;i<iloop;i++){
	  //������ڽ����ϵ��к�
	  int row = saleout.get(saleoutbid[i]);
	  //�۱�����
	  UFDouble nexchangeotobrate = body[row].getNexchangeotobrate();
	  //���ȡ����������д���۳��ⵥ�ı�����˰����
	  if(hcostprice.containsKey(saleoutbid[i]) && 
			  hcostprice.get(saleoutbid[i]) != null){
		  UFDouble ncostprice = new UFDouble((BigDecimal)hcostprice.get(saleoutbid[i]));
		  //ԭ����˰���� = ������˰���� * ����
      UFDouble noriginalcurprice = null;
      try{
      String pk_corp = saleinvoice.getHeadVO().getPk_corp();
      BusinessCurrencyRateUtil currateutil =  new BusinessCurrencyRateUtil(pk_corp);
      SOCurrencyRateUtil socurrateutil = new SOCurrencyRateUtil(pk_corp);
      String pk_curtype = saleinvoice.getHeadVO().getCcurrencyid();
      //����VO
      noriginalcurprice = currateutil.getAmountByOpp(socurrateutil.getLocalCurrPK(),
          pk_curtype,ncostprice, nexchangeotobrate, 
          saleinvoice.getHeadVO().getDbilldate()==null?
              getClientEnvironment().getDate().toString(): saleinvoice.getHeadVO().getDbilldate().toString());
      }catch(Exception e){
        SCMEnv.out("ȡ�ɱ��۹��̳���:"+e);
        showErrorMessage("ȡ�ɱ��۹��̳���:"+e.getMessage()); 
        return;
      }
		  //�����Ƿ�˰
		  if(st.SA_02.booleanValue()){
              //˰��
			  UFDouble ntaxrate = body[row].getNtaxrate()==null?new UFDouble(0):body[row].getNtaxrate();
			  //ԭ�Һ�˰���� = ԭ����˰���� * (1+˰��/100)
			  UFDouble noriginalcurtaxnetprice = noriginalcurprice.multiply(new UFDouble(1).add(ntaxrate.div(new UFDouble(100))));
			  getBillCardPanel().setBodyValueAt(noriginalcurtaxnetprice, row, "noriginalcurtaxnetprice");
			 
		  }else
			  getBillCardPanel().setBodyValueAt(noriginalcurprice, row, "noriginalcurnetprice");
      
      //���¼����������۽��
      //����ԭ�ȵ������ۿ�
       UFDouble ndiscountrate = getBillCardPanel().getBodyValueAt(row, "ndiscountrate") == null? new UFDouble(100):SmartVODataUtils.getUFDouble(getBillCardPanel().getBodyValueAt(row, "ndiscountrate"));
      //�� �����ۿ� * ��Ʒ�ۿ���Ϊ�����ۿ۷ŵ������ۿ��ֶ���
       UFDouble nitemdiscount = getBillCardPanel().getBodyValueAt(row, "nitemdiscountrate") == null? new UFDouble(100):SmartVODataUtils.getUFDouble(getBillCardPanel().getBodyValueAt(row, "nitemdiscountrate"));;
       getBillCardPanel().setBodyValueAt(ndiscountrate.multiply(nitemdiscount).div(new UFDouble(100)), row,"ndiscountrate");
        nc.ui.scm.pub.panel.RelationsCal.calculate(row, getBillCardPanel(), 
            getBillCardPanel().getCalculatePara("noriginalcurtaxnetprice"), "noriginalcurtaxnetprice", 
            SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeys(),
            "nc.vo.so.so002.SaleinvoiceBVO", "nc.vo.so.so002.SaleVO"); 
       //�ָ������ۿ�ֵ
       getBillCardPanel().setBodyValueAt(ndiscountrate, row,"ndiscountrate");
       //�������ó��ǰ���
       getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "noriginalcursummny"), row, "nsubsummny");
       getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "nsummny"), row, "nsubcursummny");
		  
	 //���û��ȡ����������д���۳��ⵥ�ı�����˰����  
	  }else{
      aryrowno.add(body[row].getCrowno());
      errindex.add(row); 
	  }	
	}
   }
  //����û��ȡ������
  if(aryrowno.size() >0){
	  StringBuilder errstr = new StringBuilder();
	  for(int i=0;i<aryrowno.size();i++){
		  errstr.append(">").append(aryrowno.get(i)).append("< û��ȡ���ɱ��� \n");
	  }
    nc.ui.scm.pub.panel.SetColor.setErrRowColor(getBillCardPanel(), errindex);
	  showErrorMessage(errstr.toString()); 
	  existErrRows=true;
  } 
  getBillCardPanel().setHeadItem("ninvoicediscountrate", new UFDouble(100));
  getBillCardPanel().setHeadItem("ntotalsummny", getBillCardPanel().calcurateTotal("noriginalcursummny"));
  getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
  
}

/**
   * �����������������۷�Ʊ֧�ֱ༭״̬׷�ӣ���ɡ��������С����ܡ�
   * <b>����˵��</b>
   * @param bo �����̿ɲ������ӵĵ�������
   * @author fengjb
   * @time 2008-7-23 ����11:08:01
   */
  private void onRefAddLine(ButtonObject bo) {
	    // ���ε�������  
	    String sUpSrcType = null;

	    PfUtilClient.childButtonClicked(bo, SaleInvoiceTools.getLoginPk_Corp(),
	        "40060302", SaleInvoiceTools.getLoginUserId(),
	        SaleBillType.SaleInvoice, this);

	    SaleinvoiceVO[] vosRefAddLine = null;
	    if (!nc.ui.pub.pf.PfUtilClient.makeFlag) {
	      if (PfUtilClient.isCloseOK()) {
	    	  vosRefAddLine = (SaleinvoiceVO[]) PfUtilClient.getRetVos();
	      }
	    }
	    if (vosRefAddLine == null || vosRefAddLine.length == 0)
	      return;
      if(vosRefAddLine[0].getChildrenVO()!=null && vosRefAddLine[0].getChildrenVO().length>0){
        sUpSrcType = (String)vosRefAddLine[0].getChildrenVO()[0].getAttributeValue("cupreceipttype");
      }
	    if (vosRefAddLine[0].getBodyVO() == null)
	      return;
	    vosRefAddLine[0].getHeadVO().setFinvoicetype(new Integer(getDefaultInvoiceType()));
	    
		//��Ʊ�ϲ�
		SaleinvoiceVO[] aryRetVO = null;
		aryRetVO = st.mergeSourceVOs(vosRefAddLine);
   
	   //�õ�����Ҫ�����Ҫ׷�ӵĵ�ǰ��Ƭ�����ϵı���VO����
	   SaleinvoiceBVO[] addToCardBody = checkBeforeRefAdd(aryRetVO);
     if (SaleBillType.SaleOutStore.equals(sUpSrcType)) {
       //�ѵ�ǰ��Ƭ�����ϵ���ǰ�������й��������ݹ��˳������뵽β��������
       SaleinvoiceVO curcardvo = (SaleinvoiceVO) getBillCardPanel().getBillData().getBillValueVO(
           SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
       ArrayList<SaleinvoiceBVO> arynewbvo = new ArrayList<SaleinvoiceBVO>();
       for(SaleinvoiceBVO bvo:curcardvo.getBodyVO()){
         if(bvo.getCinvoice_bid() == null)
           arynewbvo.add(bvo);
       }
       SaleinvoiceVO newinvoiceVO = (SaleinvoiceVO) getBillCardPanel().getBillData().getBillValueVO(
           SaleinvoiceVO.class.getName(), SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
       if(arynewbvo.size() >0){
       int size = arynewbvo.size();
       SaleinvoiceBVO[] newbvo = new SaleinvoiceBVO[arynewbvo.size()+addToCardBody.length];
       for(int i = 0,loop =size;i<loop;i++)
         newbvo[i] = arynewbvo.get(i);
       for(int j=0,loop = addToCardBody.length;j<loop;j++)
         newbvo[j+size] = addToCardBody[j];
       newinvoiceVO.setChildrenVO(newbvo);
       }
       else{
         newinvoiceVO.setChildrenVO(addToCardBody);
       }
       /** �����ο�Ʊβ�� */
     
        dealMny(new SaleinvoiceVO[]{newinvoiceVO});
      }
	   if(addToCardBody.length > 0){
       //���ó�����
       for(SaleinvoiceBVO bvo:addToCardBody){
         bvo.setAttributeValue("nsubsummny", bvo.getAttributeValue("noriginalcursummny"));
         bvo.setAttributeValue("nsubcursummny", bvo.getAttributeValue("nsummny"));
       }
         
		   //��ȡԭ�ȿ�Ƭ������
		   int oldrowcount =getBillCardPanel().getRowCount(); 
		   int[] addlinerow = new int[addToCardBody.length];
		   for(int i =0;i<addToCardBody.length;i++){
			   addlinerow[i] = oldrowcount + i ;
         getBillCardPanel().getBodyPanel().addLine();
		   }
		   getBillCardPanel().getBillModel().setBodyRowVOs(addToCardBody, addlinerow);
		   BillRowNo.addLineRowNos(getBillCardPanel(), ScmConst.SO_Invoice, "crowno", oldrowcount-1, addlinerow);
       //�������������еĸ������༭�Ժͻ�����
       boolean isshow = getBillCardPanel().getBodyItem("cinvclassid").isShow();
       for(Integer rowindex:addlinerow){
         //������״̬
         getBillCardPanel().getBillModel().setRowState(rowindex,BillModel.ADD);
         //������ִ�м��ع�ʽ
         getBillCardPanel().getBillModel().execLoadFormulaByRow(rowindex);
         //�����������ֶ���ʾ��Ϊ��
         if(isshow && null == getBillCardPanel().getBodyValueAt(rowindex, "cinvclassid")){
         String[] invclformul = new String[]{
             "cinvclassid->getColValue(bd_invbasdoc,pk_invcl,pk_invbasdoc,cinvbasdocid)"
         };
         getBillCardPanel().execBodyFormulas(rowindex,invclformul);
         }
         //����������༭��
         getBillCardPanel().setAssistChange(rowindex);
         if (null == getBillCardPanel().getBodyValueAt(rowindex, "cpackunitid")
             || "".equals(getBillCardPanel().getBodyValueAt(rowindex, "cpackunitid")))
           continue;
         getBillCardPanel().initScalefactor(rowindex);
         // ���㸨��������
         String[] appendFormulaViaPrice = {
             "norgviaprice->noriginalcurprice*scalefactor",
             "norgviapricetax->noriginalcurtaxprice*scalefactor"
         };
         getBillCardPanel().execBodyFormulas(rowindex, appendFormulaViaPrice);
         
       }
       getBillCardPanel().initFreeItem();
	   }		
         
   
	      setButtonsStateEdit();
        getBillCardPanel().setHeadItem("ntotalsummny", getBillCardPanel().calcurateTotal("noriginalcursummny"));
        getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
	      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon",
	          "UPPSCMCommon-000350")
	      /*
	       * @res "�༭����..."
	       */
	      );
	  }

private SaleinvoiceBVO[] checkBeforeRefAdd(SaleinvoiceVO[] addvo) {
	if(addvo ==null || addvo.length ==0 ){
	   return null;
	}
  //��ȡ��ͷ�ķ�Ʊ�ۿ�ֵ
  Object temp = getBillCardPanel().getHeadItem("ninvoicediscountrate").getValueObject();
  
  UFDouble ninvoicediscount = temp == null ? new UFDouble(100):SmartVODataUtils.getUFDouble(temp);
  
	SaleinvoiceVO  curbill = (SaleinvoiceVO) getBillCardPanel().getBillValueVO(SaleinvoiceVO.class.getName(), 
			SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
	SaleVO curhead = curbill.getHeadVO();
	SaleVO addhead = null;
	StringBuilder errmsg = new StringBuilder();
    ArrayList<SaleinvoiceBVO> toAddBody = new ArrayList<SaleinvoiceBVO>();
	for(int i=0;i<addvo.length;i++){
		addhead = addvo[i].getHeadVO();
		if(checkHeadItem(curhead.getCreceiptcorpid(),addhead.getCreceiptcorpid())){
			errmsg.append(getErrMsg(addvo[i],"ccustbaseid"));
			continue;
		}
		if(checkHeadItem(curhead.getCsalecorpid(),addhead.getCsalecorpid())){
			errmsg.append(getErrMsg(addvo[i],"csalecorpid"));
			continue;
		}
		if(checkHeadItem(curhead.getCcalbodyid(),addhead.getCcalbodyid())){
			errmsg.append(getErrMsg(addvo[i],"ccalbodyid"));
			continue;
		}
		if(checkHeadItem(curhead.getCcurrencyid(), addhead.getCcurrencyid())){
			errmsg.append(getErrMsg(addvo[i],"ccurrencyid"));
      continue;
		}
		for(int j = 0,loop = addvo[i].getBodyVO().length;j<loop;j++){
      addvo[i].getBodyVO()[j].setNinvoicediscountrate(ninvoicediscount);
      addvo[i].getBodyVO()[j].setNuniteinvoicemny(new UFDouble(0));
			toAddBody.add(addvo[i].getBodyVO()[j]);
		}	
	}
  if(errmsg.length()>0){
	  showErrorMessage(errmsg.toString());
  }
  SaleinvoiceBVO[] bodys = new SaleinvoiceBVO[toAddBody.size()];
	return toAddBody.toArray(bodys);
}
private String getErrMsg(SaleinvoiceVO vo,String key) {
	if(vo == null)
	return "";
	
	StringBuilder errmsg = new StringBuilder();
	String keyname = null;
	if("ccustbaseid".equals(key)){
		keyname = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
    "UC000-0001589")
    /*
     * @res "�ͻ�"
     */;
    
	}else if("csalecorpid".equals(key)){
		keyname = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
    "UC000-0004128")
    /*
     * @res "������֯"
     */;
	}else if("ccalbodyid".equals(key)){
    keyname = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
    "UC000-0001825")
    /*
     * @res "�����֯"
     */;
	}else if("ccurrencyid".equals(key)){
    keyname = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC000-0001755")
      /*
       * @res "����"
       */;
	}
	if(null != keyname){
		for(int i =0,loop =vo.getBodyVO().length;i<loop;i++ ){
      errmsg.append(NCLangRes.getInstance().getStrByID("40060501",
          "UPP40060501-100093", null, new String[] {vo.getBodyVO()[i].getCupsourcebillcode(),
          vo.getBodyVO()[i].getUpsourcerowno(),keyname})).append("\n");
		}
	}
	if(errmsg.length()>0){
		return errmsg.toString();
	}else
		return "";
}

private boolean checkHeadItem(String curstr,String addstr){
	if(curstr ==null || curstr.trim().length()==0 || addstr ==null 
			|| addstr.trim().length()==0 || curstr.equals(addstr)){
		return false;
	}
	return true;
}
/**
   * �õ�����VO�� �������ڣ�(2001-6-23 9:47:36)
   * 
   * @return nc.vo.so.so001.SaleinvoiceVO
   */
  public void deletevoicefromui(String csaleid) {
    try {
      if (getShowState() == ListShow) {

        getBillListPanel().getHeadBillModel().delLine(new int[] {
          getBillListPanel().getHeadTable().getSelectedRow()
        });
        getBillListPanel().getBodyBillModel().clearBodyData();

      }
      else {
        int rowcount = getBillListPanel().getHeadBillModel().getRowCount();
        for (int i = 0; i < rowcount; i++) {
          String id = (String) getBillListPanel().getHeadBillModel()
              .getValueAt(i, "csaleid");
          if (id != null && id.equals(csaleid)) {
            getBillListPanel().getHeadBillModel().delLine(new int[] {
              i
            });
            getBillListPanel().getBodyBillModel().clearBodyData();
          }
        }
        getBillCardPanel().addNew();

      }
      int index = getVOCache().findPos(csaleid);
      getVOCache().removeVOAt(index);
    }
    catch (Exception e) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000256")/* @res "���ݼ���ʧ�ܣ�" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }

  }

  /**
   * ȫѡ
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 ����03:49:18
   */
  private void onSelectAll() {
    if (!getVOCache().isEmpty()) {
      getBillListPanel().getHeadTable().setRowSelectionInterval(0,
          getBillListPanel().getHeadTable().getRowCount() - 1);

      setButtonsStateBrowse();
    }

  }

  /**
   * ȫ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 ����03:49:33
   */
  private void onUnSelectAll() {
    if (!getVOCache().isEmpty()) {
      getBillListPanel().getHeadTable().removeRowSelectionInterval(0,
          getBillListPanel().getHeadTable().getRowCount() - 1);

      setButtonsStateBrowse();
    }

  }

  private void setButtonsStateByLinkQueryBusitype() {
    setButtonsStateBrowse();
    getBtns().m_boAdd.setEnabled(true);
    getBtns().m_boBusiType.setEnabled(true);

    updateButtons();
  }

  /**
   * ˢ��
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 ����03:49:33
   */
  private void onRefresh() {
    // yt add 2003-12-02
    // ����ѯ������뻺��
    SaleVO[] vosInvoice = null;
    try {
      vosInvoice = SaleinvoiceBO_Client.queryHeadAllData(getQueryDlg()
          .getWhere(),getClientEnvironment().getUser().getPrimaryKey(),getQueryDlg().getIsSelAuditing());
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("scmcommon", "UPPSCMCommon-000059")/* @res "����" */, e
          .getMessage());
    }

    // ���û���
    getVOCache().setCacheDataByHeadVOs(vosInvoice);

    // �л����б�
    onList();
  }

  /**
   * ��λ�� �������ڣ�(2001-12-4 10:56:17)
   */
  private void onLocal() {
    nc.ui.scm.pub.report.LocateDialog dlg = new nc.ui.scm.pub.report.LocateDialog(
        this, getBillListPanel().getHeadTable());
    dlg.showModal();
  }

  /**
   * @return m_iShowState
   */
  public int getShowState() {
    return m_iShowState;
  }

  /**
   * @param showState
   *          Ҫ���õ� m_iShowState
   */
  private void setShowState(int showState) {
    m_iShowState = showState;
  }

  /**
   * ����ת����ť
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-7 ����04:40:35
   */
  private void onCancelTransfer() {

    // ????? ��˴�������ɾ�������Ӧ����ѯ��

    // �б��·���ѡ�еĵ����е�δ������
    // ��Ƭ�·������ŵ���
    if (getShowState() == ListShow) {
      int[] iaSerialNum = getBillListPanel().getHeadTable().getSelectedRows();
      for (int i = iaSerialNum.length - 1; i >= 0; i--) {
        // �Ժ���ǰ�ӻ�����ɾ�� AND �ӽ���ɾ��
        if (getVOCache().getVO_Load(iaSerialNum[i]).getHeadVO().isNew()) {
          getVOCache().removeVOAt(iaSerialNum[i]);
        }
      }
      getBillListPanel().getHeadBillModel().delLine(iaSerialNum);
      getBillListPanel().updateUI();
    }
    else {
      // ��ȥ��ǰ���ݣ����Զ���������һ��
      getVOCache().removeVOAt(getVOCache().getPos());
      getBillCardPanel().loadCardData(
          getVOCache().getVO_Load(getVOCache().rollToNextPos()));
    }

    setButtonsStateBrowse();

  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.pub.linkoperate.ILinkMaintain#doMaintainAction(nc.ui.pub.linkoperate.ILinkMaintainData)
   */
  public void doMaintainAction(ILinkMaintainData maintaindata) {
    // m_strID=maintaindata.getBillID();
	  SaleinvoiceVO saleinvoice = getBillCardPanel().loadDataWhole(maintaindata.getBillID(),
        SaleInvoiceCardPanel.Entry_FromMsgPanel);
	 getVOCache().setCacheData(new SaleinvoiceVO[]{saleinvoice});
	 if(saleinvoice.getHeadVO().getDapprovedate() == null)
		 onModify();
	 else
		 onCard();
  }

  /**
   * �ӿڷ��������ϴβ�ѯ����ˢ�»��� LIST����ĳЩ��������Ҫ���°�������ѯ����ʱ���ô˽ӿڷ����� ʹ��ʱ�������Զ��ϲ���Ʊ��
   * 
   * @see nc.ui.so.so002.IInvoiceListPanel#updateCache()
   */
  public void updateCache() {
    getVOCache().setCacheData(null);

    SaleVO[] sales = null;
    try {
      /**�޸� BY fengjb 20080826 V55 ֧�֡�����������ѯ����**/
      if(getQueryDlg().getConditionVO() != null){
    	  sales = SaleinvoiceBO_Client.queryHeadAllData(getQueryDlg().getWhere(),getClientEnvironment().getUser().getPrimaryKey(),getQueryDlg().getIsSelAuditing());
      }
    }
    catch (Exception e) {
      SCMEnv.out(e);
      showErrorMessage(e.getMessage());
    }

    getVOCache().setCacheDataByHeadVOs(sales);
  }

  private void setButtonsStateOPP() {
    getBtns().m_boAction.setEnabled(false);
    getBtns().m_boBrowse.setEnabled(false);
    getBtns().m_boPrint.setEnabled(false);
    getBtns().m_boPreview.setEnabled(false);
    getBtns().m_boModify.setEnabled(false);
    getBtns().m_boCancel.setEnabled(true);
    getBtns().m_boSave.setEnabled(true);
    getBtns().m_boCancelTransfer.setEnabled(false);
    getBtns().m_boPrev.setEnabled(false);
    getBtns().m_boNext.setEnabled(false);
    // getBtns().m_boReturn.setEnabled(false);
    // getBtns().m_boAfterAction.setEnabled(false);
    getBtns().m_boLineOper.setEnabled(false);
    //��������
    getBtns().m_boRefAdd.setEnabled(false);
    //ȡ�ɱ���
    getBtns().m_boFetchCost.setEnabled(false);
//    getBtns().m_boAddLine.setEnabled(false);
//    getBtns().m_boDelLine.setEnabled(false);
//    getBtns().m_boInsertLine.setEnabled(false);
//    getBtns().m_boCopyLine.setEnabled(false);
//    getBtns().m_boPasteLine.setEnabled(false);
    getBtns().m_boDocument.setEnabled(false);
    getBtns().m_boOrderQuery.setEnabled(false);

    getBtns().m_boAuditFlowStatus.setEnabled(false);

    getBtns().m_boBlankOut.setEnabled(false);
    getBtns().m_boModify.setEnabled(false);
    getBtns().m_boApprove.setEnabled(false);
    getBtns().m_boUnApprove.setEnabled(false);
    // getBtns().m_boAfterAction.setEnabled(false);
    // getBtns().m_boStockLock.setEnabled(false);
    getBtns().m_boSendAudit.setEnabled(true);

    // yt add 2004-04-09
    getBtns().m_boUnite.setEnabled(false);
    getBtns().m_boUniteCancel.setEnabled(false);
    getBillCardPanel().setBodyMenuShow(false);
    updateButtons();
  }

  /**
   * ���ñ༭״̬�İ�ť
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-7 ����09:34:04
   */
  private void setButtonsStateEdit() {
    // ҵ������
    getBtns().m_boBusiType.setEnabled(false);

    // ����
    getBtns().m_boAdd.setEnabled(false);

    // ����
    getBtns().m_boSave.setEnabled(true);

    // ά��
    // �޸ģ�ȡ����ԭ����������ɾ����ԭ�����ϣ�������ת�����ϲ���Ʊ�������ϲ�
    getBtns().m_boMaintain.setEnabled(true);
    getBtns().m_boModify.setEnabled(false);
    getBtns().m_boBlankOut.setEnabled(false);
    getBtns().m_boCancelTransfer.setEnabled(false);
    getBtns().m_boCancel.setEnabled(true);
    
    getBtns().m_boUnite.setEnabled(false);
    getBtns().m_boUniteCancel.setEnabled(false);
    if(getBillCardPanel().getVO() != null){
	    // �Գ����ɲ�����
	    if (getBillCardPanel().getVO().getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN) {
	      getBtns().m_boUnite.setEnabled(false);
	      getBtns().m_boUniteCancel.setEnabled(false);
	    }
	    else {
	      // ֻ�з�Ʊ�ܽ��>0 �ſɽ��г����
	      getBtns().m_boUnite.setEnabled(getBillCardPanel().getVO().getHeadVO()
	          .isLgtZero());
	      getBtns().m_boUniteCancel.setEnabled(getBillCardPanel().getVO()
	          .getHeadVO().isLgtZero()
	          && getBillCardPanel().getVO().getHeadVO().isStrike());
	    }
    }

    // �в���
	// ���У�ɾ�У������У����ӣ��������У�ճ����

		// �Գ巢Ʊ��ֹ�в��� ��֤������Դ�����ַ�Ʊ lining zhongwei
		if (getBillCardPanel().getVO() != null
				&& SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN == Integer.parseInt(getBillCardPanel().getHeadItem(
						"fcounteractflag").getValueObject().toString())) {
			getBtns().m_boLineOper.setEnabled(false);
			getBtns().m_boAddLine.setEnabled(false);
			getBtns().m_boDelLine.setEnabled(false);
			getBtns().m_boCopyLine.setEnabled(false);
			getBtns().m_boPasteLine.setEnabled(false);
			getBtns().m_boPasteLineTail.setEnabled(false);
		}
		// ����ԭ���߼�
		else {
			getBtns().m_boLineOper.setEnabled(true);
			boolean isStrike = getBillCardPanel().isStrike();
			getBtns().m_boAddLine.setEnabled(!isStrike);
			getBtns().m_boDelLine.setEnabled(!isStrike);
			getBtns().m_boCopyLine.setEnabled(!isStrike);
			getBtns().m_boPasteLine.setEnabled(!isStrike);
		}
    
    // add by fengjb 2008-09-11 V55���в��������� ��Ƭ�༭�������кŰ�ť
    getBtns().m_boCardEdit.setEnabled(true);
    getBtns().m_boReRowNO.setEnabled(true);
    
    //��������
    if (getBillCardPanel().getVO().getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN) {
      getBtns().m_boRefAdd.setEnabled(false);
    }else{
    getBtns().m_boRefAdd.setEnabled(true);
    }
    //ȡ�ɱ���
    getBtns().m_boFetchCost.setEnabled(! getBillCardPanel().getVO().getHeadVO().isStrike());
    
    
    // ����
    getBtns().m_boApprove.setEnabled(false);

    boolean bCanSendAudit = false;
    try{
    	bCanSendAudit = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(
            SaleBillType.SaleInvoice, getBillCardPanel().getVO().getHeadVO().getCbiztype(),
            // NO_BUSINESS_TYPE,
            getClientEnvironment().getCorporation().getPk_corp(),
            getClientEnvironment().getUser().getPrimaryKey());
    }catch(Exception e){
    	e.printStackTrace();
    }
    // ִ��
    getBtns().m_boAction.setEnabled(bCanSendAudit);
    // ��������
    getBtns().m_boSendAudit.setEnabled(bCanSendAudit);
    
    getBtns().m_boUnApprove.setEnabled(false);

    // ��ѯ
    getBtns().m_boQuery.setEnabled(false);

    // ���
    // ˢ�£����ӣ�����λ����ҳ����ҳ��ԭ����һҳ������ҳ��ԭ����һҳ����ĩҳ��ȫѡ��ȫ��
    getBtns().m_boBrowse.setEnabled(false);
    getBtns().m_boRefresh.setEnabled(false);
    getBtns().m_boLocal.setEnabled(false);
    // ��ĩ����ҳ
    getBtns().m_boFirst.setEnabled(false);
    getBtns().m_boNext.setEnabled(false);
    getBtns().m_boPrev.setEnabled(false);
    getBtns().m_boLast.setEnabled(false);
    getBtns().m_boSelectAll.setEnabled(false);
    getBtns().m_boUnSelectAll.setEnabled(false);

    // �л�
    getBtns().m_boCard.setEnabled(false);

    // ��ӡ����
    // Ԥ������ӡ���ϲ���ʾ
    getBtns().m_boPrintManage.setEnabled(true);
    getBtns().m_boPreview.setEnabled(false);
    getBtns().m_boPrint.setEnabled(false);
    getBtns().m_boBillCombin.setEnabled(true);

    // ��������
    // ���ɶԳ巢Ʊ������˰���ĵ�����
    getBtns().m_boAssistFunction.setEnabled(true);
    getBtns().m_boOpposeAct.setEnabled(true);
    getBtns().m_boSoTax.setEnabled(true);
    getBtns().m_boDocument.setEnabled(true);

    // ������ѯ
    // ���飬������ʾ/���أ�ȡ��ԭ��������ť�����ܺͶ�����һ�£���������״̬��
    // �ͻ���Ϣ����Ʊִ��������ͻ����ã�ë��Ԥ��
    getBtns().m_boAssistant.setEnabled(true);
    getBtns().m_boOrderQuery.setEnabled(false);
    getBtns().m_boATP.setEnabled(true);
    getBtns().m_boAuditFlowStatus.setEnabled(true);
    getBtns().m_boCustInfo.setEnabled(true);
    getBtns().m_boExecRpt.setEnabled(false);
    getBtns().m_boCustCredit.setEnabled(true);
    getBtns().m_boPrifit.setEnabled(true);
    getBillCardPanel().setBodyMenuShow(true);
    updateButtons();
  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.so.pub.IBatchWorker#informWhenInterrupt(java.lang.String)
   */
  public void informWhenInterrupt(String sActionName) {
    if (ISaleInvoiceAction.Approve.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000041")/*
                                                                       * @res
                                                                       * "�����������û��жϣ�"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnApprove.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000042")/*
                                                                       * @res
                                                                       * "����������û��жϣ�"
                                                                       */);
    }
    else if (ISaleInvoiceAction.Unite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100041")/*
                                                                       * @res
                                                                       * "����������û��жϣ�"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnUnite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100042")/*
                                                                       * @res
                                                                       * "����������û��жϣ�"
                                                                       */);
    }

  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.so.pub.IBatchWorker#informWhenSucceed(java.lang.String)
   */
  public void informWhenSucceed(String sActionName) {
    if (ISaleInvoiceAction.Approve.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000043")/*
                                                                       * @res
                                                                       * "��������������"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnApprove.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000044")/*
                                                                       * @res
                                                                       * "�������������"
                                                                       */);
    }
    else if (ISaleInvoiceAction.Unite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100043")/*
                                                                       * @res
                                                                       * "�������������"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnUnite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100044")/*
                                                                       * @res
                                                                       * "�������������"
                                                                       */);
    }

  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.so.pub.IBatchWorker#reloadDataAfterRun()
   */
  public void reloadDataAfterRun() {
	  java.util.Vector vTemp = new java.util.Vector();
	  Object oTemp = null;
	  for(int i = 0; i < getBillListPanel().getHeadBillModel().getRowCount(); i++){
		  oTemp = getBillListPanel().getHeadBillModel().getValueAt(i, "csaleid");
		  if(oTemp != null) vTemp.addElement(oTemp.toString());
	  }
	  if(vTemp.size() > 0){
		  String id[] = new String[vTemp.size()];
		  vTemp.copyInto(id);
		  
		  String sql = "so_saleinvoice.dr = 0 and so_saleinvoice.csaleid in ('";
		  for(int i = 0; i < id.length - 1; i++) sql += id[i] + "','";
		  sql += id[id.length - 1] + "')";

	    // ����ѯ������뻺��
	    SaleVO[] vosInvoice = null;
	    try {
	      vosInvoice = SaleinvoiceBO_Client.queryHeadAllData(sql);
	    }
	    catch (Exception e) {
	      SCMEnv.out(e);
	      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
	          .getStrByID("scmcommon", "UPPSCMCommon-000059")/* @res "����" */, e
	          .getMessage());
	    }

	    // ���û���
	    getVOCache().setCacheDataByHeadVOs(vosInvoice);

	    // �л����б�
	    onList();
	  }
  }

  public SaleInvoiceBtn getBtns() {
    if (m_buttons == null) {
      m_buttons = new SaleInvoiceBtn();
    }

    return m_buttons;
  }

public void doAddAction(ILinkAddData addData) {
	SaleinvoiceVO[] vosNew = null;
	try {
		// ���۶���
		if (SaleBillType.SaleOrder.equals(addData.getSourceBillType())) {
			// ��ѯ����
			SaleOrderVO order = (SaleOrderVO) SaleOrderBO_Client
					.queryData(addData.getSourceBillID());
			// VO����
			vosNew = new SaleinvoiceVO[] { (SaleinvoiceVO) PfUtilUITools
					.runChangeData(SaleBillType.SaleOrder,
							SaleBillType.SaleInvoice, order) };						
		}// end if saleorder

	    if (vosNew == null || vosNew.length == 0)
	        return;
	    
	    SaleinvoiceVO[] aryRetVO = st.mergeSourceVOs(vosNew);

	    // ���û���
	    getVOCache().setCacheData(aryRetVO);

	    //����һ���Լ��,�ϲ�VO
	    getVOCache().setSaleinvoiceVO(
		        getVOCache().getPos(),
		        SaleinvoiceVO.mergeCheck(vosNew, st.SO_30
		            .booleanValue(), getBtns().getBusiType()));

	    // ------2-------------to card
		add(getBillCardPanel(), "Center");
		// TODO �˴��ظ�����������״������Ż�
		getBillCardPanel().loadCardData(getVOCache().getCurrentVO());
		getBillCardPanel().modify(getVOCache().getCurrentVO());
		
		// --------3------------------------
		setShowState(CardShow);
		setOperationState(ISaleInvoiceOperState.STATE_EDIT);
		setButtonsStateEdit();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon",
		     "UPPSCMCommon-000350") /* * @res "�༭����..."*/);

	} catch (Exception e) {
	    MessageDialog.showErrorDlg(this, "", e.getMessage());
	    return;
	}
	  
}

///**
// * ����������������������ǰ�����ε���ID\����\���
// * <p>
// * <b>examples:</b>
// * <p>
// * ʹ��ʾ��
// * <p>[0]����,[1]��˰�ϼ�,[2]��˰���
// * <b>����˵��</b>
// * <p>
// * @author wsy
// * @time 2007-9-17 ����01:38:40
// */
//private void saveSourceMny(SaleinvoiceVO saleinvoiceNew[]){
//	hsSourceMny.clear();
//	UFDouble[] udNumMny=null; 
//	for(int i=0,iLen=saleinvoiceNew.length;i<iLen;i++){
//		for(int j=0,jLen=saleinvoiceNew[i].getItemVOs().length;j<jLen;j++){
//			if(saleinvoiceNew[i].getItemVOs()[j].getCupsourcebillbodyid()==null)
//        continue;
//			udNumMny= new UFDouble[7];
//      //���ۼ�����λ����
//			udNumMny[0]=saleinvoiceNew[i].getItemVOs()[j].getNquotenumber();
//			//ԭ��				
//			udNumMny[1]=saleinvoiceNew[i].getItemVOs()[j].getNoriginalcursummny();
//			udNumMny[2]=saleinvoiceNew[i].getItemVOs()[j].getNoriginalcurmny();
//			//����
//			udNumMny[3]=saleinvoiceNew[i].getItemVOs()[j].getNsummny();
//			udNumMny[4]=saleinvoiceNew[i].getItemVOs()[j].getNmny();
//			//���ǰ���
//			udNumMny[5]=saleinvoiceNew[i].getItemVOs()[j].getNsubcursummny();
//			udNumMny[6]=saleinvoiceNew[i].getItemVOs()[j].getNsubsummny();
//			hsSourceMny.put(saleinvoiceNew[i].getItemVOs()[j].getCupsourcebillbodyid(), udNumMny);
//		}
//	}
//}

//private void restoreSourceMnyList(){
//	String cupsourcebillbodyid=null;
//	UFDouble ud = null;
//	UFDouble[] uds =null;
//	for(int i=0,iLen=getBillListPanel().getBodyBillModel().getRowCount();i<iLen;i++){
//		cupsourcebillbodyid= (String)getBillListPanel().getBodyBillModel().getValueAt(i,"cupsourcebillbodyid");
//		if(cupsourcebillbodyid==null)
//      continue;
//		uds=hsSourceMny.get(cupsourcebillbodyid);
//		if(uds==null||uds[0]==null||uds[0].doubleValue()==0)
//      continue;
//		ud=(UFDouble)getBillListPanel().getBodyBillModel().getValueAt(i,"nquotenumber");
//		if(ud==null)
//      continue;
//		if(ud.compareTo(uds[0])==0){
//			if(uds[1]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[1], i, "noriginalcursummny");
//			if(uds[2]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[2], i, "noriginalcurmny");
//			if(uds[1]!=null&&uds[2]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[1].sub(uds[2]), i, "noriginalcurtaxmny");
//			if(uds[3]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[3], i, "nsummny");
//			if(uds[4]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[4], i, "nmny");
//			if(uds[3]!=null&&uds[4]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[3].sub(uds[4]), i, "ntaxmny");
//			if(uds[5]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[7], i, "nsubcursummny");
//			if(uds[6]!=null)
//				getBillListPanel().getBodyBillModel().setValueAt(uds[8], i, "nsubsummny");			
//			
//		}
//		
//	}
//}

/**
 * ���෽����д
 * ��Ƭ�༭�����в���ʵ��
 * @see nc.ui.pub.bill.BillActionListener#onEditAction(int)
 */
public boolean onEditAction(int action) {
  //�ж��Ƿ������в���
  if (BillScrollPane.ADDLINE == action && !iAddButn) {
    getBillCardPanel().actionAddLine();
  return false;
  }
  return true;
 }
}