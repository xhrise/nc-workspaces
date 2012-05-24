package nc.ui.eh.cw.h1104005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractCtrl;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.cw.h1104005.ArapFyVO;
import nc.vo.eh.pub.PubBillVO;

/**功能：费用单录入
 * @author 张起源
 * 时间：2008-5-29 10:45:29
 */
public class ClientCtrl extends AbstractCtrl {

     @Override
	public int[] getCardButtonAry() {       
         return new int[]{
                 IBillButton.Add, 
                 IBillButton.Edit, 
                 IBillButton.Delete,
                 IBillButton.Line, 
                 IBillButton.Save,
                 IBillButton.Action,
                 IBillButton.Cancel,
                 IBillButton.Return,
                 IBillButton.Refresh,
                 IBillButton.Print,
                 IEHButton.Prev,
                 IEHButton.Next,
                 IEHButton.CONFIRMBUG
         };
     }

     @Override
	public int[] getListButtonAry() {
           return new int[]{
        		   IBillButton.Query, 
        		   IBillButton.Add, 
                   IBillButton.Card,
                   IBillButton.Refresh,
                   IEHButton.Prev,
                   IEHButton.Next
           };
     }


    @Override
	public String getBillType() {
        return IBillType.eh_h1104005;
    }


    @Override
	public String[] getBillVoName() {
        return new String[]{
            PubBillVO.class.getName(),
            ArapFyVO.class.getName(),
            ArapFyVO.class.getName()
            
        };
    }
    @Override
	public String getChildPkField() {
        return null;
    }

    @Override
	public String getPkField() {
        return "pk_fy";
    }
    
    @Override
    public boolean isExistBillStatus() {
        return false;
    }
 
   



}



