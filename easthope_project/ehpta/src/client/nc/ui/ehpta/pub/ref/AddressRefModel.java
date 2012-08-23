package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.vo.ml.NCLangRes4VoTransl;


/**
 * 地点档案
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计
 * <p>
 * 
 * @version NC5.0
 * @author sxj
 */
public class AddressRefModel extends AbstractRefGridTreeModel {

	public AddressRefModel() {
		init();
		// TODO 自动生成构造函数存根
	}

	public void init() {
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
		//
		setFieldCode(new String[] { "addrcode", "addrname" });	

		setHiddenFieldCode(new String[] { "pk_address", "bd_address.pk_areacl" });
		
		setPkFieldCode("pk_address");
		
		setWherePart("where ( bd_address.pk_corp ='0001' or bd_address.pk_corp='"
				+ getPk_corp() + "') ");
		
		setTableName("bd_address");
		
		setRefCodeField("addrcode");
		
		setRefNameField("addrname");
		
		setRefTitle("地点档案");
		
		setDefaultFieldCount(2);
		setDocJoinField("bd_address.pk_areacl");
		setOrderPart(" addrcode");

		resetFieldName();

	}
}
