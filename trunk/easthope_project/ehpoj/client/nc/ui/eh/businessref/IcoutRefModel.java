/**
 * @(#)ReceiptRefModel.java	V3.1 2008-6-27
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.eh.businessref;

import java.util.Vector;

import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.pub.ClientEnvironment;

/**
 * 功能：成品出库单
 * @author wm
 * @date  2008-6-27 13:36:23
 */

public class IcoutRefModel  extends AbstractRefModel{
	
	public  Vector vector=null;
	ClientEnvironment ce = ClientEnvironment.getInstance();
	public IcoutRefModel(){
		super();
		reloadData();
	}
	
	@Override
	public int getDefaultFieldCount() {
        return 3;
    }

	@Override
	public String[] getFieldCode() {
		return new String[]{
				//"a.billno","b.custname","cc.billno","pk_icout"
				"billno","custname","ladbillno","pk_icout"
		};
	}
	@Override
	public String[] getFieldName() {
		return new String[]{
				"单据号","客户","提货通知单单号","主键"
		};
	}

	@Override
	public String getPkFieldCode() {
		return "pk_icout";
	}

	@Override
	public String getRefTitle() {
		return "出库单";
	}

	@Override
	public String getTableName() {
		//return "eh_icout a,bd_cubasdoc b,bd_cumandoc bb,eh_ladingbill cc";
		return " EH_VIEW_ICO_LAD a ";
	}
	
	@Override
	public String getWherePart() {
        //return "a.pk_corp = '"+ce.getCorporation().getPk_corp()+"'  and a.pk_cubasdoc=bb.pk_cumandoc and b.pk_cubasdoc=bb.pk_cubasdoc and isnull(a.lock_flag,'N')='N' and (isnull(a.sb_flag,'N')='N' or a.sb_flag='' ) and isnull(bb.dr,0)=0 and  isnull(a.dr,0)=0";
		//return "cc.pk_ladingbill = a.vsourcebillid AND cc.pk_corp = '"+ce.getCorporation().getPk_corp()+"'  AND a.pk_corp = '"+ce.getCorporation().getPk_corp()+"'  and a.pk_cubasdoc=bb.pk_cumandoc and b.pk_cubasdoc=bb.pk_cubasdoc and isnull(a.lock_flag,'N')='N' and (isnull(a.sb_flag,'N')='N' or a.sb_flag='' ) and isnull(bb.dr,0)=0 and  isnull(a.dr,0)=0 and NVL(cc.dr,0)= 0";
		return " icopk_corp = '"+ce.getCorporation().getPk_corp()+"' AND ladpk_corp = '"+ce.getCorporation().getPk_corp()+"' ";
    }
	
	@Override
	public String getOrderPart() {   
		// TODO Auto-generated method stub
		return "a.billno desc";
	}
}

