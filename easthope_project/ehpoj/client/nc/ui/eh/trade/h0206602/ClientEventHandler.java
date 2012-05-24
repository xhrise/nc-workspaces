/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.h0206602;


import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.h0206602.TradeDriverVO;

/**
 * ����:˾������
 * ZB08
 * @author WB
 * 2008-11-21 20:08:18
 *
 */

public class ClientEventHandler extends AbstractEventHandler {
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

   
	
    @Override
	public void onBoSave() throws Exception {
    	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
    	TradeDriverVO vo = (TradeDriverVO)getBillUI().getVOFromUI().getParentVO();
        String driverid = vo.getDriverid();					//��ʻԱ���֤��
        String sql = "select * from eh_trade_driver where  driverid = '"+driverid+"' and pk_corp = '"+_getCorp().getPk_corp()+"' and pk_driver <> '"+vo.getPk_driver()+"' and isnull(dr,0)=0";
        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
		if(arr!=null&&arr.size()>0){
			getBillUI().showErrorMessage("�˼�ʻԱ�Ѿ�����,���ܱ���!");
			return;
		}
    	super.onBoSave();
    }    
  
    /**
     * ������˽����޸� add by zqy 2010��11��23��15:49:46
     */
    protected void onBoEdit() throws Exception {
    	super.onBoEdit3();
    }
    
    /**
     * ������˽���ɾ��
     */
    protected void onBoDelete() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoDelete2();
    }

    
}

