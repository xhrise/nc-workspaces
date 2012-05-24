
package nc.ui.eh.kc.h0251005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.TDInvdocByBOMRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.pub.KcVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明：材料出库单 
 * @author 张起源
 * 时间：2008-5-08
 */
public class ClientUI extends AbstractClientUI {
    
    public static String vpk_unit = null; //修改前的单位pk
    public static String unitname = null; //修改前的单位名字

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
	public void setDefaultData() throws Exception {
        super.setDefaultData();
		//获得出库日期
		getBillCardPanel().setHeadItem("ckdate", _getDate());
        
	}
	 @Override
	    protected void initPrivateButton() {
		 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	        addPrivateButton(btn1);
	    	super.initPrivateButton();
	    }
	
    

    @Override
	public boolean beforeEdit(BillEditEvent arg0) {
        //获取更改前的单位 add by zqy 2008-5-23 10:42:23
        String strKey = arg0.getKey();
        if(strKey.endsWith("vunit")){
            int row = getBillCardPanel().getBillTable().getRowCount();
            vpk_unit = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit").toString();
            unitname = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "vunit")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "vunit").toString();
        }
        //在选择仓库时获取物料,此时要根据物料仓库查询库存数量(没有仓位时)
        if(strKey.endsWith("vstore")){
            int row = getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            if(pk_invbasdoc==null){
            	showErrorMessage("请先选择物料!");
            }
        }
        //在选择仓位时获取物料,仓库 此时要根据物料仓库仓位查询库存数量
        if(strKey.endsWith("vpk_pos")){
            int row = getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_store = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store").toString();
            if(pk_invbasdoc==null){
            	showErrorMessage("请先选择物料!");
            }
            if(pk_store==null){
            	showErrorMessage("请先选择仓库!");
            }
        }
        //在选择替代物料之前将成品主键和当前原料主键 取出传到 参照中
        if(strKey.equals("vtdinvcode")){
            int row = getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?null:
            						getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_cpinvbasdocs = getBillCardPanel().getBodyValueAt(row, "pk_cpinvbasdocs")==null?null:
				getBillCardPanel().getBodyValueAt(row, "pk_cpinvbasdocs").toString();
            TDInvdocByBOMRefModel.pk_cpinvbasdocs = pk_cpinvbasdocs;
            TDInvdocByBOMRefModel.pk_ylinvbaddoc = pk_invbasdoc;
        }
        return super.beforeEdit(arg0);
    }
		 
	@SuppressWarnings("unchecked")
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
        
        //单位转换判断 add by zqy 2008-5-23 10:42:09
        if(strKey.equals("vunit")){
            int row = e.getRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_unit = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_unit").toString();
            HashMap hm = new PubTools().canChange(pk_invbasdoc);
            if(!(hm.containsKey(pk_unit))){
                showErrorMessage("你所选的单位在物料档案中没有维护，请核实！");
                getBillCardPanel().setBodyValueAt(vpk_unit, row, "pk_unit");
                getBillCardPanel().setBodyValueAt(unitname, row, "vunit");
                return;
            }
        }
        //根据物料、仓库带出对应库存(没有仓位时),单价  add by wb at 2008-6-14 10:17:41
        if(strKey.equals("vstore")||strKey.equals("vinvcode")){
            int row = e.getRow();
            String pk_invbasdoc = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            String pk_store = getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store")==null?null:
                getBillCardWrapper().getBillCardPanel().getBodyValueAt(row, "pk_store").toString();
            HashMap hm;
			try {
				hm = new PubTools().getDateinvKC(pk_store, pk_invbasdoc, _getDate(), "0", _getCorp().getPk_corp());
				UFDouble kcamount = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
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
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            
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
        //选择替代物料
        if(strKey.equals("vtdinvcode")){
            int row = e.getRow();
            String pk_tdinvbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_tdinvbasdoc")==null?null:
            						getBillCardPanel().getBodyValueAt(row, "pk_tdinvbasdoc").toString();
            if(pk_tdinvbasdoc!=null){
            	getBillCardPanel().getBillModel().setCellEditable(row,"vunit", true);
            	getBillCardPanel().getBillModel().setCellEditable(row,"blmount", true);
            }
        }
        
        /**选择仓库后，判断是否是末级仓库(花召滨要求) add by zqy 2010年11月23日10:38:19**/
        if(strKey.equals("pk_outtype") && e.getPos()==HEAD){
        	String pk_outtype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject()==null?"":
        		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject().toString();
        	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_outtype+"' and nvl(dr,0)=0 ";
        	try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				if(arr!=null && arr.size()>0){
					showErrorMessage("请选择末级出库类型!");
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_outtype", null);
					return;
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        }
        
        
		super.afterEdit(e);
	}
}