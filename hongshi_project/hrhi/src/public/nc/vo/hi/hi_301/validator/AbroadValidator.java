package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-8-2 16:28:10)
 * @author��Administrator
 */
public class AbroadValidator extends CurrentSubsetValidator {
/**
 * AbroadValidator ������ע�⡣
 */
public AbroadValidator() {
	super();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-8-2 16:31:20)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception �쳣˵����
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
	throws java.lang.Exception {
	 super.validate(record);
	 UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("dabroaddate");//dabroaddate  begindate
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dabroadreturn");//dabroadreturn  enddate
		if (begindate != null && enddate != null)
			if (begindate.after(enddate)){
//				throw new Exception("����(����)ʱ�����С�ڻع�ʱ��!");
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPT600704-000243")/*@res "����(����)ʱ�����С�ڻع�ʱ��!"*/);
			}
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
}
}
