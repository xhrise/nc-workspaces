package nc.ui.bi.query.designer;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Hashtable;

import nc.ui.bi.query.manager.QueryModelBO_Client;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.iuforeport.businessquery.QueryBaseDef;
import nc.vo.iuforeport.businessquery.SelectFldVO;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * ��һ���ѯ��ҵ����ͼ����ƽ��� �������ڣ�(2005-5-13 16:55:14)
 * 
 * @author���쿡��
 */
public class WizardSqlDesignUI extends AbstractQueryDesignTabPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//����
	public final static int TAB_TABLE = 0;

	public final static int TAB_JOIN = 1;

	public final static int TAB_FLD = 2;

	public final static int TAB_COND = 3;

	public final static int TAB_ORDERBY = 4;

	public final static int TAB_PROCESS = 5;

	public final static int TAB_METADATA = 5;
	
	private String	strSQLErrMsg = null;

	/**
	 * BusiModelDesignUI ������ע�⡣
	 */
	public WizardSqlDesignUI() {
		super();
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getResultFromPanels()
	 */
	public void getResultFromPanels(BIQueryModelDef qmd) {
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).getResultFromPanel(qmd);
		}
		//ͬ���ֶ���ʾ��
		procMetaDispname();

		//����ֹ�SQL��Ϣ���з���,Ӱ��getSqlByBaseDef��
		//		try {
		//			String sql = BIModelUtil.getSqlByBaseDef(qmd.getQueryBaseDef(),
		//					null);
		//			qmd.getQueryBaseVO().setHandSql(sql);
		//		} catch (Exception e) {
		//			AppDebug.debug(e);
		//		}
	}

	/**
	 * �������������� �������ڣ�(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public AbstractQueryDesignSetPanel[] getSetPanels() {
		AbstractQueryDesignSetPanel pn1 = new SetTablePanel();
		AbstractQueryDesignSetPanel pn2 = new SetJoinPanel();
		AbstractQueryDesignSetPanel pn3 = new SetFldPanel();
		AbstractQueryDesignSetPanel pn4 = new SetCondPanel();
		AbstractQueryDesignSetPanel pn5 = new SetOrderbyPanel();
		AbstractQueryDesignSetPanel pn6 = new SetProcessCodeUI();
		m_sps = new AbstractQueryDesignSetPanel[] { pn1, pn2, pn3, pn4, pn5,
				pn6 };
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

		//�������ݱ�ҳǩ�����ֵ�
		//((SetTablePanel) getSetPanel(TAB_TABLE)).setDatadict(getDatadict());
		//�����ʼ��
		((SetJoinPanel) getSetPanel(SetJoinPanel.TAB_TITLE)).initEditorValue();
		((SetCondPanel) getSetPanel(SetCondPanel.TAB_TITLE)).initEditorValue();

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
		//����QBD
		refreshQbd();
		//Ԥִ��
		changeFlds(getQueryBaseDef());
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
	private void changeFlds(QueryBaseDef qbd) {
		String dsNameForDef = getDefDsName();
		try {
			//����нṹ��Ϣ
			ResultSetMetaData rsmd = QueryModelBO_Client.getFldType(qbd, null,
					dsNameForDef);
			int iColCount = rsmd.getColumnCount();
			//���ÿ�е���������
			Integer[] iColTypes = new Integer[iColCount];
			int[] iPrecision = new int[iColCount];
			int[] iScale = new int[iColCount];
			for (int i = 0; i < iColCount; i++) {
				iColTypes[i] = new Integer(rsmd.getColumnType(i + 1));
				iPrecision[i] = rsmd.getPrecision(i + 1);
				iScale[i] = rsmd.getScale(i + 1);
				//���������Ͳ�ѯ�ֶΣ���ΪMETADATA�޷��õ���ȷ���ȣ�
				if (iColTypes[i].intValue() == Types.NUMERIC
						&& iPrecision[i] == 0 && iScale[i] == 0) {
					iPrecision[i] = 38;
					iScale[i] = 8;
				}
			}

			SelectFldVO[] sfs = qbd.getSelectFlds();
			int iLen = sfs.length;
			if (iColTypes.length != iLen) {
				MessageDialog.showWarningDlg(this,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000099")/* @res "��ѯ����" */,
						NCLangRes.getInstance().getStrByID("10241201",
								"UPP10241201-000831")/* @res "�����ܣ�" */);
				return;
			}
			String strHint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"10241201", "UPP10241201-000832")/* @res "�﷨У��ͨ����" */;
			for (int i = 0; i < iLen; i++) {
				sfs[i].setColtype(iColTypes[i]);
				sfs[i].setPrecision(iPrecision[i]);
				sfs[i].setScale(iScale[i]);
				String strType = BIModelUtil.isNumberType(iColTypes[i]
						.intValue()) ? NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000833")/*
														  * @res "��ֵ��"
														  */
				: NCLangRes.getInstance().getStrByID("10241201",
						"UPP10241201-000328")/* @res "�ַ���" */;
				strHint += "\n        " + sfs[i].getFldname() + " -- "
						+ strType + ":" + iColTypes[i].intValue();
			}
			strSQLErrMsg = null;
			AppDebug.debug(strHint);//@devTools System.out.println(strHint);
		} catch (Exception e) {
			AppDebug.debug(e);
			strSQLErrMsg = getSQLExecErrMsg(e);
			MessageDialog.showWarningDlg(this, "UFBI", strSQLErrMsg);
		}
	}

	/**
	 * ����QBD
	 */
	public void refreshQbd() {
		((SetTablePanel) getSetPanel(SetTablePanel.TAB_TITLE)).refreshQbd();
		((SetJoinPanel) getSetPanel(SetJoinPanel.TAB_TITLE)).refreshQbd();
		((SetFldPanel) getSetPanel(SetFldPanel.TAB_TITLE)).refreshQbd();
		((SetCondPanel) getSetPanel(SetCondPanel.TAB_TITLE)).refreshQbd();
		((SetOrderbyPanel) getSetPanel(SetOrderbyPanel.TAB_TITLE)).refreshQbd();
	}

	/**
	 * ��ʾ��Ϣ
	 */
	public String getShowInfo() {
		//����
		refreshQbd();
		//���SQL
		String sql = BIModelUtil.getSqlByBaseDef(getQueryBaseDef(), null);
		return sql;
	}
	
	public String doCheck() {
		if( strSQLErrMsg != null ){
			return strSQLErrMsg;
		}
		return  super.doCheck();
	}


}