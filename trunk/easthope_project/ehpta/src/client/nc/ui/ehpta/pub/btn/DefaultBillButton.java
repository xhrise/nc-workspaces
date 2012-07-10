package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public final class DefaultBillButton {
	
	public static final int DISABLED = 101;
	public static final int ENABLED = 102;
	public static final int DOCUMENT = 103;
	
	
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
}
