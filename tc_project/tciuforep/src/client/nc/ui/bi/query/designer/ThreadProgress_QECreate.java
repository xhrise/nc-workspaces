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
 * ��ѯִ�н����߳� �������ڣ�(2003-10-21 12:05:29)
 * 
 * @author���쿡��
 */
public class ThreadProgress_QECreate extends ThreadProgress_QEQuery {
	//��ѯ��¼��
	private int m_iRecordCount = 0;

	/**
	 * ThreadProgress ������ע�⡣
	 */
	public ThreadProgress_QECreate() {
		super();
	}

	/**
	 * ִ�в�ѯ �������ڣ�(2003-10-22 11:25:32)
	 * @i18n miufo00461=�޲�ѯ�ֶ�
	 */
	protected void execQuery(QueryModelDef qmd, Hashtable hashParam,
			BIQueryExecutor qe) throws Exception {

		//���ݼӹ�����Ҫ��ѯԤִ��
		DataProcessVO dp = qmd.getDataProcessVO();
		if (ModelUtil.isCodeEmpty(dp)) {
			//���SQL����֮��Ĳ�ѯ����
			QueryBaseDef qbd = ModelUtil.getBaseDefAfterRepair(qmd, hashParam);
			//����ʱ��
			qe.setQueryBaseDef(qbd);
			qe.setHashParam(hashParam);
			if (qbd.getSelectFlds() == null) {
				AppDebug.debug(StringResource.getStringResource("miufo00461"));//@devTools System.out.println("�޲�ѯ�ֶ�");
				//ִ�в�ѯ
				super.execQuery(qmd, hashParam, qe);
				return;
			}
			if (qmd.getScs() == null)
				m_iRecordCount = qe.getDataSet_create();
		}

		if (m_iRecordCount <= QueryConst.ALERT_RECORD_COUNT)
			//ִ�в�ѯ
			super.execQuery(qmd, hashParam, qe);
	}

	/**
	 * ��ü�¼���� �������ڣ�(2003-10-22 11:41:32)
	 * 
	 * @return int
	 */
	public int getRecordCount() {
		return m_iRecordCount;
	}

	/*
	 * ���� Javadoc��
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