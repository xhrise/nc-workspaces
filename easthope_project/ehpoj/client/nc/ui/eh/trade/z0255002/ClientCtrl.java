package nc.ui.eh.trade.z0255002;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.eh.trade.z0255001.IcoutVO;

/** 
 * 说明：出库单 
 * 单据类型：ZA11
 * @author 王兵 
 * 时间：2008-4-8 19:43:27
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_z0255001;
    }

	 @Override
	public String[] getBillVoName() {
	        // TODO 自动生成方法存根
	        return new String[]{
	            PubBillVO.class.getName(),
	            IcoutVO.class.getName(),
	            IcoutBVO.class.getName()    
	        };
	    }

	
	@Override
	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "pk_icout_b";
	}

	@Override
	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_icout";
	}

	  @Override
	public int[] getListButtonAry() {
	        return nc.ui.eh.pub.PubTools.getSPLButton();
	    }

		@Override
		public int[] getCardButtonAry() {
			return nc.ui.eh.pub.PubTools.getSPCButton();
		}
		
     /* （非 Javadoc）
      * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
      */
     @Override
	public int getBusinessActionType() {
         // TODO 自动生成方法存根
         //return IBusinessActionType.PLATFORM;
         return IBusinessActionType.PLATFORM;
     }


}
