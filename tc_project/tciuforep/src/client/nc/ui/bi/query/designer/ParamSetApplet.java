/*
 * �������� 2005-7-6
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
 * ��������APPLET
 */
public class ParamSetApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//�����������ʵ��
	private ParamSetPanel m_paramSetPn = null;

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
		//��ò���
		ParamVO[] params = BIModelUtil.getParams(queryId, dsNameForDef);
		//�������
		getParamSetPn().setDefDsName(dsNameForDef);
		getParamSetPn().setParamsArray(new ParamVO[][] { params },
				new String[] { queryId });
		//�������
		getJAppletContentPane().add(getParamSetPn(), BorderLayout.CENTER);
	}

	/**
	 * ���������ʵ�� �������ڣ�(2005-5-16 19:05:13)
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
