package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.pub.lang.UFDate;

/**
 * �Ͷ���ͬ��
 * �������ڣ�(2004-5-18 9:17:14)
 * @author��Administrator
 */
public class CtrtValidator extends OrderlessSubsetValidator {
/**
 * CtrtValidator ������ע�⡣
 */
	public CtrtValidator() {
		super();
		setRules(new String[][] {
				{
						"vcontractnum",
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"600704", "UPP600704-100178")/* @res "��ͬ���" */,
						"notnull" },
				{
						"begindate",
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"600704", "UPP600704-100179")/* @res "��ͬ��ʼ����" */,
						"notnull" },
				{
						"pk_termtype",
						nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
								"600704", "UPP600704-100180")/*@res "��ͬ��������"*/,
						"notnull" } });
	}
/**
 *��ͬ���Ψһ
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	try {
		//�Ͷ���ͬ�Ӽ����� �̶��������� �ǹ̶�����ʱ�����ӽ������ڵķǿռ��顣
		String pk_termtype = (String)record.getAttributeValue("$pk_termtype");
		if("0001AA1000000000120W".equals(pk_termtype)){//�ǹ̶�����
			setRules(new String[][]{
					{"vcontractnum",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100178")/*@res "��ͬ���"*/,"notnull"},
					{"begindate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100179")/*@res "��ͬ��ʼ����"*/,"notnull"},
					{"pk_termtype",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100180")/*@res "��ͬ��������"*/,"notnull"},
					{"enddate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100181")/*@res "��ͬ��������"*/,"notnull"}
			});
		}else{
			setRules(new String[][]{
					{"vcontractnum",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100178")/*@res "��ͬ���"*/,"notnull"},
					{"begindate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100179")/*@res "��ͬ��ʼ����"*/,"notnull"},
					{"pk_termtype",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100180")/*@res "��ͬ��������"*/,"notnull"}
			});
		}
		super.validate(record);
		//
		//��ʼ���ڷǿ�
		UFDate probegindate = (UFDate) record.getAttributeValue("probegindate");
		UFDate probenddate = (UFDate) record.getAttributeValue("probenddate");
		//��ʼ���ڱ���С�ڽ�������
		if (probegindate != null && probenddate != null)
			if (probegindate.after(probenddate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPT600704-000324")/*@res "���ÿ�ʼ���ڲ����������ý������ڣ�"*/);

		//�������Ƿ�Ψһ
		String pk_psnbasdoc = (String) record.getAttributeValue("pk_psnbasdoc");
		String vcontractnum = (String) record.getAttributeValue("vcontractnum");
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("hi_psndoc_ctrt", "pk_psnbasdoc", pk_psnbasdoc, "vcontractnum", vcontractnum, null);
		if (exists){
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000133")/*@res "��ͬ���룺"*/ + vcontractnum + nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000123")/*@res "�Ѿ����ڣ�"*/);
		}
	} catch (Exception e) {
		rethrow(e, "vcontractnum");
	}
}
}
