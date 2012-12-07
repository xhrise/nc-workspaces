package nc.ui.ar.m20060301;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ufida.iufo.pub.tools.AppDebug;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.arap.actions.SearchActionNor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.gen.GeneraterBillNO;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.businessaction.IBusinessController;
import nc.vo.arap.exception.ExceptionHandler;
import nc.vo.ehpta.hq010403.AdjustVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;

public class Ar2006030X extends nc.ui.ep.dj.DjPflowPanel implements nc.ui.pub.bill.BillEditListener, nc.ui.pub.bill.BillEditListener2{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3787994406728040662L;

	public Ar2006030X() {
		super(0);
		this.setIsAloneNode(UFBoolean.TRUE);
		// setM_Node("2006030101");
		// setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(getClientEnvironment().getCorporation().getPrimaryKey(),nc.vo.arap.global.ResMessage.$SysCode_AR));
		 
		// 添加afterEdit监听
		// add by river for 2012-08-06
		getArapDjPanel1().getBillCardPanelDj().addBillEditListenerHeadTail(this);
		
	}

	public void postInit() {
		if (ILinkType.NONLINK_TYPE == this.getLinkedType()) {
			setDjlxbm(getBillType());
			super.postInit();
			// setDjlxbm("D0");//设置单据类型编码
			// setDjlxbm(nc.ui.arap.global.PubData.getDjlxbmByPkcorp(
			// getClientEnvironment().getCorporation().getPrimaryKey(), 0));

		}
	}

	protected void initialize() {
		initialize(false, false, true);
		initBillListTemplet();
	}

	public String getNodeCode() {
		return this.getDjSettingParam().getNodeID();
	}

	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("2006030102",
				"UPP2006030102-000837")/* @res "单据录入" */;
	}

	public boolean beforeEdit(BillEditEvent e) {
		return true;
	}

	/**
	 *  功能 ： 表头表尾afterEdit事件
	 *  
	 *  author : river 
	 *  
	 *  create : 2012-08-06
	 *  
	 */
	public void afterEdit(BillEditEvent e) {
		
		try {
			if("zyx6".equals(e.getKey()))  
				afterSetPk_contract(e);
			
			if("zyx8".equals(e.getKey()))
				afterSetIsCredit(e);
			
			if("notetype".equals(e.getKey()))
				afterSetNotetype(e);
			
			if("pj_jsfs".equals(e.getKey()))
				afterSetPj_jsfs(e);
			
		} catch(Exception ex) {
			Logger.error(ex.getMessage(), ex, this.getClass(), "afterEdit");
		}
			
	}
	
	protected final void afterSetPj_jsfs(BillEditEvent e) throws Exception {
		
		String refPK = ((UIRefPane)getArapDjPanel1().getBillCardPanelDj().getHeadItem(e.getKey()).getComponent()).getRefPK();
		
		if("0001A810000000000JC0".equals(refPK)) 
			((UICheckBox)getArapDjPanel1().getBillCardPanelDj().getHeadItem("zyx8").getComponent()).setSelected(true);
		else 
			((UICheckBox)getArapDjPanel1().getBillCardPanelDj().getHeadItem("zyx8").getComponent()).setSelected(false);
		
	}
	
	/**
	 *  功能 ： 点击合同（zyx6）时的afterEdit事件
	 *  
	 *  author ： river
	 *  
	 *  create ： 2012-08-06
	 * 
	 * @param e
	 * @throws Exception
	 */
	private final void afterSetPk_contract(BillEditEvent e) throws Exception {
		if(getArapDjPanel1().getBillCardPanelDj().getHeadItem(e.getKey()).getValueObject() != null) {
			
			Object purchcode = UAPQueryBS.getInstance().executeQuery("select pk_cubasdoc from bd_cumandoc where pk_cumandoc = (select purchcode from ehpta_sale_contract where pk_contract = '"+getArapDjPanel1().getBillCardPanelDj().getHeadItem(e.getKey()).getValueObject()+"' and nvl(dr,0)=0 )", new ColumnProcessor());
			((UIRefPane)getArapDjPanel1().getBillCardPanelDj().getHeadItem("hbbm").getComponent()).setPK(purchcode);
			
			((UIRefPane)getArapDjPanel1().getBillCardPanelDj().getHeadItem("deptid").getComponent()).setPK("1120A7100000000XNDFR");
		}
	}
	
	/**
	 *  功能 ： 点击是否信用证（zyx8）时的afterEdit事件
	 *  
	 *  author ： river
	 *  
	 *  create ： 2012-08-06
	 * 
	 * @param e
	 * @throws Exception
	 */
	private final void afterSetIsCredit(BillEditEvent e) throws Exception {

		if(getArapDjPanel1().getBillCardPanelDj().getHeadItem(e.getKey()).getValueObject() != null) {
			
			boolean iscredit = ((UICheckBox)getArapDjPanel1().getBillCardPanelDj().getHeadItem(e.getKey()).getComponent()).isSelected();
			
			if(iscredit) {
//				getArapDjPanel1().getBillCardPanelDj().getHeadItem("notetype").setValue(null);
//				getArapDjPanel1().getBillCardPanelDj().getHeadItem("notetype").setEnabled(false);
				getArapDjPanel1().getBillCardPanelDj().getHeadItem("pj_jsfs").setValue("0001A810000000000JC0");
				getArapDjPanel1().getBillCardPanelDj().execHeadFormula("pj_jsfs->getColValue(bd_balatype , pk_balatype , pk_balatype , pj_jsfs)");
				
			} else {
//				getArapDjPanel1().getBillCardPanelDj().getHeadItem("notetype").setEnabled(true);
				getArapDjPanel1().getBillCardPanelDj().getHeadItem("pj_jsfs").setValue(null);
				getArapDjPanel1().getBillCardPanelDj().execHeadFormula("pj_jsfs->getColValue(bd_balatype , pk_balatype , pk_balatype , pj_jsfs)");
				
			}
		}
		
	}
	
	/**
	 *  功能 ： 点击票据类别（notetype）时的afterEdit事件
	 *  
	 *  author ： river
	 *  
	 *  create ： 2012-08-06
	 * 
	 * @param e
	 * @throws Exception
	 */
	private final void afterSetNotetype(BillEditEvent e) throws Exception {
		
		if(getArapDjPanel1().getBillCardPanelDj().getHeadItem(e.getKey()).getValueObject() != null) {
			
			UIRefPane noteTypeRef = (UIRefPane) getArapDjPanel1().getBillCardPanelDj().getHeadItem(e.getKey()).getComponent();
			String code = noteTypeRef.getRefCode();
			
			if(code != null && !"".equals(code)) {
				if("lc".equals(code))
					((UICheckBox)getArapDjPanel1().getBillCardPanelDj().getHeadItem("zyx8").getComponent()).setSelected(true);
				else 
					((UICheckBox)getArapDjPanel1().getBillCardPanelDj().getHeadItem("zyx8").getComponent()).setSelected(false);
			}
			
		}
		
	}

	public void bodyRowChange(BillEditEvent e) {
		
	}
}