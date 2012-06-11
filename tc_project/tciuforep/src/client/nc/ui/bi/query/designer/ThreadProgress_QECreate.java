package nc.ui.bi.query.designer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import nc.vo.bi.query.manager.BIQueryExecutor;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.pub.querymodel.DataProcessVO;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.pub.querymodel.QueryConst;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 查询执行进度线程 创建日期：(2003-10-21 12:05:29)
 * 
 * @author：朱俊彬
 */
public class ThreadProgress_QECreate extends ThreadProgress_QEQuery {
	//查询记录数
	private int m_iRecordCount = 0;

	/**
	 * ThreadProgress 构造子注解。
	 */
	public ThreadProgress_QECreate() {
		super();
	}

	/**
	 * 执行查询 创建日期：(2003-10-22 11:25:32)
	 * @i18n miufo00461=无查询字段
	 */
	protected void execQuery(QueryModelDef qmd, Hashtable hashParam,
			BIQueryExecutor qe) throws Exception {

		//数据加工不需要查询预执行
		DataProcessVO dp = qmd.getDataProcessVO();
		if (ModelUtil.isCodeEmpty(dp)) {
			//获得SQL整理之后的查询定义
			QueryBaseDef qbd = ModelUtil.getBaseDefAfterRepair(qmd, hashParam);
			//建临时表
			qe.setQueryBaseDef(qbd);
			qe.setHashParam(hashParam);
			if (qbd.getSelectFlds() == null) {
				AppDebug.debug(StringResource.getStringResource("miufo00461"));//@devTools System.out.println("无查询字段");
				//执行查询
				super.execQuery(qmd, hashParam, qe);
				return;
			}
			if (qmd.getScs() == null)
				m_iRecordCount = qe.getDataSet_create();
		}

		if (m_iRecordCount <= QueryConst.ALERT_RECORD_COUNT)
			//执行查询
			super.execQuery(qmd, hashParam, qe);
	}

	/**
	 * 获得记录行数 创建日期：(2003-10-22 11:41:32)
	 * 
	 * @return int
	 */
	public int getRecordCount() {
		return m_iRecordCount;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.pub.querymodel.ThreadProgress#getResult()
	 */
	public Object getResult() {
		ArrayList<Serializable> result = new ArrayList<Serializable>();
		result.add(new Integer(getRecordCount()));
		if (getRecordCount() <= QueryConst.ALERT_RECORD_COUNT) {
			result.add(getMultiDataSet());
		} else {
			result.add(null);
		}
		return result;
	}
}  