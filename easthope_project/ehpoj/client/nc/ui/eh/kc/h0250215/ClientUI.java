/**
 * 
 */
package nc.ui.eh.kc.h0250215;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵��: ����ڼ����
 * @author ţұ
 * 2007-9-20 ����01:03:18
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
     * ����: �Զ���ʾ��¼
     * @throws Exception
     * @author ţұ
     * 2007-10-23 ����06:24:59
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
     * @author ţұ
     * 2007-10-23 ����06:30:45
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
     * @author ţұ
     * 2007-10-23 ����06:25:25
     */
    protected String getBodyWherePart() {
        return " pk_corp='" + _getCorp().getPk_corp() + "' order by nyear,nmonth";
    }
    
    /*
     * ����˵�����Զ��尴ť
     */
    @Override
	protected void initPrivateButton() {
    	//add by houcq 2011-04-27������������ڼ䰴ť
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.PeriodClose, "����", "����");
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.PERIODCANCEL, "������", "ȡ������");
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.DOCMANAGE, "��������ڼ�", "��������ڼ�");
        
        btn.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        btn1.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        btn2.setOperateStatus(new int[]{IBillStatus.COMMIT,IBillStatus.CHECKGOING,IBillStatus.CHECKPASS,IBillStatus.NOPASS});
        addPrivateButton(btn);
        addPrivateButton(btn1);
        addPrivateButton(btn2);
    }
    
    
}
