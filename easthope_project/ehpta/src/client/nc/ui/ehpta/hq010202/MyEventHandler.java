package nc.ui.ehpta.hq010202;

import nc.ui.ehpta.pub.valid.Validata;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.query.HYQueryConditionDLG;
import nc.ui.trade.query.INormalQuery;
import nc.vo.pub.SuperVO;
import nc.vo.querytemplate.TemplateInfo;

/**
  *
  *������AbstractMyEventHandler�������ʵ���࣬
  *��Ҫ�������˰�ť��ִ�ж������û����Զ���Щ����������Ҫ�����޸�
  *@author author
  *@version tempProject version
  */
  
  public class MyEventHandler 
                                          extends AbstractMyEventHandler{

	public MyEventHandler(BillManageUI billUI, IControllerBase control){
		super(billUI,control);		
	}
	
	
	@Override
	protected void onBoSave() throws Exception {
		Validata.saveValidataIsNull(getBillCardPanelWrapper().getBillCardPanel() , getBillCardPanelWrapper().getBillVOFromUI() , new String[]{"ehpta_storfare_b"});
		super.onBoSave();
	}
}