package nc.ui.bi.query.freequery;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.vo.iufo.freequery.BIMultiDataSet;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.querymodel.DatasetUtil;

import com.borland.dx.dataset.StorageDataSet;
import com.ufsoft.iufo.data.IMetaData;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;

/**
 * 自由查询的数据集预览对话框
 * 
 * @author ll
 * 
 */
public class DataSetPreviewDlg extends UfoDialog implements ActionListener {
	// 菜单操作的面板
	private UIPanel m_menuPanel = null;

	// 修改查询
	private UIButton m_btnUpdateQuery = null;

	// 保存查询
	private UIButton m_btnSaveQuery = null;

	// 报表设计
	private UIButton m_btnRepDesign = null;

	// 导出Excel
	private UIButton m_btnExport = null;

	// 条件面板上的查询按钮
	private UIButton m_btnQueryCondition = null;

	// 查询条件面板
	private UIPanel m_condPanel = null;

	private UFOTable m_table = null;

	private static final long serialVersionUID = 1L;

	private IFreeQueryDesigner m_designer = null;

	private IFreeQueryModel m_queryDef = null;

	private BIMultiDataSet m_dataSet = null;

	public DataSetPreviewDlg(Container parent, IFreeQueryDesigner designer,
			ContextVO context) {
		super(parent);
		m_designer = designer;
		initUI();
	}

	private void initUI() {
		this.setSize(550, 450);
		UIPanel mainPanel = new UIPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getMenuPanel(), BorderLayout.NORTH);
		UIPanel panel = new UIPanel();
		panel.setLayout(new BorderLayout());
		UIPanel panel2 = new UIPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(getCondPanel(), BorderLayout.CENTER);
		m_btnQueryCondition = createBtn("uiufo150001");
		panel2.add(m_btnQueryCondition, BorderLayout.EAST);// uiufo150001=查询
		panel.add(panel2, BorderLayout.NORTH);
		panel.add(getTablePane(), BorderLayout.CENTER);
		mainPanel.add(panel, BorderLayout.CENTER);
		this.setContentPane(mainPanel);
	}

	private UIPanel getMenuPanel() {
		if (m_menuPanel == null) {
			m_menuPanel = new UIPanel();
			m_menuPanel.setSize(400, 40);

			// miufo1001396=修改,miufopublic108=保存,mbirep00002=报表设计,miufo1000961=导出,miufo1001782=导出Excel
			m_btnUpdateQuery = createBtn("miufo1001396");
			m_btnSaveQuery = createBtn("miufopublic108");
			m_btnRepDesign = createBtn("mbirep00002");
			m_btnExport = createBtn("miufo1001782");
			m_menuPanel.add(m_btnUpdateQuery);
			m_menuPanel.add(m_btnSaveQuery);
			m_menuPanel.add(m_btnRepDesign);
			m_menuPanel.add(m_btnExport);
		}
		return m_menuPanel;
	}

	private UIButton createBtn(String resourceId) {
		UIButton btn = new UIButton(StringResource
				.getStringResource(resourceId));
		btn.addActionListener(this);
		return btn;
	}

	private UIPanel getCondPanel() {
		if (m_condPanel == null) {
			m_condPanel = new UIPanel();
			m_condPanel.setSize(400, 50);
		}
		return m_condPanel;
	}

	private UFOTable getTablePane() {
		if (m_table == null) {
			// m_table = UFOTable.createFiniteTable(1, 1);
			m_table = UFOTable.createInfiniteTable(true, true);
		}
		return m_table;
	}

	private CellsModel getCellsModel() {
		return getTablePane().getCellsModel();
	}

	@SuppressWarnings("unchecked")
	public void setDatas(BIMultiDataSet dataSet, IFreeQueryModel queryDef) {
		m_queryDef = queryDef;
		m_dataSet = dataSet;

		// 更新查询条件面板
		getCondPanel().removeAll();
		getCondPanel().add(m_designer.getConditionPanel(queryDef));

		// 更新表格内容
		IMetaData[] metadatas = dataSet.getMetadatas();
		if (metadatas == null || metadatas.length == 0)
			return;

		// 设置列标题
		int cols = getCellsModel().getColNum();
		HeaderModel colHeader = getCellsModel().getColumnHeaderModel();
		if (cols <= metadatas.length){
			colHeader.addHeader(0, metadatas.length - cols);
		}
		
		for (int i = 0; i < metadatas.length; i++) {
			Header header = colHeader.getHeader(i);
			header.setValue(metadatas[i].getName());
		}

		// 填充数据
		StorageDataSet sds = dataSet.getDataSet();
		Vector<Object[]> vDatas = DatasetUtil.getObjArrayByDataset(sds);
		for (int i = 0; i < vDatas.size(); i++) {
			Object[] data = vDatas.get(i);
			for (int j = 0; j < data.length; j++) {
				getCellsModel().setCellValue(i, j, convert2Double(data[j]));
			}
		}
	}

	private Object convert2Double(Object obj) {
		if (obj instanceof UFDouble || obj instanceof Integer)
			return Double.parseDouble(obj.toString());
		return obj;

	}

	public void actionPerformed(ActionEvent e) {
		// 重新调用查询设计向导
		if (e.getSource() == m_btnUpdateQuery) {
			setVisible(false);
			if (m_designer.designQuery(getParent(), m_queryDef, null)) {
				m_queryDef = m_designer.getQueryDefResult();
				// resetDatas();
			}
			setVisible(true);

		} else if (e.getSource() == m_btnSaveQuery) {

		}// 进入报表设计界面
		else if (e.getSource() == m_btnRepDesign) {
			setResult(ID_OK);
			close();
		}// 预览结果导出到Excel
		else if (e.getSource() == m_btnExport) {

		}// 更新查询条件
		else if (e.getSource() == m_btnQueryCondition) {
			// IQueryCondition cond = m_designer.getUserDifineCondition();
			// m_queryDef.setQueryConditon(cond);
			// resetDatas();
		}
	}

	public IFreeQueryModel getQueryDef() {
		return m_queryDef;
	}

	public BIMultiDataSet getDataSet() {
		return m_dataSet;
	}

	// private void resetDatas() {
	// m_dataSet = (new MeasQueryExcutor()).getQueryResult(m_queryDef,
	// m_context);
	// setDatas(m_dataSet, m_queryDef);
	//
	// }
}
