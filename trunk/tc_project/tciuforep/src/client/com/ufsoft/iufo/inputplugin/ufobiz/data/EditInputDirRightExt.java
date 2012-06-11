package com.ufsoft.iufo.inputplugin.ufobiz.data;

import com.ufsoft.iufo.resource.StringResource;

public class EditInputDirRightExt extends AbsEditInputDirExt {
	protected int getAltChar() {
		return 'R';
	}

	protected int getEditInputDir() {
		return InputDirConstant.DIR_RIGHT;
	}

	protected String getMenuName() {
		return StringResource.getStringResource("uiufotask00075");
	}
}
