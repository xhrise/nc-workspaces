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
 * 第二类查询（自由SQL）设计界面 创建日期：(2005-5-13 16:55:14)
 * 
 * @author：朱俊彬
 */
public class FreeSqlDesignUI extends AbstractQueryDesignTabPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//常量
	public final static int TAB_HANDSQL = 0;

	public final static int TAB_PROCESS = 1;
	
	private String		strSQLErrMsg = null;

	/**
	 * BusiModelDesignUI 构造子注解。
	 */
	public FreeSqlDesignUI() {
		super();
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getResultFromPanels()
	 */
	public void getResultFromPanels(BIQueryModelDef qmd) {
		//预执行
		analyzeFlds();
		//获得设置结果
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).getResultFromPanel(qmd);
		}
		//同步字段显示名
		procMetaDispname();
	}

	/**
	 * 获得设置面板数组 创建日期：(2005-5-13 16:48:29)
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
		//获得查询对象
		BIQueryModelDef qmd = getQueryModelDef();
		//初始化
		int iLen = m_sps.length;
		for (int i = 0; i < iLen; i++) {
			getSetPanel(i).setResultToPanel(qmd);
		}
		//数据字典
		((SetHandSqlPanel) getSetPanel(SetHandSqlPanel.TAB_TITLE)).initTree();

		//数据加工
		SetProcessCodeUI pn = ((SetProcessCodeUI) getSetPanel(SetProcessCodeUI.TAB_TITLE));
		pn.init();

		//元数据初始化处理
		initMetaDataHash(qmd.getMetadatas());
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getMetaDatas()
	 */
	public MetaDataVO[] getMetaDatas() {
		//预执行
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
			//填充元数据
			if (hashFldDim.containsKey(fldAlias)) {
				Object[] objs = (Object[]) hashFldDim.get(fldAlias);
				//维度标志
				boolean bDimflag = ((Boolean) objs[0]).booleanValue();
				mds[i].setDimflag(bDimflag);
				//维度主键
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

		//环境参数处理
		try {
			QueryModelDef qmd = getQueryModelDef().getBaseModel();
			//获得参数
			ParamVO[] params = qmd.getParamVOs();
			//处理替换型参数
			Hashtable hashParam = BIModelUtil.makeHashForReplaceParam(params);
			//加环境信息
			hashParam = BIModelUtil.addEnvInfo(hashParam);
			//添加自定义环境参数
			//			IEnvParam iEnvParam = null; //BI
			//			Object[] objEnvParams = new Object[] { iEnvParam,
			// qmd.getDsName(),
			//					new EnvInfo(true) };
			//			hashParam.put(QueryConst.ENV_PARAM_KEY, objEnvParams);
			//获得SQL
			handSql = BIModelUtil.replaceHandSql(handSql, hashParam);
		} catch (Exception e) {
			AppDebug.debug(e);
		}

		//预执行解析
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