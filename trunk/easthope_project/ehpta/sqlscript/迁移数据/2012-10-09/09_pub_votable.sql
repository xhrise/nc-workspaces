prompt Importing table pub_votable...
set feedback off
set define off
insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_storagedoc', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010201.StorContractVO', null, 'voperatorid', 'HQ01', null, '0001AA1000000020DTIY', 'pk_storagedoc', '2012-07-02 17:29:40', 'ehpta_storcontract');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010201.StorcontractBVO', null, null, 'HQ01', null, '0001AA1000000020DTIZ', 'pk_storagedoc', '2012-07-02 12:34:35', 'ehpta_storcontract_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_transport', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010101.TransportContractVO', null, 'voperatorid', 'HQ02', null, '0001AA1000000020KEFP', 'pk_transport', '2012-07-23 22:25:09', 'ehpta_transport_contract');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010101.TransportContractBVO', null, null, 'HQ02', null, '0001AA1000000020KEFQ', 'pk_transport', '2012-08-23 14:14:40', 'ehpta_transport_contract_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_maintain', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010301.MaintainVO', null, 'voperatorid', 'HQ03', null, '0001AA1000000020L0R9', 'pk_maintain', '2012-08-02 09:44:44', 'ehpta_maintain');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_contract', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010401.SaleContractVO', null, 'voperatorid', 'HQ04', null, '1120AA1000000011EA45', 'pk_contract', '2012-07-09 14:43:56', 'ehpta_sale_contract');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010401.SaleContractBsVO', null, null, 'HQ04', null, '1120AA1000000011EA46', 'pk_contract', '2012-07-10 20:40:06', 'ehpta_sale_contract_bs');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_storfare', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010501.StorfareVO', null, 'voperatorid', 'HQ05', null, '1120A11000000011EEPO', 'pk_storfare', '2012-07-09 14:53:08', 'ehpta_storfare');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010501.EhptaStorfareBVO', null, null, 'HQ05', null, '1120A11000000011EEPP', 'pk_storfare', '2012-07-09 14:53:08', 'ehpta_storfare_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_contract', 'vbillno', 'nc.vo.ehpta.hq010402.MultiBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010401.SaleContractVO', null, 'voperatorid', 'HQ06', null, '0001A21000000020HIBX', 'pk_contract', '2012-07-10 16:50:37', 'ehpta_sale_contract');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.ehpta.hq010402.MultiBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010402.SaleContractBVO', null, null, 'HQ06', null, '0001A21000000020HIBY', 'pk_contract', '2012-07-10 20:39:49', 'ehpta_sale_contract_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.ehpta.hq010402.MultiBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010402.EhptaAidcustVO', null, null, 'HQ06', null, '0001A21000000020HIBZ', 'pk_contract', '2012-07-10 16:50:37', 'ehpta_aidcust');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.ehpta.hq010402.MultiBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010402.EhptaPrepolicyVO', null, null, 'HQ06', null, '0001A21000000020HIC0', 'pk_contract', '2012-07-10 16:50:37', 'ehpta_prepolicy');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_adjust', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010403.AdjustVO', null, 'voperatorid', 'HQ07', null, '0001AA1000000020K4D6', 'pk_adjust', '2012-07-22 20:47:57', 'ehpta_adjust');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_transport', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010101.TransportContractVO', null, 'voperatorid', 'HQ08', null, '0001AA1000000020MY4C', 'pk_transport', '2012-08-23 14:15:37', 'ehpta_transport_contract');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010101.TransportContractBVO', null, null, 'HQ08', null, '0001AA1000000020MY4D', 'pk_transport', '2012-08-23 14:15:57', 'ehpta_transport_contract_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_calcinterest', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010901.CalcInterestVO', null, 'voperatorid', 'HQ09', null, '0001AA1000000020MQJ6', 'pk_calcinterest', '2012-08-20 10:36:11', 'ehpta_calc_interest');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010901.EhptaCalcInterestBVO', null, null, 'HQ09', null, '0001AA1000000020MQJ7', 'pk_calcinterest', '2012-08-20 10:36:11', 'ehpta_calc_interest_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_settlement', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010910.CalcSettlementVO', null, 'voperatorid', 'HQ10', null, '0001AA1000000020N24O', 'pk_settlement', '2012-08-27 11:49:19', 'ehpta_calc_settlement');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_transfee', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010920.CalcUpperTransfeeHVO', null, 'voperatorid', 'HQ11', null, '0001AA1000000020N89R', 'pk_transfee', '2012-09-11 11:54:21', 'ehpta_calc_upper_transfee_h');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010920.EhptaCalcUpperTransfeeBVO', null, null, 'HQ11', null, '0001AA1000000020N89S', 'pk_transfee', '2012-09-11 11:54:21', 'ehpta_calc_upper_transfee_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_transfee', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010930.CalcUnderTransfeeHVO', null, 'voperatorid', 'HQ12', null, '0001AA1000000020NGCX', 'pk_transfee', '2012-09-13 10:58:49', 'ehpta_calc_under_transfee_h');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010930.CalcUnderTransfeeBVO', null, null, 'HQ12', null, '0001AA1000000020NGCY', 'pk_transfee', '2012-09-13 10:58:52', 'ehpta_calc_under_transfee_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_storfee', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010940.CalcStorfeeHVO', null, 'voperatorid', 'HQ13', null, '0001AA1000000020NMXR', 'pk_storfee', '2012-09-17 14:48:22', 'ehpta_calc_storfee_h');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010940.CalcStorfeeBVO', null, null, 'HQ13', null, '0001AA1000000020NMXS', 'pk_storfee', '2012-09-17 14:48:30', 'ehpta_calc_storfee_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_rebates', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010950.CalcRebatesHVO', null, 'voperatorid', 'HQ14', null, '0001AA1000000020O6Z8', 'pk_rebates', '2012-09-26 14:41:52', 'ehpta_calc_rebates_h');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010950.EhptaCalcRebatesBVO', null, null, 'HQ14', null, '0001AA1000000020O6Z9', 'pk_rebates', '2012-09-26 14:41:52', 'ehpta_calc_rebates_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_sale_balance', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq0305.SaleBalanceHVO', null, 'voperatorid', 'HQ16', null, '0001AA1000000028JZIK', 'pk_sale_balance', '2012-11-13 17:11:49', 'ehpta_calc_sale_balance_h');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq0305.SaleBalanceBVO', null, null, 'HQ16', null, '0001AA1000000028JZIL', 'pk_sale_balance', '2012-11-14 13:26:54', 'ehpta_calc_sale_balance_b');

prompt Done.
