package nc.ui.ehpta.refpub;
import nc.ui.bd.ref.AbstractRefModel;

public class SendTypeRef extends AbstractRefModel{
	
	public SendTypeRef(){
		super();
		init();
	}

	private void init() {
		setRefNodeName("运输方式");
		setRefTitle("运输方式");
		setFieldCode(new String[]{
				"sendcode","sendname"
		});
		setFieldName(new String[]{
				"运输方式编码","运输方式名称"
		});
		setHiddenFieldCode(new String[]{
				"pk_sendtype"
		});
		setPkFieldCode("pk_sendtype");
		setTableName("bd_sendtype");
		setRefCodeField("sendname");
		setRefNameField("sendname");
	}
}
