/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.stock.h0150305;


import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.h0205615.TradePeriodplanVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能:五金采购申请
 * ZB22
 * @author WB
 * 2009-1-5 15:33:07
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
	TradePeriodplanVO hvo = null;
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	//唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("表体有相同物料存在，不允许操作！");
            return;
        }
        //add by houcq 2011-10-24 begin
        /*
         * 1、库存量字段，根据登录制单日期，带出实时的库存情况。
			2、当申请数量小于库存数量时，在保存时，系统提示：‘该物料尚有库存，无需申请购买，请确认再制单！’点确定后，返回操作。
         */
        int rows = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < rows; i++) {	
        	UFDouble kcamount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kcamount").toString());	               
            UFDouble amount = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "amount")==null?new UFDouble(0):
            	new UFDouble(getBillCardPanelWrapper().getBillCardPanel().
            			getBodyValueAt(i, "amount").toString());	            
                     
            if(kcamount.sub(amount).toDouble()>0){
            	getBillUI().showErrorMessage("第("+(i+1)+"行)物料尚有库存，无需申请购买，请确认再制单!");
            	return ;
            }            
        }
      //add by houcq 2011-10-24 end
        super.onBoSave_withBillno();
    }
    
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    }
    
    
}

