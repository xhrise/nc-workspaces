/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * ���ܣ����Ͽ��(��Ʒ�������)
 * @author wb
 * @date 2008-12-2 15:32:56
 */
public class InvdocKCRefModel extends AbstractRefModel {
	public static String pk_cubasdoc;
	 private nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
	public InvdocKCRefModel() {
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
               "invcode","invmnecode","invname","invspec","invtype","def1","brandname","amount","pk_invbasdoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "���ϱ���","������","��������","���","�ͺ�","��ɫ","Ʒ��","���","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "���Ͽ��";
	}

	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_kcinvbasdoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_invbasdoc";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" isnull(is_flag,0) = 1 and pk_corp = '"+ce.getCorporation().getPk_corp()+"'");
    	return sql.toString();
    }

}
