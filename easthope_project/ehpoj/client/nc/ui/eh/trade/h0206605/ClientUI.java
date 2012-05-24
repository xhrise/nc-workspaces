/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.h0206605;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.businessref.LadingbillRefModel;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;

/**
 * ����:���������ͬ
 * ZA95
 * @author WB
 * 2008-10-10 11:35:11 
 *
 */
public class ClientUI extends AbstractClientUI{

	UIRefPane ref=null;
	public ClientUI() {
        super();
        ref=(UIRefPane) getBillCardPanel().getHeadItem("pk_ladingbills").getComponent();
        //���ò��տ��Զ�ѡ
		ref.setMultiSelectedEnabled(true);
		ref.setProcessFocusLost(false);
		ref.setButtonFireEvent(true);
		ref.setTreeGridNodeMultiSelected(true);
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO �Զ����ɷ������
        return new ClientCtrl();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public void setDefaultData() throws Exception {
        super.setDefaultData();
        getBillCardPanel().getHeadItem("ver").setValue(1);
    }
   
    @Override
    public void afterEdit(BillEditEvent e) {
    	String strKey=e.getKey();
    	
    	 if (e.getPos()==HEAD){
             if("pk_cubasdoc".equalsIgnoreCase(strKey)){
             	String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                     		getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
             	LadingbillRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������
             }
             if("pk_ladingbills".equalsIgnoreCase(strKey)){
            	String[] billnos = ref.getRefNames();
//            	String billno = ref.getRefName();
              	getBillCardPanel().setHeadItem("def_1",PubTools.combinArrayToString2( billnos));
              	String[] pk_ladingbills = ref.getRefPKs();
//              	String pk_ladingbill = ref.getRefPK();
              	/*******�������֪ͨ���������������� add by wb at 2008-11-18 10:48:08*****/
              	StringBuffer sql  = new StringBuffer()
              	.append(" select a.piece-isnull(b.yspiece,0) piece,a.amount-isnull(b.ysamount,0) amount  ")
              	.append(" from  (select sum(isnull(ladingamount,0)) piece,sum(isnull(zamount,0)) amount")
              	.append(" from eh_ladingbill_b where pk_ladingbill in "+PubTools.combinArrayToString(pk_ladingbills)+" ")
                .append(" and isnull(dr,0)=0) a,")
                .append(" (select sum(isnull(piece,0)) yspiece,sum(isnull(amount,0)) ysamount ")
                .append(" from eh_trade_transportcontract where pk_ladingbills = '"+1+"' and vbillstatus = 1 and isnull(dr,0)=0) b ");
              	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        		try {
            		ArrayList  arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            		if(arr!=null&&arr.size()>0){
        				HashMap hmA = (HashMap)arr.get(0);
        				UFDouble piece = new UFDouble(hmA.get("piece")==null?"0":hmA.get("piece").toString());		//����
        				UFDouble amount = new UFDouble(hmA.get("amount")==null?"0":hmA.get("amount").toString());	//��������
        				getBillCardPanel().setHeadItem("piece", piece);
        				getBillCardPanel().setHeadItem("amount", amount);
        			}
              }catch(Exception ex){
            	  ex.printStackTrace();
              }
             }
         }
        super.afterEdit(e);
    }
 
    /*
     * ע���Զ��尴ť
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
    	nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.DOCMANAGE, "��ͬ����", "��ͬ����");
    	btn.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
    	addPrivateButton(btn);
    	nc.vo.trade.button.ButtonVO btnBG = ButtonFactory.createButtonVO(
				IEHButton.STOCKCHANGE, "��ͬ���", "��ͬ���");
    	btnBG.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
    	addPrivateButton(btnBG);
    	nc.vo.trade.button.ButtonVO butt = ButtonFactory.createButtonVO(
    			IEHButton.CONFIRMBUG, "�ϴ���Ƭ", "�ϴ���Ƭ");
    	butt.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT});
    	this.addPrivateButton(butt);
        super.initPrivateButton();
    }
 
}

   
    

