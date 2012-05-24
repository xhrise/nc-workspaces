
package nc.ui.eh.kc.h0257005;


import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.kc.h0257005.CalcKcybbVO;
import nc.vo.pub.SuperVO;

/**
 * ˵����ԭ�Ͽ���±���
 * ���ͣ�ZA48
 * ���ߣ�wb
 * ʱ�䣺2008��5��8��16:34:56
 */
public class ClientEventHandler extends ManageEventHandler {
     nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
        // TODO Auto-generated constructor stub
    }
    
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.CALCKCYBB:    // ����
                onBoCalcKCYBB();
                break;
        }
    }
    @Override
    protected void onBoQuery() throws Exception {
    	// TODO Auto-generated method stub
    	StringBuffer sbWhere = new StringBuffer()
    	.append(" invtype = 'Y' and ");             // ԭ��
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
		String pk_store = calcDialog.pk_store;
        if(pk_period!=null&&pk_period.length()>0){    
			IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());	    	
			try {
				/*
	        	 *���Ӽ���ʱ�����·���֤���ܡ�
				 *����ͷ���·����¼���ڵ��·ݲ�һ��ʱ����ʾ���������·�������·ݲ�ͳһ���޷����㣬��ȷ�Ϻ���������
				 *���·�һ��ʱ�����м��㡣
	        	 */
	        	//add by houcq 2011-05-04 begin
	        	 String sql = "select nmonth FROM eh_period WHERE pk_period = '"+pk_period+"' and  pk_corp = '"+ce.getCorporation().getPk_corp()+"' and isnull(dr,0)=0";
	        	 Object ob  = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
	        	 if (ob!=null)
	        	 {
	        		 int month =Integer.parseInt(ob.toString());
	        		 if (month!=_getDate().getMonth())
	        		 {
	        			 getBillUI().showErrorMessage("�����·�������·ݲ�ͳһ���޷����㣬��ȷ�Ϻ�������");
	        			 return;
	        		 }
	        	 }
	        	//end 
				PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
//				StordocVO storVO = (StordocVO) iUAPQueryBS.retrieveByPK(StordocVO.class, pk_store);
				int ret = getBillUI().showYesNoMessage("��ȷ��Ҫ���¼���"+perVO.getNyear()+"��"+perVO.getNmonth()+"��ԭ�Ͽ���±�����");
		        if (ret ==UIDialog.ID_YES){
		        	CalcKcybbVO kcVO = new CalcKcybbVO();
		        	kcVO.setPk_corp(_getCorp().getPk_corp());
		        	kcVO.setPk_period(pk_period);
		        	kcVO.setPk_store(pk_store);
		        	kcVO.setCoperatorid(_getOperator());
		        	kcVO.setCalcdate(_getDate());
		        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		        	pubItf.calcYLKCYBB(kcVO);
		        	//��������ʾ������
		        	String whereSql = "  pk_period = '"+pk_period+"' and invtype = 'Y'  and pk_corp = '"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0 ";
		        	nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
	                SuperVO[] supervo = business.queryByCondition(CalcKcybbVO.class, whereSql);
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

