create or replace view vw_sale_contract_balance as
select pk_contract , '' csaleid , type, case when type = 1 then '����' when type = 2 then '��Ϣ��'  when type = 3 then '�ҽ�۲��' when type = 4 then  '������'  when type = 5 then  '�˲���'  end as typename
, mny   from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1
-- �տ���������
union all

select pk_contract , csaleid , '11' , '��ʹ�û���' , nvl(sum(nheadsummny), 0) from so_sale
 where pk_contract is not null and nvl(dr, 0) = 0 and (contracttype = 10 or contracttype = 20)
   and (FSTATUS = 2 or (FSTATUS = 1 and ISCREDIT = 'Y'))
 group by pk_contract, csaleid;