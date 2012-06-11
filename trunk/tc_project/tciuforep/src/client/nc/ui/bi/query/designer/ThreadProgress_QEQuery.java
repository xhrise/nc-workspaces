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
 * ��ѯִ�н����߳� �������ڣ�(2003-10-21 12:05:29)
 * 
 * @author���쿡��
 */
public class ThreadProgress_QEQuery extends ThreadProgress {
	// ��ѯģ�Ͷ���
	protected QueryModelDef m_qmd = null;

	// ������ϣ��
	protected Hashtable m_hashParam = null;

	// ��ѯִ����
	protected BIQueryExecutor m_qe = null;

	// ��ѯ�����
	protected BIMultiDataSet m_mds = null;

	// ��������Դ
	protected String m_defDsName = null;

	/**
	 * ThreadProgress ������ע�⡣
	 */
	public ThreadProgress_QEQuery() {
		super();
	}

	/**
	 * ���ȫ������ �������ڣ�(2004-6-8 16:28:20)
	 */
	public void clear() {
		// ��ѯģ�Ͷ���
		m_qmd = null;
		// ������ϣ��
		m_hashParam = null;
		// ��ѯִ����
		m_qe = null;
		// ��ѯ�����
		m_mds = null;
		// ��������Դ
		m_defDsName = null;
	}

	/**
	 * ִ�в�ѯ �������ڣ�(2003-10-22 11:25:32)
	 */
	protected void execQuery(QueryModelDef qmd, Hashtable hashParam, BIQueryExecutor qe) throws Exception {

		qmd.setFldgroups(null);

//		// ҵ������ʱ���� TODO ��ʱע��2006-06-29���˷���Ӧ��û���ô�
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
			// ���ݼӹ�
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
				// ��üӹ���Ĳ�����ϣ��
				m_mds.setHashParam(dsdwc.getHashParam());
			}
		} else {
			SimpleCrossVO[] scs = qmd.getScs();
			if (scs != null) {
				MergeHeaderVO mh = qmd.getMh();
				StorageDataSet dataSet = null;
				if (mh != null) {
					// �ϲ�Ԫ��
					dataSet = DatasetUtil.getDatasetForMergeElementCross(mh, hashParam, m_defDsName);
				} else {
					// �򵥽���
					QueryBaseDef qbd = qe.getQueryBaseDef();
					dataSet = DatasetUtil.getDatasetForSimpleCross(scs, qbd, hashParam);
				}
				m_mds = new BIMultiDataSet();
				m_mds.setDataSet(dataSet);
			} else {
				QueryBaseDef qbd = qe.getQueryBaseDef();
				// qbd.setTempletId("union");
				// ��ò�ѯ�����
				if (qbd.getSelectFlds() != null) {
					StorageDataSet dataSet = qe.getDataSet_query();
					m_mds = new BIMultiDataSet();
					m_mds.setDataSet(dataSet);
					// ��ת����
					RotateCrossVO rc = qbd.getRotateCross();
					if (rc != null) {
						// ��������ǰ�����
						m_mds.setDsBeforeRotate(dataSet);
						//
						Object[] objs = DatasetUtil.getDatasetForRotateCross(rc, dataSet);
						// ��ý����
						dataSet = (StorageDataSet) objs[0];
						// �۸�����
						int iRowCount = (rc.getStrRows() == null) ? 0 : rc.getStrRows().length;
						DatasetUtil.tamperColname(dataSet, iRowCount);
						//
						m_mds.setDataSet(dataSet);
						// zjb+
						m_mds.setHashParam(hashParam);
						// ��ö��ͷ
						FldgroupVO[] fldgroups = (FldgroupVO[]) objs[1];
						// ������ţ�������ͷ��
						qmd.setFldgroups(fldgroups);
					}
				}
			}
		}
	}

	/**
	 * ��ý���� �������ڣ�(2003-10-22 11:42:56)
	 * 
	 * @return com.borland.dx.dataset.StorageDataSet
	 */
	public BIMultiDataSet getMultiDataSet() {
		return m_mds;
	}

	/**
	 * ���ݽ�������ݻ�ý���� �������ڣ�(2003-10-8 12:56:58)
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
		// �������
		for (int i = 0; i < dataSet.getColumnCount(); i++)
			dataSet.getColumn(i).setCaption(((QEDataSetAfterProcessDescriptor)dsdwc).getCaptions()[i]);
		}
		return dataSet;
	}

	/**
	 * ThreadProgress ������ע�⡣
	 */
	public void run() {
		try {
			// ִ�в�ѯ������ѯ�����ݼӹ�����
			execQuery(m_qmd, m_hashParam, m_qe);
		} catch (Exception e) {
			AppDebug.debug(e);
		} finally {
			m_bEnd = true;
		}
	}

	/**
	 * �����߳̽�����־ �������ڣ�(01-9-10 10:08:37)
	 * 
	 * @return boolean
	 */
	public void setEnd(boolean bEnd) {
		m_bEnd = bEnd;
	}

	/**
	 * ���ò�ѯ��Ϣ �������ڣ�(2003-10-22 11:36:43)
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
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.pub.querymodel.ThreadProgress#getResult()
	 */
	public Object getResult() {
		return getMultiDataSet();
	}
}
