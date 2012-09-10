prompt Importing table bd_busitype...
set feedback off
set define off
insert into bd_busitype (BUSICODE, BUSINAME, BUSIPROP, DEFAULTGATHER, DEFSTR2, DEFSTR3, DEFSTR4, DEFSTR5, DR, ENCAPSULATE, FKTYPE, ISDAPCLASSIFIED, ISITEM, MNECODE, OWETYPE, PK_BUSITYPE, PK_CORP, RECEIPTTYPE, SKTYPE, TS, VERIFYRULE, YSTYPE)
values ('PTA0', 'PTA销售流程', 1, null, null, null, null, null, 0, 'N', null, 'N', 'N', null, null, '1120AA1000000011FQIR', '1120', null, null, '2012-07-22 12:51:35', 'Y', null);

insert into bd_busitype (BUSICODE, BUSINAME, BUSIPROP, DEFAULTGATHER, DEFSTR2, DEFSTR3, DEFSTR4, DEFSTR5, DR, ENCAPSULATE, FKTYPE, ISDAPCLASSIFIED, ISITEM, MNECODE, OWETYPE, PK_BUSITYPE, PK_CORP, RECEIPTTYPE, SKTYPE, TS, VERIFYRULE, YSTYPE)
values ('PTA2', 'PTA内部调拨', 0, null, null, null, null, null, 0, 'N', null, 'N', 'N', null, null, '0001AA1000000020IKPZ', '@@@@', null, null, '2012-08-05 20:15:35', 'Y', null);

insert into bd_busitype (BUSICODE, BUSINAME, BUSIPROP, DEFAULTGATHER, DEFSTR2, DEFSTR3, DEFSTR4, DEFSTR5, DR, ENCAPSULATE, FKTYPE, ISDAPCLASSIFIED, ISITEM, MNECODE, OWETYPE, PK_BUSITYPE, PK_CORP, RECEIPTTYPE, SKTYPE, TS, VERIFYRULE, YSTYPE)
values ('PTA', 'PTA销售流程', 1, null, null, null, null, null, 0, 'N', null, 'N', 'N', null, null, '0001AA1000000020E0D4', '@@@@', null, null, '2012-07-02 16:26:50', 'Y', null);

prompt Done.
