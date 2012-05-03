package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.pub.lang.*;
/**
 * �μ�������֯��
 * �������ڣ�(2004-5-18 8:58:49)
 * @author��Administrator
 */
public class OrgpsnValidator extends CurrentSubsetValidator {
/**
 * OrgpsnValidator ������ע�⡣
 */
public OrgpsnValidator() {
	super();
	setRules(new String[][]{
			{"pk_om_dumorg",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000158")/*@res "������֯"*/,"notnull"},
			{"pk_orgrole",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000159")/*@res "������֯��ɫ"*/,"notnull"}
		});
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
	 begindate = (UFDate) record.getAttributeValue("djoindate");
	 UFBoolean flag = (UFBoolean)record.getAttributeValue("bquit");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("dquitdate");
		if (begindate != null) {			
				if (enddate != null && begindate.after(enddate))
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl
							.getNCLangRes().getStrByID("600704",
									"UPP600704-000160")/* @res "�˳�ʱ�䲻�����ڼ���ʱ�䣡" */);
				GeneralVO vo = HIDelegator.getPsnInf().queryDetailForDumorg(
						(String) record.getAttributeValue("$pk_om_dumorg"));
				if (vo != null && vo.getAttributeValue("builddate") != null) {
					UFDate builddate = new UFDate((String) vo
							.getAttributeValue("builddate"));
					if (builddate != null && builddate.after(begindate))
						throw new Exception(nc.vo.ml.NCLangRes4VoTransl
								.getNCLangRes().getStrByID("600704",
										"UPT600704-000326")/* @res "�μ����ڲ�������������֯�Ĵ������ڣ�" */);
				}
			}
			if (enddate != null && flag == null || enddate != null
					&& !flag.booleanValue()) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704", "UPP600704-000161")/*
																	 * @res
																	 * "δ�˳�������֯����Ա���������˳�����"
																	 */);
			}
			if (flag != null && flag.booleanValue() && enddate == null) {
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704", "UPP600704-000162")/*@res "�Ѿ��˳�������֯���������˳�����"*/);
			}
	} catch (Exception ex) {
		rethrow(ex, "dquitdate");
	}
}
}
