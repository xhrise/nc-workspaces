package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/*
 * ���ƣ���������
 * @author ����Դ
 * 2008-7-24 9:39:11
 * 
 * */

public class OuttypeRefModel extends AbstractRefModel {
	
	public OuttypeRefModel() {
		super();
	}
	
	@Override
	public String[] getFieldCode() {
		return new String[]{
               "typecode","typename","pk_outtype"
        };
	}

    @Override
	public String[] getFieldName() {
		return new String[]{
                "������ͱ���","�����������","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		return "��������";
	}

	@Override
	public String getTableName() {
		return "eh_outtype";
	}

	@Override
	public String getPkFieldCode() {
		return "pk_outtype";
	}

	
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 ";
    }



}
