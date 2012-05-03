package nc.vo.hi.hi_301.validator;

/**
 * 员工惩罚记录。
 * 创建日期：(2004-5-18 9:09:40)
 * @author：Administrator
 */
public class PunValidator extends CurrentSubsetValidator {
/**
 * PunValidator 构造子注解。
 */
public PunValidator() {
	super();
	setRules(new String[][]{
//		{"vpunishtype","惩罚类型","notnull"},
//		{"vpunishmeas","惩罚措施","notnull"}
	});
}
}
