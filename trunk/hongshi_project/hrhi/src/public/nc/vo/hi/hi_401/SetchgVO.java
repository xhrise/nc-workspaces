/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product.                              *
\***************************************************************/

package nc.vo.hi.hi_401;

import nc.vo.pub.*;

/**
 * Setchg�ľۺ�VO�ࡣ
 *
 * �������ڣ�(2002-3-20)
 * @author��tianhb
 */
public class SetchgVO extends AggregatedValueObject {

	private SetchgHeaderVO header = null;
	private SetchgItemVO[] items = null;

	// ʱ�����ʾ��������δʹ�ã�
	long currentTimestamp; // ��ǰʱ���
	long initialTimestamp; // �����ݿ����ʱ��õ�ʱ���
/**
 * SetchgVO ������ע�⡣
 */
public SetchgVO() {
	super();
}
/**
 * <p>����ӱ���VO���顣
 * <p>
 * �������ڣ�(2002-3-20)
 * @return nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public CircularlyAccessibleValueObject[] getChildrenVO() {

	return items;
}
/**
 * <p>���ĸ����VO��
 * <p>
 * �������ڣ�(2002-3-20)
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 */
public CircularlyAccessibleValueObject getParentVO() {

	return header;
}

/**
 * <p>����ĸ����VO��
 * <p>
 * �������ڣ�(2002-3-20)
 * @param parent nc.vo.pub.CircularlyAccessibleValueObject
 */
public void setParentVO(CircularlyAccessibleValueObject parent) {

	header = (SetchgHeaderVO) parent;
}
/**
 * <p>�����ӱ���VO���顣
 * <p>
 * �������ڣ�(2002-3-20)
 * @param children nc.vo.pub.CircularlyAccessibleValueObject[]
 */
public void setChildrenVO(CircularlyAccessibleValueObject[] children) {

	items = (SetchgItemVO[]) children;
}

	public String tablename="";

/**
 * <p>���ĸ����VO��
 * <p>
 * �������ڣ�(2002-3-20)
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 */
public String getTablename() {

	return tablename;
}

/**
 * <p>���ĸ����VO��
 * <p>
 * �������ڣ�(2002-3-20)
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 */
public void setTablename(String newtable) {

	tablename=newtable;
}
}