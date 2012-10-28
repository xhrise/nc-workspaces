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
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>销售发票UI
 * <li>...
 * </ul>
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * XXX版本增加XXX的支持。
 * <p>
 * <p>
 * 
 * @version 本版本号
 * @since 上一版本号
 * @author wangyf
 * @time 2007-3-6 上午11:03:07
 */

public class SaleInvoiceUI extends nc.ui.pub.ToftPanel implements
    BillTableMouseListener, nc.ui.pf.query.ICheckRetVO, ILinkApprove,
    ILinkQuery, IFreshTsListener, IInvoiceListPanel, IInvoiceCardPanel,
    ILinkMaintain, ILinkAdd, IBatchWorker,BillActionListener{
  
	private static final long serialVersionUID = 1L;
 
	private boolean iAddButn = false; 

  // 当前的显示界面是LIST还是CARD
  public static final int ListShow = 0;

  public static final int CardShow = 1;

  // 线程对话框
  private ProccDlg m_proccdlg = null;

  // 单据显示状态
  private int m_iShowState = ListShow;

  // 查询框
  private SaleInvoiceQueryDlg dlgQuery = null;

  // 是否消息中心
  private boolean m_bInMsgPanel = false;

  // 按钮
  private SaleInvoiceBtn m_buttons = null;

  // 卡片界面
  private SaleInvoiceCardPanel ivjBillCardPanel = null;

  // 当前操作状态
  private int m_iOperationState = ISaleInvoiceOperState.STATE_BROWSE;

  private PrintLogClient m_PrintLogClient = null;
  // 销售发票工具类
  SaleInvoiceTools st =  new SaleInvoiceTools();

  // 销售发票缓存类
  private SaleInvoiceVOCache m_vocache = new SaleInvoiceVOCache();

  // 列表界面
  private SaleInvoiceListPanel ivjBillListPanel = null;

  // 是否曾经查询过，控制刷新按钮的可用性
  private boolean m_bEverQuery = false;
  
  //保存新增行的上游金额20070917
 //  public Hashtable<String, UFDouble[]> hsSourceMny = new java.util.Hashtable<String, UFDouble[]>();
  
  //标记：取成本价是否存在错误行
  private boolean existErrRows = false;
  
  //V55销售发票支持二次开发扩展
  private InvokeEventProxy pluginproxy = null;
 
  /**
   * 方法功能描述：获取插件代理类。
   * <b>参数说明</b>
   * @return
   * @time 2009-1-15 下午04:56:15
   */
  public InvokeEventProxy getPluginProxy(){
    try{
    if(null == this.pluginproxy)
      this.pluginproxy = new InvokeEventProxy("so", SaleBillType.SaleInvoice,
          new SOPluginUI(this, SaleBillType.SaleInvoice));
    }catch(Exception e){
      showErrorMessage("销售发票支持二次开发扩展出错："+ e);
    }
    return this.pluginproxy;
  }
  /**
   * SaleInvoice 构造子注解。
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
    //  二次开发扩展
    getPluginProxy().setButtonStatus();
  }

  /**
   * 后续动作。 创建日期：(2001-6-1 13:12:36)
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
              "UPP40060501-000067")/* @res "成功！" */);
    }
    catch (Exception e) {
      showErrorMessage(bo.getName()
          + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000069")/* @res "失败！" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
  }

  /**
   * 处理在Card面板上更新单据后，保持列表面板上的vo同时更新
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 下午04:31:53
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
//      // 设置表头金额精度
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
   * 根据卡片更新缓存
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 下午04:31:37
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

    // 原来只使用ID更新，两个界面合为一个后，
    // 新增的单据ID可能为空，因此加入根据POS更新的处理
    if (null == getVOCache().getVO_Load(voHead.getCsaleid())) {
      SaleinvoiceVO catchevo =  getVOCache().getVO_NotLoad(getVOCache().getPos());
      if(null == catchevo || null== catchevo.getHeadVO().getCsaleid()){
        //根据出库汇总生成，此VO加入到缓存中来
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
      // 公司ID
      headVO.setPkcorp(getCorpPrimaryKey());
      // 库存组织ID
      headVO.setCcalbodyid((String) getBillListPanel().getHeadBillModel()
          .getValueAt(row, "ccalbodyid"));
      // 库存组织名称
      headVO.setCcalbodyname((String) getBillListPanel().getHeadBillModel()
          .getValueAt(row, "ccalbodyname"));
      // 单据类型
      headVO.setBilltype(getBillListPanel().getBillType());
      // 币种
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
        // 存货ID
        bodyVO.setCinventoryid((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cinventoryid"));
        // 存货编码
        bodyVO.setCode((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cinventorycode"));
        // 存货名称
        bodyVO.setName((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cinventoryname"));
        String gg = (String) getBillListPanel().getBodyBillModel().getValueAt(
            i, "GG");
        gg = gg == null ? "" : gg;
        String xx = (String) getBillListPanel().getBodyBillModel().getValueAt(
            i, "XX");
        xx = xx == null ? "" : xx;
        // 规格型号
        bodyVO.setSize(gg + xx);
        // 批次
        bodyVO.setCbatchid((String) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "cbatchid"));
        // 数量
        bodyVO.setNumber((UFDouble) getBillListPanel().getBodyBillModel()
            .getValueAt(i, "nnumber"));
        // 净价
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
        // 无税金额
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
      // 公司ID
      headVO.setPkcorp(getCorpPrimaryKey());
      // 库存组织ID
      headVO.setCcalbodyid(getBillCardPanel().getHeadItem("ccalbodyid")
          .getValue());
      // 库存组织名称
      // headVO.setCcalbodyname(getBillCardPanel().getHeadItem("ccalbodyname").getValue());
      UIRefPane ccalbodyid = (UIRefPane) getBillCardPanel().getHeadItem(
          "ccalbodyid").getComponent();
      headVO.setCcalbodyname(ccalbodyid.getRefName());
      // 单据类型
      headVO.setBilltype(getBillCardPanel().getBillType());
      // 币种
      headVO.setCurrencyid(getBillCardPanel().getHeadItem("ccurrencyid")
          .getValue());
      nc.vo.so.so006.ProfitItemVO[] bodyVOs = new nc.vo.so.so006.ProfitItemVO[getBillCardPanel()
          .getRowCount()];
      for (int i = 0; i < bodyVOs.length; i++) {
        nc.vo.so.so006.ProfitItemVO bodyVO = new nc.vo.so.so006.ProfitItemVO();
        // 存货ID
        bodyVO.setCinventoryid((String) getBillCardPanel().getBodyValueAt(i,
            "cinventoryid"));
        // 存货编码
        bodyVO.setCode((String) getBillCardPanel().getBodyValueAt(i,
            "cinventorycode"));
        // 存货名称
        bodyVO.setName((String) getBillCardPanel().getBodyValueAt(i,
            "cinventoryname"));
        String gg = (String) getBillCardPanel().getBodyValueAt(i, "GG");
        gg = gg == null ? "" : gg;
        String xx = (String) getBillCardPanel().getBodyValueAt(i, "XX");
        xx = xx == null ? "" : xx;
        // 规格型号
        bodyVO.setSize(gg + xx);
        // 批次
        bodyVO.setCbatchid((String) getBillCardPanel().getBodyValueAt(i,
            "cbatchid"));
        // 数量
        bodyVO.setNumber((UFDouble) getBillCardPanel().getBodyValueAt(i,
            "nnumber"));
        // 净价
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
        // 无税金额
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
          "UPP40060501-000069")/* @res "失败！" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }

  }

  /**
   * 父类方法重写
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
   * 动作执行。 修改，增加净价修改判断的处理，通过捕获异常和设置标志进行 创建日期：(2001-6-1 13:12:36) 修改日期
   * (2003-8-20) 杨波
   */
  private void doUnApproveWork() {

    java.util.HashMap hsvos = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-000055")/*
                                                       * @res "正在进行弃审前的准备..."
                                                       */);
    try {
      hsvos = getBillListPanel().getApproveSaleInvoiceVOs();// ("SoUnApprove");
      if (hsvos == null || hsvos.size() <= 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000056")/* @res "请选择需弃审的发票！" */);
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
                                                       * @res "开始弃审..."
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
      // ShowToolsInThread.showMessage(proccdlg, "正在弃审发票...["
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
          // ShowToolsInThread.showMessage(proccdlg, "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]弃审成功！", "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]弃审成功！");

        }
        else {
          hSuccess.put(key, saleinvoice);
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000060", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]" + "弃审操作已被用户取消！",
          // "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]"
          // + "弃审操作已被用户取消！");
        }
      }
      catch (Exception e) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000061", null, new String[] {
              ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += e.getMessage();
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);
        // ShowToolsInThread.showMessage(proccdlg, " ", "发票["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]弃审失败：" + e.getMessage());
        if (m_proccdlg.getckHint().isSelected()) {
          sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000061", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          if (ShowToolsInThread.showYesNoDlg(m_proccdlg, sMsg
              + e.getMessage()
              + nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
                  "UPP40060501-000062")/*
                                         * @res "是否继续弃审以下的发票？"
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
                                                                       * "弃审操作被用户中断！"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000044")/*
                                                                       * @res
                                                                       * "弃审操作结束！"
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
                                                                       * "正在更新界面数据..."
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
   * 父类方法重写
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
   * 按单据状态更新按钮状态
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b> iState 按钮状态
   * 
   * @return
   * <p>
   * @author wangyf
   * @time 2007-3-6 上午10:33:09
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
                                                       * @res "正在进行审批前的准备..."
                                                       */);
    try {
      hsvos = getBillListPanel().getApproveSaleInvoiceVOs();// ("APPROVE");
      if (hsvos == null || hsvos.size() <= 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000046")/* @res "请选择待审批的发票！" */);
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
                                                       * @res "开始审批..."
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
      // ShowToolsInThread.showMessage(proccdlg, "正在审批发票...["
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
          // ShowToolsInThread.showMessage(proccdlg, "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]审批成功！", "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]审批成功！");
          // }

        }
        else {
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-000051", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]" + "审批操作以被用户取消！",
          // "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]"
          // + "审批操作以被用户取消！");
        }

      }
      catch (Exception e) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-000052", null, new String[] {
              ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += e.getMessage();
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);
        // ShowToolsInThread.showMessage(proccdlg, " ", "发票["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]审批失败：" + e.getMessage());
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
                                         * @res "是否继续审批以下的发票？"
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
                                                                       * "审批操作被用户中断！"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000043")/*
                                                                       * @res
                                                                       * "审批操作结束！"
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
                                                                       * "正在更新界面数据..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  /**
   * 生成对冲发票
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-19 下午03:48:03
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

    // 设置界面
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

    // 设置界面、加入缓存
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
   * 浏览状态按钮
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-6 下午04:30:53
   */
  private void setButtonsStateBrowse() {

    // 是否有VO被选中
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

    // 是否缓存区中有未保存的单据
    boolean haveNewBill = false;
    if (haveVOSelected && voFromPanel.getHeadVO().isNew()) {
      // 只是提高效率
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

    // 业务类型
    getBtns().m_boBusiType.setEnabled(!haveNewBill);

    // 新增
    getBtns().m_boAdd.setEnabled(!haveNewBill);

    // 保存
    getBtns().m_boSave.setEnabled(false);

    // 维护
    // 修改，取消（原名放弃），删除（原名作废），放弃转单，合并开票，放弃合并
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
      // modify by fengjb 20080918 审批中状态按钮控制改变
      }else if(iBillStatus == BillStatus.AUDITING){
        //是否有审批人
        boolean isApprove = voFromPanel.getHeadVO().getCapproveid() == null? false:true;
        getBtns().m_boModify.setEnabled(!isApprove);
      }
      else {
        getBtns().m_boModify.setEnabled(false);
      }
      getBtns().m_boCancel.setEnabled(false);
      // 已保存过的单据，状态合适则删除可用
      //modify by fengjb V55 审批中状态单据不允许删除
      if (haveVOSelected
          && !voFromPanel.getHeadVO().isNew()
          && (iBillStatus == BillStatus.NOPASS
               || iBillStatus == BillStatus.FREE)) {
        getBtns().m_boBlankOut.setEnabled(true);
      }
      else {
        getBtns().m_boBlankOut.setEnabled(false);
      }
      // 如果当前单据ID为空，则放弃转单可用
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
//        // 对冲生成不可用
//        if (!haveVOSelected
//            || voFromPanel.getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN) {
//          getBtns().m_boUnite.setEnabled(false);
//          getBtns().m_boUniteCancel.setEnabled(false);
//        }
//        else {
//          // 只有发票总金额>0 才可进行冲减。
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

    // 行操作
    // 增行，删行，插入行（增加），复制行，粘贴行
    getBtns().m_boLineOper.setEnabled(false);
    //参照增行
    getBtns().m_boRefAdd.setEnabled(false);
    //取成本价
    getBtns().m_boFetchCost.setEnabled(false);

    // 审批
    getBtns().m_boApprove.setEnabled(haveVOSelected
        && !voFromPanel.getHeadVO().isNew()
        && iBillStatus != BillStatus.AUDIT);

    // 执行
    // 送审，弃审
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

    // 查询
    getBtns().m_boQuery.setEnabled(!haveNewBill);

    // 浏览
    // 刷新（增加），定位，首页，上页（原名上一页），下页（原名下一页），末页，全选，全消
    getBtns().m_boBrowse.setEnabled(true);
    {
      getBtns().m_boRefresh.setEnabled(m_bEverQuery && !haveNewBill);
      // 首末上下页
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

    // 切换：无缓存时，或有单据被选中时
    if (getShowState() == CardShow) {
      getBtns().m_boCard.setName(NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000464")/*
                                               * @res "列表显示"
                                               */);
      getBtns().m_boCard.setHint(NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000464"));
    }
    else {
      getBtns().m_boCard.setName(NCLangRes.getInstance().getStrByID(
          "SCMCOMMON", "UPPSCMCommon-000463")/*
                                               * @res "卡片显示"
                                               */);
      getBtns().m_boCard.setHint(NCLangRes.getInstance().getStrByID(
              "SCMCOMMON", "UPPSCMCommon-000463"));
    }
    getBtns().m_boCard.setEnabled(getVOCache().isEmpty() || haveVOSelected);

    // 打印
    // 预览，打印，合并显示
    getBtns().m_boPrintManage.setEnabled(haveVOSelected);
    {
      getBtns().m_boPreview.setEnabled(true);
      getBtns().m_boPrint.setEnabled(true);
      if(getShowState() == CardShow) getBtns().m_boBillCombin.setEnabled(true);
      else getBtns().m_boBillCombin.setEnabled(false);
    }

    // 辅助功能
    // 生成对冲发票，传金税，文档管理
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
      // 库存锁定
//      Object cfreezeid = getBillCardPanel().getBodyValueAt(
//          getBillCardPanel().getBillTable().getSelectedRow(), "cfreezeid");
      // if (cfreezeid != null && cfreezeid.toString().trim().length() != 0) {
      // // 根据单据状态设置单据
      // getBtns().m_boStockLock.setEnabled(false);
      // }
      // else {
      // if (getBillCardPanel().getBillStatus() == BillStatus.AUDIT)
      // getBtns().m_boStockLock.setEnabled(true);
      // else
      // getBtns().m_boStockLock.setEnabled(false);
      // }
    }

    // 辅助查询
    // 联查，存量显示/隐藏（取代原可用量按钮，功能和订单做一致），审批流状态，
    // 客户信息，发票执行情况，客户信用，毛利预估
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
   * 设置单据状态。 创建日期：(2001-6-13 15:17:39)
   * 
   * @param iState
   *          int 单据状态 iOppState 对冲标志
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
        // 预览键置灰
        getBtns().m_boPreview.setEnabled(false);
        // 审批流状态置灰
        getBtns().m_boAuditFlowStatus.setEnabled(false);
        // setImageType(IMAGE_CANCEL);
        // strState = "BLANKOUT";
        break;
      }
        // 添加“正在审批”状态
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
      }// 添加“审批未通过”状态
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
      // 单据状态
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
    // 审批后已对冲单据，不能弃审，修订
    if (iState == BillStatus.AUDIT && iOppStatus == 1) {
      getBtns().m_boUnApprove.setEnabled(false);
    }
    // 审批且对冲标记为正常，则单据能对冲
    if (iState == BillStatus.AUDIT && iOppStatus == 0) {
      getBtns().m_boOpposeAct.setEnabled(true);
    }
    else {
      getBtns().m_boOpposeAct.setEnabled(false);
    }

  }

  /**
   * 初始化类。
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
   * 父类方法重写
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
   * 方法功能描述：销售发票删除功能实现。
   * <b>参数说明</b>
   * @author fengjb
   * @time 2008-11-26 上午10:18:41
   */
  private void onBlankOut() {
    
    if (MessageDialog.showYesNoDlg(this, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("common", "UC001-0000039"), nc.ui.ml.NCLangRes
        .getInstance().getStrByID("common", "UCH002")) != MessageDialog.ID_YES)
      return;
    //获得要删除的VO
    SaleinvoiceVO voInvoice = (SaleinvoiceVO) getVOForAction();
   //发票合并开票
    if (voInvoice.getHeadVO().isStrike()) {
      if(MessageDialog.showYesNoDlg(this,nc.ui.ml.NCLangRes.getInstance()
    	        .getStrByID("common", "UC001-0000039"), NCLangRes.getInstance().getStrByID("40060501",
          "UPP40060501-000161")/* @res"是否取消合并开票?" */) != MessageDialog.ID_YES){
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

    // 赋冲减的数据
    voInvoice.setAllinvoicevo(voInvoice);
    voInvoice.setBstrikeflag(new UFBoolean(false));
    voInvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());
   
    try {
      // 插件支持
      getPluginProxy().beforeAction(Action.DELETE, new SaleinvoiceVO[] { voInvoice });
      onDelete(voInvoice);
    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
      return;
    }

    // 从缓存中移去删除的VO
    getVOCache().removeVOBy(voInvoice.getHeadVO().getCsaleid());

    if (getShowState() == ListShow) {
      getBillListPanel().getHeadBillModel().delLine(new int[] {
        getBillListPanel().getHeadTable().getSelectedRow()
      });
      getBillListPanel().updateUI();
    }
    else {
      // 除去当前单据，并自动滚动到下一张
//      getVOCache().removeVOAt(getVOCache().getPos());
      getBillCardPanel().loadCardData(
          getVOCache().getVO_Load(getVOCache().getPos()));
    }
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

  }

  /**
   * 此处插入方法说明。 创建日期：(2001-3-17 9:00:09)
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
        "UPP40060501-000065")/* @res "数据加载成功！" */);
    
    setShowState(CardShow);
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();
    getBillCardPanel().setEnabled(false);

    updateUI();

  }

  /**
   * 切换到列表界面
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-20 下午02:22:48
   */
  private void onList() {

    // 显示
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
   * 返回 BillCardPanel 特性值。
   * 
   * @return nc.ui.pub.bill.BillCardPanel
   */
  /* 警告：此方法将重新生成。 */
  public SaleInvoiceCardPanel getBillCardPanel() {
    if (ivjBillCardPanel == null) {
      ivjBillCardPanel = new SaleInvoiceCardPanel(this);
      //设置表体菜单项
      UIMenuItem menu[] = ivjBillCardPanel.getBodyMenuItems();
      java.util.Vector<UIMenuItem> vTemp = new java.util.Vector<UIMenuItem>();
      for(UIMenuItem bodymenu: menu) 
        vTemp.addElement(bodymenu);
      menu = new UIMenuItem[vTemp.size()];
      vTemp.copyInto(menu);
      ivjBillCardPanel.setBodyMenu(menu);
      //设置选中背景色
      ivjBillCardPanel.getBillTable().setRowSelectionAllowed(true);
      ivjBillCardPanel.getBillTable().setColumnSelectionAllowed(false);
      ivjBillCardPanel.getBillTable().setSelectionMode(
        javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      //设置整单折扣、发票折扣精度为6位
      ivjBillCardPanel.getHeadItem("ndiscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getHeadItem("ninvoicediscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getBodyItem("ndiscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getBodyItem("nitemdiscountrate").setDecimalDigits(6);
      ivjBillCardPanel.getBodyItem("ninvoicediscountrate").setDecimalDigits(6);
    }
    return ivjBillCardPanel;
  }
  /**
   * 方法功能描述返回当前卡片界面上表体每行上层来源行ID。
   * <b>参数说明</b>
   * @return
   * @author fengjb
   * @time 2008-7-23 下午02:16:16
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
   * 方法功能描述：点击分单条件弹出分单条件界面并且点击了“确定”按钮的时候设置当前界面上的分单条件值。
   * <b>参数说明</b>
   * @author fengjb
   * @time 2008-8-29 下午03:45:35
   */
 public void setSplitCondition(SaleDispartVO[] dispvo){
   st.setSplitCondition(dispvo);
 }
 

  /**
   * 鼠标双击事件 创建日期：(2001-6-20 17:19:03)
   */
  public void mouse_doubleclick(nc.ui.pub.bill.BillMouseEnent e) {
    if (e.getPos() == BillItem.HEAD) {
      onCard();
      // 如果是新单据,则需直接修改
      
      // 双击时不在进行判断单据是否为新单据  // modify by river for 2012-09-21 
//      if (getBillCardPanel().isNewBill()) {
//        onModify();
//      }
    }
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-4-20 11:15:48)
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
   * 批量自动合并开票
   */
  private void batchAutoUnit() {
    java.util.HashMap hsvos = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-100045")/*
                                                       * @res "正在进行合并开票前的准备..."
                                                       */);
    try {
      hsvos = getBillListPanel().getApproveSaleInvoiceVOs();// ("UNIT");
      if (hsvos == null || hsvos.size() <= 0) {
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100046")/*
                                   * @res "请选择待合并开票的发票！"
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
                                                       * @res "开始合并开票..."
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
      // ShowToolsInThread.showMessage(proccdlg, "正在合并开票...["
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
                                                             * "非自由状态，不能合并开票"
                                                             */);

        if (saleinvoice.getHsArsubAcct() != null
            && saleinvoice.getHsArsubAcct().size() > 0)
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("40060501", "UPP40060501-100064")/*
                                                             * @res
                                                             * "已合并开票，先取消后才会再次合并开票"
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
          // ShowToolsInThread.showMessage(proccdlg, "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]合并开票成功！", "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]合并开票成功！");
          // }

        }
        else {
          String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
              "UPP40060501-100051", null, new String[] {
                ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
              });
          ShowToolsInThread.showMessage(m_proccdlg, sMsg, sMsg);
          // ShowToolsInThread.showMessage(proccdlg, "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]" + "没有符合条件的冲应收单！",
          // "发票["
          // + ((SaleVO) saleinvoice.getParentVO())
          // .getVreceiptcode() + "]"
          // + "没有符合条件的冲应收单！");
        }

      }
      catch (Exception e) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100052", null, new String[] {
              ((SaleVO) saleinvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += e.getMessage();
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);
        // ShowToolsInThread.showMessage(proccdlg, " ", "发票["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]合并开票失败：" + e.getMessage());
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
                                         * @res "是否继续合并开票以下的发票？"
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
                                                                       * "合并开票操作被用户中断！"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100043")/*
                                                                       * @res
                                                                       * "合并开票操作结束！"
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
                                                                       * "正在更新界面数据..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  /**
   * 此处插入方法说明。 创建日期：(2001-4-20 11:16:08)
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
   * 此处插入方法说明。 创建日期：(2001-4-20 11:15:26) 修改日期：2003-12-02 修改内容：加入缓存
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
   * 此处插入方法说明。 创建日期：(2001-4-20 11:16:23) 修改日期：2003-12-02 修改内容：加入缓存
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
   * 返回 BillListPanel 特性值。
   * 
   * @return nc.ui.pub.bill.BillListPanel
   */
  /* 警告：此方法将重新生成。 */
  public SaleInvoiceListPanel getBillListPanel() {
    if (ivjBillListPanel == null) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000135")/*
                                 * @res "开始加载列表模板...."
                                 */);

      try {
        ivjBillListPanel = new SaleInvoiceListPanel(this);
        // 表头加背景色
        ivjBillListPanel.getHeadTable().setRowSelectionAllowed(true);
        ivjBillListPanel.getHeadTable().setColumnSelectionAllowed(false);
        ivjBillListPanel.getHeadTable().setSelectionMode(
          javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // 表体加背景色
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
                                 * @res "列表模板加载成功！"
                                 */);

      getBillListPanel().addMouseListener(this);
      //fengjb 20081020  V55设置整单折扣、发票折扣精度为6位
      getBillListPanel().getHeadItem("ndiscountrate").setDecimalDigits(6);
      getBillListPanel().getHeadItem("ninvoicediscountrate").setDecimalDigits(6);
      getBillListPanel().getBodyItem("ndiscountrate").setDecimalDigits(6);
      getBillListPanel().getBodyItem("nitemdiscountrate").setDecimalDigits(6);
      getBillListPanel().getBodyItem("ninvoicediscountrate").setDecimalDigits(6);
      
    }
    return ivjBillListPanel;
  }

  /**
   * 子类实现该方法，返回业务界面的标题。
   * 
   * @version (00-6-6 13:33:25)
   * @return java.lang.String
   */
  public String getTitle() {
    // return m_title;
    return getBillCardPanel().getBillData().getTitle();
  }

  /**
   * 销售发票界面初始化
   */
  /* 警告：此方法将重新生成。 */
  private void initialize() {
    // 加载参数
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
    //右键菜单增加"重排行号"
    nc.ui.scm.pub.BillTools.addReSortRowNoToPopMenu(getBillCardPanel(), null);
    //增加右键菜单中"卡片编辑"
    nc.ui.scm.pub.BillTools. addCardEditToBodyMenus(getBillCardPanel(), null);
    //表体右键菜单
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
   * 批审批中审批一张发票
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-7-31 上午09:53:00
   */
  private void onApprove() {

    SaleinvoiceVO voInvoice = (SaleinvoiceVO) getVOForAction();
    voInvoice.setCl(new ClientLink(getClientEnvironment()));
    
    HashMap<String,SmartVO> hsnewvo= null; 
    try {
      if (voInvoice.getHeadVO().getDbilldate().compareTo(
          getClientEnvironment().getDate()) > 0)
        throw new nc.vo.pub.BusinessException("审批日期不能小于制单日期！");
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
    /************记录业务日志*************/
     insertOperLog(voInvoice,nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
     "UC001-0000027")/* @res"审批" */);
    /************记录业务日志* end ************/
    // -----------成功
    if (!PfUtilClient.isSuccess()) {
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060301",
          "UPP40060301-000254")/*
                                 * @res "送审操作已经被用户取消！"
                                 */);
      return;
    }
    
    updateUIValue(hsnewvo);
    
    //依据单据状态显示审核结果
    int status = -1;
    if (getShowState() == ListShow) 
      status = getVOCache().getCurrentVO().getHeadVO().getFstatus();
    else if (getShowState() == CardShow) 
      status = SmartVODataUtils.getInteger(getBillCardPanel().getHeadItem("fstatus").getValueObject());
    
   String res = "审批操作结果：";
        if(BillStatus.AUDIT == status)
          res += "审批通过";
        else if(BillStatus.AUDITING == status)
          res += "正在审批";
        else if(BillStatus.NOPASS == status)
          res +="审批未通过";
        else if(BillStatus.FREE == status)
          res +="驳回制单人";
        else
          res += "未知状态";
        showHintMessage(res);
//      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
//          "UPP40060501-000123")/* @res "发票审批成功！" */);
  }

  /**方法功能描述：V55 发票保存、审核、弃审、删除操作支持业务日志。
   * <b>参数说明</b>
   * @param voinvoice
   * @param action
   * @author fengjb
   * @time 2008-8-26 下午07:15:04
   */
  private void insertOperLog(SaleinvoiceVO voinvoice, String action) {
    Operlog operlog=new Operlog();
    voinvoice.getOperatelogVO().setOpratorname(nc.ui.pub.ClientEnvironment.getInstance().getUser().getUserName());
    voinvoice.getOperatelogVO().setCompanyname(nc.ui.pub.ClientEnvironment.getInstance().getCorporation().getUnitname());
    voinvoice.getOperatelogVO().setOperatorId(nc.ui.pub.ClientEnvironment.getInstance().getUser().getPrimaryKey());
    operlog.insertBusinessExceptionlog(voinvoice, action, null, nc.vo.scm.funcs.Businesslog.MSGMESSAGE, SaleBillType.SaleInvoice);
    
  }

  /**
   * 批弃审中弃审一张发票
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-19 上午11:48:04
   */
  private void onUnApprove() {
    
    SaleinvoiceVO voInvoice = (SaleinvoiceVO) getVOForAction();
    HashMap<String,SmartVO> hsnewvo = null;
    // 赋冲减的数据
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
       
      /************记录业务日志*************/
      insertOperLog(voInvoice, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000028")/* @res"弃审" */);
      /************记录业务日志* end ************/
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmcommon",
          "UPPSCMCommon-000184")/* @res "弃审成功！" */);
    }
  }

  /**
   * 可用量
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
   * 客户信息
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-1 下午02:56:21
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
              "UPP40060501-000069")/* @res "失败！" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
  }

  /**
   * 客户信用
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
	 * 导入金税票号
	 * 
	 * @author 蒲强华
	 * @since 2008-12-2
	 */
	private void onImportTaxCode() {
		GoldTaxVO[] goldTaxVOs = new TransGoldTaxDlg(this).importGoldTax();
		SCMEnv.out("导入金税VO数组长度 goldTaxVOs.length = " + goldTaxVOs.length);
		SaleVO[] saleVOs = new SaleVO[goldTaxVOs.length];
		for (int i = 0; i < goldTaxVOs.length; i++) {
			GoldTaxHeadVO taxHeadVO = goldTaxVOs[i].getParentVO();
			SaleVO saleVO = new SaleVO();
			// 销售发票单据号
			saleVO.setVreceiptcode(taxHeadVO.getCode());
			// 金税票号
			saleVO.setCgoldtaxcode(taxHeadVO.getTaxBillNo());
			saleVOs[i] = saleVO;
		}
		try {
			// 根据单据号更新金税票号
			SaleinvoiceBO_Client.updateSaleinvoiceValue(saleVOs, new String[]{"cgoldtaxcode"}, new String[]{"vreceiptcode"});
			showHintMessage("金税票号导入成功");
		} catch (Exception e) {
			SCMEnv.error("更新金税票号发生异常", e);
			showErrorMessage("更新金税票号发生异常");
		}
	}

	/**
	 * 传金税
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
			SCMEnv.out("传金税导出成功");
			try {
				// 传金税时间
				UFDateTime transTime = ClientEnvironment.getServerTime();
				Map<String, UFDateTime> tsMap = SaleinvoiceBO_Client.updateWhenToGoldTax(new ArrayList<String>(voMapOfId.keySet()), transTime);
				if (getShowState() == ListShow) {
					int rowCount = getBillListPanel().getHeadBillModel().getRowCount();
					// 循环更新TS、是否传金税、传金税时间
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
									// 更新缓存
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
							// 更新TS、是否传金税、传金税时间
							getBillCardPanel().setHeadItem("ts", ts);
							getBillCardPanel().setHeadItem("btogoldtax", UFBoolean.TRUE);
							getBillCardPanel().setHeadItem("dtogoldtaxtime", transTime.toString());

							SaleinvoiceVO saleinvoiceVO = voMapOfId.get(id);
							if (null != saleinvoiceVO) {
								saleinvoiceVO.getParentVO().setAttributeValue("ts", ts);
								saleinvoiceVO.getParentVO().setAttributeValue("btogoldtax", UFBoolean.TRUE);
								saleinvoiceVO.getParentVO().setAttributeValue("dtogoldtaxtime", transTime.toString());
								// 更新缓存
								getVOCache().setSaleinvoiceVO(((SaleVO)saleinvoiceVO.getParentVO()).getPrimaryKey(), saleinvoiceVO);
							}
						}
					}
				}
			} catch (Exception e) {
				SCMEnv.error("将传金税时间更新到数据库发生异常", e);
			}
		} else {
			SCMEnv.out("传金税未导出成功");
		}
	}

  /**
   * 业务类型变化。 创建日期：(2001-9-14 9:41:00)
   * 
   * @param bo
   *          nc.ui.pub.ButtonObject
   */
  private void onBusiType(ButtonObject bo) {
    bo.setSelected(true);
//    getBillCardPanel().addNew();
    getBillCardPanel().setBusiType(bo.getTag());

    // 变化按钮
    getBtns().changeButtonsWhenBusiTypeSelected(bo);
    setButtons(getBtns().getButtonArray());
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

    setTitleText(getBillCardPanel().getTitle());

  }
  /**
   * 方法功能描述：在新增或者修改时依据当前业务类型设置"参照增行"可参照的。
   * <b>参数说明</b>
   * @param nowBusitype
   * @author fengjb
   * @time 2008-7-29 下午02:39:36
   */
  private void changeRefAddButtonByBusiType(String nowBusitype){
	  if(nowBusitype == null || nowBusitype.trim().length() ==0){
		  getBtns().m_boRefAdd.removeAllChildren();
	  }
        //获得该单据类型在某业务类型下配置的所有来源单据类型
		BillbusinessVO[] billBusiVOs = PfUIDataCache.getSourceByCorpAndBillAndBusi(SaleInvoiceTools.getLoginPk_Corp(), 
				 SaleBillType.SaleInvoice, nowBusitype);
		 getBtns().m_boRefAdd.removeAllChildren();
		if (billBusiVOs == null)
			return;

		// 将来源单据都作为子菜单按钮
		ButtonObject btnAddChild = null;
		for (int i = 0; i < billBusiVOs.length; i++) {
			String showName = Pfi18nTools.i18nBilltypeName(billBusiVOs[i].getPk_billtype(), billBusiVOs[i].getBilltypename());
			btnAddChild = new ButtonObject(showName);
			btnAddChild.setPowerContrl(false);
			// 设置按钮的TAG为“3C:1001AA10000000004SG5”
			btnAddChild.setTag(billBusiVOs[i].getPk_billtype().trim() + ":" + billBusiVOs[i].getPk_businesstype().trim());
			getBtns().m_boRefAdd.addChildButton(btnAddChild);
     }
  }

  /**
   * 取消发票操作。 创建日期：(2001-4-20 11:19:14)
   */

  private void onCancelSave() {
 
    // 设置PANEL，不可更改和后续语句之顺序，因内部使用了当前操作状态判断
    if (null != getVOCache().getCurrentVO()
        && null != getVOCache().getCurrentVO().getHeadVO()
        && getVOCache().getCurrentVO().getHeadVO().isNew()) {
      // 来自于出库汇总开票，则此VO还未曾纳入到缓存中来
      if (!getBillCardPanel().isOutSumMakeInvoice()) 
        getVOCache().removeVOAt(getVOCache().getPos()); 
    }
    //获得取消前列表下选中的VO主键
    if(null == getVOCache().getCurrentVO() && null != getBillListPanel().getSelectedVO()
        && null != getBillListPanel().getSelectedVO().getHeadVO()){
     String csaleid = (String) getBillListPanel().getSelectedVO().getHeadVO().getPrimaryKey();
     getVOCache().setPos(getVOCache().findPos(csaleid));
    }
   
    getBillCardPanel().setPanelAfterCancelSave(getVOCache().getCurrentVO());
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();

    showHintMessage("");
    
    //非出库汇总
    getBillCardPanel().setOutSumMakeInvoice(false);

    updateUI();

  }

  /**
   * 此处插入方法说明。 创建日期：(2001-4-20 11:18:26)
   */
  private void onModify() {
    if (getShowState() == ListShow) {
      int iRow = getBillListPanel().getHeadTable().getSelectedRow();
      if (iRow < 0) {
        showErrorMessage("请选择列表中的一行发票");
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
    //发票修改的时候依据当前VO的业务类型去设置可参照的单据类型
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
                                             * @res "编辑单据..."
                                             */);
  }

  // }
  /**
   * 方法功能描述：简要描述本方法的功能。
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-6 上午10:06:28
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
   * 批量取消合并开票
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-7-30 下午04:50:26
   */
  private void batchCancelUnit() {
    java.util.HashMap hmapIdVsVO = null;
    java.util.HashMap hSuccess = new java.util.HashMap();

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(0));
    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-100055")/*
                                                       * @res
                                                       * "正在进行取消合并开票前的准备..."
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
                                 * @res "请选择待合并开票的发票！"
                                 */);
      return;
    }

    int max = m_proccdlg.getUIProgressBar1().getMaximum();
    int maxcount = hmapIdVsVO.size();

    ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes.getInstance()
        .getStrByID("40060501", "UPP40060501-100057")/*
                                                       * @res "开始合并开票..."
                                                       */);

    SaleinvoiceVO voInvoice = null;
    java.util.Iterator iter = hmapIdVsVO.keySet().iterator();
    int count = 0;

    while (iter.hasNext()) {
      Object key = iter.next();
//       TODO 此处对ID赋值
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
                     * proccdlg, "正在合并开票...[" /* + ((SaleVO)
                     * saleinvoice.getParentVO()).getVreceiptcode() /* + "]");
                     */
      voInvoice.setCuruserid(getClientEnvironment().getUser().getPrimaryKey());

      if (m_proccdlg.isInterrupted())
        break;

      // (1)加载数据到CARD－＞(2)合并开票（恢复数据）－(3)＞保存

      // (1)加载数据到CARD
      getBillCardPanel().loadCardData(voInvoice);
      // 保存修改前单据号
      SaleinvoiceVO hvo = (SaleinvoiceVO) getBillCardPanel().getVO();
      getBillCardPanel().setOldVO(hvo);
      getBillCardPanel().updateValue();
      // (2)合并开票
      boolean needUnitCancel = getBillCardPanel().uniteCancel();
      if (!needUnitCancel) {
        continue;
      }
      // (3)保存
      String sBusinessException = onSave();

      if (sBusinessException != null) {
        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100061", null, new String[] {
              ((SaleVO) voInvoice.getParentVO()).getVreceiptcode()
            });
        sMsg += sBusinessException;
        ShowToolsInThread.showMessage(m_proccdlg, " ", sMsg);/*
                                                               * ShowToolsInThread.showMessage(proccdlg, " ",
                                                               * "发票[" +
                                                               * ((SaleVO)
                                                               * saleinvoice.getParentVO())
                                                               * .getVreceiptcode() +
                                                               * "]合并开票失败：" +
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
                                         * @res "是否继续合并开票以下的发票？"
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
        // 从界面得到新VO
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
                                                               * "发票[" +
                                                               * ((SaleVO)
                                                               * saleinvoice.getParentVO())
                                                               * .getVreceiptcode() +
                                                               * "]合并开票成功！",
                                                               * "发票[" +
                                                               * ((SaleVO)
                                                               * saleinvoice.getParentVO())
                                                               * .getVreceiptcode() +
                                                               * "]合并开票成功！");}
                                                               */
      }
      count++;
    }

    ShowToolsInThread.showStatus(m_proccdlg, new Integer(max));

    if (m_proccdlg.isInterrupted()) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100042")/*
                                                                       * @res
                                                                       * "合并开票操作被用户中断！"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100044")/*
                                                                       * @res
                                                                       * "合并开票操作结束！"
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
                                                                       * "正在更新界面数据..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  /**
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-9 下午04:20:05
   */
  private void onNew(ButtonObject bo) {
    //时间桩
    UFDouble chgvotime = null;
    UFDouble dealmnytime = null;
    UFDouble addtouitime = null;
    PfUtilClient.childButtonClicked(bo, SaleInvoiceTools.getLoginPk_Corp(),
        "40060302", SaleInvoiceTools.getLoginUserId(),
        SaleBillType.SaleInvoice, this);
    
    long s = System.currentTimeMillis();
    nc.vo.scm.pub.SCMEnv.out("进行VO对照开始...");
    SaleinvoiceVO[] vosNew = null;
    if (!nc.ui.pub.pf.PfUtilClient.makeFlag) {
      if (PfUtilClient.isCloseOK()) {
        vosNew = (SaleinvoiceVO[]) PfUtilClient.getRetVos();
      }
    }
    
    chgvotime = SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s)
                          .div(1000);
    nc.vo.scm.pub.SCMEnv.out("VO对照共用时[ "+ chgvotime  + " S] ");
    
    if (null == vosNew || vosNew.length == 0  
        || null == vosNew[0].getBodyVO() || vosNew[0].getBodyVO().length ==0)
      return;
    //上游单据类型  
    String sUpSrcType = vosNew[0].getBodyVO()[0].getCupreceipttype();
    //显示订单号字段
    if (SaleBillType.SaleOrder.equals(sUpSrcType)) {
	      getBillCardPanel().hideBodyTableCol("cupsourcebillcode");
	     }
    //显示出库单号字段
	  else if (SaleBillType.SaleOutStore.equals(sUpSrcType)) {
	      getBillCardPanel().showBodyTableCol("cupsourcebillcode");
	      getBillCardPanel().hideBodyTableCol("csourcebillcode");
	     }
    //设置默认发票类型
    vosNew[0].getHeadVO().setFinvoicetype(getDefaultInvoiceType());
    
    //解决姜哲提出的“,开票客户不同的销售订单开一张票时应为第一个订单开票客户”
    //通过流程平台取到的VO数组顺序是颠倒的,因此要进行下转换
    if(vosNew.length > 1 && st.SO_30 .booleanValue() ){
    	SaleinvoiceVO temp = vosNew[0];
    	vosNew[0] = vosNew[vosNew.length-1];
    	vosNew[vosNew.length-1]= temp;
    }
    
  // 20070917
 //	saveSourceMny(vosNew);
   s = System.currentTimeMillis();
  nc.vo.scm.pub.SCMEnv.out("进行分单、尾差处理开始...");
  //按照分单条件分单
	SaleinvoiceVO[] aryRetVO = st.mergeSourceVOs(vosNew);

	/** 处理多次开票尾差 */
	if (SaleBillType.SaleOutStore.equals(sUpSrcType)) {
		dealMny(aryRetVO);
	}
  dealmnytime = SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s)
   .div(1000);
  nc.vo.scm.pub.SCMEnv.out("分单、尾差处理共用时[ " + dealmnytime + " S] ");
  //计算发票表头的合计金额、冲减金额、净额 ，每行发票附表行的冲减前金额
  calcTotalMny(aryRetVO);
	  
    // 多张转到列表，一张则到卡片
    if (aryRetVO.length > 1) {
      //设置缓存
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
      nc.vo.scm.pub.SCMEnv.out("加载新增发票VO到卡片界面处理开始...");
      // TODO 此处重复，但否则会抛错，可再优化
//      long s2 = System.currentTimeMillis();
//      nc.vo.scm.pub.SCMEnv.out("----------------------------------------------");
//      nc.vo.scm.pub.SCMEnv.out("loadCardData方法处理开始...");
    
      getBillCardPanel().loadCardData(aryRetVO[0]);
      
      
//      nc.vo.scm.pub.SCMEnv.out("loadCardData方法处理共用时[ "+ SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s2).div(1000)+ " S] ");
//      
//      s2 = System.currentTimeMillis();
//      nc.vo.scm.pub.SCMEnv.out("modify方法处理开始...");
      
      
      getBillCardPanel().modify(aryRetVO[0]);
      
      
//      nc.vo.scm.pub.SCMEnv.out("modify方法处理共用时[ "+ SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s2).div(1000)+ " S] ");
      addtouitime = SmartVODataUtils.getUFDouble(System.currentTimeMillis() - s).div(1000);
      nc.vo.scm.pub.SCMEnv.out("加载新增发票VO到卡片界面处理共用时[ "+ addtouitime+ " S] ");
      // --------3------------------------
      setShowState(CardShow);
      setOperationState(ISaleInvoiceOperState.STATE_EDIT);
    
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon",
          "UPPSCMCommon-000350")
      /*
       * @res "编辑单据..."
       */
      );
      
    //设置表头默认值，此处计算迁移到前面，因为如果多单的时候会无法计算表头的金额合计
//    getBillCardPanel().setHeadItem("ninvoicediscountrate", new UFDouble(100));
//    getBillCardPanel().setHeadItem("ntotalsummny", getBillCardPanel().calcurateTotal("noriginalcursummny"));
//    getBillCardPanel().setHeadItem("nstrikemny", new UFDouble(0));
//    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
    setButtonsStateEdit();
//    if(null !=chgvotime && null != dealmnytime
//        && null != addtouitime ){
//      UFDouble totaltime = addtouitime.add(dealmnytime).add(addtouitime);
//      nc.vo.scm.pub.SCMEnv.out("发票新增三个部分共用时[ " + totaltime+ " S ]"); 
//      
//      nc.vo.scm.pub.SCMEnv.out("VO对照占用比率[ " 
//          + addtouitime.div(totaltime).multiply(100)+"% ]"); 
//      
//      nc.vo.scm.pub.SCMEnv.out("分单、尾差处理占用比率[ " 
//          + dealmnytime.div(totaltime).multiply(100)+"% ]"); 
//      
//      nc.vo.scm.pub.SCMEnv.out("加载新增发票VO到卡片界面处理占用比率[ " 
//          + addtouitime.div(totaltime).multiply(100)+"% ]"); 
//    }
    }
  }
/**
 * 处理销售订单->出库单->发票流程中的尾差
 * 因为前台分单之后数据发生变化，所以需要在前台重新进行尾差处理
 * @author jianghp 20080519
 */
private void dealMny(SaleinvoiceVO[] aryRetVO) {
  
	for (int i = 0,loop = aryRetVO.length; i < loop; i++) {
		//按照出库单的来源单据类型将表体分开，分别进行尾差处理
		Hashtable<String, ArrayList<SaleinvoiceBVO>> ht = new Hashtable<String, ArrayList<SaleinvoiceBVO>>();
    //缓存途损数量，在处理尾差时考虑
    HashMap<String, UFDouble>  hswastagenum = new HashMap<String,UFDouble>();
		for (int j = 0,jloop = aryRetVO[i].getChildrenVO().length; j < jloop; j++) {
      
      SaleinvoiceBVO voicebvo = aryRetVO[i].getBodyVO()[j];
      //源头单据类型
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
        //要减去累计途损数量
        if (hswastagenum.containsKey(csourcebid)) {
                UFDouble wastagenum = hswastagenum.get(csourcebid);
                CircularlyAccessibleValueObject voUpInfo = (CircularlyAccessibleValueObject) voUpInfos[j];
                // 数量 = 上游单据数量 - 累计途损数量
                voUpInfo.setAttributeValue("nnumber", SmartVODataUtils
                    .getUFDouble(voUpInfo.getAttributeValue("nnumber")).sub(
                        wastagenum));
                String price = "noriginalcurnetprice";
                String mny = "noriginalcurmny";
                // 含税优先
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

		// 由于尾差处理之后，可能单行的价税合计、税额、无税金额之间的关系不满足，所以需要重新调整
		try {
      for(int j =0,jloop = aryRetVO[i].getBodyVO().length;j<jloop;j++){
       SaleinvoiceBVO saleItem  =aryRetVO[i].getBodyVO()[j];
      //设置订单折扣 = 整单折扣 * 单品折扣
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
 * 方法功能描述：设置销售发票表头金额合计字段的数值，并且设置表体行的冲减前金额。
 * <b>参数说明</b>
 * @param retvos
 * @time 2008-12-8 下午07:08:09
 */
	private void calcTotalMny(SaleinvoiceVO[] newinvoicevos) {
		for(SaleinvoiceVO newinvoicevo:newinvoicevos){
      //设置表头发票折扣默认值为100
      newinvoicevo.getHeadVO().setNinvoicediscountrate(new UFDouble(100));
      //发票表头合计金额
      UFDouble ntotalsummny = new UFDouble(0);
			for(SaleinvoiceBVO newbvo:newinvoicevo.getBodyVO()){
        newbvo.setNsubsummny(newbvo.getNoriginalcursummny());
        newbvo.setNsubcursummny(newbvo.getNsummny());
        //过滤掉赠品行
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
	 * 查询跟当前出库单表体行同源的其它出库单行的结算记录
	 * 
	 * @param corder_bid
	 * @param curitemid
	 * @return
	 * @throws Exception
	 */
	private IEqualValueDownBill[] getOtherDownInfo(String corder_bid) throws Exception {
		// 根据出库单纪录的来源行id，查询来源行对应的其它下游行
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
	 * 查询当前表体已经存在的所有发票记录
	 * 
	 * @param corder_bids
	 * @return
	 */
	private Hashtable<String,ArrayList<IEqualValueDownBill[]>> getOtherDownInfo(String[] corder_bids) {
		// 查询所有结算记录
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
  	 * 根据销售发票模板上的默认发票类型来初始化下拉框
  	 * @return
  	 */
  private int getDefaultInvoiceType() {
      
   //模板上设置的默认发票类型 
   String defaultInvoiceType = getBillCardPanel().getHeadItem(
          "finvoicetype").getDefaultValue();
   //如果用户设置默认发票类型
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
   * 保存发票数据 因有些程序需要返回异常，所以有异常时返回异常
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @return
   * <p>
   * @author wangyf
   * @time 2007-7-31 上午10:59:13
   */
  private String onSave() {

    // 得到待保存的VO，同时进行了检验
    SaleinvoiceVO voSaved = getBillCardPanel().getSaveVO();
    // 得到的待保存VO为空,返回
    if (null == voSaved) 
      return null;
    //设置前台clientlink
    voSaved.setCl(new ClientLink(getClientEnvironment()));
    //设置出库汇总开票
    voSaved.setIsOutSumMakeInvoice(getBillCardPanel().isOutSumMakeInvoice());
   
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
        "UPP40060501-000080")/* @res "开始保存数据...." */);
    nc.vo.scm.pub.SCMEnv.out("开始保存：" + System.currentTimeMillis());

//    ArrayList<String> listBillID = null;
    SaleinvoiceVO newinvoicevo = null;
    String sMessage = "";
    try {
      //  二次开发扩展
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
      //    二次开发扩展
      getPluginProxy().afterAction(nc.vo.scm.plugin.Action.SAVE, new SaleinvoiceVO[]{voSaved});
    }
    catch (Exception e) {
      sMessage = e.getMessage();
      showErrorMessage(sMessage);
      nc.vo.scm.pub.SCMEnv.out(e);

      return sMessage;

    }
    /************记录业务日志*************/
    insertOperLog(voSaved, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
        "UC001-0000001")/* @res"保存" */);
    /************记录业务日志* end ************/
    
    nc.vo.scm.pub.SCMEnv.out("保存结束：" + System.currentTimeMillis());
    showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
        "UPP40060501-000083"));

    // 如果发票是对冲生成，则还需更新此发票的源头发票
    // 原来代码是更新LIST，此处修改为更新CACHE
    String sUpSrcType = (String) getBillCardPanel().getBodyValueAt(0,
        "cupreceipttype");
    if (getBillCardPanel().isNewBill()
        && SaleBillType.SaleInvoice.equals(sUpSrcType)) {
      SaleinvoiceVO voUp = getVOCache().getVO_Load(
          (String) voSaved.getBodyVO()[0].getCupsourcebillid());

      String strWhere = "so_saleinvoice.csaleid = '"
          + voUp.getHeadVO().getCsaleid() + "'";
      // 重新加载表头TS
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
    //卡片下刷新界面和缓存数据
    // 设置PANEL
    getBillCardPanel().setPanelAfterSave(newinvoicevo, isInMsgPanel());

    // 更新缓存
    updateCacheVOByCard();
    //恢复颜色
    nc.ui.scm.pub.panel.SetColor.resetColor(getBillCardPanel());
    //出库汇总状态
    getBillCardPanel().setOutSumMakeInvoice(false);

    // 按钮状态
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
 
    setButtonsStateBrowse();
   

    return sMessage.length() == 0 ? null : sMessage;
  }

  /**
   * 此处插入方法说明。 创建日期：(2003-11-21 11:26:14)
   * 
   * @return java.util.ArrayList
   */
  protected ArrayList getFormulaItemHeader() {
    ArrayList arylistHeadField = new ArrayList();

    // 表头及表尾涉及到的公式
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
   * 审批操作
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param vo
   * @return
   * @throws Exception
   *           <p>
   * @author wangyf
   * @time 2007-8-2 下午02:57:54
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
        throw new nc.vo.pub.BusinessException("审批日期不能小于制单日期！");

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
   * 获得单据类型。 创建日期：(2001-11-15 8:52:43)
   * 
   * @return java.lang.String
   */
  private nc.ui.scm.sourcebill.SourceBillFlowDlg getSourceDlg() {
    nc.ui.scm.sourcebill.SourceBillFlowDlg soureDlg = new nc.ui.scm.sourcebill.SourceBillFlowDlg(
        this, SaleBillType.SaleInvoice,/* 当前单据类型 */
        ((SaleinvoiceVO) getVo()).getHeadVO().getCsaleid(), getBillCardPanel()
            .getBusiType(),/* 当前业务类型 */
        getBillCardPanel().getOperator(),/* 当前用户ID */
        ((SaleinvoiceVO)getVo()).getHeadVO().getVreceiptcode()/* 单据号 */
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
   * 发票执行情况
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param bo
   *          <p>
   * @author wangyf
   * @time 2007-3-23 下午03:21:12
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
          "UPP40060501-000069")/* @res "失败！" */);
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
            "UPP40060501-100071")/* @res "请选择待审批的发票！" */);
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
        // ShowToolsInThread.showMessage(proccdlg, " ", "发票["
        // + ((SaleVO) saleinvoice.getParentVO())
        // .getVreceiptcode() + "]审批失败：" + e.getMessage());
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
                                         * @res "是否继续审批以下的发票？"
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
                                                                       * "审批操作被用户中断！"
                                                                       */);
    }
    else {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100070")/*
                                                                       * @res
                                                                       * "审批操作结束！"
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
                                                                       * "正在更新界面数据..."
                                                                       */);
      ShowToolsInThread.doLoadAdminData(this);
    }

  }

  private void setInMsgPanel(boolean newBInMsgPanel) {
    m_bInMsgPanel = newBInMsgPanel;
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.pub.linkoperate.ILinkApprove#doApproveAction(nc.ui.pub.linkoperate.ILinkApproveData)
   */
  public void doApproveAction(ILinkApproveData approvedata) {
    setInMsgPanel(true);
    // 加载数据
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
   * IInvoiceListPanel接口方法
   * 
   * @see nc.ui.so.so002.IInvoiceListPanel#getVOCache()
   */
  public SaleInvoiceVOCache getVOCache() {
    // 
    return m_vocache;
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.pub.linkoperate.ILinkQuery#doQueryAction(nc.ui.pub.linkoperate.ILinkQueryData)
   */
  public void doQueryAction(ILinkQueryData querydata) {
    // 加载数据
    getBillCardPanel().loadDataWhole(querydata.getBillID(), isInMsgPanel());
    // 根据单据状态设置单据
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
   * 此处插入方法说明。 创建日期：(2003-11-6 15:19:12)
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
                                                                           * "单据类型配置了工作流，但该单据没有在工作流中。"
                                                                           */);
        }
      }
    }
    catch (Throwable e) {
      throw new nc.vo.pub.ValidationException(nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("40060501", "UPP40060501-000070")/*
                                                         * @res
                                                         * "单据类型配置了工作流，但该单据没有在工作流中。"
                                                         */);
    }
  }

  /**
   * 合并显示 创建日期：
   */
  private void onBillCombin() {
    //fengjb  20080917 修改初始化方法
    SOCollectSettingDlg dlg = new SOCollectSettingDlg(this,SaleBillType.SaleInvoice,
        SaleInvoiceTools.getNodeCode(),SaleInvoiceTools.getLoginPk_Corp(),SaleInvoiceTools.getLoginUserId(),
        SaleinvoiceVO.class.getName(),SaleVO.class.getName(),SaleinvoiceBVO.class.getName());
    
    dlg.setBilltype(ScmConst.SO_Invoice);
    dlg.setNodecode(SaleInvoiceTools.getNodeCode());
    
    //  v55 合并显示新需求 增加存货类合并条件
    Configuration configuation =  nc.vo.scm.goldtax.Configuration.load(getClientEnvironment().getCorporation().getPk_corp());
    ArrayList<String> aryfixgroup = new ArrayList<String>();
    //如果金税参数为按存货类合并，默认合并栏目为：存货分类编码、存货分类名称、币种
    if(configuation.isMergeInvClass()){
      aryfixgroup.add("invclasscode");
      aryfixgroup.add("invclassname");
      aryfixgroup.add("ccurrencytypename");
      getBillCardPanel().getBodyItem("invclasscode").setShow(true);
      getBillCardPanel().getBodyItem("invclassname").setShow(true);
    }
    //如果金税参数为按存货合并或者不合并，默认合并栏目为：存货编码、存货名称、规格、型号、币种
    else{
      aryfixgroup.add("cinventorycode");
      aryfixgroup.add("cinventoryname");
      aryfixgroup.add("GG");
      aryfixgroup.add("XX");
      aryfixgroup.add("ccurrencytypename");
     
    }
    //如果勾选了按单价合并，默认合并显示栏目增加：含税净价
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
        "UPT40060501-000058")/* @res "合并显示" */);

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
	 * 联查 创建日期：(2001-6-1 13:12:36)
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
   * 批弃审中弃审一张发票
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param vo
   * @return
   * @throws Exception
   *           <p>
   * @author wangyf
   * @time 2007-8-7 下午03:27:34
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
      /************记录业务日志*************/
      insertOperLog(saleinvoice, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000028")/* @res"弃审" */);
      /************记录业务日志* end ************/
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * 单据打印后刷新ts(2004-12-01 23:25:18)
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
   * 方法功能描述：V55修改为使用billprinttools实现支持多模板选择。
   * <b>参数说明</b>
   * @param previewflag
   * @author fengjb
   * @time 2008-11-26 下午01:13:02
   */
  private void onPrint(boolean previewflag) {
    
    boolean total = getBillCardPanel().getBodyPanel().isTatolRow();
    //不打印小计合计行，解决用户打印翻倍问题
    getBillCardPanel().getBodyPanel().setTotalRowShow(false);
  
    try {
      ArrayList<SaleinvoiceVO> alPrintVO = new ArrayList<SaleinvoiceVO>();
      //列表显示时
      if (ListShow == getShowState() && !previewflag) {
        //列表下选中行
        int selectRows[] = getBillListPanel().getHeadTable().getSelectedRows();
        for (int i = 0,iloop = selectRows.length; i < iloop; i++) {
            //加载数据
            getBillCardPanel().loadCardData(
                getVOCache().getVO_Load(selectRows[i]));
      
            SaleinvoiceVO saleinvoice = (SaleinvoiceVO) getBillCardPanel()
            .getBillValueVO(SaleinvoiceVO.class.getName(),
                SaleVO.class.getName(), SaleinvoiceBVO.class.getName());
             alPrintVO.add(saleinvoice);
        }
      }else{
      if(ListShow == getShowState()){
        //列表下选中行
        int selectRows[] = getBillListPanel().getHeadTable().getSelectedRows();
        //加载数据
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
   * 方法功能描述：批操作中删除一张销售发票。
   * <b>参数说明</b>
   * @param vo
   * @return
   * @throws Exception
   * @author fengjb
   * @time 2008-11-26 上午10:17:25
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
    // 发票是对冲生成 更新列表上的原发票的对冲标记
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
              // 更改表头数据
              getBillListPanel().getHeadBillModel().setValueAt(
                  newsourheadvo.getTs(), i, "ts");
              getBillListPanel().getHeadBillModel().setValueAt(
                  newsourheadvo.getFcounteractflag(), i, "fcounteractflag");
          }
        }
      }
    }
    if (PfUtilClient.isSuccess()) {
      /************记录业务日志*************/
      insertOperLog(vo, nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC001-0000039")/* @res"删除" */);
     /************记录业务日志* end ************/
      showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("scmsopreor",
          "UPPscmsopreor-000055")/* @res "作废成功！" */);
      return true;
    }

    return false;
  }

  /*****************************************************************************
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:2003-09-27 作者：左小军
   ****************************************************************************/
  private void onSendAudit() {
	if(getOperationState() != ISaleInvoiceOperState.STATE_BROWSE)
     onSave();
  SaleinvoiceVO voCur = null;
    //获得要送审的VO
   if (getShowState() == ListShow) {
     voCur = getBillListPanel().getSelectedVO();
   }
   else if (getShowState() == CardShow) {
     voCur = getBillCardPanel().getVO();
   }       

   if (null == voCur || null == voCur.getParentVO()) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "请选择单据" */);
    } else {
      try {
        boolean isExist = nc.ui.pub.pf.PfUtilBO_Client.isExistWorkFlow(
            SaleBillType.SaleInvoice, voCur.getHeadVO().getCbiztype(),
            getClientEnvironment().getCorporation().getPk_corp(),
            getClientEnvironment().getUser().getPrimaryKey());
        if (! isExist) {
          showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "SCMCOMMON", "UPPSCMCommon-000111")/*
                                                   * @res "该操作员没有配置审批流"
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
                                                     * @res "单据已发送到审批流中，但尚未开始审批"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_FINISHED)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000078")/*
                                                     * @res "单据已审批完成"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.WORKFLOW_ON_PROCESS)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000079")/*
                                                     * @res "单据正在审批中"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.BILLTYPE_NO_WORKFLOW)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000112")/*
                                                     * @res "该单据类型没有配置工作流"
                                                     */);
          else if (iWorkflowstate == nc.vo.pub.pf.IWorkFlowStatus.ABNORMAL_WORKFLOW_STATUS)
            showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
                "SCMCOMMON", "UPPSCMCommon-000277")/*
                                                     * @res "未知的异常状态"
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
                                                 * @res "送审成功!"
                                                 */);
        }
        else {
          showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
              "40060301", "UPP40060301-000292")/*
                                                 * @res "送审操作已经被用户取消！"
                                                 */);
        }
        //刷新前台数据
        updateUIValue(hsnewvo);
        
        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "40060301", "UPP40060301-000291")/*
             * @res "送审成功!"
             */);
      }
      catch (Exception e) {
        showWarningMessage(e.getMessage()
            + nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
                "UPPSCMCommon-000408")/*
                                       * @res "送审失败！"
                                       */);
      }
    }
  }

  /**方法功能描述：更新当前界面UI数据和数据库中一致。
   * <b>参数说明</b>
   * @param voCur
   * @param hsts
   * @author fengjb
   * @time 2008-11-19 上午11:26:12
   */
  private void updateUIValue(HashMap<String, SmartVO> hsnewvo) {
    
    //要更新值为空
    if(hsnewvo == null || hsnewvo.size() ==0)
      return;
    
    //列表界面下
    if (getShowState() == ListShow) {
      //刷新界面显示
      getBillListPanel().updateUIValue(hsnewvo);
      //刷新缓存
      updateCacheVOByList();
      
      getBillListPanel().updateUI();
    }
    else if (getShowState() == CardShow) {
      // 设置为审批中状态
      getBillCardPanel().updateUIValue(hsnewvo);
      updateCacheVOByCard(); 
      
      getBillCardPanel().updateValue();
    }       
    setOperationState(ISaleInvoiceOperState.STATE_BROWSE);
    setButtonsStateBrowse();
    
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.so.so002.IInvoiceCardPanel#setOperationState(int)
   */
  public void setOperationState(int iState) {
    m_iOperationState = iState;
  }

  /**
   * 此处插入方法说明。 创建日期：(2001-4-20 11:21:59) 修改日期：2003-9-8 修改人：杨涛 修改日期：2003-11-07
   * 修改人：杨涛 修改内容：按出库单号查询 修改日期：2003-12-02 修改内容：增加缓存 修改日期：2003-12-12 修改人：杨涛
   * 修改内容：增加自定义项设置
   */
  private void onQuery() {

    if (getQueryDlg().showModal() == QueryConditionClient.ID_CANCEL)
      return;

    m_bEverQuery = true;

    onRefresh();
  }

  /**
   * 返回当前操作状态
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b> iStatie 操作状态
   * 
   * @return 业务类型
   *         <p>
   * @author wangyf
   * @time 2007-3-6 上午10:33:09
   */
  public int getOperationState() {

    return m_iOperationState;
  }

  /**
   * 此处插入方法说明。 功能描述: 输入参数: 返回值: 异常处理: 日期:
   */
  private void onAuditFlowStatus() {
    SaleinvoiceVO hvo = null;
    if (getShowState() == ListShow) hvo = getBillListPanel().getSelectedVO();
    else hvo = (SaleinvoiceVO) getBillCardPanel().getVO();
    if (hvo == null || hvo.getParentVO() == null) {
      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON",
          "UPPSCMCommon-000199")/* @res "请选择单据" */);
    }
    else {
      SaleVO header = (SaleVO) hvo.getParentVO();
      String pk = header.getCsaleid();
      if (pk == null) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
            "SCMCOMMON", "UPPSCMCommon-000067")/* @res "单据号为空" */);
      }
      else {
        nc.ui.pub.workflownote.FlowStateDlg approvestatedlg = new nc.ui.pub.workflownote.FlowStateDlg(
            this, "32", pk);
        approvestatedlg.showModal();
      }
    }
  }

  /**
   * 合并开票
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-7-30 下午04:45:46
   */
  private void onUnite() {
    if (getShowState() == ListShow) {

      String sCode = getVOCache().getCurrentVO().getHeadVO().getVreceiptcode();

      if (getBillListPanel().autoUnit()) {

        String sMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("40060501",
            "UPP40060501-100051", null, new String[] {
              sCode
            }/**发票[{0}]没有符合条件的冲应收单**/);
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
        "UPT40060501-000048")/* @res "合并开票" */);
  }

  /**
   * 此处插入方法说明。 创建日期：(2004-02-06 10:38:09)
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
   * 关闭窗口的客户端接口。可在本方法内完成窗口关闭前的工作。
   * 
   * @return boolean 返回值为true表示允许窗口关闭，返回值为false表示不允许窗口关闭。 创建日期：(2001-8-8
   *         13:52:37)
   */
  public boolean onClosing() {
    if (getOperationState() == ISaleInvoiceOperState.STATE_EDIT) {
      int nReturn = MessageDialog.showYesNoCancelDlg(this, nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCOMMON", "UPPSCMCommon-000270")/* @res "提示" */,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UCH001")/* @res "是否保存已修改的数据？" */);
       
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
   * 子类实现该方法，响应按钮事件。
   * 
   * @version (00-6-1 10:32:59)
   * @param bo
   *          ButtonObject
   */

  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
    // 易用性修改 提示
    showHintMessage("");
  
    try {
		//  二次开发扩展
		getPluginProxy().beforeButtonClicked(bo);
  } catch (BusinessException e) {
    SCMEnv.out(e);
    showErrorMessage(e.getMessage());
    return;
  }

    // 列表选多行处理
    if (getShowState() == ListShow) {
      if (getBillListPanel().getHeadTable().getSelectedRowCount() > 1) {
        if (bo == getBtns().m_boApprove || bo == getBtns().m_boBlankOut
            || bo == getBtns().m_boUnApprove || bo == getBtns().m_boUnite
            || bo == getBtns().m_boUniteCancel) {
          m_proccdlg = new ProccDlg(this, bo.getHint(), this, bo.getTag());
          m_proccdlg.showModal();
          return;
        }
        // ????? 此处需再确认
        // else {
        // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
        // "40060501", "UPP40060501-000072")/*
        // * @res "请选择单行"
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
      // * @res "请选择一条单据"
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

    // 业务类型
    if (bo.getParent() == getBtns().m_boBusiType) {
      onBusiType(bo);
    }
    // 新增
    else if (bo.getParent() == getBtns().m_boAdd) {
      // 出库汇总开票
      if (bo == getBtns().m_boGather) {
        onOutSumMakeInvoice();
      }
      else {
        onNew(bo);
      }
    //参照增行
    }else if(bo.getParent() == getBtns().m_boRefAdd){
    	onRefAddLine(bo);
    }
    //取成本价
    else if(bo == getBtns().m_boFetchCost ){
    	onFetchCost();
    }
    // 保存
    else if (bo == getBtns().m_boSave)
      onSave();
    // 审批
    else if (bo == getBtns().m_boApprove)
      onApprove();
    // 切换
    else if (bo == getBtns().m_boCard) {
      if (getShowState() == CardShow) {
        onList();
      }
      else {
        onCard();
      }
    }
    // 维护
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
    // 行操作
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
    // 执行
    else if (bo.getParent() == getBtns().m_boAction) {
      if (bo == getBtns().m_boSendAudit)
        onSendAudit();
      else if (bo == getBtns().m_boUnApprove) {
        onUnApprove();
      }
    }
    // 查询
    else if (bo == getBtns().m_boQuery) {
      onQuery();
      return;
    }
    // 浏览
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
    // 打印
    else if (bo.getParent() == getBtns().m_boPrintManage) {
      if (bo == getBtns().m_boPrint)
        onPrint(false);
      else if (bo == getBtns().m_boPreview) {
        onPrint(true);
      }
      // 合并显示
      else if (bo == getBtns().m_boBillCombin)
        onBillCombin();
    }
    // 辅助查询
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
    // 辅助功能
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
	//  二次开发扩展
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
   * 方法功能描述：卡片编辑功能实现。
   * <b>参数说明</b>
   * @author fengjb
   * @time 2008-9-11 下午01:36:38
   */
  private void onBoCardEdit(){
    getBillCardPanel().startRowCardEdit();
 }

  /**
   * 方法功能描述：V55销售发票支持无利润销售，增加“取成本价”按钮。
   * <b>参数说明</b>
   * @author fengjb
   * @time 2008-7-29 下午08:03:38
   */
  private void onFetchCost() {
    //取当前界面VO值
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
	   //如果销售发票来源于销售出库单，取成本价
	   if(SaleBillType.SaleOutStore.equals(body[i].getCupreceipttype())
         && null != body[i].getCupsourcebillbodyid() ){
		 saleout.put(body[i].getCupsourcebillbodyid(),i);
	   //否则，认为是错误行，给提示
	   }
   }
   Hashtable hcostprice = new Hashtable();
   Set<String> keyset = saleout.keySet();
   String[] saleoutbid = null;
   //存在上游是销售出库单的发票行
   if(keyset.size() > 0){
   try{
   saleoutbid= new String[keyset.size()];
   keyset.toArray(saleoutbid);
   //查询发票上游出库单对应行存货核算回写的单价
   hcostprice = SaleinvoiceBO_Client.queryCostPrice(saleoutbid);
   }catch(Exception e){
	   SCMEnv.out(e.getMessage());
	   MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
		          .getStrByID("scmcommon", "UPPSCMCommon-000059")/* @res "错误" */, e
		          .getMessage());
   }
   //分析查询得到的结果
  for(int i=0,iloop =saleoutbid.length;i<iloop;i++){
	  //获得所在界面上的行号
	  int row = saleout.get(saleoutbid[i]);
	  //折本汇率
	  UFDouble nexchangeotobrate = body[row].getNexchangeotobrate();
	  //如果取到存货核算回写销售出库单的本币无税单价
	  if(hcostprice.containsKey(saleoutbid[i]) && 
			  hcostprice.get(saleoutbid[i]) != null){
		  UFDouble ncostprice = new UFDouble((BigDecimal)hcostprice.get(saleoutbid[i]));
		  //原币无税单价 = 本币无税单价 * 汇率
      UFDouble noriginalcurprice = null;
      try{
      String pk_corp = saleinvoice.getHeadVO().getPk_corp();
      BusinessCurrencyRateUtil currateutil =  new BusinessCurrencyRateUtil(pk_corp);
      SOCurrencyRateUtil socurrateutil = new SOCurrencyRateUtil(pk_corp);
      String pk_curtype = saleinvoice.getHeadVO().getCcurrencyid();
      //币种VO
      noriginalcurprice = currateutil.getAmountByOpp(socurrateutil.getLocalCurrPK(),
          pk_curtype,ncostprice, nexchangeotobrate, 
          saleinvoice.getHeadVO().getDbilldate()==null?
              getClientEnvironment().getDate().toString(): saleinvoice.getHeadVO().getDbilldate().toString());
      }catch(Exception e){
        SCMEnv.out("取成本价过程出错:"+e);
        showErrorMessage("取成本价过程出错:"+e.getMessage()); 
        return;
      }
		  //基价是否含税
		  if(st.SA_02.booleanValue()){
              //税率
			  UFDouble ntaxrate = body[row].getNtaxrate()==null?new UFDouble(0):body[row].getNtaxrate();
			  //原币含税净价 = 原币无税单价 * (1+税率/100)
			  UFDouble noriginalcurtaxnetprice = noriginalcurprice.multiply(new UFDouble(1).add(ntaxrate.div(new UFDouble(100))));
			  getBillCardPanel().setBodyValueAt(noriginalcurtaxnetprice, row, "noriginalcurtaxnetprice");
			 
		  }else
			  getBillCardPanel().setBodyValueAt(noriginalcurprice, row, "noriginalcurnetprice");
      
      //重新计算数量单价金额
      //缓存原先的整单折扣
       UFDouble ndiscountrate = getBillCardPanel().getBodyValueAt(row, "ndiscountrate") == null? new UFDouble(100):SmartVODataUtils.getUFDouble(getBillCardPanel().getBodyValueAt(row, "ndiscountrate"));
      //把 整单折扣 * 单品折扣作为订单折扣放到整单折扣字段上
       UFDouble nitemdiscount = getBillCardPanel().getBodyValueAt(row, "nitemdiscountrate") == null? new UFDouble(100):SmartVODataUtils.getUFDouble(getBillCardPanel().getBodyValueAt(row, "nitemdiscountrate"));;
       getBillCardPanel().setBodyValueAt(ndiscountrate.multiply(nitemdiscount).div(new UFDouble(100)), row,"ndiscountrate");
        nc.ui.scm.pub.panel.RelationsCal.calculate(row, getBillCardPanel(), 
            getBillCardPanel().getCalculatePara("noriginalcurtaxnetprice"), "noriginalcurtaxnetprice", 
            SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeys(),
            "nc.vo.so.so002.SaleinvoiceBVO", "nc.vo.so.so002.SaleVO"); 
       //恢复整单折扣值
       getBillCardPanel().setBodyValueAt(ndiscountrate, row,"ndiscountrate");
       //重新设置冲减前金额
       getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "noriginalcursummny"), row, "nsubsummny");
       getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(row, "nsummny"), row, "nsubcursummny");
		  
	 //如果没有取到存货核算回写销售出库单的本币无税单价  
	  }else{
      aryrowno.add(body[row].getCrowno());
      errindex.add(row); 
	  }	
	}
   }
  //存在没有取到的行
  if(aryrowno.size() >0){
	  StringBuilder errstr = new StringBuilder();
	  for(int i=0;i<aryrowno.size();i++){
		  errstr.append(">").append(aryrowno.get(i)).append("< 没有取到成本价 \n");
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
   * 方法功能描述：销售发票支持编辑状态追加，完成“参照增行”功能。
   * <b>参数说明</b>
   * @param bo 本流程可参照增加的单据类型
   * @author fengjb
   * @time 2008-7-23 上午11:08:01
   */
  private void onRefAddLine(ButtonObject bo) {
	    // 上游单据类型  
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
	    
		//发票合并
		SaleinvoiceVO[] aryRetVO = null;
		aryRetVO = st.mergeSourceVOs(vosRefAddLine);
   
	   //得到符合要求的需要追加的当前卡片界面上的表体VO数组
	   SaleinvoiceBVO[] addToCardBody = checkBeforeRefAdd(aryRetVO);
     if (SaleBillType.SaleOutStore.equals(sUpSrcType)) {
       //把当前卡片界面上的以前参照增行过来的数据过滤出来加入到尾差处理过程中
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
       /** 处理多次开票尾差 */
     
        dealMny(new SaleinvoiceVO[]{newinvoiceVO});
      }
	   if(addToCardBody.length > 0){
       //设置冲减金额
       for(SaleinvoiceBVO bvo:addToCardBody){
         bvo.setAttributeValue("nsubsummny", bvo.getAttributeValue("noriginalcursummny"));
         bvo.setAttributeValue("nsubcursummny", bvo.getAttributeValue("nsummny"));
       }
         
		   //获取原先卡片界面上
		   int oldrowcount =getBillCardPanel().getRowCount(); 
		   int[] addlinerow = new int[addToCardBody.length];
		   for(int i =0;i<addToCardBody.length;i++){
			   addlinerow[i] = oldrowcount + i ;
         getBillCardPanel().getBodyPanel().addLine();
		   }
		   getBillCardPanel().getBillModel().setBodyRowVOs(addToCardBody, addlinerow);
		   BillRowNo.addLineRowNos(getBillCardPanel(), ScmConst.SO_Invoice, "crowno", oldrowcount-1, addlinerow);
       //参照增行增加行的辅计量编辑性和换算率
       boolean isshow = getBillCardPanel().getBodyItem("cinvclassid").isShow();
       for(Integer rowindex:addlinerow){
         //新增行状态
         getBillCardPanel().getBillModel().setRowState(rowindex,BillModel.ADD);
         //新增行执行加载公式
         getBillCardPanel().getBillModel().execLoadFormulaByRow(rowindex);
         //如果存货分类字段显示且为空
         if(isshow && null == getBillCardPanel().getBodyValueAt(rowindex, "cinvclassid")){
         String[] invclformul = new String[]{
             "cinvclassid->getColValue(bd_invbasdoc,pk_invcl,pk_invbasdoc,cinvbasdocid)"
         };
         getBillCardPanel().execBodyFormulas(rowindex,invclformul);
         }
         //辅计量存货编辑性
         getBillCardPanel().setAssistChange(rowindex);
         if (null == getBillCardPanel().getBodyValueAt(rowindex, "cpackunitid")
             || "".equals(getBillCardPanel().getBodyValueAt(rowindex, "cpackunitid")))
           continue;
         getBillCardPanel().initScalefactor(rowindex);
         // 计算辅计量单价
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
	       * @res "编辑单据..."
	       */
	      );
	  }

private SaleinvoiceBVO[] checkBeforeRefAdd(SaleinvoiceVO[] addvo) {
	if(addvo ==null || addvo.length ==0 ){
	   return null;
	}
  //获取表头的发票折扣值
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
     * @res "客户"
     */;
    
	}else if("csalecorpid".equals(key)){
		keyname = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
    "UC000-0004128")
    /*
     * @res "销售组织"
     */;
	}else if("ccalbodyid".equals(key)){
    keyname = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
    "UC000-0001825")
    /*
     * @res "库存组织"
     */;
	}else if("ccurrencyid".equals(key)){
    keyname = nc.ui.ml.NCLangRes.getInstance().getStrByID("common",
      "UC000-0001755")
      /*
       * @res "币种"
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
   * 得到单据VO。 创建日期：(2001-6-23 9:47:36)
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
          "UPPSCMCommon-000256")/* @res "数据加载失败！" */);
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }

  }

  /**
   * 全选
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 下午03:49:18
   */
  private void onSelectAll() {
    if (!getVOCache().isEmpty()) {
      getBillListPanel().getHeadTable().setRowSelectionInterval(0,
          getBillListPanel().getHeadTable().getRowCount() - 1);

      setButtonsStateBrowse();
    }

  }

  /**
   * 全消
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 下午03:49:33
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
   * 刷新
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-23 下午03:49:33
   */
  private void onRefresh() {
    // yt add 2003-12-02
    // 将查询结果加入缓存
    SaleVO[] vosInvoice = null;
    try {
      vosInvoice = SaleinvoiceBO_Client.queryHeadAllData(getQueryDlg()
          .getWhere(),getClientEnvironment().getUser().getPrimaryKey(),getQueryDlg().getIsSelAuditing());
    }
    catch (Exception e) {
      SCMEnv.out(e);
      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
          .getStrByID("scmcommon", "UPPSCMCommon-000059")/* @res "错误" */, e
          .getMessage());
    }

    // 设置缓存
    getVOCache().setCacheDataByHeadVOs(vosInvoice);

    // 切换到列表
    onList();
  }

  /**
   * 定位。 创建日期：(2001-12-4 10:56:17)
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
   *          要设置的 m_iShowState
   */
  private void setShowState(int showState) {
    m_iShowState = showState;
  }

  /**
   * 放弃转单按钮
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-7 下午04:40:35
   */
  private void onCancelTransfer() {

    // ????? 因此处类似于删除，因此应加入询问

    // 列表下放弃选中的单据中的未保存者
    // 卡片下放弃该张单据
    if (getShowState() == ListShow) {
      int[] iaSerialNum = getBillListPanel().getHeadTable().getSelectedRows();
      for (int i = iaSerialNum.length - 1; i >= 0; i--) {
        // 自后向前从缓存中删除 AND 从界面删除
        if (getVOCache().getVO_Load(iaSerialNum[i]).getHeadVO().isNew()) {
          getVOCache().removeVOAt(iaSerialNum[i]);
        }
      }
      getBillListPanel().getHeadBillModel().delLine(iaSerialNum);
      getBillListPanel().updateUI();
    }
    else {
      // 除去当前单据，并自动滚动到下一张
      getVOCache().removeVOAt(getVOCache().getPos());
      getBillCardPanel().loadCardData(
          getVOCache().getVO_Load(getVOCache().rollToNextPos()));
    }

    setButtonsStateBrowse();

  }

  /**
   * 父类方法重写
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
   * 接口方法，按上次查询条件刷新缓存 LIST做了某些操作后，需要重新按条件查询，此时调用此接口方法。 使用时机：如自动合并开票后
   * 
   * @see nc.ui.so.so002.IInvoiceListPanel#updateCache()
   */
  public void updateCache() {
    getVOCache().setCacheData(null);

    SaleVO[] sales = null;
    try {
      /**修改 BY fengjb 20080826 V55 支持”待审批“查询条件**/
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
    //参照增行
    getBtns().m_boRefAdd.setEnabled(false);
    //取成本价
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
   * 设置编辑状态的按钮
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-8-7 上午09:34:04
   */
  private void setButtonsStateEdit() {
    // 业务类型
    getBtns().m_boBusiType.setEnabled(false);

    // 新增
    getBtns().m_boAdd.setEnabled(false);

    // 保存
    getBtns().m_boSave.setEnabled(true);

    // 维护
    // 修改，取消（原名放弃），删除（原名作废），放弃转单，合并开票，放弃合并
    getBtns().m_boMaintain.setEnabled(true);
    getBtns().m_boModify.setEnabled(false);
    getBtns().m_boBlankOut.setEnabled(false);
    getBtns().m_boCancelTransfer.setEnabled(false);
    getBtns().m_boCancel.setEnabled(true);
    
    getBtns().m_boUnite.setEnabled(false);
    getBtns().m_boUniteCancel.setEnabled(false);
    if(getBillCardPanel().getVO() != null){
	    // 对冲生成不可用
	    if (getBillCardPanel().getVO().getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN) {
	      getBtns().m_boUnite.setEnabled(false);
	      getBtns().m_boUniteCancel.setEnabled(false);
	    }
	    else {
	      // 只有发票总金额>0 才可进行冲减。
	      getBtns().m_boUnite.setEnabled(getBillCardPanel().getVO().getHeadVO()
	          .isLgtZero());
	      getBtns().m_boUniteCancel.setEnabled(getBillCardPanel().getVO()
	          .getHeadVO().isLgtZero()
	          && getBillCardPanel().getVO().getHeadVO().isStrike());
	    }
    }

    // 行操作
	// 增行，删行，插入行（增加），复制行，粘贴行

		// 对冲发票禁止行操作 保证数据来源于蓝字发票 lining zhongwei
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
		// 保持原有逻辑
		else {
			getBtns().m_boLineOper.setEnabled(true);
			boolean isStrike = getBillCardPanel().isStrike();
			getBtns().m_boAddLine.setEnabled(!isStrike);
			getBtns().m_boDelLine.setEnabled(!isStrike);
			getBtns().m_boCopyLine.setEnabled(!isStrike);
			getBtns().m_boPasteLine.setEnabled(!isStrike);
		}
    
    // add by fengjb 2008-09-11 V55行行操作下新增 卡片编辑、重排行号按钮
    getBtns().m_boCardEdit.setEnabled(true);
    getBtns().m_boReRowNO.setEnabled(true);
    
    //参照增行
    if (getBillCardPanel().getVO().getHeadVO().getFcounteractflag() == SaleVO.FCOUNTERACTFLAG_COUNTERACT_GEN) {
      getBtns().m_boRefAdd.setEnabled(false);
    }else{
    getBtns().m_boRefAdd.setEnabled(true);
    }
    //取成本价
    getBtns().m_boFetchCost.setEnabled(! getBillCardPanel().getVO().getHeadVO().isStrike());
    
    
    // 审批
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
    // 执行
    getBtns().m_boAction.setEnabled(bCanSendAudit);
    // 送审，弃审
    getBtns().m_boSendAudit.setEnabled(bCanSendAudit);
    
    getBtns().m_boUnApprove.setEnabled(false);

    // 查询
    getBtns().m_boQuery.setEnabled(false);

    // 浏览
    // 刷新（增加），定位，首页，上页（原名上一页），下页（原名下一页），末页，全选，全消
    getBtns().m_boBrowse.setEnabled(false);
    getBtns().m_boRefresh.setEnabled(false);
    getBtns().m_boLocal.setEnabled(false);
    // 首末上下页
    getBtns().m_boFirst.setEnabled(false);
    getBtns().m_boNext.setEnabled(false);
    getBtns().m_boPrev.setEnabled(false);
    getBtns().m_boLast.setEnabled(false);
    getBtns().m_boSelectAll.setEnabled(false);
    getBtns().m_boUnSelectAll.setEnabled(false);

    // 切换
    getBtns().m_boCard.setEnabled(false);

    // 打印管理
    // 预览，打印，合并显示
    getBtns().m_boPrintManage.setEnabled(true);
    getBtns().m_boPreview.setEnabled(false);
    getBtns().m_boPrint.setEnabled(false);
    getBtns().m_boBillCombin.setEnabled(true);

    // 辅助功能
    // 生成对冲发票，传金税，文档管理
    getBtns().m_boAssistFunction.setEnabled(true);
    getBtns().m_boOpposeAct.setEnabled(true);
    getBtns().m_boSoTax.setEnabled(true);
    getBtns().m_boDocument.setEnabled(true);

    // 辅助查询
    // 联查，存量显示/隐藏（取代原可用量按钮，功能和订单做一致），审批流状态，
    // 客户信息，发票执行情况，客户信用，毛利预估
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
   * 父类方法重写
   * 
   * @see nc.ui.so.pub.IBatchWorker#informWhenInterrupt(java.lang.String)
   */
  public void informWhenInterrupt(String sActionName) {
    if (ISaleInvoiceAction.Approve.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000041")/*
                                                                       * @res
                                                                       * "审批操作被用户中断！"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnApprove.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000042")/*
                                                                       * @res
                                                                       * "弃审操作被用户中断！"
                                                                       */);
    }
    else if (ISaleInvoiceAction.Unite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100041")/*
                                                                       * @res
                                                                       * "弃审操作被用户中断！"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnUnite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100042")/*
                                                                       * @res
                                                                       * "弃审操作被用户中断！"
                                                                       */);
    }

  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.so.pub.IBatchWorker#informWhenSucceed(java.lang.String)
   */
  public void informWhenSucceed(String sActionName) {
    if (ISaleInvoiceAction.Approve.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000043")/*
                                                                       * @res
                                                                       * "审批操作结束！"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnApprove.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-000044")/*
                                                                       * @res
                                                                       * "弃审操作结束！"
                                                                       */);
    }
    else if (ISaleInvoiceAction.Unite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100043")/*
                                                                       * @res
                                                                       * "弃审操作结束！"
                                                                       */);
    }
    else if (ISaleInvoiceAction.UnUnite.equals(sActionName)) {
      ShowToolsInThread.showMessage(m_proccdlg, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("40060501", "UPP40060501-100044")/*
                                                                       * @res
                                                                       * "弃审操作结束！"
                                                                       */);
    }

  }

  /**
   * 父类方法重写
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

	    // 将查询结果加入缓存
	    SaleVO[] vosInvoice = null;
	    try {
	      vosInvoice = SaleinvoiceBO_Client.queryHeadAllData(sql);
	    }
	    catch (Exception e) {
	      SCMEnv.out(e);
	      MessageDialog.showErrorDlg(this, nc.ui.ml.NCLangRes.getInstance()
	          .getStrByID("scmcommon", "UPPSCMCommon-000059")/* @res "错误" */, e
	          .getMessage());
	    }

	    // 设置缓存
	    getVOCache().setCacheDataByHeadVOs(vosInvoice);

	    // 切换到列表
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
		// 销售订单
		if (SaleBillType.SaleOrder.equals(addData.getSourceBillType())) {
			// 查询订单
			SaleOrderVO order = (SaleOrderVO) SaleOrderBO_Client
					.queryData(addData.getSourceBillID());
			// VO对照
			vosNew = new SaleinvoiceVO[] { (SaleinvoiceVO) PfUtilUITools
					.runChangeData(SaleBillType.SaleOrder,
							SaleBillType.SaleInvoice, order) };						
		}// end if saleorder

	    if (vosNew == null || vosNew.length == 0)
	        return;
	    
	    SaleinvoiceVO[] aryRetVO = st.mergeSourceVOs(vosNew);

	    // 设置缓存
	    getVOCache().setCacheData(aryRetVO);

	    //数据一致性检测,合并VO
	    getVOCache().setSaleinvoiceVO(
		        getVOCache().getPos(),
		        SaleinvoiceVO.mergeCheck(vosNew, st.SO_30
		            .booleanValue(), getBtns().getBusiType()));

	    // ------2-------------to card
		add(getBillCardPanel(), "Center");
		// TODO 此处重复，但否则会抛错，可再优化
		getBillCardPanel().loadCardData(getVOCache().getCurrentVO());
		getBillCardPanel().modify(getVOCache().getCurrentVO());
		
		// --------3------------------------
		setShowState(CardShow);
		setOperationState(ISaleInvoiceOperState.STATE_EDIT);
		setButtonsStateEdit();
		showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("SCMCommon",
		     "UPPSCMCommon-000350") /* * @res "编辑单据..."*/);

	} catch (Exception e) {
	    MessageDialog.showErrorDlg(this, "", e.getMessage());
	    return;
	}
	  
}

///**
// * 方法功能描述：保存新增前的上游单据ID\数量\金额
// * <p>
// * <b>examples:</b>
// * <p>
// * 使用示例
// * <p>[0]数量,[1]价税合计,[2]无税金额
// * <b>参数说明</b>
// * <p>
// * @author wsy
// * @time 2007-9-17 下午01:38:40
// */
//private void saveSourceMny(SaleinvoiceVO saleinvoiceNew[]){
//	hsSourceMny.clear();
//	UFDouble[] udNumMny=null; 
//	for(int i=0,iLen=saleinvoiceNew.length;i<iLen;i++){
//		for(int j=0,jLen=saleinvoiceNew[i].getItemVOs().length;j<jLen;j++){
//			if(saleinvoiceNew[i].getItemVOs()[j].getCupsourcebillbodyid()==null)
//        continue;
//			udNumMny= new UFDouble[7];
//      //报价计量单位数量
//			udNumMny[0]=saleinvoiceNew[i].getItemVOs()[j].getNquotenumber();
//			//原币				
//			udNumMny[1]=saleinvoiceNew[i].getItemVOs()[j].getNoriginalcursummny();
//			udNumMny[2]=saleinvoiceNew[i].getItemVOs()[j].getNoriginalcurmny();
//			//本币
//			udNumMny[3]=saleinvoiceNew[i].getItemVOs()[j].getNsummny();
//			udNumMny[4]=saleinvoiceNew[i].getItemVOs()[j].getNmny();
//			//冲减前金额
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
 * 父类方法重写
 * 卡片编辑下增行操作实现
 * @see nc.ui.pub.bill.BillActionListener#onEditAction(int)
 */
public boolean onEditAction(int action) {
  //判断是否是增行操作
  if (BillScrollPane.ADDLINE == action && !iAddButn) {
    getBillCardPanel().actionAddLine();
  return false;
  }
  return true;
 }
}