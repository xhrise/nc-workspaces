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
 * 查询设计设置页签 创建日期：(2005-5-13 16:45:23)
 * 
 * @author：朱俊彬
 */
public abstract class AbstractQueryDesignTabPanel extends UITabbedPane
		implements ChangeListener {

	//查询对象定义
	private BIQueryModelDef m_qmd = null;

	//查询基本定义
	private QueryBaseDef m_qbd = null;

	//设置面板数组
	protected AbstractQueryDesignSetPanel[] m_sps = null;

	//定义数据源
	private String m_defDsName = null;

	//数据字典实例
	private Datadict m_datadict = null;

	//查询设计面板实例
	private QueryDesignPanel m_designPanel = null;

	//上次选中页签序号
	private int m_iLastSelTabIndex = 0;

	/**
	 * AbstractQueryDesignTabPanel 构造子注解。
	 */
	public AbstractQueryDesignTabPanel() {
		super();
		initialize();
	}

	/**
	 * AbstractQueryDesignTabPanel 构造子注解。
	 * 
	 * @param p0
	 *            int
	 */
	public AbstractQueryDesignTabPanel(int p0) {
		super(p0);
	}

	/**
	 * 获得设置面板数组 创建日期：(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public abstract AbstractQueryDesignSetPanel[] getSetPanels();

	/**
	 * 根据序号获得某个设置面板 创建日期：(2005-5-13 16:48:29)
	 */
	public AbstractQueryDesignSetPanel getSetPanel(int iIndex) {
		return m_sps[iIndex];
	}

	/**
	 * 根据标题ID获得某个设置面板 创建日期：(2005-5-13 16:48:29)
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
	 * 每当部件抛出异常时被调用
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* 除去下列各行的注释，以将未捕捉到的异常打印至 stdout。 */
		AppDebug.debug("--------- 未捕捉到的异常 ---------");//@devTools System.out.println("--------- 未捕捉到的异常 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * 初始化 创建日期：(2005-5-13 16:49:44)
	 */
	public void init() {
		AbstractQueryDesignSetPanel[] pns = getSetPanels();
		int iLen = (pns == null) ? 0 : pns.length;
		//去除监听
		removeChangeListener(this);
		//插入页签
		for (int i = 0; i < iLen; i++) {
			insertTab(StringResource.getStringResource(pns[i].getPanelTitle()), null, pns[i], null, i);
			pns[i].setTabPn(this);
		}


		//加入元数据设置界面
		AbstractQueryDesignSetPanel pnMetaData = new SetMetaDataPanel(isMetaDataEditable());
		//加入物化定义设置界面
		AbstractQueryDesignSetPanel pnMaterSet = new SetMaterPanel();
		

		//修正m_sps
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
		//添加监听
		addChangeListener(this);
		
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
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
	 * 设置查询对象 创建日期：(2005-5-16 18:38:58)
	 */
	public void setQueryModelDef(BIQueryModelDef qmd) {
		m_qmd = qmd;
		m_qbd = qmd.getBaseModel().getQueryBaseDef();
		m_qbd.setDsName(qmd.getDsName());
		//初始化设置面板
		setResultToPanels();
	}

	/**
	 * 设置查询基本定义 创建日期：(2005-5-16 18:38:58)
	 */
	public void setQueryBaseDef(QueryBaseDef qbd) {
		m_qbd = qbd;
	}

	/**
	 * 从设置面板获得结果 创建日期：(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public abstract void getResultFromPanels(BIQueryModelDef qmd);

	/**
	 * 初始化设置面板 创建日期：(2005-5-13 16:48:29)
	 * 
	 * @return nc.ui.bi.query.designer.AbstractQueryDesignSetPanel[]
	 */
	public abstract void setResultToPanels();

	/**
	 * 获得查询对象
	 */
	protected BIQueryModelDef getQueryModelDef() {
		return m_qmd;
	}

	/**
	 * 获得元数据VO，用于设置元数据页签
	 */
	public abstract MetaDataVO[] getMetaDatas();

	/**
	 * @return 返回 datadict。
	 */
	public Datadict getDatadict() {
		return m_datadict;
	}

	/**
	 * @param datadict
	 *            要设置的 datadict。
	 */
	public void setDatadict(Datadict datadict) {
		m_datadict = datadict;
	}

	/**
	 * @return 返回 String。
	 */
	public String getDefDsName() {
		return m_defDsName;
	}

	/**
	 * @return 返回 String。
	 */
	public QueryBaseDef getQueryBaseDef() {
		return m_qbd;
	}

	/**
	 * 保存结果
	 */
	public void saveResult() {
		String dsNameForDef = getDefDsName();
		//获得结果
		getResultFromPanels(m_qmd);
		try {
			//转换为XML
			//String definition = BIQueryDefToXmlUtil.saveQueryDefToXml(m_qmd);
			//执行更新
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
		//更新字段-元数据哈希表
		int iTabCount = getTabCount();
		int iSelIndex = getSelectedIndex();
		if (m_iLastSelTabIndex == iTabCount - 2) {

			//刷新字段-元数据哈希表
			getMetaDataPanel().refreshHashFldDim();
		} else if (getSetPanel(iSelIndex).getPanelTitle().equals(
				SetMetaDataPanel.TAB_TITLE) ) {

			//从子类获得元数据
			MetaDataVO[] mds = getMetaDatas();
			//设置元数据
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
			//从子类获得元数据
			MetaDataVO[] mds = getMetaDatas();
			//设置元数据
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
	 * 获得元数据设置面板
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
	 * 初始化别名-元数据哈希表
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
				//DimensionVO 是否存在
				DimensionVO dimVO = DimensionSrv.getByID(mds[i].getPk_dimdef());
				if( dimVO != null ){
					dimdef = dimVO;
				}
				//dimdef.setPrimaryKey(mds[i].getPk_dimdef());
			}
			//dimdef.setDimname(mds[i].getDimname()== null?"":mds[i].getDimname());
			//维护字段-元数据哈希表
			hashFldDim.put(mds[i].getFldalias(), new Object[] {
					new Boolean(bDimflag), dimdef });
		}
		getMetaDataPanel().setHashFldDim(hashFldDim);
	}

	/**
	 * @return 返回 designPanel。
	 */
	public QueryDesignPanel getDesignPanel() {
		return m_designPanel;
	}

	/**
	 * @param designPanel
	 *            要设置的 designPanel。
	 */
	public void setDesignPanel(QueryDesignPanel designPanel) {
		m_designPanel = designPanel;
	}

	/**
	 * 合法性校验
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
	 * 同步字段显示名和数据类型
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
	 * 根据查询对象创建查询向导
	 */
	public static AbstractQueryDesignTabPanel createTabPn(QueryModelVO qm) {
		//根据不同查询类型采用不同设计界面
		AbstractQueryDesignTabPanel tabPn = null;
		if (qm.getType().equals(BIQueryConst.TYPE_WIZARDSQL)) {
			tabPn = new WizardSqlDesignUI();
		} else if (qm.getType().equals(BIQueryConst.TYPE_FREESQL)) {
			tabPn = new FreeSqlDesignUI();
		} else if (qm.getType().equals(BIQueryConst.TYPE_BUSIFUNC)) {
			tabPn = new BusiFunctionDesignUI();
		} else if( qm.getType().equals(BIQueryConst.TYPE_INPUTMODEL)){//采集模型
			tabPn = new InputModelDesignUI();
		}else{
			tabPn = new BusiModelDesignUI();
		}
		return tabPn;
	}

	/**
	 * 设置定义数据源
	 */
	public void setDefDsName(String defDsName) {
		m_defDsName = defDsName;
	}

	/**
	 * 显示信息
	 */
	public String getShowInfo() {
		return "";
	}
	
	protected boolean isMetaDataEditable(){
		return false;
	}
	protected  String  getSQLExecErrMsg(Exception e){
		String strErrMsg = StringResource.getStringResource("mbiquery0119");//SQL 语法错误
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
	 * 切换页签,在此之前做页面检查
	 */
	public void setSelectedIndex(int index) {

		if( m_sps != null ){
			//得到上一个
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