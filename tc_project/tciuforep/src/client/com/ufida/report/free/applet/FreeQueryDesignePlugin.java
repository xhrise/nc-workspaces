package com.ufida.report.free.applet;

import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import nc.bs.bi.report.manager.DataManageObjectUFBI;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.itf.iufo.freequery.IMember;
import nc.ui.bi.integration.dimension.CodeQueryCreater;
import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.iufo.resmng.ResMngDirBO_Client;
import nc.us.bi.rolap.FreeRepDataService;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.QueryModelSrv;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.bi.report.manager.ReportResource;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.measure.MeasureQueryModelDef;
import nc.vo.iufo.resmng.ResMngDirVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.pub.ValueObject;

import com.ufida.bi.base.BIException;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.adhoc.applet.AdhocDescriptor;
import com.ufida.report.adhoc.applet.AdhocPlugin;
import com.ufida.report.adhoc.model.AdhocBaseAnalyzer;
import com.ufida.report.adhoc.model.AdhocDataCenter;
import com.ufida.report.adhoc.model.AdhocPageDimField;
import com.ufida.report.adhoc.model.AdhocQueryModel;
import com.ufida.report.free.FreeRepModel;
import com.ufida.report.free.MeasQueryExcutor;
import com.ufida.report.rep.applet.BINavigationPanel;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportUtil;
import com.ufida.report.rep.model.DefaultReportField;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.freequery.FreeQueryContextVO;
import com.ufsoft.iuforeport.freequery.FreeQueryTranceObj;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.UserUIEvent;
import com.ufsoft.table.re.SheetCellEditor;
import com.ufsoft.table.re.SheetCellRenderer;

/**
 * ���ɲ�ѯ����Ʋ��
 * 
 * @author zzl 2005-5-25
 */
public class FreeQueryDesignePlugin extends BIPlugIn implements IUfoContextKey {
	public static final String FREE_QUERYFOLDER_ID = "FreeQueryFolder00001";

	private static String modulePK = IBIResMngConstants.MODULE_QUERY;

	private FreeRepModel m_model = null;

	private String pk_report = null;

	private String m_userID = null;

	private boolean m_bFormat = false;

	private FreeRepDescriptor m_desc = null;

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#startup()
	 */
	public void startup() {
		// ��ʼ��ģ��
		FreeQueryContextVO context = (FreeQueryContextVO) getReport().getContextVo();
		if (context == null)
			return;
		// ��������
		Object repIdObj = context.getAttribute(REPORT_PK);
		pk_report = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
		
//		pk_report = context.getContextId();
		m_userID = context.getCurUserID();
		FreeQueryTranceObj tranceObj = (FreeQueryTranceObj)context.getAttribute(FreeQueryContextVO.FREEQEURY_TRANS_OBJ);
		if (tranceObj != null && tranceObj.getQueryDefs() != null) {
			IFreeQueryModel queryDef = tranceObj.getQueryDefs()[0];
			if (queryDef != null)// ���ۺϲ�ѯ������ֱ�Ӵ�����������
				initReportDesigner(getReport(), queryDef, context, true, true);
		}
		// ���ɲ�ѯ�������ʱֱ����ʾ����
		if (getReport().getOperationState() == ContextVO.OPERATION_FORMAT) {
			m_bFormat = true;
		}
		//
		getReport().setOperationState(ContextVO.OPERATION_INPUT);
		AdhocPlugin adhocPlugin = (AdhocPlugin) getReport().getPluginManager().getPlugin(AdhocPlugin.class.getName());
		if (adhocPlugin != null) {
			adhocPlugin.setOperationState(ContextVO.OPERATION_INPUT);
		}

	}

	public boolean isFormat() {
		return m_bFormat;
	}

	public static void initReportDesigner(UfoReport report, IFreeQueryModel queryDef, FreeQueryContextVO context,
			boolean bCreateReport, boolean addQuery) {
		AdhocQueryModel queryModel = new AdhocQueryModel(queryDef);
		IMetaData[] flds = queryModel.getMetaDatas();
		AdhocPlugin adhocPlugin = (AdhocPlugin) report.getPluginManager().getPlugin(AdhocPlugin.class.getName());
		if (adhocPlugin != null) {
			boolean isFormatState = adhocPlugin.getModel().isFormatState();
			adhocPlugin.getModel().changeState(true, false);
			AdhocDescriptor desc = (AdhocDescriptor) adhocPlugin.getDescriptor();
			BINavigationPanel biNavigationPanel = desc.getBINavigationPanel();
			AdhocQueryModel[] existQuerys = adhocPlugin.getModel().getDataCenter().getAllQuerys();
			boolean bExist = false;
			if (existQuerys != null)
				for (int i = 0; i < existQuerys.length; i++) {
					AdhocQueryModel query = existQuerys[i];
					if (query.getQueryID().equals(queryDef.getID())) {
						existQuerys[i].setQueryModel(queryDef);
						bExist = true;
						break;
					}
				}
			if (bExist) {
				// ɾ�����ϵĽڵ�
				DefaultTreeModel treeModel = ((DefaultTreeModel) biNavigationPanel.getQueryModelTree().getModel());
				if(treeModel.getChildCount(treeModel.getRoot())>0)
					treeModel.removeNodeFromParent((MutableTreeNode)treeModel.getChild(treeModel.getRoot(), 0));
			}
			// ���뵽�������Դ
			IMetaData[] showFlds = getShowFlds(flds);
			biNavigationPanel.addFieldNode(queryDef, BaseReportUtil.convertMetaDataToRepField(queryDef.getID(),
					showFlds));
			if (bCreateReport && (queryDef instanceof MeasureQueryModelDef)) { // Ŀǰֻ��ָ���ѯ֧���Զ�������������
				MeasQueryExcutor measExcutor = (MeasQueryExcutor) queryModel.getExec();
				// ���ϸ��������
				IMetaData[] repFields = measExcutor.getDefaultReportFlds();
				desc.updateReportField(repFields, true);
				// ���ҳά���ֶ�����
				ArrayList<Object[]> pageFields = measExcutor.getDefaultPageFlds();
				if (pageFields != null) {
					for (Object[] pageInfo : pageFields) {
						AdhocPageDimField pageFld = new AdhocPageDimField(queryModel, (IMetaData) pageInfo[0]);
						if (pageInfo[1] != null)
							pageFld.setSelectedValue((IMember) pageInfo[1]);
						adhocPlugin.getModel().addPageDim(pageFld);
					}
				}
				// ���Ĭ�ϵĹ�������
				AdhocBaseAnalyzer filter = measExcutor.addDefaultFilter();
				if (filter != null)
					adhocPlugin.getModel().getDataCenter().getAnalyzerModel().addAnalyzer(0, filter);

			}
			if(addQuery){			// ����ģ���еĲ�ѯ�б�
				adhocPlugin.getModel().getDataCenter().addQuery(queryDef);
			}
			adhocPlugin.getModel().changeState(isFormatState, true);
		}
	}

	private static IMetaData[] getShowFlds(IMetaData[] allFlds) {
		ArrayList<IMetaData> al = new ArrayList<IMetaData>();
		for (IMetaData meta : allFlds) {
			if(meta instanceof DefaultReportField){
				if (((DefaultReportField)meta).isDefaultShow())
					al.add(meta);
			}else if (!meta.isKey())
				al.add(meta);
		}
		return al.toArray(new IMetaData[0]);
	}

	/**
	 * ��ȡ���ɲ�ѯģ�Ͷ����ʵ��
	 * 
	 * @param defName
	 * @return
	 */
	public IFreeQueryModel getFreeQueryDef(String defName) {
		return (IFreeQueryModel) getReport().getCellsModel().getExtProp(defName);
	}

	/**
	 * �������ɲ�ѯģ�Ͷ����ʵ��
	 * 
	 * @param defName
	 * @param def
	 */
	public void setFreeQueryDef(String defName, IFreeQueryModel def) {
		if (def == null) {
			getReport().getCellsModel().getExtProps().remove(defName);

		} else {
			getReport().getCellsModel().putExtProp(defName, def);
		}
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#shutdown()
	 */
	public void shutdown() {
		AdhocPlugin adhocPlugin = (AdhocPlugin) getReport().getPluginManager().getPlugin(AdhocPlugin.class.getName());
		if (adhocPlugin != null) {

			AdhocQueryModel[] existQuerys = adhocPlugin.getModel().getDataCenter().getAllQuerys();
			for (int i = 0; i < existQuerys.length; i++) {
				(new FreeRepDataService()).clearTempTables(existQuerys[i].getQueryModel());

			}
		}
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#store()
	 */
	/**
	 * @i18n miufo00302=�������ɲ�ѯ������������Ϊ��
	 * @i18n miufotableinput000005=����ʧ��
	 */
	public void store() {
		getReport().stopCellEditing();
		ReportSrv srv = new ReportSrv();
		try {
			AdhocPlugin adhoc = (AdhocPlugin) getReport().getPluginManager().getPlugin(AdhocPlugin.class.getName());
			if (adhoc == null)
				return;
			ReportVO saveReport = null;
			if (pk_report != null) {
				ValueObject[] vos = srv.getByIDs(new String[] { pk_report });
				if (vos != null && vos.length > 0)
					saveReport = (ReportVO) vos[0];
			}
			FreeQueryContextVO fContext = (FreeQueryContextVO) getReport().getContextVo();
			boolean bCreate = false;
			if (saveReport == null) {
				FreeQuerySaveDlg dlg = new FreeQuerySaveDlg(this.getReport(), fContext.getCurUserID(), fContext.getCurUnitID());
				if (dlg.showModal() != UfoDialog.ID_OK)
					return;
				saveReport = createNewReportVO(dlg.getReportInfo(), m_userID); // �������������Ϣ
				bCreate = true;
			}
			saveQueryDef(adhoc.getModel().getDataCenter(), saveReport.getReportcode(), saveReport.getReportname()); // �����ѯ
			int oldState = adhoc.getModel().getOperationState();
			adhoc.getModel().changeState(true, false);
			saveReport.setDefinition(adhoc.getModel()); // ���±���ģ��
			if (bCreate) {
				ValueObject[] vo2 = srv.create(new ReportVO[] { saveReport });// ����������棨������
				if (vo2 != null && vo2.length > 0) {
					pk_report = vo2[0].getPrimaryKey();
					getReport().getContextVo().setContextId(pk_report);
				}
			} else {
				srv.update(new ReportVO[] { saveReport });// ����������棨�޸ģ�
			}
			AppDebug.debug("�������ɲ�ѯ������������Ϊ��" + pk_report);
			adhoc.getModel().changeState(oldState == ContextVO.OPERATION_FORMAT, false);
			setDirty(false);
		} catch (Exception e) {
			AppDebug.debug(e);
			JOptionPane.showMessageDialog(getReport(), StringResource.getStringResource("miufotableinput000005"));
		}
	}

	private ReportVO createNewReportVO(String[] repInfo, String userID) {
		ReportVO saveReport = new ReportVO();
		saveReport.setReportcode(repInfo[0]);
		saveReport.setReportname(repInfo[1]);
		saveReport.setType(ReportResource.INT_REPORT_FREE);
		saveReport.setNote(repInfo[2]);
		saveReport.setPk_folderID(repInfo[3]);
		saveReport.setOwnerid(userID);
		return saveReport;
	}

	/**
	 * @i18n uiufofurl530128=���ɲ�ѯ
	 */
	private void saveQueryDef(AdhocDataCenter dataCenter, String repCode, String repName) throws Exception {
		AdhocQueryModel query = dataCenter.getCurrQuery();// TODO Ŀǰ�ǵ�����Դ
		if (query == null)
			return;
		if (!(query.getQueryModel() instanceof MeasureQueryModelDef)) {
			// �Ǵ����͵Ĳ�ѯĿǰ��֧�ֱ༭�ʹ���
			return;
		}
		// ������ϵͳԤ�á��Ĳ�ѯĿ¼
		ResMngDirVO sysFolder = ResMngDirBO_Client.loadDirByPK(FREE_QUERYFOLDER_ID, modulePK);
		if (sysFolder == null)
			CodeQueryCreater.createSysQueryFolder(FREE_QUERYFOLDER_ID, StringResource.getStringResource("uiufofurl530128"), m_userID);

		String queryID = query.getQueryID();
		QueryModelVO[] qms = QueryModelSrv.getByIDs(new String[] { queryID });
		QueryModelVO newQueryVO = null;
		if (qms != null && qms.length > 0) {
			newQueryVO = qms[0];
		}
		MeasQueryExcutor excutor = (MeasQueryExcutor) query.getExec();

		if (newQueryVO == null) {// ִ����������ѯ
			String queryType = excutor.getQMD().getForgeQueryModel().getType();
			newQueryVO = createNewQueryModelVO(queryID, repCode, repName, queryType);
			QueryModelBO_Client.insert(newQueryVO, excutor.getQMD(), DataManageObjectUFBI.UFBI_DATASOURCE);
		} else {// ��ѯ����ĸ���
			QueryModelBO_Client.updateQmd(queryID, excutor.getQMD(), DataManageObjectUFBI.UFBI_DATASOURCE);
		}
	}

	private QueryModelVO createNewQueryModelVO(String queryID, String queryCode, String queryName, String queryType) {
		QueryModelVO queryModelVO = new QueryModelVO();
		// queryModelVO.setID(IDMaker.makeID(20));
		queryModelVO.setID(queryID);
		queryModelVO.setPk_folderID(FREE_QUERYFOLDER_ID);
		queryModelVO.setName(queryName);
		queryModelVO.setQuerycode(queryCode);
		queryModelVO.setOwneruser(UserInfoVO.SYSINIT_USERID);

		queryModelVO.setPk_datasource(DataManageObjectUFBI.UFBI_DATASOURCE);
		if(queryType != null)
			queryModelVO.setType(queryType);
		else
			queryModelVO.setType(BIQueryConst.TYPE_BUSIMODEL);
		return queryModelVO;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#isDirty()
	 */
	public boolean isDirty() {
		// TODO �Զ����ɷ������
		return false;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getSupportData()
	 */
	public String[] getSupportData() {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataRender(java.lang.String)
	 */
	public SheetCellRenderer getDataRender(String extFmtName) {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDataEditor(java.lang.String)
	 */
	public SheetCellEditor getDataEditor(String extFmtName) {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.table.UserActionListner#actionPerform(com.ufsoft.table.UserUIEvent)
	 */
	public void actionPerform(UserUIEvent e) {
		// TODO �Զ����ɷ������

	}

	/*
	 * @see com.ufsoft.table.Examination#isSupport(int, java.util.EventObject)
	 */
	public String isSupport(int source, EventObject e) throws ForbidedOprException {
		// TODO �Զ����ɷ������
		return null;
	}

	/*
	 * @see com.ufsoft.report.plugin.IPlugIn#getDescriptor()
	 */
	public IPluginDescriptor createDescriptor() {
		m_desc = new FreeRepDescriptor(this);
		return m_desc;
	}

	public FreeRepModel getModel() {
		if (m_model == null) {
			m_model = new FreeRepModel("");
		}
		return m_model;
	}

	public int getOperationState() {
		return this.getReport().getOperationState();
	}

	public void setOperationState(int operationState) throws BIException {
		if (getModel() != null) {
			getModel().setOperationState(operationState, ((BIContextVO) getReport().getContextVo()).getReportPK(),
					((BIContextVO) getReport().getContextVo()).getCurUserID());
		}
		getReport().setOperationState(operationState);
	}
}
  