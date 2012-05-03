/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product.                              *
\***************************************************************/

package nc.vo.hi.hi_108;

import nc.vo.pub.*;

/**
 * Chgbilltype的聚合VO类。
 *
 * 创建日期：(2002-3-5)
 * @author：zhonghaijing
 */
public class ChgbilltypeVO extends AggregatedValueObject {

	private ChgbilltypeHeaderVO header = null;
	private ChgbilltypeItemVO[] items = null;

	// 时间戳标示，现在暂未使用：
	long currentTimestamp; // 当前时间戳
	long initialTimestamp; // 从数据库读入时获得的时间戳
/**
 * ChgbilltypeVO 构造子注解。
 */
public ChgbilltypeVO() {
	super();
}
/**
 * <p>获得子表的VO数组。
 * <p>
 * 创建日期：(2002-3-5)
 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public CircularlyAccessibleValueObject[] getChildrenVO() {

	return items;
}
/**
 * <p>获得母表的VO。
 * <p>
 * 创建日期：(2002-3-5)
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 */
public CircularlyAccessibleValueObject getParentVO() {

	return header;
}
/**
 * <p>设置子表的VO数组。
 * <p>
 * 创建日期：(2002-3-5)
 * @param children nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void setChildrenVO(CircularlyAccessibleValueObject[] children) {

	items = (ChgbilltypeItemVO[]) children;
}
/**
 * <p>设置母表的VO。
 * <p>
 * 创建日期：(2002-3-5)
 * @param parent nc.vo.pub.CircularlyAccessibleValueObject
 */
public void setParentVO(CircularlyAccessibleValueObject parent) {

	header = (ChgbilltypeHeaderVO) parent;
}

}