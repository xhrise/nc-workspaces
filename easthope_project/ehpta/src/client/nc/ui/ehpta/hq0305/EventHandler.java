package nc.ui.ehpta.hq0305;

import java.util.List;

import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.ehpta.hq0305.SaleBalanceBVO;
import nc.vo.fp.combase.pub01.IBillStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;

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
		
		switch (intBtn) {
		
			case DefaultBillButton.Statistics:
				onBoStatistics();
				break;
		
			default:
				break;
				
		}
	}
	
	@SuppressWarnings("unchecked")
	protected final void onBoStatistics() throws Exception {
		
		BillItem item = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("period");
		
		if(item != null && item.getComponent() instanceof UIRefPane) {
			
			String period = ((UIRefPane) item.getComponent()).getRefName();
			
			if(period != null && !"".equals(period)) {
				
				Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_calc_sale_balance_h where period = '"+period+"' and nvl(dr,0) = 0 ", new ColumnProcessor());
				if(count > 0) {
					getBillUI().showWarningMessage("当前期间已经统计，如需重新统计，请删除后再进行统计！");
					return ;
					
				}
				
				UFDate beforeDate = new UFDate(period + "-01");
				String lastDay = CalcFunc.getLastDay(beforeDate);
				UFDate afterDate = new UFDate(period + "-" + lastDay);
				String upPeriod = CalcFunc.getUpperPeriod(period);
				
				StringBuilder builder = new StringBuilder();
				builder.append(" select pk_cumandoc , custname , pk_contract , concode , connamed , sum(firstmny) firstmny , sum(currmny) currmny , sum(salemny) salemny , ");
				builder.append(" nvl(sum(firstmny) , 0) + nvl(sum(currmny) , 0) - nvl(sum(salemny) , 0) salebalance , ");
				builder.append(" nvl(sum(adtype2) , 0) adtype2 , nvl(sum(adtype3) , 0) adtype3 , nvl(sum(adtype4) , 0) adtype4 , ");
				builder.append(" (nvl(sum(firstmny) , 0) + nvl(sum(currmny) , 0) - nvl(sum(salemny) , 0) +  ");
				builder.append(" nvl(sum(adtype2) , 0) + nvl(sum(adtype3) , 0) + nvl(sum(adtype4) , 0)) balance ");
				builder.append(" from ( ");
				
				builder.append(" select cuman.pk_cumandoc , cubas.custname , sale.pk_contract , sale.concode , salecont.connamed , 0 firstmny , 0 currmny , ");
				builder.append(" sum(sale.nheadsummny) salemny , 0 adtype2 , 0 adtype3 , 0 adtype4   ");
				builder.append(" from so_sale sale , bd_cumandoc cuman , bd_cubasdoc cubas , ehpta_adjust adjust , ehpta_sale_contract salecont ");
				builder.append(" where sale.ccustomerid = cuman.pk_cumandoc(+) ");
				builder.append(" and cuman.pk_cubasdoc = cubas.pk_cubasdoc(+) ");
				builder.append(" and sale.pk_contract = adjust.pk_contract(+) ");
				builder.append(" and sale.pk_contract = salecont.pk_contract(+) ");
				builder.append(" and sale.pk_contract is not null  ");
				builder.append(" and (sale.contracttype = '10' or sale.contracttype = '20') ");
				builder.append(" and nvl(sale.dr , 0) = 0 ");
				builder.append(" and sale.dbilldate >= '" + beforeDate.toString() + "' and sale.dbilldate <= '" + afterDate.toString() + "'  ");
				builder.append(" and sale.pk_corp = '" + _getCorp().getPk_corp() + "' ");
				builder.append(" group by cuman.pk_cumandoc , cubas.custname , sale.pk_contract , sale.concode , salecont.connamed ");
				builder.append(" union all  ");
				
				builder.append(" select cuman.pk_cumandoc , cubas.custname , adjust.pk_contract , salecont.vbillno , salecont.connamed , 0 firstmny , adjust.mny currmny , ");
				builder.append(" 0 salemny , 0 adtype2 , 0 adtype3 , 0 adtype4  ");
				builder.append(" from ehpta_adjust adjust , bd_cumandoc cuman , bd_cubasdoc cubas , ehpta_sale_contract salecont");
				builder.append(" where adjust.pk_cubasdoc = cuman.pk_cumandoc(+) ");
				builder.append(" and cuman.pk_cubasdoc = cubas.pk_cubasdoc(+) ");
				builder.append(" and adjust.pk_contract = salecont.pk_contract(+) ");
				builder.append(" and adjust.type = 1 and nvl(adjust.dr , 0) = 0 and adjust.pk_contract is not null  ");
				builder.append(" and adjust.adjustdate >= '" + beforeDate.toString() + "' and adjust.adjustdate <= '" + afterDate.toString() + "'  ");
				builder.append(" and adjust.pk_corp = '" + _getCorp().getPk_corp() + "' ");
				builder.append(" union all  ");
				
				builder.append(" select cuman.pk_cumandoc , cubas.custname , adjust.pk_contract , salecont.vbillno , salecont.connamed , 0 firstmny , 0 currmny , ");
				builder.append(" 0 salemny , decode(adjust.type , 2 , adjust.mny , 0) adtype2 ,  ");
				builder.append(" decode(adjust.type , 3 , adjust.mny , 0) adtype3 ,  ");
				builder.append(" decode(adjust.type , 4 , adjust.mny , 0) adtype4  ");
				builder.append(" from ehpta_adjust adjust , bd_cumandoc cuman , bd_cubasdoc cubas , ehpta_sale_contract salecont ");
				builder.append(" where adjust.pk_cubasdoc = cuman.pk_cumandoc(+) ");
				builder.append(" and cuman.pk_cubasdoc = cubas.pk_cubasdoc(+) ");
				builder.append(" and adjust.pk_contract = salecont.pk_contract(+) ");
				builder.append(" and adjust.type <> 1 and nvl(adjust.dr , 0) = 0 and adjust.pk_contract is not null  ");
				builder.append(" and adjust.adjustdate >= '" + beforeDate.toString() + "' and adjust.adjustdate <= '" + afterDate.toString() + "'  ");
				builder.append(" and adjust.pk_corp = '" + _getCorp().getPk_corp() + "' ");
				builder.append(" union all  ");
				
				builder.append(" select balanceb.pk_cumandoc , balanceb.custname , balanceb.pk_contract , balanceb.concode , balanceb.connamed , balanceb.balance firstmny , ");
				builder.append(" 0 currmny, 0 salemny, 0 adtype2, 0 adtype3, 0 adtype4 ");
				builder.append(" from ehpta_calc_sale_balance_b balanceb , ehpta_calc_sale_balance_h balanceh  ");
				builder.append(" where balanceb.pk_sale_balance = balanceh.pk_sale_balance(+) ");
				builder.append(" and balanceh.period = '" + upPeriod + "' and balanceh.vbillstatus = " + IBillStatus.CHECKPASS + " and nvl(balanceh.dr , 0) = 0 ");
				builder.append(" and nvl(balanceb.dr , 0) = 0  ");
				builder.append(" and balanceh.pk_corp = '" + _getCorp().getPk_corp() + "' ");
				builder.append(" ) group by pk_cumandoc , custname , pk_contract , concode , connamed ");
				
				List<SaleBalanceBVO> balanceList = (List<SaleBalanceBVO>) UAPQueryBS.getInstance().executeQuery(builder.toString(), new BeanListProcessor(SaleBalanceBVO.class));
				
				HYBillVO newBillVO = new HYBillVO();
				newBillVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
				newBillVO.setChildrenVO(balanceList.toArray(new SaleBalanceBVO[0]));
				getBillCardPanelWrapper().setCardData(newBillVO);

			} else 
				throw new Exception("请选择表头期间后再进行统计操作！");
			
		} else 
			throw new Exception("period不存在或者类型不正确，请至单据模板HQ16中进行确认！");
		
	}

}