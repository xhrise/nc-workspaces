package nc.vo.yto.blacklist;

import java.util.Arrays;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.yto.blacklist.HiPsndocBadAppHVO;
import nc.vo.yto.blacklist.HiPsndocBadAppBVO;	

/**
 * 
 * 单子表/单表头/单表体聚合VO
 *
 * 创建日期:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO {

	private static final long serialVersionUID = 6435007046765840249L;

	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return (HiPsndocBadAppBVO[]) super.getChildrenVO();
	}

	public CircularlyAccessibleValueObject getParentVO() {
		return (HiPsndocBadAppHVO) super.getParentVO();
	}

	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		if( children == null || children.length == 0 ){
			super.setChildrenVO(null);
		}
		else{
			super.setChildrenVO((CircularlyAccessibleValueObject[]) Arrays.asList(children).toArray(new HiPsndocBadAppBVO[0]));
		}
	}

	public void setParentVO(CircularlyAccessibleValueObject parent) {
		super.setParentVO((HiPsndocBadAppHVO)parent);
	}

}
