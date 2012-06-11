/*
 * 创建日期 2005-7-6
 *
 */
package nc.ui.bi.query.designer;

import java.awt.BorderLayout;

import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.iufo.pub.DataManageObjectIufo;
import nc.vo.pub.querymodel.ParamVO;

/**
 * @author zjb
 * 
 * 参数设置APPLET
 */
public class ParamSetApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//参数设置面板实例
	private ParamSetPanel m_paramSetPn = null;

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
		//获得参数
		ParamVO[] params = BIModelUtil.getParams(queryId, dsNameForDef);
		//设置面板
		getParamSetPn().setDefDsName(dsNameForDef);
		getParamSetPn().setParamsArray(new ParamVO[][] { params },
				new String[] { queryId });
		//加入界面
		getJAppletContentPane().add(getParamSetPn(), BorderLayout.CENTER);
	}

	/**
	 * 获得设计面板实例 创建日期：(2005-5-16 19:05:13)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignPanel
	 */
	public ParamSetPanel getParamSetPn() {
		if (m_paramSetPn == null) {
			m_paramSetPn = new ParamSetPanel();
			//m_paramSetPn.setDesignApplet(this);
		}
		return m_paramSetPn;
	}
}
