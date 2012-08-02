------------------------------------------------------
-- Export file for user NCCW                        --
-- Created by Administrator on 2012-08-01, 10:57:55 --
------------------------------------------------------

set define off
spool pta_table_script.log

prompt
prompt Creating table EHPTA_ADJUST
prompt ===========================
prompt
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

prompt
prompt Creating table EHPTA_AIDCUST
prompt ============================
prompt
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_AIDCUST_HISTORY
prompt ====================================
prompt
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
  ts             CHAR(19),
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_HANGINGPRICE
prompt =================================
prompt
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
  ts                  CHAR(19),
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_PREPOLICY
prompt ==============================
prompt
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_PREPOLICY_HISTORY
prompt ======================================
prompt
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
  ts               CHAR(19),
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_SALE_CONTRACT
prompt ==================================
prompt
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_SALE_CONTRACT_B
prompt ====================================
prompt
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_SALE_CONTRACT_BS
prompt =====================================
prompt
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_SALE_CONTRACT_B_HISTORY
prompt ============================================
prompt
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
  ts                CHAR(19),
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_SALE_CONTRACT_HISTORY
prompt ==========================================
prompt
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
  ts              CHAR(19),
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_STORCONTRACT
prompt =================================
prompt
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
  ts             CHAR(19),
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
    minextents 1
    maxextents unlimited
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
    minextents 1
    maxextents unlimited
  );

prompt
prompt Creating table EHPTA_STORCONTRACT_B
prompt ===================================
prompt
create table NCCW.EHPTA_STORCONTRACT_B
(
  pk_storcontract_b CHAR(20) not null,
  pk_storagedoc     CHAR(20),
  feetype           VARCHAR2(32),
  concesessionsday  INTEGER,
  storccontracttype VARCHAR2(128),
  signprice         NUMBER(32,8),
  memo              VARCHAR2(512),
  dr                INTEGER,
  ts                CHAR(19),
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

prompt
prompt Creating table EHPTA_STORCONTRACT_B_HISTORY
prompt ===========================================
prompt
create table NCCW.EHPTA_STORCONTRACT_B_HISTORY
(
  pk_storcontract_b_his CHAR(20) not null,
  pk_storagedoc         CHAR(20),
  feetype               VARCHAR2(32),
  concesessionsday      INTEGER,
  storccontracttype     VARCHAR2(128),
  signprice             NUMBER(32,8),
  memo                  VARCHAR2(512),
  dr                    INTEGER,
  ts                    CHAR(19),
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

prompt
prompt Creating table EHPTA_STORCONTRACT_HISTORY
prompt =========================================
prompt
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
  dr                INTEGER,
  ts                CHAR(19),
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

prompt
prompt Creating table EHPTA_TRANSPORT_CONTRACT
prompt =======================================
prompt
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
  dr           INTEGER,
  ts           CHAR(19),
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

prompt
prompt Creating table EHPTA_TRANSPORT_CONTRACT_B
prompt =========================================
prompt
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
  dr             INTEGER,
  ts             CHAR(19),
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


spool off
