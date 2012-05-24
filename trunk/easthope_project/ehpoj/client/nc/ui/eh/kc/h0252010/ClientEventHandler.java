package nc.ui.eh.kc.h0252010;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;



/**
 * 功能说明：材料调拨单审批
 * @author 王明
 * 2008-05-07 下午04:03:18
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
