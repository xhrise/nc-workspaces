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
 * 本类用来定义一个单元格中所有需要包含的HR报表单元格信息，主要包括，对应的信息项
 * 及其对应的扩展信息
 * @author wangxing
 *
 */
public class HrCellDataVO extends ValueObject {
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 3465678768455234154L;
	// 信息项的名字，这个名字将保存在Cell中，作为键值使用
	public static final String HR_EXT_NAME = "HR_CELL_EXT_FORMAT";
	public static final String HR_FLDCODE_RECORDNUM = "recordnum";
	
	// 单元格归属类型
	// 独立单元格
	public static final int HR_CELL_BELONGTO_SINGLE = 0;
	// 区域单元格
	public static final int HR_CELL_BELONGTO_AREA = 1;
	private int cellBelongTo = HR_CELL_BELONGTO_SINGLE;
	
	// 主集的两个表名：
	public static final String[] PERSONSET_MAIN_TABNAMES = {
		"bd_psndoc",
		"bd_psnbasdoc"
	};
	// 信息项类别
	// 人员信息
	public static final int ITEM_TYPE_PERSON = 1;
	// 系统信息
	public static final int ITEM_TYPE_SYSTEM = 2;
	// 默认为人员信息
	private int itemType = ITEM_TYPE_PERSON;
	
	// 人员信息类别
	// 主集
	public static final int PERSON_TYPE_PARENT = 1;
	// 子集
	public static final int PERSON_TYPE_CHILD = 2;
	// 默认为主集
	private int personType = PERSON_TYPE_PARENT;
	
	// 信息项
	// 信息项名称，如果此信息项是人员信息项
	private String itemName = null;
	// 信息项名称，如果此信息项是人员信息项，itemKey就是真实的字段名
	private String itemKey = null;
	// 如果是数据库的对应字段，则这个属性保存数据库表的名称
	private String itemTable = null;
	// 信息项数据类型，默认为字符型
	private int itemDataType = CommonValue.DATATYPE_STRING;
	
	// 日期格式ID
	private int dateFormatID = 0;
	// 日期格式名称
	private String dateFormatName = null;
	// 日期前缀符
	private String dateFormatPrefix = null;
	// 日期函数
	private int dateFuncID = 0;
	
	// 日期函数名称
	private String dateFuncName = null;
	
	// 子集定位方式，两种，序号定位和条件定位
	// 序号定位
	public static final int SUBSET_LOCATION_ORDER = 1;
	// 条件定位
	public static final int SUBSET_LOCATION_CONDITION = 2;
	// 定位方式，默认为序号定位
	private int subLocation = SUBSET_LOCATION_ORDER;
	// 序号定位子集记录取值方式
	// 最初
	public static final int SUBSET_LOCATION_ORDER_FIRST = 3;
	// 最近
	public static final int SUBSET_LOCATION_ORDER_LAST = 1;
	// 最初第
	public static final int SUBSET_LOCATION_ORDER_FIRSTNO = 2;
	// 最近第
	public static final int SUBSET_LOCATION_ORDER_LASTNO = 0;
	// 序号定位具体取值方式,默认为最近
	private int subSetLocationOrder = SUBSET_LOCATION_ORDER_LAST;
	// 具体条数
	private int subSetLocationOrderCount = 1;
	// 条件定位的WHERE条件
	private ItemConditionVO[] subSetLocationConditionVOs = null;
	
	// 数据承载部分
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
	 * 构造函数
	 *
	 */
	public HrCellDataVO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 构造函数
	 */
	public String getEntityName() {
		// TODO Auto-generated method stub
		return "HrCellDataVO";
	}

	/**
	 * 覆盖父类的方法
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

	/**
	 * 判断是否是子集
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
	 * 标记使用ResultProcessor取数时是取第一个还是最后一个的标记
	 * 
	 * @return true指第一个
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
	 * 生成单个Cell的SQL语句
	 * @param pk_psndoc
	 * @param pk_psnbasdoc
	 * @param pk_corp
	 * @param isPartTime
	 * @return
	 */
	public String toSqlCondition(String pk_psndoc, String pk_psnbasdoc,
			String pk_psndoc_sub, String pk_corp, boolean isPartTime,
			BusinessFuncParser_sql funcParser) throws Exception {
		// 生成SQL头
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
			// 日期型公式要关联多个表查询
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
			
			// 主集SQL就此返回
			return sqlBuf.toString();
		}
			
//		if(subLocation==SUBSET_LOCATION_ORDER){
//			sqlBuf.append(" top ");
//			sqlBuf.append(subSetLocationOrderCount);
//			sqlBuf.append(" ");
//		// 条件定位
//		}else{
//			sqlBuf.append(" top 1 ");
//		// 条件定位
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
		// 设置用户
		// 如果是业务子集
		if(CommonUtils.isPsnBusinessSet(this.itemTable)){
			sqlBuf.append(" pk_psndoc='");
			sqlBuf.append(pk_psndoc);
			sqlBuf.append("' ");
			
		// 如果是普通子集
		}else{
			sqlBuf.append(" pk_psnbasdoc='");
			sqlBuf.append(pk_psnbasdoc);
			sqlBuf.append("' ");
		}
		
		// 特殊处理
		if (this.itemTable.equalsIgnoreCase("hi_psndoc_part")) {
			sqlBuf.append(" and jobtype<>0 ");
		} else if (this.itemTable.equalsIgnoreCase("hi_psndoc_deptchg")) {
			sqlBuf.append(" and jobtype=0 ");
		} else if (this.itemTable.equalsIgnoreCase("hi_psndoc_training")) {
			sqlBuf.append(" and approveflag = 2 ");
		} else if (this.itemTable.equalsIgnoreCase("hi_psndoc_ctrt")) {
			sqlBuf.append(" and isrefer = 'Y' ");
		}
		
		// 序号定位
		if(subLocation==SUBSET_LOCATION_ORDER){
			switch(subSetLocationOrder){
				// 最初第几条,从HR_FLDCODE_RECORDNUM=最大值开始数，取第几条记录
				case SUBSET_LOCATION_ORDER_FIRSTNO:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" desc ");
					break;
				}
				//最初几条, 从HR_FLDCODE_RECORDNUM=最大值 开始数，取几条记录
				case SUBSET_LOCATION_ORDER_FIRST:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" desc ");
					break;
				}
				//最近第几条, 从HR_FLDCODE_RECORDNUM=0开始数，取第几条记录
				case SUBSET_LOCATION_ORDER_LASTNO:{
					sqlBuf.append(" order by ");
					sqlBuf.append(HR_FLDCODE_RECORDNUM);
					sqlBuf.append(" asc ");
					break;
				}
				// 最近几条,从HR_FLDCODE_RECORDNUM=0开始数，取几条记录
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
		// 条件定位
		}else{
			// 判断条件是否为空
			if(this.subSetLocationConditionVOs!=null && this.subSetLocationConditionVOs.length>0){
				for(int i=0; i<this.subSetLocationConditionVOs.length; i++){
					sqlBuf.append(" and ");
					sqlBuf.append(subSetLocationConditionVOs[i].toSqlCondition());
				}//end if
			}//end if
			// 遍历得到条件，并加入SQL语句中
			sqlBuf.append(" order by ");
			sqlBuf.append(HR_FLDCODE_RECORDNUM);
			sqlBuf.append(" asc ");
		}//end if
		
		return sqlBuf.toString();
	}

	/**
	 * 重写克隆方法
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
		// 具体处理各种数据类型
			// 逻辑类型
		else if(CommonValue.DATATYPE_BOOLEAN==this.itemDataType){
			vo.setValue(new UFBoolean(((UFBoolean)value).booleanValue()));
			
			// 日期类型
		}else if(CommonValue.DATATYPE_DATE==this.itemDataType){
			vo.setValue(((UFDate)value).clone());
			
			// 小数类型
		}else if(CommonValue.DATATYPE_DECIMAL==this.itemDataType){
			vo.setValue(new UFDouble(((UFDouble)value).doubleValue()));
			
			// 日期公式
		}else if(CommonValue.DATATYPE_FORMULADATE==this.itemDataType){
			vo.setValue((String)value);
			
			// 整数
		}else if(CommonValue.DATATYPE_INTEGER==this.itemDataType){
			vo.setValue(new Integer(((Integer)value)).intValue());
			
			// 备注，大文本
		}else if(CommonValue.DATATYPE_MEMO==this.itemDataType){
			vo.setValue((String)value);
			
			// 照片，byte[]
		}else if(CommonValue.DATATYPE_PICTURE==this.itemDataType){
			byte[] src = (byte[])value;
			byte[] dest = new byte[src.length];
			System.arraycopy(src, 0, dest, 0, dest.length);
			vo.setValue(dest);
			
			// 字符串
		}else if(CommonValue.DATATYPE_STRING==this.itemDataType){
			vo.setValue((String)value);
			
			// 时间戳
		}else if(CommonValue.DATATYPE_TIME==this.itemDataType){
//			vo.setValue(((UFDateTime)value).clone());
			vo.setValue((String)value);

			// 参照
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
