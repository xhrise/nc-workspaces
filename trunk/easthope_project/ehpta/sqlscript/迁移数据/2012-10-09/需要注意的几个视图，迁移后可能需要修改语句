create or replace view vw_pta_sale_contract_balance as
select PK_CONTRACT,CSALEID,TYPE,TYPENAME,sum(MNY) mny , iscredit , pk_cumandoc , def6 from (

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny , def3 iscredit , pk_cumandoc , def6 from (select pk_contract , '' csaleid , type, decode(type , 1 , '货款' , 2 , '贴息额' , 3 , '挂结价差额' , 4 , '返利额' , 5 , '运补额' , 6 , '仓储费' , 7 , '装卸费' , '') typename
, mny , def3 , decode(type , 1 , pk_cubasdoc , null) pk_cumandoc , def6 from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and (nvl(def4 , 'N') = 'N' or type = 1) ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename , temp.def3 , temp.pk_cumandoc , temp.def6
-- 收款及其他金额项

union all

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny , def3 iscredit , pk_cumandoc , def6 from (select pk_contract , '' csaleid , type, decode(type , 1 , '货款' , 2 , '贴息额' , 3 , '挂结价差额' , 4 , '返利额' , 5 , '运补额' , 6 , '仓储费' , 7 , '装卸费' , '') typename
, (mny * -1) mny , def3 , null pk_cumandoc , def6 from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and nvl(def4 , 'N') = 'Y' and nvl(type,1) <> 1 ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename , temp.def3 , temp.pk_cumandoc , temp.def6

union all

select pk_contract , '' , '11' , '已提货金额' , nvl(sum(nheadsummny) * -1 , 0) , iscredit , ccustomerid pk_cumandoc , null from so_sale
 where pk_contract is not null and nvl(dr, 0) = 0 and (contracttype = 10 or contracttype = 20)
   and (FSTATUS = 2)
 group by pk_contract,iscredit , ccustomerid

) group by PK_CONTRACT,CSALEID,TYPE,TYPENAME,iscredit , pk_cumandoc , def6;