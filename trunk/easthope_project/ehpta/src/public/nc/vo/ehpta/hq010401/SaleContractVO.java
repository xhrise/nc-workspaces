/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.ehpta.hq010401;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b> 在此处简要描述此类的功能 </b>
 * 
 * <p>
 * 在此处添加此类的描述信息
 * </p>
 * 
 * 创建日期:2012-7-9
 * 
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class SaleContractVO extends SuperVO {

	public String def5;
	public String pk_corp;
	public String pk_psndoc;
	public String connamed;
	public UFDate dmakedate;
	public Integer dr;
	public String voperatorid;
	public String vapprovenote;
	public UFDate orderdate;
	public String pk_billtype;
	public String def7;
	public Integer vbillstatus;
	public UFBoolean stopcontract;
	public UFBoolean rebate_flag;
	public String purchname;
	public UFDateTime ts;
	public String def6;
	public String pk_busitype;
	public String bargainor;
	public String orderaddress;
	public String vbillno;
	public String pk_contract;
	public String def10;
	public String memo;
	public UFDouble poundsofpoor;
	public String def8;
	public String pk_deptdoc;
	public String def1;
	public UFDate dapprovedate;
	public Integer version;
	public String vapproveid;
	public String def2;
	public UFDate pay_cutoffdate;
	public UFDate edate;
	public UFDate deliverydate;
	public String def4;
	public String contype;
	public String purchcode;
	public UFDate sdate;
	public String def3;
	public String def9;
	public String termination;
	public String custcode;
	public UFBoolean close_flag;

	public static final String DEF5 = "def5";
	public static final String PK_CORP = "pk_corp";
	public static final String PK_PSNDOC = "pk_psndoc";
	public static final String CONNAMED = "connamed";
	public static final String DMAKEDATE = "dmakedate";
	public static final String DR = "dr";
	public static final String VOPERATORID = "voperatorid";
	public static final String VAPPROVENOTE = "vapprovenote";
	public static final String ORDERDATE = "orderdate";
	public static final String PK_BILLTYPE = "pk_billtype";
	public static final String DEF7 = "def7";
	public static final String VBILLSTATUS = "vbillstatus";
	public static final String STOPCONTRACT = "stopcontract";
	public static final String REBATE_FLAG = "rebate_flag";
	public static final String PURCHNAME = "purchname";
	public static final String TS = "ts";
	public static final String DEF6 = "def6";
	public static final String PK_BUSITYPE = "pk_busitype";
	public static final String BARGAINOR = "bargainor";
	public static final String ORDERADDRESS = "orderaddress";
	public static final String VBILLNO = "vbillno";
	public static final String PK_CONTRACT = "pk_contract";
	public static final String DEF10 = "def10";
	public static final String MEMO = "memo";
	public static final String POUNDSOFPOOR = "poundsofpoor";
	public static final String DEF8 = "def8";
	public static final String PK_DEPTDOC = "pk_deptdoc";
	public static final String DEF1 = "def1";
	public static final String DAPPROVEDATE = "dapprovedate";
	public static final String VERSION = "version";
	public static final String VAPPROVEID = "vapproveid";
	public static final String DEF2 = "def2";
	public static final String PAY_CUTOFFDATE = "pay_cutoffdate";
	public static final String EDATE = "edate";
	public static final String DELIVERYDATE = "deliverydate";
	public static final String DEF4 = "def4";
	public static final String CONTYPE = "contype";
	public static final String PURCHCODE = "purchcode";
	public static final String SDATE = "sdate";
	public static final String DEF3 = "def3";
	public static final String DEF9 = "def9";
	public static final String TERMINATION = "termination";

	public UFBoolean getClose_flag() {
		return close_flag;
	}

	public void setClose_flag(UFBoolean close_flag) {
		this.close_flag = close_flag;
	}

	public String getCustcode() {
		return custcode;
	}

	public void setCustcode(String custcode) {
		this.custcode = custcode;
	}

	/**
	 * 属性def5的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef5() {
		return def5;
	}

	/**
	 * 属性def5的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef5
	 *            String
	 */
	public void setDef5(String newDef5) {

		def5 = newDef5;
	}

	/**
	 * 属性pk_corp的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * 属性pk_corp的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_corp
	 *            String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * 属性pk_psndoc的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPk_psndoc() {
		return pk_psndoc;
	}

	/**
	 * 属性pk_psndoc的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_psndoc
	 *            String
	 */
	public void setPk_psndoc(String newPk_psndoc) {

		pk_psndoc = newPk_psndoc;
	}

	/**
	 * 属性connamed的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getConnamed() {
		return connamed;
	}

	/**
	 * 属性connamed的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newConnamed
	 *            String
	 */
	public void setConnamed(String newConnamed) {

		connamed = newConnamed;
	}

	/**
	 * 属性dmakedate的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDate
	 */
	public UFDate getDmakedate() {
		return dmakedate;
	}

	/**
	 * 属性dmakedate的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDmakedate
	 *            UFDate
	 */
	public void setDmakedate(UFDate newDmakedate) {

		dmakedate = newDmakedate;
	}

	/**
	 * 属性dr的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性voperatorid的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getVoperatorid() {
		return voperatorid;
	}

	/**
	 * 属性voperatorid的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newVoperatorid
	 *            String
	 */
	public void setVoperatorid(String newVoperatorid) {

		voperatorid = newVoperatorid;
	}

	/**
	 * 属性vapprovenote的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getVapprovenote() {
		return vapprovenote;
	}

	/**
	 * 属性vapprovenote的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newVapprovenote
	 *            String
	 */
	public void setVapprovenote(String newVapprovenote) {

		vapprovenote = newVapprovenote;
	}

	/**
	 * 属性orderdate的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDate
	 */
	public UFDate getOrderdate() {
		return orderdate;
	}

	/**
	 * 属性orderdate的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newOrderdate
	 *            UFDate
	 */
	public void setOrderdate(UFDate newOrderdate) {

		orderdate = newOrderdate;
	}

	/**
	 * 属性pk_billtype的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * 属性pk_billtype的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_billtype
	 *            String
	 */
	public void setPk_billtype(String newPk_billtype) {

		pk_billtype = newPk_billtype;
	}

	/**
	 * 属性def7的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef7() {
		return def7;
	}

	/**
	 * 属性def7的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef7
	 *            String
	 */
	public void setDef7(String newDef7) {

		def7 = newDef7;
	}

	/**
	 * 属性vbillstatus的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return Integer
	 */
	public Integer getVbillstatus() {
		return vbillstatus;
	}

	/**
	 * 属性vbillstatus的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newVbillstatus
	 *            Integer
	 */
	public void setVbillstatus(Integer newVbillstatus) {

		vbillstatus = newVbillstatus;
	}

	/**
	 * 属性stopcontract的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getStopcontract() {
		return stopcontract;
	}

	/**
	 * 属性stopcontract的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newStopcontract
	 *            UFBoolean
	 */
	public void setStopcontract(UFBoolean newStopcontract) {

		stopcontract = newStopcontract;
	}

	/**
	 * 属性rebate_flag的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFBoolean
	 */
	public UFBoolean getRebate_flag() {
		return rebate_flag;
	}

	/**
	 * 属性rebate_flag的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newRebate_flag
	 *            UFBoolean
	 */
	public void setRebate_flag(UFBoolean newRebate_flag) {

		rebate_flag = newRebate_flag;
	}

	/**
	 * 属性purchname的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPurchname() {
		return purchname;
	}

	/**
	 * 属性purchname的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPurchname
	 *            String
	 */
	public void setPurchname(String newPurchname) {

		purchname = newPurchname;
	}

	/**
	 * 属性ts的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	/**
	 * 属性def6的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef6() {
		return def6;
	}

	/**
	 * 属性def6的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef6
	 *            String
	 */
	public void setDef6(String newDef6) {

		def6 = newDef6;
	}

	/**
	 * 属性pk_busitype的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPk_busitype() {
		return pk_busitype;
	}

	/**
	 * 属性pk_busitype的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_busitype
	 *            String
	 */
	public void setPk_busitype(String newPk_busitype) {

		pk_busitype = newPk_busitype;
	}

	/**
	 * 属性bargainor的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getBargainor() {
		return bargainor;
	}

	/**
	 * 属性bargainor的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newBargainor
	 *            String
	 */
	public void setBargainor(String newBargainor) {

		bargainor = newBargainor;
	}

	/**
	 * 属性orderaddress的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getOrderaddress() {
		return orderaddress;
	}

	/**
	 * 属性orderaddress的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newOrderaddress
	 *            String
	 */
	public void setOrderaddress(String newOrderaddress) {

		orderaddress = newOrderaddress;
	}

	/**
	 * 属性vbillno的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getVbillno() {
		return vbillno;
	}

	/**
	 * 属性vbillno的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newVbillno
	 *            String
	 */
	public void setVbillno(String newVbillno) {

		vbillno = newVbillno;
	}

	/**
	 * 属性pk_contract的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPk_contract() {
		return pk_contract;
	}

	/**
	 * 属性pk_contract的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_contract
	 *            String
	 */
	public void setPk_contract(String newPk_contract) {

		pk_contract = newPk_contract;
	}

	/**
	 * 属性def10的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef10() {
		return def10;
	}

	/**
	 * 属性def10的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef10
	 *            String
	 */
	public void setDef10(String newDef10) {

		def10 = newDef10;
	}

	/**
	 * 属性memo的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 属性memo的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newMemo
	 *            String
	 */
	public void setMemo(String newMemo) {

		memo = newMemo;
	}

	/**
	 * 属性poundsofpoor的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDouble
	 */
	public UFDouble getPoundsofpoor() {
		return poundsofpoor;
	}

	/**
	 * 属性poundsofpoor的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPoundsofpoor
	 *            UFDouble
	 */
	public void setPoundsofpoor(UFDouble newPoundsofpoor) {

		poundsofpoor = newPoundsofpoor;
	}

	/**
	 * 属性def8的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef8() {
		return def8;
	}

	/**
	 * 属性def8的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef8
	 *            String
	 */
	public void setDef8(String newDef8) {

		def8 = newDef8;
	}

	/**
	 * 属性pk_deptdoc的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPk_deptdoc() {
		return pk_deptdoc;
	}

	/**
	 * 属性pk_deptdoc的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_deptdoc
	 *            String
	 */
	public void setPk_deptdoc(String newPk_deptdoc) {

		pk_deptdoc = newPk_deptdoc;
	}

	/**
	 * 属性def1的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef1() {
		return def1;
	}

	/**
	 * 属性def1的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef1
	 *            String
	 */
	public void setDef1(String newDef1) {

		def1 = newDef1;
	}

	/**
	 * 属性dapprovedate的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDate
	 */
	public UFDate getDapprovedate() {
		return dapprovedate;
	}

	/**
	 * 属性dapprovedate的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDapprovedate
	 *            UFDate
	 */
	public void setDapprovedate(UFDate newDapprovedate) {

		dapprovedate = newDapprovedate;
	}

	/**
	 * 属性version的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return Integer
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 属性version的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newVersion
	 *            Integer
	 */
	public void setVersion(Integer newVersion) {

		version = newVersion;
	}

	/**
	 * 属性vapproveid的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getVapproveid() {
		return vapproveid;
	}

	/**
	 * 属性vapproveid的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newVapproveid
	 *            String
	 */
	public void setVapproveid(String newVapproveid) {

		vapproveid = newVapproveid;
	}

	/**
	 * 属性def2的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef2() {
		return def2;
	}

	/**
	 * 属性def2的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef2
	 *            String
	 */
	public void setDef2(String newDef2) {

		def2 = newDef2;
	}

	/**
	 * 属性pay_cutoffdate的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDate
	 */
	public UFDate getPay_cutoffdate() {
		return pay_cutoffdate;
	}

	/**
	 * 属性pay_cutoffdate的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPay_cutoffdate
	 *            UFDate
	 */
	public void setPay_cutoffdate(UFDate newPay_cutoffdate) {

		pay_cutoffdate = newPay_cutoffdate;
	}

	/**
	 * 属性edate的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDate
	 */
	public UFDate getEdate() {
		return edate;
	}

	/**
	 * 属性edate的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newEdate
	 *            UFDate
	 */
	public void setEdate(UFDate newEdate) {

		edate = newEdate;
	}

	/**
	 * 属性deliverydate的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDate
	 */
	public UFDate getDeliverydate() {
		return deliverydate;
	}

	/**
	 * 属性deliverydate的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDeliverydate
	 *            UFDate
	 */
	public void setDeliverydate(UFDate newDeliverydate) {

		deliverydate = newDeliverydate;
	}

	/**
	 * 属性def4的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef4() {
		return def4;
	}

	/**
	 * 属性def4的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef4
	 *            String
	 */
	public void setDef4(String newDef4) {

		def4 = newDef4;
	}

	/**
	 * 属性contype的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getContype() {
		return contype;
	}

	/**
	 * 属性contype的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newContype
	 *            String
	 */
	public void setContype(String newContype) {

		contype = newContype;
	}

	/**
	 * 属性purchcode的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPurchcode() {
		return purchcode;
	}

	/**
	 * 属性purchcode的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPurchcode
	 *            String
	 */
	public void setPurchcode(String newPurchcode) {

		purchcode = newPurchcode;
	}

	/**
	 * 属性sdate的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return UFDate
	 */
	public UFDate getSdate() {
		return sdate;
	}

	/**
	 * 属性sdate的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newSdate
	 *            UFDate
	 */
	public void setSdate(UFDate newSdate) {

		sdate = newSdate;
	}

	/**
	 * 属性def3的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef3() {
		return def3;
	}

	/**
	 * 属性def3的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef3
	 *            String
	 */
	public void setDef3(String newDef3) {

		def3 = newDef3;
	}

	/**
	 * 属性def9的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getDef9() {
		return def9;
	}

	/**
	 * 属性def9的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newDef9
	 *            String
	 */
	public void setDef9(String newDef9) {

		def9 = newDef9;
	}

	/**
	 * 属性termination的Getter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getTermination() {
		return termination;
	}

	/**
	 * 属性termination的Setter方法.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newTermination
	 *            String
	 */
	public void setTermination(String newTermination) {

		termination = newTermination;
	}

	/**
	 * 验证对象各属性之间的数据逻辑正确性.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList errFields = new ArrayList(); // errFields record those null

		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:

		if (pk_contract == null) {
			errFields.add(new String("pk_contract"));
		}

		StringBuffer message = new StringBuffer();
		message.append("下列字段不能为空:");
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			throw new NullFieldException(message.toString());
		}
	}

	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2012-7-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;

	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2012-7-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_contract";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2012-7-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "ehpta_sale_contract";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2012-7-9
	 */
	public SaleContractVO() {

		super();
	}

	/**
	 * 使用主键进行初始化的构造子.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_contract
	 *            主键值
	 */
	public SaleContractVO(String newPk_contract) {

		// 为主键字段赋值:
		pk_contract = newPk_contract;

	}

	/**
	 * 返回对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_contract;

	}

	/**
	 * 设置对象标识,用来唯一定位对象.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @param newPk_contract
	 *            String
	 */
	public void setPrimaryKey(String newPk_contract) {

		pk_contract = newPk_contract;

	}

	/**
	 * 返回数值对象的显示名称.
	 * 
	 * 创建日期:2012-7-9
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "ehpta_sale_contract";

	}
}
