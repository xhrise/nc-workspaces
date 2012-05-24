package nc.ui.eh.cw.h1103010;

import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵�����տ (����)
 * @author ����Դ 
 * ʱ�䣺2008-5-28 14:36:14
 */
public class ClientEventHandler extends AbstractEventHandler {
    
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
    
    @Override
	public void onBoAudit() throws Exception {
        String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
        if(pk==null||pk.length()==0){
            getBillUI().showErrorMessage("��ѡ��һ�ŵ���!");
            return;
        }
        int iRet = getBillUI().showYesNoMessage("�Ƿ�ȷ���������տ?");
        if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
             super.onBoAudit();
             setBoEnabled();   
        }else{
             return;
        }
        
    }
    
    //���ð�ť�Ŀ���״̬
    @Override
	protected void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        Integer vbillstatus = (Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        
        if (vbillstatus==null){
        }
        else{   
            if(vbillstatus==IBillStatus.CHECKPASS){
                getButtonManager().getButton(IBillButton.Audit).setEnabled(false);
            }else{
                getButtonManager().getButton(IBillButton.Audit).setEnabled(true);
            }
        }
        getBillUI().updateButtonUI();
    }

     @Override
    protected void onBoCard() throws Exception {
        super.onBoCard();
        setBoEnabled();         
    }
	
}