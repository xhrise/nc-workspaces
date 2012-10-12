package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;


/**
 * 结算方式
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
public class BalanceTypeRefModel extends AbstractRefModel {

	public BalanceTypeRefModel() {
		init();
		// TODO 自动生成构造函数存根
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bd.ref.DefaultRefModel#setRefNodeName(java.lang.String)
	 */
	public void init() {
		setRefTitle("结算方式");
		setFieldCode(new String[] { "balancode", "balanname" }); // 7项
		setHiddenFieldCode(new String[] { "pk_balatype" });
		setTableName("bd_balatype");
		setPkFieldCode("pk_balatype");
		setWherePart(" (pk_corp='" + getPk_corp() + "' or pk_corp= '" + getGroupCode() + "' or pk_corp is null) and pk_balatype in ('0001A810000000000JBQ', '0001A810000000000JBW', '0001A810000000000JBY' , '0001A810000000000JC0') ");
		setSealedWherePart("sealflag is null or sealflag <>'Y'");
		resetFieldName();
	}

}
