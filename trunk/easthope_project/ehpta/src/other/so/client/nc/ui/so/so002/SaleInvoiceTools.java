package nc.ui.so.so002;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.bd.b21.BusinessCurrencyRateUtil;
import nc.bs.framework.common.NCLocator;

import nc.itf.scm.so.receive.IReceiveSplitQueryService;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.para.SysInitBO_Client;
import nc.ui.scm.pub.def.DefSetTool;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so016.SOToolsBO_Client;

import nc.vo.bd.def.DefVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ScmConst;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.so.pub.SOCurrencyRateUtil;
import nc.vo.so.so002.SaleVO;
import nc.vo.so.so002.SaleinvoiceBVO;
import nc.vo.so.so002.SaleinvoiceVO;
import nc.vo.so.so016.SoVoTools;
import nc.vo.so.sodispart.SaleDispartVO;

public class SaleInvoiceTools {
  public SaleInvoiceTools() {
		
		// 批量获取
	    try {
	      head_defs = DefSetTool.getDefHead(getLoginPk_Corp(), ScmConst.SO_Invoice);

	      body_defs = DefSetTool.getDefBody(getLoginPk_Corp(), ScmConst.SO_Invoice);

	    }
	    catch (Exception e) {
	      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
	    }
	    

	    String[] para = {
	        "SA08", "SA15", "SO27", "SO22", "BD501", "BD502", "BD503",
	        "BD505", "BD302", "SO30", "SA02", "SA13",
	        // yt add 2004-12-19
	        "SO34", "SO36", "SO49", "SO59", "SO60", "SO41", "BD301","SO40",
	        "SO67"
	    };

	    // 批量获取初始参数
	    if (hsparas == null) {
	      // 批量获取
	      try {
	        hsparas = SysInitBO_Client
	            .queryBatchParaValues(getLoginPk_Corp(), para);
	        UFBoolean ud = SysInitBO_Client.getParaBoolean("0001", "SA09");

	        if (hsparas != null && ud != null)
	          hsparas.put("SA09", ud.toString());
          
          IReceiveSplitQueryService bo = (IReceiveSplitQueryService) NCLocator.getInstance().lookup(
              IReceiveSplitQueryService.class.getName());
          
          SO_76 = bo.queryAll(getEnvironment(),SaleBillType.SaleInvoice,SaleDispartVO.ONLY_SPLIT);

	      }
	      catch (Exception e) {
	        nc.vo.scm.pub.SCMEnv.out(e.getMessage());
	      }

	    }

	    // 获取参数
	    String svalue = null;
	    // SA_08
	    svalue = (String) hsparas.get("SA08");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SA_08 = UFBoolean.FALSE;
	    else
	      SA_08 = new UFBoolean(svalue.trim());

	    svalue = (String) hsparas.get("SA15");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SA_15 = UFBoolean.FALSE;
	    else
	      SA_15 = new UFBoolean(svalue.trim());

	    svalue = (String) hsparas.get("SO27");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SO_27 = UFBoolean.FALSE;
	    else
	      SO_27 = new UFBoolean(svalue.trim());

	    svalue = (String) hsparas.get("SO22");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SO_22 = new UFDouble(0.0);
	    else
	      SO_22 = new UFDouble(svalue.trim());

	    // SO_06 = (String) hsparas.get("SO06");

	    svalue = (String) hsparas.get("BD501");
	    if (svalue == null || svalue.trim().length() <= 0)
	      BD_501 = new Integer(2);
	    else
	      BD_501 = new Integer(svalue.trim());

	    svalue = (String) hsparas.get("BD502");
	    if (svalue == null || svalue.trim().length() <= 0)
	      BD_502 = new Integer(2);
	    else
	      BD_502 = new Integer(svalue.trim());

	    svalue = (String) hsparas.get("BD503");
	    if (svalue == null || svalue.trim().length() <= 0)
	      BD_503 = new Integer(2);
	    else
	      BD_503 = new Integer(svalue.trim());

	    svalue = (String) hsparas.get("BD505");
	    if (svalue == null || svalue.trim().length() <= 0)
	      BD_505 = new Integer(2);
	    else
	      BD_505 = new Integer(svalue.trim());

	    svalue = (String) hsparas.get("BD302");
	    if (svalue == null || svalue.trim().length() <= 0)
	      BD_302 = UFBoolean.FALSE;
	    else
	      BD_302 = new UFBoolean(svalue.trim());

	    svalue = (String) hsparas.get("SO20");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SO_20 = UFBoolean.FALSE;
	    else
	      SO_20 = new UFBoolean(svalue.trim());

	    svalue = (String) hsparas.get("SO30");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SO_30 = UFBoolean.FALSE;
	    else
	      SO_30 = new UFBoolean(svalue.trim());
	    
	    svalue = (String) hsparas.get("SO40");
	    if (svalue == null || svalue.trim().length() <= 0)
	    	SO_40 = "调整折扣";
	    else
	    	SO_40 = svalue;

	    // 基价含税同时考虑集团和公司

	    svalue = (String) hsparas.get("SA02");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SA_02 = UFBoolean.FALSE;
	    else
	      SA_02 = new UFBoolean(svalue.trim());

	    String SA13 = (String) hsparas.get("SA13");
	    if ("集团定价".equals(SA13)) {/*-=notranslate=-*/

	      svalue = (String) hsparas.get("SA09");
	      if (svalue == null || svalue.trim().length() <= 0)
	        SA_02 = UFBoolean.FALSE;
	      else
	        SA_02 = new UFBoolean(svalue.trim());

	    }

	    // yt add 2004-12-19
	    svalue = (String) hsparas.get("SO34");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SO_34 = new UFDouble(0.0);
	    else
	      SO_34 = new UFDouble(svalue.trim());
	    //
	    String s36 = (String) hsparas.get("SO36");
	    if (s36 == null || s36.trim().length() == 0)
	      SO_36 = UFBoolean.FALSE;
	    else
	      SO_36 = new UFBoolean(s36.trim());

	    String s49 = (String) hsparas.get("SO49");
	    if (s49 == null || s49.trim().length() == 0)
	      SO_49 = UFBoolean.FALSE;
	    else
	      SO_49 = new UFBoolean(s49.trim());

	    String s59 = (String) hsparas.get("SO59");
	    if (s59 == null || s59.trim().length() == 0)
	      SO_59 = UFBoolean.FALSE;
	    else
	      SO_59 = new UFBoolean(s59.trim());

	    String s60 = (String) hsparas.get("SO60");
	    if (s60 == null || s60.trim().length() == 0)
	      SO_60 = UFBoolean.FALSE;
	    else
	      SO_60 = new UFBoolean(s60.trim());

	    svalue = (String) hsparas.get("SO41");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SO_41 = UFBoolean.FALSE;
	    else
	      SO_41 = new UFBoolean(svalue.trim());

	    BD_301 = (String)hsparas.get("BD301"); 
	    
	    svalue = (String)hsparas.get("SO67");
	    if(svalue !=null && svalue.trim().length()>0){
	    	SO_67 = new Integer(svalue);
	    }
      if(SO_76 ==null){
        SO_76 =  SaleDispartVO.getDefaultSaleDispartVO(getEnvironment(), SaleBillType.SaleInvoice);
      }
  }
  private nc.vo.scm.pub.session.ClientLink getEnvironment() {
    if (m_Cl==null){
        m_Cl = new nc.vo.scm.pub.session.ClientLink(ClientEnvironment
            .getInstance()); 
    }
    return m_Cl;
}
// public UFBoolean BD302 = null;
  //
  // public BusinessCurrencyRateUtil currtype = null;
  private static BusinessCurrencyRateUtil currtype = null; // 币种
  
  private static SOCurrencyRateUtil m_socurrtype = null; 

//  private static SaleInvoiceTools m_tools = null;

  // 参数哈希表
  private java.util.Hashtable hsparas = null;

  // 设置自定义项表头
  private static DefVO[] head_defs = null;

  // 设置自定义项表体
  private static DefVO[] body_defs = null;

  // 价格参数
  public UFBoolean SA_02 = null; // 基价含税

  public UFBoolean SA_08 = null; // 发票净价可否更改

  public UFBoolean SA_15 = null; // 是否询价

  // 销售发票是否按照订单客户分单
  public UFBoolean SO_60 = UFBoolean.TRUE;

  // yt add 2004-12-19
  // 单据行的默认数量
  public UFDouble SO_34 = new UFDouble(0.0);

  public UFBoolean SO_36 = UFBoolean.TRUE;

  public UFBoolean SO_49 = UFBoolean.FALSE;

  // 合并开票参数 add 2004-03-15
  public UFBoolean SO_27 = null;// 是否按照产品线进行合并开票

  // yt add 2004-03-26
  public UFBoolean SO_30 = null; // 是否允许同一客户下开票单位不同的销售订单开票

  public UFDouble SO_22 = new UFDouble(0);

  public UFBoolean SO_20 = new UFBoolean("Y"); // 销售管理是否使用通用模版
  
  public String SO_40 = null;//调整价税合计、含税净价、无税金额、无税净价时调整折扣还是单价
  
  public UFBoolean SO_59 = null;
  // fengjb add 2008-08-06
  public SaleDispartVO[]  SO_76 = null;//参照出库单开票的默认分单方式
  
  public Integer SO_67 = null;//发票限行
  
  
  //add by fengjb 20080829 缓存的分单对话框点击确定时的分单条件
  private SaleDispartVO[] m_splitcondition = null;
  
  // 公共参数
  public UFBoolean BD_302 = null; // 小数位数

  public Integer BD_501 = null; // 主计量数量小数位数

  public Integer BD_502 = null; // 辅计量数量小数位数

  public Integer BD_503 = null; // 换算率

  public Integer BD_505 = null; // 单价小数位数
  
  public UFBoolean SO_41 = null;//单据修改后是否修改制单人

  public static final String sAll = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40069907", "UPT40069907-000001")/* @res "全部" */;

  public static final String sReturnPrif = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40060501", "UPP40060501-000139")/* @res "返利单" */;

  public static final String sSafePrice = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40060501", "UPP40060501-000138")/* @res "价保单" */;

  public static final String sSelfMake = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40060501", "UPP40060501-000140")/* @res "手工录入" */;

  //客户信息20071214
  private static HashMap<String,String[][]> m_hCustInfo = new HashMap<String,String[][]>();
  
  public String BD_301 = null;//本位币
  
  private nc.vo.scm.pub.session.ClientLink m_Cl = null;
  
  
  /**
   * 方法功能描述：点击分单条件弹出分单条件界面并且点击了“确定”"保存为默认"按钮的时候设置当前界面上的分单条件值。
   * <b>参数说明</b>
   * @author fengjb
   * @time 2008-8-29 下午03:45:35
   */
 public void setSplitCondition(SaleDispartVO[] dispvo){
   m_splitcondition = dispvo;
 }
 
 /**
  * 
  */
 public SaleDispartVO[] getSplitCondition(){
   if(m_splitcondition == null)
     return SO_76;
   else
   return m_splitcondition;
 }

  /**
   * 销售出库单->销售发票时，批票取销售出库单上数据（根据来源类型是3U还是32，取退货申请单或销售订单）
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param saleinvoice
   *          发票VO
   * @return
   *          <p>
   * @author wangyf
   * @time 2007-8-6 下午01:17:45
   */
  public SaleinvoiceVO getDataFrom4CTo32(SaleinvoiceVO saleinvoice) {

    SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[]) saleinvoice.getChildrenVO();
    SaleVO head = (SaleVO) saleinvoice.getParentVO();
    try {
      // 处理库存相关数据
      doSaleinvoiceBVOsByIC(bodys);
      // 拆分单据，按源头单据类型
      SaleinvoiceBVO[][] vos = (SaleinvoiceBVO[][]) nc.vo.scm.pub.vosplit.SplitBillVOs
          .getSplitVOs(bodys, new String[] {
            "creceipttype"
          });
      // ArrayList al3U= new ArrayList();
      // ArrayList al30= new ArrayList();
      // ArrayList alOther= new ArrayList();
      SaleinvoiceBVO[] btmp = null; // 临时
      // 处理相关数据
      for (int i = 0, iLen = vos.length; i < iLen; i++) {
        btmp = (SaleinvoiceBVO[]) vos[i];
        if (btmp[0].getCreceipttype() != null
            && btmp[0].getCreceipttype().equals("3U")) {
          doSaleinvoiceBVOsBy3U(btmp);
          // al3U.add(btmp);
        }
        else if (btmp[0].getCreceipttype() != null
            && btmp[0].getCreceipttype().equals("30")) {
          doSaleinvoiceBVOsBy30(BD_302, btmp, head);
          // al30.add(btmp);
        }
        // else
        // alOther.add(btmp);
      }
      // 合并VO
      // alOther.addAll(al3U);
      // alOther.addAll(al30);
      // bodys = (SaleinvoiceBVO[])alOther.toArray(new
      // SaleinvoiceBVO[alOther.size()]);
      // saleinvoice.set

    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }

    return saleinvoice;
  }

  /**
   * 处理相关来源于出库单的信息:取相关价格信息
   * 
   * @param bodys
   */
  private static void doSaleinvoiceBVOsByIC(SaleinvoiceBVO[] bodys)
      throws Exception {

    ArrayList cicbodyids = new ArrayList();
    for (int i = 0; i < bodys.length; i++) {
      cicbodyids.add(bodys[i].getCupsourcebillbodyid());
    }
    String[] fds = new String[] {
        "nquoteprice", "cquotecurrency", "cquoteunitid", "nquoteunitrate"
    };

    java.util.HashMap hsicrows = SOToolsBO_Client.getAnyValueSORow(
        "ic_general_b", fds, "cgeneralbid", (String[]) cicbodyids
            .toArray(new String[cicbodyids.size()]), null);

    if (hsicrows != null && hsicrows.size() > 0) {
      nc.vo.so.so001.SORowData row = null;
      for (int k = 0, loopk = bodys.length; k < loopk; k++) {
        if (bodys[k] == null && bodys[k].getCupsourcebillbodyid() == null)
          continue;
        row = (nc.vo.so.so001.SORowData) hsicrows.get(bodys[k]
            .getCupsourcebillbodyid());
        if (row == null)
          continue;

        bodys[k].setCcurrencytypeid(row.getString(1));
        bodys[k].setNquoteoriginalcurtaxnetprice(row.getUFDouble(0));

        // 先存在一个变量里
        bodys[k].setM_outquoteorgcurtaxnetprice(row.getUFDouble(0));

      }
    }
  }

  /**
   * 处理源头为退货申请单的发票子VO
   * 
   * @param bvos
   * @return
   */
  private static void doSaleinvoiceBVOsBy3U(SaleinvoiceBVO[] bodys)
      throws Exception {

    ArrayList csourcebillbodyids = new ArrayList();
    for (int i = 0; i < bodys.length; i++) {
      csourcebillbodyids.add(bodys[i].getCsourcebillbodyid());
    }

    String[] fieldnames = new String[] {
        "pk_apply_b", "ccurrencytypeid", "ndiscountrate", "nitemdiscountrate",
        "nexchangeotobrate", "ndiscountmny", "ntaxrate",
        "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice",
        "noriginalcurtaxnetprice", "nprice", "ntaxprice", "nnetprice",
        "ntaxnetprice", "noriginalcurnetprice", "noriginalcurprice",
        "noriginalcurtaxnetprice", "noriginalcurtaxprice", "nsummny",
        "noriginalcursummny", "pk_productline", "noriginalcurdiscountmny",
        "noriginalcurtaxmny", "noriginalcurmny"
    };
    java.util.HashMap hsrow = SOToolsBO_Client.getAnyValueSORow("so_apply_b",
        fieldnames, "pk_apply_b", (String[]) csourcebillbodyids
            .toArray(new String[csourcebillbodyids.size()]), null);
    if (hsrow != null && hsrow.size() > 0) {
      nc.vo.so.so001.SORowData row = null;
      for (int k = 0, loopk = bodys.length; k < loopk; k++) {
        if (bodys[k] == null && bodys[k].getCsourcebillbodyid() == null)
          continue;
        row = (nc.vo.so.so001.SORowData) hsrow.get(bodys[k]
            .getCsourcebillbodyid());
        if (row == null)
          continue;
        // bodys[k].setCcurrencytypeid(row.getString(1));
        bodys[k].setNdiscountrate(row.getUFDouble(2));
        bodys[k].setNitemdiscountrate(row.getUFDouble(3));
        bodys[k].setNexchangeotobrate(row.getUFDouble(4));
//        bodys[k].setNexchangeotoarate(row.getUFDouble(5));
        bodys[k].setNdiscountmny(row.getUFDouble(5));
        bodys[k].setNtaxrate(row.getUFDouble(6));
        bodys[k].setNoriginalcurprice(row.getUFDouble(7));
        bodys[k].setNoriginalcurtaxprice(row.getUFDouble(8));
        bodys[k].setNoriginalcurnetprice(row.getUFDouble(9));
        bodys[k].setNoriginalcurtaxnetprice(row.getUFDouble(10));
        bodys[k].setNprice(row.getUFDouble(11));
        bodys[k].setNtaxprice(row.getUFDouble(12));
        bodys[k].setNnetprice(row.getUFDouble(13));
        bodys[k].setNtaxnetprice(row.getUFDouble(14));
        bodys[k].setNquotenetprice(row.getUFDouble(13));
        bodys[k].setNquoteprice(row.getUFDouble(11));
        bodys[k].setNquotetaxnetprice(row.getUFDouble(14));
        bodys[k].setNquotetaxprice(row.getUFDouble(12));
        bodys[k].setNquoteoriginalcurnetprice(row.getUFDouble(15));
        bodys[k].setNquoteoriginalcurprice(row.getUFDouble(16));
        bodys[k].setNquoteoriginalcurtaxprice(row.getUFDouble(18));
        // bodys[k].setNquoteoriginalcurtaxnetprice(row
        // .getUFDouble(17));
        bodys[k].setM_salequoteorgcurtaxnetprice(row.getUFDouble(17));

        bodys[k].setNsubquotenetprice(row.getUFDouble(13));
        bodys[k].setNsubquoteprice(row.getUFDouble(11));
        bodys[k].setNsubquotetaxnetprice(row.getUFDouble(14));
        bodys[k].setNsubquotetaxprice(row.getUFDouble(12));
        // bodys[k].setNsubsummny(row.getUFDouble(19));
        // bodys[k].setNsummny(row.getUFDouble(19));
        // bodys[k].setNoriginalcursummny(row.getUFDouble(20));
        bodys[k].setNsubtaxnetprice(row.getUFDouble(14));
        bodys[k].setCprolineid(row.getString(21));
        bodys[k].setNoriginalcurdiscountmny(row.getUFDouble(22));
        bodys[k].setNoriginalcurtaxmny(row.getUFDouble(23));
        // bodys[k].setNoriginalcurmny(row.getUFDouble(24));
        // yt add 2004-11-11
        bodys[k].setNsubcursummny(row.getUFDouble(19));
     
      }
    }

  }

  /**
   * 处理源头为销售订单的发票子VO
   * 
   * @param bvos
   * @return
   */
  private static void doSaleinvoiceBVOsBy30(UFBoolean BD302,
      SaleinvoiceBVO[] bodys, SaleVO head) throws Exception {
	  /**VO对照后续类已经进行了所有处理，不知道此处用意，去掉，如果有错误在后续类中完善**/
	  if(true)return;
	  
    ArrayList<String> csourcebillbodyids = new ArrayList<String>();
    for (int i = 0; i < bodys.length; i++) {
      csourcebillbodyids.add(bodys[i].getCsourcebillbodyid());
    }

    String[] fieldnames = new String[] {
        "corder_bid", "ccurrencytypeid", "ndiscountrate", "nitemdiscountrate",
        "nexchangeotobrate", "ndiscountmny", "ntaxrate",
        "noriginalcurprice", "noriginalcurtaxprice", "noriginalcurnetprice",
        "noriginalcurtaxnetprice", "nprice", "ntaxprice", "nnetprice",
        "ntaxnetprice", "norgqttaxprc", "norgqtprc", "norgqttaxnetprc",
        "norgqtnetprc", "nsummny", "noriginalcursummny", "cprolineid",
        "noriginalcurdiscountmny", "noriginalcurtaxmny", "noriginalcurmny",
        "ct_manageid", "nquoteunitrate", "nqttaxnetprc", "nqtnetprc",
        "nqttaxprc", "nqtprc"
    };
    java.util.HashMap hsrow = SOToolsBO_Client.getAnyValueSORow(
        "so_saleorder_b", fieldnames, "corder_bid",
        (String[]) csourcebillbodyids.toArray(new String[csourcebillbodyids
            .size()]), null);
    if (hsrow != null && hsrow.size() > 0) {
      nc.vo.so.so001.SORowData row = null;
      for (int k = 0, loopk = bodys.length; k < loopk; k++) {
        if (bodys[k] == null && bodys[k].getCsourcebillbodyid() == null)
          continue;
        row = (nc.vo.so.so001.SORowData) hsrow.get(bodys[k]
            .getCsourcebillbodyid());
        if (row == null)
          continue;
        bodys[k].setNdiscountrate(row.getUFDouble(2));

        bodys[k].setNitemdiscountrate(row.getUFDouble(3));
        // bodys[k].setCcurrencytypeid(row.getString(1));
        /*
         * 注：此处的折本折辅不知为何故，从取自订单被改为取自默认。由于没有注释，已无从考证。 //2007-1-29 wsy
         * 修改测试dw提的问题NC00119914，经wusp确认，改回取自订单。 //后续如有此类修改请标注原因。 wsy 2007-1-29
         */
        if (bodys[k].getCcurrencytypeid() != null
            && !bodys[k].getCcurrencytypeid().equals(row.getString(1))) {
          UFDouble currrates = new SOCurrencyRateUtil().getExchangeRate(bodys[k].getCcurrencytypeid(),
              head.getDbilldate().toString());
          bodys[k].setNexchangeotobrate(currrates);
//          bodys[k].setNexchangeotoarate(currrates[1]);
          bodys[k].setNtaxrate(row.getUFDouble(6));
          // setNquoteoriginalcurtaxnetprice
          bodys[k].setNquoteoriginalcurtaxprice(bodys[k]
              .getNquoteoriginalcurtaxnetprice().div(
                  bodys[k].getNdiscountrate()).multiply(100.0).div(
                  bodys[k].getNitemdiscountrate()).multiply(100.0));
        }
        else {

          bodys[k].setNexchangeotobrate(row.getUFDouble(4));
//          bodys[k].setNexchangeotoarate(row.getUFDouble(5));

          bodys[k].setNtaxrate(row.getUFDouble(6));
          bodys[k].setNoriginalcurprice(row.getUFDouble(7));
          bodys[k].setNoriginalcurtaxprice(row.getUFDouble(8));
          bodys[k].setNoriginalcurnetprice(row.getUFDouble(9));
          bodys[k].setNoriginalcurtaxnetprice(row.getUFDouble(10));
          bodys[k].setNprice(row.getUFDouble(11));
          bodys[k].setNtaxprice(row.getUFDouble(12));
          bodys[k].setNnetprice(row.getUFDouble(13));
          bodys[k].setNtaxnetprice(row.getUFDouble(14));
          bodys[k].setNquotenetprice(row.getUFDouble(28));
          bodys[k].setNquoteprice(row.getUFDouble(30));
          bodys[k].setNquotetaxnetprice(row.getUFDouble(27));
          bodys[k].setNquotetaxprice(row.getUFDouble(29));
          bodys[k].setNquoteoriginalcurnetprice(row.getUFDouble(18));
          bodys[k].setNquoteoriginalcurprice(row.getUFDouble(16));
          bodys[k].setNquoteoriginalcurtaxprice(row.getUFDouble(15));
          // bodys[k].setNquoteoriginalcurtaxnetprice(row
          // .getUFDouble(17));
          // 先存在一个变量里
          bodys[k].setM_salequoteorgcurtaxnetprice(row.getUFDouble(17));

          bodys[k].setNsubquotenetprice(row.getUFDouble(28));
		  bodys[k].setNsubquoteprice(row.getUFDouble(30));
		  bodys[k].setNsubquotetaxnetprice(row.getUFDouble(27));
		  bodys[k].setNsubquotetaxprice(row.getUFDouble(29));

					/** 使用净价代替单价计算 wangsenyang zhongwei* */
					if (bodys[k].getNtaxprice() != null) {
						// bodys[k].setNmny(bodys[k].getNprice().multiply(bodys[k].getNnumber()));
						bodys[k].setNmny(bodys[k].getNnetprice().multiply(
								bodys[k].getNnumber()));
						bodys[k].setNtaxmny(bodys[k].getNmny().multiply(
								bodys[k].getNtaxrate()).div(100.0));
						bodys[k].setNsummny(bodys[k].getNmny().add(
								bodys[k].getNtaxmny()));
					}

					bodys[k].setNsubtaxnetprice(row.getUFDouble(14));
					bodys[k].setCprolineid(row.getString(21));
					bodys[k].setNoriginalcurdiscountmny(row.getUFDouble(22));
					bodys[k].setNoriginalcurtaxmny(row.getUFDouble(23));

					if (bodys[k].getNoriginalcurprice() != null) {
						// bodys[k].setNoriginalcurmny(bodys[k].getNoriginalcurprice().multiply(bodys[k].getNnumber()));
						bodys[k].setNoriginalcurmny(bodys[k]
								.getNoriginalcurnetprice().multiply(
										bodys[k].getNnumber()));
						bodys[k].setNoriginalcurtaxmny(bodys[k]
								.getNoriginalcurmny().multiply(
										bodys[k].getNtaxrate()).div(100.0));
						bodys[k].setNoriginalcursummny(bodys[k]
								.getNoriginalcurmny().add(
										bodys[k].getNoriginalcurtaxmny()));
					}
					/** 使用净价代替单价计算 wangsenyang zhongwei* */
          
          bodys[k].setCt_manageid(row.getString(25));
          // yt add 2004-11-11
          bodys[k].setNsubsummny(bodys[k].getNoriginalcursummny());
          bodys[k].setNsubcursummny(bodys[k].getNsummny());
          bodys[k].setNdiscountmny(row.getUFDouble(5));
          
          //折扣额需要重新计算，算法同后台处理
          bodys[k].setNoriginalcurdiscountmny(SoVoTools.mult(bodys[k].getNoriginalcurtaxprice(), bodys[k].getNnumber()));
          bodys[k].setNoriginalcurdiscountmny(SoVoTools.sub(bodys[k].getNoriginalcurdiscountmny(), bodys[k].getNoriginalcursummny()));
          
//          bodys[k].setNassistcurdiscountmny(SoVoTools.mult(bodys[k].getNassistcurtaxprice(), bodys[k].getNnumber()));
//          bodys[k].setNassistcurdiscountmny(SoVoTools.sub(bodys[k].getNassistcurdiscountmny(), bodys[k].getNassistcursummny()));

          bodys[k].setNdiscountmny(SoVoTools.mult(bodys[k].getNtaxprice(),bodys[k].getNnumber()));
          bodys[k].setNdiscountmny(SoVoTools.sub(bodys[k].getNdiscountmny(),bodys[k].getNsummny()));

        }

      }
    }

  }

  public static BusinessCurrencyRateUtil getBusiCurrUtil() {
    if (currtype == null) {
      try {
        currtype = new BusinessCurrencyRateUtil(getLoginPk_Corp());
      }
      catch (Exception exp) {
        exp.printStackTrace();
        throw new BusinessRuntimeException(exp.getMessage());
      }

    }

    return currtype;
  }
  
  public static SOCurrencyRateUtil getSoBusiCurrUtil() {
	    if (m_socurrtype == null) {
	      try {
	        m_socurrtype = new SOCurrencyRateUtil(getLoginPk_Corp());
	      }
	      catch (Exception exp) {
	        exp.printStackTrace();
	        throw new BusinessRuntimeException(exp.getMessage());
	      }

	    }

	    return m_socurrtype;
	  }

  /**
   * 获取自定义项
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @param pkcorp
   *          <p>
   * @author wangyf
   * @time 2007-3-7 下午02:13:23
   */
  static {
    

  }

  public static DefVO[] getBody_defs() {
    return body_defs;
  }

  public static DefVO[] getHead_defs() {
    return head_defs;
  }

  /**
   * 获取登陆日期
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @return 公司ID
   *         <p>
   * @author wangyf
   * @time 2007-3-7 下午02:15:12
   */
  public static UFDate getLoginDate() {
    return ClientEnvironment.getInstance().getDate();
  }

  /**
   * 获取登陆公司主键
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @return 公司ID
   *         <p>
   * @author wangyf
   * @time 2007-3-7 下午02:15:12
   */
  public static String getLoginPk_Corp() {
    return ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
  }

  /**
   * 获取登陆用户ID
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * 
   * @return 公司ID
   *         <p>
   * @author wangyf
   * @time 2007-3-7 下午02:15:12
   */
  public static String getLoginUserId() {
    return ClientEnvironment.getInstance().getUser().getPrimaryKey();
  }

  /**
   * 根据一个发票VO，得到其对冲发票VO
   * <p>
   * <b>examples:</b>
   * <p>
   * 使用示例
   * <p>
   * <b>参数说明</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-19 下午04:31:54
   */
  public static SaleinvoiceVO getOppVO(SaleinvoiceVO voOld)
      throws BusinessException {

    if (voOld == null)
      return null;
    SaleVO head = (SaleVO) voOld.getParentVO();
    if (head != null
        && (voOld.getChildrenVO() == null || voOld.getChildrenVO().length == 0)) {
      SaleinvoiceBVO[] bodys = null;
      try {
        bodys = SaleinvoiceBO_Client.queryBodyData(head.getCsaleid());
        voOld.setChildrenVO(bodys);
      }
      catch (Exception e) {
        nc.vo.scm.pub.SCMEnv.out(e.getMessage());
      }
    }
    // 检查单据是否已经修改，通过时间戳
    //
    try {
      SaleVO salehvo = SaleinvoiceBO_Client.queryHeadData(head.getCsaleid());
      if (salehvo != null) {
        if (head.getTs() != null && !head.getTs().equals(salehvo.getTs())) {
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("sopub", "UPPsopub-000315"));
          // "发票已经发生修改，请重新查询后再进行对冲！");
        }
      }
    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
    //
    // 单据状态必须是审批。单据对冲标志必须是0-正常。单据没有参加冲应收（直接查询单据是否在冲应收的执行表中存在记录－为效率充应收执行表应增加发票ID和发票子表ID的索引）
    //
    boolean bstrikeflag = false;
    if (head.getNstrikemny() != null && head.getNstrikemny().doubleValue() == 0)
      bstrikeflag = false;
    else {
      bstrikeflag = true;
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "sopub", "UPPsopub-000312"));
      // "合并开票的单据不能对冲");
    }

    if (head.getFcounteractflag() != null
        && head.getFcounteractflag().intValue() != SaleVO.FCOUNTERACTFLAG_NORMAL) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "sopub", "UPPsopub-000313"));
      // "对冲标记为正常的单据才能对冲！");
    }
    if (head.getFstatus() != null
        && head.getFstatus().intValue() != BillStatus.AUDIT) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "sopub", "UPPsopub-000314"));
      // "审批的单据才能对冲！");
    }

    // 2- 发票VO转换成对冲类型单据VO
    // 所有数量，金额列变成加一个负号
    // 来源单据类型变成发票自己，来源单据ID变成对应的原发票ID
    // 对冲标记设置为2-因对冲生成
    // 清空单据ID，单据号
    SaleinvoiceVO newvo = SaleinvoiceVO.convToOppVO(voOld, ClientEnvironment
        .getInstance().getBusinessDate());

    SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[]) newvo.getChildrenVO();
    newvo.getHeadVO().setCoperatorid(ClientEnvironment.getInstance().getUser().getPrimaryKey());
    newvo.getHeadVO().setDbilldate(ClientEnvironment.getInstance().getDate());
    newvo.getHeadVO().setDmakedate(ClientEnvironment.getInstance().getDate());
    newvo.getHeadVO().setDbilltime(null);
    newvo.getHeadVO().setDmoditime(null);
    newvo.getHeadVO().setDaudittime(null);

    nc.vo.so.so016.SoVoTools
        .execFormulas(
            new String[] {
              "discountflag->getColValue(bd_invbasdoc,discountflag,pk_invbasdoc,cinvbasdocid)"
            }, bodys);
    newvo.setChildrenVO(bodys);

    return newvo;

  }

  /**
   * 初始化自由项(换算率)。 创建日期：(2001-10-9 13:05:04)
   */
  public static void initFreeItem(CircularlyAccessibleValueObject[] bodyvos,
      BillModel billModel) {
    if (bodyvos == null) {
      return;
    }
    try {
      nc.ui.scm.pub.FreeVOParse freeVOParse = new nc.ui.scm.pub.FreeVOParse();
      freeVOParse.setFreeVO(bodyvos, "vfree0", "vfree", "cinvbasdocid",
          "cinventoryid", false);
      if (bodyvos != null && bodyvos.length > 0) {
        Object oTemp = null;
        for (int i = 0; i < bodyvos.length; i++) {
          oTemp = bodyvos[i].getAttributeValue("vfree0");
          if (oTemp != null && oTemp.toString().trim().length() > 0) {
            billModel.setValueAt(oTemp, i, "vfree0");
          }
        }
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
      nc.vo.scm.pub.SCMEnv.out("自由项设置失败!");
    }
  }

  /**
   * ?user> 功能：根据存货基本档案，如果是折扣类或劳务类均返回TRUE，否则返回FALSE 参数：存货基本档案 返回：boolean 例外：
   * 日期：(2004-7-2 14:23:34) 修改日期，修改人，修改原因，注释标志：
   * 
   * @return boolean
   * @param cinvbasdocid
   *          java.lang.String
   */
  public static boolean isLaborOrDiscount(String cinvbasdocid) {
    try {
      UFBoolean isDiscount = new UFBoolean(((Object[]) nc.ui.scm.pub.CacheTool
          .getCellValue("bd_invbasdoc", "pk_invbasdoc", "discountflag",
              cinvbasdocid))[0].toString());
      UFBoolean isLabor = new UFBoolean(((Object[]) nc.ui.scm.pub.CacheTool
          .getCellValue("bd_invbasdoc", "pk_invbasdoc", "laborflag",
              cinvbasdocid))[0].toString());
      if (isDiscount.booleanValue() || isLabor.booleanValue())
        return true;
      return false;
    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
    return true;
  }

  /**
   * 方法功能描述：新增销售发票分单条件
   * 销售发票参照销售出库单时依据SO76分单；
   * 销售发票参照销售订单时依据SO60分单。
   * <b>参数说明</b>
   * @param arySourceVO
   * @return
   * @author fengjb
   * @time 2008-8-6 下午01:48:37
   */
  public SaleinvoiceVO[] mergeSourceVOs(SaleinvoiceVO[] arySourceVO) {
    // 分单依据：主表客户，销售组织，附表币种
    SaleinvoiceVO[] aryRetVO = null;
    String[] sHeaditems = null;
    String[] sBodyitems = null;
   //是否允许同一客户下开票单位不同的销售订单开票
    if (SO_30.booleanValue())
      sHeaditems = new String[] {
        "csalecorpid"
      };
    else
      sHeaditems = new String[] {
          "creceiptcorpid", "csalecorpid" , "pk_contract" , "iscredit" , "vouchid" , /* 添加拆分规则，增加按合同拆分的维度  add by river for 2012-11-16 */
      };
    
    String sUpSrcType = arySourceVO[0].getItemVOs()[0].getCupreceipttype();
    //来源销售订单
    if(SaleBillType.SaleOrder.equals(sUpSrcType)){
    if (SO_60.booleanValue() && SO_27.booleanValue())
      sBodyitems = new String[] {
          "ccurrencytypeid", "ccustomerid", "cprolineid"
      };
    else if (SO_60.booleanValue())
      sBodyitems = new String[] {
          "ccurrencytypeid", "ccustomerid"
      };
    else if (SO_27.booleanValue())
      sBodyitems = new String[] {
          "ccurrencytypeid", "cprolineid"
      };
    else
      sBodyitems = new String[] {
        "ccurrencytypeid"
      };
    aryRetVO = (SaleinvoiceVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
    .getSplitVOs("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO",
        "nc.vo.so.so002.SaleinvoiceBVO", arySourceVO, sHeaditems,
        sBodyitems);
    //来源销售出库单
    }else if(SaleBillType.SaleOutStore.equals(sUpSrcType)){
      //分单条件
      SaleDispartVO[] thisSplitCon = getSplitCondition();
    	ArrayList<String> bodykey = new ArrayList<String>();
      //是否选择“实际出库日期时距”的分单条件
	    boolean isoutstack = false;
	    UFDouble limit = new UFDouble(0);
      
    for(SaleDispartVO thiscon:thisSplitCon){
		// 订单客户
      if("ccustomerid".equals(thiscon.getDispartkey())){
		if(thiscon.getBdefault().booleanValue() && SO_27.booleanValue()){
			bodykey.add("ccurrencytypeid");
			bodykey.add("ccustomerid");
			bodykey.add("cprolineid");
		}else if(thiscon.getBdefault().booleanValue()){
			bodykey.add("ccurrencytypeid");
			bodykey.add("ccustomerid");
		}else if(SO_27.booleanValue()){
			bodykey.add("ccurrencytypeid");
			bodykey.add("cprolineid");
		}else{
			bodykey.add("ccurrencytypeid");
		}
    // 出库单号
      }else if("cupsourcebillcode".equals(thiscon.getDispartkey())){

		if(thiscon.getBdefault().booleanValue())
			bodykey.add("cupsourcebillcode");
		
      }else if("d4cbizdate".equals(thiscon.getDispartkey())){
		// 实际出库日期
		if(thiscon.getBdefault().booleanValue())
			bodykey.add("d4cbizdate");
		
      }else if("datefromto".equals(thiscon.getDispartkey())){
		// 实际出库单日期时距
		if(thiscon.getBdefault().booleanValue() && thiscon.getValue() != null){
			isoutstack = true;
			limit =  thiscon.getValue();
	     	}
       }
    }
		if(bodykey.size()>0){
			sBodyitems = new String[bodykey.size()];
			bodykey.toArray(sBodyitems);
		    aryRetVO = (SaleinvoiceVO[]) nc.vo.scm.pub.vosplit.SplitBillVOs
			    .getSplitVOs("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO",
			        "nc.vo.so.so002.SaleinvoiceBVO", arySourceVO, sHeaditems,
			        sBodyitems);
		}
		if(isoutstack){
			aryRetVO = (SaleinvoiceVO[])nc.vo.scm.pub.vosplit.SplitBillVOs
			    .getSplitVOsByDate("nc.vo.so.so002.SaleinvoiceVO", "nc.vo.so.so002.SaleVO",
			        "nc.vo.so.so002.SaleinvoiceBVO",aryRetVO,"d4cbizdate",limit.intValue());
		}
		
    }

    SaleinvoiceVO[] aryCloneVOs = aryRetVO;
    if (aryRetVO.length > 1) {
      try {

        aryCloneVOs = (SaleinvoiceVO[]) nc.vo.scm.pub.smart.ObjectUtils
            .serializableClone(aryRetVO);
      }
      catch (Exception ex) {
        SCMEnv.out(ex);
      }
    }
    return aryCloneVOs;
  }

  /**
   * 获得节点号。 创建日期：(2001-11-27 13:51:07)
   * 
   * @return java.lang.String
   */
  public static String getNodeCode() {
    return "40060501";// or 40060501
  }
  
  /*
   * 根据精度要求,对数据重新处理
   * 2007-12-04 xhq
   */
  public static SaleinvoiceVO retrimDecimal(SaleinvoiceVO VO, SaleInvoiceCardPanel cardPanel){
	  
	  int nPriceDecimal = 2;//价格
	  int nCurMnyDecimal = 2;//原币金额
	  int nMnyDecimal = 2;//本币金额
	
	  BillItem item = cardPanel.getBodyItem("noriginalcurprice");
	  if(item != null) nPriceDecimal = item.getDecimalDigits();
	  
	  item = cardPanel.getBodyItem("noriginalcurmny");
	  if(item != null) nCurMnyDecimal = item.getDecimalDigits();
	  
	  item = cardPanel.getBodyItem("nmny");
	  if(item != null) nMnyDecimal = item.getDecimalDigits();

	  SaleinvoiceBVO bodyVO[] = VO.getBodyVO();
	  for(int i = 0; i < bodyVO.length; i++){
		  if(bodyVO[i].getNoriginalcurnetprice() != null && Math.abs(bodyVO[i].getNoriginalcurnetprice().doubleValue()) < getRoundDouble(nPriceDecimal)) bodyVO[i].setNoriginalcurnetprice(new UFDouble(0));
		  if(bodyVO[i].getNoriginalcurprice() != null && Math.abs(bodyVO[i].getNoriginalcurprice().doubleValue()) < getRoundDouble(nPriceDecimal)) bodyVO[i].setNoriginalcurprice(new UFDouble(0));
		  if(bodyVO[i].getNoriginalcurtaxnetprice() != null && Math.abs(bodyVO[i].getNoriginalcurtaxnetprice().doubleValue()) < getRoundDouble(nPriceDecimal)) bodyVO[i].setNoriginalcurtaxnetprice(new UFDouble(0));
		  if(bodyVO[i].getNoriginalcurtaxprice() != null && Math.abs(bodyVO[i].getNoriginalcurtaxprice().doubleValue()) < getRoundDouble(nPriceDecimal)) bodyVO[i].setNoriginalcurtaxprice(new UFDouble(0));

		  if(bodyVO[i].getNoriginalcurmny() != null && Math.abs(bodyVO[i].getNoriginalcurmny().doubleValue()) < getRoundDouble(nCurMnyDecimal)) bodyVO[i].setNoriginalcurmny(new UFDouble(0));
		  if(bodyVO[i].getNoriginalcurtaxmny() != null && Math.abs(bodyVO[i].getNoriginalcurtaxmny().doubleValue()) < getRoundDouble(nCurMnyDecimal)) bodyVO[i].setNoriginalcurtaxmny(new UFDouble(0));
		  if(bodyVO[i].getNoriginalcursummny() != null && Math.abs(bodyVO[i].getNoriginalcursummny().doubleValue()) < getRoundDouble(nCurMnyDecimal)) bodyVO[i].setNoriginalcursummny(new UFDouble(0));
		  if(bodyVO[i].getNoriginalcurdiscountmny() != null && Math.abs(bodyVO[i].getNoriginalcurdiscountmny().doubleValue()) < getRoundDouble(nCurMnyDecimal)) bodyVO[i].setNoriginalcurdiscountmny(new UFDouble(0));
		  if(bodyVO[i].getNsubcursummny() != null && Math.abs(bodyVO[i].getNsubcursummny().doubleValue()) < getRoundDouble(nCurMnyDecimal)) bodyVO[i].setNsubcursummny(new UFDouble(0));

		  if(bodyVO[i].getNmny() != null && Math.abs(bodyVO[i].getNmny().doubleValue()) < getRoundDouble(nMnyDecimal)) bodyVO[i].setNmny(new UFDouble(0));
		  if(bodyVO[i].getNtaxmny() != null && Math.abs(bodyVO[i].getNtaxmny().doubleValue()) < getRoundDouble(nMnyDecimal)) bodyVO[i].setNtaxmny(new UFDouble(0));
		  if(bodyVO[i].getNsummny() != null && Math.abs(bodyVO[i].getNsummny().doubleValue()) < getRoundDouble(nMnyDecimal)) bodyVO[i].setNsummny(new UFDouble(0));
		  if(bodyVO[i].getNdiscountmny() != null && Math.abs(bodyVO[i].getNdiscountmny().doubleValue()) < getRoundDouble(nMnyDecimal)) bodyVO[i].setNdiscountmny(new UFDouble(0));
		  if(bodyVO[i].getNsubsummny() != null && Math.abs(bodyVO[i].getNsubsummny().doubleValue()) < getRoundDouble(nMnyDecimal)) bodyVO[i].setNsubsummny(new UFDouble(0));
	  }
	  
	  VO.setChildrenVO(bodyVO);	  
	  return VO;
  }

  private static double getRoundDouble(int n){
	  double d = 1.0;
	  for(int i = 0; i < n; i++) d = d / 10;
	  return d;
  }
  /**
   * 方法功能描述：获得客户相关信息。
   * <b>参数说明</b>
   * @param custID
   * @return
   * @time 2008-12-15 下午01:45:24
   */
  public static String[][] getCustInfo(String custID){
		if(null != m_hCustInfo.get(custID)) 
      return (String[][])m_hCustInfo.get(custID);
    else 
      return null;
	  }
	  
  public static void setCustInfo(String custID, String results[][]){
	  m_hCustInfo.put(custID, results);
  }
  
  public static String[] getInvoiceItems_Mny() {
    return new String[] {
        "noriginalcurtaxmny", "noriginalcurmny", "noriginalcursummny",
        "noriginalcurdiscountmny", "ntaxmny", "nmny", "nsummny",
        "ndiscountmny", "nsubsummny", "nuniteinvoicemny", "ncostmny",
        "ntotalpaymny"};
  }

  /**
   * 发票和本币、辅币相关的不可编辑项 jianghp+ 20080522
   * @return
   * V55 本币价税合计和无税金额可编辑
   */
	public static String[] getSaleOrderItems_Price_Mny_NoOriginal() {
		return new String[] { "nsubquoteprice", "nsubquotetaxprice",
				"nsubquotenetprice", "nsubquotetaxnetprice", "nsubtaxnetprice",
				"nquoteprice", "nquotetaxprice", "nquotenetprice",
				"nquotetaxnetprice", "nprice", "ntaxprice",
				"nnetprice", "ntaxnetprice", "ntaxmny", 
        "ndiscountmny", "nsubcursummny" };
	}
}
