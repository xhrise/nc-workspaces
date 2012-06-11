package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.Container;

import javax.swing.JPanel;

import nc.itf.iufo.freequery.IFreeQueryCondition;
import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;
import nc.vo.iufo.freequery.DataSetQueryModel;

import com.ufsoft.iufo.data.DefaultMetaData;
import com.ufsoft.iufo.data.MetaDataTypes;
import com.ufsoft.report.ContextVO;
import com.ufsoft.report.util.MultiLang;

public class DataSetQueryDesigner implements IFreeQueryDesigner {

	private DataSetQueryModel m_query = null;

	public boolean designQuery(Container parent, IFreeQueryModel queryDef, ContextVO context) {
		m_query = (DataSetQueryModel) queryDef;
		return true;
	}

	public JPanel getConditionPanel(IFreeQueryModel queryDef) {
		return new JPanel();
	}

	public String getImageFile() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @i18n uiuforep00063=二维数据集
	 */
	public String getMenuName() {
		return MultiLang.getString("uiuforep00063");
	}

	public String getModelName() {
		return DataSetQueryModel.class.getName();
	}

	public IFreeQueryModel getQueryDefResult() {
		return m_query;
	}

	public IFreeQueryCondition getUserDifineCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 创建测试数据
	 * 
	 * @return
	 */
	public static DataSetQueryModel createTestQueryDef() {
		String[] colNames = new String[] { "A1", "A2", "A3", "B1", "B2", "B3" };
		DefaultMetaData[] metadatas = new DefaultMetaData[colNames.length];
		Object[][] datas = new Object[10][colNames.length];
		for (int i = 0; i < metadatas.length; i++) {
			if (i < 3) {
				metadatas[i] = new DefaultMetaData(MetaDataTypes.STRING, colNames[i], colNames[i]);
				for (int j = 0; j < datas.length; j++) {
					datas[j][i] = "d" + j + "-" + i;
				}
			} else {
				metadatas[i] = new DefaultMetaData(MetaDataTypes.NUMBER, colNames[i], colNames[i]);
				for (int j = 0; j < datas.length; j++) {
					datas[j][i] = Double.valueOf((j-1) * 100 + i - 3);
				}
			}
		}

		DataSetQueryModel def = new DataSetQueryModel(datas, metadatas);
		return def;
	}

}
 