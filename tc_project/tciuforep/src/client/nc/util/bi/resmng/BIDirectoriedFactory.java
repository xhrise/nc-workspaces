/*
 * 创建日期 2006-3-16
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class BIDirectoriedFactory {
	private BIDirectoriedFactory(){
		super();
	}
	/**
	 * IUFO系统所有的IDirectoried的集合类
	 */
	private static HashMap<String, IDirectoried> s_mapIDirectoried = new HashMap<String, IDirectoried>(3);
	
	static{		
		try {
			//注意：每增加一个模块的缺省树装载器,就得增加相应的注册代码
			
			//注册各个模块的缺省树装载器和表装载器
			registIDirectoried(IBIResMngConstants.MODULE_REPORT);
			registIDirectoried(IBIResMngConstants.MODULE_FREEQUERY);
			
			registIDirectoried(IBIResMngConstants.MODULE_DIMENSION);
			
			registIDirectoried(IBIResMngConstants.MODULE_QUERY);
			
		} catch (NoSuchIDirectoriedException e) {
			//应该从来不会发生
			AppDebug.debug(e);//@devTools e.printStackTrace(System.out);
		}	
	}
	/**
	 * 得到模块的缺省IFiled
	 * @param strModuleID
	 * @return
	 */
	public static IDirectoried getDefaultIDirectoried(String strModuleID){
		//获得缺省IFiled的实例
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
