/*
 * Created on 2006-9-4
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;

/**
 * ���ܣ������豸�õ����豸��ά����Ŀ
 * @author wb
 * @date 2008-12-20 18:27:25
 */
public class SbwxXMByPksbRefModel extends AbstractRefModel {
	public static String pk_sb;
	public SbwxXMByPksbRefModel() {
		super();
	}
	
	@Override
	public int getDefaultFieldCount() {
	        return 4;
	}
	    
	
	@Override
	public String[] getFieldCode() {
		// TODO �Զ����ɷ������
		return new String[]{
               "project","project projecta","period","lastdate","pk_sb_b"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "ά����Ŀ","ά����Ŀ","ά������","���ά��ʱ��","����"
			};
	}

	
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "�豸ά����Ŀ";
	}

	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "eh_sc_sbbasdoc_b";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_sb_b";
	}

	
    @Override
	public String getWherePart() {
    	StringBuffer sql = new StringBuffer()
        .append(" isnull(dr,0)=0 and pk_sb = '"+pk_sb+"'");
    	return sql.toString();
    }

}
