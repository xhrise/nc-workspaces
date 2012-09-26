create or replace view vw_pta_upper_transfee as
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
