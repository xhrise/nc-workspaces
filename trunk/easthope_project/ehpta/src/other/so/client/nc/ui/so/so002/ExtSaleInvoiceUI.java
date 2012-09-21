package nc.ui.so.so002;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so002.SaleinvoiceVO;

/**
 * <p>
 * <b>本类主要完成以下功能：</b>
 * <ul>
 * <li>销售发票UI
 * <li>...
 * </ul>
 * <p>
 * <b>变更历史（可选）：</b>
 * <p>
 * XXX版本增加XXX的支持。
 * <p>
 * <p>
 * 
 * @version 本版本号
 * @since 上一版本号
 * @author wangyf
 * @time 2007-3-6 上午11:03:07
 */

@SuppressWarnings({ "restriction" })
public class ExtSaleInvoiceUI extends SaleInvoiceUI {

	private static final long serialVersionUID = -8127806262429494903L;

	protected String[] formulas = new String[]{
		"noriginalcursummny->noriginalcursummny - nuniteinvoicemny ",
			
	};
	
	@Override
	public void setButtonsState() {
		super.setButtonsState();
		
		if (getOperationState() == ISaleInvoiceOperState.STATE_EDIT) {
			getBtns().m_boPTAUnite.setEnabled(true);
			getBtns().m_boPTAUniteCancle.setEnabled(false);
		} else {
			getBtns().m_boPTAUnite.setEnabled(false);
			getBtns().m_boPTAUniteCancle.setEnabled(false);
		}
		
		SaleinvoiceVO voFromPanel = null;
	    
	    voFromPanel = getBillCardPanel().getVO();

	    if (voFromPanel == null || voFromPanel.getParentVO() == null)
			 voFromPanel = getBillListPanel().getVO();
		
		if(voFromPanel != null && voFromPanel.getParentVO() != null && getOperationState() == ISaleInvoiceOperState.STATE_EDIT) {
			if(voFromPanel.getChildrenVO() != null && voFromPanel.getChildrenVO().length > 0) {
				Object noriginalcurdiscountmny = voFromPanel.getChildrenVO()[0].getAttributeValue("noriginalcurdiscountmny");
			
				if(noriginalcurdiscountmny != null && Double.valueOf(noriginalcurdiscountmny.toString()) != 0 ) {
					getBtns().m_boUnite.setEnabled(false);
					getBtns().m_boPTAUnite.setEnabled(false);
					getBtns().m_boPTAUniteCancle.setEnabled(true);
				} else {
					
					try {
						Object mny = UAPQueryBS.iUAPQueryBS.executeQuery("select nvl(noriginalcurdiscountmny,0) from so_saleinvoice_b where cinvoice_bid = '"+voFromPanel.getChildrenVO()[0].getCsale_bid()+"'", new ColumnProcessor());
					
						if(mny == null || Double.valueOf(mny.toString()) == 0) {
							getBtns().m_boUnite.setEnabled(true);
							getBtns().m_boPTAUnite.setEnabled(true);
							getBtns().m_boPTAUniteCancle.setEnabled(false);
						} else {
							getBtns().m_boUnite.setEnabled(false);
							getBtns().m_boPTAUnite.setEnabled(false);
							getBtns().m_boPTAUniteCancle.setEnabled(false);
						}
						
					} catch (BusinessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		updateButtons();
	}
	
	@Override
	public void onButtonClicked(ButtonObject bo) {
		super.onButtonClicked(bo);
		setButtonsState();
//		getBillCardPanel().execBodyFormulas(0, new String[]{"nuniteinvoicemny->(nnumber * noriginalcurtaxprice ) - noriginalcursummny"});
	}
	
	private AdjustVO[] adjustVOs = null;

	public AdjustVO[] getAdjustVOs() {
		return adjustVOs;
	}

	public void setAdjustVOs(AdjustVO[] adjustVOs) {
		this.adjustVOs = adjustVOs;
	}
	
	public void onBoConfirm() throws Exception {
		
		System.out.println(getAdjustVOs().length);
		UFDouble mny = new UFDouble("0" , 2);
		if(getAdjustVOs() != null && getAdjustVOs().length > 0) {
			
			for(AdjustVO adjust : getAdjustVOs()) {
				mny = mny.add(adjust.getMny());
				
			}
			
			getBillCardPanel().setBodyValueAt(mny, 0, "nuniteinvoicemny");
			getBillCardPanel().execBodyFormulas(0, formulas);
			
			getBillCardPanel().calculateNumber(0, "noriginalcursummny");
		    getBillCardPanel().setHeadItem("ntotalsummny", getBillCardPanel().calcurateTotal("noriginalcursummny"));
		    getBillCardPanel().execHeadFormula("nnetmny->ntotalsummny-nstrikemny");
		    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(0, "noriginalcursummny"),
		       0, "nsubsummny");
		    getBillCardPanel().setBodyValueAt(getBillCardPanel().getBodyValueAt(0, "nsummny"), 0,
		        "nsubcursummny");
			
		    getBillCardPanel().getBillModel().setRowState(0, BillModel.MODIFICATION);
		    
		}
		
	}
	
}