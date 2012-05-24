package nc.ui.eh.cw.h1103015;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;

/**
 * 说明：收款单确认
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
         super.onBoSave();
         
     }

     @Override
	protected void onBoElse(int intBtn) throws Exception {
            switch (intBtn) {
            case IEHButton.CONFIRMBUG:  //确认按钮
                setEditionChange();
                break;
            }
        }
        
        private void setEditionChange() throws Exception {
            
            //点击确认按钮之后，确认状态，确认人，确认日期自动添加
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_flag", "Y");
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_psndoc", _getOperator());
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("qr_rq", _getDate());
            
            onBoSave();
            
        }
        
        @Override
		protected void onBoCard() throws Exception {
            super.onBoCard();
        }
        
        
        @Override
		protected void onBoQuery() throws Exception {
            StringBuffer sbWhere = new StringBuffer();
            if(askForQueryCondition(sbWhere)==false) 
                return;     
            String sqlWhere = sbWhere.toString();
            sqlWhere = sqlWhere.replaceFirst("审批不通过", "0");
            sqlWhere = sqlWhere.replaceFirst("审批通过", "1");
            sqlWhere = sqlWhere.replaceFirst("审批中", "2");
            sqlWhere = sqlWhere.replaceFirst("提交态", "3");
            sqlWhere = sqlWhere.replaceFirst("作废", "4");
            sqlWhere = sqlWhere.replaceFirst("冲销", "5");
            sqlWhere = sqlWhere.replaceFirst("终止", "6");
            sqlWhere = sqlWhere.replaceFirst("冻结状态", "7");
            sqlWhere = sqlWhere.replaceFirst("自由态", "8");
            
            //查询条件
            String billtype = IBillType.eh_h1103005;
            SuperVO[] queryVos = queryHeadVOs(sqlWhere.toString()+" and (vbilltype = '"+billtype+"') ");

            getBufferData().clear();
            // 增加数据到Buffer
            addDataToBuffer(queryVos);
            updateBuffer(); 
        }
        
}