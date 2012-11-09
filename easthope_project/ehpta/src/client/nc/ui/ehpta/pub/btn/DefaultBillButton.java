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
	 *  维护
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
	 *  打开 / 关闭
	 */
	public static final int OpenOrClose = 109;
	
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
	
	/**
	 * 辅助功能
	 */
	public static final int Support = 131;
	
	/**
	 * 推入余额调整单
	 */
	public static final int DataIn = 132;
	
	/**
	 * 取消推入
	 */
	public static final int CancleDataIn = 133;
	
	/**
	 *  结算
	 */
	public static final int Settle = 134;
	
	/**
	 * 反结算
	 */
	public static final int CancleSettle = 135;
	
	public static final ButtonVO getSettleButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(Settle);
		btnVo.setBtnCode("结算");
		btnVo.setBtnName("结算");
		btnVo.setBtnChinaName("结算");

		btnVo.setChildAry(new int[] {  });

		return btnVo;
	}
	
	public static final ButtonVO getCancleSettleButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(CancleSettle);
		btnVo.setBtnCode("反结算");
		btnVo.setBtnName("反结算");
		btnVo.setBtnChinaName("反结算");

		btnVo.setChildAry(new int[] {  });

		return btnVo;
	}
	
	public static final ButtonVO getOpenOrCloseButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(OpenOrClose);
		btnVo.setBtnCode("打开 / 关闭");
		btnVo.setBtnName("打开 / 关闭");
		btnVo.setBtnChinaName("打开 / 关闭");

		btnVo.setChildAry(new int[] {  });

		return btnVo;
	}
	
	public static final ButtonVO getSupportButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(Support);
		btnVo.setBtnCode("辅助功能");
		btnVo.setBtnName("辅助功能");
		btnVo.setBtnChinaName("辅助功能");

		btnVo.setChildAry(new int[] { DataIn , CancleDataIn });

		return btnVo;
	}
	
	public static final ButtonVO getDataInButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(DataIn);
		btnVo.setBtnCode("推入余额调整单");
		btnVo.setBtnName("推入余额调整单");
		btnVo.setBtnChinaName("推入余额调整单");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getCancleDataInButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(CancleDataIn);
		btnVo.setBtnCode("取消推入");
		btnVo.setBtnName("取消推入");
		btnVo.setBtnChinaName("取消推入");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getDocumentButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(DOCUMENT);
		btnVo.setBtnCode("文件管理");
		btnVo.setBtnName("文件管理");
		btnVo.setBtnChinaName("文件管理");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getDisabledButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(DISABLED);
		btnVo.setBtnCode("停用");
		btnVo.setBtnName("停用");
		btnVo.setBtnChinaName("停用");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getEnabledButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(ENABLED);
		btnVo.setBtnCode("启用");
		btnVo.setBtnName("启用");
		btnVo.setBtnChinaName("启用");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getMakeNewContractButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(MAKENEWCONTRACT);
		btnVo.setBtnCode("合同变更");
		btnVo.setBtnName("合同变更");
		btnVo.setBtnChinaName("合同变更");

		btnVo.setChildAry(new int[] { });

		return btnVo;
	}
	
	public static final ButtonVO getLinkButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(LINKQUERY);
		btnVO.setBtnCode("联查");
		btnVO.setBtnName("联查");
		btnVO.setBtnChinaName("联查");
		
		btnVO.setChildAry(new int[] {RECEIVABLE , DELIVERY , INVOICE});
		
		return btnVO;
	}
	
	public static final ButtonVO getReceivableButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(RECEIVABLE);
		btnVO.setBtnCode("到款记录");
		btnVO.setBtnName("到款记录");
		btnVO.setBtnChinaName("到款记录");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getDeliveryButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(DELIVERY);
		btnVO.setBtnCode("发货记录");
		btnVO.setBtnName("发货记录");
		btnVO.setBtnChinaName("发货记录");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getInvoiceButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(INVOICE);
		btnVO.setBtnCode("开票记录");
		btnVO.setBtnName("开票记录");
		btnVO.setBtnChinaName("开票记录");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getMaintainButtonVO() {
		
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Maintain);
		btnVO.setBtnCode("维护");
		btnVO.setBtnName("维护");
		btnVO.setBtnChinaName("维护");
		
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
		btnVO.setBtnCode("统计");
		btnVO.setBtnName("统计");
		btnVO.setBtnChinaName("统计");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getMarkButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Mark);
		btnVO.setBtnCode("批改");
		btnVO.setBtnName("批改");
		btnVO.setBtnChinaName("批改");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getSelAllButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(SelAll);
		btnVO.setBtnCode("全选");
		btnVO.setBtnName("全选");
		btnVO.setBtnChinaName("全选");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getSelNoneButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(SelNone);
		btnVO.setBtnCode("全消");
		btnVO.setBtnName("全消");
		btnVO.setBtnChinaName("全消");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getConfirmButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Confirm);
		btnVO.setBtnCode("确认");
		btnVO.setBtnName("确认");
		btnVO.setBtnChinaName("确认");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
	public static final ButtonVO getCancelconfirmButtonVO() {
		ButtonVO btnVO = new ButtonVO();
		btnVO.setBtnNo(Cancelconfirm);
		btnVO.setBtnCode("取消确认");
		btnVO.setBtnName("取消确认");
		btnVO.setBtnChinaName("取消确认");
		
		btnVO.setChildAry(new int[] {});
		
		return btnVO;
	}
	
}
