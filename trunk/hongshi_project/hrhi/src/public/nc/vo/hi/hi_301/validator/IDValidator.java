package nc.vo.hi.hi_301.validator;

import nc.vo.hi.hi_301.ValidException;
import nc.vo.hr.validate.IDValidateUtil;
import nc.vo.pub.BusinessException;

/**
 * ���֤���顣
 * �������ڣ�(2004-5-14 19:43:50)
 * @author��Administrator
 */
public class IDValidator implements IFieldValidator {
/**
 * validate ����ע�⡣
 */
public void validate(java.lang.Object o) throws java.lang.Exception {
	String id = ((String[]) o)[0];
	////���֤�������Ϊ��
	if (id == null || id.trim().length() == 0)
		return;
	IDValidateUtil idutil = new IDValidateUtil(id);
	if(!idutil.validate()){
		//throw new Exception(idutil.getErrMsg());
		//Ϊ������ʱУ��idʱ�������쳣�������ཫ���Դ��쳣�������󣬶���ֻ������ʾ��
		if (idutil.getErrCode() == 0||idutil.getErrCode() == 4){
			throw new ValidException(idutil.getErrMsg(),ValidException.LEVEL_DECIDEBYUSER);
		}else{
			throw new ValidException(idutil.getErrMsg(),ValidException.LEVEL_SERIOUS);
		}
	}
	//15����18λ
	//15λ��
	//String birthBits;
	//String sexBits;
	//if (id.length() == 15) {
		//birthBits = "19" + id.substring(6, 12);
		//sexBits = id.substring(14);
	//} else {
		//birthBits = id.substring(6, 14);
		//sexBits = id.substring(16, 17);
	//}
	////�ж����֤�źͳ��������Ƿ��Ӧ
	//if (birth != null && birth.trim().length() > 0) {
		//birthBits =
			//birthBits.substring(0, 4)
				//+ "-"
				//+ birthBits.substring(4, 6)
				//+ "-"
				//+ birthBits.substring(6, 8);
		//if (!birthBits.equals(birth))
			//throw new Exception("����λ�����ղ�һ��");
	//}
	//if (sex != null && sex.trim().length() > 0) {
		//int sb = Integer.parseInt(sexBits);
		//if(sex.equals("��")){
	        //if(sb%2==0)
				//throw new Exception("�Ա�λ���Ա�һ��");
		//}else{
	        //if((sb%2)!=0)
				//throw new Exception("�Ա�λ���Ա�һ��");
		//}
	//}
}
}