package nc.ui.so.so001.order;

import java.awt.event.ActionEvent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.plugin.Action;
import nc.vo.so.so001.SaleOrderVO;

@SuppressWarnings("restriction")
public class ExtSaleOrderAdminUIPlugin implements IScmUIPlugin {

	public boolean init(SCMUIContext ctx) {
		return false;
	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {

	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
		ExtSaleOrderAdminUI saleUI = (ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel();
		
		if("增加".equals(bo.getParent() == null ? bo.getName() : bo.getParent().getName())) {
			
			if(ctx.getBillCardPanel().getHeadItem("contracttype") != null) {
				if(PfUtilClient.getRetVos() != null && PfUtilClient.getRetVos().length > 0) {
					if(Integer.valueOf(PfUtilClient.getRetVos()[0].getParentVO().getAttributeValue("contracttype") == null ? "0" : PfUtilClient.getRetVos()[0].getParentVO().getAttributeValue("contracttype").toString()) == 10)
						((UIComboBox) ctx.getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("现货合同");
					else if(Integer.valueOf(PfUtilClient.getRetVos()[0].getParentVO().getAttributeValue("contracttype") == null ? "0" : PfUtilClient.getRetVos()[0].getParentVO().getAttributeValue("contracttype").toString()) == 20)
						((UIComboBox) ctx.getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("长单合同");
				}
			}
		}
		
		if("卡片显示".equals(bo.getName())) {
			
			int num = ctx.getBillListPanel().getHeadTable().getSelectedRow();

			String csaleid = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
			
			SaleOrderVO saleorder = saleUI.vocache.getSaleOrderVO(csaleid);
			if(ctx.getBillCardPanel().getHeadItem("contracttype") != null) {
				if(saleorder != null && saleorder.getParentVO() != null) {
					if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 10)
						((UIComboBox) ctx.getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("现货合同");
					else if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 20)
						((UIComboBox) ctx.getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("长单合同");
				}
			}
		}
		
		if("刷新".equals(bo.getName()) || "查询".equals(bo.getName())) {
						
			String csaleid = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(0 ,"csaleid");
			
			SaleOrderVO saleorder = saleUI.vocache.getSaleOrderVO(csaleid);
			if(ctx.getBillCardPanel().getHeadItem("contracttype") != null) {
				if(saleorder != null && saleorder.getParentVO() != null) {
					if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 10)
						((UIComboBox) ctx.getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("现货合同");
					else if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 20)
						((UIComboBox) ctx.getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("长单合同");
				}
			}
		}

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
