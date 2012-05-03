package nc.vo.hi.hi_301.validator;

/**
 * ͨ���ֶμ�顣
 * �������ڣ�(2004-5-14 17:04:20)
 * @author��Administrator
 */
import java.util.*;

import nc.vo.hi.hi_301.ValidException;
import nc.vo.pub.BusinessException;
public class FieldValidator {

	//�����ֶμ��ע��
	private static Object[][] validators = { 
	    { "notnull", new NotNullValidator()},
	    {"id",new IDValidator()},
	    {"date",new DateValidator()}
	};

	//����ӳ����
	private final static Hashtable map = new Hashtable();
	static {

	    //��ʼ������ӳ��
		init();
	}
	private static void init(){
		for(int i=0;i<validators.length;i++){
			map.put(validators[i][0],validators[i][1]);
		}
	}
/**
 *ʹ��ruleName�涨�Ĺ���������Ϊprincipal������ֵo
 * ruleNameΪԤ����Ĺ������ƻ���"<��������>"����ʽ
 */
public static void validate(String principal, String ruleName, Object o) throws Exception {
	IFieldValidator validator = null;
	if (ruleName.startsWith("<")) {
		String ruleClass = ruleName.substring(1, ruleName.length() - 1);
		try {
			validator = (IFieldValidator) Class.forName(ruleClass).newInstance();
		} catch (Exception e) {
		}
	} else {
		validator = (IFieldValidator) map.get(ruleName);
	}
	if (validator != null) {
		try {
			validator.validate(o);
		} catch (Exception e) {
			//�������ػ��쳣����ѽ
			//throw new Exception(principal + e.getMessage());
			//�޸�Ϊ��
			if(e instanceof ValidException){
				throw e;
			}else{
				throw new Exception(principal + e.getMessage());
			}
		}
	}
}
}
