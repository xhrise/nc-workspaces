package com.ufsoft.iuforeport;


@Deprecated
public class UfoReportUtil {

//	/**
//	 * ��ʼ�����ؼ��Ĳ��
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
//		ufoReport.addPlugIn(PrintPlugin.class.getName());        //��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
//		ufoReport.addPlugIn(TotalSourcePlugin.class.getName());
//
//      //������㣬���㣬��ˣ�ȫ�����;
//      //�����¼����鿴�¼����ݣ��鿴��Դ��
//      //����Excel���ݣ�����Ufo���ݣ�����Iufo���ݣ�
//      //����Excel��ʽ������Html��ʽ
//	//	ufoReport.addPlugIn(InputDataPlugIn.class.getName());
//      //#����
//		
//	}
//
//	private static void reportformat(final UfoReport report,  Hashtable<String, Object> params){
//		//#�ļ��˵�
//		report.addPlugIn(FormatCorePlugin.class.getName());
//		report.addPlugIn(RepNamePlugin.class.getName());
//		report.addActionExt(new FileSaveExt(report){//����
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
//		//#�༭�˵�
//		report.addPlugIn(EditPlugin.class.getName());
//        report.addPlugIn(FindReplacePlugin.class.getName());
//	    report.addPlugIn(InsertDeletePlugin.class.getName());
//	    report.addPlugIn(InsertDelImgPlugin.class.getName());
//        report.addPlugIn(FillPlugin.class.getName());
//		
//		//#��ʽ�˵�
//        report.addPlugIn(CellAttrPlugin.class.getName());
//        report.addPlugIn(DynAreaDefPlugIn.class.getName());
//        report.addPlugIn(CombineCellPlugin.class.getName());
//        report.addPlugIn(PostilPlugin.class.getName());
//        report.addPlugIn(RoundDigitAreaPlugin.class.getName());
//        report.addPlugIn(HeaderLockPlugin.class.getName());
//        report.addPlugIn(HeaderSizePlugin.class.getName());
//        report.addPlugIn(StylePlugin.class.getName());
//        //add by guogang 2007-6-1 �������������˵�
//        report.addPlugIn(ComboBoxPlugIn.class.getName());
//        //end add
//		
//        //#��Ӳ��    	
//        report.addPlugIn(ExcelImpExpPlugin.class.getName());
//    	report.addPlugIn(StatusShowPlugin.class.getName());
////        report.addPlugIn(LocationPlugin.class.getName());
//        
//        report.addPlugIn(PaginationPlugin.class.getName());
//        report.addPlugIn(PrintPlugin.class.getName());
////      zzjȷ�ϣ�������applet��֧��xml���뵼����ͨ��web����xml����������
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
//        //#���ݲ˵�
//        report.addPlugIn(BusinessQueryPlugin.class.getName());
//        report.addPlugIn(KeyDefPlugin.class.getName());
//        report.addPlugIn(MeasureDefPlugIn.class.getName());
//        report.addPlugIn(DataProcessPlugin.class.getName());
//        report.addPlugIn(FormulaDefPlugin.class.getName());
//       
//        //#���ڲ˵� add by guogang 2007-7-20
//        report.addActionExt(new ViewMngExt(report));
//        
//        //#�����˵�
//        report.addPlugIn(HelpPlugin.class.getName());
//        report.addPlugIn(LogPlugin.class.getName());
//    
//	}
//	
//	private static void tableinput(UfoReport ufoReport,  Hashtable<String, Object> params){
//
//		ufoReport.addPlugIn(InputCorePlugin.class.getName());
//		ufoReport.addPlugIn(KeyInputPlugin.class.getName());     //�ؼ���¼��
//		ufoReport.addPlugIn(MeasureInputPlugin.class.getName()); //ָ��¼��
//		ufoReport.addPlugIn(DynAreaInputPlugin.class.getName()); //��̬��¼��
//      //#�ļ�
//		ufoReport.addPlugIn(InputFilePlugIn.class.getName());    //����ѡ�񣬱��棬�˳�,�л��ؼ���
//		ufoReport.addPlugIn(PrintPlugin.class.getName());        //��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
//      //#�༭
//		ufoReport.addPlugIn(EditPlugin.class.getName());         //�༭��ġ����С����ơ�ճ����ɾ���������
//		ufoReport.addPlugIn(Patch31Plugin.class.getName());      //�������༭��ġ����С����ơ�ճ�����������
//      //m_oUfoReport.addPlugIn(FindReplacePlugin.class.getName());//���ң��滻
//		ufoReport.addPlugIn(LocationPlugin.class.getName());     //��λ
//      //#��ʽ
//		ufoReport.addPlugIn(StylePlugin.class.getName());        //��ʾ���
//		ufoReport.addPlugIn(HeaderSizePlugin.class.getName());   //�иߣ��п�
//		ufoReport.addPlugIn(HeaderLockPlugin.class.getName());   //��//���ᴰ�ڣ�ȡ������
//      //#����:
//      //��ʽ׷��
//		ufoReport.addPlugIn(FormulaTracePlugIn.class.getName());
//      //������㣬���㣬��ˣ�ȫ�����;
//      //�����¼����鿴�¼����ݣ��鿴��Դ��
//      //����Excel���ݣ�����Ufo���ݣ�����Iufo���ݣ�
//      //����Excel��ʽ������Html��ʽ
//		ufoReport.addPlugIn(InputDataPlugIn.class.getName());
//		addExcelExpPlugIn(ufoReport);
//
//	  //���ɲ�ѯ
//      TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
//      if(inputContextVO.isGenralQuery()){
//    	  ufoReport.addPlugIn(FreeQueryPlugin.class.getName());
//      }
//
//      //#����
//		ufoReport.addPlugIn(HelpPlugin.class.getName());         //����������
//		ufoReport.addPlugIn(LogPlugin.class.getName());          //��־
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
