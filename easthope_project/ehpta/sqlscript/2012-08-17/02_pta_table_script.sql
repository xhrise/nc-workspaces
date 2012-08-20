create table EHPTA_MAINTAIN
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
  ts           CHAR(19),
  dr           INTEGER,
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
-- Create/Recreate primary, unique and foreign key constraints 
alter table EHPTA_MAINTAIN
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