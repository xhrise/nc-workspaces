package com.ufsoft.iufo.fmtplugin.formula;

import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.iufo.datasetmanager.IDataSetDefService;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardListPanel;
import nc.ui.pub.querytoolize.AbstractWizardStepPanel;
import nc.ui.pub.querytoolize.AbstractWizardTabPn;
import nc.ui.pub.querytoolize.WizardShareObject;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.pub.dsmanager.DataSetDesignObject;

import com.ufida.dataset.Condition;
import com.ufida.dataset.DataSet;
import com.ufida.dataset.IContext;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.script.extfunc.DataSetFunc;
import com.ufsoft.script.extfunc.LimitRowNumber;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;

/*
 * 数据集函数设计全程向导列表.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class DataSetFuncDesignWizardListPn extends AbstractWizardListPanel implements IUfoContextKey {	
	private static final long serialVersionUID = 1L;

	//PK-数据集哈希表
	private Hashtable<String, DataSet> hashPKDataSet = new Hashtable<String, DataSet>();
	
	// 数据集设计数据结构
	protected DataSetFuncDesignObject m_dsfuncdo = null;
	
	private UfoCalcEnv m_calcEnv = null;
	
	private IContext contextVo = null;
	
	private CellsPane cellsPane = null;
	
	/**
	 * DataSetFuncDesignWizardListPn 构造子注解。
	 */
	public DataSetFuncDesignWizardListPn(DataSetFuncDesignObject dsdo, UfoCalcEnv calcEnv,CellsPane cellsPane) {
		super();
		this.m_dsfuncdo = dsdo;
		this.m_calcEnv = calcEnv;
		this.contextVo = dsdo.getContext();
		this.cellsPane = cellsPane;
		initData();
		initUI();
	}

	/**
	 * @i18n miufo00849=数据集函数设计
	 * @i18n miufo00897=（创建过程）
	 * @i18n miufo00898=（修改过程）
	 */
	public String getWizardTitle() {
		boolean bCreate = (m_dsfuncdo.getStatus() == DataSetDesignObject.STATUS_CREATE);
		String hint = bCreate ? StringResource.getStringResource("miufo00897") : StringResource.getStringResource("miufo00898");
		return StringResource.getStringResource("miufo00849") + hint;
	}

	public String getWizardDescription() {
		return "";
	}

	public void initWizard() {
	}

	public void initData(){
		if(m_dsfuncdo == null || m_dsfuncdo.getEditedDataSetFunc() == null) return;
		DataSetFunc func = m_dsfuncdo.getEditedDataSetFunc();
		DataSetDefVO datasetVO = new DataSetDefVO();
		datasetVO.setCode(func.getDataSetVal().getCode());
		datasetVO.setName(func.getDataSetVal().getName());
		datasetVO.setPk_datasetdef(func.getDataSetVal().getPk_datasetdef());
		datasetVO.getDataSetDef().getMetaData().addField(func.getDataSetFieldsVal());
		
		m_dsfuncdo.setCurDataSetDef(datasetVO);
		m_dsfuncdo.setDefDsName(func.getDataSetVal().getPk_datasetdef());
		
		LimitRowNumber limitRowNumber = func.getDataSetLimitRowFilterCond(m_calcEnv);
		Hashtable<String, Condition[]> fieldConds = func.getDataSetFieldFilterCond(m_calcEnv,false);
		Hashtable<String, String> paramConds = func.getDataSetParamCord(m_calcEnv);
		m_dsfuncdo.setParamCordDef(fieldConds, paramConds, limitRowNumber);
		
		DataSetFuncMetaDataPanel metaPanel = getMetaPanel();
		metaPanel.setCurSelDataSetPK(datasetVO.getPk_datasetdef());
		
	}
	
	/**
	 * @i18n miufo00899=数据集函数向导
	 */
	public boolean completeWizard() {
		// 执行校验
		String strErr = check();
		if (strErr != null) {
			MessageDialog.showWarningDlg(this,
					StringResource.getStringResource("miufo00899"), strErr);
			return false;
		}
		// 设置函数条件
		DataSetFuncDesignObject objDataSetSharedObject = (DataSetFuncDesignObject)getWizardShareObject();
		objDataSetSharedObject.setFuncRowCord(getParameterPanel().getRowCordFilter());
		objDataSetSharedObject.setFuncLimitRowCord(getParameterPanel().getLimitRowFilter());
		objDataSetSharedObject.setFuncParamCord(getParameterPanel().getParamCordFilter());
		return true;
	}

	public DataSetFuncMetaDataPanel getMetaPanel(){
		AbstractWizardStepPanel[] wStepPanels = getStepPanels();
		return (DataSetFuncMetaDataPanel)wStepPanels[1];	
	}
	
	public DataSetFuncParameterPanel getParameterPanel(){
		AbstractWizardStepPanel[] wStepPanels = getStepPanels();
		return (DataSetFuncParameterPanel)wStepPanels[2];	
	}
	
	public AbstractWizardStepPanel[] getStepPanels() {
		if (m_stepPanels == null) {
			m_stepPanels = FuncDesignWizardFactory.createDataSetFuncSteps(
					getOwnerID(), this, getWizShareObj(),cellsPane);
		}
		return m_stepPanels;
	}

	/**
	 * @i18n miufo00900=查询数据集
	 * @i18n miufo00901=获取字段有误
	 */
	public DataSet getDataSet(String datasetdef) {
		DataSet bds = hashPKDataSet.get(datasetdef);
		if(bds == null){
			try {
				DataSetDefVO datasetVO = DataSetDefBO_Client.loadDataSetDefVOByPk(datasetdef);
				bds = datasetVO.getDataSetDef();
				hashPKDataSet.put(datasetdef, bds);
			} catch (Exception e) {
				MessageDialog.showWarningDlg(this, StringResource.getStringResource("miufo00900"), StringResource.getStringResource("miufo00901"));
			}
		}
		return bds;
	}
	
	public DataSetFuncDesignObject getWizShareObj() {
		return m_dsfuncdo;
	}

	/**
	 * 获得向导页签（另一展现样式）
	 */
	public AbstractWizardTabPn getWizardTabPn() {
		AbstractWizardTabPn tabPn = FuncDesignWizardFactory.createDataSetFuncTab(
				getWizShareObj(), this);
		return tabPn;
	}

	private String getOwnerID(){
//		ContextVO ctx = m_report.getContextVo();
		String strRepId = contextVo.getAttribute(REPORT_PK) == null ? null : (String)contextVo.getAttribute(REPORT_PK);
			
		return strRepId;
	}
	
	public DataSetDefVO createDataSetDef(DataSetDefVO vo) throws Exception {
		IDataSetDefService service = (IDataSetDefService) NCLocator
				.getInstance().lookup("IDataSetDefService");
		return service.createDataSetDef(vo);
	}

	public DataSetDefVO updateDataSetDef(DataSetDefVO vo) throws Exception {
		IDataSetDefService service = (IDataSetDefService) NCLocator
				.getInstance().lookup("IDataSetDefService");
		return service.updateDataSetDef(vo);
	}

	public DataSetDefVO getDatasetDef() {
		return getWizShareObj().getCurDataSetDef();
	}

//	public UfoReport getUfoReport(){
//		return m_report;
//	}
	
	public String check() {
		String errorStr = null;
		for (int i = 0; i < getStepPanels().length; i++) {
			errorStr = ((BasicWizardStepPanel)getStepPanels()[i]).check();
			if (errorStr != null) {
				return errorStr;
			}
		}
		return null;
	}

	@Override
	public WizardShareObject getWizardShareObject() {
		return (WizardShareObject) getWizShareObj();
	}

	public IContext getContextVo() {
		return contextVo;
	}

	public CellsModel getCellsModel() {
		return cellsPane.getDataModel();
	}

}
