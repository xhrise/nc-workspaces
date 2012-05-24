
package nc.ui.eh.kc.h0251010;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.pub.KcVO;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 说明：其它材料出库单 
 * @author 张起源
 * 时间：2008-5-08
 */
public class ClientUI extends AbstractClientUI {
    
    public static String vpk_unit = null;
    public static String unitname = null;
    
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
	    //审批流
         getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
         getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
		super.initSelfData();
	}

	@Override
	public void setDefaultData() throws Exception {
		//获得出库日期
		getBillCardPanel().setHeadItem("ckdate", _getDate());        
        super.setDefaultData();
	}

    @Override
	public boolean beforeEdit(BillEditEvent arg0) {
        //获得修改前的单位
        int row = arg0.getRow();
        vpk_unit = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit").toString(); //修改前的单位
        
        unitname = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "vunit")==null?"":
            getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "vunit").toString();
                
        return super.beforeEdit(arg0);
    }
    
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		
		
		//表体中金额等于出库数量*单价
		if(strKey.equals("blmount")||strKey.equals("price")){
			int row = getBillCardPanel().getBillTable().getRowCount();
			for(int i=0;i<row;i++){
				UFDouble blmount = new UFDouble(getBillCardPanel().getBodyValueAt(i, "blmount")==null?"":
					getBillCardPanel().getBodyValueAt(i, "blmount").toString());
				UFDouble price = new UFDouble(getBillCardPanel().getBodyValueAt(i, "price")==null?"":
					getBillCardPanel().getBodyValueAt(i, "price").toString());
				UFDouble summoney = blmount.multiply(price);
				getBillCardPanel().setBodyValueAt(summoney, i, "summoney");
				
			}			
		}
		 //根据物料、仓库带出对应库存(没有仓位时),单价  add by wb at 2008-6-14 10:17:41
        if(strKey.equals("vstore")||strKey.equals("vinvcode")){
            int row = e.getRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_store = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store").toString();
            KcVO kcVO = new KcVO();
            kcVO.setPk_invbasdoc(pk_invbasdoc);
            kcVO.setPk_store(pk_store);
            kcVO.setPk_pos(null);
            kcVO.setPk_corp(_getCorp().getPk_corp());
            UFDouble kcamount = new PubTools().getKCamount_Back(kcVO,_getDate()); //库存
            
            int nmonth = _getDate().getMonth();
            int nyear = _getDate().getYear();
            
            CalcKcybbVO kcbbVO = new CalcKcybbVO();
            kcbbVO.setPk_corp(_getCorp().getPk_corp());
            kcbbVO.setPk_kcybb(pk_invbasdoc);
            kcbbVO.setPk_store(pk_store);
            kcbbVO.setPk_period(_getDate().toString());
            kcbbVO.setNyear(nyear);
            kcbbVO.setNmonth(nmonth);
            UFDouble price = new PubTools().getCKPrice(kcbbVO,2);    // 材料出库单价 (本月期初金额+本月出库金额)/(本月期初数量+本月出库数量)
            UFDouble blmount = new UFDouble(getBillCardPanel().getBodyValueAt(row, "blmount")==null?"":
				getBillCardPanel().getBodyValueAt(row, "blmount").toString());
			UFDouble summoney = blmount.multiply(price);
			getBillCardPanel().setBodyValueAt(price, row, "price");
			getBillCardPanel().setBodyValueAt(summoney, row, "summoney");
            getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount");
            
        }
        //根据物料、仓库、仓位带出对应库存  add by wb at 2008-6-14 10:17:41
        if(strKey.equals("vpk_pos")){
            int row = e.getRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_store = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store").toString();
            String pk_pos = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_pos")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_pos").toString();
            KcVO kcVO = new KcVO();
            kcVO.setPk_invbasdoc(pk_invbasdoc);
            kcVO.setPk_store(pk_store);
            kcVO.setPk_pos(pk_pos);
            kcVO.setPk_corp(_getCorp().getPk_corp());
            UFDouble kcamount = new PubTools().getKCamount_Back(kcVO,_getDate());
            getBillCardPanel().setBodyValueAt(kcamount, row, "kcamount");
        } 
        
        //单位选择后的判断 add by zhangqiyuan 2008-5-23 11:05:47
       if(strKey.equals("vunit")){
           int row = e.getRow();
        String pk_unit = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit").toString();  //表体中的单位(字段)
           UFDouble blmount = new UFDouble(getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "blmount")==null?"0":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "blmount").toString());  //表体中的数量
           UFDouble price = new UFDouble(getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "price")==null?"0":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "price").toString());  //表体中的单价
           UFDouble summoney = new UFDouble(getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "summoney")==null?"0":
               getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "summoney").toString());  //表体中的金额数
//           super.changeDW(vpk_unit, "pk_unit", "blmount", "price", "summoney");          
       }
        
		super.afterEdit(e);
	}
    
     @Override
	protected void initPrivateButton() {
         nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
         btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnPrev);
         nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
         btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnNext);
    }
}