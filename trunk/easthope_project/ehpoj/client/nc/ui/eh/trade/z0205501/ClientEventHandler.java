package nc.ui.eh.trade.z0205501;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.trade.z0205501.DiscountrateVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * ����˵�����ۿ۱���
 * @author ����
 * 2008-6-5 11:52:16
 */
public class ClientEventHandler extends CardEventHandler {
	
	
	public ClientEventHandler(BillCardUI arg0, ICardController arg1) {
		super(arg0, arg1);
		try {
			setBoEnabled();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("editcoperid", _getOperator());
	    getBillCardPanelWrapper().getBillCardPanel().setTailItem("editdate", _getDate());
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		super.onBoSave();
		setBoEnabled();
	}
	
    @Override
	protected void onBoQuery() throws Exception {
	       nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
	            SuperVO[] supervo;
	            supervo=business.queryByCondition(DiscountrateVO.class, " pk_corp='"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0");
	            getBufferData().clear();
	            
	            if (supervo != null && supervo.length != 0)
	            {
	                int len=supervo.length;
	                for(int i=0;i<len;i++){
	                    AggregatedValueObject aVo =
	                        (AggregatedValueObject) Class
	                            .forName(getUIController().getBillVoName()[0])
	                            .newInstance();
	                    aVo.setParentVO(supervo[i]);
	                    aVo.setChildrenVO(null);
	                    getBufferData().addVOToBuffer(aVo);
	                }
	                getBillUI().setListHeadData(supervo);
	                getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
	                getBufferData().setCurrentRow(0);       
	            }
	            else
	            {
	                getBillUI().setListHeadData(null);
	                getBillUI().setBillOperate(IBillOperate.OP_INIT);
	                getBufferData().setCurrentRow(-1);
	            }
	             
	            
	        }
	  @Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
		if(pk!=null&&pk.length()>0){
			getBillUI().showErrorMessage("�ۿ۱����Ѿ�����,��������,���޸�!");
			onBoEdit();
			getBillUI().setBillOperate(IBillOperate.OP_EDIT);
			return;
		}
		super.onBoAdd(arg0);
	}
	  
	 @Override
	protected void onBoCancel() throws Exception {
		 super.onBoCancel();
		 ((ClientUI)getBillUI()).initValue();
		 setBoEnabled();
	}
	 
	@Override
	protected void onBoEdit() throws Exception {
		// TODO Auto-generated method stub
		super.onBoEdit();
		getBillUI().setBillOperate(IBillOperate.OP_EDIT);
	}
	
	 protected void setBoEnabled() throws Exception {
	        String pk = getBillUI().getChangedVOFromUI().getParentVO().getPrimaryKey();
	        if (pk ==null||pk.equals("")){
	        	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
	        }
	        else{
	        	getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
	        	getButtonManager().getButton(IBillButton.Add).setEnabled(false);
	        }
	        getBillUI().updateButtonUI();
	    }
}
