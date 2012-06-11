package nc.vo.tc.imp;

import nc.vo.pub.SuperVO;

public class KeywordVO extends SuperVO {

	private String pk_keyword;
	private String name;
	private int type;
	private String isbuiltin;
	private int len;
	private String isseal;
	private int key_count;
	private String ver;
	
	
	
	public String getIsbuiltin() {
		return isbuiltin;
	}

	public void setIsbuiltin(String isbuiltin) {
		this.isbuiltin = isbuiltin;
	}

	public String getIsseal() {
		return isseal;
	}

	public void setIsseal(String isseal) {
		this.isseal = isseal;
	}

	public int getKey_count() {
		return key_count;
	}

	public void setKey_count(int key_count) {
		this.key_count = key_count;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPk_keyword() {
		return pk_keyword;
	}

	public void setPk_keyword(String pk_keyword) {
		this.pk_keyword = pk_keyword;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

}
