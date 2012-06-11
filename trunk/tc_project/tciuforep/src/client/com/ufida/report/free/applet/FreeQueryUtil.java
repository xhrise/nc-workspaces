package com.ufida.report.free.applet;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;

import com.ufsoft.iufo.fmtplugin.freequery.CreateMeasQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.DataSetQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.SelMeasureQueryDesigner;

/**
 * 各类自由查询的处理工具类
 * 
 * @author ll
 * 
 */
public class FreeQueryUtil {
	public static IFreeQueryDesigner getDesignerByModelType(String queryModelType) {
		// 数据集类型
		if (queryModelType == IFreeQueryModel.TYPE_DATASET) {
			return new DataSetQueryDesigner();
		}// 指标查询
		else if (queryModelType == IFreeQueryModel.TYPE_MEASUREQUERY) {
			return new CreateMeasQueryDesigner();
		}// 选择已保存的指标查询
		else if (queryModelType == IFreeQueryModel.TYPE_SELMEASQUERY) {
			return new SelMeasureQueryDesigner();
		}// BI查询模型
		else if (queryModelType == IFreeQueryModel.TYPE_BIQUERYMODEL) {
			 return new QueryModelVODesigner();
		}
		return null;
	}

}
