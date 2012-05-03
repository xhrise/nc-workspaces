package nc.vo.hr.pub.carddef;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * 系统信息项的VO
 * @author wangxing
 *
 */
public class SystemInfoItemVO extends ValueObject {
	
	// 客户端信息项
    	
	public final String[] SYS_INFOITEM_CLIENTTYPE_ITEMNAMES = {
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000124"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000125"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000126"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000127"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000128"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000129"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000130"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000131"),
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000132"),
		// 法人代表,V5.02新加系统信息项
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000146"),
		// 营业地址,V5.02新加系统信息项
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000147")
		
		
//		"单位名称",
//		"用户名称",
//		"登录日期",
//		"服务器日期",
//		"服务器时间",
//		"单位简称",
//		"单位通讯地址",
//		"单位邮编",
//		"单位网址"
//		UPP60133301-000146=法人代表
//		UPP60133301-000147=营业地址
	};
	
	// 信息项索引
	// 未定义
	public static final int SYS_INFOITEM_NONE = -1;
	// 公司名称
	public static final int SYS_INFOITEM_CORPNAME = 0;
	// 用户名称
	public static final int SYS_INFOITEM_USERNAME = 1;
	// 登录日期
	public static final int SYS_INFOITEM_LOGDATE = 2;
	// 服务器日期
	public static final int SYS_INFOITEM_SERVERDATE = 3;
	// 服务器时间
	public static final int SYS_INFOITEM_SERVERTIME = 4;
	// 公司简称
	public static final int SYS_INFOITEM_CORPSHORTNAME = 5;
	// 公司通讯地址
	public static final int SYS_INFOITEM_MAILADDR = 6;
	// 公司邮编
	public static final int SYS_INFOITEM_MAILNO = 7;
	// 公司网址
	public static final int SYS_INFOITEM_URL = 8;
	// 法人代表
	public static final int SYS_INFOITEM_LEGALBODY = 9;
	// 营业地址
	public static final int SYS_INFOITEM_SALEADDR = 10;
	
	// VO 的具体信息
	// 为了形成一棵树，特记录树ＩＤ的字段
	private String itemTreeID = null;
	
	// 信息向索引
	private int itemKey = SYS_INFOITEM_NONE;
	
	// 信息项名称
	private String itemName = null;
	
	// 信息项数据类型，默认为字符串类型
	private int itemDataType = CommonValue.DATATYPE_STRING;
	
	// 值
	private String itemValue = null;
	

	/**
	 * 
	 */
	public String getEntityName() {
	    
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	public void validate() throws ValidationException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the itemDateType
	 */
	public int getItemDataType() {
		return itemDataType;
	}

	/**
	 * @param itemDateType the itemDateType to set
	 */
	public void setItemDataType(int itemDataType) {
		this.itemDataType = itemDataType;
	}

	/**
	 * @return the itemKey
	 */
	public int getItemKey() {
		return itemKey;
	}

	/**
	 * @param itemKey the itemKey to set
	 */
	public void setItemKey(int itemKey) {
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
	 * 重写ｔｏＳｔｒｉｎｇ方法
	 */
	public String toString(){
		return getItemName();
	}

	/**
	 * @return the itemTreeID
	 */
	public String getItemTreeID() {
		return itemTreeID;
	}

	/**
	 * @param itemTreeID the itemTreeID to set
	 */
	public void setItemTreeID(String itemTreeID) {
		this.itemTreeID = itemTreeID;
	}
	
	/**
	 * @return the itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}

	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
}
