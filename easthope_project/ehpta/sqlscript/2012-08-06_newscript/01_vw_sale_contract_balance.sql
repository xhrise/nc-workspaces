create or replace view vw_sale_contract_balance as
select pk_contract , '' csaleid , type, case when type = 1 then '货款' when type = 2 then '贴息额'  when type = 3 then '挂结价差额' when type = 4 then  '返利额'  when type = 5 then  '运补额'  end as typename
, mny   from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1
-- 收款及其他金额项
union all

select pk_contract , csaleid , '11' , '已使用货款' , nvl(sum(nheadsummny), 0) from so_sale
 where pk_contract is not null and nvl(dr, 0) = 0 and (contracttype = 10 or contracttype = 20)
   and (FSTATUS = 2 or (FSTATUS = 1 and ISCREDIT = 'Y'))
 group by pk_contract, csaleid;