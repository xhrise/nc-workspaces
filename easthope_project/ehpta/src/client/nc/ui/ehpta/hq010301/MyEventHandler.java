package nc.ui.ehpta.hq010301;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.fp.combase.pub01.IBillStatus;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class MyEventHandler extends AbstractMyEventHandler {

	IUAPQueryBS iUAPQuery = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
	public MyEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}


	@Override
	protected void onBoSave() throws Exception {
		Object obj =  iUAPQuery.executeQuery("select to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss') from dual", new ColumnProcessor());
		
		AggregatedValueObject billVO = getBillUI().getChangedVOFromUI();
		billVO.getParentVO().setAttributeValue("maintenancetime", obj.toString());
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
	public void onBoAudit() throws Exception {
		super.onBoAudit();
		
		
		if(((Integer)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillstatus")) == IBillStatus.CHECKPASS){
			((UICheckBox)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("approvalstatus").getComponent()).setSelected(true);
			try { iUAPQuery.executeQuery("update  ehpta_hangingprice set approvalstatus = 'Y' where pk_hangingprice = '"+getBufferData().getCurrentVO().getParentVO().getAttributeValue("pk_hangingprice")+"'", null); } catch(Exception e) {} 
		}else{
			((UICheckBox)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("approvalstatus").getComponent()).setSelected(false);
			
			try { iUAPQuery.executeQuery("update  ehpta_hangingprice set approvalstatus = 'N' where pk_hangingprice = '"+getBufferData().getCurrentVO().getParentVO().getAttributeValue("pk_hangingprice")+"'", null); } catch(Exception e) {} 
		}
		
	}
	
	@Override
	protected void onBoCancelAudit() throws Exception {
		
		super.onBoCancelAudit();
		
		if(((Integer)getBufferData().getCurrentVO().getParentVO().getAttributeValue("vbillstatus")) != IBillStatus.CHECKPASS){
			
			((UICheckBox)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("approvalstatus").getComponent()).setSelected(false);
			
			try { iUAPQuery.executeQuery("update  ehpta_hangingprice set approvalstatus = 'N' where pk_hangingprice = '"+getBufferData().getCurrentVO().getParentVO().getAttributeValue("pk_hangingprice")+"'", null); } catch(Exception e) {}
		}else{
			((UICheckBox)getBillCardPanelWrapper().getBillCardPanel().getHeadItem("approvalstatus").getComponent()).setSelected(true);
			
			try { iUAPQuery.executeQuery("update  ehpta_hangingprice set approvalstatus = 'Y' where pk_hangingprice = '"+getBufferData().getCurrentVO().getParentVO().getAttributeValue("pk_hangingprice")+"'", null); } catch(Exception e) {}
			
		}
		
	}
	
	@Override
	protected void onBoSelAll() throws Exception {
		SelectAll((BillManageUI)getBillUI(),true);
		
	}

	@Override
	protected void onBoSelNone() throws Exception {
		SelectAll((BillManageUI)getBillUI(), false);
	}
	
	private void SelectAll(BillManageUI billUI, boolean isNeedSelected) {
		int row = billUI.getBillListPanel().getHeadTable().getRowCount();
		BillModel headModel = billUI.getBillListPanel().getHeadBillModel();
		if(isNeedSelected){
			for(int n = 0; n < row; n++){
				if(headModel.getRowState(n) != BillModel.SELECTED){
					headModel.setRowState(n, BillModel.SELECTED);
				}
			}
		}else{
			for(int n = 0; n < row; n++){
				if(headModel.getRowState(n) != BillModel.UNSTATE){
					headModel.setRowState(n, BillModel.UNSTATE);
				}
			}
		}
		billUI.getBillListPanel().updateUI();
	}
	

	
	

}