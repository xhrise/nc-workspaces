package nc.ui.iufo.query.datasetmanager;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

/**
 * Adhoc������
 * 
 * @author caijie
 */
public class DataSetManagerPlugin extends AbstractPlugIn {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected IPluginDescriptor createDescriptor() {
		return new DataSetManagerDescriptor(this);
	}




}
