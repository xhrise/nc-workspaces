/**
 * @(#)StockCheckreportCVO.java	V3.1 2008-8-29
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.vo.eh.iso.z0502005;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

public class StockCheckreportCVO extends SuperVO {

    public String pk_checkreport_c;
    public String pk_checkreport;
    public String pk_invbasdoc;
    public String pk_corp;
    public UFDateTime ts; 
    public Integer dr;
    public String vsourcebillid;
    public String jcbillno;
    public UFDouble amount;
    public String def_1;
    public String def_2;
    public String def_3;
    public String def_4;
    public String def_5;
    public UFDouble def_6;
    public UFDouble def_7;
    public UFDouble def_8;
    public UFDouble def_9;
    public UFDouble def_10;

    public String getVsourcebillid() {
        return vsourcebillid;
    }
 
    public void setVsourcebillid(String vsourcebillid) {
        this.vsourcebillid = vsourcebillid;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public String getPk_checkreport() {
        return pk_checkreport;
    }

    public void setPk_checkreport(String pk_checkreport) {
        this.pk_checkreport = pk_checkreport;
    }

    public String getPk_checkreport_c() {
        return pk_checkreport_c;
    }

    public void setPk_checkreport_c(String pk_checkreport_c) {
        this.pk_checkreport_c = pk_checkreport_c;
    }

    public String getPk_corp() {
        return pk_corp;
    }

    public void setPk_corp(String pk_corp) {
        this.pk_corp = pk_corp;
    }

    public String getPk_invbasdoc() {
        return pk_invbasdoc;
    }

    public void setPk_invbasdoc(String pk_invbasdoc) {
        this.pk_invbasdoc = pk_invbasdoc;
    }

    public UFDateTime getTs() {
        return ts;
    }

    public void setTs(UFDateTime ts) {
        this.ts = ts;
    }

    public String getPKFieldName() {
        return "pk_checkreport_c";
    }

    public String getParentPKFieldName() {
        return "pk_checkreport";
    }

    public String getTableName() {
        return "eh_stock_checkreport_c";
    }

    public String getJcbillno() {
        return jcbillno;
    }

    public void setJcbillno(String jcbillno) {
        this.jcbillno = jcbillno;
    }

	public UFDouble getAmount() {
		return amount;
	}

	public void setAmount(UFDouble amount) {
		this.amount = amount;
	}
    
    
}

