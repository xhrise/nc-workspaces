package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-8-2 16:54:31)
 * @author��Administrator
 */
public class SpeitemValidator extends CurrentSubsetValidator {
/**
 * SpeitemValidator ������ע�⡣
 */
public SpeitemValidator() {
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
	 begindate = (UFDate) record.getAttributeValue("dspebegin");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dspeend");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000167")/*@res "��Ŀ��ʼʱ�����С����Ŀ��ֹʱ�䣡"*/);
	} catch (Exception ex) {
		rethrow(ex, "dengend");
	}
}
}
