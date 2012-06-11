package nc.ui.bi.query.designer;
import java.util.Hashtable;

import nc.ui.pub.beans.UIPanel;
import nc.vo.bi.query.manager.BIQueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * ��ѯ���������� �������ڣ�(2005-5-13 16:39:25)
 * 
 * @author���쿡��
 */
public abstract class AbstractQueryDesignSetPanel extends UIPanel {

	//��ѯ���ҳǩʵ��
	private AbstractQueryDesignTabPanel m_tabPn = null;

	/**
	 * AbstractQueryDesignSetPanel ������ע�⡣
	 */
	public AbstractQueryDesignSetPanel() {
		super();
		initialize();
	}

	/**
	 * ��������� �������ڣ�(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public abstract String getPanelTitle();

	/**
	 * ��ý�� �������ڣ�(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public abstract void getResultFromPanel(BIQueryModelDef qmd);

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
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
	 * ���ý�� �������ڣ�(2005-5-13 16:43:08)
	 * 
	 * @return nc.vo.pub.ValueObject[]
	 */
	public abstract void setResultToPanel(BIQueryModelDef qmd);

	/**
	 * @return ���� tabPn��
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
	 * �Ϸ���У��
	 */
	public abstract String check();

	/**
	 * ҳ�л�ʱУ�飬ȱʡ����check()
	 * @return
	 */
	public String checkOnSwitch(){
		return check();
	}
	/**
	 * �����ж��ظ��Ĺ�ϣ�� �������ڣ�(2003-10-28 9:04:53)
	 */
	public Hashtable getHashTableId() {
		return null;
	}
}
 