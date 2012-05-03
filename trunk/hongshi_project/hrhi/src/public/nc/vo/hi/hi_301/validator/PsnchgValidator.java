package nc.vo.hi.hi_301.validator;

/**
 * 员工流动信息。
 * 创建日期：(2004-5-18 9:12:18)
 * @author：Administrator
 */
public class PsnchgValidator extends OrderlessSubsetValidator {
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-18 9:13:02)
 */
public PsnchgValidator() {
	super();
//	setRules(new String[][]{
//		{"chgdate","流动日期","notnull"},
//		{"chgflag","进出状态","notnull"},
//		{"chgtype","流动类别","notnull"}
//	});
	setRules(null);
}
}
