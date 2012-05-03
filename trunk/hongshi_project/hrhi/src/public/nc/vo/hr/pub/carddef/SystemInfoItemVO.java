package nc.vo.hr.pub.carddef;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ValidationException;
import nc.vo.pub.ValueObject;

/**
 * ϵͳ��Ϣ���VO
 * @author wangxing
 *
 */
public class SystemInfoItemVO extends ValueObject {
	
	// �ͻ�����Ϣ��
    	
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
		// ���˴���,V5.02�¼�ϵͳ��Ϣ��
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000146"),
		// Ӫҵ��ַ,V5.02�¼�ϵͳ��Ϣ��
		NCLangRes4VoTransl.getNCLangRes().getStrByID(CommonValue.MOUDLE_CODE_CARDDEF, "UPP60070104-000147")
		
		
//		"��λ����",
//		"�û�����",
//		"��¼����",
//		"����������",
//		"������ʱ��",
//		"��λ���",
//		"��λͨѶ��ַ",
//		"��λ�ʱ�",
//		"��λ��ַ"
//		UPP60133301-000146=���˴���
//		UPP60133301-000147=Ӫҵ��ַ
	};
	
	// ��Ϣ������
	// δ����
	public static final int SYS_INFOITEM_NONE = -1;
	// ��˾����
	public static final int SYS_INFOITEM_CORPNAME = 0;
	// �û�����
	public static final int SYS_INFOITEM_USERNAME = 1;
	// ��¼����
	public static final int SYS_INFOITEM_LOGDATE = 2;
	// ����������
	public static final int SYS_INFOITEM_SERVERDATE = 3;
	// ������ʱ��
	public static final int SYS_INFOITEM_SERVERTIME = 4;
	// ��˾���
	public static final int SYS_INFOITEM_CORPSHORTNAME = 5;
	// ��˾ͨѶ��ַ
	public static final int SYS_INFOITEM_MAILADDR = 6;
	// ��˾�ʱ�
	public static final int SYS_INFOITEM_MAILNO = 7;
	// ��˾��ַ
	public static final int SYS_INFOITEM_URL = 8;
	// ���˴���
	public static final int SYS_INFOITEM_LEGALBODY = 9;
	// Ӫҵ��ַ
	public static final int SYS_INFOITEM_SALEADDR = 10;
	
	// VO �ľ�����Ϣ
	// Ϊ���γ�һ�������ؼ�¼���ɣĵ��ֶ�
	private String itemTreeID = null;
	
	// ��Ϣ������
	private int itemKey = SYS_INFOITEM_NONE;
	
	// ��Ϣ������
	private String itemName = null;
	
	// ��Ϣ���������ͣ�Ĭ��Ϊ�ַ�������
	private int itemDataType = CommonValue.DATATYPE_STRING;
	
	// ֵ
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
	 * ��д����ӣ�����緽��
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
