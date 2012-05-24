
package nc.ui.eh.sc.h0451505;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.ipub.ISHSHConst;
import nc.vo.eh.sc.h0451505.ScPgdBVO;
import nc.vo.eh.sc.h0451505.ScPgdBillVO;
import nc.vo.eh.sc.h0451505.ScPgdPsnVO;
import nc.vo.eh.sc.h0451505.ScPgdVO;

/**
 * 功能说明：派工单(生产计划单)
 * @author 王兵
 * 2008-5-7 11:36:45
 */
public class ClientCtrl extends AbstractCtrl {

    public ClientCtrl() {
        super();
    }

    @Override
	public int[] getCardButtonAry() {
    	 int[] btns=nc.ui.eh.button.ButtonTool.insertButtons(
	                new int[] { IBillButton.Busitype},
	                ISHSHConst.CARD_BUTTONS, 0);
    	    //modify by houcq 2010-11-29取消生产计划单终止单据按钮
	    	//int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.SENDFA,IEHButton.BusinesBtn},0);
	    	int[] btnss=nc.ui.eh.button.ButtonTool.insertButtons(btns,new int[]{IEHButton.SENDFA},0);
	    	return btnss;
    }
    
    @Override
	public int[] getListButtonAry() {
        return nc.ui.eh.button.ButtonTool.insertButtons(
               new int[] { IBillButton.Busitype},ISHSHConst.LIST_BUTTONS_M, 0);
    }
    
    @Override
	public String[] getBillVoName() {
        return new String[]{
            ScPgdBillVO.class.getName(),
            ScPgdVO.class.getName(),
            ScPgdBVO.class.getName(),
            ScPgdPsnVO.class.getName()
        };
    }
   
    @Override
	public String getPkField() {
        return "pk_pgd";
    }
    
    @Override
	public String getBillType() {
        return IBillType.eh_h0451505;
    }
    


}