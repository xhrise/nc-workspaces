package nc.vo.ehpta.hq010101;

import java.util.Arrays;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.ehpta.hq010101.EhptaTransportVO;
import nc.vo.ehpta.hq010101.EhptaTransportBVO;	

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO {

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (EhptaTransportBVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (EhptaTransportVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new EhptaTransportBVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((EhptaTransportVO)parent);
	}

}
