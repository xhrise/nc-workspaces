
package nc.ui.ehpta.pub.ref;

import java.util.Hashtable;

import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ���̵���,�ͻ�����,��Ӧ�̵���
 * ���̵�����������,�ͻ�������������,��Ӧ�̵�����������
 * <p>
 * <strong>�ṩ�ߣ�</strong>UAP
 * <p>
 * <strong>ʹ���ߣ�</strong>
 * 
 * <p>
 * <strong>���״̬��</strong>��ϸ��� 
 * <p>
 * @version 	NC5.0
 * @author 		sxj
 */
public class CustmandocDefaultRefModel extends AbstractRefGridTreeModel {

	public CustmandocDefaultRefModel() {
		super();
		init("���̵���");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init(String refNodeName) {
		m_strRefNodeName = refNodeName;
		setRootName(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC000-0001235")/* @res "��������" */);
		setClassFieldCode(new String[] { "areaclcode", "areaclname",
				"pk_areacl", "pk_corp", "pk_fatherarea" });
		setFatherField("pk_fatherarea");
		setChildField("pk_areacl");
		setClassJoinField("pk_areacl");
		setClassTableName("bd_areacl");
		setClassDefaultFieldCount(2);
		// sxj 2003-02-20
		setClassWherePart(" (pk_corp='" + getPk_corp() + "' or pk_corp= '"
				+ "0001" + "')");
		// setClassWherePart(" pk_corp='"+getPk_corp()+"'");
		setClassDataPower(true);

		setConfig(refNodeName);
		setHiddenFieldCode(new String[] { "bd_cubasdoc.taxpayerid",
				"bd_cumandoc.pk_cumandoc", "bd_cubasdoc.pk_cubasdoc",
				"bd_cubasdoc.pk_areacl" });
		setTableName("bd_cumandoc inner join bd_cubasdoc on bd_cumandoc.pk_cubasdoc=bd_cubasdoc.pk_cubasdoc ");

		setPkFieldCode("bd_cumandoc.pk_cumandoc");
		setRefCodeField("bd_cumandoc.pk_cumandoc");
		setRefNameField("bd_cubasdoc.custname");
		
		setStrPatch("distinct");
		// setDefaultFieldCount(5);
		setDocJoinField("bd_cubasdoc.pk_areacl");
//		setBlurFields(getFieldCode()); // sxj add
		setMnecode(new String[] { "bd_cumandoc.cmnecode",
				"bd_cubasdoc.custname" }); // sxj add
		setRefQueryDlgClaseName("nc.ui.bd.b09.QueryDlgForRef");
		// ��������
		Hashtable contents = new Hashtable();
		contents.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000371")/* @res "�ⲿ��λ" */); // 0
		contents.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000372")/* @res "�ڲ����㵥λ��" */); // 1
		contents.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000373")/* @res "�ڲ����˵�λ" */); // 2
		contents.put("3", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000374")/* @res "�ڲ�������Ա" */); // 3
		// ��Ӧ��״̬
		Hashtable contents1 = new Hashtable();
		contents1.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000375")/* @res "Ǳ��" */); // 0
		contents1.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000376")/* @res "����׼" */); // 1
		contents1.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000377")/* @res "ʧЧ" */); // 2
		// ��������
		Hashtable contents2 = new Hashtable();
		contents2.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC000-0001589")/* @res "�ͻ�" */); // 0
		contents2.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC000-0000275")/* @res "��Ӧ��" */); // 1
		contents2.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000378")/* @res "���̿ͻ�" */); // 2
		contents2.put("3", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000379")/* @res "���̹�Ӧ��" */); // 3
		Hashtable convert = new Hashtable();
		convert.put("bd_cubasdoc.custprop", contents);
		convert.put("bd_cumandoc.custstate", contents1);
		convert.put("bd_cumandoc.custflag", contents2);
		setDispConvertor(convert);
		String strFomula = "getColValue(bd_settleunit, settleunitname, pk_settleunit ,bd_cumandoc.pk_settleunit)";
		setFormulas(new String[][]{{"bd_cumandoc.pk_settleunit",strFomula}});
		String wherePart = null;
		if (refNodeName.equals("���̵���")) {
			wherePart = " bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "' AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";
		} else if (refNodeName.equals("�ͻ�����")) {

			wherePart=" bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2') ";

		} else if (refNodeName.equals("��Ӧ�̵���")) {

			wherePart=" bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "' AND (bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='3') ";

		} else if (refNodeName.equals("���̵�����������")) {
			wherePart=" bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";

			//
		} else if (refNodeName.equals("�ͻ�������������")) {
			wherePart=" (bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2')) ";

		} else if (refNodeName.equals("��Ӧ�̵�����������")) {
			wherePart=" bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "'  AND (bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='3') ";
		}
		if (isGroupAssignData()){
			String groupDataWherePart = " and bd_cumandoc.pk_cubasdoc in (select pk_cubasdoc from bd_cubasdoc where pk_corp='0001') ";
			setWherePart(wherePart+ groupDataWherePart);
		}else {
			setWherePart(wherePart);
		}
		//
		resetFieldName();
		setRefDocEditClassName("nc.ui.bd.b09.CustManRefDocEditor");

	}
	
//	protected String getSql_Match(String[] fieldNames, String[] values,
//			String strPatch, String[] strFieldCode, String[] hiddenFields,
//			String strTableName, String strWherePart, String strOrderField) {
//		
//		String[] toLowCasefieldNames = new String[fieldNames.length];
//		String[] toLowCaseValues = new String[values.length];
//		
//		if (isIgnoreCase()){
//			for (int i = 0; i < toLowCasefieldNames.length; i++) {
//				toLowCasefieldNames[i]= addLowerStr(fieldNames[i]);
//			}
//			for (int i = 0; i < toLowCaseValues.length; i++) {
//				toLowCaseValues[i]= values[i].toLowerCase();
//			}
//			
//		}else{
//			toLowCasefieldNames = fieldNames;
//			toLowCaseValues = values;
//		}
//		
//		return super.getSql_Match(toLowCasefieldNames, toLowCaseValues, strPatch, strFieldCode, hiddenFields, strTableName, strWherePart, strOrderField);
//	}
//
//	private String addLowerStr(String fieldName) {
//		return "lower("+fieldName+")";
//	}
//	
//	//���⴦����̵�������
//	private boolean isIgnoreCase(){
//		return getRefNodeName().indexOf("����")>=0;
//	}
//	protected String addBlurWherePart() {
//		
//		String blurWherePart = null;
//		
//		if (isIgnoreCase()){
//			// ����ģ��---���ǲ�����WherePart
//			String[] toLowCasefieldNames = new String[getBlurFields().length];
//			for (int i = 0; i < toLowCasefieldNames.length; i++) {
//				toLowCasefieldNames[i]= addLowerStr(getBlurFields()[i]);
//			}
//			String toLowcaseBlurValue = getBlurValue().toLowerCase();
//		
//			StringBuffer sqlBuffer = new StringBuffer();
//			if (RefUtil.isIncludeBlurChar(getBlurValue())) {
//
//				String value = toLowcaseBlurValue.replace('*', '%').replace('?', '_');
//				String blurSql = RefUtil.getCompositeSql(toLowCasefieldNames, value,
//						" like ", null, null, " or ");
//				sqlBuffer.append(" and (");
//				sqlBuffer.append(blurSql);
//
//				sqlBuffer.append(")");
//			} else { // �������������Ϊ����
//				if (isMnecodeInput()) {
//					String blurSql = RefUtil.getCompositeSql(toLowCasefieldNames,
//							toLowcaseBlurValue, "=", null, null, " or ");
//					sqlBuffer.append(" and ( ").append(blurSql).append(" )");
//
//				}
//			}
//			blurWherePart= sqlBuffer.toString();
//		}else{
//			blurWherePart = super.addBlurWherePart();
//		}
//	
//		return blurWherePart;
//	}
}
