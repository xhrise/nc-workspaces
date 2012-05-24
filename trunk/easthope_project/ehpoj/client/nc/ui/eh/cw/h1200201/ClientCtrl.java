
package nc.ui.eh.cw.h1200201;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0255001.IcoutBVO;

/**
 * 无金额入库管理
 * @author wb
 * 2008-8-21 10:41:21 
 */
public class ClientCtrl implements ICardController, ISingleController {

	public ClientCtrl() {
		super();
	}
	public String[] getCardBodyHideCol() {
		return null;
	}
	public int[] getCardButtonAry() {
		return new int[]{
				   IBillButton.Query,
				   IBillButton.Add,
	               IBillButton.Edit,
//	               IBillButton.Line,
				   IBillButton.Save,
				   IBillButton.Cancel,
				   IBillButton.Refresh
	              };
	}

	public String getBillType() {
		return IBillType.eh_h1200201;
	}
	public String[] getBillVoName() {
		return new String[]{
				PubBillVO.class.getName(),
				IcoutBVO.class.getName(),
				IcoutBVO.class.getName(),
		};
	}
	public String getBodyCondition() {
		return null;
	}
	public String getBodyZYXKey() {
		return null;
	}
	public int getBusinessActionType() {
		return IBusinessActionType.BD;
	}
	public String getChildPkField() {
		return null;
	}
	public String getHeadZYXKey() {
		return null;
	}
	public String getPkField() {
		return "pk_icout_b";
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
	public boolean isSingleDetail() {
		return true;
	}
	/**
     * 卡片是否显示行号 创建日期：(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowCardRowNo() {
        return true;
    }

    /**
     * 卡片是否显示合计行 创建日期：(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowCardTotal() {
        return true;
    }
	/**
     * 列表是否显示行号 创建日期：(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowListRowNo() {
        return true;
    }

    /**
     * 列表是否显示合计行 创建日期：(2003-1-5 15:29:05)
     * 
     * @return boolean
     */
    public boolean isShowListTotal() {
        return true;
    }
}
