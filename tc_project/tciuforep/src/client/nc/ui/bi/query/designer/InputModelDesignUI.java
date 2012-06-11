/*
 * 创建日期 2006-7-28
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
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
		//获得查询对象
		BIQueryModelDef qmd = getQueryModelDef();
		
		//元数据初始化处理
		initMetaDataHash(qmd.getMetadatas());
		
		//初始化
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
