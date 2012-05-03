package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.ui.pub.ClientEnvironment;
import nc.vo.hi.hi_301.*;
import nc.vo.pub.BusinessException;
/**
 * 此处插入类型描述。
 * 创建日期：(2004-5-17 9:44:37)
 * @author：Administrator
 */
import java.util.*;
public abstract class AbstractValidator implements IValidator {
	private String[][] rules=null;

	//有效性检验器高速映射表
	private static final Hashtable validatorMap=new Hashtable();
	static{
		init();
	}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-8-2 17:05:13)
 * @param records nc.vo.pub.CircularlyAccessibleValueObject[]
 * @exception java.lang.Exception 异常说明。
 */
public static void checkNullRecord(
	nc.vo.pub.CircularlyAccessibleValueObject[] records)
	throws java.lang.Exception {
	if (records != null && records.length > 0) {
		for (int i = 0; i < records.length; i++) {
			GeneralVO vo = (GeneralVO) records[i];
			java.util.HashMap values = vo.getValues();
			Set sets = values.keySet();
			Iterator ite = sets.iterator();
			boolean isNullRecord = true;
			while(ite.hasNext()){
	            String key = (String)ite.next();
	            if(!key.equals("pk_psndoc")&&!key.equals("pk_psndoc_sub")&&!key.equals("recordnum")&&!key.equals("lastflag")){
		            Object o = values.get(key);
		            if(o!=null){
			            isNullRecord = false;
			            break;
		            }
	            }
			}
			if(isNullRecord){
	            throw new Exception(/*nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")@res "第"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000121")/*@res "条记录为空记录，请输入！"*/);
			}
		}
	}
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-17 9:48:42)
 * @return java.lang.String[]
 * @param fieldCode java.lang.String
 */
public String[] getFieldValidator(String fieldCode) {
	if(getRules()!=null){
		String[][]ruleNames=getRules();
		for(int i=0;i<ruleNames.length;i++){
			if(ruleNames[i][0].equals(fieldCode))
				return new String[]{ruleNames[i][1],ruleNames[i][2]};
		}
	}
	return null;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-17 10:06:00)
 * @return java.lang.String[][]
 */
public java.lang.String[][] getRules() {
	return rules;
}
/**
 * 获得当前表的有效性检验器。
 * 创建日期：(2004-5-18 10:51:13)
 * @return nc.vo.hi.hi_301.validator.IValidator
 * @param tableCode java.lang.String
 */
public static IValidator getValidator(String tableCode) {
	//先从映射表中按照表名查
	IValidator validator = (IValidator) validatorMap.get(tableCode);
	if (validator == null) {
		//如果不再注册表中，则使用表名和公司名作为联合主键查找是否注册
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
//		String pk_corp = nc.ui.hr.global.Global.getCorpPK();
		validator = (IValidator) validatorMap.get(tableCode + pk_corp);
		if (validator == null) {
			//如果还是没有，则查询数据字典并初始化
			try {
				int type = HIDelegator.getIHRhiQBS().getSubsetType(tableCode, pk_corp);
				if (type > 0) {
					switch (type) {
						case 1 : //无规律变更
							validator = new OrderlessSubsetValidator();
							break;
						case 2 : //周期性变更
							validator = new PeriodicalSubsetValidator();
							break;
						case 3 : //同期记录
							validator = new CurrentSubsetValidator();
							break;
					}
				}
				if (validator != null) {
					if (tableCode.indexOf("cpdef") != -1)
						//公司自定义，需要按照表名加公司的形式存
						validatorMap.put(tableCode + pk_corp, validator);
					else
						//集团级自定义，或者预定义按照表名存
						validatorMap.put(tableCode, validator);
				}
			} catch (Exception e) {
			}
		}
	}
	return validator;
}
private static void init() {
	//初始化检验器
	for (int i = 0; i < validators.length; i++) {
		validatorMap.put(validators[i][0], validators[i][1]);
	}
}
/**
 * 判断是否是同期记录。
 * 创建日期：(2004-5-21 8:54:20)
 * @return boolean
 * @param tableCode java.lang.String
 */
public static boolean isCurrentTable(String tableCode) {
	IValidator validator=getValidator(tableCode);
	return validator!=null&&(validator instanceof CurrentSubsetValidator);
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-6-5 10:41:09)
 * @param e java.lang.Exception
 * @param fieldCode java.lang.String
 * @exception nc.vo.hi.hi_301.validator.FieldValidationException 异常说明。
 */
protected void rethrow(Exception e, String fieldCode) throws ValidException {
	ValidException fve = null;
	if(e instanceof ValidException){
		fve = (ValidException)e;
	}else{
		fve=new ValidException(e.getMessage());
	}
	fve.setFieldCode(fieldCode);
	throw fve;
}
/**
 * 此处插入方法描述。
 * 创建日期：(2004-5-17 10:06:00)
 * @param newRules java.lang.String[][]
 */
public void setRules(java.lang.String[][] newRules) {
	rules = newRules;
}
/**
 * validate 方法注解。
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	String[][] rules = getRules();
	if (rules != null) {
		for (int i = 0; i < rules.length; i++) {
			try {
				FieldValidator.validate(rules[i][1], rules[i][2], ((GeneralVO)record).getFieldValue(rules[i][0]));
			} catch (Exception e) {
	            //重新组织，携带字段信息
	            rethrow(e,rules[i][0]);
			}
		}
	}
}
}
