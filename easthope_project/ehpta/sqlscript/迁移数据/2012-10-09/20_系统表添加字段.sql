
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

-- 销售发票 END


alter table BD_REFTABLE modify reftablename varchar2(1024);

alter table so_saleinvoice add salecode varchar2(20);

alter table so_sale add settleflag char(1) ;

alter table so_sale add settledate char(10);

alter table ic_general_h add pk_contract char(20);

alter table so_saleorder_b add rebateflag char(1);


-- 2012-10-12添加

alter table so_sale add issince char(1);

-- 2012-10-19添加
alter table so_sale add vouchid varchar2(20);
alter table so_sale add vouchmny number(32,8);

-- 2012-10-24添加
alter table so_saleorder_b add ctransmodeid varchar2(20);

alter table so_saleorder_b add pk_transport_b varchar2(20);
alter table so_saleorder_b add transprice number(32,8);

alter table so_saleorder_b add copermodeid varchar2(20);


alter table so_sale add estoraddrid varchar2(20);

alter table so_saleorder_b add pk_storcont_b varchar2(20);
alter table so_saleorder_b add storprice number(32,8);


-- 2012-10-27 添加
alter table ic_general_h add estoraddrid varchar2(20);

alter table ic_general_b add transprice number(32,8);
alter table ic_general_b add storprice number(32,8);
alter table ic_general_b add copermodeid varchar2(20);

-- 2012-10-29 添加
alter table ic_general_b add pk_transport_b varchar2(20);

alter table ic_general_b add pk_storcont_b varchar2(20);

-- 2012-11-02
alter table so_sale add settletype varchar2(10);

-- 2012-11-07
alter table so_sale add totalnnumber number(32,8);
alter table so_sale add totalcnnumber varchar2(128);

