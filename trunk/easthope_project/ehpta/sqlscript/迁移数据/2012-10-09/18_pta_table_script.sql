
create table NCCW.EHPTA_ADJUST
(
  pk_adjust    CHAR(20) not null,
  type         VARCHAR2(10),
  reason       VARCHAR2(256),
  mny          NUMBER(32,8),
  pk_contract  CHAR(20),
  pk_cubasdoc  CHAR(20),
  memo         VARCHAR2(512),
  vbillno      VARCHAR2(32),
  adjustdate   CHAR(10),
  managerid    CHAR(20),
  vbillstatus  INTEGER,
  pk_corp      CHAR(4),
  pk_busitype  CHAR(20),
  pk_billtype  VARCHAR2(20),
  dapprovedate CHAR(10),
  voperatorid  CHAR(20),
  vapprovenote VARCHAR2(128),
  vapproveid   CHAR(20),
  dmakedate    CHAR(10),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss')
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
alter table NCCW.EHPTA_ADJUST
  add constraint PK_EHPTA_ADJUST primary key (PK_ADJUST)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_AIDCUST
(
  pk_aidcust  CHAR(20) not null,
  pk_contract CHAR(20),
  pk_custdoc  CHAR(20),
  custname    VARCHAR2(64),
  custcode    VARCHAR2(64),
  dr          INTEGER default 0,
  ts          CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1        VARCHAR2(128),
  def2        VARCHAR2(128),
  def3        VARCHAR2(128),
  def4        VARCHAR2(128),
  def5        VARCHAR2(128)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_AIDCUST
  add constraint PK_EHPTA_AIDCUST primary key (PK_AIDCUST)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_AIDCUST_HISTORY
(
  pk_aidcust_his CHAR(20) not null,
  version_his    INTEGER,
  pk_aidcust     CHAR(20),
  pk_contract    CHAR(20),
  pk_custdoc     CHAR(20),
  custname       VARCHAR2(64),
  custcode       VARCHAR2(64),
  dr             INTEGER default 0,
  ts             CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1           VARCHAR2(128),
  def2           VARCHAR2(128),
  def3           VARCHAR2(128),
  def4           VARCHAR2(128),
  def5           VARCHAR2(128)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_AIDCUST_HISTORY
  add constraint PK_EHPTA_AIDCUST_HISTORY primary key (PK_AIDCUST_HIS)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_BATCHCODE
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
  ts              CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
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
create unique index NCCW.U_IND_INV_CODE_01 on NCCW.EHPTA_BATCHCODE (PK_INVBASDOC, VBATCHCODE)
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
alter table NCCW.EHPTA_BATCHCODE
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


create table NCCW.EHPTA_CALC_INTEREST
(
  pk_calcinterest CHAR(20) not null,
  pk_custdoc      CHAR(20),
  period          VARCHAR2(10),
  vbillno         VARCHAR2(32),
  dmakedate       CHAR(10),
  voperatorid     CHAR(20),
  dapprovedate    CHAR(10),
  vapproveid      CHAR(20),
  vbillstatus     INTEGER,
  pk_corp         CHAR(4),
  pk_busitype     VARCHAR2(20),
  pk_billtype     VARCHAR2(20),
  vapprovenote    VARCHAR2(128),
  dr              INTEGER default 0,
  ts              CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1            VARCHAR2(128),
  def2            VARCHAR2(128),
  def3            VARCHAR2(128),
  def4            VARCHAR2(128),
  def5            VARCHAR2(128),
  def6            VARCHAR2(128),
  def7            VARCHAR2(128),
  def8            VARCHAR2(128),
  def9            VARCHAR2(128),
  def10           VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_INTEREST
  add constraint PK_EHPTA_CALC_INTEREST primary key (PK_CALCINTEREST)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_INTEREST_B
(
  pk_calcinterest_b CHAR(20) not null,
  pk_calcinterest   CHAR(20),
  pk_receivable     CHAR(20),
  djzbid            VARCHAR2(32),
  contno            VARCHAR2(20),
  version           VARCHAR2(10),
  transtype         VARCHAR2(10),
  custname          VARCHAR2(64),
  redate            CHAR(10),
  remny             NUMBER(32,8),
  billtype          VARCHAR2(50),
  interestto        VARCHAR2(50),
  days              INTEGER,
  rate              NUMBER(32,8),
  interestmny       NUMBER(32,8),
  actualmny         NUMBER(32,8),
  memo              VARCHAR2(512),
  calcflag          CHAR(1),
  dr                INTEGER default 0,
  ts                CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1              VARCHAR2(128),
  def2              VARCHAR2(128),
  def3              VARCHAR2(128),
  def4              VARCHAR2(128),
  def5              VARCHAR2(128),
  def6              VARCHAR2(128),
  def7              VARCHAR2(128),
  def8              VARCHAR2(128),
  def9              VARCHAR2(128),
  def10             VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_INTEREST_B
  add constraint PK_EHPTA_CALC_INTEREST_B primary key (PK_CALCINTEREST_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_REBATES_B
(
  pk_rebates   CHAR(20),
  pk_rebates_b CHAR(20) not null,
  rowno        VARCHAR2(10),
  pk_cumandoc  CHAR(20),
  custname     VARCHAR2(64),
  pk_contract  CHAR(20),
  concode      VARCHAR2(64),
  pk_invmandoc CHAR(20),
  invname      VARCHAR2(64),
  period       VARCHAR2(32),
  num          NUMBER(32,8),
  nnumber      NUMBER(32,8),
  comprate     NUMBER(32,8),
  preprice     NUMBER(32,8),
  premny       NUMBER(32,8),
  adjustmny    NUMBER(32,8),
  actmny       NUMBER(32,8),
  orderbids    VARCHAR2(4000),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128),
  def11        VARCHAR2(128),
  def12        VARCHAR2(128),
  def13        VARCHAR2(128),
  def14        VARCHAR2(128),
  def15        VARCHAR2(128),
  def16        VARCHAR2(128),
  def17        VARCHAR2(128),
  def18        VARCHAR2(128),
  def19        VARCHAR2(128),
  def20        VARCHAR2(128),
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr           INTEGER default 0
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
alter table NCCW.EHPTA_CALC_REBATES_B
  add constraint PK_EHPTA_CALC_REBATES_B primary key (PK_REBATES_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_REBATES_H
(
  pk_rebates   CHAR(20) not null,
  period       VARCHAR2(10),
  vbillno      VARCHAR2(32),
  dmakedate    CHAR(10),
  voperatorid  CHAR(20),
  dapprovedate CHAR(10),
  vapproveid   CHAR(20),
  vbillstatus  INTEGER,
  pk_corp      CHAR(4),
  pk_busitype  VARCHAR2(20),
  pk_billtype  VARCHAR2(20),
  vapprovenote VARCHAR2(128),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_REBATES_H
  add constraint PK_EHPTA_CALC_REBATES_H primary key (PK_REBATES)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_SALE_BALANCE_B
(
  pk_sale_balance   CHAR(20),
  pk_sale_balance_b CHAR(20) not null,
  pk_cumandoc       CHAR(20),
  custname          VARCHAR2(64),
  pk_contract       CHAR(20),
  concode           VARCHAR2(32),
  connamed          VARCHAR2(64),
  firstmny          NUMBER(32,8),
  currmny           NUMBER(32,8),
  salemny           NUMBER(32,8),
  salebalance       NUMBER(32,8),
  adtype2           NUMBER(32,8),
  adtype3           NUMBER(32,8),
  adtype4           NUMBER(32,8),
  balance           NUMBER(32,8),
  ts                CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr                INTEGER default 0,
  def1              VARCHAR2(128),
  def2              VARCHAR2(128),
  def3              VARCHAR2(128),
  def4              VARCHAR2(128),
  def5              VARCHAR2(128),
  def6              VARCHAR2(128),
  def7              VARCHAR2(128),
  def8              VARCHAR2(128),
  def9              VARCHAR2(128),
  def10             VARCHAR2(128),
  def11             VARCHAR2(128),
  def12             VARCHAR2(128),
  def13             VARCHAR2(128),
  def14             VARCHAR2(128),
  def15             VARCHAR2(128),
  def16             VARCHAR2(128),
  def17             VARCHAR2(128),
  def18             VARCHAR2(128),
  def19             VARCHAR2(128),
  def20             VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_SALE_BALANCE_B
  add constraint PK_EHPTA_CALC_SALE_BALANCE_B primary key (PK_SALE_BALANCE_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_SALE_BALANCE_H
(
  pk_sale_balance CHAR(20) not null,
  period          VARCHAR2(20),
  vbillno         VARCHAR2(32),
  dmakedate       CHAR(10),
  voperatorid     CHAR(20),
  dapprovedate    CHAR(10),
  vapproveid      CHAR(20),
  vbillstatus     INTEGER,
  pk_corp         CHAR(4),
  pk_busitype     VARCHAR2(20),
  pk_billtype     VARCHAR2(20),
  vapprovenote    VARCHAR2(128),
  dr              INTEGER default 0,
  ts              CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1            VARCHAR2(128),
  def2            VARCHAR2(128),
  def3            VARCHAR2(128),
  def4            VARCHAR2(128),
  def5            VARCHAR2(128),
  def6            VARCHAR2(128),
  def7            VARCHAR2(128),
  def8            VARCHAR2(128),
  def9            VARCHAR2(128),
  def10           VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_SALE_BALANCE_H
  add constraint PK_EHPTA_CALC_SALE_BALANCE_H primary key (PK_SALE_BALANCE)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_SETTLEMENT
(
  pk_settlement CHAR(20) not null,
  csaleid       CHAR(20),
  pk_contract   CHAR(20),
  pk_maintain   CHAR(20),
  cinvbasdocid  CHAR(20),
  ccustomerid   CHAR(20),
  custname      VARCHAR2(64),
  concode       VARCHAR2(64),
  vreceiptcode  VARCHAR2(64),
  cmakedate     CHAR(10),
  invcode       VARCHAR2(64),
  invname       VARCHAR2(64),
  measname      VARCHAR2(64),
  nnumber       NUMBER(32,8),
  lastingprice  NUMBER(32,8),
  clastingmny   NUMBER(32,8),
  settlemny     NUMBER(32,8),
  csettlemny    NUMBER(32,8),
  clsmny        NUMBER(32,8),
  settleflag    CHAR(1),
  settledate    CHAR(10),
  ts            CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr            INTEGER default 0,
  pk_corp       CHAR(4),
  pk_busitype   CHAR(20),
  pk_billtype   VARCHAR2(20),
  vbillstatus   INTEGER,
  vapproveid    CHAR(20),
  dapprovedate  CHAR(10),
  voperatorid   CHAR(20),
  vbillno       VARCHAR2(32),
  vapprovenote  VARCHAR2(128),
  vbusicode     VARCHAR2(20),
  dmakedate     CHAR(10)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 15104K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_CALC_SETTLEMENT
  add constraint PK_EHPTA_CALC_SETTLEMENT primary key (PK_SETTLEMENT)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_STORFEE_B
(
  pk_storfee   CHAR(20),
  pk_storfee_b CHAR(20) not null,
  vbatchcode   VARCHAR2(64),
  pk_stordoc   CHAR(20),
  pk_invmandoc CHAR(20),
  invname      VARCHAR2(64),
  indate       CHAR(10),
  outdate      CHAR(10),
  pk_cumandoc  CHAR(20),
  vreceiptcode VARCHAR2(32),
  cgeneralhid  CHAR(20),
  cgeneralbid  CHAR(20),
  outcode      VARCHAR2(32),
  overdate     CHAR(10),
  noutnum      NUMBER(32,8),
  feetype      VARCHAR2(32),
  signprice    NUMBER(32,8),
  hmny         NUMBER(32,8),
  stordays     NUMBER(32,8),
  freedays     NUMBER(32,8),
  days         NUMBER(32,8),
  storprice    NUMBER(32,8),
  stormny      NUMBER(32,8),
  pk_contract  CHAR(20),
  concode      VARCHAR2(32),
  contracttype VARCHAR2(16),
  rowno        VARCHAR2(10),
  settleflag   CHAR(1),
  settledate   CHAR(10),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128),
  def11        VARCHAR2(128),
  def12        VARCHAR2(128),
  def13        VARCHAR2(128),
  def14        VARCHAR2(128),
  def15        VARCHAR2(128),
  def16        VARCHAR2(128),
  def17        VARCHAR2(128),
  def18        VARCHAR2(128),
  def19        VARCHAR2(128),
  def20        VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_STORFEE_B
  add constraint PK_EHPTA_CALC_STORFEE_B primary key (PK_STORFEE_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_STORFEE_H
(
  pk_storfee   CHAR(20) not null,
  period       VARCHAR2(10),
  vbillno      VARCHAR2(32),
  dmakedate    CHAR(10),
  voperatorid  CHAR(20),
  dapprovedate CHAR(10),
  vapproveid   CHAR(20),
  vbillstatus  INTEGER,
  pk_corp      CHAR(4),
  pk_busitype  VARCHAR2(20),
  pk_billtype  VARCHAR2(20),
  vapprovenote VARCHAR2(128),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_STORFEE_H
  add constraint PK_EHPTA_CALC_STORFEE_H primary key (PK_STORFEE)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_UNDER_TRANSFEE_B
(
  pk_transfee    CHAR(20),
  pk_transfee_b  CHAR(20) not null,
  cgeneralhid    CHAR(20),
  cbillno        VARCHAR2(32),
  sdate          CHAR(10),
  pk_sstordoc    CHAR(20),
  saddress       VARCHAR2(64),
  eaddress       VARCHAR2(64),
  piersfee       NUMBER(32,8),
  inlandshipfee  NUMBER(32,8),
  carfee         NUMBER(32,8),
  pk_transport   CHAR(20),
  pk_transport_b CHAR(20),
  transtype      VARCHAR2(10),
  fee            NUMBER(32,8),
  num            NUMBER(32,8),
  transmny       NUMBER(32,8),
  settleflag     CHAR(1),
  settledate     CHAR(10),
  rowno          VARCHAR2(10),
  dr             INTEGER default 0,
  ts             CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1           VARCHAR2(128),
  def2           VARCHAR2(128),
  def3           VARCHAR2(128),
  def4           VARCHAR2(128),
  def5           VARCHAR2(128),
  def6           VARCHAR2(128),
  def7           VARCHAR2(128),
  def8           VARCHAR2(128),
  def9           VARCHAR2(128),
  def10          VARCHAR2(128),
  def11          VARCHAR2(128),
  def12          VARCHAR2(128),
  def13          VARCHAR2(128),
  def14          VARCHAR2(128),
  def15          VARCHAR2(128),
  def16          VARCHAR2(128),
  def17          VARCHAR2(128),
  def18          VARCHAR2(128),
  def19          VARCHAR2(128),
  def20          VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_UNDER_TRANSFEE_B
  add constraint PK_EHPTA_CALC_UNDER_TRANSFEE_B primary key (PK_TRANSFEE_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_UNDER_TRANSFEE_H
(
  pk_transfee  CHAR(20) not null,
  period       VARCHAR2(10),
  vbillno      VARCHAR2(32),
  dmakedate    CHAR(10),
  voperatorid  CHAR(20),
  dapprovedate CHAR(10),
  vapproveid   CHAR(20),
  vbillstatus  INTEGER,
  pk_corp      CHAR(4),
  pk_busitype  VARCHAR2(20),
  pk_billtype  VARCHAR2(20),
  vapprovenote VARCHAR2(128),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_UNDER_TRANSFEE_H
  add constraint PK_EHPTA_CALC_UNDER_TRANSFEE_H primary key (PK_TRANSFEE)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_UPPER_TRANSFEE_B
(
  pk_transfee   CHAR(20),
  pk_transfee_b CHAR(20) not null,
  shipname      VARCHAR2(64),
  sourceno      VARCHAR2(32),
  sdate         CHAR(10),
  num           NUMBER(32,8),
  edate         CHAR(10),
  eaddr         VARCHAR2(64),
  pk_invmandoc  CHAR(20),
  pk_measdoc    CHAR(20),
  numof         NUMBER(32,8),
  recnum        NUMBER(32,8),
  outnum        NUMBER(32,8),
  dieselprice   NUMBER(32,8),
  rises         NUMBER(32,8),
  fee           NUMBER(32,8),
  transmny      NUMBER(32,8),
  outmny        NUMBER(32,8),
  paymny        NUMBER(32,8),
  settleflag    CHAR(1),
  settledate    CHAR(10),
  dr            INTEGER default 0,
  ts            CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1          VARCHAR2(128),
  def2          VARCHAR2(128),
  def3          VARCHAR2(128),
  def4          VARCHAR2(128),
  def5          VARCHAR2(128),
  def6          VARCHAR2(128),
  def7          VARCHAR2(128),
  def8          VARCHAR2(128),
  def9          VARCHAR2(128),
  def10         VARCHAR2(128),
  def11         VARCHAR2(128),
  def12         VARCHAR2(128),
  def13         VARCHAR2(128),
  def14         VARCHAR2(128),
  def15         VARCHAR2(128),
  def16         VARCHAR2(128),
  def17         VARCHAR2(128),
  def18         VARCHAR2(128),
  def19         VARCHAR2(128),
  def20         VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_UPPER_TRANSFEE_B
  add constraint PK_EHPTA_CALC_UPPER_TRANSFEE_B primary key (PK_TRANSFEE_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_CALC_UPPER_TRANSFEE_H
(
  pk_transfee  CHAR(20) not null,
  period       VARCHAR2(10),
  vbillno      VARCHAR2(32),
  dmakedate    CHAR(10),
  voperatorid  CHAR(20),
  dapprovedate CHAR(10),
  vapproveid   CHAR(20),
  vbillstatus  INTEGER,
  pk_corp      CHAR(4),
  pk_busitype  VARCHAR2(20),
  pk_billtype  VARCHAR2(20),
  vapprovenote VARCHAR2(128),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128)
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
alter table NCCW.EHPTA_CALC_UPPER_TRANSFEE_H
  add constraint PK_EHPTA_CALC_UPPER_TRANSFEE_H primary key (PK_TRANSFEE)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_HANGINGPRICE
(
  pk_hangingprice     CHAR(20) not null,
  maintenancedate     VARCHAR2(20),
  chname              VARCHAR2(100),
  maintenancetime     CHAR(19),
  priceclassification VARCHAR2(50),
  pricestandard       VARCHAR2(200),
  guapaiprice         NUMBER(32,8),
  jiesuanprice        NUMBER(32,8),
  remarks             VARCHAR2(200),
  vbillstatus         INTEGER,
  voperatorid         CHAR(20),
  singledate          CHAR(10),
  vapproveid          CHAR(20),
  dapprovedate        CHAR(10),
  approvalstatus      CHAR(1),
  vapprovenote        VARCHAR2(100),
  def1                VARCHAR2(100),
  def2                VARCHAR2(100),
  def3                VARCHAR2(100),
  def4                VARCHAR2(100),
  def5                VARCHAR2(100),
  def6                VARCHAR2(100),
  def7                VARCHAR2(100),
  def8                VARCHAR2(100),
  def9                VARCHAR2(100),
  def10               VARCHAR2(100),
  pk_corp             CHAR(4),
  ts                  CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr                  INTEGER default 0,
  weihudate           VARCHAR2(64),
  pk_invbasdoc        CHAR(20),
  invspec             VARCHAR2(64)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_HANGINGPRICE
  add constraint PK_EHPTA_HANGINGPRICE primary key (PK_HANGINGPRICE)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_MAINTAIN
(
  pk_maintain  CHAR(20) not null,
  maindate     CHAR(10),
  pk_invbasdoc CHAR(20),
  maintime     CHAR(19),
  type         VARCHAR2(2),
  standard     VARCHAR2(2),
  listingmny   NUMBER(32,8),
  settlemny    NUMBER(32,8),
  memo         VARCHAR2(256),
  vbillstatus  INTEGER,
  voperatorid  CHAR(20),
  dmakedate    CHAR(10),
  vapproveid   CHAR(20),
  dapprovedate CHAR(10),
  vapprovenote VARCHAR2(256),
  pk_corp      CHAR(4),
  pk_busitype  VARCHAR2(20),
  pk_billtype  VARCHAR2(20),
  vbillno      VARCHAR2(32),
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr           INTEGER default 0,
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128)
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
alter table NCCW.EHPTA_MAINTAIN
  add constraint PK_EHPTA_MAINTAIN primary key (PK_MAINTAIN)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_PREPOLICY
(
  pk_prepolicy CHAR(20) not null,
  pk_contract  CHAR(20),
  upcomprate   NUMBER(32,8),
  lowcomprate  NUMBER(32,8),
  prerate      NUMBER(32,8),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1         VARCHAR2(128),
  def5         VARCHAR2(128),
  def4         VARCHAR2(128),
  def3         VARCHAR2(128),
  def2         VARCHAR2(128)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_PREPOLICY
  add constraint PK_EHPTA_PREPOLICY primary key (PK_PREPOLICY)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_PREPOLICY_HISTORY
(
  pk_prepolicy_his CHAR(20) not null,
  version_his      INTEGER,
  pk_prepolicy     CHAR(20),
  pk_contract      CHAR(20),
  upcomprate       NUMBER(32,8),
  lowcomprate      NUMBER(32,8),
  prerate          NUMBER(32,8),
  dr               INTEGER default 0,
  ts               CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1             VARCHAR2(128),
  def5             VARCHAR2(128),
  def4             VARCHAR2(128),
  def3             VARCHAR2(128),
  def2             VARCHAR2(128)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_PREPOLICY_HISTORY
  add constraint PK_EHPTA_PREPOLICY_HISTORY primary key (PK_PREPOLICY_HIS)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_SALE_CONTRACT
(
  pk_contract    CHAR(20) not null,
  vbillno        VARCHAR2(32),
  contype        VARCHAR2(32),
  purchcode      CHAR(20),
  purchname      VARCHAR2(64),
  bargainor      VARCHAR2(64),
  orderdate      CHAR(10),
  orderaddress   VARCHAR2(64),
  connamed       VARCHAR2(128),
  sdate          CHAR(10),
  edate          CHAR(10),
  pk_deptdoc     CHAR(20),
  pk_psndoc      CHAR(20),
  rebate_flag    CHAR(1),
  poundsofpoor   NUMBER(32,8),
  termination    VARCHAR2(64),
  version        INTEGER,
  stopcontract   CHAR(1),
  pay_cutoffdate CHAR(10),
  deliverydate   CHAR(10),
  memo           VARCHAR2(512),
  vbillstatus    INTEGER,
  pk_corp        CHAR(4),
  pk_busitype    VARCHAR2(20),
  pk_billtype    VARCHAR2(20),
  vapproveid     CHAR(20),
  dapprovedate   CHAR(10),
  voperatorid    CHAR(20),
  vapprovenote   VARCHAR2(64),
  dmakedate      CHAR(10),
  def1           VARCHAR2(128),
  def2           VARCHAR2(128),
  def3           VARCHAR2(128),
  def4           VARCHAR2(128),
  def5           VARCHAR2(128),
  def6           VARCHAR2(128),
  def7           VARCHAR2(128),
  def8           VARCHAR2(128),
  def9           VARCHAR2(128),
  def10          VARCHAR2(128),
  ts             CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr             INTEGER default 0,
  custcode       VARCHAR2(32),
  close_flag     CHAR(1) default 'N',
  contprice      NUMBER(32,8)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_SALE_CONTRACT
  add constraint PK_EHPTA_SALE_CONTRACT primary key (PK_CONTRACT)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_SALE_CONTRACT_B
(
  pk_contract_b CHAR(20) not null,
  pk_contract   CHAR(20),
  pk_invbasdoc  CHAR(20),
  invname       VARCHAR2(64),
  invspec       VARCHAR2(64),
  pak_invspec   VARCHAR2(64),
  pk_measdoc    CHAR(20),
  execduration  INTEGER,
  durationunit  VARCHAR2(2),
  sdate         VARCHAR2(20),
  edate         VARCHAR2(20),
  num           NUMBER(32,8),
  totalnum      NUMBER(32,8),
  numof         NUMBER(32,8),
  preprice      NUMBER(32,8),
  pk_priceweb   CHAR(20),
  memo          VARCHAR2(512),
  ts            CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr            INTEGER default 0,
  def1          VARCHAR2(128),
  def2          VARCHAR2(128),
  def3          VARCHAR2(128),
  def4          VARCHAR2(128),
  def5          VARCHAR2(128),
  def6          VARCHAR2(128),
  def7          VARCHAR2(128),
  def8          VARCHAR2(128),
  def9          VARCHAR2(128),
  def10         VARCHAR2(128),
  taxrate       NUMBER(32,8)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_SALE_CONTRACT_B
  add constraint PK_EHPTA_SALE_CONTRACT_B primary key (PK_CONTRACT_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_SALE_CONTRACT_BS
(
  pk_contract_b CHAR(20) not null,
  pk_contract   CHAR(20),
  pk_invbasdoc  CHAR(20),
  invname       VARCHAR2(64),
  invspec       VARCHAR2(64),
  pk_measdoc    CHAR(20),
  num           NUMBER(32,8),
  numof         NUMBER(32,8),
  taxprice      NUMBER(32,8),
  sumpricetax   NUMBER(32,8),
  taxrate       NUMBER(32,8),
  tax           NUMBER(32,8),
  notaxloan     NUMBER(32,8),
  memo          VARCHAR2(512),
  dr            INTEGER default 0,
  ts            CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1          VARCHAR2(128),
  def2          VARCHAR2(128),
  def3          VARCHAR2(128),
  def4          VARCHAR2(128),
  def5          VARCHAR2(128),
  def6          VARCHAR2(128),
  def7          VARCHAR2(128),
  def8          VARCHAR2(128),
  def9          VARCHAR2(128),
  def10         VARCHAR2(128)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_SALE_CONTRACT_BS
  add constraint PK_EHPTA_SALE_CONTRACT_BS primary key (PK_CONTRACT_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_SALE_CONTRACT_B_HISTORY
(
  pk_contract_b_his CHAR(20) not null,
  version_his       INTEGER,
  pk_contract_b     CHAR(20),
  pk_contract       CHAR(20),
  pk_invbasdoc      CHAR(20),
  invname           VARCHAR2(64),
  invspec           VARCHAR2(64),
  pk_invspec        VARCHAR2(64),
  pk_measdoc        CHAR(20),
  execduration      INTEGER,
  durationunit      VARCHAR2(2),
  sdate             CHAR(10),
  edate             CHAR(10),
  num               NUMBER(32,8),
  totalnum          NUMBER(32,8),
  numof             INTEGER,
  preprice          NUMBER(32,8),
  pk_priceweb       CHAR(20),
  memo              VARCHAR2(512),
  ts                CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr                INTEGER default 0,
  def1              VARCHAR2(128),
  def2              VARCHAR2(128),
  def3              VARCHAR2(128),
  def4              VARCHAR2(128),
  def5              VARCHAR2(128),
  def6              VARCHAR2(128),
  def7              VARCHAR2(128),
  def8              VARCHAR2(128),
  def9              VARCHAR2(128),
  def10             VARCHAR2(128),
  taxrate           NUMBER(32,8)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_SALE_CONTRACT_B_HISTORY
  add constraint PK_EHPTA_SALE_CONTRACT_B_HISTO primary key (PK_CONTRACT_B_HIS)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_SALE_CONTRACT_HISTORY
(
  pk_contract_his CHAR(20) not null,
  pk_contract     CHAR(20),
  vbillno         VARCHAR2(32),
  contype         VARCHAR2(32),
  purchcode       CHAR(20),
  custcode        VARCHAR2(64),
  purchname       VARCHAR2(64),
  bargainor       VARCHAR2(64),
  orderdate       CHAR(10),
  orderaddress    VARCHAR2(64),
  connamed        VARCHAR2(128),
  sdate           CHAR(10),
  edate           CHAR(10),
  pk_deptdoc      CHAR(20),
  pk_psndoc       CHAR(20),
  rebate_flag     CHAR(1),
  poundsofpoor    NUMBER(32,8),
  close_flag      CHAR(1),
  termination     VARCHAR2(64),
  version         INTEGER,
  stopcontract    CHAR(1),
  pay_cutoffdate  CHAR(10),
  deliverydate    CHAR(10),
  memo            VARCHAR2(512),
  vbillstatus     INTEGER,
  pk_corp         CHAR(4),
  pk_busitype     VARCHAR2(20),
  pk_billtype     VARCHAR2(20),
  vapproveid      CHAR(20),
  dapprovedate    CHAR(10),
  voperatorid     CHAR(20),
  vapprovenote    VARCHAR2(64),
  dmakedate       CHAR(10),
  def1            VARCHAR2(128),
  def2            VARCHAR2(128),
  def3            VARCHAR2(128),
  def4            VARCHAR2(128),
  def5            VARCHAR2(128),
  def6            VARCHAR2(128),
  def7            VARCHAR2(128),
  def8            VARCHAR2(128),
  def9            VARCHAR2(128),
  def10           VARCHAR2(128),
  ts              CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr              INTEGER default 0,
  contprice       NUMBER(32,8)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_SALE_CONTRACT_HISTORY
  add constraint PK_EHPTA_SALE_CONTRACT_HISTORY primary key (PK_CONTRACT_HIS)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_STORCONTRACT
(
  pk_storagedoc  CHAR(20) not null,
  vbillno        VARCHAR2(20),
  signdate       CHAR(10),
  signcompany    VARCHAR2(20),
  pk_stordoc     CHAR(20),
  sdate          CHAR(10),
  edate          CHAR(10),
  dmakedate      CHAR(10),
  voperatorid    CHAR(20),
  vbillstatus    INTEGER,
  pk_busitype    CHAR(20),
  pk_billtype    VARCHAR2(20),
  memo           VARCHAR2(512),
  vapproveid     CHAR(20),
  concessionsday INTEGER,
  vapprovenote   VARCHAR2(512),
  dapprovedate   CHAR(10),
  dr             INTEGER default 0,
  ts             CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  pk_corp        CHAR(4),
  ty_flag        CHAR(1),
  stopdate       CHAR(10),
  entrycharge    NUMBER(32,8),
  def1           VARCHAR2(100),
  def2           VARCHAR2(100),
  def3           VARCHAR2(100),
  def4           VARCHAR2(100),
  def5           VARCHAR2(100),
  def6           VARCHAR2(100),
  def7           VARCHAR2(100),
  def8           VARCHAR2(100),
  def9           VARCHAR2(100),
  def10          VARCHAR2(100),
  storaddr       VARCHAR2(64),
  version        VARCHAR2(5),
  stopcontract   CHAR(1)
)
tablespace NNC_DATA01
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
alter table NCCW.EHPTA_STORCONTRACT
  add constraint PK_EHPTA_STORCONTRACT primary key (PK_STORAGEDOC)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_STORCONTRACT_B
(
  pk_storcontract_b CHAR(20) not null,
  pk_storagedoc     CHAR(20),
  feetype           VARCHAR2(32),
  concesessionsday  INTEGER,
  storccontracttype VARCHAR2(128),
  signprice         NUMBER(32,8),
  memo              VARCHAR2(512),
  dr                INTEGER default 0,
  ts                CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1              VARCHAR2(128),
  def2              VARCHAR2(128),
  def3              VARCHAR2(128),
  def4              VARCHAR2(128),
  def5              VARCHAR2(128)
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
alter table NCCW.EHPTA_STORCONTRACT_B
  add constraint PK_EHPTA_STORCONTRACT_B primary key (PK_STORCONTRACT_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_STORCONTRACT_B_HISTORY
(
  pk_storcontract_b_his CHAR(20) not null,
  pk_storagedoc         CHAR(20),
  feetype               VARCHAR2(32),
  concesessionsday      INTEGER,
  storccontracttype     VARCHAR2(128),
  signprice             NUMBER(32,8),
  memo                  VARCHAR2(512),
  dr                    INTEGER default 0,
  ts                    CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1                  VARCHAR2(128),
  def2                  VARCHAR2(128),
  def3                  VARCHAR2(128),
  def4                  VARCHAR2(128),
  def5                  VARCHAR2(128),
  version_his           VARCHAR2(50)
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
alter table NCCW.EHPTA_STORCONTRACT_B_HISTORY
  add constraint PK_EHPTA_STORCONTRACT_B_HISTOR primary key (PK_STORCONTRACT_B_HIS)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_STORCONTRACT_HISTORY
(
  pk_storagedoc_his CHAR(20) not null,
  pk_storagedoc     CHAR(20),
  vbillno           VARCHAR2(20),
  signdate          CHAR(10),
  signcompany       VARCHAR2(20),
  pk_stordoc        CHAR(20),
  storaddr          VARCHAR2(64),
  sdate             CHAR(10),
  edate             CHAR(10),
  memo              VARCHAR2(512),
  ty_flag           CHAR(1),
  stopdate          CHAR(10),
  dmakedate         CHAR(10),
  voperatorid       CHAR(20),
  vbillstatus       INTEGER,
  pk_busitype       VARCHAR2(20),
  pk_billtype       VARCHAR2(20),
  vapprovenote      VARCHAR2(512),
  dapprovedate      CHAR(10),
  vapproveid        CHAR(20),
  pk_corp           CHAR(4),
  dr                INTEGER default 0,
  ts                CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1              VARCHAR2(128),
  def2              VARCHAR2(128),
  def3              VARCHAR2(128),
  def4              VARCHAR2(128),
  def5              VARCHAR2(128),
  def6              VARCHAR2(128),
  def7              VARCHAR2(128),
  def8              VARCHAR2(128),
  def9              VARCHAR2(128),
  def10             VARCHAR2(128),
  version           VARCHAR2(50),
  stopcontract      CHAR(1),
  close_flag        CHAR(1)
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
alter table NCCW.EHPTA_STORCONTRACT_HISTORY
  add constraint PK_EHPTA_STORCONTRACT_HISTORY primary key (PK_STORAGEDOC_HIS)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_TRANSPORT_CONTRACT
(
  pk_transport CHAR(20) not null,
  transtype    VARCHAR2(10),
  vbillno      VARCHAR2(20),
  transdate    CHAR(10),
  pk_trans     CHAR(20),
  pk_carrier   CHAR(20),
  pk_guarantee CHAR(20),
  sdate        CHAR(10),
  edate        CHAR(10),
  memo         VARCHAR2(128),
  close_flag   CHAR(1),
  closedate    CHAR(10),
  pk_corp      CHAR(4),
  pk_busitype  CHAR(20),
  pk_billtype  CHAR(20),
  vbillstatus  INTEGER,
  vapproveid   CHAR(20),
  dapprovedate CHAR(10),
  voperatorid  CHAR(20),
  dr           INTEGER default 0,
  ts           CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dmakedate    CHAR(10),
  vapprovenote VARCHAR2(128),
  def1         VARCHAR2(128),
  def2         VARCHAR2(128),
  def3         VARCHAR2(128),
  def4         VARCHAR2(128),
  def5         VARCHAR2(128),
  def6         VARCHAR2(128),
  def7         VARCHAR2(128),
  def8         VARCHAR2(128),
  def9         VARCHAR2(128),
  def10        VARCHAR2(128),
  transname    VARCHAR2(128),
  signaddr     VARCHAR2(128)
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
alter table NCCW.EHPTA_TRANSPORT_CONTRACT
  add constraint PK_EHPTA_TRANSPORT_CONTRACT primary key (PK_TRANSPORT)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


create table NCCW.EHPTA_TRANSPORT_CONTRACT_B
(
  pk_transport_b CHAR(20) not null,
  pk_transport   CHAR(20),
  pk_sstordoc    CHAR(20),
  sstoraddr      VARCHAR2(64),
  pk_estordoc    CHAR(20),
  estoraddr      VARCHAR2(64),
  pk_sendtype    CHAR(20),
  shipprice      NUMBER(32,8),
  dieselprice    NUMBER(32,8),
  shipregulation NUMBER(32,8),
  ashipprice     NUMBER(32,8),
  piersfee       NUMBER(32,8),
  storcarfee     NUMBER(32,8),
  storshipfee    NUMBER(32,8),
  inlandshipfee  NUMBER(32,8),
  carfee         NUMBER(32,8),
  transprice     NUMBER(32,8),
  memo           VARCHAR2(128),
  dr             INTEGER default 0,
  ts             CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1           VARCHAR2(128),
  def2           VARCHAR2(128),
  def3           VARCHAR2(128),
  def4           VARCHAR2(128),
  def5           VARCHAR2(128),
  def6           VARCHAR2(128),
  def7           VARCHAR2(128),
  def8           VARCHAR2(128),
  def9           VARCHAR2(128),
  def10          VARCHAR2(128)
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
alter table NCCW.EHPTA_TRANSPORT_CONTRACT_B
  add constraint PK_EHPTA_TRANSPORT_CONTRACT_B primary key (PK_TRANSPORT_B)
  using index 
  tablespace NNC_DATA01
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 256K
    next 256K
    minextents 1
    maxextents unlimited
    pctincrease 0
  );


