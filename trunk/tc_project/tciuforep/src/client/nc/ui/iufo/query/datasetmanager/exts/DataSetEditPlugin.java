package nc.ui.iufo.query.datasetmanager.exts;

import nc.ui.iufo.query.datasetmanager.DataSetManager;
import java.awt.Container;
import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class DataSetEditPlugin  extends AbstractPlugIn {
	
	private Container container;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataSetManager manager;
	
	public DataSetEditPlugin(DataSetManager dsManager,Container container)
	{
		this.manager = dsManager;
		this.container = container;
	}
	
	protected IPluginDescriptor createDescriptor() {
		return new DataSetEditDescriptor(this);
	}
	
	public DataSetManager getDataSetManager()
	{
		return this.manager;
	}
	
	public Container getContainer()
	{
		return this.container;
	}
}
