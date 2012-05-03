package nc.vo.hr.pub.carddef;

import nc.vo.hi.pub.CommonValue;
import nc.vo.hr.formulaset.BusinessFuncParser_sql;
import nc.vo.hr.global.GlobalTool;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ������������һ����Ԫ����������Ҫ������HR����Ԫ����Ϣ����Ҫ��������Ӧ����Ϣ��
 * �����Ӧ����չ��Ϣ
 * @author wangxing
 *
 */
public class HrCellDataVO extends ValueObject {
	/**
	 * ���л�ID
	 */
	private static final long serialVersionUID = 3465678768455234154L;
	// ��Ϣ������֣�������ֽ�������Cell�У���Ϊ��ֵʹ��
	public static final String HR_EXT_NAME = "HR_CELL_EXT_FORMAT";
	public static final String HR_FLDCODE_RECORDNUM = "recordnum";
	
	// ��Ԫ���������
	// ������Ԫ��
	public static final int HR_CELL_BELONGTO_SINGLE = 0;
	// ����Ԫ��
	public static final int HR_CELL_BELONGTO_AREA = 1;
	private int cellBelongTo = HR_CELL_BELONGTO_SINGLE;
	
	// ����������������
	public static final String[] PERSONSET_MAIN_TABNAMES = {
		"bd_psndoc",
		"bd_psnbasdoc"
	};
	// ��Ϣ�����
	// ��Ա��Ϣ
	public static final int ITEM_TYPE_PERSON = 1;
	// ϵͳ��Ϣ
	public static final int ITEM_TYPE_SYSTEM = 2;
	// Ĭ��Ϊ��Ա��Ϣ
	private int itemType = ITEM_TYPE_PERSON;
	
	// ��Ա��Ϣ���
	// ����
	public static final int PERSON_TYPE_PARENT = 1;
	// �Ӽ�
	public static final int PERSON_TYPE_CHILD = 2;
	// Ĭ��Ϊ����
	private int personType = PERSON_TYPE_PARENT;
	
	// ��Ϣ��
	// ��Ϣ�����ƣ��������Ϣ������Ա��Ϣ��
	private String itemName = null;
	// ��Ϣ�����ƣ��������Ϣ������Ա��Ϣ�itemKey������ʵ���ֶ���
	private String itemKey = null;
	// ��������ݿ�Ķ�Ӧ�ֶΣ���������Ա������ݿ�������
	private String itemTable = null;
	// ��Ϣ���������ͣ�Ĭ��Ϊ�ַ���
	private int itemDataType = CommonValue.DATATYPE_STRING;
	
	// ���ڸ�ʽID
	private int dateFormatID = 0;
	// ���ڸ�ʽ����
	private String dateFormatName = null;
	// ����ǰ׺��
	private String dateFormatPrefix = null;
	// ���ں���
	private int dateFuncID = 0;
	
	// ���ں�������
	private String dateFuncName = null;
	
	// �Ӽ���λ��ʽ�����֣���Ŷ�λ��������λ
	// ��Ŷ�λ
	public static final int SUBSET_LOCATION_ORDER = 1;
	// ������λ
	public static final int SUBSET_LOCATION_CONDITION = 2;
	// ��λ��ʽ��Ĭ��Ϊ��Ŷ�λ
	private int subLocation = SUBSET_LOCATION_ORDER;
	// ��Ŷ�λ�Ӽ���¼ȡֵ��ʽ
	// ���
	public static final int SUBSET_LOCATION_ORDER_FIRST = 3;
	// ���
	public static final int SUBSET_LOCATION_ORDER_LAST = 1;
	// �����
	public static final int SUBSET_LOCATION_ORDER_FIRSTNO = 2;
	// �����
	public static final int SUBSET_LOCATION_ORDER_LASTNO = 0;
	// ��Ŷ�λ����ȡֵ��ʽ,Ĭ��Ϊ���
	private int subSetLocationOrder = SUBSET_LOCATION_ORDER_LAST;
	// ��������
	private int subSetLocationOrderCount = 1;
	// ������λ��WHERE����
	private ItemConditionVO[] subSetLocationConditionVOs = null;
	
	// ���ݳ��ز���
	private String refType = null;
	private int cellRow = -1;
	private int cellCol = -1;
	private Object value = null; 

	/**
	 * @return the cellCol
	 */
	public int getCellCol() {
		return cellCol;
	}

	/**
	 * @param cellCol the cellCol to set
	 */
	public void setCellCol(int cellCol) {
		this.cellCol = cellCol;
	}

	/**
	 * @return the cellRow
	 */
	public int getCellRow() {
		return cellRow;
	}

	/**
	 * @param cellRow the cellRow to set
	 */
	public void setCellRow(int cellRow) {
		this.cellRow = cellRow;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * ���캯��
	 *
	 */
	public HrCellDataVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * ���캯��
	 */
	public String getEntityName() {
		// TODO Auto-generated method stub
		return "HrCellDataVO";
	}

	/**
	 * ���Ǹ���ķ���
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

	/**
	 * �ж��Ƿ����Ӽ�
	 * @return
	 */
	public boolean isMainset(){
		for(int i=0; i<PERSONSET_MAIN_TABNAMES.length; i++){
			if(PERSONSET_MAIN_TABNAMES[i].equals(this.getItemTable())){
				return true;
			}//end if
		}//end for
		return false;
	}
	/**
	 * @return the dateFormatID
	 */
	public int getDateFormatID() {
		return dateFormatID;
	}

	/**
	 * @param dateFormatID the dateFormatID to set
	 */
	public void setDateFormatID(int dateFormatID) {
		this.dateFormatID = dateFormatID;
	}

	/**
	 * @return the dateFormatName
	 */
	public String getDateFormatName() {
		return dateFormatName;
	}

	/**
	 * @param dateFormatName the dateFormatName to set
	 */
	public void setDateFormatName(String dateFormatName) {
		this.dateFormatName = dateFormatName;
	}

	/**
	 * @return the dateFuncID
	 */
	public int getDateFuncID() {
		return dateFuncID;
	}

	/**
	 * @param dateFuncID the dateFuncID to set
	 */
	public void setDateFuncID(int dateFuncID) {
		this.dateFuncID = dateFuncID;
	}

	/**
	 * @return the dateFuncName
	 */
	public String getDateFuncName() {
		return dateFuncName;
	}

	/**
	 * @param dateFuncName the dateFuncName to set
	 */
	public void setDateFuncName(String dateFuncName) {
		this.dateFuncName = dateFuncName;
	}

	/**
	 * @return the itemKey
	 */
	public String getItemKey() {
		return itemKey;
	}

	/**
	 * @param itemKey the itemKey to set
	 */
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemTable
	 */
	public String getItemTable() {
		return itemTable;
	}

	/**
	 * @param itemTable the itemTable to set
	 */
	public void setItemTable(String itemTable) {
		this.itemTable = itemTable;
	}

	/**
	 * @return the itemType
	 */
	public int getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the personType
	 */
	public int getPersonType() {
		return personType;
	}

	/**
	 * @param personType the personType to set
	 */
	public void setPersonType(int personType) {
		this.personType = personType;
	}

	/**
	 * @return the subLocation
	 */
	public int getSubLocation() {
		return subLocation;
	}

	/**
	 * @param subLocation the subLocation to set
	 */
	public void setSubLocation(int subLocation) {
		this.subLocation = subLocation;
	}

	/**
	 * @return the subSetLocationCondition
	 */
	public ItemConditionVO[] getSubSetLocationCondition() {
		return subSetLocationConditionVOs;
	}

	/**
	 * @param subSetLocationCondition the subSetLocationCondition to set
	 */
	public void setSubSetLocationCondition(ItemConditionVO[] vos) {
		this.subSetLocationConditionVOs = vos;
	}

	/**
	 * @return the subSetLocationOrder
	 */
	public int getSubSetLocationOrder() {
		return subSetLocationOrder;
	}

	/**
	 * @param subSetLocationOrder the subSetLocationOrder to set
	 */
	public void setSubSetLocationOrder(int subSetLocationOrder) {
		this.subSetLocationOrder = subSetLocationOrder;
	}

	/**
	 * @return the subSetLocationOrderCount
	 */
	public int getSubSetLocationOrderCount() {
		return subSetLocationOrderCount;
	}

	/**
	 * @param subSetLocationOrderCount the subSetLocationOrderCount to set
	 */
	public void setSubSetLocationOrderCount(int subSetLocationOrderCount) {
		this.subSetLocationOrderCount = subSetLocationOrderCount;
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
	 * @return the dateFormatPrefix
	 */
	public String getDateFormatPrefix() {
		return dateFormatPrefix;
	}

	/**
	 * @param dateFormatPrefix the dateFormatPrefix to set
	 */
	public void setDateFormatPrefix(String dateFormatPrefix) {
		this.dateFormatPrefix = dateFormatPrefix;
	}
	
	/**
	 * ���ʹ��ResultProcessorȡ��ʱ��ȡ��һ���������һ���ı��
	 * 
	 * @return trueָ��һ��
	 */
	public boolean isAscFirst(){
		switch(subSetLocationOrder){
		case SUBSET_LOCATION_ORDER_FIRSTNO:{
			return false;
		}
		case SUBSET_LOCATION_ORDER_FIRST:{
			return true;
		}
		case SUBSET_LOCATION_ORDER_LASTNO:{
			return false;
		}
		case SUBSET_LOCATION_ORDER_LAST:{
			return true;
		}
		default:{
			return true;
		}
		}
	}
	
	
	/**
	 * ���ɵ���Cell��SQL���
	 * @param pk_psndoc
	 * @param pk_psnbasdoc
	 * @param pk_corp
	 * @param isPartTime
	 * @return
	 */
	public String toSqlCondition(String pk_psndoc, String pk_psnbasdoc,
			String pk_psndoc_sub, String pk_corp, boolean isPartTime,
			BusinessFuncParser_sql funcParser) throws Exception {
		// ����SQLͷ
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" select ");
		if(isMainset()){
			if (this.itemKey.indexOf("UFAGE[") >= 0) {
				String trueFields = nc.vo.hr.global.DateFormulaParse
						.proDateFormula(this.itemKey, this.itemTable);
				sqlBuf.append(trueFields);
			} else if (this.itemKey.indexOf(CommonValue.UFFORMULA_DATA) >= 0) {
				String trueFields = GlobalTool.proDateFormulaSql(this.itemKey,
						this.itemTable, funcParser);
				sqlBuf.append(trueFields);
			} else
				sqlBuf.append(this.itemKey);
			sqlBuf.append(" from ");
			// �����͹�ʽҪ����������ѯ
			if (this.itemKey.indexOf(CommonValue.UFFORMULA_DATA) >= 0) {
				sqlBuf.append(" bd_psndoc inner join bd_psnbasdoc on bd_psndoc.pk_psnbasdoc = bd_psnbasdoc.pk_psnbasdoc ");
			} else {
				sqlBuf.append(this.itemTable);
			}
			sqlBuf.append(" where ");
			if (this.itemKey.indexOf(CommonValue.UFFORMULA_DATA) >= 0) {
				sqlBuf.append(" bd_psndoc.pk_psndoc = '");
				sqlBuf.append(pk_psndoc);
				sqlBuf.append("' ");
			} else if (PERSONSET_MAIN_TABNAMES[0].equals(this.itemTable)) {
				sqlBuf.append(" pk_psndoc = '");
				sqlBuf.append(pk_psndoc);
				sqlBuf.append("' ");
			} else {
				sqlBuf.append(" pk_psnbasdoc='");
				sqlBuf.append(pk_psnbasdoc);
				sqlBuf.append("' ");
			}
			
			// ����SQL�ʹ˷���
			return sqlBuf.toString();
		}
			
//		if(subLocation==SUBSET_LOCATION_ORDER){
//			sqlBuf.append(" top ");
//			sqlBuf.append(subSetLocationOrderCount);
//			sqlBuf.append(" ");
//		// ������λ
//		}else{
//			sqlBuf.append(" top 1 ");
//		// ������λ
//		}//end if
		
		if (this.itemKey.indexOf("UFAGE[") >= 0) {
			String trueFields = nc.vo.hr.global.DateFormulaParse
					.proDateFormula(this.itemKey, this.itemTable);
			sqlBuf.append(trueFields);
		} else if (this.itemKey.indexOf(CommonValue.UFFORMULA_DATA) >= 0) {
			String trueFields = GlobalTool.proDateFormulaSql(this.itemKey,
					this.itemTable, funcParser);
			sqlBuf.append(trueFields);
		}else
			sqlBuf.append(this.itemKey);
		
		sqlBuf.append(" from ");
		sqlBuf.append(this.itemTable.equalsIgnoreCase("hi_psndoc_part")?"hi_psndoc_deptchg":this.itemTable);
		sqlBuf.append(" where ");
		// �����û�
		// �����ҵ���Ӽ�
		if(CommonUtils.isPsnBusinessSet(this.itemTable)){
			sqlBuf.append(" pk_psndoc='");
			sqlBuf.append(pk_psndoc);
			sqlBuf.append("' ");
			
		// �������ͨ�Ӽ�
		}else{
			sqlBuf.append(" pk_psnbasdoc='");
			sqlBuf.append(pk_psnbasdoc);
			sqlBuf.append("' ");
		}
		
		// ���⴦��
		if (this.itemTable.equalsIgnoreCase("hi_psndoc_part")) {
			sqlBuf.append(" and jobtype<>0 ");
		} else if (this.itemTable.equalsIgnoreCase("hi_psndoc_deptchg")) {
			sqlBuf.append(" and jobtype=0 ");
		} else if (this.itemTable.equalsIgnoreCase("hi_psndoc_training")) {
			sqlBuf.append(" and approveflag = 2 ");
		} else if (this.itemTable.equalsIgnoreCase("hi_psndoc_ctrt")) {
			sqlBuf.append(" and isrefer = 'Y' ");
		}
		
		// ��Ŷ�λ
		if(subLocation==SUBSET_LOCATION_ORDER){
			switch(subSetLocationOrder){
				// ����ڼ���,��HR_FLDCODE_RECORDNUM=���ֵ��ʼ����ȡ�ڼ�����¼
				case SUBSET_LOCATION_ORDER_FIRSTNO:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" desc ");
					break;
				}
				//�������, ��HR_FLDCODE_RECORDNUM=���ֵ ��ʼ����ȡ������¼
				case SUBSET_LOCATION_ORDER_FIRST:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" desc ");
					break;
				}
				//����ڼ���, ��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ�ڼ�����¼
				case SUBSET_LOCATION_ORDER_LASTNO:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" asc ");
					break;
				}
				// �������,��HR_FLDCODE_RECORDNUM=0��ʼ����ȡ������¼
				case SUBSET_LOCATION_ORDER_LAST:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" asc ");
					break;
				}
				default:{
					break;
				}
			}
		// ������λ
		}else{
			// �ж������Ƿ�Ϊ��
			if(this.subSetLocationConditionVOs!=null && this.subSetLocationConditionVOs.length>0){
				for(int i=0; i<this.subSetLocationConditionVOs.length; i++){
					sqlBuf.append(" and ");
					sqlBuf.append(subSetLocationConditionVOs[i].toSqlCondition());
				}//end if
			}//end if
			// �����õ�������������SQL�����
			sqlBuf.append(" order by ");
			sqlBuf.append(HR_FLDCODE_RECORDNUM);
			sqlBuf.append(" asc ");
		}//end if
		
		return sqlBuf.toString();
	}

	/**
	 * ��д��¡����
	 */
	public Object clone() {
		HrCellDataVO vo = new HrCellDataVO();
		
		vo.setDateFormatID(dateFormatID);
		vo.setPersonType(personType);
		vo.setItemType(itemType);
		vo.setItemName(itemName);
		vo.setItemKey(itemKey);
		vo.setItemTable(itemTable);
		vo.setItemDataType(itemDataType);
		vo.setDateFormatID(dateFormatID);
		vo.setDateFormatName(dateFormatName);
		vo.setDateFormatPrefix(dateFormatPrefix);
		vo.setDateFuncID(dateFuncID);
		vo.setDateFuncName(dateFuncName);
		vo.setSubLocation(subLocation);
		vo.setSubSetLocationOrder(subSetLocationOrder);
		vo.setSubSetLocationOrderCount(subSetLocationOrderCount);
		ItemConditionVO[] vos = null;
		if(subSetLocationConditionVOs!=null && subSetLocationConditionVOs.length>0){
			vos = new ItemConditionVO[subSetLocationConditionVOs.length];
			for(int i=0; i<vos.length; i++){
				vos[i] = subSetLocationConditionVOs[i]==null?null:(ItemConditionVO)subSetLocationConditionVOs[i].clone();
			}
		}
		vo.setSubSetLocationCondition(vos);
		vo.setCellRow(cellRow);
		vo.setCellCol(cellCol);
		vo.setRefType(refType);
		
		if(value==null){
			vo.setValue(null);
		}
		// ���崦�������������
			// �߼�����
		else if(CommonValue.DATATYPE_BOOLEAN==this.itemDataType){
			vo.setValue(new UFBoolean(((UFBoolean)value).booleanValue()));
			
			// ��������
		}else if(CommonValue.DATATYPE_DATE==this.itemDataType){
			vo.setValue(((UFDate)value).clone());
			
			// С������
		}else if(CommonValue.DATATYPE_DECIMAL==this.itemDataType){
			vo.setValue(new UFDouble(((UFDouble)value).doubleValue()));
			
			// ���ڹ�ʽ
		}else if(CommonValue.DATATYPE_FORMULADATE==this.itemDataType){
			vo.setValue((String)value);
			
			// ����
		}else if(CommonValue.DATATYPE_INTEGER==this.itemDataType){
			vo.setValue(new Integer(((Integer)value)).intValue());
			
			// ��ע�����ı�
		}else if(CommonValue.DATATYPE_MEMO==this.itemDataType){
			vo.setValue((String)value);
			
			// ��Ƭ��byte[]
		}else if(CommonValue.DATATYPE_PICTURE==this.itemDataType){
			byte[] src = (byte[])value;
			byte[] dest = new byte[src.length];
			System.arraycopy(src, 0, dest, 0, dest.length);
			vo.setValue(dest);
			
			// �ַ���
		}else if(CommonValue.DATATYPE_STRING==this.itemDataType){
			vo.setValue((String)value);
			
			// ʱ���
		}else if(CommonValue.DATATYPE_TIME==this.itemDataType){
//			vo.setValue(((UFDateTime)value).clone());
			vo.setValue((String)value);

			// ����
		}else if(CommonValue.DATATYPE_UFREF==this.itemDataType){
			vo.setValue((String)value);
		}
		
		// TODO Auto-generated method stub
		return vo;
	}

	/**
	 * @return the cellBelongTo
	 */
	public int getCellBelongTo() {
		return cellBelongTo;
	}

	/**
	 * @param cellBelongTo the cellBelongTo to set
	 */
	public void setCellBelongTo(int cellBelongTo) {
		this.cellBelongTo = cellBelongTo;
	}

	/**
	 * @return the refType
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * @param refType the refType to set
	 */
	public void setRefType(String refType) {
		this.refType = refType;
	}
	public String toString(){
		return getItemName()+"("+getItemKey()+")="+(getValue()==null?"":getValue().toString());
	}
	
}
