create or replace view vw_pta_settlement as
select sale.csaleid , sale.pk_contract , mt.pk_maintain , orderb.cinvbasdocid , sale.ccustomerid ,
cubas.custname , sale.concode ,  sale.vreceiptcode , sale.dmakedate cmakedate ,
invbas.invcode , invbas.invname , meas.measname , orderb.nnumber , orderb.lastingprice ,
(orderb.lastingprice * orderb.nnumber) clastingmny , mt.settlemny , (mt.settlemny * orderb.nnumber) csettlemny ,
((orderb.lastingprice - mt.settlemny) * orderb.nnumber) clsmny , sale.settleflag , sale.settledate ,
sale.dr
from so_sale sale
left join so_saleorder_b orderb on orderb.csaleid = sale.csaleid
left join bd_invbasdoc invbas on invbas.pk_invbasdoc = orderb.cinvbasdocid
left join bd_measdoc meas on meas.pk_measdoc = invbas.pk_measdoc
left join ehpta_maintain mt on substr(mt.maindate , 0 , 7) = substr(sale.dmakedate , 0 , 7)
left join bd_cumandoc cuman on cuman.pk_cumandoc = sale.ccustomerid
left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc

where sale.contracttype is not null
and (sale.contracttype = 20)
and sale.pk_contract is not null
and mt.type = 2
and nvl(orderb.lastingprice,0) <> nvl(mt.settlemny,0)
and mt.pk_invbasdoc = orderb.cinvbasdocid
and mt.vbillstatus = 1
and sale.fstatus = 2

and nvl(sale.dr , 0) = 0
and nvl(orderb.dr ,0) = 0
and nvl(invbas.dr ,0) = 0
and nvl(meas.dr ,0) = 0
and nvl(mt.dr ,0) = 0;
