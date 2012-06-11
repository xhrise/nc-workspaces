package com.ufsoft.iufo.inputplugin.hbdraft;

import java.awt.event.ActionEvent;

import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.hbbb.pub.Util;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.MeasTraceInfo;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.iufo.resource.StringResource;

public class UfoHBDraftTraceSrcAction extends AbstractPluginAction{

	@Override
	public void execute(ActionEvent e) {
		
		HBDraftTraceVO trace = getSelTraceVO();
		if(trace.getColType().equals(HBDraftViewer.COLUMN_REPDATA)){
			traceMeasure(trace);
		}else if(trace.getColType().equals(HBDraftViewer.COLUMN_VOUCHDATA)){
			traceVouch(trace);
		}
	}
	
	public boolean isEnabled() {
		return getSelTraceVO()!= null;
	}
	/**
	 * @i18n miufotableinput0003=查看来源
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(MultiLang.getString("data"), "traceDataGroup");
		descriptor.setName(StringResource.getStringResource("miufotableinput0003"));//"合并底稿"
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.POPUPMENU});
		return descriptor;
	}
	
	/**
	 * 追踪报表数据
	 * @param trace
	 */
	private void traceMeasure(HBDraftTraceVO trace){
		MeasurePubDataVO pubdata = getTraceMeasurePubData(trace);
		IContext context = getEditor().getContext();
		MeasTraceInfo traceInfo = new MeasTraceInfo(trace.getMeasurePK(), pubdata.getKeywords(), trace.getReportId(), pubdata
						.getAloneID(), true);
		traceInfo.setGeneryQuery(true);
		traceInfo.setShowRepTree(true);
		String strOrgPK = context.getAttribute(IUfoContextKey.ORG_PK) == null ? null : (String)context.getAttribute(IUfoContextKey.ORG_PK);
		String unitId = context.getAttribute(IUfoContextKey.LOGIN_UNIT_ID) == null ? null : (String)context.getAttribute(IUfoContextKey.LOGIN_UNIT_ID);
		String strOperUserId = (String)context.getAttribute(IUfoContextKey.CUR_USER_ID);
		traceInfo.setStrLoginUnitID(unitId);
		traceInfo.setStrOrgPK(strOrgPK);
		traceInfo.setStrOperUserPK(strOperUserId);
		
		String taskId=(String)context.getAttribute(IUfoContextKey.TASK_PK);
		String langCode=(String)context.getAttribute(IUfoContextKey.CURRENT_LANG);
		traceInfo.setStrLangCode(langCode);
		traceInfo.setStrTaskId(taskId);
		if(context.getAttribute(IUfoContextKey.DATA_SOURCE) instanceof DataSourceVO){
			 traceInfo.setDataSource((DataSourceVO)context.getAttribute(IUfoContextKey.DATA_SOURCE));
		}else if(context.getAttribute(IUfoContextKey.DATA_SOURCE) instanceof DataSourceInfo){
			 traceInfo.setDataSource((DataSourceInfo)context.getAttribute(IUfoContextKey.DATA_SOURCE));
		}
	     WindowNavUtil.traceZiorMeasure(getMainboard(),traceInfo,true);
	}
	/**
	 * 追踪凭证
	 * @param trace
	 * @i18n uiufofurl0326=抵销分录查询
	 */
	private void traceVouch(HBDraftTraceVO trace){
		HBDraftViewer editor = getEditor();
		String strViewerId = getVouchViewerId(trace);
		HBDraftVouchViewer vouchViewer = (HBDraftVouchViewer) editor.openView(HBDraftVouchViewer.class.getName(), strViewerId, editor.getId());
		vouchViewer.setTitle(StringResource.getStringResource("uiufofurl0326"));
		vouchViewer.initCellsModel(trace);
	}
	
	private String getVouchViewerId(HBDraftTraceVO trace){
		return trace.getTaskId()+trace.getAloneId()+ trace.getMeasurePK();
	}
	/**
	 * 得到所选单元格追踪的业务属性
	 * @return
	 */
	private HBDraftTraceVO getSelTraceVO(){
		CellsModel model = getCellsModel();
		if(model == null)
          return null;
		CellPosition cellPos  = model.getSelectModel().getSelectedArea().getStart();
		Object obj = model.getBsFormat(cellPos, HBDraftViewer.HBDRAFT_TRACE_FMT);
		if(obj != null)
			return (HBDraftTraceVO) obj;
		else
			return null;
	}

	private HBDraftViewer getEditor(){
		Viewer viewer = getCurrentView();
		if(!(viewer instanceof HBDraftViewer))
			return null;
		else
			return (HBDraftViewer) viewer;
	}
	/**
	 * 得到追踪的measurepubdata
	 * @param trace
	 * @return
	 */
	private MeasurePubDataVO getTraceMeasurePubData(HBDraftTraceVO trace){
		String strAloneId = trace.getAloneId();
		String strUnitId = trace.getUnitId();
		MeasurePubDataVO pubdata = null;
		try {
			pubdata = MeasurePubDataBO_Client.findByAloneID(strAloneId);
		
		pubdata.setAloneID(null);
		pubdata.setKeywordByPK(KeyVO.CORP_PK, strUnitId);
		pubdata.setVer(trace.getVer());
		pubdata.setFormulaID(null);
		pubdata.setUnitPK(strUnitId);

		if(trace.getVer() == HBBBSysParaUtil.VER_HBBB && Util.isHBRepDataRelatingWithTask()){
			pubdata.setFormulaID(trace.getTaskId());
		}
		pubdata = MeasurePubDataBO_Client.findByKeywords(pubdata);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		return pubdata;
	}

	private CellsModel getCellsModel() {
		HBDraftViewer editor = getEditor();
		return editor!=null ? editor.getCellsModel() : null;

	}
	
	

}
 