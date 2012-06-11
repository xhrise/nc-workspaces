package nc.ui.iufo.query.datasetmanager;

import java.awt.BorderLayout;
import java.awt.Container;

import nc.ui.pub.beans.UIDialog;

import com.ufida.dataset.DataSet;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 数据集预览对话框
 */
public class DataSetPreviewDlg extends UIDialog {

	private static final long serialVersionUID = 1L;

	private DataSetPreviewPanel m_pnPreview = null;

	public DataSetPreviewDlg(Container parent) {
		super(parent);
		initUI();
	}

	/**
	 * @i18n miufo01173=数据集预览
	 */
	private void initUI() {
		setTitle(StringResource.getStringResource("miufo01173"));
		setSize(800, 600);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getPnPreview(), BorderLayout.CENTER);
	}

	public void setDatas(DataSet dataSet) throws Exception {
		getPnPreview().setDatas(dataSet);
	}

	private DataSetPreviewPanel getPnPreview() {
		if (m_pnPreview == null) {
			m_pnPreview = new DataSetPreviewPanel();
		}
		return m_pnPreview;
	}
}
 