create or replace view vw_pta_sale_contract_balance as
select "PK_CONTRACT","CSALEID","TYPE","TYPENAME","MNY" from (

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny from (select pk_contract , '' csaleid , type, decode(type , 1 , '货款' , 2 , '贴息额' , 3 , '挂结价差额' , 4 , '返利额' , 5 , '运补额' , 6 , '仓储费' , 7 , '装卸费' , '') typename
, mny   from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and (nvl(def4 , 'N') = 'N' or type = 1) ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename
-- 收款及其他金额项

union all

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny from (select pk_contract , '' csaleid , type, decode(type , 2 , '已使用贴息额' , 3 , '已使用挂结价差额' , 4 , '已使用返利额' , 5 , '已使用运补额' , 6 , '已使用仓储费' , 7 , '已使用装卸费' , '') typename
, (mny * -1) mny  from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and nvl(def4 , 'N') = 'Y' and nvl(type,1) <> 1 ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename

union all

select pk_contract , csaleid , '11' , '已使用货款' , nvl(sum(nheadsummny) * -1 , 0) from so_sale
 where pk_contract is not null and nvl(dr, 0) = 0 and (contracttype = 10 or contracttype = 20)
   and (FSTATUS = 2 or (FSTATUS = 1 and ISCREDIT = 'Y'))
 group by pk_contract, csaleid

);
