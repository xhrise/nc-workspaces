package nc.ui.bi.query.designer;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.beans.Beans;
import java.util.Hashtable;

import javax.swing.JFrame;

import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIFrame;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.BIQueryUtil;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.querymodel.DataDictForNode;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 查询设计Applet
 * 
 * 创建日期：(2005-5-12 18:38:02)
 * 
 * @author：朱俊彬
 */
public class QueryDesignApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//查询设计面板实例
	private QueryDesignPanel m_designPn = null;

	//查询主键-查询对象定义哈希表
	private Hashtable<String, BIQueryModelDef> m_hashQMD = new Hashtable<String, BIQueryModelDef>();

	//数据源-数据字典哈希表
	private Hashtable<String, Datadict> m_hashDsDD = new Hashtable<String, Datadict>();

	/**
	 * 主入口点 - 当部件作为应用程序运行时，启动这个部件。
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(String[] args) {
		try {
			JFrame frame = new UIFrame();
			QueryDesignApplet aQueryDesignApplet;
			Class iiCls = Class
					.forName("nc.ui.bi.query.designer.QueryDesignApplet");
			ClassLoader iiClsLoader = iiCls.getClassLoader();
			aQueryDesignApplet = (QueryDesignApplet) Beans.instantiate(
					iiClsLoader, "nc.ui.bi.query.designer.QueryDesignApplet");
			frame.getContentPane().add("Center", aQueryDesignApplet);
			frame.setSize(aQueryDesignApplet.getSize());
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			frame.show();
			Insets insets = frame.getInsets();
			frame.setSize(frame.getWidth() + insets.left + insets.right, frame
					.getHeight()
					+ insets.top + insets.bottom);
			frame.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("JApplet 的 main() 中发生异常");
			AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
		}
	}

	/**
	 * @param datadict
	 */
	public void setDatadict(Datadict datadict) {
		getDesignPn().setDatadict(datadict);
	}

	/**
	 * 设置查询设计面板实例 创建日期：(2005-5-16 19:05:13)
	 */
	public void setDesignPn(QueryDesignPanel newDesignPn) {
		m_designPn = newDesignPn;
	}

	/**
	 * 设置查询主键 创建日期：(2005-5-16 18:38:58)
	 */
	public void setQueryModelDef(BIQueryModelDef qmd) {
		//5.01
		if( m_designPn != null && qmd != null && qmd.getBaseModel() != null){
			Datadict dict = m_designPn.getDatadict();
			if( dict != null && dict instanceof DataDictForNode ){
				BIModelUtil.checkIufoDatadict((DataDictForNode)dict, qmd.getBaseModel().getQueryBaseDef());
			}
		}
		getDesignPn().setQueryModelDef(qmd);

	}

	/**
	 * 设置查询设计设置页签 创建日期：(2005-5-16 18:38:58)
	 */
	public void setTabPn(AbstractQueryDesignTabPanel tabPn) {
		getDesignPn().setTabPn(tabPn);
	}

	/**
	 * 设置定义数据源 创建日期：(2005-5-16 18:38:58)
	 */
	public void setDefDsName(String dsNameForDef) {
		getDesignPn().setDefDsName(dsNameForDef);
	}

	/**
	 * 获得查询设计面板实例 创建日期：(2005-5-16 19:05:13)
	 * 
	 * @return nc.ui.bi.query.designer.QueryDesignPanel
	 */
	public QueryDesignPanel getDesignPn() {
		if (m_designPn == null) {
			m_designPn = new QueryDesignPanel();
			m_designPn.setDesignApplet(this);
			m_designPn.setBnCancelVisible(false);
		}
		return m_designPn;
	}

	/**
	 * 根据数据源获得数据字典
	 */
	private Datadict getDatadict(String dsCode) {
		Datadict dd = null;
		if (m_hashDsDD.containsKey(dsCode)) {
			dd = (Datadict) m_hashDsDD.get(dsCode);
		} else {
			dd = BIQueryUtil.getDataDictByDsn(dsCode);
			if( dd != null ){
				m_hashDsDD.put(dsCode, dd);
			}
		}
		return dd;
	}

	/**
	 * 根据数据源获得数据字典
	 */
	private BIQueryModelDef getQueryModelDef(QueryModelVO qm) {
		BIQueryModelDef qmd = null;
		String queryId = qm.getPrimaryKey();
		if (m_hashQMD.containsKey(queryId)) {
			qmd = (BIQueryModelDef) m_hashQMD.get(queryId);
		} else {
			//获得定义数据源
//			String dsNameForDef = getParameter(BIQueryConst.APPLET_DSNAME_DEF);
			qmd = BIQueryUtil.getQueryModelDef(qm, null);
			m_hashQMD.put(queryId, qmd);
		}
		return qmd;
	}

	/**
	 * 根据主键获得查询对象
	 */
	private QueryModelVO getQueryModel(String queryId) {
		String strWhere = "pk_querymodel='" + queryId + "'";
		//获得定义数据源
//		String dsNameForDef = getParameter(BIQueryConst.APPLET_DSNAME_DEF);
		//获得查询信息
		QueryModelVO[] qms = BIQueryUtil.getQueryModels(strWhere, null);
		return (qms == null || qms.length == 0) ? null : qms[0];
	}

	/**
	 * Design
	 */
	private void doDesign() {
		//获得定义数据源
		String dsNameForDef = getParameter(BIQueryConst.APPLET_DSNAME_DEF);
		//获得查询ID
		String queryId = getParameter(BIQueryConst.APPLET_QUERY_ID);
		//System.out.println("QUERY_ID = " + queryId);

		//获得查询信息
		QueryModelVO qm = getQueryModel(queryId);
		if (qm == null) {
			return;
		}
		//根据不同查询类型采用不同设计界面
		AbstractQueryDesignTabPanel tabPn = AbstractQueryDesignTabPanel
				.createTabPn(qm);

		//根据数据源获得数据字典
//		String dsNameForDef = qm.getPk_datasource();
		String dsCode = qm.getDscode();
		if( dsCode == null ){
			dsCode = dsNameForDef;
		}
		Datadict dd = null;
		if(qm.getType().equals(BIQueryConst.TYPE_INPUTMODEL) == false){
			dd = getDatadict(dsCode);
			if( dd == null ){
				MessageDialog.showErrorDlg(this, StringResource.getStringResource("ubiquery0126"), 
					StringResource.getStringResource("mbiquery0129", new String[]{dsCode}));
				//"无法得到数据字典信息,请检查数据源{0}是否可以访问");
			}
		}
		//根据查询ID获得查询定义
		BIQueryModelDef qmd = getQueryModelDef(qm);
		//
		setTabPn(tabPn);
		setDefDsName(dsNameForDef);
		setDatadict(dd);
		setQueryModelDef(qmd);
	}

	/**
	 * 初始化界面 创建日期：(2003-10-17 10:22:29)
	 */
	public void initUI() {
		getJAppletContentPane().add(getDesignPn(), BorderLayout.CENTER);
		//设计
		doDesign();
	}
}  