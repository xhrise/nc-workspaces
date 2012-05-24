
package nc.ui.eh.stock.z00150;


import nc.bs.eh.trade.z00150.ClientUICheckRuleGetter;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
/**
 * �������
 * @throws Exception
 * @author ����
 * 2008-05-15 ����04:03:18
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
    /**
     * ����: �Զ���ʾ���������
     * @throws Exception
     * @author ����
     * 2008-03-24 ����04:03:18
     */
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
     * @author ����
     * 2008-03-24 ����04:03:18
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
     * ����: ��һ����˳���ʼ��ֵ
     * @return
     * @return:String
     * @author ����
     * 2008-03-24 ����04:03:18
     */
    protected String getBodyWherePart() {
        return "  1=1 order by typecode";
    }
  //ǰ��̨У��
	@Override
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}
}