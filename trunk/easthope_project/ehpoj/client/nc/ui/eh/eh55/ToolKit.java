package nc.ui.eh.eh55;

import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

public class ToolKit {
	
	/**
	 * ����������ݲ��ò�ѯ����ֱ�ӳ���
	 * ���ߣ�����
	 * ʱ�䣺2009��4��27��9:36:33
	 */
	/**
	public void showDataUI(){
		try {
            Class c = Class.forName(getUIControl().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
            //��Ҫ�����
            getBufferData().clear();
            if (vos != null) {
                HYBillVO billVO = new HYBillVO();
                //�������ݵ�����
                billVO.setChildrenVO(vos);
                //�������ݵ�����
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }
                //���õ�ǰ��
                getBufferData().setCurrentRow(0);
            } else {
                getBufferData().setCurrentRow(-1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	*/

}
