package nc.vo.hi.hi_301.validator;

import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-8-2 16:42:03)
 * @author��Administrator
 */
public class TryoutValidator extends OrderlessSubsetValidator {
/**
 * TryoutValidator ������ע�⡣
 */
public TryoutValidator() {
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
	 begindate = (UFDate) record.getAttributeValue("dtrybegin");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dtryend");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000169")/*@res "������ʼʱ�����С��������ֹʱ�䣡"*/);
	} catch (Exception ex) {
		rethrow(ex, "dtryend");
	}
}
}
