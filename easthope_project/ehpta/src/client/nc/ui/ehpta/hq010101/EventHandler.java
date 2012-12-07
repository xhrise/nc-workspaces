package nc.ui.ehpta.hq010101;

import java.util.ArrayList;
import java.util.List;

import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * 
 * ������һ�������࣬��ҪĿ�������ɰ�ť�¼�����Ŀ��
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
			return;// �û������˲�ѯ

		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		queryVos = filterDate(queryVos);
		
		getBufferData().clear();
		// �������ݵ�Buffer
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
	
	@Override
	protected void onBoLineCopy() throws Exception {
		int selectedRow = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		if (selectedRow != -1) {
			CircularlyAccessibleValueObject[] vos = getBillCardPanelWrapper().getSelectedBodyVOs();
			
			/**
			 *  �������ǽ�ԭ������ɾ���� ������ɺ��������쳣��
			 */
			if(vos != null && vos.length > 0) {
				for(CircularlyAccessibleValueObject vo : vos) {
					vo.setPrimaryKey(null);
				}
			}
			
			getBillCardPanelWrapper().setCopyedBodyVOs(vos);

		}
	}
	
	@Override
	protected void onBoSave() throws Exception {

		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), getBillCardPanelWrapper().getBillVOFromUI(), ((ClientUICtrl)getUIController()).getBodyTableName());
		
		super.onBoSave();
	}

}