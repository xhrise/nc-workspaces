/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * ���ܣ����ݿ��̵õ�����(���۶���)
 * @author wb
 * @date 2008-5-5 18:52:00
 */
public class InvdocByCusdocRefModel extends AbstractRefModel {
	public static String pk_cubasdoc;
	public InvdocByCusdocRefModel() {
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
	        return 3;
	}
	    
	
	@Override
	public String[] getFieldCode() {
		// TODO �Զ����ɷ������
		return new String[]{
               "invcode","invmnecode","invname","invspec","invtype","colour","brandname","pk_invmandoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "���ϱ���","������","��������","���","�ͺ�","��ɫ","Ʒ��","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "���ϵ���";
	}

	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_view_invmandoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_invmandoc";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" pk_invmandoc in")
        .append(" (")
        .append(" select pk_invbasdoc from eh_custkxl")
        .append(" where pk_cubasdoc like '"+pk_cubasdoc+"'")
        .append(" and nvl(dr,0)=0")
        .append(" )");
    	return sql.toString();
    }

}
