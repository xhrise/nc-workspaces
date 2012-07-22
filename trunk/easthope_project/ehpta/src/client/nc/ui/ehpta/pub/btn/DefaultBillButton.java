package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public final class DefaultBillButton {
	
	public static final int DISABLED = 101; // ͣ��
	public static final int ENABLED = 102; // ����
	public static final int DOCUMENT = 103; // ����
	public static final int MAKENEWCONTRACT = 104; // ��ͬ���
	public static final int LINKQUERY = 105; // ����
	
	public static final int RECEIVABLE = 110; //�������¼
	public static final int DELIVERY = 111; // ������¼
	public static final int INVOICE = 112; // ��Ʊ��¼
	
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
	
	
}
