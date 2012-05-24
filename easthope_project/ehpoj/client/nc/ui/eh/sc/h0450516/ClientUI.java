package nc.ui.eh.sc.h0450516;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.sc.h0450516.ScBomapplyaBVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能说明：BOM生报表（一）
 * @author 王明
 * 2008年12月30日9:10:07
 */

public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}

	@Override
	public void setDefaultData() throws Exception {
		 //生产日期
		 getBillCardPanel().setHeadItem("applydate", _getDate()); 
		super.setDefaultData_withNObillno();
	}
    
	@Override
	protected void initSelfData() {
		//审批流
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
	     super.initSelfData();
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		if(e.getPos()==BODY){
			int row = e.getRow();
			
			if(strKey.equals("vinvcode")){
				try {
					String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?null:
						getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();					//物料
					if(pk_invbasdoc!=null){
						HashMap hm =  new PubTools().getDateinvKC(null, pk_invbasdoc, _getDate(), "0", _getCorp().getPk_corp());
						UFDouble kcamount = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());			//库存数量
			    		getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount");
			    		UFDouble price = getInvPrice(pk_invbasdoc);
			    		getBillCardPanel().setBodyValueAt(price, row, "price");
			    		ScBomapplyaBVO bvo = getInvPFxx(pk_invbasdoc);
			    		if(bvo!=null){
				    		getBillCardPanel().setBodyValueAt(bvo.getWet(), row, "wet");
				    		getBillCardPanel().setBodyValueAt(bvo.getGlair(), row, "glair");
				    		getBillCardPanel().setBodyValueAt(bvo.getCa(), row, "ca");
				    		getBillCardPanel().setBodyValueAt(bvo.getPhosphor(), row, "phosphor");
				    		getBillCardPanel().setBodyValueAt(bvo.getSalinity(), row, "salinity");
				    		getBillCardPanel().setBodyValueAt(bvo.getAsh(), row, "ash");
				    		getBillCardPanel().setBodyValueAt(bvo.getFi(), row, "fi");
				    		getBillCardPanel().setBodyValueAt(bvo.getFat(), row, "fat");
			    		}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		super.afterEdit(e);	
	}
	 @Override
	    protected void initPrivateButton() {
		 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	        addPrivateButton(btn1);
	    	super.initPrivateButton();
	    }
	 
	 /***
	  * 市场价 取盈亏考核中最新市场单价
	  * @param pk_invbasdoc
	  * @return
	  * @throws Exception
	  */
	 public UFDouble getInvPrice(String pk_invbasdoc) throws Exception{
		 UFDouble price = new UFDouble(0); 
		 StringBuffer sql = new StringBuffer()
		 .append("  SELECT * FROM( SELECT b.price FROM eh_trade_materialprice a ,eh_trade_materialprice_b b")
		 .append("  WHERE a.pk_price = b.pk_price")
		 .append("  AND b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		 .append("  AND NVL(a.dr,0) = 0 AND NVL(b.dr,0)=0")
		 .append("  ORDER BY b.ts DESC ) WHERE ROWNUM <= '1'");
		 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 Object obj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
		 price = obj==null?price:new UFDouble(obj.toString());
		 return price;
	 }
	 
	 /***
	  * 取最后该物料的配方信息
	  * @param pk_invbasdoc
	  * @return
	  * @throws Exception
	  */
	 public ScBomapplyaBVO getInvPFxx(String pk_invbasdoc) throws Exception{
		 StringBuffer sql = new StringBuffer()
		 .append("  SELECT * FROM (SELECT b.* FROM eh_sc_bomapplya a ,eh_sc_bomapplya_b b")
		 .append("  WHERE a.pk_applya = b.pk_applya")
		 .append("  AND b.pk_invbasdoc = '"+pk_invbasdoc+"' and a.vbillstatus = 1")
		 .append("  AND NVL(a.dr,0) = 0 AND NVL(b.dr,0)=0")
		 .append("  ORDER BY b.ts DESC )WHERE ROWNUM <= '1' ");
		 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		 ScBomapplyaBVO bvo = (ScBomapplyaBVO)iUAPQueryBS.executeQuery(sql.toString(), new BeanProcessor(ScBomapplyaBVO.class));
		 return bvo;
	 }
}
