/**
 * @(#)CHGZA28TOZA30.java	V3.1 2008-6-6
 * 
 * Copyright 1988-2005 UFIDA, Inc. All Rights Reserved.
 *
 * This software is the proprietary information of UFSoft, Inc.
 * Use is subject to license terms.
 *
 */
package nc.ui.pf.changedir;

import nc.vo.pf.change.UserDefineFunction;

public class CHGZA28TOZA30 extends nc.ui.pf.change.VOConversionUI{

    public CHGZA28TOZA30() {
        super();
    }
    /**
    * 获得后续类的全录经名称。
    * @return java.lang.String[]
    */
    @Override
	public String getAfterClassName() {
        return null;
    }
    /**
    * 获得另一个后续类的全录径名称。
    * @return java.lang.String[]
    */
    @Override
	public String getOtherClassName() {
        return null;
    }
    /**
    * 获得字段对应。
    * @return java.lang.String[]
    */
    @Override
	public String[] getField() {
        return new String[] {
//            "H_pk_invbasdoc->H_def_1",
            "H_pk_sample->H_billno",
            "H_vsourcebilltype->H_vbilltype",
            "H_vsourcebillid->H_pk_procheckapply",
            "H_pk_procheckapplys->H_pk_procheckapplys",
            "B_pk_project->B_def_1",
            "B_fxtype->B_def_2",
            "B_ll_ceil->B_def_3",
            "B_ll_limit->B_def_4",
            "B_rece_ceil->B_def_5",
            "B_rece_limit->B_def_11",
            "B_vsourcebillid->B_pk_procheckapply_b",
//            "B_def_8->B_dr",
            "B_def_8->B_instalment"
        
        };
    }
    /**
    * 获得公式。
    * @return java.lang.String[]
    */
    @Override
	public String[] getFormulas() {
        return null;
    }
    /**
    * 返回用户自定义函数。
    */
    @Override
	public UserDefineFunction[] getUserDefineFunction() {
        return null;
    }
}

