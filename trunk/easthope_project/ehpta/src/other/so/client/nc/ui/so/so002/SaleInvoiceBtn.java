package nc.ui.so.so002;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillStatus;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.pub.bill.ButtonTree;
import nc.ui.scm.pub.bill.ScmButtonConst;
import nc.ui.scm.so.SaleBillType;
import nc.vo.pub.BusinessException;
import nc.vo.scm.pub.SCMEnv;

/**
 * <p>
 * <b>������Ҫ������¹��ܣ�</b>
 * <ul>
 * <li>������Ŀ1
 * <li>������Ŀ2
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
 * @time 2007-3-6 ����04:14:39
 */
public class SaleInvoiceBtn {

  public final String BTN_ATP = "������";

  public final String BTN_CUSTINFO = "�ͻ���Ϣ";

  public final String BTN_CUSTCREDIT = "�ͻ�����";

  public final String BTN_PRIFIT = "ë��Ԥ��";

  public final String BTN_SoTax = "����˰";

  public final String BTN_UniteInvoice = "�ϲ���Ʊ";

  public final String BTN_UniteCancel = "�����ϲ�";

  public final String BTN_Gathe = "������ܿ�Ʊ";

  public final String BTN_ExecRpt = "��Ʊִ�����";

  public final String BTN_AuditFlowStatus = "������״̬";

  public final String BTN_OpposeAct = "���ɶԳ巢Ʊ";
  
  public final String BTN_RefAdd = "��������";
  
  public final String BTN_FetchCost = "ȡ�ɱ���";
  
  public final String BTN_CardEdit = "��Ƭ�༭";
  
  public final String BTN_ReRowNO = "�����к�";
  
  public final String BTN_ImportTaxCode = "����˰Ʊ��";
  
  // add by river for 2012-08-06
  public final String BTN_ContBalance = "��ͬ���";

  private ButtonTree bt = null;

  // ------------------------UI
  public ButtonObject m_boBusiType = null ;
  // ����
  public ButtonObject m_boAdd = null ;
//  ����
  public ButtonObject m_boSave = null ;

  // ά��
  // �޸ģ�ȡ����ԭ����������ɾ����ԭ�����ϣ�������ת�����ϲ���Ʊ�������ϲ�
  // public ButtonObject m_boBill
  public ButtonObject m_boMaintain = null ;
  public ButtonObject m_boModify = null ;
  public ButtonObject m_boCancel = null ;
  public ButtonObject m_boBlankOut = null ;
  public ButtonObject m_boCancelTransfer =null ;
  public ButtonObject m_boUnite =null;
  public ButtonObject m_boUniteCancel = null ;

  // �в���
  // ���У�ɾ�У������У����ӣ��������У�ճ���� 
  //v55 �¼� ��Ƭ�༭�������к�
  public ButtonObject m_boLineOper = null ;
  public ButtonObject m_boAddLine = null ;
  public ButtonObject m_boDelLine = null ;
  public ButtonObject m_boInsertLine = null ;
  public ButtonObject m_boCopyLine = null ;
  public ButtonObject m_boPasteLine = null ;
  public ButtonObject m_boPasteLineTail = null;
  public ButtonObject m_boCardEdit = null;
  public ButtonObject m_boReRowNO = null;
  //��������
  public ButtonObject m_boRefAdd = null;
  //ȡ�ɱ���
  public ButtonObject m_boFetchCost = null;
  
  // ����
  public ButtonObject m_boApprove = null ;

  // ִ��
  // ��������
  public ButtonObject m_boAction = null ;
  public ButtonObject m_boSendAudit = null ;
  public ButtonObject m_boUnApprove = null ;
  
//  ��ѯ
  public ButtonObject m_boQuery = null ;

  // ���
  // ˢ�£����ӣ�����λ����ҳ����ҳ��ԭ����һҳ������ҳ��ԭ����һҳ����ĩҳ��ȫѡ��ȫ��
  public ButtonObject m_boBrowse = null ;
  public ButtonObject m_boRefresh = null ;
  public ButtonObject m_boLocal = null ;
  public ButtonObject m_boFirst = null ;
  public ButtonObject m_boPrev = null ;
  public ButtonObject m_boNext = null ;
  public ButtonObject m_boLast = null ;
  public ButtonObject m_boSelectAll = null ;
  public ButtonObject m_boUnSelectAll = null ;
  
//  ��Ƭ��ʾ/�б���ʾ
  public ButtonObject m_boCard = null ;
  
  // ��ӡ
  // Ԥ������ӡ���ϲ���ʾ
  public ButtonObject m_boPrintManage =null ;
  public ButtonObject m_boPreview = null ;
  public ButtonObject m_boPrint = null ;
  public ButtonObject m_boBillCombin = null ;
 
//������ܿ�Ʊ
  public ButtonObject m_boGather = null ;
  
  // ��������
  // ���ɶԳ巢Ʊ������˰���ĵ�����
  public ButtonObject m_boAssistFunction =null ;
  public ButtonObject m_boOpposeAct = null ;
  public ButtonObject m_boSoTax = null ;
  public ButtonObject m_boDocument = null ;
  public ButtonObject m_boImportTaxCode = null;


  // ������ѯ
  // ���飬������ʾ/���أ�ȡ��ԭ��������ť�����ܺͶ�����һ�£���������״̬��
  // �ͻ���Ϣ����Ʊִ��������ͻ����ã�ë��Ԥ�����ͻ���Ϣ
  public ButtonObject m_boAssistant = null ;
  public ButtonObject m_boOrderQuery = null ;
  public ButtonObject m_boATP = null ;
  public ButtonObject m_boAuditFlowStatus = null ;
  public ButtonObject m_boCustInfo = null ;
  public ButtonObject m_boExecRpt = null ;
  public ButtonObject m_boCustCredit =null ;
  public ButtonObject m_boPrifit = null ;

  //��ͬ���
  // add by river for 2012-08-06
  public ButtonObject m_boContBalance = null;

  // PTA�ϲ���Ʊ
  // add by river for 2012-09-20
  public ButtonObject m_boPTAUnite = null;
  
  // PTAȡ���ϲ���Ʊ
  // add by river for 2021-09-20
  public ButtonObject m_boPTAUniteCancle = null;

  private ButtonObject[] aryButtonGroup = null;

  /**
   * ���ص�ǰѡ���ҵ������
   * <p>
   * <b>examples:</b>
   * <p>
   * ʹ��ʾ��
   * <p>
   * <b>����˵��</b>
   * 
   * @return ҵ������
   *         <p>
   * @author wangyf
   * @time 2007-3-6 ����10:33:09
   */
  public String getBusiType() {

    if (m_boBusiType.getChildButtonGroup() != null
        && m_boBusiType.getChildButtonGroup().length > 0) {

      for (int i = 0; i < m_boBusiType.getChildCount(); i++) {
        if (m_boBusiType.getChildButtonGroup()[i].isSelected()) {
          return m_boBusiType.getChildButtonGroup()[i].getTag();
        }
      }

      // ��ʼ��ʱʹ��
      return m_boBusiType.getChildButtonGroup()[0].getTag();
    }
    else {
      return null;
    }
  }

  /**
   * ���ذ�ť��
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
   * @time 2007-3-20 ����01:58:56
   */
  public ButtonObject[] getButtonArray() {
    return aryButtonGroup;
  }

  /**
   * ���õ���״̬�� �������ڣ�(2001-6-13 15:17:39)
   * 
   * @param iState
   *          int ����״̬ iOppState �Գ��־
   */
  public void setStateByBillStatus(SaleInvoiceListPanel listPanel) {
    int iState = listPanel.getBillStatus();
    switch (iState) {
      case BillStatus.FREE: {
        m_boBlankOut.setEnabled(true);
        m_boModify.setEnabled(true);
        m_boApprove.setEnabled(true);
        m_boUnApprove.setEnabled(false);
//        m_boAfterAction.setEnabled(false);
//        m_boStockLock.setEnabled(false);
        m_boDocument.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        // strState = "FREE";
        break;
      }
      case BillStatus.AUDIT: {
        m_boBlankOut.setEnabled(false);
        m_boApprove.setEnabled(false);
        m_boUnApprove.setEnabled(true);
        m_boModify.setEnabled(false);
//        m_boAfterAction.setEnabled(true);
//        m_boStockLock.setEnabled(false);
        m_boDocument.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        m_boOrderQuery.setEnabled(false);
        // setImageType(IMAGE_AUDIT);
        // strState = "AUDIT";
        break;
      }
      case BillStatus.BLANKOUT: {
        m_boModify.setEnabled(false);
        m_boAction.setEnabled(false);
        m_boAssistant.setEnabled(false);
//        m_boAfterAction.setEnabled(false);
        m_boPrint.setEnabled(false);
//        m_boStockLock.setEnabled(false);
        m_boDocument.setEnabled(false);
        m_boOrderQuery.setEnabled(false);
        m_boOrderQuery.setEnabled(false);
        // Ԥ�����û�
        m_boPreview.setEnabled(false);
        // ������״̬�û�
        m_boAuditFlowStatus.setEnabled(false);
        // setImageType(IMAGE_CANCEL);
        // strState = "BLANKOUT";
        break;
      }
        // ��ӡ�����������״̬
      case BillStatus.AUDITING: {
        m_boBlankOut.setEnabled(false);
        m_boApprove.setEnabled(true);
        m_boUnApprove.setEnabled(false);
//        m_boStockLock.setEnabled(false);
        // m_boAction.setEnabled(false);
//        m_boAfterAction.setEnabled(false);
        m_boSave.setEnabled(false);
        m_boModify.setEnabled(false);
        // setImageType(IMAGE_APPROVING);
        // strState = "AUDITING";
        break;
      }// ��ӡ�����δͨ����״̬
      case BillStatus.NOPASS: {
        m_boBlankOut.setEnabled(true);
        m_boModify.setEnabled(true);
        m_boSave.setEnabled(false);
        m_boCancel.setEnabled(false);
        m_boApprove.setEnabled(true);
        m_boUnApprove.setEnabled(false);
//        m_boAfterAction.setEnabled(false);
//        m_boStockLock.setEnabled(false);
        m_boDocument.setEnabled(true);
        m_boOrderQuery.setEnabled(true);
        m_boOrderQuery.setEnabled(true);

        // setImageType(IMAGE_APPROVEANDFAIL);
        // strState = "NOPASS";
        break;
      }
    }

    int selectRow = listPanel.getHeadTable().getSelectedRow();
    int iOppStatus = 0;
    if (selectRow > -1) {
      // ����״̬
      if (listPanel.getHeadItem("fcounteractflag") != null
          && listPanel.getHeadBillModel().getValueAt(selectRow,
              "fcounteractflag") != null
          && listPanel.getHeadItem("fcounteractflag").converType(
              listPanel.getHeadBillModel().getValueAt(selectRow,
                  "fcounteractflag")) != null) {
        iOppStatus = Integer.parseInt(listPanel.getHeadItem("fcounteractflag")
            .converType(
                listPanel.getHeadBillModel().getValueAt(selectRow,
                    "fcounteractflag")).toString());
      }
    }
    // �������ѶԳ嵥�ݣ����������޶�
    if (iState == BillStatus.AUDIT && iOppStatus == 1) {
      m_boUnApprove.setEnabled(false);
    }
    // �����ҶԳ���Ϊ�������򵥾��ܶԳ�
    if (iState == BillStatus.AUDIT && iOppStatus == 0) {
      m_boOpposeAct.setEnabled(true);
    }
    else {
      m_boOpposeAct.setEnabled(false);
    }

  }

  /**
   * SaleInvoiceBtn �Ĺ�����
   */
  public SaleInvoiceBtn() {
    // ��ʼ����ť��
    try {
      bt = new ButtonTree(SaleInvoiceTools.getNodeCode());
    }
    catch (BusinessException e) {
      SCMEnv.out(e);
      return;
    }
    
    // ҵ������
    // ��ȡҵ������
    m_boBusiType = bt.getButton(ScmButtonConst.BTN_BUSINESS_TYPE);// ҵ������
    PfUtilClient.retBusinessBtn(m_boBusiType, ClientEnvironment.getInstance()
        .getCorporation().getPrimaryKey(), SaleBillType.SaleInvoice);

    // ����
    m_boAdd = bt.getButton(ScmButtonConst.BTN_ADD);
    /**V55 ���۷�Ʊ֧�ֱ༭״̬׷��**/
    m_boRefAdd = bt.getButton(BTN_RefAdd);
    if (m_boBusiType.getChildButtonGroup() != null
        && m_boBusiType.getChildButtonGroup().length > 0) {
//      ButtonObject[] bo = m_boBusiType.getChildButtonGroup();
//      for (int i = 0; i < bo.length; i++) {
//        bo[i].setName(bo[i].getName());
//      }
//      m_boBusiType.setChildButtonGroup(bo);
      m_boBusiType.getChildButtonGroup()[0].setSelected(true);
      m_boBusiType.getChildButtonGroup()[0].setSelected(true);
      m_boBusiType.setCheckboxGroup(true);
      PfUtilClient.retAddBtn(m_boAdd, ClientEnvironment.getInstance()
          .getCorporation().getPrimaryKey(), SaleBillType.SaleInvoice,
          m_boBusiType.getChildButtonGroup()[0]);
      PfUtilClient.retAddBtn(m_boRefAdd, ClientEnvironment.getInstance()
              .getCorporation().getPrimaryKey(), SaleBillType.SaleInvoice,
              m_boBusiType.getChildButtonGroup()[0]);
//      bo = m_boAdd.getChildButtonGroup();
//      for (int i = 0; i < bo.length; i++) {
//        bo[i].setName(bo[i].getName());
//      }
//      m_boAdd.setChildButtonGroup(bo);
    }    
    //ȡ�ɱ���
    m_boFetchCost = bt.getButton(BTN_FetchCost);
    
//    ����
    m_boSave = bt.getButton(ScmButtonConst.BTN_SAVE); // ����

    // ά��
    // �޸ģ�ȡ����ԭ����������ɾ����ԭ�����ϣ�������ת�����ϲ���Ʊ�������ϲ�
    // m_boBill
    m_boMaintain = bt.getButton(ScmButtonConst.BTN_BILL); // �޸�
    m_boModify = bt.getButton(ScmButtonConst.BTN_BILL_EDIT); // �޸�
    m_boCancel = bt.getButton(ScmButtonConst.BTN_BILL_CANCEL);// ȡ��
    m_boBlankOut = bt.getButton(ScmButtonConst.BTN_BILL_DELETE);
    m_boCancelTransfer = bt.getButton(ScmButtonConst.BTN_REF_CANCEL);
    m_boUnite = bt.getButton(BTN_UniteInvoice);
    m_boUniteCancel = bt.getButton(BTN_UniteCancel);// �����ϲ�
    
    m_boPTAUnite = new ButtonObject("PTA�ϲ���Ʊ" , "" , 0 , "PTA�ϲ���Ʊ");
    m_boPTAUniteCancle = new ButtonObject("PTAȡ���ϲ�" , "" , 0 , "PTAȡ���ϲ�");
    m_boMaintain.addChildButton(m_boPTAUnite);
    m_boMaintain.addChildButton(m_boPTAUniteCancle);
    
    // �в���
    // ���У�ɾ�У������У����ӣ��������У�ճ���� V55 ������Ƭ�༭�������к�
    m_boLineOper = bt.getButton(ScmButtonConst.BTN_LINE);// �в���
    m_boAddLine = bt.getButton(ScmButtonConst.BTN_LINE_ADD); // ������
    m_boDelLine = bt.getButton(ScmButtonConst.BTN_LINE_DELETE); // ɾ����
    m_boInsertLine = bt.getButton(ScmButtonConst.BTN_LINE_INSERT); 
    m_boCopyLine = bt.getButton(ScmButtonConst.BTN_LINE_COPY);
    m_boPasteLine = bt.getButton(ScmButtonConst.BTN_LINE_PASTE);
    m_boPasteLineTail = bt.getButton(ScmButtonConst.BTN_LINE_PASTE_TAIL);
    m_boCardEdit = bt.getButton(BTN_CardEdit);
    m_boReRowNO = bt.getButton(BTN_ReRowNO);

    // ����
    m_boApprove = bt.getButton(ScmButtonConst.BTN_AUDIT); // ����

    // ִ��
    // ��������
    m_boAction = bt.getButton(ScmButtonConst.BTN_EXECUTE);// ִ��
    m_boSendAudit = bt.getButton(ScmButtonConst.BTN_EXECUTE_AUDIT);// ����
    m_boUnApprove = bt.getButton(ScmButtonConst.BTN_EXECUTE_AUDIT_CANCEL); // ����
    
//    ��ѯ
    m_boQuery = bt.getButton(ScmButtonConst.BTN_QUERY);

    // ���
    // ˢ�£����ӣ�����λ����ҳ����ҳ��ԭ����һҳ������ҳ��ԭ����һҳ����ĩҳ��ȫѡ��ȫ��
    m_boBrowse = bt.getButton(ScmButtonConst.BTN_BROWSE);
    m_boRefresh = bt.getButton(ScmButtonConst.BTN_BROWSE_REFRESH);
    m_boLocal = bt.getButton(ScmButtonConst.BTN_BROWSE_LOCATE);
    m_boFirst = bt.getButton(ScmButtonConst.BTN_BROWSE_TOP);
    m_boPrev = bt.getButton(ScmButtonConst.BTN_BROWSE_PREVIOUS);
    m_boNext = bt.getButton(ScmButtonConst.BTN_BROWSE_NEXT);
    m_boLast = bt.getButton(ScmButtonConst.BTN_BROWSE_BOTTOM);
    m_boSelectAll = bt.getButton(ScmButtonConst.BTN_BROWSE_SELECT_ALL);
    m_boUnSelectAll = bt.getButton(ScmButtonConst.BTN_BROWSE_SELECT_NONE);
    
//    ��Ƭ��ʾ/�б���ʾ
    m_boCard = bt.getButton(ScmButtonConst.BTN_SWITCH);
    
    // ��ӡ
    // Ԥ������ӡ���ϲ���ʾ
    m_boPrintManage = bt.getButton(ScmButtonConst.BTN_PRINT);
    m_boPreview = bt.getButton(ScmButtonConst.BTN_PRINT_PREVIEW);
    m_boPrint = bt.getButton(ScmButtonConst.BTN_PRINT_PRINT);
    m_boBillCombin = bt.getButton(ScmButtonConst.BTN_PRINT_DISTINCT);
   
//  ������ܿ�Ʊ
    m_boGather = bt.getButton(BTN_Gathe);
    
    // ��������
    // ���ɶԳ巢Ʊ������˰���ĵ�����
    m_boAssistFunction = bt.getButton(ScmButtonConst.BTN_ASSIST_FUNC);// ��������
    m_boOpposeAct = bt.getButton(BTN_OpposeAct);
    m_boSoTax = bt.getButton(BTN_SoTax);
    m_boDocument = bt.getButton(ScmButtonConst.BTN_ASSIST_FUNC_DOCUMENT);
    m_boImportTaxCode = bt.getButton(BTN_ImportTaxCode);

    // ������ѯ
    // ���飬������ʾ/���أ�ȡ��ԭ��������ť�����ܺͶ�����һ�£���������״̬��
    // �ͻ���Ϣ����Ʊִ��������ͻ����ã�ë��Ԥ�����ͻ���Ϣ
    m_boAssistant = bt.getButton(ScmButtonConst.BTN_ASSIST_QUERY);
    m_boOrderQuery = bt.getButton(ScmButtonConst.BTN_ASSIST_QUERY_RELATED);
   
    // ����ͬ��ť��ӵ�������ѯ��
    // add by river for 2012-08-06
    m_boContBalance = new ButtonObject("��ͬ���" , "" , 0 , "��ͬ���");
    m_boAssistant.addChildButton(m_boContBalance);
    
    m_boATP = bt.getButton(BTN_ATP);// ������
    m_boATP.setVisible(false);
    
    m_boAuditFlowStatus = bt.getButton(ScmButtonConst.BTN_ASSIST_QUERY_WORKFLOW);
    m_boCustInfo = bt.getButton(BTN_CUSTINFO);;
    m_boExecRpt = bt.getButton(BTN_ExecRpt);
    m_boCustCredit = bt.getButton(BTN_CUSTCREDIT);
    m_boPrifit = bt.getButton(BTN_PRIFIT);
    {
      m_boATP.setTag(ISaleInvoiceAction.Atp);
      m_boCustInfo.setTag(ISaleInvoiceAction.CustInfo);
      m_boCustCredit.setTag(ISaleInvoiceAction.CustCredit);
      m_boPrifit.setTag(ISaleInvoiceAction.Prifit);
      m_boSoTax.setTag(ISaleInvoiceAction.Tax);
      m_boApprove.setTag(ISaleInvoiceAction.Approve);
      m_boBlankOut.setTag(ISaleInvoiceAction.BlankOut);
      m_boUnApprove.setTag(ISaleInvoiceAction.UnApprove);
      m_boUnite.setTag(ISaleInvoiceAction.Unite);
      m_boUniteCancel.setTag(ISaleInvoiceAction.UnUnite);
    }
    aryButtonGroup = bt.getButtonArray();
    
    

  }

  /**
   * ѡ���ҵ�����ͱ仯ʱ���޸ġ����������������С���ť��ʾ
   */
  public void changeButtonsWhenBusiTypeSelected(ButtonObject bo) {
    // ����ҵ������
    m_boBusiType.setTag(bo.getTag());

    // ����
    PfUtilClient.retAddBtn(m_boAdd, SaleInvoiceTools.getLoginPk_Corp(),
        SaleBillType.SaleInvoice, bo);
    //��������
    PfUtilClient.retAddBtn(m_boRefAdd, SaleInvoiceTools.getLoginPk_Corp(), 
    	SaleBillType.SaleInvoice, bo);
//V55 ��ѡҵ���������������������۳��ⵥ�������Զ����뷢����Ʒ�����������ܿ�Ʊ��ť�ſ���
    String sVerifyrule = null;
    try {
      sVerifyrule = SaleinvoiceBO_Client.getVerifyrule(getBusiType());
    }
    catch (Exception ex) {
      nc.vo.scm.pub.SCMEnv.out(ex.getMessage());
    }
    boolean isBizAutoRegister = false;
    try{
      isBizAutoRegister = SaleinvoiceBO_Client.isBizAutoRegister(getBusiType());
    }catch(Exception e){
      nc.vo.scm.pub.SCMEnv.out(e);
    }
//    if ("W".equals(sVerifyrule)) {
    if(isBizAutoRegister){
      m_boAdd.addChildButton(m_boGather);
    }
  }

  public ButtonObject[] getApproveActionButtonAry() {

    return new ButtonObject[] {
        m_boDocument, m_boAuditFlowStatus, // ������״̬
        m_boApprove, m_boOrderQuery

    };
  }

}