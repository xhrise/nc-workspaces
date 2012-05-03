package nc.vo.hr.xmldatainout;

/**
 * U8系统的一个公司有一个帐套，而Hr系统一个帐套下有多个公司
   这样就必须在传递时提供对照信息，Hr的一个公司对应U8的一个
   接受帐套。
 * 创建日期：(2003-3-7 9:43:21)
 * @author：常晓晖
 */
public class Corp_U8SenderVO extends nc.vo.pub.ValueObject{
	//HR的公司主键
	private java.lang.String m_strPK = null;
	//HR的公司名称
	private java.lang.String m_strCorpName = null;
	//U8中对应的Sender号
	private java.lang.String m_strSenderNo = null;
	//备注信息
	private java.lang.String m_strMemo;
/**
 * ChrCorp_U8Sender 构造子注解。
 */
public Corp_U8SenderVO() {
	super();
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:44:23)
 * @return java.lang.String
 */
public java.lang.String getM_strCorpName() {
	return m_strCorpName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:45:26)
 * @return java.lang.String
 */
public java.lang.String getM_strMemo() {
	return m_strMemo;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:43:57)
 * @return java.lang.String
 */
public java.lang.String getM_strPK() {
	return m_strPK;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:44:51)
 * @return java.lang.String
 */
public java.lang.String getM_strSenderNo() {
	return m_strSenderNo;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:44:23)
 * @param newM_strCorpName java.lang.String
 */
public void setM_strCorpName(java.lang.String newM_strCorpName) {
	m_strCorpName = newM_strCorpName;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:45:26)
 * @param newM_strMemo java.lang.String
 */
public void setM_strMemo(java.lang.String newM_strMemo) {
	m_strMemo = newM_strMemo;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:43:57)
 * @param newM_strPK java.lang.String
 */
public void setM_strPK(java.lang.String newM_strPK) {
	m_strPK = newM_strPK;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:44:51)
 * @param newM_strSenderNo java.lang.String
 */
public void setM_strSenderNo(java.lang.String newM_strSenderNo) {
	m_strSenderNo = newM_strSenderNo;
}

/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public java.lang.String getEntityName() {
	return "Corp_U8SenderVO";
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
