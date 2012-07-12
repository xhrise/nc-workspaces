package nc.vo.ehpta.hq010402;

public class AidcustHistoryVO extends AidcustVO {

	private static final long serialVersionUID = -3684010643388853320L;
	
	private String pk_aidcust_his;
	private Integer version_his;
	
	public String getPk_aidcust_his() {
		return pk_aidcust_his;
	}

	public void setPk_aidcust_his(String pk_aidcust_his) {
		this.pk_aidcust_his = pk_aidcust_his;
	}

	public Integer getVersion_his() {
		return version_his;
	}

	public void setVersion_his(Integer version_his) {
		this.version_his = version_his;
	}

	@Override
	public String getTableName() {
		return "ehpta_aidcust_history";
	}
	
	@Override
	public String getParentPKFieldName() {
		return "pk_conthistory";
	}
	
	@Override
	public String getPrimaryKey() {
		return pk_aidcust_his;
	}
	
	@Override
	public void setPrimaryKey(String key) {
		pk_aidcust_his = key;
	}
	
	@Override
	public String getEntityName() {
		return "ehpta_aidcust_history";
	}
}
