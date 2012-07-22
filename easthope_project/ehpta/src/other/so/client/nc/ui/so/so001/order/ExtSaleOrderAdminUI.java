package nc.ui.so.so001.order;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIComboBox;
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
public class ExtSaleOrderAdminUI extends SaleBillUI {

	//按钮初始化标记
	private boolean b_init;
	
	private final IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);

	public ExtSaleOrderAdminUI() {
		super();
	}

	public ExtSaleOrderAdminUI(String pk_corp, String billtype, String busitype, String operator, String id) {
		super(pk_corp, billtype, busitype, operator, id);
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
	
	@Override
	protected void onNewByOther(AggregatedValueObject[] saleVOs)
			throws Exception {
		
		if(saleVOs != null && saleVOs.length > 0) {
			Object pk_contract = saleVOs[0].getParentVO().getAttributeValue("pk_contract");
			if(pk_contract != null) {
				Integer count = (Integer)iUAPQueryBS.executeQuery("select count(1) from so_sale where pk_contract = '"+pk_contract+"' and nvl(dr,0)=0 ", new ColumnProcessor());
			
				if(count > 0 )
					throw new Exception("该合同已经录入销售订单！");
			}
		}
		
		super.onNewByOther(saleVOs);
	}
	
	@Override
	protected void onCard() {
		super.onCard();
		
		
		// 双击列表界面数据时在PLUGIN中无法调用mouse_doubleclick方法，但会调用onCard方法，在此加上设置界面合同类型的代码块
		// add by river for 2012-07-20
		// start ..
		
		String csaleid = (String) getBillListPanel().getHeadBillModel().getValueAt(getBillListPanel().getHeadTable().getSelectedRow(), "csaleid");
		SaleOrderVO saleorder = vocache.getSaleOrderVO(csaleid);
		if(getBillCardPanel().getHeadItem("contracttype") != null) {
			if(saleorder != null && saleorder.getParentVO() != null) {
				if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 10)
					((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("现货合同");
				else if(Integer.valueOf(saleorder.getParentVO().getAttributeValue("contracttype") == null ? "0" : saleorder.getParentVO().getAttributeValue("contracttype").toString()) == 20)
					((UIComboBox) getBillCardPanel().getHeadItem("contracttype").getComponent()).setSelectedItem("长单合同");
			}
		}
		
		// .. end
	}
}
