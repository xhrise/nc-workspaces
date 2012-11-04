
create or replace force view nccw.vw_pta_interest as
select distinct cubas.custname custname,
                zb.vouchid pk_receivable,
                zb.djbh djzbid,
                contract.pk_contract def5,
                contract.vbillno contno,
                to_char(contract.version) version,
                case
                  when contract.contype = '现货合同' then
                   '10'
                  when contract.contype = '长单合同' then
                   '20'
                  else
                   ''
                end transtype,
                zb.effectdate redate,
                fb.dfybje remny,
                cubas.custname interestto,
                90 days,
                bala.balanname billtype,
                zb.pj_jsfs def1,
                fb.hbbm def2,
                cuman.pk_cumandoc def3 ,
                cuman.pk_corp pk_corp
  from arap_djzb zb
  left join arap_djfb fb
    on fb.vouchid = zb.vouchid
  left join ehpta_sale_contract contract
    on zb.zyx6 = contract.pk_contract
  left join bd_balatype bala
    on bala.pk_balatype = zb.pj_jsfs
  left join bd_cubasdoc cubas
    on cubas.pk_cubasdoc = fb.hbbm
  left join bd_cumandoc cuman
    on cuman.pk_cubasdoc = cubas.pk_cubasdoc
 where zb.zyx6 is not null

   and nvl(zb.dr, 0) = 0
   and nvl(fb.dr, 0) = 0
   and nvl(contract.dr, 0) = 0
   and nvl(bala.dr, 0) = 0
   and nvl(cubas.dr, 0) = 0
   and nvl(cuman.dr, 0) = 0
   and zb.djzt = 3
   and (cuman.custflag = '0' or cuman.custflag = '2')
   and (select nvl(count(1), 0)
          from ehpta_calc_interest_b itstb
          left join ehpta_calc_interest itst
            on itst.pk_calcinterest = itstb.pk_calcinterest
         where pk_receivable = zb.vouchid
           and nvl(itst.dr, 0) = 0
           and nvl(itstb.dr, 0) = 0) = 0
  and zb.pj_jsfs = '0001A810000000000JBQ';


create or replace force view nccw.vw_pta_rebates as
select sale.ccustomerid pk_cumandoc ,cubas.custname , salecont.pk_contract , salecont.vbillno concode, orderb.cinventoryid pk_invmandoc ,  invbas.invname  ,  sale.period  ,
salecontb.num , sum(orderb.nnumber) nnumber , decode(nvl(salecontb.num,0),0,0,(nvl(sum(orderb.nnumber),0) / nvl(salecontb.num,0) * 100)) comprate ,
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
and nvl(orderb.rebateflag,'N') = 'N'

group by sale.ccustomerid , salecont.vbillno, invbas.invname  , sale.period ,
salecontb.num,salecontb.preprice,cubas.custname,salecont.pk_contract,orderb.cinventoryid ,
sale.pk_corp
order by salecont.vbillno asc;


create or replace force view nccw.vw_pta_salecont_invbalance as
select PK_CONTRACT,CSALEID,TYPE,TYPENAME, mny , iscredit , pk_cumandoc from (

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny , def3 iscredit , pk_cumandoc from (select pk_contract , '' csaleid , type, decode(type , 1 , '货款' , 2 , '贴息额' , 3 , '挂结价差额' , 4 , '返利额' , 5 , '运补额' , 6 , '仓储费' , 7 , '装卸费' , '') typename
, mny , def3 , decode(type , 1 , pk_cubasdoc , null) pk_cumandoc from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and (nvl(def4 , 'N') = 'N' or type = 1) ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename , temp.def3 , temp.pk_cumandoc
-- 收款及其他金额项

union all

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny , def3 iscredit , pk_cumandoc from (select pk_contract , '' csaleid , type, decode(type , 1 , '货款' , 2 , '贴息额' , 3 , '挂结价差额' , 4 , '返利额' , 5 , '运补额' , 6 , '仓储费' , 7 , '装卸费' , '') typename
, (mny * -1) mny , def3 , null pk_cumandoc  from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and nvl(def4 , 'N') = 'Y' and nvl(type,1) <> 1 ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename , temp.def3 , temp.pk_cumandoc

union all

select pk_contract, '' , '11' , '累计开票额' , nvl(sum(ntotalsummny) * -1 , 0) , iscredit , creceiptcorpid
from so_saleinvoice
where pk_contract is not null and nvl(dr,0)=0  and (saletype = '现货合同' or saletype = '长单合同')
and fstatus = 2
group by pk_contract , creceiptcorpid , iscredit

);


create or replace force view nccw.vw_pta_sale_contract as
select contract.*  from ehpta_sale_contract contract
    left join ehpta_sale_contract_bs contract_bs on contract.pk_contract = contract_bs.pk_contract
    left join (select nvl(sum(saleb.nnumber),0) nnumber , sale.pk_contract from so_saleorder_b saleb left join so_sale sale on saleb.csaleid = sale.csaleid where nvl(sale.dr,0)=0 and nvl(saleb.dr,0)=0 group by sale.pk_contract) orderb on orderb.pk_contract = contract_bs.pk_contract
    where contract_bs.num > orderb.nnumber  and contract.vbillstatus = 1 and nvl(contract.dr,0) = 0and nvl(contract_bs.dr,0)=0
    union all
    select "PK_CONTRACT","VBILLNO","CONTYPE","PURCHCODE","PURCHNAME","BARGAINOR","ORDERDATE","ORDERADDRESS","CONNAMED","SDATE","EDATE","PK_DEPTDOC","PK_PSNDOC","REBATE_FLAG","POUNDSOFPOOR","TERMINATION","VERSION","STOPCONTRACT","PAY_CUTOFFDATE","DELIVERYDATE","MEMO","VBILLSTATUS","PK_CORP","PK_BUSITYPE","PK_BILLTYPE","VAPPROVEID","DAPPROVEDATE","VOPERATORID","VAPPROVENOTE","DMAKEDATE","DEF1","DEF2","DEF3","DEF4","DEF5","DEF6","DEF7","DEF8","DEF9","DEF10","TS","DR","CUSTCODE","CLOSE_FLAG","CONTPRICE" from ehpta_sale_contract
    where vbillstatus = 1 and pk_contract not in (select pk_contract from so_sale where pk_contract is not null and nvl(dr,0)=0) and nvl(dr,0)=0;


create or replace force view nccw.vw_pta_sale_contract_balance as
select PK_CONTRACT,CSALEID,TYPE,TYPENAME,sum(MNY) mny , iscredit , pk_cumandoc from (

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny , def3 iscredit , pk_cumandoc from (select pk_contract , '' csaleid , type, decode(type , 1 , '货款' , 2 , '贴息额' , 3 , '挂结价差额' , 4 , '返利额' , 5 , '运补额' , 6 , '仓储费' , 7 , '装卸费' , '') typename
, mny , def3 , decode(type , 1 , pk_cubasdoc , null) pk_cumandoc from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and (nvl(def4 , 'N') = 'N' or type = 1) ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename , temp.def3 , temp.pk_cumandoc
-- 收款及其他金额项

union all

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny , def3 iscredit , pk_cumandoc from (select pk_contract , '' csaleid , type, decode(type , 1 , '货款' , 2 , '贴息额' , 3 , '挂结价差额' , 4 , '返利额' , 5 , '运补额' , 6 , '仓储费' , 7 , '装卸费' , '') typename
, (mny * -1) mny , def3 , null pk_cumandoc  from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and nvl(def4 , 'N') = 'Y' and nvl(type,1) <> 1 ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename , temp.def3 , temp.pk_cumandoc

union all

select pk_contract , '' , '11' , '已提货金额' , nvl(sum(nheadsummny) * -1 , 0) , iscredit , ccustomerid pk_cumandoc from so_sale
 where pk_contract is not null and nvl(dr, 0) = 0 and (contracttype = 10 or contracttype = 20)
   and (FSTATUS = 2)
 group by pk_contract,iscredit , ccustomerid

) group by PK_CONTRACT,CSALEID,TYPE,TYPENAME,iscredit , pk_cumandoc;


create or replace force view nccw.vw_pta_sale_contract_bs as
select contract_bs.pk_contract_b,
       contract_bs.pk_contract,
       contract_bs.pk_invbasdoc,
       contract_bs.invname,
       contract_bs.invspec,
       contract_bs.pk_measdoc,
       (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0)) num,
       round(case when invbas.unitweight = 0 then 0 else ((nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0)) / invbas.unitweight) end,0) numof,
       contract_bs.taxprice,
       (contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) sumpricetax ,
       contract_bs.taxrate,
       round(((contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) - ((contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) / (1 + contract_bs.taxrate / 100))),2) tax,
       round(((contract_bs.taxprice * (nvl(to_number(contract_bs.num),0) - nvl(to_number(orderb.nnumber),0))) / (1 + contract_bs.taxrate / 100)),2) notaxloan,
       contract_bs.memo,
       contract_bs.dr,
       contract_bs.ts,
       contract_bs.def1,
       contract_bs.def2,
       contract_bs.def3,
       contract_bs.def4,
       contract_bs.def5,
       contract_bs.def6,
       contract_bs.def7,
       contract_bs.def8,
       contract_bs.def9,
       contract_bs.def10
  from ehpta_sale_contract_bs contract_bs
left join (select nvl(sum(saleb.nnumber),0) nnumber , sale.pk_contract from so_saleorder_b saleb left join so_sale sale on saleb.csaleid = sale.csaleid where nvl(sale.dr,0)=0 and nvl(saleb.dr,0)=0 group by sale.pk_contract) orderb on orderb.pk_contract = contract_bs.pk_contract
left join bd_invmandoc invman on invman.pk_invmandoc = contract_bs.pk_invbasdoc
left join bd_invbasdoc invbas on invbas.pk_invbasdoc = invman.pk_invbasdoc
where nvl(to_number(contract_bs.num),0) > nvl(to_number(orderb.nnumber),0) and nvl(contract_bs.dr,0)=0;


create or replace force view nccw.vw_pta_settlement as
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


create or replace force view nccw.vw_pta_storfee as
select genb.vbatchcode , genh.cwarehouseid pk_stordoc , genh.pk_contract , genh.concode , decode(genh.contracttype , 10 , '现货合同' , 20 , '长单合同' , genh.contracttype) contracttype ,
genb.cinventoryid pk_invmandoc , invbas.invname ,
(select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  indate ,
-- to_char(to_date(trim(substr(genb.vbatchcode, instr(genb.vbatchcode, ' - ') + 3 , 8)) , 'yyyy-MM-dd'),'yyyy-MM-dd') indate ,
genb.dbizdate outdate , genh.ccustomerid pk_cumandoc , sale.vreceiptcode ,
genb.cgeneralbid , genb.cgeneralhid , genh.vbillcode outcode , saleb.deliverydate overdate , genb.noutnum ,
decode(storcontb.feetype , 1 , '仓库费' , 2 , '直驳费' , 3 , '船-库-船' , 4 , '船-库-车' , 5 , '直驳费（船-船）' , 6 , '直驳费（船-车）' , decode(storcontb1.feetype , 1 , '仓库费' , 2 , '直驳费' , 3 , '船-库-船' , 4 , '船-库-车' , 5 , '直驳费（船-船）' , 6 , '直驳费（船-车）' , null)) feetype ,
nvl(storcontb.signprice , storcontb1.signprice) signprice , nvl(genb.noutnum,0) * nvl(storcontb.signprice , storcontb1.signprice) hmny ,


to_date(genb.dbizdate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid) , 'yyyy-MM-dd') + 1 def1 ,
(case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genb.dbizdate , 'yyyy-MM-dd') > 0 then to_date(genb.dbizdate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') end ) + 1 stordays ,
nvl(storcontb1.concesessionsday,0) freedays ,storcontb1.pk_storcontract_b ,
case when ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genb.dbizdate , 'yyyy-MM-dd') > 0 then to_date(genb.dbizdate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) < 0 then 0 else ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genb.dbizdate , 'yyyy-MM-dd') > 0 then to_date(genb.dbizdate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) end days ,
storcontb1.signprice storprice,
decode(storcontb1.storccontracttype  , '1' , '元/吨' , '2' , '元/吨/天' , null) def2 ,
decode(storcontb1.storccontracttype , '1' , storcontb1.signprice * genb.noutnum , '2' , (case when ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genb.dbizdate , 'yyyy-MM-dd') > 0 then to_date(genb.dbizdate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) < 0 then 0 else ((case when to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date(genb.dbizdate , 'yyyy-MM-dd') > 0 then to_date(genb.dbizdate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') else to_date(saleb.deliverydate , 'yyyy-MM-dd') - to_date((select max(dbizdate) from ic_general_b where cbodywarehouseid = genh.cwarehouseid and vbatchcode =  genb.vbatchcode and nvl(dr,0) = 0 and csourcetype = '4K' and cbodybilltypecode = '4A' and cinventoryid = genb.cinventoryid)  , 'yyyy-MM-dd') end ) + 1) - nvl(storcontb1.concesessionsday,0) end) * storcontb1.signprice * genb.noutnum
, null) stormny

from ic_general_h genh
left join ic_general_b genb on genb.cgeneralhid = genh.cgeneralhid
left join so_sale sale on sale.csaleid = genb.csourcebillhid
left join so_saleorder_b saleb on saleb.corder_bid = genb.csourcebillbid
left join ehpta_storcontract_b storcontb on storcontb.pk_storcontract_b = genb.pk_storcont_b
left join ehpta_storcontract storcont on storcont.pk_stordoc = genh.cwarehouseid
left join ehpta_storcontract_b storcontb1 on storcontb1.pk_storagedoc = storcont.pk_storagedoc
left join bd_invbasdoc invbas on invbas.pk_invbasdoc = genb.cinvbasid

where genh.daccountdate is not null
and decode(genh.vuserdef4 , 'Y' , 'Y' , 'N' , 'N' , 'N') = 'N' and genh.concode is not null
and (nvl(genh.contracttype , 0) = 10 or nvl(genh.contracttype , 0) = 20) and nvl(genb.transprice,0) > 0
and genb.vbatchcode is not null and nvl(storcontb1.feetype , '0') = '1'
and storcont.vbillstatus = 1 and genh.fbillflag = 3

and nvl(genh.dr,0) = 0 and nvl(genb.dr,0) = 0
and nvl(sale.dr,0) = 0 and nvl(saleb.dr,0) = 0
and nvl(storcontb.dr,0) = 0 and nvl(storcont.dr,0) = 0 and nvl(storcontb1.dr,0) = 0
and nvl(invbas.dr , 0 ) = 0 and nvl(storcontb.feetype , storcontb1.feetype) <> 1;


create or replace force view nccw.vw_pta_under_transfee as
select genh.cgeneralhid , genh.vbillcode cbillno , genb.cinventoryid def1 , invbas.invname def2 , genh.dbilldate sdate ,
genh.cwarehouseid pk_sstordoc , stordoc.storname def3 , stordoc.storaddr saddress ,
address.addrname eaddress , transcontb.piersfee , transcontb.inlandshipfee , transcontb.carfee , genh.pk_transport , genb.pk_transport_b pk_transport_b ,
genb.noutnum num , genb.transprice fee , (nvl(genb.transprice,0) * nvl(genb.noutnum,0)) transmny
,genb.cgeneralbid def5 , genh.ccustomerid def6 , genh.pk_contract def4,transcontb.def1 def7
from ic_general_h genh
left join ic_general_b genb on genh.cgeneralhid = genb.cgeneralhid
left join bd_stordoc stordoc on stordoc.pk_stordoc = genh.cwarehouseid
left join ehpta_transport_contract_b transcontb on transcontb.pk_transport_b = genb.pk_transport_b
left join bd_address address on address.pk_address = transcontb.estoraddr
left join bd_invbasdoc invbas on invbas.pk_invbasdoc = genb.cinvbasid

where decode(genh.vuserdef3 , 'Y' , 'Y' , 'N' , 'N' , 'N') = 'N' and genh.concode is not null
and (nvl(genh.contracttype , 0) = 10 or nvl(genh.contracttype , 0) = 20) and nvl(genb.transprice,0) > 0
and genh.fbillflag = 3
and genh.daccountdate is not null;


create or replace force view nccw.vw_pta_upper_transfee as
select ingenb.cgeneralhid def2,
       trim(substr(ingenb.vbatchcode, 0, instr(ingenb.vbatchcode, ' - '))) shipname,
       ingenh.vbillcode sourceno,
       ingenb.cinventoryid pk_invmandoc,
       invbas.invname def3,
       ingenh.cwarehouseid def4,
       instor.storname def5,
       outgenb.dbizdate sdate,
       nvl(outgenb.noutnum, 0) num,
       ingenb.dbizdate edate,
       nvl(ingenb.ninnum, 0) recnum,
       -- instor.pk_address def6,
       instor.storaddr eaddr,
       to_char(nvl(transcontb.dieselprice,0)) def7,
       to_char(nvl(transcontb.shipregulation,0)) def8,
       to_number(nvl(ingenh.vuserdef1, 0)) def9 ,
       nvl(outgenb.noutnum, 0) - nvl(ingenb.ninnum, 0) outnum,
       nvl(ingenh.vuserdef1, 0) fee,
       to_number(nvl(ingenh.vuserdef1, 0)) * to_number(nvl(ingenb.ninnum, 0)) transmny,
       to_number(nvl(ingenh.vuserdef1, 0)) * to_number(nvl(ingenb.ninnum, 0)) paymny
  from ic_general_b ingenb
  left join ic_general_h ingenh on ingenh.cgeneralhid = ingenb.cgeneralhid
  left join ic_general_b outgenb on outgenb.csourcebillbid = ingenb.csourcebillbid
  left join ic_general_h outgenh on outgenh.cgeneralhid = outgenb.cgeneralhid
  left join bd_invbasdoc invbas on invbas.pk_invbasdoc = ingenb.cinvbasid
  left join bd_stordoc instor on instor.pk_stordoc = ingenh.cwarehouseid
  left join bd_stordoc outstor on outstor.pk_stordoc = outgenh.cwarehouseid
  left join ehpta_transport_contract_b transcontb on transcontb.pk_transport_b = ingenh.vuserdef2
  -- left join bd_address inaddr on inaddr.pk_address = instor.pk_address
 where ingenb.vbatchcode is not null
   and ingenb.csourcetype = '4K'
   and outgenb.csourcetype = '4K'
   and outgenh.cbilltypecode = '4I'
   and ingenh.cbilltypecode = '4A'

   and decode(ingenh.vuserdef3, 'Y' , 'Y' , 'N' , 'N' , 'N') = 'N'
   and outgenh.cwarehouseid = '1120A7100000000Z1ZJX'

   and nvl(ingenb.dr , 0 ) = 0
   and nvl(ingenh.dr , 0 ) = 0
   and nvl(outgenb.dr , 0 ) = 0
   and nvl(outgenh.dr , 0 ) = 0
   and nvl(invbas.dr , 0 ) = 0
   and nvl(instor.dr , 0 ) = 0
   and nvl(outstor.dr , 0 ) = 0
   -- and nvl(inaddr.dr , 0 ) = 0

   and nvl(ingenh.fbillflag , 0) = 3
   and nvl(outgenh.fbillflag , 0) = 3;


