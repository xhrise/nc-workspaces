package com.ufsoft.iufo.fmtplugin.pluginregister;

import java.util.Hashtable;

import nc.ui.iufo.input.table.TableInputParam;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.window.WindowMngPlugin;
import com.ufsoft.iufo.inputplugin.biz.FormulaTracePlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputAutoCalcPlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputCheckPlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputDataPlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.RepSelectionPlugIn;
import com.ufsoft.iufo.inputplugin.biz.data.RepDataPostProcessor;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaInputPlugin;
import com.ufsoft.iufo.inputplugin.inputcore.InputCorePlugin;
import com.ufsoft.iufo.inputplugin.key.KeyInputPlugin;
import com.ufsoft.iufo.inputplugin.measure.MeasureInputPlugin;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigationPlugin;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNextPlugin;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.report.PluginRegister;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.sysplugin.edit.EditPlugin;
import com.ufsoft.report.sysplugin.excel.ExcelExpPlugin;
import com.ufsoft.report.sysplugin.excel.IPostProcessor;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headerlock.HeaderLockPlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.location.LocationPlugin;
import com.ufsoft.report.sysplugin.postil.PostilPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.style.StylePlugin;
import com.ufsoft.table.undo.UndoPlugin;

public class TableInputPluginRegister extends PluginRegister implements IUfoContextKey {

	public TableInputPluginRegister() {
	}
	
	public TableInputPluginRegister(Hashtable params) {
		super(params);
	}

	public void register() {
		UfoReport ufoReport = getReport();
		

		ufoReport.addPlugIn(UndoPlugin.class.getName());
		
		ufoReport.addPlugIn(InputCorePlugin.class.getName());
		ufoReport.addPlugIn(KeyInputPlugin.class.getName());     //关键字录入
		ufoReport.addPlugIn(MeasureInputPlugin.class.getName()); //指标录入	
		//#文件
		ufoReport.addPlugIn(InputFilePlugIn.class.getName());    //报表选择，保存，退出,切换关键字
		ufoReport.addPlugIn(PrintPlugin.class.getName());        //打印相关菜单：页面设置，打印预览，打印
		//#编辑
		ufoReport.addPlugIn(EditPlugin.class.getName());         //编辑组的“剪切、复制、粘贴、删除；清除，
		//modify by 王宇光 2008-4-30 关掉了此类，剪切，粘贴，复制，清楚等编辑插件都在report项目下
//		ufoReport.addPlugIn(Patch31Plugin.class.getName());      //工具栏编辑组的“剪切、复制、粘贴、清除；”
		ufoReport.addPlugIn(DynAreaInputPlugin.class.getName()); //动态区录入 add by 王宇光 2008-4-8 调整了此插件的加载顺序
		ufoReport.addPlugIn(FindReplacePlugin.class.getName());//查找，替换
		ufoReport.addPlugIn(LocationPlugin.class.getName());     //定位
		//#格式
		ufoReport.addPlugIn(StylePlugin.class.getName());        //显示风格
		ufoReport.addPlugIn(HeaderSizePlugin.class.getName());   //行高，列宽
		ufoReport.addPlugIn(HeaderLockPlugin.class.getName());   //（//冻结窗口，取消冻结
		//#数据:
		//公式追踪
		ufoReport.addPlugIn(FormulaTracePlugIn.class.getName());
		//区域计算，计算，审核，全部审核;
		//汇总下级，查看下级数据，查看来源；
		//导入Excel数据，导入Ufo数据，导入Iufo数据；	 	
		ufoReport.addPlugIn(InputDataPlugIn.class.getName());

		addExcelExpPlugIn();

		//自由查询
		TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
		Object genralQueryObj = inputContextVO.getAttribute(GENRAL_QUERY);
		boolean isgenralQuery = genralQueryObj == null ? false : Boolean.parseBoolean(genralQueryObj.toString());
		
		if(isgenralQuery){
			ufoReport.addPlugIn("com.ufida.report.anareport.applet.AnaQeuryPlugin");//在上级包中
		}

		//#帮助
		ufoReport.addPlugIn(HelpPlugin.class.getName());         //帮助，关于
//		ufoReport.addPlugIn(LogPlugin.class.getName());          //日志
//		ufoReport.addPlugIn(RepNamePlugin.class.getName());

		boolean bRepData = getParamter(ITableInputAppletParam.PARAM_REPDATA) != null;
		boolean bRepDataByRepView = false;
		if(bRepData){
			String strOperType = (String) getParamter(ITableInputAppletParam.PARAM_OPERTYPE);
			bRepDataByRepView = TableInputParam.OPERTYPE_REPDATA_VIEW_REP2.equalsIgnoreCase(strOperType);
		}

		if(!(!bRepData || bRepDataByRepView)){ 
			//!(isPrint() || isMQuery() || isHBDraft())
			ufoReport.addPlugIn(RepSelectionPlugIn.class.getName());
		}
		ufoReport.addPlugIn(InputCheckPlugIn.class.getName());
		ufoReport.addPlugIn(InputAutoCalcPlugIn.class.getName());      

		ufoReport.addPlugIn(WindowMngPlugin.class.getName());
		ufoReport.addPlugIn(QueryNavigationPlugin.class.getName());
		ufoReport.addPlugIn(QueryNextPlugin.class.getName());
		
		//@edit by zhaopq at 2009-1-9,上午09:24:07
		ufoReport.addPlugIn(PostilPlugin.class.getName());

	}
	
	private void addExcelExpPlugIn(){
		UfoReport ufoReport = getReport();
		ufoReport.addPlugIn(ExcelExpPlugin.class.getName());
		ExcelExpPlugin excelExpPlugin = (ExcelExpPlugin) ufoReport.getPluginManager().getPlugin(ExcelExpPlugin.class.getName());
		IPostProcessor postProcessor=new RepDataPostProcessor();
		postProcessor.setUfoReport(ufoReport);
		excelExpPlugin.registerPostProcessExpExcel(postProcessor);
	}
	
}
