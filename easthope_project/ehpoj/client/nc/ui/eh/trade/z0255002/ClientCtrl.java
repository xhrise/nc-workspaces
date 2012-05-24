package nc.ui.eh.trade.z0255002;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.eh.trade.z0255001.IcoutVO;

/** 
 * ˵�������ⵥ 
 * �������ͣ�ZA11
 * @author ���� 
 * ʱ�䣺2008-4-8 19:43:27
 */
public class ClientCtrl extends AbstractCtrl {

	
	@Override
	public String getBillType() {
        // TODO �Զ����ɷ������
        return IBillType.eh_z0255001;
    }

	 @Override
	public String[] getBillVoName() {
	        // TODO �Զ����ɷ������
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
		
     /* ���� Javadoc��
      * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
      */
     @Override
	public int getBusinessActionType() {
         // TODO �Զ����ɷ������
         //return IBusinessActionType.PLATFORM;
         return IBusinessActionType.PLATFORM;
     }


}
