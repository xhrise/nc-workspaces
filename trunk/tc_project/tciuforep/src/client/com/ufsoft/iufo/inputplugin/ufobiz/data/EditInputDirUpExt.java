package com.ufsoft.iufo.inputplugin.ufobiz.data;

import com.ufsoft.iufo.resource.StringResource;

public class EditInputDirUpExt extends AbsEditInputDirExt {
	protected int getAltChar() {
		return 'U';
	}

	protected int getEditInputDir() {
		return InputDirConstant.DIR_UP;
	}

	protected String getMenuName() {
		return StringResource.getStringResource("uiufotask00076");
	}
}
