package nc.vo.ehpta.hq010402;

public class SaleContractBHistoryVO extends SaleContractBVO {
	private static final long serialVersionUID = 5736246230481553851L;
	
	private String pk_contract_b_his;
	private Integer version_his;
	
	public String getPk_contract_b_his() {
		return pk_contract_b_his;
	}

	public void setPk_contract_b_his(String pk_contract_b_his) {
		this.pk_contract_b_his = pk_contract_b_his;
	}

	public Integer getVersion_his() {
		return version_his;
	}

	public void setVersion_his(Integer version_his) {
		this.version_his = version_his;
	}

	@Override
	public String getTableName() {
		return "ehpta_sale_contract_b_history";
	}
	
	@Override
	public String getParentPKFieldName() {
		return "pk_conthistory";
	}
	
	@Override
	public String getPrimaryKey() {
		return pk_contract_b_his;
	}
	
	@Override
	public void setPrimaryKey(String newPk_contract_b) {
		pk_contract_b_his = newPk_contract_b;
	}
	
	@Override
	public String getPKFieldName() {
		return "pk_contract_b_his";
	}
	
	@Override
	public String getEntityName() {
		return "ehpta_sale_contract_b_history";
	}

}
