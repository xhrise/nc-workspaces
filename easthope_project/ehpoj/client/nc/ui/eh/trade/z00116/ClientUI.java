
package nc.ui.eh.trade.z00116;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;


/**
 * ��Ӧ����������
 * @throws Exception
 * @author ����
 * 2008��12��26��14:59:17
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
//        try {
//
//            Class c = Class.forName(getUIControl().getBillVoName()[1]);
//            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
//            //��Ҫ�����
//            getBufferData().clear();
//
//            if (vos != null) {
//                HYBillVO billVO = new HYBillVO();
//                //�������ݵ�����
//                billVO.setChildrenVO(vos);
//                //�������ݵ�����
//                if (getBufferData().isVOBufferEmpty()) {
//                    getBufferData().addVOToBuffer(billVO);
//                } else {
//                    getBufferData().setCurrentVO(billVO);
//                }
//
//                //���õ�ǰ��
//                getBufferData().setCurrentRow(0);
//            } else {
//                getBufferData().setCurrentRow(-1);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
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
//        return " pk_corp='" + _getCorp().getPk_corp() + "' order by areacode ";
    	return " pk_corp='" + _getCorp().getPk_corp() + "'";
    }
    
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.ToCusbasdoc,"���µ����̵���","���µ����̵���");
        btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btnNext);
   	super.initPrivateButton();
   }
    
    
}