package nc.ui.ehpta.hq010610;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ehpta.pub.convert.MarkDlg;
import nc.ui.ehpta.pub.dlg.PeriodDlg;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.ehpta.hq010610.SaleMnymodifyVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.trade.pub.HYBillVO;

public class EventHandler extends CardEventHandler {

	protected final String[] gTaxprice = new String[] {
			"norgqttaxnetprc" , 
			"norgqttaxprc" ,
			"noriginalcurtaxnetprice" ,
			"noriginalcurtaxprice" ,
			"nqttaxnetprc" ,
			"nqttaxprc" ,
			"ntaxnetprice" ,
			"ntaxprice" ,
		};
			
		protected final String[] gPrice = new String[] {
			"nnetprice",
			"norgqtnetprc",
			"norgqtprc",
			"noriginalcurnetprice",
			"noriginalcurprice",
			"nprice",
			"nqtnetprc",
			"nqtprc",
		};
			
		protected final String[] gSummny = new String[] {
			"noriginalcursummny",
			"nsummny" ,
		};
		
		protected final String[] gMny = new String[] {
			"nmny",
			"noriginalcurmny",
		};
		
		protected final String[] gTaxmny = new String[] {
			"noriginalcurtaxmny",
			"ntaxmny",
		};
	
	public EventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@Override
	protected void onBoElse(int intBtn) throws Exception {

		switch (intBtn) {
			case DefaultBillButton.Statistics:
				onBoStatistics();
				break;
	
			case DefaultBillButton.Mark:
				onBoMark();
				break;
				
			case DefaultBillButton.SelAll:
				onBoSelAll();
				break;
				
			case DefaultBillButton.SelNone:
				onBoSelNone();
				break;
				
			case DefaultBillButton.Confirm:
				onBoConfirm();
				break;
				
			default:
				break;
			
		}
		
	}
	
	protected final void onBoStatistics() throws Exception {
		
		PeriodDlg peroidDlg = new PeriodDlg(getBillUI());
		
		if(peroidDlg.showModal() != UIDialog.ID_OK)
			return ;
		
		if(peroidDlg.getFieldRef().getRefName() == null || "".equals(peroidDlg.getFieldRef().getRefName())) {
			
			getBufferData().clear();
			updateBuffer();
			
			throw new Exception("会计期间不能为空...");
			
		}
		
		getBufferData().clear();
		
		String peroid = peroidDlg.getFieldRef().getRefName();
		
		UFDate firstDate = new UFDate(peroid + "-01");
		UFDate lastDate = new UFDate(peroid + "-" + CalcFunc.getLastDay(firstDate));
		
		super.onBoAdd(getButtonManager().getButton(IBillButton.Add));
		
		StringBuilder builder = new StringBuilder();
		builder.append(" select orderb.norgqttaxnetprc, orderb.norgqttaxprc, orderb.noriginalcurtaxnetprice, orderb.noriginalcurtaxprice, ");
		builder.append(" orderb.nqttaxnetprc, orderb.nqttaxprc, orderb.ntaxnetprice, orderb.ntaxprice, orderb.nnetprice, ");
		builder.append(" orderb.norgqtnetprc, orderb.norgqtprc, orderb.noriginalcurnetprice, orderb.noriginalcurprice, ");
		builder.append(" orderb.nprice, orderb.nqtnetprc, orderb.nqtprc, orderb.noriginalcursummny, orderb.nsummny, ");
		builder.append(" orderb.nmny, orderb.noriginalcurmny, orderb.noriginalcurtaxmny, orderb.ntaxmny, orderb.ntaxrate , orderb.nnumber , ");
		builder.append(" orderb.corder_bid, orderb.csaleid, sale.vreceiptcode, sale.concode, sale.pk_contract ");
		builder.append(" , cubas.custname def1 ");
		builder.append(" from so_saleorder_b orderb left join so_sale sale on sale.csaleid = orderb.csaleid ");
		builder.append(" left join bd_cumandoc cuman on cuman.pk_cumandoc = sale.ccustomerid ");
		builder.append(" left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc ");
		builder.append(" where not exists (select 1 from so_saleinvoice_b invb where orderb.corder_bid = invb.csourcebillbodyid and nvl(invb.dr, 0) = 0) ");
		builder.append(" and sale.pk_contract is not null and sale.contracttype = '10' and nvl(orderb.dr, 0) = 0 and nvl(sale.dr, 0) = 0 ");
		builder.append(" and sale.dbilldate between '"+firstDate.toString()+"' and '"+lastDate.toString()+"' and sale.pk_corp = '"+_getCorp().getPk_corp()+"' ");
		
		List<SaleMnymodifyVO> mnyList = (List<SaleMnymodifyVO>) UAPQueryBS.getInstance().executeQuery(builder.toString(), new BeanListProcessor(SaleMnymodifyVO.class));
		
		if(mnyList == null || mnyList.size() == 0) {
			super.onBoCancel();
			getBillUI().showHintMessage("查询结束，没有可以统计的数据。");
			return ;
		} else 
			getBillUI().showHintMessage("查询结束。");
			
		
		HYBillVO billVO = new HYBillVO();
		billVO.setChildrenVO(mnyList.toArray(new SaleMnymodifyVO[0]));
		getBillCardPanelWrapper().setCardData(billVO);
		
		getBufferData().addVOToBuffer(billVO);
		
	}
	
	protected final void onBoMark() throws Exception {

		MarkDlg markDlg = MarkDlg.getInstance(getBillUI() , getUIController().getBillType());
		
		SaleMnymodifyVO[] selBodyVOs = (SaleMnymodifyVO[]) getBillCardPanelWrapper().getBillCardPanel().getBillModel().getBodySelectedVOs(SaleMnymodifyVO.class.getName());
		if(selBodyVOs == null || selBodyVOs.length == 0)
			selBodyVOs = (SaleMnymodifyVO[]) getBillCardPanelWrapper().getSelectedBodyVOs();
		
		if(selBodyVOs == null || selBodyVOs.length == 0) {
			getBillUI().showErrorMessage("请至少选择一条记录进行批改操作。");
			return ;
		}
		
		if(markDlg.showModal() == UIDialog.ID_OK) {
			
			if(markDlg.getFieldRef().getRefPK() == null || "".equals(markDlg.getFieldRef().getRefPK())) 
				throw new Exception ("属性为空，不能进行批改操作。");
			
			SaleMnymodifyVO[] bodyVOs = (SaleMnymodifyVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			
			for(SaleMnymodifyVO selbodyVO : selBodyVOs) {
				for(SaleMnymodifyVO bodyVO : bodyVOs) {
					if(selbodyVO.getCorder_bid().equals(bodyVO.getCorder_bid())) {
						
						if(markDlg.getTxtValue() instanceof UITextField) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(SaleMnymodifyVO.class, markDlg.getFieldRef().getRefCode(), ((UITextField)markDlg.getTxtValue()).getText()));
						} else if(markDlg.getTxtValue() instanceof UICheckBox) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(SaleMnymodifyVO.class, markDlg.getFieldRef().getRefCode(), new UFBoolean(((UICheckBox)markDlg.getTxtValue()).isSelected())));
						} else if(markDlg.getTxtValue() instanceof UIRefPane) {
							bodyVO.setAttributeValue(markDlg.getFieldRef().getRefCode(), ConvertFunc.change(SaleMnymodifyVO.class, markDlg.getFieldRef().getRefCode(), ((UIRefPane)markDlg.getTxtValue()).getRefName()));
						}
						
						break;
					}
				}
				
			}
			
			HYBillVO billVO = new HYBillVO();
			billVO.setParentVO(getBillCardPanelWrapper().getBillVOFromUI().getParentVO());
			billVO.setChildrenVO(bodyVOs);
			getBillCardPanelWrapper().setCardData(billVO);
			
			getBufferData().clear();
			getBufferData().addVOToBuffer(billVO);
			
			for(int row = 0 , count = bodyVOs.length ; row < count ; row ++) {
				((ClientUI)getBillUI()).afterSetMnys(row);
			}
		
		}
		
	}
	
	@Override
	protected void onBoSelAll() throws Exception {
		selectAll(true);
	}
	
	@Override
	protected void onBoSelNone() throws Exception {
		selectAll(false);
	}
	
	/**
	 * 功能 ： 全选或全消行记录
	 * 
	 * Author : river 
	 * 
	 * Create : 2012-08-21
	 * 
	 * @throws Exception
	 */
	private final void selectAll( boolean isNeedSelected) throws Exception {
		int row = getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO().length;
		BillModel headModel = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
		if (isNeedSelected) {
			for (int n = 0; n < row; n++) {
				if (headModel.getRowState(n) != BillModel.SELECTED) {
					headModel.setRowState(n, BillModel.SELECTED);
				}
			}
		} else {
			for (int n = 0; n < row; n++) {
				if (headModel.getRowState(n) != BillModel.UNSTATE) {
					headModel.setRowState(n, BillModel.UNSTATE);
				}
			}
		}

		getBillUI().updateUI();
	}
	
	protected void onBoConfirm() throws Exception {
		
		int type = getBillUI().showYesNoMessage("该操作将直接修改提单[ 现货合同 ]类型的金额相关信息，确定要进行此操作吗？");
		if(!(type == UIDialog.ID_YES))
			return ;
		
		SaleMnymodifyVO[] bodyVOs = (SaleMnymodifyVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		if(bodyVOs != null && bodyVOs.length > 0) {
			
			Set<String> csaleidSet = new HashSet<String>();
			
			for(SaleMnymodifyVO bodyVO : bodyVOs) {
				
				csaleidSet.add(bodyVO.getCsaleid());
				
				List<String> fieldList = new ArrayList<String>();
				
				for(String attr : gTaxprice) {
					fieldList.add(attr + " = " + bodyVO.getAttributeValue(attr));
				}
				
				for(String attr : gPrice) {
					fieldList.add(attr + " = " + bodyVO.getAttributeValue(attr));
				}
				
				for(String attr : gSummny) {
					fieldList.add(attr + " = " + bodyVO.getAttributeValue(attr));
				}
				
				for(String attr : gMny) {
					fieldList.add(attr + " = " + bodyVO.getAttributeValue(attr));
				}
				
				for(String attr : gTaxmny) {
					fieldList.add(attr + " = " + bodyVO.getAttributeValue(attr));
				}
				
				StringBuilder builder = new StringBuilder();
				builder.append(" update so_saleorder_b set " + ConvertFunc.change(fieldList.toArray(new String[0])) + " where corder_bid = '" + bodyVO.getCorder_bid() + "'");

				try {
					UAPQueryBS.getInstance().executeQuery(builder.toString(), null);
				} catch(Exception e) {
					Logger.info("update so_saleorder_b by primary key : " + bodyVO.getCorder_bid() , this.getClass(), "onBoConfirm");
				}
				
			}
			
			// 更新销售订单主表的整单加税合计。
			for(String csaleid : csaleidSet) {
				try {
					
					Object ntaxprice = UAPQueryBS.getInstance().executeQuery("select sum(nsummny) from so_saleorder_b where csaleid = '"+csaleid+"' and nvl(dr,0)=0 ", new ColumnProcessor());
					UAPQueryBS.getInstance().executeQuery("update so_sale set nheadsummny = " + ntaxprice + " where csaleid = '"+csaleid+"'", null);
				
				} catch(Exception ex) {
					Logger.info("update so_sale by primary key : " + csaleid); 
				}
			}
			
		} 
		
		getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		
	} 
	
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		
		if (getBufferData().isVOBufferEmpty()) {
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		} else {
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		}
		
		if(getUICardController() instanceof ISingleController)
			if(((ISingleController)getUICardController()).isSingleDetail())
				getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		
	}

}
