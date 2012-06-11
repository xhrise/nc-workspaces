/**
 * Form1.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import com.ufida.web.action.ActionForm;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.WebTreeModel;

/**
 * 类作用描述文字 ll 2006-01-17
 */
public class ImportDataForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String RB_VALUE_APPEND = "appand";

	public final static String RB_VALUE_REPLACE = "replace";

	//
	private String importType;

	private String dimID;

	private String queryID;
	
	private String spliterRuleType;
	
	private String oldQueryName ;

	private WebTreeModel query_model;

	private WebTableModel tableModel;
	
	private String currentUIFlag;//当前页面标志 
	
	private int  conflictDealType;//倒入时名称或编码出现冲突时的处理方式 

	//
	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getQueryID() {
		return queryID;
	}

	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}

	public WebTreeModel getQuery_model() {
		return query_model;
	}

	public void setQuery_model(WebTreeModel query_model) {
		this.query_model = query_model;
	}

	public WebTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(WebTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public String getDimID() {
		return dimID;
	}

	public void setDimID(String dimID) {
		this.dimID = dimID;
	}

	public String getCurrentUIFlag() {
		return currentUIFlag;
	}

	public void setCurrentUIFlag(String currentUIFlag) {
		this.currentUIFlag = currentUIFlag;
	}

	public String getOldQueryName() {
		return oldQueryName;
	}

	public void setOldQueryName(String oldQueryName) {
		this.oldQueryName = oldQueryName;
	}

	public String getSpliterRuleType() {
		return spliterRuleType;
	}

	public void setSpliterRuleType(String spliterRuleType) {
		this.spliterRuleType = spliterRuleType;
	}

	public int getConflictDealType() {
		return conflictDealType;
	}

	public void setConflictDealType(int conflictDealType) {
		this.conflictDealType = conflictDealType;
	}

	/**
	 * ActionForm属性值校验
	 * 
	 * @return 校验错误提示信息集合
	 */
	// public Vector validate() {
	// return null;
	// }
}
