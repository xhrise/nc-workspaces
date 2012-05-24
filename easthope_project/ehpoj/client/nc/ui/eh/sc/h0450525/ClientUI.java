package nc.ui.eh.sc.h0450525;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * 功能说明：产能设置
 * ZB31
 * @author 王兵
 * 2008-10-29 15:41:36
 */
public class ClientUI extends BillCardUI {
    
	public ClientUI() {
	    super();
	    init();
	}

	
	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().getHeadItem("pk_corp").setValue(_getCorp().getPk_corp());
		getBillCardPanel().setTailItem("coperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate",_getDate());
		
		}

	@Override
	protected void initSelfData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ICardController createController() {
		// TODO Auto-generated method stub
		return new ClientCtrl();
	}

	  @Override
	public CardEventHandler createEventHandler() {
	        // TODO Auto-generated method stub
	        return new ClientEventHandler(this,this.getUIControl());
	    }
	@Override
	public String getRefBillType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void init(){
		try {
            Class c = Class.forName(getUIControl().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, " pk_corp='" + _getCorp().getPk_corp() + "' ");
            //需要先清空
            getBufferData().clear();

            if (vos != null&&vos.length>0) {
                HYBillVO billVO = new HYBillVO();
                //加载数据到单据
                billVO.setParentVO(vos[0]);
                //加载数据到缓冲
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }
                //设置当前行
                getBufferData().setCurrentRow(0);
                getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
                getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
                getButtonManager().getButton(IBillButton.Refresh).setEnabled(true);
            	getButtonManager().getButton(IBillButton.Add).setEnabled(false);
            	updateButtons();
            } else {
                getBufferData().setCurrentRow(-1);
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        
	} 

}

