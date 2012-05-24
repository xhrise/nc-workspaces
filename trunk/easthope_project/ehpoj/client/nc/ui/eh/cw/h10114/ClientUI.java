
package nc.ui.eh.cw.h10114;

import nc.bs.eh.cw.h10114.ClientUICheckRuleGetter;
import nc.ui.eh.pub.ICombobox;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * ���ܣ�װж����
 * ���ߣ�zqy
 * ʱ�䣺2008-9-10 14:16:51 
 */

public class ClientUI extends BillCardUI {

    public ClientUI() {
        super();
        initilize();
    }

    public ClientUI(String arg0, String arg1, String arg2, String arg3, String arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
        initilize();
    }

    @Override
	protected ICardController createController() {
        return new ClientCtrl();
    }

    @Override
	protected CardEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }

    @Override
	public String getRefBillType() {
        return null;
    }

    
    @Override
	public void setDefaultData() throws Exception {
        try {

            Class c = Class.forName(getUIControl().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
            //��Ҫ�����
            getBufferData().clear();

            if (vos != null) {
                HYBillVO billVO = new HYBillVO();
                //�������ݵ�����
                billVO.setChildrenVO(vos);
                //�������ݵ�����
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //���õ�ǰ��
                getBufferData().setCurrentRow(0);
            } else {
                getBufferData().setCurrentRow(-1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
	protected void initSelfData() {
        getBillCardWrapper().initBodyComboBox("invtype", ICombobox.STR_DBTYPE,true);
        getBillCardWrapper().initBodyComboBox("invtype", ICombobox.STR_DBTYPE,true);
    }
    
    protected String getBodyWherePart() {
        return " pk_corp='" + _getCorp().getPk_corp() + "' and isnull(dr,0)=0  ";
    }
    
    @Override
	public Object getUserObject() {
        return new ClientUICheckRuleGetter();
    }

}