package com.ufsoft.iufo.fmtplugin.pluginregister;

import java.util.Hashtable;

import nc.ui.iufo.query.datasetmanager.DataSetManagerPlugin;

import com.ufida.dataset.Context;
import com.ufsoft.iufo.fmtplugin.businessquery.BusinessQueryPlugin;
import com.ufsoft.iufo.fmtplugin.dataexplorer.DataExplorerPlugin;
import com.ufsoft.iufo.fmtplugin.dataprocess.DataProcessPlugin;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaDefPlugIn;
import com.ufsoft.iufo.fmtplugin.formatcore.FormatCorePlugin;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.formula.FormulaDefPlugin;
import com.ufsoft.iufo.fmtplugin.key.KeyDefPlugin;
import com.ufsoft.iufo.fmtplugin.location.LocationPlugin;
import com.ufsoft.iufo.fmtplugin.measure.MeasureDefPlugIn;
import com.ufsoft.iufo.fmtplugin.monitor.MonitorPlugin;
import com.ufsoft.iufo.fmtplugin.rounddigitarea.RoundDigitAreaPlugin;
import com.ufsoft.iufo.fmtplugin.statusshow.StatusShowPlugin;
import com.ufsoft.iufo.fmtplugin.sumfunc.SumFuncPlugin;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.PluginRegister;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.component.ProcessController;
import com.ufsoft.report.exception.MessageException;
import com.ufsoft.report.sysplugin.cellattr.CellAttrPlugin;
import com.ufsoft.report.sysplugin.combinecell.CombineCellPlugin;
import com.ufsoft.report.sysplugin.edit.EditPlugin;
import com.ufsoft.report.sysplugin.excel.ExcelExpPlugin;
import com.ufsoft.report.sysplugin.excel.ExcelImpPlugin;
import com.ufsoft.report.sysplugin.excel.IPostProcessImpExcel;
import com.ufsoft.report.sysplugin.file.FileSaveExt;
import com.ufsoft.report.sysplugin.fill.FillPlugin;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headerlock.HeaderLockPlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.insertdelete.InsertDeletePlugin;
import com.ufsoft.report.sysplugin.insertimg.InsertDelImgPlugin;
import com.ufsoft.report.sysplugin.log.LogPlugin;
import com.ufsoft.report.sysplugin.pagination.PaginationPlugin;
import com.ufsoft.report.sysplugin.postil.PostilPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.repname.RepNamePlugin;
import com.ufsoft.report.sysplugin.style.StylePlugin;
import com.ufsoft.report.sysplugin.view.ViewMngExt;
import com.ufsoft.report.sysplugin.view.ViewMngPlugin;
import com.ufsoft.report.toolbar.CellAttrToolBarPlugIn;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.ForbidedOprException;
import com.ufsoft.table.undo.UndoPlugin;
 
public class ReportFormatPluginRegister extends PluginRegister implements IUfoContextKey{

	public ReportFormatPluginRegister() {
		super();
	}
	
	public ReportFormatPluginRegister(Hashtable params) {
		super(params);
	}

	public void register() {
		getReport().addPlugIn(FormatCorePlugin.class.getName());//格式验证
		getReport().addPlugIn(RepNamePlugin.class.getName());
		
		//#文件菜单
		getReport().addActionExt(new FileSaveExt(getReport()){//保存
			public UfoCommand getCommand() {
				return new UfoCommand(){
					/**
					 * @i18n miufopublic391=保存成功
					 */
					public void execute(Object[] params) {
						
						//唉，用线程做保存操作造成保存的同时还能更改模型，而且出了异常还不能通过report框架处理。 
						//liuyy+ 
						
						ProcessController controller = new ProcessController(true);
						controller.setRunnable(new Runnable() {
							public void run() {
								try{
									String saveResult = save();
									if(saveResult != null && saveResult.length() > 0){
										getReport().getStatusBar().setHintMessage(saveResult);
										throw new ForbidedOprException(saveResult);
										
									}else{
										String msg = StringResource.getStringResource("miufopublic391");
										getReport().getStatusBar().setHintMessage(msg);
	//									throw new MessageException(MessageException.TYPE_INFO, msg);
									}
									
								} catch(Throwable e){
									UfoPublic.sendMessage(e, getReport());
									
								}
							}
						});
						controller.setString(MultiLang.getString("save"));
						getReport().getStatusBar().setProcessDisplay(controller);
						
					}					
				};
			}    		
    	});
		
		getReport().addPlugIn(ExcelImpPlugin.class.getName());
        getReport().addPlugIn(ExcelExpPlugin.class.getName());
        getReport().addPlugIn(DataExplorerPlugin.class.getName());
        getReport().addPlugIn(PrintPlugin.class.getName());
        
        //#编辑菜单
        //modify by 王宇光 关掉了此插件的加载，剪切，复制，粘贴，清楚等插件都在report项目
//        getReport().addPlugIn(Patch31Plugin.class.getName());//工具栏编辑组的“剪切、复制、粘贴、清除；

		getReport().addPlugIn(UndoPlugin.class.getName());
        
		getReport().addPlugIn(EditPlugin.class.getName());
        getReport().addPlugIn(InsertDeletePlugin.class.getName());
	    getReport().addPlugIn(InsertDelImgPlugin.class.getName());
	    getReport().addPlugIn(LocationPlugin.class.getName());//定位插件 add by 王宇光 2008-5-23
	    getReport().addPlugIn(FindReplacePlugin.class.getName());
	    getReport().addPlugIn(FillPlugin.class.getName());
        getReport().addPlugIn(SumFuncPlugin.class.getName());
        
        getReport().addPlugIn(CombineCellPlugin.class.getName());
        
        //保证单元属性各工具栏在公式内容工具栏插件前
        //add by guogang 2007-6-1 工具栏的下拉菜单
        getReport().addPlugIn(CellAttrToolBarPlugIn.class.getName());
        //end add
        //保证‘单元属性’右键在前
        getReport().addPlugIn(CellAttrPlugin.class.getName());
        //保证‘组合单元’右键在前
       

        getReport().addPlugIn(KeyDefPlugin.class.getName());      
        getReport().addPlugIn(FormulaDefPlugin.class.getName());
        getReport().addPlugIn(MeasureDefPlugIn.class.getName());
        
        getReport().addPlugIn(RoundDigitAreaPlugin.class.getName());
        getReport().addPlugIn(StylePlugin.class.getName());
        getReport().addPlugIn(PostilPlugin.class.getName());
        getReport().addPlugIn(PaginationPlugin.class.getName());
        getReport().addPlugIn(HeaderLockPlugin.class.getName());
        getReport().addPlugIn(HeaderSizePlugin.class.getName());
        getReport().addPlugIn(DynAreaDefPlugIn.class.getName());
        	
    	getReport().addPlugIn(StatusShowPlugin.class.getName());
    	
        //#数据菜单
        getReport().addPlugIn(BusinessQueryPlugin.class.getName());
        getReport().addPlugIn(LogPlugin.class.getName());
        getReport().addPlugIn(DataProcessPlugin.class.getName());
        
        //分析表不显示数据集管理菜单
        UfoContextVO ctx = (UfoContextVO)getReport().getContextVo();
        boolean isAnaRep = ctx.getAttribute(ANA_REP) == null ? false : Boolean.parseBoolean(ctx.getAttribute(ANA_REP).toString());       	
    	
        if(!isAnaRep){
        	getReport().addPlugIn(DataSetManagerPlugin.class.getName());	
        }

        //#窗口菜单 add by guogang 2007-7-20
        getReport().addPlugIn(ViewMngPlugin.class.getName());
        
        //#帮助菜单

        getReport().addPlugIn(MonitorPlugin.class.getName());
        
        getReport().addPlugIn(HelpPlugin.class.getName());
        
    
//      zzj确认，报表工具applet不支持xml导入导出。通过web进行xml交互操作。
//        getReport().addPlugIn(RepFmtXMLImpExtPlugin.class.getName());
        
        ExcelImpPlugin excelImpPlugin = (ExcelImpPlugin) getReport().getPluginManager().getPlugin(ExcelImpPlugin.class.getName());
        excelImpPlugin.registerPostProcessImpExcel(new IPostProcessImpExcel(){
			public void dealAfterImpExcel() {
				CellsModelOperator.initModelProperties((UfoContextVO)getReport().getContextVo(), getReport().getCellsModel());
				
			}        	
        });
        
      
	}
	
	   /**
	 * @i18n miufo01437=系统预制模板不允许修改！
	 */
	private String save(){
	    	try {
	    		Context context =  getReport().getContextVo();
	    		if((Integer)context.getAttribute(FORMAT_RIGHT) == UfoContextVO.RIGHT_FORMAT_READ){
	    			String errMsg = StringResource.getStringResource("uiiufofmt00014");
	    			Object modelObj = context.getAttribute(MODEL);
	    			boolean bModel = modelObj == null ? false : (Boolean)modelObj;
	    			
	    			if(bModel){
	    				errMsg = StringResource.getStringResource("miufo01437");
	    			}
	    			 
	    			getReport().setDirty(false);

	    			throw new MessageException(errMsg);
	    			
	    		}
//	    		if(contextVO.getFormatRight() == UfoContextVO.RIGHT_FORMAT_PERSONAL){
//	    			UfoPublic.showConfirmDialog(this, "提示：您对当前报表的修改只有个性化公式可以保存成功！");
//	    		}
	    	 
	    		
	    		FormatCorePlugin formatCorePlugin = (FormatCorePlugin)getReport().getPluginManager().getPlugin(FormatCorePlugin.class.getName());
	            if(!formatCorePlugin.verifyBeforeSave()){
	                return "";
	            }
	    		if(isDirty()){
	    			 
	    			getReport().setDirty(!CellsModelOperator.saveReportFormat(getReport().getContextVo(), getReport().getCellsModel()));
	    		}
	    		
	    	} catch(MessageException e){
	    		throw e;
	        	
			} catch (Throwable e) {
//				AppDebug.debug(e);
				throw new MessageException(e.getMessage());
				
			}	
			return "";
	    }
	   
	   private boolean isDirty(){
	    	return getReport().isDirty() || getReport().getCellsModel().getPrintSet().isDirty();
	    }
	

	
}
