package nc.ui.ic.ic218;

import java.util.ArrayList;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.ic.pub.bill.GeneralBillClientUI;
import nc.ui.ic.pub.bill.GeneralBillUICtl;
import nc.ui.ic.pub.bill.IButtonManager;
import nc.ui.ic.pub.bill.ICButtonConst;
import nc.ui.ic.pub.bill.OffLineCtrl;
import nc.ui.ic.pub.bill.QueryDlgHelp;
import nc.ui.ic.pub.query.QueryDlgUtil;
import nc.ui.ic.pub.query.SCMDefaultFilter;
import nc.ui.ic.pub.tools.GenMethod;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.FramePanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.change.PfChangeBO_Client;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.scm.pub.FactoryLoginDialog;
import nc.ui.scm.pub.redunmulti.ISourceRedunUI;
import nc.ui.scm.pub.redunmulti.PubRedunMultiDlg;
import nc.ui.scm.pub.redunmulti.PubTransBillPaneVO;
import nc.ui.to.outer.SettlePathDlgForIC;
import nc.ui.to.service.ITOToIC_QryDLg;
import nc.vo.ic.ic700.IntList;
import nc.vo.ic.pub.ICConst;
import nc.vo.ic.pub.SmartVOUtilExt;
import nc.vo.ic.pub.bill.GeneralBillItemVO;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.bill.IItemKey;
import nc.vo.ic.pub.bill.QryInfoConst;
import nc.vo.ic.pub.check.CheckTools;
import nc.vo.ic.pub.check.VOCheck;
import nc.vo.ic.pub.exp.ICBusinessException;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ic.BillMode;
import nc.vo.scm.constant.ic.CONST;
import nc.vo.scm.constant.ic.InOutFlag;
import nc.vo.scm.ic.exp.ATPNotEnoughException;
import nc.vo.scm.pub.FactoryVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.pub.session.ClientLink;
import nc.vo.scm.pub.smart.SmartFieldMeta;
import nc.vo.so.so120.CreditNotEnoughException;
import nc.vo.so.so120.PeriodNotEnoughException;

/**
 * 此处插入类型说明。
 * 创建日期：(2004-2-19 15:34:51)
 * @author：郑树森
 * 
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
  //
  //	//参照三方调拨订单
  //	ButtonObject m_boAdd5C;
  //	//参照公司间调拨订单
  //	ButtonObject m_boAdd5D;
  //	//参照组织间调拨订单
  //	ButtonObject m_boAdd5E;
  //	ButtonObject m_boAdd5I;
  //
  //	//按钮
  //	ButtonObject m_boAddSelf;
  private FactoryLoginDialog m_DlgFactory;

  ////参照发货单
  //ButtonObject m_boAdd7D;
  ////参照发运单
  //ButtonObject m_boAdd7F;

  //单据调拨订单参照对话框

  //登陆的库存组织
  private java.lang.String m_sCalbodyid;

  private String m_sErr = null;

  //	//修改调出公司、组织
  //	ButtonObject m_boModifySettlePath;
  ModifyCorpDlg m_dlgModifyCorp;

  SettlePathDlgForIC m_dlgModifySettlePath;

	private IButtonManager m_buttonManager;

  /**
   * ClientUI 构造子注解。
   */
  public ClientUI() {
    super();
    initialize();

  }
  
  /**
   * ClientUI 构造子注解。
   * add by liuzy 2007-12-18 根据节点编码初始化单据模版
   */
  public ClientUI(FramePanel fp) {
   super(fp);
   initialize();
  }

  /**
   * ClientUI 构造子注解。
   * @param pk_corp java.lang.String
   * @param billType java.lang.String
   * @param businessType java.lang.String
   * @param operator java.lang.String
   * @param billID java.lang.String
   */
  public ClientUI(
      String pk_corp, String billType, String businessType, String operator,
      String billID) {
    super(pk_corp, billType, businessType, operator, billID);
  }
  
  @Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		
		if("noutnum".equals(e.getKey())) 
			afterSetNoutnum(e);
		
	}
  
  @Override
	public boolean beforeEdit(BillEditEvent e) {
	  
	  
	  	if("cprojectname".equals(e.getKey()) && ((BillItem)e.getSource()).getComponent() instanceof UIRefPane) {
	  		
	  		((UIRefPane)((BillItem)e.getSource()).getComponent()).setWhereString(" bd_jobbasfil.pk_jobtype = (select pk_jobtype from bd_jobtype where jobtypename like '船名%' and nvl(dr,0)=0)  ");
	  		
	  	}
	  
		return super.beforeEdit(e);
	}
  
  /**
   * river 
   * 
   * 件数计算
   * 
   * @param e
   */
  protected final void afterSetNoutnum(BillEditEvent e) {
	  try {
		  
			Object cinvbasid = getBillCardPanel().getBodyValueAt(e.getRow(), "cinvbasid");
			Object unitweight = UAPQueryBS.getInstance().executeQuery("select nvl(unitweight , 0) from bd_invbasdoc where pk_invbasdoc = '"+cinvbasid+"'", new ColumnProcessor());
			UFDouble weight = new UFDouble(unitweight.toString());
			if(weight.doubleValue() == 0 )
				return ;
			
			UFDouble noutnum = new UFDouble(e.getValue() == null ? "0" : e.getValue().toString() , 2);
			
			UFDouble divValue = noutnum.div(weight);
			getBillCardPanel().setBodyValueAt( CalcFunc.calcNumOf(divValue) , e.getRow(), "ncountnum");
			
		} catch(Exception ex) {
			Logger.error(ex.getMessage(), ex, this.getClass(), "afterEdit");
		}
  }

  /**
   * 创建者：王乃军
   * 功能：单据编辑后处理
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   */
  protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
    if (e.getKey().equals("cotherwhid")) {
      //调拨仓库
      String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
          .getHeadItem("cotherwhid").getComponent()).getRefName();
      //保存名称以在列表形式下显示。
      if (getM_voBill() != null)
        getM_voBill().setHeaderValue("cotherwhname", sName);
    }
  }

  /**
   * 创建者：王乃军
   * 功能：表体行列选择改变
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
  protected void afterBillItemSelChg(int iRow, int iCol) {
  }

  /**
   * 此处插入方法说明。
   * 创建日期：(2004-5-10 17:33:36)
   * @param voRetvos nc.vo.pub.AggregatedValueObject[]
   */
  private void afterVoLoaded() {
    //控制界面 
    String[] keys = new String[] {
        "noutnum", "noutassistnum", "nshouldoutassistnum", "nshouldoutnum",
        "nmny"
    };

    //如果退货，只能输负数，否则正数
    if (getM_voBill() != null && getM_voBill().getHeaderVO() != null
        && ((getM_voBill().getHeaderVO().getBoutretflag()!=null && getM_voBill().getHeaderVO().getBoutretflag().booleanValue()) 
            || (getM_voBill().getHeaderVO().getFreplenishflag()!=null && getM_voBill().getHeaderVO().getFreplenishflag().booleanValue()))) {
      nc.ui.ic.pub.bill.GeneralBillUICtl.setValueRange(getBillCardPanel(),
          keys, nc.vo.scm.pub.bill.SCMDoubleScale.MINVALUE, 0);
    }
    else {
      nc.ui.ic.pub.bill.GeneralBillUICtl.setValueRange(getBillCardPanel(),
          keys, 0, nc.vo.scm.pub.bill.SCMDoubleScale.MAXVALUE);
    }

    //调拨类型不可修改
    if (getBillCardPanel().getHeadItem("alloctypename") != null)
      getBillCardPanel().getHeadItem("alloctypename").setEnabled(false);
    //调拨退回不可修改
    if (getBillCardPanel().getHeadItem("freplenishflag") != null)
      getBillCardPanel().getHeadItem("freplenishflag").setEnabled(false);

    calcSpace(getM_voBill());
  }

  /**
   * 创建者：王乃军
   * 功能：单据表体编辑事件前触发处理
   * 参数：e单据编辑事件
   * 返回：
   * 例外：
   * 日期：(2001-5-8 19:08:05)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
  public boolean beforeBillItemEdit(nc.ui.pub.bill.BillEditEvent e) {
    if (e.getKey() == IItemKey.CALBODY) {
      getBillCardPanel().getHeadItem(IItemKey.CALBODY).setEnabled(false);
    }
    return false;
  }

  /**
   * 创建者：王乃军
   * 功能：表体行列选择改变
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
  protected void beforeBillItemSelChg(int iRow, int iCol) {

  }

  /**
   *     检查打开该节点的前提条件。用于处理“只有满足某一条件时，才能打开该节点”
   * 的情况。
   *     需要判断“只有满足某一条件时，才能打开该节点”的节点，需要实现本方法。
   * 在方法内进行条件判断。
   *     基类根据返回值进行相应处理，如果返回值为一个非空字符串，那么基类不打开
   * 该节点，只在一个对话框中显示返回的字符串；如果返回值为null，那么基类象对待
   * 其他节点一样打开该节点。
   *
   * 创建日期：(2002-3-11 10:39:16)
   * @return java.lang.String
   */
  public String checkPrerequisite() {
    return m_sErr;
  }

  /**
   * 创建者：王乃军
   * 功能：抽象方法：保存前的VO检查
   * 参数：待保存单据
   * 返回：
   * 例外：
   * 日期：(2001-5-24 下午 5:17)
   * 修改日期，修改人，修改原因，注释标志：
   */
  protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
    //	String sSourceBillType = getSourBillTypeCode();
    try {
      VOCheck.checkOutRetVO(voBill);
      boolean bCheck = true;
      bCheck = super.checkVO();
      //调拨退回的数量须为负数

      return bCheck;

    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      return false;
    }
  }

  /**
   * ******************************************
   *  功能：拉调拨订单生单时查询对话框的创建
   *　修改：邵兵　2005-05-12
   *　
   *  ******************************************
   * @return ITOToIC_QryDLg
   */
  private ITOToIC_QryDLg createDlgQuery() {
    ITOToIC_QryDLg dlgQry = null;

    try {
      //        dlgQry = (ITOToIC_QryDLg)InterServUI.getInterInstance(ProductCode.PROD_TO, InterRegister.TO0005, new Object[]{this});
      dlgQry = (ITOToIC_QryDLg) Class.forName("nc.ui.to.outer.QRYOrderDlg")
          .newInstance();//NewObjectService.newInstance("TO","nc.ui.to.outer.QRYOrderDlg");
      //(ITOToIC_QryDLg)NCLocator.getInstance().lookup(ITOToIC_QryDLg.class.getName());
    }
    catch (Exception e) {
      // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000293")/*@res "该功能不可用（原因：内部交易管理系统没有安装）。"*/);
      //此处没有抛出异常，在onAddToOrder()中会有判断。
      //邵兵　2005-05-12
    }
    return dlgQry;
  }

  /**
   * 过滤单据参照
   * 创建者：张欣
   * 功能：初始化参照过滤
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-7-17 10:33:20)
   * 修改日期，修改人，修改原因，注释标志：
   *
   */
  public void filterRef(String sCorpID) {

    try {

      super.filterRef(sCorpID);
      //清空了库存组织
      //如果当前的仓库不属于
      //过滤仓库参照
      String sConstraint[] = null;
      if (getCalbodyid() != null) {
        sConstraint = new String[1];
        sConstraint[0] = " AND pk_calbody='" + getCalbodyid() + "'";
      }
      nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
          IItemKey.WAREHOUSE);
      //修改人：刘家清 修改日期：2007-9-26上午11:32:12 修改原因：只能用sCorpID，不能用getEnvironment().getCorpID()
      //nc.ui.ic.pub.bill.initref.RefFilter.filtWh(bi, getEnvironment().getCorpID(), sConstraint);
      nc.ui.ic.pub.bill.initref.RefFilter.filtWh(bi, sCorpID, sConstraint);

    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.error(e);
    }
  }

  /**
   * ******************************************
   *  功能：<|>
   *
   *  创建日期：(2004-3-9 11:09:31)
   *  @param：
   *
   *  @return：
   *  ******************************************
   * @return java.lang.String
   */
  public java.lang.String getCalbodyid() {
    if (m_sCalbodyid == null && isStartFromTO()) {
      FactoryVO vo = (FactoryVO) ClientEnvironment.getInstance().getValue(
          FactoryVO.TO_CALBODY + "," + getEnvironment().getCorpID() + "," + getEnvironment().getUserID());
      if (vo != null)
        m_sCalbodyid = vo.getPrimaryKey();
    }

    return m_sCalbodyid;
  }

  /**
   * 创建者：王乃军
   * 功能：得到查询对话框
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   **/
//  protected QueryConditionDlgForBill getConditionDlg() {
//    if (ivjQueryConditionDlg == null) {
//      ivjQueryConditionDlg = super.getConditionDlg();
//      ivjQueryConditionDlg.setCombox("body.cfirsttype", new String[][] {
//          {
//              "", ""
//          },
//          {
//              BillTypeConst.TO_ORDER3,
//              nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
//                  "UPT40080618-000006")
//          /*@res "三方调拨订单"*/},
//          {
//              BillTypeConst.TO_ORDER2,
//              nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
//                  "UPT40080618-000007")
//          /*@res "公司间调拨订单"*/},
//          {
//              BillTypeConst.TO_ORDER1,
//              nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
//                  "UPT40080618-000008")
//          /*@res "组织间调拨订单"*/},
//          {
//              BillTypeConst.TO_ORDER4,
//              nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
//                  "UPT40080618-000009")
//          /*@res "组织内调拨订单"*/}
//      });
//      ivjQueryConditionDlg.setRefInitWhereClause("head.coutcalbodyid", "库存组织", /*-=notranslate=-*/
//      "pk_corp=", "head.coutcorpid");
//      //ivjQueryConditionDlg.setRefInitWhereClause(
//      //"head.pk_calbody",
//      //"库存组织",
//      //"pk_corp=",
//      //"head.pk_corp");
//      ivjQueryConditionDlg.setRefInitWhereClause("head.cothercalbodyid",
//          "库存组织", /*-=notranslate=-*/
//          "pk_corp=", "head.cothercorpid");
//
//      ivjQueryConditionDlg.setRefInitWhereClause("head.cotherwhid", "仓库档案", /*-=notranslate=-*/
//      "bd_stordoc.pk_calbody=", "head.cothercalbodyid");
//      //ivjQueryConditionDlg.setCorpRefs("head.coutcorpid",new String[]{"head.coutcalbodyid"});
//      ivjQueryConditionDlg.setCorpRefs("head.pk_corp", GenMethod
//          .getDataPowerFieldFromDlg(ivjQueryConditionDlg, false, new String[] {
//              "head.cothercorpid", "head.coutcorpid", "body.creceieveid",
//              "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid"
//          }));
//      ivjQueryConditionDlg.setCorpRefs("head.cothercorpid", new String[] {
//          "head.cothercalbodyid", "head.cotherwhid", "body.creceieveid"
//      });
//      ivjQueryConditionDlg.setCorpRefs("head.coutcorpid", new String[] {
//        "head.coutcalbodyid"
//      });
//
//      String[] otherfieldcodes = new String[] {
//          "body.creceieveid", "head.cothercalbodyid", "head.cotherwhid",
//          "head.coutcalbodyid"
//      };
//      GenMethod.setDataPowerFlag(ivjQueryConditionDlg, false, otherfieldcodes);
//      UIRefPane ref = null;
//      for (int i = 0; i < otherfieldcodes.length; i++) {
//        ref = ivjQueryConditionDlg.getRefPaneByNodeCode(otherfieldcodes[i]);
//        if (ref != null && ref.getRefModel() != null)
//          ref.getRefModel().setUseDataPower(false);
//      }
//    }
//    return ivjQueryConditionDlg;
//  }
//
//  /**
//   * 此处插入方法说明。
//   用于子类对Condition的个性化修改设置
//   的重载方法
//   * 创建日期：(2003-11-25 20:58:54)
//   */
//  protected void getConDlginitself(
//      nc.ui.ic.pub.bill.query.QueryConditionDlgForBill queryCondition) {
//    if (m_sCalbodyid != null) {
//      queryCondition.setRefInitWhereClause("head.pk_calbody", "库存组织",
//          "pk_calbody='" + m_sCalbodyid + "' and pk_corp=", "pk_corp"); /*-=notranslate=-*/
//    }
//
//  }
  
  
  public QueryDlgHelp getQryDlgHelp() {
    if(m_qryDlgHelp==null){
      m_qryDlgHelp = new QueryDlgHelp(this){
        protected void init() {
            super.init();
            getQueryDlg().setLogFields(new String[]{"head.coutcalbodyid","head.coutcorpid",
                "head.cothercorpid","head.cothercalbodyid","head.cotherwhid"});
            /*getQueryDlg().setCombox("body.cfirsttype", new String[][] {
                {
                    null, ""
                },
                {
                    BillTypeConst.TO_ORDER3,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
                        "UPT40080618-000006")
                @res "三方调拨订单"},
                {
                    BillTypeConst.TO_ORDER2,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
                        "UPT40080618-000007")
                @res "公司间调拨订单"},
                {
                    BillTypeConst.TO_ORDER1,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
                        "UPT40080618-000008")
                @res "组织间调拨订单"},
                {
                    BillTypeConst.TO_ORDER4,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
                        "UPT40080618-000009")
                @res "组织内调拨订单"}
            });*/
            getQueryDlg().setRefInitWhereClause("head.coutcalbodyid", "库存组织", /*-=notranslate=-*/
            "pk_corp=", "head.coutcorpid");
            //ivjQueryConditionDlg.setRefInitWhereClause(
            //"head.pk_calbody",
            //"库存组织",
            //"pk_corp=",
            //"head.pk_corp");
            getQueryDlg().setRefInitWhereClause("head.cothercalbodyid",
                "库存组织", /*-=notranslate=-*/
                "pk_corp=", "head.cothercorpid");
            
//            getQueryDlg().setRefInitWhereClause("head.cotherwhid", "仓库档案",
//                "pk_corp=", "head.cothercorpid");
  
            getQueryDlg().setRefInitWhereClause("head.cotherwhid", "仓库档案", /*-=notranslate=-*/
                "bd_stordoc.pk_calbody=", "head.cothercalbodyid");
            
            //ivjQueryConditionDlg.setCorpRefs("head.coutcorpid",new String[]{"head.coutcalbodyid"});
//            getQueryDlg().setCorpRefs("head.pk_corp", GenMethod
//                .getDataPowerFieldFromDlg(ivjQueryConditionDlg, false, new String[] {
//                    "head.cothercorpid", "head.coutcorpid", "body.creceieveid",
//                    "head.cothercalbodyid", "head.cotherwhid", "head.coutcalbodyid"
//                }));
            getQueryDlg().setCorpRefs("head.cothercorpid", new String[] {
                "head.cothercalbodyid", "head.cotherwhid", "body.creceieveid"
            });
            getQueryDlg().setCorpRefs("head.coutcorpid", new String[] {
              "head.coutcalbodyid"
            });
  
            String[] otherfieldcodes = new String[] {
                "body.creceieveid", "head.cothercalbodyid", "head.cotherwhid",
                "head.coutcalbodyid"
            };
            
//            GenMethod.setDataPowerFlag(ivjQueryConditionDlg, false, otherfieldcodes);
//            UIRefPane ref = null;
//            for (int i = 0; i < otherfieldcodes.length; i++) {
//              ref = ivjQueryConditionDlg.getRefPaneByNodeCode(otherfieldcodes[i]);
//              if (ref != null && ref.getRefModel() != null)
//                ref.getRefModel().setUseDataPower(false);
//            }
            
            if (m_sCalbodyid != null) {
              getQueryDlg().setRefInitWhereClause("head.pk_calbody", "库存组织",
                  "pk_calbody='" + m_sCalbodyid + "' and pk_corp=", "pk_corp"); /*-=notranslate=-*/
            }
            
          }
        
          @Override
          public String processWhere(SCMDefaultFilter filter, String basicWhere) { 
            super.processWhere(filter, basicWhere);
            FilterMeta meta = (FilterMeta)filter.getFilterMeta();
            String fieldcode = meta.getFieldCode();
            String swhere = basicWhere;
            String sField=null;
            
            
            if(fieldcode.startsWith("order.")){
              
              StringBuilder sb = new StringBuilder();
              sField=fieldcode.substring(fieldcode.indexOf(".")+1);
              meta.setFieldCode(sField);
              swhere = QueryDlgUtil.getSqlString(filter);
              meta.setFieldCode(fieldcode);
              sb.append(" ");
              sb.append(" exists ( ");
              sb.append(" select to_bill.cbillid from to_bill where body.cfirstbillhid=to_bill.cbillid and to_bill.dr=0 and ");
              sb.append(swhere);
              sb.append(" ) ");
              swhere = sb.toString();
            }else if(fieldcode.startsWith("orderbody.")){
              
              String coutcalbodywhere = GenMethod.fromIn("to_bill_b.coutcbid", getQueryDlg().getFieldValuePksAtLogPane("head.coutcalbodyid"));
                            
              StringBuilder sb = new StringBuilder();
              sField=fieldcode.substring(fieldcode.indexOf(".")+1);
              meta.setFieldCode(sField);
              swhere = QueryDlgUtil.getSqlString(filter);
              meta.setFieldCode(fieldcode);
              sb.append(" ");
              sb.append(" exists ( ");
              sb.append(" select to_bill_b.cbill_bid from to_bill_b where body.cfirstbillbid=to_bill_b.cbill_bid and to_bill_b.dr=0 and ");
              if(coutcalbodywhere!=null || coutcalbodywhere.trim().length()>0)
                sb.append(coutcalbodywhere+" and ");
              sb.append(swhere);
              sb.append(" ) ");
            }
            return swhere;
          }
          protected String getBillFixWhere() {
            if(m_sCalbodyid!=null)
              return super.getBillFixWhere()+" and pk_calbody ='"+m_sCalbodyid+"' ";
            else
              return super.getBillFixWhere();
          }
      };
    }
    return m_qryDlgHelp;
  }

  /**
   * ******************************************
   *  功能：<|>
   *
   *  创建日期：(2004-3-9 11:29:08)
   *  @param：
   *
   *  @return：
   *  ******************************************
   * @return nc.ui.scm.pub.FactoryLoginDialog
   */
  private FactoryLoginDialog getDlgFactory() {
    if (m_DlgFactory == null)
      m_DlgFactory = new nc.ui.scm.pub.FactoryLoginDialog(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
              "UPP4008busi-000294")/*@res "登陆库存组织"*/);
    return m_DlgFactory;
  }

  /**
   * 创建者：王乃军
   * 功能：得到用户输入的额外查询条件
   * 参数：//查询条件数组
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
//  public String getExtendQryCond(nc.vo.pub.query.ConditionVO[] voaCond) {
//
//    //转换为子查询
//    if (voaCond != null) {
//      String sFieldCode = null;
//      String sField = null;
//      for (int i = 0; i < voaCond.length; i++) {
//        if (voaCond[i] != null && voaCond[i].getFieldCode() != null) {
//
//          sFieldCode = voaCond[i].getFieldCode().trim();
//          //"dbilldate"
//          if (sFieldCode.startsWith("order.")) {
//            sField = sFieldCode.substring(sFieldCode.indexOf(".") + 1);
//            nc.vo.ic.pub.GenMethod.setCondIn(voaCond[i],
//                " select cbillid from to_bill where dr=0 ",
//                "body.cfirstbillhid", sField);
//
//          }
//          //dplanoutdate,dplanarrivedate
//          else if (sFieldCode.startsWith("orderbody.")) {
//            sField = sFieldCode.substring(sFieldCode.indexOf(".") + 1);
//            nc.vo.ic.pub.GenMethod.setCondIn(voaCond[i],
//                " select cbill_bid from to_bill_b where dr=0 ",
//                "body.cfirstbillbid", sField);
//
//          }
//          //else if(sFieldCode.startsWith("orderbody.dplanarrivedate")){
//          //nc.vo.ic.pub.GenMethod.setCondIn(voaCond[i],
//          //" select cbill_bid from to_bill_b where dr=0 ","body.cfirstbillbid","dplanarrivedate");
//
//          //}
//
//        }
//      }
//    }
//
//    if (m_sCalbodyid != null)
//      return super.getExtendQryCond(voaCond) + " and pk_calbody ='"
//          + m_sCalbodyid + "' ";
//    else
//      return super.getExtendQryCond(voaCond);
//  }

  /**
   * 此处插入方法说明。
   * 功能描述:
   * 作者：王乃军
   * 输入参数:
   * 返回值:
   * 异常处理:
   * 日期:(2003-6-25 20:43:17)
   * @return java.util.ArrayList
   */
  protected java.util.ArrayList getFormulaItemHeader() {
    java.util.ArrayList arylistItemField = super.getFormulaItemHeader();

    //原有的公式
    //库存组织 1

    arylistItemField.add(new String[] {
        "bodyname", "coutbodyname", "coutcalbodyid"
    });
    arylistItemField.add(new String[] {
        "bodyname", "cotherbodyname", "cothercalbodyid"
    });
    arylistItemField.add(new String[] {
        "unitname", "cotherunitname", "cothercorpid"
    });
    arylistItemField.add(new String[] {
        "unitname", "coutunitname", "coutcorpid"
    });

    return arylistItemField;
  }

  /**
   * 初始化类。
   */
  /* 警告：此方法将重新生成。 */
  public void initialize() {
	super.initialize();

  if (isStartFromTO() && getCalbodyid() == null) {
    //登陆库存组织
    getDlgFactory().showModal();
    if (getCalbodyid() == null) {
      //showErrorMessage("登陆库存组织失败！");
      m_sErr = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
          "UPP4008busi-000295")/*@res "登陆库存组织失败！"*/;
      return;
    }

  }
  //如果登陆库存组织，设置库存组织不可编辑
  if (getCalbodyid() != null
      && getBillCardPanel().getHeadItem(IItemKey.CALBODY) != null) {
    getBillCardPanel().getHeadItem(IItemKey.CALBODY).setEnabled(false);
    getBillCardPanel().getHeadItem(IItemKey.CALBODY).setEdit(false);
  }

  if (getBillCardPanel().getHeadItem("cotherwhid") != null) {
    nc.ui.pub.beans.UIRefPane ref = (nc.ui.pub.beans.UIRefPane) getBillCardPanel()
        .getHeadItem("cotherwhid").getComponent();
    if (ref != null)
      ref.getRefModel().setUseDataPower(false);

  }

  String[] hidekey = {
      "nleftnum", "nleftastnum"
  };
  
  GeneralBillUICtl.showItem(getBillCardPanel().getBodyPanel(), false, hidekey);
  GeneralBillUICtl.showItem(getBillListPanel().getChildListPanel(), false, hidekey);
  
  getBillCardPanel().getBodyPanel().getTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
  getBillListPanel().getChildListPanel().getTable().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

  }

  /**
   * 创建者：王乃军
   * 功能：初始化系统参数
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
  protected void initPanel() {
    //需要单据参照
    super.setNeedBillRef(false);
  }

  public String getBillType() {
  	return nc.vo.ic.pub.BillTypeConst.m_allocationOut;
  }

  public String getFunctionNode() {
  	return "40080820";
  }

  public int getInOutFlag() {
  	return InOutFlag.OUT;
  }

  /**
   * ******************************************
   *  功能：<|>
   *
   *  创建日期：(2004-3-9 12:17:55)
   *  @param：
   *
   *  @return：
   *  ******************************************
   * @return boolean
   */
  private boolean isStartFromTO() {
    return false;//m_bIsUseTo;//getModuleCode().startsWith("4009");
  }
  
  protected class ButtonManager218 extends nc.ui.ic.pub.bill.GeneralButtonManager {

	public ButtonManager218(GeneralBillClientUI clientUI) throws BusinessException {
		super(clientUI);
	}

	  /**
	   * 子类实现该方法，响应按钮事件。
	   * @version (00-6-1 10:32:59)
	   *
	   * @param bo ButtonObject
	   */
	  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
	    if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_ADD_MANUAL) || 
          bo == getButtonTree().getButton(ICButtonConst.BTN_ADD)){
	      return;//onAddSelf();
      }else if (bo == getButtonTree().getButton(
          ICButtonConst.BTN_BILL_ADD_TO_5X)) {//调拨订单
        //按钮事件处理
        bo.setTag("5X:0001AA100000000001ZC");
        nc.ui.ic.pub.pf.ICSourceRefBaseDlg.childButtonClicked(bo, getEnvironment().getCorpID(),
            getFunctionNode(), getEnvironment().getUserID(), getBillType(), getClientUI());
        if (nc.ui.ic.pub.pf.ICSourceRefBaseDlg.isCloseOK()) {

          nc.vo.pub.AggregatedValueObject[] vos = nc.ui.ic.pub.pf.ICSourceRefBaseDlg
              .getRetsVos();
          
          

          onAddToOrder(vos);

        }
      }
      else if (bo == getButtonTree().getButton(
          ICButtonConst.BTN_BILL_ADD_TO_4331)) { //发货单
        onAddToTran4331();
      }
	    else if (bo == getButtonTree().getButton(ICButtonConst.BTN_OUT_RETURN))
	      onOutReturn();
	    //else if(bo==m_boAdd7D)
	    //onAdd7D();
	    //else if(bo==m_boAdd7F)
	    //onAdd7F();
	    else if (bo == getButtonTree().getButton(
	        ICButtonConst.BTN_ASSIST_FUNC_SETTLE_PATH))
	      //onModifyOutCorp();
	      onModifySettlePath();
      else if (bo == getButtonTree().getButton(
          ICButtonConst.BTN_ASSIST_CANCEL_SETTLE_PATH))
        onCancelSettlePath();
	    else
	      super.onButtonClicked(bo);
	  }

	  public void onAudit() {
	    qryLocSN(getM_iLastSelListHeadRow(), QryInfoConst.LOC_SN);
	    super.onAudit();
	  }

	  /**
	   * 创建者：王乃军
	   * 功能：修改处理
	   * 参数：
	   * 返回：
	   * 例外：
	   * 日期：(2001-5-9 9:23:32)
	   * 修改日期，修改人，修改原因，注释标志：
	   *
	   *
	   *
	   *
	   */
	  public void onUpdate() {
	    super.onUpdate();
	    GeneralBillUICtl.setItemEdit(getBillCardPanel(), BillItem.HEAD, IItemKey.freplenishflag, false);
	    afterVoLoaded();

	  }

	  /**
	   * ******************************************
	   *  功能：<|>
	   *
	   *  创建日期：(2004-3-10 12:33:02)
	   *  @param：
	   *
	   *  @return：
	   *  ******************************************
	   */
	  private void onAddSelf() {
	    onAdd();
	  }

	  /**
	   * ******************************************
	   *  功能：<|>
	   *
	   *  创建日期：(2004-3-10 12:33:02)
	   *  @param：
	   *
	   *  @return：
	   *  ******************************************
	   */
	  private void onAddToOrder(AggregatedValueObject[] vos) {

	    if (vos == null) {
	      return;
	    }
	    try {
	    	
	        if (vos != null && vos.length > 0)
				// 检查单据是否来源于新的参照界面
				if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO()
						.getAttributeValue(ICConst.IsFromNewRef))) {
					// 按库存默认方式分单
					vos = nc.vo.ic.pub.GenMethod.splitGeneralBillVOs(
							(GeneralBillVO[]) vos,
							getBillType(), getBillListPanel()
									.getHeadBillModel()
									.getFormulaParse());
					// 将外来单据的单位转换为库存默认单位.
					nc.vo.ic.pub.GenMethod.convertICAssistNumAtUI(
							(GeneralBillVO[]) vos, GenMethod.getIntance());
				}
	      
	      GeneralBillUICtl.procOrdEndAfterRefAdd((GeneralBillItemVO[])SmartVOUtilExt.getBodyVOs(vos), getBillCardPanel(), getBillType());
	          
	      //v5 lj 支持多张单据参照生成
	      if (vos != null && vos.length == 1) {
	        setRefBillsFlag(false);
	        setBillRefResultVO(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), vos);
	      }
	      else {
	        setRefBillsFlag(true);//是参照生成多张
	        setBillRefMultiVOs(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), (GeneralBillVO[]) vos);
	      }
	      GeneralBillUICtl.setItemEdit(getBillCardPanel(), BillItem.HEAD, IItemKey.freplenishflag, false);
	      // end v5 

	    }
	    catch (Exception e) {
	      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	          "UPP4008busi-000297")/*@res "发生错误:"*/
	          + e.getMessage());
	    }
	  }
    
    /**
     * ******************************************
     *  功能：<|>
     *
     *  创建日期：(2004-3-10 12:33:02)
     *  @param：
     *
     *  @return：
     *  ******************************************
     */
    private void onAddToTran4331() {
     
      try {
        
        
        PubTransBillPaneVO[] sourecVO = new PubTransBillPaneVO[1];
        sourecVO[0] = new PubTransBillPaneVO("4331","发货单",
            (ISourceRedunUI)Class.forName("nc.ui.so.soreceive.redun.ReceiveTo4YRedunSourceCtrl").newInstance(),
            null,"SO",null,null);
        PubTransBillPaneVO[] curVO = new PubTransBillPaneVO[1];
        curVO[0] = new PubTransBillPaneVO("4Y","调拨出库单",
            null,null,"IC",null,null);
        
        PubRedunMultiDlg refdlg = new PubRedunMultiDlg(getClientUI(),sourecVO,curVO,true);
        if(refdlg.showModal()!=UIDialog.ID_OK)
          return;
        AggregatedValueObject[] sourcebillvos =  refdlg.getRetBillVos();
        if(sourcebillvos==null || sourcebillvos.length<=0)
          return ;
        
        AggregatedValueObject[] vos = null;
        try{
          //sourcebillvos = nc.vo.ic.pub.GenMethod.splitSourceVOs(sourcebillvos, "4331", getBillType());
          vos = PfChangeBO_Client.pfChangeBillToBillArray(sourcebillvos, "4331", getBillType());
          vos = nc.vo.ic.pub.GenMethod.splitTargetVOs(vos, "4331", getBillType());
          if (vos != null && vos.length > 0)
  			// 检查单据是否来源于新的参照界面
  			if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO()
  					.getAttributeValue(ICConst.IsFromNewRef))) {
  				// 按库存默认方式分单
  				vos = nc.vo.ic.pub.GenMethod.splitGeneralBillVOs(
  						(GeneralBillVO[]) vos,
  						getBillType(), getBillListPanel()
  								.getHeadBillModel()
  								.getFormulaParse());
  				// 将外来单据的单位转换为库存默认单位.
  				nc.vo.ic.pub.GenMethod.convertICAssistNumAtUI(
  						(GeneralBillVO[]) vos, GenMethod.getIntance());
  			}
        }catch(Exception e){
          getClientUI().showErrorMessage(e.getMessage());
          nc.vo.scm.pub.SCMEnv.out(e);
          return;
        }
        
        GeneralBillUICtl.procOrdEndAfterRefAdd((GeneralBillItemVO[])SmartVOUtilExt.getBodyVOs(vos), getBillCardPanel(), getBillType());
            
        //v5 lj 支持多张单据参照生成
        if (vos != null && vos.length == 1) {
          setRefBillsFlag(false);
          setBillRefResultVO(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), vos);
        }
        else {
          setRefBillsFlag(true);//是参照生成多张
          setBillRefMultiVOs(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), (GeneralBillVO[]) vos);
        }
        GeneralBillUICtl.setItemEdit(getBillCardPanel(), BillItem.HEAD, IItemKey.freplenishflag, false);
        // end v5 

      }
      catch (Exception e) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
            "UPP4008busi-000297")/*@res "发生错误:"*/
            + e.getMessage());
      }
    }

	  /**
	   * 方法功能描述：出库退回 v52add
	   * <p>
	   * <b>examples:</b>
	   * <p>
	   * 使用示例
	   * <p>
	   * <b>参数说明</b>
	   * <p>
	   * @author yangb
	   * @time 2007-7-5 下午01:25:44
	   */
	  private void onOutReturn() {

	    try {
	      
	      if (getM_iMode() != BillMode.Browse){
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000522")/* @res "未保存的单据,不能退回" */);
	        return;
	      }

	      GeneralBillVO vo = getCurVO();
	      if (vo == null) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "scmcommon", "UPPSCMCommon-000167")/* @res "没有选择单据" */);
	        return;
	      }

	      int[] selrows = null;
	      if (BillMode.List == getM_iCurPanel())
	        selrows = getBillListPanel().getChildListPanel().getTable().getSelectedRows();
	      else
	        selrows = getBillCardPanel().getBodyPanel().getTable().getSelectedRows();
	      
	      if (selrows == null || selrows.length <= 0) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000515")/* @res "没有选中的表体行。" */);
	        return;
	      }

	      GeneralBillUICtl.getNaccumoutbacknumFromBB3(vo);

	      vo = (GeneralBillVO) vo.clone();

	      GeneralBillItemVO[] itemvos = vo.getItemVOs();
	      GeneralBillItemVO[] selitemvos = new GeneralBillItemVO[selrows.length];
	      for (int i = 0; i < selrows.length; i++)
	        selitemvos[i] = itemvos[selrows[i]];
	      vo.setChildrenVO(selitemvos);

	      VOCheck.checkCanOutRet(vo);

	      GeneralBillUICtl.processBillVOFor(vo, GeneralBillUICtl.Action.RETURN,
	          m_bIsInitBill);
	      
//	    当前是列表形式时，首先切换到表单形式
	      if (BillMode.List == getM_iCurPanel()) 
	        onSwitch();

	      setM_voBill(vo);

	      //  设置当前的条码框的条码 韩卫 2004-04-05
	      if (getM_utfBarCode() != null)
	    	  getM_utfBarCode().setCurBillItem(null);

	      getBillCardPanel().getBillModel().setNeedCalculate(false);

	      //  新增
	      getBillCardPanel().addNew();
	      setBillVO(getM_voBill());
	      
	      GeneralBillUICtl.processUIWhenRetOut(getBillCardPanel());

	      for (int row = 0; row < getBillCardPanel().getBillModel().getRowCount(); row++) {
	        // 设置行状态为新增
	        getBillCardPanel().getBillModel().setRowState(row,
	            nc.ui.pub.bill.BillModel.ADD);
	        getBillCardPanel().getBillModel().setValueAt(null, row,
	            IItemKey.NAME_BODYID);
	        getBillCardPanel().getBillModel().setValueAt(null, row,
	            IItemKey.NTRANOUTNUM);
	        getBillCardPanel().getBillModel().setValueAt(null, row,
	            IItemKey.NTRANOUTASTNUM);
	        
	      }
	      //  设置新增单据的初始数据，如日期，制单人等。
	      setNewBillInitData();
	      getBillCardPanel().setEnabled(true);
	      setM_iMode(BillMode.New);
	      
	      GeneralBillUICtl.procFlagAndCalcNordcanoutnumAfterRefAdd(getM_voBill(),getBillCardPanel(),getBillType());

	      //  设置单据号是否可编辑
	      if (getBillCardPanel().getHeadItem("vbillcode") != null)
	        getBillCardPanel().getHeadItem("vbillcode").setEnabled(
	            m_bIsEditBillCode);
	      
	      nc.ui.pub.bill.BillEditEvent ee = new nc.ui.pub.bill.BillEditEvent(this,vo.getHeaderValue(IItemKey.WAREHOUSE),IItemKey.WAREHOUSE);
	      
	      getEditCtrl().afterWhEdit(ee,null,null);

	      setButtonStatus(true);

	    }
	    catch (Exception e) {
	      GenMethod.handleException(getClientUI(), null, e);
	    }
	    finally {
	      getBillCardPanel().getBillModel().setNeedCalculate(true);
	    }
	  }
    
    /**
     * 取消结算路径 
     */
    private void onCancelSettlePath() {
    	


	    try {
	      if (BillMode.New == getM_iMode()) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000539")/* @res "请单据保存之后再取消结算路径！" */);
	        return;
	      }

	      ArrayList alSelected = getSelectedBills();
	      if (alSelected == null || alSelected.size() != 1) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "scmcommon", "UPPSCMCommon-000167")/* @res "没有选择单据" */);
	        return;
	      }

	      int iselrow = getBillListPanel().getHeadTable().getSelectedRow();
	      GeneralBillVO voBill = (GeneralBillVO) alSelected.get(0);

	      if (voBill.getHeaderVO().getCgeneralhid() == null) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000505")/* @res "请单据保存之后再指定结算！" */);
	        return;
	      }
	      
	      if(voBill.getHeaderVO().getBoutretflag()!=null && voBill.getHeaderVO().getBoutretflag().booleanValue()){
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000531")/* @res "退回单据不需要指定结算路径" */);
	        return;
	      }
	      
	      if (null == voBill.getHeaderVO().getAttributeValue("csettlepathid")){
	    	  showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	  	            "4008other", "UPP4008other-000537")/* @res "单据结算路径已经为空，不需要取消结算路径！" */);
	  	        return;
	      }

	      GeneralBillItemVO[] voItems = voBill.getItemVOs();
	      UFDouble ufd = null;

	      for (int i = 0; i < voItems.length; i++) {

	        ufd = (UFDouble) voItems[i].getAttributeValue("nsettlenum1");

	        if (ufd != null && ufd.doubleValue() != 0.0)
	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000507")/* @res "已经做过调入调出结算，不能再指定结算路径！" */);

	        if (!"5D".equals(voItems[i].getCfirsttype()))

	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000508")/* @res "只有公司间调拨定单可以指定结算路径！" */);

	      }


	      //		


	      voBill.getHeaderVO().setCoperatoridnow(getEnvironment().getUserID());
	      GeneralBillVO voRet = GeneralHHelper.modifySettlePath(voBill, null);
	      if (voRet != null && voRet.getHeaderVO() != null) {
	        voRet.getHeaderVO().setAttributeValue("csettlepathid", null);
	        voRet.getHeaderVO().setAttributeValue("cpathname", null);
	        String[] keys = new String[] {
	            "ts", "csettlepathid", "cpathname"
	        };
	        refreshHeadValue(new GeneralBillVO[] {
	          voRet
	        }, keys);
	        selectListBill(iselrow);
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	            "UPP4008busi-000376")/*@res "更新成功！"*/);
	      }
	    }

	    catch (Exception e) {
	      nc.vo.scm.pub.SCMEnv.error(e);
	      showErrorMessage(e.getMessage());
	    }
      
    }

	  /**
	   * 指定结算路径 
	   */
	  private void onModifySettlePath() {

	    try {
	      if (BillMode.New == getM_iMode()) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000538")/* @res "请单据保存之后再指定结算路径！" */);
	        return;
	      }

	      ArrayList alSelected = getSelectedBills();
	      if (alSelected == null || alSelected.size() != 1) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "scmcommon", "UPPSCMCommon-000167")/* @res "没有选择单据" */);
	        return;
	      }

	      int iselrow = getBillListPanel().getHeadTable().getSelectedRow();
	      GeneralBillVO voBill = (GeneralBillVO) alSelected.get(0);

	      if (voBill.getHeaderVO().getCgeneralhid() == null) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000505")/* @res "请单据保存之后再指定结算！" */);
	        return;
	      }
	      
	      if(voBill.getHeaderVO().getBoutretflag()!=null && voBill.getHeaderVO().getBoutretflag().booleanValue()){
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000531")/* @res "退回单据不需要指定结算路径" */);
	        return;
	      }

	      GeneralBillItemVO[] voItems = voBill.getItemVOs();
	      UFDouble ufd = null;

	      for (int i = 0; i < voItems.length; i++) {

	        ufd = (UFDouble) voItems[i].getAttributeValue("nsettlenum1");

	        if (ufd != null && ufd.doubleValue() != 0.0)
	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000507")/* @res "已经做过调入调出结算，不能再指定结算路径！" */);

	        if (!"5D".equals(voItems[i].getCfirsttype()))

	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000508")/* @res "只有公司间调拨定单可以指定结算路径！" */);

	      }

	      SettlePathDlgForIC dlgModifySettlePath = new SettlePathDlgForIC(
	          (String) voBill.getHeaderVO().getAttributeValue("cothercorpid"),
	          getEnvironment().getCorpID(), getClientUI(), "指定结算路径");

	      if (dlgModifySettlePath == null)
	        return;

	      //dlgModifySettlePath.showModal();
	      dlgModifySettlePath.show();
	      if (dlgModifySettlePath.getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
	        return;
	      //		
	      //
	      String cpathid = dlgModifySettlePath.getSelectedSettlePathID();
	      //		

	      //String cpathid="1001AA10000000006QA9";
	      if (cpathid == null) {
	        SCMEnv.out("路径空！");
	        return;
	      }
	      voBill.getHeaderVO().setCoperatoridnow(getEnvironment().getUserID());
	      
	      //修改人：刘家清 修改时间：2008-12-25 下午04:17:15 修改原因：调用内部交易是否指定过结算路径时使用。
	      voBill.getHeaderVO().setAttributeValue("clientLink", new ClientLink(getClientEnvironment()));
	      
	      GeneralBillVO voRet = GeneralHHelper.modifySettlePath(voBill, cpathid);
	      if (voRet != null && voRet.getHeaderVO() != null) {
	        voRet.getHeaderVO().setAttributeValue("csettlepathid", cpathid);
	        String[] keys = new String[] {
	            "ts", "csettlepathid", "cpathname"
	        };
	        refreshHeadValue(new GeneralBillVO[] {
	          voRet
	        }, keys);
	        selectListBill(iselrow);
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	            "UPP4008busi-000376")/*@res "更新成功！"*/);
	      }
	    }

	    catch (Exception e) {
	      nc.vo.scm.pub.SCMEnv.error(e);
	      showErrorMessage(e.getMessage());
	    }
	  }

	  /**
	   * ******************************************
	   *  功能：<|>
	   *
	   *  创建日期：(2004-3-10 12:33:02)
	   *  @param：
	   *
	   *  @return：
	   *  ******************************************
	   */
	  private void onAddToOrder(String funnode, String qrynodekey,
	      String sourcetype, ITOToIC_QryDLg dlgQry) {

	    if (dlgQry == null) {
	      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	          "UPP4008busi-000293")/*@res "该功能不可用（原因：内部交易管理系统没有安装）。"*/);
	      return;
	    }

	    nc.vo.pub.AggregatedValueObject[] vos = dlgQry.getReturnVOs(getEnvironment().getCorpID(),
	        getEnvironment().getUserID(), sourcetype, getBillType(), funnode, qrynodekey, getClientUI());

	    if (vos == null) {
	      return;
	    }
	    try {
	      ////5C订单VO,转化为库存单据VO
	      nc.vo.pub.AggregatedValueObject[] voRetvos = (nc.vo.pub.AggregatedValueObject[]) nc.ui.pub.change.PfChangeBO_Client
	          .pfChangeBillToBillArray(vos, sourcetype, getBillType());
	      setBillRefResultVO(((GeneralBillVO)voRetvos[0]).getHeaderVO().getCbiztypeid(), voRetvos);
	      afterVoLoaded();

	    }
	    catch (Exception e) {
	      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	          "UPP4008busi-000297")/*@res "发生错误:"*/
	          + e.getMessage());
	    }
	  }

	  /**
	   * 指定调出公司 
	   */
	  private void onModifyOutCorp() {

	    try {
	      ArrayList alSelected = getSelectedBills();
	      if (alSelected == null || alSelected.size() == 0)
	        return;
	      if (m_dlgModifyCorp == null) {
	        m_dlgModifyCorp = new ModifyCorpDlg(getEnvironment().getCorpID());

	      }
	      m_dlgModifyCorp.showModal();
	      if (m_dlgModifyCorp.getResult() != nc.ui.pub.beans.UIDialog.ID_OK)
	        return;
	      ArrayList alparam = m_dlgModifyCorp.getResValue();
	      ArrayList alErrorCode = new ArrayList();

	      ArrayList alSameCorpBillCode = new ArrayList(); //存放指定调出公司与调入公司相同的单据号。 邵兵 on Jun 4, 2005		

	      //指定调出公司的pk
	      String sAppointedCorp = alparam.get(0).toString();
	      nc.vo.ic.pub.bill.GeneralBillHeaderVO headVO = null; //表头vo		

	      //单据过滤：只能修改来源于销售并且出入库单都已经签字的直运业务。
	      for (int i = 0; i < alSelected.size(); i++) {
	        GeneralBillVO rowVO = (GeneralBillVO) alSelected.get(i);
	        headVO = rowVO.getHeaderVO();

	        //将操作员和登陆日期放到表头
	        headVO.setCoperatoridnow(getEnvironment().getUserID()); //当前操作员
	        headVO.setAttributeValue("clogdatenow", getEnvironment().getLogDate()); //当前登录日期

	        //必须是直运签字单据
	        if (headVO.getFallocflag().intValue() != CONST.IC_ALLOCDIRECT
	            || !(headVO.getFbillflag().intValue() == 3 || headVO.getFbillflag()
	                .intValue() == 4)) {
	          alErrorCode.add(headVO.getVbillcode());
	          alSelected.remove(i);
	          i = i - 1;

	        }
	        else if (sAppointedCorp
	            .equals(headVO.getAttributeValue("cothercorpid"))) { //指定调出公司不能与调入公司相同(公司间调拨订单)
	          alSameCorpBillCode.add(headVO.getVbillcode());
	          alSelected.remove(i);
	          i = i - 1;

	        }
	      }

	      ArrayList alResult = null;
	      ArrayList alNOin = null; //没有完全生成调拨入库单，或者已经结算的单据
	      ArrayList alInvNotInOrg = null; //存放存货不属于指定的调出组织的单据号

	      if (alSelected != null && alSelected.size() > 0) {
	        nc.vo.ic.pub.bill.GeneralBillVO[] selVOs = new nc.vo.ic.pub.bill.GeneralBillVO[alSelected
	            .size()];
	        selVOs = (GeneralBillVO[]) alSelected.toArray(selVOs);
	        /**			 
	         * ArrayList alResult  index = 0: 没有完全生成调拨入库单，或者已经结算的单据.
	         *                     index = 1: 符合条件的单据: 修改调出公司及库存组织后的更新vo.
	         *                     index = 2: 存货不属于指定的调出组织的单据号.
	         **/
	        alResult = GeneralHHelper.modifyOutCorp(selVOs, alparam);

	        alNOin = (ArrayList) alResult.get(0);
	        Object oFreshUIInfo = alResult.get(1);
	        alInvNotInOrg = (ArrayList) alResult.get(2);

	        if (oFreshUIInfo != null) {
	          nc.vo.ic.pub.smallbill.SMGeneralBillVO[] voResult = null;
	          voResult = (nc.vo.ic.pub.smallbill.SMGeneralBillVO[]) oFreshUIInfo;
	          String[] keys = new String[] {
	              "ts", "coutcorpid", "coutunitname", "coutcalbodyid",
	              "coutbodyname"
	          };
	          refreshHeadValue(voResult, keys);
	        }
	      }

	      //未处理单据提示
	      StringBuffer sbHintMessage = new StringBuffer();
	      if (alErrorCode.size() > 0) {

	        sbHintMessage
	            .append(
	                nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	                    "UPP4008busi-000374")/*@res "只能修改来源于销售并且出入库单都已经签字的直运业务,下列单据不能修改调出公司："*/)
	            .append("\n").append(alErrorCode.toString());
	      }
	      if (alSameCorpBillCode.size() > 0) {

	        sbHintMessage
	            .append(
	                nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	                    "UPP4008busi-000389")/*@res "The following document's transfer-in company is the same as specified transfer-out company: "*/)
	            .append("\n").append(alSameCorpBillCode.toString());
	      }

	      if (alNOin != null && alNOin.size() > 0) {
	        if (sbHintMessage.length() > 0)
	          sbHintMessage.append("\n");
	        sbHintMessage.append(
	            nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	                "UPP4008busi-000387")/*@res "下列单据没有完全生成调拨入库单，或者已经结算："*/)
	            .append("\n").append(alNOin.toString());
	      }
	      if (alInvNotInOrg != null && alInvNotInOrg.size() > 0) {
	        if (sbHintMessage.length() > 0)
	          sbHintMessage.append("\n");
	        sbHintMessage.append(
	            nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	                "UPP4008busi-000388")/*@res "下列单据含有不属于指定库存组织的存货："*/).append(
	            "\n").append(alInvNotInOrg.toString());
	      }
	      if (sbHintMessage.length() != 0)
	        nc.ui.pub.beans.MessageDialog.showHintDlg(getClientUI(), null, sbHintMessage
	            .toString());
	      else
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	            "UPP4008busi-000376")/*@res "更新成功！"*/);
	    }

	    catch (Exception e) {
	      nc.vo.scm.pub.SCMEnv.error(e);
	      showHintMessage(e.getMessage());
	    }
	  }
	  
  }

  public IButtonManager getButtonManager() {
  	if (m_buttonManager == null) {
  		try {
  			m_buttonManager = new ButtonManager218(this);
  		} catch (BusinessException e) {
  			//日志异常
  			nc.vo.scm.pub.SCMEnv.error(e);
  			showErrorMessage(e.getMessage());
  		}
  	}
  	return m_buttonManager;
  }

  /**
   * 父类方法重写
   * 
   * @see nc.ui.ic.pub.bill.GeneralBillClientUI#onClosing()
   */
  public boolean onClosing() {

    if (BillMode.List == getM_iCurPanel() && m_bRefBills) {
      int iret = MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("4008busi", "UPP4008busi-000400")/*@res "操作尚未完成，是否关闭？"*/);
      if (iret == MessageDialog.ID_YES)
        return true;
      else
        return false;
    }

    return super.onClosing();

  }

  /**
   * ?user>
   * 功能：
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2005-2-4 15:16:45)
   * 修改日期，修改人，修改原因，注释标志：
   * 
   * @param smallvos nc.vo.ic.pub.smallbill.SMGeneralBillVO[]
   */
  private void refreshHeadValue(AggregatedValueObject[] smallvos, String[] keys) {
    if (smallvos == null)
      return;
    java.util.HashMap htsmall = new java.util.HashMap();

    for (int i = 0; i < smallvos.length; i++) {
      htsmall.put(smallvos[i].getParentVO().getAttributeValue("cgeneralhid"),
          smallvos[i]);
    }

    if (getM_alListData() != null) {
      for (int i = 0; i < getM_alListData().size(); i++) {
        GeneralBillVO voBill = (GeneralBillVO) getM_alListData().get(i);
        String hid = voBill.getHeaderVO().getCgeneralhid();
        if (htsmall.containsKey(hid)) {
          AggregatedValueObject svo = (AggregatedValueObject) htsmall.get(hid);

          for (int j = 0; j < keys.length; j++) {
            voBill.getHeaderVO().setAttributeValue(keys[j],
                svo.getParentVO().getAttributeValue(keys[j]));
          }
          //新刷卡片

          if (getM_voBill() != null && getM_voBill().getHeaderVO() != null
              && getM_voBill().getHeaderVO().getCgeneralhid().equals(hid)) {

            getBillCardPanel().getHeadItem("ts").setValue(
                svo.getParentVO().getAttributeValue("ts"));
            for (int j = 0; j < keys.length; j++) {
              getM_voBill().getHeaderVO().setAttributeValue(keys[j],
                  svo.getParentVO().getAttributeValue(keys[j]));
            }
            for (int j = 0; j < keys.length; j++) {
              if (getBillCardPanel().getHeadItem(keys[j]) != null)
                getBillCardPanel().getHeadItem(keys[j]).setValue(
                    svo.getParentVO().getAttributeValue(keys[j]));
            }

          }

        }

      }

      //刷新列表界面显示
      nc.vo.ic.pub.bill.GeneralBillHeaderVO voh[] = new nc.vo.ic.pub.bill.GeneralBillHeaderVO[getM_alListData()
          .size()];
      for (int i = 0; i < getM_alListData().size(); i++) {
        if (getM_alListData().get(i) != null)
          voh[i] = (nc.vo.ic.pub.bill.GeneralBillHeaderVO) ((GeneralBillVO) getM_alListData()
              .get(i)).getParentVO();

        //SCMEnv.out("list data error!-->" + i);

      }

      setListHeadData(voh);
    }

  }

  /**
   * 创建者：王乃军
   * 功能：在列表方式下选择一张单据
   * 参数： 单据在alListData中的索引
   * 返回：无
   * 例外：
   * 日期：(2001-11-23 18:11:18)
   *  修改日期，修改人，修改原因，注释标志：
   */
  protected void selectBillOnListPanel(int iBillIndex) {
  }

  /**
   * 创建者：王乃军
   * 功能：根据设定的按钮初始化菜单。
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-11-23 18:11:18)
   *  修改日期，修改人，修改原因，注释标志：
   */
  /*
   protected void setButtons() {
   try {
   Vector vSubMenu = null;
   if (m_vTopMenu != null && m_vTopMenu.size() > 0) {

   //顶层菜单
   m_boaButtonGroup = new ButtonObject[m_vTopMenu.size()];
   //子菜单
   ButtonObject boSub = null;
   for (int i = 0; i < m_vTopMenu.size(); i++) {
   if (m_vTopMenu.elementAt(i) instanceof Vector) {
   //子菜单
   vSubMenu = (Vector) m_vTopMenu.elementAt(i);
   //第0个是顶层菜单
   boSub = (ButtonObject) vSubMenu.elementAt(0);
   for (int j = 1; j < vSubMenu.size(); j++)
   boSub.addChildButton((ButtonObject) vSubMenu.elementAt(j));
   m_boaButtonGroup[i] = boSub;
   } else
   //顶层菜单
   m_boaButtonGroup[i] = (ButtonObject) m_vTopMenu.elementAt(i);
   }
   setButtons(m_boaButtonGroup);
   }
   } catch (Exception e) {
   handleException(e);
   }

   }
   */
  /**
   * 创建者：王乃军
   * 功能：抽象方法：设置按钮状态，在setButtonStatus中调用。
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
  protected void setButtonsStatus(int iBillMode) {
  }

  /**
   * ******************************************
   *  功能：<|>
   *
   *  创建日期：(2004-3-9 11:09:31)
   *  @param：
   *
   *  @return：
   *  ******************************************
   * @param newCalbodyis java.lang.String
   */
  public void setCalbodyid(java.lang.String newCalbodyid) {
    m_sCalbodyid = newCalbodyid;
  }

  /**
   * 创建者：王乃军
   * 功能：设置新增单据的初始数据，如日期，制单人等。设置库存组织
   * 参数：
   * 返回：
   * 例外：
   * 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   *
   *
   *
   *
   */
  protected void setNewBillInitData() {
    super.setNewBillInitData();
    try {

      //------------------ head values --------------------------------
      if (getBillCardPanel().getHeadItem(IItemKey.CALBODY) != null
          && getCalbodyid() != null) {
        getBillCardPanel().setHeadItem(IItemKey.CALBODY, getCalbodyid());

      }

    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.error(e);

    }

  }

  /*
   * 签字及取消签字动作后，重新查询动作作用vo，并刷新界面。
   * 
   * @author: shaobing
   * @date:2005-06-21
   */
  /**
   * 此处插入方法说明。 功能：刷新ts,得到单据状态 参数： 返回： 例外： 日期：(2002-6-4 19:54:51)
   * 修改日期，修改人，修改原因，注释标志：
   * 
   * @return java.lang.String
   * @param sBillPK
   *            java.lang.String
   */
//  protected String freshStatusTs(String sBillPK) throws Exception {
//    String sBillStatus = null;
//
//    //重新查询vo            
//    //拼接查询条件
//    String sQryWhere = " head.cgeneralhid ='" + getM_voBill().getPrimaryKey()
//        + "'  ";
//    QryConditionVO voQryCond = new QryConditionVO(sQryWhere);
//    voQryCond.setIntParam(0, GeneralBillVO.QRY_FULL_BILL_PURE);
//
//    //查询
//    GeneralBillVO voRetBill = (GeneralBillVO) GeneralBillHelper.queryBills(
//        getBillType(), voQryCond).get(0);
//
//    getM_alListData().remove(getM_iLastSelListHeadRow());
//    getM_alListData().add(getM_iLastSelListHeadRow(), voRetBill);
//    setM_voBill((GeneralBillVO) getM_alListData().get(
//        getM_iLastSelListHeadRow()));
//
//    ArrayList altemp = new ArrayList();
//    altemp.add(getM_voBill());
//    setAlistDataByFormula(1, altemp);
//
//    //更新表单界面数据
//    setBillVO(getM_voBill());
//
//    //更新列表界面数据
//    GeneralBillHeaderVO voaHeader[] = new GeneralBillHeaderVO[getM_alListData()
//        .size()];
//    for (int i = 0; i < getM_alListData().size(); i++) {
//      if (getM_alListData().get(i) != null) {
//        voaHeader[i] = (GeneralBillHeaderVO) ((GeneralBillVO) getM_alListData()
//            .get(i)).getParentVO();
//        if (voaHeader[i].getCgeneralhid() != null
//            && voaHeader[i].getCgeneralhid().equals(sBillPK)
//            && voaHeader[i].getFbillflag() != null)
//          sBillStatus = voaHeader[i].getFbillflag().toString();
//      }
//      else {
//        nc.vo.scm.pub.SCMEnv.out("list data error!-->" + i);
//        sBillStatus = BillStatus.DELETED;
//      }
//
//    }
//    setListHeadData(voaHeader);
//    selectListBill(getM_iLastSelListHeadRow());
//
//    return sBillStatus;
//  }

  /**
   * 创建者：王乃军 功能：在表单设置显示VO 参数： 返回： 例外： 日期：(2001-5-9 9:23:32)
   * 修改日期，修改人，修改原因，注释标志：
   * 
   * 
   * 
   * 
   */
  public void setBillVO(GeneralBillVO bvo, boolean bUpdateBotton,
      boolean bExeFormule) {
    if(bvo!=null)
      getBB3Info(bvo.getItemVOs(), getBillCardPanel().getBillModel());
    super.setBillVO(bvo, bUpdateBotton, bExeFormule);
    if (nc.vo.ic.pub.GenMethod.isSEmptyOrNull((String) bvo.getHeaderVO().getCgeneralhid()))
      afterVoLoaded();
  }
  
  /**
   * 创建者：王乃军 功能：选中列表形式下的第sn张单据 参数：sn 单据序号
   * 
   * 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
   * 
   * 
   * 
   *  
   */
  public void setListBodyData(GeneralBillItemVO voi[]) {
    getBB3Info(voi,getBillListPanel().getBodyBillModel());
    super.setListBodyData(voi);
  }

  
  /**
   * 
   * 
   * 返回： 例外： 日期：(2001-5-9 9:23:32) 修改日期，修改人，修改原因，注释标志：
   * 
   * 
   * 
   * 
   */
  public void getBB3Info(GeneralBillItemVO[] voi,BillModel md) {
    if(voi==null || voi.length<=0 || md==null)
      return;
    UFBoolean bget_naccumoutbacknum = CheckTools.toUFBoolean(voi[0].getAttributeValue(IItemKey.bget_naccumoutbacknum));
    if(bget_naccumoutbacknum!=null && bget_naccumoutbacknum.booleanValue())
      return;
    ArrayList<String> bb3field = new ArrayList<String>(4);
    ArrayList<String> vofield = new ArrayList<String>(4);
    IntList type = new IntList(4);
    String[] uikeys = new String[]{IItemKey.naccumoutbacknum};
    String[] vofieldkeys = new String[]{IItemKey.naccumoutbacknum};
    BillItem item = md.getItemByKey(IItemKey.naccumoutbacknum);
    for(int i=0;i<uikeys.length;i++){
      item = md.getItemByKey(uikeys[i]);
      if(item!=null && item.isShow()){
        vofield.add(uikeys[i]);
        bb3field.add(vofieldkeys[i]);
        type.add(SmartFieldMeta.JAVATYPE_UFDOUBLE);
      }
    }
    if(vofield.size()>0){
      try{
        GeneralBillUICtl.fillBodyVOFromBB3(voi, vofield.toArray(new String[vofield.size()]), 
            bb3field.toArray(new String[bb3field.size()]), type.toIntArray());
        for(int i=0;i<voi.length;i++){
          voi[i].setAttributeValue(IItemKey.bget_naccumoutbacknum, UFBoolean.TRUE);
        }
      }catch(Exception e){
        GenMethod.handleException(null, null, e);
      }
    }
  }
  
  @Override
	public boolean onSaveBase() {
	  try {
			nc.vo.ic.pub.bill.Timer t = new nc.vo.ic.pub.bill.Timer();
			m_timer.start("保存开始");
			t.start();
			// 滤去表单形式下的空行
			filterNullLine();

			m_timer.showExecuteTime("filterNullLine");
			// 无表体行 ------------ EXIT -------------------
			if (getBillCardPanel().getRowCount() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000072")/* @res "请输入表体行!" */);
				// 不添加新行 add by hanwei 2004-06-08 ,调拨出入库单有些情况下不能自制
				return false;
			}
			// added by zhx 030626 检查行号的合法性; 该方法应放在过滤空行的后面。
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(
					getBillCardPanel(), IItemKey.CROWNO)) {
				return false;
			}
			// 当前的表体行数
			int iRowCount = getBillCardPanel().getRowCount();
			// 界面的单据数据
			GeneralBillVO voInputBill = null;
			// 从界面中获得需要的数据
			voInputBill = getBillVO();
			
			// add by river for 2012-11-20
			// 将承运商直接设置到VO中.   原因：界面中无法获取到承运商的数据。通过ITEM可以获取到。
			Object ctrancustid = getBillCardPanel().getHeadItem("ctrancustid").getValueObject();
			voInputBill.getParentVO().setAttributeValue("ctrancustid", ctrancustid);
			
			// 得到数据错误，出错 ------------ EXIT -------------------
			if (voInputBill == null || voInputBill.getParentVO() == null
					|| voInputBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				return false;
			}
			// 输入的表体行
			GeneralBillItemVO voInputBillItem[] = voInputBill.getItemVOs();
			// 得到数据行
			int iVORowCount = voInputBillItem.length;
			// 得到数据行和界面行数不一致，出错 ------------ EXIT -------------------
			if (iVORowCount != iRowCount) {
				SCMEnv.out("data error." + iVORowCount + "<>" + iRowCount);
				return false;
			}
			m_timer.showExecuteTime("From fliterNullLine Before setIDItems");
			// VO校验准备数据
			getM_voBill().setIDItems(voInputBill);
			// 设置单据类型
			getM_voBill().setHeaderValue("cbilltypecode", getBillType());

			m_timer.showExecuteTime("setIDItems");

			// 重置单据行号zhx 0630:
			if (iRowCount > 0 && getM_voBill().getChildrenVO() != null) {
				if (getBillCardPanel().getBodyItem(IItemKey.CROWNO) != null)
					for (int i = 0; i < iRowCount; i++) {
						getM_voBill().setItemValue(
								i,
								IItemKey.CROWNO,
								getBillCardPanel().getBodyValueAt(i,
										IItemKey.CROWNO));

					}
			}
			// VO校验 ------------ EXIT -------------------
			if (!checkVO(getM_voBill())) {

				return false;
			}
			m_timer.showExecuteTime("VO校验");

			// 如果没有单据日期，填写为当前登录日期
			if (getBillCardPanel().getHeadItem("dbilldate") == null
					|| getBillCardPanel().getHeadItem("dbilldate")
							.getValueObject() == null
					|| getBillCardPanel().getHeadItem("dbilldate")
							.getValueObject().toString().trim().length() == 0) {
				SCMEnv.out("-->no bill date.");
				getM_voBill().setHeaderValue("dbilldate",
						getEnvironment().getLogDate());
			}
			m_timer.showExecuteTime("设置单据类型和单据日期");

			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008bill", "UPP4008bill-000102")/* @res "正在保存，请稍候..." */);

			// 保存的核心方法入口 add by hanwei 2004-04

			// 默认单据状态 add by hanwei
			m_sBillStatus = nc.vo.ic.pub.bill.BillStatus.FREE;
			// 实际m_sBillStatus的赋值在onSaveBaseKernel中的：saveUpdateBill,saveNewBill

			getM_voBill().setIsCheckCredit(true);
			getM_voBill().setIsCheckPeriod(true);
			getM_voBill().setIsCheckAtp(true);
			getM_voBill().setGetPlanPriceAtBs(false);
			getM_voBill().setIsRwtPuUserConfirmFlag(false);

			// 补充空值
			//fillItemNullValue();

			while (true) {
				try {

					onSaveBaseKernel(getM_voBill(), getEnvironment()
							.getUserID());
					break;

				} catch (Exception ee1) {

					BusinessException realbe = nc.ui.ic.pub.tools.GenMethod
							.handleException(null, null, ee1);
					if (realbe != null
							&& realbe.getClass() == nc.vo.scm.pub.excp.RwtIcToPoException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						int iFlag = showYesNoMessage(realbe.getMessage());
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							getM_voBill().setIsRwtPuUserConfirmFlag(true);
							continue;
						} else
							return false;
					} else if (realbe != null
							&& realbe.getClass() == CreditNotEnoughException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						// 是否继续？ 改为多语形式 modify by qinchao  20081225 圣诞节，共3处替换
						int iFlag = showYesNoMessage(realbe.getMessage()
								+ " \r\n" + 
								nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","ClientUI-000001")/* @res "是否继续" */);
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							getM_voBill().setIsCheckCredit(false);
							continue;
						} else
							return false;
					} else if (realbe != null
							&& realbe.getClass() == PeriodNotEnoughException.class) {
						// 错误信息显示，并询问用户“是否继续？”
						int iFlag = showYesNoMessage(realbe.getMessage()
								+ " \r\n" + 
								nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","ClientUI-000001")/* @res "是否继续" */);
						// 如果用户选择继续
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							getM_voBill().setIsCheckPeriod(false);
							continue;
						} else
							return false;
					} else if (realbe != null
							&& realbe.getClass() == ATPNotEnoughException.class) {
						ATPNotEnoughException atpe = (ATPNotEnoughException) realbe;
						if (atpe.getHint() == null) {
							showErrorMessage(atpe.getMessage());
							return false;
						} else {
							// 错误信息显示，并询问用户“是否继续？”
							int iFlag = showYesNoMessage(atpe.getMessage()
									+ " \r\n" + 
									nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","ClientUI-000001")/* @res "是否继续" */);
							// 如果用户选择继续
							if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
								getM_voBill().setIsCheckAtp(false);
								continue;
							} else {
								return false;
							}
						}
					} else {
						if (realbe != null)
							throw realbe;
						else
							throw ee1;
					}
				}
			}
			
			

			// 是普通新增、或修改
			if (BillMode.New == getM_iMode() || BillMode.Update == getM_iMode()) {
				// necessary！//刷新单据状态
				getBillCardPanel().updateValue();
				m_timer.showExecuteTime("updateValue");
				// coperatorid
				setM_iMode(BillMode.Browse);
				
				getEditCtrl().resetCardEditFlag(getBillCardPanel());
				// 不可编辑
				getBillCardPanel().setEnabled(false);
				// 重设按钮状态
				setButtonStatus(false);
				m_timer.showExecuteTime("setButtonStatus");

				// 清空现存量
				// 屏蔽 by hanwei 2003-11-13 避免保存后界面选择出现存量为空
				// m_voBill.clearInvQtyInfo();
				// 选中第一行
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// 重置序列号是否可用
				setBtnStatusSN(0, false);
				// 刷新第一行现存量显示
				setTailValue(0);
				m_timer.showExecuteTime("刷新第一行现存量显示");
			}

//			if (m_sBillStatus != null && !m_sBillStatus.equals(BillStatus.FREE)
//					&& !m_sBillStatus.equals(BillStatus.DELETED)) {
//				SCMEnv.out("**** saved and signed ***");
//				freshAfterSignedOK(m_sBillStatus);
//				m_timer.showExecuteTime("freshAfterSignedOK");
//			}
    
    m_sBillStatus = afterAuditFrushData(getM_voBill(),true);
    
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH005")/* @res "保存成功" */);

			// 对于有来源的单据进行不同的界面控制；zhx 1130
			ctrlSourceBillUI(true);
			m_timer.showExecuteTime("来源单据界面控制");
			t.stopAndShow("保存合计");

			// save the barcodes to excel file according to param IC***
			m_timer.showExecuteTime("开始执行保存条码文件");
			OffLineCtrl ctrl = new OffLineCtrl(this);
			ctrl.saveExcelFile(getM_voBill(), getCorpPrimaryKey());
			m_timer.showExecuteTime("执行保存条码文件结束");
			nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());
			return true;

		} catch (java.net.ConnectException ex1) {
			SCMEnv.out(ex1.getMessage());
			if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000104")/*
														 * @res
														 * "当前网络中断，是否将单据信息保存到默认目录："
														 */
			) == MessageDialog.ID_YES) {
				onButtonClicked(getButtonManager().getButton(
						ICButtonConst.BTN_EXPORT_TO_DIRECTORY));
				// onBillExcel(1);// 保存单据信息到默认目录
			}
		} catch (Exception e) {

			if (e instanceof nc.vo.ic.ic009.PackCheckBusException) {

				handleException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000105")/* @res "保存出错。" */);
				String se = e.getMessage();
				if (se != null) {
					int index = se.indexOf("$$ZZZ$$");
					if (index >= 0)
						se = se.substring(index + 7);
				}
				// packCheckBusDialog = null;
				getpackCheckBusDialog().setText(se);

				getpackCheckBusDialog().showModal();

			} else {

				handleException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000105")/* @res "保存出错。" */);
				String se = e.getMessage();
				if (se != null) {
					int index = se.indexOf("$$ZZZ$$");
					if (index >= 0)
						se = se.substring(index + 7);
				}
				showErrorMessage(se);
			}
			
			if (e instanceof ICBusinessException){
				ICBusinessException ee = (ICBusinessException) e;
				// 更改颜色
				nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(),ee);
			}

		}
		return false;
	}

}