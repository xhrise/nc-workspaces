package nc.vo.hi.hi_301.validator;

/**
 * �ǿռ��顣
 * �������ڣ�(2004-5-14 17:09:37)
 * @author��Administrator
 */
public class NotNullValidator implements IFieldValidator {
/**
 * NotNullValidator ������ע�⡣
 */
public NotNullValidator() {
	super();
}
/**
 * validate ����ע�⡣
 */
public void validate(Object o) throws Exception {
	if(o==null)
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000156")/*@res "����Ϊ��"*/);
	String v=o.toString();
	if(v.trim().length()==0)
		throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000156")/*@res "����Ϊ��"*/);
}
}
