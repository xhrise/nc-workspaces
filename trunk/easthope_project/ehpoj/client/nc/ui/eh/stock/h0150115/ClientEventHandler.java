package nc.ui.eh.stock.h0150115;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150110.StockSpecplanVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：特殊采购（审批）
 * @author wb
 * 时间:2009-2-9 11:08:23
 */

public class ClientEventHandler extends AbstractSPEventHandler {
    
		
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
	@Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.SpecialCG:   
                onBoOacode();
                break;
        }   
    }

	@Override
	public void onBoAudit() throws Exception {
		StockSpecplanVO hvo = (StockSpecplanVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	int invtype = hvo.getInvtype();
    	String[] invtypes = ICombobox.SpecCG_flag;
    	String oacode = hvo.getOaspcode();		//OA审批单号
    	if((oacode==null||oacode.length()==0)&&(invtype==1||invtype==2)){		//在2、3中类型时必须录入审批单号
    		getBillUI().showErrorMessage("在原料类型为:"+invtypes[invtype]+" 时必须录入OA审批单号!");
    		return;
    	}
		super.onBoAudit();
	}
	/**
	 * OA审批单号
	 * @throws Exception 
	 *
	 */
	private void onBoOacode() throws Exception {
		int res = getBillUI().showOkCancelMessage("是否确认输入OA审批单号?");
		if(res==UIDialog.ID_OK){
			onBoEdit();
			BillItem[] headitems = getBillCardPanelWrapper().getBillCardPanel().getHeadItems();
			for(int i=0;i<headitems.length;i++){
				String item = headitems[i].getKey();
				if(item!=null&&!item.equals("oaspcode")){
					headitems[i].setEnabled(false);
				}
			}
			
			getBillCardPanelWrapper().getBillCardPanel().getBillModel().setEnabled(false);
			
			getButtonManager().getButton(IBillButton.Audit).setEnabled(false);
			getBillUI().updateButtons();
		};
	}
	
	//设置按钮的可用状态
    @Override
	public void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        if (vbillstatus==null){
        }
        else{   
            switch (vbillstatus.intValue()){
                //free
                case IBillStatus.FREE:
                        getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(false);
                        break;
                //commit
                case IBillStatus.COMMIT:
                    getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(true);
                    break;
                //CHECKGOING
                case IBillStatus.CHECKGOING:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(true);
                    break;
                //CHECKPASS
                case IBillStatus.CHECKPASS:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(false);
                	break;
                //NOPASS
                case IBillStatus.NOPASS:
                	getButtonManager().getButton(IEHButton.SpecialCG).setEnabled(true);
                    break;
                
            }
        }
        getBillUI().updateButtonUI();
        super.setBoEnabled();
    }
    
    @Override
    protected void onBoSave() throws Exception {
    	super.onBoSave();
    	setBoEnabled();
    }
}
