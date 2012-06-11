package com.ufsoft.table.undo;

import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class UndoPlugin  extends AbstractPlugIn {

	protected IPluginDescriptor createDescriptor() {
		return new AbstractPlugDes(this) {
			protected IExtension[] createExtensions() {
				return new IExtension[] {
						new UndoExt(UndoPlugin.this),
						new RedoExt(UndoPlugin.this),
				};
			}
		};
	}
	

}
