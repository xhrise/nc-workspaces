/**
 * @(#)ClientEventHandler.java	V3.1 2007-3-11
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.sc.h0451510;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;
/**
 * 功能说明：派工单(审批)
 * @author 王兵
 * 2008-5-7 11:36:45
 */
public class ClientEventHandler extends ManageEventHandler {
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer sbWhere = new StringBuffer();
		if(askForQueryCondition(sbWhere)==false) 
			return;		
		String sqlWhere = sbWhere.toString();
		sqlWhere = sqlWhere.replaceFirst("审批不通过", "0");
		sqlWhere = sqlWhere.replaceFirst("审批通过", "1");
		sqlWhere = sqlWhere.replaceFirst("审批中", "2");
		sqlWhere = sqlWhere.replaceFirst("提交态", "3");
		sqlWhere = sqlWhere.replaceFirst("作废", "4");
		sqlWhere = sqlWhere.replaceFirst("冲销", "5");
		sqlWhere = sqlWhere.replaceFirst("终止", "6");
		sqlWhere = sqlWhere.replaceFirst("冻结状态", "7");
		sqlWhere = sqlWhere.replaceFirst("自由态", "8");
		
		SuperVO[] queryVos = queryHeadVOs(sqlWhere);
		
       getBufferData().clear();
       // 增加数据到Buffer
       addDataToBuffer(queryVos);

       updateBuffer();
	}

  
            
}
