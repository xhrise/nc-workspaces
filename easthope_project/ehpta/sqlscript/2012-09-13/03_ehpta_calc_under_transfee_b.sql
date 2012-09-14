create table ehpta_calc_under_transfee_b  (
   pk_transfee        char(20),
   pk_transfee_b      char(20)                        not null,
   cgeneralhid        char(20),
   cbillno            varchar2(32),
   sdate              char(10),
   pk_sstordoc        char(20),
   saddress           varchar2(64),
   eaddress           varchar2(64),
   piersfee           DECIMAL(32,8),
   inlandshipfee      DECIMAL(32,8),
   carfee             DECIMAL(32,8),
   pk_transport       char(20),
   pk_transport_b     char(20),
   transtype          varchar2(10),
   fee                DECIMAL(32,8),
   num                DECIMAL(32,8),
   transmny           DECIMAL(32,8),
   settleflag         char(1),
   settledate         char(10),
   rowno              varchar2(10),
   dr                 SMALLINT,
   ts                 char(19),
   def1               varchar2(128),
   def2               varchar2(128),
   def3               varchar2(128),
   def4               varchar2(128),
   def5               varchar2(128),
   def6               varchar2(128),
   def7               varchar2(128),
   def8               varchar2(128),
   def9               varchar2(128),
   def10              varchar2(128),
   def11              varchar2(128),
   def12              varchar2(128),
   def13              varchar2(128),
   def14              varchar2(128),
   def15              varchar2(128),
   def16              varchar2(128),
   def17              varchar2(128),
   def18              varchar2(128),
   def19              varchar2(128),
   def20              varchar2(128)
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

alter table ehpta_calc_under_transfee_b
   add constraint PK_EHPTA_CALC_UNDER_TRANSFEE_B primary key (pk_transfee_b);