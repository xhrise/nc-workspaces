prompt Importing table pub_messagedrive...
set feedback off
set define off
insert into pub_messagedrive (ACTIONTYPE, CONFIGFLAG, DR, OPERATOR, PK_BILLTYPE, PK_BUSINESSTYPE, PK_CORP, PK_DRIVECONFIG, PK_SOURCEBILLTYPE, PK_SOURCEBUSINESSTYPE, SOURCEACTION, SYSINDEX, TS)
values ('CRTAPBILL', 1, null, null, '25', '0001AA1000000020IKPZ', '@@@@', '@@@@ZZ100000000041U5', '25', '0001AA1000000020IKPZ', 'APPROVE', 1, '2012-08-05 22:18:22');

insert into pub_messagedrive (ACTIONTYPE, CONFIGFLAG, DR, OPERATOR, PK_BILLTYPE, PK_BUSINESSTYPE, PK_CORP, PK_DRIVECONFIG, PK_SOURCEBILLTYPE, PK_SOURCEBUSINESSTYPE, SOURCEACTION, SYSINDEX, TS)
values ('SAVE', 1, null, null, 'D1', '0001AA1000000020IKPZ', '@@@@', '@@@@ZZ100000000041U4', '25', '0001AA1000000020IKPZ', 'CRTAPBILL', 1, '2012-08-05 22:13:57');

insert into pub_messagedrive (ACTIONTYPE, CONFIGFLAG, DR, OPERATOR, PK_BILLTYPE, PK_BUSINESSTYPE, PK_CORP, PK_DRIVECONFIG, PK_SOURCEBILLTYPE, PK_SOURCEBUSINESSTYPE, SOURCEACTION, SYSINDEX, TS)
values ('SAVE', 1, null, null, 'D0', '0001AA1000000020IKPZ', '@@@@', '@@@@ZZ100000000041U3', '32', '0001AA1000000020IKPZ', 'APPROVE', 1, '2012-08-05 22:13:24');

prompt Done.
