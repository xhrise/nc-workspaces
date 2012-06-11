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
 * ȡ�������������.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class DataSetFuncParameterPanel extends BasicWizardStepPanel {
	private static final long serialVersionUID = 1L;

	private RowCordFilterPanel m_rcfp = null;

	private UIPanel ivjPnNorth = null;

	private UIPanel ivjPnSouth = null;

	//��ǰѡ�����ݼ�(���ݼ��л�ʱ����¸ñ�ʶ)
	private String m_curSelDataSet = null;
	
	private DataSetFuncDesignWizardListPn m_dswlp = null;
	
	private CellsPane cellsPane;
	
	/**
	 * ���� PnNorth ����ֵ��
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
	 * ���� PnSouth ����ֵ��
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
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
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
	 * DescriptorPanel ���߻�������ע��
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
		
		//���¼����
		String fieldCordErrorStr = m_rcfp.check();
		if(fieldCordErrorStr != null && fieldCordErrorStr.length() > 0){
			return fieldCordErrorStr;
		}
		return null;
	}

	/**
	 * �������������
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
	 * ��ò�����������
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
	 * @i18n miufo00895=ȡ������
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("miufo00895");
	}

	/**
	 * �������������������
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
				//@edit by yza 2009-2-23 QE���ݼ���ʼ������ʱ��Ҫ��Context�л��NC����Դ��Ϣ
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
