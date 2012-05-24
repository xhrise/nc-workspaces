
package nc.ui.eh.refpub;

/*
 * ���ܣ�����������
 * ���ߣ�zqy
 * ʱ�䣺2008-9-10 11:12:35
 */

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class FytypeRefModel extends AbstractRefModel   {
	
    ClientEnvironment ce = ClientEnvironment.getInstance();
    
    public FytypeRefModel() {
        super();
    }
    @Override
	public String[] getFieldCode() {
        return new String[]{
               "fylbcode","fylbname","pk_fylb"
        };
    }

    @Override
	public String[] getFieldName() {
        return new String[]{
                "���ͱ���","��������","����"
            };
    }

    
    @Override
	public String getRefTitle() {
        return "�������";
    }

    @Override
	public String getTableName() {
        return "eh_arap_fylb";
    }

    @Override
	public String getPkFieldCode() {
        return "pk_fylb";
    }
    
    @Override
	public String getWherePart() {
        return " isnull(dr,0)=0 and pk_corp='"+ce.getCorporation().getPk_corp()+"' ";
    }

    
}

