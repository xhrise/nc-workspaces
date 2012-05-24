/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.jf.h0700302;


import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;

/**
 * 功能:积分兑换
 * ZB48
 * @author houcq56
 * 2011-11-02 13:48:06
 *
 */
public class ClientEventHandler extends AbstractEventHandler {
   
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
    @SuppressWarnings("unchecked")
	@Override
	public void onBoSave() throws Exception {
    	

         super.onBoSave_withBillno();	
         
    }
    
    @Override 
    protected void onBoEdit() throws Exception 
    {
    	 super.onBoEdit();
         
    }
    @Override
	protected void onBoDelete() throws Exception {
	   	
    	super.onBoTrueDelete();
	}
    
    public void onButton_N(ButtonObject bo, BillModel model) {
		super.onButton_N(bo, model);	    
	}
    
    @Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    }
   
    @Override
    protected void onBoLineDel() throws Exception {    	 
    	super.onBoLineDel();
    }
    @Override
	public String addCondtion(){
    	return " vbilltype = '"+IBillType.eh_h0700302+"'";
    }
    
}

