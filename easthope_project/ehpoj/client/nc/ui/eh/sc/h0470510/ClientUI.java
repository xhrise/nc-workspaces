
package nc.ui.eh.sc.h0470510;


import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/** 
 * ���ܣ��豸�ͺ�ά��
 * ZA27
 * 2009-1-9 14:19:58
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
	protected void initSelfData() {
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
    /**
     * ����: ��ʼ������setDefaultData()
     * @return:void
     * @author ����Դ
     * 2008-04-11
     */
    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * ����: �����ѯ����
     * @return
     * @return:String
     * @author ����Դ
     * 2008-03-24
     */
    protected String getBodyWherePart() {
        return "pk_corp = '"+_getCorp().getPk_corp()+"'";//�޸ģ�ֻ�ܿ�����˾������豸ά���� ʱ�䣺2009-11-16
    }

	@Override
	public Object getUserObject() {
		return null;
	}


}