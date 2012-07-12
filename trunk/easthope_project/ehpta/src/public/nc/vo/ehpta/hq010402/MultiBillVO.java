/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/

package nc.vo.ehpta.hq010402;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.trade.pub.HYBillVO;
import nc.vo.trade.pub.IExAggVO;

/**
 * 
 * 该类是用于多子表的聚合VO
 * 
 * 创建日期:2012-7-10
 * 
 * @author Your Author Name
 * @version Your Project 1.0
 */
public class MultiBillVO extends HYBillVO implements IExAggVO {

	// 用于装载多子表数据的HashMap
	private HashMap hmChildVOs = new HashMap();

	/**
	 * 返回多个子表的编码 必须与单据模版的页签编码对应 创建日期：2012-7-10
	 * 
	 * @return String[]
	 */
	public String[] getTableCodes() {

		return new String[] { "ehpta_sale_contract_b", "ehpta_aidcust",
				"ehpta_prepolicy" };

	}

	/**
	 * 返回多个子表的中文名称 创建日期：2012-7-10
	 * 
	 * @return String[]
	 */
	public String[] getTableNames() {

		return new String[] { "长单合同基本信息", "辅助客户", "优惠政策", };
	}

	/**
	 * 取得所有子表的所有VO对象 创建日期：2012-7-10
	 * 
	 * @return CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getAllChildrenVO() {

		ArrayList al = new ArrayList();
		for (int i = 0; i < getTableCodes().length; i++) {
			CircularlyAccessibleValueObject[] cvos = getTableVO(getTableCodes()[i]);
			if (cvos != null)
				al.addAll(Arrays.asList(cvos));
		}

		return (SuperVO[]) al.toArray(new SuperVO[0]);
	}

	/**
	 * 返回每个子表的VO数组 创建日期：2012-7-10
	 * 
	 * @return CircularlyAccessibleValueObject[]
	 */
	public CircularlyAccessibleValueObject[] getTableVO(String tableCode) {

		return (CircularlyAccessibleValueObject[]) hmChildVOs.get(tableCode);
	}

	/**
	 * 
	 * 创建日期：2012-7-10
	 * 
	 * @param SuperVO
	 *            item
	 * @param String
	 *            id
	 */
	public void setParentId(SuperVO item, String id) {
	}

	/**
	 * 为特定子表设置VO数据 创建日期：2012-7-10
	 * 
	 * @param String
	 *            tableCode
	 * @para CircularlyAccessibleValueObject[] vos
	 */
	public void setTableVO(String tableCode,
			CircularlyAccessibleValueObject[] vos) {

		hmChildVOs.put(tableCode, vos);
	}

	/**
	 * 缺省的页签编码 创建日期：2012-7-10
	 * 
	 * @return String
	 */
	public String getDefaultTableCode() {

		return getTableCodes()[0];
	}

	/**
	 * 
	 * 创建日期：2012-7-10
	 * 
	 * @param String
	 *            tableCode
	 * @param String
	 *            parentId
	 * @return SuperVO[]
	 */
	public SuperVO[] getChildVOsByParentId(String tableCode, String parentId) {

		return null;
	}

	/**
	 * 
	 * 创建日期：2012-7-10
	 * 
	 * @return HashMap
	 */
	public HashMap getHmEditingVOs() throws Exception {

		return null;
	}

	/**
	 * 
	 * 创建日期:2012-7-10
	 * 
	 * @param SuperVO
	 *            item
	 * @return String
	 */
	public String getParentId(SuperVO item) {

		return null;
	}
}
