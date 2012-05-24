package nc.ui.eh.cw.h1104005;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.cw.h1104005.ArapFyVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IBillStatus;

/**功能：费用单录入
 * @author 张起源
 * 时间：2008-5-29 10:45:29
 */

public class ClientEH extends ManageEventHandler {
    
	public ClientEH(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}


    @Override
	protected void onBoSave() throws Exception {
        //非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        super.onBoSave();
        setBoEnabled();
    }
    
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn) {
        case IEHButton.CONFIRMBUG:  //确认按钮
            setConfirmbug();
            break;
        case IEHButton.Prev:    //上一页 下一页
            onBoBrows(intBtn);
            break;
        case IEHButton.Next:    //上一页 下一页
            onBoBrows(intBtn);
            break;
        }
    }


    private void setConfirmbug() throws Exception {
        
//        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_flag", "Y");
//        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_psndoc", _getOperator());
//        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_rq", _getDate());
//        onBoSave();
//        setBoEnabled();
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ArapFyVO avo = (ArapFyVO) aggvo.getParentVO();
        String qr_flag = avo.getQr_flag()==null?"N":avo.getQr_flag().toString();
        String PrimaryKey = avo.getPrimaryKey()==null?"":avo.getPrimaryKey().toString();
        if("Y".equals(qr_flag)){
            getBillUI().showErrorMessage("该单据已经确认!");
            return;
        }
        else if(!PrimaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行确认操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                avo.setAttributeValue("qr_flag", new UFBoolean(true));
                avo.setAttributeValue("qr_psndoc", _getOperator());
                avo.setAttributeValue("qr_rq", _getDate());
                ivoPersistence.updateVO(avo);
                getBillUI().showWarningMessage("已经确认成功");
                onBoRefresh();
            }
            else{
                return;
            }
        }
        setBoEnabled();
    }
    
    //设置按钮的可用状态
    protected void setBoEnabled() throws Exception {
        ArapFyVO fyVO = (ArapFyVO)getBillUI().getVOFromUI().getParentVO();
        Integer vbillstatus=fyVO.getVbillstatus();
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
        boolean qr_flag = fyVO.getQr_flag().booleanValue();
        
        if (qr_flag){
            getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
            getButtonManager().getButton(IEHButton.CONFIRMBUG).setEnabled(false);
        }
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
        getBillUI().updateButtonUI();
        }
    }
    
    @Override
    protected void onBoCard() throws Exception {
        super.onBoCard();
        setBoEnabled();
    }
    private void onBoBrows(int intBtn) throws java.lang.Exception {
		// 动作执行前处理
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
		// 动作执行后处理
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
																	 * "转换第:" +
																	 * getBufferData().getCurrentRow() +
																	 * "页完成)"
																	 */
						);
         setBoEnabled();
	}


}
