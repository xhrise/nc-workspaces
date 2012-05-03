package nc.vo.hi.hi_301.validator;

import nc.bs.logging.Logger;
import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-8-2 11:01:44)
 * @author��Administrator
 */
public class AssValidator extends OrderlessSubsetValidator {
/**
 * AssValidator ������ע�⡣
 */
public AssValidator() {
	super();
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-8-2 11:05:50)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception �쳣˵����
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record)
	throws java.lang.Exception {
		setRules(new String[][] {
			{"begindate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"common", "UC000-0003900")/* @res "��ʼ����" */,"notnull" },
			{"enddate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"common", "UC000-0003203")/* @res "��ֹ����" */,"notnull" } });
		super.validate(record);
	UFDate begindate=null;
	 begindate = (UFDate) record.getAttributeValue("begindate");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("enddate");
		if (begindate != null && enddate != null)
			if (begindate.after(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000129")/*@res "��������ʼʱ�䲻�����ڿ�������ֹʱ�䣡"*/);
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
	if(record.getAttributeValue("nassessscore")!=null){
		Logger.error(record.getAttributeValue("nassessscore").getClass().getName());
		UFDouble value = (UFDouble)record.getAttributeValue("nassessscore");
		if((value.compareTo(new nc.vo.pub.lang.UFDouble(0.0))<0)||(value.compareTo(new UFDouble(100.0))>0)){
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000130")/*@res "���˳ɼ�ֵ�ķ�ΧӦ����0-100"*/);
		}
	}
	}
}
