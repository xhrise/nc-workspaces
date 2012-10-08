create or replace view vw_pta_rebates as 
select sale.ccustomerid pk_cumandoc ,cubas.custname , salecont.pk_contract , salecont.vbillno concode, orderb.cinventoryid pk_invmandoc ,  invbas.invname  ,  sale.period  , 
salecontb.num , sum(orderb.nnumber) nnumber , (nvl(sum(orderb.nnumber),0) / nvl(salecontb.num,0) * 100) comprate , 
nvl(salecontb.preprice,0) preprice , (nvl(sum(orderb.nnumber),0) * nvl(salecontb.preprice,0)) premny , 
0 adjustmny , (nvl(sum(orderb.nnumber),0) * nvl(salecontb.preprice,0)) actmny ,  WMSYS.WM_CONCAT(chr(39) || orderb.corder_bid || chr(39)) orderbids
, sale.pk_corp
from so_sale sale 

left join so_saleorder_b orderb on orderb.csaleid = sale.csaleid
left join ehpta_sale_contract salecont on salecont.pk_contract = sale.pk_contract
left join ehpta_sale_contract_b salecontb on salecontb.pk_contract = sale.pk_contract
left join bd_invbasdoc invbas on invbas.pk_invbasdoc = orderb.cinvbasdocid 
left join bd_cumandoc cuman on cuman.pk_cumandoc = sale.ccustomerid
left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc

where sale.pk_contract is not null and sale.contracttype = 20 and sale.fstatus = 2

and nvl(sale.dr,0)=0
and nvl(orderb.dr,0)=0
and nvl(salecont.dr,0)=0
and nvl(salecontb.dr,0)=0
and nvl(invbas.dr,0)=0
and nvl(cuman.dr,0)=0
and nvl(cubas.dr,0)=0

group by sale.ccustomerid , salecont.vbillno, invbas.invname  , sale.period , 
salecontb.num,salecontb.preprice,cubas.custname,salecont.pk_contract,orderb.cinventoryid ,
sale.pk_corp
order by salecont.vbillno asc;