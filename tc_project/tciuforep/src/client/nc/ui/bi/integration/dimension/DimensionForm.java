/*
 * 创建日期 2006-2-13
 *
 */
package nc.ui.bi.integration.dimension;

import nc.ui.iufo.resmng.uitemplate.ResTreeObjForm;

import com.ufida.web.container.WebPanel;

/**
 * 任务对象Form
 * @author ll
 */
public class DimensionForm extends ResTreeObjForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dimName;
	private String dimCode;
	private String dimType;
	private String dimNote;
	private String createType;
	private String refDimID;
	private WebPanel createTypePanel;
	private boolean modifyUI ;
	
	public String getCreateType() {
		return createType;
	}
	public void setCreateType(String createType) {
		this.createType = createType;
	}
	public String getDimName() {
		return dimName;
	}
	public void setDimName(String dimName) {
		this.dimName = dimName;
	}
	public String getDimNote() {
		return dimNote;
	}
	public void setDimNote(String dimNote) {
		this.dimNote = dimNote;
	}
	public String getDimType() {
		return dimType;
	}
	public void setDimType(String dimType) {
		this.dimType = dimType;
	}
	public String getDimCode() {
		return dimCode;
	}
	public void setDimCode(String dimCode) {
		this.dimCode = dimCode;
	}
	public String getRefDimID() {
		return refDimID;
	}
	public void setRefDimID(String refDimID) {
		this.refDimID = refDimID;
	}
	public WebPanel getCreateTypePanel() {
		return createTypePanel;
	}
	public void setCreateTypePanel(WebPanel createTypePanel) {
		this.createTypePanel = createTypePanel;
	}
	public boolean isModifyUI() {
		return modifyUI;
	}
	public void setModifyUI(boolean modifyUI) {
		this.modifyUI = modifyUI;
	}

	//NOTICE 注意：需要UI提交的属性，必须是类本身的属性(父类属性目前不能完成提交效果)！//改善web框架才行
	//以下属性就是需要UI提交的父类属性
	/**
     * 右表选项
     */
 	private String bizTableSelectedID = null;
	/**
     *左树NodeID
     */
 	private String bizTreeSelectedID = null;
	public String getBizTableSelectedID() {
		return bizTableSelectedID;
	}

	public String getBizTreeSelectedID() {
		return bizTreeSelectedID;
	}

	public void setBizTableSelectedID(String bizTableSelectedID) {
		this.bizTableSelectedID = bizTableSelectedID;		
	}

	public void setBizTreeSelectedID(String bizTreeSelectedID) {
		this.bizTreeSelectedID = bizTreeSelectedID;		
	}
}
