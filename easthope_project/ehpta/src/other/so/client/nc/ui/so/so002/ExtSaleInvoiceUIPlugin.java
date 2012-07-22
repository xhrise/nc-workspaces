package nc.ui.so.so002;

import java.awt.event.ActionEvent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.plugin.Action;

@SuppressWarnings("restriction")
public class ExtSaleInvoiceUIPlugin implements IScmUIPlugin {

	public boolean init(SCMUIContext ctx) {
		return false;
	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
	}

	public boolean beforeEdit(BillEditEvent e, SCMUIContext ctx) {
		return false;
	}

	public void afterEdit(BillEditEvent e, SCMUIContext ctx) {
		
	}

	public void bodyRowChange(BillEditEvent e, SCMUIContext ctx) {
		
	}

	public void mouse_doubleclick(BillMouseEnent e, SCMUIContext ctx) {
		
	}

	public void onMenuItemClick(ActionEvent e, SCMUIContext ctx) {
		
	}

	public void beforeAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		
	}

	public void afterAction(Action action, AggregatedValueObject[] billvos,
			SCMUIContext conx) throws BusinessException {
		
	}

	public void setButtonStatus(SCMUIContext conx) {
		
	}

	public void beforeSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		
	}

	public void afterSetBillVOToCard(AggregatedValueObject billvo,
			SCMUIContext conx) {
		
	}

	public void beforeSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		
	}

	public void afterSetBillVOsToListHead(
			CircularlyAccessibleValueObject[] headvos, SCMUIContext conx) {
		
	}

	public void beforeSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		
	}

	public void afterSetBillVOsToListBody(
			CircularlyAccessibleValueObject[] bodyvos, SCMUIContext conx) {
		
	}

	public void onAddLine(SCMUIContext conx) throws BusinessException {
		
	}

	public void onPastLine(SCMUIContext conx) throws BusinessException {
		
	}

	public String onQuery(String swhere, SCMUIContext conx)
			throws BusinessException {
		return null;
	}

	public boolean beforeEdit(BillItemEvent e, SCMUIContext conx) {
		return false;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVo, AggregatedValueObject[] nowVo)
			throws BusinessException {
		return null;
	}

	public Object[] retBillToBillRefVOs(
			CircularlyAccessibleValueObject[] headVos,
			CircularlyAccessibleValueObject[] bodyVos) throws BusinessException {
		return null;
	}

}
