package nc.vo.hr.xmldatainout;

/**
 * 此处插入类型描述。
 * 创建日期：(2003-3-6 21:54:32)
 * @author：常晓晖
 */


 
/**
 * 此处插入类型描述。
 * 创建日期：(2003-3-6 21:54:32)
 * @author：常晓晖
 */
public class ConfigVO extends nc.vo.pub.ValueObject {
	private boolean m_WithU8;
	private java.lang.String m_U8SoapAddr;
	private Corp_U8SenderVO[] m_CorpSenderAry;
/**
 * ConfigVO 构造子注解。
 */
public ConfigVO() {
	super();
}
/**
 * 返回数值对象的显示名称。
 * 
 * 创建日期：(2001-2-15 14:18:08)
 * @return java.lang.String 返回数值对象的显示名称。
 */
public String getEntityName() {
	return "EAIConfigVO";
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:58:52)
 * @return nc.bs.hr.xmldatainout.Corp_U8SenderVO[]
 */
public Corp_U8SenderVO[] getM_CorpSenderAry() {
	return m_CorpSenderAry;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:31:32)
 * @return int
 */
public String getM_U8SoapAddr() {
	return m_U8SoapAddr;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:30:22)
 * @return boolean
 */
public boolean getM_WithU8() {
	return m_WithU8;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:30:22)
 * @return boolean
 */
public boolean isM_WithU8() {
	return m_WithU8;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:58:52)
 * @param newM_CorpSenderAry nc.bs.hr.xmldatainout.Corp_U8SenderVO[]
 */
public void setM_CorpSenderAry(Corp_U8SenderVO[] newM_CorpSenderAry) {
	m_CorpSenderAry = newM_CorpSenderAry;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:31:32)
 * @param newM_U8SoapAddr int
 */
public void setM_U8SoapAddr(String newM_U8SoapAddr) {
	m_U8SoapAddr = newM_U8SoapAddr;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2003-3-7 9:30:22)
 * @param newM_WithU8 boolean
 */
public void setM_WithU8(boolean newM_WithU8) {
	m_WithU8 = newM_WithU8;
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
