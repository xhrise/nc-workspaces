
select decode(receivable.rowno , null , sale.rowno , receivable.rowno) rowno , receivable.dates , receivable.balance , sale.dbilldate , 
sale.vreceiptcode , sale.nnumber , sale.dbizdate , sale.noutnum , sale.ntaxprice , sale.nsummny , sale.vnotebody
from (
select 0 rowno , 'ÉÏÆÚÆÚÄ©Óà¶î' dates , sum(ehpta_calc_sale_balance_b.balance) balance , ehpta_calc_sale_balance_b.pk_cumandoc 
from ehpta_calc_sale_balance_b 
left join ehpta_calc_sale_balance_h on ehpta_calc_sale_balance_h.pk_sale_balance = ehpta_calc_sale_balance_b.pk_sale_balance
where ehpta_calc_sale_balance_h.period = case when substr('2012-11-01' , 6 , 2) - 1 < 10 and substr('2012-11-01' , 6 , 2) - 1 > 0 
then substr('2012-11-01' , 0 , 4) || '-0' || to_char(substr('2012-11-01' , 6 , 2) - 1)
when substr('2012-11-01' , 6 , 2) - 1 < 1 then to_char(substr('2012-11-01' , 0 , 4) - 1) || '-12'
else substr('2012-11-01' , 0 , 4) || '-' || to_char(substr('2012-11-01' , 6 , 2) - 1) end and nvl(ehpta_calc_sale_balance_h.dr , 0) = 0 
and nvl(ehpta_calc_sale_balance_b.dr , 0) = 0 
and ehpta_calc_sale_balance_b.pk_cumandoc = '0001A8100000000DUB5O'
group by ehpta_calc_sale_balance_b.pk_cumandoc 

union all 

select rownum rowno , adjust.*
from (select ehpta_adjust.adjustdate , ehpta_adjust.mny , ehpta_adjust.pk_cubasdoc from ehpta_adjust 
where ehpta_adjust.type = 1 and nvl(ehpta_adjust.dr , 0) = 0 and ehpta_adjust.vbillstatus = 1 
and ehpta_adjust.adjustdate between substr('2012-11-30' , 0 , 7) || '-01' and '2012-11-30' 
and ehpta_adjust.pk_cubasdoc = '0001A8100000000DUB5O'
order by ehpta_adjust.adjustdate asc) adjust
) receivable 


left join 

(select rownum rowno ,  sale.* from (

select so_sale.dbilldate , so_sale.vreceiptcode , sum(so_saleorder_b.nnumber) nnumber , 
ic_general_b.dbizdate , ic_general_b.noutnum , so_saleorder_b.ntaxprice , (ic_general_b.noutnum * so_saleorder_b.ntaxprice) nsummny , ic_general_b.vnotebody 
from so_sale 
left join so_saleorder_b on so_saleorder_b.csaleid = so_sale.csaleid
left join ic_general_b on ic_general_b.csourcebillbid = so_saleorder_b.corder_bid
where so_sale.dbilldate between substr('2012-11-30' , 0 , 7) || '-01' and '2012-11-30' 
and so_sale.ccustomerid = '0001A8100000000DUB5O' 
and nvl(so_sale.dr , 0) = 0
and nvl(so_saleorder_b.dr , 0) = 0 and nvl(ic_general_b.dr , 0) = 0
group by so_sale.dbilldate , so_sale.vreceiptcode , ic_general_b.dbizdate , 
ic_general_b.noutnum , so_saleorder_b.ntaxprice , ic_general_b.vnotebody 
order by so_sale.dbilldate asc
) sale ) sale on sale.rowno = receivable.rowno
order by receivable.rowno

-- select substr('2012-11-01' , 6 , 2) - 1 from dual
/*

select 
case when substr('2012-11-01' , 6 , 2) - 1 < 10 and substr('2012-11-01' , 6 , 2) - 1 > 0 
then substr('2012-11-01' , 0 , 4) || '-0' || to_char(substr('2012-11-01' , 6 , 2) - 1)
when substr('2012-11-01' , 6 , 2) - 1 < 1 then to_char(substr('2012-11-01' , 0 , 4) - 1) || '-12'
else substr('2012-11-01' , 0 , 4) || '-' || to_char(substr('2012-11-01' , 6 , 2) - 1) end period
from dual



*/
