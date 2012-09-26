package nc.vo.ehpta.hq010401;

public class VW_SaleContractVO extends SaleContractVO {

	private static final long serialVersionUID = 6231594480313294424L;
	
	@Override
	public String getTableName() {
		
//		create or replace view vw_pta_sale_contract as
//		select contract.*  from ehpta_sale_contract contract
//	    left join ehpta_sale_contract_bs contract_bs on contract.pk_contract = contract_bs.pk_contract
//	    left join (select nvl(sum(saleb.nnumber),0) nnumber , sale.pk_contract from so_saleorder_b saleb left join so_sale sale on saleb.csaleid = sale.csaleid where nvl(sale.dr,0)=0 and nvl(saleb.dr,0)=0 group by sale.pk_contract) orderb on orderb.pk_contract = contract_bs.pk_contract
//	    where contract_bs.num > orderb.nnumber  and contract.vbillstatus = 1 and nvl(contract.dr,0) = 0and nvl(contract_bs.dr,0)=0
//	    union all
//	    select "PK_CONTRACT","VBILLNO","CONTYPE","PURCHCODE","PURCHNAME","BARGAINOR","ORDERDATE","ORDERADDRESS","CONNAMED","SDATE","EDATE","PK_DEPTDOC","PK_PSNDOC","REBATE_FLAG","POUNDSOFPOOR","TERMINATION","VERSION","STOPCONTRACT","PAY_CUTOFFDATE","DELIVERYDATE","MEMO","VBILLSTATUS","PK_CORP","PK_BUSITYPE","PK_BILLTYPE","VAPPROVEID","DAPPROVEDATE","VOPERATORID","VAPPROVENOTE","DMAKEDATE","DEF1","DEF2","DEF3","DEF4","DEF5","DEF6","DEF7","DEF8","DEF9","DEF10","TS","DR","CUSTCODE","CLOSE_FLAG","CONTPRICE" from ehpta_sale_contract
//	    where vbillstatus = 1 and pk_contract not in (select pk_contract from so_sale where pk_contract is not null and nvl(dr,0)=0) and nvl(dr,0)=0;
			
		
		return "vw_pta_sale_contract"; // йсм╪
	}
}
