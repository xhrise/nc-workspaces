create table EHPTA_CALC_SALEINV_BALANCE_B
(
  pk_saleinv_balance   CHAR(20),
  pk_saleinv_balance_b CHAR(20) not null,
  pk_cumandoc          CHAR(20),
  custname             VARCHAR2(64),
  firstmny             NUMBER(32,8),
  currmny              NUMBER(32,8),
  totalmny             NUMBER(32,8),
  balance              NUMBER(32,8),
  ts                   CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  dr                   INTEGER default 0,
  def1                 VARCHAR2(128),
  def2                 VARCHAR2(128),
  def3                 VARCHAR2(128),
  def4                 VARCHAR2(128),
  def5                 VARCHAR2(128),
  def6                 VARCHAR2(128),
  def7                 VARCHAR2(128),
  def8                 VARCHAR2(128),
  def9                 VARCHAR2(128),
  def10                VARCHAR2(128),
  def11                VARCHAR2(128),
  def12                VARCHAR2(128),
  def13                VARCHAR2(128),
  def14                VARCHAR2(128),
  def15                VARCHAR2(128),
  def16                VARCHAR2(128),
  def17                VARCHAR2(128),
  def18                VARCHAR2(128),
  def19                VARCHAR2(128),
  def20                VARCHAR2(128)
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
alter table EHPTA_CALC_SALEINV_BALANCE_B
  add constraint PK_EHPTA_CALC_SALEINV_BALA_B primary key (PK_SALEINV_BALANCE_B)
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


create table EHPTA_CALC_SALEINV_BALANCE_H
(
  pk_saleinv_balance CHAR(20) not null,
  period             VARCHAR2(20),
  vbillno            VARCHAR2(32),
  dmakedate          CHAR(10),
  voperatorid        CHAR(20),
  dapprovedate       CHAR(10),
  vapproveid         CHAR(20),
  vbillstatus        INTEGER,
  pk_corp            CHAR(4),
  pk_busitype        VARCHAR2(20),
  pk_billtype        VARCHAR2(20),
  vapprovenote       VARCHAR2(128),
  dr                 INTEGER default 0,
  ts                 CHAR(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
  def1               VARCHAR2(128),
  def2               VARCHAR2(128),
  def3               VARCHAR2(128),
  def4               VARCHAR2(128),
  def5               VARCHAR2(128),
  def6               VARCHAR2(128),
  def7               VARCHAR2(128),
  def8               VARCHAR2(128),
  def9               VARCHAR2(128),
  def10              VARCHAR2(128)
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
alter table EHPTA_CALC_SALEINV_BALANCE_H
  add constraint PK_EHPTA_CALC_SALEINV_BALA_H primary key (PK_SALEINV_BALANCE)
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
