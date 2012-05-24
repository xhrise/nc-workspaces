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
 * ����˵�����ɹ���(����)
 * @author ����
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
		sqlWhere = sqlWhere.replaceFirst("������ͨ��", "0");
		sqlWhere = sqlWhere.replaceFirst("����ͨ��", "1");
		sqlWhere = sqlWhere.replaceFirst("������", "2");
		sqlWhere = sqlWhere.replaceFirst("�ύ̬", "3");
		sqlWhere = sqlWhere.replaceFirst("����", "4");
		sqlWhere = sqlWhere.replaceFirst("����", "5");
		sqlWhere = sqlWhere.replaceFirst("��ֹ", "6");
		sqlWhere = sqlWhere.replaceFirst("����״̬", "7");
		sqlWhere = sqlWhere.replaceFirst("����̬", "8");
		
		SuperVO[] queryVos = queryHeadVOs(sqlWhere);
		
       getBufferData().clear();
       // �������ݵ�Buffer
       addDataToBuffer(queryVos);

       updateBuffer();
	}

  
            
}
