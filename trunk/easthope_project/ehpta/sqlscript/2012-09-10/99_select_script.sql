select * from pub_billtemplet where bill_templetcaption like '%PTA%' or (pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ');

select * from pub_billtemplet_b where pk_billtemplet in (select pk_billtemplet from pub_billtemplet where bill_templetcaption like '%PTA%' or (pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ'))  ;

select * from pub_billtemplet_t  where pk_billtemplet in (select pk_billtemplet from pub_billtemplet where bill_templetcaption like '%PTA%' or (pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ'))  ;


select * from bd_billtype where pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ';

select * from pub_billaction where pk_billtype like 'HQ%' and pk_billtype <> 'HQ';

select * from pub_query_templet where model_code like 'HQ%' and model_code <> 'HQ';

select * from pub_query_condition where pk_templet in (select id from pub_query_templet where model_code like 'HQ%' and model_code <> 'HQ');


select * from sm_funcregister where fun_code like 'HQ%';

select * from pub_votable where pk_billtype like 'HQ%' and pk_billtype <> 'HQ';

select * from bd_busitype where businame like '%PTA%'

select * from pub_billbusiness where pk_businesstype = '1120AA1000000011FQIR';

select * from pub_billsource where referbusinesstype = '1120AA1000000011FQIR';

select * from pub_busiclass where pk_billtype like 'HQ%' and pk_billtype <> 'HQ';

select * from pub_billcode_rule where pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ';

select * from pub_systemplate where funnode like 'HQ%';

select * from vw_sale_contract_bs
select * from vw_sale_contract
select * from vw_ehpta_settlement













