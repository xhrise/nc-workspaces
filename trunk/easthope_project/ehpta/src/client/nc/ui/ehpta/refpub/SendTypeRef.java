package nc.ui.ehpta.refpub;
import nc.ui.bd.ref.AbstractRefModel;

public class SendTypeRef extends AbstractRefModel{
	
	public SendTypeRef(){
		super();
		init();
	}

	private void init() {
		setRefNodeName("���䷽ʽ");
		setRefTitle("���䷽ʽ");
		setFieldCode(new String[]{
				"sendcode","sendname"
		});
		setFieldName(new String[]{
				"���䷽ʽ����","���䷽ʽ����"
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
