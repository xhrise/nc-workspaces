package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.pub.lang.*;
/**
 * Ա����ְ�����
 * �������ڣ�(2004-5-18 8:44:29)
 * @author��Administrator
 */
public class PartValidator extends CurrentSubsetValidator {
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-18 8:45:43)
 */
public PartValidator() {
	super();
	setRules(new String[][]{
		{ "pk_psncl", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000042")/*@res "��Ա���"*/, "notnull" },
		{ "pk_corp", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000053")/*@res "��˾"*/, "notnull" },
		{ "pk_deptdoc", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000109")/*@res "��ְ����"*/, "notnull" },
		{ "jobtype", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPT600704-000184")/*@res "��ְ����"*/, "notnull" },
//		{ "begindate", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000144")/*@res "��ʼ����"*/, "notnull" }
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
	 begindate = (UFDate) record.getAttributeValue("begindate");
	try {
		UFDate enddate = (UFDate) record.getAttributeValue("enddate");
		if (begindate != null && enddate != null)
			if (!begindate.before(enddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000163")/*@res "��ְ��ʼʱ�����С�ڼ�ְ��ֹʱ�䣡"*/);
	} catch (Exception ex) {
		rethrow(ex, "enddate");
	}
	//���ź͸�λ��Ӧ��ϵ�Ƿ���ȷ
	String deptpk = (String) ((nc.vo.hi.hi_301.GeneralVO)record).getFieldValue("pk_deptdoc");
	String jobpk = (String) ((nc.vo.hi.hi_301.GeneralVO)record).getFieldValue("pk_postdoc");
	if (deptpk!= null&&jobpk!=null) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("om_job", "", null, "pk_om_job", jobpk, "pk_deptdoc='"+deptpk+"'");
		if (!exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000147")/*@res "ѡ��ĸ�λ�Ͳ��Ų�ƥ�䣬������ѡ��"*/);
	}
		// �����ļ�ְ��¼�������ڲ���Ϊ�գ�
		UFBoolean bendflag = (UFBoolean) ((nc.vo.hi.hi_301.GeneralVO) record)
				.getFieldValue("bendflag");
		if (bendflag.booleanValue()
				&& record.getAttributeValue("enddate") == null) {
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("600704", "UPT600704-000322")/*
																 * @res
																 * "�����ļ�ְ��¼�������ڲ���Ϊ�գ�"
																 */);
		}
	}
}
