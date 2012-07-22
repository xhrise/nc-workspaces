package nc.vo.yto.business;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class CorpVO extends SuperVO{
	
	private UFDate begindate;
	private String briefintro;
	private String chargedeptcode;
	private String chargedeptname;
	private String citycounty;
	private String corptype;
	private String countryarea;
	private UFDate createdate;
	private String def1;
	private String def10;
	private String def11;
	private String def12;
	private String def13;
	private String def14;
	private String def15;
	private String def16;
	private String def17;
	private String def18;
	private String def19;
	private String def2;
	private String def20;
	private String def3;
	private String def4;
	private String def5;
	private String def6;
	private String def7;
	private String def8;
	private String def9;
	private Integer dr;
	private String ecotype;
	private String email1;
	private String email2;
	private String email3;
	private String enddate;
	private String fathercorp;
	private String fax1;
	private String fax2;
	private String foreignname;
	private UFBoolean holdflag;
	private String idnumber;
	private String industry;
	private String innercode;
	private UFBoolean ishasaccount;
	private UFBoolean isseal;
	private UFBoolean isuseretail;
	private UFBoolean isworkingunit;
	private String legalbodycode;
	private String linkman1;
	private String linkman2;
	private String linkman3;
	private String maxinnercode;
	private String memo;
	private UFDouble ownersharerate;
	private String phone1;
	private String phone2;
	private String phone3;
	private String pk_corp;
	private String pk_corpkind;
	private String pk_currency;
	private String postaddr;
	private String province;
	private UFDouble regcapital;
	private String region;
	private String saleaddr;
	private UFDate sealeddate;
	private Integer showorder;
	private String taxcode;
	private Integer taxpayertype;
	private UFDateTime ts;
	private String unitcode;
	private String unitdistinction;
	private String unitname;
	private String unitshortname;
	private String url;
	private String zipcode;
	
	public static final String BEGINDATE = "begindate";
	public static final String BRIEFINTRO = "briefintro";
	public static final String CHARGEDEPTCODE = "chargedeptcode";
	public static final String CHARGEDEPTNAME = "chargedeptname";
	public static final String CITYCOUNTY = "citycounty";
	public static final String CORPTYPE = "corptype";
	public static final String COUNTRYAREA = "countryarea";
	public static final String CREATEDATE = "createdate";
	public static final String DEF1 = "def1";
	public static final String DEF10 = "def10";
	public static final String DEF11 = "def11";
	public static final String DEF12 = "def12";
	public static final String DEF13 = "def13";
	public static final String DEF14 = "def14";
	public static final String DEF15 = "def15";
	public static final String DEF16 = "def16";
	public static final String DEF17 = "def17";
	public static final String DEF18 = "def18";
	public static final String DEF19 = "def19";
	public static final String DEF2 = "def2";
	public static final String DEF20 = "def20";
	public static final String DEF3 = "def3";
	public static final String DEF4 = "def4";
	public static final String DEF5 = "def5";
	public static final String DEF6 = "def6";
	public static final String DEF7 = "def7";
	public static final String DEF8 = "def8";
	public static final String DEF9 = "def9";
	public static final String DR = "dr";
	public static final String ECOTYPE = "ecotype";
	public static final String EMAIL1 = "email1";
	public static final String EMAIL2 = "email2";
	public static final String EMAIL3 = "email3";
	public static final String ENDDATE = "enddate";
	public static final String FATHERCORP = "fathercorp";
	public static final String FAX1 = "fax1";
	public static final String FAX2 = "fax2";
	public static final String FOREIGNNAME = "foreignname";
	public static final String HOLDFLAG = "holdflag";
	public static final String IDNUMBER = "idnumber";
	public static final String INDUSTRY = "industry";
	public static final String INNERCODE = "innercode";
	public static final String ISHASACCOUNT = "ishasaccount";
	public static final String ISSEAL = "isseal";
	public static final String ISUSERETAIL = "isuseretail";
	public static final String ISWORKINGUNIT = "isworkingunit";
	public static final String LEGALBODYCODE = "legalbodycode";
	public static final String LINKMAN1 = "linkman1";
	public static final String LINKMAN2 = "linkman2";
	public static final String LINKMAN3 = "linkman3";
	public static final String MAXINNERCODE = "maxinnercode";
	public static final String MEMO = "memo";
	public static final String OWNERSHARERATE = "ownersharerate";
	public static final String PHONE1 = "phone1";
	public static final String PHONE2 = "phone2";
	public static final String PHONE3 = "phone3";
	public static final String PK_CORP = "pk_corp";
	public static final String PK_CORPKIND = "pk_corpkind";
	public static final String PK_CURRENCY = "pk_currency";
	public static final String POSTADDR = "postaddr";
	public static final String PROVINCE = "province";
	public static final String REGCAPITAL = "regcapital";
	public static final String REGION = "region";
	public static final String SALEADDR = "saleaddr";
	public static final String SEALEDDATE = "sealeddate";
	public static final String SHOWORDER = "showorder";
	public static final String TAXCODE = "taxcode";
	public static final String TAXPAYERTYPE = "taxpayertype";
	public static final String TS = "ts";
	public static final String UNITCODE = "unitcode";
	public static final String UNITDISTINCTION = "unitdistinction";
	public static final String UNITNAME = "unitname";
	public static final String UNITSHORTNAME = "unitshortname";
	public static final String URL = "url";
	public static final String ZIPCODE = "zipcode";

	public UFDate getBegindate() {
		return begindate;
	}

	public void setBegindate(UFDate begindate) {
		this.begindate = begindate;
	}

	public String getBriefintro() {
		return briefintro;
	}

	public void setBriefintro(String briefintro) {
		this.briefintro = briefintro;
	}

	public String getChargedeptcode() {
		return chargedeptcode;
	}

	public void setChargedeptcode(String chargedeptcode) {
		this.chargedeptcode = chargedeptcode;
	}

	public String getChargedeptname() {
		return chargedeptname;
	}

	public void setChargedeptname(String chargedeptname) {
		this.chargedeptname = chargedeptname;
	}

	public String getCitycounty() {
		return citycounty;
	}

	public void setCitycounty(String citycounty) {
		this.citycounty = citycounty;
	}

	public String getCorptype() {
		return corptype;
	}

	public void setCorptype(String corptype) {
		this.corptype = corptype;
	}

	public String getCountryarea() {
		return countryarea;
	}

	public void setCountryarea(String countryarea) {
		this.countryarea = countryarea;
	}

	public UFDate getCreatedate() {
		return createdate;
	}

	public void setCreatedate(UFDate createdate) {
		this.createdate = createdate;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef10() {
		return def10;
	}

	public void setDef10(String def10) {
		this.def10 = def10;
	}

	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	public String getDef12() {
		return def12;
	}

	public void setDef12(String def12) {
		this.def12 = def12;
	}

	public String getDef13() {
		return def13;
	}

	public void setDef13(String def13) {
		this.def13 = def13;
	}

	public String getDef14() {
		return def14;
	}

	public void setDef14(String def14) {
		this.def14 = def14;
	}

	public String getDef15() {
		return def15;
	}

	public void setDef15(String def15) {
		this.def15 = def15;
	}

	public String getDef16() {
		return def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef17() {
		return def17;
	}

	public void setDef17(String def17) {
		this.def17 = def17;
	}

	public String getDef18() {
		return def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
	}

	public String getDef19() {
		return def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef20() {
		return def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public String getDef6() {
		return def6;
	}

	public void setDef6(String def6) {
		this.def6 = def6;
	}

	public String getDef7() {
		return def7;
	}

	public void setDef7(String def7) {
		this.def7 = def7;
	}

	public String getDef8() {
		return def8;
	}

	public void setDef8(String def8) {
		this.def8 = def8;
	}

	public String getDef9() {
		return def9;
	}

	public void setDef9(String def9) {
		this.def9 = def9;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getEcotype() {
		return ecotype;
	}

	public void setEcotype(String ecotype) {
		this.ecotype = ecotype;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getFathercorp() {
		return fathercorp;
	}

	public void setFathercorp(String fathercorp) {
		this.fathercorp = fathercorp;
	}

	public String getFax1() {
		return fax1;
	}

	public void setFax1(String fax1) {
		this.fax1 = fax1;
	}

	public String getFax2() {
		return fax2;
	}

	public void setFax2(String fax2) {
		this.fax2 = fax2;
	}

	public String getForeignname() {
		return foreignname;
	}

	public void setForeignname(String foreignname) {
		this.foreignname = foreignname;
	}

	public UFBoolean getHoldflag() {
		return holdflag;
	}

	public void setHoldflag(UFBoolean holdflag) {
		this.holdflag = holdflag;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getInnercode() {
		return innercode;
	}

	public void setInnercode(String innercode) {
		this.innercode = innercode;
	}

	public UFBoolean getIshasaccount() {
		return ishasaccount;
	}

	public void setIshasaccount(UFBoolean ishasaccount) {
		this.ishasaccount = ishasaccount;
	}

	public UFBoolean getIsseal() {
		return isseal;
	}

	public void setIsseal(UFBoolean isseal) {
		this.isseal = isseal;
	}

	public UFBoolean getIsuseretail() {
		return isuseretail;
	}

	public void setIsuseretail(UFBoolean isuseretail) {
		this.isuseretail = isuseretail;
	}

	public UFBoolean getIsworkingunit() {
		return isworkingunit;
	}

	public void setIsworkingunit(UFBoolean isworkingunit) {
		this.isworkingunit = isworkingunit;
	}

	public String getLegalbodycode() {
		return legalbodycode;
	}

	public void setLegalbodycode(String legalbodycode) {
		this.legalbodycode = legalbodycode;
	}

	public String getLinkman1() {
		return linkman1;
	}

	public void setLinkman1(String linkman1) {
		this.linkman1 = linkman1;
	}

	public String getLinkman2() {
		return linkman2;
	}

	public void setLinkman2(String linkman2) {
		this.linkman2 = linkman2;
	}

	public String getLinkman3() {
		return linkman3;
	}

	public void setLinkman3(String linkman3) {
		this.linkman3 = linkman3;
	}

	public String getMaxinnercode() {
		return maxinnercode;
	}

	public void setMaxinnercode(String maxinnercode) {
		this.maxinnercode = maxinnercode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public UFDouble getOwnersharerate() {
		return ownersharerate;
	}

	public void setOwnersharerate(UFDouble ownersharerate) {
		this.ownersharerate = ownersharerate;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public String getPk_corp() {
		return pk_corp;
	}

	public void setPk_corp(String pk_corp) {
		this.pk_corp = pk_corp;
	}

	public String getPk_corpkind() {
		return pk_corpkind;
	}

	public void setPk_corpkind(String pk_corpkind) {
		this.pk_corpkind = pk_corpkind;
	}

	public String getPk_currency() {
		return pk_currency;
	}

	public void setPk_currency(String pk_currency) {
		this.pk_currency = pk_currency;
	}

	public String getPostaddr() {
		return postaddr;
	}

	public void setPostaddr(String postaddr) {
		this.postaddr = postaddr;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public UFDouble getRegcapital() {
		return regcapital;
	}

	public void setRegcapital(UFDouble regcapital) {
		this.regcapital = regcapital;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSaleaddr() {
		return saleaddr;
	}

	public void setSaleaddr(String saleaddr) {
		this.saleaddr = saleaddr;
	}

	public UFDate getSealeddate() {
		return sealeddate;
	}

	public void setSealeddate(UFDate sealeddate) {
		this.sealeddate = sealeddate;
	}

	public Integer getShoworder() {
		return showorder;
	}

	public void setShoworder(Integer showorder) {
		this.showorder = showorder;
	}

	public String getTaxcode() {
		return taxcode;
	}

	public void setTaxcode(String taxcode) {
		this.taxcode = taxcode;
	}

	public Integer getTaxpayertype() {
		return taxpayertype;
	}

	public void setTaxpayertype(Integer taxpayertype) {
		this.taxpayertype = taxpayertype;
	}

	public UFDateTime getTs() {
		return ts;
	}

	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	public String getUnitcode() {
		return unitcode;
	}

	public void setUnitcode(String unitcode) {
		this.unitcode = unitcode;
	}

	public String getUnitdistinction() {
		return unitdistinction;
	}

	public void setUnitdistinction(String unitdistinction) {
		this.unitdistinction = unitdistinction;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getUnitshortname() {
		return unitshortname;
	}

	public void setUnitshortname(String unitshortname) {
		this.unitshortname = unitshortname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String getPKFieldName() {
		return "pk_corp";
	}

	@Override
	public String getParentPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		return "bd_corp";
	}

}
