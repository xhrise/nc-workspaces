package nc.ui.iufo.query.datasetmanager;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;



public class DataSetManagerExt extends AbsActionExt {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		// TODO 自动生成方法存根
		return null;
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(MultiLang.getString("report00114"));
		uiDes.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00004") });
		return new ActionUIDes[] { uiDes };
	}
	
	public ActionUIDes getToolbarDes(){
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setToolBar(true);
		uiDes.setName(MultiLang.getString("report00114"));
		uiDes.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00004") });
		return uiDes;
	}

//	/**
//	 * @i18n report00112=导入XML
//	 * @i18n data=数据
//	 * @i18n import=导入
//	 */
//	public ActionUIDes[] getUIDesArr() {
//		ActionUIDes uiDes = new ActionUIDes();
//		uiDes.setName(MultiLang.getString("report00112"));
//		uiDes.setPaths(new String[] { StringResource.getStringResource("mbiadhoc00004") });
//		return new ActionUIDes[] { uiDes };
//	}
//
//	public UfoCommand getCommand() {
//		return null;
//	}
//
//	public boolean isEnabled(Component focusComp) {
//		// return m_adhocPlugin.getOperationState() ==
//		// UfoReport.OPERATION_FORMAT;
//		return m_adhocPlugin.isFormat();
//
//	}
//
//	public Object[] getParams(UfoReport container) {
//		JFileChooser chooser = new UIFileChooser();
//		ExtNameFileFilter xf = new ExtNameFileFilter("xml");
//		chooser.setFileFilter(xf);
//		chooser.setMultiSelectionEnabled(false);
//		int returnVal = chooser.showOpenDialog(m_adhocPlugin.getReport());
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//			File file = chooser.getSelectedFile();
//			if (!file.exists()) {
//				UfoPublic.showErrorDialog(m_adhocPlugin.getReport(), MultiLang.getString("file_not_exist"),
//						MultiLang.getString("error"));
//				return null;
//			}
//
//			AdhocModel model = null;
//			try {
//				ReportVO repVO = BIReportXMLUtil.parseAdhocReport(file.getAbsolutePath());
//				model = (AdhocModel) repVO.getDefinition();
//				model.setPK(repVO.getID());
//			} catch (Exception e) {
//				AppDebug.debug(e);
//				JOptionPane.showMessageDialog(m_adhocPlugin.getReport(), e.getMessage(), "",
//						JOptionPane.ERROR_MESSAGE);
//			}
//			AdhocPlugin.drillThrough(m_adhocPlugin.getReport(), model, UfoReport.OPERATION_FORMAT, null);
//		}
//		return null;
//	}
}



