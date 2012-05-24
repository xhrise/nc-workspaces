
package nc.ui.eh.trade.z0205525;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明：折扣调整单
 * @author 张起源
 * 时间：2008-4-12
 */
	public class ClientUI extends AbstractClientUI {
	public ClientUI() {
	     super();
	 }
   
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	
	@Override
	protected void initSelfData() {
		super.initSelfData();
	}
	@Override
	public boolean beforeEdit(BillEditEvent arg0) {
			String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
				getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
			String pk_cubasdocadd=getBillCardPanel().getHeadItem("pk_cubasdocadd").getValueObject()==null?"":
				getBillCardPanel().getHeadItem("pk_cubasdocadd").getValueObject().toString();
			if(pk_cubasdoc.equals("")&&pk_cubasdocadd.equals("")){
				showErrorMessage("客户不能为空,请先选择客户!");
				int rowcount=getBillCardPanel().getRowCount();
	            int[] rowss=new int[rowcount];
	            for(int i=rowcount - 1;i>=0;i--){
	                rowss[i]=i;
	            getBillCardPanel().getBillModel().delLine(rowss);
	            }
			}
		
		return super.beforeEdit(arg0);
	}
	@Override
	public void afterEdit(BillEditEvent e) {
		if(e.getPos()==HEAD&&e.getKey().equals("pk_cubasdoc")){
			String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
				getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
			InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;
			getBillCardPanel().setHeadItem("pk_cubasdocadd", "");
			try {
				UFDouble discount = getCustZKye(pk_cubasdoc);				//调出客户的折扣余额
				getBillCardPanel().setHeadItem("def_6", discount);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			int rowcount=getBillCardPanel().getRowCount();
            int[] rowss=new int[rowcount];
            for(int i=rowcount - 1;i>=0;i--){
                rowss[i]=i;
            getBillCardPanel().getBillModel().delLine(rowss);
            }
		}
		
		super.afterEdit(e);
	}
	
	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();

	}
	 @Override
	protected void initPrivateButton() {
		 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"生成明细","生成明细");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_ADD});
	        addPrivateButton(btn1);
	    	super.initPrivateButton();
	    }
    
	 
	 /**
	  * 查询客户的二次折扣余额
	  * @param pk_cubasdoc
	  * @return
	  * @throws Exception
	  * wb 2008-11-26 9:44:48
	  */
	 public UFDouble getCustZKye(String pk_cubasdoc) throws Exception{
		 UFDouble discount = new UFDouble(0);
		 StringBuffer sql=new StringBuffer()
		 .append("select sum(isnull(ediscount,0)) value from eh_perioddiscount ")
         .append(" where pk_corp='"+_getCorp().getPk_corp()+"' and nyear = "+_getDate().getYear()+" and nmonth = "+_getDate().getMonth()+"")
         .append(" and pk_cubasdoc='"+pk_cubasdoc+"'   and isnull(dr,0)=0 ");
		 IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		 ArrayList arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
         if(arr!=null&&!arr.isEmpty()){
              HashMap hm = (HashMap) arr.get(0);
              discount = new UFDouble(hm.get("value")==null?"0":hm.get("value").toString());
         }
         return discount;
	 }

}