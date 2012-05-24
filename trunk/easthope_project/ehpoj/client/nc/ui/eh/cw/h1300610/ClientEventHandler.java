package nc.ui.eh.cw.h1300610;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.cw.h1300610.ArapSumgzVO;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * ���ܣ������ܶ����
 * ZB07
 * ����:WB
 * ʱ�䣺2008-11-3 10:25:38
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
		 //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //���ù�˾����
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp"); 
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getOperator(), i, "editcoperid"); 
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getDate(), i, "editdate");
         }
        
         super.onBoSave();
         ((ClientUI)getBillUI()).setDefaultData();
	 }

     @Override
	protected void onBoBodyQuery() throws Exception {
            StringBuffer sbWhere = new StringBuffer();
            if(askForQueryCondition(sbWhere)==false) 
                return; 
            String pk_corp = _getCorp().getPrimaryKey();
            SuperVO[] queryVos = queryHeadVOs(sbWhere.toString()+" and (pk_corp = '"+pk_corp+"') ");

            //��Ҫ�����
            getBufferData().clear();
            if (queryVos != null) {
                HYBillVO billVO = new HYBillVO();
                //�������ݵ�����
                billVO.setChildrenVO(queryVos);
                //�������ݵ�����
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //���õ�ǰ��
                getBufferData().setCurrentRow(0);
            } else {
                getBufferData().setCurrentRow(-1);
            }
		    updateBuffer();
        }
        
     @Override
    protected void onBoRefresh() throws Exception {
    	super.onBoRefresh();
    	((ClientUI)getBillUI()).setDefaultData();
    }
     
     /* ���� Javadoc��
      * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
      */
     @Override
	protected void onBoElse(int intBtn) throws Exception {
         switch (intBtn)
         {
             case IEHButton.CALCKCYBB:    //���㹤���ܶ�
                 onBoCalcSumGZ();
                 break;
         }
         super.onBoElse(intBtn);
     }

     /**
      * �����ܶ�ļ���
      * wb
      * 2008-11-3 10:46:37
      */
	private void onBoCalcSumGZ() {
		CalcDialog calcDialog = new CalcDialog();
		calcDialog.showModal();
		String pk_period = CalcDialog.pk_period;				//�ڼ�
		String jsmethod = CalcDialog.jsmethod==null?"":CalcDialog.jsmethod.equals("����")?"0":"1";	//���ʼ��㷽ʽ
        if(pk_period!=null&&pk_period.length()>0){
			IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
				int ret = getBillUI().showYesNoMessage("��ȷ��Ҫ���¼���"+perVO.getNyear()+"��"+perVO.getNmonth()+"�µĹ����ܶ���?");
		        if (ret ==UIDialog.ID_YES){
		        	int nyear = perVO.getNyear();
		    		int nmonth = perVO.getNmonth();
		    		String pk_corp = _getCorp().getPk_corp();
		    		ArapSumgzVO gzvo = new ArapSumgzVO();
		    		gzvo.setNyear(nyear);
		    		gzvo.setNmonth(nmonth);
		    		gzvo.setPk_corp(pk_corp);
		    		gzvo.setDmakedate(_getDate());
		    		gzvo.setCoperatorid(_getOperator());
		    		gzvo.setEditdate(_getDate());
		    		gzvo.setEditcoperid(_getOperator());
		    		gzvo.setMemo(jsmethod);
		    		PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
		    		gzvo = pubItf.calcSumGZ(gzvo);
			    	// ��������ʾ������
			        SuperVO[] vos = new SuperVO[1];
			        vos[0] = gzvo;
	                //��Ҫ�����
	                getBufferData().clear();
	                if (vos != null) {
	                    HYBillVO billVO = new HYBillVO();
	                    //�������ݵ�����
	                    billVO.setChildrenVO(vos);
	                    //�������ݵ�����
	                    if (getBufferData().isVOBufferEmpty()) {
	                        getBufferData().addVOToBuffer(billVO);
	                    } else {
	                        getBufferData().setCurrentVO(billVO);
	                    }

	                    //���õ�ǰ��
	                    getBufferData().setCurrentRow(0);
	                } else {
	                    getBufferData().setCurrentRow(-1);
	                }
				    updateBuffer();
		        }
			}catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
}
