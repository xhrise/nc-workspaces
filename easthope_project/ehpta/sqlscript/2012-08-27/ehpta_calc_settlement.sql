create table ehpta_calc_settlement  (
   pk_settlement      char(20)                        not null,
   csaleid            char(20),
   pk_contract        char(20),
   pk_maintain        char(20),
   cinvbasdocid       char(20),
   ccustomerid        char(20),
   custname           VARCHAR2(64),
   concode            VARCHAR2(64),
   vreceiptcode       VARCHAR2(64),
   cmakedate          char(10),
   invcode            VARCHAR2(64),
   invname            VARCHAR2(64),
   measname           VARCHAR2(64),
   nnumber            DECIMAL(32, 8),
   lastingprice       DECIMAL(32, 8),
   clastingmny        DECIMAL(32, 8),
   settlemny          DECIMAL(32, 8),
   csettlemny         DECIMAL(32, 8),
   clsmny             DECIMAL(32, 8),
   settleflag         char(1),
   settledate         char(10),
   ts                 char(19),
   dr                 SMALLINT,
   pk_corp            char(4),
   pk_busitype        char(20),
   pk_billtype        VARCHAR2(20),
   vbillstatus        INTEGER,
   vapproveid         char(20),
   dapprovedate       char(10),
   voperatorid        char(20),
   vbillno            VARCHAR2(32),
   vapprovenote       VARCHAR2(128),
   vbusicode          VARCHAR2(20),
   dmakedate          char(10)
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

alter table ehpta_calc_settlement
   add constraint PK_EHPTA_CALC_SETTLEMENT primary key (pk_settlement);
