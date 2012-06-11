package com.ufida.report.anareport.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.ui.bi.query.manager.RptProvider;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.dsmanager.ParameterAnalyzerClient;
import nc.ui.pub.dsmanager.ParameterSetDlg;
import nc.vo.bd.CorpVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.sm.UserVO;
import nc.vo.sm.config.Account;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.Context;
import com.ufida.dataset.Parameter;
import com.ufida.dataset.descriptor.Descriptor;
import com.ufida.dataset.descriptor.LimitRowDescriptor;
import com.ufida.dataset.metadata.Field;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.anareport.model.AnaDataSetTool;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.IufoFormat;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.sysplugin.excel.ExcelExpCmd;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;

/**
 * ���ݼ�Ԥ���Ի���
 */
public class DataSetPreviewDlg extends UfoDialog implements ActionListener {
	// �˵����������
	private UIPanel m_menuPanel = null;

	// ����Excel
	private UIButton m_btnExport = null;

	private UfoReport m_report = null;

	private static final long serialVersionUID = 1L;

	private DataSet m_dataSet = null;
	private Context m_context = null;

	public DataSetPreviewDlg(Container parent, Context context, String dsName) {
		super(parent);
		m_context = context;
		initUI(dsName);
	}

	/**
	 * @i18n miufo00427=���ݽ��Ԥ������
	 */
	private void initUI(String dsName) {
		this.setSize(850, 700);
		this.setTitle(StringResource.getStringResource("miufo00427") + dsName);
		UIPanel mainPanel = new UIPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getMenuPanel(), BorderLayout.NORTH);
		UIPanel panel = new UIPanel();
		panel.setLayout(new BorderLayout());
		panel.add(getTablePane(), BorderLayout.CENTER);
		mainPanel.add(panel, BorderLayout.CENTER);
		this.setContentPane(mainPanel);
	}

	private UIPanel getMenuPanel() {
		if (m_menuPanel == null) {
			m_menuPanel = new UIPanel();
			m_menuPanel.setSize(400, 40);

			// miufo1001396=�޸�,miufopublic108=����,mbirep00002=�������,miufo1000961=����,miufo1001782=����Excel
			m_btnExport = createBtn("miufo1001782");
			m_menuPanel.add(m_btnExport);
		}
		return m_menuPanel;
	}

	private UIButton createBtn(String resourceId) {
		UIButton btn = new UIButton(StringResource.getStringResource(resourceId));
		btn.addActionListener(this);
		return btn;
	}

	private UFOTable getTablePane() {
		if (m_report == null) {
			// m_table = UFOTable.createFiniteTable(1, 1);
			m_report = new UfoReport(UfoReport.OPERATION_INPUT, new ContextVO(m_context));
		}
		return m_report.getTable();
	}

	private CellsModel getCellsModel() {
		return getTablePane().getCellsModel();
	}

	@SuppressWarnings("unchecked")
	public void setDatas(DataSet dataSet) {
		dataSet = (DataSet)dataSet.clone();//Ԥ��ʱ�ȿ�¡һ�ݣ�open����¡�����ݼ�
		
		m_dataSet = dataSet;
		Parameter[] params = dataSet.getUsedParameters();
		// yza+ 2008-11-14 ������ҪContext
		if (dataSet.getProvider().getContext() == null) {
			dataSet.getProvider().setContext(this.m_context);
		}
		
		//@edit by yza at 2009-2-18 ���ɱ���������ݼ�ʱ��ʼ��NC������Ϣ
		if(!(dataSet.getProvider() instanceof RptProvider) &&
				ClientEnvironment.getInstance().getConfigAccount() == null){
			//RptPRovider����ҪNC������Ϣ
			initNCContext(dataSet.getProvider().getContext());
		}
		
		if (params != null && params.length > 0) // ���Ѿ�ʹ�õĲ���
		{
			ParameterSetDlg dlg = new ParameterSetDlg(this.m_report, dataSet);
			dlg.showModal();
		}
		Descriptor[] descs = null;
		int maxRow = ParameterAnalyzerClient.BLOWSE_MAX_ROW;
		m_dataSet.open(m_context, null);
		if(m_dataSet.getRowCount()>maxRow){
			LimitRowDescriptor limit = new LimitRowDescriptor();
			limit.setOffsetStart(1);
			limit.setOffsetEnd(maxRow);
			descs = new Descriptor[]{limit};
			MessageDialog.showWarningDlg(this, NCLangRes.getInstance().getStrByID("20081106","upp08110600309"), NCLangRes.getInstance().getStrByID("20081106","upp08110601075"));
			//��������������������ִ�в�ѯ
			m_dataSet.open(m_context, descs);
		}
		// ���±������
		Field[] flds = dataSet.getMetaData().getFields(true);
		if (flds == null || flds.length == 0)
			return;

		// �����б���
		int cols = getCellsModel().getColNum();
		HeaderModel colHeader = getCellsModel().getColumnHeaderModel();
		if (cols <= flds.length) {
			colHeader.addHeader(0, flds.length - cols);
		}

		for (int i = 0; i < flds.length; i++) {
			Header header = colHeader.getHeader(i);
			header.setValue(flds[i].getCaption());
		}

		// �������
		Object data = null;
		int count = dataSet.getRowCount();
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < flds.length; j++) {
				CellPosition pos = CellPosition.getInstance(i, j);
				data = dataSet.getData(i, flds[j].getFldname());
				getCellsModel().setCellValue(pos, AnaDataSetTool.convert2Double(data));
				if (AnaDataSetTool.isInteger(flds[j].getDataType())) {
					IufoFormat format = ((IufoFormat) getCellsModel().getCellFormat(pos));
					if (format == null) {
						format = new IufoFormat();
						format.setDateType(TableConstant.CELLTYPE_NUMBER);
						getCellsModel().setCellFormat(i, j, format);
					}
					((IufoFormat) getCellsModel().getCellFormat(pos)).setDecimalDigits(0);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		// Ԥ�����������Excel
		if (e.getSource() == m_btnExport) {
			(new ExcelExpCmd()).execute(new Object[] { m_report });
		}
	}
	
	private void initNCContext(Context context){
		if(context == null)
			return;
		ClientEnvironment ce = ClientEnvironment.getInstance();
		DataSourceVO dsVO = null;
		if(context.getAttribute(IUfoContextKey.DATA_SOURCE)!=null &&
				(context.getAttribute(IUfoContextKey.DATA_SOURCE) instanceof DataSourceVO)){
			dsVO = (DataSourceVO)context.getAttribute(IUfoContextKey.DATA_SOURCE);
		}
		String dsn = ModelUtil.getDefaultDsname();
		String unitId = "";
		String loginName = "";
		try {
			if(dsVO == null || dsVO.getType()!=DataSourceVO.TYPENC2){
				AppDebug.debug("NC����Դ��Ч,ʹ��Ĭ��NC����Դ:"+(dsn==null?"":dsn));
			}else{
				dsn = dsVO.getAddr();
				unitId = dsVO.getUnitId();
				loginName = dsVO.getLoginName();
				InvocationInfoProxy.getInstance().setDefaultDataSource(dsn);
			}
			
			// ������Ϣ 
			ce.setConfigAccount(new Account());
			ce.getConfigAccount().setAccountCode("");
			ce.getConfigAccount().setDataSourceName(dsn);
			// ��˾��Ϣ
			ce.setCorporation(new CorpVO());
			ce.getCorporation().setPrimaryKey(unitId);
			// �û���Ϣ
			ce.setUser(new UserVO());
			ce.getUser().setUserCode(loginName);
		} catch (Exception e) {
			AppDebug.debug(e.getMessage());
		}
	}
}
