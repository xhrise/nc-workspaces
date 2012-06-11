package nc.ui.bi.query.designer;

import nc.ui.bd.ref.AbstractRefModel;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 业务函数参照 创建日期：(02-2-27 15:02:28)
 * 
 * @author：朱俊彬
 */
public class BusiFunctionModelRef extends AbstractRefModel {
	/**
	 * DicTableRef 构造子注解。
	 */
	public BusiFunctionModelRef() {
		super();
	}

	/**
	 * 默认显示字段中的显示字段数----表示显示前几个字段
	 */
	public int getDefaultFieldCount() {
		return 2;
	}

	/**
	 * 显示字段列表 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String[] getFieldCode() {
		return new String[] { "classname", "funcname" };
	}

	/**
	 * 显示字段中文名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo00378=类名
	 * @i18n miufo1001051=名称
	 */
	public java.lang.String[] getFieldName() {
		return new String[] { StringResource.getStringResource("miufo00378"), StringResource.getStringResource("miufo1001051") };
	}

	/**
	 * 主键字段名
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		//return "pk_busifunc";
		return "classname";
	}

	/**
	 * 参照标题 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo00379=业务函数参照
	 */
	public String getRefTitle() {
		return StringResource.getStringResource("miufo00379");
	}

	/**
	 * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return "bi_busifunc";
	}

	/**
	 * 查询条件 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getWherePart() {
		return "1=1";
	}
}
 