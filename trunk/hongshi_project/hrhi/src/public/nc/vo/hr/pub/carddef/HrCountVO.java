package nc.vo.hr.pub.carddef;

import nc.vo.pub.SuperVO;

/**
 * 自己使用的用来得到Count数据的VO
 * @author wangxing
 *
 */
public class HrCountVO extends SuperVO {
	
	public int count = 0;

	public HrCountVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
}
