package com.ufsoft.iufo.fmtplugin.dataprocess;
import java.util.Hashtable;
import java.util.Vector;

import com.ufida.dataset.Context;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessSetter;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.FieldMapUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupAggregateDefDlg;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupFormulaVO;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupLayingDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.IDataProcessType;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.ContextVO;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.extfunc.GAggrFuncDriver;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;

public class GroupStatDataProcessCmd extends AbsDataProcessCmd implements IUfoContextKey{

	protected boolean excuteImpl(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFlds) {
		String strRepId = getReport().getContextVo().getAttribute(REPORT_PK) == null ? null : (String)getReport().getContextVo().getAttribute(REPORT_PK);
		
//        String strRepPK = getReport().getContextVo().getContextId();
        Object onServerObj = getReport().getContextVo().getAttribute(ON_SERVER);
        boolean bOnServer = onServerObj == null ? false :(Boolean)onServerObj;
        return generateGroupAggregate(dynAreaCell, vecAllDynAreaDPFlds, strRepId, bOnServer);
	}
    private boolean generateGroupAggregate(DynAreaCell dynAreaCell, Vector<DataProcessFld> vecAllDynAreaDPFld, String strRepPK,
			boolean bOnServer) {

		// 准备好区域数据处理对象及其成员:数据处理定义,数据处理区域
		AreaDataProcess areaDataProcess = getDynAreaModel().getDataProcess(dynAreaCell.getDynAreaVO().getDynamicAreaPK());
		DataProcessDef dataProcessDef = areaDataProcess.getDataProcessDef();
		if (!(dataProcessDef instanceof GroupLayingDef)) {
			// 提示，或者已经让菜单Disabled
			return false;
		}
		// 计算当前选中的相对单元
		CellPosition relateCell = DataProcessUtil.getRelateCell(getCellsModel().getSelectModel().getAnchorCell(),
				dynAreaCell.getArea().getStart());
		if (areaDataProcess.getTableArea().containsDetailArea().getWholeAreaPos().contain(relateCell)) {
			// 提示（只能在分组分层的表头表尾区域定义分组统计函数），或者已经让菜单Disabled
			return false;
		}
		Vector<GroupFormulaVO> groupFormulaList = areaDataProcess.getGroupFormulaList();
		GroupFormulaVO curGroupFormulaVO = null;
		if (groupFormulaList != null) {
			for(int i=0;i<groupFormulaList.size();i++){
				GroupFormulaVO each = (GroupFormulaVO) groupFormulaList.get(i);
				if(each.getPos().equals(relateCell)){
					curGroupFormulaVO = each;
				}
			}			
		} else {
			groupFormulaList = new Vector<GroupFormulaVO>();
			areaDataProcess.setGroupFormulaList(groupFormulaList);
		}

		// 设置数据处理定义，和数据处理区域格式
		// 1）分组统计函数界面设置
		GroupFormulaVO returnGroupFormulaVO = null;
		if (curGroupFormulaVO == null) {
			returnGroupFormulaVO = new GroupFormulaVO();
			returnGroupFormulaVO.setPos(relateCell);
			returnGroupFormulaVO.setType(GroupFormulaVO.GROUPFORMULA_GROUP_AGG); // 分组公式
		} else {
			returnGroupFormulaVO = (GroupFormulaVO) curGroupFormulaVO.clone();
		}
		DataProcessSrv dataProcessSrv = new DataProcessSrv((ContextVO)getReport().getContextVo(),getCellsModel(),true);
		// 得到Hash(mapName,,指标/关键字的数据类型（对应指标数据类型）)
		Hashtable<String, Integer> hashDPFldTypes = DataProcessUtil.getMeasTypes(dynAreaCell.getDynAreaVO().getDynamicAreaPK(),
				dataProcessSrv, vecAllDynAreaDPFld, strRepPK, bOnServer);
		returnGroupFormulaVO = DataProcessSetter.generateGroupAggregate(getReport(), vecAllDynAreaDPFld, hashDPFldTypes,
				returnGroupFormulaVO);

		// 分组函数计算方式改变，不再需要分组信息
		// 2）计算并设置分组信息
		/*
		 * String[] strcGroupLayingInfos =
		 * DataProcessUtil.calGourpLayingInfos(areaDataProcess.getTableArea(),
		 * relateCell); curGroupFormulaVO.setGroupInfos(strcGroupLayingInfos);
		 */
		// 回设分组函数
		boolean bDirty = false;
		if (returnGroupFormulaVO != null) {
			// 若选择的是“无”公式，则删除该公式
			if (returnGroupFormulaVO.getFormulaContent().indexOf(GroupAggregateDefDlg.FUNCNAME_NONE) < 0) {
				// 解析公式
				IParsed expr = null;

				UfoFmlExecutor proxy = getFormulaHandler();
	
				try {
					proxy.registerFuncDriver(new GAggrFuncDriver((UfoCalcEnv) proxy.getExecutorEnv()));
					// 环境对象中设置动态区域的关键字、指标映射
					Context contextVO = getContextVO();
					Object repIdObj = contextVO.getAttribute(REPORT_PK);
					String strReportPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null;
					
					(proxy.getExecutorEnv()).setFieldMaps(FieldMapUtil.getFieldMaps(dynAreaCell.getDynAreaPK(),
							dataProcessSrv,strReportPK, proxy.getExecutorEnv()));
					try {
						expr = proxy.parseExpr(returnGroupFormulaVO.getFormulaContent());
						returnGroupFormulaVO.setLet(expr);
					} catch (ParseException ex) {
						AppDebug.debug(ex);//@devTools ex.printStackTrace(System.out);
					}
					if (curGroupFormulaVO != null) {
						groupFormulaList.remove(curGroupFormulaVO);
					} 
					groupFormulaList.add(returnGroupFormulaVO);
					bDirty = true;

					proxy.unRegisterFuncDrivers(GAggrFuncDriver.class.getName());
				} catch (Exception ex) {
					AppDebug.debug(ex);//@devTools ex.printStackTrace(System.out);
				}
			} else {
				if (curGroupFormulaVO != null) {
					groupFormulaList.remove(returnGroupFormulaVO);
				}
				bDirty = true;
			}

			//动态区域的大小范围小于生成的数据处理区域大小范围时，
			//需要在动态区域的最下行或最右列，增加行或类
			//	分组统计函数不改变数据处理区域的大小
		}
		return bDirty;
	}
	private UfoFmlExecutor getFormulaHandler() {
		FormulaModel formulaModel = FormulaModel.getInstance(getCellsModel());
		return formulaModel.getUfoFmlExecutor();
		
//		FormulaDefPlugin pi = (FormulaDefPlugin) getReport().getPluginManager().getPlugin(
//				FormulaDefPlugin.class.getName());
//		UfoFmlExecutor handler=pi.getFmlExecutor();
//		return handler;		
	}
	
    @Override
	protected void adjustDynAreaFormat(DynAreaCell dynAreaCell, AreaPosition oldArea) {
		// TODO Auto-generated method stub		
	}

    @Override
	protected int getDataProcessType() {
		return IDataProcessType.PROCESS_GROUP_AGGREGEATE;
	}
}
