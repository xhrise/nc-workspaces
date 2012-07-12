package nc.vo.ehpta.hq010402;

public class PrepolicyHistoryVO extends PrepolicyVO {

	private static final long serialVersionUID = -8775160868878766111L;
	
	private String pk_prepolicy_his;
	private Integer version_his;
	
	public String getPk_prepolicy_his() {
		return pk_prepolicy_his;
	}

	public void setPk_prepolicy_his(String pk_prepolicy_his) {
		this.pk_prepolicy_his = pk_prepolicy_his;
	}

	public Integer getVersion_his() {
		return version_his;
	}

	public void setVersion_his(Integer version_his) {
		this.version_his = version_his;
	}

	@Override
	public String getTableName() {
		return "ehpta_prepolicy_history";
	}
	
	@Override
	public String getParentPKFieldName() {
		return "pk_conthistory";
	}
	
	@Override
	public String getPrimaryKey() {
		return pk_prepolicy_his;
	}
	
	@Override
	public void setPrimaryKey(String newPk_prepolicy) {
		pk_prepolicy_his = newPk_prepolicy;
	}
	
	@Override
	public String getPKFieldName() {
		return "pk_prepolicy_his";
	}
	
	@Override
	public String getEntityName() {
		return "ehpta_prepolicy_history";
	}
}
