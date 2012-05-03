package nc.vo.hr.alert;

/**
 * �˴���������������
 * �������ڣ�(2003-1-22 13:20:11)
 * @author��zhanglf
 */
import java.util.*;
import nc.vo.pub.CircularlyAccessibleValueObject;
//import nc.vo.pub.lang.*;
public class POIDataVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	//��¼�������ݵ�Object��ʽ����
	private Hashtable ht= new Hashtable();
	private ArrayList al = null;
	private Vector vec = new Vector();
	public String sKeyofFreeVO= "keyoffreevo";
	public String m_sFieldNames[] = null;
/**
 * POIDataVO ������ע�⡣
 */
public POIDataVO() {
	super();
}
/**
 * POIDataVO ������ע�⡣
 */
public POIDataVO(Hashtable initHT) {
	super();
	ht = initHT;
}
/**
 * POIDataVO ������ע�⡣
 */
public POIDataVO(Hashtable initHT ,Vector initVec) {
	super();
	ht = initHT;
	vec = initVec;
}
/**
 * �˴����뷽��˵����
 * ���ܣ�
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-8-29 22:30:01)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return nc.vo.dm.pub.DMVO
 */
public Object clone() {
	POIDataVO dmdvo= new POIDataVO();
	String[] sNames= getAttributeNames();
	if (null != sNames) {
		for (int i= 0; i < sNames.length; i++) {
			dmdvo.setAttributeValue(sNames[i], getAttributeValue(sNames[i]));
		}
	}
	dmdvo.setStatus(getStatus());
	return dmdvo;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-30 13:15:27)
 * @param otherVO nc.vo.dm.pub.POIDataVO
 */
public void combineOtherVO(POIDataVO otherVO) {
	if (otherVO != null) {
		String[] sOtherFieldNames = otherVO.getAttributeNames();
		if (sOtherFieldNames == null)
			return;
		for (int i = 0; i < sOtherFieldNames.length; i++) {
			setAttributeValue(sOtherFieldNames[i], otherVO.getAttributeValue(sOtherFieldNames[i]));
		}
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-30 19:12:25)
 */
public void convertToArrayList() {
	String[] sNames= getAttributeNames();
	if (sNames == null || sNames.length == 0)
		return;
	al= new ArrayList();
	ArrayList alKey= null;
	for (int i= 0; i < sNames.length; i++) {
		alKey= new ArrayList();
		alKey.add(sNames[i]);
		alKey.add(getAttributeValue(sNames[i]));
		al.add(alKey);
	}
	ht= null;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(2002-5-30 19:12:25)
 */
public void convertToHashTable() {
	ht = new Hashtable();
	ArrayList alKey = null;
	String sName = null;
	for (int i = 0; i < al.size(); i++) {
		alKey = (ArrayList) al.get(i);
		sName = alKey.get(0).toString();
		setAttributeValue(sName, alKey.get(1));
	}
	al = null;
}
/**
 * �˴����뷽��˵����
 * ���ܣ���hashtable��ȡ������keys
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-3 19:52:20)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return java.lang.Object[]
 * @param Keys java.lang.String[]
 */
public Object[] getAllKeysFromHashtable(Hashtable hashtable) {
	Vector vValue= new Vector();
	java.util.Enumeration enumer= hashtable.keys();
	Object obj;
	while (enumer.hasMoreElements()) {
		obj= enumer.nextElement();
		vValue.add(obj);
	}

	if (vValue.size() == 0)
		return new Object[] {
	};
	else {
		Object[] sKeys= new Object[vValue.size()];
		vValue.copyInto(sKeys);
		return sKeys;
	}
}
/**
 * �˴����뷽��˵����
 * ���ܣ���hashtable��ȡ������keys
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-3 19:52:20)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return java.lang.Object[]
 * @param Keys java.lang.String[]
 */
public String[] getAllStrKeysFromHashtable(Hashtable htAllPKs) {
	Vector vAllPKs = new Vector();
	java.util.Enumeration enumer = htAllPKs.keys();
	Object obj;
	while (enumer.hasMoreElements()) {
		obj = enumer.nextElement();
		vAllPKs.add(obj.toString());
	}
	if (vAllPKs.size() == 0)
		return new String[0];
	else {
		String[] onlyPKs = new String[vAllPKs.size()];
		vAllPKs.copyInto(onlyPKs);
		return onlyPKs;
	}
}
public java.lang.String[] getAttributeNames() {
	if (null == ht)
		convertToHashTable();
	Vector vName = new Vector();
	java.util.Enumeration enumer = getHt().keys();
	Object obj;
	while (enumer.hasMoreElements()) {
		obj = enumer.nextElement().toString().trim();

		vName.add(obj);

	}

	if (vName.size() == 0)
		return null;
	else {
		String[] sNames = new String[vName.size()];
		vName.copyInto(sNames);
		return sNames;
	}
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object[] getAttributeValue(String[] attributeName) {
	Object[] objReturn= new Object[attributeName.length];
	for (int i= 0; i < attributeName.length; i++) {
		objReturn[i]= getAttributeValue(attributeName[i]);
	}
	return objReturn;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
	if (null == ht)
		convertToHashTable();
	if ((null != attributeName) && (getHt().containsKey(attributeName)))
		return getHt().get(attributeName);
	if (null != attributeName && attributeName.startsWith("vfree")) {
		if (null == getAttributeValue(sKeyofFreeVO)) {
			return null;
		} 
	}
	return null;
}
/**
 * ������ֵ�������ʾ���ơ�
 * 
 * �������ڣ�(2001-2-15 14:18:08)
 * @return java.lang.String ������ֵ�������ʾ���ơ�
 */
public String getEntityName() {
	return null;
}
/**
 * �˴����뷽��˵����
 * ���ܣ�
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-5-8 12:31:44)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return java.util.Hashtable
 */
protected java.util.Hashtable getHt() {
	return ht;
}
/**
 * �õ��ж��Ƿ������ͬ�е�where�Ӿ䡣
 * �������ڣ�(2003-1-24 10:51:59)
 * @return java.lang.String
 * @param dtVO nc.vo.poi.initdbfromexcel.POIDataVO
 */
public String getIFExistWhere(
	String[] sFieldNames,
	String sPrimaryKey,
	POIDataVO dtVO) {
	String sIFExistWhere = "";
	for (int i = 0; i < sFieldNames.length; i++) {
		if (!sFieldNames[i].equals(sPrimaryKey)) {
			if (dtVO.getAttributeValue(sFieldNames[i]).toString().trim().length() != 0) {
				sIFExistWhere += sFieldNames[i]
					+ "='"
					+ dtVO.getAttributeValue(sFieldNames[i]).toString()
					+ "'";
			}
			else{
	            sIFExistWhere += sFieldNames[i]
					+ " is null ";
				   
			}
			if (i != sFieldNames.length - 1)
				sIFExistWhere += " and ";
		}
	}

	return sIFExistWhere;
}
/**
 * �˴����뷽��˵����
 * ���ܣ�ȡ�û�����ͬ��ֵ��������null
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-3 19:52:20)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return java.lang.Object[]
 * @param Keys java.lang.String[]
 */
public Object[] getIndependenceValueFromKeys(String[] Keys) {
	Object[] objs= getAttributeValue(Keys);
	Hashtable htTemp= new Hashtable();
	for (int i= 0; i < objs.length; i++) {
		if (null != objs[i]) {
			htTemp.put(objs[i], objs[i]);
		}
	}
	return getAllKeysFromHashtable(htTemp);
}
/**
 * �õ���Ҫ�������ݿ�������飨�á������ָ�����
 * �������ڣ�(2003-1-23 16:41:18)
 * @return java.lang.String
 * @param sFieldNames java.lang.String[]
 */
public String getInserName(String[] sFieldNames) {
	String sInserName = "";
	for (int i = 0; i < sFieldNames.length; i++){
		sInserName += sFieldNames[i];
		if(i!=sFieldNames.length-1)
			sInserName += ",";
		
	}
	return sInserName;
}
/**
 * �õ�Ҫ�����ֵ��
 * �������ڣ�(2003-1-23 16:42:12)
 * @return java.lang.String
 * @param sFieldNames java.lang.String[]
 * @param dtVO nc.vo.poi.initdbfromexcel.POIDataVO
 */
public String getInserValue(String[] sFieldNames, POIDataVO dtVO) {
	String sInserName = "";
	for (int i = 0; i < sFieldNames.length; i++) {
		if (sFieldNames[i].trim().equals("pk_corp")) {
			String strCorp = "";
			strCorp = dtVO.getAttributeValue(sFieldNames[i]).toString();
			if (strCorp != null && strCorp.toString().trim().length() == 4)
				sInserName = "'" + strCorp + "'";
			else
				sInserName += "'aaaa'";
		}
		else if (sFieldNames[i].trim().equals("dr"))
			sInserName += "0";
		else
			sInserName += "'" + dtVO.getAttributeValue(sFieldNames[i]) + "'";
		if (i != sFieldNames.length - 1)
			sInserName += ",";

	}
	return sInserName;
}
/**
 * �˴����뷽��˵����
 * ���ܣ�
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-8-30 19:49:47)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return int
 */
public int getKeyNumber() {
	if (null == getHt()) {
		convertToHashTable();
	}
	return getHt().size();
}
/**
 * �˴����뷽��˵����
 * ���ܣ���һ��VO��ɶ���VO����һ����ת����ʽ
 * ������alNewKeys Ϊһ��String���ں��±��е�keys����
		 alNewKeysGetValueType,Ϊһ��int��Ϊ1�����VO�е�ֵ��ȡֵ��Ϊ0�����VO�е�nameȡֵ
		 alNewKeysGetValueFromKeys,Ϊһ��String[]��

		 ʾ�����£�
		 alNewKeys                 {statickey0,statickey1,statickey2,statickey3,statickey4}
		 alNewKeysGetValueType     {Integer(1),       1,          1,         1,          1   }
		 alOldKeysGetValueFromKeys {statickey0,statickey1,{dynamickey00,dynamickey10},{dynamickey01,dynamickey11},{dynamickey02,dynamickey12}}
		 ��VO�ж�ӦֵΪ                 aaa1       aaa2        pk0          pk1             pk0_v0      pk1_v0        pk0_v1        pk1_v1
		 ��VO������ֵΪ             
		                           {aaa1,aaa2,pk0,pk0_v0,pk0_v1}
		                           {aaa1,aaa2,pk1,pk1_v0,pk1_v1}
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-3 20:43:40)
 * �޸����ڣ�2002-9-4 
 * �޸��ˣ�����
 * �޸�ԭ��ע�ͱ�־��
 * @return nc.vo.dm.pub.POIDataVO[]
 * @param alNewKeys java.util.ArrayList
 * @param alNewKeysGetValueType java.util.ArrayList
 * @param alNewKeysGetValueFromKeys java.util.ArrayList
 */
public POIDataVO[] getMultiDataVOs(
	ArrayList alNewKeys,
	ArrayList alNewKeysGetValueType,
	ArrayList alOldKeysGetValueFromKeys) {
	POIDataVO[] dmdvos = new POIDataVO[1];
	dmdvos[0] = new POIDataVO();
	Hashtable htForGen = new Hashtable();
	//������������
	for (int i = 0; i < alOldKeysGetValueFromKeys.size(); i++) {
		if (alOldKeysGetValueFromKeys.get(i) instanceof ArrayList) {
			ArrayList altmp = (ArrayList) alOldKeysGetValueFromKeys.get(i);
			dmdvos = new POIDataVO[altmp.size()];
			for (int j = 0; j < dmdvos.length; j++) {
				dmdvos[j] = new POIDataVO();
			}
			break;
		}
	}
	//��ֵ
	for (int i = 0; i < alOldKeysGetValueFromKeys.size(); i++) {
		if (alOldKeysGetValueFromKeys.get(i) instanceof String) { //�Ծ�ֵ̬
			String value = (String) getAttributeValue((String) alOldKeysGetValueFromKeys.get(i));
			//if (value != null && value.trim().length() != 0)
			if (value != null)
				htForGen.put(alNewKeys.get(i), value);
		}
		else { //�����ֵ
			ArrayList altmp = (ArrayList) alOldKeysGetValueFromKeys.get(i);
			for (int j = 0; j < altmp.size(); j++) {
				//dmdvos[j]= new POIDataVO();
				if (((Integer) alNewKeysGetValueType.get(i)).intValue() == 0) //��0������Ҫ��
					dmdvos[j].setAttributeValue((String) alNewKeys.get(i), altmp.get(j));
				else //��1������Ҫ��
					dmdvos[j].setAttributeValue((String) alNewKeys.get(i), getAttributeValue((String) altmp.get(j)));
			}
		}
	}
	//
	//Vector vValue = new Vector();
	//java.util.enumereration enumer = htForGen.keys();
	//Object obj;
	//while (enumer.hasMoreElements()) {
	//obj = enumer.nextElement();
	//vValue.add(obj);
	//}
	//Object[] sKeys = new Object[vValue.size()];
	//vValue.copyInto(sKeys);
	Object[] sKeys = getAllKeysFromHashtable(htForGen);
	for (int i = 0; i < dmdvos.length; i++) {
		for (int j = 0; j < sKeys.length; j++) {
			dmdvos[i].setAttributeValue((String) sKeys[j], htForGen.get(sKeys[j]));
		}
	}
	return dmdvos;
}
/**
 * @author            ������
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * 
 *  @param	         ����˵��
 * 
 *  @return	         ����ֵ
 * 
 *  @exception     �쳣����
 * 
 *  @see               ��Ҫ�μ�����������
 *  @since	         �������һ���汾���˷�������ӽ���������ѡ��
 * // @wdeprecated   �÷����������һ���汾���Ѿ������������滻������ѡ��
 * (2002-11-21 17:17:37)
 * @return java.lang.String
 */
public String getPrimaryKey() {
	return null;
}
/**
 * �õ���ѯ��IN�Ӿ�
 * �������ڣ�(2002-10-4 16:06:56)
 * @return java.lang.String[]
 * @param aAreaPK java.lang.String
 */
public String getStrPKs(String[] AreaPKs) throws java.sql.SQLException,  nc.vo.pub.BusinessException {
	StringBuffer areapks = new StringBuffer();
	if (AreaPKs != null && AreaPKs.length > 0 && AreaPKs[0] != null && AreaPKs[0].length() > 0) {
		areapks.append("'");
		areapks.append(AreaPKs[0]);
		areapks.append("'");
		for (int i = 1; i < AreaPKs.length; i++) {
			if (AreaPKs[i] != null && AreaPKs[i].trim().length() != 0) {
				areapks.append(",'");
				areapks.append(AreaPKs[i]);
				areapks.append("'");
			}
		}
	}
	return areapks.toString();
}
/**
 * ÿ���ٸ�PKֵΪһ��,�õ���ѯ��IN�Ӿ�
 �˷���ʵ����������:
 
	para:  pk_field,  stringp[]�ں�����ֵ
	����:
	((0=1) or pk_field in ('a','b'...200��) or pk_field in ('c','d'...200��))
 
 * �������ڣ�(2002-10-4 16:06:56)
 * @return java.lang.String[]
 * @param aAreaPK java.lang.String
 */
public StringBuffer getStrPKs(String sFieldName, String[] AreaPKs)
	throws java.sql.SQLException,  nc.vo.pub.BusinessException {
	StringBuffer areapks= new StringBuffer();
	if (sFieldName != null
		&& sFieldName.trim().length() != 0
		&& AreaPKs != null
		&& AreaPKs.length > 0
		&& AreaPKs[0] != null
		&& AreaPKs[0].length() > 0) {
		int iModNum= 200;
		sFieldName= sFieldName.trim();
		areapks.append("((0=1");

		for (int i= 0; i < AreaPKs.length; i++) {
			if (AreaPKs[i] != null && AreaPKs[i].trim().length() != 0) {
				if (i % iModNum == 0) {
					areapks.append(") or ");
					areapks.append(sFieldName).append(" in (");
					areapks.append("'").append(AreaPKs[i]).append("'");
					continue;
				}
				areapks.append(",'");
				areapks.append(AreaPKs[i]);
				areapks.append("'");
			}
		}
		areapks.append("))");
	}
	return areapks;
}
/**
 * �˴����뷽��˵����
 * ���ܣ�
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-5-8 12:31:44)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return java.util.Hashtable
 */
public java.util.Vector getVec() {
	return vec;
}
/**
 * �˴����뷽��˵����
 * �������ڣ�(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {
	if (null == ht)
		convertToHashTable();
	if (null != name) {
		if (value != null) {
			if (value instanceof String) {
				value = value.toString().trim();
			}

			getHt().put(name, value);

		} else
			if (getHt().containsKey(name))
				getHt().remove(name);
	}
}
/**
 * �˴����뷽��˵����
 �����ͱ�ͷVO��ȫ��������������ɾ��״̬��
 * �������ڣ�(2002-5-30 20:33:22)
 * @param status int
 */
public void setDrTo(int drStatus) {
	setAttributeValue("dr", new Integer(drStatus));
}
/**
 * @author            ������
 * �����Ĺ��ܡ���;�������Եĸ��ģ��Լ�����ִ��ǰ������״̬��
 * 
 *  @param	         ����˵��
 * 
 *  @return	         ����ֵ
 * 
 *  @exception     �쳣����
 * 
 *  @see               ��Ҫ�μ�����������
 *  @since	         �������һ���汾���˷�������ӽ���������ѡ��
 * // @wdeprecated   �÷����������һ���汾���Ѿ������������滻������ѡ��
 * (2002-11-21 17:18:20)
 * @param key java.lang.String
 */
public void setPrimaryKey(String key) {
}
/**
 * �˴����뷽��˵����
 * ���ܣ�����һ��VOת����DMVO
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-8-29 10:43:31)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @param otherVO nc.vo.pub.AggregatedValueObject
 */
public POIDataVO translateFromOtherVO(nc.vo.pub.CircularlyAccessibleValueObject otherVO) {
	POIDataVO ddvo= new POIDataVO();
	String[] sNames= otherVO.getAttributeNames();
	if (null != sNames) {
		for (int i= 0; i < sNames.length; i++) {
			ddvo.setAttributeValue(sNames[i], otherVO.getAttributeValue(sNames[i]));
		}
	}
	return ddvo;
}
/**
 * �˴����뷽��˵����
 * ���ܣ������µ�VO���������VO������
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-26 11:27:37)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 * @param sVOClassName java.lang.String
 */
public CircularlyAccessibleValueObject translateToOtherVO(String sVOClassName) throws Exception {
	try {
		if (null == sVOClassName || sVOClassName.trim().length() == 0)
			return null;
		Class cMyClass= Class.forName(sVOClassName);
		CircularlyAccessibleValueObject vo= (CircularlyAccessibleValueObject) cMyClass.newInstance();
		String[] sNames= getAttributeNames();
		if (null != sNames) {
			for (int i= 0; i < sNames.length; i++) {
				vo.setAttributeValue(sNames[i], getAttributeValue(sNames[i]));
			}
		}
		return vo;
	} catch (Exception ee) {
		throw ee;
	}
}
/**
 * �˴����뷽��˵����
 * ���ܣ������µ�VO���������VO
 * ������ 
 * ���أ�
 * ���⣺
 * ���ڣ�(2002-9-26 11:27:37)
 * �޸����ڣ��޸��ˣ��޸�ԭ��ע�ͱ�־��
 * @return nc.vo.pub.CircularlyAccessibleValueObject
 * @param sVOClassName java.lang.String
 */
public void translateToOtherVO(CircularlyAccessibleValueObject vo) {
	if (null == vo)
		return;
	String[] sNames= getAttributeNames();
	if (null != sNames) {
		for (int i= 0; i < sNames.length; i++) {
			vo.setAttributeValue(sNames[i], getAttributeValue(sNames[i]));
		}
	}
}
/**
 * ��֤���������֮��������߼���ȷ�ԡ�
 * 
 * �������ڣ�(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException �����֤ʧ�ܣ��׳�
 *     ValidationException���Դ�����н��͡�
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
