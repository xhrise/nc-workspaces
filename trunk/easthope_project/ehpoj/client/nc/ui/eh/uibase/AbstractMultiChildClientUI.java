package nc.ui.eh.uibase;

/**
 * ��ҳǩUI����
 * @author ����
 * �������� 2009��3��27��16:06:16
 */



import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.multichild.MultiChildBillManageUI;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.IBillStatus;


public class AbstractMultiChildClientUI extends MultiChildBillManageUI {
	
	public AbstractMultiChildClientUI() {
		super();
	}
	public AbstractMultiChildClientUI(String pk_corp, String pk_billType, String pk_busitype,
			String operater, String billId) {
		super(pk_corp, pk_billType, pk_busitype, operater, billId);
	}
	@Override
	protected AbstractManageController createController() {
		return null;
	}
	@Override
	protected ManageEventHandler createEventHandler() {
		return new AbstractEventHandler(this, this.getUIControl());
	}
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return null;
	}
	
	@Override
	protected void initSelfData() {
		//������
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);		
	}
	
	@Override
	public void setDefaultData() throws Exception {		
		 //��ͷ���õ�
        String pk_corp = _getCorp().getPrimaryKey();
        BillItem oper = getBillCardPanel().getTailItem("coperatorid");
        if (oper != null)
            oper.setValue(_getOperator());
        else
            getBillCardPanel().getHeadItem("coperatorid").setValue(_getOperator());
        BillItem date = getBillCardPanel().getTailItem("dmakedate");
        if (date != null)
            date.setValue(_getDate());
        else
            getBillCardPanel().getHeadItem("dmakedate").setValue(_getDate());
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null)
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
       
        getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);
        
        BillItem vbilltype = getBillCardPanel().getHeadItem("vbilltype");
        if(vbilltype!=null){
        	getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());
        }
        
        BillItem vbillstatus = getBillCardPanel().getHeadItem("vbillstatus");
        if (vbillstatus!= null)
        	getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
	}
	
	@Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		
	}
	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		
	}
	/*
	 * (non-Javadoc) @����˵�����Զ��尴ť
	 */
	@Override
	protected void initPrivateButton() {
		 nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"��һҳ","��һҳ");
    	 btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnPrev);
         nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"��һҳ","��һҳ");
         btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnNext);
         nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.BusinesBtn,"ҵ�����","ҵ�����");
         btnBus.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnBus);
         super.initPrivateButton();
	}
	
	 @Override
	public void  afterEdit(BillEditEvent e) {
        String strKey=e.getKey();
         if(e.getPos()==HEAD&&getBillCardPanel().getHeadItem(strKey)!=null){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execHeadFormulas(formual);

            }else if (e.getPos()==BODY&&getBillCardPanel().getBodyItem(strKey)!=null){
                String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            }else{
                getBillCardPanel().execTailEditFormulas();
            }
    }
	 
	
}