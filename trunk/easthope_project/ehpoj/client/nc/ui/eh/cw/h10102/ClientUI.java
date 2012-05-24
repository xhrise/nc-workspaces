
package nc.ui.eh.cw.h10102;



import nc.bs.eh.cw.h10102.ClientUICheckRuleGetter;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
/**
 * �������
 * @author ����
 * 2008-05-28 
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
    private void initilize() {
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
        try {
            setDefaultData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected String getBodyWherePart() {
        return "  pk_corp='" + _getCorp().getPk_corp() + "' and isnull(dr,0)=0 order by fylbcode ";
    }
    
    @Override
	public void afterEdit(BillEditEvent e) {
    	String Str=e.getKey();
    	
    	if(Str.equals("fylbcode")){
    		int row=e.getRow();
    		getBillCardPanel().setBodyValueAt(_getOperator(), row, "coperatorid");
    		getBillCardPanel().setBodyValueAt(_getDate(), row, "dmakedate");
    		String[] formual=getBillCardPanel().getBodyItem("coperatorid").getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
    	}
    	super.afterEdit(e);
    }

	@Override
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}


}