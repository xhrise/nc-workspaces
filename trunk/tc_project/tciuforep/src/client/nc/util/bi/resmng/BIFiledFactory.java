/*
 * �������� 2006-3-16
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.util.bi.resmng;
import java.util.HashMap;
import java.util.Hashtable;

import nc.itf.bi.exproperty.IBIExPropConstants;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.us.bi.query.manager.QuerySrv;
import nc.us.bi.report.manager.BIFreeQueryReportSrv;
import nc.us.bi.report.manager.BIReportSrv;
import nc.util.iufo.resmng.IFiled;
import nc.util.iufo.resmng.NoSuchIFiledException;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * @author zyjun
 *
 *  BI �ļ�����Factory
 */
public class BIFiledFactory {
	private BIFiledFactory(){
		super();
	}
	private static HashMap<String, IFiled> s_mapIFiled = new HashMap<String, IFiled>(5);
	static{		
		try {
			//ע�⣺ÿ����һ��ģ���ȱʡ��װ����,�͵�������Ӧ��ע�����
			
			//ע�����ģ���ȱʡ��װ�����ͱ�װ����
			registIFiled(IBIResMngConstants.MODULE_QUERY);
			registIFiled(IBIResMngConstants.MODULE_REPORT);		
			registIFiled(IBIResMngConstants.MODULE_FREEQUERY);		
			registIFiled(IBIResMngConstants.MODULE_DIMENSION);		
			registIFiled(IBIExPropConstants.EXPROP_MODULE_DIMENSION);	
		} catch (NoSuchIFiledException e) {
			//Ӧ�ô������ᷢ��
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
		}	
	}
	/**
	 * �õ�ģ���ȱʡIFiled
	 * @param strModuleID
	 * @return
	 */
	public static IFiled getDefaultIFiled(String strModuleID){
		//���ȱʡIFiled��ʵ��
		if(strModuleID == null){
			return null;
		}
		return (IFiled)s_mapIFiled.get(strModuleID);
	}
	/**
	 * @param module_analysis_report
	 * @throws NoSuchIFiledException
	 */
	private static void registIFiled(String strModuleID) throws NoSuchIFiledException {
		IFiled itfFiled = null;
		if( strModuleID.equals(IBIResMngConstants.MODULE_QUERY)){
			itfFiled = QuerySrv.getInstance();
		}else if( strModuleID.equals(IBIResMngConstants.MODULE_REPORT)){
			itfFiled = BIReportSrv.getInstance();
		}else if( strModuleID.equals(IBIResMngConstants.MODULE_FREEQUERY)){
			itfFiled = BIFreeQueryReportSrv.getInstance();
		}else if( strModuleID.equals(IBIResMngConstants.MODULE_DIMENSION)){
			itfFiled = DimensionSrv.getInstance();
		}else if (strModuleID.equals(IBIExPropConstants.EXPROP_MODULE_DIMENSION)){
			itfFiled = DimensionSrv.getInstance();
		}else{	
			//no IFiled
			throw new NoSuchIFiledException();
		}
		s_mapIFiled.put(strModuleID,itfFiled);		
	}

}
