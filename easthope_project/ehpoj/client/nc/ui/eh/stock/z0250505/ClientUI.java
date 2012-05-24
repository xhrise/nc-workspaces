package nc.ui.eh.stock.z0250505;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;



/**
 * 功能说明：入库单
 * @author 王明
 * 2008-03-24 下午04:03:18
 */
public class ClientUI extends AbstractClientUI {
    	/**
    	 * 入库批次的修改：由原来在表头修改到表体，用表体的def_2字段
    	 * 徐命全  于 2009-10-15 修改
    	 */
    	private String pc = "";
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
		getBillCardPanel().setHeadItem("indate",_getDate());
		pc = PubTools.getPC("eh_stock_in", _getDate());// 批次
//		getBillCardPanel().setHeadItem("instalment",pc);     // 批次
		//表体批次
		BillModel mode = getBillCardPanel().getBillModel();
		for(int i = 0;i < mode.getRowCount();i ++){
		    mode.setValueAt(pc, i, "def_2");
		}
		getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
		//add by houcq begin 2010-11-22 
		String vsourcebilltype=getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject()==null?"":
			this.getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject().toString();//modify by houcq 2011-02-09
		if (!"".equals(vsourcebilltype))
		{
			getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
			getBillCardPanel().getBodyItem("inamount").setEnabled(false);
			getBillCardPanel().getBodyItem("inprice").setEnabled(false);
			getBillCardPanel().getBodyItem("vinvcode").setEnabled(false);//add by houcq 2011-03-09除自制单据外，物料不允许修改
		}
		else
		{
			getBillCardPanel().getBodyItem("vinvcode").setEnabled(true);//add by houcq 2011-03-09除自制单据外，物料不允许修改
			getBillCardPanel().getBodyItem("inamount").setEnabled(true);
			getBillCardPanel().getBodyItem("inprice").setEnabled(true);
		}
		//add by houcq end 
		super.setDefaultData();
	}
	@Override
	protected void initSelfData() {
		
		 getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
	     getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
		super.initSelfData();
		
	}
    @Override
	public void afterEdit(BillEditEvent e) {
        String strKey=e.getKey();
         if(e.getPos()==HEAD){
                String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
                getBillCardPanel().execHeadFormulas(formual);
            }else if (e.getPos()==BODY){
                String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
                getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            }else{
                getBillCardPanel().execTailEditFormulas();
            }
         //增行时，对批次进行设置
         if(strKey.equals("vinvcode")){
             int row=e.getRow();
             getBillCardPanel().setBodyValueAt(pc, row, "def_2");
         }
         if(strKey.equals("inprice")){
        	 int row=e.getRow();
        	 UFDouble inprice=new UFDouble(getBillCardPanel().getBodyValueAt(row, "inprice")==null?"0":
        		 getBillCardPanel().getBodyValueAt(row, "inprice").toString());
        	 getBillCardPanel().setBodyValueAt(inprice, row, "def_9");
         }
         //对扣重的计算 by 王明 to 05.20
         if(strKey.equals("deduamount")){
        	 int row=e.getRow();        	
        	UFDouble def_8=new UFDouble(getBillCardPanel().getBodyValueAt(row, "def_8")==null?"0":
       		 getBillCardPanel().getBodyValueAt(row, "def_8").toString());
        	 UFDouble deduamount=new UFDouble(getBillCardPanel().getBodyValueAt(row, "deduamount")==null?"0":
        		 getBillCardPanel().getBodyValueAt(row, "deduamount").toString());
        	 
        	 if(deduamount.toDouble()>=def_8.toDouble())
        	 {
        		 showErrorMessage("你所扣的重量不可以大于入库重量");
        		 getBillCardPanel().setBodyValueAt("", row, "deduamount");
        		 return;
        	 }
        	 UFDouble delete=def_8.sub(deduamount);
        	 getBillCardPanel().setBodyValueAt(delete, row, "inamount");
        	 
         }
        //对扣价的计算 by 王明 to 05.20
         if(strKey.equals("deduprice")){
        	 int row=e.getRow();
        	 UFDouble inprice=new UFDouble(getBillCardPanel().getBodyValueAt(row, "inprice")==null?"0":
        		 getBillCardPanel().getBodyValueAt(row, "inprice").toString());
        	 if(inprice.toDouble()<=0){
        		 getBillCardPanel().setBodyValueAt(inprice, row, "def_9");
        	 }
         	
         	UFDouble def_9=new UFDouble(getBillCardPanel().getBodyValueAt(row, "def_9")==null?"0":
        		 getBillCardPanel().getBodyValueAt(row, "def_9").toString());
         	
         	
        	 UFDouble deduprice=new UFDouble(getBillCardPanel().getBodyValueAt(row, "deduprice")==null?"0":
        		 getBillCardPanel().getBodyValueAt(row, "deduprice").toString());
        	 
        	 if(deduprice.toDouble()>=def_9.toDouble())
        	 {
        		 showErrorMessage("你所扣的价格不可以大于入库价格");
        		 getBillCardPanel().setBodyValueAt("", row, "deduprice");
        		 return;
        	 }
        	 UFDouble delete=def_9.sub(deduprice);
        	 getBillCardPanel().setBodyValueAt(delete, row, "inprice");
        	 
         }
         
         /**选择仓库后，判断是否是末级仓库(花召滨要求) add by zqy 2010年11月23日10:38:19**/
         if(strKey.equals("pk_intype") && e.getPos()==HEAD){
         	String pk_intype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject()==null?"":
         		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject().toString();
         	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
         	String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_intype+"' and nvl(dr,0)=0 ";
         	try {
 				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
 				if(arr!=null && arr.size()>0){
 					showErrorMessage("请选择末级入库类型!");
 					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_intype", null);
 					return;
 				}
 			} catch (BusinessException e1) {
 				e1.printStackTrace();
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

    


}
