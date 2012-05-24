
package nc.ui.eh.cw.h10112;

import nc.ui.eh.button.IEHButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;

/**
 * 说明: 电费基础
 * @author zqy
 * 时间：2008-9-10 14:58:09
 */

public class ClientEventHandler extends ManageEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
    @Override
	protected void onBoSave() throws Exception {
        //非空的有效性判断
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {
            //设置公司编码
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
        }
        //保存时不允许为空
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
       
        super.onBoSave();
     }

	@Override
	protected void onBoEdit() throws Exception {
        super.onBoEdit();
     }
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
        case IEHButton.Prev:    //上一页 下一页
            onBoBrows(intBtn);
            break;
        case IEHButton.Next:    //上一页 下一页
            onBoBrows(intBtn);
            break;
		}
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
     
	//设置按钮的可用状态
    protected void setBoEnabled() throws Exception {
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
    
   @Override
protected void onBoCard() throws Exception {
   	super.onBoCard();
   	setBoEnabled();
    }
}
