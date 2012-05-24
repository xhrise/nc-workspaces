/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.stock.h0150005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150005.GyspjBVO;
import nc.vo.eh.stock.h0150005.GyspjVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 
 * @author 
 功能：供应商评价
 作者：zqy
 日期：2009-3-9 下午02:42:09
 */

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	@SuppressWarnings("unchecked")
    public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_cubasdoc","pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("表体有相同供应商,物料存在，不允许操作！");
            return;
        }
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        GyspjVO hvo = (GyspjVO)aggvo.getParentVO();
        GyspjBVO[] bvo = (GyspjBVO[]) aggvo.getChildrenVO();
        String pk_cubasdoc = null;              //客户PK
        String pk_invbasdoc = null;             //物料PK
        UFDouble workpoint = null;              //工作配合分
        StringBuffer error1 = new StringBuffer();  //错误信息
        StringBuffer error2 = new StringBuffer();  //重复维护
        if(bvo!=null && bvo.length>0){
        	HashMap hm = getExitsData(hvo.getPk_gyspj(), hvo.getPk_period(),_getCorp().getPk_corp());
            for(int i=0;i<bvo.length;i++){
                pk_cubasdoc = bvo[i].getPk_cubasdoc()==null?"":bvo[i].getPk_cubasdoc().toString();
                pk_invbasdoc = bvo[i].getPk_invbasdoc()==null?"":bvo[i].getPk_invbasdoc().toString();
                workpoint = new UFDouble(bvo[i].getWorkpoint()==null?"0":bvo[i].getWorkpoint().toString());
                if(workpoint.toDouble()>10){
                  	error1.append("第"+(i+1)+"行的工作配合分不能大于10分,请核实!\r\n");
                }
                if(hm.containsKey(pk_cubasdoc+pk_invbasdoc)){
                	error2.append("第"+(i+1)+"行的供应商对应的物料已做评价,请核实!\r\n");
                }
            }
        }
        if(error1!=null && error1.length()>0){
            getBillUI().showErrorMessage(error1.toString());
            return;
        }
        if(error2!=null && error2.length()>0){
            getBillUI().showErrorMessage(error2.toString());
            return;
        }
		super.onBoSave();
	}
    
	@Override
	protected void onBoCopy() throws Exception {
		super.onBoCopy();
		((ClientUI)getBillUI()).setDefaultData();
		getBillUI().updateUI();
	}

	/* （非 Javadoc）
     * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
     */
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
        	case IEHButton.GENRENDETAIL:    //生成明细
        		onGENRENDETAIL();
            break;
        }   
        super.onBoElse(intBtn);
    }
	/***
	 * 找到当月已做评价的供应商物料
	 * wb 2009年4月1日17:41:32
	 * @param pk_gyspj
	 * @param pk_period
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("unchecked")
	public HashMap getExitsData(String pk_gyspj,String pk_period,String pk_corp) throws Exception{
    	HashMap<String,String> hm = new HashMap<String, String>();
    	String sql = " select b.pk_cubasdoc,b.pk_invbasdoc from eh_gyspj a,eh_gyspj_b b where a.pk_gyspj = b.pk_gyspj and a.pk_period = '"+pk_period+"'" +
    			     " and a.pk_gyspj<>'"+pk_gyspj+"' and a.pk_corp = '"+pk_corp+"' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0";
    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                    
	    ArrayList<HashMap> arr = (ArrayList<HashMap>) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
	    if(arr!=null&&arr.size()>0){
	    	String pk_cubasdoc = null;
	    	String pk_invbasdoc = null;
	    	for(HashMap hmA:arr){
	    		pk_cubasdoc = hmA.get("pk_cubasdoc")==null?"":hmA.get("pk_cubasdoc").toString();
	    		pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
	    		hm.put(pk_cubasdoc+pk_invbasdoc, pk_cubasdoc+pk_invbasdoc);
	    	}
	    }
	    return hm;
    }
    
    /***
     * 生成本月供应商物料明细
     * wb 
     * 2009-4-24 10:42:33
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void onGENRENDETAIL() throws Exception{
    	int rowss= getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        if(rowss>0){
            int iRet = getBillUI().showYesNoMessage("生成明细前要清空表体，是否确定?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                    int[] rows=new int[rowss];
                    for(int i=rowss - 1;i>=0;i--){
                        rows[i]=i;
                    }
                    getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
            }else{
            	return;
            }
        }  
        //<修改> 功能：存货档案系统表字段：eh_invbasdoc->bd_invbasdoc,def_2->invpinpai,colour->def1日期：2009-08-10 下午15:42:09;作者：张志远
    	//<修改> 功能：客商档案管理表和基本表：bd_cumandoc,bd_cubasdoc日期：2009-08-14 下午15:42:09;作者：张志远
        //<修改> 功能：品牌表：eh_brand，取品牌名称：brandname日期：2009-08-14 下午15:42:09;作者：张志远
        //String dates = _getDate().toString().substring(0,7);
        String dates = null;
        //年
        String year = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject()==null?"":this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject().toString();
        //月
        String month = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmonth").getValueObject()==null?"":this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmonth").getValueObject().toString();
        if("".equals(year)||"".equals(month)){
        	this.getBillUI().showErrorMessage("请选择明细年月");
        }else{
        	dates = year + "-" + month;
        }
        StringBuffer sql = new StringBuffer()
//    	.append("  SELECT DISTINCT a.pk_cubasdoc,c.custcode,c.custname, f.pk_invmandoc pk_invbasdoc,d.invcode,d.invname,d.invspec,d.invtype,g.brandname,d.def1")
//    	.append("  FROM eh_stock_in a ,eh_stock_in_b b,bd_cubasdoc c,bd_invbasdoc d,bd_cumandoc e  ,bd_invmandoc f ,eh_brand g ")
//    	.append("  WHERE a.pk_in = b.pk_in")
//    	.append("  AND a.pk_cubasdoc = e.pk_cumandoc")
//    	.append("  AND c.pk_cubasdoc = e.pk_cubasdoc")
//    	.append("  AND b.pk_invbasdoc = f.pk_invmandoc")
//    	.append("  AND d.pk_invbasdoc = f.pk_invbasdoc")
//    	.append("  AND g.pk_brand = d.invpinpai")
//    	.append("  AND a.vbillstatus = 1 ")
//    	.append("  AND a.pk_contract IS NOT NULL")
//    	.append("  AND a.pk_corp = '"+_getCorp().getPk_corp()+"'")
//    	.append("  AND a.indate like '"+dates+"%'")
//    	.append("  AND NVL(a.dr,0)=0 AND NVL(b.dr,0)=0");
    	.append(" SELECT DISTINCT a.pk_cubasdoc, ")
    	.append("                 c.custcode, ")
    	.append("                 c.custname, ")
    	.append("                 f.pk_invmandoc pk_invbasdoc, ")
    	.append("                 d.invcode, ")
    	.append("                 d.invname, ")
    	.append("                 d.invspec, ")
    	.append("                 d.invtype, ")
    	.append("                 g.brandname, ")
    	.append("                 d.def1 ")
    	.append("   FROM eh_stock_in a ")
    	.append("  INNER JOIN eh_stock_in_b b ON a.pk_in = b.pk_in ")
    	.append("  INNER JOIN bd_cumandoc e ON a.pk_cubasdoc = e.pk_cumandoc ")
    	.append("  INNER JOIN bd_cubasdoc c ON c.pk_cubasdoc = e.pk_cubasdoc ")
    	.append("  INNER JOIN bd_invmandoc f ON b.pk_invbasdoc = f.pk_invmandoc ")
    	.append("  INNER JOIN bd_invbasdoc d ON d.pk_invbasdoc = f.pk_invbasdoc ")
    	.append("  LEFT JOIN eh_brand g ON g.pk_brand = d.invpinpai ")
    	.append("  WHERE a.vbillstatus = 1 ")
    	//.append("    AND a.pk_contract IS NOT NULL ")
    	.append("    AND a.pk_corp = '"+_getCorp().getPk_corp()+"' ")
    	.append("    AND a.indate like '"+dates+"%' ")
    	.append("    AND NVL(a.dr, 0) = 0 ")
    	.append("    AND NVL(b.dr, 0) = 0 ");

    	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());                    
	    ArrayList<HashMap> arr = (ArrayList<HashMap>) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
	    if(arr!=null&&arr.size()>0){
	    	String pk_cubasdoc = null;
	    	String custcode = null;
	    	String custname = null;
	    	String pk_invbasdoc = null;
	    	String invcode = null;
	    	String invname = null;
	    	String invspec = null;
	    	String invtype = null;
	    	String brandname = null;
	    	String colour = null;
	    	int i  = 0 ;
	    	for(HashMap hmA:arr){
	    		pk_cubasdoc = hmA.get("pk_cubasdoc")==null?"":hmA.get("pk_cubasdoc").toString();
	    		custcode = hmA.get("custcode")==null?"":hmA.get("custcode").toString();
	    		custname = hmA.get("custname")==null?"":hmA.get("custname").toString();
	    		pk_invbasdoc = hmA.get("pk_invbasdoc")==null?"":hmA.get("pk_invbasdoc").toString();
	    		invcode = hmA.get("invcode")==null?"":hmA.get("invcode").toString();
	    		invname = hmA.get("invname")==null?"":hmA.get("invname").toString();
	    		invspec = hmA.get("invspec")==null?"":hmA.get("invspec").toString();
	    		invtype = hmA.get("invtype")==null?"":hmA.get("invtype").toString();
	    		brandname = hmA.get("brandname")==null?"":hmA.get("brandname").toString();
	    		colour = hmA.get("def1")==null?"":hmA.get("def1").toString();
	    		getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(custcode, i, "custcode");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(custname, i, "custname");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(pk_cubasdoc, i, "pk_cubasdoc");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(invcode, i, "invcode");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(invname, i, "invname");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(invspec, i, "gg");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(invtype, i, "xh");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(colour, i, "ys");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(brandname, i, "pp");
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setValueAt(pk_invbasdoc, i, "pk_invbasdoc");
                i++;
	    	}
	    }
    }
    
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit2();
    }
}

