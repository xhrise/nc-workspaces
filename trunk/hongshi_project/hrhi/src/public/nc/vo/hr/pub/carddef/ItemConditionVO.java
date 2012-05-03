package nc.vo.hr.pub.carddef;

import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * ��Ա��Ϣ�Ӽ��������Ƚϣ֣�
 * @author wangxing
 *
 */
public class ItemConditionVO extends ValueObject {
	
	
	/**
	 * ���л�ID
	 */
	private static final long serialVersionUID = 2234819431943717329L;

	// ��������룬��Ӧ���ֶα���
	private String conditionItemCode = null;
	
	// ����������
	private String conditionItemName = null;
	
	// �����Ƚϲ�����
	private int relaOperation = CommonValue.RELAOPER_EQUALS;
	
	// �Աȵ���ֵ 
	private String comparValue = null;
	
	// �ֶε��������ͣ�Ĭ������Ϊ�ַ���
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
	 * �õ�һ��SQL����
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
		
//		 �����������͵õ����ݵķ���
		switch(itemDataType){
		// �ַ����ֶ�
		case CommonValue.DATATYPE_STRING:
		// �߼��ֶ�
		case CommonValue.DATATYPE_BOOLEAN:
		// �����ֶ�
		case CommonValue.DATATYPE_DATE:
		// ���ڹ�ʽ�ֶ�
		case CommonValue.DATATYPE_FORMULADATE:
		// ���ı��ֶ�
		case CommonValue.DATATYPE_MEMO:
		// ʱ���ֶ�
		case CommonValue.DATATYPE_TIME:
		// �����ֶ�
		case CommonValue.DATATYPE_UFREF:{
			conditionBuf.append("'");
			conditionBuf.append(comparValue);
			conditionBuf.append("' ");
			break;
		}
			
		// С���ֶ�
		case CommonValue.DATATYPE_DECIMAL:
		// �����ֶ�
		case CommonValue.DATATYPE_INTEGER:{
			conditionBuf.append(comparValue);
			break;
		}
		
		// ��Ƭ�ֶ�
		case CommonValue.DATATYPE_PICTURE:{
			conditionBuf.append(comparValue);
			break;
		}
		
		// Ĭ��Ϊ�ַ����ֶ�
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
