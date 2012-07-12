package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public final class DefaultBillButton {
	
	public static final int DISABLED = 101;
	public static final int ENABLED = 102;
	public static final int DOCUMENT = 103;
	public static final int MAKENEWCONTRACT = 104;
	
	public static final ButtonVO getDocumentButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(DOCUMENT);
		btnVo.setBtnCode("document");
		btnVo.setBtnName("文件管理");
		btnVo.setBtnChinaName("文件管理");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getDisabledButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(DISABLED);
		btnVo.setBtnCode("disabled");
		btnVo.setBtnName("停用");
		btnVo.setBtnChinaName("停用");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getEnabledButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ENABLED);
		btnVo.setBtnCode("enabled");
		btnVo.setBtnName("启用");
		btnVo.setBtnChinaName("启用");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getMakeNewContractButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(MAKENEWCONTRACT);
		btnVo.setBtnCode("makenewcontract");
		btnVo.setBtnName("合同变更");
		btnVo.setBtnChinaName("合同变更");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
}
