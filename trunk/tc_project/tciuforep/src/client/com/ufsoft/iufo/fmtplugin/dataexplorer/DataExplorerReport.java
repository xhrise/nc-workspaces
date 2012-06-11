package com.ufsoft.iufo.fmtplugin.dataexplorer;

import java.awt.Component;
import java.util.Arrays;

import nc.ui.iufo.input.table.IufoRefData;
import nc.vo.iufo.keydef.KeyVO;

import com.ufida.zior.perfwatch.PerfWatch;
import com.ufsoft.iufo.fmtplugin.ReportFormatApplet;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.service.ReportCalcSrv;
import com.ufsoft.iufo.inputplugin.biz.DSInfoSetExt;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.data.CalExt;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsData;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsDlg;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsExt;
import com.ufsoft.iufo.inputplugin.biz.file.InputChangeKeywordsOper;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaInputPlugin;
import com.ufsoft.iufo.inputplugin.inputcore.AbsUfoContextVO;
import com.ufsoft.iufo.inputplugin.inputcore.InputCorePlugin;
import com.ufsoft.iufo.inputplugin.inputcore.RefData;
import com.ufsoft.iufo.inputplugin.key.KeyInputPlugin;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.inputplugin.measure.MeasureInputPlugin;
import com.ufsoft.iuforeport.tableinput.TableInputOperUtil;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputAppletParam;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputAuth;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.edit.EditPlugin;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headerlock.HeaderLockPlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.style.StylePlugin;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellsModel;
import com.ufsoft.iufo.resource.StringResource;

public class DataExplorerReport extends UfoReport{
	private static final long serialVersionUID = 6531779325361747620L;
	private ReportFormatApplet _formatApplet;
	
	public static final String FORMATMODEL_IN_SHOWMODEL_WHEN_DATAPROCESS = "formatModel_in_showModel_when_dataProcess";
	static DataExplorerReport getInstance(ContextVO inputContextVO, ReportFormatApplet formatApplet){
		UfoReport formatReport = formatApplet.getUfoReport();
		CellsModel cellsModel = formatReport.getCellsModel();
		cellsModel = (CellsModel) cellsModel.clone();
		cellsModel = CellsModelOperator.convertDataModelToLWModel(cellsModel);
		cellsModel = CellsModelOperator.convertDynAreaModelToLWModel(cellsModel, true);
		cellsModel.setClientProperty(FORMATMODEL_IN_SHOWMODEL_WHEN_DATAPROCESS, cellsModel);
		return new DataExplorerReport(inputContextVO,cellsModel,formatApplet);
	}
	
	private DataExplorerReport(ContextVO contextVO, CellsModel cellsModel, ReportFormatApplet formatApplet){
		super(OPERATION_INPUT,contextVO,cellsModel);
		_formatApplet = formatApplet;
		getCellsModel().setCellsAuth(new TableInputAuth(this));
		initPlugins();
        //��ʼ�����ؼ���һЩ״̬
        initTableInputCtrl();
	}
	/**
	 * ����TableInputApplet�Ĳ�����أ�ȡ������Ĺ��ܡ�
	 */
	private void initPlugins() {
		addActionExt(new DSInfoSetExt(getFocusReport()));
		addPlugIn(InputCorePlugin.class.getName());
		addPlugIn(KeyInputPlugin.class.getName());
		addPlugIn(MeasureInputPlugin.class.getName());
		addPlugIn(DynAreaInputPlugin.class.getName());
		addPlugIn(PrintPlugin.class.getName());//��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
		//�༭
		addPlugIn(EditPlugin.class.getName());//�༭��ġ����С����ơ�ճ����ɾ���������
		addPlugIn(FindReplacePlugin.class.getName());//���ң��滻	      
		//#��ʽ
		addPlugIn(StylePlugin.class.getName());//��ʾ���
		addPlugIn(HeaderSizePlugin.class.getName()); //�иߣ��п�
		addPlugIn(HeaderLockPlugin.class.getName());//��//���ᴰ�ڣ�ȡ������

		//#����
		addPlugIn(HelpPlugin.class.getName());//����������

		addActionExt(new CalExt(getFocusReport()){
			public boolean isEnabled(Component focusComp){
				return true;
			}

			public UfoCommand getCommand() {
				return new UfoCommand(){
					/**
					 * @i18n miufohbbb00202=�������[
					 */
					public void execute(Object[] params) {
						PerfWatch pw = new PerfWatch(StringResource.getStringResource("miufohbbb00202") + getContextVo().getName()  + "(" + getContextVo().getReportcode() + ")]");
						ReportCalcSrv reportCalcSrv = new ReportCalcSrv(new UfoContextVO(getContextVo()),getCellsModel());
						reportCalcSrv.calcAllFormula(false);	
						pw.stop();
					}					
				};
			}

		});
		addActionExt(new ChangeKeywordsExt(this){
			public Object[] getParams(UfoReport container) {
				//�õ��л��ؼ������ݶ���	    	
				KeyVO[] keyVOs = getKeyModel().getMainKeyVOs();
				Arrays.sort(keyVOs); 
				String strOrgPK = ((AbsUfoContextVO)container.getContextVo()).getAttribute(IUfoContextKey.ORG_PK) == null ? null : (String)((AbsUfoContextVO)container.getContextVo()).getAttribute(IUfoContextKey.ORG_PK);		
				
				ChangeKeywordsData[] changeKeywordsDatas = 
					(ChangeKeywordsData[]) TableInputOperUtil.geneChangeKeywordsDatas(	
							keyVOs,null,false,strOrgPK);
				//��ѡ�񱨱�Ĵ���
				ChangeKeywordsDlg dlg = new ChangeKeywordsDlg(container,changeKeywordsDatas);
				dlg.setVisible(true);
				if (dlg.getResult() == UfoDialog.ID_OK) {     
					return new Object[]{dlg.getInputKeyValues(),keyVOs,container};
				}
				return null;
			}

			public UfoCommand getCommand() {
				return new UfoCommand(){
					public void execute(Object[] params) {
						if(params == null)
							return;
						String[] keyValues = (String[]) params[0];
						KeyVO[] keyVOs = (KeyVO[]) params[1];
						UfoReport container = (UfoReport) params[2];
						
						//����Ԥ��״̬���л��ؼ���ʱ�����뱨��ؼ��ֶ���
						Object[] otherParams = null;
						if(keyValues ==null || keyValues.length==0){
							otherParams = new Object[1];
							otherParams[0] = keyVOs;
						} else{
							otherParams = new Object[keyValues.length+1];
							System.arraycopy(keyValues, 0, otherParams, 0, keyValues.length);
							otherParams[keyValues.length] = keyVOs;
						}
						
						IInputBizOper inputMenuOper = new InputChangeKeywordsOper(container, otherParams);
						inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDSUBMIT);
					}						
				};
			}
			public boolean isEnabled(Component focusComp) {
				return true;
			}
		});
		
		//addPlugIn(LogPlugin.class.getName());//��־
		
		//�رչ���
		addActionExt(new AbsActionExt(){

			@Override
			public UfoCommand getCommand() {
				// TODO Auto-generated method stub
				return new UfoCommand(){

					@Override
					public void execute(Object[] params) {
						getFrame().dispose();
						
					}
					
				};
			}

			@Override
			public Object[] getParams(UfoReport container) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ActionUIDes[] getUIDesArr() {
				ActionUIDes uiDes3 = new ActionUIDes();
				uiDes3.setToolBar(true);
				uiDes3.setGroup(MultiLang.getString("file"));
				uiDes3.setPaths(new String[] {});
				uiDes3.setName(MultiLang.getString("uiuforep0000894"));
				uiDes3.setTooltip(MultiLang.getString("uiuforep0000894"));
				uiDes3.setImageFile("reportcore/exit.gif");
				return new ActionUIDes[]{uiDes3};
			}
			
		});
	}

    /**
	 * ��ʼ�����ؼ���һЩ״̬
	 */
	private void initTableInputCtrl(){
		//׼���������¼��Ļ���,
        RefData.setProxy(new IufoRefData());      
		
        //����������ʾ���뻹�����Ƶ�����:true,false
		String strShowRefID = _formatApplet.getParameter(ITableInputAppletParam.PARAM_TI_SHOWREFID);
        boolean bShowRefID = "true".equalsIgnoreCase(strShowRefID)?true:false;
        ReportStyle.setShowRefID(bShowRefID);
        //��ʽ�Ƿ��¼��:true,false
		String strFormulaCanInput = _formatApplet.getParameter(ITableInputAppletParam.PARAM_TI_FORMULACANINPUT);
        boolean bFormulaCanInput = "true".equalsIgnoreCase(strFormulaCanInput)?true:false;
        MeasureFmt.setCanInput(bFormulaCanInput);
        
        //����Ϊ0ʱ�Ƿ���ʾ:true,false
		String strDisplayZero = _formatApplet.getParameter(ITableInputAppletParam.PARAM_TI_DISPLAYZERO);
        boolean bDisplayZero = "true".equalsIgnoreCase(strDisplayZero)?true:false;
        ReportStyle.setShowZero(bDisplayZero);
	}
	private KeywordModel getKeyModel(){
		return KeywordModel.getInstance(getCellsModel());
	}
	
}
 