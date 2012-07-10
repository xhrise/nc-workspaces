package nc.ui.ehpta.hq010101;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.ehpta.pub.btn.DefaultBillButton;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.bd.warehouseinfo.StordocVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.field.BillField;
import nc.vo.trade.pub.IBillStatus;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 
 * @author author
 * @version tempProject version
 */
public class ClientUI extends AbstractClientUI  {
	
	private AggregatedValueObject nowAggVO = null;
	
	protected ManageEventHandler createEventHandler() {
		return new MyEventHandler(this, getUIControl());
	}

	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}

	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}

	protected void initSelfData() {
		UIRefPane sendRef = (UIRefPane) getBillCardPanel().getBodyItem("shipaddr").getComponent();
		sendRef.setWhereString(" pk_corp = " + _getCorp().getPk_corp()+ " and nvl(dr , 0) = 0 ");

		UIRefPane getRef = (UIRefPane) getBillCardPanel().getBodyItem("arriveaddr").getComponent();
		getRef.setWhereString(" pk_corp = " + _getCorp().getPk_corp()+ " and nvl(dr , 0) = 0 ");
		
		getBillListPanel().setParentMultiSelect(true);

	}
/**
 * 自定义按钮
 */
	@Override
	protected void initPrivateButton() {
		
		super.initPrivateButton();

		addPrivateButton(DefaultBillButton.getDisabledButtonVO());
		addPrivateButton(DefaultBillButton.getEnabledButtonVO());
		
	}
	
	public void setDefaultData() throws Exception {
		BillField fileDef = BillField.getInstance();
		String billtype = getUIControl().getBillType();
		String pkCorp = ClientEnvironment.getInstance().getCorporation()
				.getPrimaryKey();

		String[] itemkeys = new String[] { fileDef.getField_Corp(),
				fileDef.getField_Operator(), fileDef.getField_Billtype(),
				fileDef.getField_BillStatus() , "singledate" };
		Object[] values = new Object[] { pkCorp,
				ClientEnvironment.getInstance().getUser().getPrimaryKey(),
				billtype, new Integer(IBillStatus.FREE).toString(),_getDate() };

		for (int i = 0; i < itemkeys.length; i++) {
			BillItem item = null;
			item = getBillCardPanel().getHeadItem(itemkeys[i]);
			if (item == null)
				item = getBillCardPanel().getTailItem(itemkeys[i]);
			if (item != null)
				item.setValue(values[i]);
		}
	}

	@Override
	public void afterUpdate() {
		super.afterUpdate();
		
		updateButtonState();
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
		UIRefPane trans = null;
		try {
			if ("pk_shipperparty".equals(e.getKey())) {
				trans = (UIRefPane) getBillCardPanel().getHeadItem(e.getKey()).getComponent();
				Object transtuoyun = iUAPQueryBS.executeQuery("select custname from bd_cubasdoc where pk_cubasdoc='" + trans.getRefPK() + "'",new ColumnProcessor());
				getBillCardPanel().getHeadItem("shipperparty").setValue(transtuoyun);
			
			} else if("pk_securedparty".equals(e.getKey())){
				trans = (UIRefPane) getBillCardPanel().getHeadItem(e.getKey()).getComponent();
				Object transdangbao = iUAPQueryBS.executeQuery("select custname from bd_cubasdoc where pk_cubasdoc='" + trans.getRefPK() + "'", new ColumnProcessor());
				getBillCardPanel().getHeadItem("securedparty").setValue(transdangbao);
			
			}else if ("pk_shipperowner".equals(e.getKey())) {
				trans = (UIRefPane)getBillCardPanel().getHeadItem(e.getKey()).getComponent();
				Object transchengyun = iUAPQueryBS.executeQuery("select custname from bd_cubasdoc where pk_cubasdoc='"+trans.getRefPK()+"'", new ColumnProcessor());
				getBillCardPanel().getHeadItem("shipperowner").setValue(transchengyun);
			
			}else if("pk_send".equals(e.getKey())){
				trans = (UIRefPane)getBillCardPanel().getBodyItem(e.getKey()).getComponent();
				Object fayunaddr = iUAPQueryBS.executeQuery("select storaddr from bd_stordoc where pk_stordoc='"+trans.getRefPK()+"'", new ColumnProcessor());
				getBillCardPanel().setBodyValueAt(fayunaddr, e.getRow(), "shipaddr");
				getBillCardPanel().setBodyValueAt(trans.getRefPK(), e.getRow(), "sendpk");
				
			}else if("pk_arrive".equals(e.getKey())){
				trans = (UIRefPane)getBillCardPanel().getBodyItem(e.getKey()).getComponent();
				Object daohuoaddr = iUAPQueryBS.executeQuery("select storaddr from bd_stordoc where pk_stordoc='"+trans.getRefPK()+"'", new ColumnProcessor());
				getBillCardPanel().setBodyValueAt(daohuoaddr, e.getRow(), "arriveaddr");
				getBillCardPanel().setBodyValueAt(trans.getRefPK(), e.getRow(), "arrivepk");
				
			}else if("shippingprice".equals(e.getKey()) || "shippingprice_a".equals(e.getKey())){
				
				UFDouble shipp_a = new UFDouble(getBillCardPanel().getBodyValueAt(e.getRow(), "shippingprice_a") == null ? "0" : getBillCardPanel().getBodyValueAt(e.getRow(), "shippingprice_a").toString());
				if(shipp_a.doubleValue() < -100) {
					shipp_a = new UFDouble(-100);
					getBillCardPanel().setBodyValueAt(shipp_a , e.getRow() , "shippingprice_a" );
				}
				
				UFDouble shipp = new UFDouble(getBillCardPanel().getBodyValueAt(e.getRow() , "shippingprice") == null ? "0" : getBillCardPanel().getBodyValueAt(e.getRow() , "shippingprice").toString() );
				UFDouble calculate = shipp_a.doubleValue() == -100 ? new UFDouble(0) : shipp.multiply(shipp_a.div(100).add(1));
				getBillCardPanel().setBodyValueAt(calculate, e.getRow(), "actualshippingprice");
			
			
			}else if("mtzbfprice".equals(e.getKey()) || "rkscprice".equals(e.getKey()) || "rkscprice_c".equals(e.getKey()) || "nhcyprice".equals(e.getKey()) || "carprice".equals(e.getKey())){
				
				UFDouble mtprice = new UFDouble( getBillCardPanel().getBodyValueAt(e.getRow(), "mtzbfprice") == null ? "0" :  getBillCardPanel().getBodyValueAt(e.getRow(), "mtzbfprice").toString());
				UFDouble rkprice = new UFDouble( getBillCardPanel().getBodyValueAt(e.getRow(), "rkscprice") == null ? "0" :  getBillCardPanel().getBodyValueAt(e.getRow(), "mtzbfprice").toString());
				UFDouble rkcprice = new UFDouble( getBillCardPanel().getBodyValueAt(e.getRow(), "rkscprice_c") == null ? "0" : getBillCardPanel().getBodyValueAt(e.getRow(), "rkscprice_c").toString());
				UFDouble nhprice = new UFDouble( getBillCardPanel().getBodyValueAt(e.getRow(), "nhcyprice") == null ? "0" : getBillCardPanel().getBodyValueAt(e.getRow(), "nhcyprice").toString());
				UFDouble caprice = new UFDouble (getBillCardPanel().getBodyValueAt(e.getRow(), "carprice") == null ? "0" : getBillCardPanel().getBodyValueAt(e.getRow(), "carprice").toString());
				
				UFDouble calculateprice = mtprice.add(rkprice).add(rkcprice).add(nhprice).add(caprice);
				getBillCardPanel().setBodyValueAt(calculateprice, e.getRow(), "sjfee");
			}
			
		} catch (Exception e2) {
			AppDebug.debug(e2);
		}
	}

	
	private void updateButtonState(){
		
		if(nowAggVO ==null || nowAggVO.getParentVO()==null) 
			return ;
		
		UFBoolean ty_flag = (UFBoolean) nowAggVO.getParentVO().getAttributeValue("stopstatus");
	
		try{
			if(ty_flag.booleanValue()){
				getButtonManager().getButton(DefaultBillButton.DISABLED).setEnabled(false);
				getButtonManager().getButton(DefaultBillButton.ENABLED).setEnabled(true);
			}else{
				getButtonManager().getButton(DefaultBillButton.DISABLED).setEnabled(true);
				getButtonManager().getButton(DefaultBillButton.ENABLED).setEnabled(false);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		updateButtons();
		
	}
	
	@Override
	protected int getExtendStatus(AggregatedValueObject vo) {
		
		nowAggVO = vo;
		
		return super.getExtendStatus(vo);
	}

	@Override
	public boolean beforeEdit(BillEditEvent e) {
		
		return super.beforeEdit(e);
	}

}
