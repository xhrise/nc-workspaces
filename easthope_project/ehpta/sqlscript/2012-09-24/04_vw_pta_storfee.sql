create or replace view vw_pta_storfee as
select genb.vbatchcode , genh.cwarehouseid pk_stordoc , genh.pk_contract , genh.concode , decode(genh.contracttype , 10 , '现货合同' , 20 , '长单合同' , genh.contracttype) contracttype ,
genb.cinventoryid pk_invmandoc , invbas.invname ,
to_char(to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8)) , 'yyyy-MM-dd'),'yyyy-MM-dd') indate ,
genh.dbilldate outdate , genh.ccustomerid pk_cumandoc , sale.vreceiptcode ,
genb.cgeneralbid , genb.cgeneralhid , genh.vbillcode outcode , saleb.deliverydate overdate , genb.noutnum ,
decode(storcontb.feetype , 1 , '仓库费' , 2 , '直驳费' , 3 , '船-库-船' , 4 , '船-库-车' , 5 , '直驳费（船-船）' , 6 , '直驳费（船-车）' , null) feetype ,
storcontb.signprice , nvl(genb.noutnum,0) * nvl(storcontb.signprice,0) hmny ,

(case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') end ) + 1 stordays ,
nvl(storcontb1.concesessionsday,0) freedays ,
case when ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) < 0 then 0 else ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) end days ,
storcontb1.signprice storprice ,
(case when ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) < 0 then 0 else ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genh.dbilldate , 'yyyy-MM-dd') > 0 then to_date(genh.dbilldate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8) + 1) , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) end) * storcontb1.signprice * genb.noutnum stormny

from ic_general_h genh
left join ic_general_b genb on genb.cgeneralhid = genh.cgeneralhid
left join so_sale sale on sale.csaleid = genb.csourcebillhid
left join so_saleorder_b saleb on saleb.corder_bid = genb.csourcebillbid
left join ehpta_storcontract_b storcontb on storcontb.pk_storcontract_b = genh.pk_storcontract_b
left join ehpta_storcontract storcont on storcont.pk_stordoc = genh.cwarehouseid
left join ehpta_storcontract_b storcontb1 on storcontb1.pk_storagedoc = storcont.pk_storagedoc
left join bd_invbasdoc invbas on invbas.pk_invbasdoc = genb.cinvbasid

where genh.daccountdate is not null

and decode(genh.vuserdef4 , 'Y' , 'Y' , 'N' , 'N' , 'N') = 'N' and genh.concode is not null
and (nvl(genh.contracttype , 0) = 10 or nvl(genh.contracttype , 0) = 20) and nvl(genh.storprice,0) > 0
and genb.vbatchcode is not null and nvl(storcontb1.feetype , '0') = '1'
and storcont.vbillstatus = 1 and genh.fbillflag = 3

and nvl(genh.dr,0) = 0 and nvl(genb.dr,0) = 0
and nvl(sale.dr,0) = 0 and nvl(saleb.dr,0) = 0
and nvl(storcontb.dr,0) = 0 and nvl(storcont.dr,0) = 0 and nvl(storcontb1.dr,0) = 0
and nvl(invbas.dr , 0 ) = 0;
