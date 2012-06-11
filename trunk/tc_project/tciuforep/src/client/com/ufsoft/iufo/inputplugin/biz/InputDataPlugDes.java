/*
 * �������� 2006-4-5
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
 * ����¼������ݲ˵���������
 * @author liulp
 *
 */
public class InputDataPlugDes extends AbstractPlugDes{

    public InputDataPlugDes(IPlugIn plugin) {
        super(plugin);
    }

    protected IExtension[] createExtensions() {
        //�������
        IExtension areaCalExt = new AreaCalExt(getReport());
        //����
        IExtension calExt = new CalExt(getReport());
        
        IExtension traceExt=new TraceDataExt(getReport());
        
        IExtension traceSubExt=new TraceSubExt(getReport());
        
        //���
        IExtension checkRepExt = new CheckRepExt(getReport());        
        //ȫ�����(������)
        IExtension checkAllRepExt = new CheckAllRepExt(getReport());
        
        IExtension hbdraftExt = new HBDraftExt(getReport());
        
//        //�����¼�(?)
//        IExtension colletSubExt = new AbsIufoBizMenuExt(){
//            protected String[] getPaths() {
//                return doGetDataMenuPaths();
//            }
//            protected String getMenuName() {
//                return "�����¼�(?)";
//            }
//            protected UfoCommand doGetCommand(UfoReport ufoReport) {
//                return null;
//            }            
//        };   
//        //�鿴�¼�����(?)
//        IExtension viewSubDataExt = new AbsIufoBizMenuExt(){
//            protected String[] getPaths() {
//                return doGetDataMenuPaths();
//            }
//            protected String getMenuName() {
//                return "�鿴�¼�����(?)";
//            }
//            protected UfoCommand doGetCommand(UfoReport ufoReport) {
//                return null;
//            }            
//        };   
//        //�鿴��Դ(?)   
//        IExtension viewDataSrcExt = new AbsIufoBizMenuExt(){
//            protected String[] getPaths() {
//                return doGetDataMenuPaths();
//            }
//            protected String getMenuName() {
//                return "�鿴��Դ(?)";
//            }
//            protected UfoCommand doGetCommand(UfoReport ufoReport) {
//                return null;
//            }            
//        };   

        //����->Iufo����
        IExtension importIufoDataExt = new ImportIufoDataExt(getReport());
        //����->Excel����
        IExtension importExcelDataExt = new ImportExcelDataExt(getReport());
        //����->Ufo����
//        IExtension importUfoDataExt = new ImportUfoDataExt(getReport());
        //����->Excel��ʽ
//        IExtension exportData2ExcelExt = new ExportData2ExcelExt(getReport());
        //����->html��ʽ
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
