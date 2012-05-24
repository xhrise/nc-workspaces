package nc.ui.eh.sc.h0450525;

import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * ����˵������������
 * ZB31
 * @author ����
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
            //��Ҫ�����
            getBufferData().clear();

            if (vos != null&&vos.length>0) {
                HYBillVO billVO = new HYBillVO();
                //�������ݵ�����
                billVO.setParentVO(vos[0]);
                //�������ݵ�����
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }
                //���õ�ǰ��
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

