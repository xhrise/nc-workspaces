package nc.vo.ehpta.hq010301;

import java.util.Arrays;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.ehpta.hq010301.EhptaHangingpriceVO;

/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO {

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (EhptaHangingpriceVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO(children);
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((EhptaHangingpriceVO)parent);
	}

}
