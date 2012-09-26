package nc.vo.ehpta.hq010401;

public class VW_SaleContractBsVO extends SaleContractBsVO {

	private static final long serialVersionUID = 4750205753607165534L;
	
	@Override
	public String getTableName() {
		
//		create or replace view vw_pta_sale_contract_bs as
//		select contract_bs.pk_contract_b,
//		       contract_bs.pk_contract,
//		       contract_bs.pk_invbasdoc,
//		       contract_bs.invname,
//		       contract_bs.invspec,
//		       contract_bs.pk_measdoc,
//		       (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0)) num,
//		       round(case when invbas.unitweight = 0 then 0 else ((nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0)) / invbas.unitweight) end,0) numof,
//		       contract_bs.taxprice,
//		       (contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) sumpricetax ,
//		       contract_bs.taxrate,
//		       round(((contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) - ((contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) / (1 + contract_bs.taxrate / 100))),2) tax,
//		       round(((contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) / (1 + contract_bs.taxrate / 100)),2) notaxloan,
//		       contract_bs.memo,
//		       contract_bs.dr,
//		       contract_bs.ts,
//		       contract_bs.def1,
//		       contract_bs.def2,
//		       contract_bs.def3,
//		       contract_bs.def4,
//		       contract_bs.def5,
//		       contract_bs.def6,
//		       contract_bs.def7,
//		       contract_bs.def8,
//		       contract_bs.def9,
//		       contract_bs.def10
//		  from ehpta_sale_contract_bs contract_bs
//		left join (select nvl(sum(saleb.nnumber),0) nnumber , sale.pk_contract from so_saleorder_b saleb left join so_sale sale on saleb.csaleid = sale.csaleid where nvl(sale.dr,0)=0 and nvl(saleb.dr,0)=0 group by sale.pk_contract) orderb on orderb.pk_contract = contract_bs.pk_contract
//		left join bd_invmandoc invman on invman.pk_invmandoc = contract_bs.pk_invbasdoc
//		left join bd_invbasdoc invbas on invbas.pk_invbasdoc = invman.pk_invbasdoc
//		where nvl(to_number(contract_bs.num),0) > nvl(to_number(orderb.nnumber),0) and nvl(contract_bs.dr,0)=0;
		
		return "vw_pta_sale_contract_bs"; // йсм╪
	}
	

}
