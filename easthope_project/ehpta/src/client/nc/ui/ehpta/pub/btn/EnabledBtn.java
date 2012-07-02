package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public class EnabledBtn {
	/**
	 *  按钮编号  ：  102 
	 *  
	 *  功能 ： 停用
	 */
	public static Integer NO = 102;
	
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(NO);
		btnVo.setBtnCode("enabled");
		btnVo.setBtnName("启用");
		btnVo.setBtnChinaName("启用");

		btnVo.setChildAry(new int[] {

		});

		return btnVo;
	}
}
