package nc.ui.eh.itfrefpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;
/**
 * U8ģ���ֶβ���
 * wb
 * 2008-7-11 14:47:28
 * */
public class TColumRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
    public static String pk_billtype = null;
    public static String tablename_h = null;
    public static String tablename_b = null;
    public TColumRefModel() {
		super();
		// TODO �Զ����ɹ��캯�����
	}
	
    /**
     * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
     */
    @Override
	public int getDefaultFieldCount() {
        return 3;
    }
    
	@Override
	public String[] getFieldCode() {
		// TODO �Զ����ɷ������
		return new String[]{
               "defaultshowname","itemkey","case pos when 0 then '��ͷ' when 1 then '����' when 2 then '��β' end","pk_billtemplet_b"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
				"����","�ֶ�","ģ��λ��","����"
			};
	}
    
	    
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "ƾ֤ģ���ֶβ���";
	}
	
	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "pub_billtemplet_b";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_billtemplet_b";
	}

	
    @Override
	public String getWherePart() {
        StringBuffer whereSQL = new StringBuffer()
//        .append(" pk_billtemplet in (select pk_billtemplet from pub_billtemplet where pk_billtypecode = '"+pk_billtype+"') ")
//        .append(" and itemkey in (select b.name from sys.tables a , sys.all_columns b,sys.types c ")
//        .append(" where a.object_id = b.object_id and c.system_type_id = b.system_type_id")
//        .append(" and c.name = 'numeric' and a.name in( '"+tablename_h+"','"+tablename_b+"'))")
//        .append(" and isnull(dr,0)=0 ");
        .append(" table_code in('"+tablename_h+"','"+tablename_b+"') and isnull(dr,0)=0 ");
    	return whereSQL.toString();// and itemkey not like 'v%'
    }
    

}
