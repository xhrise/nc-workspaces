package nc.ui.eh.cw.h1102005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.cw.h1102005.ArapQuerymnyVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明：查款单 
 * @author 张起源 
 * 时间：2008-5-28 10:30:13
 */
public class ClientEventHandler extends AbstractEventHandler {
     
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		//非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        UFDouble zje = new UFDouble();
        for(int i=0;i<row;i++){
            UFDouble inmny = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "inmny")==null?"0":
                getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "inmny").toString());
            zje = inmny.add(zje);
        }
        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("zje", zje);
        
        super.onBoSave();
    }
    
    @Override
	public String addCondtion() {
        return " vbilltype = '"+IBillType.eh_h1102005+"'";
    }

    
    @SuppressWarnings("unchecked")
	@Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    	/****增行时带出客户所在地区，及管理档案中公司银行  edit by wb 2009-8-20 14:05:54***/
    	ArapQuerymnyVO hvo = (ArapQuerymnyVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	String pk_cubasdoc = hvo.getPk_cubasdoc()==null?null:hvo.getPk_cubasdoc();
        if(pk_cubasdoc!=null){
        	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        	StringBuffer sql = new StringBuffer()
        	.append("  select c.areaclname,d.pk_bankaccbas,d.accountcode,d.accountname")
        	.append("  from bd_cumandoc a join bd_cubasdoc b on a.pk_cubasdoc = b.pk_cubasdoc")
        	.append("  join bd_areacl c on b.pk_areacl = c.pk_areacl")
        	.append("  left join bd_bankaccbas d on a.def4 = d.pk_bankaccbas")
        	.append("  where a.pk_cumandoc = '"+pk_cubasdoc+"'");
	        ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
	        if(arr!=null&&arr.size()>0){
	        	HashMap hm = (HashMap)arr.get(0);
	        	String areaclname = hm.get("areaclname")==null?"":hm.get("areaclname").toString();
	        	String pk_bank = hm.get("pk_bankaccbas")==null?"":hm.get("pk_bankaccbas").toString();
	        	String accountcode = hm.get("accountcode")==null?"":hm.get("accountcode").toString();
	        	String accountname = hm.get("accountname")==null?"":hm.get("accountname").toString();
	        	int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
	        	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(areaclname, row, "hkarea");
		    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), row, "hkrq");
		    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_bank, row, "inbank");
		    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(accountcode, row, "inno");
		    	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(accountname, row, "vinbank");
	        }
        }
    }
    	
}