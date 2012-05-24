package nc.ui.eh.iso.z0502010;

import nc.ui.eh.pub.IBillType;
import nc.ui.eh.uibase.AbstractSPEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.SuperVO;



/**
 * ����˵������ⱨ�浥��������
 * @author ����Դ
 * ʱ�䣺2008-4-11 
 */

public class ClientEventHandler extends AbstractSPEventHandler{
	
	

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}
	 
     @Override
	public String addCondtion() {
            return " vbilltype = '" + IBillType.eh_z0502005 + "' ";
        }
        
     @Override
	protected void onBoQuery() throws Exception {
         StringBuffer sbWhere = new StringBuffer();
         if(askForQueryCondition(sbWhere)==false) 
             return;

         String sqlWhere = sbWhere.toString();
         int pos = sqlWhere.indexOf("���ϸ�", 0);
         if(pos<=-1){
             sqlWhere = sqlWhere.replaceFirst("�ϸ�", "0");
         }else{
             sqlWhere = sqlWhere.replaceFirst("���ϸ�", "1");
         }
         SuperVO[] queryVos = queryHeadVOs(sqlWhere);
         
         getBufferData().clear();
         // �������ݵ�Buffer
         addDataToBuffer(queryVos);

         updateBuffer();
     }
     
}
