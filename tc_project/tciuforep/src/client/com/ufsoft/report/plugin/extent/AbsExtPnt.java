package com.ufsoft.report.plugin.extent;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

@Deprecated
public abstract class AbsExtPnt implements IExtPnt {

	private Vector _exts = new Vector();

	public void registeExt(IExt ext) {
		_exts.add(ext);
	}

	public List getExts() {
		return Collections.unmodifiableList(_exts);
	}

}
