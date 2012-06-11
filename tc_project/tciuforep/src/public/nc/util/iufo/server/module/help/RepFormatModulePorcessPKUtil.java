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
 * ���뱨���ʽʱ�������������ݿ�֮��ָ�ꡢ�������롢�ؼ��֡���λPK����ͬ����Ҫ�Ա����ʽģ�����õ���Щֵ�ĵط������滻�Ĺ�����
 * @author weixl
 */
public class RepFormatModulePorcessPKUtil {
	/**
	 * �Թؼ���ģ�ͽ����滻����
	 * @param cellModel�������ʽģ��
	 * @param report������VO
	 * @return,����ʱ�Ĺؼ���PK���滻��Ĺؼ���PK�Ķ��ձ�
	 * @throws Exception
	 */
	public static Hashtable<String,String> processKeywordModel(CellsModel cellModel,ReportVO report) throws Exception{
		//��Ҫ���صĵ���ʱ�Ĺؼ���PK���滻��Ĺؼ���PK�Ķ��ձ�
		Hashtable<String,String> hashKeyPKMap=new Hashtable<String,String>();
		
		KeywordModel keyModel=KeywordModel.getInstance(cellModel);
		KeywordCache keyCache=IUFOUICacheManager.getSingleton().getKeywordCache();
		KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
		
		//�½��Ĺؼ���PK����ֹ�����ؼ�����ͬһ��PK
		Hashtable<String,Boolean> hashUsedKeyPK=new Hashtable<String,Boolean>();

		//����ؼ���PK��ؼ���VO�Ķ��ձ�
		String[] strKeyPKs=(String[])keyModel.getKeyPK2VO().keySet().toArray(new String[0]);
		for(int i=0;i<strKeyPKs.length;i++){
			String strKeyPK=strKeyPKs[i];
			KeyVO key=(KeyVO)keyModel.getKeyPK2VO().get(strKeyPK);
			if (key==null)
				continue;		
			
			//�ж�ϵͳ���Ƿ����ͬ���Ĺؼ��֣��˴��ж�ֻ��˽�йؼ��������壬������йؼ��֣���ϵͳ�в����ڣ�
			//���ڵ���ʱcheckKey�����оͻᱨ��
			KeyVO existKey=keyCache.getByName(key.getName(),key.isPrivate()?report.getReportPK():null);
			
			//������ڴ˹ؼ��֣���KeywordModel�еĹؼ���PK�滻��ϵͳ���Ѿ���Ĺؼ��ֵ�PK
			if (existKey!=null){
				hashKeyPKMap.put(key.getKeywordPK(),existKey.getKeywordPK());
				keyModel.getKeyPK2VO().remove(strKeyPK);
				keyModel.getKeyPK2VO().put(existKey.getKeywordPK(),key);
				key.setKeywordPK(existKey.getKeywordPK());
				key.setRepIdOfPrivate(existKey.getRepIdOfPrivate());
				hashUsedKeyPK.put(key.getKeywordPK(),Boolean.TRUE);
			}
		}		
		
		//���������Ĺؼ��֣������µ�PK������keyModel�е�keyPK2VO�����滻
		strKeyPKs=(String[])keyModel.getKeyPK2VO().keySet().toArray(new String[0]);
		for(int i=0;i<strKeyPKs.length;i++){
			String strKeyPK=strKeyPKs[i];
			KeyVO key=(KeyVO)keyModel.getKeyPK2VO().get(strKeyPK);
			if (key==null || key.isPrivate()==false)
				continue;
				
			KeyVO existKey=keyCache.getByName(key.getName(),report.getReportPK());
			if (existKey==null){
				//�µĹؼ��֣��϶���˽�йؼ���
				String strNewKeyPK=getOnePK(hashUsedKeyPK,key.getKeywordPK(),20,keyCache);
				keyModel.getKeyPK2VO().remove(strKeyPK);
				keyModel.getKeyPK2VO().put(strNewKeyPK,key);
				
				hashKeyPKMap.put(key.getKeywordPK(),strNewKeyPK);	
				key.setKeywordPK(strNewKeyPK);
				key.setRepIdOfPrivate(report.getReportPK());				
			}
		}

		//�������õõ����¾ɹؼ���PK�Ķ��ձ���keyModel�е�mainDisplayKeys��mainUndisplayKeys��
		//mainUndisplayKeyValues��subTableKeys���õ��Ĺؼ���PK�����滻
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
		
		//��������ؼ������PK������ùؼ������ϵͳ���Ѿ����ڣ���ʹ�����е�PK�����򴴽��µĹؼ�����ϣ��õ��´�����PK
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
	 * ���뱨���ʽʱ����ָ�ꡢ�ؼ��ֶ�Ӧ����ʱ����Ҫ�����еĴ���PK�仯��ϵͳ���Ѿ����ڵĴ���PK
	 * @param cellsModel�������ʽģ�� 
	 * @param strMsg��У��ʱ������Ϣ
	 * @throws Exception
	 */
	public static void processKeyMeasureCodeRef(CellsModel cellsModel,StringBuffer strMsg) throws Exception{
		KeywordModel keyModel=KeywordModel.getInstance(cellsModel);
		MeasureModel measModel=MeasureModel.getInstance(cellsModel);
		CodeCache codeCache=IUFOUICacheManager.getSingleton().getCodeCache();
		
		//��keyModel�е�keyPK2VO�н��д���
		Hashtable hashKeyPK2VO=keyModel.getKeyPK2VO();
		KeyVO[] keys=(KeyVO[])hashKeyPK2VO.values().toArray(new KeyVO[0]);
		for (int i=0;i<keys.length;i++){
			//��KeyVO���д����жϴ����Ƿ���ڣ���������ڣ�����������Ϣ
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
		
		//��MeasureModel��measurePK2VO���д���
		Hashtable hashMeasurePK2VO=measModel.getMeasurePK2VO();
		MeasureVO[] measures=(MeasureVO[])hashMeasurePK2VO.values().toArray(new MeasureVO[0]);
		for (int i=0;i<measures.length;i++){
			////��MeasureVO���д����жϴ����Ƿ���ڣ���������ڣ�����������Ϣ
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
	 * ��MeasureModel����ת��
	 * @param cellModel������ʱ�������ı����ʽģ��
	 * @param report������ʱ�������ı������
	 * @param strOldRepPK����ֱ��ͨ������XML�ļ���������ʱ���˲���ָ��ԭ�������PK,��������ָ���Ǳ���ָ�껹������ָ�꣬�ڶ༶�����������˲���ֵΪnull
	 * @param bModel,�Ƿ��Ǳ���ģ��
	 * @return������ʱ��������ָ��PK��ת������ָ��PK�Ĳ��ձ�
	 * @throws Exception
	 */
	public static Hashtable<String,String> processMeasureModel(CellsModel cellModel,ReportVO report,String strOldRepPK,boolean bModel) throws Exception{
		ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
		MeasureCache measCache=IUFOUICacheManager.getSingleton().getMeasureCache();
		KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
		RepFormatModelCache formatCache=IUFOUICacheManager.getSingleton().getRepFormatCache();
		CellsModel oldModel=formatCache.getUfoTableFormatModel(report.getReportPK());
		
		//��������õ���ָ�ꡢ��̬�����ؼ���ģ�͡�ָ��������PK�Ĳ��չ�ϵ
		MeasureModel measureModel=MeasureModel.getInstance(cellModel);
		DynAreaModel dynAreaModel=DynAreaModel.getInstance(cellModel);
		KeywordModel keyModel=KeywordModel.getInstance(cellModel);
		DynAreaVO[] dynAreas=dynAreaModel.getDynAreaVOs();
		PK2Code4Xml pk2Code=PK2Code4Xml.getInstance(cellModel);
				
		//ϵͳ���Ѿ����ڵ�ָ�ꡢ��̬�����ؼ���ģ��
		MeasureModel oldMeasModel=MeasureModel.getInstance(oldModel);
		KeywordModel oldKeyModel=KeywordModel.getInstance(oldModel);
		DynAreaModel oldDynModel=DynAreaModel.getInstance(oldModel);
		DynAreaVO[] oldDynAreas=oldDynModel.getDynAreaVOs();
		
		//ϵͳ���½���ָ����PK����ֹ����ͬһ��ָ����PK
		Hashtable<String,Boolean> hashUsedMeasPackPK=new Hashtable<String,Boolean>();
		
		//���ָ�����ݱ��ϣ����Ϊָ�����ݱ���Ҫ���´���
		measureModel.getDbTableNamesAll().clear();
		
		//�����ļ��е�MeasurePK�����ɵ�MeasurePK�Ķ��ձ�
		Hashtable<String,String> hashMeasPKMap=new Hashtable<String,String>();
		
		//����̬��(��������)���д���
		for (int i=-1;i<dynAreas.length;i++){
			String strDynAreaPK=null;
			String strOldMeasPackPK=null;
			String strMeasPackPK=null;
			Vector vExistKey=null;
			
			//�õ�����ʱ�������Ķ�̬���������dynAreaPK��ָ����PK��ϵͳ���Ѵ��ڵ�ָ����PK
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
			
			//���ϵͳ�в����ڸ�ָ����PK������һ���µ�PK
			if (strOldMeasPackPK==null)
				strOldMeasPackPK=getOnePK(hashUsedMeasPackPK,strMeasPackPK,MeasurePackVO.MEASUREPACK_PK_LENGTH,measCache);
			
			//����measureModel�е�measurePackPKs
			measureModel.getAllMeasurePackPKs().put(strDynAreaPK,strOldMeasPackPK);
			
			//����̬����Ӧ��ָ�����ݱ�������գ������������ں��洦��������
			Vector vDbTable=new Vector();
			measureModel.getDbTableNamesAll().put(strDynAreaPK,vDbTable);
			
			//�õ��ö�̬����λ��ָ���Ӧ��ϵMap
			Map<CellPosition,String> mapMeasPKByPos = measureModel.getDynAreaMeasures(strDynAreaPK);
			
			//�õ�measureModel�е�measurePK2VO�������е�MeasureVO���д���
			Hashtable hashMeasure=measureModel.getMeasurePK2VO();
			
			String[] strMeasPKs=(String[])hashMeasure.keySet().toArray(new String[0]);
			for (int j=0;j<strMeasPKs.length;j++){
				//�ж�ָ���Ƿ��ڵ�ǰ��̬���У�������ڣ���������
				if (mapMeasPKByPos.containsValue(strMeasPKs[j])==false)
					continue;
				
				MeasureVO measure=(MeasureVO)hashMeasure.get(strMeasPKs[j]);
				
				//��ָ������reportpk�滻��ϵͳ�е�ReportPK
				String strOldMeasRepPK=measure.getReportPK();
				String strReportPK=null;
				String strRepCode=pk2Code.getReportCodeByPK(measure.getReportPK());
				if (strOldMeasRepPK.equals(strOldRepPK))
					strReportPK=report.getReportPK();
				else
					strReportPK=repCache.getRepPKByCode(strRepCode,bModel);
				measure.setReportPK(strReportPK);
				
				//�ж�ϵͳ���Ƿ���ڸ�ָ�꣬������ڣ�����code��dbtable��dbcolumn��ԭָ����ͬ
				MeasureVO oldMeasure=measCache.loadMeasuresByInputCode(strReportPK,measure.getInputCode());
				String strNewMeasPK=null;
				
				//�п�������ָ�����Ƽ���Ӧ�ı�����ͬ����λ�ڲ�ͬ�Ķ�̬���������������Ҫ�ж�����ָ���Ӧ�Ĺؼ����Ƿ�һ��
				//���ܸ���KeyCombPK�����жϣ���Ϊ�ڵ���ı���ģ���У��䶯̬����keyCombPK���ܻ�δ����
				Vector vImportKey=null;
				if (oldMeasure!=null)
					vImportKey=kgCache.getByPK(oldMeasure.getKeyCombPK()).getVecKeys();
				if (oldMeasure!=null && RepFormatImportCheckUtil.isSameMeasureProp(oldMeasure,measure) && RepFormatImportCheckUtil.isSameKeyGroup(vExistKey, vImportKey)){
					measure.setDbcolumn(oldMeasure.getDbcolumn());
					measure.setDbtable(oldMeasure.getDbtable());
					strNewMeasPK=oldMeasure.getCode();
				}
				//���������µ�ָ��PK������dbtable��dbcolumnΪ�գ���ReportBOȥ����dbtable,dbcolumn
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
				
				//�������ڱ����ָ�꣬��dbTable�ӽ�vDbTable������
				if (measure.getDbtable()!=null && measure.getReportPK().equals(report.getReportPK()) && vDbTable.contains(measure.getDbtable())==false)
					vDbTable.add(measure.getDbtable());
				
				//�滻measurePK2VO�е�����
				hashMeasure.remove(measure.getCode());
				hashMeasure.put(strNewMeasPK,measure);
				
				//��ӽ��¾�ָ��PK���ձ���
				hashMeasPKMap.put(measure.getCode(),strNewMeasPK);
				measure.setCode(strNewMeasPK);
			}
		}
		
		//��Ӧÿ����̬�����䵥Ԫ����ָ����չ�ϵ���е�ָ��PK��Ҫ�滻
		for(String dynAreaPK : measureModel.getDynAreaPKs()){
			Map<CellPosition,String> dynAreaPosPK = measureModel.getDynAreaMeasures(dynAreaPK);
			CellPosition[] measCellPos=dynAreaPosPK.keySet().toArray(new CellPosition[0]);
			for(CellPosition cellPos : measCellPos){
				String oldMeasurePK = dynAreaPosPK.get(cellPos);
				String newMeasurePK = hashMeasPKMap.get(oldMeasurePK);
				//����Ҳ������µ�ָ��PK���򽫸õ�Ԫ���ָ����չ�ϵɾ��
				if(newMeasurePK != null)
					measureModel.setDynAreaMeasurePK(dynAreaPK, cellPos, newMeasurePK);
				else
					measureModel.removeMeasureVOByPos(cellPos);
			}
		}
		
		return hashMeasPKMap;
	}
	
	/**
	 * ����ʽģ���е�λPK�Ĵ���
	 * @param cellModel
	 * @throws Exception
	 */
	public static void processFormulaModel(CellsModel cellModel) throws Exception{
		UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		
		FormulaModel formulaModel=FormulaModel.getInstance(cellModel);
		PK2Code4Xml pk2Code=PK2Code4Xml.getInstance(cellModel);
		
		//���ñ���Ĵ�����λ
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
		
		//�Ը��Ի���ʽ���д���
		Hashtable hashFormula=formulaModel.getUnitPersonFormulaAll();
		if (hashFormula==null || hashFormula.size()<=0)
			return;
		
		//�滻unitPersonFormula�еĵ�λPK��ϵͳ���Ѿ����ڵĵ�λ��PK
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
	 * ����ҵ���ѯ�����ݴ������õ���ָ�ꡢ�ؼ���PK���滻
	 * @param cellModel������ģ��
	 * @param hashMeasPKMap���¾�ָ��PK���ձ�
	 * @param hashKeyPKMap���¾ɹؼ���PK���ձ�
	 * @throws Exception
	 */
	public static void processReportBusinessQuery(CellsModel cellModel,Hashtable<String,String> hashMeasPKMap,Hashtable<String,String> hashKeyPKMap) throws Exception{
		//������Ҫ������ֶ�ӳ���ϵ
		Vector<FieldMap> vFieldMap=new Vector<FieldMap>();
		
		//�õ�ҵ���ѯ�е��ֶ�ӳ���ϵ
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
		
		//�õ����ݴ����������ֶε��ֶ�ӳ���ϵ
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
		
		//��FieldMap�е�mapPK����ָ�ꡢ�ؼ��ֶ��ձ������������µĵ�mapPK,�õ��µ�mapName
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
		
		//���ݴ����еĶ���������Hashtable����mapName�������ģ�����mapName���ˣ���Ҫ�Ը�Hashtable������
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
	 * �����ӡ���õ��и��Ի��������ڵ�λPK�仯�����Ĵ���
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
