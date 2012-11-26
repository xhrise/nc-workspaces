
select bd_stordoc.storname , 
case when instr(ehpta_calc_storfee_b.vbatchcode , ' - ' , 1 , 1) > 0 then 
substr(ehpta_calc_storfee_b.vbatchcode , 0 , instr(ehpta_calc_storfee_b.vbatchcode , ' - ' , 1 , 1) - 1)
else null end shipname ,
ehpta_calc_storfee_b.vbatchcode , 
nvl(TEMQ_mtfyxhsl.ninnum , 0) ninnum , 
case when decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(TEMQ_mtfyxhsl.ninnum , 0) / nvl(bd_invbasdoc.unitweight , 0)) > 
round(decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(TEMQ_mtfyxhsl.ninnum , 0) / nvl(bd_invbasdoc.unitweight , 0))) then 
round(decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(TEMQ_mtfyxhsl.ninnum , 0) / nvl(bd_invbasdoc.unitweight , 0))) + 1 else 
decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(TEMQ_mtfyxhsl.ninnum , 0) / nvl(bd_invbasdoc.unitweight , 0)) end ninnumof , 
ehpta_calc_storfee_b.indate , nvl(ehpta_calc_storfee_b.noutnum , 0 ) noutnum ,

case when decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(ehpta_calc_storfee_b.noutnum , 0) / nvl(bd_invbasdoc.unitweight , 0)) > 
round(decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(ehpta_calc_storfee_b.noutnum , 0) / nvl(bd_invbasdoc.unitweight , 0))) then 
round(decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(ehpta_calc_storfee_b.noutnum , 0) / nvl(bd_invbasdoc.unitweight , 0))) + 1 else 
round(decode(nvl(bd_invbasdoc.unitweight , 0) , 0 , 0 , nvl(ehpta_calc_storfee_b.noutnum , 0) / nvl(bd_invbasdoc.unitweight , 0))) end noutnumof  ,

ehpta_calc_storfee_b.outdate , ehpta_calc_storfee_b.overdate overdate , bd_cubasdoc.custname , ehpta_calc_storfee_b.feetype , ehpta_calc_storfee_b.signprice , ehpta_calc_storfee_b.hmny ,
ehpta_calc_storfee_b.storprice , ehpta_calc_storfee_b.stordays , ehpta_calc_storfee_b.days , ehpta_calc_storfee_b.stormny , nvl(ehpta_calc_storfee_b.hmny , 0) + nvl(ehpta_calc_storfee_b.stormny , 0) mny

from ehpta_calc_storfee_b 
left join bd_stordoc  on bd_stordoc.pk_stordoc = ehpta_calc_storfee_b.pk_stordoc
left join (	
select ninnum , cbodywarehouseid , vbatchcode , cinventoryid
from ic_general_b where nvl(dr, 0) = 0 and csourcetype = '4K'  and cbodybilltypecode = '4A'
) TEMQ_mtfyxhsl on (TEMQ_mtfyxhsl.cbodywarehouseid = ehpta_calc_storfee_b.pk_stordoc and TEMQ_mtfyxhsl.vbatchcode = ehpta_calc_storfee_b.vbatchcode 
and TEMQ_mtfyxhsl.cinventoryid = ehpta_calc_storfee_b.pk_invmandoc)
left join bd_invmandoc bd_invmandoc on bd_invmandoc.pk_invmandoc = ehpta_calc_storfee_b.pk_invmandoc
left join bd_invbasdoc bd_invbasdoc on bd_invbasdoc.pk_invbasdoc = bd_invmandoc.pk_invbasdoc
left join bd_cumandoc bd_cumandoc on bd_cumandoc.pk_cumandoc = ehpta_calc_storfee_b.pk_cumandoc
left join bd_cubasdoc bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc
left join ehpta_calc_storfee_h on ehpta_calc_storfee_h.pk_storfee = ehpta_calc_storfee_b.pk_storfee
where -- ehpta_calc_storfee_h.vbillstatus = 1 and 
nvl(ehpta_calc_storfee_h.dr ,0 ) = 0 and nvl(ehpta_calc_storfee_b.dr , 0) = 0
and 
(case when instr(ehpta_calc_storfee_b.vbatchcode , ' - ' , 1 , 1) > 0 then 
substr(ehpta_calc_storfee_b.vbatchcode , 0 , instr(ehpta_calc_storfee_b.vbatchcode , ' - ' , 1 , 1) - 1)
else null end ) in ('°²¼ª806')

