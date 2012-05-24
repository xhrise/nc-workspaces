/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.h0206603;


import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.h0206603.TradeCarrecordVO;

/**
 * 功能:车辆档案
 * ZB09
 * @author WB
 * 2008-11-21 20:27:30
 *
 */

public class ClientEventHandler extends AbstractEventHandler {
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
	
    @Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	TradeCarrecordVO vo = (TradeCarrecordVO)getBillUI().getVOFromUI().getParentVO();
    	 //add by houcq begin 2010-11-18
        //String sql = "select * from eh_trade_carrecord where (carnumber = '"+vo.getCarnumber()+"' or enginecode = '"+vo.getEnginecode()+"' or shelfcode = '"+vo.getShelfcode()+"'" +
        //		     " or transportcode = '"+vo.getTransportcode()+"' or safetycode = '"+vo.getSafetycode()+"' or passescode = '"+vo.getPassescode()+"') and pk_car <> '"+vo.getPk_car()+"' and pk_corp = '"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0";
    	String sql = "select * from eh_trade_carrecord where (carnumber = '"+vo.getCarnumber()+"' and enginecode = '"+vo.getEnginecode()+"' and shelfcode = '"+vo.getShelfcode()+"'" +
	     " and transportcode = '"+vo.getTransportcode()+"' and safetycode = '"+vo.getSafetycode()+"' and passescode = '"+vo.getPassescode()+"') and pk_car = '"+vo.getPk_car()+"' and pk_corp = '"
	     +_getCorp().getPk_corp()+"' and isnull(dr,0)=0";
    	//add by houcq end 
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		if(arr!=null&&arr.size()>0){
			getBillUI().showErrorMessage("此车辆信息已经存在,不能保存!");
			return;
		}
    	super.onBoSave();
    }

    /**
     * 允许比人进行修改 add by zqy 2010年11月23日15:51:53
     */
    protected void onBoEdit() throws Exception {
    	super.onBoEdit3();
    }
    
    /**
     * 允许比人进行删除 add by zqy 2010年11月23日15:52:11
     */
    protected void onBoDelete() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoDelete2();
    }
    
}

