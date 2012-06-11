/*
 * 创建日期 2005-7-1
 *
 */
package nc.ui.bi.query.designer;

import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.IBusiFunction;
import nc.vo.bi.query.manager.MetaDataVO;

/**
 * @author zjb
 * 
 * 业务函数
 */
public class BusiFunctionDesignUI extends AbstractQueryDesignTabPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//常量
	public final static int TAB_BUSIFUNC = 0;

	/**
	 *  
	 */
	public BusiFunctionDesignUI() {
		super();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getSetPanels()
	 */
	public AbstractQueryDesignSetPanel[] getSetPanels() {
		AbstractQueryDesignSetPanel pn1 = new SetBusiFunctionPanel();
		m_sps = new AbstractQueryDesignSetPanel[] { pn1 };
		return m_sps;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getResultFromPanels(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanels(BIQueryModelDef qmd) {
		//获得设置结果
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).getResultFromPanel(qmd);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#setResultToPanels()
	 */
	public void setResultToPanels() {
		//获得查询对象
		BIQueryModelDef qmd = getQueryModelDef();
		//初始化
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).setResultToPanel(qmd);
		}

		//界面初始化
		((SetBusiFunctionPanel) getSetPanel(TAB_BUSIFUNC)).initUI();

		//元数据初始化处理
		initMetaDataHash(qmd.getMetadatas());
	}

	/*
	 * （非 Javadoc）
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
