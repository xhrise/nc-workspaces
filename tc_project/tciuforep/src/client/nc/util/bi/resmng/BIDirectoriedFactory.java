/*
 * �������� 2006-3-16
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.util.bi.resmng;
import java.util.HashMap;

import nc.us.bi.integration.dimension.DimensionSrv;
import nc.us.bi.query.manager.QuerySrv;
import nc.us.bi.report.manager.BIFreeQueryReportSrv;
import nc.us.bi.report.manager.BIReportSrv;
import nc.util.iufo.resmng.IDirectoried;
import nc.util.iufo.resmng.NoSuchIDirectoriedException;
import nc.util.iufo.resmng.NoSuchIFiledException;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zyjun
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class BIDirectoriedFactory {
	private BIDirectoriedFactory(){
		super();
	}
	/**
	 * IUFOϵͳ���е�IDirectoried�ļ�����
	 */
	private static HashMap<String, IDirectoried> s_mapIDirectoried = new HashMap<String, IDirectoried>(3);
	
	static{		
		try {
			//ע�⣺ÿ����һ��ģ���ȱʡ��װ����,�͵�������Ӧ��ע�����
			
			//ע�����ģ���ȱʡ��װ�����ͱ�װ����
			registIDirectoried(IBIResMngConstants.MODULE_REPORT);
			registIDirectoried(IBIResMngConstants.MODULE_FREEQUERY);
			
			registIDirectoried(IBIResMngConstants.MODULE_DIMENSION);
			
			registIDirectoried(IBIResMngConstants.MODULE_QUERY);
			
		} catch (NoSuchIDirectoriedException e) {
			//Ӧ�ô������ᷢ��
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
		}	
	}
	/**
	 * �õ�ģ���ȱʡIFiled
	 * @param strModuleID
	 * @return
	 */
	public static IDirectoried getDefaultIDirectoried(String strModuleID){
		//���ȱʡIFiled��ʵ��
		if(strModuleID == null){
			return null;
		}
		return (IDirectoried)s_mapIDirectoried.get(strModuleID);
	}
	/**
	 * @param module_analysis_report
	 * @throws NoSuchIFiledException
	 * @throws NoSuchIDirectoriedException
	 */
	private static void registIDirectoried(String strModuleID) throws NoSuchIDirectoriedException {
		IDirectoried itfDirectoried = null;
		if(strModuleID.equals(IBIResMngConstants.MODULE_DIMENSION) ){
			itfDirectoried = DimensionSrv.getInstance();
		}else if(strModuleID.equals(IBIResMngConstants.MODULE_REPORT) ){
			itfDirectoried = BIReportSrv.getInstance();
		}else if(strModuleID.equals(IBIResMngConstants.MODULE_FREEQUERY) ){
			itfDirectoried = BIFreeQueryReportSrv.getInstance();
		}else if(strModuleID.equals(IBIResMngConstants.MODULE_QUERY) ){
			itfDirectoried = QuerySrv.getInstance();
		}else{
			//no IDirectoried
			throw new NoSuchIDirectoriedException();
		}
		s_mapIDirectoried.put(strModuleID,itfDirectoried);
	}

}
