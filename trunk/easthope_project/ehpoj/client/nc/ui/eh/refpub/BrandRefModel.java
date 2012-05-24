package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * @author ����Դ
 *
 */
public class BrandRefModel extends AbstractRefModel {
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public BrandRefModel(){
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 3;
    }

	@Override
	public String[] getFieldCode() {
		// TODO Auto-generated method stub
		return new String[]{
				"brandcode","brandname","pk_brand"
		};
	}

	@Override
	public String[] getFieldName() {
		// TODO Auto-generated method stub
		return new String[]{
				"Ʒ�Ʊ���","Ʒ������","����"
		};
	}

	@Override
	public String getPkFieldCode() {
		// TODO Auto-generated method stub
		return "pk_brand";
	}

	@Override
	public String getRefTitle() {
		// TODO Auto-generated method stub
		return "Ʒ��ά��";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "eh_brand";
	}
	
	@Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";
    }

	
}
