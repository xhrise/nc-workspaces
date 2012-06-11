/*
 * 创建日期 2006-4-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.query.designer;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.DimMaterialDef;
import nc.vo.bi.query.manager.MaterialInfoVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class SetMaterPanel extends AbstractQueryDesignSetPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0111";//"物化定义";

	//常量
	public final static int COL_FLDNAME = 1;

	public final static int COL_FLDDIM = 2;

	public final static int COL_FLDLEVEL = 3;



	//常量
	public final static String FLD_NAME = "ubiquery0101";//"显示名";

	//常量
	public final static String FLD_DIMENSION = "ubiquery0107";//"维度";

	public final static String FLD_LEVEL = "ubiquery0112";//"层次";

	//公用维度定义
//	private DimensionVO[] m_dims = null;

	private UITablePane ivjTablePn = null;
	
	//	private boolean		m_bMaterEnabled = false;
	private Hashtable<String, Object>		m_hashMater = new Hashtable<String, Object>();
	
	private boolean		m_bHashMaterInited = false;

	/**
	 *  
	 */
	public SetMaterPanel() {
		super();
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
		Object[] objsAttr = new Integer[DimRescource.INT_MAX_FLDPRE_NUMBER +1];
		objsAttr[0] = null;
		for( int i=1; i<objsAttr.length; i++ ){
			objsAttr[i] = new Integer(i);
		}
		//列属性列编辑器
		JComboBox cbbAttr = new UIComboBox(objsAttr);
		TableColumn tc = getTable().getColumnModel().getColumn(COL_FLDLEVEL);
		tc.setCellEditor(new UIRefCellEditor(cbbAttr));

	}

	/**
	 * 初始化类。
	 */
	/* 警告：此方法将重新生成。 */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetMaterPanel");
			setSize(480, 320);
			//add(getCheckBox(), "North");
			add(getTablePn(), "Center");
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
		DefaultTableModel tm = new DefaultTableModel(new Object[] { "",
				StringResource.getStringResource(FLD_NAME), 
				StringResource.getStringResource(FLD_DIMENSION), 
				StringResource.getStringResource(FLD_LEVEL) }, 0) {
			public int getColumnCount() {
				return 4;
			}

			public boolean isCellEditable(int row, int col) {
				return (col == COL_FLDLEVEL);
			}
		};
		getTable().setModel(tm);
		//设置表属性
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 160, 160, 160 });
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
		//终止编辑态
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		
		MaterialInfoVO		materInfoVO = null;
		
		int 		iRowCount = getTable().getRowCount();
		ArrayList<DimMaterialDef>	aryDefs = new ArrayList<DimMaterialDef>();
		for (int i = 0; i < iRowCount; i++) {
			//层次
			Integer		nLevel = (Integer)getTM().getValueAt(i, COL_FLDLEVEL);
			if( nLevel!= null ){
				//维度PK，
				DimensionVO dimVO = (DimensionVO) getTM().getValueAt(i, COL_FLDDIM);
				aryDefs.add( new DimMaterialDef(dimVO.getDimID(), nLevel.intValue()) );
			}
		}
		if( aryDefs.size() >0 ){
			DimMaterialDef[]	defs = new DimMaterialDef[aryDefs.size()];
			aryDefs.toArray(defs);
			materInfoVO = new MaterialInfoVO(defs);
		}
		//回设
		qmd.getAdvModel().setMaterialDef(materInfoVO);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {

		if( m_bHashMaterInited == false){
			//根据qmd中的信息设置hashMater
			MaterialInfoVO	 materInfoVO  = qmd.getMaterialDef();
			if( materInfoVO != null ){
				DimMaterialDef[]	defs = materInfoVO.getMaterialDefs();
				for( int i=0; i<defs.length; i++ ){
					if( defs[i] != null ){
						m_hashMater.put(defs[i].getDimPK(), new Integer(defs[i].getMaterialLevel()));
					}
				}
			}			
			m_bHashMaterInited = true;
		}else{
			m_hashMater = getMaterInfoHash();
		}
		//获得模型中的所有关联的公共维度
		MetaDataVO[]	mdataVOs = qmd.getMetadatas();
//		m_bMaterEnabled = true;
		if( mdataVOs != null ){
			ArrayList<MetaDataVO>	aryList = new ArrayList<MetaDataVO>(); 
			ArrayList<DimensionVO>	aryDims = new ArrayList<DimensionVO>();
			for( int i=0; i<mdataVOs.length; i++ ){
				if( mdataVOs[i].getPk_dimdef()!= null && mdataVOs[i].getPk_dimdef().length() >0){
					DimensionVO		dimVO = getDimVO(mdataVOs[i]);
					if( dimVO != null ){
						aryList.add(mdataVOs[i]);
						aryDims.add(dimVO);
					}
				}
			}
			
			getTM().setNumRows(aryList.size());
			for( int i=0; i<aryList.size(); i++ ){
				//设置模型
				MetaDataVO	mDataVO = (MetaDataVO)aryList.get(i);
				getTM().setValueAt(mDataVO.getFldalias(), i, COL_FLDNAME);
				DimensionVO		dimVO = (DimensionVO)aryDims.get(i);
				getTM().setValueAt(dimVO, i, COL_FLDDIM);
//				if( m_hashMater.get(mDataVO.getPk_dimdef()) != null ){
					getTM().setValueAt( m_hashMater.get(mDataVO.getPk_dimdef()), i, COL_FLDLEVEL);

			}
			

		}
		//m_checkBoxMater.set
	}

	public Hashtable<String, Object>		getMaterInfoHash(){
		int iRowCount = getTable().getRowCount();
		for (int i = 0; i < iRowCount; i++){
			DimensionVO		dimVO = (DimensionVO)getTM().getValueAt(i, COL_FLDDIM);
			if(getTM().getValueAt(i, COL_FLDLEVEL) != null){
				m_hashMater.put(dimVO.getDimID(), getTM()
					.getValueAt(i, COL_FLDLEVEL));
			}else{
				m_hashMater.remove(dimVO.getDimID());
			}
		}
		return m_hashMater;
	}
	
	private DimensionVO  	getDimVO(MetaDataVO mDataVO){
		Hashtable hashFldDims = getTabPn().getMetaDataPanel().getHashFldDim();
		if( hashFldDims != null ){
			Object[]	objs = (Object[])hashFldDims.get(mDataVO.getFldalias());
			if( objs != null ){
				return (DimensionVO)objs[1];
			}
		}
		return null;
	}
	/**
	 * 合法性检查 创建日期：(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {

		return null;
	}
}
 