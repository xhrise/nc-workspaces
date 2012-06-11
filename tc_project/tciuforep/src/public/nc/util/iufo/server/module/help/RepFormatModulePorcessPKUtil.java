package nc.util.iufo.server.module.help;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.KeywordCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.CodeCache;
import nc.pub.iufo.cache.base.IUFOCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasurePackVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.businessquery.ReportBusinessQuery;
import com.ufsoft.iufo.fmtplugin.businessquery.ReportQueryVO;
import com.ufsoft.iufo.fmtplugin.businessquery.ReportSelectFldVO;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFld;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DefaultDataProcessDef;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.FieldMap;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.FieldMapUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupLayingDef;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.fmtplugin.xml.PK2Code4Xml;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.print.PrintSet;

/**
 * 导入报表格式时，由于两个数据库之间指标、报表、代码、关键字、单位PK不相同，需要对报表格式模型中用到这些值的地方进行替换的工具类
 * @author weixl
 */
public class RepFormatModulePorcessPKUtil {
	/**
	 * 对关键字模型进行替换处理
	 * @param cellModel，报表格式模型
	 * @param report，报表VO
	 * @return,导入时的关键字PK与替换后的关键字PK的对照表
	 * @throws Exception
	 */
	public static Hashtable<String,String> processKeywordModel(CellsModel cellModel,ReportVO report) throws Exception{
		//需要返回的导入时的关键字PK与替换后的关键字PK的对照表
		Hashtable<String,String> hashKeyPKMap=new Hashtable<String,String>();
		
		KeywordModel keyModel=KeywordModel.getInstance(cellModel);
		KeywordCache keyCache=IUFOUICacheManager.getSingleton().getKeywordCache();
		KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
		
		//新建的关键字PK表，防止两个关键字用同一个PK
		Hashtable<String,Boolean> hashUsedKeyPK=new Hashtable<String,Boolean>();

		//处理关键字PK与关键字VO的对照表
		String[] strKeyPKs=(String[])keyModel.getKeyPK2VO().keySet().toArray(new String[0]);
		for(int i=0;i<strKeyPKs.length;i++){
			String strKeyPK=strKeyPKs[i];
			KeyVO key=(KeyVO)keyModel.getKeyPK2VO().get(strKeyPK);
			if (key==null)
				continue;		
			
			//判断系统中是否存在同名的关键字，此处判断只对私有关键字有意义，如果公有关键字，在系统中不存在，
			//则在导入时checkKey方法中就会报错
			KeyVO existKey=keyCache.getByName(key.getName(),key.isPrivate()?report.getReportPK():null);
			
			//如果存在此关键字，其KeywordModel中的关键字PK替换成系统中已经存的关键字的PK
			if (existKey!=null){
				hashKeyPKMap.put(key.getKeywordPK(),existKey.getKeywordPK());
				keyModel.getKeyPK2VO().remove(strKeyPK);
				keyModel.getKeyPK2VO().put(existKey.getKeywordPK(),key);
				key.setKeywordPK(existKey.getKeywordPK());
				key.setRepIdOfPrivate(existKey.getRepIdOfPrivate());
				hashUsedKeyPK.put(key.getKeywordPK(),Boolean.TRUE);
			}
		}		
		
		//对于新增的关键字，生成新的PK，并对keyModel中的keyPK2VO进行替换
		strKeyPKs=(String[])keyModel.getKeyPK2VO().keySet().toArray(new String[0]);
		for(int i=0;i<strKeyPKs.length;i++){
			String strKeyPK=strKeyPKs[i];
			KeyVO key=(KeyVO)keyModel.getKeyPK2VO().get(strKeyPK);
			if (key==null || key.isPrivate()==false)
				continue;
				
			KeyVO existKey=keyCache.getByName(key.getName(),report.getReportPK());
			if (existKey==null){
				//新的关键字，肯定是私有关键字
				String strNewKeyPK=getOnePK(hashUsedKeyPK,key.getKeywordPK(),20,keyCache);
				keyModel.getKeyPK2VO().remove(strKeyPK);
				keyModel.getKeyPK2VO().put(strNewKeyPK,key);
				
				hashKeyPKMap.put(key.getKeywordPK(),strNewKeyPK);	
				key.setKeywordPK(strNewKeyPK);
				key.setRepIdOfPrivate(report.getReportPK());				
			}
		}

		//以下是用得到的新旧关键字PK的对照表，对keyModel中的mainDisplayKeys、mainUndisplayKeys、
		//mainUndisplayKeyValues、subTableKeys中用到的关键字PK进行替换
		Hashtable hashMainDispKeys=keyModel.getMainDisplayKeys();
	    Enumeration keys=hashMainDispKeys.keys();
		while (keys.hasMoreElements()){
			Object objKey=keys.nextElement();
			String strKeyPK=(String)hashMainDispKeys.get(objKey);
			strKeyPK=hashKeyPKMap.get(strKeyPK);
			if (strKeyPK!=null)
				hashMainDispKeys.put(objKey,strKeyPK);
		}
	
		Vector vMainUnDispKeys=keyModel.getMainUndisplayKeys();
		for (int i=0;i<vMainUnDispKeys.size();i++){
			String strKeyPK=(String)vMainUnDispKeys.get(i);
			strKeyPK=hashKeyPKMap.get(strKeyPK);
			if (strKeyPK!=null)
				vMainUnDispKeys.set(i,strKeyPK);
		}

		HashMap hashMainUnDisplayKeyValues=keyModel.getMainUndisplayKeyValues();
		strKeyPKs=(String[])hashMainUnDisplayKeyValues.keySet().toArray(new String[0]);
		for (int i=0;i<strKeyPKs.length;i++){
			Object objVal=hashMainUnDisplayKeyValues.get(strKeyPKs[i]);
			String strKeyPK=hashKeyPKMap.get(strKeyPKs[i]);
			if (strKeyPK!=null)
				hashMainUnDisplayKeyValues.put(strKeyPK,objVal);
		}

		Hashtable hashSubKeys=keyModel.getSubTableKeys();
		keys=hashSubKeys.keys();
		while (keys.hasMoreElements()){
			Hashtable hashOneSubKeys=(Hashtable)hashSubKeys.get(keys.nextElement());
			if (hashOneSubKeys==null)
				continue;
			
			Enumeration subKeys=hashOneSubKeys.keys();
			while (subKeys.hasMoreElements()){
				Object objKey=subKeys.nextElement();
				String strKeyPK=(String)hashOneSubKeys.get(objKey);
				strKeyPK=hashKeyPKMap.get(strKeyPK);
				if (strKeyPK!=null)
					hashOneSubKeys.put(objKey,strKeyPK);
			}
		}
		
		//处理主表关键字组合PK，如果该关键字组合系统中已经存在，则使用已有的PK，否则创建新的关键字组合，得到新创建的PK
		KeyVO[] keyVOs=keyModel.getMainKeyVOs();
		KeyGroupVO keyGroup=new KeyGroupVO(keyVOs);
		KeyGroupVO existKeyGroup=kgCache.getPkByKeyGroup(keyGroup);
		if (existKeyGroup==null){
			existKeyGroup=(KeyGroupVO)kgCache.add(keyGroup);
		}
		keyModel.setMainKeyCombPK(existKeyGroup.getKeyGroupPK());
		
		return hashKeyPKMap; 
	}
	
	/**
	 * 导入报表格式时，有指标、关键字对应代码时，需要将其中的代码PK变化成系统中已经存在的代码PK
	 * @param cellsModel，报表格式模型 
	 * @param strMsg，校验时出错信息
	 * @throws Exception
	 */
	public static void processKeyMeasureCodeRef(CellsModel cellsModel,StringBuffer strMsg) throws Exception{
		KeywordModel keyModel=KeywordModel.getInstance(cellsModel);
		MeasureModel measModel=MeasureModel.getInstance(cellsModel);
		CodeCache codeCache=IUFOUICacheManager.getSingleton().getCodeCache();
		
		//对keyModel中的keyPK2VO中进行处理
		Hashtable hashKeyPK2VO=keyModel.getKeyPK2VO();
		KeyVO[] keys=(KeyVO[])hashKeyPK2VO.values().toArray(new KeyVO[0]);
		for (int i=0;i<keys.length;i++){
			//对KeyVO进行处理，判断代码是否存在，如果不存在，给出出错信息
			if (keys[i].getRef()!=null && keys[i].getRef().trim().length()>0){
				CodeVO code=codeCache.findCodeByName(keys[i].getRef().trim());
				if (code!=null)
					keys[i].setRef(code.getId());
				else
					strMsg.append(StringResource.getStringResource("miufoexpnew00038",new String[]{keys[i].getName(),keys[i].getRef()})+"\r\n");
			}
			else
				keys[i].setRef(null);
		}
		
		//对MeasureModel中measurePK2VO进行处理
		Hashtable hashMeasurePK2VO=measModel.getMeasurePK2VO();
		MeasureVO[] measures=(MeasureVO[])hashMeasurePK2VO.values().toArray(new MeasureVO[0]);
		for (int i=0;i<measures.length;i++){
			////对MeasureVO进行处理，判断代码是否存在，如果不存在，给出出错信息
			if (measures[i].getRefPK()!=null && measures[i].getRefPK().trim().length()>0){
				CodeVO code=codeCache.findCodeByName(measures[i].getRefPK().trim());
				if (code!=null)
					measures[i].setRefPK(code.getId());
				else
					strMsg.append(StringResource.getStringResource("miufoexpnew00038",new String[]{measures[i].getName(),measures[i].getRefPK()})+"\r\n");
			}
			else
				measures[i].setRefPK(null);
		}		
	}
	
	/**
	 * 对MeasureModel进行转换
	 * @param cellModel，导入时解析到的报表格式模型
	 * @param report，导入时解析到的报表对象
	 * @param strOldRepPK，在直接通过导入XML文件创建报表时，此参数指定原来报表的PK,用于区分指标是本表指标还是他表指标，在多级服务器处，此参数值为null
	 * @param bModel,是否是报表模型
	 * @return，导入时解析到的指标PK与转换到的指标PK的参照表
	 * @throws Exception
	 */
	public static Hashtable<String,String> processMeasureModel(CellsModel cellModel,ReportVO report,String strOldRepPK,boolean bModel) throws Exception{
		ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
		MeasureCache measCache=IUFOUICacheManager.getSingleton().getMeasureCache();
		KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
		RepFormatModelCache formatCache=IUFOUICacheManager.getSingleton().getRepFormatCache();
		CellsModel oldModel=formatCache.getUfoTableFormatModel(report.getReportPK());
		
		//导入解析得到的指标、动态区、关键字模型、指标名称与PK的参照关系
		MeasureModel measureModel=MeasureModel.getInstance(cellModel);
		DynAreaModel dynAreaModel=DynAreaModel.getInstance(cellModel);
		KeywordModel keyModel=KeywordModel.getInstance(cellModel);
		DynAreaVO[] dynAreas=dynAreaModel.getDynAreaVOs();
		PK2Code4Xml pk2Code=PK2Code4Xml.getInstance(cellModel);
				
		//系统中已经存在的指标、动态区、关键字模型
		MeasureModel oldMeasModel=MeasureModel.getInstance(oldModel);
		KeywordModel oldKeyModel=KeywordModel.getInstance(oldModel);
		DynAreaModel oldDynModel=DynAreaModel.getInstance(oldModel);
		DynAreaVO[] oldDynAreas=oldDynModel.getDynAreaVOs();
		
		//系统中新建的指标组PK，防止生成同一个指标组PK
		Hashtable<String,Boolean> hashUsedMeasPackPK=new Hashtable<String,Boolean>();
		
		//清空指标数据表哈希表，因为指标数据表需要重新创建
		measureModel.getDbTableNamesAll().clear();
		
		//导入文件中的MeasurePK与生成的MeasurePK的对照表
		Hashtable<String,String> hashMeasPKMap=new Hashtable<String,String>();
		
		//按动态区(包括主表)进行处理
		for (int i=-1;i<dynAreas.length;i++){
			String strDynAreaPK=null;
			String strOldMeasPackPK=null;
			String strMeasPackPK=null;
			Vector vExistKey=null;
			
			//得到导入时解析到的动态区或主表的dynAreaPK，指标组PK及系统中已存在的指标组PK
			if (i==-1){
				strDynAreaPK=DynAreaVO.MAINTABLE_DYNAREAPK;
				strOldMeasPackPK=oldMeasModel.getMainMeasurePackPK();
				strMeasPackPK=measureModel.getMainMeasurePackPK();
				vExistKey=kgCache.getByPK(keyModel.getMainKeyCombPK()).getVecKeys();
			}
			else{
				strDynAreaPK=dynAreas[i].getDynamicAreaPK();
				KeyVO[] keys=dynAreaModel.getKeywordModel().getKeyVOs(dynAreas[i].getDynamicAreaPK());
				vExistKey=new Vector();
				if (keys!=null)
					vExistKey.addAll(Arrays.asList(keys));
				DynAreaVO findDynArea=RepFormatImportCheckUtil.findExistDynAreaModel(dynAreas[i],oldDynAreas,keyModel,oldKeyModel);			
				strMeasPackPK=(String)measureModel.getAllMeasurePackPKs().get(dynAreas[i].getDynamicAreaPK());
				if (findDynArea!=null)
					strOldMeasPackPK=(String)oldMeasModel.getAllMeasurePackPKs().get(findDynArea.getDynamicAreaPK());
			}
			
			//如果系统中不存在该指标组PK，生成一个新的PK
			if (strOldMeasPackPK==null)
				strOldMeasPackPK=getOnePK(hashUsedMeasPackPK,strMeasPackPK,MeasurePackVO.MEASUREPACK_PK_LENGTH,measCache);
			
			//更新measureModel中的measurePackPKs
			measureModel.getAllMeasurePackPKs().put(strDynAreaPK,strOldMeasPackPK);
			
			//将动态区对应的指标数据表数组清空，该数组内容在后面处理中生成
			Vector vDbTable=new Vector();
			measureModel.getDbTableNamesAll().put(strDynAreaPK,vDbTable);
			
			//得到该动态区中位置指标对应关系Map
			Map<CellPosition,String> mapMeasPKByPos = measureModel.getDynAreaMeasures(strDynAreaPK);
			
			//得到measureModel中的measurePK2VO，对其中的MeasureVO进行处理
			Hashtable hashMeasure=measureModel.getMeasurePK2VO();
			
			String[] strMeasPKs=(String[])hashMeasure.keySet().toArray(new String[0]);
			for (int j=0;j<strMeasPKs.length;j++){
				//判断指标是否在当前动态区中，如果不在，不作处理
				if (mapMeasPKByPos.containsValue(strMeasPKs[j])==false)
					continue;
				
				MeasureVO measure=(MeasureVO)hashMeasure.get(strMeasPKs[j]);
				
				//将指标所在reportpk替换新系统中的ReportPK
				String strOldMeasRepPK=measure.getReportPK();
				String strReportPK=null;
				String strRepCode=pk2Code.getReportCodeByPK(measure.getReportPK());
				if (strOldMeasRepPK.equals(strOldRepPK))
					strReportPK=report.getReportPK();
				else
					strReportPK=repCache.getRepPKByCode(strRepCode,bModel);
				measure.setReportPK(strReportPK);
				
				//判断系统中是否存在该指标，如果存在，设置code、dbtable、dbcolumn与原指标相同
				MeasureVO oldMeasure=measCache.loadMeasuresByInputCode(strReportPK,measure.getInputCode());
				String strNewMeasPK=null;
				
				//有可能两个指标名称及对应的报表相同，但位于不同的动态区的情况，所以需要判断两个指标对应的关键字是否一样
				//不能根据KeyCombPK进行判断，因为在导入的报表模型中，其动态区的keyCombPK可能还未生成
				Vector vImportKey=null;
				if (oldMeasure!=null)
					vImportKey=kgCache.getByPK(oldMeasure.getKeyCombPK()).getVecKeys();
				if (oldMeasure!=null && RepFormatImportCheckUtil.isSameMeasureProp(oldMeasure,measure) && RepFormatImportCheckUtil.isSameKeyGroup(vExistKey, vImportKey)){
					measure.setDbcolumn(oldMeasure.getDbcolumn());
					measure.setDbtable(oldMeasure.getDbtable());
					strNewMeasPK=oldMeasure.getCode();
				}
				//否则生成新的指标PK，并置dbtable、dbcolumn为空，由ReportBO去生成dbtable,dbcolumn
				else{
					if (strOldRepPK!=null && strOldMeasRepPK.equals(strOldRepPK)==false){
						hashMeasure.remove(measure.getCode());
						continue;
					}
					else{
						measure.setDbcolumn(null);
						measure.setDbtable(null);
						strNewMeasPK=MeasureCache.getNewMeasurePK(strOldMeasPackPK);
					}
				}
				
				//对于属于本表的指标，其dbTable加进vDbTable数组中
				if (measure.getDbtable()!=null && measure.getReportPK().equals(report.getReportPK()) && vDbTable.contains(measure.getDbtable())==false)
					vDbTable.add(measure.getDbtable());
				
				//替换measurePK2VO中的内容
				hashMeasure.remove(measure.getCode());
				hashMeasure.put(strNewMeasPK,measure);
				
				//添加进新旧指标PK对照表中
				hashMeasPKMap.put(measure.getCode(),strNewMeasPK);
				measure.setCode(strNewMeasPK);
			}
		}
		
		//对应每个动态区，其单元格与指标对照关系表中的指标PK需要替换
		for(String dynAreaPK : measureModel.getDynAreaPKs()){
			Map<CellPosition,String> dynAreaPosPK = measureModel.getDynAreaMeasures(dynAreaPK);
			CellPosition[] measCellPos=dynAreaPosPK.keySet().toArray(new CellPosition[0]);
			for(CellPosition cellPos : measCellPos){
				String oldMeasurePK = dynAreaPosPK.get(cellPos);
				String newMeasurePK = hashMeasPKMap.get(oldMeasurePK);
				//如果找不到的新的指标PK，则将该单元格的指标对照关系删掉
				if(newMeasurePK != null)
					measureModel.setDynAreaMeasurePK(dynAreaPK, cellPos, newMeasurePK);
				else
					measureModel.removeMeasureVOByPos(cellPos);
			}
		}
		
		return hashMeasPKMap;
	}
	
	/**
	 * 处理公式模型中单位PK的处理
	 * @param cellModel
	 * @throws Exception
	 */
	public static void processFormulaModel(CellsModel cellModel) throws Exception{
		UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		
		FormulaModel formulaModel=FormulaModel.getInstance(cellModel);
		PK2Code4Xml pk2Code=PK2Code4Xml.getInstance(cellModel);
		
		//设置报表的创建单位
		if (formulaModel.getCreateUnitID()!=null && formulaModel.getCreateUnitID().trim().length()>0){
			String strUnitCode=pk2Code.getUnitCodeByPK(formulaModel.getCreateUnitID().trim());
			UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strUnitCode);
			if (unitInfo!=null)
				formulaModel.setCreateUnitID(unitInfo.getPK());
			else
				formulaModel.setCreateUnitID(null);
		}
		else
			formulaModel.setCreateUnitID(null);
		
		//对个性化公式进行处理
		Hashtable hashFormula=formulaModel.getUnitPersonFormulaAll();
		if (hashFormula==null || hashFormula.size()<=0)
			return;
		
		//替换unitPersonFormula中的单位PK与系统中已经存在的单位的PK
		String[] strUnitPKs=(String[])hashFormula.keySet().toArray(new String[0]);
		for (int i=0;i<strUnitPKs.length;i++){
			Object objFormula=hashFormula.get(strUnitPKs[i]);
			hashFormula.remove(strUnitPKs[i]);
			
			String strUnitCode=pk2Code.getUnitCodeByPK(strUnitPKs[i]);
			UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strUnitCode);
			if (unitInfo!=null)
				hashFormula.put(unitInfo.getPK(),objFormula);
		}
	}
	
	/**
	 * 处理业务查询、数据处理中用到的指标、关键字PK的替换
	 * @param cellModel，报表模型
	 * @param hashMeasPKMap，新旧指标PK参照表
	 * @param hashKeyPKMap，新旧关键字PK参照表
	 * @throws Exception
	 */
	public static void processReportBusinessQuery(CellsModel cellModel,Hashtable<String,String> hashMeasPKMap,Hashtable<String,String> hashKeyPKMap) throws Exception{
		//所有需要处理的字段映射关系
		Vector<FieldMap> vFieldMap=new Vector<FieldMap>();
		
		//得到业务查询中的字段映射关系
		ReportBusinessQuery busiQuery=ReportBusinessQuery.getInstance(cellModel);
		Vector vRepQuery=busiQuery.getAllReportQuery();		
		if (vRepQuery!=null){
			for (int i=0;i<vRepQuery.size();i++){
				ReportQueryVO repQuery=(ReportQueryVO)vRepQuery.get(i);
				
				ReportSelectFldVO[] selFlds=repQuery.getSelectFlds();
				if (selFlds==null)
					continue;
				
				vFieldMap.addAll(Arrays.asList(selFlds));
			}
		}
		
		//得到数据处理中排序字段的字段映射关系
		DynAreaModel dynAreaModel=DynAreaModel.getInstance(cellModel);
		DynAreaVO[] dynAreas=dynAreaModel.getDynAreaVOs();
		if (dynAreas!=null){
			for (int i=0;i<dynAreas.length;i++){
				AreaDataProcess dataProcess=dynAreaModel.getDataProcess(dynAreas[i].getDynamicAreaPK());
				if (dataProcess!=null && dataProcess.getDataProcessDef()!=null && dataProcess.getDataProcessDef() instanceof DefaultDataProcessDef){
					DefaultDataProcessDef defaultProcessDef=(DefaultDataProcessDef)dataProcess.getDataProcessDef();
					if (defaultProcessDef!=null)
						vFieldMap.addAll(Arrays.asList(defaultProcessDef.getOrderByFlds()));
				}
			}
		}
		
		//对FieldMap中的mapPK，用指标、关键字对照表做处理，根据新的的mapPK,得到新的mapName
		Hashtable<String,String> hashNameMap=new Hashtable<String,String>();
		for (int i=0;i<vFieldMap.size();i++){
			FieldMap map=vFieldMap.get(i);
			String strOldPK=map.getMapPK();
			if (strOldPK!=null){
				Hashtable<String,String> hashPKMap=null;
				if (map.getMapType()==FieldMap.FIELD_MAP_KEYWORD)
					hashPKMap=hashKeyPKMap;
				else
					hashPKMap=hashMeasPKMap;
				
				String strNewPK=hashPKMap.get(strOldPK);
				if (strNewPK!=null){
					map.setMapPK(strNewPK);
					
					String strNewMapName=FieldMapUtil.getMapName(map.getMapType(), strNewPK);
					hashNameMap.put(map.getMapName(),strNewMapName);
					map.setMapName(strNewMapName);
				}
			}
		}
		
		//数据处理中的定义条件的Hashtable是以mapName作主键的，现在mapName变了，需要对该Hashtable做处理
		if (dynAreas!=null){
			for (int i=0;i<dynAreas.length;i++){
				AreaDataProcess dataProcess=dynAreaModel.getDataProcess(dynAreas[i].getDynamicAreaPK());
				if (dataProcess!=null && dataProcess.getDataProcessDef()!=null && dataProcess.getDataProcessDef() instanceof GroupLayingDef){
					Hashtable<String, DataProcessFld> hashFld=((GroupLayingDef)dataProcess.getDataProcessDef()).getDetailFlds();
					if (hashFld!=null){
						String[] strNames=hashFld.keySet().toArray(new String[0]);
						for (int j=0;j<strNames.length;j++){
							String strNewName=hashNameMap.get(strNames[j]);
							if (strNewName!=null){
								DataProcessFld objFld=hashFld.get(strNames[j]);
								hashFld.remove(strNames[j]);
								hashFld.put(strNewName,objFld);
							}
						}
					}
				}
			}
		}		
	}
	
	/**
	 * 处理打印设置的中个性化设置由于单位PK变化所做的处理
	 * @param cellsModel
	 */
	public static void processPrintSet(CellsModel cellsModel){
		Hashtable<String,Object> htPrintSet = (Hashtable<String,Object>)cellsModel.getExtProp(PrintSet.class);
		if (htPrintSet==null || htPrintSet.size()<=0)
			return;
		
		UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		PK2Code4Xml pk2Code=PK2Code4Xml.getInstance(cellsModel);
		
		String[] strUnitPKs=(String[])htPrintSet.keySet().toArray(new String[0]);
		for (int i=0;i<strUnitPKs.length;i++){
			Object printSet=htPrintSet.get(strUnitPKs[i]);
			htPrintSet.remove(strUnitPKs[i]);
			
			String strUnitCode=pk2Code.getUnitCodeByPK(strUnitPKs[i]);
			if (strUnitCode==null)
				continue;
			
			UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strUnitCode);
			if (unitInfo==null)
				continue;
			
			htPrintSet.put(unitInfo.getPK(),printSet);
		}
	}
	
	private static String getOnePK(Hashtable<String,Boolean> hashUsedPK,String strPK,int iLen,IUFOCache cache) throws Exception{	
		strPK=IDMaker.makeID(iLen);
		while (true){
			if (hashUsedPK.containsKey(strPK)==false){
				boolean bExist=false;
				if (cache instanceof MeasureCache)
					bExist=((MeasureCache)cache).getPackage(strPK)!=null;
				else
					bExist=cache.get(strPK)!=null;
				
				if (bExist==false){
					hashUsedPK.put(strPK,Boolean.TRUE);
					return strPK;
				}
			}
			strPK=IDMaker.makeID(iLen);
		}
	}	
}
