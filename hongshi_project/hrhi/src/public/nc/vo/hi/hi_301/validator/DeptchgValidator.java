package nc.vo.hi.hi_301.validator;

import nc.itf.hi.HIDelegator;
import nc.vo.pub.lang.*;
/**
 * �˴���������������
 * �������ڣ�(2004-5-17 17:17:17)
 * @author��Administrator
 */
import nc.vo.hi.hi_301.*;

public class DeptchgValidator extends OrderlessSubsetValidator {
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-17 17:27:00)
 * @param record nc.vo.pub.CircularlyAccessibleValueObject
 * @exception java.lang.Exception �쳣˵����
 */
private void checkRecord(nc.vo.pub.CircularlyAccessibleValueObject record) throws java.lang.Exception {
	//��ʼ���ڷǿ�
	UFDate begindate = (UFDate) record.getAttributeValue("begindate");
	FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000144")/*@res "��ʼ����"*/, "notnull", begindate);
	//����������
	Integer recordNum = (Integer) record.getAttributeValue("recordnum");
	UFBoolean lastflag = (UFBoolean) record.getAttributeValue("lastflag");
	UFDate enddate = (UFDate) record.getAttributeValue("enddate");
	if (recordNum != null && recordNum.intValue() > 1) {
		//�ǵ�ǰ��¼�������ڲ���Ϊ��
		FieldValidator.validate(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000145")/*@res "��������"*/, "notnull", enddate);
	}
	//��ʼ���ڱ���С�ڽ�������
	if (begindate != null && enddate != null)
		if (!begindate.before(enddate))
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000146")/*@res "��ʼ���ڱ���С�ڽ������ڣ�"*/);
}
/**
 * �˴����뷽��������
 * �������ڣ�(2004-5-17 10:06:00)
 * @return java.lang.String[][]
 */
public java.lang.String[][] getRules() {
	return new String[][] { 
			{ "pk_psncl", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000042")/*@res "��Ա���"*/, "notnull" },			
	        {"pk_deptdoc", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","upt600704-000139")/*@res "����"*/, "notnull" }
	};

}
/**
 * validate ����ע�⡣
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
	if (records == null)
		return;
	GeneralVO vo = (GeneralVO)records[0];
	//���ź͸�λ��Ӧ��ϵ�Ƿ���ȷ
	String deptpk = (String) vo.getFieldValue("pk_deptdoc");
	String jobpk = (String) vo.getFieldValue("pk_postdoc");
	if (deptpk!= null&&jobpk!=null) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("om_job", "", null, "pk_om_job", jobpk, "pk_deptdoc='"+deptpk+"'");
		if (!exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000147")/*@res "ѡ��ĸ�λ�Ͳ��Ų�ƥ�䣬������ѡ��"*/);
	}
	if (records.length > 1) {
	    //���ÿһ��¼����ȷ��
		for(int i=0;i<records.length;i++){
			checkRecord(records[i]);
		}
		//����¼֮����Ч��
		//super.validate(records);
	} else
		if (records.length > 0) {//Ψһ��¼
			//��ʼ���ڷǿ�
			UFDate begindate = (UFDate) records[0].getAttributeValue("begindate");
			//����������
			UFDate enddate = (UFDate) records[0].getAttributeValue("enddate");
			//��ʼ���ڱ���С�ڽ�������
			if (begindate != null && enddate != null){
				if (!begindate.before(enddate))	throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000146")/*@res "��ʼ���ڱ���С�ڽ������ڣ�"*/);
				//--------------------
				//	�ж��Ƿ�Ϊ��ְ��¼���޸�
				boolean flag = HIDelegator.getIHRhiQBS().checkPsnWorkDate(
						(String)(records[0].getAttributeValue("$pk_corp")), (String)vo.getAttributeValue("pk_psndoc"),
						begindate, enddate);
				if(flag){
//					throw new Exception("��ְ���ں�������˾����ְ�����г�ͻ");
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"600700", "UPP600700-000239")/*@res "��ְ���ں�������˾����ְ�����г�ͻ"*/);
				}
				//  ----------------------
			}
		}
	for (int i = 0; i < records.length-1; i++) {
		UFDate firstbegindate = (UFDate)records[i].getAttributeValue("begindate");
		UFDate firstenddate = (UFDate)records[i].getAttributeValue("enddate");//wangkf add
		UFDate secondbegindate =(UFDate)records[i+1].getAttributeValue("begindate");//wangkf add
		UFDate secondenddate=(UFDate)records[i+1].getAttributeValue("enddate");
		if(firstenddate!=null&&secondbegindate!=null){
			if(!secondbegindate.after(firstenddate)){
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")/*@res "��"*/+(i+2)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000148")/*@res "�м�¼�Ŀ�ʼ���ڲ��ܵ��ڻ������ڵ�"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000149")/*@res "�м�¼�Ľ������ڣ�"*/);
			}
		}
		if(firstbegindate!=null&&secondbegindate != null){//wangkf add ��Ҫ����µ��ж�
			if(!secondbegindate.after(firstbegindate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")/*@res "��"*/+(i+2)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000148")/*@res "�м�¼�Ŀ�ʼ���ڲ��ܵ��ڻ������ڵ�"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100149")/*@res "�м�¼�Ŀ�ʼ���ڣ�"*/);
		
		}
		if(firstbegindate!=null&&secondbegindate != null&&firstenddate==null){//wangkf add ��Ҫ����µ��ж�
			if(!secondbegindate.getDateBefore(1).after(firstbegindate))
				throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000091")/*@res "��"*/+(i+2)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000148")/*@res "�м�¼�Ŀ�ʼ���ڲ��ܵ��ڻ������ڵ�"*/+(i+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-100150")/*@res "�м�¼�Ŀ�ʼ���ں�һ�죡"*/);
		
		}
	}

}
/**
 * ���Ǹ��෽�����ָ��������
 */
public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
	String[][] rules = getRules();
	if (rules != null) {
		for (int i = 0; i < rules.length; i++) {
			try {
				FieldValidator.validate(rules[i][1], rules[i][2], ((GeneralVO)record).getFieldValue(rules[i][0]));
			} catch (Exception e) {
	            //������֯��Я���ֶ���Ϣ
	            rethrow(e,rules[i][0]);
			}
		}
	}
	
//	���ź͸�λ��Ӧ��ϵ�Ƿ���ȷ
	String deptpk = (String) ((GeneralVO)record).getFieldValue("pk_deptdoc");
	String jobpk = (String) ((GeneralVO)record).getFieldValue("pk_postdoc");
	if (deptpk!= null&&jobpk!=null) {
		boolean exists = HIDelegator.getIHRhiQBS().recordExists("om_job", "", null, "pk_om_job", jobpk, "pk_deptdoc='"+deptpk+"'");
		if (!exists)
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000147")/*@res "ѡ��ĸ�λ�Ͳ��Ų�ƥ�䣬������ѡ��"*/);
	}
	
}
}
