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
 * 功能：工资总额计算
 * ZB07
 * 作者:WB
 * 时间：2008-11-3 10:25:38
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	 @Override
	protected void onBoSave() throws Exception {
		 //保存时不允许为空
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
         for (int i = 0; i < row; i++) {
             //设置公司编码
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

            //需要先清空
            getBufferData().clear();
            if (queryVos != null) {
                HYBillVO billVO = new HYBillVO();
                //加载数据到单据
                billVO.setChildrenVO(queryVos);
                //加载数据到缓冲
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }

                //设置当前行
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
     
     /* （非 Javadoc）
      * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
      */
     @Override
	protected void onBoElse(int intBtn) throws Exception {
         switch (intBtn)
         {
             case IEHButton.CALCKCYBB:    //计算工资总额
                 onBoCalcSumGZ();
                 break;
         }
         super.onBoElse(intBtn);
     }

     /**
      * 工资总额的计算
      * wb
      * 2008-11-3 10:46:37
      */
	private void onBoCalcSumGZ() {
		CalcDialog calcDialog = new CalcDialog();
		calcDialog.showModal();
		String pk_period = CalcDialog.pk_period;				//期间
		String jsmethod = CalcDialog.jsmethod==null?"":CalcDialog.jsmethod.equals("正常")?"0":"1";	//工资计算方式
        if(pk_period!=null&&pk_period.length()>0){
			IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				PeriodVO perVO = (PeriodVO) iUAPQueryBS.retrieveByPK(PeriodVO.class, pk_period);
				int ret = getBillUI().showYesNoMessage("你确认要重新计算"+perVO.getNyear()+"年"+perVO.getNmonth()+"月的工资总额吗?");
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
			    	// 将数据显示到界面
			        SuperVO[] vos = new SuperVO[1];
			        vos[0] = gzvo;
	                //需要先清空
	                getBufferData().clear();
	                if (vos != null) {
	                    HYBillVO billVO = new HYBillVO();
	                    //加载数据到单据
	                    billVO.setChildrenVO(vos);
	                    //加载数据到缓冲
	                    if (getBufferData().isVOBufferEmpty()) {
	                        getBufferData().addVOToBuffer(billVO);
	                    } else {
	                        getBufferData().setCurrentVO(billVO);
	                    }

	                    //设置当前行
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
