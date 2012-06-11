package nc.ui.bi.query.designer;

import nc.ui.bd.ref.AbstractRefModel;
import nc.vo.iufo.pub.IDatabaseNames;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 查询模型参照 创建日期：(02-2-27 15:02:28)
 * 
 * @author：朱俊彬
 */
public class QueryModelRef extends AbstractRefModel {
	/**
	 * DicTableRef 构造子注解。
	 */
	public QueryModelRef() {
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
		return new String[] { "pk_querymodel", "queryname" };
	}

	/**
	 * 显示字段中文名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo1001384=主键
	 * @i18n miufo1003134=名称
	 */
	public java.lang.String[] getFieldName() {
		return new String[] { StringResource.getStringResource("miufo1001384"), StringResource.getStringResource("miufo1003134") };
	}

	/**
	 * 主键字段名
	 * 
	 * @return java.lang.String
	 */
	public String getPkFieldCode() {
		return "pk_querymodel";
	}

	/**
	 * 参照标题 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 * @i18n miufo00304=查询对象参照
	 */
	public String getRefTitle() {
		return StringResource.getStringResource("miufo00304");
	}

	/**
	 * 参照数据库表或者视图名 创建日期：(01-4-4 0:57:23)
	 * 
	 * @return java.lang.String
	 */
	public String getTableName() {
		return IDatabaseNames.BI_QUERY;
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
 