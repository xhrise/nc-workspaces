/*
 * 创建日期 2006-3-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
 *  BI 文件服务Factory
 */
public class BIFiledFactory {
	private BIFiledFactory(){
		super();
	}
	private static HashMap<String, IFiled> s_mapIFiled = new HashMap<String, IFiled>(5);
	static{		
		try {
			//注意：每增加一个模块的缺省树装载器,就得增加相应的注册代码
			
			//注册各个模块的缺省树装载器和表装载器
			registIFiled(IBIResMngConstants.MODULE_QUERY);
			registIFiled(IBIResMngConstants.MODULE_REPORT);		
			registIFiled(IBIResMngConstants.MODULE_FREEQUERY);		
			registIFiled(IBIResMngConstants.MODULE_DIMENSION);		
			registIFiled(IBIExPropConstants.EXPROP_MODULE_DIMENSION);	
		} catch (NoSuchIFiledException e) {
			//应该从来不会发生
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
		}	
	}
	/**
	 * 得到模块的缺省IFiled
	 * @param strModuleID
	 * @return
	 */
	public static IFiled getDefaultIFiled(String strModuleID){
		//获得缺省IFiled的实例
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
