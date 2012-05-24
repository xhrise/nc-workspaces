package nc.ui.eh.trade.z00130;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.stock.z00130.DiscounttypeBVO;
import nc.vo.eh.stock.z00130.DiscounttypeVO;

/**
 * 功能说明：折扣类型
 * @author 王兵
 * 2008年4月15日16:11:07
 */
public class ClientCtrl extends AbstractCtrl {

  
    @Override
	public int[] getCardButtonAry() {
        // TODO 自动生成方法存根
        return new int[]{
                IBillButton.Query,
                IBillButton.Add,
                IBillButton.Edit,
                IBillButton.Line,
                IBillButton.Delete,
                IBillButton.Save,
                IBillButton.Cancel,
                IBillButton.Return,
                IBillButton.Refresh
            };
    }
    
    @Override
	public int[] getListButtonAry() {
       return ISHSHConst.LIST_BUTTONS_M;
    }


    @Override
	public String getBillType() {
        return IBillType.eh_z00130;
    }

    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            DiscounttypeVO.class.getName(),
            DiscounttypeBVO.class.getName()  
        };
    }

    @Override
	public String getChildPkField() {
        return "pk_discounttype_b";
    }

    @Override
	public String getPkField() {
        return "pk_discounttype";
    }
   
     @Override
    public int getBusinessActionType() {
    	// TODO Auto-generated method stub
    	return nc.ui.trade.businessaction.IBusinessActionType.BD;
    }
    
     @Override
    public boolean isExistBillStatus() {
    	// TODO Auto-generated method stub
    	return false;
    }
}
