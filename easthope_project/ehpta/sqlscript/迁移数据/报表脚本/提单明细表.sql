select bd_cubasdoc.custname , so_sale.vreceiptcode , ehpta_sale_contract.connamed ,
so_sale.dbilldate , bd_invbasdoc.invname , bd_invbasdoc.invspec , so_saleorder_b.nnumber , 
so_saleorder_b.ntaxprice , so_saleorder_b.nsummny --ic_general_h.vbillcode , ic_general_b.vbatchcode , ic_general_b.dbizdate ,
-- ic_general_b.noutnum , (ic_general_b.noutnum * so_saleorder_b.ntaxprice) taxmny
from so_sale 
left join so_saleorder_b on so_saleorder_b.csaleid = so_sale.csaleid
left join ehpta_sale_contract on ehpta_sale_contract.pk_contract = so_sale.pk_contract
--left join ic_general_b on ic_general_b.csourcebillbid = so_saleorder_b.corder_bid
--left join ic_general_h on ic_general_h.cgeneralhid = ic_general_b.cgeneralhid
left join bd_invbasdoc on bd_invbasdoc.pk_invbasdoc = so_saleorder_b.cinvbasdocid
left join bd_cumandoc on bd_cumandoc.pk_cumandoc = so_sale.creceiptcorpid
left join bd_cubasdoc on bd_cubasdoc.pk_cubasdoc = bd_cumandoc.pk_cubasdoc
where so_sale.pk_contract is not null and (so_sale.contracttype = '10' or so_sale.contracttype = '20')
and nvl(so_sale.dr , 0) = 0 and nvl(so_saleorder_b.dr , 0) = 0 and nvl(ehpta_sale_contract.dr , 0) = 0
--and nvl(ic_general_h.dr , 0) = 0 and nvl(ic_general_b.dr , 0) = 0 
and ehpta_sale_contract.vbillstatus = 1


-- 客户 、 合同  、 提单日期 、 存货 、 数量 、 单价 、 出库单 、 出库时间 、 出库数量 、 出库金额 、
