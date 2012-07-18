package nc.ui.pf.changedir;

import nc.bs.pf.change.ConversionPolicyEnum;
import nc.ui.pf.afterclass.CHGHQ04TO30AFTER;
import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHGHQ04TOD2 extends VOConversionUI {
	
	/**
	 * 获得交换后处理类 全名称
	 * 
	 * @return java.lang.String
	 */
	public String getAfterClassName() {
		return null;
	}

	/**
	 * 获得交换后处理类 全名称
	 * 
	 * @return java.lang.String
	 */
	public String getOtherClassName() {
		return null;
	}

	/**
	 * 返回交换类型枚举ConversionPolicyEnum，默认为单据项目-单据项目
	 * 
	 * @return ConversionPolicyEnum
	 * @since 5.5
	 */
	public ConversionPolicyEnum getConversionPolicy() {
		return ConversionPolicyEnum.BILLITEM_BILLITEM;
	}

	/**
	 * 获得映射类型的交换规则
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getField() {
		return new String[] {
				"H_bbje->H_sumamount",
				"H_dr->H_dr",
				"H_dwbm->H_pk_corp",
				"H_lrr->H_coperatorid",
				// "H_notetype->H_notecode",
				"H_pj_jsfs->H_payway",
				"H_ts->H_ts",
				"H_ybje->H_sumamount",
				"B_bbye->B_amount",
				"B_bzbm->H_coin",
				// "B_cinventoryid->B_pk_invbasdoc",
				"H_zyx4->H_pk_contract",
				// "B_contractno->H_concode",
				"B_deptid->H_depart", "B_dfbbje->B_amount",
				"B_dfbbwsje->B_amount",
				"B_dffbje->B_amount",
				"B_dfybje->B_amount",
				"B_dfybwsje->B_amount",
				"B_dwbm->H_pk_corp",
				"B_fbye->B_amount",
				"B_memo->B_memo",
				// "B_notetype->H_notecode",
				"B_pj_jsfs->H_payway", "B_pk_corp->B_pk_corp",
				"B_qxrq->H_orderdate", "B_ts->B_ts", "B_ts->B_ts",
				"B_ybye->B_amount", "B_xyzh->H_pk_contract",
				"B_ddlx->H_pk_contract", "B_cksqsh->B_pk_contract_b",
				"B_ddhh->B_pk_contract_b", "H_bfyhzh->H_getbank",
				"B_bfyhzh->H_getbank", "B_ywybm->H_pk_psndoc" };
	}

	/**
	 * 获得赋值类型的交换规则
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getAssign() {
		return new String[] {

				"H_djdl->sk",
				// "H_djkjnd->SYSACCOUNTYEAR",
				"H_djlxbm->D2", "H_djzt->1", "H_hzbz->-1", "H_isjszxzf->N",
				"H_isnetready->N", "H_isreded->N", "H_lybz->0", "H_prepay->N",
				"H_pzglh->0", "H_qcbz->N", "H_sxbz->0",
				"H_ywbm->0001B8100000000B66PG",
				"H_xslxbm->0001A7100000001MGTYJ", "H_zzzt->0", "B_bbhl->1",
				"B_djdl->sk", "B_djxtflag->0", "B_flbh->0",
				"B_isSFKXYChanged->N", "B_isverifyfinished->N",
				"B_jsfsbm->XHHT", "B_kslb->1", "B_occupationmny->0",
				"B_old_sys_flag->N", "B_pausetransact->N",
				"B_pjdirection->none", "B_sfbz->3",
				"B_verifyfinisheddate->3000-01-01", "B_wldx->0", "B_xgbh->-1",
				"B_ph->XHHT", "B_jsfsbm->XHHT", "H_zyx5->0001ZZ100000001DMIR8" };
	}

	/**
	 * 获得公式类型的交换规则
	 * 
	 * @return java.lang.String[]
	 */
	public String[] getFormulas() {
		return new String[] {
				"H_djkjqj->mon();",
				"H_djkjnd->year();",
				"H_djrq->date();",
				"H_effectdate->date();",
				"H_hbbm->getColValue(bd_cumandoc,pk_cubasdoc, pk_cumandoc, H_purchcode);",
				"H_xslxmc->getColValue(bd_busitype,businame,pk_busitype,H_xslxbm)",
				"B_billdate->date();", "B_fx->-1",
				"B_hbbm->getColValue(bd_cumandoc,pk_cubasdoc, pk_cumandoc, H_purchcode);" };
	}

	/**
	 * 返回用户自定义函数
	 */
	public UserDefineFunction[] getUserDefineFunction() {
		return null;
	}
}
