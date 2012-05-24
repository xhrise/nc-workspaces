package nc.ui.eh.sc.h0450510;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.sc.h0450510.BdTeamBVO;
import nc.vo.eh.sc.h0450510.BdTeamVO;


/**
 * 功能说明：班组档案
 * @author 王明
 * 2008-05-07 下午04:03:18
 */
public class ClientCtrl extends AbstractManageController {

    public int[] getCardButtonAry() {        
    	return new int[] { 
        		IBillButton.Add, 
        		IBillButton.Edit, 
        		IBillButton.Delete,
                IBillButton.Line, 
                IBillButton.Save, 
                IBillButton.Cancel,
                IBillButton.Return,
                IBillButton.Refresh,
                IBillButton.Print
      
    	};
    }
    public int[] getListButtonAry() {
       return new int[] { 
    		   IBillButton.Query, 
    		   IBillButton.Add, 
               IBillButton.Card,
               IBillButton.Refresh 
     
       };
    }


    public String getBillType() {
        return IBillType.eh_z0450510;
    }

    public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            BdTeamVO.class.getName(),
            BdTeamBVO.class.getName()  
        };
    }

    public String getChildPkField() {
        return "pk_team_b";
    }

    public String getPkField() {
        return "pk_team";
    }

	public String[] getCardBodyHideCol() {
		return null;
	}

	public boolean isShowCardRowNo() {
		return false;
	}

	public boolean isShowCardTotal() {
		return false;
	}

	public String getBodyCondition() {
		return null;
	}

	public String getBodyZYXKey() {
		return null;
	}

	public String getHeadZYXKey() {
		return null;
	}

	public Boolean isEditInGoing() throws Exception {
		return null;
	}

	public boolean isExistBillStatus() {
		return false;
	}

	public boolean isLoadCardFormula() {
		return false;
	}

	public String[] getListBodyHideCol() {
		return null;
	}

	public String[] getListHeadHideCol() {
		return null;
	}

	public boolean isShowListRowNo() {
		return false;
	}

	public boolean isShowListTotal() {
		return false;
	}
	 public int getBusinessActionType() {
	        return nc.ui.trade.businessaction.IBusinessActionType.BD;
	    }

}
