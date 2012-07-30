package nc.ui.so.so001.order;

import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.scm.extend.IFuncExtend;
import nc.ui.scm.so.SaleBillType;
import nc.ui.so.so001.panel.SaleBillUI;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.so.so001.SaleOrderVO;

/**
 * 销售订单管理 创建日期：(2012-07-22 )
 * 
 * @author river
 * 
 * 
 */
@SuppressWarnings({ "serial", "restriction" })
public class ExtSaleOrderAdminUI extends SaleBillUI implements BillCardBeforeEditListener {

	//按钮初始化标记
	private boolean b_init;
	
	public ExtSaleOrderAdminUI() {
		super();
		
		// 设置表头及表尾的编辑前事件
		// add by river for 2012-07-23
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	
	public ExtSaleOrderAdminUI(String pk_corp, String billtype, String busitype, String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
		
		// 设置表头及表尾的编辑前事件
		// add by river for 2012-07-23
		getBillCardPanel().setBillBeforeEditListenerHeadTail(this);
	}
	
	@Override
	public String getBillButtonAction(nc.ui.pub.ButtonObject bo) {
		return null;
	}

	@Override
	public String getBillButtonState() {
		return null;
	}

	/**
	 * 获得按钮数组。
	 * 
	 * 支持插件菜单,location规则如下：0 卡片列表;1 退货;2 BOM; 3 核销
	 * 
	 * 创建日期：(2001-11-15 8:58:51)
	 * 
	 * @return nc.ui.pub.ButtonObject[]
	 */
	public nc.ui.pub.ButtonObject[] getBillButtons() {
		if (!b_init) {
			initBtnGrp();
			b_init = true;
		}

		if (strShowState.equals("列表")) {
			return aryListButtonGroup;
		} else if (strShowState.equals("卡片")) {
			initLineButton();
			return aryButtonGroup;
		} else {
			return aryBatchButtonGroup;
		}
	}

	/**
	 * V51要求卡片和列表的按钮一致
	 * 
	 */
	private void initBtnGrp() {

		// 列表按钮
		ButtonObject[] aryListButtonGroup = { boBusiType, boAdd, boSave, boMaintain, boLine, boAudit, boAction,
				boQuery, boBrowse, boCard, boPrntMgr, boAssistant, boAsstntQry };

		// 卡片按钮
		ButtonObject[] aryButtonGroup = { boBusiType, boAdd, boSave, boMaintain, boLine, boAudit, boAction, boQuery,
				boBrowse, boReturn, boPrntMgr, boAssistant, boAsstntQry };

		// 退货卡片按钮
		ButtonObject[] aryBatchButtonGroup = { boBatch, boLine, boRefundmentDocument, boBack };

		// 配置销售按钮组
		ButtonObject[] bomButtonGroup = { boBomSave, boBomEdit, boBomCancel, boBomReturn, boOrderQuery,
		//				boBomPrint 
		};

		IFuncExtend funcExtend = getFuncExtend();
		if (funcExtend != null) {
			ButtonObject[] boExtend = m_funcExtend.getExtendButton();
			if (boExtend != null && boExtend.length > 0) {

				// 卡片按钮
				ButtonObject[] botempcard = new ButtonObject[aryButtonGroup.length + boExtend.length];

				System.arraycopy(aryButtonGroup, 0, botempcard, 0, aryButtonGroup.length);

				System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length, boExtend.length);

				aryButtonGroup = botempcard;

				// 列表按钮
				ButtonObject[] botemplist = new ButtonObject[aryListButtonGroup.length + boExtend.length];

				System.arraycopy(aryListButtonGroup, 0, botemplist, 0, aryListButtonGroup.length);

				System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length, boExtend.length);

				aryListButtonGroup = botemplist;
			}

		}
		ButtonObject[] boExtend = getExtendBtns();
		if (boExtend != null && boExtend.length > 0) {

			// 卡片按钮
			ButtonObject[] botempcard = new ButtonObject[aryButtonGroup.length + boExtend.length];

			System.arraycopy(aryButtonGroup, 0, botempcard, 0, aryButtonGroup.length);

			System.arraycopy(boExtend, 0, botempcard, aryButtonGroup.length, boExtend.length);

			aryButtonGroup = botempcard;

			// 列表按钮
			ButtonObject[] botemplist = new ButtonObject[aryListButtonGroup.length + boExtend.length];

			System.arraycopy(aryListButtonGroup, 0, botemplist, 0, aryListButtonGroup.length);

			System.arraycopy(boExtend, 0, botemplist, aryListButtonGroup.length, boExtend.length);

			aryListButtonGroup = botemplist;
		}

		// 导入插件菜单
		ButtonObject[][] ret_butns = new SaleOrderPluginMenuUtil().addMenu(aryListButtonGroup, aryButtonGroup,
				aryBatchButtonGroup, bomButtonGroup, getModuleCode());

		this.aryListButtonGroup = ret_butns[0];
		this.aryButtonGroup = ret_butns[1];
		this.aryBatchButtonGroup = ret_butns[2];
		this.bomButtonGroup = ret_butns[3];

	}

	@Override
	public String getBillID() {

		if ("退货".equals(strState)) { /*-=notranslate=-*/
			return id;
		}

		if (strShowState == "列表") {
			return (String) getBillListPanel().getHeadBillModel().getValueAt(num, "csaleid");
		} else if (strShowState == "卡片") {
			return (String) getBillCardPanel().getHeadItem("csaleid").getValueObject();
		} else
			return null;
	}

	@Override
	public String getNodeCode() {
		return "40060301";
	}

	@Override
	public String getTitle() {
		return getBillListPanel().getBillListData().getTitle();
	}

	protected void initButtons() {

		// 业务类型
		PfUtilClient.retBusinessBtn(boBusiType, getCorpPrimaryKey(), SaleBillType.SaleOrder);

		if (boBusiType.getChildButtonGroup() != null && boBusiType.getChildButtonGroup().length > 0) {
			boBusiType.setTag(boBusiType.getChildButtonGroup()[0].getTag());
			boBusiType.getChildButtonGroup()[0].setSelected(true);
			boBusiType.setCheckboxGroup(true);

			// 新增
			PfUtilClient.retAddBtn(boAdd, getCorpPrimaryKey(), getBillType(), boBusiType.getChildButtonGroup()[0]);

		}

		// 维护
		boMaintain.removeAllChildren();
		boMaintain.addChildButton(boEdit);
		boMaintain.addChildButton(boCancel);
		boMaintain.addChildButton(boBlankOut);
		boMaintain.addChildButton(boCopyBill);

		// 行操作
		boLine.removeAllChildren();
		boLine.addChildButton(boAddLine);
		boLine.addChildButton(boDelLine);
		boLine.addChildButton(boInsertLine);
		boLine.addChildButton(boCopyLine);
		boLine.addChildButton(boPasteLine);
		boLine.addChildButton(boPasteLineToTail);
		boLine.addChildButton(boFindPrice);
		boLine.addChildButton(boCardEdit);
		boLine.addChildButton(boResortRowNo);

		// 浏览
		boBrowse.removeAllChildren();
		boBrowse.addChildButton(boRefresh);
		boBrowse.addChildButton(boFind);
		boBrowse.addChildButton(boFirst);
		boBrowse.addChildButton(boPre);
		boBrowse.addChildButton(boNext);
		boBrowse.addChildButton(boLast);
		boBrowse.addChildButton(boListSelectAll);
		boBrowse.addChildButton(boListDeselectAll);

		//执行
		retElseBtn("Order002", "Order001");
		retElseBtn("Order002", "Order003");

		//辅助查询
		boAsstntQry.removeAllChildren();
		boAsstntQry.addChildButton(boOrderQuery);
		boAsstntQry.addChildButton(boOnHandShowHidden);
		boAsstntQry.addChildButton(boAuditFlowStatus);
		boAsstntQry.addChildButton(boCustCredit);
		boAsstntQry.addChildButton(boOrderExecRpt);
		boAsstntQry.addChildButton(boCustInfo);
		boAsstntQry.addChildButton(boPrifit);
		
		// 添加 合同余额 按钮
		// add by river for 2012-07-30
		// start ..
		boAsstntQry.addChildButton(new ButtonObject("合同余额" , "" , 0 , "合同余额"));
		// ..end 

		//打印
		boPrntMgr.removeAllChildren();
		boPrntMgr.addChildButton(boPreview);
		boPrntMgr.addChildButton(boPrint);
		boPrntMgr.addChildButton(boSplitPrint);

		//协同
		boAssistant.addChildButton(boCoPushPo);
		boAssistant.addChildButton(boCoRefPo);

		if (strShowState.equals("列表")) { /*-=notranslate=-*/
			setButtons(getBillButtons());
		}//end list
		else if (strShowState.equals("卡片")) { /*-=notranslate=-*/

			if (strState.equals("BOM")) {
				setButtons(bomButtonGroup);
			} else {
				setButtons(getBillButtons());
			}

		}//end card
	}
	
	public void setButtonsState() { 
		super.setButtonsState();
		
		getPluginProxy().setButtonStatus();
	}

	@Override
	protected String getClientUI() {
		return ExtSaleOrderAdminUI.class.getName();
	}
	
	/**
	 *  功能 : 增加时控制合同是否已经添加，长单合同不做控制。
	 *  
	 *  Author : river
	 *  
	 *  Create Date : 2012-07-26
	 *  
	 */
	@Override
	protected void onNewByOther(AggregatedValueObject[] saleVOs)
			throws Exception {
		
		if(saleVOs != null && saleVOs.length > 0) {
			Object pk_contract = saleVOs[0].getParentVO().getAttributeValue("pk_contract");
			if(pk_contract != null) {
				Integer count = (Integer) UAPQueryBS.iUAPQueryBS.executeQuery("select count(1) from so_sale where pk_contract = '"+pk_contract+"' and nvl(dr,0)=0 and contracttype = 10 ", new ColumnProcessor());
			
				if(count > 0 )
					throw new Exception("该合同已经录入销售订单！");
			}
		}
		
		super.onNewByOther(saleVOs);
	}
	
	@Override
	protected void onCard() {
		super.onCard();
		
	}
	
	// 表头及表尾ITEM的编辑前事件
	// add by river for 2012-07-23
	public boolean beforeEdit(BillItemEvent e) {
		
		if(getBillCardPanel().getHeadItem("contracttype") != null) {
			
			UIComboBox comboBox = ((UIComboBox)getBillCardPanel().getHeadItem("contracttype").getComponent());
			if(comboBox != null && comboBox.getSelectdItemValue() != null) {
				if("ccustomerid".equals(e.getItem().getKey()) || "creceiptcorpid".equals(e.getItem().getKey()) || "creceiptcustomerid".equals(e.getItem().getKey())) {
				
					Object pk_contract = getBillCardPanel().getHeadItem("pk_contract").getValueObject();
				
					UIRefPane ccustomerRef = ((UIRefPane)e.getItem().getComponent());
					
					String wherePart = " 1 = 1  and (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";
					
					String nextWherePart = "";
					
					switch(Integer.valueOf(comboBox.getSelectdItemValue().toString())) {
					case 10 :
						nextWherePart = wherePart + " and pk_cumandoc in (select purchcode from ehpta_sale_contract where pk_contract = '"+pk_contract+"') ";
						break;
						
					case 20 : 
						
						nextWherePart = wherePart + " and pk_cumandoc in (select pk_custdoc from ehpta_aidcust where pk_contract = '"+pk_contract+"') ";
						break;
						
					default :
						
						break;
					}
					
						
					ccustomerRef.setWhereString(nextWherePart);
					
				} else if("cdeptid".equals(e.getItem().getKey())) {
					UIRefPane deptRef = (UIRefPane) e.getItem().getComponent();
					deptRef.setWhereString(" ( pk_corp = '"+getCorpPrimaryKey()+"' and pk_deptdoc = '"+deptRef.getRefPK()+"') ");
				} else if("cemployeeid".equals(e.getItem().getKey())) {
					UIRefPane employeeRef =  (UIRefPane) e.getItem().getComponent();
					employeeRef.setWhereString(" bd_psndoc.pk_corp='" + getCorpPrimaryKey() + "'  and bd_psndoc.indocflag='Y' and bd_psndoc.pk_psndoc = (select pk_psndoc from ehpta_sale_contract where pk_contract = '"+getBillCardPanel().getHeadItem("pk_contract").getValueObject()+"')");
				} else if("period".equals(e.getItem().getKey())) {
					switch(Integer.valueOf(comboBox.getSelectdItemValue().toString())) {
					case 10 :

						break;
						
					case 20 : 
						
						// 选择期间时自动按照表体行中的存货编码带出挂牌价、结算价、含税单价，计算出挂结价差
						
						
						break;
						
					default :
						
						break;
					}
				}
			}
		}
		return true;
	}

}
