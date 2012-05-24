package nc.ui.eh.kc.h0251510;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;



/**
 * 功能说明：生产备料单（审批）
 * @author 张起源
 * 时间：2008年5月19日17:21:43 
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
