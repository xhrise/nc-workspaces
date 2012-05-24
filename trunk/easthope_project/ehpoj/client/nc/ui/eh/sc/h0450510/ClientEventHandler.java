package nc.ui.eh.sc.h0450510;

import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.SuperVO;

/**
 * ����˵�������鵵��
 * @author ����
 * 2008-05-07 ����04:03:18
 */

public class ClientEventHandler extends ManageEventHandler {
	String oldcode=null;
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

    @Override
	public void onBoSave() throws Exception {
		
		// �Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();	
		super.onBoSave();

	}

	@Override
	protected void onBoEdit() throws Exception {
		oldcode=(String) (getBillCardPanelWrapper().getBillCardPanel().getHeadItem("teamcode")==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("teamcode").getValueObject());
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("teamcode", " "+oldcode);
			
		super.onBoEdit();
	}

    
	@Override
	protected void onBoCancel() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("teamcode",oldcode);
		super.onBoCancel();
	}
	
	 private QueryConditionClient dlg = null;
     protected QueryConditionClient getQueryDLG()
     {        
         if(dlg == null){
             dlg = createQueryDLG();
         }
         return dlg;
     }
          
    @Override
	protected void onBoQuery() throws Exception {
    	ClientEnvironment ce = ClientEnvironment.getInstance();
    	int type = getQueryDLG().showModal();
        if(type==1){
			String sqlWhere = getQueryDLG().getWhereSQL()==null?"":getQueryDLG().getWhereSQL();
			SuperVO[] queryVos = queryHeadVOs(sqlWhere.toString()+" pk_corp = '"+ce.getCorporation().getPk_corp()+"'");
			
	       getBufferData().clear();
	       // �������ݵ�Buffer
	       addDataToBuffer(queryVos);
	
	       updateBuffer();
        }
	}        
    
    protected QueryConditionClient createQueryDLG() {
    	QueryConditionClient dlg = new QueryConditionClient();
	        dlg.setTempletID(_getCorp().getPk_corp(), getBillUI().getModuleCode(), null, null); 
	        dlg.setNormalShow(false);
        return dlg;
    }

}
