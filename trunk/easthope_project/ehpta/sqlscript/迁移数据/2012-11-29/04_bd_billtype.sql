prompt Importing table bd_billtype...
set feedback off
set define off
insert into bd_billtype (ACCOUNTCLASS, BILLSTYLE, BILLTYPENAME, CANEXTENDTRANSACTION, CHECKCLASSNAME, CLASSNAME, COMPONENT, DATAFINDERCLZ, DEF1, DEF2, DEF3, DR, FORWARDBILLTYPE, ISACCOUNT, ISITEM, ISROOT, ISTRANSACTION, NODECODE, PARENTBILLTYPE, PK_BILLTYPECODE, REFERCLASSNAME, SYSTEMCODE, TS, WEBNODECODE, WHERESTRING)
values (null, null, '客户提单余额', 'N', 'nc.bs.trade.business.HYSuperDMO', '<Y>HQ0305', null, null, null, null, 'nc.ui.ehpta.hq0305.ClientUICheckRule', null, null, 'N', 'Y', 'N', null, 'HQ0305', null, 'HQ16', null, 'ehpta', '2012-11-13 16:40:23', null, null);

insert into bd_billtype (ACCOUNTCLASS, BILLSTYLE, BILLTYPENAME, CANEXTENDTRANSACTION, CHECKCLASSNAME, CLASSNAME, COMPONENT, DATAFINDERCLZ, DEF1, DEF2, DEF3, DR, FORWARDBILLTYPE, ISACCOUNT, ISITEM, ISROOT, ISTRANSACTION, NODECODE, PARENTBILLTYPE, PK_BILLTYPECODE, REFERCLASSNAME, SYSTEMCODE, TS, WEBNODECODE, WHERESTRING)
values (null, null, '客户发票余额', 'N', 'nc.bs.trade.business.HYSuperDMO', '<Y>HQ0306', null, null, null, null, 'nc.ui.ehpta.hq0306.SaleInvClientUICheckRule', null, null, 'N', 'Y', 'N', null, 'HQ0306', null, 'HQ17', null, 'ehpta', '2012-11-21 15:58:22', null, null);

prompt Done.
