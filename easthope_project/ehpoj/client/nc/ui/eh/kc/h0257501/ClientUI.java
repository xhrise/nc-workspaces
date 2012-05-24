package nc.ui.eh.kc.h0257501;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.trade.z0205501.DiscountrateVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.HYBillVO;

/**
 * 功能说明：折扣比例
 * @author 王兵
 * 2008-6-5 11:52:16
 */
public class ClientUI extends BillCardUI {
    
	public ClientUI() {
	    super();
	    init();
//	    initValue();
	}

	@SuppressWarnings("static-access")
	public  void initValue(){
		try {
			IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
            Vector vc  = (Vector)iUAPQueryBS.executeQuery("SELECT * FROM eh_discountrate WHERE pk_corp='" + _getCorp().getPk_corp() + "' and isnull(dr,0)=0 ",new VectorProcessor());
            DiscountrateVO rateVO  = new DiscountrateVO();
            //需要先清空
            getBufferData().clear();

            if (vc != null&&vc.size()>0) {
            	Vector vcc = (Vector)vc.get(0);
            	String pk_discountrate = vcc.get(0)==null?"":vcc.get(0).toString();
            	UFDouble discountrate = new UFDouble(vcc.get(1)==null?"0":vcc.get(1).toString());
            	String coperatorid  = vcc.get(2)==null?"":vcc.get(2).toString();
            	UFDate dmakedate = new UFDate(vcc.get(3)==null?"":vcc.get(3).toString());
            	String eidtcoperid  = vcc.get(4)==null?"":vcc.get(4).toString();
            	UFDate editdate = new UFDate(vcc.get(5)==null?"":vcc.get(5).toString());
            	
                //加载数据到单据
            	getBillCardPanel().setHeadItem(rateVO.DISCOUNTRATE, discountrate);
                getBillCardPanel().setTailItem(rateVO.COPERATORID, coperatorid);
                getBillCardPanel().setTailItem(rateVO.DMAKEDATE, dmakedate);
                getBillCardPanel().setTailItem(rateVO.EDITCOPERID,eidtcoperid);
                getBillCardPanel().setTailItem(rateVO.EDITDATE, editdate);
                getBillCardPanel().setHeadItem(rateVO.PK_CORP, _getCorp().getPk_corp());
                getBillCardPanel().setHeadItem(rateVO.PK_DISCOUNTRATE, pk_discountrate);
                getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
//        		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IBillButton.Edit,"修改","修改");
//                btn.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
            } 
        } catch (Exception e) {
            e.printStackTrace();
        } 
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

