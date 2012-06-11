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
 * ����: Ϊ��������ṩһ�鸨�����߷�����
 * ��������:(2006-10-12 04:59:06)
 * @author chxiaowei
 */
public class ReportCalUtil implements IUfoContextKey{
	private static Logger logger = Logger.getLogger(ReportCalUtil.class);
	
	/**��������*/
    public final static int ALL_FORMULA = 0; //ȫ������
    public final static int AREA_FORMULA = 1; //��������ʽ
    
    /**
     * ��¼�ؼ��ֶ����������Ӧ�Ĺ�ʽ���ݶ���,key=�ؼ��ֶ������� value=������
     * ��ʽ���ݶ���FormulaVO����ʽ�����б��������ȷ������ʽ�ͽ�����������⹫
     * ʽ�����ͼ���ʱ�Ķ�ν�����
     */
	private Map<IArea, FormulaVO> m_areaReferred = null;
	
    /**����ʽ�������*/
    private UfoAreaFormulaProxy m_cmdExecutor = null;
    
    /**����Ԫ��ʽ������**/
    private UfoFormulaList m_AreaFormulaList = null;
    
    /**��ǰ����**/
    private CellsModel m_cellsModel = null;
    private Context m_ctx = null;
    
    /**
     * ��ǰ�������ݵ�AloneID
     */
    private String m_strAloneID=null;
    
    /**
     * ��ǰ����pk
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
    	
    	//���칫ʽ��ͬʱ����ؼ�������ͬ�ؼ������ݶ���
    	initAreaFormulaList(m_cmdExecutor);
    }

    /**
	 * ���ܣ��������ʽ�������
	 * @return��cmdExecutor
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
	 * ���ܣ���ʼ������ʽ���㻷����
	 * @return��calEnv
	 */
	private AreaExprCalcEnv initAreaExprCalcEnv(){
		AreaExprCalcEnv calEnv = new AreaExprCalcEnv();
		calEnv.setDataChannel(new ReportDataChannel(m_cellsModel));
		calEnv.setExEnv(CommonExprCalcEnv.EX_CALC_NOTCALCMSELEC,
				CommonExprCalcEnv.EX_VALUE_ON); //������mselect����
		return calEnv;
	}

	/**
	 * ���ܣ����칫ʽ��ͬʱ����ؼ�������ͬ�ؼ������ݶ���
	 *   <p>1.Ԥ��������ʽ����ӵ������У����칫ʽ����
	 *      2.��������ʽ����������ʽ���ݶ���</p>
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
	 * ���ܣ��Ե�Ԫ��ʽ�����﷨��飬���湫ʽ��ʽ���������<code>FormulaVO</code>�����У�����ö���
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
	 * ���ܣ���Ӹ�����ʽ���ڵ���������������б�,������뵽��ʽ���С�
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
//     * ���ܣ������������͵ĵ�Ԫ��ʽ����ܹ�ʽʱ,�õ����Ӧ������.
//	 * ��Ԫ��ʽҲ�᷵�ص�����Ԫ��ɵ�����.
//	 * @param  cellPos ��Ԫλ��.
//	 * @return ��Ӧ������
//	 */
//	private AreaPosition getAreaExtendByCellPos(CellPosition cellPos) {
//	    FormulaFmt fcvo = (FormulaFmt)m_cellsModel.getBsFormat(cellPos, FormulaFmt.EXT_FMT_FORMULAINPUT);
//	    
//	    //���û�б���Ԫû�ж��幫ʽ,�򷵻�null
//	    if(fcvo == null) return null;
//	    
//	    //�������͵Ĺ�ʽ,�����㷨�����Ż�Ϊ�ӵ�ǰ��Ԫ����������
//	    //��Ϊ�迼����ϵ�Ԫ�������ϵ�Ԫ��ȡ����Ϻ�����.
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
     * ���ܣ�ִ�б�������ʽ���㡣
     * @param area ��ǰ�༭���������������㣬null��ʾ�������
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
	 * ���ܣ�ִ��ȫ��ʽ���㣬���������ģ�Ͷ����ȫ���������ȷ�Ĺ�ʽ��
	 * @return
	 */
	private void calcFullTableFml() throws CmdException {
		for(IArea oriArea : m_areaReferred.keySet()){
			FormulaVO fml = m_areaReferred.get(oriArea);
			IArea realArea = DynAreaOperUtil.getRealArea(oriArea, m_cellsModel);
			//�����ʽ�ڶ�̬���У������ö�̬�����㻷��
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
	 * ���ܣ�����ָ���������õ�����ʽ��
	 * @param area ֵ���༭�ĵ�Ԫλ��
	 * @throws CmdException
	 */
	private void calcRefAreaFml(IArea area) throws CmdException {
		if(area == null) return;
		
		area = DynAreaOperUtil.getFormatArea(area, m_cellsModel);
		Vector<IArea> vecRefAreaList = m_AreaFormulaList.getDirectRefForm(area);
		
		for (int i = 0; i < vecRefAreaList.size(); i++) {
			try {
				//�����ʽ�ڶ�̬���У������ö�̬�����㻷��
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
	 * ��ʽ�ڶ�̬��ʱ�������ö�̬�����㻷��
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
	 * �����̬���ļ��㻷������
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
		 * ������ϵ�Ԫ,m_areaReferred��ֻ��¼�׵�Ԫkey,����ʽ���м�¼��ϵ�Ԫ�����������Դ˴����⴦��
		 */
		FormulaVO oFormula = m_areaReferred.get(areaKey);
		if (oFormula == null && areaKey.isCell() == false) {
			// ������ϵ�Ԫ���
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
