package nc.ui.eh.sc.h0451005;

import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.sc.h0451005.ScPosmBVO;
import nc.vo.eh.sc.h0451005.ScPosmVO;
import nc.vo.pub.lang.UFDouble;

/**
 * 功能说明：生产任务单
 * @author 王明
 * 2008-05-07 下午04:03:18
 * 改为 生产订单 edit by wb 2009-2-7 15:29:12
 */


public class ClientEventHandler extends AbstractEventHandler {
	
	public static ScPosmBVO[] eidtBVO = null;
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn){
         case IEHButton.LOCKBILL:    //关闭单据
             onBoLockBill();
             break;
		}
		super.onBoElse(intBtn);
	}
	
    @Override
	@SuppressWarnings("unchecked")
	public void onBoSave() throws Exception {
    	//对非空验证
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		//自制的时候前台校验
		String vsourcebilltype=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype")==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vsourcebilltype").getValueObject().toString();
		if(vsourcebilltype.equals("")){
	        BillModel bm = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
	        int res = new PubTools().uniqueCheck(bm, new String[] { "pk_invbasdoc"});
	        if (res == 1) {
	            getBillUI().showErrorMessage("产品有重复，不允许操作！");
	            return;
	        }
		}
		super.onBoSave();
		ScPosmVO re=(ScPosmVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
		String vsourcebillid=re.getVsourcebillid()==null?"":re.getVsourcebillid().toString();
		if(!vsourcebillid.equals("")){		//关闭上游MRP单据
			String updateSql = " update eh_sc_mrp set sc_flag = 'Y' where pk_mrp in ("+vsourcebillid+")";
			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			pubItf.updateSQL(updateSql);
		}
		
	}
	 
	// add by zqy 2008-5-20 11:25:07
    @Override
	public void onButton_N(ButtonObject bo, BillModel model) {
        super.onButton_N(bo, model);
        String bocode = bo.getCode();
        // 当从销售订单生成生产任务单时，生产日期不可修改
        if(bocode.equals("销售订单")) {
        	HashMap hm = new PubTools().getInvSafeKC(null);		//安全库存量 
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scdate").setEnabled(false);
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for (int i = 0; i < row; i++) {
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "vinvcode", false);
                //加上安全库存 add by wb at 2008-10-21 13:34:48
                String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
                							getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
                UFDouble safekc =  new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
                UFDouble scamount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "scmount")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "scmount").toString());		//安排生产数量 = 订单数量-已生产数量
                UFDouble truekc = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kc")==null?"":
					getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "kc").toString());			//实际库存
                UFDouble scrwamount = scamount.sub(truekc).add(safekc);				//生产任务数量=订单数量-实际库存量+安全库存量
                //如果生产任务数量<零，生产任务数量取零不安排生产并可修改；如果生产任务数量〉零，生产任务数量取正数安排生产并可修改
                if(scrwamount.toDouble()<0){
                	scamount = new UFDouble(0);
                	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt("不需要安排生产", i, "memo");
                }
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(safekc, i, "safekc");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scamount, i, "scmount");
            } 
        }
        /****************************增加从MRP->生产订单(任务单)流程**************************************************/
        if(bocode.equals("MRP运算")) {
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("scdate").setEnabled(false);
        }
        getBillUI().updateUI();
    }
    
    @Override
    protected void onBoDelete() throws Exception {
    	int res = onBoDeleteN(); // 1为删除 0为取消删除
    	if(res==0){
    		return;
    	}
    	ScPosmVO re=(ScPosmVO) (getBillCardPanelWrapper().getBillVOFromUI()).getParentVO();
		String vsourcebillid=re.getVsourcebillid()==null?"":re.getVsourcebillid().toString();
		if(!vsourcebillid.equals("")){		//开启上游MRP单据
			String updateSql = " update eh_sc_mrp set sc_flag = 'N' where pk_mrp in ("+vsourcebillid+")";
			PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			pubItf.updateSQL(updateSql);
		}
    	super.onBoTrueDelete();
    	
	}
    
    @Override
    protected void onBoEdit() throws Exception {
    	super.onBoEdit();
    }
    
    
}
