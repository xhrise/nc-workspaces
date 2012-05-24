
package nc.ui.eh.iso.z0500201;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.card.BillCardUI;
import nc.ui.trade.card.CardEventHandler;
import nc.vo.eh.iso.z0500201.BdCheckitemVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

/**
 * ���ܣ������Ŀ��
 * @author ����Դ
 * 2008-04-11 
 */
public class ClientEventHandler extends CardEventHandler {

	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	} 
	 
	 @Override
	protected void onBoSave() throws Exception {
         //�ǿյ���Ч���ж�
         int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();        
         for (int i = 0; i < row; i++) {
             //���ù�˾����
             getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");             
         }
         //����ʱ������Ϊ��
         getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate(); 
         super.onBoSave();
         onBoQuery();
         ((ClientUI)getBillUI()).setDefaultData();
	 }
	 
	 @Override
	protected void onBoQuery() throws Exception {
         SuperVO[] vos = getBusiDelegator().queryByCondition(BdCheckitemVO.class,  "pk_corp='" + _getCorp().getPk_corp() + "' order by itemcode");
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
	}

	/**
	 * ���ܣ���������Ŀ�ѱ�����������ɾ��
	 * ʱ�䣺2010-03-01
	 * ���ߣ���־Զ
	 * */
	protected void onBoLineDel() throws Exception {
		if(this.usedCheckitem()){
			this.getBillUI().showErrorMessage("�ü����Ŀ�ѱ����ò�����ɾ����");
			return;
		}
		super.onBoLineDel();
	}

	protected void onBoDelete() throws Exception {
		if(this.usedCheckitem()){
			this.getBillUI().showErrorMessage("�ü����Ŀ�ѱ����ò�����ɾ����");
			return;
		}
		super.onBoDelete();
	}
	
	/**
	 * ���ܣ��жϼ����Ŀ�ѱ�����������ɾ��
	 * ʱ�䣺2010-03-01
	 * ���ߣ���־Զ
	 * */
	public boolean usedCheckitem(){
		boolean value = false;
		int row = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		String pk_checkitem = this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "pk_checkitem")==null?"":
								 this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(row, "pk_checkitem").toString();
		StringBuffer strSql = new StringBuffer()
		.append(" select pk_project from( ")
		.append(" select pk_project from eh_iso_b where pk_project = '"+pk_checkitem+"' ")
		.append(" union all ")
		.append(" select pk_project From eh_stock_checkreport_b where pk_project = '"+pk_checkitem+"') ");
		IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		ArrayList arr = null;
		try {
			arr = (ArrayList) iUAPQueryBS.executeQuery(strSql.toString(), new MapListProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		if(arr.size()>0){
			value = true;
		}
		return value;
	}
	 
	


}
