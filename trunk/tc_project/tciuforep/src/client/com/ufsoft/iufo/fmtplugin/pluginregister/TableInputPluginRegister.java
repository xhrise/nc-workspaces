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
		ufoReport.addPlugIn(KeyInputPlugin.class.getName());     //�ؼ���¼��
		ufoReport.addPlugIn(MeasureInputPlugin.class.getName()); //ָ��¼��	
		//#�ļ�
		ufoReport.addPlugIn(InputFilePlugIn.class.getName());    //����ѡ�񣬱��棬�˳�,�л��ؼ���
		ufoReport.addPlugIn(PrintPlugin.class.getName());        //��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
		//#�༭
		ufoReport.addPlugIn(EditPlugin.class.getName());         //�༭��ġ����С����ơ�ճ����ɾ���������
		//modify by ����� 2008-4-30 �ص��˴��࣬���У�ճ�������ƣ�����ȱ༭�������report��Ŀ��
//		ufoReport.addPlugIn(Patch31Plugin.class.getName());      //�������༭��ġ����С����ơ�ճ�����������
		ufoReport.addPlugIn(DynAreaInputPlugin.class.getName()); //��̬��¼�� add by ����� 2008-4-8 �����˴˲���ļ���˳��
		ufoReport.addPlugIn(FindReplacePlugin.class.getName());//���ң��滻
		ufoReport.addPlugIn(LocationPlugin.class.getName());     //��λ
		//#��ʽ
		ufoReport.addPlugIn(StylePlugin.class.getName());        //��ʾ���
		ufoReport.addPlugIn(HeaderSizePlugin.class.getName());   //�иߣ��п�
		ufoReport.addPlugIn(HeaderLockPlugin.class.getName());   //��//���ᴰ�ڣ�ȡ������
		//#����:
		//��ʽ׷��
		ufoReport.addPlugIn(FormulaTracePlugIn.class.getName());
		//������㣬���㣬��ˣ�ȫ�����;
		//�����¼����鿴�¼����ݣ��鿴��Դ��
		//����Excel���ݣ�����Ufo���ݣ�����Iufo���ݣ�	 	
		ufoReport.addPlugIn(InputDataPlugIn.class.getName());

		addExcelExpPlugIn();

		//���ɲ�ѯ
		TableInputContextVO inputContextVO = (TableInputContextVO)ufoReport.getContextVo();
		Object genralQueryObj = inputContextVO.getAttribute(GENRAL_QUERY);
		boolean isgenralQuery = genralQueryObj == null ? false : Boolean.parseBoolean(genralQueryObj.toString());
		
		if(isgenralQuery){
			ufoReport.addPlugIn("com.ufida.report.anareport.applet.AnaQeuryPlugin");//���ϼ�����
		}

		//#����
		ufoReport.addPlugIn(HelpPlugin.class.getName());         //����������
//		ufoReport.addPlugIn(LogPlugin.class.getName());          //��־
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
		
		//@edit by zhaopq at 2009-1-9,����09:24:07
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
