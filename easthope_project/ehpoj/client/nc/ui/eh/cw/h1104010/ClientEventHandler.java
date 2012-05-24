package nc.ui.eh.cw.h1104010;

import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBillStatus;

/** 
 * 说明：费用单审批
 * @author wb
 * 时间：2008-8-20 20:44:15
 */
public class ClientEventHandler extends AbstractEventHandler {
    
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
    
    @Override
	public void onBoAudit() throws Exception {
        String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
        if(pk==null||pk.length()==0){
            getBillUI().showErrorMessage("请选择一张单据!");
            return;
        }
        int iRet = getBillUI().showYesNoMessage("是否确认审批此费用单吗?");
        if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
             super.onBoAudit();
             setBoEnabled();   
        }else{
             return;
        }
        
    }
    
    //设置按钮的可用状态
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