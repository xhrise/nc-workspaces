package com.ufsoft.iufo.inputplugin.ufobiz.data;

import com.ufsoft.iufo.resource.StringResource;

public class EditInputDirLeftExt extends AbsEditInputDirExt {
	protected int getAltChar() {
		return 'L';
	}

	protected int getEditInputDir() {
		return InputDirConstant.DIR_LEFT;
	}

	protected String getMenuName() {
		return StringResource.getStringResource("uiufotask00077");
	}
}
