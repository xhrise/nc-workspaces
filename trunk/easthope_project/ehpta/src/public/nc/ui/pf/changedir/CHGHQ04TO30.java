package nc.ui.pf.changedir;

import nc.ui.pf.afterclass.SaleContractAfterCHG;
import nc.ui.pf.change.VOConversionUI;
import nc.vo.pf.change.UserDefineFunction;

public class CHGHQ04TO30 extends VOConversionUI {
	
	public String getAfterClassName() {
		
	    return SaleContractAfterCHG.class.getName();
	  }

	  public String getOtherClassName() {
	    return SaleContractAfterCHG.class.getName();
	  }

	  //　字段转换
	  public String[] getField() {
		  // 前 H表 = so_sale  B表 = so_saleorder_b
	    return new String[] { 
		      "H_cbiztype->H_pk_busitype",   // 业务类型
		      "H_ccurrencytypeid->00010000000000000001",  // 币种   默认：人民币
		      "H_ccustomerid->H_purchcode", // 客商主键
		      "H_creceiptcustomerid->H_purchcode", // 客商主键
		      "H_cdeptid->H_pk_deptdoc",  // 销售部门
		      "H_cemployeeid->H_pk_psndoc", // 业务员
	 	      "H_coperatorid->H_voperatorid",  //　操作人
		      "H_creceiptcorpid->H_purchcode",  // 开票单位  （ 客户开票？）
		      "H_creceipttype->30", // 单据类型  销售订单 = 30
		      "H_pk_corp->H_pk_corp",  // 公司
		      "H_ts->H_ts",  //　时间戳
		      "H_pk_contract->H_pk_contract",  // 合同主键 （EH已添加字段）
		      "H_sendcompany->H_bargainor", // 卖方 （EH添加）
		      "H_concode->H_vbillno",  // 合同编码 （EH添加）
		      "H_nheadsummny->H_contprice", // 整单金额合计
		      "B_cinvbasdocid->B_pk_invbasdoc",  //　存货档案主键（表体 - so_saleorder_b）
		      "B_cinventoryid->B_pk_invbasdoc", //　存货档案主键（表体 - so_saleorder_b）
		      "B_crowno->B_def1",   //  行号（表体 - so_saleorder_b） PTA合同中重新添加  ， 增长为 10 -> 20 -> 30
		      "B_nnumber->B_num",  // 数量 
		      "B_numof->B_numof", // 件数
		      "B_noriginalcurmny->B_notaxloan",  // 原币无税金额
		      "B_noriginalcursummny->B_sumpricetax",  // 原币价税合计
		      "B_noriginalcurtaxmny->B_tax",  // 原币税额
		      "B_noriginalcurtaxnetprice->B_taxprice",  // 原币含税净价 
		      "B_noriginalcurtaxprice->B_taxprice",  // 原币含税单价
		      "B_nsummny->B_sumpricetax", // 本币价税合计
		      "B_nquoteunitnum->B_num", // 报价单位数量
		      
		      "B_norgqttaxnetprc->B_taxprice", // 报价单位含税净价  
		      "B_norgqttaxprc->B_taxprice", // 报价单位含税单价
		      "B_nqttaxnetprc->B_taxprice", // 报价单位本币含税净价 
		      "B_nqttaxprc->B_taxprice",  // 报价单位本币含税单价 


		      
		      "B_ntaxnetprice->B_taxprice",  // 原币含税净价 
		      "B_ntaxprice->B_taxprice",  // 原币含税单价
		      "B_ntaxrate->B_taxrate",  // 税率
		      "B_pk_corp->H_pk_corp",  // 公司主键
		      "B_ts->B_ts",  // 时间戳
		      "B_csourcebillbodyid->B_pk_contract_b", // 来源单据附表ID ， 合同子表主键
		      "B_csourcebillid->H_pk_contract"  // 来源单据主表ID , 合同主表主键
		      
	      };
	  }
	  
	  // 加载时直接赋值
	  public String[] getAssign() {
		// 来源单据类型 , PTA现货合同 = HQ04
	    return new String[] { 
	    		"H_editionnum->1.0",
	    		"H_contracttype->10", // 合同类型 , 设置现货合同为 10
	    		"H_ccurrencytypeid->00010000000000000001",
	    		"H_nexchangeotobrate->1",
	    		"H_ndiscountrate->100",
	    		"H_ccalbodyid->1120A8100000000YSLF2",
	    		"H_csalecorpid->0001A8100000000044PP",
	    		"B_creceipttype->\"HQ04\"" , 
	    		"B_ndiscountmny->0" , 
	    		"B_ndiscountrate->100" , 
	    		"B_nitemdiscountrate->100" , 
	    		"B_noriginalcurdiscountmny->0",
	    		"B_ccurrencytypeid->00010000000000000001",   //　原币   币种 （表体 - so_saleorder_b） 默认：人民币
	    		"B_nexchangeotobrate->1",
	    		"B_cadvisecalbodyid->1120A8100000000YSLF2",
	    		
	    }; 
	  }

	 // 加载是设置的公式
	  public String[] getFormulas() {
	    return new String[] { 
	    		"H_dbilldate->date();", 
	    		"H_dmakedate->date();" ,
	    		"B_noriginalcurnetprice->B_taxprice / (1 + B_taxrate / 100)",  // 原币无税净价 （净价 =？ 单价）
			    "B_noriginalcurprice->B_taxprice / (1 + B_taxrate / 100)",  // 原币无税单价
			    "B_nnetprice->B_taxprice / (1 + B_taxrate / 100)",  // 原币无税净价 （净价 =？ 单价）
			    "B_nprice->B_taxprice / (1 + B_taxrate / 100)",  // 原币无税单价
			    "B_norgqtnetprc->B_taxprice / (1 + B_taxrate / 100)", // 报价单位无税净价  
			    "B_norgqtprc->B_taxprice / (1 + B_taxrate / 100)", //  报价单位无税单价  
			    "B_nqtprc->B_taxprice / (1 + B_taxrate / 100)",  // 报价单位本币无税单价 
			    "B_nqtnetprc->B_taxprice / (1 + B_taxrate / 100)", // 报价单位本币无税净价 
			    "B_creceipttype->\"HQ04\"" , 
	    }; 
	  }

	  public UserDefineFunction[] getUserDefineFunction() {
	    return null;
	  }
}
