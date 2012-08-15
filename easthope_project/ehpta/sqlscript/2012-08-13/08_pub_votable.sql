prompt Importing table pub_votable...
set feedback off
set define off
insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_calcinterest', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010901.CalcInterestVO', null, 'voperatorid', 'HQ09', null, '0001AA1000000020M9HQ', 'pk_calcinterest', '2012-08-14 11:12:08', 'ehpta_calc_interest');

prompt Done.
