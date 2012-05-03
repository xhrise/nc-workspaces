package nc.vo.hi.hi_301.validator;

/**
 * 员工奖励记录。
 * 创建日期：(2004-5-18 9:07:57)
 * @author：Administrator
 */
public class EncValidator extends CurrentSubsetValidator {
/**
 * EncValidator 构造子注解。
 */
public EncValidator() {
	super();
//	setRules(new String[][]{
//		{"vencourtype","奖励类型","notnull"},
//		{"vencourmeas","奖励措施","notnull"}
//	});
	setRules(null);
}
}
