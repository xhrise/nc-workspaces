
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefTreeModel;
import nc.ui.pub.ClientEnvironment;

/*
 * ��֯������
 */

public class InvclRefModel extends AbstractRefTreeModel   {
    
    ClientEnvironment ce = ClientEnvironment.getInstance();
    public InvclRefModel() {	
        super();
        
    }

    @Override
	public int getDefaultFieldCount() {
        return 3;
    }
    
    @Override
	public String[] getFieldCode() {
        return new String[]{
               "invclasscode","invclassname","pk_invcl"
        };
    }

    @Override
	public String[] getFieldName() {
        return new String[]{
                "�������","��������","����"
            };
    }

    
    @Override
	public String getRefTitle() {
        return "���Ϸ���";
    }

    @Override
	public String getTableName() {
        return "bd_invcl";
    }

    @Override
	public String getPkFieldCode() {
        return "pk_invcl";
    }

    
    @Override
	public String getWherePart() {
        return " 1=1 and isnull(dr,0)=0 ";
    }

}

