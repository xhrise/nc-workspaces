/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * ���ܣ����ݿ��̵õ��ϼ�����(�ڶ����ۿ���)
 * @author wb
 * @date 2008-5-5 18:52:00
 */
public class CubasBySubcuRefModel extends AbstractRefModel {
	public static String pk_subcubasdoc = null;
	public CubasBySubcuRefModel() {
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
	        return 2;
	}
	    
	
	@Override
	public String[] getFieldCode() {
		// TODO �Զ����ɷ������
		return new String[]{
				"mnecode","custname","custcode","custshortname","taxpayerid","conaddr","phone2","pk_cumandoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
				"������","�ͻ�����","�ͻ�����","�ͻ����","��˰�˵ǼǺ�","ͨ�ŵ�ַ","�绰","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "�ͻ�����";
	}

	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_view_cumandoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_cumandoc";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" pk_corp = '"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and pk_cubasdoc in ")
        .append(" (")
        .append(" select pk_cubasdoc1 from bd_cubasdoc a,bd_cumandoc b")
        .append(" where a.pk_cubasdoc = b.pk_cubasdoc and pk_cumandoc = '"+pk_subcubasdoc+"'")
        .append(" and nvl(a.dr, 0) = 0 and nvl(b.dr,0)=0")
        .append(" union all")
        .append(" select a.pk_cubasdoc from bd_cubasdoc a,bd_cumandoc b")
        .append(" where a.pk_cubasdoc = b.pk_cubasdoc and pk_cumandoc = '"+pk_subcubasdoc+"'")
        .append(" and nvl(a.dr, 0) = 0 and nvl(b.dr,0)=0 ")
        .append(" ) ");
    	return sql.toString();
    }

}
