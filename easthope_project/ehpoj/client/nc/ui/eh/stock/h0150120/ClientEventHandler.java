/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.stock.h0150120;


import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * ����:�����ɹ��ƻ�
 * ZB17
 * @author WB
 * 2008-12-25 11:18:27
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	 //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_period"});
        if(res==1){
            getBillUI().showErrorMessage("��������ͬ����·ݣ������������");
            return;
        }
        super.onBoSave_withBillno();
    }
    
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    }
    
    
}

