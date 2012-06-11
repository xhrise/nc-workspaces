/*
 * DimChartDescriptor.java
 * 创建日期 2005-2-17
 * Created by CaiJie
 */
package com.ufida.report.chart.applet;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import nc.ui.pub.beans.UIDialog;

import org.jfree.chart.JFreeChart;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.chart.common.ChartPropertyDlg;
import com.ufida.report.chart.common.ChartPublic;
import com.ufida.report.chart.model.DimChartException;
import com.ufida.report.chart.model.DimChartResource;
import com.ufida.report.chart.model.DimDataProvider;
import com.ufida.report.chart.property.ChartProperty;
import com.ufida.report.chart.property.IDataProperty;
import com.ufida.report.multidimension.applet.DimensionPopupMenuDes;
import com.ufida.report.multidimension.applet.SelDimSetDialog;
import com.ufida.report.multidimension.model.DataDrillSet;
import com.ufida.report.multidimension.model.IMultiDimConst;
import com.ufida.report.multidimension.model.SelDimModel;
import com.ufida.report.rep.applet.BINavigationExt;
import com.ufida.report.rep.applet.BIPlugIn;
import com.ufida.report.rep.applet.BIReportSaveExt;
import com.ufida.report.rep.applet.PageDimNavigationPanel;
import com.ufida.report.rep.model.BIContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportNavPanel;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.BaseDialog;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.sysplugin.print.HeaderFooterExt;
import com.ufsoft.report.sysplugin.print.HeaderFooterMngDlg;
import com.ufsoft.report.sysplugin.print.HeaderFooterModel;
import com.ufsoft.report.sysplugin.print.PrintExt;
import com.ufsoft.report.sysplugin.print.PrintPreViewExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.print.PrintSet;

/**
 * @author caijie 
 * @since 3.1
 */
public class DimChartDescriptor extends AbstractPlugDes{       
    
    /**设置图表**/
    @SuppressWarnings("unused")
	private class CreateDimChartWirzardExt extends AbsActionExt{

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
         */
        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes1 = new ActionUIDes();
            uiDes1.setName(DimChartResource.SET_CHART_REPORT_WIZARD);
            uiDes1.setPaths(new String[]{DimChartResource.CHART_REPORT});           
            return new ActionUIDes[]{uiDes1};
        }
        public boolean isEnabled(Component focusComp) {
            if (getDimChartPlugin().getModel() == null
                    || getDimChartPlugin().getModel().getOperationState() != UfoReport.OPERATION_FORMAT ) {
                return false;
            } else {
                return true;
            }
        }
        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
         */
        public UfoCommand getCommand() {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
         */
        public Object[] getParams(UfoReport container) {    
          DimChartPlugin plugin = (DimChartPlugin) getPlugin();           
          CreateDimChartWirzardDlg dlg = new CreateDimChartWirzardDlg(plugin, new SetDimChartWizard0(((BIContextVO)getReport().getContextVo()).getCurUserID(), ((BIContextVO)getReport().getContextVo()).getReportPK()));    
          
//          JDialog dlg = new JDialog();
//          dlg.add(new SelectQueryPanel(((BIContextVO)getReport().getContextVo()).getUserPK()));
//          dlg.setSize(500,500);
          dlg.show();
          if(dlg.getSelectOption() == BaseDialog.OK_OPTION) {
              ChartProperty chart = dlg.getChartProperty();
              if(chart == null) return null;             
              try {
                plugin.getModel().addChartProperty(chart);
            } catch (DimChartException e) {
            	AppDebug.debug(e);
                DimChartException.show(e.getMessage());
            }
          }         
          return null;
      
        }}
    /**图表属性**/
    private class ChartPropertyExt extends AbsActionExt{

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
         */
        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes1 = new ActionUIDes();
            uiDes1.setName(DimChartResource.SET_CHART_PROPERTY);
            uiDes1.setPaths(new String[]{DimChartResource.CHART_REPORT});
            ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
            uiDes2.setPaths(new String[]{});
            uiDes2.setPopup(true);
            return new ActionUIDes[]{uiDes1,uiDes2};
        }
        public boolean isEnabled(Component focusComp) {
            if ((getDimChartPlugin().getModel()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getChartType()!= ChartPublic.UNDIFINED)){
                return true;
            } else {
                return false;             
            }
        }
        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
         */
        public UfoCommand getCommand() {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
         */
        public Object[] getParams(UfoReport container) {
            DimChartPlugin plugin = (DimChartPlugin) getPlugin();

            ChartProperty chartPorp = null;
            JFreeChart chart = null;
            try {
                chartPorp = (ChartProperty) plugin.getModel().getChartPropertry().clone();
                if(plugin.getModel().getOperationState() == UfoReport.OPERATION_INPUT){
                    chart = (JFreeChart) plugin.getModel().getCharts()[0].clone();
                }else {
                    chart = ChartPublic.createSampleChart(chartPorp.getChartType());
                }
                
            } catch (CloneNotSupportedException e) {
               return null;
            }
           
            ChartPropertyDlg dlg = null;
            try {
                dlg = new ChartPropertyDlg(plugin.getReport(),chartPorp, chart);
                dlg.setTitle(DimChartResource.SET_CHART_PROPERTY);
            } catch (Exception e) {                
AppDebug.debug(e);//@devTools                 AppDebug.debug(e);
                return null;
            }
            dlg.show();            
            if(dlg.getSelectOption() == BaseDialog.OK_OPTION) {               
                ChartProperty prop = dlg.getChartProperty();
                plugin.getModel().updateChartProperty(prop);                
            }            
                
            return null;
        }
    }
    
    
    /**数据属性**/
    private class DataPropertyExt extends AbsActionExt{

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
         */
        /**
		 * @i18n miuforepcalc0010=数据源
		 */
        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes1 = new ActionUIDes();
            uiDes1.setName(StringResource.getStringResource("miuforepcalc0010"));
            uiDes1.setPaths(new String[]{DimChartResource.CHART_REPORT});
            ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
            uiDes2.setPaths(new String[]{});
            uiDes2.setPopup(true);
            return new ActionUIDes[]{uiDes1,uiDes2};
        }
        public boolean isEnabled(Component focusComp) {
            if ((getDimChartPlugin().getModel()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getChartType()!= ChartPublic.UNDIFINED)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getDataProperty()!= null)    
                    &&(getDimChartPlugin().getModel().getChartPropertry().getDataProperty().getDataProvider()!= null)){
            	IDataProperty dataProperty = getDimChartPlugin().getModel().getChartPropertry().getDataProperty();
            	if(dataProperty.getDataProvider() == null)return false;
            	if(dataProperty.getDataProvider() instanceof DimDataProvider){
            		 DimDataProvider prop = (DimDataProvider) dataProperty.getDataProvider();
                     try {
                         if(prop.getMultiDimemsionModel().getSelDimModel() != null) {
                             return true;
                         }else {
                             return false;
                         }
                     } catch (Exception e) {
                        return false;
                     }
            	}       
            }
            return false; 
        }
        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
         */
        public UfoCommand getCommand() {
            // TODO Auto-generated method stub
            return null;
        }

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
         */
        public Object[] getParams(UfoReport container) {           
			SelDimSetDialog dlg =  new SelDimSetDialog(getDimChartPlugin().getReport());    			
			dlg.setSelModel(getDimChartPlugin().getModel().getMultiDimemsionModel().getSelDimModel(),
			        getDimChartPlugin().getModel().getUserID());
			dlg.getBtnLastStep().setEnabled(false);
			if (dlg.showModal() == UIDialog.ID_OK) {
				SelDimModel selDim = dlg.getSelModel();
				getDimChartPlugin().getModel().getMultiDimemsionModel().setSelDimModel(selDim);
			}		
            return null;
        }}
    /**选择数据**/
    private class DataTableExt extends AbsActionExt{

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
         */
        /**
		 * @i18n uiuforelease00014=查看数据
		 */
        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes1 = new ActionUIDes();
            uiDes1.setName(StringResource.getStringResource("uiuforelease00014"));
            uiDes1.setPaths(new String[]{DimChartResource.CHART_REPORT});
            ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
            uiDes2.setPaths(new String[]{});
            uiDes2.setPopup(true);
            return new ActionUIDes[]{uiDes1,uiDes2};
        }
        public boolean isEnabled(Component focusComp) {
        	if(getDimChartPlugin().getModel().getOperationState() != UfoReport.OPERATION_INPUT) return false;
            if ((getDimChartPlugin().getModel()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getChartType()!= ChartPublic.UNDIFINED)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getDataProperty()!= null)    
                    &&(getDimChartPlugin().getModel().getChartPropertry().getDataProperty().getDataProvider()!= null)){
            	IDataProperty dataProperty = getDimChartPlugin().getModel().getChartPropertry().getDataProperty();
            	if(dataProperty.getDataProvider() == null)return false;
            	if(dataProperty.getDataProvider() instanceof DimDataProvider){
            		 DimDataProvider prop = (DimDataProvider) dataProperty.getDataProvider();
                     try {
                         if(prop.getMultiDimemsionModel().getSelDimModel() != null) {
                             return true;
                         }else {
                             return false;
                         }
                     } catch (Exception e) {
                        return false;
                     }
            	}       
            }
            return false; 
        }
        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
         */
        public UfoCommand getCommand() {         
            return null;
        }

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
         */
        public Object[] getParams(UfoReport container) {           
        	DimDataTableDlg dlg =  new DimDataTableDlg(getDimChartPlugin().getReport(), getDimChartPlugin().getModel());    			
        	
        	dlg.setLocationRelativeTo(null);          
            dlg.setModal(false);
            dlg.setVisible(true);
//			if (dlg.showModal() == UIDialog.ID_OK) {
//				dlg.close();
//				SelDimModel selDim = dlg.getSelModel();
//				getDimChartPlugin().getModel().getMultiDimemsionModel().setSelDimModel(selDim);
//			}		
            return null;
        }}
    
    /**预览图表**/
    private class PreViewExt extends AbsActionExt{
        int startupOperation = 0;
        int operation = 0;
        ActionUIDes au1 = null;
        ActionUIDes au2 = null;
        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
         */
        /**
		 * @i18n mbiadhoc00004=文件
		 */
        public ActionUIDes[] getUIDesArr() {
            startupOperation = getDimChartPlugin().getOperationState();  
            operation = getDimChartPlugin().getOperationState();  
            au1 = new ActionUIDes();
            au1.setName(DimChartResource.PREVIEW_CHART_REPORT);
            au1.setImageFile("reportcore/preview.gif");
            au1.setPaths(new String[]{StringResource.getStringResource("mbiadhoc00004")});
            au2 = (ActionUIDes) au1.clone();
            au2.setPaths(new String[]{});
            au2.setToolBar(true);
            return new ActionUIDes[]{au1,au2};
        }
        public boolean isEnabled(Component focusComp) {
            if ((getDimChartPlugin().getModel()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getChartType()!= ChartPublic.UNDIFINED)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getDataProperty()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getDataProperty().getDataProvider()!= null)
                    && (startupOperation == UfoReport.OPERATION_FORMAT)){
                return true;
            } else {
                return false;             
            }
        }
        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
         */
        public UfoCommand getCommand() {            
            return null;
        }

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
         */
        public Object[] getParams(UfoReport container) {    
            if(operation == UfoReport.OPERATION_INPUT) {               
                getDimChartPlugin().getModel().setOperationState(UfoReport.OPERATION_FORMAT, ((BIContextVO)getDimChartPlugin().getReport().getContextVo()).getReportPK(), ((BIContextVO)getDimChartPlugin().getReport().getContextVo()).getCurUserID());
                operation = UfoReport.OPERATION_FORMAT;               
            }else {
                getDimChartPlugin().getModel().setOperationState(UfoReport.OPERATION_INPUT, ((BIContextVO)getDimChartPlugin().getReport().getContextVo()).getReportPK(), ((BIContextVO)getDimChartPlugin().getReport().getContextVo()).getCurUserID());
                operation = UfoReport.OPERATION_INPUT;               
            }            
            
          return null;
        }
        /*
         * @see com.ufsoft.report.plugin.IActionExt#getListeners(java.awt.Component)
         */
        public EventListener getListener(final Component stateChangeComp) {
            
            PropertyChangeListener lis = new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent evt) {
                    if(stateChangeComp instanceof JMenuItem) {
                        ((JMenuItem)stateChangeComp).setText(getMenuName());
                    }                    
                }
                
            };
            getDimChartPlugin().getModel().addChangeListener(lis);
            return null;        
        }
        private String getMenuName(){
            boolean isFormatState = getDimChartPlugin().getOperationState() == UfoReport.OPERATION_FORMAT;
            return isFormatState?DimChartResource.PREVIEW_CHART_REPORT:DimChartResource.EXIT_PREVIEW_CHART_REPORT;
        }
    
    }
    
	
    /**设置图表类型**/
    private class ChangeChartTypeExt extends AbsActionExt{

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
         */
        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes1 = new ActionUIDes();
            uiDes1.setName(DimChartResource.CHANGE_CHART_TYPE);
            uiDes1.setImageFile("biplugin/selectchart.gif");
            uiDes1.setPaths(new String[]{DimChartResource.CHART_REPORT});
            ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
            uiDes2.setPaths(new String[]{});
            uiDes2.setToolBar(true);
            uiDes2.setPopup(true);
            return new ActionUIDes[]{uiDes1,uiDes2};
        }
        public boolean isEnabled(Component focusComp) {
            if ((getDimChartPlugin().getModel()!= null)            		
                    &&(getDimChartPlugin().getModel().getChartPropertry()!= null)
                    &&(getDimChartPlugin().getModel().getChartPropertry().getChartType()!= ChartPublic.UNDIFINED)){
                return true;
            } else {
                return false;             
            }
        }
        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
         */
        public UfoCommand getCommand() {            
            return null;
        }

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getParams(com.ufsoft.report.UfoReport)
         */
        public Object[] getParams(UfoReport container) {   
               
            SelectChartTypeDlg dlg = new SelectChartTypeDlg(getDimChartPlugin().getReport(), getDimChartPlugin().getModel().getChartPropertry().getChartType());    
            dlg.setLocationRelativeTo(getDimChartPlugin().getReport());
            dlg.show();
            if(dlg.getResult() == UfoDialog.ID_OK) {
                getDimChartPlugin().getModel().setChartType(dlg.getSelectedType());
            }         
            
          return null;
        }}
    
    public abstract class AbsDimChartActionExt extends AbsActionExt {
        private int m_drill = -1;
        public AbsDimChartActionExt(int drillType) {     
            m_drill = drillType;
        }

        public int getDrillType() {
            return m_drill;
        }
        public boolean isEnabled(Component focusComp) {
            if (getDimChartPlugin().getModel() == null
            		|| (getDimChartPlugin().getModel().getChartPropertry().getChartType()== ChartPublic.UNDIFINED)
                    || getDimChartPlugin().getModel().getMultiDimemsionModel() == null
                    || getDimChartPlugin().getModel().getMultiDimemsionModel().getSelDimModel() == null) 
            {
                return false;
            }else {
                return true;
            }
        }
        
        public DataDrillSet getDataDrillModel() {
            DataDrillSet model = null;
            try {
                model = getDimChartPlugin().getModel().getMultiDimemsionModel().getDateDrillSet();
            }catch(Exception e) {
                
            }
            return  model;
        }
    }
    
    /** 数据钻取操作（包括4种操作，其中钻取要看钻取类型） */
	public  class ChartDrillDetailExt extends AbsDimChartActionExt {		

		public ChartDrillDetailExt(int drill_type) {
			super(drill_type);			
		}

		public Object[] getParams(UfoReport container) {	
		    int drilltype = super.getDrillType();
            if (super.getDrillType() == DimensionPopupMenuDes.DATA_DRILL) {
                drilltype = super.getDataDrillModel().getDrillType();
            }                
            try {
                getDimChartPlugin().getDimChartPanel().drill(drilltype);
            } catch (DimChartException e1) {
                DimChartException.show(e1.getMessage());
            }
			return null;
		}

		public ActionUIDes[] getUIDesArr() {
			ActionUIDes desc = new ActionUIDes();
			desc.setName(DimensionPopupMenuDes.getMenuName(super.getDrillType()));
			desc.setPopup(true);

			return new ActionUIDes[] { desc };
		}

        /* (non-Javadoc)
         * @see com.ufsoft.report.plugin.AbsActionExt#getCommand()
         */
        public UfoCommand getCommand() {
            // TODO Auto-generated method stub
            return null;
        }

	}
	
	/** 向下钻取的4种方式 */
	public  class ChartDrillTypeExt extends AbsDimChartActionExt {

		public ChartDrillTypeExt(int drillType) {
		    super(drillType);			
		}

		public UfoCommand getCommand() {
			return null;
		}

		public Object[] getParams(UfoReport container) {
		    DataDrillSet model = getDataDrillModel();                
            if(model != null) {
                model.setDrillType(super.getDrillType());
            }               
			return null;
		}

		public ActionUIDes[] getUIDesArr() {
			ToggleMenuUIDes desc = new ToggleMenuUIDes();
			desc.setName(DimensionPopupMenuDes.getMenuName(super.getDrillType()));
			desc.setPaths(new String[] { StringResource.getStringResource("ubimultidim0041")});
			desc.setPopup(true);
			
			desc.setButtonGroup(StringResource.getStringResource("ubimultidim0041"));//设置ButtonGroup
			
			return new ActionUIDes[] { desc };
		}

		  /*
         * @see com.ufsoft.report.plugin.IActionExt#getListeners(java.awt.Component)
         */
        public void initListenerByComp(final Component stateChangeComp) {
            if((getDataDrillModel() != null) && (getDataDrillModel().getDrillType() == super.getDrillType())) {
                ((JRadioButtonMenuItem)stateChangeComp).setSelected(true); 
            }else {
                ((JRadioButtonMenuItem)stateChangeComp).setSelected(false); 
            }            
        }
        
	}

    private PageDimNavigationPanel pageDimNavigationPanel = null;
    /**
     * 
     */
    public DimChartDescriptor(DimChartPlugin chartPlugin) {
        super(chartPlugin);       
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getName()
     */
    public String getName() {
        return DimChartResource.CHART_REPORT;
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getNote()
     */
    public String getNote() {
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getPluginPrerequisites()
     */
    public String[] getPluginPrerequisites() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ufsoft.report.plugin.AbstractPlugDes#createExtensions()
     */
    protected IExtension[] createExtensions() {
        return new IExtension[] {
                //new CreateDimChartWirzardExt(),
                new BIReportSaveExt((BIPlugIn)getPlugin()),
                new PreViewExt(),

                new ChartPropertyExt(),    
                new DataPropertyExt(),   
                new ChangeChartTypeExt(),
                new DataTableExt(),
                new ChartDrillTypeExt(IMultiDimConst.DATA_DRILLNEXT),
				new ChartDrillTypeExt(IMultiDimConst.DATA_DRILLDESCENDANT),
				new ChartDrillTypeExt(IMultiDimConst.DATA_DRILLBUTTOM),
				new ChartDrillTypeExt(IMultiDimConst.DATA_DRILLSAME_GENERATION),
				
				new ChartDrillDetailExt(DimensionPopupMenuDes.DATA_DRILL),		
                new ChartDrillDetailExt(IMultiDimConst.DATA_DRILLUP),
                new ChartDrillDetailExt(IMultiDimConst.DATA_DRILLTOP),
                getPageDimNavigationExt(),
                new ChartPrintPageSetExt()/** 页面设置*/,
                new ChartPrintPreViewExt(),//打印预览		
        		new ChartPrintExt()//打印
        };
    }
    /*
     * @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
     */
    public String getHelpNode() {
        return null;
    }

   
    
    /**页维度字段导航*/
    private INavigationExt getPageDimNavigationExt() {
        if(pageDimNavigationPanel == null) {
            pageDimNavigationPanel = new PageDimNavigationPanel(); 
            
            //当页纬度变化时更新面板            
            getDimChartPlugin().getModel().addChangeListener(pageDimNavigationPanel);   
            
            if(getDimChartPlugin().getModel().getMultiDimemsionModel() != null) {//通知页纬度面板，添加页纬度
                getDimChartPlugin().getModel().notifyListeners(new PropertyChangeEvent(this,IMultiDimConst.PROPERTY_SELDIM_CHANGED, null,getDimChartPlugin().getModel().getMultiDimemsionModel().getSelDimModel()));
			}
        }
        INavigationExt ext = new BINavigationExt(ReportNavPanel.NORTH_NAV, pageDimNavigationPanel);
        return ext;
    }
    
    public PageDimNavigationPanel getPageDimNavigationPanel() {
        return pageDimNavigationPanel;
    }
    
    private DimChartPlugin getDimChartPlugin() {
        return (DimChartPlugin) getPlugin();
    }    
    
    private class ChartPrintPageSetExt extends AbsActionExt{

    	
    	public ChartPrintPageSetExt(){
    	}
    	
    	   public ActionUIDes[] getUIDesArr() {
    	        ActionUIDes uiDes1 = new ActionUIDes();
    	        uiDes1.setGroup(MultiLang.getString("printToolBar"));
    	        uiDes1.setImageFile("reportcore/pageset.gif");
    	        uiDes1.setName(MultiLang.getString("PagePrintSet"));
    	        uiDes1.setPaths(new String[]{MultiLang.getString("file")});
    	        ActionUIDes uiDes2 = (ActionUIDes) uiDes1.clone();
    	        uiDes2.setTooltip(MultiLang.getString("PagePrintSet"));
    	        uiDes2.setPaths(new String[]{});
    	        uiDes2.setToolBar(true);
    	        return new ActionUIDes[]{uiDes1,uiDes2};
    	    }
    	    public UfoCommand getCommand() {
    	    	
    	    	UfoCommand cmd=new UfoCommand(){
    				public void execute(Object[] params) {
    					if(getDimChartPlugin()==null )
    						return;
    					getDimChartPlugin().getChartComp().pageFromat();
    				}
    	    		
    	    	};
    	    	return cmd;
    	    }

    	    /*
    	     * (non-Javadoc)
    	     * 
    	     * @see com.ufsoft.report.menu.ICommandExt#getParams(com.ufsoft.report.UfoReport)
    	     */
    	    public Object[] getParams(UfoReport container) {
    	        return null;
    	    }
    }
    private class ChartPrintPreViewExt extends PrintPreViewExt{
    	public ChartPrintPreViewExt(){
    		super(null);
    	}
        public UfoCommand getCommand() {       
        	UfoCommand cmd=new UfoCommand(){
				public void execute(Object[] params) {
					if(getDimChartPlugin()==null )
						return;
					getDimChartPlugin().getChartComp().printPreview(getDimChartPlugin().getReport());
				}
	    		
	    	};
	    	return cmd;
        }
    }
    
    private class ChartPrintExt extends PrintExt{
    	public ChartPrintExt(){
    		super(null);
    	}
        public UfoCommand getCommand() {       
        	UfoCommand cmd=new UfoCommand(){
				public void execute(Object[] params) {
					if(getDimChartPlugin()==null )
						return;
					getDimChartPlugin().getChartComp().print(true);
				}
	    		
	    	};
	    	return cmd;
        }
    }
    
    private class ChartHeaderFooterExt extends HeaderFooterExt{
    	public ChartHeaderFooterExt(){
    		super();
    	}
    	public Object[] getParams(UfoReport container) {
    		if(getDimChartPlugin().getModel()==null)
    			return null;

    		PrintSet printSet = getDimChartPlugin().getModel().getPrintSet();
    		HeaderFooterModel model = printSet.getHeaderFooterModel();
    		HeaderFooterMngDlg dlg = new HeaderFooterMngDlg(container,model);
    		dlg.setVisible(true);
    		if(dlg.getResult()==UfoDialog.ID_OK){
    			HeaderFooterModel newModel = dlg.getModel();
    			printSet.setHeaderFooterModel(newModel);
    		}
    		return null;
    	}
    }
}
