package nc.ui.eh.eh55;

import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;

public class ToolKit {
	
	/**
	 * 单表体的数据不用查询可以直接出来
	 * 作者：王明
	 * 时间：2009年4月27日9:36:33
	 */
	/**
	public void showDataUI(){
		try {
            Class c = Class.forName(getUIControl().getBillVoName()[1]);
            SuperVO[] vos = getBusiDelegator().queryByCondition(c, getBodyWherePart());
            //需要先清空
            getBufferData().clear();
            if (vos != null) {
                HYBillVO billVO = new HYBillVO();
                //加载数据到单据
                billVO.setChildrenVO(vos);
                //加载数据到缓冲
                if (getBufferData().isVOBufferEmpty()) {
                    getBufferData().addVOToBuffer(billVO);
                } else {
                    getBufferData().setCurrentVO(billVO);
                }
                //设置当前行
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
