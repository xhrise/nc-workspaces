package nc.ui.ehpta.hq010101;

import java.util.ArrayList;

import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.ehpta.hq010101.EhptaTransportVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.trade.pub.IExAggVO;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoCommit() throws Exception {
		AggregatedValueObject modelVo = getBufferData().getCurrentVOClone();
		modelVo.getParentVO().setAttributeValue(
				getBillField().getField_Operator(), getBillUI()._getOperator());
		beforeOnBoAction(28, modelVo);
		String strTime = ClientEnvironment.getServerTime().toString();
		ArrayList retList = getBusinessAction()
				.commit(modelVo,
						getUIController().getBillType(),
						(new StringBuilder(String.valueOf(getBillUI()
								._getDate().toString()))).append(
								strTime.substring(10)).toString(),
						getBillUI().getUserObject());
		if (PfUtilClient.isSuccess()) {
			Object o = retList.get(1);
			if (o instanceof AggregatedValueObject) {
				AggregatedValueObject retVo = (AggregatedValueObject) o;
				afterOnBoAction(28, retVo);
				CircularlyAccessibleValueObject childVos[] = getChildVO(retVo);
				if (childVos == null)
					modelVo.setParentVO(retVo.getParentVO());
				else
					modelVo = retVo;
			}
			getBufferData().setVOAt(getBufferData().getCurrentRow(), modelVo);
			getBufferData().setCurrentRow(getBufferData().getCurrentRow());
		}

		updateBuffer();
	}

	private CircularlyAccessibleValueObject[] getChildVO(
			AggregatedValueObject retVo) {
		CircularlyAccessibleValueObject childVos[] = (CircularlyAccessibleValueObject[]) null;
		if (retVo instanceof IExAggVO)
			childVos = ((IExAggVO) retVo).getAllChildrenVO();
		else
			childVos = retVo.getChildrenVO();
		return childVos;
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		if (intBtn == DefaultBillButton.DISABLED) {
			setDisable(true);
		} else if (intBtn == DefaultBillButton.ENABLED) {
			setDisable(false);
		}
	}

	private void setDisable(boolean b) throws Exception {
		EhptaTransportVO mainVO = (EhptaTransportVO) getBufferData()
				.getCurrentVO().getParentVO();
		if (mainVO != null && mainVO.getAttributeValue("pk_transport") != null) {
			if (b) {
				mainVO.setStopdate(_getDate());
				mainVO.setStopstatus(new UFBoolean(b));
			} else {
				mainVO.setStopdate(null);
				mainVO.setStopstatus(new UFBoolean(b));
			}
			HYPubBO_Client.update(mainVO);
		}
		Integer currentRow = getBufferData().getCurrentRow();
		updateBuffer();
		getBufferData().setCurrentRow(currentRow);
	}

}