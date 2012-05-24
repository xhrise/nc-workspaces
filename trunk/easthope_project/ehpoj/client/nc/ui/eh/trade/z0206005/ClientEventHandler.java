package nc.ui.eh.trade.z0206005;

import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0206005.OrderVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDouble;

/**
 * ���� ���۶���
 * 
 * @author �麣 2008-04-08
 */

public class ClientEventHandler extends AbstractEventHandler {
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}

	@Override
	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
	}

	@Override
	public void onBoSave() throws Exception {
		getBillCardPanelWrapper().getBillCardPanel().getBillData()
				.dataNotNullValidate();
		// Ψһ��У��
		BillModel bm = getBillCardPanelWrapper().getBillCardPanel()
				.getBillModel();

		int res = new PubTools().uniqueCheck(bm,
				new String[] { "pk_invbasdoc" });
		if (res == 1) {
			getBillUI().showErrorMessage("�������ظ���");
			return;
		}

		super.onBoSave();
		setBoEnabled();
	}


	@Override
	protected void onBoQuery() throws Exception {
		super.onBoQuery();
	}

	@Override
	protected void onBoLineDel() throws Exception {
		super.onBoLineDel();
		// �����еı���Ӧ�ս��ϼƺ�д���ͷ�е�Ӧ�տ��ܶ�
		int rows = getBillCardPanelWrapper().getBillCardPanel().getBillTable()
				.getRowCount();
		UFDouble ze = new UFDouble(0); // Ӧ���ܶ�
		UFDouble jgze = new UFDouble(0); // �����ܶ�
		UFDouble firstdiscze = new UFDouble(0); // һ���ۿ��ܶ�
		UFDouble seccountze = new UFDouble(0); // �����ۿ��ܶ�
		for (int i = 0; i < rows; i++) {
			UFDouble bcysje = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "bcysje") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "bcysje").toString());
			UFDouble totalprice = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "totalprice") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "totalprice").toString());
			UFDouble firstdis = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "firstcount") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "firstcount").toString());
			UFDouble scount = new UFDouble(
					getBillCardPanelWrapper().getBillCardPanel()
							.getBodyValueAt(i, "secondcount") == null ? "0"
							: getBillCardPanelWrapper().getBillCardPanel()
									.getBodyValueAt(i, "secondcount")
									.toString());
			seccountze = seccountze.add(scount);
			//add by houcq 2011-03-08 begin
			bcysje=totalprice.sub(firstdis);
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
			//add by houcq 2011-03-08 end
			ze = ze.add(bcysje);
			jgze = jgze.add(totalprice);
			firstdiscze = firstdiscze.add(firstdis);
		}
		try {
			OrderVO orVO = (OrderVO) getBillCardPanelWrapper()
					.getChangedVOFromUI().getParentVO();
			UFDouble lastzk = new UFDouble(orVO.getSecondamount() == null ? "0"
					: orVO.getSecondamount().toString());
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_7",
					lastzk.sub(seccountze)); // �����ۿ���� =
												// �����ۿ۽��(�����ۿ�)-�������ö����ۿ�
			getBillCardPanelWrapper().getBillCardPanel()
					.setHeadItem("yfze", ze);
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_9",
					jgze);
			// getBillCardPanel().setHeadItem("zkye",jgze.multiply(IPubInterface.DISCOUNTRATE));
			// //edit by wb at 2008-7-4 16:16:16
			getBillCardPanelWrapper().getBillCardPanel().setHeadItem("zkye",
					jgze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze)); // ���۶����ۿ�
																					// (���*40%-һ���ۿ��ܶ�)
																					// edit
																					// by
																					// wb
																					// at
																					// 2008-7-10
																					// 12:25:07);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int rowcount = getBillCardPanelWrapper().getBillCardPanel()
				.getBillTable().getRowCount();
		for (int i = 0; i < rowcount; i++) {
			getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null,
					i, "secondcount");
		}
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6", null);
	}
	
	
//	���ð�ť�Ŀ���״̬
    @Override
	public void setBoEnabled() throws Exception {
    	 AggregatedValueObject aggvo=getBillUI().getVOFromUI();
             //��һҳ ��һҳ�İ�ť״̬  add by wb at 2008-6-20 14:30:23
             if(getButtonManager().getButton(IEHButton.Prev)!=null){
 	            if(!getBufferData().hasPrev()){
 	    			getButtonManager().getButton(IEHButton.Prev).setEnabled(false);
 	    		}
 	            else{
 	            	getButtonManager().getButton(IEHButton.Prev).setEnabled(true);
 	            }
 	    		if(!getBufferData().hasNext()){
 	    			getButtonManager().getButton(IEHButton.Next).setEnabled(false);
 	    		}
 	    		else{
 	            	getButtonManager().getButton(IEHButton.Next).setEnabled(true);
 	            }
             }
             getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
             getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
             // ���йرհ�ťʱ�Թرհ�ť�Ŀ��� add by wb at 2008-6-20 14:30:23
             String[] keys = aggvo.getParentVO().getAttributeNames();
             if(keys!=null && keys.length>0){
                 for(int i=0;i<keys.length;i++){
                     if(keys[i].endsWith("lock_flag")){ 
                     	String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
                             getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
                         if(lock_flag.equals("false")){
                         	if(getButtonManager().getButton(IEHButton.LOCKBILL)!=null){
                         		getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(true);
                         	}
                         	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(true);	
                         	}
                         }else{
                         	if(getButtonManager().getButton(IEHButton.LOCKBILL)!=null){
                         		getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(false);
                         	}
                         	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                         		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(false);	//��ҵ������µ�һ����ť��Ϊ���ɲ���
                         	}
                         	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                         }
                         break;
                     }
                 }
             }
            OrderVO orVO = (OrderVO) getBillCardPanelWrapper().getChangedVOFromUI().getParentVO();
         	boolean sc_flag = orVO.getScrw_flag()==null?false:orVO.getScrw_flag().booleanValue();		//������������ʱ�����޸�
         	if(sc_flag){
         		getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
         	}
             
         getBillUI().updateButtonUI();
    }
    
    @Override
    protected void onBoCard() throws Exception {
    	// TODO Auto-generated method stub
    	onBoCard2();
    	setBoEnabled();
    }
    
    @Override
    protected void onBoEdit() throws Exception {
    	OrderVO orVO = (OrderVO) getBillCardPanelWrapper().getChangedVOFromUI().getParentVO();
    	boolean sc_flag = orVO.getScrw_flag()==null?false:orVO.getScrw_flag().booleanValue();		//������������ʱ�����޸�
    	String th_flag = orVO.getTh_flag()==null?"N":orVO.getTh_flag().toString();								//�������ʱ�����޸�
    	if(sc_flag){
    		getBillUI().showErrorMessage("�����۶���������������,�����޸�!");
    		return;
    	}
    	if(th_flag.equals("Y")){
    		getBillUI().showErrorMessage("�����۶��������,�����޸�!");
    		return;
    	}
    	super.onBoEdit();
    }
 
   
}
