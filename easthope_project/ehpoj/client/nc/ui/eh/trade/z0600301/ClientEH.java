package nc.ui.eh.trade.z0600301;

import nc.ui.pub.ButtonObject;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z0600301.WeighbridgeconfigVO;
import nc.vo.pub.SuperVO;

public class ClientEH extends ManageEventHandler {
	public ClientEH(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}


    @Override
	protected void onBoSave() throws Exception {
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        String pk_weighbridgeconfig=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_weighbridgeconfig").getValueObject()==null?"":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_weighbridgeconfig").getValueObject().toString();
          if(pk_weighbridgeconfig.equals("")){
        	  String key= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wbcode").getValueObject().toString();
              WeighbridgeconfigVO[] queryVos = (WeighbridgeconfigVO[]) queryHeadVOs("1=1 and wbcode='"+key+"' and pk_corp = '"+_getCorp().getPk_corp()+"'");
    	       if(queryVos.length != 0){
    	    	   getBillUI().showErrorMessage("地磅编码重复，请检查！");
    	       		return;
    	       }
          }
        super.onBoSave();
    }
    
    @Override
    protected void onBoQuery() throws Exception {
    	
        SuperVO[] queryVos = queryHeadVOs("1=1 and  pk_corp = '"+_getCorp().getPk_corp()+"'");

        getBufferData().clear();
        // 增加数据到Buffer
        addDataToBuffer(queryVos);
        updateBuffer();
        
    }
    @Override
    public void onButton(ButtonObject arg0) {
    	super.onButton(arg0);
    	String bocode = arg0.getCode();
    	if(bocode.equals("增加")){
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wbcode").setEnabled(true);
    	}
    }


}
