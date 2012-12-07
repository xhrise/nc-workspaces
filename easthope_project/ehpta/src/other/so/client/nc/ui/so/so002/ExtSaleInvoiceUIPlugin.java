package nc.ui.so.so002;

import java.awt.event.ActionEvent;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.ehpta.pub.IAdjustType;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.ui.scm.pub.bill.ScmButtonConst;
import nc.ui.so.so001.order.SaleContractBalanceDlg;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.so.so002.SaleinvoiceVO;

@SuppressWarnings("restriction")
public class ExtSaleInvoiceUIPlugin implements IScmUIPlugin {

	public boolean init(SCMUIContext ctx) {
		return true;
	}

	// river
	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
		if("审核".equals(bo.getName()) || "送审".equals(bo.getName())) {
			beforeOnBoAudit(ctx , bo);
		}
		
		
	}
	
	// river
	@SuppressWarnings({ "unused", "static-access" })
	protected final void beforeOnBoAudit(SCMUIContext ctx , ButtonObject bo) throws BusinessException {
		
		SaleinvoiceVO billVO = null;
		
		try { 
			if(((SaleInvoiceUI)ctx.getToftPanel()).getShowState() == ((SaleInvoiceUI)ctx.getToftPanel()).CardShow) 
				billVO = ((SaleInvoiceCardPanel)ctx.getBillCardPanel()).getVO();
			else 
				 billVO = ((SaleInvoiceListPanel)ctx.getBillListPanel()).getSelectedVO();
		} catch(Exception ex) {
			try {
				
				if(((ExtSaleInvoiceUI)ctx.getToftPanel()).getShowState() == ((ExtSaleInvoiceUI)ctx.getToftPanel()).CardShow) 
					billVO = ((SaleInvoiceCardPanel)ctx.getBillCardPanel()).getVO();
				else 
					 billVO = ((SaleInvoiceListPanel)ctx.getBillListPanel()).getSelectedVO();
				
			} catch(Exception ex2) {
				billVO = null;
			}
		}
		
		Object saletype = billVO.getParentVO().getAttributeValue("saletype");
		if(saletype != null) {
			Object pk_contract = billVO.getParentVO().getAttributeValue("pk_contract");
			
			if(pk_contract == null || "".equals(pk_contract))
				throw new BusinessException ("销售合同不能为空！");
			
			if(billVO == null) 
				throw new BusinessException("billVO is null");
			
			Object iscredit = ((UFBoolean)billVO.getParentVO().getAttributeValue("iscredit")).toString();
			Object ccustomerid = billVO.getParentVO().getAttributeValue("creceiptcorpid");
			
			String sqlPart = " ";
			try {
				if(new UFBoolean(iscredit.toString()).booleanValue()) {
					
					String[] adjustType = new String[]{
						"'" + IAdjustType.Receivables + "'" , 
						"'" + IAdjustType.Otherfee + "'" , 
					};
					
					
					sqlPart += "and type in ("+ConvertFunc.change(adjustType)+") and ( pk_cumandoc = '"+ccustomerid+"' or pk_cumandoc is null ) ";
					
				} else  {
					
					String[] adjustType = new String[]{
						"'" + IAdjustType.Receivables + "'" , 
						"'" + IAdjustType.Otherfee + "'" , 
						"'" + IAdjustType.Discount + "'" , 
						"'" + IAdjustType.LSSubPrice + "'" , 
						"'" + IAdjustType.Rebates + "'" , 
					};
					
					sqlPart += "and type in ("+ConvertFunc.change(adjustType)+") and ( pk_cumandoc = '"+ccustomerid+"' or pk_cumandoc is null ) ";
				}
				
			
			} catch(Exception e) {
				throw new BusinessException(e.getMessage());
			}
			
			Object mny = UAPQueryBS.getInstance().executeQuery(" select sum(mny) from vw_pta_salecont_invbalance where pk_contract = '"+pk_contract+"' and iscredit = '"+iscredit+"' " + sqlPart, new ColumnProcessor());
			UFDouble nowMny = new UFDouble("0",2);
			if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
				for(CircularlyAccessibleValueObject bodyVO : billVO.getChildrenVO()) {
					UFDouble noriginalcursummny = (UFDouble) bodyVO.getAttributeValue("noriginalcursummny");
					nowMny = nowMny.add(noriginalcursummny);
				}
			}
			
			if(new UFDouble(mny == null ? "0" : mny.toString()).sub(nowMny).doubleValue() < 0) {
				if(!"Y".equals(iscredit.toString())) {
					if("审核".equals(bo.getName()) || "送审".equals(bo.getName())) {
						UFDouble contBalance = new UFDouble(mny == null ? "0" : mny.toString() , 2).sub(nowMny , 2);
						
//						if(contBalance.doubleValue() <= 0 && rabates.doubleValue() >= contBalance.abs().doubleValue()) {
							int type = ctx.getToftPanel().showYesNoMessage("合同余额小于本次提货金额。\n合同余额:[ " + contBalance + " ]。\n是否继续执行此操作！");
							if(!(type == UIDialog.ID_YES))
								throw new BusinessException("本次操作已被取消！");
//						} else {
//							throw new BusinessException("合同余额小于本次提货金额。\n合同余额:[ " + contBalance + " ]。\n可用返利额：[ " + rabates + " ]。\n审核失败！");
//						}
					} 
				} 
				
			}
		}
	}

	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
		
		// river
		if("PTA合同余额".equals(bo.getName())) { 
			afterOnBoContBalance(bo , ctx);
			
		} else if("PTA合并开票".equals(bo.getName())) {
			afterOnBoPTAUnite(bo , ctx);
			
		} else if("PTA取消合并".equals(bo.getName())) {
			afterOnBoPTAUniteCancle(bo , ctx);
			
		} else if("保存".equals(bo.getName())) {
			afterOnBoSave(bo , ctx);
			
		}

	}
	
	// river
	public final void afterOnBoContBalance(ButtonObject bo, SCMUIContext ctx) throws BusinessException {
		
		Object[] obj = new Object[4];
		SaleinvoiceVO billVO = null;
		if(((SaleInvoiceUI)ctx.getToftPanel()).getShowState() == ((SaleInvoiceUI)ctx.getToftPanel()).CardShow) 
			billVO = ((SaleInvoiceCardPanel)ctx.getBillCardPanel()).getVO();
		else 
			 billVO = ((SaleInvoiceListPanel)ctx.getBillListPanel()).getSelectedVO();
		
		
		obj[1] = "";
		
		obj[2] = (String) billVO.getParentVO().getAttributeValue("csaleid");
		obj[2] = obj[2] == null ? "" : obj[2];
		
		
		Object ccustomerid = billVO.getParentVO().getAttributeValue("creceiptcorpid");
		
		Map queryMap = (Map) UAPQueryBS.getInstance().executeQuery("select nvl(iscredit , 'N') iscredit , pk_contract , concode from so_saleinvoice where csaleid = '"+obj[2]+"' ", new MapProcessor());
		
		Object iscredit = billVO.getParentVO().getAttributeValue("iscredit");
		if(iscredit == null)
			iscredit = queryMap.get("iscredit");
		
		obj[0] = (String) billVO.getParentVO().getAttributeValue("pk_contract");
		
		if(obj[0] == null)
			obj[0] = queryMap.get("pk_contract");
		
		obj[0] = obj[0] == null ? "" : obj[0];
		
		obj[1] = "";
		
		
		obj[3] = (String) billVO.getParentVO().getAttributeValue("concode");
		
		if(obj[3] == null)
			obj[3] = queryMap.get("concode");
		
		obj[3] = obj[3] == null ? "" : obj[3];
		
		
		String sqlPart = " ";
		try {
			if(new UFBoolean(iscredit.toString()).booleanValue()) {
				
				String[] adjustType = new String[]{
					"'" + IAdjustType.Receivables + "'" , 
					"'" + IAdjustType.Otherfee + "'" , 
				};
				
				
				sqlPart += "and type in ("+ConvertFunc.change(adjustType)+") and ( pk_cumandoc = '"+ccustomerid+"' or pk_cumandoc is null ) ";
				
			} else  {
				
				String[] adjustType = new String[]{
					"'" + IAdjustType.Receivables + "'" , 
					"'" + IAdjustType.Otherfee + "'" , 
					"'" + IAdjustType.Discount + "'" , 
					"'" + IAdjustType.LSSubPrice + "'" , 
					"'" + IAdjustType.Rebates + "'" , 
				};
				
				sqlPart += "and type in ("+ConvertFunc.change(adjustType)+") and ( pk_cumandoc = '"+ccustomerid+"' or pk_cumandoc is null ) ";
			}
			
		
		} catch(Exception e) {
			throw new BusinessException(e.getMessage());
		}
		
		
		String[] sqlString = new String[]{
				"select typename , mny from vw_pta_salecont_invbalance where pk_contract = '"+obj[0]+"' and iscredit = '"+iscredit+"' " + sqlPart ,
				"select '当前开票金额' , nvl(sum(ntotalsummny) * -1 , 0) from so_saleinvoice where pk_contract is not null and nvl(dr, 0) = 0 and FSTATUS <> 2 and (saletype = '现货合同' or saletype = '长单合同') and pk_contract = '"+obj[0]+"' and iscredit = '"+iscredit+"'  and creceiptcorpid = '"+ccustomerid+"' and csaleid = '"+obj[2]+"'" , 
		};
		
		SaleContractBalanceDlg balanceDlg = new SaleContractBalanceDlg(ctx.getIctxpanel().getToftPanel() , obj , sqlString);
		balanceDlg.showModal();
	}
	
	// river
	public final void afterOnBoPTAUnite(ButtonObject bo, SCMUIContext ctx) throws BusinessException {
		
		QueryConditionClient condition = new QueryConditionClient(ctx.getToftPanel());
		condition.setTempletID(ctx.getLoginCorpID(), "HQ010403", ctx.getLoginUserID(), null);
		condition.setNormalShow(false);
		
		if(condition.showModal() == UIDialog.ID_OK) {
			SaleinvoiceVO billVO = ((SaleInvoiceCardPanel)ctx.getBillCardPanel()).getVO();
			String pk_contract = (String) billVO.getParentVO().getAttributeValue("pk_contract");
			String whereSql = condition.getWhereSQL();
			if(whereSql == null || "".equals(whereSql))
				whereSql = " 1 = 1 and vbillstatus = 1 and pk_contract = '"+pk_contract+"' and nvl(def4 , 'N') = 'N' and nvl(dr,0) = 0 and nvl(type,1) <> 1 ";
			else 
				whereSql += " and vbillstatus = 1 and pk_contract = '"+pk_contract+"' and nvl(def4 , 'N') = 'N' and nvl(dr,0) = 0 and nvl(type,1) <> 1 ";
			
			LinkQueryData data = new LinkQueryData(whereSql == null ? "" : whereSql , (SaleInvoiceUI)ctx.getToftPanel());
			SFClientUtil.openLinkedQueryDialog("HQ010403", (SaleInvoiceUI)ctx.getToftPanel(), data);

		}
		
	}
	
	// river
	public final void afterOnBoPTAUniteCancle(ButtonObject bo, SCMUIContext ctx) throws BusinessException {
		
		SaleinvoiceVO billVO = ((SaleInvoiceCardPanel)ctx.getBillCardPanel()).getVO();
		
		if(billVO != null && billVO.getParentVO() != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
			
			for(int row = 0 , len = billVO.getChildrenVO().length ; row < len ; row ++ ) {
				ctx.getBillCardPanel().setBodyValueAt(0, row, "noriginalcurdiscountmny");
				ctx.getBillCardPanel().setBodyValueAt(0, row, "nuniteinvoicemny");
				ctx.getBillCardPanel().execBodyFormulas(row, new String[]{"noriginalcursummny->(nnumber * noriginalcurtaxprice )"});
				
				((SaleInvoiceCardPanel )ctx.getBillCardPanel()).calculateNumber(row, "noriginalcursummny");
				ctx.getBillCardPanel().setHeadItem("ntotalsummny", ((SaleInvoiceCardPanel )ctx.getBillCardPanel()).calcurateTotal("noriginalcursummny"));
				ctx.getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
				ctx.getBillCardPanel().setBodyValueAt(ctx.getBillCardPanel().getBodyValueAt(row, "noriginalcursummny"), row, "nsubsummny");
				ctx.getBillCardPanel().setBodyValueAt(ctx.getBillCardPanel().getBodyValueAt(row, "nsummny"), row, "nsubcursummny");
				ctx.getBillCardPanel().getBillModel().setRowState(row, BillModel.MODIFICATION);
			
			}
			
			((SaleInvoiceUI)ctx.getToftPanel()).getBtns().m_boUnite.setEnabled(true);
			((SaleInvoiceUI)ctx.getToftPanel()).getBtns().m_boPTAUnite.setEnabled(true);
			((SaleInvoiceUI)ctx.getToftPanel()).getBtns().m_boPTAUniteCancle.setEnabled(false);
		}
	}
	
	// river
	public final void afterOnBoSave(ButtonObject bo, SCMUIContext ctx) throws BusinessException {
		
		AdjustVO[] adjustVOs = ((ExtSaleInvoiceUI)ctx.getToftPanel()).getAdjustVOs();
		if(adjustVOs != null && adjustVOs.length > 0) {
			SaleinvoiceVO voFromPanel = (SaleinvoiceVO) ((ExtSaleInvoiceUI)ctx.getToftPanel()).getVo();
			Object noriginalcurdiscountmny = null;
			if(voFromPanel != null && voFromPanel.getChildrenVO() != null && voFromPanel.getChildrenVO().length > 0)
				noriginalcurdiscountmny = voFromPanel.getChildrenVO()[0].getAttributeValue("noriginalcurdiscountmny");
			
			if(voFromPanel != null && voFromPanel.getPrimaryKey() != null) {
				
				if(noriginalcurdiscountmny != null && Double.valueOf(noriginalcurdiscountmny.toString()) != 0 ) {
					for(AdjustVO adjust : adjustVOs) {
						adjust.setDef4("Y");
						adjust.setDef5(voFromPanel.getPrimaryKey());
					}
					
					HYPubBO_Client.updateAry(adjustVOs);
				} else {
					
					AdjustVO[] queryVOs = (AdjustVO[]) HYPubBO_Client.queryByCondition(AdjustVO.class, " def5 = '"+voFromPanel.getPrimaryKey()+"'");
					for(AdjustVO adjust : queryVOs) {
						adjust.setDef4("N");
						adjust.setDef5(null);
					}
					
					HYPubBO_Client.updateAry(queryVOs);
					
				}
			}
		}
		
		((ExtSaleInvoiceUI)ctx.getToftPanel()).setButtonsState();
	}

	public boolean beforeEdit(BillEditEvent e, SCMUIContext ctx) {
		return true;
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

		if(action == Action.DELETE)
			afterDelete(billvos , conx);
		
	}
	
	protected final void afterDelete(AggregatedValueObject[] billvos , SCMUIContext conx) throws BusinessException {
		
		if(billvos != null) {
			
			try {
				for(AggregatedValueObject billVO : billvos ) {
					String primaryKey = billVO.getParentVO().getPrimaryKey();
					try { UAPQueryBS.getInstance().executeQuery("update ehpta_adjust set def4 = 'N' , def5 = '' where def5 = '"+primaryKey+"'", null); } catch(Exception ex) { }
				}
			} catch(Exception e) {
				Logger.error(e.getMessage(), e, this.getClass(), "afterDelete");
			}
			
		}
		
	}

	public void setButtonStatus(SCMUIContext conx) {
		
		// add by river for 2012-11-16
		// 根据表体折扣金额，将行操作相关按钮进行状态设置
		try {
			SaleInvoiceUI ui = (SaleInvoiceUI)conx.getToftPanel();
			AggregatedValueObject billVO = ui.getVo();
			if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
				
				UFDouble summny = new UFDouble(0 , 2);
				for(CircularlyAccessibleValueObject bodyVO : billVO.getChildrenVO()) {
					UFDouble noriginalcurdiscountmny = (UFDouble)bodyVO.getAttributeValue("noriginalcurdiscountmny");	
					summny = summny.add(ConvertFunc.change(noriginalcurdiscountmny));
				}
				
				if(summny.doubleValue() > 0) {
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_ADD).setEnabled(false);
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_COPY).setEnabled(false);
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_DELETE).setEnabled(false);
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_PASTE).setEnabled(false);
				} else {
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_ADD).setEnabled(true);
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_COPY).setEnabled(true);
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_DELETE).setEnabled(true);
					ui.getButtonObjectByCode(ScmButtonConst.BTN_LINE_PASTE).setEnabled(true);
				}
				
			}
		} catch(Exception e) {
			Logger.error(e.getMessage(), e, this.getClass(), "setButtonStatus");
		}
		
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
