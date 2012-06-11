/**
 * DimDataAuthEditForm.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-04-18
 */
package nc.ui.bi.dataauth;

import com.ufida.web.action.ActionForm;

/**
 * 数据权限编辑form,为报表纬度、纬度、安全策略的编辑的公共form
 * zyjun
 * 2006-04-18
 */
public class DataAuthEditForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
 	private boolean 	update;
     //
 	private String		repPK;
 	private String 	dimPK;
     //
 	private String 	dimMemberPK;
 	
 	private String 	authPK;
     //
 	private String 	autheePKs;
 	
 	private String[][] autheeListItems;
 	
 	private String		userField;
 	private String		dimField;
 	
 	private String[][] userFieldItems;
 	private String[][] dimFieldItems;
 	
     //
 	private int 		type;
     //
 	private boolean 	includeSelf;
     //
 	private boolean 	includeSlibe;
     //
 	private boolean 	includeAncestor;
     //
 	private boolean 	includeParent;
     //
 	private boolean 	includeChild;
     //
 	private boolean 	includeOffSpring;
 	
 	private String		langCode;
 
	public String[][] getAutheeListItems() {
		return autheeListItems;
	}
	public void setAutheeListItems(String[][] autheeListItems) {
		this.autheeListItems = autheeListItems;
	}
	public String getAutheePKs() {
		return autheePKs;
	}
	public void setAutheePKs(String autheePKs) {
		this.autheePKs = autheePKs;
	}
	public String getAuthPK() {
		return authPK;
	}
	public void setAuthPK(String authPK) {
		this.authPK = authPK;
	}
	public String getDimField() {
		return dimField;
	}
	public void setDimField(String dimField) {
		this.dimField = dimField;
	}
	public String[][] getDimFieldItems() {
		return dimFieldItems;
	}
	public void setDimFieldItems(String[][] dimFieldItems) {
		this.dimFieldItems = dimFieldItems;
	}
	public String getDimMemberPK() {
		return dimMemberPK;
	}
	public void setDimMemberPK(String dimMemberPK) {
		this.dimMemberPK = dimMemberPK;
	}
	public String getDimPK() {
		return dimPK;
	}
	public void setDimPK(String dimPK) {
		this.dimPK = dimPK;
	}
	public boolean isIncludeAncestor() {
		return includeAncestor;
	}
	public void setIncludeAncestor(boolean includeAncestor) {
		this.includeAncestor = includeAncestor;
	}
	public boolean isIncludeChild() {
		return includeChild;
	}
	public void setIncludeChild(boolean includeChild) {
		this.includeChild = includeChild;
	}
	public boolean isIncludeOffSpring() {
		return includeOffSpring;
	}
	public void setIncludeOffSpring(boolean includeOffSpring) {
		this.includeOffSpring = includeOffSpring;
	}
	public boolean isIncludeParent() {
		return includeParent;
	}
	public void setIncludeParent(boolean includeParent) {
		this.includeParent = includeParent;
	}
	public boolean isIncludeSelf() {
		return includeSelf;
	}
	public void setIncludeSelf(boolean includeSelf) {
		this.includeSelf = includeSelf;
	}
	public boolean isIncludeSlibe() {
		return includeSlibe;
	}
	public void setIncludeSlibe(boolean includeSlibe) {
		this.includeSlibe = includeSlibe;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public String getRepPK() {
		return repPK;
	}
	public void setRepPK(String repPK) {
		this.repPK = repPK;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	public String getUserField() {
		return userField;
	}
	public void setUserField(String userField) {
		this.userField = userField;
	}
	public String[][] getUserFieldItems() {
		return userFieldItems;
	}
	public void setUserFieldItems(String[][] userFieldItems) {
		this.userFieldItems = userFieldItems;
	}
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <FormVO name="DimDataAuthEditForm" package="nc.ui.bi.dataauth">
      <FieldsVO bUpdate="boolean" dimMemberPK="String" dimPK="String" includeAncestor="int" includeChild="int" includeOffSpring="int" includeParent="int" includeSelf="int" includeSlibe="int" strUserPKs="String[]" type="int">
      </FieldsVO>
    </FormVO>
@WebDeveloper*/