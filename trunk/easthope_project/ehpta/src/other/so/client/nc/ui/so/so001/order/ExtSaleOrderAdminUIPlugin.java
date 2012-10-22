package nc.ui.so.so001.order;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.calc.CalcFunc;
import nc.ui.ehpta.pub.ref.LPriceRefPane;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.bill.BillCellEditor;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.scm.plugin.IScmUIPlugin;
import nc.ui.scm.plugin.SCMUIContext;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ehpta.hq010401.SaleContractBsVO;
import nc.vo.ehpta.hq010402.SaleContractBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.plugin.Action;
import nc.vo.so.so001.SaleOrderVO;
import nc.vo.so.so001.SaleorderBVO;
import nc.vo.so.so001.SaleorderHVO;

@SuppressWarnings({"restriction" , "rawtypes"})
public class ExtSaleOrderAdminUIPlugin implements IScmUIPlugin {
	
	
	// 表体加载公式，挂结价反推金额调用
	// add by river for 2012-08-17
	private String[] formulas = new String[]{
		"noriginalcurprice->(noriginalcurtaxprice / (ntaxrate / 100 + 1))", // 原币无税单价
		"noriginalcurmny->noriginalcurprice * nnumber", // 原币无税金额
		"noriginalcursummny->noriginalcurtaxprice * nnumber", // 原币价税合计
		"noriginalcurtaxmny->(noriginalcurtaxprice - noriginalcurtaxprice / (ntaxrate / 100 + 1)) * nnumber ", // 原币税额
		"noriginalcurnetprice->noriginalcurprice", // 原币无税净价 = 原币无税单价
		"noriginalcurtaxnetprice->noriginalcurtaxprice ", // 原币含税净价 = 原币含税单价
		"norgqttaxnetprc->noriginalcurtaxprice",
		"norgqtnetprc->noriginalcurprice",
		"numof->int(nnumber / getColValue(bd_invbasdoc , unitweight , pk_invbasdoc , getColValue(bd_invmandoc , pk_invbasdoc , pk_invmandoc , cinventoryid)))",
	};
		
	// 挂牌价参照设置
	// add by river for 2012-08-17
	private UIRefPane lpRef = null;

	public boolean init(SCMUIContext ctx) {
		return true;
	}

	public void beforeButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {

		if("保存".equals(bo.getName())) 
			beforeOnBoSave(ctx);
		
		else if("审核".equals(bo.getName()) || "送审".equals(bo.getName())) {
			if("列表".equals(((ExtSaleOrderAdminUI)ctx.getToftPanel()).strShowState))
				throw new BusinessException("列表状态不能进行此操作，请转至卡片界面操作。");
			
			beforeOnBoAudit(ctx , bo);
			
		} else if("弃审".equals(bo.getName())) {
			if("列表".equals(((ExtSaleOrderAdminUI)ctx.getToftPanel()).strShowState))
				throw new BusinessException("列表状态不能进行弃审操作，请转至卡片界面操作。");
			
			beforeOnBoCancleAudit(ctx);
		}
		
	}
	
	protected final void beforeOnBoSave(SCMUIContext ctx) throws BusinessException {
		BillItem contypeItem = ctx.getBillCardPanel().getBillData().getHeadItem("contracttype");
		if(contypeItem != null && contypeItem.getValueObject() != null) {
			BillItem conItem = ctx.getBillCardPanel().getBillData().getHeadItem("pk_contract");
			
			if(conItem == null || conItem.getValueObject() == null || "".equals(conItem.getValueObject()))
				throw new BusinessException ("销售合同不能为空！");
			
			Object mny = UAPQueryBS.getInstance().executeQuery("select nvl(sum(mny),0) from ehpta_adjust where pk_contract = '"+conItem.getValueObject()+"' and vbillstatus = 1 and nvl(dr,0)=0 ", new ColumnProcessor());
			
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
						
						Object num = UAPQueryBS.getInstance().executeQuery("select nvl(sum(num),0) from ehpta_sale_contract_bs where pk_contract = '"+conItem.getValueObject()+"' and nvl(dr,0)=0 ", new ColumnProcessor());
						
						if(nnumber.doubleValue() > new UFDouble(num.toString()).doubleValue())
							((ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel()).showWarningMessage("提货数量大于销售合同总数量");
					
					}
					
					// 现货合同保存时不做金额提示
//					if(sumMny.sub(Double.valueOf(headsummnyItem.getValueObject().toString())).doubleValue() < 0) 
//						((ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel()).showWarningMessage("合同余额小于本次提货金额");
					
					break;
					
				case 20 :
					
					Logger.info(" 长单合同 : Action -> Save ");
					
					billVO = ctx.getBillCardPanel().getBillData().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
					
					if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
						UFDouble nnumber = new UFDouble("0");
						for(CircularlyAccessibleValueObject bodyVO : billVO.getChildrenVO()) {
							nnumber = nnumber.add(Double.valueOf(bodyVO.getAttributeValue("nnumber").toString()));
						}
						
						Vector retVector = (Vector) UAPQueryBS.getInstance().executeQuery("select contb.num , contb.sdate , contb.edate from ehpta_sale_contract_b contb left join ehpta_sale_contract cont on cont.pk_contract = contb.pk_contract where cont.pk_contract = '"+billVO.getParentVO().getAttributeValue("pk_contract")+"' and nvl(cont.dr,0)=0 and nvl(contb.dr,0)=0", new VectorProcessor());
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
					
					Object nheadsummny =  UAPQueryBS.getInstance().executeQuery("select nvl(sum(nheadsummny),0) from so_sale where pk_contract is not null and nvl(dr,0)=0 and (contracttype = 10 or contracttype = 20 ) and pk_contract = '"+conItem.getValueObject()+"'", new ColumnProcessor());
					if(sumMny.sub(new UFDouble(nheadsummny.toString())).sub(Double.valueOf(headsummnyItem.getValueObject().toString())).doubleValue() < 0)
						((ExtSaleOrderAdminUI) ctx.getIctxpanel().getToftPanel()).showWarningMessage("合同余额小于本次提货金额");
					
					break;
				
				default :
					break;
					
			}
		}
	}

	protected final void beforeOnBoAudit(SCMUIContext ctx , ButtonObject bo) throws BusinessException {
		BillItem contypeItem = ctx.getBillCardPanel().getBillData().getHeadItem("contracttype");
		if(contypeItem != null && contypeItem.getValueObject() != null) {
			BillItem conItem = ctx.getBillCardPanel().getBillData().getHeadItem("pk_contract");
			
			if(conItem == null || conItem.getValueObject() == null || "".equals(conItem.getValueObject()))
				throw new BusinessException ("销售合同不能为空！");
			
			AggregatedValueObject billVO = ctx.getBillCardPanel().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
			if(billVO == null) 
				throw new BusinessException("billVO is null");
			
			Object mny = UAPQueryBS.getInstance().executeQuery(" select sum(mny) from vw_pta_sale_contract_balance where pk_contract = '"+conItem.getValueObject()+"' and iscredit = '"+billVO.getParentVO().getAttributeValue("iscredit")+"' ", new ColumnProcessor());
			UFDouble nowMny = new UFDouble("0",2);
			if(billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
				for(CircularlyAccessibleValueObject bodyVO : billVO.getChildrenVO()) {
					UFDouble noriginalcursummny = (UFDouble) bodyVO.getAttributeValue("noriginalcursummny");
					nowMny = nowMny.add(noriginalcursummny);
				}
			}
			
			if(new UFDouble(mny.toString()).sub(nowMny).doubleValue() < 0) {
				if("审核".equals(bo.getName())) {
					throw new BusinessException("合同余额小于本次提货金额。\n合同余额:[ "+new UFDouble(mny.toString()).sub(nowMny).toString()+" ]。\n审核失败！");
				} else if("送审".equals(bo.getName())) {
					int type = ctx.getToftPanel().showYesNoMessage("合同余额小于本次提货金额。\n合同余额:[ "+new UFDouble(mny.toString()).sub(nowMny).toString()+" ]。\n是否继续执行此操作！");
					if(type == UIDialog.ID_NO || type == UIDialog.ID_CANCEL)
						throw new BusinessException("本次操作已被取消！");
				}
			}
		}
	}
	
	protected final void beforeOnBoCancleAudit(SCMUIContext ctx) throws BusinessException {
		Object csaleid = ctx.getBillCardPanel().getBillData().getHeadItem("csaleid").getValueObject();
		Object settleflag = UAPQueryBS.getInstance().executeQuery("select nvl(settleflag,'N') from so_sale where csaleid = '"+csaleid+"'", new ColumnProcessor());
	
		if("Y".equals(settleflag))
			throw new BusinessException("挂结价差已结算，不能进行弃审操作！");
	}
	
	public void afterButtonClicked(ButtonObject bo, SCMUIContext ctx)
			throws BusinessException {
		
		if("合同余额".equals(bo.getName())) {
			
			int num = ctx.getBillListPanel().getHeadTable().getSelectedRow();
			Object[] obj = new Object[4];
			Object iscredit = false;
			if(num == -1) {
				iscredit = ctx.getBillCardPanel().getHeadItem("iscredit").getValueObject();
				
				obj[0] =  ctx.getBillCardPanel().getHeadItem("pk_contract").getValueObject();
				obj[0] = obj[0] == null ? "" : obj[0];
				
				obj[1] = "";
				
				obj[2] = (String) ctx.getBillCardPanel().getHeadItem("csaleid").getValueObject();
				obj[2] = obj[2] == null ? "" : obj[2];
				
				obj[3] = (String) ctx.getBillCardPanel().getHeadItem("concode").getValueObject();
				obj[3] = obj[3] == null ? "" : obj[3];
				
			} else { 
				
				iscredit = ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "iscredit");
				
				obj[0] = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "pk_contract");
				obj[0] = obj[0] == null ? "" : obj[0];
				
				obj[1] = "";
				
				obj[2] = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
				obj[2] = obj[2] == null ? "" : obj[2];
				
				obj[3] = (String) ctx.getBillListPanel().getHeadBillModel().getValueAt(num, "concode");
				obj[3] = obj[3] == null ? "" : obj[3];
				
			}
			
			String[] sqlString = new String[]{
					" select typename , mny from vw_pta_sale_contract_balance where pk_contract = '"+obj[0]+"' and iscredit = '"+new UFBoolean(iscredit.toString()).toString()+"' " , 
					" select '当前提货金额' , nvl(sum(nheadsummny) * -1 , 0) from so_sale where pk_contract is not null and nvl(dr, 0) = 0 and FSTATUS = 1 and (contracttype = 10 or contracttype = 20) and pk_contract = '"+obj[0]+"' and iscredit = '"+new UFBoolean(iscredit.toString()).toString()+"' group by pk_contract,iscredit "
			};
			
			SaleContractBalanceDlg balanceDlg = new SaleContractBalanceDlg(ctx.getIctxpanel().getToftPanel() , obj , sqlString);
			balanceDlg.showModal();
		} 

		try { 
			if("修改".equals(bo.getName()) || "增行".equals(bo.getName())) {
				
				Object period = ctx.getBillCardPanel().getHeadItem("period").getValueObject();
				String lastDay = CalcFunc.builder(new UFDate(period + "-01"));
				
				Object settlemny = UAPQueryBS.getInstance().executeQuery("select settlemny from ehpta_maintain where nvl(dr,0)=0 and vbillstatus = 1 and maindate >= '"+period+"-01' and maindate <= '"+period+"-"+lastDay+"' and type = 2", new ColumnProcessor());
				Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_maintain where nvl(dr,0)=0 and vbillstatus = 1 and maindate >= '"+period+"-01' and maindate <= '"+period+"-"+lastDay+"' and type = 1", new ColumnProcessor());
				
				
				if ("修改".equals(bo.getName())) {
						
					if(settlemny == null && count > 0) {
						ctx.getBillCardPanel().getBodyItem("lastingprice").setEdit(true);
						ctx.getBillCardPanel().getBodyItem("lastingprice").setEnabled(true);
						
						if(lpRef == null)
							lpRef = new UIRefPane();

						lpRef.setRefModel(new LPriceRefPane());
						lpRef.setWhereString("nvl(dr,0)=0 and vbillstatus = 1 and maindate >= '"+period+"-01' and maindate <= '"+period+"-"+lastDay+"' and type = 1");
						
						ctx.getBillCardPanel().getBodyItem("lastingprice").setComponent(lpRef);
					}
					
				} else if("增行".equals(bo.getName())) {
					
					if(settlemny != null) {
						
						AggregatedValueObject  billVO = ((ExtSaleOrderAdminUI)ctx.getToftPanel()).getVo();
						if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
							
							ctx.getBillCardPanel().setBodyValueAt(new UFDouble(settlemny.toString(),2), billVO.getChildrenVO().length - 1, "lastingprice", "table");
							ctx.getBillCardPanel().setBodyValueAt(new UFDouble(settlemny.toString(),2), billVO.getChildrenVO().length - 1, "settlementprice" , "table");
							ctx.getBillCardPanel().setBodyValueAt(new UFDouble(settlemny.toString(),2), billVO.getChildrenVO().length - 1, "noriginalcurtaxprice" , "table");
							
							ctx.getBillCardPanel().execBodyFormulas(billVO.getChildrenVO().length - 1, formulas);
							
							ctx.getBillCardPanel().getBodyItem("lastingprice").setEdit(false);
							ctx.getBillCardPanel().getBodyItem("lastingprice").setEnabled(false);
							
						}
					}
					
				}
			}
		} catch (Exception e) { 
			Logger.error(e.getMessage(), e, this.getClass(), "afterButtonClicked"); 
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
				
				if("period".equals(e.getKey()) || "iscredit".equals(e.getKey())) {
					
					if("period".equals(e.getKey()))
						afterSetInvdoc(e , ctx);
					
					afterSetPeriod(e , ctx);
				}
				
				if("issince".equals(e.getKey()))
					afterSetIsSince(e , ctx);
				
				if("vouchid".equals(e.getKey())) 
					ctx.getBillCardPanel().getHeadItem("vouchmny").setValue(((UIRefPane)ctx.getBillCardPanel().getHeadItem(e.getKey()).getComponent()).getRefCode());
					
			} else {
				
				if("nnumber".equals(e.getKey()))
					afterSetNnumber(e , ctx);
				
				if("lastingprice".equals(e.getKey()))
					afterSetLastingprice(e , ctx);
				
				if("noriginalcurtaxprice".equals(e.getKey()))
					afterSetNoriginalcurtaxprice(e , ctx);
				
			}
		} catch(Exception ex) {
			Logger.error(e);
		}

	}
	
	private final void afterSetInvdoc(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		/**
		 * 根据表头选择的期间到长单合同中查找对应期间或范围内的表体记录，
		 * 获取到存货相关信息及来源单据的相关信息
		 */
		
		Object period = ctx.getBillCardPanel().getHeadItem("period").getValueObject();
		Object pk_contract = ctx.getBillCardPanel().getHeadItem("pk_contract").getValueObject();
		SaleContractBVO[] contbVOs = (SaleContractBVO[]) HYPubBO_Client.queryByCondition(SaleContractBVO.class, " pk_contract = '"+pk_contract+"' and ((edate >= '"+period+"' and sdate <= '"+period+"') or sdate = '"+period+"') nvl(dr,0) = 0 ");
		if(contbVOs != null && contbVOs.length > 0) {
			
			AggregatedValueObject billVO = ((ExtSaleOrderAdminUI)ctx.getToftPanel()).getVo();
			if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0) {
				int[] rowNos = new int[billVO.getChildrenVO().length];
				for(int row = 0 , len = billVO.getChildrenVO().length ; row < len ; row ++ ) {
					rowNos[row] = row;
				}
				
				((ExtSaleOrderAdminUI)ctx.getToftPanel()).onDelLine(rowNos);
				
			}
			
			((ExtSaleOrderAdminUI)ctx.getToftPanel()).onAddLine();
			
//			Object pk_invbasdoc = UAPQueryBS.getInstance().executeQuery("select pk_invbasdoc from bd_invmandoc where pk_invmandoc = '"+contbVOs[0].getPk_invbasdoc()+"'", new ColumnProcessor());
			
//			ctx.getBillCardPanel().setBodyValueAt(contbVOs[0].getPk_invbasdoc(), 0, "pk_invmandoc");
			ctx.getBillCardPanel().setBodyValueAt(contbVOs[0].getPk_invbasdoc(), 0, "cinventoryid");
//			ctx.getBillCardPanel().setBodyValueAt(contbVOs[0].getPk_invbasdoc(), 0, "cinventoryid1");
			
			ctx.getBillCardPanel().setBodyValueAt(contbVOs[0].getPk_contract_b(), 0, "csourcebillbodyid");
			ctx.getBillCardPanel().setBodyValueAt(contbVOs[0].getPk_contract(), 0, "csourcebillid");
			
//			ctx.getBillCardPanel().execBodyFormula(0, "cinventoryid");
//			ctx.getBillCardPanel().execBodyFormula(0, "cinventorycode");
			
			BillItem[]  billItems = ctx.getBillCardPanel().getBodyItems();
			
			for(BillItem item : billItems) {
				ctx.getBillCardPanel().execBodyFormula(0, item.getKey());
			}
			
			ctx.getBillCardPanel().execBodyFormulas(0, new String[]{
					"cinventorycode->getColValue(bd_invbasdoc,invcode,pk_invbasdoc,cinvbasdocid)"
			});
			
			/* 待续... */
			
		}
	}
	
	private final void afterSetIsSince(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		Object issince = ctx.getBillCardPanel().getHeadItem(e.getKey()).getValueObject();
		if("true".equals(issince)) {
			((UIRefPane)ctx.getBillCardPanel().getHeadItem("pk_transport").getComponent()).setEnabled(false);
			((UIRefPane)ctx.getBillCardPanel().getHeadItem("pk_transport").getComponent()).setEditable(false);
			((UIRefPane)ctx.getBillCardPanel().getHeadItem("pk_transport").getComponent()).setPK(null);
			ctx.getBillCardPanel().getHeadItem("pk_transport").setValue(null);
			ctx.getBillCardPanel().getHeadItem("pk_transport").setNull(false);
		} else {
			((UIRefPane)ctx.getBillCardPanel().getHeadItem("pk_transport").getComponent()).setEnabled(true);
			((UIRefPane)ctx.getBillCardPanel().getHeadItem("pk_transport").getComponent()).setEditable(true);
			ctx.getBillCardPanel().getHeadItem("pk_transport").setNull(true);
		}
		
	}
	
	/**
	 *  功能 ： 运输合同控制
	 *  
	 *  Author : river 
	 *  
	 *  Create : 2012-08-17
	 *  
	 * @param e
	 * @param ctx
	 * @throws Exception
	 */
	private final void afterSetPk_transport(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		Object pk_transport = ctx.getBillCardPanel().getHeadItem(e.getKey()).getValueObject();
		
		StringBuilder builder = new StringBuilder();
		builder.append(" select bas.custname , addr.addrname from bd_cubasdoc bas left join bd_cumandoc man on man.pk_cubasdoc = bas.pk_cubasdoc ");
		builder.append(" left join ehpta_transport_contract transcont on transcont.pk_carrier = man.pk_cumandoc ");
		builder.append(" left join bd_custaddr addr on addr.pk_cubasdoc = bas.pk_cubasdoc ");
		builder.append(" where transcont.pk_transport = '"+pk_transport+"' and addr.defaddrflag = 'Y' ");
		
		Vector retVector = (Vector) UAPQueryBS.getInstance().executeQuery(builder.toString(), new VectorProcessor());
		
		if(retVector == null || retVector.size() == 0) {
			builder = new StringBuilder();
			builder.append(" select bas.custname , addr.addrname from bd_cubasdoc bas left join bd_cumandoc man on man.pk_cubasdoc = bas.pk_cubasdoc ");
			builder.append(" left join ehpta_transport_contract transcont on transcont.pk_carrier = man.pk_cumandoc ");
			builder.append(" left join bd_custaddr addr on addr.pk_cubasdoc = bas.pk_cubasdoc ");
			builder.append(" where transcont.pk_transport = '"+pk_transport+"' and rownum = 1  ");
			retVector = (Vector) UAPQueryBS.getInstance().executeQuery(builder.toString(), new VectorProcessor());
		}
		
		if(retVector != null && retVector.size() > 0) {
			Object custname = ((Vector)retVector.get(0)).get(0);
			Object addrname = ((Vector)retVector.get(0)).get(1);
			
			ctx.getBillCardPanel().getHeadItem("carriersname").setValue(custname);
			ctx.getBillCardPanel().getHeadItem("carriersaddr").setValue(addrname);
			
		} else {
			ctx.getBillCardPanel().getHeadItem("carriersname").setValue(null);
			ctx.getBillCardPanel().getHeadItem("carriersaddr").setValue(null); 
		}
	}
	
	/**
	 *  功能 ：仓库控制
	 *  
	 *  Author : river 
	 *  
	 *  Create : 2012-08-17
	 *  
	 * @param e
	 * @param ctx
	 * @throws Exception
	 */
	private final void afterSetStorage(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		Object pk_stordoc = ctx.getBillCardPanel().getHeadItem(e.getKey()).getValueObject();
		
		Object storaddr = UAPQueryBS.getInstance().executeQuery("select storaddr from bd_stordoc where pk_stordoc = '"+pk_stordoc+"'", new ColumnProcessor());
		
		ctx.getBillCardPanel().getHeadItem("storageaddress").setValue(storaddr);
		
	}
	
	/**
	 *  功能 ： 执行期间控制
	 *  
	 *  Author : river 
	 *  
	 *  Create : 2012-08-17
	 *  
	 * @param e
	 * @param ctx
	 * @throws Exception
	 */
	private final void afterSetPeriod(BillEditEvent e, SCMUIContext ctx) throws Exception {

		Object period = ctx.getBillCardPanel().getHeadItem("period").getValueObject();
		AggregatedValueObject billVO = ctx.getBillCardPanel().getBillData().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
		Boolean iscredit = new Boolean(ctx.getBillCardPanel().getHeadItem("iscredit").getValueObject().toString());
		
		if(iscredit) {
			ctx.getBillCardPanel().getHeadItem("vouchid").setNull(true);
			ctx.getBillCardPanel().getHeadItem("vouchid").setEdit(true);
			ctx.getBillCardPanel().getHeadItem("vouchid").setEnabled(true);
		} else {
			ctx.getBillCardPanel().getHeadItem("vouchid").setNull(false);
			ctx.getBillCardPanel().getHeadItem("vouchid").setEdit(false);
			ctx.getBillCardPanel().getHeadItem("vouchid").setEnabled(false);
			ctx.getBillCardPanel().getHeadItem("vouchid").setValue(null);
			ctx.getBillCardPanel().getHeadItem("vouchmny").setValue(null);
		}
		
		if(period != null && !"".equals(period) && !iscredit) {
			
			String lastDay = CalcFunc.builder(new UFDate(period + "-01"));
			
			Object settlemny = UAPQueryBS.getInstance().executeQuery("select settlemny from ehpta_maintain where nvl(dr,0)=0 and vbillstatus = 1 and maindate >= '"+period+"-01' and maindate <= '"+period+"-"+lastDay+"' and type = 2", new ColumnProcessor());
			Integer count = (Integer) UAPQueryBS.getInstance().executeQuery("select nvl(count(1),0) from ehpta_maintain where nvl(dr,0)=0 and vbillstatus = 1 and maindate >= '"+period+"-01' and maindate <= '"+period+"-"+lastDay+"' and type = 1", new ColumnProcessor());
			
			if(settlemny != null && !"".equals(settlemny)) {
				
				if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
					for(int i = 0 , j = billVO.getChildrenVO().length ; i < j ; i ++) {
						ctx.getBillCardPanel().setBodyValueAt(new UFDouble(settlemny.toString(),2), i, "lastingprice", "table");
						ctx.getBillCardPanel().setBodyValueAt(new UFDouble(settlemny.toString(),2), i, "settlementprice" , "table");
						ctx.getBillCardPanel().setBodyValueAt(new UFDouble(settlemny.toString(),2), i, "noriginalcurtaxprice" , "table");
						
						ctx.getBillCardPanel().execBodyFormulas(i, formulas);
					}
					
					ctx.getBillCardPanel().getBodyItem("lastingprice").setEdit(false);
					ctx.getBillCardPanel().getBodyItem("lastingprice").setEnabled(false);
				}
			} else if(count > 0) {
				
				if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
					for(int i = 0 , j = billVO.getChildrenVO().length ; i < j ; i ++) {
						ctx.getBillCardPanel().setBodyValueAt(null, i, "lastingprice", "table");
						ctx.getBillCardPanel().setBodyValueAt(null, i, "settlementprice" , "table");
						ctx.getBillCardPanel().setBodyValueAt(null, i, "noriginalcurtaxprice" , "table");
						ctx.getBillCardPanel().execBodyFormulas(i, formulas);
					}

					ctx.getBillCardPanel().getBodyItem("lastingprice").setComponent(new UITextField());
					ctx.getBillCardPanel().getBodyItem("lastingprice").setEdit(false);
					ctx.getBillCardPanel().getBodyItem("lastingprice").setEnabled(false);
				}
				
				ctx.getBillCardPanel().getBodyItem("lastingprice").setEdit(true);
				ctx.getBillCardPanel().getBodyItem("lastingprice").setEnabled(true);
				
				if(lpRef == null)
					lpRef = new UIRefPane();

				lpRef.setRefModel(new LPriceRefPane());
				lpRef.setWhereString("nvl(dr,0)=0 and vbillstatus = 1 and maindate >= '"+period+"-01' and maindate <= '"+period+"-"+lastDay+"' and type = 1");
				
					
				ctx.getBillCardPanel().getBodyItem("lastingprice").setComponent(lpRef);
				
			} else {
				if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
					for(int i = 0 , j = billVO.getChildrenVO().length ; i < j ; i ++) {
						ctx.getBillCardPanel().setBodyValueAt(null, i, "lastingprice", "table");
						ctx.getBillCardPanel().setBodyValueAt(null, i, "settlementprice" , "table");
						ctx.getBillCardPanel().setBodyValueAt(null, i, "noriginalcurtaxprice" , "table");
						
						ctx.getBillCardPanel().execBodyFormulas(i, formulas);
						
						ctx.getBillCardPanel().getBodyItem("lastingprice").setComponent(new UITextField());
						ctx.getBillCardPanel().getBodyItem("lastingprice").setEdit(false);
						ctx.getBillCardPanel().getBodyItem("lastingprice").setEnabled(false);
						
					}
				}
			}
			
			ctx.getBillCardPanel().getBodyItem("noriginalcurtaxprice").setEdit(false);
			ctx.getBillCardPanel().getBodyItem("noriginalcurtaxprice").setEnabled(false);
		
		} else {
			if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
				for(int i = 0 , j = billVO.getChildrenVO().length ; i < j ; i ++) {
					ctx.getBillCardPanel().setBodyValueAt(null, i, "lastingprice", "table");
					ctx.getBillCardPanel().setBodyValueAt(null, i, "settlementprice" , "table");
					ctx.getBillCardPanel().setBodyValueAt(null, i, "noriginalcurtaxprice" , "table");
					
					if("10".equals(billVO.getParentVO().getAttributeValue("contracttype"))) {
						Object csourcebillbodyid = billVO.getChildrenVO()[i].getAttributeValue("csourcebillbodyid");
						SaleContractBsVO sourcebodyVO = (SaleContractBsVO) HYPubBO_Client.queryByPrimaryKey(SaleContractBsVO.class, (String)csourcebillbodyid);
						ctx.getBillCardPanel().setBodyValueAt(sourcebodyVO.getTaxprice(), i, "noriginalcurtaxprice" , "table");
						sourcebodyVO = null;
					}
					
					ctx.getBillCardPanel().execBodyFormulas(i, formulas);
					
				}
				
				ctx.getBillCardPanel().getBodyItem("lastingprice").setEdit(false);
				ctx.getBillCardPanel().getBodyItem("lastingprice").setEnabled(false);
				
				if(iscredit) {
					ctx.getBillCardPanel().getBodyItem("noriginalcurtaxprice").setEdit(true);
					ctx.getBillCardPanel().getBodyItem("noriginalcurtaxprice").setEnabled(true);
				}
			}
		}
	}
	
	/**
	 *  功能 ： 数量控制
	 *  
	 *  Author : river 
	 *  
	 *  Create : 2012-08-17
	 *  
	 * @param e
	 * @param ctx
	 * @throws Exception
	 */
	private final void afterSetNnumber(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		AggregatedValueObject billVO = ctx.getBillCardPanel().getBillData().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
		if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
			for(int i = 0 , j = billVO.getChildrenVO().length ; i < j ; i ++) {

				ctx.getBillCardPanel().execBodyFormulas(i, formulas);
				
			}
		}
		
		AggregatedValueObject  selVO = ((ExtSaleOrderAdminUI)ctx.getToftPanel()).getVo();
		
		if(selVO != null) {
			UFDouble noriginalcursummny = new UFDouble("0" , 2);
			for(CircularlyAccessibleValueObject vo : selVO.getChildrenVO()) {
				noriginalcursummny = noriginalcursummny.add(vo.getAttributeValue("noriginalcursummny") == null ? new UFDouble("0") : new UFDouble(vo.getAttributeValue("noriginalcursummny").toString()));
			}
		
			ctx.getBillCardPanel().execHeadFormula("nheadsummny->" + noriginalcursummny.toString());

		}
		
	}
	
	/**
	 *  功能 ：挂牌价控制
	 *  
	 *  Author : river 
	 *  
	 *  Create : 2012-08-17
	 *  
	 * @param e
	 * @param ctx
	 * @throws Exception
	 */
	private final void afterSetLastingprice(BillEditEvent e, SCMUIContext ctx) throws Exception {
		if(e.getValue() != null && !"".equals(e.getValue())) {
			ctx.getBillCardPanel().setBodyValueAt(null, e.getRow(), "settlementprice", "table");
			ctx.getBillCardPanel().setBodyValueAt(new UFDouble(e.getValue().toString(),2), e.getRow(), "noriginalcurtaxprice", "table");
			
			ctx.getBillCardPanel().execBodyFormulas(e.getRow(), formulas);
			
		} else {
			ctx.getBillCardPanel().setBodyValueAt(null, e.getRow(), "settlementprice", "table");
			ctx.getBillCardPanel().setBodyValueAt(null, e.getRow(), "noriginalcurtaxprice", "table");
			
			ctx.getBillCardPanel().execBodyFormulas(e.getRow(), formulas);
		}
		
		AggregatedValueObject  selVO = ((ExtSaleOrderAdminUI)ctx.getToftPanel()).getVo();
		
		if(selVO != null) {
			UFDouble noriginalcursummny = new UFDouble("0" , 2);
			for(CircularlyAccessibleValueObject vo : selVO.getChildrenVO()) {
				noriginalcursummny = noriginalcursummny.add(vo.getAttributeValue("noriginalcursummny") == null ? new UFDouble("0") : new UFDouble(vo.getAttributeValue("noriginalcursummny").toString()));
			}
		
			ctx.getBillCardPanel().execHeadFormula("nheadsummny->" + noriginalcursummny.toString());
		}
		
		
		
	}
	
	/**
	 *  功能 ：当订单为信用证方式时，挂牌价不可选，挂牌价由含税单价获得。
	 *  
	 *  Author : river 
	 *  
	 *  Create : 2012-08-17
	 *  
	 * @param e
	 * @param ctx
	 * @throws Exception
	 */
	private final void afterSetNoriginalcurtaxprice(BillEditEvent e, SCMUIContext ctx) throws Exception {
		
		String[] formulas = new String[]{
				"lastingprice->noriginalcurtaxprice",
				"settlementprice->noriginalcurtaxprice",
		};
		
		AggregatedValueObject billVO = ctx.getBillCardPanel().getBillData().getBillValueVO(SaleOrderVO.class.getName(), SaleorderHVO.class.getName(), SaleorderBVO.class.getName());
		if(billVO != null && billVO.getChildrenVO() != null && billVO.getChildrenVO().length > 0 ) {
			if(!"10".equals(billVO.getParentVO().getAttributeValue("contracttype"))) {
				for(int i = 0 , j = billVO.getChildrenVO().length ; i < j ; i ++) {
					ctx.getBillCardPanel().execBodyFormulas(i, formulas);
					
				}
			}
		}
		
		AggregatedValueObject  selVO = ((ExtSaleOrderAdminUI)ctx.getToftPanel()).getVo();
		
		if(selVO != null) {
			UFDouble noriginalcursummny = new UFDouble("0" , 2);
			for(CircularlyAccessibleValueObject vo : selVO.getChildrenVO()) {
				noriginalcursummny = noriginalcursummny.add(vo.getAttributeValue("noriginalcursummny") == null ? new UFDouble("0") : new UFDouble(vo.getAttributeValue("noriginalcursummny").toString()));
			}
		
			ctx.getBillCardPanel().execHeadFormula("nheadsummny->" + noriginalcursummny.toString());
		}
		
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
