package nc.ui.bi.query.designer;

import java.util.Hashtable;

import nc.ui.pub.beans.MessageDialog;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import nc.vo.pub.querymodel.ParamVO;
import nc.vo.pub.querymodel.QueryModelDef;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * �ڶ����ѯ������SQL����ƽ��� �������ڣ�(2005-5-13 16:55:14)
 * 
 * @author���쿡��
 */
public class FreeSqlDesignUI extends AbstractQueryDesignTabPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//����
	public final static int TAB_HANDSQL = 0;

	public final static int TAB_PROCESS = 1;
	
	private String		strSQLErrMsg = null;

	/**
	 * BusiModelDesignUI ������ע�⡣
	 */
	public FreeSqlDesignUI() {
		super();
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getResultFromPanels()
	 */
	public void getResultFromPanels(BIQueryModelDef qmd) {
		//Ԥִ��
		analyzeFlds();
		//������ý��
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).getResultFromPanel(qmd);
		}
		//ͬ���ֶ���ʾ��
		procMetaDispname();
	}

	/**
	 * �������������� �������ڣ�(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public AbstractQueryDesignSetPanel[] getSetPanels() {
		AbstractQueryDesignSetPanel pn1 = new SetHandSqlPanel();
		AbstractQueryDesignSetPanel pn2 = new SetProcessCodeUI();
		m_sps = new AbstractQueryDesignSetPanel[] { pn1, pn2 };
		return m_sps;
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#initSetPanels()
	 */
	public void setResultToPanels() {
		//��ò�ѯ����
		BIQueryModelDef qmd = getQueryModelDef();
		//��ʼ��
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).setResultToPanel(qmd);
		}
		//�����ֵ�
		((SetHandSqlPanel) getSetPanel(SetHandSqlPanel.TAB_TITLE)).initTree();

		//���ݼӹ�
		SetProcessCodeUI pn = ((SetProcessCodeUI) getSetPanel(SetProcessCodeUI.TAB_TITLE));
		pn.init();

		//Ԫ���ݳ�ʼ������
		initMetaDataHash(qmd.getMetadatas());
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getMetaDatas()
	 */
	public MetaDataVO[] getMetaDatas() {
		//Ԥִ��
		analyzeFlds();
		//
		SelectFldVO[] sfs = getQueryBaseDef().getSelectFlds();
		int iLen = (sfs == null) ? 0 : sfs.length;
		MetaDataVO[] mds = new MetaDataVO[iLen];
		Hashtable hashFldDim = getMetaDataPanel().getHashFldDim();
		for (int i = 0; i < iLen; i++) {
			String fldAlias = sfs[i].getFldalias();
			mds[i] = new MetaDataVO();
			mds[i].setFldname(sfs[i].getFldname());
			mds[i].setFldalias(fldAlias);
			mds[i].setColtype(sfs[i].getColtype());
			mds[i].setPrecision(sfs[i].getPrecision());
			mds[i].setScale(sfs[i].getScale());
			//���Ԫ����
			if (hashFldDim.containsKey(fldAlias)) {
				Object[] objs = (Object[]) hashFldDim.get(fldAlias);
				//ά�ȱ�־
				boolean bDimflag = ((Boolean) objs[0]).booleanValue();
				mds[i].setDimflag(bDimflag);
				//ά������
				DimensionVO dimdef = (DimensionVO) objs[1];
				mds[i].setPk_dimdef(dimdef.getPrimaryKey());
				mds[i].setDimname(dimdef.getDimname());
			}
		}
		return mds;
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getMetaDatas()
	 */
	@SuppressWarnings("unchecked")
	private void analyzeFlds() {
		QueryBaseDef qbd = getQueryBaseDef();
		String handSql = ((SetHandSqlPanel) getSetPanel(SetHandSqlPanel.TAB_TITLE))
				.getHandSql();

		//������������
		try {
			QueryModelDef qmd = getQueryModelDef().getBaseModel();
			//��ò���
			ParamVO[] params = qmd.getParamVOs();
			//�����滻�Ͳ���
			Hashtable hashParam = BIModelUtil.makeHashForReplaceParam(params);
			//�ӻ�����Ϣ
			hashParam = BIModelUtil.addEnvInfo(hashParam);
			//����Զ��廷������
			//			IEnvParam iEnvParam = null; //BI
			//			Object[] objEnvParams = new Object[] { iEnvParam,
			// qmd.getDsName(),
			//					new EnvInfo(true) };
			//			hashParam.put(QueryConst.ENV_PARAM_KEY, objEnvParams);
			//���SQL
			handSql = BIModelUtil.replaceHandSql(handSql, hashParam);
		} catch (Exception e) {
			AppDebug.debug(e);
		}

		//Ԥִ�н���
		try {
			qbd = BIModelUtil.getBaseDefBySql(qbd, handSql, getMetaDataPanel()
					.getHashAliasDisp());
			setQueryBaseDef(qbd);
			getQueryModelDef().getBaseModel().setQueryBaseVO(qbd);
			strSQLErrMsg = null;
		} catch (Exception e) {
			AppDebug.debug(e);
			strSQLErrMsg = getSQLExecErrMsg(e);
			MessageDialog.showWarningDlg(this, "UFBI", strSQLErrMsg);
		}
	}
	public String doCheck() {
		if( strSQLErrMsg != null ){
			return strSQLErrMsg;
		}
		return super.doCheck();
	}
}