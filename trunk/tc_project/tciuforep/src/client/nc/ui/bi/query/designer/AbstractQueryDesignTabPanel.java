package nc.ui.bi.query.designer;
import java.util.Hashtable;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UITabbedPane;
import nc.vo.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIQueryConst;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.bi.query.manager.QueryModelVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.ddc.datadict.Datadict;
import nc.vo.pub.querymodel.QueryBaseVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * ��ѯ�������ҳǩ �������ڣ�(2005-5-13 16:45:23)
 * 
 * @author���쿡��
 */
public abstract class AbstractQueryDesignTabPanel extends UITabbedPane
		implements ChangeListener {

	//��ѯ������
	private BIQueryModelDef m_qmd = null;

	//��ѯ��������
	private QueryBaseDef m_qbd = null;

	//�����������
	protected AbstractQueryDesignSetPanel[] m_sps = null;

	//��������Դ
	private String m_defDsName = null;

	//�����ֵ�ʵ��
	private Datadict m_datadict = null;

	//��ѯ������ʵ��
	private QueryDesignPanel m_designPanel = null;

	//�ϴ�ѡ��ҳǩ���
	private int m_iLastSelTabIndex = 0;

	/**
	 * AbstractQueryDesignTabPanel ������ע�⡣
	 */
	public AbstractQueryDesignTabPanel() {
		super();
		initialize();
	}

	/**
	 * AbstractQueryDesignTabPanel ������ע�⡣
	 * 
	 * @param p0
	 *            int
	 */
	public AbstractQueryDesignTabPanel(int p0) {
		super(p0);
	}

	/**
	 * �������������� �������ڣ�(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public abstract AbstractQueryDesignSetPanel[] getSetPanels();

	/**
	 * ������Ż��ĳ��������� �������ڣ�(2005-5-13 16:48:29)
	 */
	public AbstractQueryDesignSetPanel getSetPanel(int iIndex) {
		return m_sps[iIndex];
	}

	/**
	 * ���ݱ���ID���ĳ��������� �������ڣ�(2005-5-13 16:48:29)
	 */
	public AbstractQueryDesignSetPanel getSetPanel(String title) {
		int iIndex = -1;
		for (int i = 0; i < m_sps.length; i++) {
			if (m_sps[i].getPanelTitle().equals(title)) {
				iIndex = i;
				break;
			}
		}
		return m_sps[iIndex];
	}

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
	 * ��ʼ�� �������ڣ�(2005-5-13 16:49:44)
	 */
	public void init() {
		AbstractQueryDesignSetPanel[] pns = getSetPanels();
		int iLen = (pns == null) ? 0 : pns.length;
		//ȥ������
		removeChangeListener(this);
		//����ҳǩ
		for (int i = 0; i < iLen; i++) {
			insertTab(StringResource.getStringResource(pns[i].getPanelTitle()), null, pns[i], null, i);
			pns[i].setTabPn(this);
		}


		//����Ԫ�������ý���
		AbstractQueryDesignSetPanel pnMetaData = new SetMetaDataPanel(isMetaDataEditable());
		//�����ﻯ�������ý���
		AbstractQueryDesignSetPanel pnMaterSet = new SetMaterPanel();
		

		//����m_sps
		iLen = (m_sps == null) ? 0 : m_sps.length;
		AbstractQueryDesignSetPanel[] temps = new AbstractQueryDesignSetPanel[iLen + 2];
		for (int i = 0; i < iLen; i++) {
			temps[i] = m_sps[i];
		}
		temps[iLen] = pnMetaData;
		temps[iLen+1] = pnMaterSet;
		m_sps = temps;
		
		insertTab(StringResource.getStringResource(pnMetaData.getPanelTitle()), null, pnMetaData, null, iLen);
		pnMetaData.setTabPn(this);
		insertTab(StringResource.getStringResource(pnMaterSet.getPanelTitle()), null, pnMaterSet, null, iLen+1);
		pnMaterSet.setTabPn(this);
		//��Ӽ���
		addChangeListener(this);
		
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("AbstractQueryDesignTabPanel");
			setSize(480, 320);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		addChangeListener(this);
		init();
		// user code end
	}

	/**
	 * ���ò�ѯ���� �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setQueryModelDef(BIQueryModelDef qmd) {
		m_qmd = qmd;
		m_qbd = qmd.getBaseModel().getQueryBaseDef();
		m_qbd.setDsName(qmd.getDsName());
		//��ʼ���������
		setResultToPanels();
	}

	/**
	 * ���ò�ѯ�������� �������ڣ�(2005-5-16 18:38:58)
	 */
	public void setQueryBaseDef(QueryBaseDef qbd) {
		m_qbd = qbd;
	}

	/**
	 * ����������ý�� �������ڣ�(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public abstract void getResultFromPanels(BIQueryModelDef qmd);

	/**
	 * ��ʼ��������� �������ڣ�(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public abstract void setResultToPanels();

	/**
	 * ��ò�ѯ����
	 */
	protected BIQueryModelDef getQueryModelDef() {
		return m_qmd;
	}

	/**
	 * ���Ԫ����VO����������Ԫ����ҳǩ
	 */
	public abstract MetaDataVO[] getMetaDatas();

	/**
	 * @return ���� datadict��
	 */
	public Datadict getDatadict() {
		return m_datadict;
	}

	/**
	 * @param datadict
	 *            Ҫ���õ� datadict��
	 */
	public void setDatadict(Datadict datadict) {
		m_datadict = datadict;
	}

	/**
	 * @return ���� String��
	 */
	public String getDefDsName() {
		return m_defDsName;
	}

	/**
	 * @return ���� String��
	 */
	public QueryBaseDef getQueryBaseDef() {
		return m_qbd;
	}

	/**
	 * ������
	 */
	public void saveResult() {
		String dsNameForDef = getDefDsName();
		//��ý��
		getResultFromPanels(m_qmd);
		try {
			//ת��ΪXML
			//String definition = BIQueryDefToXmlUtil.saveQueryDefToXml(m_qmd);
			//ִ�и���
			QueryModelBO_Client.updateQmd(m_qmd.getID(), m_qmd,
					dsNameForDef);
		} catch (Exception e) {
			AppDebug.debug(e);
		}
	}

	/*
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		//�����ֶ�-Ԫ���ݹ�ϣ��
		int iTabCount = getTabCount();
		int iSelIndex = getSelectedIndex();
		if (m_iLastSelTabIndex == iTabCount - 2) {

			//ˢ���ֶ�-Ԫ���ݹ�ϣ��
			getMetaDataPanel().refreshHashFldDim();
		} else if (getSetPanel(iSelIndex).getPanelTitle().equals(
				SetMetaDataPanel.TAB_TITLE) ) {

			//��������Ԫ����
			MetaDataVO[] mds = getMetaDatas();
			//����Ԫ����
			BIQueryModelDef qmd = getQueryModelDef();
			qmd.setMetadatas(mds);
			getMetaDataPanel().setResultToPanel(qmd);
		}
		if( m_iLastSelTabIndex == iTabCount - 1) {
			getDesignPanel().setBnOkEnable(false);
			getDesignPanel().setBnNextEnable(true);
		}else if( getSetPanel(iSelIndex).getPanelTitle().equals(
				SetMaterPanel.TAB_TITLE)) {
			getDesignPanel().setBnOkEnable(true);
			getDesignPanel().setBnNextEnable(false);
			//getDesignPanel().setBN
			//��������Ԫ����
			MetaDataVO[] mds = getMetaDatas();
			//����Ԫ����
			BIQueryModelDef qmd = getQueryModelDef();
			qmd.setMetadatas(mds);
			getMaterPanel().setResultToPanel(qmd);
		}
		
		if( iSelIndex >0 ){
			getDesignPanel().setBnLastEnable(true);
		}else{
			getDesignPanel().setBnLastEnable(false);
		}
		m_iLastSelTabIndex = iSelIndex;
	}

	/**
	 * ���Ԫ�����������
	 */
	public SetMetaDataPanel getMetaDataPanel() {
		int iLen = m_sps.length;
		return (SetMetaDataPanel) getSetPanel(iLen - 2);
	}
	
	public SetMaterPanel	getMaterPanel(){
		int iLen = m_sps.length;
		return (SetMaterPanel) getSetPanel(iLen - 1);
	}

	/**
	 * ��ʼ������-Ԫ���ݹ�ϣ��
	 */
	public void initMetaDataHash(MetaDataVO[] mds) {
		int iLen = (mds == null) ? 0 : mds.length;
		Hashtable<String, Object[]> hashFldDim = new Hashtable<String, Object[]>();
		for (int i = 0; i < iLen; i++) {
			boolean bDimflag = mds[i].getDimflag();
			DimensionVO dimdef = new DimensionVO();
			dimdef.setDimname("");
			if( bDimflag && mds[i].getPk_dimdef() != null &&
				mds[i].getPk_dimdef().length()>0){
				//DimensionVO �Ƿ����
				DimensionVO dimVO = DimensionSrv.getByID(mds[i].getPk_dimdef());
				if( dimVO != null ){
					dimdef = dimVO;
				}
				//dimdef.setPrimaryKey(mds[i].getPk_dimdef());
			}
			//dimdef.setDimname(mds[i].getDimname()== null?"":mds[i].getDimname());
			//ά���ֶ�-Ԫ���ݹ�ϣ��
			hashFldDim.put(mds[i].getFldalias(), new Object[] {
					new Boolean(bDimflag), dimdef });
		}
		getMetaDataPanel().setHashFldDim(hashFldDim);
	}

	/**
	 * @return ���� designPanel��
	 */
	public QueryDesignPanel getDesignPanel() {
		return m_designPanel;
	}

	/**
	 * @param designPanel
	 *            Ҫ���õ� designPanel��
	 */
	public void setDesignPanel(QueryDesignPanel designPanel) {
		m_designPanel = designPanel;
	}

	/**
	 * �Ϸ���У��
	 */
	public String doCheck() {
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			String strErr = getSetPanel(i).check();
			if (strErr != null) {
				return StringResource.getStringResource(getSetPanel(i).getPanelTitle()) + ": " + strErr;
			}
		}
		return null;
	}

	/**
	 * ͬ���ֶ���ʾ������������
	 */
	protected void procMetaDispname() {
		QueryBaseVO qb = getQueryModelDef().getQueryBaseVO();
		if (qb != null) {
			SelectFldVO[] sfs = qb.getSelectFlds();
			MetaDataVO[] mds = getQueryModelDef().getMetadatas();
			int iLen = sfs.length;
			for (int i = 0; i < iLen; i++) {
				sfs[i].setFldname(mds[i].getFldname());
				sfs[i].setColtype(mds[i].getColtype());
			}
		}
	}

	/**
	 * ���ݲ�ѯ���󴴽���ѯ��
	 */
	public static AbstractQueryDesignTabPanel createTabPn(QueryModelVO qm) {
		//���ݲ�ͬ��ѯ���Ͳ��ò�ͬ��ƽ���
		AbstractQueryDesignTabPanel tabPn = null;
		if (qm.getType().equals(BIQueryConst.TYPE_WIZARDSQL)) {
			tabPn = new WizardSqlDesignUI();
		} else if (qm.getType().equals(BIQueryConst.TYPE_FREESQL)) {
			tabPn = new FreeSqlDesignUI();
		} else if (qm.getType().equals(BIQueryConst.TYPE_BUSIFUNC)) {
			tabPn = new BusiFunctionDesignUI();
		} else if( qm.getType().equals(BIQueryConst.TYPE_INPUTMODEL)){//�ɼ�ģ��
			tabPn = new InputModelDesignUI();
		}else{
			tabPn = new BusiModelDesignUI();
		}
		return tabPn;
	}

	/**
	 * ���ö�������Դ
	 */
	public void setDefDsName(String defDsName) {
		m_defDsName = defDsName;
	}

	/**
	 * ��ʾ��Ϣ
	 */
	public String getShowInfo() {
		return "";
	}
	
	protected boolean isMetaDataEditable(){
		return false;
	}
	protected  String  getSQLExecErrMsg(Exception e){
		String strErrMsg = StringResource.getStringResource("mbiquery0119");//SQL �﷨����
		String		strExceptionMsg = e.getMessage();
		int			nPos = strExceptionMsg.indexOf("java.sql.SQLException:");
		if( nPos >-1){
			nPos += "java.sql.SQLException:".length();
			strErrMsg += strExceptionMsg.substring(nPos);
		}else{
			strErrMsg += e.getMessage();
		}
		return strErrMsg;
	}

	/**
	 * �л�ҳǩ,�ڴ�֮ǰ��ҳ����
	 */
	public void setSelectedIndex(int index) {

		if( m_sps != null ){
			//�õ���һ��
			int   nSelIndex = getSelectedIndex();
			if( nSelIndex >=0 ){
				String strErrMsg =  m_sps[nSelIndex].checkOnSwitch();
				if( strErrMsg != null ){
					MessageDialog.showErrorDlg(this, "", strErrMsg);
					return;
				}
			}
		}
		super.setSelectedIndex(index);
	}
	
} 