/*
 * 创建日期 2006-4-18
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.dataauth;

import com.ufida.web.action.ActionForm;

/**
 * @author zyjun
 *
 * 
 */
public class DataAuthMngForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	String		dimPK;
	private	String  	repPK;
	
	private    String[][]  dimItems;
	
	public String getDimPK() {
		return dimPK;
	}
	public void setDimPK(String dimPK) {
		this.dimPK = dimPK;
	}
	public String getRepPK() {
		return repPK;
	}
	public void setRepPK(String repPK) {
		this.repPK = repPK;
	}
	
	public String[][] getDimItems() {
		return dimItems;
	}
	public void setDimItems(String[][] dimItems) {
		this.dimItems = dimItems;
	}
}
