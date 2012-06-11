package nc.ui.bi.query.designer;
import java.util.Hashtable;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.queryengine.IEmbedCodeUtil;
import nc.ui.pub.querymodel.ThreadProgress;
import nc.vo.bi.query.manager.BIQueryExecutor;
import nc.vo.iufo.freequery.BIMultiDataSet;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.pub.cquery.FldgroupVO;
import nc.vo.pub.dbbase.QEDataSetAfterProcessDescriptor;
import nc.vo.pub.dbbase.QEDataSetDescriptor;
import nc.vo.pub.querymodel.DataProcessVO;
import nc.vo.pub.querymodel.DatasetUtil;
import nc.vo.pub.querymodel.MergeHeaderVO;
import nc.vo.pub.querymodel.ModelUtil;
import nc.vo.pub.querymodel.QueryModelDef;
import nc.vo.pub.querymodel.RotateCrossVO;
import nc.vo.pub.querymodel.SimpleCrossVO;

import com.borland.dx.dataset.StorageDataSet;
import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 查询执行进度线程 创建日期：(2003-10-21 12:05:29)
 * 
 * @author：朱俊彬
 */
public class ThreadProgress_QEQuery extends ThreadProgress {
	// 查询模型定义
	protected QueryModelDef m_qmd = null;

	// 参数哈希表
	protected Hashtable m_hashParam = null;

	// 查询执行器
	protected BIQueryExecutor m_qe = null;

	// 查询结果集
	protected BIMultiDataSet m_mds = null;

	// 定义数据源
	protected String m_defDsName = null;

	/**
	 * ThreadProgress 构造子注解。
	 */
	public ThreadProgress_QEQuery() {
		super();
	}

	/**
	 * 清空全局属性 创建日期：(2004-6-8 16:28:20)
	 */
	public void clear() {
		// 查询模型定义
		m_qmd = null;
		// 参数哈希表
		m_hashParam = null;
		// 查询执行器
		m_qe = null;
		// 查询结果集
		m_mds = null;
		// 定义数据源
		m_defDsName = null;
	}

	/**
	 * 执行查询 创建日期：(2003-10-22 11:25:32)
	 */
	protected void execQuery(QueryModelDef qmd, Hashtable hashParam, BIQueryExecutor qe) throws Exception {

		qmd.setFldgroups(null);

//		// 业务函数临时处理 TODO 临时注释2006-06-29，此方法应该没有用处
//		if (qmd instanceof BIQueryModelDef) {
//			BIQueryModelDef qmd0 = (BIQueryModelDef) qmd;
//			if (qmd0.getBusifunc() != null) {
//				BIEnvInfo env = new BIEnvInfo();
//				env.setDsNameForExe(qmd.getDsName());
//				m_mds = new BusiFunctionExecutor().getMultiDataSet(qmd0, env);
//			}
//		}

		//
		DataProcessVO dp = qmd.getDataProcessVO();
		if (!ModelUtil.isCodeEmpty(dp)) {
			// 数据加工
			// DataSetDataWithColumn dsdwc =
			// DataProcessUtilBO_Client.process(qmd,
			// hashParam, ModelUtil.getLoginEnv());
			IEmbedCodeUtil name = (IEmbedCodeUtil) NCLocator.getInstance().lookup(IEmbedCodeUtil.class.getName());
			QEDataSetDescriptor dsdwc =name.process(qmd, hashParam, ModelUtil.getLoginEnv());
			//DataSetDataWithColumn dsdwc = name.process(qmd, hashParam, ModelUtil.getLoginEnv());
			if (dsdwc.getDataSetData() != null) {
				StorageDataSet dataSet = makeDataSet(dsdwc);
				m_mds = new BIMultiDataSet();
				m_mds.setDataSet(dataSet);
				// 获得加工后的参数哈希表
				m_mds.setHashParam(dsdwc.getHashParam());
			}
		} else {
			SimpleCrossVO[] scs = qmd.getScs();
			if (scs != null) {
				MergeHeaderVO mh = qmd.getMh();
				StorageDataSet dataSet = null;
				if (mh != null) {
					// 合并元素
					dataSet = DatasetUtil.getDatasetForMergeElementCross(mh, hashParam, m_defDsName);
				} else {
					// 简单交叉
					QueryBaseDef qbd = qe.getQueryBaseDef();
					dataSet = DatasetUtil.getDatasetForSimpleCross(scs, qbd, hashParam);
				}
				m_mds = new BIMultiDataSet();
				m_mds.setDataSet(dataSet);
			} else {
				QueryBaseDef qbd = qe.getQueryBaseDef();
				// qbd.setTempletId("union");
				// 获得查询结果集
				if (qbd.getSelectFlds() != null) {
					StorageDataSet dataSet = qe.getDataSet_query();
					m_mds = new BIMultiDataSet();
					m_mds.setDataSet(dataSet);
					// 旋转交叉
					RotateCrossVO rc = qbd.getRotateCross();
					if (rc != null) {
						// 保留交叉前结果集
						m_mds.setDsBeforeRotate(dataSet);
						//
						Object[] objs = DatasetUtil.getDatasetForRotateCross(rc, dataSet);
						// 获得结果集
						dataSet = (StorageDataSet) objs[0];
						// 篡改列名
						int iRowCount = (rc.getStrRows() == null) ? 0 : rc.getStrRows().length;
						DatasetUtil.tamperColname(dataSet, iRowCount);
						//
						m_mds.setDataSet(dataSet);
						// zjb+
						m_mds.setHashParam(hashParam);
						// 获得多表头
						FldgroupVO[] fldgroups = (FldgroupVO[]) objs[1];
						// 垃圾存放（交叉多表头）
						qmd.setFldgroups(fldgroups);
					}
				}
			}
		}
	}

	/**
	 * 获得结果集 创建日期：(2003-10-22 11:42:56)
	 * 
	 * @return com.borland.dx.dataset.StorageDataSet
	 */
	public BIMultiDataSet getMultiDataSet() {
		return m_mds;
	}

	/**
	 * 根据结果集数据获得结果集 创建日期：(2003-10-8 12:56:58)
	 * 
	 * @return com.borland.dx.dataset.StorageDataSet
	 * @param dsdwc
	 *            nc.vo.pub.querymodel.DataSetDataWithColumn
	 */
	protected StorageDataSet makeDataSet(QEDataSetDescriptor dsdwc) {
		StorageDataSet dataSet = new StorageDataSet();
		if(dsdwc instanceof QEDataSetAfterProcessDescriptor)
		{
		dsdwc.getDataSetData().loadDataSet(dataSet);
		// 添加列名
		for (int i = 0; i < dataSet.getColumnCount(); i++)
			dataSet.getColumn(i).setCaption(((QEDataSetAfterProcessDescriptor)dsdwc).getCaptions()[i]);
		}
		return dataSet;
	}

	/**
	 * ThreadProgress 构造子注解。
	 */
	public void run() {
		try {
			// 执行查询——查询、数据加工部分
			execQuery(m_qmd, m_hashParam, m_qe);
		} catch (Exception e) {
			AppDebug.debug(e);
		} finally {
			m_bEnd = true;
		}
	}

	/**
	 * 设置线程结束标志 创建日期：(01-9-10 10:08:37)
	 * 
	 * @return boolean
	 */
	public void setEnd(boolean bEnd) {
		m_bEnd = bEnd;
	}

	/**
	 * 设置查询信息 创建日期：(2003-10-22 11:36:43)
	 * 
	 * @param qmd
	 *            nc.vo.pub.querymodel.QueryModelDef
	 * @param hashParam
	 *            java.util.Hashtable
	 * @param qe
	 *            nc.vo.iuforeport.businessquery.QueryExecutor
	 */
	public void setQueryInfo(QueryModelDef qmd, Hashtable hashParam, BIQueryExecutor qe, String defDsName) {

		m_qmd = qmd;
		m_hashParam = hashParam;
		m_qe = qe;
		m_defDsName = defDsName;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.pub.querymodel.ThreadProgress#getResult()
	 */
	public Object getResult() {
		return getMultiDataSet();
	}
}
