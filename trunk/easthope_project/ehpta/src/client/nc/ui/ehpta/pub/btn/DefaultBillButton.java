package nc.ui.ehpta.pub.btn;

import nc.ui.trade.button.IBillButton;
import nc.vo.trade.button.ButtonVO;

public final class DefaultBillButton {
	
	/**
	 * ͣ��
	 */
	public static final int DISABLED = 101; 
	
	/**
	 * ����
	 */
	public static final int ENABLED = 102; 
	
	/**
	 * ����
	 */
	public static final int DOCUMENT = 103; 
	
	/**
	 * ��ͬ���
	 */
	public static final int MAKENEWCONTRACT = 104; 
	
	/**
	 * ����
	 */
	public static final int LINKQUERY = 105;
	
	/**
	 *  ��Ϣά��
	 */
	public static final int Maintain = 106;
	
	/**
	 *  ȷ��
	 */
	public static final int Confirm = 107;
	
	/**
	 *  ȡ��ȷ��
	 */
	public static final int Cancelconfirm = 108;
	
	/**
	 * �����¼
	 */
	public static final int RECEIVABLE = 110; 
	
	/**
	 * ������¼
	 */
	public static final int DELIVERY = 111; 
	
	/**
	 * ��Ʊ��¼
	 */
	public static final int INVOICE = 112; 
	
	/**
	 * ͳ�� 
	 */
	public static final int Statistics = 121; 
	
	/**
	 *  ����
	 */
	public static final int Mark = 122;
	
	/**
	 * ȫѡ
	 */
	public static final int SelAll = 123;
	
	/**
	 * ȫ��
	 */
	public static final int SelNone = 124;
	
	
	public static final ButtonVO getDocumentButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(DOCUMENT);
		btnVo.setBtnCode("document");
		btnVo.setBtnName("�ļ�����");
		btnVo.setBtnChinaName("�ļ�����");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getDisabledButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(DISABLED);
		btnVo.setBtnCode("disabled");
		btnVo.setBtnName("ͣ��");
		btnVo.setBtnChinaName("ͣ��");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getEnabledButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ENABLED);
		btnVo.setBtnCode("enabled");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getMakeNewContractButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(MAKENEWCONTRACT);
		btnVo.setBtnCode("makenewcontract");
		btnVo.setBtnName("��ͬ���");
		btnVo.setBtnChinaName("��ͬ���");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getLinkButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(LINKQUERY);
		btnVO.setBtnCode("linkquery");
		btnVO.setBtnName("����");
		btnVO.setBtnChinaName("����");
		
		btnVO.setChildAry(new int[] {RECEIVABLE , DELIVERY , INVOICE});
		
		return btnVO;
	}
	
	public static final ButtonVO getReceivableButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(RECEIVABLE);
		btnVO.setBtnCode("receivable");
		btnVO.setBtnName("�����¼");
		btnVO.setBtnChinaName("�����¼");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getDeliveryButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(DELIVERY);
		btnVO.setBtnCode("delivery");
		btnVO.setBtnName("������¼");
		btnVO.setBtnChinaName("������¼");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getInvoiceButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(INVOICE);
		btnVO.setBtnCode("invoice");
		btnVO.setBtnName("��Ʊ��¼");
		btnVO.setBtnChinaName("��Ʊ��¼");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getMaintainButtonVO() {
		
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Maintain);
		btnVO.setBtnCode("maintain");
		btnVO.setBtnName("��Ϣά��");
		btnVO.setBtnChinaName("��Ϣά��");
		
		btnVO.setChildAry(new int[] {
				DefaultBillButton.Statistics,
				DefaultBillButton.Mark,
				DefaultBillButton.SelAll,
				DefaultBillButton.SelNone,
				
		});
		
		return btnVO;
	}
	
	public static final ButtonVO getStatisticsButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Statistics);
		btnVO.setBtnCode("statistics");
		btnVO.setBtnName("ͳ��");
		btnVO.setBtnChinaName("ͳ��");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getMarkButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Mark);
		btnVO.setBtnCode("mark");
		btnVO.setBtnName("����");
		btnVO.setBtnChinaName("����");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getSelAllButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(SelAll);
		btnVO.setBtnCode("SelAll");
		btnVO.setBtnName("ȫѡ");
		btnVO.setBtnChinaName("ȫѡ");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getSelNoneButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(SelNone);
		btnVO.setBtnCode("SelNone");
		btnVO.setBtnName("ȫ��");
		btnVO.setBtnChinaName("ȫ��");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getConfirmButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Confirm);
		btnVO.setBtnCode("Confirm");
		btnVO.setBtnName("ȷ��");
		btnVO.setBtnChinaName("ȷ��");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getCancelconfirmButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Cancelconfirm);
		btnVO.setBtnCode("Cancelconfirm");
		btnVO.setBtnName("ȡ��ȷ��");
		btnVO.setBtnChinaName("ȡ��ȷ��");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
}
