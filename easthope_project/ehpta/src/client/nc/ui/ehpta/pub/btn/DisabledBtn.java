package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public class DisabledBtn {
	
	/**
	 *  ��ť���  ��  101 
	 *  
	 *  ���� �� ͣ��
	 */
	public static Integer NO = 101;
	
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(NO);
		btnVo.setBtnCode("disabled");
		btnVo.setBtnName("ͣ��");
		btnVo.setBtnChinaName("ͣ��");

		btnVo.setChildAry(new int[] {

		});

		return btnVo;
	}
}
