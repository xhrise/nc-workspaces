package nc.ui.eh.trade.z0600302;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0600302.SberrorrangeVO;

/**
 * 功能说明：成品司磅误差范围设置
 * ZB06
 * @author 王兵
 * 2008-10-29 15:41:36
 */
public class ClientCtrl implements  ICardController{

  
    public int[] getCardButtonAry() {
        // TODO 自动生成方法存根
        return new int[]{
                IBillButton.Add,
                IBillButton.Edit,
                IBillButton.Delete,
                IBillButton.Save,
                IBillButton.Cancel,
                IBillButton.Refresh
            };
    }
    
    public int[] getListButtonAry() {
       return null;
    }


    public String getBillType() {
        return IBillType.eh_h0600302;
    }

    public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            SberrorrangeVO.class.getName(),
            SberrorrangeVO.class.getName()  
        };
    }

    public String getChildPkField() {
        return null;
    }

    public String getPkField() {
        return "pk_range";
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

	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return false;
	}

	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return nc.ui.trade.businessaction.IBusinessActionType.BD;
	}

	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}
}
