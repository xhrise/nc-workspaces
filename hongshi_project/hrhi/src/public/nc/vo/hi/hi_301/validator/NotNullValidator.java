package nc.vo.hi.hi_301.validator;

/**
 * 非空检验。
 * 创建日期：(2004-5-14 17:09:37)
 * @author：Administrator
 */
public class NotNullValidator implements IFieldValidator {
/**
 * NotNullValidator 构造子注解。
 */
public NotNullValidator() {
	super();
}
/**
 * validate 方法注解。
 */
public void validate(Object o) throws Exception {
	if(o==null)
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000156")/*@res "不能为空"*/);
	String v=o.toString();
	if(v.trim().length()==0)
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000156")/*@res "不能为空"*/);
}
}
