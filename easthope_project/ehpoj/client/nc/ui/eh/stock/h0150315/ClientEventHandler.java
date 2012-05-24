package nc.ui.eh.stock.h0150315;

/**
 * ���ɹ�����
 * ZB23
 * @author wangbing
 * 2009-1-7 17:38:34
 */

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.h0150315.StockWjdecisionCVO;
import nc.vo.eh.stock.h0150315.StockWjdecisionVO;
import nc.vo.eh.trade.z00115.CubasdocVO;
import nc.vo.pub.BusinessException;

public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	
	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("editcoperid", _getOperator());
		getBillCardPanelWrapper().getBillCardPanel().setTailItem("editdate", _getDate());
     	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
     	super.onBoSave_withBillno();
     	StockWjdecisionVO hvo =(StockWjdecisionVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String pk_apply = hvo.getVsourcebillid();
        if(pk_apply!=null&&pk_apply.length()>0){
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String updateSQL = "update eh_stock_apply set jc_flag = 'Y' where pk_apply = '"+pk_apply+"' ";   
        	pubitf.updateSQL(updateSQL);
        }
	}
	
	
	@Override
	protected void onBoDelete() throws Exception {
	    //��д�ĵ��ɹ�����
	   	int res = onBoDeleteN(); // 1Ϊɾ�� 0Ϊȡ��ɾ��
	   	if(res==0){
	   		return;
	   	}
	   	StockWjdecisionVO hvo =(StockWjdecisionVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String pk_apply = hvo.getVsourcebillid();
        if(pk_apply!=null&&pk_apply.length()>0){
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String updateSQL = "update eh_stock_apply set jc_flag = 'N' where pk_apply = '"+pk_apply+"' ";   
        	pubitf.updateSQL(updateSQL);
        }
	    super.onBoTrueDelete();
	}
	
    //���ö�ҳǩ��ӡ add by zqy
    @Override
    protected void onBoPrint() throws Exception {
        nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
                ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
        nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
                dataSource);
        print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
                ._getModuleCode(), getBillUI()._getOperator(), getBillUI()
                .getBusinessType(), getBillUI().getNodeKey());
        print.selectTemplate();
        print.preview();
    }
   
    @Override
	public void onButton_N(ButtonObject bo, BillModel model) {
    	super.onButton_N(bo, model);
    	String bocode=bo.getCode()==null?"":bo.getCode();
        //�������֪ͨ�����ɳ��ⵥʱ���ͻ��ͷ�Ʊ�Ų�����༭��ͬʱ���岻�ܽ����в���
//        if(bocode.equals("���ɹ�����")){
//        	String pk_psndoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").getValueObject()==null?"":
//        				getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").getValueObject().toString();
//        	System.out.println(pk_psndoc);
//        	String[] formual = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").getEditFormulas();//��ȡ�༭��ʽ
//	        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formual);
//	        getBillUI().updateUI();
//        }
        
    }
    //add by houcq 2011-04-08
    @Override
	public void onBoEdit() throws Exception {
    	super.onBoEdit();    
    	StockWjdecisionCVO[] cvos =(StockWjdecisionCVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			for (int i=0;i<cvos.length;i++)
			{
				StockWjdecisionCVO cvo = cvos[i];
				StringBuffer str = new StringBuffer()
				.append("SELECT * FROM bd_cubasdoc cubas WHERE cubas.pk_cubasdoc IN(SELECT cuman.pk_cubasdoc FROM bd_cumandoc cuman WHERE cuman.pk_cumandoc = '"+cvo.getPk_cubasdoc()+"')");    			
				CubasdocVO cuvo = (CubasdocVO)iUAPQueryBS.executeQuery(str.toString(), new BeanProcessor(CubasdocVO.class));
				boolean sf_flag = cuvo.getFreecustflag()==null?false:cuvo.getFreecustflag().booleanValue();   //ɢ�����
				getBillCardPanelWrapper().getBillCardPanel().getBodyItem("psninfo").setEnabled(sf_flag);
			}
		} catch (BusinessException e1) {
			e1.printStackTrace();
		}
    }
}
