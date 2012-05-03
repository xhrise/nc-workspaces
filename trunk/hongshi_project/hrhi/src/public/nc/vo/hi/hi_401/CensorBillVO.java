package nc.vo.hi.hi_401;

/**
 * 此处插入类型说明。
 * 创建日期：(2002-2-1 16:50:53)
 * @author：田海波
 */
public class CensorBillVO extends nc.vo.pub.AggregatedValueObject {
public nc.vo.pub.CircularlyAccessibleValueObject header=null;
	
	
/**
 * BillVO 构造子注解。
 */
public CensorBillVO() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 */
public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
	return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
	return header;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 */
public void setChildrenVO(nc.vo.pub.CircularlyAccessibleValueObject[] children) {}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
	header=parent;
	
	
	}
}
