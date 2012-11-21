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
		
		// ������ȡ
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

	    // ������ȡ��ʼ����
	    if (hsparas == null) {
	      // ������ȡ
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

	    // ��ȡ����
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
	    	SO_40 = "�����ۿ�";
	    else
	    	SO_40 = svalue;

	    // ���ۺ�˰ͬʱ���Ǽ��ź͹�˾

	    svalue = (String) hsparas.get("SA02");
	    if (svalue == null || svalue.trim().length() <= 0)
	      SA_02 = UFBoolean.FALSE;
	    else
	      SA_02 = new UFBoolean(svalue.trim());

	    String SA13 = (String) hsparas.get("SA13");
	    if ("���Ŷ���".equals(SA13)) {/*-=notranslate=-*/

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
  private static BusinessCurrencyRateUtil currtype = null; // ����
  
  private static SOCurrencyRateUtil m_socurrtype = null; 

//  private static SaleInvoiceTools m_tools = null;

  // ������ϣ��
  private java.util.Hashtable hsparas = null;

  // �����Զ������ͷ
  private static DefVO[] head_defs = null;

  // �����Զ��������
  private static DefVO[] body_defs = null;

  // �۸����
  public UFBoolean SA_02 = null; // ���ۺ�˰

  public UFBoolean SA_08 = null; // ��Ʊ���ۿɷ����

  public UFBoolean SA_15 = null; // �Ƿ�ѯ��

  // ���۷�Ʊ�Ƿ��ն����ͻ��ֵ�
  public UFBoolean SO_60 = UFBoolean.TRUE;

  // yt add 2004-12-19
  // �����е�Ĭ������
  public UFDouble SO_34 = new UFDouble(0.0);

  public UFBoolean SO_36 = UFBoolean.TRUE;

  public UFBoolean SO_49 = UFBoolean.FALSE;

  // �ϲ���Ʊ���� add 2004-03-15
  public UFBoolean SO_27 = null;// �Ƿ��ղ�Ʒ�߽��кϲ���Ʊ

  // yt add 2004-03-26
  public UFBoolean SO_30 = null; // �Ƿ�����ͬһ�ͻ��¿�Ʊ��λ��ͬ�����۶�����Ʊ

  public UFDouble SO_22 = new UFDouble(0);

  public UFBoolean SO_20 = new UFBoolean("Y"); // ���۹����Ƿ�ʹ��ͨ��ģ��
  
  public String SO_40 = null;//������˰�ϼơ���˰���ۡ���˰����˰����ʱ�����ۿۻ��ǵ���
  
  public UFBoolean SO_59 = null;
  // fengjb add 2008-08-06
  public SaleDispartVO[]  SO_76 = null;//���ճ��ⵥ��Ʊ��Ĭ�Ϸֵ���ʽ
  
  public Integer SO_67 = null;//��Ʊ����
  
  
  //add by fengjb 20080829 ����ķֵ��Ի�����ȷ��ʱ�ķֵ�����
  private SaleDispartVO[] m_splitcondition = null;
  
  // ��������
  public UFBoolean BD_302 = null; // С��λ��

  public Integer BD_501 = null; // ����������С��λ��

  public Integer BD_502 = null; // ����������С��λ��

  public Integer BD_503 = null; // ������

  public Integer BD_505 = null; // ����С��λ��
  
  public UFBoolean SO_41 = null;//�����޸ĺ��Ƿ��޸��Ƶ���

  public static final String sAll = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40069907", "UPT40069907-000001")/* @res "ȫ��" */;

  public static final String sReturnPrif = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40060501", "UPP40060501-000139")/* @res "������" */;

  public static final String sSafePrice = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40060501", "UPP40060501-000138")/* @res "�۱���" */;

  public static final String sSelfMake = nc.ui.ml.NCLangRes.getInstance()
      .getStrByID("40060501", "UPP40060501-000140")/* @res "�ֹ�¼��" */;

  //�ͻ���Ϣ20071214
  private static HashMap<String,String[][]> m_hCustInfo = new HashMap<String,String[][]>();
  
  public String BD_301 = null;//��λ��
  
  private nc.vo.scm.pub.session.ClientLink m_Cl = null;
  
  
  /**
   * ������������������ֵ����������ֵ��������沢�ҵ���ˡ�ȷ����"����ΪĬ��"��ť��ʱ�����õ�ǰ�����ϵķֵ�����ֵ��
   * <b>����˵��</b>
   * @author fengjb
   * @time 2008-8-29 ����03:45:35
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
   * ���۳��ⵥ->���۷�Ʊʱ����Ʊȡ���۳��ⵥ�����ݣ�������Դ������3U����32��ȡ�˻����뵥�����۶�����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param saleinvoice
   *          ��ƱVO
   * @return
   *          <p>
   * @author wangyf
   * @time 2007-8-6 ����01:17:45
   */
  public SaleinvoiceVO getDataFrom4CTo32(SaleinvoiceVO saleinvoice) {

    SaleinvoiceBVO[] bodys = (SaleinvoiceBVO[]) saleinvoice.getChildrenVO();
    SaleVO head = (SaleVO) saleinvoice.getParentVO();
    try {
      // �������������
      doSaleinvoiceBVOsByIC(bodys);
      // ��ֵ��ݣ���Դͷ��������
      SaleinvoiceBVO[][] vos = (SaleinvoiceBVO[][]) nc.vo.scm.pub.vosplit.SplitBillVOs
          .getSplitVOs(bodys, new String[] {
            "creceipttype"
          });
      // ArrayList al3U= new ArrayList();
      // ArrayList al30= new ArrayList();
      // ArrayList alOther= new ArrayList();
      SaleinvoiceBVO[] btmp = null; // ��ʱ
      // �����������
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
      // �ϲ�VO
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
   * ���������Դ�ڳ��ⵥ����Ϣ:ȡ��ؼ۸���Ϣ
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

        // �ȴ���һ��������
        bodys[k].setM_outquoteorgcurtaxnetprice(row.getUFDouble(0));

      }
    }
  }

  /**
   * ����ԴͷΪ�˻����뵥�ķ�Ʊ��VO
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
   * ����ԴͷΪ���۶����ķ�Ʊ��VO
   * 
   * @param bvos
   * @return
   */
  private static void doSaleinvoiceBVOsBy30(UFBoolean BD302,
      SaleinvoiceBVO[] bodys, SaleVO head) throws Exception {
	  /**VO���պ������Ѿ����������д�����֪���˴����⣬ȥ��������д����ں�����������**/
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
         * ע���˴����۱��۸���֪Ϊ�ιʣ���ȡ�Զ�������Ϊȡ��Ĭ�ϡ�����û��ע�ͣ����޴ӿ�֤�� //2007-1-29 wsy
         * �޸Ĳ���dw�������NC00119914����wuspȷ�ϣ��Ļ�ȡ�Զ����� //�������д����޸����עԭ�� wsy 2007-1-29
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
          // �ȴ���һ��������
          bodys[k].setM_salequoteorgcurtaxnetprice(row.getUFDouble(17));

          bodys[k].setNsubquotenetprice(row.getUFDouble(28));
		  bodys[k].setNsubquoteprice(row.getUFDouble(30));
		  bodys[k].setNsubquotetaxnetprice(row.getUFDouble(27));
		  bodys[k].setNsubquotetaxprice(row.getUFDouble(29));

					/** ʹ�þ��۴��浥�ۼ��� wangsenyang zhongwei* */
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
					/** ʹ�þ��۴��浥�ۼ��� wangsenyang zhongwei* */
          
          bodys[k].setCt_manageid(row.getString(25));
          // yt add 2004-11-11
          bodys[k].setNsubsummny(bodys[k].getNoriginalcursummny());
          bodys[k].setNsubcursummny(bodys[k].getNsummny());
          bodys[k].setNdiscountmny(row.getUFDouble(5));
          
          //�ۿ۶���Ҫ���¼��㣬�㷨ͬ��̨����
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
   * ��ȡ�Զ�����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @param pkcorp
   *          <p>
   * @author wangyf
   * @time 2007-3-7 ����02:13:23
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
   * ��ȡ��½����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @return ��˾ID
   *         <p>
   * @author wangyf
   * @time 2007-3-7 ����02:15:12
   */
  public static UFDate getLoginDate() {
    return ClientEnvironment.getInstance().getDate();
  }

  /**
   * ��ȡ��½��˾����
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @return ��˾ID
   *         <p>
   * @author wangyf
   * @time 2007-3-7 ����02:15:12
   */
  public static String getLoginPk_Corp() {
    return ClientEnvironment.getInstance().getCorporation().getPrimaryKey();
  }

  /**
   * ��ȡ��½�û�ID
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @return ��˾ID
   *         <p>
   * @author wangyf
   * @time 2007-3-7 ����02:15:12
   */
  public static String getLoginUserId() {
    return ClientEnvironment.getInstance().getUser().getPrimaryKey();
  }

  /**
   * ����һ����ƱVO���õ���Գ巢ƱVO
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * <p>
   * 
   * @author wangyf
   * @time 2007-3-19 ����04:31:54
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
    // ��鵥���Ƿ��Ѿ��޸ģ�ͨ��ʱ���
    //
    try {
      SaleVO salehvo = SaleinvoiceBO_Client.queryHeadData(head.getCsaleid());
      if (salehvo != null) {
        if (head.getTs() != null && !head.getTs().equals(salehvo.getTs())) {
          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
              .getStrByID("sopub", "UPPsopub-000315"));
          // "��Ʊ�Ѿ������޸ģ������²�ѯ���ٽ��жԳ壡");
        }
      }
    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.out(e.getMessage());
    }
    //
    // ����״̬���������������ݶԳ��־������0-����������û�вμӳ�Ӧ�գ�ֱ�Ӳ�ѯ�����Ƿ��ڳ�Ӧ�յ�ִ�б��д��ڼ�¼��ΪЧ�ʳ�Ӧ��ִ�б�Ӧ���ӷ�ƱID�ͷ�Ʊ�ӱ�ID��������
    //
    boolean bstrikeflag = false;
    if (head.getNstrikemny() != null && head.getNstrikemny().doubleValue() == 0)
      bstrikeflag = false;
    else {
      bstrikeflag = true;
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "sopub", "UPPsopub-000312"));
      // "�ϲ���Ʊ�ĵ��ݲ��ܶԳ�");
    }

    if (head.getFcounteractflag() != null
        && head.getFcounteractflag().intValue() != SaleVO.FCOUNTERACTFLAG_NORMAL) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "sopub", "UPPsopub-000313"));
      // "�Գ���Ϊ�����ĵ��ݲ��ܶԳ壡");
    }
    if (head.getFstatus() != null
        && head.getFstatus().intValue() != BillStatus.AUDIT) {
      throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID(
          "sopub", "UPPsopub-000314"));
      // "�����ĵ��ݲ��ܶԳ壡");
    }

    // 2- ��ƱVOת���ɶԳ����͵���VO
    // ��������������б�ɼ�һ������
    // ��Դ�������ͱ�ɷ�Ʊ�Լ�����Դ����ID��ɶ�Ӧ��ԭ��ƱID
    // �Գ�������Ϊ2-��Գ�����
    // ��յ���ID�����ݺ�
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
   * ��ʼ��������(������)�� �������ڣ�(2001-10-9 13:05:04)
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
      nc.vo.scm.pub.SCMEnv.out("����������ʧ��!");
    }
  }

  /**
   * ?user> ���ܣ����ݴ������������������ۿ���������������TRUE�����򷵻�FALSE ����������������� ���أ�boolean ���⣺
   * ���ڣ�(2004-7-2 14:23:34) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
   * ���������������������۷�Ʊ�ֵ�����
   * ���۷�Ʊ�������۳��ⵥʱ����SO76�ֵ���
   * ���۷�Ʊ�������۶���ʱ����SO60�ֵ���
   * <b>����˵��</b>
   * @param arySourceVO
   * @return
   * @author fengjb
   * @time 2008-8-6 ����01:48:37
   */
  public SaleinvoiceVO[] mergeSourceVOs(SaleinvoiceVO[] arySourceVO) {
    // �ֵ����ݣ�����ͻ���������֯���������
    SaleinvoiceVO[] aryRetVO = null;
    String[] sHeaditems = null;
    String[] sBodyitems = null;
   //�Ƿ�����ͬһ�ͻ��¿�Ʊ��λ��ͬ�����۶�����Ʊ
    if (SO_30.booleanValue())
      sHeaditems = new String[] {
        "csalecorpid"
      };
    else
      sHeaditems = new String[] {
          "creceiptcorpid", "csalecorpid" , "pk_contract" , "iscredit" , "vouchid" , /* ��Ӳ�ֹ������Ӱ���ͬ��ֵ�ά��  add by river for 2012-11-16 */
      };
    
    String sUpSrcType = arySourceVO[0].getItemVOs()[0].getCupreceipttype();
    //��Դ���۶���
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
    //��Դ���۳��ⵥ
    }else if(SaleBillType.SaleOutStore.equals(sUpSrcType)){
      //�ֵ�����
      SaleDispartVO[] thisSplitCon = getSplitCondition();
    	ArrayList<String> bodykey = new ArrayList<String>();
      //�Ƿ�ѡ��ʵ�ʳ�������ʱ�ࡱ�ķֵ�����
	    boolean isoutstack = false;
	    UFDouble limit = new UFDouble(0);
      
    for(SaleDispartVO thiscon:thisSplitCon){
		// �����ͻ�
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
    // ���ⵥ��
      }else if("cupsourcebillcode".equals(thiscon.getDispartkey())){

		if(thiscon.getBdefault().booleanValue())
			bodykey.add("cupsourcebillcode");
		
      }else if("d4cbizdate".equals(thiscon.getDispartkey())){
		// ʵ�ʳ�������
		if(thiscon.getBdefault().booleanValue())
			bodykey.add("d4cbizdate");
		
      }else if("datefromto".equals(thiscon.getDispartkey())){
		// ʵ�ʳ��ⵥ����ʱ��
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
   * ��ýڵ�š� �������ڣ�(2001-11-27 13:51:07)
   * 
   * @return java.lang.String
   */
  public static String getNodeCode() {
    return "40060501";// or 40060501
  }
  
  /*
   * ���ݾ���Ҫ��,���������´���
   * 2007-12-04 xhq
   */
  public static SaleinvoiceVO retrimDecimal(SaleinvoiceVO VO, SaleInvoiceCardPanel cardPanel){
	  
	  int nPriceDecimal = 2;//�۸�
	  int nCurMnyDecimal = 2;//ԭ�ҽ��
	  int nMnyDecimal = 2;//���ҽ��
	
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
   * ����������������ÿͻ������Ϣ��
   * <b>����˵��</b>
   * @param custID
   * @return
   * @time 2008-12-15 ����01:45:24
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
   * ��Ʊ�ͱ��ҡ�������صĲ��ɱ༭�� jianghp+ 20080522
   * @return
   * V55 ���Ҽ�˰�ϼƺ���˰���ɱ༭
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
