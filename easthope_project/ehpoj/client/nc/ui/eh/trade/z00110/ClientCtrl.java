package nc.ui.eh.trade.z00110;

import nc.ui.eh.pub.IBillType;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.bill.ISingleController;
import nc.ui.trade.businessaction.IBusinessActionType;
import nc.ui.trade.button.IBillButton;
import nc.vo.eh.pub.PubBillVO;
import nc.vo.eh.trade.z00110.MarketareaVO;
/**
 * �����г�
 * @throws Exception
 * @author ����
 * 2008-03-24 ����04:03:18
 */
public class ClientCtrl implements ICardController, ISingleController {

	/**
	 * 
	 */
	public ClientCtrl() {
		// TODO Auto-generated constructor stub
		super();
	}

	/*
	 * (non-Javadoc)
	 * ��ʾ��Ƭ���������
	 * @see nc.ui.trade.bill.ICardController#getCardBodyHideCol()
	 */
	public String[] getCardBodyHideCol() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * ��ʾ��Ƭ��ť
	 * @see nc.ui.trade.bill.ICardController#getCardButtonAry()
	 */
	public int[] getCardButtonAry() {
		// TODO Auto-generated method stub
		return new int[] { 
				IBillButton.Add, 
				IBillButton.Edit, 
				IBillButton.Line,
				IBillButton.Save, 
				IBillButton.Cancel
		};
	}

	/*
	 * (non-Javadoc)
	 *�Ƿ���ʾ����
	 * @see nc.ui.trade.bill.ICardController#isShowCardRowNo()
	 */
	public boolean isShowCardRowNo() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * �Ƿ���ʾ��Ƭ�ܺ�
	 * @see nc.ui.trade.bill.ICardController#isShowCardTotal()
	 */
	public boolean isShowCardTotal() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * ȡ����������
	 * @see nc.ui.trade.controller.IControllerBase#getBillType()
	 */
	public String getBillType() {
		// TODO Auto-generated method stub
		return IBillType.eh_z00110;
	}

	/*
	 * (non-Javadoc)
	 * ȡ��VO
	 * @see nc.ui.trade.controller.IControllerBase#getBillVoName()
	 */
	public String[] getBillVoName() {
		// TODO Auto-generated method stub
		return new String[] { 
				PubBillVO.class.getName(),
				MarketareaVO.class.getName(),
				MarketareaVO.class.getName() };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.controller.IControllerBase#getBodyCondition()
	 */
	public String getBodyCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.controller.IControllerBase#getBodyZYXKey()
	 */
	public String getBodyZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * �Ƿ���ƽ̨
	 * @see nc.ui.trade.controller.IControllerBase#getBusinessActionType()
	 */
	public int getBusinessActionType() {
		// TODO Auto-generated method stub
		return IBusinessActionType.BD;
	}

	/*
	 * (non-Javadoc)
	 * �ӱ�PK
	 * @see nc.ui.trade.controller.IControllerBase#getChildPkField()
	 */
	public String getChildPkField() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.controller.IControllerBase#getHeadZYXKey()
	 */
	public String getHeadZYXKey() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * ȡ�����������
	 * @see nc.ui.trade.controller.IControllerBase#getPkField()
	 */
	public String getPkField() {
		// TODO Auto-generated method stub
		return "pk_marketarea";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.controller.IControllerBase#isEditInGoing()
	 */
	public Boolean isEditInGoing() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * �ı䵥��״̬
	 * @see nc.ui.trade.controller.IControllerBase#isExistBillStatus()
	 */
	public boolean isExistBillStatus() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.controller.IControllerBase#isLoadCardFormula()
	 */
	public boolean isLoadCardFormula() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.ui.trade.bill.ISingleController#isSingleDetail()
	 */
	public boolean isSingleDetail() {
		// TODO Auto-generated method stub
		return true;
	}

}
