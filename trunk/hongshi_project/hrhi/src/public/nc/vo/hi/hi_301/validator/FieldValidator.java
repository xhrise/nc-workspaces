package nc.vo.hi.hi_301.validator;

/**
 * 通用字段检查。
 * 创建日期：(2004-5-14 17:04:20)
 * @author：Administrator
 */
import java.util.*;

import nc.vo.hi.hi_301.ValidException;
import nc.vo.pub.BusinessException;
public class FieldValidator {

	//常见字段检查注册
	private static Object[][] validators = { 
	    { "notnull", new NotNullValidator()},
	    {"id",new IDValidator()},
	    {"date",new DateValidator()}
	};

	//高速映射器
	private final static Hashtable map = new Hashtable();
	static {

	    //初始化高速映射
		init();
	}
	private static void init(){
		for(int i=0;i<validators.length;i++){
			map.put(validators[i][0],validators[i][1]);
		}
	}
/**
 *使用ruleName规定的规则检查主体为principal的输入值o
 * ruleName为预定义的规则名称或者"<规则类名>"的形式
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
			//这样来截获异常不行呀
			//throw new Exception(principal + e.getMessage());
			//修改为：
			if(e instanceof ValidException){
				throw e;
			}else{
				throw new Exception(principal + e.getMessage());
			}
		}
	}
}
}
