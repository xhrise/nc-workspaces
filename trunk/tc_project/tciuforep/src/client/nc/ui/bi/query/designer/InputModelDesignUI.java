/*
 * �������� 2006-7-28
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.bi.query.designer;

import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;

public class InputModelDesignUI extends AbstractQueryDesignTabPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public MetaDataVO[] getMetaDatas() {
		BIQueryModelDef 	qmd = getQueryModelDef();
		if( qmd != null ){
			getMetaDataPanel().getResultFromPanel(qmd);
			return qmd.getMetadatas();
		}
		return null;
	}

	@Override
	public void getResultFromPanels(BIQueryModelDef qmd) {
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).getResultFromPanel(qmd);
		}
	}

	@Override
	public AbstractQueryDesignSetPanel[] getSetPanels() {	
		return null;
	}

	@Override
	public void setResultToPanels() {
		//��ò�ѯ����
		BIQueryModelDef qmd = getQueryModelDef();
		
		//Ԫ���ݳ�ʼ������
		initMetaDataHash(qmd.getMetadatas());
		
		//��ʼ��
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).setResultToPanel(qmd);
		}

	}

	@Override
	protected boolean isMetaDataEditable() {
		return true;
	}
	
	
	

}
