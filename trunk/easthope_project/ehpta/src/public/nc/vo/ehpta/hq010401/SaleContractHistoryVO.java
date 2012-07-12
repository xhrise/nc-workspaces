package nc.vo.ehpta.hq010401;

public class SaleContractHistoryVO extends SaleContractVO {

	private static final long serialVersionUID = -7551458649976527192L;
	
	private String pk_contract_his;
	
	public String getPk_contract_his() {
		return pk_contract_his;
	}

	public void setPk_contract_his(String pk_contract_his) {
		this.pk_contract_his = pk_contract_his;
	}

	@Override
	public String getTableName() {
		return "ehpta_sale_contract_history";
	}
	
	@Override
	public String getPrimaryKey() {
		return pk_contract_his;
	}
	
	@Override
	public void setPrimaryKey(String newPk_contract) {
		pk_contract_his = newPk_contract;
	}
	
	@Override
	public String getPKFieldName() {
		return "pk_contract_his";
	}
	
	@Override
	public String getEntityName() {
		return "ehpta_sale_contract_history";
	}
}
