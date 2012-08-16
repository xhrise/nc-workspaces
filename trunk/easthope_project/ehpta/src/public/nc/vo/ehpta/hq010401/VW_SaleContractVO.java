package nc.vo.ehpta.hq010401;

public class VW_SaleContractVO extends SaleContractVO {

	private static final long serialVersionUID = 6231594480313294424L;
	
	@Override
	public String getTableName() {
		
//		create or replace view vw_sale_contract as
//		select contract.*  from ehpta_sale_contract contract
//		left join ehpta_sale_contract_bs contract_bs on contract.pk_contract = contract_bs.pk_contract
//		left join (select nvl(sum(saleb.nnumber),0) nnumber , sale.pk_contract from so_saleorder_b saleb left join so_sale sale on saleb.csaleid = sale.csaleid where nvl(sale.dr,0)=0 and nvl(saleb.dr,0)=0 group by sale.pk_contract) orderb on orderb.pk_contract = contract_bs.pk_contract
//		where contract_bs.num > orderb.nnumber  and contract.vbillstatus = 1 and nvl(contract.dr,0) = 0and nvl(contract_bs.dr,0)=0
//		union all
//		select * from ehpta_sale_contract
//		where vbillstatus = 1 and pk_contract not in (select pk_contract from so_sale where pk_contract is not null and nvl(dr,0)=0) and nvl(dr,0)=0;
			
		
		return "vw_sale_contract"; // йсм╪
	}
}
