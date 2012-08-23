package nc.ui.ehpta.hq010101;

import java.util.ArrayList;
import java.util.List;

import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {

	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	protected void onBoElse(int intBtn) throws Exception {
	}
	
	@Override
	protected void onBoQuery() throws Exception {
		StringBuffer strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		queryVos = filterDate(queryVos);
		
		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();
	}
	
	protected final SuperVO[] filterDate(SuperVO[] queryVos) throws Exception {
		
		List<SuperVO> filterList = new ArrayList<SuperVO>();
		
		if("HQ02".equals(getUIController().getBillType())) {
			for(SuperVO vo : queryVos) {
				if("upper".equals(vo.getAttributeValue("transtype")))
					filterList.add(vo);
			}
		} else if("HQ08".equals(getUIController().getBillType())) {
			for(SuperVO vo : queryVos) {
				if("under".equals(vo.getAttributeValue("transtype")))
					filterList.add(vo);
			}
		}
		
		return filterList.toArray(new SuperVO[0]);
		
	}

}