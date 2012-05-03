package nc.vo.hi.hi_307;

import nc.vo.pub.CircularlyAccessibleValueObject;

import nc.vo.pub.ValidationException;

public class KeyPersonGrpVO extends CircularlyAccessibleValueObject implements
		Cloneable {

	private String pk_keypsn_group;
	private String pk_corp;
	private String group_code;
	private String group_name;
	private String group_desc;
	private String creator;
	private String createDate;
	private boolean isSealed;
	private String sealedDate;

	
	
	@Override
	public String[] getAttributeNames() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public Object getAttributeValue(String attributeName) {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public void setAttributeValue(String name, Object value) {
		// TODO 自动生成方法存根

	}

	@Override
	public String getEntityName() {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public void validate() throws ValidationException {
		// TODO 自动生成方法存根

	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getGroup_code() {
		return group_code;
	}

	public void setGroup_code(String group_code) {
		this.group_code = group_code;
	}

	public String getGroup_desc() {
		return group_desc;
	}

	public void setGroup_desc(String group_desc) {
		this.group_desc = group_desc;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public boolean isSealed() {
		return isSealed;
	}

	public void setSealed(boolean isSealed) {
		this.isSealed = isSealed;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_keypsn_group() {
		return pk_keypsn_group;
	}

	public void setPk_keypsn_group(String pk_keypsn_group) {
		this.pk_keypsn_group = pk_keypsn_group;
	}

	public String getSealedDate() {
		return sealedDate;
	}

	public void setSealedDate(String sealedDate) {
		this.sealedDate = sealedDate;
	}
	
	public String toString(){
		return getGroup_code()+" "+getGroup_name();
	}

}
