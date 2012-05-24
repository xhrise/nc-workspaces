package nc.ui.eh.cw.h1100505;

import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.cw.h1100505.ArapFkqsVO;
import nc.vo.pub.AggregatedValueObject;

/**
 * ����˵�����������뵥
 * @author zqy
 * 2008-05-28 ����04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {

    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }

    @Override
	public void onBoSave() throws Exception {
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        ArapFkqsVO avo = (ArapFkqsVO) aggvo.getParentVO();
        String pk_sfkfs = avo.getPk_sfkfs()==null?"":avo.getPk_sfkfs().toString();//�ո��ʽ
        String cubasbank = avo.getCubasbank()==null?"0":avo.getCubasbank().toString();//�����ʻ�
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        String SQL = " select iscash from eh_arap_sfkfs where pk_sfkfs='"+pk_sfkfs+"' and isnull(dr,0)=0 ";
        Vector vector = (Vector) iUAPQueryBS.executeQuery(SQL.toString(), new VectorProcessor());
        if(vector!=null&&vector.size()==1){
        	Vector ve =(Vector) vector.get(0);
        	String iscash = ve.get(0)==null?"":ve.get(0).toString();
            if(!iscash.equals("Y") && cubasbank.length()<1){
                getBillUI().showErrorMessage("�뵽���̵�����ά���ÿ��̵������˻����˺�,лл!");
                return;
            }
        }
        super.onBoSave_withBillno();
    }
    
}
