create table ehpta_calc_storfee_h  (
   pk_storfee         char(20)                        not null,
   period             varchar2(10),
   vbillno            varchar2(32),
   dmakedate          char(10),
   voperatorid        char(20),
   dapprovedate       char(10),
   vapproveid         char(20),
   vbillstatus        INTEGER,
   pk_corp            char(4),
   pk_busitype        varchar2(20),
   pk_billtype        varchar2(20),
   vapprovenote       varchar2(128),
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
   def10              varchar2(128)
) tablespace NNC_DATA01
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

alter table ehpta_calc_storfee_h
   add constraint PK_EHPTA_CALC_STORFEE_H primary key (pk_storfee);