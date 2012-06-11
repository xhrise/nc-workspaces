/*
 * 创建日期 2005-7-19
 *
 */
package nc.ui.bi.query.designer;

import java.awt.BorderLayout;

import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.iufo.pub.DataManageObjectIufo;

/**
 * @author zjb
 * 
 * 辅助工具APPLET
 */
public class ToolFuncApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//辅助工具面板实例
	private ToolFuncPanel m_pnToolFunc = null;

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.BIDesignApplet#initUI()
	 */
	public void initUI() {
		//获得定义数据源
		String dsNameForDef = DataManageObjectIufo.IUFO_DATASOURCE;//getParameter(BIQueryConst.APPLET_DSNAME_DEF);
		//获得查询ID
		String queryId = getParameter(BIQueryConst.APPLET_QUERY_ID);
		//设置面板
		getPnToolFunc().setDsNameForDef(dsNameForDef);
		getPnToolFunc().setQueryId(queryId);
		//加入界面
		getJAppletContentPane().add(getPnToolFunc(), BorderLayout.CENTER);
	}

	/**
	 * 获得辅助工具面板实例 创建日期：(2005-5-16 19:05:13)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignPanel
	 */
	public ToolFuncPanel getPnToolFunc() {
		if (m_pnToolFunc == null) {
			m_pnToolFunc = new ToolFuncPanel();
			//m_pnToolFunc.setDesignApplet(this);
		}
		return m_pnToolFunc;
	}
}
