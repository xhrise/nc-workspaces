package nc.ui.eh.trade.z0200301;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0200301.PriceBVO;
import nc.vo.eh.trade.z0200301.PriceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.lang.UFDate;

/**�Ƽ۵�����
 * @author ����
 * 2008-03-24 ����04:03:18
 */

public class ClientEventHandler extends AbstractEventHandler {
	
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
 
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.ChooseInv: //������ϸ
			onBoChooseInv();
			break;
		}
		super.onBoElse(intBtn);
	}
//	5.01 by add wm ģ����ѯ
	@SuppressWarnings("unchecked")
	private void onBoChooseInv() {
		PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		QueryDialog dialog = new QueryDialog();
		dialog.showModal();
		String OkorNo=dialog.getOkCanel()==null?"":dialog.getOkCanel();
		if(OkorNo.equals("")||OkorNo.equals("ȡ��")){
			return;
		}
		
		String[] queryConditon=dialog.getPriceVo();
		
		//���ַ�������Ĵ���
		String BM="___________";//11���ո�
    	int intComboBoxt=new Integer(queryConditon[0]).intValue();
    	String code=BM.substring(0, intComboBoxt-1)+queryConditon[1];
		//end
		try {
			//modify by houcq 2011-04-20
			//ArrayList alPrice = pubitf.queryChoose(code,_getCorp().getPrimaryKey());
			ArrayList alPrice = pubitf.queryChoose(code,_getCorp().getPrimaryKey(),_getDate());
			if(alPrice==null || alPrice.size()==0){
				getBillUI().showErrorMessage("û���ҵ���Ӧ������!");
				
			    int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
	            int[] rows=new int[rowcount];
	            for(int i=rowcount - 1;i>=0;i--){
	                rows[i]=i;
	            }
	            getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
	            this.getBillUI().updateUI();
				
				return;
			}else{
				PriceBVO[] bvo = (PriceBVO[]) alPrice.toArray(new PriceBVO[alPrice.size()]);
				 int iRet = getBillUI().showYesNoMessage("������ϸǰҪ��ձ��壬�Ƿ�ȷ����");
				 if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
		            int rowcount=getBillCardPanelWrapper().getBillCardPanel().getRowCount();
		            int[] rows=new int[rowcount];
		            for(int i=rowcount - 1;i>=0;i--){
		                rows[i]=i;
		            }
		            getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rows);
		            this.getBillUI().updateUI();
				 }else{
		            return;
		        }
				 
		        for(int i=0;i<bvo.length;i++){
		            onBoLineAdd();
		            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bvo[i].getPk_invbasdoc(), i, "pk_invbasdoc");
		            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bvo[i].getDef_1(), i, "vcode");
		            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bvo[i].getDef_2(), i, "vinvabsdoc");
		            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bvo[i].getOldprice(), i, "oldprice");
		        }
		        getBillUI().updateUI();  
		        
		        for(int j=0;j<bvo.length;j++){
		        	String[] formual=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("pk_invbasdoc").getLoadFormula();//��ȡ�༭��ʽ
		            getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(j,formual);
		        }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
    public void onBoSave() throws Exception {
        
        AggregatedValueObject aggVO = getBillUI().getVOFromUI();
		// �Էǿ���֤
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
           //Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
        if(res==1){
            getBillUI().showErrorMessage("��Ʒ���ظ���");
            return;
        }
       //ִ�����ڵ�У��
       UFDate  userdate=((PriceVO)aggVO.getParentVO()).getZxdate();
       if(!(userdate.compareTo(_getDate())==0||userdate.after(_getDate()))){
    	   getBillUI().showErrorMessage("ִ������Ӧ�ô��ڵ�������"); 
    	   return;  
       } 
       //��Ч���ڵ�У��
       UFDate  YXdate=((PriceVO)aggVO.getParentVO()).getYxdate();
       if(!(YXdate.compareTo(_getDate())==0||YXdate.after(_getDate()))){
    	   getBillUI().showErrorMessage("��Ч����Ӧ�ô��ڵ�������"); 
    	   return;  
       } 
       //�԰�ť������
       getButtonManager().getButton(IEHButton.ChooseInv).setEnabled(false);
       getBillUI().updateButtonUI();
       super.onBoSave();
       
	}
    
	@Override
	protected void onBoCard() throws Exception {
		super.onBoCard();
		getButtonManager().getButton(IEHButton.ChooseInv).setEnabled(false);
		getBillUI().updateButtonUI();
	}
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		  //�԰�ť�ı༭ChooseInv
		getButtonManager().getButton(IEHButton.ChooseInv).setEnabled(true);
		getBillUI().updateButtonUI();
		
	}
}
