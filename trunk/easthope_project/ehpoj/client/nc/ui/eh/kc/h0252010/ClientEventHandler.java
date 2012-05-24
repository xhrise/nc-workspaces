package nc.ui.eh.kc.h0252010;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;



/**
 * ����˵�������ϵ���������
 * @author ����
 * 2008-05-07 ����04:03:18
 */

public class ClientEventHandler extends ManageEventHandler{
	
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
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
