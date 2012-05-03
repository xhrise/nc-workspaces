package nc.vo.hi.hi_301;

/**
 * 此处插入类型描述。
 * @author：Administrator
 * 创建日期：(2002-12-27 11:18:27)
 * @version	
 * @see		
 * @since	
 * 
 * // 修改人 + 修改日期
 * // 修改说明
 * 
 */
public class HrJtMainVO extends nc.vo.pub.ValueObject {
	private HRMainVO mainvo;
	private nc.vo.hi.hi_401.PsnDataVO[] workexpvo;
	private nc.vo.hi.hi_401.PsnDataVO[] articlevo;
	private nc.vo.hi.hi_401.PsnDataVO[] taskvo;
/**
 * HrJtMainVO 构造子注解。
 */
public HrJtMainVO() {
	super();
}
/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @return nc.vo.hi.hi_401.PsnDataVO
 */
public nc.vo.hi.hi_401.PsnDataVO[] getArticlevo() {
	return articlevo;
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
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @return nc.vo.hi.hi_301.HRMainVO
 */
public HRMainVO getMainvo() {
	return mainvo;
}
/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @return nc.vo.hi.hi_401.PsnDataVO
 */
public nc.vo.hi.hi_401.PsnDataVO[] getTaskvo() {
	return taskvo;
}
/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @return nc.vo.hi.hi_401.PsnDataVO
 */
public nc.vo.hi.hi_401.PsnDataVO[] getWorkexpvo() {
	return workexpvo;
}
/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @param newArticlevo nc.vo.hi.hi_401.PsnDataVO
 */
public void setArticlevo(nc.vo.hi.hi_401.PsnDataVO[] newArticlevo) {
	articlevo = newArticlevo;
}
/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @param newMainvo nc.vo.hi.hi_301.HRMainVO
 */
public void setMainvo(HRMainVO newMainvo) {
	mainvo = newMainvo;
}
/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @param newTaskvo nc.vo.hi.hi_401.PsnDataVO
 */
public void setTaskvo(nc.vo.hi.hi_401.PsnDataVO[] newTaskvo) {
	taskvo = newTaskvo;
}
/**
 * 函数的功能、用途、对属性的更改，以及函数执行前后对象的状态。
 * @param		参数说明
 * @return		返回值
 * @exception 异常描述
 * @see		需要参见的其它内容
 * @since		从类的那一个版本，此方法被添加进来。（可选）
 * 
 * 
 * 
 * @param newWorkexp nc.vo.hi.hi_401.PsnDataVO
 */
public void setWorkexpvo(nc.vo.hi.hi_401.PsnDataVO[] newWorkexp) {
	workexpvo = newWorkexp;
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
