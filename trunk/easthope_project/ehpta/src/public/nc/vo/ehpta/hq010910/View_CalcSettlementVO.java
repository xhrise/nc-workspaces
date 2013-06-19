/***************************************************************\
 *     The skeleton of this class is generated by an automatic *
 * code generator for NC product. It is based on Velocity.     *
\***************************************************************/
package nc.vo.ehpta.hq010910;

import java.util.ArrayList;
import nc.vo.pub.*;
import nc.vo.pub.lang.*;

/**
 * <b> �ڴ˴���Ҫ��������Ĺ��� </b>
 * 
 * <p>
 * �ڴ˴����Ӵ����������Ϣ
 * </p>
 * 
 * ��������:2012-8-27
 * 
 * @author ${vmObject.author}
 * @version Your Project 1.0
 */
public class View_CalcSettlementVO extends SuperVO {

	private static final long serialVersionUID = 2327940666346489253L;
	
	public String invcode;
	public UFDouble settlemny;
	public String voperatorid;
	public UFDouble clsmny;
	public String custname;
	public String cinvbasdocid;
	public UFDouble clastingmny;
	public UFDate cmakedate;
	public String vreceiptcode;
	public String pk_contract;
	public String measname;
	public UFBoolean settleflag;
	public UFDouble csettlemny;
	public String pk_settlement;
	public String invname;
	public String concode;
	public String csaleid;
	public UFDouble nnumber;
	public UFDouble lastingprice;
	public UFDate settledate;
	public String pk_maintain;
	public String ccustomerid;
	
	public String getCcustomerid() {
		return ccustomerid;
	}

	public void setCcustomerid(String ccustomerid) {
		this.ccustomerid = ccustomerid;
	}

	public String getInvcode() {
		return invcode;
	}

	public void setInvcode(String invcode) {
		this.invcode = invcode;
	}

	public UFDouble getSettlemny() {
		return settlemny;
	}

	public void setSettlemny(UFDouble settlemny) {
		this.settlemny = settlemny;
	}

	public String getVoperatorid() {
		return voperatorid;
	}

	public void setVoperatorid(String voperatorid) {
		this.voperatorid = voperatorid;
	}

	public UFDouble getClsmny() {
		return clsmny;
	}

	public void setClsmny(UFDouble clsmny) {
		this.clsmny = clsmny;
	}

	public String getCustname() {
		return custname;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getCinvbasdocid() {
		return cinvbasdocid;
	}

	public void setCinvbasdocid(String cinvbasdocid) {
		this.cinvbasdocid = cinvbasdocid;
	}

	public UFDouble getClastingmny() {
		return clastingmny;
	}

	public void setClastingmny(UFDouble clastingmny) {
		this.clastingmny = clastingmny;
	}

	public UFDate getCmakedate() {
		return cmakedate;
	}

	public void setCmakedate(UFDate cmakedate) {
		this.cmakedate = cmakedate;
	}

	public String getVreceiptcode() {
		return vreceiptcode;
	}

	public void setVreceiptcode(String vreceiptcode) {
		this.vreceiptcode = vreceiptcode;
	}

	public String getPk_contract() {
		return pk_contract;
	}

	public void setPk_contract(String pk_contract) {
		this.pk_contract = pk_contract;
	}

	public String getMeasname() {
		return measname;
	}

	public void setMeasname(String measname) {
		this.measname = measname;
	}

	public UFBoolean getSettleflag() {
		return settleflag;
	}

	public void setSettleflag(UFBoolean settleflag) {
		this.settleflag = settleflag;
	}

	public UFDouble getCsettlemny() {
		return csettlemny;
	}

	public void setCsettlemny(UFDouble csettlemny) {
		this.csettlemny = csettlemny;
	}

	public String getPk_settlement() {
		return pk_settlement;
	}

	public void setPk_settlement(String pk_settlement) {
		this.pk_settlement = pk_settlement;
	}

	public String getInvname() {
		return invname;
	}

	public void setInvname(String invname) {
		this.invname = invname;
	}

	public String getConcode() {
		return concode;
	}

	public void setConcode(String concode) {
		this.concode = concode;
	}

	public String getCsaleid() {
		return csaleid;
	}

	public void setCsaleid(String csaleid) {
		this.csaleid = csaleid;
	}

	public UFDouble getNnumber() {
		return nnumber;
	}

	public void setNnumber(UFDouble nnumber) {
		this.nnumber = nnumber;
	}

	public UFDouble getLastingprice() {
		return lastingprice;
	}

	public void setLastingprice(UFDouble lastingprice) {
		this.lastingprice = lastingprice;
	}

	public UFDate getSettledate() {
		return settledate;
	}

	public void setSettledate(UFDate settledate) {
		this.settledate = settledate;
	}

	public String getPk_maintain() {
		return pk_maintain;
	}

	public void setPk_maintain(String pk_maintain) {
		this.pk_maintain = pk_maintain;
	}

	@Override
	public String getParentPKFieldName() {
		return null;
	}

	@Override
	public String getPKFieldName() {
		return null;
	}

	@Override
	public String getTableName() {


//		create or replace view vw_pta_settlement as
//		select sale.csaleid , sale.pk_contract , mt.pk_maintain , orderb.cinvbasdocid , sale.ccustomerid ,
//		cubas.custname , sale.concode ,  sale.vreceiptcode , sale.dmakedate cmakedate ,
//		invbas.invcode , invbas.invname , meas.measname , orderb.nnumber , orderb.lastingprice ,
//		(orderb.lastingprice * orderb.nnumber) clastingmny , mt.settlemny , (mt.settlemny * orderb.nnumber) csettlemny ,
//		((orderb.lastingprice - mt.settlemny) * orderb.nnumber) clsmny , sale.settleflag , sale.settledate ,
//		sale.dr
//		from so_sale sale
//		left join so_saleorder_b orderb on orderb.csaleid = sale.csaleid
//		left join bd_invbasdoc invbas on invbas.pk_invbasdoc = orderb.cinvbasdocid
//		left join bd_measdoc meas on meas.pk_measdoc = invbas.pk_measdoc
//		left join ehpta_maintain mt on substr(mt.maindate , 0 , 7) = substr(sale.dmakedate , 0 , 7)
//		left join bd_cumandoc cuman on cuman.pk_cumandoc = sale.ccustomerid
//		left join bd_cubasdoc cubas on cubas.pk_cubasdoc = cuman.pk_cubasdoc
//
//		where sale.contracttype is not null
//		and (sale.contracttype = 20)
//		and sale.pk_contract is not null
//		and mt.type = 2
//		and nvl(orderb.lastingprice,0) <> nvl(mt.settlemny,0)
//		and mt.pk_invbasdoc = orderb.cinvbasdocid
//		and mt.vbillstatus = 1
//		and sale.fstatus = 2
//
//		and nvl(sale.dr , 0) = 0
//		and nvl(orderb.dr ,0) = 0
//		and nvl(invbas.dr ,0) = 0
//		and nvl(meas.dr ,0) = 0
//		and nvl(mt.dr ,0) = 0;


		return "vw_pta_settlement";
	}

}