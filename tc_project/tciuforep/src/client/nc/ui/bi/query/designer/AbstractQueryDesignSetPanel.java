package nc.ui.bi.query.designer;
import java.util.Hashtable;

import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.query.manager.BIQueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 查询设计设置面板 创建日期：(2005-5-13 16:39:25)
 * 
 * @author：朱俊彬
 */
public abstract class AbstractQueryDesignSetPanel extends UIPanel {

	//查询设计页签实例
	private AbstractQueryDesignTabPanel m_tabPn = null;

	/**
	 * AbstractQueryDesignSetPanel 构造子注解。
	 */
	public AbstractQueryDesignSetPanel() {
		super();
		initialize();
	}

	/**
	 * 获得面板标题 创建日期：(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public abstract String getPanelTitle();

	/**
	 * 获得结果 创建日期：(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public abstract void getResultFromPanel(BIQueryModelDef qmd);

	/**
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("AbstractQueryDesignSetPanel");
			setLayout(new java.awt.BorderLayout());
			setSize(480, 320);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * 设置结果 创建日期：(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public abstract void setResultToPanel(BIQueryModelDef qmd);

	/**
	 * @return 返回 tabPn。
	 */
	public AbstractQueryDesignTabPanel getTabPn() {
		return m_tabPn;
	}

	/**
	 * @param tabPn
	 */
	public void setTabPn(AbstractQueryDesignTabPanel tabPn) {
		m_tabPn = tabPn;
	}

	/**
	 * 合法性校验
	 */
	public abstract String check();

	/**
	 * 页切换时校验，缺省调用check()
	 * @return
	 */
	public String checkOnSwitch(){
		return check();
	}
	/**
	 * 用于判断重复的哈希表 创建日期：(2003-10-28 9:04:53)
	 */
	public Hashtable getHashTableId() {
		return null;
	}
}
 