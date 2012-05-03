package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.ui.pub.ClientEnvironment;
import nc.vo.hi.hi_301.*;
import nc.vo.pub.BusinessException;
/**
 * �˴���������������
 * �������ڣ�(2004-5-17 9:44:37)
 * @author��Administrator
 */
import java.util.*;
public abstract class AbstractValidator implements IValidator {
	private String[][] rules=null;

	//��Ч�Լ���������ӳ���
	private static final Hashtable validatorMap=new Hashtable();
	static{
		init();
	}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-8-2 17:05:13)
 * @param records nc.vo.pub.CircularlyAccessibleValueObject[]
 * @exception java.lang.Exception �쳣˵����
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
	            throw new Exception(/*nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")@res "��"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000121")/*@res "����¼Ϊ�ռ�¼�������룡"*/);
			}
		}
	}
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-17 9:48:42)
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
 * �˴����뷽��������
 * �������ڣ�(2004-5-17 10:06:00)
 * @return java.lang.String[][]
 */
public java.lang.String[][] getRules() {
	return rules;
}
/**
 * ��õ�ǰ�����Ч�Լ�������
 * �������ڣ�(2004-5-18 10:51:13)
 * @return nc.vo.hi.hi_301.validator.IValidator
 * @param tableCode java.lang.String
 */
public static IValidator getValidator(String tableCode) {
	//�ȴ�ӳ����а��ձ�����
	IValidator validator = (IValidator) validatorMap.get(tableCode);
	if (validator == null) {
		//�������ע����У���ʹ�ñ����͹�˾����Ϊ�������������Ƿ�ע��
		String pk_corp = ClientEnvironment.getInstance().getCorporation().getPk_corp();
//		String pk_corp = nc.ui.hr.global.Global.getCorpPK();
		validator = (IValidator) validatorMap.get(tableCode + pk_corp);
		if (validator == null) {
			//�������û�У����ѯ�����ֵ䲢��ʼ��
			try {
				int type = HIDelegator.getIHRhiQBS().getSubsetType(tableCode, pk_corp);
				if (type > 0) {
					switch (type) {
						case 1 : //�޹��ɱ��
							validator = new OrderlessSubsetValidator();
							break;
						case 2 : //�����Ա��
							validator = new PeriodicalSubsetValidator();
							break;
						case 3 : //ͬ�ڼ�¼
							validator = new CurrentSubsetValidator();
							break;
					}
				}
				if (validator != null) {
					if (tableCode.indexOf("cpdef") != -1)
						//��˾�Զ��壬��Ҫ���ձ����ӹ�˾����ʽ��
						validatorMap.put(tableCode + pk_corp, validator);
					else
						//���ż��Զ��壬����Ԥ���尴�ձ�����
						validatorMap.put(tableCode, validator);
				}
			} catch (Exception e) {
			}
		}
	}
	return validator;
}
private static void init() {
	//��ʼ��������
	for (int i = 0; i < validators.length; i++) {
		validatorMap.put(validators[i][0], validators[i][1]);
	}
}
/**
 * �ж��Ƿ���ͬ�ڼ�¼��
 * �������ڣ�(2004-5-21 8:54:20)
 * @return boolean
 * @param tableCode java.lang.String
 */
public static boolean isCurrentTable(String tableCode) {
	IValidator validator=getValidator(tableCode);
	return validator!=null&&(validator instanceof CurrentSubsetValidator);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-6-5 10:41:09)
 * @param e java.lang.Exception
 * @param fieldCode java.lang.String
 * @exception nc.vo.hi.hi_301.validator.FieldValidationException �쳣˵����
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
 * �˴����뷽��������
 * �������ڣ�(2004-5-17 10:06:00)
 * @param newRules java.lang.String[][]
 */
public void setRules(java.lang.String[][] newRules) {
	rules = newRules;
}
/**
 * validate ����ע�⡣
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	String[][] rules = getRules();
	if (rules != null) {
		for (int i = 0; i < rules.length; i++) {
			try {
				FieldValidator.validate(rules[i][1], rules[i][2], ((GeneralVO)record).getFieldValue(rules[i][0]));
			} catch (Exception e) {
	            //������֯��Я���ֶ���Ϣ
	            rethrow(e,rules[i][0]);
			}
		}
	}
}
}
