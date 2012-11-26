package nc.ui.ehpta.hq0306;

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
import nc.vo.ehpta.hq0306.SaleinvBalanceBVO;
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
				
				Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_calc_saleinv_balance_h where period = '"+period+"' and nvl(dr,0) = 0 ", new ColumnProcessor());
				if(count > 0) {
					getBillUI().showWarningMessage("当前期间已经统计，如需重新统计，请删除后再进行统计！");
					return ;
					
				}
				
				UFDate beforeDate = new UFDate(period + "-01");
				String lastDay = CalcFunc.getLastDay(beforeDate);
				UFDate afterDate = new UFDate(period + "-" + lastDay);
				String upPeriod = CalcFunc.getUpperPeriod(period);
				
				StringBuilder builder = new StringBuilder();
				builder.append(" select pk_cumandoc , custname , sum(firstmny) firstmny , sum(currmny) currmny , sum(totalmny) totalmny ,  ");
				builder.append(" sum(firstmny) + sum(currmny) - sum(totalmny) balance ");
				builder.append(" from ( ");
				builder.append(" select saleinv.creceiptcorpid pk_cumandoc  , saleinv.vprintcustname custname ,  ");
				builder.append(" 0 firstmny , 0 currmny , saleinv.ntotalsummny totalmny  ");
				builder.append(" from so_saleinvoice saleinv  ");
				builder.append(" where saleinv.dbilldate between '"+beforeDate.toString()+"' and '"+afterDate.toString()+"'  ");
				builder.append(" and saleinv.pk_corp = '"+_getCorp().getPk_corp()+"' and nvl(saleinv.dr,0) = 0 and saleinv.fstatus = 2 ");
				builder.append(" union all ");
				builder.append(" select cuman.pk_cumandoc, cubas.custname,  ");
				builder.append(" 0 firstmny, adjust.mny currmny, 0 totalmny ");
				builder.append(" from ehpta_adjust adjust, bd_cumandoc cuman, bd_cubasdoc cubas, ehpta_sale_contract salecont ");
				builder.append(" where adjust.pk_cubasdoc = cuman.pk_cumandoc(+) and cuman.pk_cubasdoc = cubas.pk_cubasdoc(+) ");
				builder.append(" and adjust.pk_contract = salecont.pk_contract(+) and adjust.type = 1 ");
				builder.append(" and nvl(adjust.dr, 0) = 0 and adjust.pk_contract is not null ");
				builder.append(" and adjust.adjustdate between '"+beforeDate.toString()+"' and '"+afterDate.toString()+"' ");
				builder.append(" and adjust.pk_corp = '"+_getCorp().getPk_corp()+"' and adjust.vbillstatus = 1 ");
				builder.append(" union all ");
				builder.append(" select balanceb.pk_cumandoc, balanceb.custname,  ");
				builder.append(" balanceb.balance firstmny, 0 currmny, 0 salemny ");
				builder.append(" from ehpta_calc_saleinv_balance_b balanceb,ehpta_calc_saleinv_balance_h balanceh ");
				builder.append(" where balanceb.pk_saleinv_balance = balanceh.pk_saleinv_balance(+) ");
				builder.append(" and balanceh.period = '"+upPeriod+"' and balanceh.vbillstatus = 1 and nvl(balanceh.dr, 0) = 0 ");
				builder.append(" and nvl(balanceb.dr, 0) = 0 and balanceh.pk_corp = '"+_getCorp().getPk_corp()+"' ");
				builder.append(" ) group by pk_cumandoc , custname ");
				
				List<SaleinvBalanceBVO> balanceList = (List<SaleinvBalanceBVO>) UAPQueryBS.getInstance().executeQuery(builder.toString(), new BeanListProcessor(SaleinvBalanceBVO.class));
				
				HYBillVO newBillVO = new HYBillVO();
				newBillVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
				newBillVO.setChildrenVO(balanceList.toArray(new SaleinvBalanceBVO[0]));
				getBillCardPanelWrapper().setCardData(newBillVO);

			} else 
				throw new Exception("请选择表头期间后再进行统计操作！");
			
		} else 
			throw new Exception("period不存在或者类型不正确，请至单据模板HQ17中进行确认！");
		
	}

}