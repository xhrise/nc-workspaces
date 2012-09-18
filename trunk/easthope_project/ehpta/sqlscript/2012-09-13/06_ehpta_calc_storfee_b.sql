create table ehpta_calc_storfee_b  (
   pk_storfee         char(20),
   pk_storfee_b       char(20)                        not null,
   vbatchcode         varchar2(64),
   pk_stordoc         char(20),
   pk_invmandoc       char(20),
   invname            varchar2(64),
   indate             char(10),
   outdate            char(10),
   pk_cumandoc        char(20),
   vreceiptcode       varchar2(32),
   cgeneralhid        char(20),
   cgeneralbid        char(20),
   outcode            varchar2(32),
   overdate           char(10),
   noutnum            DECIMAL(32, 8),
   feetype            varchar2(32),
   signprice          DECIMAL(32, 8),
   hmny               DECIMAL(32, 8),
   stordays           DECIMAL(32, 8),
   freedays           DECIMAL(32, 8),
   days               DECIMAL(32, 8),
   storprice          DECIMAL(32, 8),
   stormny            DECIMAL(32, 8),
   pk_contract        char(20),
   concode            varchar2(32),
   contracttype       varchar2(16),
   rowno              varchar2(10),
   settleflag         char(1),
   settledate         char(10),
   dr                 SMALLINT default 0,
   ts                 char(19) default to_char(sysdate , 'yyyy-MM-dd HH24:mm:ss'),
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
)  tablespace NNC_DATA01
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

alter table ehpta_calc_storfee_b
   add constraint PK_EHPTA_CALC_STORFEE_B primary key (pk_storfee_b);