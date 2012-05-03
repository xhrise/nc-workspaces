package nc.vo.hi.hi_301.validator;

import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-8-2 16:28:10)
 * @author��Administrator
 */
public class EduValidator extends CurrentSubsetValidator {
/**
 * EduValidator ������ע�⡣
 */
public EduValidator() {
	super();
	setRules(new String[][]{
//		{"education","ѧ��","notnull"}
	});
}

/**
 * validate ����ע�⡣
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
	if (records == null){
		return;
	}
	int count = 0;
	for (int i = 0; i < records.length; i++) {
		Object obj = records[i].getAttributeValue("lasteducation");
		UFBoolean isLasteducation = (UFBoolean)obj;//�Ƿ����ѧ��
		if(isLasteducation != null && isLasteducation.booleanValue()){
			count++;
		}
		if(count > 1){
//			throw new Exception("���ѧ��ֻ����һ��");//������
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100151")/*@res "���ѧ��ֻ����һ��"*/);
		}
	}
	
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
	 begindate = (UFDate) record.getAttributeValue("begindate");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("enddate");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000150")/*@res "��ѧ���ڱ���С�ڱ�ҵ���ڣ�"*/);
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
}
}
