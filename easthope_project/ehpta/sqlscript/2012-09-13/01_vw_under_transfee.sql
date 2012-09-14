create or replace view vw_under_transfee as 
select genh.cgeneralhid , genh.vbillcode cbillno , genb.cinventoryid def1 , invbas.invname def2 , genh.daccountdate sdate , 
genh.cwarehouseid pk_sstordoc , stordoc.storname def3 , stordoc.storaddr saddress , 
address.addrname eaddress , transcontb.piersfee , transcontb.inlandshipfee , transcontb.carfee , genh.pk_transport , genh.pk_transport_b ,
genb.noutnum num , genh.transprice fee , (nvl(genh.transprice,0) * nvl(genb.noutnum,0)) transmny 
,genb.cgeneralbid def5 , genh.ccustomerid def6 , genh.pk_contract def4
from ic_general_h genh
left join ic_general_b genb on genh.cgeneralhid = genb.cgeneralhid
left join bd_stordoc stordoc on stordoc.pk_stordoc = genh.cwarehouseid
left join ehpta_transport_contract_b transcontb on transcontb.pk_transport_b = genh.pk_transport_b
left join bd_address address on address.pk_address = transcontb.estoraddr
left join bd_invbasdoc invbas on invbas.pk_invbasdoc = genb.cinvbasid

where decode(genh.vuserdef3 , 'Y' , 'Y' , 'N' , 'N' , 'N') = 'N' and genh.concode is not null
and (nvl(genh.contracttype , 0) = 10 or nvl(genh.contracttype , 0) = 20) and nvl(genh.transprice,0) > 0
and genh.daccountdate is not null