package nc.ui.ehpta.refpub;

import nc.ui.bd.ref.AbstractRefModel;

public class SendckRef extends AbstractRefModel {
	
	public SendckRef(){
		super();
		init();
	}

	private void init() {
		setRefNodeName("�ֿ⵵��");
		setRefTitle("�ֿ⵵��");
		setFieldCode(new String[]{
				"storcode","storname","storaddr"
		});
		setFieldName(new String[]{
				"�ֿ����","�ֿ�����","�ֿ��ַ"
		});
		setHiddenFieldCode(new String[]{
				"pk_stordoc"
		});
		setPkFieldCode("pk_stordoc");
		setTableName("bd_stordoc");
		setRefCodeField("pk_stordoc");
		setRefNameField("storname");
	}

}

