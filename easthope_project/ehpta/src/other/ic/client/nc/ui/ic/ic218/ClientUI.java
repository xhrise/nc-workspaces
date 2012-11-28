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
 * �˴���������˵����
 * �������ڣ�(2004-2-19 15:34:51)
 * @author��֣��ɭ
 * 
 */
public class ClientUI extends nc.ui.ic.pub.bill.GeneralBillClientUI {
  //
  //	//����������������
  //	ButtonObject m_boAdd5C;
  //	//���չ�˾���������
  //	ButtonObject m_boAdd5D;
  //	//������֯���������
  //	ButtonObject m_boAdd5E;
  //	ButtonObject m_boAdd5I;
  //
  //	//��ť
  //	ButtonObject m_boAddSelf;
  private FactoryLoginDialog m_DlgFactory;

  ////���շ�����
  //ButtonObject m_boAdd7D;
  ////���շ��˵�
  //ButtonObject m_boAdd7F;

  //���ݵ����������նԻ���

  //��½�Ŀ����֯
  private java.lang.String m_sCalbodyid;

  private String m_sErr = null;

  //	//�޸ĵ�����˾����֯
  //	ButtonObject m_boModifySettlePath;
  ModifyCorpDlg m_dlgModifyCorp;

  SettlePathDlgForIC m_dlgModifySettlePath;

	private IButtonManager m_buttonManager;

  /**
   * ClientUI ������ע�⡣
   */
  public ClientUI() {
    super();
    initialize();

  }
  
  /**
   * ClientUI ������ע�⡣
   * add by liuzy 2007-12-18 ���ݽڵ�����ʼ������ģ��
   */
  public ClientUI(FramePanel fp) {
   super(fp);
   initialize();
  }

  /**
   * ClientUI ������ע�⡣
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
	  		
	  		((UIRefPane)((BillItem)e.getSource()).getComponent()).setWhereString(" bd_jobbasfil.pk_jobtype = (select pk_jobtype from bd_jobtype where jobtypename like '����%' and nvl(dr,0)=0)  ");
	  		
	  	}
	  
		return super.beforeEdit(e);
	}
  
  /**
   * river 
   * 
   * ��������
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
   * �����ߣ����˾�
   * ���ܣ����ݱ༭����
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   */
  protected void afterBillEdit(nc.ui.pub.bill.BillEditEvent e) {
    if (e.getKey().equals("cotherwhid")) {
      //�����ֿ�
      String sName = ((nc.ui.pub.beans.UIRefPane) getBillCardPanel()
          .getHeadItem("cotherwhid").getComponent()).getRefName();
      //�������������б���ʽ����ʾ��
      if (getM_voBill() != null)
        getM_voBill().setHeaderValue("cotherwhname", sName);
    }
  }

  /**
   * �����ߣ����˾�
   * ���ܣ���������ѡ��ı�
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
  protected void afterBillItemSelChg(int iRow, int iCol) {
  }

  /**
   * �˴����뷽��˵����
   * �������ڣ�(2004-5-10 17:33:36)
   * @param voRetvos nc.vo.pub.AggregatedValueObject[]
   */
  private void afterVoLoaded() {
    //���ƽ��� 
    String[] keys = new String[] {
        "noutnum", "noutassistnum", "nshouldoutassistnum", "nshouldoutnum",
        "nmny"
    };

    //����˻���ֻ���为������������
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

    //�������Ͳ����޸�
    if (getBillCardPanel().getHeadItem("alloctypename") != null)
      getBillCardPanel().getHeadItem("alloctypename").setEnabled(false);
    //�����˻ز����޸�
    if (getBillCardPanel().getHeadItem("freplenishflag") != null)
      getBillCardPanel().getHeadItem("freplenishflag").setEnabled(false);

    calcSpace(getM_voBill());
  }

  /**
   * �����ߣ����˾�
   * ���ܣ����ݱ���༭�¼�ǰ��������
   * ������e���ݱ༭�¼�
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-8 19:08:05)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
   * �����ߣ����˾�
   * ���ܣ���������ѡ��ı�
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
  protected void beforeBillItemSelChg(int iRow, int iCol) {

  }

  /**
   *     ���򿪸ýڵ��ǰ�����������ڴ���ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱
   * �������
   *     ��Ҫ�жϡ�ֻ������ĳһ����ʱ�����ܴ򿪸ýڵ㡱�Ľڵ㣬��Ҫʵ�ֱ�������
   * �ڷ����ڽ��������жϡ�
   *     ������ݷ���ֵ������Ӧ�����������ֵΪһ���ǿ��ַ�������ô���಻��
   * �ýڵ㣬ֻ��һ���Ի�������ʾ���ص��ַ������������ֵΪnull����ô������Դ�
   * �����ڵ�һ���򿪸ýڵ㡣
   *
   * �������ڣ�(2002-3-11 10:39:16)
   * @return java.lang.String
   */
  public String checkPrerequisite() {
    return m_sErr;
  }

  /**
   * �����ߣ����˾�
   * ���ܣ����󷽷�������ǰ��VO���
   * �����������浥��
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-24 ���� 5:17)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected boolean checkVO(nc.vo.ic.pub.bill.GeneralBillVO voBill) {
    //	String sSourceBillType = getSourBillTypeCode();
    try {
      VOCheck.checkOutRetVO(voBill);
      boolean bCheck = true;
      bCheck = super.checkVO();
      //�����˻ص�������Ϊ����

      return bCheck;

    }
    catch (Exception e) {
      showErrorMessage(e.getMessage());
      return false;
    }
  }

  /**
   * ******************************************
   *  ���ܣ���������������ʱ��ѯ�Ի���Ĵ���
   *���޸ģ��۱���2005-05-12
   *��
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
      // showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi","UPP4008busi-000293")/*@res "�ù��ܲ����ã�ԭ���ڲ����׹���ϵͳû�а�װ����"*/);
      //�˴�û���׳��쳣����onAddToOrder()�л����жϡ�
      //�۱���2005-05-12
    }
    return dlgQry;
  }

  /**
   * ���˵��ݲ���
   * �����ߣ�����
   * ���ܣ���ʼ�����չ���
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-7-17 10:33:20)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   */
  public void filterRef(String sCorpID) {

    try {

      super.filterRef(sCorpID);
      //����˿����֯
      //�����ǰ�Ĳֿⲻ����
      //���˲ֿ����
      String sConstraint[] = null;
      if (getCalbodyid() != null) {
        sConstraint = new String[1];
        sConstraint[0] = " AND pk_calbody='" + getCalbodyid() + "'";
      }
      nc.ui.pub.bill.BillItem bi = getBillCardPanel().getHeadItem(
          IItemKey.WAREHOUSE);
      //�޸��ˣ������� �޸����ڣ�2007-9-26����11:32:12 �޸�ԭ��ֻ����sCorpID��������getEnvironment().getCorpID()
      //nc.ui.ic.pub.bill.initref.RefFilter.filtWh(bi, getEnvironment().getCorpID(), sConstraint);
      nc.ui.ic.pub.bill.initref.RefFilter.filtWh(bi, sCorpID, sConstraint);

    }
    catch (Exception e) {
      nc.vo.scm.pub.SCMEnv.error(e);
    }
  }

  /**
   * ******************************************
   *  ���ܣ�<|>
   *
   *  �������ڣ�(2004-3-9 11:09:31)
   *  @param��
   *
   *  @return��
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
   * �����ߣ����˾�
   * ���ܣ��õ���ѯ�Ի���
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
//          /*@res "������������"*/},
//          {
//              BillTypeConst.TO_ORDER2,
//              nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
//                  "UPT40080618-000007")
//          /*@res "��˾���������"*/},
//          {
//              BillTypeConst.TO_ORDER1,
//              nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
//                  "UPT40080618-000008")
//          /*@res "��֯���������"*/},
//          {
//              BillTypeConst.TO_ORDER4,
//              nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
//                  "UPT40080618-000009")
//          /*@res "��֯�ڵ�������"*/}
//      });
//      ivjQueryConditionDlg.setRefInitWhereClause("head.coutcalbodyid", "�����֯", /*-=notranslate=-*/
//      "pk_corp=", "head.coutcorpid");
//      //ivjQueryConditionDlg.setRefInitWhereClause(
//      //"head.pk_calbody",
//      //"�����֯",
//      //"pk_corp=",
//      //"head.pk_corp");
//      ivjQueryConditionDlg.setRefInitWhereClause("head.cothercalbodyid",
//          "�����֯", /*-=notranslate=-*/
//          "pk_corp=", "head.cothercorpid");
//
//      ivjQueryConditionDlg.setRefInitWhereClause("head.cotherwhid", "�ֿ⵵��", /*-=notranslate=-*/
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
//   * �˴����뷽��˵����
//   ���������Condition�ĸ��Ի��޸�����
//   �����ط���
//   * �������ڣ�(2003-11-25 20:58:54)
//   */
//  protected void getConDlginitself(
//      nc.ui.ic.pub.bill.query.QueryConditionDlgForBill queryCondition) {
//    if (m_sCalbodyid != null) {
//      queryCondition.setRefInitWhereClause("head.pk_calbody", "�����֯",
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
                @res "������������"},
                {
                    BillTypeConst.TO_ORDER2,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
                        "UPT40080618-000007")
                @res "��˾���������"},
                {
                    BillTypeConst.TO_ORDER1,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
                        "UPT40080618-000008")
                @res "��֯���������"},
                {
                    BillTypeConst.TO_ORDER4,
                    nc.ui.ml.NCLangRes.getInstance().getStrByID("40080618",
                        "UPT40080618-000009")
                @res "��֯�ڵ�������"}
            });*/
            getQueryDlg().setRefInitWhereClause("head.coutcalbodyid", "�����֯", /*-=notranslate=-*/
            "pk_corp=", "head.coutcorpid");
            //ivjQueryConditionDlg.setRefInitWhereClause(
            //"head.pk_calbody",
            //"�����֯",
            //"pk_corp=",
            //"head.pk_corp");
            getQueryDlg().setRefInitWhereClause("head.cothercalbodyid",
                "�����֯", /*-=notranslate=-*/
                "pk_corp=", "head.cothercorpid");
            
//            getQueryDlg().setRefInitWhereClause("head.cotherwhid", "�ֿ⵵��",
//                "pk_corp=", "head.cothercorpid");
  
            getQueryDlg().setRefInitWhereClause("head.cotherwhid", "�ֿ⵵��", /*-=notranslate=-*/
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
              getQueryDlg().setRefInitWhereClause("head.pk_calbody", "�����֯",
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
   *  ���ܣ�<|>
   *
   *  �������ڣ�(2004-3-9 11:29:08)
   *  @param��
   *
   *  @return��
   *  ******************************************
   * @return nc.ui.scm.pub.FactoryLoginDialog
   */
  private FactoryLoginDialog getDlgFactory() {
    if (m_DlgFactory == null)
      m_DlgFactory = new nc.ui.scm.pub.FactoryLoginDialog(this,
          nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
              "UPP4008busi-000294")/*@res "��½�����֯"*/);
    return m_DlgFactory;
  }

  /**
   * �����ߣ����˾�
   * ���ܣ��õ��û�����Ķ����ѯ����
   * ������//��ѯ��������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
//  public String getExtendQryCond(nc.vo.pub.query.ConditionVO[] voaCond) {
//
//    //ת��Ϊ�Ӳ�ѯ
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
   * �˴����뷽��˵����
   * ��������:
   * ���ߣ����˾�
   * �������:
   * ����ֵ:
   * �쳣����:
   * ����:(2003-6-25 20:43:17)
   * @return java.util.ArrayList
   */
  protected java.util.ArrayList getFormulaItemHeader() {
    java.util.ArrayList arylistItemField = super.getFormulaItemHeader();

    //ԭ�еĹ�ʽ
    //�����֯ 1

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
   * ��ʼ���ࡣ
   */
  /* ���棺�˷������������ɡ� */
  public void initialize() {
	super.initialize();

  if (isStartFromTO() && getCalbodyid() == null) {
    //��½�����֯
    getDlgFactory().showModal();
    if (getCalbodyid() == null) {
      //showErrorMessage("��½�����֯ʧ�ܣ�");
      m_sErr = nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
          "UPP4008busi-000295")/*@res "��½�����֯ʧ�ܣ�"*/;
      return;
    }

  }
  //�����½�����֯�����ÿ����֯���ɱ༭
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
   * �����ߣ����˾�
   * ���ܣ���ʼ��ϵͳ����
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
  protected void initPanel() {
    //��Ҫ���ݲ���
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
   *  ���ܣ�<|>
   *
   *  �������ڣ�(2004-3-9 12:17:55)
   *  @param��
   *
   *  @return��
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
	   * ����ʵ�ָ÷�������Ӧ��ť�¼���
	   * @version (00-6-1 10:32:59)
	   *
	   * @param bo ButtonObject
	   */
	  public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
	    if (bo == getButtonTree().getButton(ICButtonConst.BTN_BILL_ADD_MANUAL) || 
          bo == getButtonTree().getButton(ICButtonConst.BTN_ADD)){
	      return;//onAddSelf();
      }else if (bo == getButtonTree().getButton(
          ICButtonConst.BTN_BILL_ADD_TO_5X)) {//��������
        //��ť�¼�����
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
          ICButtonConst.BTN_BILL_ADD_TO_4331)) { //������
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
	   * �����ߣ����˾�
	   * ���ܣ��޸Ĵ���
	   * ������
	   * ���أ�
	   * ���⣺
	   * ���ڣ�(2001-5-9 9:23:32)
	   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
	   *  ���ܣ�<|>
	   *
	   *  �������ڣ�(2004-3-10 12:33:02)
	   *  @param��
	   *
	   *  @return��
	   *  ******************************************
	   */
	  private void onAddSelf() {
	    onAdd();
	  }

	  /**
	   * ******************************************
	   *  ���ܣ�<|>
	   *
	   *  �������ڣ�(2004-3-10 12:33:02)
	   *  @param��
	   *
	   *  @return��
	   *  ******************************************
	   */
	  private void onAddToOrder(AggregatedValueObject[] vos) {

	    if (vos == null) {
	      return;
	    }
	    try {
	    	
	        if (vos != null && vos.length > 0)
				// ��鵥���Ƿ���Դ���µĲ��ս���
				if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO()
						.getAttributeValue(ICConst.IsFromNewRef))) {
					// �����Ĭ�Ϸ�ʽ�ֵ�
					vos = nc.vo.ic.pub.GenMethod.splitGeneralBillVOs(
							(GeneralBillVO[]) vos,
							getBillType(), getBillListPanel()
									.getHeadBillModel()
									.getFormulaParse());
					// ���������ݵĵ�λת��Ϊ���Ĭ�ϵ�λ.
					nc.vo.ic.pub.GenMethod.convertICAssistNumAtUI(
							(GeneralBillVO[]) vos, GenMethod.getIntance());
				}
	      
	      GeneralBillUICtl.procOrdEndAfterRefAdd((GeneralBillItemVO[])SmartVOUtilExt.getBodyVOs(vos), getBillCardPanel(), getBillType());
	          
	      //v5 lj ֧�ֶ��ŵ��ݲ�������
	      if (vos != null && vos.length == 1) {
	        setRefBillsFlag(false);
	        setBillRefResultVO(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), vos);
	      }
	      else {
	        setRefBillsFlag(true);//�ǲ������ɶ���
	        setBillRefMultiVOs(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), (GeneralBillVO[]) vos);
	      }
	      GeneralBillUICtl.setItemEdit(getBillCardPanel(), BillItem.HEAD, IItemKey.freplenishflag, false);
	      // end v5 

	    }
	    catch (Exception e) {
	      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	          "UPP4008busi-000297")/*@res "��������:"*/
	          + e.getMessage());
	    }
	  }
    
    /**
     * ******************************************
     *  ���ܣ�<|>
     *
     *  �������ڣ�(2004-3-10 12:33:02)
     *  @param��
     *
     *  @return��
     *  ******************************************
     */
    private void onAddToTran4331() {
     
      try {
        
        
        PubTransBillPaneVO[] sourecVO = new PubTransBillPaneVO[1];
        sourecVO[0] = new PubTransBillPaneVO("4331","������",
            (ISourceRedunUI)Class.forName("nc.ui.so.soreceive.redun.ReceiveTo4YRedunSourceCtrl").newInstance(),
            null,"SO",null,null);
        PubTransBillPaneVO[] curVO = new PubTransBillPaneVO[1];
        curVO[0] = new PubTransBillPaneVO("4Y","�������ⵥ",
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
  			// ��鵥���Ƿ���Դ���µĲ��ս���
  			if (!ICConst.IsFromNewRef.equals(vos[0].getParentVO()
  					.getAttributeValue(ICConst.IsFromNewRef))) {
  				// �����Ĭ�Ϸ�ʽ�ֵ�
  				vos = nc.vo.ic.pub.GenMethod.splitGeneralBillVOs(
  						(GeneralBillVO[]) vos,
  						getBillType(), getBillListPanel()
  								.getHeadBillModel()
  								.getFormulaParse());
  				// ���������ݵĵ�λת��Ϊ���Ĭ�ϵ�λ.
  				nc.vo.ic.pub.GenMethod.convertICAssistNumAtUI(
  						(GeneralBillVO[]) vos, GenMethod.getIntance());
  			}
        }catch(Exception e){
          getClientUI().showErrorMessage(e.getMessage());
          nc.vo.scm.pub.SCMEnv.out(e);
          return;
        }
        
        GeneralBillUICtl.procOrdEndAfterRefAdd((GeneralBillItemVO[])SmartVOUtilExt.getBodyVOs(vos), getBillCardPanel(), getBillType());
            
        //v5 lj ֧�ֶ��ŵ��ݲ�������
        if (vos != null && vos.length == 1) {
          setRefBillsFlag(false);
          setBillRefResultVO(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), vos);
        }
        else {
          setRefBillsFlag(true);//�ǲ������ɶ���
          setBillRefMultiVOs(((GeneralBillVO)vos[0]).getHeaderVO().getCbiztypeid(), (GeneralBillVO[]) vos);
        }
        GeneralBillUICtl.setItemEdit(getBillCardPanel(), BillItem.HEAD, IItemKey.freplenishflag, false);
        // end v5 

      }
      catch (Exception e) {
        showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
            "UPP4008busi-000297")/*@res "��������:"*/
            + e.getMessage());
      }
    }

	  /**
	   * �������������������˻� v52add
	   * <p>
	   * <b>examples:</b>
	   * <p>
	   * ʹ��ʾ��
	   * <p>
	   * <b>����˵��</b>
	   * <p>
	   * @author yangb
	   * @time 2007-7-5 ����01:25:44
	   */
	  private void onOutReturn() {

	    try {
	      
	      if (getM_iMode() != BillMode.Browse){
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000522")/* @res "δ����ĵ���,�����˻�" */);
	        return;
	      }

	      GeneralBillVO vo = getCurVO();
	      if (vo == null) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "scmcommon", "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�" */);
	        return;
	      }

	      int[] selrows = null;
	      if (BillMode.List == getM_iCurPanel())
	        selrows = getBillListPanel().getChildListPanel().getTable().getSelectedRows();
	      else
	        selrows = getBillCardPanel().getBodyPanel().getTable().getSelectedRows();
	      
	      if (selrows == null || selrows.length <= 0) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000515")/* @res "û��ѡ�еı����С�" */);
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
	      
//	    ��ǰ���б���ʽʱ�������л�������ʽ
	      if (BillMode.List == getM_iCurPanel()) 
	        onSwitch();

	      setM_voBill(vo);

	      //  ���õ�ǰ������������ ���� 2004-04-05
	      if (getM_utfBarCode() != null)
	    	  getM_utfBarCode().setCurBillItem(null);

	      getBillCardPanel().getBillModel().setNeedCalculate(false);

	      //  ����
	      getBillCardPanel().addNew();
	      setBillVO(getM_voBill());
	      
	      GeneralBillUICtl.processUIWhenRetOut(getBillCardPanel());

	      for (int row = 0; row < getBillCardPanel().getBillModel().getRowCount(); row++) {
	        // ������״̬Ϊ����
	        getBillCardPanel().getBillModel().setRowState(row,
	            nc.ui.pub.bill.BillModel.ADD);
	        getBillCardPanel().getBillModel().setValueAt(null, row,
	            IItemKey.NAME_BODYID);
	        getBillCardPanel().getBillModel().setValueAt(null, row,
	            IItemKey.NTRANOUTNUM);
	        getBillCardPanel().getBillModel().setValueAt(null, row,
	            IItemKey.NTRANOUTASTNUM);
	        
	      }
	      //  �����������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ�
	      setNewBillInitData();
	      getBillCardPanel().setEnabled(true);
	      setM_iMode(BillMode.New);
	      
	      GeneralBillUICtl.procFlagAndCalcNordcanoutnumAfterRefAdd(getM_voBill(),getBillCardPanel(),getBillType());

	      //  ���õ��ݺ��Ƿ�ɱ༭
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
     * ȡ������·�� 
     */
    private void onCancelSettlePath() {
    	


	    try {
	      if (BillMode.New == getM_iMode()) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000539")/* @res "�뵥�ݱ���֮����ȡ������·����" */);
	        return;
	      }

	      ArrayList alSelected = getSelectedBills();
	      if (alSelected == null || alSelected.size() != 1) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "scmcommon", "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�" */);
	        return;
	      }

	      int iselrow = getBillListPanel().getHeadTable().getSelectedRow();
	      GeneralBillVO voBill = (GeneralBillVO) alSelected.get(0);

	      if (voBill.getHeaderVO().getCgeneralhid() == null) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000505")/* @res "�뵥�ݱ���֮����ָ�����㣡" */);
	        return;
	      }
	      
	      if(voBill.getHeaderVO().getBoutretflag()!=null && voBill.getHeaderVO().getBoutretflag().booleanValue()){
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000531")/* @res "�˻ص��ݲ���Ҫָ������·��" */);
	        return;
	      }
	      
	      if (null == voBill.getHeaderVO().getAttributeValue("csettlepathid")){
	    	  showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	  	            "4008other", "UPP4008other-000537")/* @res "���ݽ���·���Ѿ�Ϊ�գ�����Ҫȡ������·����" */);
	  	        return;
	      }

	      GeneralBillItemVO[] voItems = voBill.getItemVOs();
	      UFDouble ufd = null;

	      for (int i = 0; i < voItems.length; i++) {

	        ufd = (UFDouble) voItems[i].getAttributeValue("nsettlenum1");

	        if (ufd != null && ufd.doubleValue() != 0.0)
	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000507")/* @res "�Ѿ���������������㣬������ָ������·����" */);

	        if (!"5D".equals(voItems[i].getCfirsttype()))

	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000508")/* @res "ֻ�й�˾�������������ָ������·����" */);

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
	            "UPP4008busi-000376")/*@res "���³ɹ���"*/);
	      }
	    }

	    catch (Exception e) {
	      nc.vo.scm.pub.SCMEnv.error(e);
	      showErrorMessage(e.getMessage());
	    }
      
    }

	  /**
	   * ָ������·�� 
	   */
	  private void onModifySettlePath() {

	    try {
	      if (BillMode.New == getM_iMode()) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000538")/* @res "�뵥�ݱ���֮����ָ������·����" */);
	        return;
	      }

	      ArrayList alSelected = getSelectedBills();
	      if (alSelected == null || alSelected.size() != 1) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "scmcommon", "UPPSCMCommon-000167")/* @res "û��ѡ�񵥾�" */);
	        return;
	      }

	      int iselrow = getBillListPanel().getHeadTable().getSelectedRow();
	      GeneralBillVO voBill = (GeneralBillVO) alSelected.get(0);

	      if (voBill.getHeaderVO().getCgeneralhid() == null) {
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000505")/* @res "�뵥�ݱ���֮����ָ�����㣡" */);
	        return;
	      }
	      
	      if(voBill.getHeaderVO().getBoutretflag()!=null && voBill.getHeaderVO().getBoutretflag().booleanValue()){
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
	            "4008other", "UPP4008other-000531")/* @res "�˻ص��ݲ���Ҫָ������·��" */);
	        return;
	      }

	      GeneralBillItemVO[] voItems = voBill.getItemVOs();
	      UFDouble ufd = null;

	      for (int i = 0; i < voItems.length; i++) {

	        ufd = (UFDouble) voItems[i].getAttributeValue("nsettlenum1");

	        if (ufd != null && ufd.doubleValue() != 0.0)
	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000507")/* @res "�Ѿ���������������㣬������ָ������·����" */);

	        if (!"5D".equals(voItems[i].getCfirsttype()))

	          throw new BusinessException(nc.ui.ml.NCLangRes.getInstance()
	              .getStrByID("4008other", "UPP4008other-000508")/* @res "ֻ�й�˾�������������ָ������·����" */);

	      }

	      SettlePathDlgForIC dlgModifySettlePath = new SettlePathDlgForIC(
	          (String) voBill.getHeaderVO().getAttributeValue("cothercorpid"),
	          getEnvironment().getCorpID(), getClientUI(), "ָ������·��");

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
	        SCMEnv.out("·���գ�");
	        return;
	      }
	      voBill.getHeaderVO().setCoperatoridnow(getEnvironment().getUserID());
	      
	      //�޸��ˣ������� �޸�ʱ�䣺2008-12-25 ����04:17:15 �޸�ԭ�򣺵����ڲ������Ƿ�ָ��������·��ʱʹ�á�
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
	            "UPP4008busi-000376")/*@res "���³ɹ���"*/);
	      }
	    }

	    catch (Exception e) {
	      nc.vo.scm.pub.SCMEnv.error(e);
	      showErrorMessage(e.getMessage());
	    }
	  }

	  /**
	   * ******************************************
	   *  ���ܣ�<|>
	   *
	   *  �������ڣ�(2004-3-10 12:33:02)
	   *  @param��
	   *
	   *  @return��
	   *  ******************************************
	   */
	  private void onAddToOrder(String funnode, String qrynodekey,
	      String sourcetype, ITOToIC_QryDLg dlgQry) {

	    if (dlgQry == null) {
	      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	          "UPP4008busi-000293")/*@res "�ù��ܲ����ã�ԭ���ڲ����׹���ϵͳû�а�װ����"*/);
	      return;
	    }

	    nc.vo.pub.AggregatedValueObject[] vos = dlgQry.getReturnVOs(getEnvironment().getCorpID(),
	        getEnvironment().getUserID(), sourcetype, getBillType(), funnode, qrynodekey, getClientUI());

	    if (vos == null) {
	      return;
	    }
	    try {
	      ////5C����VO,ת��Ϊ��浥��VO
	      nc.vo.pub.AggregatedValueObject[] voRetvos = (nc.vo.pub.AggregatedValueObject[]) nc.ui.pub.change.PfChangeBO_Client
	          .pfChangeBillToBillArray(vos, sourcetype, getBillType());
	      setBillRefResultVO(((GeneralBillVO)voRetvos[0]).getHeaderVO().getCbiztypeid(), voRetvos);
	      afterVoLoaded();

	    }
	    catch (Exception e) {
	      showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	          "UPP4008busi-000297")/*@res "��������:"*/
	          + e.getMessage());
	    }
	  }

	  /**
	   * ָ��������˾ 
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

	      ArrayList alSameCorpBillCode = new ArrayList(); //���ָ��������˾����빫˾��ͬ�ĵ��ݺš� �۱� on Jun 4, 2005		

	      //ָ��������˾��pk
	      String sAppointedCorp = alparam.get(0).toString();
	      nc.vo.ic.pub.bill.GeneralBillHeaderVO headVO = null; //��ͷvo		

	      //���ݹ��ˣ�ֻ���޸���Դ�����۲��ҳ���ⵥ���Ѿ�ǩ�ֵ�ֱ��ҵ��
	      for (int i = 0; i < alSelected.size(); i++) {
	        GeneralBillVO rowVO = (GeneralBillVO) alSelected.get(i);
	        headVO = rowVO.getHeaderVO();

	        //������Ա�͵�½���ڷŵ���ͷ
	        headVO.setCoperatoridnow(getEnvironment().getUserID()); //��ǰ����Ա
	        headVO.setAttributeValue("clogdatenow", getEnvironment().getLogDate()); //��ǰ��¼����

	        //������ֱ��ǩ�ֵ���
	        if (headVO.getFallocflag().intValue() != CONST.IC_ALLOCDIRECT
	            || !(headVO.getFbillflag().intValue() == 3 || headVO.getFbillflag()
	                .intValue() == 4)) {
	          alErrorCode.add(headVO.getVbillcode());
	          alSelected.remove(i);
	          i = i - 1;

	        }
	        else if (sAppointedCorp
	            .equals(headVO.getAttributeValue("cothercorpid"))) { //ָ��������˾��������빫˾��ͬ(��˾���������)
	          alSameCorpBillCode.add(headVO.getVbillcode());
	          alSelected.remove(i);
	          i = i - 1;

	        }
	      }

	      ArrayList alResult = null;
	      ArrayList alNOin = null; //û����ȫ���ɵ�����ⵥ�������Ѿ�����ĵ���
	      ArrayList alInvNotInOrg = null; //��Ŵ��������ָ���ĵ�����֯�ĵ��ݺ�

	      if (alSelected != null && alSelected.size() > 0) {
	        nc.vo.ic.pub.bill.GeneralBillVO[] selVOs = new nc.vo.ic.pub.bill.GeneralBillVO[alSelected
	            .size()];
	        selVOs = (GeneralBillVO[]) alSelected.toArray(selVOs);
	        /**			 
	         * ArrayList alResult  index = 0: û����ȫ���ɵ�����ⵥ�������Ѿ�����ĵ���.
	         *                     index = 1: ���������ĵ���: �޸ĵ�����˾�������֯��ĸ���vo.
	         *                     index = 2: ���������ָ���ĵ�����֯�ĵ��ݺ�.
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

	      //δ��������ʾ
	      StringBuffer sbHintMessage = new StringBuffer();
	      if (alErrorCode.size() > 0) {

	        sbHintMessage
	            .append(
	                nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	                    "UPP4008busi-000374")/*@res "ֻ���޸���Դ�����۲��ҳ���ⵥ���Ѿ�ǩ�ֵ�ֱ��ҵ��,���е��ݲ����޸ĵ�����˾��"*/)
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
	                "UPP4008busi-000387")/*@res "���е���û����ȫ���ɵ�����ⵥ�������Ѿ����㣺"*/)
	            .append("\n").append(alNOin.toString());
	      }
	      if (alInvNotInOrg != null && alInvNotInOrg.size() > 0) {
	        if (sbHintMessage.length() > 0)
	          sbHintMessage.append("\n");
	        sbHintMessage.append(
	            nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	                "UPP4008busi-000388")/*@res "���е��ݺ��в�����ָ�������֯�Ĵ����"*/).append(
	            "\n").append(alInvNotInOrg.toString());
	      }
	      if (sbHintMessage.length() != 0)
	        nc.ui.pub.beans.MessageDialog.showHintDlg(getClientUI(), null, sbHintMessage
	            .toString());
	      else
	        showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("4008busi",
	            "UPP4008busi-000376")/*@res "���³ɹ���"*/);
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
  			//��־�쳣
  			nc.vo.scm.pub.SCMEnv.error(e);
  			showErrorMessage(e.getMessage());
  		}
  	}
  	return m_buttonManager;
  }

  /**
   * ���෽����д
   * 
   * @see nc.ui.ic.pub.bill.GeneralBillClientUI#onClosing()
   */
  public boolean onClosing() {

    if (BillMode.List == getM_iCurPanel() && m_bRefBills) {
      int iret = MessageDialog.showYesNoDlg(this, null, nc.ui.ml.NCLangRes
          .getInstance().getStrByID("4008busi", "UPP4008busi-000400")/*@res "������δ��ɣ��Ƿ�رգ�"*/);
      if (iret == MessageDialog.ID_YES)
        return true;
      else
        return false;
    }

    return super.onClosing();

  }

  /**
   * ?user>
   * ���ܣ�
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2005-2-4 15:16:45)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
          //��ˢ��Ƭ

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

      //ˢ���б������ʾ
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
   * �����ߣ����˾�
   * ���ܣ����б�ʽ��ѡ��һ�ŵ���
   * ������ ������alListData�е�����
   * ���أ���
   * ���⣺
   * ���ڣ�(2001-11-23 18:11:18)
   *  �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  protected void selectBillOnListPanel(int iBillIndex) {
  }

  /**
   * �����ߣ����˾�
   * ���ܣ������趨�İ�ť��ʼ���˵���
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-11-23 18:11:18)
   *  �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   */
  /*
   protected void setButtons() {
   try {
   Vector vSubMenu = null;
   if (m_vTopMenu != null && m_vTopMenu.size() > 0) {

   //����˵�
   m_boaButtonGroup = new ButtonObject[m_vTopMenu.size()];
   //�Ӳ˵�
   ButtonObject boSub = null;
   for (int i = 0; i < m_vTopMenu.size(); i++) {
   if (m_vTopMenu.elementAt(i) instanceof Vector) {
   //�Ӳ˵�
   vSubMenu = (Vector) m_vTopMenu.elementAt(i);
   //��0���Ƕ���˵�
   boSub = (ButtonObject) vSubMenu.elementAt(0);
   for (int j = 1; j < vSubMenu.size(); j++)
   boSub.addChildButton((ButtonObject) vSubMenu.elementAt(j));
   m_boaButtonGroup[i] = boSub;
   } else
   //����˵�
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
   * �����ߣ����˾�
   * ���ܣ����󷽷������ð�ť״̬����setButtonStatus�е��á�
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   *
   *
   *
   *
   */
  protected void setButtonsStatus(int iBillMode) {
  }

  /**
   * ******************************************
   *  ���ܣ�<|>
   *
   *  �������ڣ�(2004-3-9 11:09:31)
   *  @param��
   *
   *  @return��
   *  ******************************************
   * @param newCalbodyis java.lang.String
   */
  public void setCalbodyid(java.lang.String newCalbodyid) {
    m_sCalbodyid = newCalbodyid;
  }

  /**
   * �����ߣ����˾�
   * ���ܣ������������ݵĳ�ʼ���ݣ������ڣ��Ƶ��˵ȡ����ÿ����֯
   * ������
   * ���أ�
   * ���⣺
   * ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
   * ǩ�ּ�ȡ��ǩ�ֶ��������²�ѯ��������vo����ˢ�½��档
   * 
   * @author: shaobing
   * @date:2005-06-21
   */
  /**
   * �˴����뷽��˵���� ���ܣ�ˢ��ts,�õ�����״̬ ������ ���أ� ���⣺ ���ڣ�(2002-6-4 19:54:51)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
   * 
   * @return java.lang.String
   * @param sBillPK
   *            java.lang.String
   */
//  protected String freshStatusTs(String sBillPK) throws Exception {
//    String sBillStatus = null;
//
//    //���²�ѯvo            
//    //ƴ�Ӳ�ѯ����
//    String sQryWhere = " head.cgeneralhid ='" + getM_voBill().getPrimaryKey()
//        + "'  ";
//    QryConditionVO voQryCond = new QryConditionVO(sQryWhere);
//    voQryCond.setIntParam(0, GeneralBillVO.QRY_FULL_BILL_PURE);
//
//    //��ѯ
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
//    //���±���������
//    setBillVO(getM_voBill());
//
//    //�����б��������
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
   * �����ߣ����˾� ���ܣ��ڱ�������ʾVO ������ ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32)
   * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
   * �����ߣ����˾� ���ܣ�ѡ���б���ʽ�µĵ�sn�ŵ��� ������sn �������
   * 
   * ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
   * ���أ� ���⣺ ���ڣ�(2001-5-9 9:23:32) �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
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
			m_timer.start("���濪ʼ");
			t.start();
			// ��ȥ����ʽ�µĿ���
			filterNullLine();

			m_timer.showExecuteTime("filterNullLine");
			// �ޱ����� ------------ EXIT -------------------
			if (getBillCardPanel().getRowCount() <= 0) {
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000072")/* @res "�����������!" */);
				// ��������� add by hanwei 2004-06-08 ,��������ⵥ��Щ����²�������
				return false;
			}
			// added by zhx 030626 ����кŵĺϷ���; �÷���Ӧ���ڹ��˿��еĺ��档
			if (!nc.ui.scm.pub.report.BillRowNo.verifyRowNosCorrect(
					getBillCardPanel(), IItemKey.CROWNO)) {
				return false;
			}
			// ��ǰ�ı�������
			int iRowCount = getBillCardPanel().getRowCount();
			// ����ĵ�������
			GeneralBillVO voInputBill = null;
			// �ӽ����л����Ҫ������
			voInputBill = getBillVO();
			
			// add by river for 2012-11-20
			// ��������ֱ�����õ�VO��.   ԭ�򣺽������޷���ȡ�������̵����ݡ�ͨ��ITEM���Ի�ȡ����
			Object ctrancustid = getBillCardPanel().getHeadItem("ctrancustid").getValueObject();
			voInputBill.getParentVO().setAttributeValue("ctrancustid", ctrancustid);
			
			// �õ����ݴ��󣬳��� ------------ EXIT -------------------
			if (voInputBill == null || voInputBill.getParentVO() == null
					|| voInputBill.getChildrenVO() == null) {
				SCMEnv.out("Bill is null !");
				return false;
			}
			// ����ı�����
			GeneralBillItemVO voInputBillItem[] = voInputBill.getItemVOs();
			// �õ�������
			int iVORowCount = voInputBillItem.length;
			// �õ������кͽ���������һ�£����� ------------ EXIT -------------------
			if (iVORowCount != iRowCount) {
				SCMEnv.out("data error." + iVORowCount + "<>" + iRowCount);
				return false;
			}
			m_timer.showExecuteTime("From fliterNullLine Before setIDItems");
			// VOУ��׼������
			getM_voBill().setIDItems(voInputBill);
			// ���õ�������
			getM_voBill().setHeaderValue("cbilltypecode", getBillType());

			m_timer.showExecuteTime("setIDItems");

			// ���õ����к�zhx 0630:
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
			// VOУ�� ------------ EXIT -------------------
			if (!checkVO(getM_voBill())) {

				return false;
			}
			m_timer.showExecuteTime("VOУ��");

			// ���û�е������ڣ���дΪ��ǰ��¼����
			if (getBillCardPanel().getHeadItem("dbilldate") == null
					|| getBillCardPanel().getHeadItem("dbilldate")
							.getValueObject() == null
					|| getBillCardPanel().getHeadItem("dbilldate")
							.getValueObject().toString().trim().length() == 0) {
				SCMEnv.out("-->no bill date.");
				getM_voBill().setHeaderValue("dbilldate",
						getEnvironment().getLogDate());
			}
			m_timer.showExecuteTime("���õ������ͺ͵�������");

			// showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
			// "4008bill", "UPP4008bill-000102")/* @res "���ڱ��棬���Ժ�..." */);

			// ����ĺ��ķ������ add by hanwei 2004-04

			// Ĭ�ϵ���״̬ add by hanwei
			m_sBillStatus = nc.vo.ic.pub.bill.BillStatus.FREE;
			// ʵ��m_sBillStatus�ĸ�ֵ��onSaveBaseKernel�еģ�saveUpdateBill,saveNewBill

			getM_voBill().setIsCheckCredit(true);
			getM_voBill().setIsCheckPeriod(true);
			getM_voBill().setIsCheckAtp(true);
			getM_voBill().setGetPlanPriceAtBs(false);
			getM_voBill().setIsRwtPuUserConfirmFlag(false);

			// �����ֵ
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
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						int iFlag = showYesNoMessage(realbe.getMessage());
						// ����û�ѡ�����
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							getM_voBill().setIsRwtPuUserConfirmFlag(true);
							continue;
						} else
							return false;
					} else if (realbe != null
							&& realbe.getClass() == CreditNotEnoughException.class) {
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						// �Ƿ������ ��Ϊ������ʽ modify by qinchao  20081225 ʥ���ڣ���3���滻
						int iFlag = showYesNoMessage(realbe.getMessage()
								+ " \r\n" + 
								nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","ClientUI-000001")/* @res "�Ƿ����" */);
						// ����û�ѡ�����
						if (iFlag == nc.ui.pub.beans.MessageDialog.ID_YES) {
							getM_voBill().setIsCheckCredit(false);
							continue;
						} else
							return false;
					} else if (realbe != null
							&& realbe.getClass() == PeriodNotEnoughException.class) {
						// ������Ϣ��ʾ����ѯ���û����Ƿ��������
						int iFlag = showYesNoMessage(realbe.getMessage()
								+ " \r\n" + 
								nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","ClientUI-000001")/* @res "�Ƿ����" */);
						// ����û�ѡ�����
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
							// ������Ϣ��ʾ����ѯ���û����Ƿ��������
							int iFlag = showYesNoMessage(atpe.getMessage()
									+ " \r\n" + 
									nc.ui.ml.NCLangRes.getInstance().getStrByID("40080802","ClientUI-000001")/* @res "�Ƿ����" */);
							// ����û�ѡ�����
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
			
			

			// ����ͨ���������޸�
			if (BillMode.New == getM_iMode() || BillMode.Update == getM_iMode()) {
				// necessary��//ˢ�µ���״̬
				getBillCardPanel().updateValue();
				m_timer.showExecuteTime("updateValue");
				// coperatorid
				setM_iMode(BillMode.Browse);
				
				getEditCtrl().resetCardEditFlag(getBillCardPanel());
				// ���ɱ༭
				getBillCardPanel().setEnabled(false);
				// ���谴ť״̬
				setButtonStatus(false);
				m_timer.showExecuteTime("setButtonStatus");

				// ����ִ���
				// ���� by hanwei 2003-11-13 ���Ᵽ������ѡ����ִ���Ϊ��
				// m_voBill.clearInvQtyInfo();
				// ѡ�е�һ��
				getBillCardPanel().getBillTable().setRowSelectionInterval(0, 0);
				// �������к��Ƿ����
				setBtnStatusSN(0, false);
				// ˢ�µ�һ���ִ�����ʾ
				setTailValue(0);
				m_timer.showExecuteTime("ˢ�µ�һ���ִ�����ʾ");
			}

//			if (m_sBillStatus != null && !m_sBillStatus.equals(BillStatus.FREE)
//					&& !m_sBillStatus.equals(BillStatus.DELETED)) {
//				SCMEnv.out("**** saved and signed ***");
//				freshAfterSignedOK(m_sBillStatus);
//				m_timer.showExecuteTime("freshAfterSignedOK");
//			}
    
    m_sBillStatus = afterAuditFrushData(getM_voBill(),true);
    
			showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"common", "UCH005")/* @res "����ɹ�" */);

			// ��������Դ�ĵ��ݽ��в�ͬ�Ľ�����ƣ�zhx 1130
			ctrlSourceBillUI(true);
			m_timer.showExecuteTime("��Դ���ݽ������");
			t.stopAndShow("����ϼ�");

			// save the barcodes to excel file according to param IC***
			m_timer.showExecuteTime("��ʼִ�б��������ļ�");
			OffLineCtrl ctrl = new OffLineCtrl(this);
			ctrl.saveExcelFile(getM_voBill(), getCorpPrimaryKey());
			m_timer.showExecuteTime("ִ�б��������ļ�����");
			nc.ui.ic.pub.tools.GenMethod.reSetRowColorWhenNOException(getBillCardPanel());
			return true;

		} catch (java.net.ConnectException ex1) {
			SCMEnv.out(ex1.getMessage());
			if (showYesNoMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"4008bill", "UPP4008bill-000104")/*
														 * @res
														 * "��ǰ�����жϣ��Ƿ񽫵�����Ϣ���浽Ĭ��Ŀ¼��"
														 */
			) == MessageDialog.ID_YES) {
				onButtonClicked(getButtonManager().getButton(
						ICButtonConst.BTN_EXPORT_TO_DIRECTORY));
				// onBillExcel(1);// ���浥����Ϣ��Ĭ��Ŀ¼
			}
		} catch (Exception e) {

			if (e instanceof nc.vo.ic.ic009.PackCheckBusException) {

				handleException(e);
				showHintMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"4008bill", "UPP4008bill-000105")/* @res "�������" */);
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
						"4008bill", "UPP4008bill-000105")/* @res "�������" */);
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
				// ������ɫ
				nc.ui.ic.pub.tools.GenMethod.setRowColorWhenException(getBillCardPanel(),ee);
			}

		}
		return false;
	}

}