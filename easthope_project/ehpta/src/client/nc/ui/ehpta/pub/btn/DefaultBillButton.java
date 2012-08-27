package nc.ui.ehpta.pub.btn;

import nc.ui.trade.button.IBillButton;
import nc.vo.trade.button.ButtonVO;

public final class DefaultBillButton {
	
	/**
	 * 停用
	 */
	public static final int DISABLED = 101; 
	
	/**
	 * 启用
	 */
	public static final int ENABLED = 102; 
	
	/**
	 * 附件
	 */
	public static final int DOCUMENT = 103; 
	
	/**
	 * 合同变更
	 */
	public static final int MAKENEWCONTRACT = 104; 
	
	/**
	 * 联查
	 */
	public static final int LINKQUERY = 105;
	
	/**
	 *  贴息维护
	 */
	public static final int Maintain = 106;
	
	/**
	 *  确认
	 */
	public static final int Confirm = 107;
	
	/**
	 *  取消确认
	 */
	public static final int Cancelconfirm = 108;
	
	/**
	 * 到款记录
	 */
	public static final int RECEIVABLE = 110; 
	
	/**
	 * 发货记录
	 */
	public static final int DELIVERY = 111; 
	
	/**
	 * 开票记录
	 */
	public static final int INVOICE = 112; 
	
	/**
	 * 统计 
	 */
	public static final int Statistics = 121; 
	
	/**
	 *  批改
	 */
	public static final int Mark = 122;
	
	/**
	 * 全选
	 */
	public static final int SelAll = 123;
	
	/**
	 * 全消
	 */
	public static final int SelNone = 124;
	
	
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
	
	public static final ButtonVO getMaintainButtonVO() {
		
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Maintain);
		btnVO.setBtnCode("maintain");
		btnVO.setBtnName("贴息维护");
		btnVO.setBtnChinaName("贴息维护");
		
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
		btnVO.setBtnName("统计");
		btnVO.setBtnChinaName("统计");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getMarkButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Mark);
		btnVO.setBtnCode("mark");
		btnVO.setBtnName("批改");
		btnVO.setBtnChinaName("批改");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getSelAllButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(SelAll);
		btnVO.setBtnCode("SelAll");
		btnVO.setBtnName("全选");
		btnVO.setBtnChinaName("全选");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getSelNoneButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(SelNone);
		btnVO.setBtnCode("SelNone");
		btnVO.setBtnName("全消");
		btnVO.setBtnChinaName("全消");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getConfirmButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Confirm);
		btnVO.setBtnCode("Confirm");
		btnVO.setBtnName("确认");
		btnVO.setBtnChinaName("确认");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getCancelconfirmButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Cancelconfirm);
		btnVO.setBtnCode("Cancelconfirm");
		btnVO.setBtnName("取消确认");
		btnVO.setBtnChinaName("取消确认");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
}
