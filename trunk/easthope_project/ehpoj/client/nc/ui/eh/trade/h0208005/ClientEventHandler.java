/**
 * @(#)MyEventHandler.java  V3.1 2007-3-22
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.trade.h0208005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.lang.UFDate;

/**
 * 功能 原料价格 ZA98
 * 
 * @author WB 2008-10-14 13:45:27
 */

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		super.onBoSave();
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.GENRENDETAIL:    //生成明细
                onBoGENRENDETAIL();
                break;
        }  
        super.onBoElse(intBtn);
    }
	
	 //生成原料明细
	@SuppressWarnings("unchecked")
	private void onBoGENRENDETAIL() {
		//先清空表体
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	int res = getBillUI().showOkCancelMessage("生成原料明细需要先清空表体,是否确认清空?");
        	if(res==1){
        		int[] rowcount=new int[rows];
                for(int i=rows - 1;i>=0;i--){
                	rowcount[i]=i;
                }
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rowcount);
        	}else{
        		return;
        	}
        }
//		StringBuffer sql = new StringBuffer()
//		.append(" select invcode,invname,pk_invbasdoc")
//		.append(" from eh_invbasdoc")
//		.append(" where invcode not like '001%' and invcode not like '01%' and SUBSTRING(invcode,1,3)<'010'")
//		.append(" and isnull(lock_flag,'N')='N'")
//		.append(" and pk_corp = '"+_getCorp().getPk_corp()+"'")
//		.append(" and isnull(dr,0)=0 order by invcode");
        UFDate	date = _getDate();
        int year = date.getYear();
        int month = date.getMonth();
        //新版SQL
        StringBuffer sql = new StringBuffer()
//        .append("  select a.invcode, a.invname, b.pk_invmandoc pk_invbasdoc")
//        .append("    from bd_invbasdoc a, bd_invmandoc b,eh_stordoc c")
//        .append("   where a.pk_invbasdoc = b.pk_invbasdoc")
//        .append("   and b.def1 = c.pk_bdstordoc ")
//        .append("   and c.is_flag = 0")
//        .append("   and b.pk_corp = '"+_getCorp().getPk_corp()+"'")
//        .append("   and ( b.sealflag = 'N' or b.sealflag is null )")
//        .append("   and a.invcode like '0101%'")
//        .append("   and nvl(b.dr,0)=0");
        /*
         * 原料价格录入： 库存月报表有库存的 且 编码=01或者 07 且 仓库类型不为成品库的 
         */
        .append(" select a.pk_invbasdoc,c.invcode,c.invname,a.qmsl from (")
        .append(" select b.pk_invbasdoc, sum(nvl(b.qmsl, 0)) qmsl")
        .append(" from eh_calc_kcybb a, eh_calc_kcybb_b b, eh_stordoc c")
        .append(" where a.pk_kcybb = b.pk_kcybb and nvl(a.dr, 0) = 0")
        .append(" and nvl(b.dr, 0) = 0 and ((b.pk_store = c.pk_bdstordoc")
        .append(" and c.is_flag <> 1) or b.pk_store is null) and a.nyear = "+year+"  and a.nmonth = "+month)
        .append(" and a.pk_corp='"+_getCorp().getPk_corp()+"'")
        .append(" group by b.pk_invbasdoc) a,bd_invmandoc b,bd_invbasdoc c")
        .append(" where a.pk_invbasdoc=b.pk_invmandoc and b.pk_invbasdoc=c.pk_invbasdoc")
        .append(" and b.pk_corp = '"+_getCorp().getPk_corp()+"'")
        .append(" and (c.invcode like '01%' or c.invcode like '07%')");
		try{
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				String pk_invbasdoc = null;			//物料
				String invcode = null;
				String invname = null;
				for(int i=0;i<arr.size();i++){
					HashMap hm = (HashMap)arr.get(i);
					pk_invbasdoc = hm.get("pk_invbasdoc")==null?null:hm.get("pk_invbasdoc").toString();
					invcode = hm.get("invcode")==null?null:hm.get("invcode").toString();
					invname = hm.get("invname")==null?null:hm.get("invname").toString();
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invcode, i, "vinvcode");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(invname, i, "vinvname");
					getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	protected void onBoCopy() throws Exception {
		super.onBoCopy();
		((ClientUI)getBillUI()).setDefaultData();
		getBillUI().updateUI();
	}
//
//	public HashMap getInvPrice(){
//		HashMap hm = new HashMap();
//		
//	}
}

