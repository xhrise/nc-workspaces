package nc.ui.eh.voucher.h10120;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.eh.trade.z0255001.IcoutVO;

/**
 * 说明：供应链单据生成凭证
 * 单据类型：ZB38
 * @author 王兵 
 * 时间：2009-11-5 11:02:34
 */
public class ClientCtrl implements  ICardController{

	
	public String getBillType() {
        // TODO 自动生成方法存根
        return IBillType.eh_h10120;
    }

	public String[] getBillVoName() {
	        // TODO 自动生成方法存根
	        return new String[]{
	            PubBillVO.class.getName(),
	            IcoutVO.class.getName(),
	            IcoutBVO.class.getName()    
	        };
	    }

	
	public String getChildPkField() {
		// TODO Auto-generated method stub
		return "pk_icout_b";
	}

	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_icout";
	}

	public int[] getCardButtonAry() {       
		 int[] btns= new int[]{
				 IBillButton.Busitype,
				 IBillButton.Add
		 };
	    	return btns;
	    }

	public int[] getListButtonAry() {
    	 int[] btns= new int[]{
				 IBillButton.Busitype,
				 IBillButton.Add
		 };
	    	return btns;
	}

     /* （非 Javadoc）
      * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
      */
	public int getBusinessActionType() {
         // TODO 自动生成方法存根
         //return IBusinessActionType.PLATFORM;
         return IBusinessActionType.PLATFORM;
     }

	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return false;
	}


}
