package com.ufsoft.iufo.inputplugin.inputcore;

import com.ufsoft.table.re.IRefComp;
import com.ufsoft.table.re.RefTextField;

public class UnitRefTextField extends RefTextField{
	private static final long serialVersionUID = 3401150660734285053L;
	private String strCurUnitCode=null;
	private String strOrgPK=null;
	public UnitRefTextField(String curUnitCode,String strOrgPK){
		super();
		this.strCurUnitCode=curUnitCode;
		this.strOrgPK=strOrgPK;
	}

	protected void initRef() {
		super.initRef();
		RefInfo refInfo = new RefInfo(RefInfo.TYPE_UNIT);
		refInfo.setOrgPK(strOrgPK);
		refInfo.setCurUnitCode(strCurUnitCode);
		IRefComp refComp = AbsCodeRefEditor.getRefComp(refInfo);
		setRefComp(refComp,null);
	}
}
