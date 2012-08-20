prompt Importing table pub_query_condition...
set feedback off
set define off
insert into pub_query_condition (CONSULT_CODE, DATA_TYPE, DISP_SEQUENCE, DISP_TYPE, DISP_VALUE, DR, FIELD_CODE, FIELD_NAME, ID, IF_AUTOCHECK, IF_DATAPOWER, IF_DEFAULT, IF_DESC, IF_GROUP, IF_IMMOBILITY, IF_MUST, IF_ORDER, IF_SUM, IF_USED, INSTRUMENTSQL, ISCONDITION, MAX_LENGTH, OPERA_CODE, OPERA_NAME, ORDER_SEQUENCE, PK_CORP, PK_TEMPLET, RESID, RETURN_TYPE, TABLE_CODE, TABLE_NAME, TS, USERDEFFLAG, VALUE)
values ('-99', 3, 100, 0, null, null, 'maindate', '起始维护日期', '0001A11000000020FJ5Q', 'N', 'N', 'Y', null, 'N', 'N', 'N', 'N', 'N', 'Y', null, 'Y', null, '=@>@>=@<@<=@like@', '等于@大于@大于等于@小于@小于等于@包含@', 1, '@@@@', '0001A11000000020FJ59', null, 0, null, null, '2012-08-13 08:31:07', null, null);

insert into pub_query_condition (CONSULT_CODE, DATA_TYPE, DISP_SEQUENCE, DISP_TYPE, DISP_VALUE, DR, FIELD_CODE, FIELD_NAME, ID, IF_AUTOCHECK, IF_DATAPOWER, IF_DEFAULT, IF_DESC, IF_GROUP, IF_IMMOBILITY, IF_MUST, IF_ORDER, IF_SUM, IF_USED, INSTRUMENTSQL, ISCONDITION, MAX_LENGTH, OPERA_CODE, OPERA_NAME, ORDER_SEQUENCE, PK_CORP, PK_TEMPLET, RESID, RETURN_TYPE, TABLE_CODE, TABLE_NAME, TS, USERDEFFLAG, VALUE)
values ('-99', 3, 100, 0, null, null, 'maindate', '结束维护日期', '0001A11000000020FJ5R', 'N', 'N', 'Y', null, 'N', 'N', 'N', 'N', 'N', 'Y', null, 'Y', null, '=@>@>=@<@<=@like@', '等于@大于@大于等于@小于@小于等于@包含@', 2, '@@@@', '0001A11000000020FJ59', null, 0, null, null, '2012-08-13 08:31:07', null, null);

insert into pub_query_condition (CONSULT_CODE, DATA_TYPE, DISP_SEQUENCE, DISP_TYPE, DISP_VALUE, DR, FIELD_CODE, FIELD_NAME, ID, IF_AUTOCHECK, IF_DATAPOWER, IF_DEFAULT, IF_DESC, IF_GROUP, IF_IMMOBILITY, IF_MUST, IF_ORDER, IF_SUM, IF_USED, INSTRUMENTSQL, ISCONDITION, MAX_LENGTH, OPERA_CODE, OPERA_NAME, ORDER_SEQUENCE, PK_CORP, PK_TEMPLET, RESID, RETURN_TYPE, TABLE_CODE, TABLE_NAME, TS, USERDEFFLAG, VALUE)
values ('SX,挂牌价=1,结算价=2', 6, 100, 0, null, null, 'type', '牌价分类', '0001A11000000020FJ5U', 'N', 'N', 'Y', null, 'N', 'N', 'N', 'N', 'N', 'Y', null, 'Y', null, '=@>@>=@<@<=@like@', '等于@大于@大于等于@小于@小于等于@包含@', 0, '@@@@', '0001A11000000020FJ59', null, 0, null, null, '2012-08-13 08:31:07', null, null);

prompt Done.
