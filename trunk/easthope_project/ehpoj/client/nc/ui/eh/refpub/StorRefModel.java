package nc.ui.eh.refpub;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

public class StorRefModel extends AbstractRefModel {
    ClientEnvironment ce = nc.ui.pub.ClientEnvironment.getInstance();
    String pk_corp=ce.getCorporation().getPk_corp();
    String Corporation=ce.getUser().getPrimaryKey();
    
	public StorRefModel() {
		super();
		// TODO �Զ����ɹ��캯�����
	}
	
    /**
     * Ĭ����ʾ�ֶ��е���ʾ�ֶ���----��ʾ��ʾǰ�����ֶ�
     */
    @Override
	public int getDefaultFieldCount() {
        return 2;
    }
      
	@Override
	public String[] getFieldCode() {
		// TODO �Զ����ɷ������
		return new String[]{
               "storcode","storname","pk_stordoc"
        };
	}

    @Override
	public String[] getFieldName() {
		// TODO �Զ����ɷ������
		return new String[]{
                "�ֿ����","�ֿ�����","����"
			};
	}
    
	     
	@Override
	public String getRefTitle() {
		// TODO �Զ����ɷ������
		return "�ֿ⵵��";
	}
	
	@Override
	public String getTableName() {
		// TODO �Զ����ɷ������
		return "bd_stordoc";
	}

	@Override
	public String getPkFieldCode() {
		// TODO �Զ����ɷ������
		return "pk_stordoc";
	}

	
//    @Override
//	public String getWherePart() {
//        return " nvl(dr,0)=0 and pk_corp='"+pk_corp+"' and pk_stordoc in (" +
//        		"select pk_bdstordoc from  eh_stordoc where (pk_stordoc in (select pk_stordoc from eh_stordoc_b   where cuserid ='"+Corporation+"' ) or pk_psndoc = '"+Corporation+"') and nvl(dr,0)=0) ";
//    }
    
	/**�����ٱ�Ҫ��ȥ�����ֿ�Ȩ�޿������� add by zqy 2010��11��23��10:51:22**/
	public String getWherePart() {
        //return " nvl(dr,0)=0 and pk_corp='"+pk_corp+"' and storcode not like 'z%'";
        return " nvl(dr,0)=0 and pk_corp='"+pk_corp+"' and storcode not like 'z%' and pk_stordoc in (select pk_bdstordoc from eh_stordoc where pk_corp='"+pk_corp+"' and nvl(dr,0)=0)";
    }

}
