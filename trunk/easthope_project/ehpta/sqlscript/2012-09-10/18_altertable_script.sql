alter table so_sale add settleflag char(1) ;

alter table so_sale add settledate char(10);

alter table BD_REFTABLE modify reftablename varchar2(1024);

alter table so_saleinvoice add salecode varchar2(20);