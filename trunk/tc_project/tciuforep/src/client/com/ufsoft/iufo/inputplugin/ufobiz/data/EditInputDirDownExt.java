package com.ufsoft.iufo.inputplugin.ufobiz.data;

import com.ufsoft.iufo.resource.StringResource;

public class EditInputDirDownExt extends AbsEditInputDirExt {
	protected int getAltChar() {
		return 'D';
	}

	protected int getEditInputDir() {
		return InputDirConstant.DIR_DOWN;
	}

	protected String getMenuName() {
		return StringResource.getStringResource("uiufotask00074");
	}
}
