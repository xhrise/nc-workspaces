delete pub_votable where pk_billtype like 'HQ0%' ;

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_storagedoc', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010201.StorContractVO', null, 'voperatorid', 'HQ01', null, '0001AA1000000020DTIY', 'pk_storagedoc', '2012-07-02 17:29:40', 'ehpta_storcontract');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010201.StorcontractBVO', null, null, 'HQ01', null, '0001AA1000000020DTIZ', 'pk_storagedoc', '2012-07-02 12:34:35', 'ehpta_storcontract_b');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values ('vapproveid', 'pk_transport', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010101.TransportContractVO', null, 'voperatorid', 'HQ02', null, '0001AA1000000020KEFP', 'pk_transport', '2012-07-23 22:25:09', 'ehpta_transport_contract');

insert into pub_votable (APPROVEID, BILLID, BILLNO, BILLVO, BUSITYPE, DEF1, DEF2, DEF3, DR, HEADBODYFLAG, HEADITEMVO, ITEMCODE, OPERATOR, PK_BILLTYPE, PK_CORP, PK_VOTABLE, PKFIELD, TS, VOTABLE)
values (null, null, null, 'nc.vo.trade.pub.HYBillVO', null, null, null, null, null, 'N', 'nc.vo.ehpta.hq010101.EhptaTransportContractBVO', null, null, 'HQ02', null, '0001AA1000000020KEFQ', 'pk_transport', '2012-07-23 22:25:09', 'ehpta_transport_contract_b');

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
values ('vapproveid', 'pk_calcinterest', 'vbillno', 'nc.vo.trade.pub.HYBillVO', 'pk_busitype', null, null, null, null, 'Y', 'nc.vo.ehpta.hq010901.CalcInterestVO', null, 'voperatorid', 'HQ09', null, '0001AA1000000020M9HQ', 'pk_calcinterest', '2012-08-14 11:12:08', 'ehpta_calc_interest');
