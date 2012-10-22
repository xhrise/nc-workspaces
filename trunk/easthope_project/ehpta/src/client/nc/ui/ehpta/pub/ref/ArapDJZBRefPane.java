package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;

public class ArapDJZBRefPane extends AbstractRefModel {

	public ArapDJZBRefPane() {
		super();
		
		init();
	}
	
	private final void init() { 
		setRefNodeName("�տ");
		setRefTitle("�տ");
		setFieldCode(new String[] {
			"vouchid",
			"djbh",
			"djrq",
			"dfbbje",
			
		});
		
		setFieldName(new String[] {
			"����",
			"�տ��",
			"�տ�����",
			"�տ���",
		});
		
		setHiddenFieldCode(new String[]{"vouchid"});
		
		setPkFieldCode("vouchid");
		
		setRefCodeField("dfbbje");
		
		setRefNameField("djbh");
		
		StringBuilder builder = new StringBuilder();
		builder.append(" (select djzb.vouchid , djzb.djbh , djzb.djrq , djfb.dfbbje , ");
		builder.append(" djzb.zyx8 , djzb.zyx6 , djfb.hbbm , djfb.dwbm from arap_djzb djzb ");
		builder.append(" left join arap_djfb djfb on djfb.vouchid = djzb.vouchid ");
		builder.append(" where nvl(djfb.dr,0) = 0 and nvl(djzb.dr,0) = 0 ");
		builder.append(" and djfb.dwbm = '"+getPk_corp()+"' and djzb.zyx8 = 'Y' and djzb.spzt = '1' ");
		builder.append(" order by djrq desc) ");
		
		setTableName(builder.toString() + " arap_djzb ");
		
		setWherePart(" 1 = 1  " );
		
		setOrderPart(" arap_djzb.vouchid ");
		
	}
}
