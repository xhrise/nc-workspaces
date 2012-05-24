
package nc.ui.eh.cw.h1200201;



import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.ipub.ISQLChange;

/**
 * 无金额入库管理
 * @author wb
 * 2008-8-21 10:41:21 
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
//        try {
//
//            Class c = Class.forName(getUIControl().getBillVoName()[1]);
//            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
//            //需要先清空
//            getBufferData().clear();
//
//            if (vos != null) {
//                HYBillVO billVO = new HYBillVO();
//                //加载数据到单据
//                billVO.setChildrenVO(vos);
//                //加载数据到缓冲
//                if (getBufferData().isVOBufferEmpty()) {
//                    getBufferData().addVOToBuffer(billVO);
//                } else {
//                    getBufferData().setCurrentVO(billVO);
//                }
//
//                //设置当前行
//                getBufferData().setCurrentRow(0);
//            } else {
//                getBufferData().setCurrentRow(-1);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
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
    	String startdate = _getDate().toString().substring(0,8)+"01"; 	//indate between '"+startdate+"' and '"+_getDate()+"' and 
    	String enddate = _getDate().toString().substring(0,8)+_getDate().getDaysMonth();
        String pk_corp = _getCorp().getPrimaryKey();
        String sql = " pk_icout in" +
        					" (select pk_icout from eh_icout where pk_corp = '"+pk_corp+"' and pk_outtype = '"+ISQLChange.OUTTYPE_HJ+"' " +
        					" and outdate between '"+startdate+"' and '"+enddate+"' and isnull(dr,0)=0 and vbillstatus = 1 ) order by pk_icout ";
        return sql;
    }

	@Override
	public Object getUserObject() {
		return null;
	}


}