package nc.ui.iufo.query.datasetmanager;

import java.util.ArrayList;

import nc.ui.pub.dsmanager.DatasetTreeDlg;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;

/**
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 * 
 * @author caijie
 */
public class DataSetManagerDescriptor extends AbstractPlugDes implements IUfoContextKey {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public class DataSetManagerExt extends AbsActionExt
	{

		@Override
		public UfoCommand getCommand() {
			// TODO 自动生成方法存根
			return null;
		}

		@Override
		public Object[] getParams(UfoReport container) {
			//added by csli:针对QE入口的情况做特殊处理 only for V56
			ContextVO context=container.getContextVo();
			Integer obj = (Integer)context.getAttribute("OpeninModalDialog");
			if((obj == null) ? false : obj>=0){
				String ownerID=context.getAttribute(REPORT_PK) == null ? null : (String)container.getContextVo().getAttribute(REPORT_PK);
				DatasetTreeDlg dlg=new DatasetTreeDlg(getReport(),false,false,ownerID,new String[]{"R","G","H"});
				dlg.showModal();
				return null;
			}
			//yza+ 2008-1-16 私有数据集拥有者ID 
			String strRepId = container.getContextVo().getAttribute(REPORT_PK) == null ? null : (String)container.getContextVo().getAttribute(REPORT_PK);
			
//			String ownerID = container.getContextVo().getContextId()==null?"":container.getContextVo().getContextId();
			DataSetManagerDlg dlg = new DataSetManagerDlg(getReport(),strRepId);////父窗口 liuyy+
			dlg.showModal();
			return null;
		}

		/**
		 * @i18n miufo00389=数据集管理
		 */
		@Override
		public ActionUIDes[] getUIDesArr() {
			ActionUIDes uiDes = new ActionUIDes();
			uiDes.setName(StringResource.getStringResource("miufo00389"));
			uiDes.setImageFile("reportcore/dataset_manager.gif");
			uiDes.setPaths(new String[] {StringResource.getStringResource("miufo00241")});
			uiDes.setShowDialog(true);
			return new ActionUIDes[] { uiDes };
		}
		
	}
	
	public DataSetManagerDescriptor(DataSetManagerPlugin plugin) {
		super(plugin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ufsoft.report.plugin.AbstractPlugDes#createExtensions()
	 */
	protected IExtension[] createExtensions() {
		ArrayList<IExtension> al_extensions = new ArrayList<IExtension>();

		al_extensions.add(new DataSetManagerExt());
		return al_extensions.toArray(new IExtension[0]);
	}


}
