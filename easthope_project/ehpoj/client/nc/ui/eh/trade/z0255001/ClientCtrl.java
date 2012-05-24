package nc.ui.eh.trade.z0255001;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
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
	public int[] getCardButtonAry() {        
//		 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
//	                new int[] { IBillButton.Busitype},
//	                ISHSHConst.CARD_BUTTONS_M, 0);
//		 return btns;
//	        int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
//	                new int[] { IBillButton.Busitype},
//	                ISHSHConst.CARD_BUTTONS_M, 0);
//            btns=nc.ui.eh.button.ButtonTool.deleteButton(3,new int[]{});
//	        return btns;
		 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS_M, 0);
	    	int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.BusinesBtn},0);
	    	
	    	return btns;//modify by houcq 2010-11-29ȡ�����ⵥ��ֹ���ݰ�ť
	    }

     @Override
	public int[] getListButtonAry() {
	       return nc.ui.eh.button.ButtonTool.insertButtons(
	               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
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
