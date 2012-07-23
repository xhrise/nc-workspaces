
package nc.ui.ehpta.pub.ref;

import nc.ui.bd.ref.AbstractRefModel;
/**
 * 仓库档案
 * <p>
 * <strong>提供者：</strong>UAP
 * <p>
 * <strong>使用者：</strong>
 * 
 * <p>
 * <strong>设计状态：</strong>详细设计 
 * <p>
 * @version 	NC5.0
 * @author 		sxj
 */
public class StorDocDefaulteRefModel extends AbstractRefModel {

	public StorDocDefaulteRefModel() {
		super();
		
		init();
		// TODO 自动生成构造函数存根
	}
	public void init() {
		
		setRefTitle("仓库档案");
		
		setFieldCode(new String[] { "pk_stordoc" , "storcode", "storname", "storaddr" }); //19项
		
		setFieldName(new String[] { "主键" , "仓库编码" , "仓库名称" , "仓库地址"});
		
		setHiddenFieldCode(new String[] { "pk_stordoc" });
		
		setTableName("bd_stordoc");
		
		setPkFieldCode("pk_stordoc");
		
		setRefCodeField("pk_stordoc");
		
		setRefNameField("storname");
		
		String subSqlCalBody_dataPower = getDataPowerSubSql("bd_calbody",nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC000-0001825")/*@res "库存组织"*/);
		
		String subSqlCalBoby = "select pk_calbody from bd_calbody where sealflag is null or sealflag='N'";
		
		if (subSqlCalBody_dataPower !=null) {
			if (subSqlCalBody_dataPower.trim().equals(" 1 < 0 ")) 
				subSqlCalBoby = subSqlCalBoby + " and (" + subSqlCalBody_dataPower + ")";
			 else 
				subSqlCalBoby = subSqlCalBoby + " and  pk_calbody in (" + subSqlCalBody_dataPower+")";;

		}
		
		setWherePart(" pk_corp='" + getPk_corp() + "' and pk_calbody in ("+subSqlCalBoby+")");
		
		setOrderPart(" bd_stordoc.pk_stordoc ");
		
	}

}
