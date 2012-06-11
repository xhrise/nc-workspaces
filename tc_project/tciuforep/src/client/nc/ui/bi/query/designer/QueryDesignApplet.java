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
 * ��ѯ���Applet
 * 
 * �������ڣ�(2005-5-12 18:38:02)
 * 
 * @author���쿡��
 */
public class QueryDesignApplet extends BIDesignApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//��ѯ������ʵ��
	private QueryDesignPanel m_designPn = null;

	//��ѯ����-��ѯ�������ϣ��
	private Hashtable<String, BIQueryModelDef> m_hashQMD = new Hashtable<String, BIQueryModelDef>();

	//����Դ-�����ֵ��ϣ��
	private Hashtable<String, Datadict> m_hashDsDD = new Hashtable<String, Datadict>();

	/**
	 * ����ڵ� - ��������ΪӦ�ó�������ʱ���������������
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
			System.err.println("JApplet �� main() �з����쳣");
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
	 * ���ò�ѯ������ʵ�� �������ڣ�(2005-5-16 19:05:13)
	 */
	public void setDesignPn(QueryDesignPanel newDesignPn) {
		m_designPn = newDesignPn;
	}

	/**
	 * ���ò�ѯ���� �������ڣ�(2005-5-16 18:38:58)
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
	 * ���ò�ѯ�������ҳǩ �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setTabPn(AbstractQueryDesignTabPanel tabPn) {
		getDesignPn().setTabPn(tabPn);
	}

	/**
	 * ���ö�������Դ �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setDefDsName(String dsNameForDef) {
		getDesignPn().setDefDsName(dsNameForDef);
	}

	/**
	 * ��ò�ѯ������ʵ�� �������ڣ�(2005-5-16 19:05:13)
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
	 * ��������Դ��������ֵ�
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
	 * ��������Դ��������ֵ�
	 */
	private BIQueryModelDef getQueryModelDef(QueryModelVO qm) {
		BIQueryModelDef qmd = null;
		String queryId = qm.getPrimaryKey();
		if (m_hashQMD.containsKey(queryId)) {
			qmd = (BIQueryModelDef) m_hashQMD.get(queryId);
		} else {
			//��ö�������Դ
//			String dsNameForDef = getParameter(BIQueryConst.APPLET_DSNAME_DEF);
			qmd = BIQueryUtil.getQueryModelDef(qm, null);
			m_hashQMD.put(queryId, qmd);
		}
		return qmd;
	}

	/**
	 * ����������ò�ѯ����
	 */
	private QueryModelVO getQueryModel(String queryId) {
		String strWhere = "pk_querymodel='" + queryId + "'";
		//��ö�������Դ
//		String dsNameForDef = getParameter(BIQueryConst.APPLET_DSNAME_DEF);
		//��ò�ѯ��Ϣ
		QueryModelVO[] qms = BIQueryUtil.getQueryModels(strWhere, null);
		return (qms == null || qms.length == 0) ? null : qms[0];
	}

	/**
	 * Design
	 */
	private void doDesign() {
		//��ö�������Դ
		String dsNameForDef = getParameter(BIQueryConst.APPLET_DSNAME_DEF);
		//��ò�ѯID
		String queryId = getParameter(BIQueryConst.APPLET_QUERY_ID);
		//System.out.println("QUERY_ID = " + queryId);

		//��ò�ѯ��Ϣ
		QueryModelVO qm = getQueryModel(queryId);
		if (qm == null) {
			return;
		}
		//���ݲ�ͬ��ѯ���Ͳ��ò�ͬ��ƽ���
		AbstractQueryDesignTabPanel tabPn = AbstractQueryDesignTabPanel
				.createTabPn(qm);

		//��������Դ��������ֵ�
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
				//"�޷��õ������ֵ���Ϣ,��������Դ{0}�Ƿ���Է���");
			}
		}
		//���ݲ�ѯID��ò�ѯ����
		BIQueryModelDef qmd = getQueryModelDef(qm);
		//
		setTabPn(tabPn);
		setDefDsName(dsNameForDef);
		setDatadict(dd);
		setQueryModelDef(qmd);
	}

	/**
	 * ��ʼ������ �������ڣ�(2003-10-17 10:22:29)
	 */
	public void initUI() {
		getJAppletContentPane().add(getDesignPn(), BorderLayout.CENTER);
		//���
		doDesign();
	}
}  