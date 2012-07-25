
package nc.ui.ehpta.pub.ref;

import java.util.Hashtable;

import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 客商档案,客户档案,供应商档案
 * 客商档案包含冻结,客户档案包含冻结,供应商档案包含冻结
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计 
 * <p>
 * @version 	NC5.0
 * @author 		sxj
 */
public class CustmandocDefaultRefModel extends AbstractRefGridTreeModel {

	public CustmandocDefaultRefModel() {
		super();
		init("客商档案");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init(String refNodeName) {
		m_strRefNodeName = refNodeName;
		setRootName(NCLangRes4VoTransl.getNCLangRes().getStrByID("common",
				"UC000-0001235")/* @res "地区分类" */);
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
		// 客商类型
		Hashtable contents = new Hashtable();
		contents.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000371")/* @res "外部单位" */); // 0
		contents.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000372")/* @res "内部核算单位门" */); // 1
		contents.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000373")/* @res "内部法人单位" */); // 2
		contents.put("3", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000374")/* @res "内部渠道成员" */); // 3
		// 供应商状态
		Hashtable contents1 = new Hashtable();
		contents1.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000375")/* @res "潜在" */); // 0
		contents1.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000376")/* @res "已批准" */); // 1
		contents1.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000377")/* @res "失效" */); // 2
		// 客商属性
		Hashtable contents2 = new Hashtable();
		contents2.put("0", NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC000-0001589")/* @res "客户" */); // 0
		contents2.put("1", NCLangRes4VoTransl.getNCLangRes().getStrByID(
				"common", "UC000-0000275")/* @res "供应商" */); // 1
		contents2.put("2", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000378")/* @res "客商客户" */); // 2
		contents2.put("3", NCLangRes4VoTransl.getNCLangRes().getStrByID("ref",
				"UPPref-000379")/* @res "客商供应商" */); // 3
		Hashtable convert = new Hashtable();
		convert.put("bd_cubasdoc.custprop", contents);
		convert.put("bd_cumandoc.custstate", contents1);
		convert.put("bd_cumandoc.custflag", contents2);
		setDispConvertor(convert);
		String strFomula = "getColValue(bd_settleunit, settleunitname, pk_settleunit ,bd_cumandoc.pk_settleunit)";
		setFormulas(new String[][]{{"bd_cumandoc.pk_settleunit",strFomula}});
		String wherePart = null;
		if (refNodeName.equals("客商档案")) {
			wherePart = " bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "' AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";
		} else if (refNodeName.equals("客户档案")) {

			wherePart=" bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2') ";

		} else if (refNodeName.equals("供应商档案")) {

			wherePart=" bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "' AND (bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='3') ";

		} else if (refNodeName.equals("客商档案包含冻结")) {
			wherePart=" bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='1' OR bd_cumandoc.custflag='2') ";

			//
		} else if (refNodeName.equals("客户档案包含冻结")) {
			wherePart=" (bd_cumandoc.pk_corp='"
					+ getPk_corp()
					+ "'  AND (bd_cumandoc.custflag='0' OR bd_cumandoc.custflag='2')) ";

		} else if (refNodeName.equals("供应商档案包含冻结")) {
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
//	//特殊处理客商档案参照
//	private boolean isIgnoreCase(){
//		return getRefNodeName().indexOf("客商")>=0;
//	}
//	protected String addBlurWherePart() {
//		
//		String blurWherePart = null;
//		
//		if (isIgnoreCase()){
//			// 处理模糊---但是不加入WherePart
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
//			} else { // 处理助记码检索为多条
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
