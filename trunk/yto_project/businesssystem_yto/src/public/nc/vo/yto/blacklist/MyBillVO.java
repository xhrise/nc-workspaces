package nc.vo.yto.blacklist;

import java.io.Serializable;
import java.util.Arrays;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.trade.pub.HYBillVO;

import nc.vo.yto.blacklist.HiPsndocBadAppHVO;
import nc.vo.yto.blacklist.HiPsndocBadAppBVO;	

/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:Your Create Data
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class  MyBillVO extends HYBillVO implements Serializable{

	/**
	 * 
	 */
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