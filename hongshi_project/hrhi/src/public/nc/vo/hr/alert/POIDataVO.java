package nc.vo.hr.alert;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-1-22 13:20:11)
 * @author：zhanglf
 */
import java.util.*;
import nc.vo.pub.CircularlyAccessibleValueObject;
//import nc.vo.pub.lang.*;
public class POIDataVO extends nc.vo.pub.CircularlyAccessibleValueObject {
	//记录所有数据的Object形式集合
	private Hashtable ht= new Hashtable();
	private ArrayList al = null;
	private Vector vec = new Vector();
	public String sKeyofFreeVO= "keyoffreevo";
	public String m_sFieldNames[] = null;
/**
 * POIDataVO 构造子注解。
 */
public POIDataVO() {
	super();
}
/**
 * POIDataVO 构造子注解。
 */
public POIDataVO(Hashtable initHT) {
	super();
	ht = initHT;
}
/**
 * POIDataVO 构造子注解。
 */
public POIDataVO(Hashtable initHT ,Vector initVec) {
	super();
	ht = initHT;
	vec = initVec;
}
/**
 * 此处插入方法说明。
 * 功能：
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-8-29 22:30:01)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 创建日期：(2002-5-30 13:15:27)
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
 * 此处插入方法说明。
 * 创建日期：(2002-5-30 19:12:25)
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
 * 此处插入方法说明。
 * 创建日期：(2002-5-30 19:12:25)
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
 * 此处插入方法说明。
 * 功能：从hashtable中取得所有keys
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-9-3 19:52:20)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 功能：从hashtable中取得所有keys
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-9-3 19:52:20)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
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
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
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
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return null;
}
/**
 * 此处插入方法说明。
 * 功能：
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-5-8 12:31:44)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.util.Hashtable
 */
protected java.util.Hashtable getHt() {
	return ht;
}
/**
 * 得到判断是否存在相同行的where子句。
 * 创建日期：(2003-1-24 10:51:59)
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
 * 此处插入方法说明。
 * 功能：取得互不相同的值，不考虑null
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-9-3 19:52:20)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 得到需要插入数据库的列名组（用‘，’分隔）。
 * 创建日期：(2003-1-23 16:41:18)
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
 * 得到要插入的值。
 * 创建日期：(2003-1-23 16:42:12)
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
 * 此处插入方法说明。
 * 功能：
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-8-30 19:49:47)
 * 修改日期，修改人，修改原因，注释标志：
 * @return int
 */
public int getKeyNumber() {
	if (null == getHt()) {
		convertToHashTable();
	}
	return getHt().size();
}
/**
 * 此处插入方法说明。
 * 功能：从一行VO变成多行VO，依一定的转换方式
 * 参数：alNewKeys 为一个String表，内含新表中的keys名称
		 alNewKeysGetValueType,为一个int表，为1代表从VO中的值里取值，为0代表从VO中的name取值
		 alNewKeysGetValueFromKeys,为一个String[]表，

		 示范如下：
		 alNewKeys                 {statickey0,statickey1,statickey2,statickey3,statickey4}
		 alNewKeysGetValueType     {Integer(1),       1,          1,         1,          1   }
		 alOldKeysGetValueFromKeys {statickey0,statickey1,{dynamickey00,dynamickey10},{dynamickey01,dynamickey11},{dynamickey02,dynamickey12}}
		 旧VO中对应值为                 aaa1       aaa2        pk0          pk1             pk0_v0      pk1_v0        pk0_v1        pk1_v1
		 新VO数组中值为             
		                           {aaa1,aaa2,pk0,pk0_v0,pk0_v1}
		                           {aaa1,aaa2,pk1,pk1_v0,pk1_v1}
 * 返回：
 * 例外：
 * 日期：(2002-9-3 20:43:40)
 * 修改日期：2002-9-4 
 * 修改人：毕晖
 * 修改原因，注释标志：
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
	//决定表体行数
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
	//置值
	for (int i = 0; i < alOldKeysGetValueFromKeys.size(); i++) {
		if (alOldKeysGetValueFromKeys.get(i) instanceof String) { //对静态值
			String value = (String) getAttributeValue((String) alOldKeysGetValueFromKeys.get(i));
			//if (value != null && value.trim().length() != 0)
			if (value != null)
				htForGen.put(alNewKeys.get(i), value);
		}
		else { //对组合值
			ArrayList altmp = (ArrayList) alOldKeysGetValueFromKeys.get(i);
			for (int j = 0; j < altmp.size(); j++) {
				//dmdvos[j]= new POIDataVO();
				if (((Integer) alNewKeysGetValueType.get(i)).intValue() == 0) //对0型数据要求
					dmdvos[j].setAttributeValue((String) alNewKeys.get(i), altmp.get(j));
				else //对1型数据要求
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
 * @author            仲瑞庆
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * 
 *  @param	         参数说明
 * 
 *  @return	         返回值
 * 
 *  @exception     异常描述
 * 
 *  @see               需要参见的其它内容
 *  @since	         从类的那一个版本，此方法被添加进来。（可选）
 * // @wdeprecated   该方法从类的那一个版本后，已经被其它方法替换。（可选）
 * (2002-11-21 17:17:37)
 * @return java.lang.String
 */
public String getPrimaryKey() {
	return null;
}
/**
 * 得到查询的IN子句
 * 创建日期：(2002-10-4 16:06:56)
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
 * 每二百个PK值为一组,得到查询的IN子句
 此方法实现如下内容:
 
	para:  pk_field,  stringp[]内含各个值
	返回:
	((0=1) or pk_field in ('a','b'...200个) or pk_field in ('c','d'...200个))
 
 * 创建日期：(2002-10-4 16:06:56)
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
 * 此处插入方法说明。
 * 功能：
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-5-8 12:31:44)
 * 修改日期，修改人，修改原因，注释标志：
 * @return java.util.Hashtable
 */
public java.util.Vector getVec() {
	return vec;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
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
 * 此处插入方法说明。
 向表体和表头VO中全部置入所给定的删除状态。
 * 创建日期：(2002-5-30 20:33:22)
 * @param status int
 */
public void setDrTo(int drStatus) {
	setAttributeValue("dr", new Integer(drStatus));
}
/**
 * @author            仲瑞庆
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * 
 *  @param	         参数说明
 * 
 *  @return	         返回值
 * 
 *  @exception     异常描述
 * 
 *  @see               需要参见的其它内容
 *  @since	         从类的那一个版本，此方法被添加进来。（可选）
 * // @wdeprecated   该方法从类的那一个版本后，已经被其它方法替换。（可选）
 * (2002-11-21 17:18:20)
 * @param key java.lang.String
 */
public void setPrimaryKey(String key) {
}
/**
 * 此处插入方法说明。
 * 功能：将另一种VO转换成DMVO
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-8-29 10:43:31)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 功能：生成新的VO，依传入的VO的类名
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-9-26 11:27:37)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 此处插入方法说明。
 * 功能：生成新的VO，依传入的VO
 * 参数： 
 * 返回：
 * 例外：
 * 日期：(2002-9-26 11:27:37)
 * 修改日期，修改人，修改原因，注释标志：
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
 * 验证对象各属性之间的数据逻辑正确性。
 * 
 * 创建日期：(2001-2-15 11:47:35)
 * @exception nc.vo.pub.ValidationException 如果验证失败，抛出
 *     ValidationException，对错误进行解释。
 */
public void validate() throws nc.vo.pub.ValidationException {}
}
