package com.ufsoft.iufo.inputplugin.biz.formulatracenew;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import nc.ui.iufo.input.control.DataSourceConfig;
import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;
import nc.ui.iufo.input.view.FormTraceResultViewer;
import nc.vo.iufo.datasource.DataSourceLoginVO;
import nc.vo.iufo.datasource.DataSourceVO;

import com.ufida.dataset.Context;
import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.formula.FormulaPlugin;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.IFormulaTraceBiz;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.FormulaParsedData;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class UfoFormulaTraceAction extends AbstractPluginAction{
	
	
	//公式追踪菜单是否被选中
	
	private CellPosition selPosition = null;

	/**
	 * @i18n miufohbbb00107=公式追踪时，焦点必须在报表上
	 */
	@Override
	public void execute(ActionEvent e) {
		boolean bTraceMenuSelected = RepDataControler.getInstance(getMainboard()).isCanFormulaTrace();
		if(!bTraceMenuSelected){
			Viewer viewer = getMainboard().getView(RepDataControler.FORMULA_TRACERESULT_ID);
			if(viewer != null)
				((FormTraceResultViewer)viewer).getTracePanel().setFormulaParedData(null);
			return;
		}
		CellsModel model = getCellsModel();
		if(model == null){
			UfoPublic.sendWarningMessage(StringResource.getStringResource("miufohbbb00107"), null);
			return;
		}
			
		CellPosition cellPos = getSelPosition();
		if(cellPos == null)
			cellPos = getCellsModel().getSelectModel().getSelectedArea().getStart();
		RepDataEditor editor = getEditor();
		IFormulaTraceBiz formulaTraceBiz = UfoFormulaTraceBizHelper.getFormulaTraceBiz();
		FormulaParsedData formulaParedData = null;
    	boolean bExistFormula = formulaTraceBiz.existFormula(getCellsModel(), cellPos);
    	
    	DataSourceVO dataSource=(DataSourceVO)editor.getContext().getAttribute(IUfoContextKey.DATA_SOURCE);
    	if (dataSource!=null){
    		DataSourceConfig config=DataSourceConfig.getInstance(getMainboard());
    		DataSourceLoginVO loginVO=config.getOneSourceConfig(editor.getPubData().getUnitPK());
    		if (loginVO!=null){
    			dataSource.setLoginName(loginVO.getDSUser());
    			dataSource.setLoginUnit(loginVO.getDSUnit());
    			dataSource.setLoginPassw(loginVO.getDSPass());
//    			dataSource.setLoginDate(loginVO.get)
    		}
    	}
    	
		if(bTraceMenuSelected && bExistFormula){
			formulaParedData = formulaTraceBiz.parseFormula(
					(Context)editor.getContext(),getCellsModel(), cellPos);
			FormTraceResultViewer viewer = getTraceResultView();
			viewer.getTracePanel().setEditor(editor);
			viewer.getTracePanel().setFormulaParedData(formulaParedData);
		}else{
			FormTraceResultViewer viewer = getTraceResultView();
			viewer.getTracePanel().setFormulaParedData(null);
		}
		setSelPosition(null);
		
	}
	
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor();
		descriptor.setGroupPaths(StringResource.getStringResource("miufo1001692"), FormulaPlugin.GROUP);
		descriptor.setName(getName());//"公式追踪"
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.MENU});
		descriptor.setCompentFactory(getComponentFactory());
		descriptor.setMemonic('F');
		return descriptor;
	}
	
	public FormTraceResultViewer getTraceResultView(){
		//iufo.input.formresult.view
		Viewer viewer = getMainboard().getView(RepDataControler.FORMULA_TRACERESULT_ID);
		if(viewer == null)
			viewer = getMainboard().openView(FormTraceResultViewer.class.getName(), RepDataControler.FORMULA_TRACERESULT_ID);
		return (FormTraceResultViewer) viewer;
		
	}
	
	/**
	 * @i18n miufohbbb00108=公式追踪
	 */
	private String getName(){
		return StringResource.getStringResource("miufohbbb00108");
	}
	
	
	private ICompentFactory getComponentFactory() {		
		return new DefaultCompentFactory(){

			@Override
			protected JComponent createMenuItem(String strGroup,
					AbstractAction action) {
				KCheckBoxMenuItem comp = new KCheckBoxMenuItem();
				comp.setGroup(strGroup);
				comp.setAction(action);
				comp.setText(getName()+"(F)");
				comp.setMnemonic('F');
				// @edit by wangyga at 2009-6-26,下午12:49:58
				RepDataControler.getInstance(getMainboard()).setCanFormulaTrace(false);
				comp.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						if(e.getStateChange() == ItemEvent.SELECTED)
							RepDataControler.getInstance(getMainboard()).setCanFormulaTrace(true);
						else
							RepDataControler.getInstance(getMainboard()).setCanFormulaTrace(false);


				}
					});
				return comp;
			}			
		};
	}
	
	private RepDataEditor getEditor(){
		Viewer viewer = getCurrentView();
		if(!(viewer instanceof RepDataEditor))
			return null;
		else
			return (RepDataEditor) viewer;
	}

	private CellsModel getCellsModel() {
		RepDataEditor editor = getEditor();
		return editor!=null ? editor.getCellsModel() : null;

	}

	public CellPosition getSelPosition() {
		return selPosition;
	}

	public void setSelPosition(CellPosition selPosition) {
		this.selPosition = selPosition;
	}
	

}
 