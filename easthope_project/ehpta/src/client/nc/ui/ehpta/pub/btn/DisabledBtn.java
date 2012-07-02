package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public class DisabledBtn {
	
	/**
	 *  按钮编号  ：  101 
	 *  
	 *  功能 ： 停用
	 */
	public static Integer NO = 101;
	
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(NO);
		btnVo.setBtnCode("disabled");
		btnVo.setBtnName("停用");
		btnVo.setBtnChinaName("停用");

		btnVo.setChildAry(new int[] {

		});

		return btnVo;
	}
}
