package nc.ui.eh.iso.z0503005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.iso.z0503005.KzkjBVO;

/**
 * 说明：扣重扣价公式
 * @author 王明
 * 时间：2008年10月9日15:02:48 
 */
public class ClientEventHandler extends AbstractEventHandler {
   
	
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		//非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        //没有物料的情况出现在有物料的情况之下
        KzkjBVO [] bvos=(KzkjBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        for(int i=0;i<bvos.length;i++){
        	KzkjBVO bvo=bvos[i];
        	String pk_invbasdoc = bvo.getPk_invbasdoc()==null?"":bvo.getPk_invbasdoc().toString();
        	if(pk_invbasdoc.equals("")){
        		for(int j=0;j<bvos.length-i-1;j++){
            		KzkjBVO bvo2=bvos[j+i+1];
                	String pk_invbasdoc2 = bvo2.getPk_invbasdoc()==null?"":bvo2.getPk_invbasdoc().toString();
                	if(!pk_invbasdoc2.equals("")){
                		getBillUI().showErrorMessage("没有物料的情况，请维护到最后！");
                		return;
                	}
            	}
        	}
        }
        super.onBoSave();
	}
	
    @Override
	protected void onBoElse(int intBtn) throws Exception {
		// TODO Auto-generated method stub
		switch (intBtn)
        {
            case IEHButton.Prev:    //上一页 下一页
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //上一页 下一页
                onBoBrows(intBtn);
                break;
        }   
	}
     
     private void onBoBrows(int intBtn) throws java.lang.Exception {
        // 动作执行前处理
        buttonActionBefore(getBillUI(), intBtn);
        switch (intBtn) {
        case IEHButton.Prev: {
            getBufferData().prev();
            break;
        }
        case IEHButton.Next: {
            getBufferData().next();
            break;
        }
        }
        // 动作执行后处理
        buttonActionAfter(getBillUI(), intBtn);
        getBillUI().showHintMessage(
                nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory", "UPPuifactory-000503",null,new String[] { 
                		nc.vo.format.Format.indexFormat(getBufferData().getCurrentRow()+1) }));
          setBoEnabled();
    }
     
     
     /**
      * 允许别人修改 add by zqy 2010年11月23日15:56:54
      */
     protected void onBoEdit() throws Exception {
     	super.onBoEdit3();
     }
   
    /**
     * 允许别人删除 add by zqy 2010年11月23日15:57:13
     */
    protected void onBoDelete() throws Exception {
    	// TODO Auto-generated method stub
    	super.onBoDelete2();
    }
     
}