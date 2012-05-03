package nc.vo.hi.hi_401;

/**
 * 此处插入类型说明。
 * 创建日期：(2002-5-23 19:43:52)
 * @author：田海波
 */
public class PostPrintDataVo extends nc.vo.pub.CircularlyAccessibleValueObject {


	//职务
	public String m_dutycode;
	public String m_dutyname;
	public String m_dutyseries;//序列名称
	public String m_dutyrank;//等级名称
	//岗位
	public String m_jobcode;
	public String m_jobname;
	public String m_postdept;//所属部门名称
	public String m_supierior;//直接上级名称


	
/**
 * PostPrintDataVo 构造子注解。
 */
public PostPrintDataVo() {
	super();
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:26:03)
 * @return java.lang.String[]
 */
public java.lang.String[] getAttributeNames() {
	return null;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public Object getAttributeValue(String attributeName) {
   
	if (attributeName == null)
		return null;
	else {
		if (attributeName.equals("dutycode")) {
			return m_dutycode;
		} else
			if (attributeName.equals("dutyname")) {
				return m_dutyname;
			} else
				if (attributeName.equals("dutyseries")) {
					return m_dutyseries;
				} else
					if (attributeName.equals("dutyrank")) {
						return m_dutyrank;
					} else
						if (attributeName.equals("jobcode")) {
							return m_jobcode;
						} else
							if (attributeName.equals("jobname")) {
								return m_jobname;
							} else
								if (attributeName.equals("postdept")) {
									return m_postdept;
								} else
									if (attributeName.equals("supierior")) {
										return m_supierior;
									} else
										return null;
	}

}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public String getAttributeValueDes(String attributeName) {

	if(getAttributeValue(attributeName)==null)return null;
	else return getAttributeValue(attributeName).toString();
	
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getDutycode() {
	return m_dutycode;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getDutyname() {
	return m_dutyname;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getDutyrank() {
	return m_dutyrank;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getDutyseries() {
	return m_dutyseries;
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
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getJobcode() {
	return m_jobcode;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getJobname() {
	return m_jobname;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getPostdept() {
	return m_postdept;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getSupierior() {
	return m_supierior;
}
/**
 * 此处插入方法说明。
 * 创建日期：(01-3-20 17:24:29)
 * @param key java.lang.String
 */
public void setAttributeValue(String name, Object value) {}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setDutycode(String code) {
	m_dutycode = code;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setDutyname(String name) {
	m_dutyname = name;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setDutyrank(String rank) {
	m_dutyrank = rank;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setDutyseries(String series) {
	m_dutyseries = series;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setJobcode(String code) {
	m_jobcode = code;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setJobname(String name) {
	m_jobname = name;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setPostdept(String dept) {
	m_postdept = dept;
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public void setSuperior(String superior) {
	m_supierior = superior;
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
