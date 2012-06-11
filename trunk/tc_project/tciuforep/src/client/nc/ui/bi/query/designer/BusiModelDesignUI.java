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
import nc.vo.pub.querymodel.QueryBaseVO;

import com.ufida.iufo.pub.tools.AppDebug;

/**
 * 第一类查询（业务视图）设计界面 创建日期：(2005-5-13 16:55:14)
 * 
 * @author：朱俊彬
 */
public class BusiModelDesignUI extends AbstractQueryDesignTabPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//常量
	public final static int TAB_TABLEJOIN = 0;

	public final static int TAB_COLUMN = 1;

	public final static int TAB_COND = 2;

	public final static int TAB_ORDERBY = 3;

	public final static int TAB_METADATA = 4;

	/**
	 * BusiModelDesignUI 构造子注解。
	 */
	public BusiModelDesignUI() {
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
		//同步字段显示名和数据类型
		procMetaDispname();

		//填充手工SQL信息（有风险,影响getSqlByBaseDef）
		QueryBaseVO qb = qmd.getQueryBaseVO();
		if (qb != null) {
			qb.setHandSql(null);
		}
		//		try {
		//			String sql = BIModelUtil.getSqlByBaseDef(qmd.getQueryBaseDef(),
		//					null);
		//			qmd.getQueryBaseVO().setHandSql(sql);
		//		} catch (Exception e) {
		//			AppDebug.debug(e);
		//		}
	}

	/**
	 * 获得设置面板数组 创建日期：(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public AbstractQueryDesignSetPanel[] getSetPanels() {
		AbstractQueryDesignSetPanel pn1 = new SetTableJoinPanel();
		AbstractQueryDesignSetPanel pn2 = new SetColumnPanel();
		AbstractQueryDesignSetPanel pn3 = new SetCondPanel();
		AbstractQueryDesignSetPanel pn4 = new SetOrderbyPanel();
		//AbstractQueryDesignSetPanel pn5 = new SetProcessCodeUI();
		m_sps = new AbstractQueryDesignSetPanel[] { pn1, pn2, pn3, pn4 };
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

		//查询字段界面初始化
		((SetColumnPanel) getSetPanel(TAB_COLUMN)).initUI();

		//设置数据表页签数据字典
		//((SetTablePanel)
		// getSetPanel(SetTablePanel.TAB_TITLE)).setDatadict(getDatadict());
		//个别初始化
		//((SetJoinPanel)
		// getSetPanel(SetJoinPanel.TAB_TITLE)).initEditorValue();
		((SetCondPanel) getSetPanel(SetCondPanel.TAB_TITLE)).initEditorValue();

		//数据加工
		//SetProcessCodeUI pn = ((SetProcessCodeUI)
		// getSetPanel(SetProcessCodeUI.TAB_TITLE));
		//pn.init();

		//元数据初始化处理
		initMetaDataHash(qmd.getMetadatas());
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getMetaDatas()
	 */
	public MetaDataVO[] getMetaDatas() {
		//更新
		refreshQbd();
		//预执行
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
		//return SetMetaDataPanel.appendAttrDims(mds);
		return mds;
	}

	/*
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignTabPanel#getMetaDatas()
	 */
	private void changeFlds(QueryBaseDef qbd) {
		try {
			String dsNameForDef = getDefDsName();
			SelectFldVO[] sfs = qbd.getSelectFlds();
			if( sfs != null && sfs.length >0){
				//获得列结构信息
				ResultSetMetaData rsmd = QueryModelBO_Client.getFldType(qbd, null,
						dsNameForDef);
				int iColCount = rsmd.getColumnCount();
				//获得每列的数据类型
				Integer[] iColTypes = new Integer[iColCount];
				int[] iPrecision = new int[iColCount];
				int[] iScale = new int[iColCount];
				for (int i = 0; i < iColCount; i++) {
					iColTypes[i] = new Integer(rsmd.getColumnType(i + 1));
					iPrecision[i] = rsmd.getPrecision(i + 1);
					iScale[i] = rsmd.getScale(i + 1);
					//处理运算型查询字段（因为METADATA无法得到正确精度）
					if (iColTypes[i].intValue() == Types.NUMERIC
							&& iPrecision[i] == 0 && iScale[i] == 0) {
						iPrecision[i] = 38;
						iScale[i] = 8;
					}
				}
	
				
				int iLen = sfs.length;
				if (iColTypes.length != iLen) {
					MessageDialog.showWarningDlg(this,
							NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-000099")/* @res "查询引擎" */,
							NCLangRes.getInstance().getStrByID("10241201",
									"UPP10241201-000831")/* @res "不可能！" */);
					return;
				}
				String strHint = nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"10241201", "UPP10241201-000832")/* @res "语法校验通过！" */;
				for (int i = 0; i < iLen; i++) {
					sfs[i].setColtype(iColTypes[i]);
					sfs[i].setPrecision(iPrecision[i]);
					sfs[i].setScale(iScale[i]);
					String strType = BIModelUtil.isNumberType(iColTypes[i]
							.intValue()) ? NCLangRes.getInstance().getStrByID(
							"10241201", "UPP10241201-000833")/*
															  * @res "数值型"
															  */
					: NCLangRes.getInstance().getStrByID("10241201",
							"UPP10241201-000328")/* @res "字符型" */;
					strHint += "\n        " + sfs[i].getFldname() + " -- "
							+ strType + ":" + iColTypes[i].intValue();
				}
				AppDebug.debug(strHint);//@devTools System.out.println(strHint);
			}
		} catch (Exception e) {
			AppDebug.debug(e);
			String		strErrMsg =getSQLExecErrMsg(e);
			MessageDialog.showWarningDlg(this, "UFBI", strErrMsg);
		}
	}

	/**
	 * 更新QBD
	 */
	public void refreshQbd() {
		((SetTableJoinPanel) getSetPanel(SetTableJoinPanel.TAB_TITLE))
				.refreshQbd();
		((SetColumnPanel) getSetPanel(SetColumnPanel.TAB_TITLE)).refreshQbd();
		((SetCondPanel) getSetPanel(SetCondPanel.TAB_TITLE)).refreshQbd();
		((SetOrderbyPanel) getSetPanel(SetOrderbyPanel.TAB_TITLE)).refreshQbd();
	}

	/**
	 * 显示信息
	 */
	public String getShowInfo() {
		//更新
		refreshQbd();
		//获得SQL
		String sql = BIModelUtil.getSqlByBaseDef(getQueryBaseDef(), null);
		return sql;
	}
	public String doCheck() {
		String	strErrMsg = super.doCheck();
		//进行SQL语句的预执行
		if( strErrMsg == null ){
			String 			dsNameForDef = getDefDsName();
			try{
				//获得列结构信息
				QueryModelBO_Client.getFldType(getQueryBaseDef(), null,
							dsNameForDef);
			}catch(Exception e){
				strErrMsg = getSQLExecErrMsg(e);
			}
		}
		return strErrMsg;
	}

}