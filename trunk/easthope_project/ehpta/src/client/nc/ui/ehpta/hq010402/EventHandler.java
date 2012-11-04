package nc.ui.ehpta.hq010402;

import java.awt.BorderLayout;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.filesystem.FileManageUI;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.arap.util.StringUtils;
import nc.vo.ehpta.hq010401.SaleContractHistoryVO;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.ehpta.hq010402.AidcustHistoryVO;
import nc.vo.ehpta.hq010402.AidcustVO;
import nc.vo.ehpta.hq010402.MultiBillVO;
import nc.vo.ehpta.hq010402.PrepolicyHistoryVO;
import nc.vo.ehpta.hq010402.PrepolicyVO;
import nc.vo.ehpta.hq010402.SaleContractBHistoryVO;
import nc.vo.ehpta.hq010402.SaleContractBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 
 * 该类是一个抽象类，主要目的是生成按钮事件处理的框架
 * 
 * @author author
 * @version tempProject version
 */

public class EventHandler extends ManageEventHandler {
	
	private SaleContractVO headvo = null; // 合同vo
	
	private String preContractOK = null; // 原合同主键
	
	private StringBuffer strWhere = null;
	
	public EventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	public final String[] getTableCodes() {
		return new String[]{"ehpta_sale_contract_b", "ehpta_aidcust", "ehpta_prepolicy"};
	}
	
	public final String getTableNames( String tableCode) {
		if(getTableCodes()[0].equals(tableCode))
			return  "长单合同基本信息";
		else if(getTableCodes()[1].equals(tableCode))
			return "辅助客户";
		else if(getTableCodes()[2].equals(tableCode))
			return "优惠政策";
		
		return null;
	}

	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
			case DefaultBillButton.DOCUMENT :
	
				documentManage();
				break;
	
			case DefaultBillButton.MAKENEWCONTRACT : 
				
				makeNewContract(); // 生成新合同
				break;
				
			case DefaultBillButton.OpenOrClose :
				
				onOpenOrClose();
				break;
				
			default:
	
				break;
		}
	}
	
	/**
	 * 合同打开/关闭操作
	 * 
	 * @throws Exception
	 */
	private final void onOpenOrClose() throws Exception {
		
		int currRow = getBufferData().getCurrentRow();
		
		AggregatedValueObject  billVO = getBufferData().getCurrentVO();
		if(billVO != null ) {
			
			if(billVO.getParentVO().getPrimaryKey() != null && !"".equals(billVO.getParentVO().getPrimaryKey())) {
				
				UFBoolean openOrCloseFlag = (UFBoolean) billVO.getParentVO().getAttributeValue("close_flag");
				if(openOrCloseFlag == null || "N".equals(openOrCloseFlag.toString()) || !openOrCloseFlag.booleanValue()) {
					
					billVO.getParentVO().setAttributeValue("close_flag", new UFBoolean(true));
					HYPubBO_Client.update((SuperVO) billVO.getParentVO());
					
				} else {
					
					billVO.getParentVO().setAttributeValue("close_flag", new UFBoolean(false));
					HYPubBO_Client.update((SuperVO) billVO.getParentVO());
					
				}
					
				billVO = HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), billVO.getParentVO().getPrimaryKey());
				getBufferData().setCurrentVO(billVO);
				updateBuffer();
				getBufferData().setCurrentRow(currRow);
				
			}
			
		}
	}
	
	/**
	 * 功能: 生成新合同:
	 * <hr>
	 * a.版本号自动加1 b.所有数据为原合同数据 c.状态为自由态
	 * 
	 * @return boolean
	 */
	private boolean makeNewContract() {
		
		try {
			
			//判断审批完成的，没有更高版本的才能继续
			CircularlyAccessibleValueObject parent=getBufferData().getCurrentVO().getParentVO();
			if (Integer.parseInt(parent.getAttributeValue("vbillstatus").toString())!=1){
				getBillUI().showWarningMessage("没有审批，不能变更！");
				return true;
			}
			
			SaleContractVO vo=(SaleContractVO) HYPubBO_Client.queryByCondition(SaleContractVO.class, String.format("vbillno= '%1$s' and vbillstatus in(1,8) order by version desc",parent.getAttributeValue("vbillno").toString()))[0];
			if (Double.parseDouble(parent.getAttributeValue("version").toString())< Double.parseDouble(String.valueOf(vo.getVersion()))) {
				getBillUI().showWarningMessage("已有更高版本，不能变更！");
				return true;
			}
			
			AggregatedValueObject aggvo = getBillUI().getVOFromUI();
			headvo = (SaleContractVO) aggvo.getParentVO();
			preContractOK = headvo.getPrimaryKey();
			// 如果没有选择项目提示错误
			if (StringUtils.isEmpty(headvo.getVbillno())) {
				getBillUI().showErrorMessage("请选择一个项目！");
				return false;
			}
			int result = getBillUI().showYesNoMessage("是否确认此操作?");
			if (result == UIDialog.ID_YES) {
				int vbillstatus = headvo.getVbillstatus();
				
				boolean flag = headvo.getClose_flag() == null ? false : headvo.getClose_flag().booleanValue();
				if (vbillstatus != IBillStatus.CHECKPASS && flag == true) {
					getBillUI().showErrorMessage("此单据没有审批通过或已经关闭,不允许版本变更!");
					return false;
				} else {
					
					getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().setSelectedIndex(0);
					
					onBoCopy();
					
					String billno = headvo.getVbillno();
					
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem(SaleContractVO.VBILLNO).setValue(billno);
					
					// 将版本号进行改变在原基础上加1
					Integer ver = headvo.getVersion() == null ? new Integer(0) : new UFDouble(headvo.getVersion()).intValue();
					ver = ver + 1;
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem(SaleContractVO.VERSION).setValue(ver);
					getBillCardPanelWrapper().getBillCardPanel().getTailItem(SaleContractVO.VOPERATORID).setValue(_getOperator());
					getBillCardPanelWrapper().getBillCardPanel().getTailItem(SaleContractVO.DMAKEDATE).setValue(_getDate());
					getBillCardPanelWrapper().getBillCardPanel().getTailItem(SaleContractVO.VAPPROVEID).setValue(null);
					getBillCardPanelWrapper().getBillCardPanel().getTailItem(SaleContractVO.DAPPROVEDATE).setValue(null);
					getBillCardPanelWrapper().getBillCardPanel().getTailItem(SaleContractVO.VAPPROVENOTE).setValue(null);
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem(SaleContractVO.VBILLSTATUS).setValue(IBillStatus.FREE);
					getBillCardPanelWrapper().getBillCardPanel().getHeadItem(SaleContractVO.STOPCONTRACT).setValue(new UFBoolean(true));
					getBillUI().updateUI();
				}
			}
			
		} catch (Exception e) {
			Logger.error(e);
			AppDebug.debug(e);
		}
		
		return true;
	}
	
	/**
	 * 功能: 作废源合同
	 * 
	 * @return boolean
	 */
	private boolean closeOriContract() throws Exception{
		
		if (isAdding() && getBufferData().getCurrentVO() != null) {
			if(getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getAttributeValue("vbillno").equals(getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillno"))) {
			
				MultiBillVO billVO = ((MultiBillVO)getBufferData().getCurrentVO());
				Integer version = 0;
				String pk_contract = "";
				SaleContractHistoryVO contractHisVO = new SaleContractHistoryVO();
				for(String attr : billVO.getParentVO().getAttributeNames()) {
					contractHisVO.setAttributeValue(attr, billVO.getParentVO().getAttributeValue(attr));
					
					if("version".equals(attr))
						version = Integer.valueOf(billVO.getParentVO().getAttributeValue(attr).toString());
				
					if("pk_contract".equals(attr))
						pk_contract = billVO.getParentVO().getAttributeValue(attr).toString();
				}
				
				SaleContractBHistoryVO[] contractBHisVO = new SaleContractBHistoryVO[0];
				SaleContractBVO[] contbVOs = (SaleContractBVO[])billVO.getTableVO(getTableCodes()[0]);
				if(contbVOs != null && contbVOs.length > 0) {
					contractBHisVO = new SaleContractBHistoryVO[contbVOs.length];
					for(int i = 0 , j = contbVOs.length ; i < j ; i++) {
						contractBHisVO[i] = new SaleContractBHistoryVO();
						for(String attr : contbVOs[i].getAttributeNames()) {
							contractBHisVO[i].setAttributeValue(attr, contbVOs[i].getAttributeValue(attr));
						}
						contractBHisVO[i].setAttributeValue("version_his", version);
					}
				}
				
				
				AidcustHistoryVO[] aidcustHisVO = new AidcustHistoryVO[0];
				CircularlyAccessibleValueObject[] aidcustVOs = billVO.getTableVO(getTableCodes()[1]);
				if(aidcustVOs != null && aidcustVOs.length > 0) {
					aidcustHisVO = new AidcustHistoryVO[aidcustVOs.length];
					for(int i = 0 , j = aidcustVOs.length ; i < j ; i++) {
						aidcustHisVO[i] = new AidcustHistoryVO();
						for(String attr : aidcustVOs[i].getAttributeNames()) {
							aidcustHisVO[i].setAttributeValue(attr, aidcustVOs[i].getAttributeValue(attr));
						}
						aidcustHisVO[i].setAttributeValue("version_his", version);
					}
				}
				
				
				PrepolicyHistoryVO[] policyHisVO = new PrepolicyHistoryVO[0];
				CircularlyAccessibleValueObject[] policyVOs = billVO.getTableVO(getTableCodes()[2]);
				if(policyVOs != null && policyVOs.length > 0) {
					policyHisVO = new PrepolicyHistoryVO[policyVOs.length];
					for(int i = 0 , j = policyVOs.length ; i < j ; i++) {
						policyHisVO[i] = new PrepolicyHistoryVO();
						for(String attr : policyVOs[i].getAttributeNames()) {
							policyHisVO[i].setAttributeValue(attr, policyVOs[i].getAttributeValue(attr));
						}
						policyHisVO[i].setAttributeValue("version_his", version);
					}
				}
				
				Integer contHisCount = (Integer) UAPQueryBS.getInstance().executeQuery("select count(1) from ehpta_sale_contract_history where pk_contract = '"+pk_contract+"' and version = " + version , new ColumnProcessor());
				if(contHisCount > 0) {
					
					try { UAPQueryBS.getInstance().executeQuery("delete ehpta_sale_contract_b_history where pk_contract = '"+pk_contract+"' and version_his = " + version ,  null); } catch(Exception e) { }
					try { UAPQueryBS.getInstance().executeQuery("delete ehpta_aidcust_history where pk_contract = '"+pk_contract+"' and version_his = " + version ,  null); } catch(Exception e) { }
					try { UAPQueryBS.getInstance().executeQuery("delete ehpta_prepolicy_history where pk_contract = '"+pk_contract+"' and version_his = " + version ,  null); } catch(Exception e) { }
					try { UAPQueryBS.getInstance().executeQuery("delete ehpta_sale_contract_history where pk_contract = '"+pk_contract+"' and version = " + version ,  null); } catch(Exception e) { }
					
				}
				
				
				HYPubBO_Client.insertAry(contractBHisVO);
				HYPubBO_Client.insertAry(aidcustHisVO);
				HYPubBO_Client.insertAry(policyHisVO);
				HYPubBO_Client.insert(contractHisVO);
				
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_sale_contract_b where pk_contract = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_aidcust where pk_contract = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_prepolicy where pk_contract = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_sale_contract where pk_contract = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
				
			} 
		} 
		
		return true;
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		saveMultiValidataIsNull(getBillCardPanelWrapper().getBillCardPanel(), (MultiBillVO) getBillCardPanelWrapper().getBillVOFromUI(), getTableCodes() );
		
		if(closeOriContract()) {
			super.onBoSave();
		}
		
		afterOnBoSave();
		
	}
	
	protected final void afterOnBoSave() throws Exception { 
		if(preContractOK != null && !"".equals(preContractOK)) {
			MultiBillVO currVO = (MultiBillVO)getBufferData().getCurrentVO();
			
			for(String tableCode : getTableCodes()) {
				CircularlyAccessibleValueObject[] tableVO = currVO.getTableVO(tableCode);
				if(tableVO != null && tableVO.length > 0) {
					for(CircularlyAccessibleValueObject bvo : tableVO) {
						bvo.setAttributeValue(SaleContractVO.DEF2, preContractOK);
					}
					
					// 更新长单合同表体各表的DEF2为起始单据的主键。
					HYPubBO_Client.updateAry((SuperVO[]) tableVO);
				}
			}
			
			try { UAPQueryBS.getInstance().executeQuery("update ehpta_sale_contract set def2 = '"+preContractOK+"' where pk_contract = '"+currVO.getParentVO().getPrimaryKey()+"'", null); } catch(Exception e) { } 
			
			
			preContractOK = null;
		}
		
		if(((MultiBillVO)getBufferData().getCurrentVO()).getTableVO(getTableCodes()[1]) != null && ((MultiBillVO)getBufferData().getCurrentVO()).getTableVO(getTableCodes()[1]).length > 0)
			HYPubBO_Client.updateAry((SuperVO[]) ((MultiBillVO)getBufferData().getCurrentVO()).getTableVO(getTableCodes()[1]));
		
		
		StringBuilder builder = new StringBuilder();
		if(getBufferData().getAllHeadVOsFromBuffer() != null && getBufferData().getAllHeadVOsFromBuffer().length > 0) {
			int i = 0;
			for(CircularlyAccessibleValueObject cavo : getBufferData().getAllHeadVOsFromBuffer()) {
				if(i == getBufferData().getAllHeadVOsFromBuffer().length - 1)
					builder.append("'" + cavo.getAttributeValue("pk_contract") + "'");
				else
					builder.append("'" + cavo.getAttributeValue("pk_contract") + "',");
				
				i ++;
			}
		}
		
		if(builder.toString() != null && !"".equals(builder.toString())) {
			
			AggregatedValueObject currAggVO = getBufferData().getCurrentVO();
			
			SuperVO[] queryVos = HYPubBO_Client.queryByCondition(SaleContractVO.class, " pk_contract in ("+builder.toString()+") and nvl(dr , 0) = 0 and pk_corp = '"+_getCorp().getPk_corp()+"' and contype = '长单合同' ");
	
			getBufferData().clear();
			// 增加数据到Buffer
			addDataToBuffer(queryVos);
	
			updateBuffer();
			
			int currRow = 0;
			for(CircularlyAccessibleValueObject cavo : getBufferData().getAllHeadVOsFromBuffer()) {
				if(cavo.getAttributeValue(SaleContractVO.VBILLNO).equals(currAggVO.getParentVO().getAttributeValue(SaleContractVO.VBILLNO))) {
					getBufferData().setCurrentRow(currRow);
					break;
				}
				
				currRow ++;
					
			}
			
			
		}
	}
	
	protected void onBoQuery() throws Exception {

		strWhere = new StringBuffer();

		if (askForQueryCondition(strWhere) == false)
			return;// 用户放弃了查询
		
		SuperVO[] queryVos = queryHeadVOs(strWhere.toString());

		getBufferData().clear();
		// 增加数据到Buffer
		addDataToBuffer(queryVos);

		updateBuffer();

	}
	
	@Override
	protected void onBoRefresh() throws Exception {
		
		super.onBoRefresh();
	}
	
	@Override
	protected void onBoCancel() throws Exception {
		
		super.onBoCancel();
		
		if(preContractOK != null)
			preContractOK = null;
		
	}
	
	@Override
	protected void onBoDelete() throws Exception {
		
		if (getBufferData().getCurrentVO() == null)
			return;
		
		if(getBillUI().showOkCancelMessage("是否确认要删除?") == UIDialog.ID_OK) {
			MultiBillVO modelVo = (MultiBillVO)getBufferData().getCurrentVO();
			String pk_contract = String.valueOf(modelVo.getParentVO().getAttributeValue(SaleContractVO.DEF2) == null ? modelVo.getParentVO().getAttributeValue(SaleContractVO.PK_CONTRACT) : modelVo.getParentVO().getAttributeValue(SaleContractVO.DEF2));
			Integer version = (Integer) modelVo.getParentVO().getAttributeValue(SaleContractVO.VERSION);
		
			if(version == 1) {
				getBusinessAction().delete(modelVo, getUIController().getBillType(),
						getBillUI()._getDate().toString(), getBillUI().getUserObject());
				if (PfUtilClient.isSuccess()) {
					// 清除界面数据
					getBillUI().removeListHeadData(getBufferData().getCurrentRow());
					if (getUIController() instanceof ISingleController) {
						ISingleController sctl = (ISingleController) getUIController();
						if (!sctl.isSingleDetail())
							getBufferData().removeCurrentRow();
					} else {
						getBufferData().removeCurrentRow();
					}

				}
				if (getBufferData().getVOBufferSize() == 0)
					getBillUI().setBillOperate(IBillOperate.OP_INIT);
				else
					getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
				getBufferData().setCurrentRow(getBufferData().getCurrentRow());
			} else {
				
				SaleContractHistoryVO[] contractHisVO = ((SaleContractHistoryVO[]) HYPubBO_Client.queryByCondition(SaleContractHistoryVO.class, " pk_contract = '"+pk_contract+"' and version = " + (version - 1)));
				SaleContractVO[] contractVO = new SaleContractVO[0];
				if(contractHisVO != null && contractHisVO.length > 0) {
					contractVO = new SaleContractVO[contractHisVO.length];
					int i = 0;
					for(SaleContractHistoryVO contVO : contractHisVO) {
						contractVO[i] = new SaleContractVO();
						for(String attr : contractVO[i].getAttributeNames()) {
							contractVO[i].setAttributeValue(attr, contVO.getAttributeValue(attr));
						}
						
						i++;
					}
				}
				
				SaleContractBHistoryVO[] contractBHisVO = (SaleContractBHistoryVO[]) HYPubBO_Client.queryByCondition(SaleContractBHistoryVO.class, " pk_contract = '"+pk_contract+"' and version_his = " + (version - 1));
				SaleContractBVO[] contractBVO = new SaleContractBVO[0];
				if(contractBHisVO != null && contractBHisVO.length > 0) {
					contractBVO = new SaleContractBVO[contractBHisVO.length];
					int i = 0;
					for(SaleContractBHistoryVO bhisVO : contractBHisVO) {
						contractBVO[i] = new SaleContractBVO();
						for(String attr : contractBVO[i].getAttributeNames()) {
							contractBVO[i].setAttributeValue(attr, bhisVO.getAttributeValue(attr));
						}
						
						i++;
					}
				}
				
				AidcustHistoryVO[] aidcustHisVO = (AidcustHistoryVO[]) HYPubBO_Client.queryByCondition(AidcustHistoryVO.class, " pk_contract = '"+pk_contract+"' and version_his = " + (version - 1));
				AidcustVO[] aidcustVO = new AidcustVO[0];
				if(aidcustHisVO != null && aidcustHisVO.length > 0) {
					aidcustVO = new AidcustVO[aidcustHisVO.length];
					int i = 0;
					for(AidcustHistoryVO aidhisVO : aidcustHisVO) {
						aidcustVO[i] = new AidcustVO();
						for(String attr : aidcustVO[i].getAttributeNames()) {
							aidcustVO[i].setAttributeValue(attr, aidhisVO.getAttributeValue(attr));
						}
						
						i++;
					}
				}
				
				PrepolicyHistoryVO[] policyHisVO = (PrepolicyHistoryVO[]) HYPubBO_Client.queryByCondition(PrepolicyHistoryVO.class, " pk_contract = '"+pk_contract+"' and version_his = " + (version - 1));
				PrepolicyVO[] policyVO = new PrepolicyVO[0];
				if(policyHisVO != null && policyHisVO.length > 0) {
					policyVO = new PrepolicyVO[policyHisVO.length];
					int i = 0;
					for(PrepolicyHistoryVO policyhisVO : policyHisVO) {
						policyVO[i] = new PrepolicyVO();
						for(String attr : policyVO[i].getAttributeNames()) {
							policyVO[i].setAttributeValue(attr, policyhisVO.getAttributeValue(attr));
						}
						
						i++;
					}
				}
				
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_sale_contract_b where def2 = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_aidcust where def2 = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_prepolicy where def2 = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
				try { UAPQueryBS.getInstance().executeQuery("delete ehpta_sale_contract where def2 = '"+pk_contract+"' " ,  null); } catch(Exception e) { }
			
				try {HYPubBO_Client.insertAry(contractBVO); } catch(Exception e) { AppDebug.debug(e); }
				try {HYPubBO_Client.insertAry(aidcustVO); } catch(Exception e) { AppDebug.debug(e); }
				try {HYPubBO_Client.insertAry(policyVO); } catch(Exception e) { AppDebug.debug(e); }
				try {HYPubBO_Client.insertAry(contractVO); } catch(Exception e) { AppDebug.debug(e); }
				
				
				StringBuilder builder = new StringBuilder();
				if(getBufferData().getAllHeadVOsFromBuffer() != null && getBufferData().getAllHeadVOsFromBuffer().length > 0) {
					int i = 0;
					for(CircularlyAccessibleValueObject cavo : getBufferData().getAllHeadVOsFromBuffer()) {
						if(i == getBufferData().getAllHeadVOsFromBuffer().length - 1)
							builder.append("'" + cavo.getAttributeValue("pk_contract") + "'");
						else
							builder.append("'" + cavo.getAttributeValue("pk_contract") + "',");
						
						i ++;
					}
				}
				
				if(builder.toString() != null && !"".equals(builder.toString())) {
					
					builder.append(",'" + pk_contract + "'");
					
					AggregatedValueObject currAggVO = getBufferData().getCurrentVO();
					
					SuperVO[] queryVos = HYPubBO_Client.queryByCondition(SaleContractVO.class, " pk_contract in ("+builder.toString()+") and nvl(dr , 0) = 0 and pk_corp = '"+_getCorp().getPk_corp()+"' and contype = '长单合同' ");
			
					getBufferData().clear();
					// 增加数据到Buffer
					addDataToBuffer(queryVos);
			
					updateBuffer();
					
					int currRow = 0;
					for(CircularlyAccessibleValueObject cavo : getBufferData().getAllHeadVOsFromBuffer()) {
						if(cavo.getAttributeValue(SaleContractVO.VBILLNO).equals(currAggVO.getParentVO().getAttributeValue(SaleContractVO.VBILLNO))) {
							getBufferData().setCurrentRow(currRow);
							break;
						}
						
						currRow ++;
							
					}
					
					
				}
				
				
				getBufferData().updateView();
			}
		}
		
	}
	
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
	}
	
	@Override
	protected void onBoLineIns() throws Exception {
		checkSelectedOne();

		super.onBoLineIns();
	}
	
	@Override
	protected void onBoLinePaste() throws Exception {
		checkSelectedOne();
		
		super.onBoLinePaste();
	}
	
	@Override
	protected void onBoLineDel() throws Exception {
		
		String selectedTableCode = getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().getSelectedTableCode();
		if(getTableCodes()[1].equals(selectedTableCode)) {
			if(getBillCardPanelWrapper().getBillCardPanel().getBillTable(selectedTableCode).getSelectedRow() == 0 )
				throw new Exception ("不能删除辅助客户的第一条记录！");
				
		} 
		
		super.onBoLineDel();
		
	}
	
	@Override
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		
		afterOnBoAudit();
		
	}
	
	protected void afterOnBoAudit() throws Exception {
		
		AggregatedValueObject billVO = getBufferData().getCurrentVOClone();
		Integer vbillstatus = (Integer) billVO.getParentVO().getAttributeValue("vbillstatus");
		UFBoolean stopcontract = (UFBoolean)billVO.getParentVO().getAttributeValue("stopcontract");
		if(vbillstatus == IBillStatus.CHECKPASS && stopcontract.booleanValue()) {
			
			MultiBillVO currVO = (MultiBillVO)getBufferData().getCurrentVO();
			
			for(String tableCode : getTableCodes()) {
				CircularlyAccessibleValueObject[] tableVO = currVO.getTableVO(tableCode);
				if(tableVO != null && tableVO.length > 0) {
					for(CircularlyAccessibleValueObject bvo : tableVO) {
						bvo.setAttributeValue(SaleContractVO.PK_CONTRACT, bvo.getAttributeValue(SaleContractVO.DEF2));
					}
					
					// 更新长单合同表体各表的DEF2为起始单据的主键。
					HYPubBO_Client.updateAry((SuperVO[]) tableVO);
				}
			}
			
			try { UAPQueryBS.getInstance().executeQuery("update ehpta_sale_contract set PK_CONTRACT = '"+currVO.getParentVO().getAttributeValue(SaleContractVO.DEF2)+"' where pk_contract = '"+currVO.getParentVO().getPrimaryKey()+"'", null); } catch(Exception e) { } 
			
			MultiBillVO newBillVO = (MultiBillVO) HYPubBO_Client.queryBillVOByPrimaryKey(getUIController().getBillVoName(), (String) currVO.getParentVO().getAttributeValue(SaleContractVO.DEF2));
			
			getBufferData().setCurrentVO(newBillVO);
			
		}
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		Integer version = (Integer) getBufferData().getCurrentVOClone().getParentVO().getAttributeValue(SaleContractVO.VERSION);
		
		if(version > 1) {
			
			int checkNum = getBillUI().showYesNoMessage("当前合同已经生成变更版本，是否继续执行弃审操作？");
			if(!(checkNum == UIDialog.ID_YES))
				throw new Exception("本次操作被取消！");
		} else 
			Validata.cancleAuditValid(getBufferData().getCurrentVO());
		
		super.onBoCancelAudit();
	}
	
	private final void checkSelectedOne() throws Exception {
		if(getTableCodes()[1].equals(getBillCardPanelWrapper().getBillCardPanel().getBodyTabbedPane().getSelectedTableCode())) 
			if(getBillCardPanelWrapper().getBillCardPanel().getBillTable(getTableCodes()[1]).getSelectedRow() == 0) 
				throw new Exception ("不能在辅助客户中插入或复制到首行...");
	}

	private final void documentManage() throws BusinessException {
		try {

			AggregatedValueObject aggVO = null;

			if (getBillUI().getBillOperate() == IBillOperate.OP_ADD)
				aggVO = getBillCardPanelWrapper().getBillVOFromUI();
			else
				aggVO = getBufferData().getCurrentVO();

			if (aggVO == null || aggVO.getParentVO() == null) {
				getBillUI().showErrorMessage("请至少选择一条记录...");
				return;
			}

			String ids = aggVO.getParentVO().getPrimaryKey();

			if (ids == null || "".equals(ids)) {
				getBillUI().showErrorMessage("新增时不能进行文件上传操作...");
				return;
			}

			FileManageUI fileui = new FileManageUI();
			fileui.setRootDirStr(ids);
			fileui.setTreeRootVisible(false);
			UIDialog dlg = new UIDialog(getBillUI());
			dlg.getContentPane().setLayout(new BorderLayout());
			dlg.getContentPane().add(fileui, "Center");
			dlg.setResizable(true);
			dlg.setSize(600, 400);
			dlg.setTitle(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"tm502comm", "UPPTM502Comm-000050")/* @res"文档管理" */);
			if (dlg.showModal() == UIDialog.ID_CANCEL)
				return;
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}
	
	public final void saveMultiValidataIsNull(BillCardPanel billCardPanel , MultiBillVO currAggVO , String[] bodyTableCodes) throws Exception {
		
		BillItem[] headItems = billCardPanel.getHeadItems();
		
		for(BillItem head : headItems) {
			if(head.isNull()) {
				
				for(String attr : currAggVO.getParentVO().getAttributeNames()) {
					if(attr.equals(head.getKey())) {
						Object obj = currAggVO.getParentVO().getAttributeValue(head.getKey());
						
						if(obj == null || "".equals(obj)) 
							throw new Exception ("表头 ：" + head.getName() + " , 不能为空！");
						
						break;
					} else {
						if(head.getValueObject() == null || "".equals(head.getValueObject()))
							throw new Exception ("表头 ：" + head.getName() + " , 不能为空！");
						
						break;
					}
					
				}
				
			}
			
		}
		
		for(String tableCode : bodyTableCodes) {
			for(BillItem body : billCardPanel.getBillData().getBodyItemsForTable(tableCode)) {
				
				if(body.isNull()) {
					SuperVO[] childVOs = (SuperVO[]) currAggVO.getTableVO(tableCode);
					if(childVOs != null && childVOs.length > 0) {
						int row = 0;
						for(SuperVO child : childVOs) {
							if(child.getTableName().equals(tableCode)) {
								for(String attr : child.getAttributeNames()) {
									if(attr.equals(body.getKey())) {
										Object obj = child.getAttributeValue(body.getKey());
										if(obj == null || "".equals(obj))
											throw new Exception ("表体[ "+getTableNames(tableCode)+" ] 行"+(row + 1)+"  ：" + body.getName() + " , 不能为空！");
									
										break;
									} 
									
								}
							
							}
							
							row ++;
						}
						
					}
					
				}
				
			}
		
		}
		
	}
	
	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		
		afterOnButton();
	}
	
	@Override
	public void onButton(ButtonObject bo) {

		super.onButton(bo);
		
		afterOnButton();
		
	}
	
	/**
	 * 点击按钮后的处理
	 */
	private final void afterOnButton() {
		
		if(getBillUI().getBillOperate() == IBillOperate.OP_ADD || getBillUI().getBillOperate() == IBillOperate.OP_EDIT || getBillUI().getBillOperate() == IBillOperate.OP_REFADD) {
			getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
			getButtonManager().getButton(DefaultBillButton.DOCUMENT).setEnabled(false);
			
		} else {
			
			AggregatedValueObject  vo = getBufferData().getCurrentVO();
			
			try {
				if(vo != null && vo.getParentVO() != null && vo.getParentVO().getPrimaryKey() != null) {
					if((Integer)vo.getParentVO().getAttributeValue("vbillstatus") == IBillStatus.CHECKPASS)
						getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(true);
					else 
						getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
				} else 
					getButtonManager().getButton(DefaultBillButton.OpenOrClose).setEnabled(false);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e, this.getClass(), "afterOnButton");
			}
			
			getButtonManager().getButton(DefaultBillButton.DOCUMENT).setEnabled(true);
			
		}
		
		getBillUI().updateButtons();
		
	}

}