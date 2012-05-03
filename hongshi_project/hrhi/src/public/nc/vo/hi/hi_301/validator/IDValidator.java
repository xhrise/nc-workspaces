package nc.vo.hi.hi_301.validator;

import nc.vo.hi.hi_301.ValidException;
import nc.vo.hr.validate.IDValidateUtil;
import nc.vo.pub.BusinessException;

/**
 * 身份证检验。
 * 创建日期：(2004-5-14 19:43:50)
 * @author：Administrator
 */
public class IDValidator implements IFieldValidator {
/**
 * validate 方法注解。
 */
public void validate(java.lang.Object o) throws java.lang.Exception {
	String id = ((String[]) o)[0];
	////身份证号码可以为空
	if (id == null || id.trim().length() == 0)
		return;
	IDValidateUtil idutil = new IDValidateUtil(id);
	if(!idutil.validate()){
		//throw new Exception(idutil.getErrMsg());
		//为了区分时校验id时产生的异常。界面类将不对此异常报出错误，而是只报出提示。
		if (idutil.getErrCode() == 0||idutil.getErrCode() == 4){
			throw new ValidException(idutil.getErrMsg(),ValidException.LEVEL_DECIDEBYUSER);
		}else{
			throw new ValidException(idutil.getErrMsg(),ValidException.LEVEL_SERIOUS);
		}
	}
	//15或者18位
	//15位的
	//String birthBits;
	//String sexBits;
	//if (id.length() == 15) {
		//birthBits = "19" + id.substring(6, 12);
		//sexBits = id.substring(14);
	//} else {
		//birthBits = id.substring(6, 14);
		//sexBits = id.substring(16, 17);
	//}
	////判断身份证号和出生年月是否对应
	//if (birth != null && birth.trim().length() > 0) {
		//birthBits =
			//birthBits.substring(0, 4)
				//+ "-"
				//+ birthBits.substring(4, 6)
				//+ "-"
				//+ birthBits.substring(6, 8);
		//if (!birthBits.equals(birth))
			//throw new Exception("出生位与生日不一致");
	//}
	//if (sex != null && sex.trim().length() > 0) {
		//int sb = Integer.parseInt(sexBits);
		//if(sex.equals("男")){
	        //if(sb%2==0)
				//throw new Exception("性别位与性别不一致");
		//}else{
	        //if((sb%2)!=0)
				//throw new Exception("性别位与性别不一致");
		//}
	//}
}
}