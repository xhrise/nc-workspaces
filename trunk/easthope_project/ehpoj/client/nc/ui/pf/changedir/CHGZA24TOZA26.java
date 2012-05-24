package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;
/**
 * 用于ZA24TOZA26的VO的动态转换类。
 *
 * 创建日期：(2008-4-23)
 * @author：平台脚本生成
 */
public class CHGZA24TOZA26 extends nc.ui.pf.change.VOConversionUI {
/**
 * CHGZA24TOZA26 构造子注解。
 */
public CHGZA24TOZA26() {
	super();
}
/**
* 获得后续类的全录经名称。
* @return java.lang.String[]
*/
@Override
public String getAfterClassName() {
	return null;
}
/**
* 获得另一个后续类的全录径名称。
* @return java.lang.String[]
*/
@Override
public String getOtherClassName() {
	return null;
}
/**
* 获得字段对应。
* @return java.lang.String[]
*/
@Override
public String[] getField() {
	return new String[] {
		"B_itemno->B_itemcode",
		"B_pk_project->B_itemname"
	};
}
/**
* 获得公式。
* @return java.lang.String[]
*/
@Override
public String[] getFormulas() {
	return null;
}
/**
* 返回用户自定义函数。
*/
@Override
public UserDefineFunction[] getUserDefineFunction() {
	return null;
}
}
