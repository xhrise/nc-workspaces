/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product.                              *
\***************************************************************/

package nc.vo.hi.hi_108;

import java.util.ArrayList;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * �ӡ�
 * 
 * �������ڣ�(2002-3-5)
 * @author��zhonghaijing
 */
public class ChgbilltypeItemVO extends CircularlyAccessibleValueObject {

	public String m_pk_setofchgbill;
	public String m_pk_chgbilltype;
	public String m_pk_setdict;
	public Integer m_dr;
	public UFDateTime m_ts;

	/**
	 *     �����������Ե�FieldObjects����Ҫ����ϵͳ�����У�
	 * ҵ������в����õ������FieldObjects��
	 */
	private static StringField m_pk_setofchgbillField;
	private static StringField m_pk_chgbilltypeField;
	private static StringField m_pk_setdictField;
	private static IntegerField m_drField;
	private static UFDateTimeField m_tsField;
/**
 * ʹ�������ֶν��г�ʼ���Ĺ����ӡ�
 *
 * �������ڣ�(2002-3-5)
 */
public ChgbilltypeItemVO() {

}
/**
 * ʹ���������г�ʼ���Ĺ����ӡ�
 *
 * �������ڣ�(2002-3-5)
 * @param ??fieldNameForMethod?? ����ֵ
 */
public ChgbilltypeItemVO(String newPk_setofchgbill) {

	// Ϊ�����ֶθ�ֵ:
	m_pk_setofchgbill = newPk_setofchgbill;
}
/**
 * ����Object�ķ���,��¡���VO����
 *
 * �������ڣ�(2002-3-5)
 */
public Object clone() {

	// ���ƻ������ݲ������µ�VO����
	Object o = null;
	try {
		o = super.clone();
	} catch (Exception e) {}
	ChgbilltypeItemVO setofchgbill = (ChgbilltypeItemVO)o;

	// �������渴�Ʊ�VO������������ԣ�

	return setofchgbill;
}
/**
 * ������ֵ�������ʾ���ơ�
 *
 * �������ڣ�(2002-3-5)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {

	return "Setofchgbill";
}
/**
 * ���ض����ʶ������Ψһ��λ����
 *
 * �������ڣ�(2002-3-5)
 * @return String
 */
public String getPrimaryKey() {

	return m_pk_setofchgbill;
}
/**
 * ���ö����ʶ������Ψһ��λ����
 *
 * �������ڣ�(2002-3-5)
 * @param m_pk_setofchgbill String 
 */
public void setPrimaryKey(String newPk_setofchgbill) {

	m_pk_setofchgbill = newPk_setofchgbill;
}
/**
 * ����m_pk_setofchgbill��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return String
 */
public String getPk_setofchgbill() {
	return m_pk_setofchgbill;
}
/**
 * ����m_pk_chgbilltype��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return String
 */
public String getPk_chgbilltype() {
	return m_pk_chgbilltype;
}
/**
 * ����m_pk_setdict��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return String
 */
public String getPk_setdict() {
	return m_pk_setdict;
}
/**
 * ����m_dr��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return Integer
 */
public Integer getDr() {
	return m_dr;
}
/**
 * ����m_ts��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return UFDateTime
 */
public UFDateTime getTs() {
	return m_ts;
}
/**
 * ����m_pk_setofchgbill��setter������
 *
 * �������ڣ�(2002-3-5)
 * @param newM_pk_setofchgbill String
 */
public void setPk_setofchgbill(String newPk_setofchgbill) {

	m_pk_setofchgbill = newPk_setofchgbill;
}
/**
 * ����m_pk_chgbilltype��setter������
 *
 * �������ڣ�(2002-3-5)
 * @param newM_pk_chgbilltype String
 */
public void setPk_chgbilltype(String newPk_chgbilltype) {

	m_pk_chgbilltype = newPk_chgbilltype;
}
/**
 * ����m_pk_setdict��setter������
 *
 * �������ڣ�(2002-3-5)
 * @param newM_pk_setdict String
 */
public void setPk_setdict(String newPk_setdict) {

	m_pk_setdict = newPk_setdict;
}
/**
 * ����m_dr��setter������
 *
 * �������ڣ�(2002-3-5)
 * @param newM_dr Integer
 */
public void setDr(Integer newDr) {

	m_dr = newDr;
}
/**
 * ����m_ts��setter������
 *
 * �������ڣ�(2002-3-5)
 * @param newM_ts UFDateTime
 */
public void setTs(UFDateTime newTs) {

	m_ts = newTs;
}
/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 *
 * �������ڣ�(2002-3-5)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws ValidationException {

	ArrayList errFields = new ArrayList(); // errFields record those null fields that cannot be null.
	// ����Ƿ�Ϊ�������յ��ֶθ��˿�ֵ���������Ҫ�޸��������ʾ��Ϣ��
	if (m_pk_setofchgbill == null) {
		errFields.add(new String("m_pk_setofchgbill"));
	}
	// construct the exception message:
	StringBuffer message = new StringBuffer();
//	message.append("�����ֶβ���Ϊ�գ�");
	message.append(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
			"600700", "UPP600700-000346")/* @res "�����ֶβ���Ϊ�գ�" */);
	if (errFields.size() > 0) {
		String[] temp = (String[]) errFields.toArray(new String[0]);
		message.append(temp[0]);
		for ( int i= 1; i < temp.length; i++ ) {
			message.append("��");
			message.append(temp[i]);
		}
		// throw the exception:
		throw new NullFieldException(message.toString());
	}
}
/**
 * <p>��Ҫ��һ��ѭ���з��ʵ����Ե��������顣
 * <p>
 * �������ڣ�(??Date??)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {

	return new String[]{ "pk_chgbilltype", "pk_setdict","setdictname", "dr", "ts" };
}
/**
 *  <p>����һ�����������ַ��������Ե�ֵ��
 *  <p>
 * �������ڣ�(2002-3-5)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {

	if (attributeName.equals("pk_setofchgbill")) {
		return m_pk_setofchgbill;
	} else
		if (attributeName.equals("pk_chgbilltype")) {
			return m_pk_chgbilltype;
		} else
			if (attributeName.equals("pk_setdict")) {
				return m_pk_setdict;
			} else
				if (attributeName.equals("dr")) {
					return m_dr;
				} else
					if (attributeName.equals("ts")) {
						return m_ts;
					} else // add
						if (attributeName.equals("setdictname")) {
							return m_setdictname;
						}
	return null;
}
/**
 *  <p>�Բ���name���͵���������ֵ��
 *  <p>
 * �������ڣ�(2002-3-5)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {

	try {
		if (name.equals("pk_setofchgbill")) {
			m_pk_setofchgbill = (String) value;
		} else
			if (name.equals("pk_chgbilltype")) {
				m_pk_chgbilltype = (String) value;
			} else
				if (name.equals("pk_setdict")) {
					m_pk_setdict = (String) value;
				} else
					if (name.equals("dr")) {
						m_dr = (Integer) value;
					} else
						if (name.equals("ts")) {
							m_ts = (UFDateTime) value;
						} else//add
							if (name.equals("setdictname")) {
								m_setdictname = (String) value;
							}
	} catch (ClassCastException e) {
//		throw new ClassCastException(
//			"setAttributeValue������Ϊ " + name + " ��ֵʱ����ת�����󣡣�ֵ��" + value + "��"); 
		throw new ClassCastException("setAttributeValue" + 
				NCLangRes4VoTransl.getNCLangRes().getStrByID("600700", "UPP600700-000374")/* @res "������Ϊ " */ + 
				name + 
				NCLangRes4VoTransl.getNCLangRes().getStrByID("600700", "UPP600700-000349")/* @res " ��ֵʱ����ת�����󣡣�ֵ��" */ +
				value + "��");
	}
}
/**
 * FieldObject��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_setofchgbillField() {

	if(m_pk_setofchgbillField == null){
		try{
			m_pk_setofchgbillField = new StringField();
			// ���Ե�����
			m_pk_setofchgbillField.setName("pk_setofchgbill");
			// ���Ե�����
			m_pk_setofchgbillField.setLabel("null");
			// �����ӶԱ����Ե�����������

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_setofchgbillField;
}
/**
 * FieldObject��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_chgbilltypeField() {

	if(m_pk_chgbilltypeField == null){
		try{
			m_pk_chgbilltypeField = new StringField();
			// ���Ե�����
			m_pk_chgbilltypeField.setName("pk_chgbilltype");
			// ���Ե�����
			m_pk_chgbilltypeField.setLabel("null");
			// �����ӶԱ����Ե�����������

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_chgbilltypeField;
}
/**
 * FieldObject��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return nc.vo.pub.FieldObject
 */
public static StringField getPk_setdictField() {

	if(m_pk_setdictField == null){
		try{
			m_pk_setdictField = new StringField();
			// ���Ե�����
			m_pk_setdictField.setName("pk_setdict");
			// ���Ե�����
			m_pk_setdictField.setLabel("null");
			// �����ӶԱ����Ե�����������

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_pk_setdictField;
}
/**
 * FieldObject��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return nc.vo.pub.FieldObject
 */
public static IntegerField getDrField() {

	if(m_drField == null){
		try{
			m_drField = new IntegerField();
			// ���Ե�����
			m_drField.setName("dr");
			// ���Ե�����
			m_drField.setLabel("null");
			// �����ӶԱ����Ե�����������

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_drField;
}
/**
 * FieldObject��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return nc.vo.pub.FieldObject
 */
public static UFDateTimeField getTsField() {

	if(m_tsField == null){
		try{
			m_tsField = new UFDateTimeField();
			// ���Ե�����
			m_tsField.setName("ts");
			// ���Ե�����
			m_tsField.setLabel("null");
			// �����ӶԱ����Ե�����������

		}catch(Throwable exception){
			handleException(exception);
		}
	}
	return m_tsField;
}
/**
 * �������ValueObject�������FieldObject����ļ��ϡ�
 *
 * �������ڣ�(2002-3-5)
 * @return nc.vo.pub.FieldObject[]
 */
public FieldObject[] getFields() {

	FieldObject[] fields = {getPk_setofchgbillField(), getPk_chgbilltypeField(), getPk_setdictField(), getDrField(), getTsField()};

	return fields;
}

	//add
	public String m_setdictname;
	// add
	private static StringField m_setdictnameFiled;

/**
 * ����m_setdictname��Getter������
 *
 * �������ڣ�(2002-3-5)
 * @return String
 */
public String getSetdictname() {
	return m_setdictname;
}

/**
 * ����m_setdictname��setter������
 *
 * �������ڣ�(2002-3-5)
 * @param newM_setdictname String
 */
public void setSetdictname(String newSetdictname) {

	m_setdictname = newSetdictname;
}
}