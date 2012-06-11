/*
 * 创建日期 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import com.ufsoft.iufo.inputplugin.biz.data.AreaCalExt;
import com.ufsoft.iufo.inputplugin.biz.data.CalExt;
import com.ufsoft.iufo.inputplugin.biz.data.CheckAllRepExt;
import com.ufsoft.iufo.inputplugin.biz.data.CheckRepExt;
import com.ufsoft.iufo.inputplugin.biz.data.ExportData2ExcelExt;
import com.ufsoft.iufo.inputplugin.biz.data.ExportData2HtmlExt;
import com.ufsoft.iufo.inputplugin.biz.data.ImportExcelDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.ImportIufoDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.TraceDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.TraceSubExt;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.hbdraft.HBDraftExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.IPlugIn;
/**
 * 报表录入的数据菜单组插件描述
 * @author liulp
 *
 */
public class InputDataPlugDes extends AbstractPlugDes{

    public InputDataPlugDes(IPlugIn plugin) {
        super(plugin);
    }

    protected IExtension[] createExtensions() {
        //区域计算
        IExtension areaCalExt = new AreaCalExt(getReport());
        //计算
        IExtension calExt = new CalExt(getReport());
        
        IExtension traceExt=new TraceDataExt(getReport());
        
        IExtension traceSubExt=new TraceSubExt(getReport());
        
        //审核
        IExtension checkRepExt = new CheckRepExt(getReport());        
        //全部审核(表间审核)
        IExtension checkAllRepExt = new CheckAllRepExt(getReport());
        
        IExtension hbdraftExt = new HBDraftExt(getReport());
        
//        //汇总下级(?)
//        IExtension colletSubExt = new AbsIufoBizMenuExt(){
//            protected String[] getPaths() {
//                return doGetDataMenuPaths();
//            }
//            protected String getMenuName() {
//                return "汇总下级(?)";
//            }
//            protected UfoCommand doGetCommand(UfoReport ufoReport) {
//                return null;
//            }            
//        };   
//        //查看下级数据(?)
//        IExtension viewSubDataExt = new AbsIufoBizMenuExt(){
//            protected String[] getPaths() {
//                return doGetDataMenuPaths();
//            }
//            protected String getMenuName() {
//                return "查看下级数据(?)";
//            }
//            protected UfoCommand doGetCommand(UfoReport ufoReport) {
//                return null;
//            }            
//        };   
//        //查看来源(?)   
//        IExtension viewDataSrcExt = new AbsIufoBizMenuExt(){
//            protected String[] getPaths() {
//                return doGetDataMenuPaths();
//            }
//            protected String getMenuName() {
//                return "查看来源(?)";
//            }
//            protected UfoCommand doGetCommand(UfoReport ufoReport) {
//                return null;
//            }            
//        };   

        //导入->Iufo数据
        IExtension importIufoDataExt = new ImportIufoDataExt(getReport());
        //导入->Excel数据
        IExtension importExcelDataExt = new ImportExcelDataExt(getReport());
        //导入->Ufo数据
//        IExtension importUfoDataExt = new ImportUfoDataExt(getReport());
        //导出->Excel格式
//        IExtension exportData2ExcelExt = new ExportData2ExcelExt(getReport());
        //导出->html格式
        IExtension exportData2HtmlExt = new ExportData2HtmlExt(getReport());
        
        if (GeneralQueryUtil.isGeneralQuery(getReport().getContext()))
        	return new IExtension[]{areaCalExt,calExt,traceExt,traceSubExt,
                checkRepExt,checkAllRepExt,hbdraftExt,
//                colletSubExt,viewSubDataExt,viewDataSrcExt,
                importIufoDataExt,
                importExcelDataExt,
//                importUfoDataExt,
//                exportData2ExcelExt,
                exportData2HtmlExt
                };
        else
        	return new IExtension[]{areaCalExt,calExt,traceExt,
                checkRepExt,checkAllRepExt,
//                colletSubExt,viewSubDataExt,viewDataSrcExt,
                importIufoDataExt,
                importExcelDataExt,
//                importUfoDataExt,
//                exportData2ExcelExt,
                exportData2HtmlExt
                };

    }
}
