package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public final class DefaultBillButton {
	
	public static final int DISABLED = 101; // 停用
	public static final int ENABLED = 102; // 启用
	public static final int DOCUMENT = 103; // 附件
	public static final int MAKENEWCONTRACT = 104; // 合同变更
	public static final int LINKQUERY = 105; // 联查
	
	public static final int RECEIVABLE = 110; //　到款记录
	public static final int DELIVERY = 111; // 发货记录
	public static final int INVOICE = 112; // 开票记录
	
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
	
	public static final ButtonVO getLinkButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(LINKQUERY);
		btnVO.setBtnCode("linkquery");
		btnVO.setBtnName("联查");
		btnVO.setBtnChinaName("联查");
		
		btnVO.setChildAry(new int[] {RECEIVABLE , DELIVERY , INVOICE});
		
		return btnVO;
	}
	
	public static final ButtonVO getReceivableButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(RECEIVABLE);
		btnVO.setBtnCode("receivable");
		btnVO.setBtnName("到款记录");
		btnVO.setBtnChinaName("到款记录");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getDeliveryButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(DELIVERY);
		btnVO.setBtnCode("delivery");
		btnVO.setBtnName("发货记录");
		btnVO.setBtnChinaName("发货记录");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getInvoiceButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(INVOICE);
		btnVO.setBtnCode("invoice");
		btnVO.setBtnName("开票记录");
		btnVO.setBtnChinaName("开票记录");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	
}
