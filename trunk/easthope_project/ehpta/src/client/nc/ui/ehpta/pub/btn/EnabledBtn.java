package nc.ui.ehpta.pub.btn;

import nc.vo.trade.button.ButtonVO;

public class EnabledBtn {
	/**
	 *  ��ť���  ��  102 
	 *  
	 *  ���� �� ͣ��
	 */
	public static Integer NO = 102;
	
	public ButtonVO getButtonVO() {
		ButtonVO btnVo = new ButtonVO();
		btnVo.setBtnNo(NO);
		btnVo.setBtnCode("enabled");
		btnVo.setBtnName("����");
		btnVo.setBtnChinaName("����");

		btnVo.setChildAry(new int[] {

		});

		return btnVo;
	}
}
