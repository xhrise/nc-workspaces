package nc.ui.ehpta.hq010301;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.convert.ConvertFunc;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.fp.combase.pub01.IBillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {

	
	protected final String[] gTaxprice = new String[] {
		"norgqttaxnetprc" , 
		"norgqttaxprc" ,
		"noriginalcurtaxnetprice" ,
		"noriginalcurtaxprice" ,
		"nqttaxnetprc" ,
		"nqttaxprc" ,
		"ntaxnetprice" ,
		"ntaxprice" ,
		"lastingprice" ,
		"settlementprice" ,
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
	
	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onBoSave() throws Exception {
		
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel() , getBillCardPanelWrapper().getBillVOFromUI(),null);
		
		
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		billVO.getParentVO().setAttributeValue("maintime", getBillUI().getEnvironment().getServerTime().toString());
        setTSFormBufferToVO(billVO);
        AggregatedValueObject checkVO = billVO;
        setTSFormBufferToVO(checkVO);
        Object o = null;
        ISingleController sCtrl = null;
        if(getUIController() instanceof ISingleController)
        {
            sCtrl = (ISingleController)getUIController();
            if(sCtrl.isSingleDetail())
            {
                o = billVO.getParentVO();
                billVO.setParentVO(null);
            } else
            {
                o = billVO.getChildrenVO();
                billVO.setChildrenVO(null);
            }
        }
        boolean isSave = true;
        if(billVO.getParentVO() == null && (billVO.getChildrenVO() == null || billVO.getChildrenVO().length == 0))
            isSave = false;
        else
        if(getBillUI().isSaveAndCommitTogether())
            billVO = getBusinessAction().saveAndCommit(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
        else
            billVO = getBusinessAction().save(billVO, getUIController().getBillType(), _getDate().toString(), getBillUI().getUserObject(), checkVO);
        if(sCtrl != null && sCtrl.isSingleDetail())
            billVO.setParentVO((CircularlyAccessibleValueObject)o);
        int nCurrentRow = -1;
        if(isSave)
            if(isEditing())
            {
                if(getBufferData().isVOBufferEmpty())
                {
                    getBufferData().addVOToBuffer(billVO);
                    nCurrentRow = 0;
                } else
                {
                    getBufferData().setCurrentVO(billVO);
                    nCurrentRow = getBufferData().getCurrentRow();
                }
            } else
            {
                getBufferData().addVOsToBuffer(new AggregatedValueObject[] {
                    billVO
                });
                nCurrentRow = getBufferData().getVOBufferSize() - 1;
            }
        if(nCurrentRow >= 0)
            getBufferData().setCurrentRowWithOutTriggerEvent(nCurrentRow);
        setAddNewOperate(isAdding(), billVO);
        setSaveOperateState();
        if(nCurrentRow >= 0)
            getBufferData().setCurrentRow(nCurrentRow);
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		
		if("1".equals(getBufferData().getCurrentVO().getParentVO().getAttributeValue("type"))) {
			((UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("listingmny").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("listingmny").getComponent()).setEditable(true);
			((UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("settlemny").getComponent()).setEnabled(false);
		}else{
			
			((UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("settlemny").getComponent()).setEnabled(true);
			((UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("settlemny").getComponent()).setEditable(true);
			((UIRefPane)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("listingmny").getComponent()).setEnabled(false);
		}
		
	}
		
	@Override
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		
		afterOnBoAudit();
		
	}
	
	/**
	 *  river
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final void afterOnBoAudit() throws Exception {
		
		AggregatedValueObject  billVO = getBufferData().getCurrentVO();
		
		if(billVO != null && billVO.getParentVO() != null ) {
		
			Object vbillstatus = billVO.getParentVO().getAttributeValue("vbillstatus");
			Object type = billVO.getParentVO().getAttributeValue("type");
			UFDouble settlemny = (UFDouble) billVO.getParentVO().getAttributeValue("settlemny");
			Object temp = billVO.getParentVO().getAttributeValue("maindate");
			UFDate maindate = null;
			String period = "";
			if(temp != null )
				maindate = new UFDate(temp.toString());
			
			if(maindate != null) {
				period = maindate.getYear() + "-" + (maindate.getMonth() < 10 ? "0" + maindate.getMonth() : "" + maindate.getMonth());
			}
			
			if(type != null && !"1".equals(type) && vbillstatus != null && IBillStatus.CHECKPASS == Integer.valueOf(vbillstatus.toString())) {
				
				StringBuilder orderbBuilder = new StringBuilder();
				orderbBuilder.append(" select orderb.* from so_saleorder_b orderb left join so_sale sale on sale.csaleid = orderb.csaleid ");
				orderbBuilder.append(" where not exists (select 1 from so_saleinvoice_b invb where orderb.corder_bid = invb.csourcebillbodyid and nvl(invb.dr,0)=0) ");
				orderbBuilder.append(" and sale.pk_contract is not null and sale.contracttype = '20' and nvl(orderb.settlementprice,0) <> " + settlemny );
				orderbBuilder.append(" and sale.settletype = '"+type+"' and sale.period = '"+period+"' and nvl(orderb.dr,0)=0 and nvl(sale.dr,0)=0 ");
				
				List<Map> orderbMapList = (List<Map>) UAPQueryBS.getInstance().executeQuery(orderbBuilder.toString(), new MapListProcessor());
				
				if(orderbMapList != null && orderbMapList.size() > 0) {
					
					Map<String , Map<String , UFDouble>> retMap = new ConcurrentHashMap<String , Map<String , UFDouble>>();
					Set<String> csaleidSet = new HashSet<String>();
					
					for(Map orderbMap : orderbMapList) {
						
						UFDouble ntaxrate = new UFDouble(orderbMap.get("ntaxrate") == null ? "0" : orderbMap.get("ntaxrate").toString());
						UFDouble nnumber = new UFDouble(orderbMap.get("nnumber") == null ? "0" : orderbMap.get("nnumber").toString());
						String corder_bid = (String) orderbMap.get("corder_bid");
						String csaleid = (String) orderbMap.get("csaleid");
						
						Map<String , UFDouble> execValMap = new ConcurrentHashMap<String , UFDouble>();
						
						for(String attr : gTaxprice) {
							execValMap.put(attr, settlemny.setScale(2, 0));
						}
						
						UFDouble price = settlemny.div(ntaxrate.div(100).add(1));
						for(String attr : gPrice) {
							execValMap.put(attr, price.setScale(2, 0));
						}
						
						UFDouble summny = settlemny.multiply(nnumber);
						for(String attr : gSummny) {
							execValMap.put(attr, summny.setScale(2, 0));
						}
						
						UFDouble taxmny = settlemny.sub(price).multiply(nnumber);
						for(String attr : gTaxmny) {
							execValMap.put(attr, taxmny.setScale(2, 0));
						}
						
						UFDouble mny = summny.sub(taxmny);
						for(String attr : gMny) {
							execValMap.put(attr, mny.setScale(2, 0));
						}
						
						retMap.put(corder_bid, execValMap);
						
						csaleidSet.add(csaleid);
					}
					
					// 更新销售订单子表金额相关的字段。
					Iterator iter = retMap.entrySet().iterator();
					while(iter.hasNext()) {
						Entry entry = (Entry) iter.next();
						
						Map valMap = (Map) entry.getValue();
						
						Iterator valIter = valMap.entrySet().iterator();
						List<String> fieldList = new ArrayList<String>();
						
						while(valIter.hasNext()) {
							Entry valEntry = (Entry) valIter.next();
							fieldList.add(valEntry.getKey() + " = '" + valEntry.getValue() + "'");
						}
						
						try { 
							UAPQueryBS.getInstance().executeQuery("update so_saleorder_b set " + ConvertFunc.change(fieldList.toArray(new String[0])) +" where corder_bid = '"+entry.getKey()+"'", null); 
						} catch(Exception ex) { 
							Logger.info("update so_saleorder_b by primary key : " + entry.getKey()); 
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
			}
		}
	}
	
	
}