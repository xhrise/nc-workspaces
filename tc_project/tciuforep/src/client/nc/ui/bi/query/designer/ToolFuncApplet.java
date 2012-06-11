/*
 * �������� 2005-7-19
 *
 */
package nc.ui.bi.query.designer;

import java.awt.BorderLayout;

import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.iufo.pub.DataManageObjectIufo;

/**
 * @author zjb
 * 
 * ��������APPLET
 */
public class ToolFuncApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//�����������ʵ��
	private ToolFuncPanel m_pnToolFunc = null;

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.BIDesignApplet#initUI()
	 */
	public void initUI() {
		//��ö�������Դ
		String dsNameForDef = DataManageObjectIufo.IUFO_DATASOURCE;//getParameter(BIQueryConst.APPLET_DSNAME_DEF);
		//��ò�ѯID
		String queryId = getParameter(BIQueryConst.APPLET_QUERY_ID);
		//�������
		getPnToolFunc().setDsNameForDef(dsNameForDef);
		getPnToolFunc().setQueryId(queryId);
		//�������
		getJAppletContentPane().add(getPnToolFunc(), BorderLayout.CENTER);
	}

	/**
	 * ��ø����������ʵ�� �������ڣ�(2005-5-16 19:05:13)
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
