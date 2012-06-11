package nc.util.iufo.server.module.help;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.AreaDataProcess;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.DataProcessFilterCond;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.FieldMapUtil;
import com.ufsoft.iufo.fmtplugin.dataprocess.basedef.GroupFormulaVO;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.FormulaVO;
import com.ufsoft.iufo.fmtplugin.formula.RepCheckVO;
import com.ufsoft.iufo.fmtplugin.formula.SimpleCheckFmlVO;
import com.ufsoft.iufo.fmtplugin.formula.UfoFmlExecutor;
import com.ufsoft.iufo.fmtplugin.service.DataProcessSrv;
import com.ufsoft.iufo.i18n.MultiLangUtil;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportContextKey;
import com.ufsoft.script.ReplMeasFormulaUtil;
import com.ufsoft.script.base.IParsed;
import com.ufsoft.script.extfunc.GAggrFuncDriver;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.IArea;

/**
 * ���롢���������ʽʱ���Ա����еĹ�ʽ�����û��ɼ������ݿⱣ��״̬֮�����ת���Ĺ�����
 * @author weixl
 *
 */
public class RepFormatModuleFormulaUtil implements IUfoContextKey{
	/**
	 * @i18n miufo1000713=����ʽ
	 */
	private static final String ERROR_FORMULA = StringResource.getStringResource("miufo1000713");
	
	/**
	 * ��ת����ʽ����
	 * @param repVO,����VO
	 * @param fModel�������ʽģ��
	 * @param dataSource������Դ
	 * @param bConvertToDB���Ƿ�ָ�ꡢ�ؼ���ת����PK
	 */
	public static void convertFormulas(ReportVO repVO, CellsModel fModel,DataSourceVO dataSource,boolean bConvertToDB) {
		if(repVO==null || fModel==null)
			return;
		
		UfoContextVO contextVO=new UfoContextVO();
		contextVO.setAttribute(ReportContextKey.REPORT_PK,repVO.getReportPK());
		contextVO.setAttribute(ReportContextKey.REPORT_CODE,repVO.getCode());
		contextVO.setAttribute(MODEL, repVO.isModel());
		contextVO.setAttribute(DATA_SOURCE,dataSource);
		contextVO.setAttribute(ON_SERVER, false);
		contextVO.setAttribute(CURRENT_LANG, MultiLangUtil.getDefaultLangCode());
		
		convertFormulas(contextVO, fModel, dataSource, bConvertToDB);
	}
	
	/**
	 * ͨ������XML�ļ���ʽ�����±���ʱ���滻���й�ʽ�е�ָ��PKΪ�µ�PK
	 * @param formatModel���������ı���ģ��
	 * @param hashMeasPKs���¾�ָ��PK�Ķ�Ӧ��ϵ
	 */
    public static void replaceMeasPKs4TransReps(CellsModel formatModel,
            Hashtable<String,String> hashMeasPKs) {
        //�滻�����������֣���Ԫ��Ӧ��ָ�����������ʽ������ʹ�õ�ָ��������
        if (formatModel == null || hashMeasPKs == null) {
            return;
        }

        ReplMeasFormulaUtil replUtil = new ReplMeasFormulaUtil(hashMeasPKs);

        String strFormulaContent = null;
        FormulaModel formulaModel = CellsModelOperator.getFormulaModel(formatModel);
        try {
            //���ȫ����λ�ĸ��Ի���Ԫ��ʽ,������̬��������
            Hashtable hashUnitFormula = formulaModel.getUnitPersonFormulaAll();
            if (hashUnitFormula != null) {
                Enumeration keys = hashUnitFormula.keys();
                while (keys.hasMoreElements()) {
                	String unitID = (String) keys.nextElement();
                    Hashtable unitformulaList = (Hashtable) hashUnitFormula.get(unitID);
                    if (unitformulaList != null && unitformulaList.size() > 0) {
                    	for(Iterator iterator = unitformulaList.values().iterator();iterator.hasNext();){
                    		Hashtable dynAreaFormulas = (Hashtable) iterator.next();
                    		for(Iterator iterator2 = dynAreaFormulas.values().iterator();iterator2.hasNext();){
                    			FormulaVO formulaVO = (FormulaVO) iterator2.next();
                        		strFormulaContent = formulaVO.getFormulaContent();
                                strFormulaContent = replUtil.convertFormula(strFormulaContent);
                                formulaVO.setFormulaContent(strFormulaContent);
                    		}
                    	}
                    }
                }
            }
            //��鹫�й�ʽ��
            Hashtable unitformulaList =formulaModel.getPublicFormulaAll();
            if (unitformulaList != null && unitformulaList.size() > 0) {
            	for(Iterator iterator = unitformulaList.values().iterator();iterator.hasNext();){
            		Hashtable dynAreaFormulas = (Hashtable) iterator.next();
            		for(Iterator iterator2 = dynAreaFormulas.values().iterator();iterator2.hasNext();){
            			FormulaVO formulaVO = (FormulaVO) iterator2.next();
                		strFormulaContent = formulaVO.getFormulaContent();
                        strFormulaContent = replUtil.convertFormula(strFormulaContent);
                        formulaVO.setFormulaContent(strFormulaContent);
            		}
            	}
            }

            //�����ܹ�ʽ��������̬��������
            Hashtable listTotalFormula = formulaModel.getFormulaAllByType(false);
            if (listTotalFormula != null
                    && listTotalFormula.size() > 0) {
                for (Iterator iterator = listTotalFormula.values().iterator();iterator.hasNext();) {
                    FormulaVO formula = (FormulaVO) iterator.next();
                    strFormulaContent = formula.getFormulaContent();
                    strFormulaContent = replUtil.convertFormula(strFormulaContent);
                    formula.setFormulaContent(strFormulaContent);
                }

            }
            //�����˹�ʽ��������̬��������
            Vector vecComplexCheckFormula = formulaModel.getComplexCheckFml();
            if (vecComplexCheckFormula != null &&  vecComplexCheckFormula.size() > 0) {
                for (int i = 0; i < vecComplexCheckFormula.size(); i++) {
                    RepCheckVO repCheckVO = (RepCheckVO) vecComplexCheckFormula.get(i);
                    strFormulaContent = repCheckVO.getFormula();
                    strFormulaContent = replUtil.convertFormula(strFormulaContent);
                    repCheckVO.setFormula(strFormulaContent);
                }
            }
            Vector vecSimpleCheckFormula = formulaModel.getSimpleCheckFml();
            if (vecSimpleCheckFormula != null && vecSimpleCheckFormula.size() > 0) {
                for (int i = 0; i < vecSimpleCheckFormula.size(); i++) {
                	SimpleCheckFmlVO repCheckVO = (SimpleCheckFmlVO) vecSimpleCheckFormula.get(i);
                    strFormulaContent = repCheckVO.getCheckCond();
                    strFormulaContent = replUtil.convertFormula(strFormulaContent);
                    repCheckVO.setCheckCond(strFormulaContent);
                }
            }
        } catch (Exception e) {
        	AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }
    }	
	
    /**
     * ת�����еĹ�ʽ
     * @param contextVO�������Ķ���
     * @param fModel�������ʽģ�Ͷ���
     * @param dataSource����ǰ����Դ
     * @param bConvertToDB���Ƿ�ָ�ꡢ�ؼ���ת����PK
     */
	public static void convertFormulas(UfoContextVO contextVO,CellsModel fModel,DataSourceVO dataSource,boolean bConvertToDB){
		UfoFmlExecutor handler=UfoFmlExecutor.getInstance(contextVO,fModel);
		FormulaModel formulaModel = FormulaModel.getInstance(fModel);
		
		if(bConvertToDB) {
			System.out.println("ָ��");
		}
		
		//���Ի���Ԫ��ʽ
		Hashtable formulaForUnit = formulaModel.getUnitPersonFormulaAll();
		if (formulaForUnit != null) {
			for(Object dynFormulas : formulaForUnit.values()){
				convertDynAreaFormulas(handler,(Hashtable)dynFormulas,bConvertToDB);
			}
		}
		//���й�ʽ��
		convertDynAreaFormulas(handler,formulaModel.getPublicFormulaAll(),bConvertToDB);
		
		//���ܹ�ʽ
		convertDynAreaFormulas(handler,formulaModel.getTotalFormula(),bConvertToDB);
		
		//��˹�ʽ
		Vector simpleChecks=formulaModel.getSimpleCheckFml();
		if (simpleChecks != null && simpleChecks.size() > 0) {
			for (int i = 0; i < simpleChecks.size(); i++) {
				SimpleCheckFmlVO repCheckVO = (SimpleCheckFmlVO) simpleChecks.get(i);
				String strCond = repCheckVO.getCheckCond();
				String strNewCond=convertSimpleCheckCond(handler,strCond,bConvertToDB);
				repCheckVO.setCheckCond(strNewCond);
			}
		}
		//����
		Vector vecChecks = formulaModel.getComplexCheckFml();
		if (vecChecks != null && vecChecks.size() > 0) {
			for (int i = 0; i < vecChecks.size(); i++) {
				RepCheckVO repCheckVO = (RepCheckVO) vecChecks.get(i);
				String strContent = repCheckVO.getFormula();
				if(bConvertToDB)
					strContent = toDBStoredCheck(handler,strContent);
				else
					strContent=toUserDefCheck(handler,strContent);
				repCheckVO.setFormula(strContent);
			}
		}

		// �õ���̬���������ݴ���ɸѡ����
		DynAreaModel dynAreaModel = DynAreaModel.getInstance(fModel);
		DynAreaVO[] dynAreas = dynAreaModel.getDynAreaVOs();
		if (dynAreas != null && dynAreas.length > 0) {
			DataProcessSrv dataProcessSrv = new DataProcessSrv(contextVO,fModel, true);
			for (int i = 0; i < dynAreas.length; i++) {
				// �õ����ݴ����ɸѡ����
				AreaDataProcess dp = dynAreaModel.getDataProcess(dynAreas[i].getDynamicAreaPK());
				if (dp != null && dp.getDataProcessDef() != null) {
					DataProcessFilterCond fCond = dp.getDataProcessDef().getDPFilterCond();
					if (fCond != null && fCond.getFilterCond() != null) {
						String strNewFilterCond=null;
						if(bConvertToDB)
							strNewFilterCond=toDBStoredCond(handler,fCond.getFilterCond(), dynAreas[i].getDynamicAreaPK());
						else
							strNewFilterCond=toUserDefCond(handler,fCond.getFilterCond(), dynAreas[i].getDynamicAreaPK());
						fCond.setFilterCond(strNewFilterCond);
					}
				}
			}
			// ��鶯̬�����е����ݴ���Ĺ�ʽ
			FuncListInst funcList = handler.getCalcEnv().loadFuncListInst();
			funcList.registerExtFuncs(new GAggrFuncDriver(handler.getCalcEnv()));
			Object repIdObj = contextVO.getAttribute(REPORT_PK);
			String strRepPK = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null;
			
			for (int i = 0; i < dynAreas.length; i++) {
				AreaDataProcess dp = dynAreaModel.getDataProcess(dynAreas[i].getDynamicAreaPK());
				if (dp != null) {
					Vector vecFormulas = dp.getGroupFormulaList();
					if (vecFormulas != null) {
						handler.getCalcEnv().setFieldMaps(
								FieldMapUtil.getFieldMaps(dynAreas[i]
										.getDynamicAreaPK(), dataProcessSrv,
										strRepPK, handler
												.getCalcEnv()));
						if (vecFormulas != null && vecFormulas.size() > 0) {
							for (int j = 0; j < vecFormulas.size(); j++) {
								GroupFormulaVO fVO = (GroupFormulaVO) vecFormulas.get(j);
								String strFormula = fVO.getFormulaContent();
								if (fVO.getType() == GroupFormulaVO.GROUPFORMULA_GROUP_AGG) {
									strFormula = handler.convertDataProcessForm(fVO,dynAreas[i].getDynamicAreaPK(),bConvertToDB);
								}
								if (strFormula == null) { // ɾ���ù�ʽ
									vecFormulas.remove(fVO);
								} else {
									fVO.setFormulaContent(strFormula);
								}
							}
						}
					}
				}
			}
			funcList.unRegisterExtFunc(GAggrFuncDriver.class.getName());
		}
	}	
	
	/**
	 * ת��һ����㹫ʽ
	 * @param handler
	 * @param dynFormulas
	 * @param bConvertToDB
	 */
	private static void convertDynAreaFormulas(UfoFmlExecutor handler,Hashtable dynFormulas,boolean bConvertToDB){
		String strFmlContent=null;
		if(dynFormulas == null) dynFormulas = new Hashtable();
		for(Object dynAreaPK : dynFormulas.keySet()){
			Hashtable eachDynFormulas = (Hashtable) dynFormulas.get(dynAreaPK);
			if(eachDynFormulas==null || eachDynFormulas.size()==0)
				continue;
			Iterator<IArea> keys=eachDynFormulas.keySet().iterator();
			while(keys.hasNext()){
				IArea area=keys.next();
				FormulaVO formulaVO = (FormulaVO) eachDynFormulas.get(area);
				if(bConvertToDB)
					strFmlContent=toDBStoredFormula(handler,(String)dynAreaPK,area,formulaVO);
				else
					strFmlContent=toUserDefFormula(handler,(String)dynAreaPK,area,formulaVO);
				formulaVO.setFormulaContent(strFmlContent);
			}
		}
	}	

	/**
	 * ת��һ����˹�ʽ
	 * @param handler
	 * @param strCheckCond
	 * @param bUser
	 * @return
	 */
	private static String convertSimpleCheckCond(UfoFmlExecutor handler,String strCheckCond,boolean bUser){
		if(strCheckCond==null)
			return null;
		try{
			IParsed parsed=handler.parseLogicExpr(strCheckCond, bUser);
			if(parsed!=null){
				if(bUser)	
					return parsed.toString(handler.getCalcEnv());
				else
					return parsed.toUserDefString(handler.getCalcEnv());
			}
			return null;
		}
		catch(Exception e){
			AppDebug.debug(e);
			return strCheckCond;
		}
	}	
	
	/**
	 * �ж��Ƿ���Ϊ����ʽ
	 * @param formula String ��ʽ���ݡ����벻����Ϊ�ա�
	 * @return boolean �Ƿ����ʽ
	 */
	private static boolean isErrorFormula(String formula){
	    return formula.trim().endsWith(ERROR_FORMULA);
	}
	
	/**
	 * ����һ����ʽ���ݵı�ǡ������ʽ������ȷ����Ҫɾ����ʽ��˵Ĵ����ǣ������ʽ���ݴ�����Ҫ��һ����ǡ�
	 * @param formula String ��ʽ����.�ǿղ���
	 * @param correct boolean �����Ƿ���ȷ
	 * @return String ���������Ĺ�ʽ����
	 */
	private static String markFormula(String formula,boolean correct){
	    formula = formula.trim();
	    if(correct) {
	        if(isErrorFormula(formula)){
	            int formulaLen = formula.length();
	            int markLen = ERROR_FORMULA.length();
	            int errorMarkCount = 1;//�����ǳ��ֵĴ���ͳ��
	            int startCheckPos = formulaLen-markLen*2;
	            while(startCheckPos>=0) {
	                if(formula.startsWith(ERROR_FORMULA,startCheckPos)){
	                    errorMarkCount++;
	                    startCheckPos-=markLen;
	                }else {
	                    break;
	                }
	            }
	            formula = formula.substring(0,formulaLen-markLen*errorMarkCount)           ;
	        }
	    }else if(!isErrorFormula(formula)){
	        formula+=ERROR_FORMULA;
	    }
	    return formula;

	}	

	/**
	 * ת��Ϊ���ݿⱣ��Ĺ�ʽ����
	 * @param handler
	 * @param strDynPK
	 * @param area
	 * @param fVO
	 * @return
	 */ 
	private static String toDBStoredFormula(UfoFmlExecutor handler,String strDynPK,IArea area,FormulaVO fVO) {
		if(FormulaModel.MAINTABLE_DYNPK.equals(strDynPK))
			strDynPK=null;
		return handler.convertFormula(fVO, strDynPK,area,true);
	}

	/**
	 * ת��Ϊ�û�����Ĺ�ʽ����
	 * @param strDynPK ��ʽ���ڶ�̬�������������strDynPK=FormulaModel.MAINTABLE_DYNPK
	 * @param strFormula
	 * @return
	 */
	private static String toUserDefFormula(UfoFmlExecutor handler,String strDynPK,IArea area,FormulaVO fVO) {
		if(FormulaModel.MAINTABLE_DYNPK.equals(strDynPK))
			strDynPK=null;
		
		return handler.convertFormula(fVO,strDynPK, area,false);
	}

	/**
	 * ת��Ϊ�û�����Ĺ�ʽ����
	 * @param strFormula
	 * @return
	 */
	private static String toUserDefCond(UfoFmlExecutor handler,String strFormula,String strDynPK) {
		if (strFormula != null && (strFormula = strFormula.trim()).length() > 0) {
			try {
				return handler.convertFormula(strFormula, strDynPK,false);
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}
		return strFormula;
	}

	/**
	 * ת��Ϊ�û�����Ĺ�ʽ����
	 * @param strFormula
	 * @return
	 */
	private static String toDBStoredCond(UfoFmlExecutor handler,String strFormula,String strDynPK) {
		if (strFormula != null && (strFormula = strFormula.trim()).length() > 0) {
			if (isErrorFormula(strFormula)) {
				strFormula = markFormula(strFormula, true);
			}
			try {
				return handler.convertFormula(strFormula, strDynPK,true);
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		}
		return markFormula(strFormula, false);
	}

	/**
	 * ת�����û��ɼ�����˹�ʽ����
	 * @param handler
	 * @param strCheck
	 * @return
	 */
	private static String toUserDefCheck(UfoFmlExecutor handler,String strCheck) {
		//��������������﷨���
		if (strCheck != null && (strCheck = strCheck.trim()).length() > 0) {
			try {
				return handler.parseRepCheckFormula(strCheck, false);
			} catch (Exception e) {
				AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			}
		}
		return strCheck;
	}

	/**
	 * ת�������ݿⱣ�����˹�ʽ����
	 * @param handler
	 * @param strCheck
	 * @return
	 */
	private static String toDBStoredCheck(UfoFmlExecutor handler,String strCheck) {
		//��������������﷨���
		if (strCheck != null && (strCheck = strCheck.trim()).length() > 0) {
			try {
				return handler.parseRepCheckFormula(strCheck, true);

			} catch (Exception e) {
				AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
			}
		}
		return strCheck;
	}
}
 