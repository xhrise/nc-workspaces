package nc.vo.hi.hi_301.validator;

import java.util.HashMap;

import nc.itf.hi.HIDelegator;

public class KeyPsnValidator extends OrderlessSubsetValidator {
	/**
	 * validate ����ע�⡣
	 */
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject[] records) throws Exception {
		if (records == null||records.length==0)
			return;
//		Vector v = new Vector();
		HashMap temphash = new HashMap();		
		for (int i = 0; i < records.length; i++) {
			String pk_keypsn_group = (String) records[i].getAttributeValue("$pk_keypsn_group");
			if (temphash.get(pk_keypsn_group) == null) {
				temphash.put(pk_keypsn_group, records[i]);
			}else{
				continue;
			}
			int count = 0;
			for (int j = 0; j < records.length; j++) {				
				Object o = records[j].getAttributeValue("enddate");
				if(pk_keypsn_group.equalsIgnoreCase((String) records[j].getAttributeValue("$pk_keypsn_group"))
						&&o==null){
					count++;
				}
				if(count>=2){
					throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600710","UPT600710-000041")/*@res "��ǰ��Ա�ڵ�"*/
							+(j+1)+nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600710","UPT600710-000042")/*@res "�м�¼����Ա����ֻ����һ����Ч��¼,��ˢ������!"*/);
				}
				
			}
		}

		
	}
	
	/**
	 * �˴����뷽��������
	 * �������ڣ�(2004-5-17 10:06:00)
	 * @return java.lang.String[][]
	 */
	public java.lang.String[][] getRules() {
		return new String[][] { 
				{ "pk_keypsn_group", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600710","UPP600710-000026")/*@res "��Ա��"*/, "notnull" },			
		        {"pk_corp", nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0000404")/*@res "��˾"*/, "notnull" },
		        {"begindate",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("600704","UPP600704-000144")/*@res "��ʼ����"*/,"notnull"}
		};

	}
	
	/**
	 * validate ����ע�⡣
	 */
	public void validate(nc.vo.pub.CircularlyAccessibleValueObject record) throws Exception {
		super.validate(record);
		Object pk_psndoc_sub = record.getAttributeValue("pk_psndoc_sub");
		String pk_keypsn_group = (String) record.getAttributeValue("$pk_keypsn_group");
		String pk_psndoc = (String) record.getAttributeValue("pk_psndoc");
		Object o = record.getAttributeValue("enddate");
		String sql = " select 1 from hi_psndoc_keypsn where enddate is null and pk_psndoc ='"+pk_psndoc+"' and pk_keypsn_group ='"+pk_keypsn_group+"'";
		if(pk_psndoc_sub!=null){
			sql+=" and pk_psndoc_sub <>'"+pk_psndoc_sub+"'";
		}
		boolean exist = HIDelegator.getPsnInf().isRecordExist(sql);
		if(exist && o==null){
//			throw new Exception("��ǰ��Ա�ڴ���Ա����ֻ����һ����Ч��¼,��ˢ������!");
			throw new Exception(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
					"600700", "UPP600700-000350")/*@res "��ǰ��Ա�ڴ���Ա����ֻ����һ����Ч��¼,��ˢ������!"*/);
		}
	}
}
