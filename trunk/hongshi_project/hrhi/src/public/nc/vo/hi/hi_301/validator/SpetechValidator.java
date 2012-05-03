package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-8-2 16:48:34)
 * @author��Administrator
 */
public class SpetechValidator extends CurrentSubsetValidator {
/**
 * SpetechValidator ������ע�⡣
 */
public SpetechValidator() {
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
	 UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("dengbegin");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dengend");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000168")/*@res "Ƹ����ʼʱ�����С��Ƹ����ֹʱ�䣡"*/);
	} catch (Exception ex) {
		rethrow(ex, "dengend");
	}
}
}
