
package nc.ui.eh.trade.h1400101;



import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.eh.trade.h1400101.CustoverageHVO;
import nc.vo.eh.trade.h1400101.CustoverageVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * ˵��������������
 * ���ͣ�ZB41
 * ���ߣ���־Զ
 * ʱ�䣺2010��01��26��
 */
public class ClientEventHandler extends ManageEventHandler {
     nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }
    
    
    
    
	@Override
	protected void onBoSave() throws Exception {
		AggregatedValueObject aggVO = getBillCardPanelWrapper().getBillVOFromUI();
		super.onBoSave();
	}




	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.CALCKCYBB:    // ����
                onBoCalcKCYBB();
                break;
        }
    }
	
    protected void onBoQuery() throws Exception {
    	// TODO Auto-generated method stub
    	StringBuffer sbWhere = new StringBuffer()
//    	.append(" invtype = 'Y' and ");             // ԭ��
    	.append("");
		if(askForQueryCondition(sbWhere)==false) 
			return;		
		SuperVO[] queryVos = queryHeadVOs(sbWhere.toString());
		
       getBufferData().clear();
       // �������ݵ�Buffer
       addDataToBuffer(queryVos);

       updateBuffer();
    }
    
	@SuppressWarnings("static-access")
	private void onBoCalcKCYBB() {
		CalcDialog calcDialog = new CalcDialog();
		calcDialog.showModal();
		String pk_period = calcDialog.pk_period;
		//String pk_store = calcDialog.pk_store;
        if(pk_period!=null&&pk_period.length()>0){
			IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
//				StordocVO storVO = (StordocVO) iUAPQueryBS.retrieveByPK(StordocVO.class, pk_store);
				int ret = getBillUI().showYesNoMessage("��ȷ��Ҫ���¼���"+perVO.getNyear()+"��"+perVO.getNmonth()+"�¿����������");
		        if (ret ==UIDialog.ID_YES){
		        	//CalcKcybbVO kcVO = new CalcKcybbVO();
		        	CustoverageHVO custvo = new CustoverageHVO();
		        	custvo.setPk_corp(_getCorp().getPk_corp());
		        	custvo.setPk_period(pk_period);
		        	custvo.setCoperatorid(_getOperator());
		        	custvo.setCalcdate(_getDate().toString());
		        	custvo.setNyear(perVO.getNyear().toString());
		        	custvo.setNmonth(perVO.getNmonth().toString());
		        	custvo.setCalcdate(_getDate().toString());
		        	
		        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		        	pubItf.getCustOverage(custvo);
		        	//��������ʾ������
		        	String whereSql = //" pk_corp = '"+_getCorp().getPk_corp()+"' and NVL(dr,0)=0 ";
		        		"nyear = "+perVO.getNyear()+" and nmonth = "+perVO.getNmonth()+" and pk_corp = '"+_getCorp().getPk_corp()+"' and NVL ( dr, 0 ) = 0";
		        	nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
	                SuperVO[] supervo = business.queryByCondition(CustoverageHVO.class, whereSql);
	                getBufferData().clear();
	     	       // �������ݵ�Buffer
	     	       addDataToBuffer(supervo);
	     	       updateBuffer();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		
	}
}

