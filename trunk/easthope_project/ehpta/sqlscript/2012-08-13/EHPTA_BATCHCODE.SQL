create table EHPTA_BATCHCODE
(
  binqc           CHAR(1),
  bseal           CHAR(1),
  cqualitylevelid CHAR(20),
  dproducedate    CHAR(10),
  dr              NUMBER(10) default 0,
  dvalidate       CHAR(10),
  pk_batchcode    CHAR(20) not null,
  pk_defdoc1      CHAR(20),
  pk_defdoc10     CHAR(20),
  pk_defdoc11     CHAR(20),
  pk_defdoc12     CHAR(20),
  pk_defdoc13     CHAR(20),
  pk_defdoc14     CHAR(20),
  pk_defdoc15     CHAR(20),
  pk_defdoc16     CHAR(20),
  pk_defdoc17     CHAR(20),
  pk_defdoc18     CHAR(20),
  pk_defdoc19     CHAR(20),
  pk_defdoc2      CHAR(20),
  pk_defdoc20     CHAR(20),
  pk_defdoc3      CHAR(20),
  pk_defdoc4      CHAR(20),
  pk_defdoc5      CHAR(20),
  pk_defdoc6      CHAR(20),
  pk_defdoc7      CHAR(20),
  pk_defdoc8      CHAR(20),
  pk_defdoc9      CHAR(20),
  pk_invbasdoc    CHAR(20),
  tbatchtime      CHAR(19),
  tchecktime      CHAR(19),
  ts              CHAR(19),
  vbatchcode      VARCHAR2(46),
  vdef1           VARCHAR2(151),
  vdef10          VARCHAR2(151),
  vdef11          VARCHAR2(151),
  vdef12          VARCHAR2(151),
  vdef13          VARCHAR2(151),
  vdef14          VARCHAR2(151),
  vdef15          VARCHAR2(151),
  vdef16          VARCHAR2(151),
  vdef17          VARCHAR2(151),
  vdef18          VARCHAR2(151),
  vdef19          VARCHAR2(151),
  vdef2           VARCHAR2(151),
  vdef20          VARCHAR2(151),
  vdef3           VARCHAR2(151),
  vdef4           VARCHAR2(151),
  vdef5           VARCHAR2(151),
  vdef6           VARCHAR2(151),
  vdef7           VARCHAR2(151),
  vdef8           VARCHAR2(151),
  vdef9           VARCHAR2(151),
  vnote           VARCHAR2(181),
  vvendbatchcode  VARCHAR2(46)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate indexes 
create unique index U_IND_INV_CODE_01 on EHPTA_BATCHCODE (PK_INVBASDOC, VBATCHCODE)
  tablespace NNC_INDEX01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 128K
    next 128K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table EHPTA_BATCHCODE
  add constraint PK_SCM_BATCHCODE_01 primary key (PK_BATCHCODE)
  using index 
  tablespace NNC_INDEX01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 128K
    next 128K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );