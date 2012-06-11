package com.ufida.report.free.applet;

import nc.itf.iufo.freequery.IFreeQueryDesigner;
import nc.itf.iufo.freequery.IFreeQueryModel;

import com.ufsoft.iufo.fmtplugin.freequery.CreateMeasQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.DataSetQueryDesigner;
import com.ufsoft.iufo.fmtplugin.freequery.SelMeasureQueryDesigner;

/**
 * �������ɲ�ѯ�Ĵ�������
 * 
 * @author ll
 * 
 */
public class FreeQueryUtil {
	public static IFreeQueryDesigner getDesignerByModelType(String queryModelType) {
		// ���ݼ�����
		if (queryModelType == IFreeQueryModel.TYPE_DATASET) {
			return new DataSetQueryDesigner();
		}// ָ���ѯ
		else if (queryModelType == IFreeQueryModel.TYPE_MEASUREQUERY) {
			return new CreateMeasQueryDesigner();
		}// ѡ���ѱ����ָ���ѯ
		else if (queryModelType == IFreeQueryModel.TYPE_SELMEASQUERY) {
			return new SelMeasureQueryDesigner();
		}// BI��ѯģ��
		else if (queryModelType == IFreeQueryModel.TYPE_BIQUERYMODEL) {
			 return new QueryModelVODesigner();
		}
		return null;
	}

}
