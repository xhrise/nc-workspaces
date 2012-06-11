package com.ufsoft.iufo.inputplugin.inputcore;

import com.ufsoft.table.re.IRefComp;
import com.ufsoft.table.re.RefTextField;

public class CodeRefTextField extends RefTextField {
	private static final long serialVersionUID = -2314078850414663274L;
	private String strCodeGroupPK=null;
	
	public CodeRefTextField(String codeGroupPK){
		super();
		strCodeGroupPK=codeGroupPK;
	}
	
	protected void initRef() {
		super.initRef();
		RefInfo refInfo = new RefInfo(strCodeGroupPK);
		IRefComp refComp = AbsCodeRefEditor.getRefComp(refInfo);
		setRefComp(refComp,null);
	}
}
