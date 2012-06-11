package com.ufida.report.complexrep.applet;
import com.ufida.iufo.pub.tools.AppDebug;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import nc.vo.bi.base.util.IDMaker;
import nc.vo.bi.report.manager.ReportResource;

import com.ufida.report.adhoc.model.AdhocModel;
import com.ufida.report.chart.model.DimChartException;
import com.ufida.report.chart.model.DimChartModel;
import com.ufida.report.free.FreeRepModel;
import com.ufida.report.multidimension.model.MultiDimemsionModel;
import com.ufida.report.rep.applet.BIReportApplet;
import com.ufida.report.rep.model.BIContextVO;
import com.ufida.report.rep.model.BaseReportModel;
import com.ufida.report.spreedsheet.model.SpreadSheetModel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;

public class AddSubRepExt extends AbsActionExt {
    

    private ComplexRepPlugin m_plugin;

    public AddSubRepExt(ComplexRepPlugin plugin) {
        m_plugin = plugin;
    }

    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        String strName=StringResource.getStringResource(StringResConst.STR_ADD_REPORT);
        uiDes.setName(strName);
        uiDes.setPopup(true);
        
        ActionUIDes uiDesTool= new ActionUIDes();
        uiDesTool.setTooltip(strName);
        uiDesTool.setToolBar(true);
        uiDesTool.setImageFile("reportcore/openfile.gif");
        
        ActionUIDes uiDesMenu = new ActionUIDes();
        uiDesMenu.setName(strName);
        uiDesMenu.setPaths(new String[]{StringResource.getStringResource(StringResConst.STR_MENU_MAIN)});
        
        return new ActionUIDes[]{uiDes,uiDesTool,uiDesMenu};
    }

    public UfoCommand getCommand() {
        return new UfoCommand(){
            public void execute(Object[] params) {
                if(params == null){
                    return;
                }
                UfoReport posReport = (UfoReport) params[0];
                boolean isCancal = ((Boolean)params[1]).booleanValue();
                if(isCancal) {
                    return;
                }
                String pos = (String) params[2];
                boolean isNew = ((Boolean)params[3]).booleanValue();
                int newType = ((Integer)params[4]).intValue();
                boolean isLinkAction = ((Boolean)params[5]).booleanValue();
                String importRepPK = (String) params[6];
                UfoReport linkRep = (UfoReport) params[7];
                

                BaseReportModel reportModel = null;
                if(isNew){
                    reportModel = getNewReportModel(newType,isLinkAction,linkRep);
                }else{
                    reportModel = getImportReportModel(importRepPK);
                }
                if(reportModel != null){
                	//增加子表pk
                	String strSubPK=generateSubRepPK();
                	
                	reportModel.setPK(strSubPK);
                    m_plugin.addSubReport(posReport,reportModel,pos);        
                }else{
                    JOptionPane.showMessageDialog(m_plugin.getReport(),StringResource.getStringResource("mbicomplex00003")/**导入或引用的报表还没有进行格式定义!*/);
                }
            }
            private String generateSubRepPK(){
//            	String strRepPK=(((BIContextVO) m_plugin.getReport().getContextVo()).getReportPK();
            	String suffix=null;
            	int i=0;
            	while(i<10){
            		suffix=IDMaker.makeID(10);
            		if(m_plugin.getModel().getSubReport(suffix)==null)
            			break;
            		i++;
            	}
            	return suffix;
            }

            private BaseReportModel getImportReportModel(String repPK) {
                BaseReportModel model = BIReportApplet.getBaseReportModel(repPK);
                return model;
            }

            private BaseReportModel getNewReportModel(int newType, boolean isLinkAction, UfoReport linkRep) {
                BaseReportModel model = null;
                switch(newType){
                    case ReportResource.INT_REPORT_ADHOC:
                        model = new AdhocModel(null);
                        break;
                    case ReportResource.INT_REPORT_MULTI:                        
                        if(isLinkAction){
                            DimChartModel chartModel = (DimChartModel) ((BIContextVO)linkRep.getContextVo()).getBaseReportModel();
                            model = chartModel.getMultiDimemsionModel();
                        }else{
                            model = new MultiDimemsionModel(null);
                        }
                        break;
                    case ReportResource.INT_REPORT_SPREADSHT:
                        model = new SpreadSheetModel(null);
                        break;
                    case ReportResource.INT_REPORT_CHART:
                        model = new DimChartModel(null);
                        ((DimChartModel)model).setMultiDimemsionModel(new MultiDimemsionModel(null));
                        if(isLinkAction){
                            MultiDimemsionModel dimModel = (MultiDimemsionModel) ((BIContextVO)linkRep.getContextVo()).getBaseReportModel();
                            try {
                                ((DimChartModel)model).LinkWithMultiDimensionReoport(dimModel,null);
                            } catch (DimChartException e) {
AppDebug.debug(e);//@devTools                                 AppDebug.debug(e);
                            }
                        }
                        break;
                    case ReportResource.INT_REPORT_FREE:
                        model = new FreeRepModel(null);
                        break;

                    default:
                        throw new IllegalArgumentException();
                }
                return model;
            }          
        };
    }

    public Object[] getParams(UfoReport container) {
        UfoReport posReport = m_plugin.getFocusSubReport();
        final ImportReportDlg dialog = new ImportReportDlg(m_plugin,posReport);
        dialog.show();     
        excuteByDialog(dialog);
        return null;
    }
    
    private void excuteByDialog(final ImportReportDlg dialog){
        if(dialog.isLinkState()){
            final SelReportDlg selReportDlg = new SelReportDlg(m_plugin.getReport().getFrame());
            selReportDlg.addActionListenerToButton(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    selReportDlg.close();
                    dialog.setVisible(true);
                    excuteByDialog(dialog);
                }
            });
            selReportDlg.show();
            dialog.setLinkState(false);            
        }else{
            getCommand().execute(getParamsFromDlg(dialog));
        }
    }
    protected Object[] getParamsFromDlg(ImportReportDlg dialog) {
        UfoReport posReport = dialog.getPosReport();
        boolean isCancle = dialog.getResult() == UfoDialog.ID_CANCEL;
        String pos = dialog.getPos();
        boolean isNew = dialog.isNew();
        Integer newType = new Integer(dialog.getNewType());
        boolean isLinkAction = dialog.isLinkAction();
        String importRepPK = dialog.getImportRepPK();
        UfoReport linkRep = dialog.getLinkReport();
        if(isNew){
            if(newType.intValue() == ReportResource.INT_REPORT_CHART 
                    && isLinkAction
                    && getTypeByReport(linkRep) != ReportResource.INT_REPORT_MULTI){
                JOptionPane.showMessageDialog(m_plugin.getReport().getFrame(),StringResource.getStringResource("mbicomplex00001")/** 新建图表的联动关联报表不是多维类型!*/);
                return null;
            }
            if(newType.intValue() == ReportResource.INT_REPORT_MULTI 
                    && isLinkAction
                    && getTypeByReport(linkRep) != ReportResource.INT_REPORT_CHART){
                JOptionPane.showMessageDialog(m_plugin.getReport().getFrame(),StringResource.getStringResource("mbicomplex00002")/**新建多维表的联动关联报表不是图表类型!*/);
                return null;
            }
        }
        return new Object[]{posReport,new Boolean(isCancle),pos,new Boolean(isNew),newType,
                new Boolean(isLinkAction),importRepPK,linkRep};
    }
    private int getTypeByReport(UfoReport report){
        return ((BIContextVO)report.getContextVo()).getBaseReportModel().getReportType().intValue();
    }
    public boolean isEnabled(Component focusComp) {
	    if(m_plugin.getModel().getOperationState()==UfoReport.OPERATION_INPUT){
	        return false;
	    }
	    return true;
    }
}
