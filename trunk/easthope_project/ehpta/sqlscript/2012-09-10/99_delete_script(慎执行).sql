delete pub_billtemplet where pk_billtemplet in (

'0001AA1000000020IEJF' , '0001AA1000000020JRX8' , '0001AA1000000020KPVO' , '0001AA1000000020K0ES',
'0001AA1000000020DPK8' , '0001AA1000000020DZPI' , '0001A11000000020FIDI' , '0001AA1000000020GZM5',
'0001A11000000020H7BY' , '0001A21000000020HI51' , '0001AA1000000020K49P' , '1120ZZ1000000011IVY7'


)  or pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ' ;

delete pub_billtemplet_b where pk_billtemplet in (

select pk_billtemplet from pub_billtemplet where pk_billtemplet in (

'0001AA1000000020IEJF' , '0001AA1000000020JRX8' , '0001AA1000000020KPVO' , '0001AA1000000020K0ES',
'0001AA1000000020DPK8' , '0001AA1000000020DZPI' , '0001A11000000020FIDI' , '0001AA1000000020GZM5',
'0001A11000000020H7BY' , '0001A21000000020HI51' , '0001AA1000000020K49P' , '1120ZZ1000000011IVY7'


)  or pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ'

)  ;

delete pub_billtemplet_t  where pk_billtemplet in (

select pk_billtemplet from pub_billtemplet where pk_billtemplet in (

'0001AA1000000020IEJF' , '0001AA1000000020JRX8' , '0001AA1000000020KPVO' , '0001AA1000000020K0ES',
'0001AA1000000020DPK8' , '0001AA1000000020DZPI' , '0001A11000000020FIDI' , '0001AA1000000020GZM5',
'0001A11000000020H7BY' , '0001A21000000020HI51' , '0001AA1000000020K49P' , '1120ZZ1000000011IVY7'


)  or pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ'

) ;



delete bd_billtype where pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ'

delete pub_billaction where pk_billtype like 'HQ%' and pk_billtype <> 'HQ';



delete pub_query_templet where model_code like 'HQ%' and model_code <> 'HQ';

delete pub_query_condition where pk_templet in (select id from pub_query_templet where model_code like 'HQ%' and model_code <> 'HQ');


delete sm_funcregister where fun_code like 'HQ%';

delete pub_votable where pk_billtype like 'HQ%' and pk_billtype <> 'HQ';

delete pub_billbusiness where pk_businesstype = '1120AA1000000011FQIR';

delete pub_busiclass where pk_billtype like 'HQ%' and pk_billtype <> 'HQ';

delete pub_billcode_rule where pk_billtypecode like 'HQ%' and pk_billtypecode <> 'HQ';

delete pub_systemplate where funnode like 'HQ%';
