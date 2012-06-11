package com.ufsoft.iufo.inputplugin.ref;

import java.awt.Container;

import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.inputplugin.inputcore.AbsCodeRefEditor;
import com.ufsoft.iufo.inputplugin.inputcore.AccPeriodRefComp;
import com.ufsoft.iufo.inputplugin.inputcore.AccPeriodYearRefComp;
import com.ufsoft.iufo.inputplugin.inputcore.RefInfo;
import com.ufsoft.table.re.IRefComp;

/**
 * 参照工厂类
 * @created by wangyga at 2008-12-26,上午10:47:22
 *
 */

public class ComRefFactory {

	/**
	 *
	 * @param param 参数
	 * @param parent
	 * @return
	 */
	public static IComRefDlg getRefInstance(RefParam param, Container parent) {
		IComRefDlg ref = getRefComDlg(parent);
		ref.setRefComp(getRefInstance(param));
		return ref;
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	private static IRefComp getRefInstance(RefParam param) {
		if (param == null)
			return null;
		int iRefType = param.getRefType();

		IRefComp refComp = null;
		RefInfo refInfo = null;

		switch (iRefType) {
		case RefParam.ACC_REF_CODE:
			Object accPreiodPkObj = param.getAttribute(RefParam.ACC_PREIOD_PK);
			String strAccPreiodPk = accPreiodPkObj != null ? accPreiodPkObj.toString() : "";
			
			Object accPreiodTypeObj = param.getAttribute(RefParam.ACC_PERIOD_TYPE);
			String strAccPreiodType = accPreiodTypeObj != null ? accPreiodTypeObj.toString() : "";
			if(KeyVO.ACC_YEAR_PK.equals(strAccPreiodType)){
				refComp = new AccPeriodYearRefComp(strAccPreiodPk,KeyVO.ACC_YEAR_PK,null);
			} else{
				refComp = new AccPeriodRefComp(strAccPreiodPk,strAccPreiodType,null,null);
			}
			break;
		case RefParam.COIN_REF_CODE:
			refInfo = new RefInfo(CodeVO.COIN_CODE_ID);
			refComp = AbsCodeRefEditor.getRefComp(refInfo);
			break;
		case RefParam.TIME_REF_CODE:

			break;
		case RefParam.UNIT_REF_CODE:	
			Object orgPkObj = param.getAttribute(RefParam.ORG_PK);
			Object curUnitCodeObj = param.getAttribute(RefParam.CUR_UNIT_CODE);
			
			String strOrgPK = orgPkObj != null ? orgPkObj.toString() : "";
			String curUnitCode = curUnitCodeObj != null ? curUnitCodeObj.toString() : "";
			
			refInfo = new RefInfo(RefInfo.TYPE_UNIT);
			refInfo.setOrgPK(strOrgPK);
			refInfo.setCurUnitCode(curUnitCode);
			refComp = AbsCodeRefEditor.getRefComp(refInfo);

			break;

		default:
			break;
		}

		return refComp;
	}

	private static IComRefDlg getRefComDlg(Container parent) {
		return new ComRefDlg(parent);
	}
}
