package com.ufsoft.iufo.inputplugin.ufobiz;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.inputplugin.ufobiz.data.EditInputDirDownExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.EditInputDirLeftExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.EditInputDirRightExt;
import com.ufsoft.iufo.inputplugin.ufobiz.data.EditInputDirUpExt;

public class EditInpuDirPlugin extends AbstractPlugin{
	protected IPluginAction[] createActions() {
		return new IPluginAction[]{new EditInputDirRightExt(),new EditInputDirLeftExt(),new EditInputDirDownExt(),new EditInputDirUpExt()};
	}

	public void shutdown() {
	}

	public void startup() {

	}
}
