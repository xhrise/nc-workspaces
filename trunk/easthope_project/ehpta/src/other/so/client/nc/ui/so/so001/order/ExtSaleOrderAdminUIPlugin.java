package nc.ui.so.so001.order;

import java.awt.event.ActionEvent;

import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.scm.plugin.Action;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;

@SuppressWarnings("restriction")
public class ExtSaleOrderAdminUIPlugin implements IScmUIPlugin {

	public boolean init(SCMUIContext ctx) {
		return false;
	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {

		if("保存".equals(bo.getName())) {
			BillItem contypeItem = ctx.getBillCardPanel().getBillData().getHeadItem("contracttype");
			if(contypeItem != null && contypeItem.getValueObject() != null) {
				BillItem conItem = ctx.getBillCardPanel().getBillData().getHeadItem("pk_contract");
				
				switch((Integer) contypeItem.getValueObject()) {
				
					case 10 :
						
						System.out.println("现货合同");
						break;
						
					case 20 :
						
						System.out.println("长单合同");
						if(conItem != null && conItem.getValueObject() != null) {
							CircularlyAccessibleValueObject[] bodyVOs = ctx.getBillCardPanel().getBillData().getBodyValueVOs(SaleorderBVO.class.getName());
							
						}
						
						break;
					
					default :
						break;
						
				}
			}
		}
	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
		if("刷新".equals(bo.getName()) || "查询".equals(bo.getName()) || "增加".equals(bo.getParent() == null ? bo.getName() : bo.getParent().getName())) {
			
			ExtSaleOrderAdminUI saleUI = (ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel();
			
			int num = ctx.getBillListPanel().getHeadTable().getSelectedRow();
			String csaleid = null;
			if(num == -1)
				csaleid = (String) ctx.getBillCardPanel().getHeadItem("csaleid").getValueObject();
			else 
				csaleid = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
			
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
