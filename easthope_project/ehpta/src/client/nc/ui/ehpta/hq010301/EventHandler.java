package nc.ui.ehpta.hq010301;

import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

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
		
	
}