package com.ufsoft.iufo.inputplugin.autocalc;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ufida.dataset.Context;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.formula.flist.UfoFormulaList;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.formula.FormulaFmt;
import com.ufsoft.iufo.inputplugin.inputcore.InputContextVO;
import com.ufsoft.report.ContextVO;
import com.ufsoft.script.AreaFormulaUtil;
import com.ufsoft.script.UfoAreaFormulaProxy;
import com.ufsoft.script.base.CommonExprCalcEnv;
import com.ufsoft.script.base.FormulaVO;
import com.ufsoft.script.datachannel.IUFODynAreaDataParam;
import com.ufsoft.script.exception.CmdException;
import com.ufsoft.script.exception.CreateProxyException;
import com.ufsoft.script.exception.ParseException;
import com.ufsoft.script.expression.AreaExprCalcEnv;
import com.ufsoft.script.expression.UfoCmdLet;
import com.ufsoft.table.AbstractArea;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

/**
 * 功能: 为报表计算提供一组辅助工具方法。
 * 创建日期:(2006-10-12 04:59:06)
 * @author chxiaowei
 */
public class ReportCalUtil implements IUfoContextKey{
	private static Logger logger = Logger.getLogger(ReportCalUtil.class);
	
	/**计算类型*/
    public final static int ALL_FORMULA = 0; //全部计算
    public final static int AREA_FORMULA = 1; //计算区域公式
    
    /**
     * 记录关键字定义区域及其对应的公式数据对象,key=关键字定义区域 value=此区域
     * 公式数据对象FormulaVO。公式对象中保存解析正确的区域公式和解析结果。避免公
     * 式分析和计算时的多次解析。
     */
	private Map<IArea, FormulaVO> m_areaReferred = null;
	
    /**区域公式计算代理*/
    private UfoAreaFormulaProxy m_cmdExecutor = null;
    
    /**主表单元公式计算链**/
    private UfoFormulaList m_AreaFormulaList = null;
    
    /**当前报表**/
    private CellsModel m_cellsModel = null;
    private Context m_ctx = null;
    
    /**
     * 当前报表数据的AloneID
     */
    private String m_strAloneID=null;
    
    /**
     * 当前报表pk
     */
    private String m_strRepPK=null;
    
 
    public ReportCalUtil(String strRepPK, CellsModel cellsModel, Context ctx){
    	m_strRepPK=strRepPK;
    	m_ctx = ctx;
    	init(cellsModel);
    }
    public ReportCalUtil(CellsModel cellsModel, ContextVO ctx){
    	this(null, cellsModel, ctx);
    }
    public String getReportPK(){
    	return m_strRepPK;
    }
    
    public String getAloneID(){
    	return m_strAloneID;
    }
    
    public void setAloneID(String strAloneID){
    	m_strAloneID=strAloneID;
    }

    private void init(CellsModel cellsModel){
    	m_cellsModel = cellsModel;
    	m_cmdExecutor = getAreaFormulaProxy();
    	
    	//构造公式链同时缓存关键字区域同关键字数据对象
    	initAreaFormulaList(m_cmdExecutor);
    }

    /**
	 * 功能：获得区域公式计算代理。
	 * @return　cmdExecutor
	 */
	private UfoAreaFormulaProxy getAreaFormulaProxy(){
		UfoAreaFormulaProxy cmdExecutor = null;
		
    	try {
			AreaExprCalcEnv calEnv = initAreaExprCalcEnv();
			cmdExecutor = new UfoAreaFormulaProxy(calEnv);
		} catch (CreateProxyException e) {
			logger.error(e);
		}
		return cmdExecutor;
    }
	
	/**
	 * 功能：初始化区域公式计算环境。
	 * @return　calEnv
	 */
	private AreaExprCalcEnv initAreaExprCalcEnv(){
		AreaExprCalcEnv calEnv = new AreaExprCalcEnv();
		calEnv.setDataChannel(new ReportDataChannel(m_cellsModel));
		calEnv.setExEnv(CommonExprCalcEnv.EX_CALC_NOTCALCMSELEC,
				CommonExprCalcEnv.EX_VALUE_ON); //不计算mselect函数
		return calEnv;
	}

	/**
	 * 功能：构造公式链同时缓存关键字区域同关键字数据对象。
	 *   <p>1.预处理区域公式并添加到缓存中，构造公式链；
	 *      2.分析区域公式并缓存区域公式数据对象；</p>
	 *   
	 * @param cmdExecutor
	 * @return
	 */
	private void initAreaFormulaList(UfoAreaFormulaProxy cmdExecutor){
		m_AreaFormulaList = new UfoFormulaList(m_cellsModel);
		m_areaReferred = new HashMap<IArea, FormulaVO>();
		if(isIufoClientInput() != null && isIufoClientInput()){
			Hashtable<CellPosition, FormulaFmt> hashFormulaList = m_cellsModel.getBsFormats(FormulaFmt.EXT_FMT_FORMULAINPUT);
			IArea areaPosTmp = null;
			for(CellPosition cellPos:hashFormulaList.keySet()){
				FormulaFmt fcvo = (FormulaFmt)hashFormulaList.get(cellPos);
				if(fcvo.getFmlContent() == null)
					continue;
				areaPosTmp = cellPos;
				if(fcvo.isOneCellType() == false && fcvo.getAreaFmlPos() != null){
					areaPosTmp = AreaPosition.getInstance(fcvo.getAreaFmlPos());
				}
				
				areaPosTmp = DynAreaOperUtil.getFormatArea(areaPosTmp, m_cellsModel);
				FormulaVO fvo = new FormulaVO(null, fcvo.getFmlContent());
				setFormulaCmdLet(areaPosTmp, fvo);
				addFormulaToAreaFormulaList(areaPosTmp, AreaFormulaUtil.getAreaList(fvo.getLet()));
			}
		} else{
			Hashtable<AbstractArea, com.ufsoft.iufo.fmtplugin.formula.FormulaVO> hashFormulaList = getDynAreaModelUtil().getFormulaModel().getFormulaAllByType(true);
			IArea areaPosTmp = null;
			for(IArea areaPos:hashFormulaList.keySet()){
				com.ufsoft.iufo.fmtplugin.formula.FormulaVO fcvo = (com.ufsoft.iufo.fmtplugin.formula.FormulaVO)hashFormulaList.get(areaPos);
				if(fcvo.getFormulaContent() == null)
					continue;

				areaPosTmp = areaPos;
				//areaPosTmp = DynAreaOperUtil.getFormatArea(areaPosTmp, m_cellsModel);
				FormulaVO fvo = new FormulaVO(null, fcvo.getFormulaContent());
				setFormulaCmdLet(areaPosTmp, fvo);
				addFormulaToAreaFormulaList(areaPosTmp, AreaFormulaUtil.getAreaList(fvo.getLet()));
			}
		}
	}
	
	/**
	 * 功能：对单元公式进行语法检查，保存公式公式解析结果到<code>FormulaVO</code>对象中，缓存该对象。
	 * @param fvo
	 */
	private void setFormulaCmdLet(IArea area, FormulaVO fvo){
		try {
        	UfoCmdLet let = (UfoCmdLet)(m_cmdExecutor.parseFormula(area, fvo.getContent()));
        	fvo.setErrorFml(false);
        	fvo.setLet(let);
        	m_areaReferred.put(area, fvo);
        } catch(ParseException ex){
        	fvo.setErrorFml(true);
        	fvo.setLet(null);
        }
	}
	
	/**
	 * 功能：添加给定公式所在的区域和引用区域列表,将其加入到公式链中。
	 * @param area
	 * @param vect
	 */
	private void addFormulaToAreaFormulaList(IArea area, Vector<IArea> vect){
		if(vect == null || m_AreaFormulaList == null) return;
		if (m_AreaFormulaList.canAddToList(area, vect)) {
			List<IArea> list = new ArrayList<IArea>();
			for(IArea a :vect){
				list.add(a);
			}
        	m_AreaFormulaList.addFormula(area, list);
        }
	}
	
//	/**
//     * 功能：当是区域类型的单元公式或汇总公式时,得到其对应的区域.
//	 * 单元公式也会返回单个单元组成的区域.
//	 * @param  cellPos 单元位置.
//	 * @return 对应的区域
//	 */
//	private AreaPosition getAreaExtendByCellPos(CellPosition cellPos) {
//	    FormulaFmt fcvo = (FormulaFmt)m_cellsModel.getBsFormat(cellPos, FormulaFmt.EXT_FMT_FORMULAINPUT);
//	    
//	    //如果没有本单元没有定义公式,则返回null
//	    if(fcvo == null) return null;
//	    
//	    //区域类型的公式,这里算法不能优化为从当前单元向四周搜索
//	    //因为需考虑组合单元情况和组合单元刚取消组合后的情况.
//	    AreaPosition area = AreaPosition.getInstance(cellPos, cellPos);
//        Hashtable<CellPosition, FormulaFmt> ht = m_cellsModel.getBsFormats(fcvo.getFmlType());
//        for(CellPosition tmpPos:ht.keySet()){
//        	if (fcvo == ht.get(tmpPos)) {
//                area = area.getInstanceUnionWith(AreaPosition.getInstance(tmpPos, tmpPos));
//            }
//        }
//        return area;
//	}
	
	/**
     * 功能：执行报表区域公式计算。
     * @param area 当前编辑区域，用于联动计算，null表示整表计算
     * @throws ParseException
     * @throws CmdException
     */
	public void calcFormula(IArea area) throws CmdException {
		if(area == null){
			calcFullTableFml();
		} else{
			calcRefAreaFml(area);
		}
	}
	
	/**
	 * 功能：执行全表公式计算，计算该数据模型定义的全部经检查正确的公式。
	 * @return
	 */
	private void calcFullTableFml() throws CmdException {
		for(IArea oriArea : m_areaReferred.keySet()){
			FormulaVO fml = m_areaReferred.get(oriArea);
			IArea realArea = DynAreaOperUtil.getRealArea(oriArea, m_cellsModel);
			//如果公式在动态区中，则设置动态区计算环境
			if(DynAreaOperUtil.isInDynArea(m_cellsModel, realArea.getStart())){
				DynAreaCell dynAreaCell = DynAreaOperUtil.getDynAreaCellByPos(m_cellsModel, realArea.getStart());
				AreaPosition[] unitAreas = dynAreaCell.getUnitAreas();
				int nUnitAreas = unitAreas.length;
				int stepRow = -1, stepCol = -1;
				ReportDataChannel dataChannel = (ReportDataChannel)((AreaExprCalcEnv)m_cmdExecutor.getEnv()).getDataChannel();
				for(int i = 0; i < nUnitAreas; i++){
					stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ? 
							dynAreaCell.getDynAreaVO().getOriArea().getHeigth() : 0;
					stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ?
							0 : dynAreaCell.getDynAreaVO().getOriArea().getWidth();
					dataChannel.setDynAreaCalcParam(new IUFODynAreaDataParam(stepRow, stepCol, i, null, dynAreaCell.getDirection(), null));
					
					try {
						fml.getLet().exec(m_cmdExecutor.getEnv());          
					} catch (CmdException e){
						//throw e;
					}
					clearDynAreaCalcEnv();
				}
				
			} else{
				try {
					fml.getLet().exec(m_cmdExecutor.getEnv());          
				} catch (CmdException e){
					//throw e;
				}
			}
		}
		
	}
	
	/**
	 * 功能：计算指定区域引用的区域公式。
	 * @param area 值被编辑的单元位置
	 * @throws CmdException
	 */
	private void calcRefAreaFml(IArea area) throws CmdException {
		if(area == null) return;
		
		area = DynAreaOperUtil.getFormatArea(area, m_cellsModel);
		Vector<IArea> vecRefAreaList = m_AreaFormulaList.getDirectRefForm(area);
		
		for (int i = 0; i < vecRefAreaList.size(); i++) {
			try {
				//如果公式在动态区中，则设置动态区计算环境
				IArea refAreaPos = vecRefAreaList.get(i);
				setDynAreaCalcParam(refAreaPos);
				FormulaVO fml = getFmlByArea(refAreaPos);
				if(fml==null){
					continue;
				}
				fml.getLet().exec(m_cmdExecutor.getEnv());   
			} catch (CmdException e){
				logger.error(e);
			}
		}
		clearDynAreaCalcEnv();
		
	}
	
	/**
	 * 公式在动态区时，则设置动态区计算环境
	 * @param fmtAreaPos
	 */
	private void setDynAreaCalcParam(IArea fmtAreaPos){
		int unitAreaNum = -1, stepRow = -1, stepCol = -1;
		IArea realArea = (AreaPosition)DynAreaOperUtil.getRealArea(fmtAreaPos, m_cellsModel);
		DynAreaCell dynAreaCell = DynAreaOperUtil.getDynAreaCellByPos(m_cellsModel, realArea.getStart());
		ReportDataChannel dataChannel = (ReportDataChannel)((AreaExprCalcEnv)m_cmdExecutor.getEnv()).getDataChannel();
		if(dynAreaCell != null){
			unitAreaNum = dynAreaCell.getOwnerUnitAreaNum(realArea.getStart());
			stepRow = dynAreaCell.getDynAreaVO().isRowDirection() ? 
					dynAreaCell.getDynAreaVO().getOriArea().getHeigth() : 0;
			stepCol = dynAreaCell.getDynAreaVO().isRowDirection() ?
					0 : dynAreaCell.getDynAreaVO().getOriArea().getWidth();
			dataChannel.setDynAreaCalcParam(new IUFODynAreaDataParam(stepRow, stepCol, unitAreaNum, null, dynAreaCell.getDirection(), null));
		}
	}
	
	/**
	 * 清除动态区的计算环境参数
	 * @param formulaHandler
	 */
	private void clearDynAreaCalcEnv() {
		ReportDataChannel dataChannel = (ReportDataChannel)((AreaExprCalcEnv)m_cmdExecutor.getEnv()).getDataChannel();
		dataChannel.removeDynAreaCalcParam();
	}
	
	public void resetCellsModel(CellsModel newModel) {
		init(newModel);
	}
	
	public CellsModel getCellsModel() {
		return m_cellsModel;
	}
	
	public DynAreaModel getDynAreaModelUtil() {
		return DynAreaModel.getInstance(m_cellsModel);
	}
	
	private FormulaVO getFmlByArea(IArea areaKey) {
		/*
		 * 对于组合单元,m_areaReferred中只纪录首单元key,而公式链中记录组合单元完整区域，所以此处特殊处理
		 */
		FormulaVO oFormula = m_areaReferred.get(areaKey);
		if (oFormula == null && areaKey.isCell() == false) {
			// 处理组合单元情况
			ArrayList<CellPosition> cells = getSeperateCellPos(areaKey);
			for (CellPosition cellTemp : cells) {
				oFormula = m_areaReferred.get(cellTemp);
				if (oFormula != null) {
					break;
				}
			}
		}
		return oFormula;
	}
	 
	private static ArrayList<CellPosition> getSeperateCellPos(IArea area) {
		CellPosition startCell = area.getStart();
		CellPosition endCell = area.getEnd();
		ArrayList<CellPosition> list = new ArrayList<CellPosition>();
		int startRow = startCell.getRow();
		int startCol = startCell.getColumn();
		int endRow = endCell.getRow();
		int endCol = endCell.getColumn();
		for (int row = startRow; row <= endRow; row++) {
			for (int col = startCol; col <= endCol; col++) {
				CellPosition cellPos = CellPosition.getInstance(row, col);

				list.add(cellPos);
			}
		}
		return list;
	}

	private Boolean isIufoClientInput(){
		if(m_ctx instanceof InputContextVO){
			Object clientFlagObj = ((InputContextVO)m_ctx).getAttribute(CLIENT_FLAG);			
			return clientFlagObj == null ? null : Boolean.parseBoolean(clientFlagObj.toString());
		}
		return null;
	}
}
