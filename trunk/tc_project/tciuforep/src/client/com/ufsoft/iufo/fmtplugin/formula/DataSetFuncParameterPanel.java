package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Dimension;

import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.WizardShareObject;
import nc.vo.iufo.datasetmanager.DataSetDefVO;

import com.ufida.dataset.DataSet;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellsPane;

/*
 * 取数条件定义面板.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class DataSetFuncParameterPanel extends BasicWizardStepPanel {
	private static final long serialVersionUID = 1L;

	private RowCordFilterPanel m_rcfp = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	//当前选择数据集(数据集切换时需更新该标识)
	private String m_curSelDataSet = null;
	
	private DataSetFuncDesignWizardListPn m_dswlp = null;
	
	private CellsPane cellsPane;
	
	/**
	 * 返回 PnNorth 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getPnNorth() {
		if (ivjPnNorth == null) {
			try {
				ivjPnNorth = new UIPanel();
				ivjPnNorth.setName("PnNorth");
				ivjPnNorth.setPreferredSize(new Dimension(10, 0));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnNorth;
	}

	/**
	 * 返回 PnSouth 特性值。
	 * 
	 * @return UIPanel
	 */
	private UIPanel getPnSouth() {
		if (ivjPnSouth == null) {
			try {
				ivjPnSouth = new UIPanel();
				ivjPnSouth.setName("PnSouth");
				ivjPnSouth.setPreferredSize(new Dimension(10, 0));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPnSouth;
	}

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	private void initialize() {
		try {
			m_rcfp = new RowCordFilterPanel(m_dswlp.getContextVo(),cellsPane);
			setName("DataSetFuncParameterPanel");
			setLayout(new BorderLayout());
			setSize(560, 360);
			add(m_rcfp, "Center");
			add(getPnNorth(), "North");
			add(getPnSouth(), "South");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * DescriptorPanel 工具化构造子注解
	 */
	public DataSetFuncParameterPanel(WizardShareObject wso, AbstractWizardListPanel awlp,CellsPane cellsPane) {
		super(wso);
		this.cellsPane = cellsPane;
		m_dswlp = (DataSetFuncDesignWizardListPn)awlp;
		initialize();		
	}
	
	@Override
	public void initStep() {
		DataSetDefVO vo = getSharedObject().getCurDataSetDef();
		load(vo);
	}

	public boolean completeStep() {
		m_rcfp.stopTableEdit();
		return true;
	}

	@Override
	public String check() {
		completeStep();
		
		//检查录入项
		String fieldCordErrorStr = m_rcfp.check();
		if(fieldCordErrorStr != null && fieldCordErrorStr.length() > 0){
			return fieldCordErrorStr;
		}
		return null;
	}

	/**
	 * 获得行条件设置
	 * @return
	 */
	public String getRowCordFilter(){
		RowCordFilterPanel rcfp = getFdp();
		if(rcfp == null){
			return "";
		}
		return rcfp.getRowCordFilter();		
	}

	/**
	 * 获得参数条件设置
	 * @return
	 */
	public String getParamCordFilter(){
		RowCordFilterPanel rcfp = getFdp();
		if(rcfp == null){
			return "";
		}
		return rcfp.getParamCordFilter();
	}
	
	/**
	 * @i18n miufo00895=取数条件
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("miufo00895");
	}

	/**
	 * 获得限制行数条件设置
	 * @return
	 */
	public String getLimitRowFilter(){
		RowCordFilterPanel rcfp = getFdp();
		if(rcfp == null){
			return "";
		}
		return rcfp.getLimitRowFilter();		
	}
	
	public void load(DataSetDefVO dataset) {
		if(dataset == null)	return;
		if(m_curSelDataSet == null || !m_curSelDataSet.equals(dataset.getPk_datasetdef())){
			m_curSelDataSet = dataset.getPk_datasetdef();
			m_rcfp.setSharedObject(getSharedObject());
			DataSet dataSet = m_dswlp.getDataSet(dataset.getPk_datasetdef());
			if(dataSet!=null && dataSet.getProvider() != null){
				//@edit by yza 2009-2-23 QE数据集初始化参照时需要从Context中获得NC数据源信息
//				if(dataSet.getProvider().getClass().getName().equals("nc.ui.bi.query.manager.RptProvider")){
//					dataSet.getProvider().setContext(getSharedObject().getContext());
//				}
				dataSet.getProvider().setContext(getSharedObject().getContext());
			}
			m_rcfp.setDataSet(dataSet);	
		}
	}

	public RowCordFilterPanel getFdp() {
		return m_rcfp;
	}

	@Override
	public boolean canFinish() {
		return super.canFinish();
	}

	public DataSetFuncDesignObject getSharedObject() {
		return (DataSetFuncDesignObject) getWizardShareObject();
	}
	
	public String getCurSelDataSetPK() {
		return m_curSelDataSet;
	}

	public void setCurSelDataSetPK(String selDataSetPK) {
		m_curSelDataSet = selDataSetPK;
	}
	
}
