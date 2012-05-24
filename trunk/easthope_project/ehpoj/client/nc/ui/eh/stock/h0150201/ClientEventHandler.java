/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.stock.h0150201;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150201.StockStandardBVO;
import nc.vo.eh.stock.h0150201.StockStandardVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 
功能：原料采购标准
作者：zqy
日期：2008-12-11 下午02:54:46
 */

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		 //唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("表体物料有重复，不允许操作！");
            return;
        }         
       
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");   
        }
        
        /**-----在相同的库存与在途使用天数中,一种物料只能维护一次---------**/
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        StockStandardVO vo = (StockStandardVO) aggvo.getParentVO();
        StockStandardBVO[] svo = (StockStandardBVO[]) aggvo.getChildrenVO();
        String pk_standard = vo.getPk_standard()==null?" ":vo.getPk_standard().toString();      //主表主键
        StringBuffer SQL = new StringBuffer();                                                 //存放错误信息
        UFDouble kcuseday = null;       //库存天数
        UFDouble ztuseday = null;       //在途天数
        for(int i=0;i<svo.length;i++){
            String pk_invbasdoc = svo[i].getPk_invbasdoc()==null?"":svo[i].getPk_invbasdoc().toString();     //表体物料PK
            StringBuffer sql = new StringBuffer()
            .append(" select kcuseday,ztuseday from eh_stock_standard ")
            .append(" where pk_standard in ( ")
            .append(" select pk_standard from eh_stock_standard_b where pk_invbasdoc='"+pk_invbasdoc+"' ")
            .append(" and pk_standard<>'"+pk_standard+"' and NVL(dr,0)=0 ) ")
            .append(" and NVL(dr,0)=0 ");
            Vector arr = (Vector)iUAPQueryBS.executeQuery(sql.toString(),new VectorProcessor());
            if(arr!=null && arr.size()>0){
                Vector ve = (Vector)arr.get(0);
                kcuseday = new UFDouble(ve.get(0)==null?"0":ve.get(0).toString());
                ztuseday = new UFDouble(ve.get(1)==null?"0":ve.get(1).toString());
                SQL.append("第("+(i+1)+")行物料已经在库存使用天数("+kcuseday+")与在途使用天数("+ztuseday+")中维护过了,请核实!\r\n");
            }
        }
        if(SQL.length()>0){
            getBillUI().showErrorMessage(SQL.toString());
            return;
        }
        
        if(ClientUI.flag==1){
            String sql = "delete from eh_stock_standard_b where pk_standard_b in "+ClientUI.pk_ftstandard_b ;
            PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            pubItf.updateSQL(sql);
        }
        ClientUI.pk_ftstandard_b="('')";
		super.onBoSave();
	}
	
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit2();
    }
	
}

