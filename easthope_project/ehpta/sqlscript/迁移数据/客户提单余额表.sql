

select pk_cumandoc , custname , pk_contract , concode , connamed , sum(firstmny) firstmny , sum(currmny) currmny , sum(salemny) salemny ,  
nvl(sum(firstmny) , 0) + nvl(sum(currmny) , 0) - nvl(sum(salemny) , 0) salebalance ,
nvl(sum(adtype2) , 0) adtype2 , nvl(sum(adtype3) , 0) adtype3 , nvl(sum(adtype4) , 0) adtype4 ,
nvl(sum(firstmny) , 0) + nvl(sum(currmny) , 0) - nvl(sum(salemny) , 0) + 
nvl(sum(adtype2) , 0) + nvl(sum(adtype3) , 0) + nvl(sum(adtype4) , 0) balance
from (

select cuman.pk_cumandoc , cubas.custname , sale.pk_contract , sale.concode , salecont.connamed , 0 firstmny , 0 currmny ,
sum(sale.nheadsummny) salemny , 0 adtype2 , 0 adtype3 , 0 adtype4  
from so_sale sale
left join bd_cumandoc cuman on cuman.pk_cumandoc = sale.ccustomerid
left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc
left join ehpta_adjust adjust on adjust.pk_contract = sale.pk_contract
left join ehpta_sale_contract salecont on salecont.pk_contract = sale.pk_contract
where pk_contract is not null 
and (contracttype = '10' or contracttype = '20')
and nvl(sale.dr , 0) = 0
and sale.dbilldate >= '2012-11-01' and sale.dbilldate <= '2012-11-30' -- 时间条件
group by cuman.pk_cumandoc , cubas.custname , sale.pk_contract , sale.concode , salecont.connamed

union all 

select cuman.pk_cumandoc , cubas.custname , adjust.pk_contract , salecont.vbillno , salecont.connamed , 0 firstmny , adjust.mny currmny ,
0 salemny , 0 adtype2 , 0 adtype3 , 0 adtype4 
from ehpta_adjust adjust
left join bd_cumandoc cuman on cuman.pk_cumandoc = adjust.pk_cubasdoc
left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc
left join ehpta_sale_contract salecont on salecont.pk_contract = adjust.pk_contract
where adjust.type = 1 and nvl(adjust.dr , 0) = 0 and adjust.pk_contract is not null 
and adjust.adjustdate >= '2012-11-01' and adjust.adjustdate <= '2012-11-30' -- 时间条件

union all 

select cuman.pk_cumandoc , cubas.custname , adjust.pk_contract , salecont.vbillno , salecont.connamed , 0 firstmny , 0 currmny ,
0 salemny , decode(adjust.type , 2 , adjust.mny , 0) adtype2 , 
decode(adjust.type , 3 , adjust.mny , 0) adtype3 , 
decode(adjust.type , 4 , adjust.mny , 0) adtype4 
from ehpta_adjust adjust
left join bd_cumandoc cuman on cuman.pk_cumandoc = adjust.pk_cubasdoc
left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc
left join ehpta_sale_contract salecont on salecont.pk_contract = adjust.pk_contract
where adjust.type <> 1 and nvl(adjust.dr , 0) = 0 and adjust.pk_contract is not null 
and adjust.adjustdate >= '2012-11-01' and adjust.adjustdate <= '2012-11-30' -- 时间条件

union all 

select balanceb.pk_cumandoc , balanceb.custname , balanceb.pk_contract , balanceb.concode , balanceb.connamed , balanceb.balance ,
0 , 0 , 0 , 0 , 0
from ehpta_calc_sale_balance_b balanceb
left join ehpta_calc_sale_balance_h balanceh on balanceh.pk_sale_balance = balanceb.pk_sale_balance
where balanceh.period = '2012-10' and balanceh.vbillstatus = 1 and nvl(balanceh.dr , 0) = 0
and nvl(balanceb.dr , 0) = 0 -- 查询上一期间的结余金额

) group by pk_cumandoc , custname , pk_contract , concode , connamed ;

-- select * from bd_cumandoc where pk_cumandoc = '0001A8100000000DUB5O'

-- select * from ehpta_adjust

-- select * from ehpta_calc_sale_balance_b

