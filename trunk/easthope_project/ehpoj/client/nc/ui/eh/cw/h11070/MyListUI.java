
package nc.ui.eh.cw.h11070;

import java.awt.Color;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 ���ܣ��տ��뷢Ʊ����
 ���ߣ�newyear
 ���ڣ�2007-3-20 ����11:27:40
 **/
public class MyListUI extends BillListUI {

    public MyListUI() {
        super();
    }

    /*
     ���ܣ����캯��
     ���ߣ�newyear.Cn
     ���ڣ�2007-3-20 ����11:27:46
     **/
    public MyListUI(String pk_corp, String pk_billType, String pk_busitype,
            String operater, String billId) {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }


    @Override
	protected IListController createController() {
        return new MyListCtrl();
    }

    @Override
	public String getRefBillType() {
        return null;
    }
    
    @Override
	protected ListEventHandler createEventHandler() {
        return  new MyEventHandler(this,this.getUIControl());
    }

    
    @Override
	protected void initSelfData() {
    	getBillListPanel().getHeadTable().setBackground(Color.yellow);

     }

    @Override
	public void setDefaultData() throws Exception {

    }
    
    
    /**
     * ��ʼ���б����ݡ�
     * �������ڣ�(2004-2-5 9:16:25)
     */
    @Override
	protected void initBillData(String strWhere) throws Exception {

    }
    
    public void setHeadData(SuperVO[] queryVos) throws Exception{
        //��ջ�������
        getBufferData().clear();
        if (queryVos != null && queryVos.length != 0)
        {
            for (int i = 0; i < queryVos.length; i++)
            {
                AggregatedValueObject aVo =(AggregatedValueObject) Class.forName(getUIControl().getBillVoName()[0]).newInstance();
                aVo.setParentVO(queryVos[i]);
                getBufferData().addVOToBuffer(aVo);
            }
            setListHeadData(queryVos);
            getBufferData().setCurrentRow(0);
            setBillOperate(IBillOperate.OP_NOTEDIT);
        }
        else
        {
            setListHeadData(queryVos);
            getBufferData().setCurrentRow(-1);
            setBillOperate(IBillOperate.OP_INIT);
        }
    }
    
    /*
     * �иı��¼���
     * �������ڣ�(2001-3-23 2:02:27)
     * @param e ufbill.BillEditEvent
     */
    @Override
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e)
    {
//        if (e.getSource() == getBillListPanel().getParentListPanel().getTable())
//            getBufferData().setCurrentRow(e.getRow());

    }
    
    /*
     * ���ܣ�ע���Զ��尴ť
     * ʱ�䣺2008-09-2
     * ���ߣ�ţұ
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"����","����");
        addPrivateButton(btnNext);      
    }


}

