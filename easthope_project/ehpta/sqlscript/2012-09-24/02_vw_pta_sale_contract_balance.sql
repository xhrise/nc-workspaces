create or replace view vw_pta_sale_contract_balance as
select "PK_CONTRACT","CSALEID","TYPE","TYPENAME","MNY" from (

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny from (select pk_contract , '' csaleid , type, decode(type , 1 , '����' , 2 , '��Ϣ��' , 3 , '�ҽ�۲��' , 4 , '������' , 5 , '�˲���' , 6 , '�ִ���' , 7 , 'װж��' , '') typename
, mny   from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and (nvl(def4 , 'N') = 'N' or type = 1) ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename
-- �տ���������

union all

select temp.pk_contract , temp.csaleid , temp.type , temp.typename , sum(temp.mny) mny from (select pk_contract , '' csaleid , type, decode(type , 2 , '��ʹ����Ϣ��' , 3 , '��ʹ�ùҽ�۲��' , 4 , '��ʹ�÷�����' , 5 , '��ʹ���˲���' , 6 , '��ʹ�òִ���' , 7 , '��ʹ��װж��' , '') typename
, (mny * -1) mny  from ehpta_adjust where nvl(dr, 0) = 0 and vbillstatus = 1 and nvl(def4 , 'N') = 'Y' and nvl(type,1) <> 1 ) temp
group by temp.pk_contract , temp.csaleid , temp.type , temp.typename

union all

select pk_contract , csaleid , '11' , '��ʹ�û���' , nvl(sum(nheadsummny) * -1 , 0) from so_sale
 where pk_contract is not null and nvl(dr, 0) = 0 and (contracttype = 10 or contracttype = 20)
   and (FSTATUS = 2 or (FSTATUS = 1 and ISCREDIT = 'Y'))
 group by pk_contract, csaleid

);
