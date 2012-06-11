package com.ufsoft.iuforeport;


@Deprecated
public class UfoReportUtil {

//	/**
//	 * 初始化表格控件的插件
//	 */
//	public static void initPlugins(UfoReport ufoReport, int type, Hashtable<String, Object> params) {
//		if(ufoReport == null){
//			return;
//		}
//		switch (type) {
////			case UfoReport.OPERATION_INPUT:
////				tableinput(ufoReport, params);
////				break;
////			case UfoReport.OPERATION_TOTALSOURCE:
////				tableTotalSource(ufoReport);
////				break;
//			case UfoReport.OPERATION_PRINT:
//				tablePrint(ufoReport);
//			default:
//				break;
//		}
//		
//	}
//	private static void tablePrint(UfoReport report){
//		report.addPlugIn(FilePlugin.class.getName());
//	    report.addPlugIn(EditPlugin.class.getName());
//	    report.addPlugIn(DndPlugin.class.getName());
//	    report.addPlugIn(HeaderLockPlugin.class.getName());
//	    report.addPlugIn(HelpPlugin.class.getName());
//	    report.addPlugIn(CombineCellPlugin.class.getName());
//	    report.addPlugIn(CellAttrPlugin.class.getName());
//	    report.addPlugIn(HeaderSizePlugin.class.getName());
//	    report.addPlugIn(LocationPlugin.class.getName());
//	    report.addPlugIn(LogPlugin.class.getName());
//	    report.addPlugIn(ComboBoxPlugIn.class.getName());
//	    report.addPlugIn(PrintPlugin.class.getName());
//	    report.addPlugIn(StylePlugin.class.getName());
//	    
//	    report.addPlugIn(PostilPlugin.class.getName());
//	    report.addPlugIn(PaginationPlugin.class.getName());
//	    report.addPlugIn(FindReplacePlugin.class.getName());
//
//	    report.addPlugIn(RepNamePlugin.class.getName());
//	    
//	    report.addPlugIn(InsertDeletePlugin.class.getName());
//	    report.addPlugIn(InsertDelImgPlugin.class.getName());
//	}
	
//	private static void tableTotalSource(UfoReport ufoReport){
//		ufoReport.addPlugIn(PrintPlugin.class.getName());        //打印相关菜单：页面设置，打印预览，打印
//		ufoReport.addPlugIn(TotalSourcePlugin.class.getName());
//
//      //区域计算，计算，审核，全部审核;
//      //汇总下级，查看下级数据，查看来源；
//      //导入Excel数据，导入Ufo数据，导入Iufo数据；
//      //导出Excel格式，导出Html格式
//	//	ufoReport.addPlugIn(InputDataPlugIn.class.getName());
//      //#帮助
//		
//	}
//
//	private static void reportformat(final UfoReport report,  Hashtable<String, Object> params){
//		//#文件菜单
//		report.addPlugIn(FormatCorePlugin.class.getName());
//		report.addPlugIn(RepNamePlugin.class.getName());
//		report.addActionExt(new FileSaveExt(report){//保存
//			public UfoCommand getCommand() {
//				return new UfoCommand(){
//					public void execute(Object[] params) {
//						ProcessController controller = new ProcessController(true);
//						controller.setRunnable(new Runnable() {
//							public void run() {
//								String saveResult = save();
//								if(saveResult != null && saveResult.length() > 0){
//									UfoPublic.showConfirmDialog(getReport(),saveResult);
//								}							
//							}
//						});
//						controller.setString(MultiLang.getString("save"));
//						report.getStatusBar().setProcessDisplay(controller);
//					}					
//				};
//			}    		
//    	});
//		
//		//#编辑菜单
//		report.addPlugIn(EditPlugin.class.getName());
//        report.addPlugIn(FindReplacePlugin.class.getName());
//	    report.addPlugIn(InsertDeletePlugin.class.getName());
//	    report.addPlugIn(InsertDelImgPlugin.class.getName());
//        report.addPlugIn(FillPlugin.class.getName());
//		
//		//#格式菜单
//        report.addPlugIn(CellAttrPlugin.class.getName());
//        report.addPlugIn(DynAreaDefPlugIn.class.getName());
//        report.addPlugIn(CombineCellPlugin.class.getName());
//        report.addPlugIn(PostilPlugin.class.getName());
//        report.addPlugIn(RoundDigitAreaPlugin.class.getName());
//        report.addPlugIn(HeaderLockPlugin.class.getName());
//        report.addPlugIn(HeaderSizePlugin.class.getName());
//        report.addPlugIn(StylePlugin.class.getName());
//        //add by guogang 2007-6-1 工具栏的下拉菜单
//        report.addPlugIn(ComboBoxPlugIn.class.getName());
//        //end add
//		
//        //#添加插件    	
//        report.addPlugIn(ExcelImpExpPlugin.class.getName());
//    	report.addPlugIn(StatusShowPlugin.class.getName());
////        report.addPlugIn(LocationPlugin.class.getName());
//        
//        report.addPlugIn(PaginationPlugin.class.getName());
//        report.addPlugIn(PrintPlugin.class.getName());
////      zzj确认，报表工具applet不支持xml导入导出。通过web进行xml交互操作。
////        report.addPlugIn(RepFmtXMLImpExtPlugin.class.getName());
//        report.addPlugIn(SumFuncPlugin.class.getName());
//        report.addPlugIn(DataExplorerPlugin.class.getName());
////        report.addPlugIn(RepNamePlugin.class.getName());
//        ExcelImpExpPlugin excelPlugin = (ExcelImpExpPlugin) report.getPluginManager().getPlugin(ExcelImpExpPlugin.class.getName());
//        excelPlugin.registerPostProcessImpExcel(new IPostProcessImpExcel(){
//			public void dealAfterImpExcel() {
//				CellsModelOperator.initModelProperties((UfoContextVO)report.getContextVo(), report.getCellsModel());
//				
//			}        	
//        });
//        
//        //#数据菜单
//        report.addPlugIn(BusinessQueryPlugin.class.getName());
//        report.addPlugIn(KeyDefPlugin.class.getName());
//        report.addPlugIn(MeasureDefPlugIn.class.getName());
//        report.addPlugIn(DataProcessPlugin.class.getName());
//        report.addPlugIn(FormulaDefPlugin.class.getName());
//       
//        //#窗口菜单 add by guogang 2007-7-20
//        report.addActionExt(new ViewMngExt(report));
//        
//        //#帮助菜单
//        report.addPlugIn(HelpPlugin.class.getName());
//        report.addPlugIn(LogPlugin.class.getName());
//    
//	}
//	
//	private static void tableinput(UfoReport ufoReport,  Hashtable<String, Object> params){
//
//		ufoReport.addPlugIn(InputCorePlugin.class.getName());
//		ufoReport.addPlugIn(KeyInputPlugin.class.getName());     //关键字录入
//		ufoReport.addPlugIn(MeasureInputPlugin.class.getName()); //指标录入
//		ufoReport.addPlugIn(DynAreaInputPlugin.class.getName()); //动态区录入
//      //#文件
//		ufoReport.addPlugIn(InputFilePlugIn.class.getName());    //报表选择，保存，退出,切换关键字
//		ufoReport.addPlugIn(PrintPlugin.class.getName());        //打印相关菜单：页面设置，打印预览，打印
//      //#编辑
//		ufoReport.addPlugIn(EditPlugin.class.getName());         //编辑组的“剪切、复制、粘贴、删除；清除，
//		ufoReport.addPlugIn(Patch31Plugin.class.getName());      //工具栏编辑组的“剪切、复制、粘贴、清除；”
//      //m_oUfoReport.addPlugIn(FindReplacePlugin.class.getName());//查找，替换
//		ufoReport.addPlugIn(LocationPlugin.class.getName());     //定位
//      //#格式
//		ufoReport.addPlugIn(StylePlugin.class.getName());        //显示风格
//		ufoReport.addPlugIn(HeaderSizePlugin.class.getName());   //行高，列宽
//		ufoReport.addPlugIn(HeaderLockPlugin.class.getName());   //（//冻结窗口，取消冻结
//      //#数据:
//      //公式追踪
//		ufoReport.addPlugIn(FormulaTracePlugIn.class.getName());
//      //区域计算，计算，审核，全部审核;
//      //汇总下级，查看下级数据，查看来源；
//      //导入Excel数据，导入Ufo数据，导入Iufo数据；
//      //导出Excel格式，导出Html格式
//		ufoReport.addPlugIn(InputDataPlugIn.class.getName());
//		addExcelExpPlugIn(ufoReport);
//
//	  //自由查询
//      TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
//      if(inputContextVO.isGenralQuery()){
//    	  ufoReport.addPlugIn(FreeQueryPlugin.class.getName());
//      }
//
//      //#帮助
//		ufoReport.addPlugIn(HelpPlugin.class.getName());         //帮助，关于
//		ufoReport.addPlugIn(LogPlugin.class.getName());          //日志
//		ufoReport.addPlugIn(RepNamePlugin.class.getName());
//		
//      boolean bRepData = params.get(ITableInputAppletParam.PARAM_REPDATA) != null;
//      boolean bRepDataByRepView = false;
// 	  if(bRepData){
//         String strOperType = (String) params.get(ITableInputAppletParam.PARAM_OPERTYPE);
//         bRepDataByRepView = TableInputParam.OPERTYPE_REPDATA_VIEW_REP2.equalsIgnoreCase(strOperType);
// 	  }
// 	  
//      if(!(!bRepData || bRepDataByRepView)){ 
//    	  //!(isPrint() || isMQuery() || isHBDraft())
//    	  ufoReport.addPlugIn(RepSelectionPlugIn.class.getName());
//      }
//      ufoReport.addPlugIn(InputCheckPlugIn.class.getName());
//      ufoReport.addPlugIn(InputAutoCalcPlugIn.class.getName());      
//      
//      ufoReport.addPlugIn(WindowMngPlugin.class.getName());
//      ufoReport.addPlugIn(QueryNavigationPlugin.class.getName());
//      ufoReport.addPlugIn(QueryNextPlugin.class.getName());
//      
//	}
//	
//	private static void addExcelExpPlugIn(UfoReport ufoReport){
//		ufoReport.addPlugIn(ExcelExpPlugin.class.getName());
//		ExcelExpPlugin excelExpPlugin = (ExcelExpPlugin) ufoReport.getPluginManager().getPlugin(ExcelExpPlugin.class.getName());
//		IPostProcessor postProcessor=new RepDataPostProcessor();
//		postProcessor.setUfoReport(ufoReport);
//		excelExpPlugin.registerPostProcessExpExcel(postProcessor);
//	}
//	
}
