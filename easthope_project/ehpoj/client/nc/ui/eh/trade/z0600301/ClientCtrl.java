package nc.ui.eh.trade.z0600301;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z0600301.WeighbridgeconfigVO;

/**功能：司磅参数设置
 * @author 牛冶
 * 2008-03-24 下午04:03:18
 */
public class ClientCtrl extends AbstractCtrl {

     @Override
	public int[] getCardButtonAry() {       
    	 
        return new int[]{IBillButton.Query,IBillButton.Add, IBillButton.Edit, IBillButton.Delete,
                 IBillButton.Save,IBillButton.Cancel,IBillButton.Return,IBillButton.Refresh};
     }

     @Override
	public int[] getListButtonAry() {
           return new int[]{IBillButton.Query, IBillButton.Add, 
                   IBillButton.Card,IBillButton.Refresh};
     }


    @Override
	public String getBillType() {
        return IBillType.eh_z0600301;
    }


    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            WeighbridgeconfigVO.class.getName(),
            WeighbridgeconfigVO.class.getName()
            
        };
    }
    @Override
	public String getChildPkField() {
        return null;
    }

    @Override
	public String getPkField() {
        return "pk_weighbridgeconfig";
    }
    
    @Override
    public boolean isExistBillStatus() {
        return false;
    }
    
    /**
     * 获得BusinessAction种类(BD\PF)。 创建日期：(2004-1-15 13:47:52)
     * 
     * @return int
     */
    @Override
	public int getBusinessActionType() {
        return nc.ui.trade.businessaction.IBusinessActionType.BD;
    }



}



