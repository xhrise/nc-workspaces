/*
 * 创建日期 2005-5-25
 *
 */
package nc.ui.bi.query.designer;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 元数据设置界面
 */
public class SetMetaDataPanel extends AbstractQueryDesignSetPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0100";//"元数据";

	// 常量
	public final static int COL_FLDNAME = 1;

	public final static int COL_FLDALIAS = 2;

	public final static int COL_FLDTYPE = 3;
	
	public final static int COL_FLDPRECISION = 4;

	public final static int COL_FLDATTR = 5;

	public final static int COL_FLDDIM = 6;

	// 常量
	public final static String FLD_NAME = "ubiquery0101";//"显示名";

	public final static String FLD_ALIAS = "ubiquery0102";//"别名";

	public final static String FLD_TYPE = "ubiquery0103";//"数据类型";

	public final static String FLD_LENGTH = "ubiquery0104";//"长度";

	public final static String FLD_ATTR = "ubiquery0105";//"列属性";

	public final static String FLD_DIM = "ubiquery0106";//"公共维度";

	// 常量
	public final static String DIMENSION = "ubiquery0107";//"维度";

	public final static String MEASURE = "ubiquery0108";//"指标";
	
	

	// 公用维度定义
	private DimensionVO[] m_dims = null;

	// 字段与维度映射关系
	private Hashtable<String, Object[]> m_hashFldDim = new Hashtable<String, Object[]>();

	private UITablePane ivjTablePn = null;

	private boolean		m_bEditable = false;
	private UIButton    m_btnAdd = null;
	private UIButton    m_btnDelete = null;
	private UIPanel  	m_btnPnl = null;
	private UIPanel     m_btnAddPnl = null;
	private UIPanel     m_btnDelPnl = null;
	
	/**
	 * 
	 */
	public SetMetaDataPanel(boolean bEditable) {
		super();
		m_bEditable = bEditable;
		initialize();
	}

	/**
	 * 获得表格 创建日期：(2005-5-17 20:02:35)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		return getTablePn().getTable();
	}

	/**
	 * 返回 TablePn 特性值。
	 * 
	 * @return UITablePane
	 */
	/* 警告：此方法将重新生成。 */
	private UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new UITablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn;
	}
	
	private  UIPanel  getBtnPnl(){
		if( m_btnPnl == null ){
			try {
				GridLayout	layout = new GridLayout(2,1);
				m_btnPnl = new UIPanel(layout);
				m_btnPnl.setName("BtnPnl");
				m_btnPnl.setPreferredSize(new Dimension(100, 55));
				m_btnPnl.setMinimumSize(new Dimension(100, 55));
				// user code begin {1}
				m_btnPnl.add(getBtnAddPnl());
				m_btnPnl.add(getBtnDelPnl());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}			
		}
		return m_btnPnl;
	}
	private  UIPanel  getBtnAddPnl(){
		if( m_btnAddPnl == null ){
			try {
				m_btnAddPnl = new UIPanel();
				m_btnAddPnl.setLayout(new UIButtonLayout());
				m_btnAddPnl.setName("BtnAddPnl");
				// user code begin {1}
				m_btnAddPnl.add(getBtnAdd(),getBtnAdd().getName());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}			
		}
		return m_btnAddPnl;
	}
	private  UIPanel  getBtnDelPnl(){
		if( m_btnDelPnl == null ){
			try {
				m_btnDelPnl = new UIPanel();
				m_btnDelPnl.setLayout(new UIButtonLayout());
				m_btnDelPnl.setName("BtnDelPnl");
				// user code begin {1}
				m_btnDelPnl.add(getBtnDelete(),getBtnDelete().getName());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}			
		}
		return m_btnDelPnl;
	}

	private  UIButton  getBtnAdd(){
		if( m_btnAdd == null ){
			try {
				m_btnAdd = new UIButton(StringResource.getStringResource("ubiquery0109"));//"增加");
				m_btnAdd.addActionListener(this);
				m_btnAdd.setName("btnAdd");
				m_btnAdd.setPreferredSize(new Dimension(70, 22));
				m_btnAdd.setMinimumSize(new Dimension(88, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}						
		}
		return m_btnAdd;
	}
	private  UIButton  getBtnDelete(){
		if( m_btnDelete == null ){
			try {
				m_btnDelete = new UIButton(StringResource.getStringResource("ubiquery0110"));//"删除"
				m_btnDelete.setName("btnDel");
				m_btnDelete.addActionListener(this);
				m_btnDelete.setPreferredSize(new Dimension(70, 22));
				m_btnDelete.setMinimumSize(new Dimension(88, 22));

				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}						
		}
		return m_btnDelete;
	}
	/**
	 * 获得表格模型 创建日期：(2005-5-17 20:03:25)
	 * 
	 * @return javax.swing.table.DefaultTableModel
	 */
	private DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
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
	 * 设置列编辑器 创建日期：(2003-9-17 14:50:41)
	 */
	public void initEditorValue() {
		TableColumnModel	tcModel = getTable().getColumnModel();
		Object[] objsAttr = new Object[] { 
								StringResource.getStringResource(DIMENSION), 
								StringResource.getStringResource(MEASURE) };
		// 列属性列编辑器
		JComboBox cbbAttr = new UIComboBox(objsAttr);
		TableColumn tc = tcModel.getColumn(COL_FLDATTR);
		tc.setCellEditor(new UIRefCellEditor(cbbAttr));

		// 获得公用维度
		DimensionVO[] dims = getDimDef();
		// 增加空项
		int iLen = (dims == null) ? 0 : dims.length;
		DimensionVO[] temps = new DimensionVO[iLen + 1];
		for (int i = 0; i < iLen; i++) {
			temps[i + 1] = dims[i];
		}
		temps[0] = getEmptyDimDef();
		dims = temps;
		// 公用维度列编辑器
		JComboBox cbbDim = new UIComboBox(dims);
		tc = tcModel.getColumn(COL_FLDDIM);
		tc.setCellEditor(new UIRefCellEditor(cbbDim));
		
		//数据类型，数值和字符，精度
		Object[]	objTypes = new Object[]{"VARCHAR","DECIMAL"};
		JComboBox  ccbType = new UIComboBox(objTypes);
		tc = tcModel.getColumn(COL_FLDTYPE);
		tc.setCellEditor(new UIRefCellEditor(ccbType));
	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetMetaDataPanel");
			setSize(480, 320);
			add(getTablePn(), "Center");
			if( m_bEditable ){
				add(getBtnPnl(), "East");
			}
			//setMetaDataAddable(false);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initTable();
		setTableCell();
		initEditorValue();
		// user code end
	}

	/**
	 * 初始化表格。 创建日期：(2005-5-17 20:04:22)
	 */
	@SuppressWarnings("serial")
	private void initTable() {
		DefaultTableModel tm = new DefaultTableModel(
								new Object[] { "", 
										StringResource.getStringResource(FLD_NAME), 
										StringResource.getStringResource(FLD_ALIAS), 
										StringResource.getStringResource(FLD_TYPE), 
										StringResource.getStringResource(FLD_LENGTH), 
										StringResource.getStringResource(FLD_ATTR),
										StringResource.getStringResource(FLD_DIM) }, 0) {
			public int getColumnCount() {
				return 7;
			}

			public boolean isCellEditable(int row, int col) {
				if( m_bEditable ){
					return (col != 0);
				}else{
					return (col == COL_FLDNAME || col == COL_FLDATTR || col == COL_FLDDIM );
				}
			}
		};
		getTable().setModel(tm);
		// 设置表属性
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 150, 150, 70, 70, 70,150 });
	}

	/**
	 * 设置居中对齐 创建日期：(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		DefaultTableCellRenderer renderer = null;
		for (int i = 0; i < getTable().getColumnCount(); i++) {
			TableColumn tc = getTable().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNoCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		// 终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();

		int iRowCount = getTable().getRowCount();
		MetaDataVO[] mds = new MetaDataVO[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			// 数据类型
			String colType = getTM().getValueAt(i, COL_FLDTYPE).toString();
			int iColType = Types.VARCHAR;
			if (!colType.trim().equals(""))
				iColType = BIModelUtil.convDB2DataType(colType);
			// 列属性
			boolean bDimflag = true;
			String colAttr = getTM().getValueAt(i, COL_FLDATTR).toString();
			if (colAttr.equals(StringResource.getStringResource(MEASURE)))
				bDimflag = false;
			// 公用维度
			DimensionVO dimdef = (DimensionVO) getTM().getValueAt(i, COL_FLDDIM);
			//
			mds[i] = new MetaDataVO();
			mds[i].setFldname(getTM().getValueAt(i, COL_FLDNAME).toString());
			mds[i].setFldalias(getTM().getValueAt(i, COL_FLDALIAS).toString());
			mds[i].setColtype(new Integer(iColType));
			mds[i].setDimflag(bDimflag);
			if( dimdef != null ){
				mds[i].setPk_dimdef(dimdef.getPrimaryKey());
				mds[i].setDimname(dimdef.getDimname());
			}else{
				mds[i].setPk_dimdef(null);
				mds[i].setDimname(null);
			}

			// 分开
//			mds[i].setPrecision(m_metaDataVOs[i].getPrecision());
//			mds[i].setScale(m_metaDataVOs[i].getScale());
			//精度由长度和精度组成
			String			strPrecision = getTM().getValueAt(i, COL_FLDPRECISION).toString();
			StringTokenizer	tokener = new StringTokenizer(strPrecision,",");
			int				nPrecision = 0;
			int				nScale = 0;
			nPrecision = Integer.parseInt(tokener.nextToken());
			if( tokener.hasMoreTokens() ){
				nScale = Integer.parseInt(tokener.nextToken());
			}
			mds[i].setPrecision(nPrecision);
			mds[i].setScale(nScale);

		}
		// 追加相关属性纬度 TODO
//		mds = appendAttrDims(mds);
		// 回设
		qmd.setMetadatas(mds);
	}

	/**
	 * 为元数据自动添加属性纬度
	 * 
	 * @param mds
	 * @param iColType
	 * @return
	 */
	public static MetaDataVO[] appendAttrDims(MetaDataVO[] mds) {
		if (mds == null || mds.length == 0)
			return mds;
		ArrayList<String> al_allDims = new ArrayList<String>();
		ArrayList<MetaDataVO> al_exMetaVO = new ArrayList<MetaDataVO>();
		int realMdsIndex = 0;
		for (int i = 0; i < mds.length; i++) {
			if (mds[i].getPk_mainDimdef() != null && mds[i].getPk_mainDimdef().length() > 0) {
				if (realMdsIndex == 0)// 记录第一个自动添加的属性纬度的位置
					realMdsIndex = i;
			} else if (mds[i].getPk_dimdef() != null && mds[i].getPk_dimdef().length() > 0) {
				al_allDims.add(mds[i].getPk_dimdef());
			}
		}
		MetaDataVO[] realMds = null;// 去除所有自定添加的属性纬度，重新进行处理
		if (realMdsIndex == 0) {
			realMds = mds;
		} else {
			realMds = new MetaDataVO[realMdsIndex];
			System.arraycopy(mds, 0, realMds, 0, realMdsIndex);
		}

		int dimCount = al_allDims.size();
		if (dimCount == 0)// 如果没有纬度，直接返回
			return realMds;

		for (int i = 0; i < realMds.length; i++) {
			if(al_allDims.contains(realMds[i].getPk_dimdef())){
			try {
				String[] attrDimIDs = DimensionSrv.getInstance().getPropDimIDs((String) al_allDims.get(i));
				if (attrDimIDs != null && attrDimIDs.length > 0) {
					for (int j = 0; j < attrDimIDs.length; j++) {
						if (!al_allDims.contains(attrDimIDs[j])) {
							DimensionVO attrDimVO = DimensionSrv.getInstance().getDimByID(attrDimIDs[j]);
							MetaDataVO meta = new MetaDataVO(attrDimVO.getDimcode(), attrDimVO.getDimname(),
									Types.VARCHAR);
							meta.setPk_dimdef(attrDimIDs[j]);
							meta.setDimname(attrDimVO.getDimname());
							meta.setPk_mainDimdef(realMds[i].getPk_dimdef());
							meta.setMainFldName(realMds[i].getFldalias());

							al_exMetaVO.add(meta);
							al_allDims.add(attrDimIDs[j]);
						}
					}
				}
			} catch (UISrvException ex) {
			} catch (Exception ex) {
			}
			}
		}
		if (al_exMetaVO.size() > 0) {
			MetaDataVO[] newMds = new MetaDataVO[realMds.length + al_exMetaVO.size()];
			System.arraycopy(realMds, 0, newMds, 0, realMds.length);
			for (int i = 0; i < al_exMetaVO.size(); i++) {
				newMds[realMds.length + i] = (MetaDataVO) al_exMetaVO.get(i);
			}
			return newMds;
		} else {
			return realMds;
		}
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {

		getTM().setNumRows(0);
		// 获得表定义
		MetaDataVO[] mds = qmd.getMetadatas();
		int iLen = (mds == null) ? 0 : mds.length;
		for (int i = 0; i < iLen; i++) {
			String fldName = mds[i].getFldname();
			String fldAlias = mds[i].getFldalias();
			// 数据类型
			int 	iFldType = (mds[i].getColtype() == null) ? Types.VARCHAR : mds[i].getColtype().intValue();
			String 	fldType = BIModelUtil.convDataType2DB(iFldType);
			String	strPrecision = null;
			if( mds[i].getScale() >0 ){
				strPrecision = mds[i].getPrecision()+","+mds[i].getScale();
			}else{
				strPrecision = ""+mds[i].getPrecision();
			}

			// 设置维度信息
			String fldAttrID = null;
			DimensionVO dimdef = null;
			if (m_hashFldDim.containsKey(fldAlias)) {
				Object[] objs = (Object[]) m_hashFldDim.get(fldAlias);
				boolean bDimflag = ((Boolean) objs[0]).booleanValue();
				fldAttrID = bDimflag ? DIMENSION: MEASURE;
				dimdef = (DimensionVO) objs[1];
			} else {
				fldAttrID = DIMENSION;
				dimdef = getEmptyDimDef();
			}
			String fldAttr = StringResource.getStringResource(fldAttrID);
			//
			Object[] objs = new Object[] { "", fldName, fldAlias, fldType, strPrecision,fldAttr, dimdef };
			getTM().addRow(objs);
		}
	}

	/**
	 * @return 返回 DimDefVO[]。
	 */
	public DimensionVO[] getDimDef() {
		if (m_dims == null)
			try {
				m_dims = DimensionSrv.getInstance().getAllDimVOs();
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		return m_dims;
	}

	/**
	 * @return 返回 DimDefVO[]。
	 */
	public DimensionVO getEmptyDimDef() {
		DimensionVO dimdef = new DimensionVO();
		dimdef.setPrimaryKey("");
		dimdef.setDimname("");
		return dimdef;
	}

	/**
	 * @return 返回 hashFldDim。
	 */
	public Hashtable getHashFldDim() {
		return m_hashFldDim;
	}

	/**
	 * @param hashFldDim
	 *            要设置的 hashFldDim。
	 */
	public void setHashFldDim(Hashtable<String, Object[]> hashFldDim) {
		m_hashFldDim = hashFldDim;
	}

	/**
	 * @param hashFldDim
	 *            要设置的 hashFldDim。
	 */
	public void refreshHashFldDim() {
		m_hashFldDim.clear();
		int iLen = getTable().getRowCount();
		for (int i = 0; i < iLen; i++) {
			// 列别名
			String fldAlias = getTM().getValueAt(i, COL_FLDALIAS).toString();
			// 列属性
			boolean bDimflag = true;
			String colAttr = getTM().getValueAt(i, COL_FLDATTR).toString();
			if (colAttr.equals(StringResource.getStringResource(MEASURE)) )
				bDimflag = false;
			// 公用维度
			DimensionVO dimdef = (DimensionVO) getTM().getValueAt(i, COL_FLDDIM);
			// 维护字段-维度哈希表
			m_hashFldDim.put(fldAlias, new Object[] { new Boolean(bDimflag), dimdef });
		}
	}

	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		// 终止编辑态
		String  strErrMsg = checkOnSwitch();
		if( strErrMsg == null ){
			int iLen = getTM().getRowCount();
			if (iLen == 0)
				return StringResource.getStringResource("mbiquery0106");//"No metadata";
		}
		return strErrMsg;
	}

	public String checkOnSwitch(){
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();

		int iLen = getTM().getRowCount();
		for (int i = 0; i < iLen; i++) {	
			// 显示名检查
			String fldname = getTM().getValueAt(i, COL_FLDNAME).toString();
			if (fldname.trim().equals(""))
				return StringResource.getStringResource("mbiquery0107",
						new String[]{Integer.toString(i+1)});//"Display name can't be empty";
			// 别名检查
			String fldalias = getTM().getValueAt(i, COL_FLDALIAS).toString();
			if (fldalias.trim().equals(""))
				return StringResource.getStringResource("mbiquery0108",
						new String[]{Integer.toString(i+1)});//"Alias name can't be empty";
			
			//判断是否有中文
			if( fldalias.length() <fldalias.getBytes().length){
				return StringResource.getStringResource("mbiquery0126",
						new String[]{Integer.toString(i+1)});//别名不能包含中文
			}
			
			//检查维度是否有重复
			String	strDimPK = getDimPK(i);
			if( strDimPK != null && strDimPK.length()>0 ){
				for( int j=i+1; j<iLen; j++ ){
					if( strDimPK.equals(getDimPK(j))){
						return StringResource.getStringResource("mbiquery0133",
							new String[]{Integer.toString(i+1), Integer.toString(j+1)});
					}
				}
			}
			if( m_bEditable ){
				boolean  bDim = true;
				if( getTM().getValueAt(i, COL_FLDATTR).toString().equals(StringResource.getStringResource(MEASURE)) ){
					bDim = false;
				}
				//名称和别名不能重复
				for( int j=i+1; j<iLen; j++ ){
					if( fldalias.equals(getTM().getValueAt(j, COL_FLDALIAS).toString())){
						return StringResource.getStringResource("mbiquery0134",
								new String[]{Integer.toString(i+1), Integer.toString(j+1)});
					}
				}
				//检查类型不能为空，
				String	strType = getTM().getValueAt(i, COL_FLDTYPE).toString();
				if( strType.trim().equals("")){
					return StringResource.getStringResource("mbiquery0100", 
							new String[]{Integer.toString(i+1)});//"type can't be empty";
				}else if( bDim && strType.equalsIgnoreCase("VARCHAR") == false){
					return StringResource.getStringResource("mbiquery0101", 
							new String[]{Integer.toString(i+1)});//"attr and type conflict";
				}else if( bDim == false && strType.equalsIgnoreCase("VARCHAR")){
					return StringResource.getStringResource("mbiquery0101", 
							new String[]{Integer.toString(i+1)});
				}
				//检查长度不能为空,长度为整数
				String	strPrecision = getTM().getValueAt(i, COL_FLDPRECISION).toString();
				if( strPrecision.trim().equals("")){
					return StringResource.getStringResource("mbiquery0102", 
							new String[]{Integer.toString(i+1)});//"precision can't be empty";
				}else{
					try{
						StringTokenizer	tokener = new StringTokenizer(strPrecision,",");
						if( Integer.parseInt(tokener.nextToken()) <0 ){
							return StringResource.getStringResource("mbiquery0131",
									new String[]{Integer.toString(i+1)});
						}
						if( tokener.hasMoreTokens() ){
							if( Integer.parseInt(tokener.nextToken())<0 ){
								return StringResource.getStringResource("mbiquery0132",
										new String[]{Integer.toString(i+1)});
							}
						}
					}catch(NumberFormatException e){
						return StringResource.getStringResource("mbiquery0103", 
								new String[]{Integer.toString(i+1)});//"precision format error";
					}
				}

				//检查维度的选择不能为空
				if( bDim && strDimPK == null){
					return StringResource.getStringResource("mbiquery0104", 
							new String[]{Integer.toString(i+1)});//"dimension can't be empty";
				}else if( bDim == false && (strDimPK != null && strDimPK.length()>0)){
					return StringResource.getStringResource("mbiquery0105", 
							new String[]{Integer.toString(i+1)});//"you shouldn't select dimension";
				}
			}
			
		}
		return null;
	}
	/**
	 * 获得字段别名-显示名哈希表 创建日期：(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public Hashtable getHashAliasDisp() {
		Hashtable<Object, Object> hashAliasDisp = new Hashtable<Object, Object>();
		int iRowCount = getTable().getRowCount();
		for (int i = 0; i < iRowCount; i++)
			hashAliasDisp.put(getTM().getValueAt(i, COL_FLDALIAS), getTM().getValueAt(i, COL_FLDNAME));
		return hashAliasDisp;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnAdd()){
			// 终止编辑态
			if (getTable().getCellEditor() != null)
				getTable().getCellEditor().stopCellEditing();
			//类型必须设置
			Object[]	objs = new Object[]{"","","","", "",""};
			getTM().addRow(objs);
		}else if (e.getSource() == getBtnDelete()){
			//得到当前选中的行
			// 终止编辑态
			if (getTable().getCellEditor() != null)
				getTable().getCellEditor().stopCellEditing();
			int		nRow = getTable().getSelectedRow();
			if( nRow >=0 ){
				getTM().removeRow(nRow);
			}
		}
	};
	
	private String getDimPK(int nIndex){
		DimensionVO		dimVO = (DimensionVO)getTM().getValueAt(nIndex, COL_FLDDIM);
		if( dimVO != null ){
			return dimVO.getDimID();
		}
		return null;
	}
} 