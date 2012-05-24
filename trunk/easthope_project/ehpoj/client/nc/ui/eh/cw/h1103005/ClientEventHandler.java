package nc.ui.eh.cw.h1103005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.cw.h1103005.ArapSkVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明：收款单 
 * @author 张起源 
 * 时间：2008-5-28 14:36:14
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
        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("hxje", zje);
        //当收款类型为单位,个人时判断对应的单位、个人字段不能为空
        String sktype = ((UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sfktype").getComponent()).getRefName();
        ArapSkVO skVO = (ArapSkVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String pk_cubasdoc = skVO.getPk_cubasdoc();		//单位
        String pk_psndoc = skVO.getPk_psndoc();			//个人
        if(sktype.equals("单位款项")&&pk_cubasdoc==null){
        	getBillUI().showErrorMessage("收款类型为单位款项,客户名称不能为空!");
        	return;
        }
        if(sktype.equals("员工款项")&&pk_psndoc==null){
        	getBillUI().showErrorMessage("收款类型为员工款项,员工名称不能为空!");
        	return;
        }
        //houcq 2011-01-11注释
//        //将客户原先余额+本次收款额合计显示到页面上，不涉及业务。时间2010-01-11作者：张志远
//        PubTools tools = new PubTools();
//        UFDouble overage = tools.getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//查找客户余额
//        UFDouble skmoney = new UFDouble(0);
//        int rows = this.getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
//        for(int i=0;i<rows;i++){
//        	UFDouble money = new UFDouble(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "inmny").toString());
//        	skmoney = skmoney.add(money);
//        }
//        overage = overage.add(skmoney);
//        UFDouble temp = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("canusemoney").getValue());
//        this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("canusemoney",temp.add(zje));
        super.onBoSave();
    }
    
    @Override
    protected void onBoElse(int intBtn) throws Exception {
        super.onBoElse(intBtn);
    }

	
    @Override
	public void onButton_N(ButtonObject bo, BillModel model) {
        super.onButton_N(bo, model);
        String bocode = bo.getCode();
        // 当从查款单生成收货单时候，客户是不允许编辑的,不允许进行行操作
        if (bocode.equals("查款单")) {
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("skrq").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sktype").setEnabled(false);
            
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for (int i = 0; i < row; i++) {
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "hkrq", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "hkarea", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "hth", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "vskfs", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "inbank", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "inno", false);
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "inmny", false);
            }
//            getButtonManager().getButton(IBillButton.AddLine).setEnabled(false);
//            getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
//            getButtonManager().getButton(IBillButton.InsLine).setEnabled(false);
//            getButtonManager().getButton(IBillButton.CopyLine).setEnabled(false);
//            getButtonManager().getButton(IBillButton.PasteLine).setEnabled(false);
        }
        
        try {
            AggregatedValueObject aggvo = this.getBillCardPanelWrapper().getBillVOFromUI();
            ArapSkVO avo = (ArapSkVO) aggvo.getParentVO();
            if(avo.equals("散户")){
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("shinfo").setEnabled(true);
            }else{
                getBillCardPanelWrapper().getBillCardPanel().getHeadItem("shinfo").setEnabled(false);
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }       
        getBillUI().updateUI();
    }	
    
    @Override
	protected void setBoEnabled() throws Exception {
        String qr_flag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qr_flag").getValueObject()==null?"":
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("qr_flag").getValueObject().toString();
        if(qr_flag.equals("true")){
            getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
        }
        super.setBoEnabled();
        
    }
    
    
    @SuppressWarnings("unchecked")
	@Override
    protected void onBoLineAdd() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoLineAdd();
    	ArapSkVO hvo = (ArapSkVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
    	String pk_cubasdoc = hvo.getPk_cubasdoc()==null?"":hvo.getPk_cubasdoc();
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
    
    /**
     * 功能：对数值设置千分位。
     * 时间：2010-01-13
     * 作者：张志远
     */
}