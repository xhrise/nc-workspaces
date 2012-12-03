prompt Importing table pub_votable...
set feedback off
set define off
insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq0305.SaleBalanceBVO', null, null, 'HQ16', null, '0001AA1000000028JZIL', 'pk_sale_balance', '2012-11-14 13:26:54', 'ehpta_calc_sale_balance_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_sale_balance', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq0305.SaleBalanceHVO', null, 'voperatorid', 'HQ16', null, '0001AA1000000028JZIK', 'pk_sale_balance', '2012-11-13 17:11:49', 'ehpta_calc_sale_balance_h');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq0306.SaleinvBalanceBVO', null, null, 'HQ17', null, '0001AA1000000028L9QR', 'pk_saleinv_balance', '2012-11-21 16:01:15', 'ehpta_calc_saleinv_balance_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_saleinv_balance', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', 'pk_billtype', null, null, null, 'Y', 'nc.vo.ehpta.hq0306.SaleinvBalanceHVO', null, 'voperatorid', 'HQ17', null, '0001AA1000000028L9QQ', 'pk_saleinv_balance', '2012-11-21 16:00:32', 'ehpta_calc_saleinv_balance_h');

prompt Done.
