package nc.ui.iufo.query.datasetmanager;

import java.awt.BorderLayout;

import nc.ui.pub.dsmanager.BasicWizardStepPanel;
import nc.ui.pub.querytoolize.WizardShareObject;
import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.pub.dsmanager.DataSetDesignObject;
import nc.vo.pub.lang.UFDouble;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.metadata.Field;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.exception.MessageException;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.header.Header;
import com.ufsoft.table.header.HeaderModel;

/**
 * 数据集预览对话框（IUFO风格）
 */
public class DataSetPreviewPanel extends BasicWizardStepPanel {

	private static final long serialVersionUID = 1L;

	private UFOTable m_table = null;

	private DataSet m_dataSet = null;

	public DataSetPreviewPanel() {
		super(null);
		initUI();
	}

	private void initUI() {
		setSize(800, 600);
		setLayout(new BorderLayout());
		add(getTablePane(), BorderLayout.CENTER);
	}

	private UFOTable getTablePane() {
		if (m_table == null) {
			m_table = UFOTable.createInfiniteTable(true, true);
		}
		return m_table;
	}

	private CellsModel getCellsModel() {
		return getTablePane().getCellsModel();
	}

	/**
	 * @i18n miufo00821=无法获取数据。
	 */
	@SuppressWarnings("unchecked")
	public void setDatas(DataSet dataSet) throws Exception {
		dataSet = (DataSet)dataSet.clone();//预览时先克隆一份，open被克隆的数据集
		
		m_dataSet = dataSet;
		m_dataSet.open(getSharedObject().getContext(), null);
		// 更新表格内容
		Field[] metadatas = dataSet.getMetaData().getFields(true);
		if (metadatas == null || metadatas.length == 0)
			return;

		// 设置列标题
		int cols = getCellsModel().getColNum();
		HeaderModel colHeader = getCellsModel().getColumnHeaderModel();
		if (cols <= metadatas.length)
			colHeader.addHeader(0, metadatas.length - cols);
 		
		for (int i = 0; i < metadatas.length; i++) {
			Header header = colHeader.getHeader(i);
			header.setValue(metadatas[i].getCaption());
		}
    
		
		// 填充数据
		try {
			Object[][] datas = dataSet.getData2Array();
			for (int i = 0; i < datas.length; i++) {
				Object[] data = datas[i];
				for (int j = 0; j < data.length; j++) {
					getCellsModel().setCellValue(i, j, convert2Double(data[j]));
				}
			}
		} catch (Exception ex) {
			AppDebug.debug(ex);
			throw new MessageException(ex.getMessage());
		}
	}

	private Object convert2Double(Object obj) {
		if (obj instanceof UFDouble || obj instanceof Integer)
			return Double.parseDouble(obj.toString());
		return obj;
	}

	public DataSetPreviewPanel(WizardShareObject wso) {
		super(wso);
		initUI();
		// 加载数据
		DataSetDefVO vo = getSharedObject().getCurDataSetDef();
		load(vo);
	}

	/**
	 * @i18n miufo1000875=预览
	 */
	public String getStepTitle() {
		return StringResource.getStringResource("miufo1000875");
	}

	public void load(DataSetDefVO vo) {
		DataSet ds = vo.getDataSetDef();
		try {
			setDatas(ds);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	public DataSetDesignObject getSharedObject() {
		return (DataSetDesignObject) getWizardShareObject();
	}
}
