prompt Importing table pub_billtemplet...
set feedback off
set define off
insert into pub_billtemplet (BILL_TEMPLETCAPTION, BILL_TEMPLETNAME, DR, METADATACLASS, MODEL_TYPE, NODECODE, OPTIONS, PK_BILLTEMPLET, PK_BILLTYPECODE, PK_CORP, RESID, SHAREFLAG, TS, VALIDATEFORMULA)
values ('客户提单余额表', 'SYSTEM', 0, null, null, 'HQ0305', null, '0001AA1000000028JZGR', 'HQ16', '@@@@', null, 'N', '2012-11-21 13:38:49', null);

insert into pub_billtemplet (BILL_TEMPLETCAPTION, BILL_TEMPLETNAME, DR, METADATACLASS, MODEL_TYPE, NODECODE, OPTIONS, PK_BILLTEMPLET, PK_BILLTYPECODE, PK_CORP, RESID, SHAREFLAG, TS, VALIDATEFORMULA)
values ('客户发票余额表', 'SYSTEM', 0, null, null, 'HQ0306', null, '0001AA1000000028L9R4', 'HQ17', '@@@@', null, 'N', '2012-11-21 16:46:48', null);

prompt Done.
