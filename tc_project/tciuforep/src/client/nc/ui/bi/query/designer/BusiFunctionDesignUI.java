/*
 * �������� 2005-7-1
 *
 */
package nc.ui.bi.query.designer;

import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.IBusiFunction;
import nc.vo.bi.query.manager.MetaDataVO;

/**
 * @author zjb
 * 
 * ҵ����
 */
public class BusiFunctionDesignUI extends AbstractQueryDesignTabPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//����
	public final static int TAB_BUSIFUNC = 0;

	/**
	 *  
	 */
	public BusiFunctionDesignUI() {
		super();
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getSetPanels()
	 */
	public AbstractQueryDesignSetPanel[] getSetPanels() {
		AbstractQueryDesignSetPanel pn1 = new SetBusiFunctionPanel();
		m_sps = new AbstractQueryDesignSetPanel[] { pn1 };
		return m_sps;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getResultFromPanels(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanels(BIQueryModelDef qmd) {
		//������ý��
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).getResultFromPanel(qmd);
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#setResultToPanels()
	 */
	public void setResultToPanels() {
		//��ò�ѯ����
		BIQueryModelDef qmd = getQueryModelDef();
		//��ʼ��
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).setResultToPanel(qmd);
		}

		//�����ʼ��
		((SetBusiFunctionPanel) getSetPanel(TAB_BUSIFUNC)).initUI();

		//Ԫ���ݳ�ʼ������
		initMetaDataHash(qmd.getMetadatas());
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getMetaDatas()
	 */
	public MetaDataVO[] getMetaDatas() {
		SetBusiFunctionPanel pnBusiFunc = (SetBusiFunctionPanel) getSetPanel(TAB_BUSIFUNC);
		IBusiFunction iBusiFunc = pnBusiFunc.getIBusiFunc();
		MetaDataVO[] mds = null;
		if (iBusiFunc != null) {
			mds = iBusiFunc.getMetaDatas(null);
		}
		return mds;
	}

}
