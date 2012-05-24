package nc.ui.eh.trade.z00130;

import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.stock.z00130.DiscounttypeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 功能说明：折扣类型
 * @author 王兵
 * 2008年4月15日16:11:07
 */
public class ClientEventHandler extends ManageEventHandler {
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
    
    
    
    
	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("editcoperid", _getOperator());
	    getBillCardPanelWrapper().getBillCardPanel().setTailItem("editdate", _getDate());
		super.onBoSave();
	}
	
	  @Override
	protected void onBoQuery() throws Exception {
	       nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
	            SuperVO[] supervo;
	            supervo=business.queryByCondition(DiscounttypeVO.class, "  isnull(dr,0)=0");
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
	        
}
