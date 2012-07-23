
package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 发运方式
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
public class SendTypeDefaultRefModel extends AbstractRefModel {

	public SendTypeDefaultRefModel() {
		super();
		init();
		
	}
	public void init() {

		setFieldCode(new String[] { "pk_sendtype" , "sendcode", "sendname" }); //4项
		
		setHiddenFieldCode(new String[] { "pk_sendtype" });
		
		setTableName("bd_sendtype");
		
		setPkFieldCode("pk_sendtype");
		
		setRefTitle("运输方式");
		
		setRefCodeField("pk_sendtype");
		
		setRefNameField("sendname");
		
		setWherePart(" pk_corp='" + getPk_corp() + "' or pk_corp= '" + getGroupCode() + "' or pk_corp is null");

		setOrderPart("  ");
		
		resetFieldName();

	}

}
