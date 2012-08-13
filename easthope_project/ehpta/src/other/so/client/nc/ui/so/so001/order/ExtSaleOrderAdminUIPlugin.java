package nc.ui.so.so001.order;

import java.awt.event.ActionEvent;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

@SuppressWarnings({"restriction" , "rawtypes"})
public class ExtSaleOrderAdminUIPlugin implements IScmUIPlugin {

	public boolean init(SCMUIContext ctx) {
		return true;
	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {

		if("保存".equals(bo.getName())) 
			beforeOnBoSave(ctx);
		
		else if("审核".equals(bo.getName()))
			beforeOnBoAudit(ctx);
		
	}
	
	private final void beforeOnBoSave(SCMUIContext ctx) throws BusinessException {
		BillItem contypeItem = ctx.getBillCardPanel().getBillData().getHeadItem("contracttype");
		if(contypeItem != null && contypeItem.getValueObject() != null) {
			BillItem conItem = ctx.getBillCardPanel().getBillData().getHeadItem("pk_contract");
			
			if(conItem == null || conItem.getValueObject() == null || "".equals(conItem.getValueObject()))
				throw new BusinessException ("销售合同不能为空！");
			
			Object mny = UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(sum(mny),0) from ehpta_adjust where pk_contract = '"+conItem.getValueObject()+"' and vbillstatus = 1 and nvl(dr,0)=0 ", new ColumnProcessor());
			
			UFDouble sumMny = new UFDouble(mny.toString());
			
			BillItem headsummnyItem = ctx.getBillCardPanel().getBillData().getHeadItem("nheadsummny");
			if(headsummnyItem == null || headsummnyItem.getValueObject() == null || "".equals(headsummnyItem.getValueObject()))
				throw new BusinessException ("表头 ： 整单加税合计金额 不能为空！");
			
			
			switch(Integer.valueOf(contypeItem.getValueObject().toString())) {
			
				case 10 :
					
					Logger.info(" 现货合同 : Action -> Save ");
					
					AggregatedValueObject billVO = ctx.getBillCardPanel().getBillData().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
					
					if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
						UFDouble nnumber = new UFDouble("0");
						for(CircularlyAccessibleValueObject bodyVO : billVO.getChildrenVO()) {
							nnumber = nnumber.add(Double.valueOf(bodyVO.getAttributeValue("nnumber").toString()));
						}
						
						Object num = UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(sum(num),0) from ehpta_sale_contract_bs where pk_contract = '"+conItem.getValueObject()+"' and nvl(dr,0)=0 ", new ColumnProcessor());
						
						if(nnumber.doubleValue() > new UFDouble(num.toString()).doubleValue())
							((ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel()).showWarningMessage("提货数量大于销售合同总数量");
					
					}
					
					if(sumMny.sub(Double.valueOf(headsummnyItem.getValueObject().toString())).doubleValue() < 0) 
						((ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel()).showWarningMessage("合同余额小于本次提货金额");
					
					break;
					
				case 20 :
					
					Logger.info(" 长单合同 : Action -> Save ");
					
					billVO = ctx.getBillCardPanel().getBillData().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
					
					if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
						UFDouble nnumber = new UFDouble("0");
						for(CircularlyAccessibleValueObject bodyVO : billVO.getChildrenVO()) {
							nnumber = nnumber.add(Double.valueOf(bodyVO.getAttributeValue("nnumber").toString()));
						}
						
						Vector retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery("select contb.num , contb.sdate , contb.edate from ehpta_sale_contract_b contb left join ehpta_sale_contract cont on cont.pk_contract = contb.pk_contract where cont.pk_contract = '"+billVO.getParentVO().getAttributeValue("pk_contract")+"' and nvl(cont.dr,0)=0 and nvl(contb.dr,0)=0", new VectorProcessor());
						if(retVector == null || retVector.size() == 0)
							throw new BusinessException("未找到来源合同！");
						
						UFDouble totalnum = new UFDouble("0");
						for(Object vct : retVector) {
							Object num = ((Vector)vct).get(0);
							Object sdate = ((Vector)vct).get(1);
							Object edate = ((Vector)vct).get(2);
							
							Object period = ctx.getBillCardPanel().getBillData().getHeadItem("period") == null ? null : ((UIRefPane)ctx.getBillCardPanel().getBillData().getHeadItem("period").getComponent()).getRefName();
							
							if(period == null || "".equals(period))
								throw new BusinessException("表头：执行期间 不能为空！");
							
							if(period.toString().compareTo(sdate.toString()) >= 0 && period.toString().compareTo(edate.toString()) <= 0) {
								totalnum = totalnum.add(new UFDouble(num.toString()));
							}
							
						}
						
						if(nnumber.doubleValue() > totalnum.doubleValue())
							((ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel()).showWarningMessage("提货数量大于销售合同当前期间执行量");
					}
					
					Object nheadsummny =  UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(sum(nheadsummny),0) from so_sale where pk_contract is not null and nvl(dr,0)=0 and (contracttype = 10 or contracttype = 20 ) and pk_contract = '"+conItem.getValueObject()+"'", new ColumnProcessor());
					if(sumMny.sub(new UFDouble(nheadsummny.toString())).sub(Double.valueOf(headsummnyItem.getValueObject().toString())).doubleValue() < 0)
						((ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel()).showWarningMessage("合同余额小于本次提货金额");
					
					break;
				
				default :
					break;
					
			}
		}
	}

	private final void beforeOnBoAudit(SCMUIContext ctx) throws BusinessException {
		BillItem contypeItem = ctx.getBillCardPanel().getBillData().getHeadItem("contracttype");
		if(contypeItem != null && contypeItem.getValueObject() != null) {
			BillItem conItem = ctx.getBillCardPanel().getBillData().getHeadItem("pk_contract");
			
			if(conItem == null || conItem.getValueObject() == null || "".equals(conItem.getValueObject()))
				throw new BusinessException ("销售合同不能为空！");
			
			Object mny = UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(sum(mny),0) from ehpta_adjust where pk_contract = '"+conItem.getValueObject()+"' and vbillstatus = 1 and nvl(dr,0)=0 ", new ColumnProcessor());
			
			UFDouble sumMny = new UFDouble(mny.toString());
			
			Object nheadsummny =  UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(sum(nheadsummny),0) from so_sale where pk_contract is not null and nvl(dr,0)=0 and contracttype >= 10 and pk_contract = '"+conItem.getValueObject()+"'", new ColumnProcessor());
			if(sumMny.sub(new UFDouble(nheadsummny.toString())).doubleValue() < 0)
				throw new BusinessException("合同余额小于本次提货金额，审核失败!");
			
		}
	}
	
	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
		if("合同余额".equals(bo.getName())) {
			
			int num = ctx.getBillListPanel().getHeadTable().getSelectedRow();
			Object[] obj = new Object[4];
			if(num == -1) {
				obj[0] = (String) ctx.getBillCardPanel().getHeadItem("pk_contract").getValueObject();
				obj[1] = "";
				obj[2] = (String) ctx.getBillCardPanel().getHeadItem("csaleid").getValueObject();
				obj[3] = (String) ctx.getBillCardPanel().getHeadItem("concode").getValueObject();
			} else { 
				obj[0] = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "pk_contract");
				obj[1] = "";
				obj[2] = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
				obj[3] = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "concode");
			}
			
			SaleContractBalanceDlg balanceDlg = new SaleContractBalanceDlg(ctx.getIctxpanel().getToftPanel() , obj);
			balanceDlg.showModal();
		}
		
	}

	public boolean beforeEdit(BillEditEvent e, SCMUIContext ctx) {
		
		return true;
	}

	public void afterEdit(BillEditEvent e, SCMUIContext ctx) {
		
		try {
			if(!(e.getSource() instanceof BillCellEditor)) {
				if("pk_transport".equals(e.getKey())) 
					afterSetPk_transport(e , ctx);
					
				if("storage".equals(e.getKey()))
					afterSetStorage(e , ctx);
			}
		} catch(Exception ex) {
			Logger.error(e);
		}

	}
	
	private final void afterSetPk_transport(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		Object pk_transport = ctx.getBillCardPanel().getHeadItem(e.getKey()).getValueObject();
		
		StringBuilder builder = new StringBuilder();
		builder.append(" select bas.custname , addr.addrname from bd_cubasdoc bas left join bd_cumandoc man on man.pk_cubasdoc = bas.pk_cubasdoc ");
		builder.append(" left join ehpta_transport_contract transcont on transcont.pk_carrier = man.pk_cumandoc ");
		builder.append(" left join bd_custaddr addr on addr.pk_cubasdoc = bas.pk_cubasdoc ");
		builder.append(" where transcont.pk_transport = '"+pk_transport+"' and addr.defaddrflag = 'Y' ");
		
		Vector retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery(builder.toString(), new VectorProcessor());
		
		if(retVector == null || retVector.size() == 0) {
			builder = new StringBuilder();
			builder.append(" select bas.custname , addr.addrname from bd_cubasdoc bas left join bd_cumandoc man on man.pk_cubasdoc = bas.pk_cubasdoc ");
			builder.append(" left join ehpta_transport_contract transcont on transcont.pk_carrier = man.pk_cumandoc ");
			builder.append(" left join bd_custaddr addr on addr.pk_cubasdoc = bas.pk_cubasdoc ");
			builder.append(" where transcont.pk_transport = '"+pk_transport+"' and rownum = 1  ");
			retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery(builder.toString(), new VectorProcessor());
		}
		
		if(retVector != null && retVector.size() > 0) {
			Object custname = ((Vector)retVector.get(0)).get(0);
			Object addrname = ((Vector)retVector.get(0)).get(1);
			
			ctx.getBillCardPanel().getHeadItem("carriersname").setValue(custname);
			ctx.getBillCardPanel().getHeadItem("carriersaddr").setValue(addrname);
			
		}
	}
	
	private final void afterSetStorage(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		Object pk_stordoc = ctx.getBillCardPanel().getHeadItem(e.getKey()).getValueObject();
		
		Object storaddr = UAPQueryBS.iUAPQueryBS.executeQuery("select storaddr from bd_stordoc where pk_stordoc = '"+pk_stordoc+"'", new ColumnProcessor());
		
		ctx.getBillCardPanel().getHeadItem("storageaddress").setValue(storaddr);
		
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
		
		return true;
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
