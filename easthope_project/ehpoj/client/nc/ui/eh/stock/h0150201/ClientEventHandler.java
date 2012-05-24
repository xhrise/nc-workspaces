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
���ܣ�ԭ�ϲɹ���׼
���ߣ�zqy
���ڣ�2008-12-11 ����02:54:46
 */

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		 //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("�����������ظ��������������");
            return;
        }         
       
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");   
        }
        
        /**-----����ͬ�Ŀ������;ʹ��������,һ������ֻ��ά��һ��---------**/
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        StockStandardVO vo = (StockStandardVO) aggvo.getParentVO();
        StockStandardBVO[] svo = (StockStandardBVO[]) aggvo.getChildrenVO();
        String pk_standard = vo.getPk_standard()==null?" ":vo.getPk_standard().toString();      //��������
        StringBuffer SQL = new StringBuffer();                                                 //��Ŵ�����Ϣ
        UFDouble kcuseday = null;       //�������
        UFDouble ztuseday = null;       //��;����
        for(int i=0;i<svo.length;i++){
            String pk_invbasdoc = svo[i].getPk_invbasdoc()==null?"":svo[i].getPk_invbasdoc().toString();     //��������PK
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
                SQL.append("��("+(i+1)+")�������Ѿ��ڿ��ʹ������("+kcuseday+")����;ʹ������("+ztuseday+")��ά������,���ʵ!\r\n");
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

