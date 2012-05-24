package nc.ui.eh.kc.h0251010;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵�����������ϳ��ⵥ 
 * @author ����Դ 
 * ʱ�䣺2008-5-08
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		// �ǿ��ж�
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		
		 //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();       
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("ԭ�ϱ����Ѿ����ڣ������������");
            return;
        }        
		super.onBoSave();
	}
	
//	//������ͬ�ı�ͨ�������������֣�����ѯ����
//	protected void onBoQuery() throws Exception {
//		StringBuffer strWhere = new StringBuffer();
//		if (askForQueryCondition(strWhere) == false)
//			return;// �û������˲�ѯ
//	
//		//��ѯ����
//		String billtype = IBillType.eh_h0251010;
//		SuperVO[] queryVos = queryHeadVOs(strWhere.toString()+" and (vbilltype = '"+billtype+"') ");
//
//		getBufferData().clear();
//		// �������ݵ�Buffer
//		addDataToBuffer(queryVos);
//		updateBuffer();	
//	}
    
	@Override
	public String addCondtion() {
		// TODO Auto-generated method stub
		return "vbilltype = '"+IBillType.eh_h0251010+"'";
	}
	
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.Prev:    //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //��һҳ ��һҳ
                onBoBrows(intBtn);
                break;
        }   
    }
    
    private void onBoBrows(int intBtn) throws java.lang.Exception {
        // ����ִ��ǰ����
        buttonActionBefore(getBillUI(), intBtn);
        switch (intBtn) {
        case IEHButton.Prev: {
            getBufferData().prev();
            break;
        }
        case IEHButton.Next: {
            getBufferData().next();
            break;
        }
        }
        // ����ִ�к���
        buttonActionAfter(getBillUI(), intBtn);
        getBillUI().showHintMessage(
                nc.ui.ml.NCLangRes.getInstance()
                        .getStrByID(
                                "uifactory",
                                "UPPuifactory-000503",
                                null,
                                new String[] { nc.vo.format.Format
                                        .indexFormat(getBufferData()
                                                .getCurrentRow()+1) })/*
                                                                     * @res
                                                                     * "ת����:" +
                                                                     * getBufferData().getCurrentRow() +
                                                                     * "ҳ���)"
                                                                     */
                        );
          setBoEnabled();
    }

    @Override
	protected void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        if (vbillstatus==null){
        }
        else{   
            switch (vbillstatus.intValue()){
                //free
                case IBillStatus.FREE:
                        getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
                        getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
                        break;
                //commit
                case IBillStatus.COMMIT:
                    getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    break;
                //CHECKGOING
                case IBillStatus.CHECKGOING:
                    getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    break;
                //CHECKPASS
                case IBillStatus.CHECKPASS:
                
                //NOPASS
                case IBillStatus.NOPASS:
                        getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                        getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                        break;
                
            }
            //��һҳ ��һҳ�İ�ť״̬  add by wb at 2008-6-20 14:30:23
            if(getButtonManager().getButton(IEHButton.Prev)!=null){
                if(!getBufferData().hasPrev()){
                    getButtonManager().getButton(IEHButton.Prev).setEnabled(false);
                }
                else{
                    getButtonManager().getButton(IEHButton.Prev).setEnabled(true);
                }
                if(!getBufferData().hasNext()){
                    getButtonManager().getButton(IEHButton.Next).setEnabled(false);
                }
                else{
                    getButtonManager().getButton(IEHButton.Next).setEnabled(true);
                }
            }
        }
    }
	
 }
    
