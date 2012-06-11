package nc.ui.iufo.query.datasetmanager.exts;

import nc.ui.iufo.query.datasetmanager.DataSetManager;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.AbsActionExt;

public abstract class DSMActionExt extends AbsActionExt {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final Object[] getParams(UfoReport container) {
		return null;
	}
	
	public abstract Object[] getParams(DataSetManager dsManager);

}
