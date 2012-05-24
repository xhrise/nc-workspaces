/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.kc.h0257515;


import nc.ui.eh.pub.IBillType;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;


/**
 * 功能:成品盘点单
 * ZB02
 * @author WB
 * 2008-10-16 13:36:14
 *
 */
public class ClientEventHandler extends nc.ui.eh.kc.h0257505.ClientEventHandler {
   
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @Override
	public void onBoSave() throws Exception {
        super.onBoSave();
    }
 
    @Override
    public String addCondtion() {
    	// TODO Auto-generated method stub
    	return " vbilltype = '" + IBillType.eh_h0257515 + "' ";
    }
    
    /***
     * 成品
     * @return
     */
    @Override
	public String getIntype(){
    	return "1";
    }
    
}

