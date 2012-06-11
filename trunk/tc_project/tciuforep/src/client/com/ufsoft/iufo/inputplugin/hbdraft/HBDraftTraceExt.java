package com.ufsoft.iufo.inputplugin.hbdraft;
import com.ufida.iufo.pub.tools.AppDebug;

import java.util.Map;

import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

public class HBDraftTraceExt  extends AbsActionExt implements IUfoContextKey{

	private UfoReport _report;
	
	public static String HBDRAFT_TRACE_FMT = "hbdraft_tracefmt";
	public static String DRAFT_UNITCOL = "unitCol";
	public static String DRAFT_VOUCHCOL = "vouchCol";
	public static String DRAFT_MEETDATACOL = "meetDataCol";
	

    /**
     * @param report
     */
	public HBDraftTraceExt(UfoReport report) {
        _report = report;
    }
	@Override
	public UfoCommand getCommand() {
			Map map =  getSelectedCellParaMap(_report);
			if(map == null)
				return null;
			String strUnitPK = (String)map.get("unitPK");
			IRepDataParam hbParam = getRepDataParam();
			String aloneId = hbParam.getAloneID();

			if(isTraceSubHBDraft()){
				hbParam.setAloneID(getAloneIdByUnitId(aloneId, strUnitPK,HBBBSysParaUtil.VER_HBBB));
				hbParam.setOperUnitPK(strUnitPK);
				return new HBDraftCmd(_report,hbParam);
			}else if(isTraceVouch()){
				return new HBDraftTraceVouchCmd(_report);
			} 
//			if(isTraceMeetData()){
//				return new HBDraftTraceMeetDataCmd(_report);
//			}
			else{
				return new HBDraftTraceCmd(_report);
			}
 	}
	
	private IRepDataParam getRepDataParam(){
		RepDataParam repDataParam = new RepDataParam();
		
		TableInputContextVO context = (TableInputContextVO)_report.getContextVo();
		Object tableInputTransObj = context.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
	
//		TableInputTransObj obj = context.getInputTransObj();
		IRepDataParam hbParam = inputTransObj.getRepDataParam();
		
		repDataParam.setAloneID(hbParam.getAloneID());
		repDataParam.setTaskPK(hbParam.getTaskPK());
		repDataParam.setReportPK(hbParam.getReportPK());
		repDataParam.setOperUnitPK(hbParam.getOperUnitPK());
		
		return repDataParam;
	}
	
	
	@Override
	public boolean isEnabled(java.awt.Component focusComp) {
		Map map = getSelectedCellParaMap(_report);
		if (map == null)
			return false;
		String colType = (String) map.get("colType");
		boolean isEnable = colType != null && (colType.equals(HBDraftTraceExt.DRAFT_UNITCOL)||colType.equals(HBDraftTraceExt.DRAFT_VOUCHCOL)||colType.equals(HBDraftTraceExt.DRAFT_VOUCHCOL));
			
//		boolean isAutoDXVouch = colType != null && colType.equals(HBDraftTraceExt.DRAFT_VOUCHCOL)&&;

		return isEnable;
	}
	@Override
	public Object[] getParams(UfoReport container) {
		return null;
	}

	/**
	 * @i18n uiuforep00116=查看数据来源
	 */
	@Override
	public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
//        String menuName = "查看数据来源";
//        if(isTraceSubHBDraft())
//        	menuName = "查看下级工作底稿";
        uiDes1.setName(MultiLang.getString("uiuforep00116"));
        uiDes1.setPaths(new String[]{MultiLang.getString("data")});//"数据"
        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
        uiDes1.setPaths(new String[]{});
        uiDes1.setPopup(true);
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
	public static String getAloneIdByUnitId(String aloneId,String unitId,int ver){
		MeasurePubDataVO pubdata = null;
		String retAloneId = null;
		try {
			pubdata = MeasurePubDataBO_Client.findByAloneID(aloneId);
		
		pubdata.setUnitPK(unitId);
		pubdata.setKeywordByPK(KeyVO.CORP_PK, unitId);
		pubdata.setAloneID(null);
		pubdata.setVer(ver);
		if(ver!= HBBBSysParaUtil.VER_HBBB)
			pubdata.setFormulaID(null);
		
		pubdata = MeasurePubDataBO_Client.findByKeywords(pubdata);
		if(pubdata!=null)
			retAloneId = pubdata.getAloneID();
		} catch (Exception e) {
			AppDebug.debug(e);
		}
		
		return retAloneId;
	}
	
	
	private boolean isTraceSubHBDraft(){
		Map map = getSelectedCellParaMap(_report);
		String strHbDraft = (String)map.get("ver");
		boolean bHbDraft = strHbDraft!=null && strHbDraft.equals("hb");
		return bHbDraft;
	}
	
	private boolean isTraceVouch(){
		Map map = getSelectedCellParaMap(_report);
		String strColType = (String)map.get("colType");
		return strColType!=null && strColType.equals(HBDraftTraceExt.DRAFT_VOUCHCOL);
	}
	
	private boolean isTraceMeetData(){
		Map map = getSelectedCellParaMap(_report);
		String strColType = (String)map.get("colType");
		return strColType!=null && strColType.equals(HBDraftTraceExt.DRAFT_MEETDATACOL);
	}
	
	public static Map getSelectedCellParaMap(UfoReport report){
		 CellPosition anchorPos = report.getCellsModel().getSelectModel().getAnchorCell();
			Map map =  (Map)report.getCellsModel().getBsFormat(anchorPos, HBDRAFT_TRACE_FMT);
			return map;
	}

}
 