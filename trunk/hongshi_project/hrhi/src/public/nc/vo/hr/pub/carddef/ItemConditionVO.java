package nc.vo.hr.pub.carddef;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * 人员信息子集的条件比较ＶＯ
 * @author wangxing
 *
 */
public class ItemConditionVO extends ValueObject {
	
	
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 2234819431943717329L;

	// 条件项编码，对应于字段编码
	private String conditionItemCode = null;
	
	// 条件项名称
	private String conditionItemName = null;
	
	// 条件比较操作符
	private int relaOperation = CommonValue.RELAOPER_EQUALS;
	
	// 对比的数值 
	private String comparValue = null;
	
	// 字段的数据类型，默认类型为字符串
	private int itemDataType = CommonValue.DATATYPE_STRING;
	/**
	 * 
	 */
	public String getEntityName() {
		// TODO Auto-generated method stub
		return ItemConditionVO.class.getName();
	}

	/**
	 * 
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the comparValue
	 */
	public String getComparValue() {
		return comparValue;
	}

	/**
	 * @param comparValue the comparValue to set
	 */
	public void setComparValue(String comparValue) {
		this.comparValue = comparValue;
	}

	/**
	 * @return the conditionItemCode
	 */
	public String getConditionItemCode() {
		return conditionItemCode;
	}

	/**
	 * @param conditionItemCode the conditionItemCode to set
	 */
	public void setConditionItemCode(String conditionItemCode) {
		this.conditionItemCode = conditionItemCode;
	}

	/**
	 * @return the conditionItemName
	 */
	public String getConditionItemName() {
		return conditionItemName;
	}

	/**
	 * @param conditionItemName the conditionItemName to set
	 */
	public void setConditionItemName(String conditionItemName) {
		this.conditionItemName = conditionItemName;
	}

	/**
	 * @return the relaOperation
	 */
	public int getRelaOperation() {
		return relaOperation;
	}

	/**
	 * @param relaOperation the relaOperation to set
	 */
	public void setRelaOperation(int relaOperation) {
		this.relaOperation = relaOperation;
	}

	/**
	 * @return the itemDataType
	 */
	public int getItemDataType() {
		return itemDataType;
	}

	/**
	 * @param itemDataType the itemDataType to set
	 */
	public void setItemDataType(int itemDataType) {
		this.itemDataType = itemDataType;
	}

	/**
	 * 得到一个SQL条件
	 * @return
	 */
	public String toSqlCondition(){
		StringBuffer conditionBuf = new StringBuffer();
		conditionBuf.append(" ");
		conditionBuf.append(conditionItemCode);
		switch(relaOperation){
		case CommonValue.RELAOPER_EQUALS:{
			conditionBuf.append(" = ");
			break;
		}
		case CommonValue.RELAOPER_BIGGER:{
			conditionBuf.append(" > ");
			break;
		}
		case CommonValue.RELAOPER_SMALLER:{
			conditionBuf.append(" < ");
			break;
		}
		case CommonValue.RELAOPER_LIKE:{
			conditionBuf.append(" like ");
			break;
		}
		default:{
			conditionBuf.append(" = ");
			break;
		}
		}
		
//		 根据数据类型得到数据的方法
		switch(itemDataType){
		// 字符串字段
		case CommonValue.DATATYPE_STRING:
		// 逻辑字段
		case CommonValue.DATATYPE_BOOLEAN:
		// 日期字段
		case CommonValue.DATATYPE_DATE:
		// 日期公式字段
		case CommonValue.DATATYPE_FORMULADATE:
		// 大文本字段
		case CommonValue.DATATYPE_MEMO:
		// 时间字段
		case CommonValue.DATATYPE_TIME:
		// 参照字段
		case CommonValue.DATATYPE_UFREF:{
			conditionBuf.append("'");
			conditionBuf.append(comparValue);
			conditionBuf.append("' ");
			break;
		}
			
		// 小数字段
		case CommonValue.DATATYPE_DECIMAL:
		// 整型字段
		case CommonValue.DATATYPE_INTEGER:{
			conditionBuf.append(comparValue);
			break;
		}
		
		// 照片字段
		case CommonValue.DATATYPE_PICTURE:{
			conditionBuf.append(comparValue);
			break;
		}
		
		// 默认为字符串字段
		default:{
			conditionBuf.append("'");
			conditionBuf.append(comparValue);
			conditionBuf.append("' ");
			break;
		}
		}// end switch
		conditionBuf.append(" ");
		return conditionBuf.toString();
	}
}
