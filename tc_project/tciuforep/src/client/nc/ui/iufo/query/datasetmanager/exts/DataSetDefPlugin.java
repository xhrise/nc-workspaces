package nc.ui.iufo.query.datasetmanager.exts;

import java.awt.Container;

import com.ufsoft.report.plugin.AbstractPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;

public class DataSetDefPlugin  extends AbstractPlugIn {
	
	private Container container;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataSetDefPlugin(Container container)
	{
		this.container = container;
	}
	
	protected IPluginDescriptor createDescriptor() {
		return new DataSetDefDescriptor(this);
	}
	
	public Container getContainer()
	{
		return this.container;
	}
}