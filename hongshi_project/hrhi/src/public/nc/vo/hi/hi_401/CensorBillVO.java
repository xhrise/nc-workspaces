package nc.vo.hi.hi_401;

/**
 * �˴���������˵����
 * �������ڣ�(2002-2-1 16:50:53)
 * @author���ﺣ��
 */
public class CensorBillVO extends nc.vo.pub.AggregatedValueObject {
public nc.vo.pub.CircularlyAccessibleValueObject header=null;
	
	
/**
 * BillVO ������ע�⡣
 */
public CensorBillVO() {
	super();
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 */
public nc.vo.pub.CircularlyAccessibleValueObject[] getChildrenVO() {
	return null;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public nc.vo.pub.CircularlyAccessibleValueObject getParentVO() {
	return header;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:36:56)
 * @return nc.vo.pub.ValueObject[]
 */
public void setChildrenVO(nc.vo.pub.CircularlyAccessibleValueObject[] children) {}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:32:28)
 * @return nc.vo.pub.ValueObject
 */
public void setParentVO(nc.vo.pub.CircularlyAccessibleValueObject parent) {
	header=parent;
	
	
	}
}
