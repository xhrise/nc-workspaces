package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

/**
 * 用于30TO4C的VO的动态转换类。
 * 
 * 创建日期：(2004-11-18)
 * 
 * @author：平台脚本生成
 */
public class CHG30TO4C extends nc.ui.pf.change.VOConversionUI {
	/**
	 * CHG30TO4C 构造子注解。
	 */
	public CHG30TO4C() {
		super();
	}

	/**
	 * 获得后续类的全录经名称。
	 * 
	 * @return java.lang.String[]
	 */
	public String getAfterClassName() {
		return "nc.ui.ic.pub.pfconv.HardLockChgVO";
	}

	/**
	 * 获得另一个后续类的全录径名称。
	 * 
	 * @return java.lang.String[]
	 */
	public String getOtherClassName() {
		return "nc.bs.ic.pub.pfconv.HardLockChgVO";
	}

	/**
	 * 获得字段对应。
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getField() {
		return new String[] {
				"H_ccustomerid->H_ccustomerid",
				// "H_pk_cubasdocC->H_ccustbasid",
				"H_cdptid->H_cdeptid", "H_pk_corp->B_pk_corp",
				"B_pk_corp->B_pk_corp", "H_vuserdef9->H_vdef9",
				"H_vuserdef8->H_vdef8", "H_vuserdef7->H_vdef7",
				"H_vuserdef6->H_vdef6", "H_vuserdef5->H_vdef5",
				"H_vuserdef4->H_vdef4", "H_vuserdef3->H_vdef3",
				"H_vuserdef2->H_vdef2", "H_vuserdef1->H_vdef1",
				"H_vuserdef10->H_vdef10", "H_vuserdef11->H_vdef11",
				"H_vuserdef12->H_vdef12", "H_vuserdef13->H_vdef13",
				"H_vuserdef14->H_vdef14", "H_vuserdef15->H_vdef15",
				"H_vuserdef16->H_vdef16", "H_vuserdef17->H_vdef17",
				"H_vuserdef18->H_vdef18", "H_vuserdef19->H_vdef19",
				"H_vuserdef20->H_vdef20", "H_cbiztype->H_cbiztype",
				"H_coperatorid->SYSOPERATOR", "H_dbilldate->SYSDATE",
				"H_coperatoridnow->SYSOPERATOR", "H_cbizid->H_cemployeeid",
				"H_vdiliveraddress->H_vreceiveaddress",
				"B_vdiliveraddress->B_vreceiveaddress", "H_ts->H_ts",
				"H_cdilivertypeid->H_ctransmodeid", "H_vnote->H_vnote",
				"H_cwhsmanagerid->B_cstoreadmin",
				"H_freplenishflag->H_bretinvflag",

				"B_breturnprofit->B_breturnprofit",
				"B_bsafeprice->B_bsafeprice", "B_castunitid->B_cpackunitid",
				"B_cfirstbillbid->B_corder_bid", "B_cfirstbillhid->B_csaleid",
				"B_cfirsttype->H_creceipttype", "B_cfreezeid->B_cfreezeid",
				"B_cinvbasid->B_cinvbasdocid",
				"B_cinventoryid->B_cinventoryid",
				"B_cinvmanid->B_cinvbasdocid", "B_cprojectid->B_cprojectid",
				"B_cprojectphaseid->B_cprojectphaseid",
				"B_creceieveid->B_creceiptcorpid",
				"B_csourcebillbid->B_corder_bid",
				"B_csourcebillhid->B_csaleid", "B_csourcebodyts->B_ts",
				"B_csourceheadts->H_ts", "B_flargess->B_blargessflag",
				"B_nsaleprice->B_nnetprice", "B_ntaxprice->B_ntaxnetprice",
				"B_vbatchcode->B_cbatchid", "B_vnotebody->B_frownote",
				"B_vsourcebillcode->H_vreceiptcode",
				"B_vsourcerowno->B_crowno",

				"B_cquoteunitid->B_cquoteunitid",
				"B_nquoteunitrate->B_nqtscalefactor",

				"B_vfree1->B_vfree1", "B_vfree2->B_vfree2",
				"B_vfree3->B_vfree3", "B_vfree4->B_vfree4",
				"B_vfree5->B_vfree5", "B_vuserdef1->B_vdef1",
				"B_vuserdef10->B_vdef10", "B_vuserdef11->B_vdef11",
				"B_vuserdef12->B_vdef12", "B_vuserdef13->B_vdef13",
				"B_vuserdef14->B_vdef14", "B_vuserdef15->B_vdef15",
				"B_vuserdef16->B_vdef16", "B_vuserdef17->B_vdef17",
				"B_vuserdef18->B_vdef18", "B_vuserdef19->B_vdef19",
				"B_vuserdef2->B_vdef2", "B_vuserdef20->B_vdef20",
				"B_vuserdef3->B_vdef3", "B_vuserdef4->B_vdef4",
				"B_vuserdef5->B_vdef5", "B_vuserdef6->B_vdef6",
				"B_vuserdef7->B_vdef7", "B_vuserdef8->B_vdef8",
				"B_vuserdef9->B_vdef9",

				"H_pk_defdoc1->H_pk_defdoc1", "H_pk_defdoc2->H_pk_defdoc2",
				"H_pk_defdoc3->H_pk_defdoc3", "H_pk_defdoc4->H_pk_defdoc4",
				"H_pk_defdoc5->H_pk_defdoc5", "H_pk_defdoc6->H_pk_defdoc6",
				"H_pk_defdoc7->H_pk_defdoc7", "H_pk_defdoc8->H_pk_defdoc8",
				"H_pk_defdoc9->H_pk_defdoc9", "H_pk_defdoc10->H_pk_defdoc10",
				"H_pk_defdoc11->H_pk_defdoc11", "H_pk_defdoc12->H_pk_defdoc12",
				"H_pk_defdoc13->H_pk_defdoc13", "H_pk_defdoc14->H_pk_defdoc14",
				"H_pk_defdoc15->H_pk_defdoc15", "H_pk_defdoc16->H_pk_defdoc16",
				"H_pk_defdoc17->H_pk_defdoc17", "H_pk_defdoc18->H_pk_defdoc18",
				"H_pk_defdoc19->H_pk_defdoc19", "H_pk_defdoc20->H_pk_defdoc20",

				"B_pk_defdoc1->B_pk_defdoc1", "B_pk_defdoc2->B_pk_defdoc2",
				"B_pk_defdoc3->B_pk_defdoc3", "B_pk_defdoc4->B_pk_defdoc4",
				"B_pk_defdoc5->B_pk_defdoc5", "B_pk_defdoc6->B_pk_defdoc6",
				"B_pk_defdoc7->B_pk_defdoc7", "B_pk_defdoc8->B_pk_defdoc8",
				"B_pk_defdoc9->B_pk_defdoc9", "B_pk_defdoc10->B_pk_defdoc10",
				"B_pk_defdoc11->B_pk_defdoc11", "B_pk_defdoc12->B_pk_defdoc12",
				"B_pk_defdoc13->B_pk_defdoc13", "B_pk_defdoc14->B_pk_defdoc14",
				"B_pk_defdoc15->B_pk_defdoc15", "B_pk_defdoc16->B_pk_defdoc16",
				"B_pk_defdoc17->B_pk_defdoc17", "B_pk_defdoc18->B_pk_defdoc18",
				"B_pk_defdoc19->B_pk_defdoc19", "B_pk_defdoc20->B_pk_defdoc20",

				"B_cquotecurrency->B_ccurrencytypeid",
				"B_nquoteprice->B_noriginalcurtaxnetprice",
				"B_nquotentprice->B_noriginalcurnetprice",

				"B_ddeliverdate->B_ddeliverdate",
				"B_creceiveareaid->B_creceiptareaid",
				"B_vreceiveaddress->B_vreceiveaddress",
				"B_creceivepointid->B_crecaddrnode",

				// 表头、表体仓库
				"H_cwarehouseid->H_storage", "B_cwarehouseid->H_storage",

				"H_pk_transport->H_pk_transport", "H_concode->H_concode",
				"H_salecode->H_vreceiptcode", "H_pk_contract->H_pk_contract",
				"H_contracttype->H_contracttype" , 
				"H_version->H_version",
				
				// 2012-10-27添加
				"H_estoraddrid->H_estoraddrid",
				"B_transprice->B_transprice",
				"B_storprice->B_storprice",
				"B_ctransmodeid->B_ctransmodeid",
				"B_copermodeid->B_copermodeid",
				"B_pk_transport_b->B_pk_transport_b" , 
				"B_pk_storcont_b->B_pk_storcont_b" , 
		};
	}

	/**
	 * 获得公式。
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getFormulas() {
		return new String[] {
				"H_pk_calbody->iif(B_creccalbodyid =NULL,B_cadvisecalbodyid,B_creccalbodyid)",
				"B_pk_calbody->iif(B_creccalbodyid =NULL,B_cadvisecalbodyid,B_creccalbodyid)",
				// "H_cwarehouseid->iif(B_creccalbodyid =NULL,B_cbodywarehouseid ,B_crecwareid)",
				// "B_cwarehouseid->iif(B_creccalbodyid =NULL,B_cbodywarehouseid ,B_crecwareid)",
				"H_pk_cubasdocC->getColValue(bd_cumandoc,pk_cubasdoc,pk_cumandoc,H_ccustomerid)",
				"H_cbilltypecode->\"4C\"",
				"B_nshouldoutnum->B_nnumber   -   B_ntotalinventorynumber - iif(B_ntotalshouldoutnum==null,0,B_ntotalshouldoutnum) + iif(B_ntranslossnum==null,0,B_ntranslossnum) ",
				"B_hsl->iif(B_scalefactor==null,B_nnumber/B_npacknumber,B_scalefactor)",
				"B_csourcetype->\"30\"",
				"B_nshouldoutassistnum->(  B_nnumber   -   B_ntotalinventorynumber    - iif(B_ntotalshouldoutnum==null,0,B_ntotalshouldoutnum) + iif(B_ntranslossnum==null,0,B_ntranslossnum)  )   *  ( iif(B_npacknumber==null,0,B_npacknumber) /   B_nnumber   )",
				"H_ctrancustid->getColValue(ehpta_transport_contract , pk_carrier , pk_transport , H_pk_transport)",
				"H_contracttype->tostring(H_contracttype)", 
				
		};
	}

	/**
	 * 返回用户自定义函数。
	 */
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
