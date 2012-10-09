
-- 销售订单 START

alter table so_sale add iscredit char(1);
alter table so_sale add pk_transport char(20);
alter table so_sale add carriersname varchar2(64);
alter table so_sale add carriersaddr varchar2(128);
alter table so_sale add period varchar2(20);

alter table so_saleorder_b add saleinvoiceid char(20) ;
alter table so_saleorder_b add numof integer;
alter table so_saleorder_b add lastingprice number(32,8);
alter table so_saleorder_b add settlementprice number(32,8) ;
alter table so_saleorder_b add deliverydate char(10);
alter table so_saleorder_b add settlementdate char(10) ;
alter table so_saleorder_b add lssubprice number(32,8) ;

-- 销售订单 END

-- 销售发票 START

alter table so_saleinvoice add pk_contract char(20);
alter table so_saleinvoice add purchcode char(20);
alter table so_saleinvoice add iscredit char(1);


alter table so_saleinvoice_b add  lastingprice  DECIMAL(32,8)	;
alter table so_saleinvoice_b add  settlementprice	DECIMAL(32,8) ;
alter table so_saleinvoice_b add  submny	DECIMAL(32,8)	;
alter table so_saleinvoice_b add  retmny	DECIMAL(32,8)	;
alter table so_saleinvoice_b add  transmny	DECIMAL(32,8)	;
alter table so_saleinvoice_b add  evalmny	DECIMAL(32,8)	;

-- 销售发票 END


alter table BD_REFTABLE modify reftablename varchar2(1024);

alter table so_saleinvoice add salecode varchar2(20);

alter table so_sale add settleflag char(1) ;

alter table so_sale add settledate char(10);

alter table ic_general_h add pk_contract char(20);
alter table ic_general_h add pk_storcontract_b char(20);
alter table ic_general_h add storprice number(32,8);
alter table ic_general_h add pk_transport_b char(20);
alter table ic_general_h add transprice number(32,8);

alter table so_saleorder_b add rebateflag char(1);